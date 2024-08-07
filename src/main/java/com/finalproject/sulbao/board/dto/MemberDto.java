package com.finalproject.sulbao.board.dto;

import com.finalproject.sulbao.board.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class MemberDto {

    private Long id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String ranks;
    private String profileImg;

    public static MemberDto toMemberDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .username(member.getUsername())
//                .password(member.getPassword())
                .name(member.getName())
                .email(member.getEmail())
                .ranks(member.getRanks())
                .profileImg(member.getProfileImg())
                .build();
    }

}
