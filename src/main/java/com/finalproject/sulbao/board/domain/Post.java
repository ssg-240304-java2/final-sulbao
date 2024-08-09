package com.finalproject.sulbao.board.domain;

import com.finalproject.sulbao.common.entity.BaseEntity;
import com.finalproject.sulbao.login.model.entity.Login;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tbl_post")
@DynamicInsert
public class Post extends BaseEntity {

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private final List<Like> like = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private final List<PostImage> postImages = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private Login login;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_category_id")
    private BoardCategory boardCategory;

    private String title;

    @Column(columnDefinition = "longtext")
    private String content;

    @ColumnDefault("0")
    private Long hit;

    @Builder
    public Post(Login login, BoardCategory boardCategory, String title, String content) {
        this.login = login;
        this.boardCategory = boardCategory;
        this.title = title;
        this.content = content;
    }

    public static Post createPost(
            Login login, BoardCategory boardCategory, String title,
            String content, List<PostImage> postImages) {
        Post post = Post.builder()
                .login(login)
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