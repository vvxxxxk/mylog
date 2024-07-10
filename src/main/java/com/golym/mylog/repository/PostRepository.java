package com.golym.mylog.repository;

import com.golym.mylog.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, String> {
    Page<PostEntity> findAllByIsActive(boolean isActive, Pageable pageable);
    Page<PostEntity> findAllByUser_UserIdAndIsActive(String userId, boolean isActive, Pageable pageable);
    Optional<PostEntity> findByPostIdAndIsActive(String postId, boolean isActive);
}
