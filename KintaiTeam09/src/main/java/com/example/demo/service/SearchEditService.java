package com.example.demo.service;

import com.example.demo.entity.Regist;
import com.example.demo.entity.SearchEdit;

//インタフェース（抽象メソッドを定義）
public interface SearchEditService {
	// 勤怠情報を検索するメソッド(サービス層)
	Regist searchAttendInfo(SearchEdit searchEdit);
	// 勤怠情報を編集するメソッド
	void update(Regist edit);
}

