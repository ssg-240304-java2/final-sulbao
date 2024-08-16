package com.finalproject.sulbao.login.model.repository;

import com.finalproject.sulbao.login.model.entity.EmailVerify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface EmailRepository extends JpaRepository<EmailVerify, Long> {

    @Query("SELECT e FROM EmailVerify e WHERE e.email = :email")
    EmailVerify findByEmail(String email);


    @Query("SELECT e.code FROM EmailVerify e WHERE e.email = :email")
    String findCodeByEmail(String email);

    @Query("UPDATE EmailVerify e SET e.isVerified=true WHERE e.email = :email")
    void updateVerifyTrue(String email);
}
