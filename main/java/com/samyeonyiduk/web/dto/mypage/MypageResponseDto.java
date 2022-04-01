package com.samyeonyiduk.web.dto.mypage;

import com.samyeonyiduk.domain.users.Users;
import com.samyeonyiduk.web.dto.titles.TitlesResponseDto;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

import java.util.List;

@Getter
@ApiModel
public class MypageResponseDto {
    private Long id;
    private String introduce;
    private String userIntra;
    private String userEmail;
    private String userImage;
    private int userLevel;
    private double userExperience;
    private String userTitle;
    private List<MypagePostResponseDto> postsList;
    private List<MypagePostResponseDto> cartsList;
    private List<TitlesResponseDto> titleList;

    public MypageResponseDto(Users entity,
                             List<MypagePostResponseDto> cartsList,
                             List<MypagePostResponseDto> postsList,
                             List<TitlesResponseDto> titleList) {
        this.id = entity.getId();
        this.introduce = entity.getIntroduce();
        this.userIntra = entity.getIntraId();
        this.userEmail = entity.getEmail();
        this.userImage = entity.getImage();
        this.userLevel = entity.getLevel();
        this.userExperience = entity.getExperience();
        this.userTitle = entity.getUserTitle();
        this.postsList = postsList;
        this.cartsList = cartsList;
        this.titleList = titleList;
    }
}
