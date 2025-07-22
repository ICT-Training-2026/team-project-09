package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Regist;
import com.example.demo.form.RegistForm;
import com.example.demo.service.RegistService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RegistController {
	
	@Autowired
	private final RegistService registService;

	@GetMapping("/regist")
	public String regist(@ModelAttribute RegistForm registForm) {
		return "regist"; //regist.html(仮称)を表示
	}


	@PostMapping("/regist-post")
	public String registPost(@ModelAttribute RegistForm registForm, Model model) {
		
		registForm.combineDateTime();
		Regist regist = new Regist(
				registForm.getUserId(),
				registForm.getDate(),
				registForm.getWorkStatus(),
				registForm.getClockIn(),
				registForm.getClockOut(),
				registForm.getActualWorkTime(),
				registForm.getBreakTime(),
				registForm.getCumOverTime(),
				registForm.getNote()
				);
		
		registService.add(regist);
		
//		System.out.println(regist.getUserId());
//		System.out.println(regist.getDate());
//		System.out.println(regist.getWorkStatus());
//		System.out.println(regist.getClockIn());
//		System.out.println(regist.getClockOut());
//		System.out.println(regist.getActualWorkTime());
//		System.out.println(regist.getBreakTime());
//		System.out.println(regist.getCumOverTime());
//		System.out.println(regist.getNote());
		
		return "regist";

	}

}
