package com.finalproject.sulbao.login.model.repository;

import com.finalproject.sulbao.login.model.dto.LoginDetails;
import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.vo.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LoginRepository extends JpaRepository<Login, Long> {
    Optional<Login> findByUserId (String userId);
    boolean existsByUserId(String userId);

    @Query("SELECT s.sellerInfo FROM Login s WHERE s.sellerInfo.businessNumber = :num")
    SellerInfo findSellerByNum(String num);

    @Query("SELECT s.sellerInfo FROM Login s WHERE s.sellerInfo.businessName = :name")
    SellerInfo findByBusinessName(String name);
}
