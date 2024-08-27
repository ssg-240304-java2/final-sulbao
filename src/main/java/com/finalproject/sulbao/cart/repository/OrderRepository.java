package com.finalproject.sulbao.cart.repository;

import com.finalproject.sulbao.auth.model.dto.OrderStatus;
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

    @Modifying
    @Query(value = "update Order k set k.delivery = '환불중' where k.orderCode = :orderCode")
    void refundOrder(Long orderCode);

    @Query(value = "select j.amount from Order k join k.orderItems j where k.orderCode IN (:orderCodes)")
    List<Integer> findByOrderCodes(List<Long> orderCodes);

    @Query(value = "select j.productNo from Order k join k.orderItems j where k.orderCode IN (:orderCodes)")
    List<Long> findByProductNos(List<Long> orderCodes);

    @Query(value = "SELECT * FROM tbl_order WHERE is_present = TRUE AND DATE_FORMAT(DATE_ADD(created_at,INTERVAL 6 DAY), '%Y-%m-%d') =  DATE_FORMAT(now(), '%Y-%m-%d') and zip_code = null", nativeQuery = true)
    List<Order> findByPresentInfo();

    @Query(value = "select p.orderItems from Order p where p.delivery= :delivery")
    List<OrderItem> findByOrderStatusProductNo(String delivery );

}
