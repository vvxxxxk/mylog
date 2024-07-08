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
@Table(name = "tb_category")
public class CategoryEntity {

    @Id
    @Column(columnDefinition = "CHAR(32)", nullable = false)
    private String categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private UserEntity user;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, columnDefinition = "timestamp default current_timestamp")
    private LocalDateTime createAt;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @ToString.Exclude
    private List<PostEntity> posts;

    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
    }

    @PreRemove
    private void preRemove() {
        for (PostEntity post : posts) {
            post.setCategory(null);
        }
    }

    public void updateCategoryName(String name) {
        this.name = name;
    }

}
