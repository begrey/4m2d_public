package com.samyeonyiduk.web.dto.posts;

import com.samyeonyiduk.domain.posts.Posts;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

import java.time.LocalDateTime;

@ApiModel
@Getter
public class PostsThumbnailResponseDto {
    private Long id;
    private String title;
    private String author;
    private String image;
    private String local;
    private int status;
    private int subscribes;
    private int view;
    private int price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostsThumbnailResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getUser().getIntraId();
        this.image = entity.getImage() + "?" + LocalDateTime.now();
        this.local = entity.getLocal();
        this.status = entity.getStatus();
        this.subscribes = entity.getSubscribes();
        this.view = entity.getView();
        this.price = entity.getPrice();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
