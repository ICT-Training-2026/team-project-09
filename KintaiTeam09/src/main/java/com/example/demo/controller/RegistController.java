package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
	public String regist(@ModelAttribute RegistForm registForm) {
		return "regist"; //regist.html(仮称)を表示
	}


	@PostMapping("/regist-post")
	public String registPost(@Validated @ModelAttribute RegistForm registForm, Model model) {
		registForm.combineDateTime();
		Regist regist = new Regist(
				registForm.getUserId(),
				registForm.getDate(),
				registForm.getWorkStatus(),
				registForm.getClockIn(),
				registForm.getClockOut(),
				registForm.getActualWorkTime(),
				registForm.getBreakTime(),
				registForm.getCumOverTime(),
				registForm.getNote()
				);
		
		registService.add(regist);
		
//		System.out.println(regist.getUserId());
//		System.out.println(regist.getDate());
//		System.out.println(regist.getWorkStatus());
//		System.out.println(regist.getClockIn());
//		System.out.println(regist.getClockOut());
//		System.out.println(regist.getActualWorkTime());
//		System.out.println(regist.getBreakTime());
//		System.out.println(regist.getCumOverTime());
//		System.out.println(regist.getNote());
		
		return "regist";

	}

}


//
//package com.example.demo.form;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//
//import jakarta.validation.constraints.NotNull;
//
//import lombok.Data;
//
//@Data
//public class RegistForm {
//
//    @NotNull(message = "社員コードは必須です")
//    private String userId;
//
//    @NotNull(message = "日付は必須です")
//    private LocalDate date;
//
//    @NotNull(message = "勤怠区分は必須です")
//    private String workStatus;
//
//    @NotNull(message = "出勤時間は必須です")
//    private LocalTime clockIn;
//
//    @NotNull(message = "退勤時間は必須です")
//    private LocalTime clockOut;
//
//    private Integer workHours;
//    private Integer breakTime;
//    private String note;
//
//    public void combineDateTime() {
//        // 出勤時間と退勤時間のバリデーション
//        if (clockIn != null && clockOut != null && clockIn.isAfter(clockOut)) {
//            throw new IllegalArgumentException("出勤時間が退勤時間より遅いです");
//        }
//
//        // 実労働時間の計算
//        if (clockIn != null && clockOut != null) {
//            workHours = (int) java.time.Duration.between(clockIn, clockOut).toMinutes();
//        }
//
//        // 休憩時間のバリデーション
//        if (workHours != null && breakTime != null && breakTime > workHours) {
//            throw new IllegalArgumentException("休憩時間は実労働時間を超えることはできません");
//        }
//
//        // 出勤時間と退勤時間が所定の範囲内にあるかのバリデーション
//        LocalTime startOfWorkDay = LocalTime.of(8, 0);
//        LocalTime endOfWorkDay = LocalTime.of(17, 45); // 17:45 = 16:45 + 60 minutes break
//        if (clockIn != null && clockOut != null) {
//            if (clockIn.isBefore(LocalTime.of(8, 0)) || clockOut.isAfter(LocalTime.of(17, 45))) {
//                throw new IllegalArgumentException("出勤時間と退勤時間は8:00から17:45の間でなければなりません");
//            }
//            if (clockIn.isAfter(clockOut)) {
//                throw new IllegalArgumentException("出勤時間は退勤時間より前でなければなりません");
//            }
//        }
//    }
//}
