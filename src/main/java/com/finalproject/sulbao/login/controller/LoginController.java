package com.finalproject.sulbao.login.controller;

import com.finalproject.sulbao.login.model.dto.LoginDetails;
import com.finalproject.sulbao.login.model.dto.SignupMemberDto;
import com.finalproject.sulbao.login.model.dto.SignupSellerDto;
import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.service.LoginService;
import com.finalproject.sulbao.login.model.vo.LoginVO;
import com.finalproject.sulbao.login.model.vo.SellerInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.List;


@RequiredArgsConstructor
@Controller
@Slf4j
public class LoginController {

    private final LoginService loginService;

    /* 성인인증 API */
    private JSONObject jsonBody;
    @GetMapping(value = "/signupAccess/**")
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

                    return "auth/signup";
                } else {
                    //인증 실패 처리
                    model.addAttribute("error-message", "술기로운 한잔은 19세 이상 성인만 가입 가능합니다.");
                    return "/";
                }
            } else {
                model.addAttribute("error-message", "성인인증에 실패하였습니다.");
                return "/";
            }
        } else {
            //토큰 호출 실패
            model.addAttribute("error-message", "성인인증에 실패하였습니다.");
            return "/";
        }
    }

    /* 회원가입 유효성 검사 - MEMBER */
    @PostMapping("/regist/member")
    public String registNewMember(@Valid @ModelAttribute("member") SignupMemberDto member, BindingResult bindingResult,
                                  HttpServletRequest httpServletRequest, Model model, RedirectAttributes redirectAttributes) {
        try{
            HttpSession session = httpServletRequest.getSession(true);
            model.addAttribute("id", session.getAttribute("id"));
            model.addAttribute("gender", session.getAttribute("gender"));
            model.addAttribute("verifyage", session.getAttribute("verifyage"));

            // 기본 항목 유효성 검사 API
            if(bindingResult.hasErrors()){
                model.addAttribute("memberDto", member);

                for(FieldError error : bindingResult.getFieldErrors()) {
                    model.addAttribute("valid_" + error.getField(), error.getDefaultMessage());
                }
                return "auth/signup";
            }

            // 비밀번호 검증
            if(!member.getUserPw().equals(member.getConfirmPw())){
                System.out.println("memberPw = " + member.getUserPw());
                System.out.println("memberConfirmPw = " + member.getConfirmPw());
                model.addAttribute("valid_confirmPw", "비밀번호가 일치하지 않습니다.");

                return "auth/signup";
            }

            // 아이디 중복 검사
            boolean exists = loginService.isUserIdExists(member.getUserId());
            if(exists) {
                model.addAttribute("valid_userId", "이미 존재하는 아이디 입니다.");

                return "auth/signup";
            }

            // 회원가입 성공
            loginService.registNewMember(member);
            redirectAttributes.addFlashAttribute("message", "성공적으로 회원가입 되었습니다.");

            return "redirect:/login";

        } catch (RuntimeException e){
            return "auth/signup";
        }
    }


    /* 사업자등록번호 중복 검사 */
    @PostMapping("/seller/number")
    @ResponseBody
    public String existNum (@RequestParam String businessNumber, Model model){

        log.info("Controller 에서 받은 businessNum :=================================== {}", businessNumber);

        SellerInfo findSeller = loginService.findSellerByNum(businessNumber);
        if(findSeller != null){
            model.addAttribute("valid_businessNumber_error", "유효하지 않은 사업자등록번호입니다.");
            return "exist";
        }
        return "null";
    }


    /* 회원가입 유효성 검사 - SELLER */
    @PostMapping("/regist/seller")
    public String registNewSeller(@Valid @ModelAttribute("sellerForm") SignupSellerDto seller, BindingResult bindingResult, Model model,
                                  RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()){
            seller.setBusinessPw(null);
            seller.setConfirmPw(null);

            // 에러 메시지와 함께 나머지 필드 값을 모델에 추가
            model.addAttribute("sellerForm", seller);

            for(FieldError error : bindingResult.getFieldErrors()) {
                model.addAttribute("valid_" + error.getField(), error.getDefaultMessage());
            }
            return "auth/signup-seller";
        }

        // 사업장명 중복 검사
        boolean existsName = loginService.isbusinessNameExists(seller.getBusinessName());
        if(existsName) {
            model.addAttribute("valid_businessName", "이미 존재하는 사업장명 입니다.");

            return "auth/signup-seller";
        }

        // 아이디 중복 검사
        boolean exists = loginService.isUserIdExists(seller.getBusinessId());
        if(exists) {
            model.addAttribute("valid_businessId", "이미 존재하는 아이디 입니다.");

            return "auth/signup-seller";
        }

        // 비밀번호 검증
        if(!seller.getBusinessPw().equals(seller.getConfirmPw())){
            model.addAttribute("valid_confirmPw", "비밀번호가 일치하지 않습니다.");

            return "auth/signup-seller";
        }

        // 회원가입 성공
        loginService.registNewSeller(seller);
        redirectAttributes.addFlashAttribute("message", "성공적으로 입점신청되었습니다. 승인 후 이메일로 전송됩니다.");

        return "redirect:/login";
    }


    /* 로그아웃 */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request,response,
                SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }

}
