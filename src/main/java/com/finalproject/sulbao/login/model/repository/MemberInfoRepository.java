package com.finalproject.sulbao.login.model.repository;

import com.finalproject.sulbao.login.model.entity.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {
}
