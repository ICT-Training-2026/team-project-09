package com.example.demo.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    
    @Override
    public Map<String, List<Map<String, Object>>> searchAllUsersAttendInfo(String month) {
        List<Map<String, Object>> rawList = exportRepository.findByMonthForAllUsers(month);
        return rawList.stream().collect(Collectors.groupingBy(r -> String.valueOf(r.get("user_code"))));
    }
}