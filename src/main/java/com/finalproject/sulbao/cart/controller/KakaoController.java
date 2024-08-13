package com.finalproject.sulbao.cart.controller;

import com.finalproject.sulbao.cart.dto.ApproveResponse;
import com.finalproject.sulbao.cart.dto.ReadyResponse;

import com.finalproject.sulbao.cart.service.KakaoPayService;
import com.finalproject.sulbao.cart.session.SessionUtils;
import groovy.util.logging.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Slf4j
@Controller
@RequestMapping("/payments")
public class KakaoController {
    private static final Logger log = LoggerFactory.getLogger(KakaoController.class);
    private final KakaoPayService kakaoPayService;

    @Autowired
        public KakaoController(KakaoPayService kakaoPayService) {
            this.kakaoPayService = kakaoPayService;
        }

    @PostMapping("/kakaopay")
    public @ResponseBody ReadyResponse payReady(@RequestBody Map<String, Object> params) {
        String name = (String) params.get("name");
        int totalPrice = Integer.parseInt((String) params.get("totalPrice"));


        ReadyResponse readyResponse = kakaoPayService.payReady(name, totalPrice);

        SessionUtils.addAttribute("tid", readyResponse.getTid());
        log.info("결제 고유번호: {}", readyResponse.getTid());
        return readyResponse;
    }

    @GetMapping("/kakaopay/completed")
    public String payCompleted(@RequestParam("pg_token") String pgToken) {
        String tid = SessionUtils.getStringAttributeValue("tid");
        log.info("결제승인 요청을 인증하는 토큰: " + pgToken);
        log.info("결제 고유번호: " + tid);
        // 카카오 결제 요청하기
        ApproveResponse approveResponse = kakaoPayService.payApprove(tid, pgToken);
        return "redirect:/cart";
    }
}
