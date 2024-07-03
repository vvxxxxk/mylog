package com.golym.mylog.repository;

import com.golym.mylog.model.entity.UserRoleMappingEntity;
import com.golym.mylog.model.entity.id.UserRoleMappingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleMappingRepository extends JpaRepository<UserRoleMappingEntity, UserRoleMappingId> {
}
