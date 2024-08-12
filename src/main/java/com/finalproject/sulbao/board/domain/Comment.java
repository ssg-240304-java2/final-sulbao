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
@Table(name = "tbl_comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private Login login;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String content;

    @Builder
    public Comment(Login login, Post post, String content) {
        this.login = login;
        this.post = post;
        this.content = content;
    }

    public static Comment createComment(String content, Login login, Post post) {
        Comment comment = Comment.builder()
                .content(content)
                .build();
        comment.setLogin(login);
        comment.setPost(post);
        return comment;
    }

    private void setLogin(Login login) {
        this.login = login;
    }

    private void setPost(Post post) {
        this.post = post;
        post.getComments().add(this);
    }
}