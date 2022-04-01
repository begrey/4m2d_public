package com.samyeonyiduk.web.dto.posts;

import com.samyeonyiduk.domain.comments.Comments;
import com.samyeonyiduk.domain.posts.Posts;
import com.samyeonyiduk.web.dto.comments.CommentsResponseDto;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApiModel
@Getter
public class PostsResponseDto {
    private Long id;
    private Long userId;
    private Long categoryId;
    private String title;
    private String content;
    private String author;
    private String category_name;
    private List<String> image;
    private int price;
    private String local;
    private int status;
    private int subscribes;
    private int view;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentsResponseDto> commentsList;
    private String subList;

    public PostsResponseDto(Posts entity, List<String> imageList, String subList) {
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.categoryId = entity.getCategory().getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getUser().getIntraId();
        this.category_name = entity.getCategory().getName();
        this.image = imageList;
        this.price = entity.getPrice();
        this.local = entity.getLocal();
        this.status = entity.getStatus();
        this.subscribes = entity.getSubscribes();
        this.view = entity.getView();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
        this.commentsList = createAllCommentList(entity.getCommentList());
        this.subList = subList;
    }

    private List<CommentsResponseDto> createAllCommentList(List<Comments> commentList) {
        List<CommentsResponseDto> allCommentList = new ArrayList<>();
        for (Comments comment : commentList) {
            allCommentList.add(new CommentsResponseDto(comment));
        }
        return allCommentList;
    }
}
