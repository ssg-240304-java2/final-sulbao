package com.finalproject.sulbao.board.controller;

import com.finalproject.sulbao.board.dto.PostDto;
import com.finalproject.sulbao.board.repository.PostRepository;
import com.finalproject.sulbao.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/temp")
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;

    @GetMapping
    public String temp(Model model) {
        List<PostDto> posts = postRepository.findAll().stream().map(PostDto::toPostDto).toList();
        model.addAttribute("posts", posts);
        return "board/admin/list";
    }

}
