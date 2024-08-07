package com.golym.mylog.repository;

import com.golym.mylog.model.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {

    List<CategoryEntity> findAllByUser_UserIdOrderByCreateAt(String userId);
    //List<CategoryEntity> findAllByUser_UserId(String userId);
    boolean existsByUser_UserIdAndName(String userId, String name);
    boolean existsByCategoryIdAndUser_UserId(String categoryId, String userId);

}
