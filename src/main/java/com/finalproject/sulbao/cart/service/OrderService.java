package com.finalproject.sulbao.cart.service;


import com.finalproject.sulbao.cart.domain.Order;
import com.finalproject.sulbao.cart.dto.OrderDTO;
import com.finalproject.sulbao.cart.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<OrderDTO> findByToken(String token) {
        List<Order> orders = orderRepository.findByToken(token);
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    public void updateOrderNameBytoken(String orderName, String token) {
        List<Order> orders = orderRepository.findByToken(token);
        if (orders.size() > 0) {
            for (Order order : orders) {
                order.setNames(orderName);
                orderRepository.save(order);
            }
        }
    }

    public void updateOrderPhoneBytoken(String orderPhone, String token) {
        List<Order> orders = orderRepository.findByToken(token);
        if (orders.size() > 0) {
            for (Order order : orders) {
                order.setPhoneNumber(orderPhone);
                orderRepository.save(order);
            }
        }
    }

    public void updatePostCodeBytoken(String postcode, String token) {
        List<Order> orders = orderRepository.findByToken(token);
        if (orders.size() > 0) {
            for (Order order : orders) {
                order.setZipCode(postcode);
                orderRepository.save(order);
            }
        }
    }

    public void updateAddress1Bytoken(String address, String token) {
        List<Order> orders = orderRepository.findByToken(token);
        if (orders.size() > 0) {
            for (Order order : orders) {
                order.setAddress1(address);
                orderRepository.save(order);
            }
        }
    }

    public void updateAddress2Bytoken(String detailAddress, String token) {
        List<Order> orders = orderRepository.findByToken(token);
        if (orders.size() > 0) {
            for (Order order : orders) {
                order.setAddress2(detailAddress);
                orderRepository.save(order);
            }
        }
    }

    public List<OrderDTO> findByProductNo(List<Long> producList) {
        List<Order> orders = orderRepository.findByProductNo(producList);
        return orders.stream()
                        .map(order -> modelMapper.map(order, OrderDTO.class))
                        .collect(Collectors.toList());
    }

    public void updateDeliveryByOrderCode(List<Long> orderCodes , String deliveryStatus) {
        orderRepository.updateDeliveryByOrderCode(orderCodes, deliveryStatus);
    }

    public List<OrderDTO> findByUserId(String userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    public void refundOrder(Long orderCode) {
        orderRepository.refundOrder(orderCode);
    }

    public List<Integer> findByOrderCodes(List<Long> orderCodes) {
        List<Integer> amounts = orderRepository.findByOrderCodes(orderCodes);
        return amounts;
    }

    public List<Long> findByProductNos(List<Long> orderCodes) {
        List<Long> productNos = orderRepository.findByProductNos(orderCodes);
        return productNos;
    }
}
