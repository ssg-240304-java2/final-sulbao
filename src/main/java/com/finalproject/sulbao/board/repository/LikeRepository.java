package com.finalproject.sulbao.board.repository;

import com.finalproject.sulbao.board.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query(value = "select l from Like l where l.login.userNo = :userId and l.post.id = :postId")
    Optional<Like> findByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);

}
