package com.golym.mylog.controller;

import com.golym.mylog.common.constants.ResponseType;
import com.golym.mylog.common.exception.BadRequestException;
import com.golym.mylog.common.exception.ConflictException;
import com.golym.mylog.model.dto.common.CategoryDto;
import com.golym.mylog.model.dto.common.PostDto;
import com.golym.mylog.model.dto.request.RequestCreateCategoryDto;
import com.golym.mylog.model.dto.request.RequestCreatePostDto;
import com.golym.mylog.model.dto.request.RequestUpdateCategoryNameDto;
import com.golym.mylog.model.dto.request.RequestUpdatePostDto;
import com.golym.mylog.model.dto.response.ResponseCreateCategoryDto;
import com.golym.mylog.model.dto.response.ResponseDto;
import com.golym.mylog.model.dto.response.ResponseGetPostsDto;
import com.golym.mylog.service.CategoryService;
import com.golym.mylog.service.PostService;
import com.golym.mylog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/blog")
@RequiredArgsConstructor
public class BlogController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final PostService postService;

    /* 카테고리 추가 */
    @PostMapping("/category")
    public ResponseEntity<?> createCategory(@Valid @RequestBody RequestCreateCategoryDto params) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        // 카테고리명 중복 체크
        if (categoryService.isExistsCategoryByUserIdAndName(userId, params.getName()))
            throw new ConflictException("Duplicate category name. name=" + params.getName());

        // 카테고리 생성
        CategoryDto category = categoryService.createCategory(userId, params.getName());

        return new ResponseEntity<>(ResponseCreateCategoryDto.builder()
                .response(new ResponseDto(ResponseType.SUCCESS))
                .category(category)
                .build(),
                HttpStatus.OK);
    }

    /* 카테고리 삭제 */
    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable("categoryId") String categoryId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        // 요청한 유저의 카테고리가 맞는지 체크
        if (!categoryService.isExistsCategoryByCategoryIdAndUserId(categoryId, userId))
            throw new BadRequestException("Invalid delete category.");

        // 카테고리 삭제
        categoryService.deleteCategory(userId, categoryId);

        return new ResponseEntity<>(new ResponseDto(ResponseType.SUCCESS), HttpStatus.OK);
    }

    /* 카테고리 수정 */
    @PatchMapping("/category/{categoryId}")
    public ResponseEntity<?> editCategoryName(@PathVariable("categoryId") String categoryId,
                                              @Valid @RequestBody RequestUpdateCategoryNameDto params) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        // 요청한 유저의 카테고리가 맞는지 체크
        if (!categoryService.isExistsCategoryByCategoryIdAndUserId(categoryId, userId))
            throw new BadRequestException("Invalid edit category.");

        // 카테고리명 수정
        categoryService.editCategoryName(categoryId, params.getName());
        return new ResponseEntity<>(new ResponseDto(ResponseType.SUCCESS), HttpStatus.OK);
    }

    /* 포스트 작성 */
    @PostMapping("/post")
    public ResponseEntity<?> createPost(@Valid @RequestBody RequestCreatePostDto params) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        // 카테고리가 비어있을 경우 null
        if (params.getCategoryId().isEmpty())
            params.setCategoryId(null);

        postService.createPost(userId, params);
        return new ResponseEntity<>(new ResponseDto(ResponseType.SUCCESS), HttpStatus.OK);
    }

    /** 포스트 목록 조회 */
    @GetMapping("/post")
    public ResponseEntity<?> getPosts(@RequestParam(value = "userId", required = false) String userId,
                                      @RequestParam(value = "categoryId", required = false) String categoryId,
                                      @PageableDefault(page = 0, size = 5, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        List<PostDto> postList;
        if (userId == null || userId.isEmpty())
            postList = postService.getPostList(pageable);
        else if (categoryId == null || categoryId.isEmpty())
            postList = postService.getPostListByUserId(userId, pageable);
        else
            postList = postService.getPostListByUserIdAndCategoryId(userId, categoryId, pageable);

        return new ResponseEntity<>(ResponseGetPostsDto.builder()
                .response(new ResponseDto(ResponseType.SUCCESS))
                .postList(postList)
                .build(),
                HttpStatus.OK);
    }

    /** 포스트 삭제 */
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("postId") String postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        // 요청한 유저의 게시글이 맞는지 체크
        if (!postService.isExistsPostByPostAndUserId(postId, userId))
            throw new BadRequestException("Invalid delete post.");

        postService.deletePost(postId);
        return new ResponseEntity<>(new ResponseDto(ResponseType.SUCCESS), HttpStatus.OK);
    }

    /**
     * 포스트 수정
     */
    @PatchMapping("/post/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable("postId") String postId,
                                        @RequestBody RequestUpdatePostDto params) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        // 요청한 유저의 게시글이 맞는지 체크
        if (!postService.isExistsPostByPostAndUserId(postId, userId))
            throw new BadRequestException("Invalid delete post.");

        // 카테고리가 비어있을 경우 null
        if (params.getCategoryId().isEmpty())
            params.setCategoryId(null);

        postService.updatePost(postId, params.getTitle(), params.getContent(), params.getCategoryId());
        return new ResponseEntity<>(new ResponseDto(ResponseType.SUCCESS), HttpStatus.OK);
    }
}
