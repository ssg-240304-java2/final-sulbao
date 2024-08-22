package com.finalproject.sulbao.cart.repository;

import com.finalproject.sulbao.cart.domain.Order;
import com.finalproject.sulbao.cart.vo.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByToken(String token);

    @Query(value = "select p from Order p join p.orderItems k where k.productNo in :productList")
    List<Order> findByProductNo(@Param("productList") List<Long> productList);

    @Query(value="select k from Order p join p.orderItems k where k.productNo in :productIdList")
    List<OrderItem> findByProductId(List<Long> productIdList);

    @Modifying
    @Query(value ="Update Order k SET k.delivery= :deliveryStatus where k.orderCode IN :orderCodes")
    void updateDeliveryByOrderCode(List<Long> orderCodes, String deliveryStatus);


    @Query(value = "select k from Order k where k.userId = :userId order by k.orderCode")
    List<Order> findByUserId(String userId);
}
