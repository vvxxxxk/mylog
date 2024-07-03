package com.golym.mylog.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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


    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
    }
}
