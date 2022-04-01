package com.samyeonyiduk.web.dto.mainpage;

import com.samyeonyiduk.domain.posts.Posts;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ApiModel
@Getter
public class MainPagePostResponseDto {
    private Long id;
    private Long userId;
    private Long categoryId;
    private String title;
    private String content;
    private String author;
    private String categoryName;
    private String image;
    private int price;
    private String local;
    private int subscribes;
    private int view;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MainPagePostResponseDto(Posts post) {
        this.id = post.getId();
        this.userId = post.getUser().getId();
        this.categoryId = post.getCategory().getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = post.getUser().getIntraId();
        this.categoryName = post.getCategory().getName();
        this.image = post.getImage() + "?" + LocalDate.now();
        this.price = post.getPrice();
        this.local = post.getLocal();
        this.subscribes = post.getSubscribes();
        this.view = post.getView();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
}
