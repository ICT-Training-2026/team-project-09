package com.example.demo.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Export;

@Repository
public class ExportRepositoryImpl implements ExportRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
//    @SuppressWarnings("deprecation")
	@Override
    public List<Map<String, Object>> search(Export export) {
        String sql = "SELECT * FROM attend_info WHERE user_code = ? AND DATE_FORMAT(date, '%Y-%m') = ?";

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,export.getUserId(), export.getMonth() );
        
//        System.out.println(list);
        return list;
    }
	
	@Override
	public List<Map<String, Object>> findByMonthForAllUsers(String month) {
	    String sql = "SELECT * FROM attend_info WHERE DATE_FORMAT(date, '%Y-%m') = ? ORDER BY user_code ASC, date ASC";

	    List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, month);

	    return list;
	}
	

}