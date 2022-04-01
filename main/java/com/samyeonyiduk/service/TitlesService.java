package com.samyeonyiduk.service;

import com.samyeonyiduk.domain.titles.TitleLists;
import com.samyeonyiduk.domain.titles.TitleListsRepository;
import com.samyeonyiduk.domain.titles.Titles;
import com.samyeonyiduk.domain.titles.TitlesRepository;
import com.samyeonyiduk.domain.users.Users;
import com.samyeonyiduk.domain.users.UsersRepository;
import com.samyeonyiduk.web.dto.titles.TitlesAddRequestDto;
import com.samyeonyiduk.web.dto.titles.TitlesPostResponseDto;
import com.samyeonyiduk.web.dto.titles.TitlesResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Exception.class)
public class TitlesService {

    public final TitlesRepository titlesRepository;
    public final TitleListsRepository titleListsRepository;
    public final UsersRepository usersRepository;

    public void save(Long userId, TitlesAddRequestDto requestDto) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(
                "해당 사용자가 없습니다. ID = " + userId));
        titlesRepository.save(requestDto.toEntity(user, requestDto.getName(), requestDto.getImage()));
    }

    public void validateTitle(Users user, TitlesPostResponseDto dto) {
        switch(dto.getName()) {
            case "42newb" :
                dto.setStatus(2);
                break;
            case "master" :
                if (user.getAuth().equals("ROLE_ADMIN") == true)
                    dto.setStatus(2);
                break;
            default:
                break;
        }
    }

    public List<TitlesPostResponseDto> showTitleList(Long userId) {
        List<TitlesPostResponseDto> titlesList = new ArrayList<>();
        List<TitleLists> titleList = titleListsRepository.findAll();
        Users user = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(
                "해당 사용자가 없습니다. ID = " + userId));
        List<Titles> userTitles = titlesRepository.findByUserId(userId);
        for (TitleLists title : titleList) {
            TitlesPostResponseDto dto = new TitlesPostResponseDto(title.getName(), title.getImage(), 0);
            validateTitle(user, dto);
            for (Titles userTitle : userTitles) {
                if (dto.getName().equals(userTitle))
                    dto.setStatus(1); //이미 획득됨
            }
            titlesList.add(dto);
        }
        return titlesList;
    }

    public List<TitlesResponseDto> showMyTitles(Long userId) {
        List<Titles> userTitles = titlesRepository.findByUserId(userId);
        List<TitlesResponseDto> myTitleList = new ArrayList<>();
        for (Titles titles : userTitles) {
            myTitleList.add(new TitlesResponseDto(titles.getId(), titles.getName(), titles.getImage()));
        }
        return myTitleList;
    }
}
