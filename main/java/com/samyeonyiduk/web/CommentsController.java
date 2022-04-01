package com.samyeonyiduk.web;

import com.samyeonyiduk.service.CommentsService;
import com.samyeonyiduk.web.dto.comments.CommentsSaveRequestDto;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@Api(tags = {"COMMENT API"})
@RestController
public class CommentsController {
    private final CommentsService commentsService;

    // PostMapping
    @PostMapping("/api/comments/")
    public void makeComment(@RequestBody CommentsSaveRequestDto commentsSaveRequestDto) {
        commentsService.save(commentsSaveRequestDto);

    }

    // PutMapping
    @PutMapping("/api/comments/{id}")
    public void update(@PathVariable("id") Long commentId, @RequestBody Map<String, Object> updateList) {
        commentsService.update(commentId, updateList);
    }
    // DeleteMapping
    @DeleteMapping("/api/comments/{id}")
    public void delete(@PathVariable("id") Long commentId) {
        commentsService.delete(commentId);
    }
}
