package com.example.demo.service;

import com.example.demo.entity.Employee;
import com.example.demo.entity.Login;

public interface LoginService {

	// DB上のユーザ情報を取得するメソッド
	Employee loadAccoundInfo(Login login);
}
