package com.finalproject.sulbao.board.controller;

import com.finalproject.sulbao.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/comments")
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<Void> saveComment(Long userId, Long postId, String content) {
        commentService.save(userId, postId, content);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @ResponseBody
    public ResponseEntity<Void> deleteComment(Long commentId) {
        commentService.deleteById(commentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public String updateComment(Long commentId, String content, Long postId) {
        commentService.updateComment(commentId, content);
        return "redirect:/zzanposts/" + postId;
    }

}
