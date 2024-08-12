package com.finalproject.sulbao.cart.repository;

import com.finalproject.sulbao.cart.domain.Carts;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Carts, Integer> {

    List<Carts> findByUserId(Long userId, Sort cartCode);
    Long deleteByCartCode(Long cartCode);

    Optional<Carts> findByCartCode(Long cartCode);
}
