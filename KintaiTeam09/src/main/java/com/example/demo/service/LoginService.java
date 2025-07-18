package com.example.demo.service;

public class LoginService {
	
	public boolean authenticate(String username, String password) {
	    User user = userRepository.findByUsername(username);
	    if (user == null || !user.getPassword().equals(password)) {
	        return false; // 認証失敗
	    }
	    return true; // 認証成功
	}	

}