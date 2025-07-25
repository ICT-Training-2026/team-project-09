package com.example.demo.entity;

import java.math.BigDecimal;
import java.sql.Date;
//import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Regist {
	
	private String userId;
	private Date date;
	private BigDecimal workStatus;

	private LocalTime clockInTime;
	private LocalTime clockOutTime;
	private LocalDateTime clockIn;
	private LocalDateTime clockOut;
	
	private BigDecimal workTime;
	private BigDecimal actualWorkTime;

	private BigDecimal BreakTime;
	private BigDecimal overTime;

	private BigDecimal cumOverTime;
	private String note;
	
}
