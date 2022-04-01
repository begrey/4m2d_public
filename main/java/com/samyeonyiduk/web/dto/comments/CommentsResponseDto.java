package com.samyeonyiduk.web.dto.comments;

import com.samyeonyiduk.domain.comments.Comments;
import lombok.Getter;

@Getter
public class CommentsResponseDto {

    private final Long id;

    private final String userIntraId;

    private final String image;

    private final String content;

    private final String createdAt;

    private final String updatedAt;

    public CommentsResponseDto(Comments comment) {
        this.id = comment.getId();
        this.userIntraId = comment.getUser().getIntraId();
        this.image = comment.getUser().getImage();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}
