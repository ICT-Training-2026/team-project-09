package com.example.demo.form;

import jakarta.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class ExportForm {
	@NotEmpty(message = "社員IDは必須です")
    private String userId;
	
	@NotEmpty(message = "年月は必須です")
    private String month;
}