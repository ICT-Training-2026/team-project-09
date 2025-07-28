package com.example.demo.form;

import jakarta.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class ExportForm {

    private String userId;
	
	@NotEmpty(message = "年月は必須です")
    private String month;
}