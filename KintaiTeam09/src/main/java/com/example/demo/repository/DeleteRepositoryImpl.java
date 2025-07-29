package com.example.demo.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Regist;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DeleteRepositoryImpl implements DeleteRepository {

	// JdbcTemplateオブジェクトを利用
	private final JdbcTemplate jdbcTemplate;
	
	@Override
	public void delete(Regist delete) {
		
		String sql =
				 
				" DELETE                     " + 
//				"   user_code = ? ,          " + 
//				"   date = ? ,               " + 
//				"   work_status_code = ? ,   " + 
//				"   clock_in = ? ,           " + 
//				"   clock_out = ? ,          " + 
//				"   worktime = ? ,           " +
//				"   breaktime = ? ,          " +
//				"   actual_worktime = ? ,    " +
//				"   overtime = ? ,           " +
//				"   cum_overtime = ? ,       " +
//				"   note = ?                 " +
				" FROM                       " + 
				"   attend_info              " +
				" WHERE                      " + 
				"   user_code = ?            " +
				" AND date = ?               " ;
		

		
		jdbcTemplate.update(sql,
//				delete.getWorkStatus(),
//				delete.getClockIn(),
//				delete.getClockOut(),
//				delete.getWorkTime(),
//				delete.getBreakTime(),
//				delete.getActualWorkTime(),
//				delete.getOverTime(),
//				delete.getCumOverTime(),
//				delete.getNote(),
//				delete.getUserId(),
//				delete.getDate()
				delete.getUserId(),
				delete.getDate()
				

		);
	}
	
}
