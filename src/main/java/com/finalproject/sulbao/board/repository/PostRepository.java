package com.finalproject.sulbao.board.repository;

import com.finalproject.sulbao.board.domain.BoardCategory;
import com.finalproject.sulbao.board.domain.Post;
import com.finalproject.sulbao.login.model.entity.Login;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    @Query("SELECT p FROM Post p WHERE p.boardCategory = :boardCategory ORDER BY p.createdAt DESC")
    Page<Post> findByBoardCategory(@Param("boardCategory") BoardCategory boardCategory, Pageable pageable);

    Long countByBoardCategory(BoardCategory boardCategory);

    @Query("SELECT tag FROM Post p JOIN p.tags tag GROUP BY tag ORDER BY count(tag) DESC")
    List<String> findTopTags();

    @Query("SELECT tag, COUNT(tag) FROM Post p JOIN p.tags tag GROUP BY tag ORDER BY COUNT(tag) DESC")
    List<Object[]> findTopTagsWithCount();

    @Query("SELECT p FROM Post p JOIN p.tags t WHERE p.boardCategory = :boardCategory AND t = :tag ORDER BY p.createdAt DESC")
    Page<Post> findByBoardCategoryAndTag(@Param("boardCategory") BoardCategory boardCategory, @Param("tag") String tag, Pageable pageable);

    @Query("SELECT count(p) FROM Post p JOIN p.tags t WHERE p.boardCategory = :boardCategory AND t = :tag")
    Long countByBoardCategoryAndTag(@Param("boardCategory") BoardCategory boardCategory, @Param("tag") String tag);

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN p.tags t " +
            "WHERE p.boardCategory = :boardCategory " +
            "AND (p.title LIKE %:keyword% " +
            "OR p.content LIKE %:keyword% " +
            "OR t LIKE %:keyword%)" +
            "ORDER BY p.createdAt DESC")
    List<Post> findByBoardCategoryAndKeyword(BoardCategory boardCategory, String keyword);

    Page<Post> findAllByLogin(Login login, Pageable pageable);


    @Query("SELECT p FROM Post p WHERE p.boardCategory = :boardCategory AND p.createdAt BETWEEN :startOfWeek AND :endOfWeek ORDER BY p.hit DESC")
    List<Post> findTopPostsByBoardCategoryAndDateRange(@Param("boardCategory") BoardCategory boardCategory,
                                                       @Param("startOfWeek") LocalDateTime startOfWeek,
                                                       @Param("endOfWeek") LocalDateTime endOfWeek,
                                                       Pageable pageable);

}
