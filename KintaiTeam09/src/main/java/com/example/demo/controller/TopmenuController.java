package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TopmenuController {

//	@GetMapping("/regist")
//    public String regist(@ModelAttribute RegistForm registForm) {
//        return "regist"; //regist.html(仮称)を表示
//    }

	@GetMapping("/search")
    public String search() {
        return "search"; //search.html(仮称)を表示
    }
	
	@GetMapping("/export")
    public String export() {
        return "export"; //export(仮称)を表示
    }
	
}
