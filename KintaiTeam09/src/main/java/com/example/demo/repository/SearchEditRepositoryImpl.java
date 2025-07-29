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
		
		// attend_infoテーブルから勤怠情報を取得
		String sql = " SELECT * FROM attend_info " +
					 " WHERE user_code=? AND date=? ";
		
		List<Map<String, Object>> list = 
				jdbcTemplate.queryForList(
				sql, searchEdit.getSearchUserId(), searchEdit.getSearchDate()
				);
		if (list.isEmpty()) {
			return null;
		}
		
		Map<String, Object> one = list.get(0);
		
		Regist regist = new Regist();
		regist.setUserId((String) one.get("user_code"));
		regist.setDate((Date) one.get("date"));
		regist.setWorkStatus((BigDecimal)one.get("work_status_code"));
//		regist.setClockInTime(((LocalDateTime) one.get("clock_in")).toLocalTime());
//		regist.setClockOutTime(((LocalDateTime) one.get("clock_out")).toLocalTime());
		regist.setActualWorkTime((BigDecimal)one.get("actual_worktime"));
		regist.setBreakTime((BigDecimal)one.get("breaktime"));
		regist.setCumOverTime((BigDecimal)one.get("cum_overtime"));
		regist.setNote((String)one.get("note"));
		
		// 出勤 or 振出の場合は出退勤時刻をセット
		BigDecimal workStatusCode = (BigDecimal)one.get("work_status_code");
		if (workStatusCode.intValue() == 1 || workStatusCode.intValue() == 2) {
			regist.setClockInTime(((LocalDateTime) one.get("clock_in")).toLocalTime());
			regist.setClockOutTime(((LocalDateTime) one.get("clock_out")).toLocalTime());
		}
		
		
		// attend_typeテーブルから勤怠区分名を取得
		String sqlStatusName = " SELECT work_status FROM attend_type " +
				               " WHERE work_status_code=?";
		List<Map<String, Object>> listStatusName = 
				jdbcTemplate.queryForList(
				sqlStatusName, (BigDecimal)one.get("work_status_code")
				);
		Map<String, Object> oneStatusName = listStatusName.get(0);
		regist.setWorkStatusName((String)oneStatusName.get("work_status"));
		
		return regist;
	}
	
	@Override
	public void update(Regist edit) { // データベースの更新SQL
		
		System.out.println("updateメソッド呼び出し完了(インフラ層)");
//		System.out.println(edit.getDate());
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		String formattedDate = sdf.format(edit.getDate());
		

		String sql =
				" UPDATE                     " + 
				"   attend_info              " + 
				" SET                        " + 
				"   work_status_code = ? ,   " + 
				"   clock_in = ? ,           " + 
				"   clock_out = ? ,          " + 
				"   worktime = ? ,           " +
				"   breaktime = ? ,          " +
				"   actual_worktime = ? ,    " +
				"   overtime = ? ,           " +
				"   cum_overtime = ? ,       " +
				"   note = ?                 " +
				" WHERE                      " + 
				"   user_code = ?            " +
				" AND date = ?               " ;
		

		
		jdbcTemplate.update(sql,
				edit.getWorkStatus(),
				edit.getClockIn(),
				edit.getClockOut(),
				edit.getWorkTime(),
				edit.getBreakTime(),
				edit.getActualWorkTime(),
				edit.getOverTime(),
				edit.getCumOverTime(),
				edit.getNote(),
				edit.getUserId(),
				edit.getDate()

		);
		
		//有給
//		System.out.println("勤怠区分コード:" + regist.getWorkStatus());
		if (edit.getWorkStatus().intValue() == 4) {
			String sqlUpdate = 
					" UPDATE employees " + 
					" SET num_paid_holiday = num_paid_holiday - 1 " + 
					" WHERE user_code = ? ";
			jdbcTemplate.update(sqlUpdate,edit.getUserId());
		}
		

		
	}
	
}
