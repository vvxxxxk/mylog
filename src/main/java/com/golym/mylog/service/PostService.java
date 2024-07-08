package com.golym.mylog.service;

import com.golym.mylog.common.utils.CodeGenerator;
import com.golym.mylog.model.dto.request.RequestCreatePostDto;
import com.golym.mylog.model.entity.PostEntity;
import com.golym.mylog.repository.CategoryRepository;
import com.golym.mylog.repository.PostRepository;
import com.golym.mylog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void createPost(String userId, RequestCreatePostDto params) {

        PostEntity postEntity = PostEntity.builder()
                .postId(CodeGenerator.generateID("P"))
                .category(params.getCategoryId() == null ? null : categoryRepository.findById(params.getCategoryId()).get())
                .user(userRepository.findById(userId).get())
                .title(params.getTitle())
                .content(params.getContent())
                .isActive(true)
                .build();

        postRepository.save(postEntity);
    }
}
