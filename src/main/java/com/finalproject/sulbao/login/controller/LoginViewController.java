package com.finalproject.sulbao.login.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@Slf4j
public class LoginViewController {

    @GetMapping("/login")
    public void loginPage() {
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

            return "signup";
        } else {
            session.invalidate();
            return "verifyagePage";
        }
    }

    @GetMapping("/verifyagePage")
    public void verifyagePage() {
    }
}
