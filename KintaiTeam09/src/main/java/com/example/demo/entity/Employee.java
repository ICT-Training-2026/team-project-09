package com.example.demo.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
	private String userId;
	private String pass;
	private String mail;
	private String name;
	private BigDecimal departmentCode;
	private String departmentName;
	private BigDecimal numPaidHoliday;
	private Integer birthMonth;
	private Integer birthDay;
	
}
