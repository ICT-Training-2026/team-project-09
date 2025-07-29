//package com.example.demo.repository;
//
//public class LoginRepository {
//
//}


package com.example.demo.repository;

import com.example.demo.entity.Employee;
import com.example.demo.entity.Login;

public interface LoginRepository {

	// DB上のユーザ情報を取得するメソッド
    Employee loadAccoundInfo(Login login);
}