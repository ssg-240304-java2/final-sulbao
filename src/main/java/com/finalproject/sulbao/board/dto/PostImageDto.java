package com.finalproject.sulbao.board.dto;

import com.finalproject.sulbao.board.domain.PostImage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class PostImageDto {

    private Long id;
    private String fileName;

    public static PostImageDto toPostImageDto(PostImage postImage) {
        return PostImageDto.builder()
                .id(postImage.getId())
                .fileName(postImage.getFileName())
                .build();
    }

}