package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Login;
import com.example.demo.form.LoginForm;
import com.example.demo.service.LoginService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class LoginController {
	
	@Autowired
    private final LoginService loginService;
	
	@GetMapping("/login")
	public String login(@ModelAttribute LoginForm loginForm) {
	    return "login"; //ログイン画面に遷移
	}
	
	@PostMapping("/login-post")
    public String loginPost(@ModelAttribute LoginForm loginForm, Model model) {
        // ログイン処理の実行
        Login login = new Login(loginForm.getUserId(), loginForm.getPass());
        boolean result = loginService.execute(login);

        // ログイン処理の成否によって処理を分岐
        if (result) { // ログイン成功時
            // Modelにユーザー情報を追加
            model.addAttribute("userId", loginForm.getUserId());
            return "top_menu";
        } else { // ログイン失敗時
            model.addAttribute("errorMessage", "ユーザーIDまたはパスワードが間違っています。");
            return "login";
        }
    }
	
	
	// 本実装
//	@GetMapping("/logout")
//	public String logout(HttpSession session) {
//	    session.invalidate(); // セッションを無効化
//	    return "redirect:/top"; // トップ画面にリダイレクト
//	}
	
	
	
    }
