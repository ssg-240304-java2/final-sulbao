package com.finalproject.sulbao.board.dto;

import com.finalproject.sulbao.board.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

import static com.finalproject.sulbao.board.common.BoardCategoryConstants.ZZANFEED_ID;

@Getter
@Setter
@ToString
@Builder
public class PostDto {

    private Long id;
    private String title;
    private String content;
    private List<PostImageDto> postImageDtoList;
    private Long like;
    private Long hit;
    private UserDto userDto;
    private List<CommentDto> commentDtoList;
    private List<String> tags;
    private String thumbnail;
    private String categoryNameEn;
    private LocalDateTime createdAt;
    private String categoryNameKr;

    public static PostDto toPostDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .userDto(UserDto.toUserDto(post.getLogin()))
                .postImageDtoList(post.getPostImages().stream().map(PostImageDto::toPostImageDto).toList())
                .like((long) post.getLikes().size())
                .hit(post.getHit())
                .commentDtoList(post.getComments().stream().map(CommentDto::toCommentDto).toList())
                .tags(post.getTags())
                .thumbnail(post.getThumbnail())
                .categoryNameEn(post.getBoardCategory().getName())
                .categoryNameKr(post.getBoardCategory().getId().equals(ZZANFEED_ID) ? "짠피드" : "술포스트")
                .createdAt(post.getCreatedAt())
                .build();
    }

}
