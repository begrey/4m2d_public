package com.samyeonyiduk.web.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponseDto implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    private String token;

    private Long userId;
    //password
    private String email;
    //username
    private String intraId;

}
