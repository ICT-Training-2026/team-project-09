package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Login;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{
    
//    @Autowired
//    private final AccountsRepository accountsRepository;
//
//    @Override
//    public boolean execute(Login login) {
//        Account account = accountsRepository.findByLogin(login);
//        return account != null;
//    }
	
	@Override
	public boolean execute(Login login) {
		
		boolean resultUser = login.getUserId().equals("user001");
		boolean resultPass = login.getPass().equals("1234");
		boolean result = resultUser && resultPass;
		
		System. out.println(login.getUserId());
		System. out.println(login.getPass());
		System. out.println(result);
		return result;
	}
}
