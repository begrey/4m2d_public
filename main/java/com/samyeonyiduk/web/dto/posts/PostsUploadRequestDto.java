package com.samyeonyiduk.web.dto.posts;

import com.samyeonyiduk.domain.category.Category;
import com.samyeonyiduk.domain.posts.Posts;
import com.samyeonyiduk.domain.users.Users;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@Builder
public class PostsUploadRequestDto {
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
    @ApiModelProperty(example="유저id")
    private Long userId;

    public Posts toEntity(Users user, Category category) {
        return Posts.builder()
                .title(title)
                .content(content)
                .price(price)
                .local(local)
                .user(user)
                .category(category)
                .build();
    }

}
