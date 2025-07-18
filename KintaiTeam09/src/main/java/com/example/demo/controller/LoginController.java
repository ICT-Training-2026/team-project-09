package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {
	
	// コメント
	@GetMapping("/login")
	public String login() {
	    return "login"; //ログイン画面に遷移
	}
	
	
//	// 仮実装(ページ遷移のみ)
//	@GetMapping("/top")
//	public String top() {
//	    return "top"; //トップ画面に遷移
//	}
	
	// 本実装
//	@GetMapping("/logout")
//	public String logout(HttpSession session) {
//	    session.invalidate(); // セッションを無効化
//	    return "redirect:/top"; // トップ画面にリダイレクト
//	}
	
	
//	// 仮実装(ページ遷移のみ)
//	@GetMapping("/topmenu")
//	public String topmenu() {
//	    return "topmenu"; // メニュー画面に遷移
//	}
	
	// 本実装
//	@PostMapping("/login")
//	public String login(@ModelAttribute LoginForm form, Model model) {
//	    boolean isAuthenticated = LoginService.authenticate(form.getUsername(), form.getPassword());
//	    if (!isAuthenticated) {
//	        model.addAttribute("error", "ログインに失敗しました。ユーザー名またはパスワードが間違っています。");
//	        return "login"; // ログイン画面に戻る
//	    }
//	    return "redirect:/top"; // 認証成功時はトップ画面にリダイレクト
//	}
	
    }
