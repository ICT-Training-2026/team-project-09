package com.example.demo.controller;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalTime;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entity.Regist;
import com.example.demo.form.RegistForm;
import com.example.demo.service.RegistService;

import ch.qos.logback.core.model.Model;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RegistController {
	
	@Autowired
    private final RegistService registService;

    @GetMapping("/regist")
	public String regist(@ModelAttribute RegistForm registForm,
			HttpSession session) {
		if (session.getAttribute("userId") != null) {
			// 今日の日付を取得
			long miliseconds = System.currentTimeMillis();
			Date today = new Date(miliseconds);
			
			// 初期値のセット
			registForm.setUserId((String)session.getAttribute("userId"));
			registForm.setDate(today);
			registForm.setClockInTime(LocalTime.of(8, 45));
			registForm.setClockOutTime(LocalTime.of(17, 30));
			registForm.setBreakTime(BigDecimal.valueOf(60));
			
			return "regist"; //regist.html(仮称)を表示
		} else {
			return "redirect:/login";
		}
		
	}
	
	@GetMapping("/success-page") // このメソッドを追加
	public String showSuccessPage() {
	    return "success-page"; // templates/success-page.html を返す
	}

	@PostMapping("/regist-post")
	public ModelAndView registPost(@Validated @ModelAttribute RegistForm registForm, BindingResult result, Model model) {
		ModelAndView modelAndView = new ModelAndView("regist");

		// エラーの数を取得
	    int errorCount = result.getErrorCount();


	    if (result.hasErrors()) {
	        System.out.println("入力不備あり");
	        
	     // --- ここから追加・修正 ---
//	        System.out.println("--- BindingResult Errors ---");
	        for (ObjectError error : result.getAllErrors()) {
	        	System.out.println(error);
	            System.out.println("  Message: " + error.getDefaultMessage());
	            modelAndView.addObject("errorMessage", error.getDefaultMessage());
	        }
//	        System.out.println("--------------------------");
	        // --- ここまで追加・修正 ---

	        if (errorCount > 1) { // 複数エラーがある場合
	        	modelAndView.addObject("hasErrors", true);
	        } else { // 単一エラーの場合 (hasErrors=falseにすることで個別エラー表示を許可)
	        	modelAndView.addObject("hasErrors", false);
	        }
	        return modelAndView;
	    } else { // バリデーションエラーがなかった場合のみ処理を進める
	        System.out.println("入力不備なし");

		    registForm.combineDateTime();

			Regist regist = new Regist();
			regist.setUserId(registForm.getUserId()) ;
			regist.setDate(registForm.getDate());
			regist.setWorkStatus(registForm.getWorkStatus());
			regist.setClockIn(registForm.getClockIn());
			regist.setClockOut(registForm.getClockOut());
			regist.setActualWorkTime(registForm.getActualWorkTime());
			regist.setBreakTime(registForm.getBreakTime());
			regist.setCumOverTime(registForm.getCumOverTime());
			regist.setNote(registForm.getNote());
			
            try {
                registService.add(regist);
            } catch (IllegalArgumentException e) {
                modelAndView.addObject("errorMessage", e.getMessage());
                return modelAndView;
            }

            return new ModelAndView("redirect:/success-page"); // 成功時はリダイレクトが良いでしょう
			

	    }
	}

    }
