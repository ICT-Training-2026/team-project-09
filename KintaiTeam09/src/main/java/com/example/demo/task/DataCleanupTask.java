package com.example.demo.task;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DataCleanupTask {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Scheduled(cron = "0 0 2 * * ?") // 毎日3時実行
    public void deleteOldAttendInfo() {
        LocalDate fiveYearsAgo = LocalDate.now().minusYears(5);
        jdbcTemplate.update("DELETE FROM attend_info WHERE date < ?", Date.valueOf(fiveYearsAgo));
    }
}
