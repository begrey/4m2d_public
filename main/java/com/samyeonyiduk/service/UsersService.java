package com.samyeonyiduk.service;

import com.samyeonyiduk.domain.carts.Carts;
import com.samyeonyiduk.domain.carts.CartsRepository;
import com.samyeonyiduk.domain.posts.Posts;
import com.samyeonyiduk.domain.users.Users;
import com.samyeonyiduk.domain.users.UsersRepository;
import com.samyeonyiduk.web.dto.mypage.MypagePostResponseDto;
import com.samyeonyiduk.web.dto.mypage.MypageResponseDto;
import com.samyeonyiduk.web.dto.titles.TitlesResponseDto;
import com.samyeonyiduk.web.dto.users.UsersSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Exception.class)
public class UsersService {

    private final UsersRepository usersRepository;
    private final CartsRepository cartsRepository;
    private final AmazonS3Service amazonS3Service;
    private final TitlesService titlesService;

    public Users save(UsersSaveRequestDto usersSaveRequestDto) {
        Users user = usersRepository.save(usersSaveRequestDto.toEntity());
        return user;
    }

    public Users findByApiId (Long id) {
        Users user = usersRepository.findByApiId(id);
        if (user == null)
            return null;
        else
            return user;
    }

    public MypageResponseDto findById(Long id) {
        Users entity = usersRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                "해당 게시물이 없습니다. ID = " + id));
        List<Carts> carts = cartsRepository.findByUserId(id);
        List<MypagePostResponseDto> cartList = new ArrayList<>();
        List<MypagePostResponseDto> postList = new ArrayList<>();
        //List<Posts> postsList = posts
        for (Posts post : entity.getPostsList()) {
            if (postExist(post.getStatus())) {
                postList.add(new MypagePostResponseDto(post));
            }
        }
        for (Carts cart : carts) {
            Posts post = cart.getPost();
            if (postExist(post.getStatus())) {
                cartList.add(new MypagePostResponseDto(post));
            }
        }
        List<TitlesResponseDto> titleList = titlesService.showMyTitles(id);
        return new MypageResponseDto(entity, cartList, postList, titleList);
    }

    public void imageUpdate(Long id, MultipartFile image) throws IOException {
        Users user = usersRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                "해당 게시물이 없습니다. ID = " + id));
        user.patch(new HashMap<>() {{
            put("image", amazonS3Service.userProfile(image, id));
        }
        });
    }

    public void patch(Long id, Map<String, Object> patchMap) throws IOException {
        Users mypage = usersRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                "해당 게시물이 없습니다. ID = " + id));
        mypage.patch(patchMap);
    }

    private boolean postExist(int status) {
            return status != 2;
    }
}
