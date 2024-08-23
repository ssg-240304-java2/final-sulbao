package com.finalproject.sulbao;

import com.finalproject.sulbao.board.dto.PostDto;
import com.finalproject.sulbao.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static com.finalproject.sulbao.board.common.BoardCategoryConstants.ZZANFEED_ID;
import static com.finalproject.sulbao.board.common.BoardCategoryConstants.ZZANPOST_ID;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;

    @GetMapping("/")
    public String index(Model model) {
        List<PostDto> zzanfeeds = postService.getWeeklyposts(ZZANFEED_ID);
        List<PostDto> zzanposts = postService.getWeeklyposts(ZZANPOST_ID);
        model.addAttribute("zzanfeeds", zzanfeeds);
        model.addAttribute("zzanposts", zzanposts);
        return "index";
    }

}
