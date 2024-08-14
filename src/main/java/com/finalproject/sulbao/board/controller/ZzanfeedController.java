package com.finalproject.sulbao.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/zzanfeeds")
public class ZzanfeedController {

    @GetMapping("/new")
    public String newPost() {
        return "/board/zzanfeed/new";
    }

}
