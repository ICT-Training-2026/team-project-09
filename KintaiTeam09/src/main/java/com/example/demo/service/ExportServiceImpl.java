package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Export;
import com.example.demo.repository.ExportRepository;

@Service
public class ExportServiceImpl implements ExportService {

    @Autowired
    private ExportRepository exportRepository;

    @Override
//    public List<Regist> searchAttendInfo(Export export) {
//        return exportRepository.search(export);
//    }
    public List<Map<String, Object>> searchAttendInfo(Export export) {
    	return exportRepository.search(export);
    }
}