package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LogoutController {

@PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // セッションを破棄
        return "top";	//templateに置かれている場合MVCのコントローラからのみ表示可能(top.htmlを表示させるためのコントローラが必要)
    }

}