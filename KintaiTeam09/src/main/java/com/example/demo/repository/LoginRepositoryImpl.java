package com.example.demo.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.demo.entity.Login;

@Repository
public class LoginRepositoryImpl implements LoginRepository {
	private final Map<String, Login> logins = new HashMap<>();

	@Override
	public void save(Login login) {
        logins.put(login.getUserId(), login);
    }

	@Override
    public boolean findByUserId(Login login) {
		boolean resultUser = login.getUserId().equals("user002");
		boolean resultPass = login.getPass().equals("5678");
		boolean result = resultUser && resultPass;
		
        System.out.println(login.getUserId());
        System.out.println(login.getPass());
        return result;
    }
}
