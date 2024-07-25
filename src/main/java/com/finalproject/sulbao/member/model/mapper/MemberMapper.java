package com.finalproject.sulbao.member.model.mapper;

import com.finalproject.sulbao.member.model.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    MemberDTO login(MemberDTO memberDTO);

    MemberDTO checkAccount(String inputData);
}
