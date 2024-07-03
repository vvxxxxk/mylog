package com.golym.mylog.model.entity;

import com.golym.mylog.model.entity.id.UserRoleMappingId;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_user_role_mapping")
public class UserRoleMappingEntity {

    @EmbeddedId
    private UserRoleMappingId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private UserEntity user;

    @MapsId("roleId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @ToString.Exclude
    private RoleEntity role;
}
