//package com.example.demo.repository;
//
//public class LoginRepository {
//
//}


package com.example.demo.repository;

import com.example.demo.entity.Login;

public interface LoginRepository {
//    private final Map<String, Login> logins = new HashMap<>();

    void save(Login login);
    boolean findByUserId(Login login);
}