package com.samyeonyiduk.web;

import com.samyeonyiduk.config.JwtUtil;
import com.samyeonyiduk.service.LoginService;
import com.samyeonyiduk.web.dto.JwtResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class LoginController {

    private final LoginService loginService;

    @Autowired
    JwtUtil jwtTokenProvider;

    @GetMapping("/login")
    public void LoginPage(HttpServletResponse response) throws IOException {
        String redirect_uri= "https://api.intra.42.fr/oauth/authorize?client_id=2b02d6cbfa01cb92c9572fc7f3fbc94895fc108fc55768a7b3f47bc1fb014f01&redirect_uri=http%3A%2F%2Fapi.4m2d.site%2Flogin%2FgetToken&response_type=code";
        response.sendRedirect(redirect_uri);
    }

    @GetMapping("/login/getToken")
    public void getLogin(@RequestParam(name = "code") String code, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = "https://api.intra.42.fr/v2/me";
        String token = loginService.getToken(code);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + token);
        HttpEntity entity = new HttpEntity(httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET,
                entity, String.class);
        JwtResponseDto jwt = loginService.saveUser(responseEntity);
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {

                if (cookie.getName().equals("Authorization") || cookie.getName().equals("userId")) {
                    cookie.setMaxAge(0); // 쿠키의 expiration 타임을 0으로 하여 없앤다.
                    cookie.setPath("/");
                }
            }
        }

        Cookie jwtToken = new Cookie("Authorization", jwt.getToken());
        Cookie userId = new Cookie("userId", jwt.getUserId().toString());
        Cookie intra = new Cookie("intra", jwt.getIntraId().toString());
        jwtToken.setMaxAge(2 * 60 * 60);
        userId.setMaxAge(2 * 60 * 60);
        intra.setMaxAge(2 * 60 * 60);
        jwtToken.setPath("/");
        userId.setPath("/");
        intra.setPath("/");
        jwtToken.setDomain("4m2d.site");
        userId.setDomain("4m2d.site");
        intra.setDomain("4m2d.site");
        response.addCookie(jwtToken);
        response.addCookie(userId);
        response.addCookie(intra);
        response.sendRedirect("http://www.4m2d.site/");


    }

}

