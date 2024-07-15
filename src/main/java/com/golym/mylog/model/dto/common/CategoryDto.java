package com.golym.mylog.model.dto.common;

import com.golym.mylog.model.entity.CategoryEntity;
import com.golym.mylog.model.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    private String categoryId;
    private String name;
    private int postCount;

    public CategoryDto(CategoryEntity categoryEntity) {
        this.categoryId = categoryEntity.getCategoryId();
        this.name = categoryEntity.getName();
        this.postCount = categoryEntity.getPosts().isEmpty() ? 0 : (int) categoryEntity.getPosts().stream()
                                                                            .filter(PostEntity::isActive)
                                                                            .count();
    }
}
