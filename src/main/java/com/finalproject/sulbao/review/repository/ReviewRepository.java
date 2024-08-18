package com.finalproject.sulbao.review.repository;

import com.finalproject.sulbao.product.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUser_userNo(Long userNo);
}
