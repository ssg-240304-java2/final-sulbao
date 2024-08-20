package com.finalproject.sulbao.board.repository;

import com.finalproject.sulbao.board.domain.BoardCategory;
import com.finalproject.sulbao.board.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByBoardCategory(BoardCategory boardCategory, Pageable pageable);

    Long countByBoardCategory(BoardCategory boardCategory);

    @Query("SELECT tag FROM Post p JOIN p.tags tag GROUP BY tag ORDER BY count(tag) DESC")
    List<String> findTopTags();

    @Query("SELECT p FROM Post p JOIN p.tags t WHERE p.boardCategory = :boardCategory AND t = :tag")
    Page<Post> findByBoardCategoryAndTag(@Param("boardCategory") BoardCategory boardCategory, @Param("tag") String tag, Pageable pageable);

    @Query("SELECT count(p) FROM Post p JOIN p.tags t WHERE p.boardCategory = :boardCategory AND t = :tag")
    Long countByBoardCategoryAndTag(@Param("boardCategory") BoardCategory boardCategory, @Param("tag") String tag);

}
