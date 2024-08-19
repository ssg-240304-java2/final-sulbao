package com.finalproject.sulbao.cart.service;


import com.finalproject.sulbao.cart.domain.Order;
import com.finalproject.sulbao.cart.dto.OrderDTO;
import com.finalproject.sulbao.cart.repository.CartRepository;
import com.finalproject.sulbao.cart.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public OrderService(OrderRepository orderRepository,
                       ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }


    public Long saveOrder(OrderDTO orderDTO) {
//        Order order = modelMapper.map(orderDTO, Order.class);
        // System.out.println(orderDTO);
        Order order = orderDTO.toEntity();

        System.out.println("order ============> " + order);
        Order save = orderRepository.save(order);
        Long orderCode = save.getOrderCode();
        return orderCode;
    }
}
