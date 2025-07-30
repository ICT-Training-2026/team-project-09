package com.example.demo.controller;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.example.demo.validation.ValidatorImpl;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RegistController {

	@Autowired
	private final RegistService registService;

	private final ValidatorImpl validatorImpl;

	@GetMapping("/regist")
	public String regist(@ModelAttribute RegistForm registForm,
			Model model, HttpSession session) {
		if (session.getAttribute("userId") != null) {
			// 入力日の日付を取得
			long miliseconds = System.currentTimeMillis();
			Date today = new Date(miliseconds);

			// ログイン中のユーザID取得
			String loginUser = (String) session.getAttribute("userId");
			// 入力日の月、その月の累積超過時間を取得
			int currentMonth = LocalDate.now().getMonthValue();
			BigDecimal cumOverTimeThisMonth = registService.loadCumOverTime(loginUser, currentMonth);
			// 残り有給休暇日数を取得
			BigDecimal numPaidHoliday = registService.loadNumPaidHoliday(loginUser);
			registForm.setAnnualLeaveDays(numPaidHoliday);

			// 初期値のセット
			registForm.setUserId(loginUser);
			registForm.setDate(today);
			registForm.setBreakTime(BigDecimal.valueOf(60));
			registForm.setCumOverTimeThisMonth(cumOverTimeThisMonth);

			model.addAttribute("cumOverTimeHour", cumOverTimeThisMonth.intValue() / 60);
			model.addAttribute("cumOverTimeMinutes", cumOverTimeThisMonth.intValue() % 60);
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
			BindingResult result, HttpSession session, Model model) {
		ModelAndView modelAndView = new ModelAndView("regist");

		// ログイン中のユーザID取得
		String loginUser = (String) session.getAttribute("userId");
		// 入力日の月、その月の累積超過時間を取得
		//		int currentMonth = LocalDate.now().getMonthValue();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(registForm.getDate());
		int month = calendar.get(Calendar.MONTH) + 1;

		BigDecimal cumOverTime = registService.loadCumOverTime(loginUser, month);
		registForm.setCumOverTime(cumOverTime);

		// 残り有給休暇日数を取得
		BigDecimal numPaidHoliday = registService.loadNumPaidHoliday(loginUser);
		registForm.setAnnualLeaveDays(numPaidHoliday);

		//振出日数を取得
		int numHurisyutsu = registService.loadNumHurisyutsu(loginUser);
		registForm.setHurisyutsuCount(numHurisyutsu);

		//振休日数を取得
		int numHurikyu = registService.loadNumHurikyu(loginUser);
		registForm.setHurikyuCount(numHurikyu);

		validatorImpl.validate(registForm, result);

		// エラーの数を取得
		int errorCount = result.getErrorCount();

		if (result.hasErrors()) {
//			System.out.println("入力不備あり");

			// --- ここから追加・修正 ---
			//	        System.out.println("--- BindingResult Errors ---");

			// バリデーションのエラーメッセージを全て取得
			List<String> errorMessages = new ArrayList<>();
			for (ObjectError error : result.getAllErrors()) {


				errorMessages.add(error.getDefaultMessage());
			}

			//アルファベット順にソート
			errorMessages.sort(String::compareTo);
			
			if (registForm.getClockInTime() != null &&
					registForm.getClockOutTime() != null) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
				String clockInTimeFmt = registForm.getClockInTime().format(formatter);
				String clockOutTimeFmt = registForm.getClockOutTime().format(formatter);
				model.addAttribute("clockInTimeFmt", clockInTimeFmt);
				model.addAttribute("clockOutTimeFmt", clockOutTimeFmt);
			}

			int cumOverTimeThisMonthHour = registForm.getCumOverTimeThisMonth().intValue() / 60;
			int cumOverTimeThisMonthMinutes = registForm.getCumOverTimeThisMonth().intValue() % 60;

			modelAndView.addObject("errorMessages", errorMessages);
			model.addAttribute("cumOverTimeHour", cumOverTimeThisMonthHour);
			model.addAttribute("cumOverTimeMinutes", cumOverTimeThisMonthMinutes);
			model.addAttribute("numPaidHoliday", numPaidHoliday);
			

			if (errorCount > 1) { // 複数エラーがある場合
				modelAndView.addObject("hasErrors", true);
			} else { // 単一エラーの場合 (hasErrors=falseにすることで個別エラー表示を許可)
				modelAndView.addObject("hasErrors", false);
			}
			return modelAndView;
		} else { // バリデーションエラーがなかった場合のみ処理を進める
//			System.out.println("入力不備なし");

			registForm.combineDateTime();
			registForm.culcWorkTime();
			registForm.culcActualWorkTime();
			registForm.culcOverTime();

			Regist regist = new Regist();
			regist.setUserId(registForm.getUserId());
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

		}
	}
}
