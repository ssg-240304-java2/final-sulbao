package com.finalproject.sulbao.review.service;

import com.finalproject.sulbao.cart.domain.Order;
import com.finalproject.sulbao.cart.dto.OrderDTO;
import com.finalproject.sulbao.cart.dto.OrderItemDTO;
import com.finalproject.sulbao.cart.repository.OrderRepository;
import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.repository.LoginRepository;
import com.finalproject.sulbao.product.model.dto.ProductDTO;
import com.finalproject.sulbao.product.model.entity.Product;
import com.finalproject.sulbao.product.model.entity.Review;
import com.finalproject.sulbao.product.repository.ProductRepository;
import com.finalproject.sulbao.review.model.dto.ReviewDTO;
import com.finalproject.sulbao.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final LoginRepository loginRepository;

    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository, OrderRepository orderRepository, ModelMapper modelMapper, LoginRepository loginRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.loginRepository = loginRepository;
    }

    // 주문한 상품정보 취득
    public ProductDTO getProductInfo(Set<OrderItemDTO> product) {
        return new ProductDTO().toDTO(productRepository.findByProductNo(product.iterator().next().getProductNo()));
    }

    public OrderDTO getOrderInfo(String orderNo) {

        Order order = orderRepository.findById(Long.valueOf(orderNo)).get();

        return modelMapper.map(order, OrderDTO.class);
    }

    @Transactional
    public void saveReview(ReviewDTO reviewDTO) {

        // 등록한 사용자 정보
        Login userInfo = loginRepository.findById(reviewDTO.getUserNo()).get();

        // 리뷰를 등록한 상품 정보
        Product product = productRepository.findByProductNo(reviewDTO.getProductNo());

        //리뷰 엔티티생성
        Review review = Review.builder()
                .user(userInfo)
                .product(product)
                .reviewContent(reviewDTO.getReviewContent())
                .build();

        reviewRepository.save(review);

        // order 리뷰여부 변경
        Order order = orderRepository.findById(reviewDTO.getOrderNo()).get();
        order.setReviewed(true);

    }

    public ReviewDTO findById(Long reviewId) {

        Review review = reviewRepository.findById(reviewId).get();
        return ReviewDTO.builder()
                .reviewId(review.getReviewId())
                .reviewContent(review.getReviewContent())
                .productDTO(new ProductDTO().toDTO(review.getProduct()))
                .user(review.getUser())
                .SellerInfo(review.getProduct().getSellerInfo())
                .createDate(review.getCreatedAt())
                .build();

    }

    public List<ReviewDTO> findByUserNo(Long userNo) {

        List<Review> reviewList = reviewRepository.findByUser_userNo(userNo);

        return reviewList.stream()
                .map(review -> ReviewDTO.builder()
                        .reviewId(review.getReviewId())
                        .reviewContent(review.getReviewContent())
                        .productDTO(new ProductDTO().toDTO(review.getProduct()))
                        .user(review.getUser())
                        .SellerInfo(review.getProduct().getSellerInfo())
                        .createDate(review.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateReview(ReviewDTO reviewDTO) {

        Review review = reviewRepository.findById(reviewDTO.getReviewId()).get();
        review.updateContent(reviewDTO.getReviewContent());

    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
