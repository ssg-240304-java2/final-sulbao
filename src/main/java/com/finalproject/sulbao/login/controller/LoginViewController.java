package com.finalproject.sulbao.login.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class LoginViewController {


    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "loginError", required = false) String loginError,
                        Model model) {
        if (error != null) {
            model.addAttribute("loginError", loginError);
        }
        return "auth/login";
    }

    @GetMapping("/signup")
    public String signUpVerifyageCheck(HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(true);
        String adultFlag = (String) session.getAttribute("verifyage");

        if ("Y".equals(adultFlag)) {

            String id = (String) session.getAttribute("id");
            String gender = (String) session.getAttribute("gender");
            model.addAttribute("id", id);
            model.addAttribute("gender", gender);

            return "auth/signup";
        } else {
            session.invalidate();
            return "auth/verifyagePage";
        }
    }

    @GetMapping("/verifyagePage")
    public String verifyagePage() {
        return "auth/verifyagePage";
    }

}
