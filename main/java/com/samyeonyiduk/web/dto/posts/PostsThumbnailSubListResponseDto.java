package com.samyeonyiduk.web.dto.posts;

import io.swagger.annotations.ApiModel;
import lombok.Getter;

import java.util.List;

@ApiModel
@Getter
public class PostsThumbnailSubListResponseDto {

    List<PostsThumbnailResponseDto> postsThumbnailResponseDtoList;

    private String subList;

    public PostsThumbnailSubListResponseDto(List<PostsThumbnailResponseDto> postsThumbnailResponseDtoList, String subList) {
        this.postsThumbnailResponseDtoList = postsThumbnailResponseDtoList;
        this.subList = subList;
    }
}
