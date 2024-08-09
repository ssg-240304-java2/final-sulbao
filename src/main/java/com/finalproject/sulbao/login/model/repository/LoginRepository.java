package com.finalproject.sulbao.login.model.repository;

import com.finalproject.sulbao.login.model.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<Login, Long> {
}
