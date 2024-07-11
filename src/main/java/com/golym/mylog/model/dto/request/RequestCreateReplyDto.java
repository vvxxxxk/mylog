package com.golym.mylog.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestCreateReplyDto {

    private String content;
    private String parentCommentId;
    private String postId;
}
