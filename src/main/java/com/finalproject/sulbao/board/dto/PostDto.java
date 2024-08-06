package com.finalproject.sulbao.board.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PostDto {

    private Long id;
    private String title;
    private String content;
    private String writerName;
    private List<PostImageDto> postImageDtoList;
    private Long like;
    private Long hit;

}
