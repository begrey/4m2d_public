package com.samyeonyiduk.web.dto.users;

import com.samyeonyiduk.domain.users.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersSaveRequestDto {
    private Long apiId;

    private String intraId;

    private String email;

    private String image;

    private String introduce;

    public Users toEntity() {
        return Users.builder()
                .apiId(apiId)
                .intraId(intraId)
                .email(email)
                .image(image)
                .introduce(introduce)
                .build();

    }
}
