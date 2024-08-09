package com.finalproject.sulbao.board.domain;

import com.finalproject.sulbao.common.entity.BaseEntity;
import com.finalproject.sulbao.login.model.entity.Login;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tbl_like")
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private Login login;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Like(Login login, Post post) {
        this.login = login;
        this.post = post;
    }

    public static Like createLike(Login login, Post post) {
        Like like = Like.builder()
                .login(login)
                .build();
        like.setPost(post);
        return like;
    }

    private void setPost(Post post) {
        this.post = post;
        post.getLikes().add(this);
    }

}
