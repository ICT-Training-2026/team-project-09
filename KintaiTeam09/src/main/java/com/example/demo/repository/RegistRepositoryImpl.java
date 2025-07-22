package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import com.example.demo.entity.Regist;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RegistRepositoryImpl implements RegistRepository {
	
	@Override
	public void add(Regist regist) {		
		// 仮実装（コンソールに表示）
		System.out.println(regist.getUserId());
		System.out.println(regist.getDate());
		System.out.println(regist.getWorkStatus());
		System.out.println(regist.getClockIn());
		System.out.println(regist.getClockOut());
		System.out.println(regist.getActualWorkTime());
		System.out.println(regist.getBreakTime());
		System.out.println(regist.getCumOverTime());
		System.out.println(regist.getNote());
		
		// DB登録処理を以下に実装
		
	}
}
