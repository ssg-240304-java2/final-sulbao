package com.finalproject.sulbao.cart.service;


import com.finalproject.sulbao.cart.dto.OrderItemDTO;
import com.finalproject.sulbao.cart.repository.OrderRepository;
import com.finalproject.sulbao.cart.vo.OrderItem;
import com.finalproject.sulbao.product.model.dto.ProductDTO;
import com.finalproject.sulbao.product.model.entity.Product;
import com.finalproject.sulbao.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class OrderProductService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    public OrderProductService(ProductRepository productRepository
    , ModelMapper modelMapper, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
    }


    public List<ProductDTO> findByUserNo(Long userNo) {
        List<Product> productList = productRepository.findBySellerInfo_userNo(userNo);

        return productList.stream()
                        .map(product -> new ProductDTO().toDTO(product))
                        .collect(Collectors.toList());
    }

    public List<OrderItemDTO> findByProductNo(List<Long> productIdList) {
        List<OrderItem> orderItemList = orderRepository.findByProductId(productIdList);

        return orderItemList.stream()
                .map(orderitem -> modelMapper.map(orderitem, OrderItemDTO.class))
                .collect(Collectors.toList());
    }

    public String findByProductNoName(Long productNo) {
        String productName = productRepository.findByProductNoName(productNo);
        return productName;
    }
}
