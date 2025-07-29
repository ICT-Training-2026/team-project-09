package com.example.demo.controller;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RegistController {
	
	@Autowired
    private final RegistService registService;

    @GetMapping("/regist")
	public String regist(@ModelAttribute RegistForm registForm,
			Model model, HttpSession session) {
		if (session.getAttribute("userId") != null) {
			// 入力日の日付を取得
			long miliseconds = System.currentTimeMillis();
			Date today = new Date(miliseconds);
			
			// ログイン中のユーザID取得
			String loginUser = (String)session.getAttribute("userId");
			// 入力日の月、その月の累積超過時間を取得
			int currentMonth = LocalDate.now().getMonthValue();
			BigDecimal cumOverTime = registService.loadCumOverTime(loginUser, currentMonth);
			// 残り有給休暇日数を取得
			BigDecimal numPaidHoliday = registService.loadNumPaidHoliday(loginUser);
			System.out.println("有給残り:" + numPaidHoliday);
			registForm.setAnnualLeaveDays(numPaidHoliday);
			
			
			// 初期値のセット
			registForm.setUserId(loginUser);
			registForm.setDate(today);
			registForm.setBreakTime(BigDecimal.valueOf(60));
			registForm.setCumOverTime(cumOverTime);
			model.addAttribute("cumOverTimeHour", cumOverTime.intValue() / 60);
			model.addAttribute("cumOverTimeMinutes", cumOverTime.intValue() % 60);
			model.addAttribute("numPaidHoliday", numPaidHoliday);
			
			return "regist"; //regist.html(仮称)を表示
		} else {
			return "redirect:/login";
		}
		
	}
    
    
    
	
	@GetMapping("/success-page") // このメソッドを追加
	public String showSuccessPage() {
	    return "regist_complete"; // templates/success-page.html を返す
	}

	@PostMapping("/regist-post")
	public ModelAndView registPost(@Validated @ModelAttribute RegistForm registForm,
			BindingResult result,HttpSession session, Model model) {
		ModelAndView modelAndView = new ModelAndView("regist");
		
		// ログイン中のユーザID取得
		String loginUser = (String) session.getAttribute("userId");
		// 入力日の月、その月の累積超過時間を取得
		int currentMonth = LocalDate.now().getMonthValue();
		BigDecimal cumOverTime = registService.loadCumOverTime(loginUser, currentMonth);
		// 残り有給休暇日数を取得
		BigDecimal numPaidHoliday = registService.loadNumPaidHoliday(loginUser);
		registForm.setAnnualLeaveDays(numPaidHoliday);
		
		
		//振出を取得●
		int numHurisyutsu = registService.loadNumHurisyutsu(loginUser); 
		registForm.setHurisyutsuCount(numHurisyutsu);// 新しいセッター
		System.out.println(numHurisyutsu);

        //振休を取得●
		int numHurikyu = registService.loadNumHurikyu(loginUser);
		registForm.setHurikyuCount(numHurikyu); // 新しいセッター
		System.out.println(numHurikyu);

		// エラーの数を取得
	    int errorCount = result.getErrorCount();


	    if (result.hasErrors()) {
	        System.out.println("入力不備あり");
	        
	     // --- ここから追加・修正 ---
//	        System.out.println("--- BindingResult Errors ---");

	        // バリデーションのエラーメッセージを全て取得
	        List<String> errorMessages = new ArrayList<>();
	        for (ObjectError error : result.getAllErrors()) {
	            System.out.println(error);
	            System.out.println("  Message: " + error.getDefaultMessage());

	            errorMessages.add(error.getDefaultMessage());
	        }
	        
	        //アルファベット順にソート
	        errorMessages.sort(String::compareTo);

	        modelAndView.addObject("errorMessages", errorMessages);
	        model.addAttribute("cumOverTimeHour", cumOverTime.intValue() / 60);
	        model.addAttribute("cumOverTimeMinutes", cumOverTime.intValue() % 60);
	        model.addAttribute("numPaidHoliday", numPaidHoliday);

//	        System.out.println("--------------------------");
	        // --- ここまで追加・修正 ---
	        
	        
	        model.addAttribute("cumOverTimeHour", cumOverTime.intValue() / 60);
			model.addAttribute("cumOverTimeMinutes", cumOverTime.intValue() % 60);
			model.addAttribute("numPaidHoliday", numPaidHoliday);
	        

	        if (errorCount > 1) { // 複数エラーがある場合
	        	modelAndView.addObject("hasErrors", true);
	        } else { // 単一エラーの場合 (hasErrors=falseにすることで個別エラー表示を許可)
	        	modelAndView.addObject("hasErrors", false);
	        }
	        return modelAndView;
	    } else { // バリデーションエラーがなかった場合のみ処理を進める
	        System.out.println("入力不備なし");

		    registForm.combineDateTime();
		    registForm.culcWorkTime();
		    registForm.culcActualWorkTime();
		    registForm.culcOverTime();

			Regist regist = new Regist();
			regist.setUserId(registForm.getUserId()) ;
			regist.setDate(registForm.getDate());
			regist.setWorkStatus(registForm.getWorkStatus());
			regist.setClockIn(registForm.getClockIn());
			regist.setClockOut(registForm.getClockOut());
			regist.setWorkTime(registForm.getWorkTime());
			regist.setActualWorkTime(registForm.getActualWorkTime());
			regist.setBreakTime(registForm.getBreakTime());
			regist.setOverTime(registForm.getOverTime());
			regist.setCumOverTime(registForm.getCumOverTime());
			regist.setNote(registForm.getNote());
			
            try {
                registService.add(regist);
            } catch (IllegalArgumentException e) {
                modelAndView.addObject("errorMessage", e.getMessage());
                
                return modelAndView;
            }

            return new ModelAndView("redirect:/success-page"); // 成功時はリダイレクトが良いでしょう
			
	
//			registService.add(regist);
//
//			return new ModelAndView("redirect:/success-page"); // 成功時はリダイレクトが良いでしょう
	    }
	}
}
