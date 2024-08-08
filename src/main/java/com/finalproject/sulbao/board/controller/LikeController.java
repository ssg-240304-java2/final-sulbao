package com.finalproject.sulbao.board.controller;

import com.finalproject.sulbao.board.repository.LikeRepository;
import com.finalproject.sulbao.board.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeRepository likeRepository;
    private final LikeService likeService;

    @GetMapping
    public boolean isLiked(Long postId, Long userId) {
        return likeRepository.findByPostIdAndUserId(postId, userId).isPresent();
    }

    @DeleteMapping
    public ResponseEntity<String> deleteLike(Long postId, Long userId) {
        likeService.delete(postId, userId);
        return ResponseEntity.ok("삭제되었습니다.");
    }

    @PostMapping
    public ResponseEntity<String> saveLike(Long postId, Long userId) {
        likeService.save(postId, userId);
        return ResponseEntity.ok("저장되었습니다.");
    }

}
