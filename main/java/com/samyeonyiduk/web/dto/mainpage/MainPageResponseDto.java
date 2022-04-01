package com.samyeonyiduk.web.dto.mainpage;

import io.swagger.annotations.ApiModel;
import lombok.Getter;

import java.util.List;

@ApiModel
@Getter
public class MainPageResponseDto {
    private List<MainPagePostResponseDto> subscribeList;
    private List<MainPagePostResponseDto> viewList;
    private String subList;

    public MainPageResponseDto(List<MainPagePostResponseDto> subscribeList,
                               List<MainPagePostResponseDto> viewList,
                               String subList) {
        this.subscribeList = subscribeList;
        this.viewList = viewList;
        this.subList = subList;
    }
}
