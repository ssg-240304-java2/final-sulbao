package com.finalproject.sulbao.board.dto;

import com.finalproject.sulbao.board.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class PostDto {

    private Long id;
    private String title;
    private String content;
    private String writerName;
    private List<PostImageDto> postImageDtoList;
    private Long like;
    private Long hit;

    public static PostDto toPostDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .writerName(post.getMember().getName())
                .postImageDtoList(post.getPostImages().stream().map(PostImageDto::toPostImageDto).toList())
                .like((long) post.getLikes().size())
                .hit(post.getHit())
                .build();
    }

}
