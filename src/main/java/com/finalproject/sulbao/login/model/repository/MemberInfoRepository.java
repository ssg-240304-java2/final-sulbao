package com.finalproject.sulbao.login.model.repository;

import com.finalproject.sulbao.login.model.entity.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {

    @Query("SELECT COUNT(m.profileName) FROM MemberInfo m WHERE m.profileName  = :profileName AND m.user.userNo != :userNo")
    boolean existsByProfileNameAndUserNoNot(String profileName, Long userNo);
}
