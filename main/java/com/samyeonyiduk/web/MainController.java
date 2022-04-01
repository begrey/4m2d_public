package com.samyeonyiduk.web;

import com.samyeonyiduk.service.PostsService;
import com.samyeonyiduk.web.dto.mainpage.MainPageResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RequiredArgsConstructor
@Api(tags = {"MAIN API"})
@RestController
public class MainController {

    private final PostsService postsService;

    @ApiOperation(value = "MainPage", notes="구독자, 조회 수 top 10을 보여주는 곳입니다.", response = Map.class)
    @GetMapping("api/{id}")
    public MainPageResponseDto showMainPage(@CookieValue(name = "subscribes", required = false) Cookie subCookie,
                                            HttpServletResponse response, @PathVariable("id") Long userId) {

        return postsService.findMainPage(userId);
    }
//    @ApiOperation(value = "Logout", notes="로그아웃", response = Map.class)
//    @GetMapping("api/logout")
//    public void logout(HttpServletRequest request, HttpServletResponse response) {
////        Cookie[] cookies = request.getCookies();
////        for(Cookie cookie : cookies) {
////            System.out.println(cookie.getName());
////            if (cookie.getName().equals("Authorization") || cookie.getName().equals("userId")) {
////                cookie.setMaxAge(0);
////            }
////        }
//    }

}
