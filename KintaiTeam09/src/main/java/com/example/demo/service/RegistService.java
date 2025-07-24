package com.example.demo.service;

import com.example.demo.entity.Regist;

// インタフェース（抽象メソッドを定義）
public interface RegistService {
	// 勤怠情報を登録するメソッド（サービス層）
	void add(Regist regist);
}
