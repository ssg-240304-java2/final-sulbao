package com.finalproject.sulbao.cart.repository;

import com.finalproject.sulbao.cart.domain.Carts;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Carts, Integer> {

    List<Carts> findByUserIdAndIsOrder(String userId,boolean isOrder, Sort cartCode);
    Long deleteByCartCode(Long cartCode);

    Optional<Carts> findByCartCode(Long cartCode);

    List<Carts> findByCartCodeIn(List<Long> cartCodes, Sort cartCode);

    @Query(value = "SELECT SUM(total_price) FROM tbl_cart WHERE cart_code IN (:cartCodes)", nativeQuery = true)
    int findTotalPurchasePriceByCartCodeIn(List<Long> cartCodes);

    @Query(value = "SELECT * FROM tbl_cart c WHERE c.cart_code = :cartCode", nativeQuery = true)
    Carts findByCartCodes(@Param("cartCode") Long cartCode);

    List<Carts> findByToken(String token);
}
