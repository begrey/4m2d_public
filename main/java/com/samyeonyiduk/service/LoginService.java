package com.samyeonyiduk.service;

import com.samyeonyiduk.config.JwtUtil;
import com.samyeonyiduk.domain.users.UserToken;
import com.samyeonyiduk.domain.users.Users;
import com.samyeonyiduk.web.dto.JwtResponseDto;
import com.samyeonyiduk.web.dto.titles.TitlesAddRequestDto;
import com.samyeonyiduk.web.dto.users.UsersSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Exception.class)
public class LoginService {

    @Value("${newb}")
    private String newb;
    @Value("${uid}")
    private String uid;
    @Value("${secret}")
    private String secret;
    @Value("${redirect}")
    private String redirect;


    private final UsersService usersService;
    private final TitlesService titlesService;

    @Autowired
    JwtUtil jwtTokenProvider;

    public String getToken(String code) {
        String query = "grant_type=" + "authorization_code" + "&"
                + "client_id=" + uid + "&"
                + "client_secret=" + secret + "&"
                + "code=" + code + "&"
                + "redirect_uri=" + redirect + "&"
                + "scope=public";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        HttpEntity entity = new HttpEntity(httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://api.intra.42.fr/oauth/token?" + query, HttpMethod.POST,
                entity, String.class);
        JSONObject jsonObject;
        jsonObject = new JSONObject(responseEntity.getBody());
        String token = (String)jsonObject.get("access_token");
        return token;
    }

    public JwtResponseDto saveUser(ResponseEntity<String> responseEntity)
    {
        //json으로 파싱해서 키값 추출
        JSONObject jsonObject;
        jsonObject = new JSONObject(responseEntity.getBody());
        String intraId = jsonObject.getString("login");
        String email = jsonObject.getString("email");
        Long apiId = jsonObject.getLong("id");
        String image = jsonObject.getString("image_url");
        String introduce = "안녕하세요, " + intraId + "입니다.";

        Users user;

        if((user = usersService.findByApiId(apiId)) == null)
        {
            UsersSaveRequestDto usersSaveRequestDto = new UsersSaveRequestDto(apiId, intraId, email, image, introduce);
            user = usersService.save(usersSaveRequestDto);
            TitlesAddRequestDto dto = new TitlesAddRequestDto(user.getId(), "42newb", newb);
            titlesService.save(user.getId(), dto);
        }
        UserToken usertoken = new UserToken(user);
        String token = jwtTokenProvider.generateToken(usertoken);
        JwtResponseDto jwtResponseDto = new JwtResponseDto(token, user.getId(), email, intraId);
        return jwtResponseDto;
    }
}
