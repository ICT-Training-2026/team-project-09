package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String search(@Valid @ModelAttribute ExportForm exportForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "export"; // エラーがある場合はエクスポート画面に戻す
        }
    	
    	String userId = exportForm.getUserId();
        String month = exportForm.getMonth();

        Export export = new Export(userId, month);
//        List<Regist> registList = exportService.searchAttendInfo(export);
        List<Map<String, Object>> registList = exportService.searchAttendInfo(export);
        
        if (registList.isEmpty()) {
            model.addAttribute("noData", true); // ← ここを追加
        } else {
            model.addAttribute("registList", registList); // ← 表に表示する場合に備えて
        }

        for (Map<String, Object> r : registList) {
            System.out.println("社員コード: " + r.get("user_code"));
            System.out.println("日付: " + r.get("date"));
            
            //始業時間
            Object clockInObj = r.get("clock_in");
            LocalDateTime clockInDateTime = (LocalDateTime) clockInObj;
            // 時を取得
            int Inhour = clockInDateTime.getHour();            
            System.out.println("始業時間の時: " + Inhour);
            // 分を取得
            int Inmin = clockInDateTime.getMinute();            
            System.out.println("始業時間の分: " + Inmin);
            
          //終業時間
            Object clockOutObj = r.get("clock_out");
            LocalDateTime clockOutDateTime = (LocalDateTime) clockOutObj;
            // 時を取得
            int Outhour = clockOutDateTime.getHour();            
            System.out.println("終業時間の時: " + Outhour);
            // 分を取得
            int Outmin = clockOutDateTime.getMinute();            
            System.out.println("終業時間の分: " + Outmin);
            
            System.out.println("実労働時間: " + r.get("actual_worktime") + " 分");
            System.out.println("休憩時間: " + r.get("breaktime") + " 分");
            System.out.println("累積超過時間: " + r.get("cum_overtime") + " 分");
            System.out.println("------------------------------------------------");
        }

        return "export";
    }
    
    @GetMapping("/export/download")
    public void downloadCsv(@ModelAttribute ExportForm exportForm, HttpServletResponse response) throws Exception {
        String userId = exportForm.getUserId();
        String month = exportForm.getMonth();

        Export export = new Export(userId, month);
        List<Map<String, Object>> registList = exportService.searchAttendInfo(export);

     // ファイル名を「kintai_ユーザーID_年月.csv」に
        String fileName = String.format("kintai_%s_%s.csv", userId, month);

        // レスポンスヘッダー設定
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (var writer = response.getWriter()) {
            // ✅ ヘッダー出力
            writer.println("社員コード,年月,始業時間（時）,始業時間（分）,終業時間（時）,終業時間（分）,労働時間（分）,休憩時間（分）,累積超過時間（分）");

            for (Map<String, Object> r : registList) {
                String userCode = String.valueOf(r.get("user_code"));
                //年月
//                String date = String.valueOf(r.get("date"));
//                String ym = date.substring(0, 7).replace("-", "/"); // "2025-07"
                //年月日
                String rawDate = String.valueOf(r.get("date")); // 例: "2025-07-01"
                String fullDate = rawDate.replace("-", "/");    // → "2025/07/01"

                
                LocalDateTime clockIn = (LocalDateTime) r.get("clock_in");
                LocalDateTime clockOut = (LocalDateTime) r.get("clock_out");

                String inHour = String.valueOf(clockIn.getHour());
                String inMin = String.format("%02d", clockIn.getMinute());
                String outHour = String.valueOf(clockOut.getHour());
                String outMin = String.format("%02d", clockOut.getMinute());

                String worktime = String.valueOf(r.get("actual_worktime"));
                String breaktime = String.valueOf(r.get("breaktime"));
                String overtime = String.valueOf(r.get("cum_overtime"));

                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                        userCode, fullDate, inHour, inMin, outHour, outMin, worktime, breaktime, overtime);
            }
        }
    }


}