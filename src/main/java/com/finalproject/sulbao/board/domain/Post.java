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
    private final List<Like> likes = new ArrayList<>();
    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private final List<PostImage> postImages = new ArrayList<>();
    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "tbl_post_tag", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "tag")
    private final List<String> tags = new ArrayList<>();
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

    public static Post createPost(Login login, BoardCategory boardCategory, String title, String content, PostImage postImage) {
        Post post = Post.builder()
                .login(login)
                .boardCategory(boardCategory)
                .title(title)
                .content(content)
                .build();
        post.setPostImage(postImage);
        return post;
    }

    public void setPostImage(PostImage postImage) {
        postImage.setPost(this);
        if (postImages.isEmpty()) {
            postImages.add(postImage);
        } else {
            postImages.set(0, postImage);
        }
    }

    public void updateHit() {
        hit++;
    }

    public void update(String title, String content, String thumbnailFileName) {
        this.title = title;
        this.content = content;
        if (thumbnailFileName != null) {
            PostImage postImage = PostImage.createPostImage(thumbnailFileName);
            setPostImage(postImage);
        }
    }
}
