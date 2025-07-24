package com.example.demo.repository;

import com.example.demo.entity.Regist;
import com.example.demo.entity.SearchEdit;

//インタフェース（抽象メソッドを定義）
public interface SearchEditRepository {
	// 勤怠情報を検索するメソッド（インフラ層）
	Regist searchAttendInfo(SearchEdit searchEdit);
}
