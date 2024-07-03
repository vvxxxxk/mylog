package com.golym.mylog.model.entity;

import com.golym.mylog.common.constants.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_role")
public class RoleEntity {

    @Id
    @Column(columnDefinition = "CHAR(5)", nullable = false)
    private String roleId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private RoleType roleName;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<UserRoleMappingEntity> userRoleMappings;
}
