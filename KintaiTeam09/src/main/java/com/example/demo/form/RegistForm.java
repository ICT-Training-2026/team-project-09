package com.example.demo.form;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class RegistForm {
	
	private String userId;
	private Date date;
	private Integer workStatus;
	private Timestamp clockIn;
	private Timestamp clockOut;
	private Integer actualWorkTime;
	private Integer BreakTime;
	private Integer cumOverTime;
	private String note;
	
}
