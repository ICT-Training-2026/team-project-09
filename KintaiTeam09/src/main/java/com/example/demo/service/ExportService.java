package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.entity.Export;

public interface ExportService {
//    List<Regist> searchAttendInfo(Export export);
	List<Map<String, Object>> searchAttendInfo(Export export);
}