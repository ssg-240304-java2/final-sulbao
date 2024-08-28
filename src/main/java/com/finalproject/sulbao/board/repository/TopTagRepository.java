package com.finalproject.sulbao.board.repository;

import com.finalproject.sulbao.board.domain.TopTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TopTagRepository extends JpaRepository<TopTag, Long> {

    @Query("SELECT tag, COUNT(tag) FROM Post p JOIN p.tags tag GROUP BY tag ORDER BY COUNT(tag) DESC")
    List<Object[]> findTopTagsWithCount();

}
