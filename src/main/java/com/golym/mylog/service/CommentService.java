package com.golym.mylog.service;

import com.golym.mylog.model.dto.common.CommentDto;
import com.golym.mylog.model.entity.CommentEntity;
import com.golym.mylog.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public List<CommentDto> getComment(String postId, Pageable pageable) {
        Page<CommentEntity> commentEntityList = commentRepository.findByPost_PostIdAndIsActive(postId, true, pageable);
        return commentEntityList.stream()
                .filter(comment -> comment.getParentComment() == null)
                .map(reply -> new CommentDto(reply, 0))
                .collect(Collectors.toList());
    }

}
