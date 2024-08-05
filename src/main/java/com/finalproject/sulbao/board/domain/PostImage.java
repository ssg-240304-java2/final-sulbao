package com.finalproject.sulbao.board.domain;

import com.finalproject.sulbao.board.repository.PostImageRepository;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
public class PostImage {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String fileName;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public PostImage(String fileName, Post post) {
        this.fileName = fileName;
        this.post = post;
    }

    public static PostImage createPostImage(String fileName) {
        return PostImage.builder()
                .fileName(fileName)
                .build();
    }

    public void setPost(Post post) {
        this.post = post;
    }

}