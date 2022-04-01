package com.samyeonyiduk.web.dto.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsersResponseDto {
    private Long apiId;

    private Long userId;

    private String intraId;

    private String email;
}
