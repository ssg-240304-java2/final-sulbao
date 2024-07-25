package com.finalproject.sulbao.member.controller;

import com.finalproject.sulbao.member.model.dto.MemberDTO;
import com.finalproject.sulbao.member.model.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@Slf4j
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String memberId, @RequestParam String memberPwd, Model model){

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberId(memberId);
        memberDTO.setMemberPwd(memberPwd);
        MemberDTO resultMemberDTO = memberService.login(memberDTO);
        if(resultMemberDTO != null){
            model.addAttribute("memberId", memberId);
            model.addAttribute("nickname", resultMemberDTO.getNickname());
            log.info("login sucess ===============> {}",resultMemberDTO);
        }else{
//            TODO: 새로고침시 양싱 재 제출 현상 발생 수정 필요
            model.addAttribute("errorFlg","error");
            log.info("login fail");
        }
        return "login";
    }

}
