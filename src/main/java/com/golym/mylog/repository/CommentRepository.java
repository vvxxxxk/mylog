package com.golym.mylog.repository;

import com.golym.mylog.model.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, String> {
    Page<CommentEntity> findByPost_PostIdAndIsActive(String postId, boolean isActive, Pageable pageable);
}
