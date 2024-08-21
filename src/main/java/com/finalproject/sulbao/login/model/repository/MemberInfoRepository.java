package com.finalproject.sulbao.login.model.repository;

import com.finalproject.sulbao.login.model.dto.ProInfoDto;
import com.finalproject.sulbao.login.model.entity.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {

    @Query("SELECT COUNT(m.profileName) FROM MemberInfo m WHERE m.profileName  = :profileName AND m.user.userNo != :userNo")
    int existsByProfileNameAndUserNoNot(String profileName, Long userNo);

    @Query("SELECT m FROM MemberInfo m WHERE m.user.userNo = (SELECT u.userNo FROM Login u WHERE u.userId = :userId)")
    MemberInfo findByUserId(String userId);

    @Query("SELECT m.proMemberInfo.proStatus FROM MemberInfo m WHERE m.user.userNo = (SELECT u.userNo FROM Login u WHERE u.userId = :userId)")
    String findProStatusByUserId(String userId);

    @Modifying
    @Query("UPDATE MemberInfo m SET m.proMemberInfo.businessNumber = :#{#proInfo.businessNumber}, m.proMemberInfo.businessLink = :#{#proInfo.businessLink}, m.proMemberInfo.proStatus = 'WAIT' WHERE m.memberNo = :#{#proInfo.memberNo}")
    void saveProMember(@Param("proInfo") ProInfoDto proInfo);


    @Modifying
    @Query("UPDATE MemberInfo m SET m.proMemberInfo.businessNumber = null, m.proMemberInfo.businessLink = null, m.proMemberInfo.proStatus = null WHERE m.memberNo = :memberNo")
    void deleteProForm(Long memberNo);
}

