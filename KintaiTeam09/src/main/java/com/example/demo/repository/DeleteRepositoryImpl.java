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
	public void delete(Regist delete) { // 削除処理のSQL
		
		String sql =
				 
				" DELETE                     " + 
				" FROM                       " + 
				"   attend_info              " +
				" WHERE                      " + 
				"   user_code = ?            " +
				" AND date = ?               " ;
		

		
		jdbcTemplate.update(sql,
				delete.getUserId(), // sqlのパラメータに対応する項目
				delete.getDate() // sqlのパラメータに対応する項目
				

		);
	}
	
}
