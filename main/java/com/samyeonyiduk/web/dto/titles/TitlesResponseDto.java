package com.samyeonyiduk.web.dto.titles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TitlesResponseDto {
    private Long id;

    private String name;

    private String image;
}
