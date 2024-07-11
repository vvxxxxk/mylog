package com.golym.mylog.service;

import com.golym.mylog.common.exception.BadRequestException;
import com.golym.mylog.common.utils.CodeGenerator;
import com.golym.mylog.model.dto.common.CommentDto;
import com.golym.mylog.model.entity.CommentEntity;
import com.golym.mylog.repository.CommentRepository;
import com.golym.mylog.repository.PostRepository;
import com.golym.mylog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<CommentDto> getComment(String postId) {
        List<CommentEntity> commentEntityList = commentRepository.findByPost_PostIdAndIsActiveOrderByCreateAt(postId, true);
        return commentEntityList.stream()
                .filter(comment -> comment.getParentComment() == null)
                .map(CommentDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void createComment(String userId, String postId, String content) {

        commentRepository.save(CommentEntity.builder()
                .commentId(CodeGenerator.generateID("CM"))
                .post(postRepository.findById(postId)
                        .orElseThrow(() -> new BadRequestException("Not found post. postId=" + postId)))
                .user(userRepository.findById(userId)
                        .orElseThrow(() -> new BadRequestException("Not found user. userId" + userId)))
                .parentComment(null)
                .content(content)
                .depth(0)
                .isActive(true)
                .build());
    }

    public boolean isExistsCommentIdAndUser_UserId(String commentId, String userId) {
        return commentRepository.existsByCommentIdAndUser_UserIdAndIsActive(commentId, userId, true);
    }

    @Transactional
    public void updateComment(String commentId, String content) {

        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new BadRequestException("Not found comment. commentId=" + commentId));
        commentEntity.updateComment(content);
        commentRepository.save(commentEntity);
    }

    @Transactional
    public void deleteComment(String commentId) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new BadRequestException("Not found comment. commentId=" + commentId));
        commentEntity.deleteComment();
        commentRepository.save(commentEntity);
    }

    @Transactional
    public void createReply(String userId, String parentCommentId, String postId, String content) {

        commentRepository.save(CommentEntity.builder()
                .commentId(CodeGenerator.generateID("CM"))
                .post(postRepository.findById(postId)
                        .orElseThrow(() -> new BadRequestException("Not found post. postId=" + postId)))
                .user(userRepository.findById(userId)
                        .orElseThrow(() -> new BadRequestException("Not found user. userId" + userId)))
                .parentComment(commentRepository.findById(parentCommentId)
                        .orElseThrow(() -> new BadRequestException("Not found comment. commentId" + parentCommentId)))
                .content(content)
                .depth(1)
                .isActive(true)
                .build());
    }
}
