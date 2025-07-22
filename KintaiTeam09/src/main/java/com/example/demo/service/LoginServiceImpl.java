
//package com.example.demo.service;
//import org.springframework.stereotype.Service;
//
//import com.example.demo.entity.Login;
//
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class LoginServiceImpl implements LoginService{
//    
//    @Autowired
//    private final AccountsRepository accountsRepository;
//
//    @Override
//    public boolean execute(Login login) {
//        Account account = accountsRepository.findByLogin(login);
//        return account != null;
//    }
//	
//	@Override
//	public boolean execute(Login login) {
//		
//		// 仮実装（DBアクセスなし）
//		boolean resultUser = login.getUserId().equals("user001");
//		boolean resultPass = login.getPass().equals("1234");
//		boolean result = resultUser && resultPass;
//		
//		// 入力した値、ログイン可否をコンソールに表示
//		System. out.println(login.getUserId());
//		System. out.println(login.getPass());
//		System. out.println(result);
//		
//		return result;
//	}
//}


package com.example.demo.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Login;
import com.example.demo.repository.LoginRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

	@Autowired
    private final LoginRepository loginRepository;

    @Override
    public boolean execute(Login login) {
        // 仮のアカウントを保存
        loginRepository.save(new Login("user001", "1234"));

        boolean result = loginRepository.findByUserId(login);
//        boolean result = login != null && login.getPass().equals(login.getPass());

        // 入力した値、ログイン可否をコンソールに表示
        System.out.println(login.getUserId());
        System.out.println(login.getPass());
        System.out.println(result);

        return result;
//        return true;
    }
    
}



