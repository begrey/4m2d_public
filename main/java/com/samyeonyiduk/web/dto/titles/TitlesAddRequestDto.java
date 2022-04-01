package com.samyeonyiduk.web.dto.titles;

import com.samyeonyiduk.domain.carts.Carts;
import com.samyeonyiduk.domain.posts.Posts;
import com.samyeonyiduk.domain.titles.Titles;
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
public class TitlesAddRequestDto {
    @ApiModelProperty(example="user 아이디")
    private Long userId;

    private String name;

    private String image;

    public Titles toEntity(Users user, String name, String image) {
        return Titles.builder()
                .user(user)
                .name(name)
                .image(image)
                .build();
    }
}
