package com.samyeonyiduk.web;

import com.samyeonyiduk.service.TitlesService;
import com.samyeonyiduk.web.dto.titles.TitlesAddRequestDto;
import com.samyeonyiduk.web.dto.titles.TitlesPostResponseDto;
import com.samyeonyiduk.web.dto.titles.TitlesResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Api(tags = {"Title API"})
@RestController
public class TitlesController {

    private final TitlesService titlesService;

    @ApiOperation(value = "title 관리", notes="title status창 관리하는곳", response = TitlesPostResponseDto.class)
    @GetMapping("api/titles/manage/{id}")
    public List<TitlesPostResponseDto> getTitleManageList(@PathVariable("id") Long userId) {
        return (titlesService.showTitleList(userId));
    }
    @ApiOperation(value = "title 불러오기", notes="내 title 리스트 불러오기", response = TitlesPostResponseDto.class)
    @GetMapping("api/titles/{id}")
    public List<TitlesResponseDto> getMyTitleList(@PathVariable("id") Long userId) {
        System.out.println("aaaaaaa");
        return (titlesService.showMyTitles(userId));
    }
    @ApiOperation(value = "title 관리", notes="title 획득하기")
    @PostMapping("api/titles/{id}")
    public void obtainTitle(@PathVariable("id") Long userId, @RequestBody TitlesAddRequestDto requestDto) {
        titlesService.save(userId, requestDto);
    }
}
