package com.samyeonyiduk.service;

import com.samyeonyiduk.domain.carts.Carts;
import com.samyeonyiduk.domain.carts.CartsRepository;
import com.samyeonyiduk.domain.posts.Posts;
import com.samyeonyiduk.domain.posts.PostsRepository;
import com.samyeonyiduk.domain.users.Users;
import com.samyeonyiduk.domain.users.UsersRepository;
import com.samyeonyiduk.web.dto.CartsAddRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Exception.class)
public class CartsService {
    private final CartsRepository cartsRepository;
    private final UsersRepository usersRepository;
    private final PostsRepository postsRepository;

    public Carts save (CartsAddRequestDto cartsAddRequestDto) {
        Users user = usersRepository.getById(cartsAddRequestDto.getUserId());
        Posts post = postsRepository.getById(cartsAddRequestDto.getPostId());
        return(cartsRepository.save(cartsAddRequestDto.toEntity(user, post)));
    }

    public Carts findById(Long id) {
        Carts cart = cartsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                "해당 장바구니가 없습니다. ID = " + id));
        return (cart);
    }

    public Carts findCartByUserAndPostID(Long userId, Long postId) {
        Carts cart = cartsRepository.findByUser_IdAndPost_Id(userId, postId);
        if (cart == null)
            throw new IllegalArgumentException("해당 장바구니가 없습니다. userID = " + userId + "postID = " + postId);
        return cart;
    }

    public List<Long> findPostIdByUserId(Long userId) {
        List<Long> subscribeBypostId = new ArrayList<>();
        List<Carts> cartsList = cartsRepository.findByUserId(userId);
        for (Carts cart : cartsList) {
            subscribeBypostId.add(cart.getPost().getId());
        }
        return subscribeBypostId;
    }

    public void deleteById(Long id) {
        cartsRepository.deleteById(id);
    }
}
