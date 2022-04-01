package com.samyeonyiduk.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.samyeonyiduk.domain.posts.Posts;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import com.amazonaws.auth.BasicAWSCredentials;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@NoArgsConstructor
@Service
public class AmazonS3Service {

    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloudfront}")
    private String cloudFront;

    @Value("${s3}")
    private String s3;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    public String userProfile(MultipartFile image, Long id) throws IOException {
        String file = "user/" + id;
        s3Client.putObject(new PutObjectRequest(bucket, file, image.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return (s3 + "/" + file + "?" + LocalDateTime.now());
    }

    public int upload(List<MultipartFile> fileList, Long id, Posts post, int num) throws IOException {
        // 업로드 로직 : post{postID}file0 ~ .. num
        String folder = "post" + id.toString() + "/";
        ObjectMetadata objMeta = new ObjectMetadata();
        for (MultipartFile file : fileList) {
            byte[] bytes = IOUtils.toByteArray(file.getInputStream());
            objMeta.setContentLength(bytes.length);
            String n = String.valueOf(num);
            String fileName = "file" + n;
            s3Client.putObject(new PutObjectRequest(bucket, folder + fileName, file.getInputStream(), objMeta)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            num++;
        }
        return num - 1;
        // post.findKeyAndPatch("imageNum", num - 1);
//      AmazonS3Client.getObject() <-직접 s3접근 but 우리는 CloudFront를 통해 캐시된 데이터를 가져올것이다
    }

    public void copyObject(Long postId, List<String> fileList, int flag) {
        System.out.println(fileList.size());
        if (fileList.size() <= 0 || fileList == null)
            return; //기존 파일 다 삭제
        System.out.println(fileList.get(0));
        String origin;
        for (int i = 0; i < fileList.size(); i++) {
            if (flag == 1) // origin to temp
                origin = "post" + postId + "/file" + fileList.get(i);
            else //temp to origin
                origin = "post" + postId + "/file" + (i + 1);
            String temp = "temp/file" +  (i + 1);
            System.out.println(origin + " " + temp + " flag" + flag);
            if (flag == 1)
                s3Client.copyObject(bucket, origin, bucket, temp);
            else
                s3Client.copyObject(bucket, temp, bucket, origin);
        }
    }

    public int patch(List<MultipartFile> fileList, Long id, Posts post, List<String> oldFileList) throws IOException {
        int newFileSize = oldFileList.size() + 1; //기존 이후에 추가할 파일 index
        if (oldFileList.size() != post.getImageNum()) {
            copyObject(id, oldFileList, 1); //origin to temp
            for (int i = 1; i <= post.getImageNum(); i++) { // 기존 파일 삭제
                String fileName = "post" + id + "/" + "file" + i;
                s3Client.deleteObject(bucket, fileName);
            }
            copyObject(id, oldFileList, 2); // temp to origin
            if (oldFileList.size() > 0 || oldFileList != null) { //기존 파일 다 삭제한거 아니면
                for (int i = 1; i <= oldFileList.size(); i++) { // temp 파일 삭제
                    String fileName = "temp/file" + i;
                    s3Client.deleteObject(bucket, fileName);
                }
            }
        }
        if (oldFileList == null || oldFileList.size() == 0)
            newFileSize = 1;
        if (fileList != null) { // 새로 업로드한 파일 추가
            return (upload(fileList, id, post, newFileSize));
        } else if (fileList == null || fileList.size() == 0) {
            return oldFileList.size();
        }
        return -1;
    }
    public List<String> getImageList(Long id, int postNum) {

        List<String> imageList = new ArrayList<>();
        imageList.add(s3 + "/post" + id.toString() + "/file1?" + LocalDate.now());
        for (int i = 2; i <= postNum; i++) {
            String fileName = "post" + id.toString() + "/" + "file" + String.valueOf(i) + "?" + LocalDateTime.now();
            imageList.add(s3 + "/" + fileName);
        }
        return imageList;
    }
}



