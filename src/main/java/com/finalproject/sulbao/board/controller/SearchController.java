package com.finalproject.sulbao.board.controller;

import com.finalproject.sulbao.board.dto.PostDto;
import com.finalproject.sulbao.board.service.PostService;
import com.finalproject.sulbao.product.model.dto.ProductComparisonDTO;
import com.finalproject.sulbao.product.model.dto.ProductDTO;
import com.finalproject.sulbao.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.finalproject.sulbao.board.common.BoardCategoryConstants.ZZANFEED_ID;
import static com.finalproject.sulbao.board.common.BoardCategoryConstants.ZZANPOST_ID;

@RequestMapping("/search")
@RequiredArgsConstructor
@Controller
public class SearchController {

    private final PostService postService;
    private final ProductService productService;

    @GetMapping
    public String search(String keyword, Model model) {

        //상품정보
        List<ProductComparisonDTO> productList = productService.findByProductKeyword(keyword,4);
        model.addAttribute("productList", productList);

        List<PostDto> zzanfeeds = postService.findByCategoryAndKeyword(keyword, ZZANFEED_ID);
        List<PostDto> zzanposts = postService.findByCategoryAndKeyword(keyword, ZZANPOST_ID);

        model.addAttribute("keyword", keyword);
        model.addAttribute("zzanfeeds", zzanfeeds);
        model.addAttribute("zzanposts", zzanposts);
        return "board/search-results";
    }

}
