package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo.form.ExportForm;
import com.example.demo.form.InquiryForm;

public class ExportController {

	@GetMapping("/inquiry")
	public String inquiry(@ModelAttribute InquiryForm inquiryForm) {
		return "inquiry"; //inquiry.html(仮称)を表示
	}
	
	@GetMapping("/export")
	public String export(@ModelAttribute ExportForm exportForm) {
		return "export"; //export.html(仮称)を表示
	}

	
	
}

