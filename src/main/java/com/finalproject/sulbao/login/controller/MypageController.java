package com.finalproject.sulbao.login.controller;

import com.finalproject.sulbao.login.model.dto.MemberProfileDto;
import com.finalproject.sulbao.login.model.repository.LoginRepository;
import com.finalproject.sulbao.login.model.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
@Slf4j
public class MypageController {

    private final LoginService service;
    private final LoginRepository repository;

    public MypageController(LoginService service, LoginRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @GetMapping("/myprofile")
    public String myProfilePage(HttpServletRequest request, HttpServletResponse response, Model model) {

//        profileName
//        profileText
//        profileImg
//        email
//        phone -> 010-5xxx-x799

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        MemberProfileDto member = service.findMemberByUserId(userId);
        model.addAttribute("profileImg", member.getProfileImg());
        model.addAttribute("profileName", member.getProfileName());
        model.addAttribute("profileText", member.getProfileText());
        model.addAttribute("email", member.getEmail());

        String phone = member.getPhone();
        if(phone != null) {
            phone = phone.substring(0, 3) + "-" + phone.charAt(3) + "***-*" + phone.substring(7, 8) + phone.substring(8, 11);
        }

        model.addAttribute("phone", phone);
        model.addAttribute("email", member.getEmail());



        return "mypage/myprofile";
    }
}
