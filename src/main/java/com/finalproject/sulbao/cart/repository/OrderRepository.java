package com.finalproject.sulbao.cart.repository;

import com.finalproject.sulbao.cart.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
