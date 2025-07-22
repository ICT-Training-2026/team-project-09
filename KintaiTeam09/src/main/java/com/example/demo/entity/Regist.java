package com.example.demo.entity;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Regist {
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
