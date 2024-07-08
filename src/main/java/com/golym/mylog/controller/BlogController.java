package com.golym.mylog.controller;

import com.golym.mylog.common.constants.ResponseType;
import com.golym.mylog.common.exception.BadRequestException;
import com.golym.mylog.common.exception.ConflictException;
import com.golym.mylog.model.dto.common.CategoryDto;
import com.golym.mylog.model.dto.common.UserDto;
import com.golym.mylog.model.dto.request.RequestCreateCategoryDto;
import com.golym.mylog.model.dto.request.RequestUpdateCategoryNameDto;
import com.golym.mylog.model.dto.response.ResponseCreateCategoryDto;
import com.golym.mylog.model.dto.response.ResponseDto;
import com.golym.mylog.service.CategoryService;
import com.golym.mylog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BlogController {

    private final UserService userService;
    private final CategoryService categoryService;


    /* 블로그 메인 폼 이동 */
    @GetMapping("/blog")
    public String blogMainForm(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        log.info("blogMainForm()-userId= {}", userId);

        userId = "U-772a6312635540f6abc596a6652014";
//        userId = "U-40af35c0ddba463599d7fd68498efb";

        UserDto user = userService.getUser(userId);
        List<CategoryDto> categoryList = categoryService.getCategoryList(userId);
        log.info("user= {}", user);
        model.addAttribute("user", user);
        model.addAttribute("categoryList", categoryList);


        return "/view/blog/blog";
    }

    /* 글작성 폼 이동 */
    @GetMapping("/blog/write")
    public String editFomr(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        log.info("blogMainForm()-userId= {}", userId);

        userId = "U-772a6312635540f6abc596a6652014";
//        userId = "U-40af35c0ddba463599d7fd68498efb";

        UserDto user = userService.getUser(userId);
        List<CategoryDto> categoryList = categoryService.getCategoryList(userId);
        System.out.println("categoryList = " + categoryList);

        log.info("user= {}", user);
        model.addAttribute("user", user);
        model.addAttribute("categoryList", categoryList);


        return "/view/blog/write";
    }

    /* ToDo 글 작성 */
    @PostMapping("/blog/post")
    public void posting() {

    }

    /* 카테고리 추가 */
    @PostMapping("/api/blog/category")
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
    @DeleteMapping("/api/blog/category/{categoryId}")
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
    @PatchMapping("/api/blog/category/{categoryId}")
    public ResponseEntity<?> editCategoryName(@PathVariable("categoryId") String categoryId,
                                              @Valid @RequestBody RequestUpdateCategoryNameDto params) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        System.out.println("categoryId = " + categoryId);

        // 요청한 유저의 카테고리가 맞는지 체크
        if (!categoryService.isExistsCategoryByCategoryIdAndUserId(categoryId, userId))
            throw new BadRequestException("Invalid edit category.");

        // 카테고리명 수정
        categoryService.editCategoryName(categoryId, params.getName());
        return new ResponseEntity<>(new ResponseDto(ResponseType.SUCCESS), HttpStatus.OK);
    }

}
