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
import org.springframework.web.bind.annotation.ModelAttribute;
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
        UserDto bloger = new UserDto();
        if (!userId.equals("anonymousUser"))
            user = userService.getUser(userId);

        List<PostDto> postList = postService.getPostList(pageable);
        model.addAttribute("user", user);
        model.addAttribute("bloger", bloger);
        model.addAttribute("postList", postList);
        return "/view/main";
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
        UserDto bloger = userService.getUser(post.getUserId());
        List<CommentDto> commentList = commentService.getComment(postId);


        System.out.println("commentList = " + commentList);

        model.addAttribute("user", user);
        model.addAttribute("bloger", bloger);
        model.addAttribute("post", post);
        model.addAttribute("commentList", commentList);

        return "/view/blog/post";
    }

    /** Mylog 페이지 **/
    @GetMapping("/blog/{userId}")
    public String blogForm(Model model,
                           @PathVariable("userId") String blogerId,
                           @RequestParam(value = "categoryId", required = false) String selectCategoryId,
                           @PageableDefault(page = 0, size = 5, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        UserDto user = new UserDto();
        if (!userId.equals("anonymousUser"))
            user = userService.getUser(userId);

        List<PostDto> postList;
        if (selectCategoryId == null)
            postList = postService.getPostListByUserId(blogerId, pageable);
        else
            postList = postService.getPostListByUserIdAndCategoryId(blogerId, selectCategoryId, pageable);

        UserDto bloger = userService.getUser(blogerId);
        List<CategoryDto> categoryList = categoryService.getCategoryList(blogerId);
        int totalPostCount = postService.getTotalPostCountByUserId(blogerId);
        model.addAttribute("user", user);
        model.addAttribute("bloger", bloger);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("postList", postList);
        model.addAttribute("totalPostCount", totalPostCount);
        model.addAttribute("selectCategoryId", selectCategoryId);
        return "/view/blog/blog";
    }

    /** 게시글 작성 페이지 **/
    @GetMapping("/blog/{userId}/write")
    public String writeForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        UserDto user = userService.getUser(userId);
        List<CategoryDto> categoryList = categoryService.getCategoryList(userId);
        int totalPostCount = postService.getTotalPostCountByUserId(userId);
        model.addAttribute("user", user);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("totalPostCount", totalPostCount);
        return "/view/blog/write";
    }

    /**
     * 게시글 수정 페이지
     */
    @GetMapping("/blog/edit/{postId}")
    public String editForm(Model model,
                           @PathVariable("postId") String postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        UserDto user = userService.getUser(userId);
        PostDto post = postService.getPost(postId);
        List<CategoryDto> categoryList = categoryService.getCategoryList(userId);
        int totalPostCount = postService.getTotalPostCountByUserId(userId);
        model.addAttribute("user", user);
        model.addAttribute("post", post);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("totalPostCount", totalPostCount);
        return "/view/blog/edit";
    }
}
