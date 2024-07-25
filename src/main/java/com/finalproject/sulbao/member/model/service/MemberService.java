package com.finalproject.sulbao.member.model.service;


import com.finalproject.sulbao.member.model.dto.MemberDTO;
import com.finalproject.sulbao.member.model.mapper.MemberMapper;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberMapper memberMapper;

    public MemberService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }


    public MemberDTO login(MemberDTO memberDTO) {
        return memberMapper.login(memberDTO);
    }

    public boolean checkAccount(String inputData) {
        MemberDTO resultMember = memberMapper.checkAccount(inputData);
        return resultMember != null;
    }
}
