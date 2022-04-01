package com.samyeonyiduk.service;

import com.samyeonyiduk.domain.carts.Carts;
import com.samyeonyiduk.domain.carts.CartsRepository;
import com.samyeonyiduk.domain.category.Category;
import com.samyeonyiduk.domain.category.CategoryRepository;
import com.samyeonyiduk.domain.posts.Posts;
import com.samyeonyiduk.domain.posts.PostsRepository;
import com.samyeonyiduk.domain.users.Users;
import com.samyeonyiduk.domain.users.UsersRepository;
import com.samyeonyiduk.web.dto.mainpage.MainPagePostResponseDto;
import com.samyeonyiduk.web.dto.mainpage.MainPageResponseDto;
import com.samyeonyiduk.web.dto.posts.PostsResponseDto;
import com.samyeonyiduk.web.dto.posts.PostsThumbnailResponseDto;
import com.samyeonyiduk.web.dto.posts.PostsThumbnailSubListResponseDto;
import com.samyeonyiduk.web.dto.posts.PostsUploadRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Exception.class)
public class PostsService {
    @Value("${cloudfront}")
    private String cloudFront;

    @Value("${s3}")
    private String s3;

    private final PostsRepository postsRepository;

    private final UsersRepository usersRepository;

    private final CategoryRepository categoryRepository;

    private final AmazonS3Service amazonS3Service;

    private final CartsRepository cartsRepository;

    public void handleExperience(int exp, Users user) {
        double currentExp = user.getExperience() + exp;
        if (currentExp >= 100) {
            user.findKeyAndPatch("level", 1);
            currentExp -= 100;
        }
        user.findKeyAndPatch("experience", currentExp);
    }

    public Posts save (PostsUploadRequestDto requestDto, List<MultipartFile> fileList) throws IOException {
        Users user = usersRepository.getById(requestDto.getUserId());
        Category category = categoryRepository.getById(requestDto.getCategoryId());
        Posts post = postsRepository.save(requestDto.toEntity(user, category));
        int imageNum = amazonS3Service.upload(fileList, post.getId(), post, 1);
        post.findKeyAndPatch("image", s3 + "/post" + post.getId() + "/file1");
        post.findKeyAndPatch("imageNum", imageNum);
        handleExperience(20, user); // 작성은 경험치 20
        return(post);
    }

    public void patchImage(Long id, List<MultipartFile> fileList, List<String> oldFileList) throws IOException {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                "해당 게시물이 없습니다. ID = " + id));
        int imageNum = amazonS3Service.patch(fileList, id, posts, oldFileList);
        if (imageNum > 0)
            posts.findKeyAndPatch("imageNum", imageNum);
    }

    public void patch(Long id, Map<String, Object> patchMap) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                "해당 게시물이 없습니다. ID = " + id));
        if (patchMap.containsKey("categoryId")) {
            Category category = categoryRepository.findById((Long)patchMap.get("categoryId")).orElseThrow(() -> new IllegalArgumentException(
                    "해당 카테고리가 없습니다. ID = " + patchMap.get("categoryId")));
            patchMap.remove("categoryId");
            patchMap.put("category", category);
            patchMap.remove("categoryId");
        }
        posts.patch(patchMap);
    }

    public PostsResponseDto findById (Long id, Long userId, int view) {
        Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                "해당 게시물이 없습니다. ID = " + id));
        if (view == 1)
            entity.findKeyAndPatch("view", 1);
        String subList = makeSubList(entity, userId);
        List<String> imageList = amazonS3Service.getImageList(id, entity.getImageNum());
        return new PostsResponseDto(entity, amazonS3Service.getImageList(id, entity.getImageNum()), subList);
    }

    public PostsThumbnailSubListResponseDto findByForeignKeyId (Long id, String type, Long userId) {
        List<Posts> postsList;
        String subList;
        if (type.equals("category")){
            if (id == 0)
                postsList =  postsRepository.findAll();
            else
                postsList = postsRepository.findByCategory_Id(id);
            subList = makeSubList(postsList, userId);
        }
        else if (type.equals("user")) {
            postsList = postsRepository.findByUser_Id(id);
            subList = makeSubList(postsList, id);
        }
        else {
            throw new IllegalArgumentException("해당 외래키가 없습니다. TYPE = " + type);
        }
        Collections.reverse(postsList);
        List<PostsThumbnailResponseDto> postsThumbnailResponseDtoList = new ArrayList<>();
        for (Posts post : postsList) {
            if (postExist(post.getStatus())) {
                postsThumbnailResponseDtoList.add(new PostsThumbnailResponseDto(post));
            }
        }
        return new PostsThumbnailSubListResponseDto(postsThumbnailResponseDtoList, subList);
    }

    public MainPageResponseDto findMainPage(Long userId) {
        List<Posts> subscribesTop10 = postsRepository.findTop10ByOrderBySubscribesDesc();
        List<MainPagePostResponseDto> subscribesList = new ArrayList<>();
        for (Posts post : subscribesTop10) {
            subscribesList.add(new MainPagePostResponseDto(post));
        }
        List<Posts> viewTop10 = postsRepository.findTop10ByOrderByCreatedAtDesc();
        List<MainPagePostResponseDto> viewList = new ArrayList<>();
        for (Posts post : viewTop10) {
            viewList.add(new MainPagePostResponseDto(post));
        }
        if (userId == 0) /* 로그인 안한상태 */{
            return new MainPageResponseDto(subscribesList, viewList, "");
        }
        String subList = "";
        List<Carts> cartsList = cartsRepository.findByUserId(userId);
        for (Carts carts : cartsList) {
            subList = subList + "/" + (carts.getPost().getId() + "/");
        }
        return new MainPageResponseDto(subscribesList, viewList, subList);
    }


    public PostsThumbnailSubListResponseDto createAllPostDtoListBySearch(String searchWord, Long userId) {
        List<Posts> findList = postsRepository.findByWordLike(searchWord);
        if (findList.isEmpty()) {
            findList = checkSearchWord(searchWord);
        }
        List<PostsThumbnailResponseDto> postList = new ArrayList<>();
        String subList = makeSubList(findList, userId);
        Collections.reverse(findList);
        for (Posts post : findList) {
            if (postExist(post.getStatus())) {
                postList.add(new PostsThumbnailResponseDto(post));
            }
        }
        return new PostsThumbnailSubListResponseDto(postList, subList);
    }

    public PostsThumbnailSubListResponseDto createPostDtoListCreatedAtDesc(Long categoryId, Long userId) {
        List<Posts> findList;
        List<PostsThumbnailResponseDto> postList = new ArrayList<>();
        if (categoryId == 0) {
            findList = postsRepository.findAllOrderByCreatedAtDesc();
        } else {
            findList = postsRepository.findByCategoryIdOrderByCreatedAtDesc(categoryId);
        }
        String subList = makeSubList(findList, userId);
        for (Posts post : findList) {
            if (postExist(post.getStatus())) {
                postList.add(new PostsThumbnailResponseDto(post));
            }
        }
        return new PostsThumbnailSubListResponseDto(postList, subList);
    }

    public PostsThumbnailSubListResponseDto createPostDtoListCreatedAtAsc(Long categoryId, Long userId) {
        List<Posts> findList;
        List<PostsThumbnailResponseDto> postList = new ArrayList<>();
        if (categoryId == 0) {
            findList = postsRepository.findAllOrderByCreatedAtAsc();
        } else {
            findList = postsRepository.findByCategoryIdOrderByCreatedAtAsc(categoryId);
        }
        String subList = makeSubList(findList, userId);
        for (Posts post : findList) {
            if (postExist(post.getStatus())) {
                postList.add(new PostsThumbnailResponseDto(post));
            }
        }
        return new PostsThumbnailSubListResponseDto(postList, subList);
    }

    public PostsThumbnailSubListResponseDto createPostDtoListSubscribesDesc(Long categoryId, Long userId) {
        List<Posts> findList;
        List<PostsThumbnailResponseDto> postList = new ArrayList<>();
        if (categoryId == 0) {
            findList = postsRepository.findAllOrderBySubscribesDesc();
        } else {
            findList = postsRepository.findByCategoryIdOrderBySubscribesDesc(categoryId);
        }
        String subList = makeSubList(findList, userId);
        for (Posts post : findList) {
            if (post.getSubscribes() > 0 && postExist(post.getStatus())) {
                postList.add(new PostsThumbnailResponseDto(post));
            }
        }
        Collections.reverse(findList);
        for (Posts post : findList) {
            if (post.getSubscribes() == 0 && postExist(post.getStatus())) {
                postList.add(new PostsThumbnailResponseDto(post));
            }
        }
        return new PostsThumbnailSubListResponseDto(postList, subList);
    }

    public PostsThumbnailSubListResponseDto createPostDtoListViewDesc(Long categoryId, Long userId) {
        List<Posts> findList;
        List<PostsThumbnailResponseDto> postList = new ArrayList<>();
        if (categoryId == 0) {
            findList = postsRepository.findAllOrderByViewDesc();
        } else {
            findList = postsRepository.findByCategoryIdOrderByViewDesc(categoryId);
        }
        String subList = makeSubList(findList, userId);
        for (Posts post : findList) {
            if (post.getView() > 0 && postExist(post.getStatus())) {
                postList.add(new PostsThumbnailResponseDto(post));
            }
        }
        Collections.reverse(findList); //list의 순서를 역순으로 정렬함
        for (Posts post : findList) {
            if (post.getView() == 0 && postExist(post.getStatus())) {
                postList.add(new PostsThumbnailResponseDto(post));
            }
        }
        return new PostsThumbnailSubListResponseDto(postList, subList);
    }

    public PostsThumbnailSubListResponseDto createPostDtoListPriceDesc(Long categoryId, Long userId) {
        List<Posts> findList;
        List<PostsThumbnailResponseDto> postList = new ArrayList<>();
        if (categoryId == 0) {
            findList = postsRepository.findAllOrderByPriceDesc();
        } else {
            findList = postsRepository.findByCategoryIdOrderByPriceDesc(categoryId);
        }
        String subList = makeSubList(findList, userId);
        for (Posts post : findList) {
            if (postExist(post.getStatus())) {
                postList.add(new PostsThumbnailResponseDto(post));
            }
        }
        return new PostsThumbnailSubListResponseDto(postList, subList);
    }

    public PostsThumbnailSubListResponseDto createPostDtoListPriceAsc(Long categoryId, Long userId) {
        List<Posts> findList;
        List<PostsThumbnailResponseDto> postList = new ArrayList<>();
        if (categoryId == 0) {
            findList = postsRepository.findAllOrderByPriceAsc();
        } else {
            findList = postsRepository.findByCategoryIdOrderByPriceAsc(categoryId);
        }
        String subList = makeSubList(findList, userId);
        for (Posts post : findList) {
            if (postExist(post.getStatus())) {
                postList.add(new PostsThumbnailResponseDto(post));
            }
        }
        return new PostsThumbnailSubListResponseDto(postList, subList);
    }

    // search 정렬

    public PostsThumbnailSubListResponseDto createSearchPostDtoListCreatedAtDesc(String searchWord, Long userId) {
        List<PostsThumbnailResponseDto> postList = new ArrayList<>();
        List<Posts> findList = postsRepository.findByWordLikeOrderByCreatedAtDesc(searchWord);
        String subList = makeSubList(findList, userId);
        for (Posts post : findList) {
            if (postExist(post.getStatus())) {
                postList.add(new PostsThumbnailResponseDto(post));
            }
        }
        return new PostsThumbnailSubListResponseDto(postList, subList);
    }

    public PostsThumbnailSubListResponseDto createSearchPostDtoListCreatedAtAsc(String searchWord, Long userId) {
        List<PostsThumbnailResponseDto> postList = new ArrayList<>();
        List<Posts> findList = postsRepository.findByWordLikeOrderByCreatedAtAsc(searchWord);
        String subList = makeSubList(findList, userId);
        for (Posts post : findList) {
            if (postExist(post.getStatus())) {
                postList.add(new PostsThumbnailResponseDto(post));
            }
        }
        return new PostsThumbnailSubListResponseDto(postList, subList);
    }

    public PostsThumbnailSubListResponseDto createSearchPostDtoListViewDesc(String searchWord, Long userId) {
        List<PostsThumbnailResponseDto> postList = new ArrayList<>();
        List<Posts> findList = postsRepository.findByWordLikeOrderByViewDesc(searchWord);
        String subList = makeSubList(findList, userId);
        for (Posts post : findList) {
            if (post.getView() > 0 && postExist(post.getStatus())) {
                postList.add(new PostsThumbnailResponseDto(post));
            }
        }
        Collections.reverse(findList);
        for (Posts post : findList) {
            if (post.getView() == 0 && postExist(post.getStatus())) {
                postList.add(new PostsThumbnailResponseDto(post));
            }
        }
        return new PostsThumbnailSubListResponseDto(postList, subList);
    }

    public PostsThumbnailSubListResponseDto createSearchPostDtoListSubscribesDesc(String searchWord, Long userId) {
        List<PostsThumbnailResponseDto> postList = new ArrayList<>();
        List<Posts> findList = postsRepository.findByWordLikeOrderBySubscribesDesc(searchWord);
        String subList = makeSubList(findList, userId);
        for (Posts post : findList) {
            if (post.getSubscribes() > 0 && postExist(post.getStatus())) {
                postList.add(new PostsThumbnailResponseDto(post));
            }
        }
        Collections.reverse(findList);
        for (Posts post : findList) {
            if (post.getSubscribes() == 0 && postExist(post.getStatus())) {
                postList.add(new PostsThumbnailResponseDto(post));
            }
        }
        return new PostsThumbnailSubListResponseDto(postList, subList);
    }

    public PostsThumbnailSubListResponseDto createSearchPostDtoListPriceDesc(String searchWord, Long userId) {
        List<PostsThumbnailResponseDto> postList = new ArrayList<>();
        List<Posts> findList = postsRepository.findByWordLikeOrderByPriceDesc(searchWord);
        String subList = makeSubList(findList, userId);
        for (Posts post : findList) {
            if (postExist(post.getStatus())) {
                postList.add(new PostsThumbnailResponseDto(post));
            }
        }
        return new PostsThumbnailSubListResponseDto(postList, subList);
    }

    public PostsThumbnailSubListResponseDto createSearchPostDtoListPriceAsc(String searchWord, Long userId) {
        List<PostsThumbnailResponseDto> postList = new ArrayList<>();
        List<Posts> findList = postsRepository.findByWordLikeOrderByPriceAsc(searchWord);
        String subList = makeSubList(findList, userId);
        for (Posts post : findList) {
            if (postExist(post.getStatus())) {
                postList.add(new PostsThumbnailResponseDto(post));
            }
        }
        return new PostsThumbnailSubListResponseDto(postList, subList);
    }

    private boolean postExist(int status) {
        return status != 2;
    }

    private String makeSubList(List<Posts> postList, Long userId) {
        String subList = "";
        if (userId == 0) {
            return subList;
        }
        List<Carts> byUserId = cartsRepository.findByUserId(userId);
        for (Posts post : postList) {
            for (Carts carts : byUserId) {
                if (post.getId().equals(carts.getPost().getId())) {
                    subList = subList + "/" + post.getId() + "/";
                }
            }
        }
        return subList;
    }

    private String makeSubList(Posts post, Long userId) {
        String subList = "";
        if (userId == 0) {
            return subList;
        }
        List<Carts> byUserId = cartsRepository.findByUserId(userId);
        for (Carts carts : byUserId) {
            if (post.getId().equals(carts.getPost().getId())) {
                subList = subList + "/" + post.getId() + "/";
            }
        }
        return subList;
    }

    private List<Posts> checkSearchWord(String searchWord) {
        Optional<Users> optionalUsers = usersRepository.findByIntraId2(searchWord);
        if (optionalUsers.isEmpty()) {
            return new ArrayList<>();
        }
        return optionalUsers.get().getPostsList();
    }
}
