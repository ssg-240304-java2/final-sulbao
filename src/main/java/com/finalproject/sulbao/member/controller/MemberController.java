package com.finalproject.sulbao.member.controller;

import com.finalproject.sulbao.login.model.service.LoginService;
import com.finalproject.sulbao.member.dto.MemberDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/member")
@Slf4j
public class MemberController {

    private final LoginService service;
    public MemberController(LoginService service) {
        this.service = service;
    }

    @GetMapping("/memberList")
    public String memberList(Model model) {

        List<MemberDto> memberList = service.findMemberList();
        model.addAttribute("memberList", memberList);

        return "admin/member/memberList";
    }

    @GetMapping("/proList")
    public String proMemberList(Model model){

        List<MemberDto> memberList = service.findProMemberList();
        model.addAttribute("memberList", memberList);

        return "admin/member/proMemberList";
    }

    @GetMapping("/sellerList")
    public String sellerList(Model model){

        List<MemberDto> sellerList = service.findSellerList();
        model.addAttribute("sellerList", sellerList);
        return "admin/member/sellerList";
    }
}
