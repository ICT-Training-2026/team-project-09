package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Regist;
import com.example.demo.entity.SearchEdit;
import com.example.demo.form.SearchEditForm;
import com.example.demo.service.SearchEditService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SearchEditController {
	
	@Autowired
	private final SearchEditService searchEditService;
	
	@PostMapping ("/search-post")
	public String search(@ModelAttribute SearchEditForm searchEditForm) {
		// 仮実装（社員ID:test_userで固定）
		SearchEdit searchEdit = new SearchEdit("test_user", searchEditForm.getSearchDate());
		Regist regist = searchEditService.searchAttendInfo(searchEdit);
		
		System.out.println("社員ID:" + regist.getUserId());
		System.out.println("勤怠区分:" + regist.getWorkStatus());
//		System.out.println("出勤時刻:" + regist.getClockIn());
//		System.out.println("退勤時刻:" + regist.getClockOut());
		System.out.println("実労働時間:" + regist.getActualWorkTime());
		System.out.println("休憩時間:" + regist.getBreakTime());
		System.out.println("累計超過時間:" + regist.getCumOverTime());
		System.out.println("備考:" + regist.getNote());
		
		return "search";
	}
}
