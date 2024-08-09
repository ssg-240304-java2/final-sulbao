package com.finalproject.sulbao.login.controller;

import com.finalproject.sulbao.login.model.dto.NewMemberDTO;
import com.finalproject.sulbao.login.model.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;


@RequiredArgsConstructor
@Controller
@Slf4j
public class LoginController {

    private final LoginService loginService;

    /* 성인인증 API *///////////////////////////////////에러페이지 처리 필요
    private JSONObject jsonBody;
    @GetMapping(value = "/signupAccess/*")
    public String redirect(HttpServletRequest request, @RequestParam String code, Model model) throws ParseException {
        System.out.println("code ======================= " + request.getParameter("code"));
        RestTemplate restTemplate = new RestTemplate();
        String credentials = "JDJhJDA0JGkyLmt0MEFDNWoxZkRCWUcvVjJ6RS5zdWdwY1AyZnUxbGZscy4w:SHdHUVprLkgxYXVCQUh1";
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Basic " + encodedCredentials);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", "http://localhost:8080/signupAccess/");
        HttpEntity<MultiValueMap<String, String>> requestParams = new HttpEntity<>(params, headers);
        ResponseEntity<?> response = restTemplate.postForEntity("https://bauth.bbaton.com/oauth/token", requestParams, String.class);
        String responseBody = (String) response.getBody();
        if (response.getStatusCode() == HttpStatus.OK) {
            //토큰 호출 성공
            JSONParser parser = new JSONParser();
            jsonBody = (JSONObject) parser.parse(responseBody);

            String token_type = (String)jsonBody.get("token_type");
            String accessToken = (String)jsonBody.get("access_token");
            restTemplate = new RestTemplate();
            headers = new HttpHeaders();
            headers.set("Authorization", token_type + " " + accessToken);
            HttpEntity entity = new HttpEntity(headers);
            ResponseEntity<?> certificationsInfo = restTemplate.exchange("https://bapi.bbaton.com/v2/user/me", HttpMethod.GET, entity, String.class);
            String responseToken = (String) certificationsInfo.getBody();
            if (certificationsInfo.getStatusCode() == HttpStatus.OK) {
                JSONObject jsonBody = (JSONObject) parser.parse(responseToken);
                String adultFlag = (String)jsonBody.get("adult_flag");
                String id = (String)jsonBody.get("user_id");
                String gender = (String)jsonBody.get("gender");
                model.addAttribute("id", id);
                model.addAttribute("gender", gender);

                if ("Y".equals(adultFlag)) {
                    //인증 성공 처리

                    // 세션에 성인인증 완료 저장
                    request.getSession().invalidate();
                    HttpSession session = request.getSession(true);  // Session이 없으면 생성
                    session.setAttribute("verifyage", adultFlag);
                    session.setAttribute("id", id);
                    session.setAttribute("gender", gender);
                    session.setMaxInactiveInterval(1800); // Session이 30분동안 유지



                    return "signup";
                } else {
                    //인증 실패 처리
                    return "index";
                }
            } else {
                return "error";
            }
        } else {
            //토큰 호출 실패
            return "error";
        }
    }

    /* 로그아웃 */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request,response,
                SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }

    /* 일반멤버 회원가입 */
    @PostMapping("/regist/member")
    public String registNewMember(@ModelAttribute NewMemberDTO sign) {
        loginService.registNewMember(sign);
        return "/login";
    }
}
