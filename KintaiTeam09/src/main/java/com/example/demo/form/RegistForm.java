package com.example.demo.form;


import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

//import java.sql.Date;

import lombok.Data;

@Data
public class RegistForm {
	private String userId;
//	private Date date;
	private LocalDate date;
	private Integer workStatus;

	private LocalTime clockInTime;
	private LocalTime clockOutTime;
	private Timestamp clockIn;
	private Timestamp clockOut;
	
	private Integer actualWorkTime;
	private Integer breakTime;
	private Integer cumOverTime;
	private String note;

	public void combineDateTime() {
        if (date != null && clockInTime != null && clockOutTime != null) {
            LocalDateTime clockInDateTime = LocalDateTime.of(date, clockInTime);
            LocalDateTime clockOutDateTime = LocalDateTime.of(date, clockOutTime);
            this.clockIn = Timestamp.valueOf(clockInDateTime);
            this.clockOut = Timestamp.valueOf(clockOutDateTime);
        }
    }
	
}
