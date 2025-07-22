package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


// 全ページ共通の処理
@Controller
public class CommonController {
	@GetMapping("/topmenu")
	public String topmenu() {
		return "top_menu"; // メニュー画面に遷移
	}

	@GetMapping("/")
	public String top() {
	    return "top"; //トップ画面に遷移
	}
}
