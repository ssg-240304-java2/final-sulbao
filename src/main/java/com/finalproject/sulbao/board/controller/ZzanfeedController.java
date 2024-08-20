package com.finalproject.sulbao.board.controller;

import com.finalproject.sulbao.board.common.SessionHandler;
import com.finalproject.sulbao.board.dto.*;
import com.finalproject.sulbao.board.service.LikeService;
import com.finalproject.sulbao.board.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.finalproject.sulbao.board.common.BoardCategoryConstants.ZZANFEED_ID;
import static com.finalproject.sulbao.login.model.entity.RoleType.ADMIN;

@Controller
@RequiredArgsConstructor
@RequestMapping("/zzanfeeds")
public class ZzanfeedController {

    private final PostService postService;
    private final SessionHandler sessionHandler;
    private final LikeService likeService;

    @GetMapping("/new")
    public String newPost() {
        return "/board/zzanfeed/new";
    }

    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<Long> savePost(@ModelAttribute ZzanfeedRequestDto requestDto, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userNo");
        PostDto post = postService.save(userId, ZZANFEED_ID, requestDto);
        return ResponseEntity.ok(post.getId());
    }

    @GetMapping("/{postId}")
    public String detail(@PathVariable Long postId, Model model, HttpServletRequest request) {
        postService.updateHit(postId);
        UserDto user = sessionHandler.getUserFromSession(request);
        PostDto post = postService.findById(postId);
        boolean isPostLikedByUser = likeService.isPostLikedByUser(postId, user.getId());
        boolean isAdmin = (user.getRoleType() == ADMIN);
        List<CommentDto> comments = post.getCommentDtoList();
        String[] contentArr = post.getContent().split("\\|");
        List<Content> contents = new ArrayList<>();
        List<String> postImages = post.getPostImageDtoList().stream().map(PostImageDto::getFileName).toList();

        for (int i = 0; i < contentArr.length; i++) {
            Content content = new Content(postImages.get(i), contentArr[i]);
            contents.add(content);
        }

        model.addAttribute("user", user);
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("isPostLikedByUser", isPostLikedByUser);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("contents", contents);
        model.addAttribute("postImages", postImages);
        return "/board/zzanfeed/detail";
    }

    @GetMapping
    public String list(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String tag) {

        Page<PostDto> postPage = postService.getPostPage(ZZANFEED_ID, page, tag);
        List<PostDto> posts = postPage.getContent();
        Long count = postService.count(ZZANFEED_ID, tag);
        List<String> tags = postService.findTopTags();

        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("count", count);
        model.addAttribute("tags", tags);
        return "/board/zzanfeed/list";
    }

    @GetMapping("/more")
    @ResponseBody
    public List<PostDto> loadMorePosts(@RequestParam(defaultValue = "0") int page, String tag) {
        return postService.loadMorePosts(ZZANFEED_ID, page, tag);
    }

    @GetMapping("/edit/{postId}")
    public String edit(@PathVariable Long postId, HttpServletRequest request, Model model) {
        UserDto user = sessionHandler.getUserFromSession(request);
        PostDto post = postService.findById(postId);

        if (!post.getUserDto().getId().equals(user.getId()) && user.getRoleType() != ADMIN) {
            return "redirect:/zzanfeeds/" + postId;
        }

        String thumbnail = post.getThumbnail();
        List<PostImageDto> images = post.getPostImageDtoList();
        String[] texts = post.getContent().split("\\|");
        List<String> tags = post.getTags();
        List<Content> contents = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            contents.add(new Content(images.get(i).getFileName(), texts[i]));
        }

        model.addAttribute("user", user);
        model.addAttribute("tags", tags);
        model.addAttribute("post", post);
        model.addAttribute("contents", contents);
        model.addAttribute("thumbnail", thumbnail);
        return "/board/zzanfeed/edit";
    }

    @PostMapping("/edit/{postId}")
    @ResponseBody
    public ResponseEntity<Void> editPost(@PathVariable Long postId, @ModelAttribute ZzanfeedRequestDto requestDto, HttpServletRequest request) {
        postService.update(postId, requestDto);
        return ResponseEntity.ok().build();
    }

    @AllArgsConstructor
    @Data
    static class Content {
        String image;
        String text;
    }

}