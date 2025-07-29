package com.example.demo.controller;

import java.time.LocalDate;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Employee;
import com.example.demo.entity.Login;
import com.example.demo.form.LoginForm;
import com.example.demo.service.LoginService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

	@Autowired
	private final LoginService loginService;

	//ログイン画面に遷移
	@GetMapping("/login")
	public String login(@ModelAttribute LoginForm loginForm) {
		return "login"; 
	}

	//ログイン情報を送信
	@PostMapping("/login-post")
	public String loginPost(@ModelAttribute LoginForm loginForm, Model model,
							@RequestParam String userId,
							@RequestParam String pass,
							HttpSession session) {
		// ログイン処理の実行
		Login login = new Login(loginForm.getUserId(), loginForm.getPass());
		Employee employee = loginService.loadAccoundInfo(login);

		
		// DB上にユーザ情報が存在する場合（ログイン成功）
		if (employee != null) { 
			// 現在の日付を取得
	        LocalDate today = LocalDate.now();
	        // 月と日をint型の変数に格納
	        int month = today.getMonthValue();
	        int day = today.getDayOfMonth();
			// セッションにユーザIDを保存
			session.setAttribute("userId",userId);
			session.setAttribute("departmentName",employee.getDepartmentName());
			
			
			// 誕生日のユーザは特別ページに遷移
			if (employee.getBirthMonth() == month && employee.getBirthDay() == day) {
				return "happy_top_menu";
			} else {
				return "top_menu";
			}
		}
		
		// DB上にユーザ情報が存在しない場合（ログイン失敗）
		else { 
			model.addAttribute("errorMessage", "ユーザーIDまたはパスワードが間違っています。");
			return "login";
		}

	}

}
