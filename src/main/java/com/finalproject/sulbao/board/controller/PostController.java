package com.finalproject.sulbao.board.controller;

import com.finalproject.sulbao.board.common.SessionHandler;
import com.finalproject.sulbao.board.dto.PostDto;
import com.finalproject.sulbao.board.dto.UserDto;
import com.finalproject.sulbao.board.repository.PostRepository;
import com.finalproject.sulbao.board.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final SessionHandler sessionHandler;

    @GetMapping("/admin/board")
    public String admin(Model model) {
        List<PostDto> posts = postRepository.findAll().stream().map(PostDto::toPostDto).toList();
        model.addAttribute("posts", posts);
        return "board/admin/list";
    }

    @GetMapping("/mypage/board")
    public String user(Model model, HttpServletRequest request) {
        if (!sessionHandler.isLogin(request)) {
            return "redirect:/login";
        }

        UserDto user = sessionHandler.getUserFromSession(request);
        List<PostDto> posts = postService.findByUser(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        return "board/user/list";
    }

}
