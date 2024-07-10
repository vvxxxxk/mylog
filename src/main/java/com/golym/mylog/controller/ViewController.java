package com.golym.mylog.controller;

import com.golym.mylog.model.dto.common.CategoryDto;
import com.golym.mylog.model.dto.common.CommentDto;
import com.golym.mylog.model.dto.common.PostDto;
import com.golym.mylog.model.dto.common.UserDto;
import com.golym.mylog.service.CategoryService;
import com.golym.mylog.service.CommentService;
import com.golym.mylog.service.PostService;
import com.golym.mylog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ViewController {

    private final UserService userService;
    private final PostService postService;
    private final CategoryService categoryService;
    private final CommentService commentService;

    /** 에러 페이지 **/
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "view/error-page";
    }

    /** 에러 페이지 **/
    @GetMapping("/unauthorized")
    public String unauthorized() {
        return "view/error-page";
    }

    /** 회원가입 페이지 **/
    @GetMapping("/signup")
    public String signupForm() {
        return "/view/user/signup";
    }

    /** 메인 페이지 **/
    @GetMapping("/main")
    public String blogMainForm(Model model,
                               @PageableDefault(page = 0, size = 5, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        UserDto user = new UserDto();
        if (!userId.equals("anonymousUser"))
            user = userService.getUser(userId);

        List<PostDto> postList = postService.getPostList(pageable);
        model.addAttribute("user", user);
        model.addAttribute("postList", postList);
        return "/view/blog/main";
    }

    /**
     * 게시글 조회
     **/
    @GetMapping("/blog/post/{postId}")
    public String postForm(Model model,
                           @PathVariable("postId") String postId,
                           @PageableDefault(page = 0, size = 5, sort = "createAt", direction = Sort.Direction.ASC) Pageable pageable) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        UserDto user = new UserDto();
        if (!userId.equals("anonymousUser"))
            user = userService.getUser(userId);

        PostDto post = postService.getPost(postId);
        List<CommentDto> commentList = commentService.getComment(postId, pageable);

        model.addAttribute("user", user);
        model.addAttribute("post", post);
        model.addAttribute("commentList", commentList);

        return "/view/blog/post";
    }

    /** Mylog 페이지 **/
    @GetMapping("/blog/{userId}")
    public String blogForm(Model model,
                           @PathVariable("userId") String blogerId,
                           @PageableDefault(page = 0, size = 5, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        UserDto user = new UserDto();
        if (!userId.equals("anonymousUser"))
            user = userService.getUser(userId);

        //UserDto user = userService.getUser(userId);
        UserDto bloger = userService.getUser(blogerId);
        List<CategoryDto> categoryList = categoryService.getCategoryList(blogerId);
        List<PostDto> postList = postService.getPostListByUserId(blogerId, pageable);
        model.addAttribute("user", user);
        model.addAttribute("bloger", bloger);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("postList", postList);
        return "/view/blog/blog";
    }

    /** 글작성 페이지 **/
    @GetMapping("/blog/{userId}/write")
    public String editFomr(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        UserDto user = userService.getUser(userId);
        List<CategoryDto> categoryList = categoryService.getCategoryList(userId);
        model.addAttribute("user", user);
        model.addAttribute("categoryList", categoryList);
        return "/view/blog/write";
    }
}
