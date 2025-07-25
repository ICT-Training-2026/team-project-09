package com.example.demo.repository;

import java.sql.Date;

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
				regist.getCumOverTime(),
				regist.getNote());

	}
	
	
	
    // 日付の重複を確認するメソッド
    public boolean isDateAlreadyRegistered(String userId, Date date) {
        String sql = "SELECT COUNT(*) FROM attend_info WHERE user_code = ? AND date = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{userId, date}, Integer.class);
        return count != null && count > 0;
    }
    
    
	
}

