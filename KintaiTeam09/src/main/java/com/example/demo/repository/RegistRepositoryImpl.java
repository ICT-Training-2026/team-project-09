package com.example.demo.repository;


import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Regist;

import lombok.RequiredArgsConstructor;

//RegistRepositoryインタフェースを実装
@Repository
@RequiredArgsConstructor
public class RegistRepositoryImpl implements RegistRepository {

	// JdbcTemplateオブジェクトを利用
	private final JdbcTemplate jdbcTemplate;

    // 勤怠情報をデータベースに登録するメソッド
	@Override
	public void add(Regist regist) {		
		
        // 日付の重複を確認
        if (isDateAlreadyRegistered(regist.getUserId(), regist.getDate())) {
            throw new IllegalArgumentException("日付が重複しています");
        }
        // 年休の場合は実労働時間を7時間(420分)にセット
        if (regist.getWorkStatus().intValue()==4) {
        	regist.setActualWorkTime(BigDecimal.valueOf(420));
        }

		
		// 本実装（データベース登録処理）
		String sql = " INSERT INTO attend_info " +
				" (user_code, date, work_status_code, clock_in, clock_out," +

				"worktime, breaktime, actual_worktime, overtime, cum_overtime, note) " +
				" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

		jdbcTemplate.update(sql, regist.getUserId(),
				regist.getDate(),
				regist.getWorkStatus(),
				regist.getClockIn(),
				regist.getClockOut(),
				regist.getWorkTime(),
				regist.getBreakTime(),
				regist.getActualWorkTime(),
				regist.getOverTime(),
				regist.getOverTime().add(regist.getCumOverTime()),
				regist.getNote());
		
		//有給
//		System.out.println("勤怠区分コード:" + regist.getWorkStatus());
		if (regist.getWorkStatus().intValue() == 4) {
			String sqlUpdate = 
					" UPDATE employees " + 
					" SET num_paid_holiday = num_paid_holiday - 1 " + 
					" WHERE user_code = ? ";
			jdbcTemplate.update(sqlUpdate,regist.getUserId());
		}

	}
	

	// 累積超過時間を取得するメソッド
	@Override
	public BigDecimal loadCumOverTime(String userId, int month) {
		String sql = "SELECT cum_overtime " +
				" FROM attend_info" +
				" WHERE user_code = ? " +
				" AND MONTH(date) = ? " +
				"ORDER BY date DESC LIMIT 1";
		
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, userId, month);
		if (list.isEmpty()) {
			return BigDecimal.valueOf(0);
		} else {
			Map<String, Object> one = list.get(0);
			BigDecimal overTime = (BigDecimal) one.get("cum_overtime");
			return overTime;
		}

	}
	
	// 残り有給休暇日数を取得するメソッド
	public BigDecimal loadNumPaidHoliday(String userId) {
		
		String sql = "SELECT num_paid_holiday " +
				" FROM employees" +
				" WHERE user_code = ? ";
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, userId);
		Map<String, Object> one = list.get(0);
		BigDecimal numPaidHoliday = (BigDecimal) one.get("num_paid_holiday");
		
		return numPaidHoliday;
	}
		


	// 振出の回数を取得するメソッド
	public int loadNumHurisyutsu(String userId) {
	    String sql = "SELECT COUNT(*) AS count FROM attend_info WHERE user_code = ? AND work_status_code = 2";
	    Integer count = jdbcTemplate.queryForObject(sql, new Object[]{userId}, Integer.class);
	    return count != null ? count : 0; // nullの場合は0を返す
	}

	// 振休の回数を取得するメソッド
	public int loadNumHurikyu(String userId) {
	    String sql = "SELECT COUNT(*) AS count FROM attend_info WHERE user_code = ? AND work_status_code = 3";
	    Integer count = jdbcTemplate.queryForObject(sql, new Object[]{userId}, Integer.class);
	    return count != null ? count : 0; // nullの場合は0を返す
	}


    // 日付の重複を確認するメソッド
    public boolean isDateAlreadyRegistered(String userId, Date date) {
        String sql = "SELECT COUNT(*) FROM attend_info WHERE user_code = ? AND date = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{userId, date}, Integer.class);
        return count != null && count > 0;
    }
    

}

