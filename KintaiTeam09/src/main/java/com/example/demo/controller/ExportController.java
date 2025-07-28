package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            return "export";
        }

        String userId = exportForm.getUserId();
        String month = exportForm.getMonth();

        if (userId == null || userId.trim().isEmpty()) {
            // 全ユーザー対象：Map → Listに変換（Java 8形式）
            Map<String, List<Map<String, Object>>> allUserData = exportService.searchAllUsersAttendInfo(month);
            List<Map<String, Object>> allData = allUserData.values()
                    .stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            if (allData.isEmpty()) {
                model.addAttribute("noData", true);
            } else {
                model.addAttribute("registList", allData);
            }

        } else {
            Export export = new Export(userId, month);
            List<Map<String, Object>> registList = exportService.searchAttendInfo(export);

            if (registList.isEmpty()) {
                model.addAttribute("noData", true);
            } else {
                model.addAttribute("registList", registList);
            }
        }

        return "export";
    }

    @GetMapping("/export/download")
    public void downloadCsv(@ModelAttribute ExportForm exportForm, HttpServletResponse response) throws Exception {
        String userId = exportForm.getUserId();
        String month = exportForm.getMonth();

        final String BOM = "\uFEFF";

        if (userId == null || userId.trim().isEmpty()) {
            // ZIPで全ユーザー分
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=\"kintai_all_" + month + ".zip\"");

            Map<String, List<Map<String, Object>>> allUserData = exportService.searchAllUsersAttendInfo(month);

            try (java.util.zip.ZipOutputStream zipOut = new java.util.zip.ZipOutputStream(response.getOutputStream())) {
                for (Map.Entry<String, List<Map<String, Object>>> entry : allUserData.entrySet()) {
                    String uid = entry.getKey();
                    List<Map<String, Object>> userData = entry.getValue();
                    String fileName = "kintai_" + uid + "_" + month + ".csv";

                    zipOut.putNextEntry(new java.util.zip.ZipEntry(fileName));
                    java.io.OutputStreamWriter writer = new java.io.OutputStreamWriter(zipOut, java.nio.charset.StandardCharsets.UTF_8);
                    java.io.PrintWriter bufferedWriter = new java.io.PrintWriter(writer, true);

                    bufferedWriter.write(BOM);
                    bufferedWriter.println("社員コード,年月,始業時間（時）,始業時間（分）,終業時間（時）,終業時間（分）,労働時間（分）,休憩時間（分）,累積超過時間（分）");

                    for (Map<String, Object> r : userData) {
                        String userCode = String.valueOf(r.get("user_code"));
                        String rawDate = String.valueOf(r.get("date")).replace("-", "/");
                        LocalDateTime clockIn = (LocalDateTime) r.get("clock_in");
                        LocalDateTime clockOut = (LocalDateTime) r.get("clock_out");

                        String inHour = String.valueOf(clockIn.getHour());
                        String inMin = String.format("%02d", clockIn.getMinute());
                        String outHour = String.valueOf(clockOut.getHour());
                        String outMin = String.format("%02d", clockOut.getMinute());

                        bufferedWriter.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                                userCode, rawDate, inHour, inMin, outHour, outMin,
                                r.get("actual_worktime"), r.get("breaktime"), r.get("cum_overtime"));
                    }

                    bufferedWriter.flush();
                    zipOut.closeEntry();
                }
            }
        } else {
            // 単一ユーザー分
            Export export = new Export(userId, month);
            List<Map<String, Object>> registList = exportService.searchAttendInfo(export);

            String fileName = String.format("kintai_%s_%s.csv", userId, month);
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            try (java.io.OutputStream os = response.getOutputStream();
                 java.io.OutputStreamWriter writer = new java.io.OutputStreamWriter(os, java.nio.charset.StandardCharsets.UTF_8);
                 java.io.PrintWriter bufferedWriter = new java.io.PrintWriter(writer)) {

                bufferedWriter.write(BOM);
                bufferedWriter.println("社員コード,年月,始業時間（時）,始業時間（分）,終業時間（時）,終業時間（分）,労働時間（分）,休憩時間（分）,累積超過時間（分）");

                for (Map<String, Object> r : registList) {
                    String userCode = String.valueOf(r.get("user_code"));
                    String rawDate = String.valueOf(r.get("date")).replace("-", "/");
                    LocalDateTime clockIn = (LocalDateTime) r.get("clock_in");
                    LocalDateTime clockOut = (LocalDateTime) r.get("clock_out");

                    String inHour = String.valueOf(clockIn.getHour());
                    String inMin = String.format("%02d", clockIn.getMinute());
                    String outHour = String.valueOf(clockOut.getHour());
                    String outMin = String.format("%02d", clockOut.getMinute());

                    bufferedWriter.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                            userCode, rawDate, inHour, inMin, outHour, outMin,
                            r.get("actual_worktime"), r.get("breaktime"), r.get("cum_overtime"));
                }

                bufferedWriter.flush();
            }
        }
    }
}
