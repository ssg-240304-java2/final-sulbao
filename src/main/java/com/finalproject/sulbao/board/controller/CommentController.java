package com.finalproject.sulbao.board.controller;

import com.finalproject.sulbao.board.domain.Comment;
import com.finalproject.sulbao.board.domain.Post;
import com.finalproject.sulbao.board.repository.CommentRepository;
import com.finalproject.sulbao.board.repository.PostRepository;
import com.finalproject.sulbao.board.service.CommentService;
import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.repository.LoginRepository;
import com.finalproject.sulbao.login.model.repository.MemberInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/comments")
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentRepository commentRepository;
    private final LoginRepository loginRepository;
    private final PostRepository postRepository;
    private final CommentService commentService;
    private final MemberInfoRepository memberInfoRepository;

    @PostMapping
    @ResponseBody
    public ResponseEntity<String> saveComment(Long userId, Long postId, String content) {
        Login login = loginRepository.findById(userId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();

        Comment comment = Comment.createComment(content, login, post);
        commentService.save(comment);
        return ResponseEntity.ok("등록되었습니다.");
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        commentRepository.deleteById(id);
        return ResponseEntity.ok("삭제되었습니다.");
    }

}
