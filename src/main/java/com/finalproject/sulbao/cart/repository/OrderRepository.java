package com.finalproject.sulbao.cart.repository;

import com.finalproject.sulbao.cart.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByToken(String token);
}
