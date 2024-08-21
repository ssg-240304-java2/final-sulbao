package com.finalproject.sulbao.login.controller;

import com.finalproject.sulbao.login.model.dto.EmailConfirmDto;
import com.finalproject.sulbao.login.model.dto.EmailMessage;
import com.finalproject.sulbao.login.model.dto.EmailResponseDto;
import com.finalproject.sulbao.login.model.entity.EmailVerify;
import com.finalproject.sulbao.login.model.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final EmailService emailService;

    // 이메일 인증 코드 전송
    @PostMapping("/email")
    @ResponseBody
    public ResponseEntity sendJoinMail(@RequestParam String businessEmail, Model model) {

        EmailMessage emailMessage = EmailMessage.builder()
                .to(businessEmail)
                .subject("[술기로운한잔] 이메일 인증을 위한 인증 코드 발송")
                .build();

        String code = emailService.sendMail(emailMessage, "auth/verify-email");
        EmailResponseDto emailResponseDto = new EmailResponseDto();
        emailResponseDto.setCode(code);

        Map<String, String> emailMap = new HashMap<>();
        emailMap.put("code", code);
        emailMap.put("id", businessEmail);

        emailService.saveEmailConfirm(emailMap);
        model.addAttribute("valid_confirmEmail","이메일 인증이 완료되었습니다.");

        return ResponseEntity.ok(emailResponseDto);
    }

    // 이메일 인증 확인
    @PostMapping("/email/code")
    @ResponseBody
    public Boolean sendJoinMail(@RequestBody EmailConfirmDto emailDto, Model model) {

        String email = emailDto.getBusinessEmail();
        String code =  emailDto.getConfirmCode();

        EmailVerify emailVerify = new EmailVerify();
        emailVerify.setEmail(email);
        emailVerify.setCode(code);
        Boolean isConfirm = emailService.confirmEmailByCode(emailVerify);

        if(!isConfirm){
            return false;
        }
        model.addAttribute("valid_confirmEmail", "true");
        return true;
    }
}
