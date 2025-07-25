package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo.form.SearchEditForm;

@Controller
public class TopmenuController {

//	@GetMapping("/regist")
//    public String regist(@ModelAttribute RegistForm registForm) {
//        return "regist"; //regist.html(仮称)を表示
//    }

	@GetMapping("/search")
    public String search(@ModelAttribute SearchEditForm searchEditForm,
    		HttpSession session) {
		if (session.getAttribute("userId") != null) {
			// 初期値のセット
			searchEditForm.setSearchUserId((String)session.getAttribute("userId"));
			
			return "search"; //search.html(仮称)を表示
		} else {
			return "redirect:/login";
		}
        
    }

	
	@GetMapping("/export")
    public String export() {
        return "export"; //export(仮称)を表示
    }
	
}
