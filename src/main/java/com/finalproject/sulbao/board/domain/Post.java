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

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "longtext")
    private String content;

    @ColumnDefault("0")
    private Long hit;

    private String thumbnail;

    @ElementCollection
    @CollectionTable(name = "tbl_post_tag", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "tag")
    private final List<String> tags = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private Login login;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_category_id")
    private BoardCategory boardCategory;

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private final List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private final List<PostImage> postImages = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(Login login, BoardCategory boardCategory, String title, String content, String thumbnail) {
        this.login = login;
        this.boardCategory = boardCategory;
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
    }

    public static Post createPost(Login login, BoardCategory boardCategory, String title, String content, String thumbnail) {
        return Post.builder()
                .login(login)
                .boardCategory(boardCategory)
                .title(title)
                .content(content)
                .thumbnail(thumbnail)
                .build();
    }

    public static Post createPost(Login login, BoardCategory boardCategory, String title, String content, String thumbnail, List<PostImage> postImages) {
        Post post = createPost(login, boardCategory, title, content, thumbnail);
        post.setPostImages(postImages);
        return post;
    }

    public static Post createPost(Login login, BoardCategory boardCategory, String title, String content, String thumbnail, List<PostImage> postImages, List<String> tags) {
        Post post = createPost(login, boardCategory, title, content, thumbnail, postImages);
        if (tags != null) {
            post.setTags(tags);
        }
        return post;
    }

    private void setTags(List<String> tags) {
        this.tags.addAll(tags);
    }

    private void updateTags(List<String> tags) {
        this.tags.clear();
        this.tags.addAll(tags);
    }

    private void setPostImages(List<PostImage> postImages) {
        postImages.forEach(postImage -> {
            postImage.setPost(this);
            this.postImages.add(postImage);
        });
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

    public void update(String thumbnail, String title, String content, List<String> tags, List<PostImage> postImages) {
        if (thumbnail != null) {
            this.thumbnail = thumbnail;
        }

        this.title = title;
        this.content = content;
        updatePostImages(postImages);
        updateTags(tags);
    }

    private void updatePostImages(List<PostImage> postImages) {
        for (int i = 0; i < postImages.size(); i++) {
            PostImage postImage = postImages.get(i);

            if (i >= this.postImages.size()) {
                this.postImages.add(postImage);
                postImage.setPost(this);
            }

            if (postImage != null) {
                this.postImages.get(i).setFileName(postImage.getFileName());
                postImage.setPost(this);
            }
        }
    }

}
