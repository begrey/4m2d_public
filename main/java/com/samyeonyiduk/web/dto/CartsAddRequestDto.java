package com.samyeonyiduk.web.dto;

import com.samyeonyiduk.domain.carts.Carts;
import com.samyeonyiduk.domain.posts.Posts;
import com.samyeonyiduk.domain.users.Users;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@Builder
public class CartsAddRequestDto {

    @ApiModelProperty(example="user 아이디")
    private Long userId;
    @ApiModelProperty(example="post 아이디")
    private Long postId;

    public Carts toEntity(Users user, Posts post) {
        return Carts.builder()
                .user(user)
                .post(post)
                .build();
    }
}
