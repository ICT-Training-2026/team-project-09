package com.example.demo.repository;

import com.example.demo.entity.Regist;

public interface DeleteRepository {

	// 勤怠情報を削除する抽象メソッド
	void delete(Regist delete);
}
