package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Regist;
import com.example.demo.entity.SearchEdit;
import com.example.demo.repository.SearchEditRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchEditServiceImpl implements SearchEditService {
	
	// SearchEditRepositoryインタフェースを実装しているBeanをDI
	private final SearchEditRepository searchEditRepository;
	
	@Override
	public Regist searchAttendInfo(SearchEdit searchEdit) {
		Regist regist = searchEditRepository.searchAttendInfo(searchEdit);
		
		return regist;
		
	}
	
//	private final SearchEditRepository searchEditRepository2;
	
	@Override
	public void update(Regist edit) {
//		System.out.println("updateメソッド呼び出し完了");
		
		searchEditRepository.update(edit);
		
	}
	
}
