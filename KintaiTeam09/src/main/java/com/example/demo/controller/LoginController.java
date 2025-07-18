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
	
//	@PostMapping("/login")
//	public String loginPost(@ModelAttribute LoginForm loginForm, Model model) {
//		model.addAttribute("userId", loginForm.getUserId());
////		model.addAttribute("userId","user001");
//	    return "topmenu"; //トップメニュー画面に遷移
//	}
	
	@PostMapping("/login-post")
    public String loginPost(@ModelAttribute LoginForm loginForm, Model model) {
        // ログイン処理の実行
        Login login = new Login(loginForm.getUserId(), loginForm.getPass());
        boolean result = loginService.execute(login);
//        boolean result = true;

        // ログイン処理の成否によって処理を分岐
        if (result) { // ログイン成功時
            // Modelにユーザー情報を追加
            model.addAttribute("userId", loginForm.getUserId());
            return "topmenu";
        } else { // ログイン失敗時
            model.addAttribute("errorMessage", "ユーザーIDまたはパスワードが間違っています。");
            return "login";
        }
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
