package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Regist;
import com.example.demo.form.RegistForm;

@Controller
public class RegistController {

	@GetMapping("/regist")
	public String regist(@ModelAttribute RegistForm registForm) {
		return "regist"; //regist.html(仮称)を表示
	}

	@PostMapping("/regist-post")
	public String registPost(@ModelAttribute RegistForm registForm, Model model) {
		Regist regist = new Regist();
		regist.setNote(registForm.getNote());
		
		System.out.println(regist.getNote());
		
		return "regist";

	}

}
