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

	
	@GetMapping("/inquiry")
    public String inquiry() {
        return "inquiry"; //inquiry(仮称)を表示
    }
	
}