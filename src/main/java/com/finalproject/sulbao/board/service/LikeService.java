package com.finalproject.sulbao.board.service;

import com.finalproject.sulbao.board.domain.Like;
import com.finalproject.sulbao.board.domain.Post;
import com.finalproject.sulbao.board.repository.LikeRepository;
import com.finalproject.sulbao.board.repository.PostRepository;
import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    final LikeRepository likeRepository;
    final PostRepository postRepository;
    final LoginRepository loginRepository;

    public void delete(Long postId, Long userId) {
        likeRepository.delete(likeRepository.findByPostIdAndUserId(postId, userId).orElseThrow());
    }

    public void save(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow();
        Login login = loginRepository.findById(userId).orElseThrow();
        likeRepository.save(Like.createLike(login, post));
    }

    public boolean isPostLikedByUser(Long postId, Long userId) {
        return likeRepository.findByPostIdAndUserId(postId, userId).isPresent();
    }

}
