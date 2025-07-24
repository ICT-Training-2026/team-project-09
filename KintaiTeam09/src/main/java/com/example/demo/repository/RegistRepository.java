package com.example.demo.repository;

import com.example.demo.entity.Regist;

//インタフェース（抽象メソッドを定義）
public interface RegistRepository {
	// 勤怠情報を登録するメソッド（インフラ層）
	void add(Regist regist);
}
