package com.example.demo.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.demo.entity.SearchEdit;
import com.example.demo.form.RegistForm;
import com.example.demo.form.SearchEditForm;
import com.example.demo.service.RegistService;
import com.example.demo.service.SearchEditService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SearchEditController {

	@Autowired
	private final SearchEditService searchEditService;
	private final RegistService registService;

	//	@GetMapping ("/search-display")
	//	public String searchDisplay() {

	//		return "search-display";
	//	}

	@GetMapping("/edit-success-page")
	public String showSuccessPage() {
		return "edit_complete";
	}

	// 検索クエリ(ユーザID,日付)を送信し、該当する勤怠情報を取得
	@PostMapping("/search-post")
	public String search(@ModelAttribute RegistForm registForm, @ModelAttribute SearchEditForm searchEditForm,
			Model model) {

		SearchEdit searchEdit = new SearchEdit(searchEditForm.getSearchUserId(), searchEditForm.getSearchDate());
		Regist regist = searchEditService.searchAttendInfo(searchEdit);

		if (regist == null) {
			model.addAttribute("searchErrorMessage", "該当する勤務データがありません");
			return "Search";
		}

		model.addAttribute("userId", regist.getUserId());
		model.addAttribute("date", regist.getDate());
		model.addAttribute("workStatus", regist.getWorkStatusName());
		model.addAttribute("clockInTime", regist.getClockInTime());
		model.addAttribute("clockOutTime", regist.getClockOutTime());
		model.addAttribute("actualWorkTime", regist.getActualWorkTime());
		model.addAttribute("breakTime", regist.getBreakTime());
		model.addAttribute("cumOverTimeHour", regist.getCumOverTime().intValue() / 60);
		model.addAttribute("cumOverTimeMinutes", regist.getCumOverTime().intValue() % 60);
		model.addAttribute("note", regist.getNote());

		registForm.setUserId(regist.getUserId());
		registForm.setDate(regist.getDate());
		registForm.setWorkStatus(regist.getWorkStatus());
		registForm.setClockInTime(regist.getClockInTime());
		registForm.setClockOutTime(regist.getClockOutTime());
		registForm.setActualWorkTime(regist.getActualWorkTime());
		registForm.setBreakTime(regist.getBreakTime());
		registForm.setCumOverTime(regist.getCumOverTime());
		registForm.setNote(regist.getNote()); 

		return "search";
	}

	@PostMapping("/search-edit") // 編集画面へ遷移
	public String edit(@ModelAttribute RegistForm registForm,
			@ModelAttribute SearchEditForm searchEditForm, HttpSession session, Model model) {

		registForm.combineDateTime();
		registForm.culcWorkTime();
		registForm.culcActualWorkTime();
		registForm.culcOverTime();

		// ログイン中のユーザID取得
		String loginUser = (String) session.getAttribute("userId");
		// 入力日の月、その月の累積超過時間を取得
		int currentMonth = LocalDate.now().getMonthValue();
		BigDecimal cumOverTime = registService.loadCumOverTime(loginUser, currentMonth);
		// 残り有給休暇日数を取得
		BigDecimal numPaidHoliday = registService.loadNumPaidHoliday(loginUser);
		registForm.setAnnualLeaveDays(numPaidHoliday);

		model.addAttribute("cumOverTimeHour", cumOverTime.intValue() / 60);
		model.addAttribute("cumOverTimeMinutes", cumOverTime.intValue() % 60);
		model.addAttribute("numPaidHoliday", numPaidHoliday);

		return "edit"; //searchへ戻るyo!!!
	}

	@PostMapping("/confirm-edit") // 編集内容を送信
	public ModelAndView confirmEdit(@Validated @ModelAttribute RegistForm registForm,
			BindingResult result, HttpSession session) {
		// ログイン中のユーザID取得
		String loginUser = (String) session.getAttribute("userId");
		// 入力日の月、その月の累積超過時間を取得
		int currentMonth = LocalDate.now().getMonthValue();
		BigDecimal cumOverTime = registService.loadCumOverTime(loginUser, currentMonth);
		// 残り有給休暇日数を取得
		BigDecimal numPaidHoliday = registService.loadNumPaidHoliday(loginUser);
		registForm.setAnnualLeaveDays(numPaidHoliday);

		// エラーの数を取得
		int errorCount = result.getErrorCount();
		System.out.println("エラーカウント:" + errorCount);

		if (result.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView("edit");

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
			modelAndView.addObject("cumOverTimeHour", cumOverTime.intValue() / 60);
			modelAndView.addObject("cumOverTimeMinutes", cumOverTime.intValue() % 60);
			modelAndView.addObject("numPaidHoliday", numPaidHoliday);

			if (errorCount > 1) { // 複数エラーがある場合
				modelAndView.addObject("hasErrors", true);
			} else { // 単一エラーの場合 (hasErrors=falseにすることで個別エラー表示を許可)
				modelAndView.addObject("hasErrors", false);
			}

			return modelAndView;

		} else {
			ModelAndView modelAndView = new ModelAndView("redirect:/edit-success-page");

			registForm.combineDateTime();
			registForm.culcWorkTime();
			registForm.culcActualWorkTime();
			registForm.culcOverTime();

			Regist edit = new Regist();
			edit.setUserId(registForm.getUserId());
			edit.setDate(registForm.getDate());
			edit.setWorkStatus(registForm.getWorkStatus());
			edit.setClockIn(registForm.getClockIn());
			edit.setClockOut(registForm.getClockOut());
			edit.setWorkTime(registForm.getWorkTime());
			edit.setActualWorkTime(registForm.getActualWorkTime());
			edit.setBreakTime(registForm.getBreakTime());
			edit.setOverTime(registForm.getOverTime());
			edit.setCumOverTime(registForm.getCumOverTime());
			edit.setNote(registForm.getNote());

			searchEditService.update(edit);

			return modelAndView;
		}

	}

}
