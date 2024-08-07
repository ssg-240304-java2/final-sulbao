package com.finalproject.sulbao.board.controller;

import com.finalproject.sulbao.board.domain.Comment;
import com.finalproject.sulbao.board.domain.Member;
import com.finalproject.sulbao.board.domain.Post;
import com.finalproject.sulbao.board.repository.CommentRepository;
import com.finalproject.sulbao.board.repository.MemberRepository;
import com.finalproject.sulbao.board.repository.PostRepository;
import com.finalproject.sulbao.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/comments")
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentService commentService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<String> saveComment(Long memberId, Long postId, String content) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();
        Comment comment = Comment.createComment(content, member, post);
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
