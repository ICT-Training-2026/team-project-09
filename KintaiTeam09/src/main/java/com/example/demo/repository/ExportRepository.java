package com.example.demo.repository;

import java.util.List;
import java.util.Map;

import com.example.demo.entity.Export;

public interface ExportRepository {
//    List<Regist> search(Export export);
	List<Map<String, Object>> search(Export export);
}