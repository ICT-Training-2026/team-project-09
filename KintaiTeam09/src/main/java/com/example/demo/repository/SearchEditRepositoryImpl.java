package com.example.demo.repository;

import java.math.BigDecimal;
import java.sql.Date;
//import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Regist;
import com.example.demo.entity.SearchEdit;

import lombok.RequiredArgsConstructor;

//SearchEditRepositoryインタフェースを実装
@Repository
@RequiredArgsConstructor
public class SearchEditRepositoryImpl implements SearchEditRepository {
	
	// JdbcTemplateオブジェクトを利用
		private final JdbcTemplate jdbcTemplate;
	
	@Override
	public Regist searchAttendInfo(SearchEdit searchEdit) {
		// 仮実装（検索クエリをコンソールに表示）
		System.out.println("検索対象社員ID:" + searchEdit.getSearchUserId());
		System.out.println("入力された日付:" + searchEdit.getSearchDate());
		
		// 本実装
		String sql = " SELECT * FROM attend_info " +
				" WHERE user_code=? AND date=? ";
		
		List<Map<String, Object>> list = 
				jdbcTemplate.queryForList(
				sql, searchEdit.getSearchUserId(), searchEdit.getSearchDate()
				);
		
		Map<String, Object> one = list.get(0);
		Regist regist = new Regist();
		regist.setUserId((String) one.get("user_code"));
		regist.setDate((Date) one.get("date"));
		regist.setWorkStatus((BigDecimal)one.get("work_status_code"));
		regist.setClockInTime(((LocalDateTime) one.get("clock_in")).toLocalTime());
		regist.setClockOutTime(((LocalDateTime) one.get("clock_out")).toLocalTime());
		regist.setActualWorkTime((BigDecimal)one.get("actual_worktime"));
		regist.setBreakTime((BigDecimal)one.get("breaktime"));
		regist.setCumOverTime((BigDecimal)one.get("cum_overtime"));
		regist.setNote((String)one.get("note"));
		
		
		return regist;
	}
}
