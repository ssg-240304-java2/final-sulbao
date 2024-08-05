package com.finalproject.sulbao.board.repository;

import com.finalproject.sulbao.board.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
