package com.samyeonyiduk.web;

import com.samyeonyiduk.domain.carts.Carts;
import com.samyeonyiduk.service.CartsService;
import com.samyeonyiduk.service.PostsService;
import com.samyeonyiduk.web.dto.CartsAddRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.http.Cookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Api(tags = {"CART API"})
@RestController
public class CartsController {

    private final CartsService cartsService;
    private final PostsService postsService;

    @ApiOperation(value = "cart 등록", notes="찜을 등록하는 곳입니다.", response = Map.class)
    @PostMapping("api/carts")
    public void save(@RequestBody CartsAddRequestDto requestDto) {

        Carts cart = cartsService.save(requestDto);
        Long postId = cart.getPost().getId();
        Map<String, Object> countSub = new HashMap<>();
        countSub.put("subscribes", 1);
        postsService.patch(postId, countSub);
    }

    @ApiOperation(value = "cart 삭제", notes="찜을 해제하는 곳입니다.", response = Map.class)
    @DeleteMapping("api/carts/{userId}/{postId}")
    public void delete(@PathVariable long userId, @PathVariable long postId) {
        Carts cart = cartsService.findCartByUserAndPostID(userId, postId);
        cartsService.deleteById(cart.getId());
        Map<String, Object> countSub = new HashMap<>();
        countSub.put("subscribes", -1);
        postsService.patch(postId, countSub);
    }
}
