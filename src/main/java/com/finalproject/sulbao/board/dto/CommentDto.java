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
    private UserDto userDto;
    private String content;
    private LocalDateTime createdAt;

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .userDto(UserDto.toUserDto(comment.getLogin()))
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

}