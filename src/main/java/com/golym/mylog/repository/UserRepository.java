package com.golym.mylog.repository;

import com.golym.mylog.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUserIdAndIsActive(String userId, boolean isActive);
}
