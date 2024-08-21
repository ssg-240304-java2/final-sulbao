package com.finalproject.sulbao.login.model.repository;

import com.finalproject.sulbao.login.model.entity.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {

    @Query("SELECT COUNT(m.profileName) FROM MemberInfo m WHERE m.profileName  = :profileName AND m.user.userNo != :userNo")
    int existsByProfileNameAndUserNoNot(String profileName, Long userNo);

    @Query("SELECT m FROM MemberInfo m WHERE m.user.userNo = (SELECT u.userNo FROM Login u WHERE u.userId = :userId)")
    MemberInfo findByUserId(String userId);

    @Query("SELECT m.proMemberInfo.proStatus FROM MemberInfo m WHERE m.user.userNo = (SELECT u.userNo FROM Login u WHERE u.userId = :userId)")
    String findProStatusByUserId(String userId);
}

