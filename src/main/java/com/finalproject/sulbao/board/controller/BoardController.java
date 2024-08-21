package com.finalproject.sulbao.board.controller;

import com.finalproject.sulbao.board.common.SessionHandler;
import com.finalproject.sulbao.board.dto.PostDto;
import com.finalproject.sulbao.board.dto.UserDto;
import com.finalproject.sulbao.board.repository.PostRepository;
import com.finalproject.sulbao.board.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final SessionHandler sessionHandler;

    @GetMapping("/board/list")
    public String admin(Model model) {
        List<PostDto> posts = postRepository.findAll().stream().map(PostDto::toPostDto).toList();

        model.addAttribute("menu", "board");
        model.addAttribute("submenu", "list");
        model.addAttribute("posts", posts);
        return "board/admin/list";
    }

    @GetMapping("/mypage/board")
    public String user(
            Model model,
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
            ) {
        if (!sessionHandler.isLogin(request)) {
            return "redirect:/login";
        }

        UserDto user = sessionHandler.getUserFromSession(request);
        Pageable pageable = PageRequest.of(page, size);
        Page<PostDto> posts = postService.findByUser(user.getId(), pageable);

        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", posts.getNumber());
        model.addAttribute("totalPages", posts.getTotalPages());
        return "board/user/list";
    }

}
