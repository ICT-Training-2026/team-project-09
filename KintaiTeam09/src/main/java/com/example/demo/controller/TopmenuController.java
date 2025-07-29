package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo.form.RegistForm;
import com.example.demo.form.SearchEditForm;

@Controller
public class TopmenuController {


	@GetMapping("/search")
    public String search(@ModelAttribute RegistForm registForm, @ModelAttribute SearchEditForm searchEditForm,
    		HttpSession session) {
		if (session.getAttribute("userId") != null) {
//			// 入力日の日付を取得
//			LocalDate today = LocalDate.now();
			
			// 初期値のセット
			searchEditForm.setSearchUserId((String)session.getAttribute("userId"));
//			searchEditForm.setSearchDate(today);
			
			return "search"; //search.html(仮称)を表示
		} else {
			return "redirect:/login";
		}
        
    }

	
	@GetMapping("/inquiry")
    public String inquiry() {
        return "export"; //inquiry(仮称)を表示
    }
	
}