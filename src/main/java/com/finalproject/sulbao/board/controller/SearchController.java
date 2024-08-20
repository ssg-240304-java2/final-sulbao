package com.finalproject.sulbao.board.controller;

import com.finalproject.sulbao.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/search")
@RequiredArgsConstructor
@Controller
public class SearchController {

    private final PostService postService;

    @GetMapping
    public String search(String keyword, Model model) {
//        List<PostDto> zzanfeeds = postService.findByKeyword(keyword, ZZANFEED_ID);
//        List<PostDto> zzanposts = postService.findByKeyword(keyword, ZZANPOST_ID);

//        model.addAttribute("zzanfeeds", zzanfeeds);
//        model.addAttribute("zzanposts", zzanposts);
        return "/board/search-results";
    }

}
