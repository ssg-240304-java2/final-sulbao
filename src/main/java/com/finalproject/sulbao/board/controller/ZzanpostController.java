package com.finalproject.sulbao.board.controller;

import com.finalproject.sulbao.board.dto.CommentDto;
import com.finalproject.sulbao.board.dto.PostDto;
import com.finalproject.sulbao.board.dto.PostImageDto;
import com.finalproject.sulbao.board.dto.UserDto;
import com.finalproject.sulbao.board.service.LikeService;
import com.finalproject.sulbao.board.service.PostService;
import com.finalproject.sulbao.common.file.FileService;
import com.finalproject.sulbao.login.model.repository.LoginRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.EnumSet;
import java.util.List;

import static com.finalproject.sulbao.login.model.entity.RoleType.ADMIN;
import static com.finalproject.sulbao.login.model.entity.RoleType.PRO_MEMBER;

@Controller
@RequestMapping("/zzanposts")
@RequiredArgsConstructor
public class ZzanpostController {

    public static final Long ZZANPOST_CATEGORY_ID = 2L;
    private final PostService postService;
    private final LikeService likeService;
    private final FileService fileService;
    private final LoginRepository loginRepository; // 회원정보 세션처리 완료되면 삭제 예정

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping
    public String list(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<PostDto> postPage = postService.getPostPage(page);
        List<PostDto> posts = postPage.getContent();
        int totalPages = postPage.getTotalPages();
        Long count = postService.count();

        model.addAttribute("posts", posts);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("count", count);
        return "/board/zzanpost/list";
    }

    @GetMapping("/more")
    @ResponseBody
    public List<PostDto> loadMorePosts(@RequestParam(defaultValue = "0") int page) {
        List<PostDto> posts = postService.loadMorePosts(page);
        return posts;
    }

    @GetMapping("/new")
    public String newPost(Model model, HttpServletRequest request) {
        login(request); // 회원정보 세션처리 완료되면 삭제 예정

        UserDto user = (UserDto) request.getSession().getAttribute("user");
        if (!EnumSet.of(PRO_MEMBER, ADMIN).contains(user.getRoleType())) {
            return "redirect:/";
        }

        model.addAttribute("user", user);
        return "/board/zzanpost/new";
    }

    @GetMapping("/edit/{postId}")
    public String editPost(Model model, @PathVariable Long postId, HttpServletRequest request) {
        login(request);

        UserDto user = (UserDto) request.getSession().getAttribute("user");
        PostDto post = postService.findById(postId);
        PostImageDto thumbnail = post.getPostImageDtoList().get(0);

        if (!post.getUserDto().getId().equals(user.getId()) && user.getRoleType() != ADMIN) {
            return "redirect:/zzanposts/" + postId;
        }

        model.addAttribute("user", user);
        model.addAttribute("post", post);
        model.addAttribute("thumbnail", thumbnail);
        return "/board/zzanpost/edit";
    }

    @PostMapping("/new")
    public ResponseEntity<Long> savePost(String title, String content, MultipartFile multipartFile, HttpServletRequest request) {
        login(request);

        Long userId = ((UserDto) request.getSession().getAttribute("user")).getId();
        String thumbnailFileName = fileService.uploadFiles(multipartFile, uploadDir).getUploadFileName();
        PostDto post = postService.save(userId, title, content, ZZANPOST_CATEGORY_ID, thumbnailFileName);
        Long postId = post.getId();
        return ResponseEntity.ok(postId);
    }

    @PutMapping("/edit")
    public ResponseEntity<Long> updatePost(Long postId, String title, String content, MultipartFile multipartFile, HttpServletRequest request) {
        login(request);

        String thumbnailFileName = null;
        if (multipartFile != null) {
            thumbnailFileName = fileService.uploadFiles(multipartFile, uploadDir).getUploadFileName();
        }
        postService.update(postId, title, content, thumbnailFileName);
        return ResponseEntity.ok(postId);
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, HttpServletRequest request) {
        login(request); // 회원정보 세션처리 완료되면 삭제 예정

        postService.updateHit(id);
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        PostDto post = postService.findById(id);
        boolean isPostLikedByUser = likeService.isPostLikedByUser(post.getId(), user.getId());
        boolean isAdmin = (user.getRoleType() == ADMIN);
        List<CommentDto> comments = post.getCommentDtoList();

        model.addAttribute("user", user);
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("isPostLikedByUser", isPostLikedByUser);
        model.addAttribute("isAdmin", isAdmin);
        return "/board/zzanpost/detail";
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.ok().build();
    }

    public void login(HttpServletRequest request) {
        UserDto user = UserDto.toUserDto(loginRepository.findById(2L).orElseThrow());
        request.getSession().setAttribute("user", user);
    }

}
