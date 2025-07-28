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
		// 仮実装（コンソールに表示）
		System.out.println("ID:" + regist.getUserId());
		System.out.println("日付:" + regist.getDate());
		System.out.println("区分:" + regist.getWorkStatus());
		System.out.println("出勤時刻:" + regist.getClockIn());
		System.out.println("退勤時刻:" + regist.getClockOut());
		System.out.println("労働時間" + regist.getWorkTime());
		System.out.println("実労働時間" + regist.getActualWorkTime());
		System.out.println("休憩時間" + regist.getBreakTime());
		System.out.println("累積超過時間" + regist.getCumOverTime());
		System.out.println("備考:" + regist.getNote());
		
		
        // 日付の重複を確認
        if (isDateAlreadyRegistered(regist.getUserId(), regist.getDate())) {
            throw new IllegalArgumentException("日付が重複しています");
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
		
//		System.out.println("勤怠区分コード:" + regist.getWorkStatus());
		if (regist.getWorkStatus().compareTo(BigDecimal.valueOf(4)) == 0) {
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
	
	
	
    // 日付の重複を確認するメソッド
    public boolean isDateAlreadyRegistered(String userId, Date date) {
        String sql = "SELECT COUNT(*) FROM attend_info WHERE user_code = ? AND date = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{userId, date}, Integer.class);
        return count != null && count > 0;
    }
    

}

