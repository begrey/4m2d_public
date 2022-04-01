package com.samyeonyiduk.web.dto.comments;

import com.samyeonyiduk.domain.comments.Comments;
import com.samyeonyiduk.domain.posts.Posts;
import com.samyeonyiduk.domain.users.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentsSaveRequestDto {
    private Long userId;

    private Long postId;

    private String content;

    public Comments toEntity(Users user, Posts post) {
        return Comments.builder()
                .user(user)
                .post(post)
                .content(content)
                .build();
    }
}
