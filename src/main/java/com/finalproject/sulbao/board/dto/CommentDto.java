package com.finalproject.sulbao.board.dto;

import com.finalproject.sulbao.board.domain.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
public class CommentDto {

    private Long id;
    private MemberDto memberDto;
    //    private PostDto postDto;
    private String content;
    private LocalDateTime createdAt;

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .memberDto(MemberDto.toMemberDto(comment.getMember()))
//                .postDto(PostDto.toPostDto(comment.getPost()))
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

}
