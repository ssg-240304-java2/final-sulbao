package com.finalproject.sulbao.board.controller;

import com.finalproject.sulbao.board.domain.Post;
import com.finalproject.sulbao.board.domain.PostImage;
import com.finalproject.sulbao.board.dto.PostDto;
import com.finalproject.sulbao.board.dto.PostImageDto;
import com.finalproject.sulbao.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/zzanposts")
@RequiredArgsConstructor
public class ZzanpostController {

    private final PostRepository postRepository;

    @GetMapping
    public String zzanpostList(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 6);
        Page<Post> postPage = postRepository.findAll(pageable);
        List<PostDto> postDtoList = postPage.getContent().stream().map(this::toPostDTO).toList();
        model.addAttribute("posts", postDtoList);
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("currentPage", page);
        return "/board/zzanpost-list";
    }

    @GetMapping("/more")
    @ResponseBody
    public List<PostDto> loadMorePosts(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 6);
        List<Post> posts = postRepository.findAll(pageable).getContent();
        return posts.stream().map(this::toPostDTO).toList();
    }

    private PostDto toPostDTO(Post post) {
        PostDto postDTO = new PostDto();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        postDTO.setWriterName(post.getMember().getName());
        postDTO.setPostImageDtoList(post.getPostImages().stream().map(this::toPostImageDTO).toList());
        postDTO.setLike((long) post.getLikes().size());
        postDTO.setHit(post.getHit());
        return postDTO;
    }

    private PostImageDto toPostImageDTO(PostImage postImage) {
        PostImageDto postImageDTO = new PostImageDto();
        postImageDTO.setId(postImage.getId());
        postImageDTO.setFileName(postImage.getFileName());
        return postImageDTO;
    }

    @GetMapping("/new")
    public String newZzanpost() {
        return "/board/new-zzanpost";
    }

    @GetMapping("/{id}")
    public String zzanpost(@PathVariable Long id, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        model.addAttribute("post", post);
        return "/board/zzanpost";
    }

}
