package com.finalproject.sulbao.board.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String content;

    @Builder
    public Comment(Member member, Post post, String content) {
        this.member = member;
        this.post = post;
        this.content = content;
    }

    public static Comment createComment(String content, Member member, Post post) {
        Comment comment = Comment.builder()
                .content(content)
                .build();
        comment.setMember(member);
        comment.setPost(post);
        return comment;
    }

    private void setMember(Member member) {
        this.member = member;
    }

    private void setPost(Post post) {
        this.post = post;
        post.getComments().add(this);
    }
}