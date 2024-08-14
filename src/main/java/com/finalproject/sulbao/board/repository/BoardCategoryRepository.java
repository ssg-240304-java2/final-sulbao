package com.finalproject.sulbao.board.repository;

import com.finalproject.sulbao.board.domain.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory, Long> {
}
