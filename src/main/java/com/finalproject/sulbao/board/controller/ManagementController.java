package com.finalproject.sulbao.board.controller;

import com.finalproject.sulbao.board.common.SessionHandler;
import com.finalproject.sulbao.board.dto.AdminPostSearchRequestDto;
import com.finalproject.sulbao.board.dto.PostDto;
import com.finalproject.sulbao.board.dto.UserDto;
import com.finalproject.sulbao.board.repository.PostRepository;
import com.finalproject.sulbao.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ManagementController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final SessionHandler sessionHandler;

    @GetMapping("/mypage/board")
    public String mypageBoard(
            Model model,
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        if (!sessionHandler.isLogin(authentication)) {
            return "redirect:/login";
        }

        UserDto user = sessionHandler.getUser(authentication);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<PostDto> posts = postService.findByUser(user.getId(), pageable);

        int currentPage = posts.getNumber();
        int totalPages = posts.getTotalPages();
        int currentGroup = currentPage / 10;
        int startPage = currentGroup * 10 + 1;
        int endPage = Math.min(startPage + 9, totalPages);

        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("hasPreviousGroup", currentGroup > 0);
        model.addAttribute("hasNextGroup", endPage < totalPages);
        model.addAttribute("menu","board");
        return "board/user/list";
    }

    @GetMapping("/board/list")
    public String boardList(Model model) {
        List<PostDto> posts = postRepository.findAll().stream().map(PostDto::toPostDto).toList();
        model.addAttribute("menu", "board");
        model.addAttribute("submenu", "list");
        model.addAttribute("posts", posts);
        return "board/admin/list";
    }

    @GetMapping("/board/search")
    public String boardSearch(AdminPostSearchRequestDto request, Model model) {
        List<PostDto> posts = postService.search(request);
        model.addAttribute("menu", "board");
        model.addAttribute("submenu", "list");
        model.addAttribute("posts", posts);
        return "board/admin/list";
    }

    @PostMapping("/board/delete")
    public ResponseEntity<Void> delete(@RequestBody List<Long> postIdList) {
        postIdList.forEach(postService::delete);
        return ResponseEntity.ok().build();
    }

}
