package com.golym.mylog.model.dto.common;

import com.golym.mylog.model.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {

    private String commentId;
    private String postId;
    private String userId;
    private String nickname;
    private int depth;
    private String parentCommentId;
    private List<CommentDto> replies;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public CommentDto(CommentEntity commentEntity, int depth) {
        this.commentId = commentEntity.getCommentId();
        this.postId = commentEntity.getPost().getPostId();
        this.userId = commentEntity.getUser().getUserId();
        this.nickname = commentEntity.getUser().getNickname();
        this.depth = commentEntity.getDepth();
        this.parentCommentId = commentEntity.getParentComment() == null ? null : commentEntity.getParentComment().getCommentId();
        this.replies = commentEntity.getReplies().stream()
                .sorted((a, b) -> a.getCreateAt().compareTo(b.getCreateAt()))
                .map(reply -> new CommentDto(reply, depth + 1))
                .collect(Collectors.toList());
        this.content = commentEntity.getContent();
        this.createAt = commentEntity.getCreateAt();
        this.updateAt = commentEntity.getUpdateAt();
    }
}
