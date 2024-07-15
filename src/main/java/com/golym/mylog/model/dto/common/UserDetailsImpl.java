package com.golym.mylog.model.dto.common;

import com.golym.mylog.model.entity.RoleEntity;
import com.golym.mylog.model.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private final UserEntity user;
    private final RoleEntity role;

    public UserDetailsImpl(UserEntity user, RoleEntity role) {
        this.user = user;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return role.getRoleName().name();
            }
        });

        return collection;
    }

    @Override
    public String getUsername() {
        return user.getUserId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }

    public boolean isActive() {
        return user.isActive();
    }
}
