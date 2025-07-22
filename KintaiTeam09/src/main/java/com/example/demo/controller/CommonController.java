package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommonController {
	@GetMapping("/topmenu")
	public String topmenu() {
		return "topmenu"; // メニュー画面に遷移
	}

	// 仮実装(ページ遷移のみ)
	@GetMapping("/top")
	public String top() {
	    return "top"; //トップ画面に遷移
	}
}
