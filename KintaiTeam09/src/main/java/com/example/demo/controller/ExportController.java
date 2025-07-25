package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Export;
import com.example.demo.form.ExportForm;
import com.example.demo.service.ExportService;

@Controller
public class ExportController {

    @Autowired
    private ExportService exportService;

    @GetMapping("/export")
    public String exportDisplay(@ModelAttribute ExportForm exportForm) {
        return "export";
    }

    @PostMapping("/export-post")
    public String search(@ModelAttribute ExportForm exportForm, Model model) {
        String userId = exportForm.getUserId();
        String month = exportForm.getMonth();

        Export export = new Export(userId, month);
//        List<Regist> registList = exportService.searchAttendInfo(export);
        List<Map<String, Object>> registList = exportService.searchAttendInfo(export);

        for (Map<String, Object> r : registList) {
            System.out.println("社員コード: " + r.get("user_code"));
            System.out.println("日付: " + r.get("date"));
            
            //始業（時）
            Object clockInObj = r.get("clock_in");
            LocalDateTime clockInDateTime = (LocalDateTime) clockInObj;
            // 時を取得
            int hour = clockInDateTime.getHour();            
            System.out.println("始業時間の時: " + hour);
            // 分を取得
            int min = clockInDateTime.getMinute();            
            System.out.println("始業時間の分: " + min);
            
            System.out.println("始業時間: " + r.get("clock_in"));
            System.out.println("終業時間: " + r.get("clock_out"));
            System.out.println("実労働時間: " + r.get("actual_worktime") + " 分");
            System.out.println("休憩時間: " + r.get("bareaktime") + " 分");
            System.out.println("累積超過時間: " + r.get("cum_overtime") + " 分");
            System.out.println("------------------------------------------------");
        }

        return "export";
    }
}
