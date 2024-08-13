package com.finalproject.sulbao.login.model.repository;

import com.finalproject.sulbao.login.model.entity.EmailVerify;
import com.finalproject.sulbao.login.model.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<EmailVerify, Long> {

//    @Query("SELECT e FROM EmailVerify e WHERE e.id = :email")
//    EmailVerify findByEmail(String email);


}
