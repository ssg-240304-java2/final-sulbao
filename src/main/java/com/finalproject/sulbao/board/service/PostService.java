package com.finalproject.sulbao.board.service;

import com.finalproject.sulbao.board.domain.Post;
import com.finalproject.sulbao.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public void updateHit(Post post) {
        post.updateHit();
    }

}
