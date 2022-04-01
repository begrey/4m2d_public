package com.samyeonyiduk.web.dto.posts;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class PostsUpdateRequestDto {

    @ApiModelProperty(example="제목")
    private String title;
    @ApiModelProperty(example="내용")
    private String content;
    @ApiModelProperty(example="가격")
    private int price;
    @ApiModelProperty(example="지역")
    private String local;
    @ApiModelProperty(example="카테고리id")
    private Long categoryId;
    @ApiModelProperty(example="기존파일들")
    private List<String> oldFileList;

    public Map<String, Object> dtoToMap() {
        Map<String, Object> patchMapDto = new HashMap<>();
        patchMapDto.put("title", this.title);
        patchMapDto.put("content", this.content);
        patchMapDto.put("price", this.price);
        patchMapDto.put("local", this.local);
        patchMapDto.put("updatedAt", "");
        patchMapDto.put("categoryId", this.categoryId);
        return patchMapDto;
    }
}
