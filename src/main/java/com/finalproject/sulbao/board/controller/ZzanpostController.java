package com.finalproject.sulbao.board.controller;

import com.finalproject.sulbao.board.common.SessionHandler;
import com.finalproject.sulbao.board.dto.CommentDto;
import com.finalproject.sulbao.board.dto.PostDto;
import com.finalproject.sulbao.board.dto.UserDto;
import com.finalproject.sulbao.board.service.LikeService;
import com.finalproject.sulbao.board.service.PostService;
import com.finalproject.sulbao.common.file.FileService;
import com.vane.badwordfiltering.BadWordFiltering;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.finalproject.sulbao.board.common.BoardCategoryConstants.ZZANPOST_ID;
import static com.finalproject.sulbao.login.model.entity.RoleType.ADMIN;
import static com.finalproject.sulbao.login.model.entity.RoleType.PRO_MEMBER;

@Controller
@RequestMapping("/zzanposts")
@RequiredArgsConstructor
public class ZzanpostController {

    private final PostService postService;
    private final LikeService likeService;
    private final FileService fileService;
    private final SessionHandler sessionHandler;
    private final BadWordFiltering contentFilter;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping
    public String list(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<PostDto> postPage = postService.getPostPage(ZZANPOST_ID, page);
        List<PostDto> posts = postPage.getContent();
        Long count = postService.count(ZZANPOST_ID);

        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("count", count);
        return "board/zzanpost/list";
    }

    @GetMapping("/more")
    @ResponseBody
    public List<PostDto> loadMorePosts(@RequestParam(defaultValue = "0") int page) {
        return postService.loadMorePosts(ZZANPOST_ID, page);
    }

    @GetMapping("/new")
    public String newPost(Model model, Authentication authentication) {
        UserDto user = sessionHandler.getUser(authentication);

        if (!EnumSet.of(PRO_MEMBER, ADMIN).contains(user.getRoleType())) {
            return "redirect:/";
        }

        model.addAttribute("user", user);
        return "board/zzanpost/new";
    }

    @GetMapping("/edit/{postId}")
    public String editPost(Model model, @PathVariable Long postId, Authentication authentication) {
        UserDto user = sessionHandler.getUser(authentication);
        PostDto post = postService.findById(postId);
        String thumbnail = post.getThumbnail();

        if (!post.getUserDto().getId().equals(user.getId()) && user.getRoleType() != ADMIN) {
            return "redirect:/zzanposts/" + postId;
        }

        model.addAttribute("user", user);
        model.addAttribute("post", post);
        model.addAttribute("thumbnail", thumbnail);
        return "board/zzanpost/edit";
    }

    @PostMapping("/new")
    public ResponseEntity<Long> savePost(String title, String content, MultipartFile multipartFile, Authentication authentication) {
        UserDto user = sessionHandler.getUser(authentication);
        Long userId = user.getId();
        String thumbnail = fileService.uploadFiles(multipartFile, uploadDir).getUploadFileName();
        PostDto post = postService.save(userId, title, content, ZZANPOST_ID, thumbnail);
        Long postId = post.getId();
        return ResponseEntity.ok(postId);
    }

    @PutMapping("/edit")
    public ResponseEntity<Long> updatePost(Long postId, String title, String content, MultipartFile multipartFile) {
        String thumbnailFileName = null;
        if (multipartFile != null) {
            thumbnailFileName = fileService.uploadFiles(multipartFile, uploadDir).getUploadFileName();
        }
        postService.update(postId, title, content, thumbnailFileName);
        return ResponseEntity.ok(postId);
    }

    @GetMapping("/{postId}")
    public String detail(@PathVariable Long postId, Model model, Authentication authentication) {
        postService.updateHit(postId);
        UserDto user = sessionHandler.getUser(authentication);
        PostDto post = postService.findById(postId);
        boolean isPostLikedByUser = likeService.isPostLikedByUser(postId, user.getId());
        boolean isAdmin = (user.getRoleType() == ADMIN);
        List<CommentDto> comments = post.getCommentDtoList();

        model.addAttribute("user", user);
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("isPostLikedByUser", isPostLikedByUser);
        model.addAttribute("isAdmin", isAdmin);
        return "board/zzanpost/detail";
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/filter-contents")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> filterContents(@RequestBody Map<String, String> requestBody) {
        String content = requestBody.get("content");
        boolean hasBadWords = contentFilter.check(content);
        Map<String, Boolean> response = new HashMap<>();
        response.put("hasBadWords", hasBadWords);
        return ResponseEntity.ok(response);
    }

}
