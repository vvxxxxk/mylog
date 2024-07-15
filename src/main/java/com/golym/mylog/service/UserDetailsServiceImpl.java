package com.golym.mylog.service;

import com.golym.mylog.model.dto.common.UserDetailsImpl;
import com.golym.mylog.model.entity.RoleEntity;
import com.golym.mylog.model.entity.UserEntity;
import com.golym.mylog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails userDetails = userRepository.findByEmail(email)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("Not found email: " + email));
        return userDetails;
    }

    private UserDetailsImpl createUserDetails(UserEntity user) {
        RoleEntity role = user.getUserRoleMappings().stream()
                .map(userRoleMapping -> userRoleMapping.getRole())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User has no roles assigned"));

        return new UserDetailsImpl(user, role);
    }
}
