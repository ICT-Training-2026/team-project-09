package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LogoutController {

@PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // セッションを破棄
        return "top";	// top画面に遷移
    }

}