package com.samyeonyiduk.web;

import com.samyeonyiduk.service.UsersService;
import com.samyeonyiduk.web.dto.mypage.MypageResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Api(tags = {"USER API"})
@RestController
public class UsersController {

    private final UsersService usersService;

    @ApiOperation(value = "user찾기", notes="마이페이지를 호출하는 곳입니다.", response = MypageResponseDto.class)
    @GetMapping("api/users/{id}")
    public MypageResponseDto findById (@PathVariable Long id) {
        return usersService.findById(id);
    }

    @ApiOperation(value = "user 수정", notes="마이페이지 내부를 수정하는 곳입니다.", response = Map.class)
    @PatchMapping("api/users/{id}")
    public void patch(@PathVariable Long id, @RequestBody Map<String, Object> patchMapDto) throws IOException {
        usersService.patch(id, patchMapDto);
    }

    @ApiOperation(value = "user 수정", notes="user profile image 수정하는 곳입니다.", response = Map.class)
    @PostMapping("api/users/{id}")
    public void imageChange(@PathVariable Long id, @RequestPart(value="image", required=true) MultipartFile image) throws IOException {
        usersService.imageUpdate(id, image);
    }
}
