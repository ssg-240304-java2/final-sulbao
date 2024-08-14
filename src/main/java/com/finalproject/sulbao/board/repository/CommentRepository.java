package com.finalproject.sulbao.board.repository;

import com.finalproject.sulbao.board.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
