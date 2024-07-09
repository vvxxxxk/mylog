package com.golym.mylog.model.dto.common;

import com.golym.mylog.common.utils.MarkdownConvertor;
import com.golym.mylog.model.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {

    private String postId;
    private String writer;
    private String category;
    private String title;
    private String content;
    private String summary;
    private int viewCount;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private int commentCount;


    public PostDto(PostEntity postEntity) {
        this.postId = postEntity.getPostId();
        this.writer = postEntity.getUser().getNickname();
        this.category = postEntity.getCategory() == null ? null : postEntity.getCategory().getName();
        this.title = postEntity.getTitle();
        this.content = postEntity.getContent();
        this.summary = summarizeContent(MarkdownConvertor.convertMarkdownToPlainText(removeImageUrls(postEntity.getContent())));
        this.viewCount = postEntity.getViewCount();
        this.createAt = postEntity.getCreateAt();
        this.updateAt = postEntity.getUpdateAt();
        this.commentCount = postEntity.getComments().isEmpty() ? 0 : postEntity.getComments().size();
    }

    // content 이미지 url 제거
    private String removeImageUrls(String content) {
        return content.replaceAll("!\\[.*?\\]\\(.*?\\)", "");
    }

    // content summarize
    private String summarizeContent(String content) {
        int summaryLength = 100; // 원하는 요약 길이
        if (content.length() > summaryLength) {
            return content.substring(0, summaryLength) + "...";
        } else {
            return content;
        }
    }
}
