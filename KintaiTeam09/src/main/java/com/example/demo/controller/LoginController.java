package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.form.LoginForm;
import com.example.demo.service.LoginService;

import ch.qos.logback.core.model.Model;


@Controller
public class LoginController {

	@GetMapping("/logout")
	public String logout(HttpSession session) {
	    session.invalidate(); // セッションを無効化
	    return "redirect:/top"; // トップ画面にリダイレクト
	}
	
	@PostMapping("/login")
	public String login(@ModelAttribute LoginForm form, Model model) {
	    boolean isAuthenticated = LoginService.authenticate(form.getUsername(), form.getPassword());
	    if (!isAuthenticated) {
	        model.addAttribute("error", "ログインに失敗しました。ユーザー名またはパスワードが間違っています。");
	        return "login"; // ログイン画面に戻る
	    }
	    return "redirect:/top"; // 認証成功時はトップ画面にリダイレクト
	}
	
    }
