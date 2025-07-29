package com.example.demo.form;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SearchEditForm {
	private String searchUserId;
	@NotNull (message = "日付は必須です")
	private LocalDate searchDate;
	
//	private BigDecimal workStatus;
//	private LocalTime clockInTime;
//    private LocalTime clockOutTime;
//    private BigDecimal actualWorkTime;
//    private BigDecimal breakTime;
//    private BigDecimal cumOverTime;
//    private String note;
    
    
}
