package com.finalproject.sulbao.login.controller;

import com.finalproject.sulbao.login.model.dto.MemberProfileDto;
import com.finalproject.sulbao.login.model.repository.LoginRepository;
import com.finalproject.sulbao.login.model.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    // 프로필 조회
    @GetMapping("/myprofile")
    public String myProfilePage(Model model) {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        MemberProfileDto member = service.findMemberByUserId(userId);

        String profileText = member.getProfileText();
        if(profileText == null){
            profileText = "한 줄 소개를 입력해주세요. (50자 이내)";
        }

        String phone = member.getPhone();
        if(phone != null && phone.length() == 11) {
            phone = phone.substring(0, 3) + "-" + phone.charAt(3) + "***-*" + phone.substring(8, 11);
        } else {
            phone = "휴대폰 번호 숫자만 입력해주세요.";
        }

        model.addAttribute("profileImg", member.getProfileImg());
        model.addAttribute("profileName", member.getProfileName());
        model.addAttribute("profileText", member.getProfileText());
        model.addAttribute("email", member.getEmail());
        model.addAttribute("birth", member.getBirth());
        model.addAttribute("phone", phone);
        model.addAttribute("gender", member.getGender());

        return "mypage/myprofile";
    }

    // 실시간 닉네임 중복 체크
    @PostMapping("/checkProfileName")
    @ResponseBody
    public int checkProfileName(@RequestParam("profileName") String profileName) {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        int isDuplicate = service.isProfileNameDuplicate(profileName, userId);

        return isDuplicate;
    }

    // 프로필 업데이트
    @PostMapping("/updateProfile")
    public String saveProfile(@ModelAttribute MemberProfileDto memberProfile, RedirectAttributes redirectAttributes){

        String message ="";
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            log.info("memberProfile ========+>>>>>> {}", memberProfile);
            service.updateMemberInfo(memberProfile, userId);
            message = "프로필이 업데이트 되었습니다.";
        } catch (Exception e) {
            message = "프로필 업데이트 중 오류가 발생했습니다. 나중에 다시 시도해주세요.";
            log.info("Error =============================== updateProfile");
            log.info("Error =============================== {}", e.toString());
        }

        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/mypage/myprofile";
    }
}
