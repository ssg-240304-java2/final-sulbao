package com.finalproject.sulbao.board.service;

import com.finalproject.sulbao.board.domain.Comment;
import com.finalproject.sulbao.board.domain.Post;
import com.finalproject.sulbao.board.repository.CommentRepository;
import com.finalproject.sulbao.board.repository.PostRepository;
import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final LoginRepository loginRepository;
    private final PostRepository postRepository;

    public void save(Long userId, Long postId, String content) {
        Login login = loginRepository.findById(userId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();
        Comment comment = Comment.createComment(content, login, post);
        commentRepository.save(comment);
    }

    public void updateComment(Long id, String content) {
        Comment comment = commentRepository.findById(id).orElseThrow();
        comment.setContent(content);
    }

    public void deleteById(Long commentId) {
        commentRepository.deleteById(commentId);
    }

}
