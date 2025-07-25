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
//        return jdbcTemplate.query(
//        	    sql,
//        	    new Object[]{export.getUserId(), export.getMonth()},
//        	    new RowMapper<Regist>() {
//        	        public Regist mapRow(ResultSet rs, int rowNum) throws SQLException {
//        	            Regist regist = new Regist();
//        	            regist.setUserId(rs.getString("user_id"));
//        	            regist.setDate(rs.getDate("date").toLocalDate());
//        	            regist.setClockInTime(rs.getTime("clock_in_time").toLocalTime());
//        	            regist.setClockOutTime(rs.getTime("clock_out_time").toLocalTime());
//        	            regist.setActualWorkTime(rs.getInt("actual_work_time"));
//        	            regist.setBreakTime(rs.getInt("break_time"));
//        	            regist.setCumOverTime(rs.getInt("cum_over_time"));
//        	            return regist;
//        	        }
//        	    }
//        	);
//        System.out.println("ユーザ名:" + export.getUserId());
//        System.out.println("年月:" + export.getMonth());
        
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,export.getUserId(), export.getMonth() );
        
//        System.out.println(list);
        return list;
    }
}
