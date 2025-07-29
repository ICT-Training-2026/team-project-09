package com.example.demo.repository;

import java.math.BigDecimal;

import com.example.demo.entity.Regist;

//インタフェース（抽象メソッドを定義）
public interface RegistRepository {
	// 勤怠情報を登録するメソッド（インフラ層）
	void add(Regist regist);
	// 累積超過時間を取得するメソッド（インフラ層）
	BigDecimal loadCumOverTime(String userId, int month);
	// 残り有給休暇日数を取得するメソッド（インフラ層）
	BigDecimal loadNumPaidHoliday(String userId);
	
	int loadNumHurikyu(String userId);
	int loadNumHurisyutsu(String userId);
}
