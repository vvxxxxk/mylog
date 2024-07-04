package com.golym.mylog.service;

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("Not found email: " + username));
    }

    private UserDetails createUserDetails(UserEntity user) {
        List<GrantedAuthority> authorities = user.getUserRoleMappings().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().getRoleName().name()))
                .collect(Collectors.toList());

        return new User(String.valueOf(user.getUserId()), user.getPassword(), authorities);
    }
}
