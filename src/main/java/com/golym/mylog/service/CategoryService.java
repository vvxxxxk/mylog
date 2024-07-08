package com.golym.mylog.service;

import com.golym.mylog.common.exception.BadRequestException;
import com.golym.mylog.common.utils.CodeGenerator;
import com.golym.mylog.model.dto.common.CategoryDto;
import com.golym.mylog.model.entity.CategoryEntity;
import com.golym.mylog.repository.CategoryRepository;
import com.golym.mylog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<CategoryDto> getCategoryList(String userId) {
        List<CategoryEntity> categoryEntityList = categoryRepository.findAllByUser_UserIdOrderByCreateAt(userId);
        return categoryEntityList.stream()
                .map(categoryEntity -> modelMapper.map(categoryEntity, CategoryDto.class))
                .collect(Collectors.toList());
    }

    public boolean isExistsCategoryByUserIdAndName(String userId, String name) {
        return categoryRepository.existsByUser_UserIdAndName(userId, name);
    }

    public boolean isExistsCategoryByCategoryIdAndUserId(String categoryId, String userId) {
        return categoryRepository.existsByCategoryIdAndUser_UserId(categoryId, userId);
    }

    @Transactional
    public CategoryDto createCategory(String userId, String name) {
        CategoryEntity categoryEntity = CategoryEntity.builder()
                .categoryId(CodeGenerator.generateID("CG"))
                .user(userRepository.findById(userId)
                        .orElseThrow(() -> new BadRequestException("Invalid userId. userId=" + userId)))
                .name(name)
                .build();

        categoryRepository.save(categoryEntity);

        return CategoryDto.builder()
                .categoryId(categoryEntity.getCategoryId())
                .name(categoryEntity.getName())
                .build();
    }

    @Transactional
    public void deleteCategory(String userId, String categoryId) {

        categoryRepository.deleteById(categoryId);
    }

    public void editCategoryName(String categoryId, String name) {

        CategoryEntity categoryEntity = categoryRepository.findById(categoryId).get();
        categoryEntity.updateCategoryName(name);
        categoryRepository.save(categoryEntity);
    }
}
