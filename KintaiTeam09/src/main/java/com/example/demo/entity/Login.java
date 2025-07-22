package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Login {
    private String userId;
    private String pass;
}


//
//package com.example.demo.entity;
//
//public class Login {
//    private String userId;
//    private String password;
//
//    // コンストラクタ
//    public Login(String userId, String password) {
//        this.userId = userId;
//        this.password = password;
//    }
//
//    // Getters and Setters
//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//}