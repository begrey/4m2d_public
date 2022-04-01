package com.samyeonyiduk.web;

import com.samyeonyiduk.service.PostsService;
import com.samyeonyiduk.web.dto.posts.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Api(tags = {"POST API"})
@RestController
public class PostsController {

    private final PostsService postsService;

    @ApiOperation(value = "post등록", notes = "게시물 등록하는 곳입니다.", response = PostsResponseDto.class)
    @PostMapping(value = "api/posts", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public void save(@ApiParam(value = "게시물 등록 form") @RequestPart(value = "data", required = false) PostsUploadRequestDto requestDto,
                     @ApiParam(value = "게시물 사진들") @RequestPart(value = "fileList", required = true) List<MultipartFile> fileList) throws IOException {
        postsService.save(requestDto, fileList);

    }

    @ApiOperation(value = "post수정", notes = "게시물 수정하는 곳입니다. 판매중 0, 판매완료 1", response = Map.class)
    @PostMapping(value = "api/posts/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public void update(@PathVariable Long id,
                       @ApiParam(value = "게시물 등록 form")
                       @RequestPart(value = "data", required = false) PostsUpdateRequestDto updateDto,
                       @ApiParam(value = "게시물 사진")
                       @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) throws IOException {
        Map<String, Object> patchMapDto = updateDto.dtoToMap();
        if (fileList != null || updateDto.getOldFileList() != null)
            postsService.patchImage(id, fileList, updateDto.getOldFileList());
        patchMapDto.remove("oldFileList");
        postsService.patch(id, patchMapDto);
    }

    @ApiOperation(value = "post 판매완료", notes = "게시물 판매완료 처리 판매중 0, 판매완료 1", response = Map.class)
    @PatchMapping(value = "api/posts/{id}")
    public void patch(@PathVariable Long id, @RequestBody Map<String, Object> patchMapDto) {
        postsService.patch(id, patchMapDto);
    }



    @ApiOperation(value = "post 상세보기", notes = "post id를 통해 단일 상세 게시물을 보는 곳입니다. view 0 : 증가안함 1 : 증가", response = PostsResponseDto.class)
    @GetMapping("api/posts/{id}/{userId}/{view}")
    public PostsResponseDto findById(@PathVariable("id") Long id, @PathVariable("userId") Long userId, @PathVariable("view") int view) throws IOException {
        return postsService.findById(id, userId, view);
    }

    @ApiOperation(value = "post 카테고리", notes = "카테고리 별로 게시물을 보는 곳입니다.", response = PostsThumbnailResponseDto.class)
    @GetMapping("api/posts/category/{id}/{userId}")
    public PostsThumbnailSubListResponseDto findByCategoryId(@PathVariable("id") Long id,
                                                             @PathVariable("userId") Long userId) {

        return postsService.findByForeignKeyId(id, "category", userId);
    }

    @ApiOperation(value = "post 유저", notes = "사용자가 등록한 게시물을 모두 보는 곳입니다.", response = PostsThumbnailResponseDto.class)
    @GetMapping("api/posts/user/{id}")
    public PostsThumbnailSubListResponseDto findByUserId(@PathVariable Long id) {

        return postsService.findByForeignKeyId(id, "user", 0L);
    }

    @ApiOperation(value = "post 검색", notes = "제목과 내용에 검색어가 포함되어 있는 모든 게시물", response = PostsThumbnailResponseDto.class)
    @GetMapping("/api/posts/search/{id}/{searchWord}")
    public PostsThumbnailSubListResponseDto allPostBySearch(@PathVariable("id") Long userId, @PathVariable("searchWord") String searchWord) {
        return postsService.createAllPostDtoListBySearch(searchWord, userId);
    }

    @ApiOperation(value = "post 정렬", notes = "하나의 카테고리에 해당되는 모든 게시물을 최신순으로 정렬한 게시물", response = PostsThumbnailResponseDto.class)
    @GetMapping("/api/posts/category/{id}/desc/{userId}")
    public PostsThumbnailSubListResponseDto allPostByCategoryOrderByCreatedAtDesc(@PathVariable("id") Long categoryId,
                                                                                  @PathVariable("userId") Long userId) {
        return postsService.createPostDtoListCreatedAtDesc(categoryId, userId);
    }

    @ApiOperation(value = "post 정렬", notes = "하나의 카테고리에 해당되는 모든 게시물을 오래된순으로 정렬한 게시물", response = PostsThumbnailResponseDto.class)
    @GetMapping("/api/posts/category/{id}/asc/{userId}")
    public PostsThumbnailSubListResponseDto allPostByCategoryOrderByCreatedAtAsc(@PathVariable("id") Long categoryId,
                                                                                 @PathVariable("userId") Long userId) {
        return postsService.createPostDtoListCreatedAtAsc(categoryId, userId);
    }

    @ApiOperation(value = "post 정렬", notes = "하나의 카테고리에 해당되는 모든 게시물을 좋아요순으로 정렬하고 좋아요가 0인 게시물들은 다시 최신순으로 정렬한 게시물", response = PostsThumbnailResponseDto.class)
    @GetMapping("/api/posts/category/{id}/subscribes/{userId}")
    public PostsThumbnailSubListResponseDto allPostByCategoryOrderBySubscribesDesc(@PathVariable("id") Long categoryId,
                                                                                   @PathVariable("userId") Long userId) {
        return postsService.createPostDtoListSubscribesDesc(categoryId, userId);
    }

    @ApiOperation(value = "post 정렬", notes = "하나의 카테고리에 해당되는 모든 게시물을 조회순으로 정렬한 게시물", response = PostsThumbnailResponseDto.class)
    @GetMapping("/api/posts/category/{id}/view/{userId}")
    public PostsThumbnailSubListResponseDto allPostByCategoryOrderByViewDesc(@PathVariable("id") Long categoryId,
                                                                             @PathVariable("userId") Long userId) {
        return postsService.createPostDtoListViewDesc(categoryId, userId);
    }

    @ApiOperation(value = "post 정렬", notes = "하나의 카테고리에 해당되는 모든 게시물을 가격 높은순으로 정렬한 게시물", response = PostsThumbnailResponseDto.class)
    @GetMapping("/api/posts/category/{id}/pricedesc/{userId}")
    public PostsThumbnailSubListResponseDto allPostByCategoryOrderByPriceDesc(@PathVariable("id") Long categoryId,
                                                                              @PathVariable("userId") Long userId) {
        return postsService.createPostDtoListPriceDesc(categoryId, userId);
    }

    @ApiOperation(value = "post 정렬", notes = "하나의 카테고리에 해당되는 모든 게시물을 가격 낮은순으로 정렬한 게시물", response = PostsThumbnailResponseDto.class)
    @GetMapping("/api/posts/category/{id}/priceasc/{userId}")
    public PostsThumbnailSubListResponseDto allPostByCategoryOrderByPriceAsc(@PathVariable("id") Long categoryId,
                                                                              @PathVariable("userId") Long userId) {
        return postsService.createPostDtoListPriceAsc(categoryId, userId);
    }

    // search 정렬

    @GetMapping("/api/posts/search/{id}/{searchWord}/desc")
    public PostsThumbnailSubListResponseDto allPostBySearchOrderByCreatedAtDesc(@PathVariable("id") Long userId,
                                                                                @PathVariable("searchWord") String searchWord) {
        return postsService.createSearchPostDtoListCreatedAtDesc(searchWord, userId);
    }

    @GetMapping("/api/posts/search/{id}/{searchWord}/asc")
    public PostsThumbnailSubListResponseDto allPostBySearchOrderByCreatedAtAsc(@PathVariable("id") Long userId,
                                                                                @PathVariable("searchWord") String searchWord) {
        return postsService.createSearchPostDtoListCreatedAtAsc(searchWord, userId);
    }

    @GetMapping("/api/posts/search/{id}/{searchWord}/subscribes")
    public PostsThumbnailSubListResponseDto allPostBySearchOrderBySubscribesDesc(@PathVariable("id") Long userId,
                                                                               @PathVariable("searchWord") String searchWord) {
        return postsService.createSearchPostDtoListSubscribesDesc(searchWord, userId);
    }

    @GetMapping("/api/posts/search/{id}/{searchWord}/view")
    public PostsThumbnailSubListResponseDto allPostBySearchOrderByViewDesc(@PathVariable("id") Long userId,
                                                                                @PathVariable("searchWord") String searchWord) {
        return postsService.createSearchPostDtoListViewDesc(searchWord, userId);
    }

    @GetMapping("/api/posts/search/{id}/{searchWord}/pricedesc")
    public PostsThumbnailSubListResponseDto allPostBySearchOrderByPriceDesc(@PathVariable("id") Long userId,
                                                                                @PathVariable("searchWord") String searchWord) {
        return postsService.createSearchPostDtoListPriceDesc(searchWord, userId);
    }

    @GetMapping("/api/posts/search/{id}/{searchWord}/priceasc")
    public PostsThumbnailSubListResponseDto allPostBySearchOrderByPriceAsc(@PathVariable("id") Long userId,
                                                                                @PathVariable("searchWord") String searchWord) {
        return postsService.createSearchPostDtoListPriceAsc(searchWord, userId);
    }

}


