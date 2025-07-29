package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Regist;
import com.example.demo.form.RegistForm;
import com.example.demo.service.DeleteService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DeleteController {
	
	@Autowired
	private final DeleteService deleteService;

//	@PostMapping("/search-edit") // 編集画面へ遷移
//	public String edit(@ModelAttribute RegistForm registForm,
//			@ModelAttribute SearchEditForm searchEditForm, Model model) {
//		
//		registForm.combineDateTime();
//	    registForm.culcWorkTime();
//	    registForm.culcActualWorkTime();
//	    registForm.culcOverTime();
//		
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
		
//		return "edit"; //searchへ戻るyo!!!
//	}
	
	@PostMapping("/confirm-delete") // 削除処理
	public String confirmDelete(@ModelAttribute RegistForm registForm) {
		
		registForm.combineDateTime();
	    registForm.culcWorkTime();
	    registForm.culcActualWorkTime();
	    registForm.culcOverTime();
		
		Regist delete = new Regist();
		delete.setUserId(registForm.getUserId()) ;
		delete.setDate(registForm.getDate());
		delete.setWorkStatus(registForm.getWorkStatus());
		delete.setClockIn(registForm.getClockIn());
		delete.setClockOut(registForm.getClockOut());
		delete.setWorkTime(registForm.getWorkTime());
		delete.setActualWorkTime(registForm.getActualWorkTime());
		delete.setBreakTime(registForm.getBreakTime());
		delete.setOverTime(registForm.getOverTime());
		delete.setCumOverTime(registForm.getCumOverTime());
		delete.setNote(registForm.getNote());
		
		deleteService.delete(delete);
		
		return "sakujokanryou"; // 削除成功ページへ遷移
		
	}
}
