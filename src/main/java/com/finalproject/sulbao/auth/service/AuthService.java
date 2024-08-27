package com.finalproject.sulbao.auth.service;

import com.finalproject.sulbao.auth.model.dto.OrderStatus;
import com.finalproject.sulbao.cart.domain.Order;
import com.finalproject.sulbao.cart.repository.OrderRepository;
import com.finalproject.sulbao.cart.vo.OrderItem;
import com.finalproject.sulbao.product.model.entity.Product;
import com.finalproject.sulbao.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AuthService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public AuthService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public OrderStatus OrderStatusCountList(Long userNo, String delivery){

        List<OrderItem> orderList = orderRepository.findByOrderStatusProductNo(delivery);

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(delivery);
        int statusCount = 0;

        for(OrderItem orderItem : orderList){

            Product product = productRepository.findByProductNo(orderItem.getProductNo());

            log.info("orderItem.getProductNo() ========= {}", orderItem.getProductNo());
            log.info("검색한 상품 정보 : {}", product);

            if(userNo == null ){
                statusCount++;
            }else{
                if(product != null && product.getSellerInfo().getUserNo() == userNo){
                    statusCount++;
                }
            }

        }
        orderStatus.setStatusCount(statusCount);

        return orderStatus;
    }
}
