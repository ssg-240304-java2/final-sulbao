package com.finalproject.sulbao.login.model.vo;

import com.finalproject.sulbao.login.model.entity.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
public class LoginVO {

    private Long userNo;

    private String userId;

    private String userPw;

    private RoleType userRole;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginVO loginVO = (LoginVO) o;
        return Objects.equals(userNo, loginVO.userNo) && Objects.equals(userId, loginVO.userId) && Objects.equals(userPw, loginVO.userPw) && userRole == loginVO.userRole;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userNo, userId, userPw, userRole);
    }
}
