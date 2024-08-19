package com.finalproject.sulbao.login.controller;

import com.finalproject.sulbao.login.model.dto.MemberProfileDto;
import com.finalproject.sulbao.login.model.repository.LoginRepository;
import com.finalproject.sulbao.login.model.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/mypage")
@Slf4j
public class MemberProfileController {

    private final LoginService service;

    public MemberProfileController(LoginService service, LoginRepository repository) {
        this.service = service;
    }

    @GetMapping("/myprofile")
    public String myProfilePage(HttpServletRequest request, HttpServletResponse response, Model model) {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        MemberProfileDto member = service.findMemberByUserId(userId);
        model.addAttribute("profileImg", member.getProfileImg());
        model.addAttribute("profileName", member.getProfileName());
        model.addAttribute("profileText", member.getProfileText());
        model.addAttribute("email", member.getEmail());
        // birth date 변환 필요함
        model.addAttribute("birth", member.getBirth());

        String phone = member.getPhone();
        if(phone != null) {
            phone = phone.substring(0, 3) + "-" + phone.charAt(3) + "***-*" + phone.substring(7, 8) + phone.substring(8, 11);
        }

        model.addAttribute("phone", phone);
        model.addAttribute("gender", member.getGender());

        return "mypage/myprofile";
    }

    // 실시간 닉네임 중복 체크
    @PostMapping("/checkProfileName")
    public ResponseEntity<Map<String, Boolean>> checkProfileName(@RequestBody Map<String, String> request) {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String profileName = request.get("profileName");
        System.out.println("profileName ============================= " + profileName);
        System.out.println("userID ============================= " + userId);

        boolean isDuplicate = service.isProfileNameDuplicate(profileName, userId);

        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/saveProfile")
    public String saveProfile(@ModelAttribute MemberProfileDto member, Model model){
        // 비동기처리해줘
        //        response.("message", "성공적으로 저장되었습니다.");
        return "mypage/myprofile";
    }
}
