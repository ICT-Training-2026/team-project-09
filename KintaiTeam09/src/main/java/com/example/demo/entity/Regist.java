package com.example.demo.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Regist {
//	private String userId;
//	private Date date;
//	private Integer workStatus;
//	private Timestamp clockIn;
//	private Timestamp clockOut;
//	private Integer actualWorkTime;
//	private Integer BreakTime;
//	private Integer cumOverTime;
//	private String note;
	
	private String userId;
	private LocalDate date;
//	private Integer workStatus;
	private BigDecimal workStatus;

//	private LocalTime clockInTime;
//	private LocalTime clockOutTime;
	private Timestamp clockIn;
	private Timestamp clockOut;
	
//	private Integer actualWorkTime;
	private BigDecimal actualWorkTime;
//	private Integer BreakTime;
	private BigDecimal BreakTime;
//	private Integer cumOverTime;
	private BigDecimal cumOverTime;
	private String note;
}
