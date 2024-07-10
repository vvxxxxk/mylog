package com.golym.mylog.service;

import com.golym.mylog.common.exception.BadRequestException;
import com.golym.mylog.common.utils.CodeGenerator;
import com.golym.mylog.model.dto.common.PostDto;
import com.golym.mylog.model.dto.request.RequestCreatePostDto;
import com.golym.mylog.model.entity.PostEntity;
import com.golym.mylog.repository.CategoryRepository;
import com.golym.mylog.repository.PostRepository;
import com.golym.mylog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<PostDto> getPostList(Pageable pageable) {
        Page<PostEntity> pagingPostEntityList = postRepository.findAllByIsActive(true, pageable);
        return pagingPostEntityList.stream()
                .map(PostDto::new)
                .collect(Collectors.toList());
    }

    public PostDto getPost(String postId) {
        PostEntity postEntity = postRepository.findByPostIdAndIsActive(postId, true)
                .orElseThrow(() -> new BadRequestException("Not Found Post. postId=" + postId));

        return new PostDto(postEntity);
    }

    public List<PostDto> getPostListByUserId(String userId, Pageable pageable) {
        Page<PostEntity> pagingPostEntityList = postRepository.findAllByUser_UserIdAndIsActive(userId, true, pageable);
        return pagingPostEntityList.stream()
                .map(PostDto::new)
                .collect(Collectors.toList());
    }
}
