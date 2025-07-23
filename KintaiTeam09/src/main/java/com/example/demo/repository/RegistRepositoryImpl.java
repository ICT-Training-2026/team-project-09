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
		System.out.println("ID:" + regist.getUserId());
		System.out.println("日付:" + regist.getDate());
		System.out.println("区分:" + regist.getWorkStatus());
		System.out.println("出勤時刻:" + regist.getClockIn());
		System.out.println("退勤時刻:" + regist.getClockOut());
		System.out.println("実労働時間" + regist.getActualWorkTime());
		System.out.println("休憩時間" + regist.getBreakTime());
		System.out.println("累積超過時間" + regist.getCumOverTime());
		System.out.println("備考:" + regist.getNote());
		
		// DB登録処理を以下に実装
		
	}
}
