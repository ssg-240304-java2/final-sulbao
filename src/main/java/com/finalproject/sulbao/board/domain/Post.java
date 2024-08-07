package com.finalproject.sulbao.board.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseEntity {

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private final List<Likes> likes = new ArrayList<>();
    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private final List<PostImage> postImages = new ArrayList<>();
    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_category_id")
    private BoardCategory boardCategory;
    private String title;
    @Column(columnDefinition = "longtext")
    private String content;
    private Long hit; // default value setting

    @Builder
    public Post(Member member, BoardCategory boardCategory, String title, String content) {
        this.member = member;
        this.boardCategory = boardCategory;
        this.title = title;
        this.content = content;
    }

    public static Post createPost(
            Member member, BoardCategory boardCategory, String title,
            String content, List<PostImage> postImages) {
        Post post = Post.builder()
                .member(member)
                .boardCategory(boardCategory)
                .title(title)
                .content(content)
                .build();
        if (postImages != null) {
            postImages.forEach(post::setPostImage);
        }
        return post;
    }

    public void setPostImage(PostImage postImage) {
        postImage.setPost(this);
        postImages.add(postImage);
    }

    public void updateHit() {
        hit++;
    }

}
