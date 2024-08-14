package com.finalproject.sulbao.board.controller;

import com.finalproject.sulbao.board.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @GetMapping
    public boolean isPostLikedByUser(Long postId, Long userId) {
        return likeService.isPostLikedByUser(postId, userId);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteLike(Long postId, Long userId) {
        likeService.delete(postId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> saveLike(Long postId, Long userId) {
        likeService.save(postId, userId);
        return ResponseEntity.ok().build();
    }

}
