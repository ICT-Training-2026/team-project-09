package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Regist;
import com.example.demo.repository.RegistRepository;

import lombok.RequiredArgsConstructor;

// RegistServiceインタフェースを実装
@Service
@RequiredArgsConstructor
public class RegistServiceImpl implements RegistService {
	
	// RegistRepositoryインタフェースを実装しているBeanをDI
	private final RegistRepository registRepository;
	
	// 勤怠情報を登録するメソッド（インフラ層にデータを受け渡す）
	@Override
	public void add(Regist regist) {
		// registRepositoryImplのaddメソッドを呼び出し
		registRepository.add(regist);
	}
}
