package com.finalproject.sulbao.cart.service;


import com.finalproject.sulbao.cart.domain.Order;
import com.finalproject.sulbao.cart.repository.OrderRepository;
import com.finalproject.sulbao.cart.vo.OrderItem;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
public class OrderItemService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public OrderItemService(OrderRepository orderRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    public void saveOrderItem(Long orderPk, Long productNo, int amount, int totalPrice) {
        Optional<Order> optionalOrder = orderRepository.findById(orderPk);
        System.out.println(optionalOrder + "=====================");
        if(optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            OrderItem orderItem = new OrderItem(productNo, amount, totalPrice);

            order.getOrderItems().add(orderItem);
            orderRepository.save(order);
        }else{
            log.error("Order with id {} not found", orderPk);
        }

    }
}
