package com.finalproject.sulbao.board.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.*;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private BoardCategory boardCategory;

    private String title;

    private String content;

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private List<PostImage> postImages = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(Member member, BoardCategory boardCategory, String title, String content) {
        this.member = member;
        this.boardCategory = boardCategory;
        this.title = title;
        this.content = content;
    }

    public static Post createPost(Member member, BoardCategory boardCategory, String title, String content, List<PostImage> postImages) {
        Post post = Post.builder()
                .member(member)
                .boardCategory(boardCategory)
                .title(title)
                .content(content)
                .build();
        for (PostImage postImage : postImages) {
            post.setPostImage(postImage);
        }
        return post;
    }

    public void setPostImage(PostImage postImage) {
        postImage.setPost(this);
        postImages.add(postImage);
    }

}
