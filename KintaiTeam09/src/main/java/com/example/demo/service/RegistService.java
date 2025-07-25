package com.example.demo.service;

import java.math.BigDecimal;

import com.example.demo.entity.Regist;

// インタフェース（抽象メソッドを定義）
public interface RegistService {
	// 勤怠情報を登録するメソッド（サービス層）
	void add(Regist regist);
	// 累積超過時間を取得するメソッド（サービス層）
	BigDecimal loadCumOverTime(String userId, int month);
}
