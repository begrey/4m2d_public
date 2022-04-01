package com.samyeonyiduk.service;

import com.samyeonyiduk.domain.comments.Comments;
import com.samyeonyiduk.domain.comments.CommentsRepository;
import com.samyeonyiduk.domain.posts.Posts;
import com.samyeonyiduk.domain.posts.PostsRepository;
import com.samyeonyiduk.domain.users.Users;
import com.samyeonyiduk.domain.users.UsersRepository;
import com.samyeonyiduk.web.dto.comments.CommentsSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Exception.class)
public class CommentsService {

    private final CommentsRepository commentsRepository;
    private final UsersRepository usersRepository;
    private final PostsRepository postsRepository;

    public void handleExperience(int exp, Users user) {
        double currentExp = user.getExperience() + exp;
        if (currentExp >= 100) {
            user.findKeyAndPatch("level", 1);
            currentExp -= 100;
        }
        user.findKeyAndPatch("experience", currentExp);
    }

    public void save(CommentsSaveRequestDto commentSaveRequestDto) {
        Optional<Users> optionalUser = usersRepository.findById(commentSaveRequestDto.getUserId());
        Optional<Posts> optionalPost = postsRepository.findById(commentSaveRequestDto.getPostId());
        if (optionalUser.isEmpty() || optionalPost.isEmpty()) {
            throw new NoSuchElementException();
        }
        commentsRepository.save(commentSaveRequestDto.toEntity(optionalUser.get(), optionalPost.get()));
        handleExperience(10, optionalUser.get()); // 댓글 10
    }

    public void update(Long commentId, Map<String, Object> updateList) {
        Optional<Comments> optionalComment = commentsRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new NoSuchElementException();
        }
        optionalComment.get().update(updateList.get("content"));
    }
    public void delete(Long commentId) {
        Optional<Comments> optionalComments = commentsRepository.findById(commentId);
        if (optionalComments.isEmpty()) {
            throw new NoSuchElementException();
        }
        commentsRepository.delete(optionalComments.get());
    }
}
