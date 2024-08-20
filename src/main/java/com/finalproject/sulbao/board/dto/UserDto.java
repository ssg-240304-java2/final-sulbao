package com.finalproject.sulbao.board.dto;

import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.entity.RoleType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class UserDto {

    private Long id;
    private String profileImageFileName;
    private String profileName;
    private RoleType roleType;

    public static UserDto toUserDto(Login login) {
        return UserDto.builder()
                .id(login.getUserNo())
                .profileImageFileName(login.getMemberInfo().getProfileImg())
                .profileName(login.getMemberInfo().getProfileName())
                .roleType(login.getUserRole())
                .build();
    }

    public static UserDto getAnonymous() {
        return UserDto.builder()
                .id(0L)
                .profileImageFileName(null)
                .profileName(null)
                .roleType(RoleType.MEMBER)
                .build();
    }
}
