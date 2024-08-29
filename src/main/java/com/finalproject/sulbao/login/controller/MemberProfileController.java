package com.finalproject.sulbao.login.controller;

import com.finalproject.sulbao.login.model.dto.MemberProfileDto;
import com.finalproject.sulbao.login.model.dto.ProFormDto;
import com.finalproject.sulbao.login.model.service.LoginService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/mypage")
@Slf4j
public class MemberProfileController {

    private final LoginService service;
    public MemberProfileController(LoginService service) {
        this.service = service;
    }

    // 프로필 조회
    @GetMapping("/myprofile")
    public String myProfilePage(Model model) {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String status = service.findProStatusByUserId(userId);
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

        model.addAttribute("profileImg", defaultUrlCheck(member.getProfileImg().getSaveImgUrl()));
        model.addAttribute("profileName", member.getProfileName());
        model.addAttribute("profileText", profileText);
        model.addAttribute("email", member.getEmail());
        model.addAttribute("birth", member.getBirth());
        model.addAttribute("phone", phone);
        model.addAttribute("gender", member.getGender());
        model.addAttribute("proStatus", status);
        model.addAttribute("businessNumber", member.getBusinessNumber());
        model.addAttribute("businessLink", member.getBusinessLink());
        model.addAttribute("date", member.getDate());
        model.addAttribute("menu","myinfo");

        return "mypage/myprofile";
    }

    // 프로필 디폴트 이미지 여부
    private String defaultUrlCheck(String saveImgUrl) {
        if(saveImgUrl == null || saveImgUrl.isEmpty()){
            saveImgUrl = "https://kr.object.ncloudstorage.com/sulbao-file/profile/default-profile.png";
        }
        return saveImgUrl;
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
    public String saveProfile(@ModelAttribute MemberProfileDto memberProfile, RedirectAttributes redirectAttributes, HttpSession session){

        String message ="";
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            service.updateMemberInfo(memberProfile, userId, session);
            message = "프로필이 업데이트 되었습니다.";
        } catch (Exception e) {
            message = "프로필 업데이트 중 오류가 발생했습니다. 나중에 다시 시도해주세요.";
            log.info("Error =============================== {}", e.toString());
        }


        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/mypage/myprofile";
    }

    // 전문가 신청 페이지 이동
    @GetMapping("/proform")
    public String proFormPage(Model model) {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String status = service.findProStatusByUserId(userId);
        log.info("pro status============================================= {}", status);
        MemberProfileDto member = service.findMemberByUserId(userId);

        model.addAttribute("proStatus", status);
        model.addAttribute("email", member.getEmail());
        model.addAttribute("businessNumber", member.getBusinessNumber());
        model.addAttribute("businessLink", member.getBusinessLink());
        model.addAttribute("date", member.getDate());
        model.addAttribute("menu","pro");

        return "mypage/pro-form";
    }

    // 전문가 신청 저장
    @PostMapping("/saveProForm")
    public String saveProForm(@ModelAttribute ProFormDto form, RedirectAttributes redirectAttributes){

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        service.saveProForm(form, userId);
        redirectAttributes.addFlashAttribute("message", "성공적으로 저장되었습니다. 승인 심사 결과는 메일로 전송됩니다.");
        return "redirect:/mypage/proform";
    }

    // 전문가 신청 취소
    @PostMapping("/cancelProForm")
    public String deleteProForm(RedirectAttributes redirectAttributes) {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        service.deleteProForm(userId);
        redirectAttributes.addFlashAttribute("message", "전문가 신청이 취소되었습니다.");
        return "redirect:/mypage/proform";
    }

    // 회원 탈퇴
    @PostMapping("/deleteAccount")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAccount(HttpSession session, HttpServletRequest request){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        service.deleteAccount(userId);
        session.invalidate();

        try {
            request.logout(); // 로그아웃 처리
        } catch (ServletException e) {
            e.printStackTrace(); // 로그아웃 실패 처리
        }
    }
}
