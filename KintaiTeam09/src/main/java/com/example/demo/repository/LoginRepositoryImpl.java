package com.example.demo.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.demo.entity.Employee;
import com.example.demo.entity.Login;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LoginRepositoryImpl implements LoginRepository {
//	private final JdbcTemplate jdbcTemplate;
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
	
	@Override
	public Employee loadAccoundInfo(Login login) {      
		// DB接続あり
//        String sql = "user_code, password, mail_address, name, department_code, num_paid_holiday FROM employees WHERE user_code = ? AND password = ?";
//        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, login.getUserId(), login.getPass());
       
		
		// DB接続なし（テスト実装、仮のリストを作成）
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("user_code", "user002");
        map.put("password", "5678");
        map.put("mail_address", "user002@example.com");
        map.put("name", "ユーザ2");
        map.put("department_code", 3);
        map.put("num_paid_holiday", 15);
        list.add(map);
        
        if (list.isEmpty()) {
            return null;
        }
        
        Map<String, Object> one = list.get(0);
        Employee employee = new Employee();
        employee.setUserId((String) one.get("user_code"));
        employee.setPass((String) one.get("password"));
        employee.setMail((String) one.get("mail_address"));
        employee.setName((String) one.get("name"));
        employee.setDepartmentCode((Integer) one.get("department_code"));
        employee.setNumPaidHoliday((Integer) one.get("num_paid_holiday"));
    	
    	return employee;
    }
}
