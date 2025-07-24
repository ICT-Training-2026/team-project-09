package com.example.demo.form;

import java.time.LocalDate;

import lombok.Data;

@Data
public class SearchEditForm {
	private String searchUserId;
	private LocalDate searchDate;
}
