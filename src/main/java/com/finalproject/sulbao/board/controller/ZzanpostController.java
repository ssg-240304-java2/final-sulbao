package com.finalproject.sulbao.board.controller;

import com.finalproject.sulbao.board.domain.Post;
import com.finalproject.sulbao.board.dto.PostDto;
import com.finalproject.sulbao.board.repository.PostRepository;
import com.finalproject.sulbao.board.service.PostService;
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
    private final PostService postService;

    @GetMapping
    public String zzanpostList(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 6);
        Page<Post> postPage = postRepository.findAll(pageable);
        List<PostDto> postDtoList = postPage.getContent().stream().map(PostDto::toPostDto).toList();
        model.addAttribute("posts", postDtoList);
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("currentPage", page);
        return "/board/zzanpost-list";
    }

    @GetMapping("/more")
    @ResponseBody
    public List<PostDto> loadMorePosts(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 6);
        return postRepository.findAll(pageable).getContent().stream().map(PostDto::toPostDto).toList();
    }

    @GetMapping("/new")
    public String newZzanpost() {
        return "/board/new-zzanpost";
    }

    @GetMapping("/{id}")
    public String zzanpost(@PathVariable Long id, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        postService.updateHit(post);
        model.addAttribute("post", post);
        return "/board/zzanpost";
    }

}
