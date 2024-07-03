package com.golym.mylog.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_post")
public class PostEntity {

    @Id
    @Column(columnDefinition = "CHAR(32)", nullable = false)
    private String postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private UserEntity user;

    @Column(nullable = true, length = 255)
    private String thumbnail;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "view_count", nullable = false, columnDefinition = "int default 0")
    private int viewCount;

    @Column(nullable = false, columnDefinition = "timestamp default current_timestamp")
    private LocalDateTime createAt;

    @Column(nullable = false, columnDefinition = "timestamp default current_timestamp on update current_timestamp")
    private LocalDateTime updateAt;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean isActive;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<CommentEntity> comments;


    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }
}
