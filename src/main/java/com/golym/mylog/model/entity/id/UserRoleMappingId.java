package com.golym.mylog.model.entity.id;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class UserRoleMappingId implements Serializable {

    private String roleId;
    private String userId;
}
