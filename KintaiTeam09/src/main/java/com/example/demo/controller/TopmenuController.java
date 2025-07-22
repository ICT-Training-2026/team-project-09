package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TopmenuController {

//	@GetMapping("/regist")
//    public String regist(@ModelAttribute RegistForm registForm) {
//        return "regist"; //regist.html(仮称)を表示
//    }

	@GetMapping("/edit")
    public String edit() {
        return "edit"; //edit.html(仮称)を表示
    }
	
	@GetMapping("/export")
    public String export() {
        return "export"; //export(仮称)を表示
    }
	
}
