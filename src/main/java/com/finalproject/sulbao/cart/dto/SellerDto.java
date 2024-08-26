package com.finalproject.sulbao.cart.dto;

import com.finalproject.sulbao.board.dto.UserDto;
import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.entity.RoleType;
import com.finalproject.sulbao.login.model.vo.MemberImage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class SellerDto {

    private Long id;
    private RoleType roleType;
    private String userId;

    public static SellerDto toUserDto(Login login) {


        return SellerDto.builder()
                .id(login.getUserNo())
                .roleType(login.getUserRole())
                .userId(login.getUserId())
                .build();
    }

    public static SellerDto getAnonymous() {
        return SellerDto.builder()
                .id(0L)
                .roleType(RoleType.MEMBER)
                .build();
    }



}
