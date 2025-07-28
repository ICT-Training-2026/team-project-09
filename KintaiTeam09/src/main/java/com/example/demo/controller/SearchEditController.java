package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Regist;
import com.example.demo.entity.SearchEdit;
import com.example.demo.form.RegistForm;
import com.example.demo.form.SearchEditForm;
import com.example.demo.service.SearchEditService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SearchEditController {
	
	@Autowired
	private final SearchEditService searchEditService;
//	private final RegistService registService; 
	
//	@GetMapping ("/search-display")
//	public String searchDisplay() {
		
//		return "search-display";
//	}
	
	@PostMapping ("/search-post") //検索機能
	public String search(@ModelAttribute RegistForm registForm, @ModelAttribute SearchEditForm searchEditForm, Model model) {

		SearchEdit searchEdit = new SearchEdit(searchEditForm.getSearchUserId(), searchEditForm.getSearchDate());
		Regist regist = searchEditService.searchAttendInfo(searchEdit);
		
		// 仮実装
		System.out.println("社員ID:" + regist.getUserId());
		System.out.println("日付:" + regist.getDate());
		System.out.println("勤怠区分:" + regist.getWorkStatus());
		System.out.println("出勤時刻:" + regist.getClockInTime());
		System.out.println("退勤時刻:" + regist.getClockOutTime());
		System.out.println("実労働時間:" + regist.getActualWorkTime());
		System.out.println("休憩時間:" + regist.getBreakTime());
		System.out.println("累計超過時間:" + regist.getCumOverTime());
		System.out.println("備考:" + regist.getNote());
		
		// 本実装
		model.addAttribute("userId", regist.getUserId());
		model.addAttribute("date", regist.getDate());
		model.addAttribute("workStatus", regist.getWorkStatus());
		model.addAttribute("clockInTime", regist.getClockInTime());
		model.addAttribute("clockOutTime", regist.getClockOutTime());
		model.addAttribute("actualWorkTime", regist.getActualWorkTime());
		model.addAttribute("breakTime", regist.getBreakTime());
		model.addAttribute("cumOverTime", regist.getCumOverTime());
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
			@ModelAttribute SearchEditForm searchEditForm, Model model) {
		
		registForm.combineDateTime();
	    registForm.culcWorkTime();
	    registForm.culcActualWorkTime();
	    registForm.culcOverTime();
		
//		Regist edit = new Regist();
//		edit.setUserId(registForm.getUserId()) ;
//		edit.setDate(registForm.getDate());
//		edit.setWorkStatus(registForm.getWorkStatus());
//		edit.setClockIn(registForm.getClockIn());
//		edit.setClockOut(registForm.getClockOut());
//		edit.setWorkTime(registForm.getWorkTime());
//		edit.setActualWorkTime(registForm.getActualWorkTime());
//		edit.setBreakTime(registForm.getBreakTime());
//		edit.setOverTime(registForm.getOverTime());
//		edit.setCumOverTime(registForm.getCumOverTime());
//		edit.setNote(registForm.getNote());
		
		
//		service.update(edit);
		
//		searchEditService.update(edit);
		
		return "edit"; //searchへ戻るyo!!!
	}
	
	@PostMapping("/confirm-edit") // 編集内容を送信
	public String confirmEdit(@ModelAttribute RegistForm registForm) {
		
		registForm.combineDateTime();
	    registForm.culcWorkTime();
	    registForm.culcActualWorkTime();
	    registForm.culcOverTime();
		
		Regist edit = new Regist();
		edit.setUserId(registForm.getUserId()) ;
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
		
		return "success-page";
		
	}
	
	}
	
	

