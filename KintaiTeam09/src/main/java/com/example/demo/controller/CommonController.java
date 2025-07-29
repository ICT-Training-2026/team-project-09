package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


// 全ページ共通の処理
@Controller
public class CommonController {
	@GetMapping("/topmenu")
	public String topmenu(HttpSession session) {
		if (session.getAttribute("userId") != null) {
			return "top_menu"; // メニュー画面に遷移
		} else {
			return "redirect:/login"; // ログイン画面にリダイレクト
		}
		
	}

	@GetMapping("/")
	public String top() {
	    return "top"; //トップ画面に遷移
	}
}
