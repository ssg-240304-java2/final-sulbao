package com.finalproject.sulbao.board.service;

import com.finalproject.sulbao.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public void updateHit(Long id) {
        postRepository.findById(id).orElseThrow().updateHit();
    }

    public void delete(Long id) {
        postRepository.deleteById(id);
    }
    
}
