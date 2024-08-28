package com.finalproject.sulbao.login.model.repository;

import com.finalproject.sulbao.login.model.dto.LoginDetails;
import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.entity.RoleType;
import com.finalproject.sulbao.login.model.vo.SellerInfo;
import com.finalproject.sulbao.member.dto.MemberDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LoginRepository extends JpaRepository<Login, Long> {

    @Query("SELECT u FROM Login u WHERE u.userId = :userId")
    Optional<Login> findByUserId (String userId);
    boolean existsByUserId(String userId);

    @Query("SELECT s.sellerInfo FROM Login s WHERE s.sellerInfo.businessNumber = :num")
    SellerInfo findSellerByNum(String num);

    @Query("SELECT s.sellerInfo FROM Login s WHERE s.sellerInfo.businessName = :name")
    SellerInfo findByBusinessName(String name);

    @Query("SELECT m.memberImage.saveImgUrl FROM MemberInfo m WHERE m.user.userNo = :userNo")
    String findProfileUrflByUserNo(Long userNo);

    @Query("SELECT u FROM Login u WHERE u.memberInfo.proMemberInfo.proStatus IN ('WAIT','APPROVE')")
    List<Login> findProMembers();

    @Query("SELECT u FROM Login u WHERE u.userRole = 'ROLE_SELLER'")
    List<Login> findSellerList();

    @Query("SELECT u FROM Login u WHERE u.userNo = :userNo")
    Optional<Login> findByUserNo(String userNo);

    @Query("SELECT u FROM Login u WHERE u.userId LIKE CONCAT('%', :searchInput, '%')")
    List<Login> findMemberBySearchUserId(String searchInput);

    @Query("SELECT u FROM Login u WHERE u.memberInfo.profileName LIKE CONCAT('%', :searchInput, '%')")
    List<Login> findMemberBySearchName(String searchInput);

    @Query("SELECT u FROM Login u WHERE u.email LIKE CONCAT('%', :searchInput, '%')")
    List<Login> findMemberBySearchEmail(String searchInput);

    @Query("SELECT u FROM Login u WHERE u.userRole = :role")
    List<Login> findMemberByRole(RoleType role);

    @Query("SELECT u FROM Login u WHERE u.userId LIKE CONCAT('%', :searchInput, '%') AND u.userRole=:role")
    List<Login> findProMemberBySearchUserId(String searchInput, RoleType role);

    @Query("SELECT u FROM Login u WHERE u.memberInfo.profileName LIKE CONCAT('%', :searchInput, '%') AND u.userRole=:role")
    List<Login> findProMemberBySearchName(String searchInput, RoleType role);

    @Query("SELECT u FROM Login u WHERE u.email LIKE CONCAT('%', :searchInput, '%') AND u.userRole=:role")
    List<Login> findProMemberBySearchEmail(String searchInput, RoleType role);

    @Query("SELECT u FROM Login u WHERE u.memberInfo.proMemberInfo.businessNumber LIKE CONCAT('%', :searchInput, '%')  AND u.userRole=:role")
    List<Login> findProMemberBySearchBusinessNum(String searchInput, RoleType role);

    @Query("SELECT u FROM Login u WHERE u.memberInfo.proMemberInfo.proStatus = :statusAll  AND u.userRole=:role")
    List<Login> findProMemberByStatus(String statusAll, RoleType role);

    @Query("SELECT u FROM Login u WHERE u.userId LIKE CONCAT('%', :searchInput, '%')  AND u.userRole=:role")
    List<Login> findSellerBySearchUserId(String searchInput, RoleType role);


    @Query("SELECT u FROM Login u WHERE u.sellerInfo.businessName = :searchInput AND u.userRole=:role")
    List<Login> findSellerBySearchName(String searchInput, RoleType role);

    @Query("SELECT u FROM Login u WHERE u.email LIKE CONCAT('%', :searchInput, '%')  AND u.userRole=:role")
    List<Login> findSellerBySearchEmail(String searchInput, RoleType role);

    @Query("SELECT u FROM Login u WHERE u.sellerInfo.businessNumber = :searchInput AND u.userRole=:role")
    List<Login> findSellerBySearchBusinessNum(String searchInput, RoleType role);

    @Query("SELECT u FROM Login u WHERE u.sellerInfo.sellerStatus = :statusAll  AND u.userRole=:role")
    List<Login> findSellerByStatus(String statusAll, RoleType role);

    @Query("SELECT u FROM Login u WHERE u.isEnabled = :availableYn")
    List<Login> findMemberByAvailable(Boolean availableYn);
}
