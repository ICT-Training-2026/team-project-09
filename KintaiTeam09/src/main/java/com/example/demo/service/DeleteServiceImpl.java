package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Regist;
import com.example.demo.repository.DeleteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteServiceImpl implements DeleteService {
	
	private final DeleteRepository deleteRepository;
	
	// JdbcTemplateオブジェクトを利用
//	private final JdbcTemplate jdbcTemplate;
	
	@Override
	public void delete(Regist delete) {
		
		deleteRepository.delete(delete); // deleteメソッドを呼び出し
	}

}
