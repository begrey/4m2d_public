package com.samyeonyiduk.web.dto.titles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TitlesPostResponseDto {

    private String name;

    private String image;

    private int status; // 0 : 미획득 1 : 획득 2 : 획득가능
}
