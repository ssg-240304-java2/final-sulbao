package com.finalproject.sulbao.login.controller;

import com.finalproject.sulbao.login.model.dto.EmailMessage;
import com.finalproject.sulbao.login.model.dto.EmailResponseDto;
import com.finalproject.sulbao.login.model.repository.EmailRepository;
import com.finalproject.sulbao.login.model.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    // 회원가입 이메일 인증 - 요청 시 body로 인증번호 반환하도록 작성하였음
    @PostMapping("/email")
    @ResponseBody
    public ResponseEntity sendJoinMail(@RequestParam String businessEmail) {

        EmailMessage emailMessage = EmailMessage.builder()
                .to(businessEmail)
                .subject("[술기로운한잔] 이메일 인증을 위한 인증 코드 발송")
                .build();

        String code = emailService.sendMail(emailMessage, "auth/email");

        EmailResponseDto emailResponseDto = new EmailResponseDto();
        emailResponseDto.setCode(code);

        Map<String, String> emailMap = new HashMap<>();
        emailMap.put("code", code);
        emailMap.put("id", businessEmail);

//        emailService.saveEmailConfirm(emailMap);

        return ResponseEntity.ok(emailResponseDto);
    }
}
