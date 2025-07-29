package com.example.demo.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Employee;
import com.example.demo.entity.Login;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LoginRepositoryImpl implements LoginRepository {
	private final JdbcTemplate jdbcTemplate;

	// DB上のユーザ情報を取得するメソッド
	@Override
	public Employee loadAccoundInfo(Login login) {      
        String sqlEmp = "SELECT user_code, password, mail_address, name, " +
        		     "department_code, num_paid_holiday, birth_month, birth_day" +
        		     " FROM employees WHERE user_code = ? AND password = ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlEmp, login.getUserId(), login.getPass());
		
        
        if (list.isEmpty()) {
            return null;
        }
        
        Map<String, Object> one = list.get(0);
        Employee employee = new Employee();
        employee.setUserId((String) one.get("user_code"));
        employee.setPass((String) one.get("password"));
        employee.setMail((String) one.get("mail_address"));
        employee.setName((String) one.get("name"));
        employee.setDepartmentCode((BigDecimal) one.get("department_code"));
        employee.setNumPaidHoliday((BigDecimal) one.get("num_paid_holiday"));
        
		if (one.get("birth_month") != null && one.get("birth_day") != null) {
			employee.setBirthMonth((int) one.get("birth_month"));
			employee.setBirthDay((int) one.get("birth_day"));
		} else {
			employee.setBirthMonth(0);
			employee.setBirthDay(0);
		}
        
        
        String sqlDep = "SELECT department FROM department WHERE department_code = ?";
        List<Map<String, Object>> listDep = jdbcTemplate.queryForList(sqlDep, (BigDecimal) one.get("department_code"));
        Map<String, Object> oneDep = listDep.get(0);
        employee.setDepartmentName((String) oneDep.get("department"));
    	
    	return employee;
    }
}
