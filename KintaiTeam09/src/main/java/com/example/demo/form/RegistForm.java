//package com.example.demo.form;
//
//
//import java.sql.Timestamp;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//
////import java.sql.Date;
//
//import lombok.Data;
//
//@Data
//public class RegistForm {
//	private String userId;
////	private Date date;
//	private LocalDate date;
//	private Integer workStatus;
//
//	private LocalTime clockInTime;
//	private LocalTime clockOutTime;
//	private Timestamp clockIn;
//	private Timestamp clockOut;
//	
//	private Integer actualWorkTime;
//	private Integer breakTime;
//	private Integer cumOverTime;
//	private String note;
//
//	public void combineDateTime() {
//        if (date != null && clockInTime != null && clockOutTime != null) {
//            LocalDateTime clockInDateTime = LocalDateTime.of(date, clockInTime);
//            LocalDateTime clockOutDateTime = LocalDateTime.of(date, clockOutTime);
//            this.clockIn = Timestamp.valueOf(clockInDateTime);
//            this.clockOut = Timestamp.valueOf(clockOutDateTime);
//        }
//    }
//	
//}

package com.example.demo.form;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
//import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class RegistForm {

	// 社員ID（ログイン中の社員情報で固定）
    @NotEmpty(message = "社員IDは必須です")
    private String userId;

    // 日付（ユーザが入力）
    @NotNull(message = "日付は必須です")
    private Date date;

    // 勤怠区分（ユーザが入力）
    @NotNull(message = "勤怠区分は必須です")
    private BigDecimal workStatus;
    
    // 出勤時刻（ユーザが入力）
    @DateTimeFormat(pattern = "HH:mm")
    @NotNull(message = "出勤時間は必須です")
    private LocalTime clockInTime;

    // 退勤時刻（ユーザが入力）
    @DateTimeFormat(pattern = "HH:mm")
    @NotNull(message = "退勤時間は必須です")
    private LocalTime clockOutTime;
    
    // 休憩時刻（ユーザが入力）
    @NotNull(message = "休憩時間は必須です")
    @Min(value = 0, message = "実労働時間は0以上でなければなりません")  
    private BigDecimal breakTime;  // BigDecimalに変更


    //出勤時刻（combineDateTimeメソッドで"日付+時刻"形式に変換）
    private LocalDateTime clockIn;
    //退勤時刻（combineDateTimeメソッドで"日付+時刻"形式に変換）
    private LocalDateTime clockOut;

    // 労働時間（culcWorktimeメソッドで自動算出）
    private BigDecimal workTime;
    
    // 実労働時間（culcActualWorktimeメソッドで自動算出）
    @Min(value = 0, message = "実労働時間は0以上でなければなりません")
    private BigDecimal actualWorkTime;  // BigDecimalに変更

    // 定時間（7時間=420分で固定）
    private final BigDecimal regularTime = BigDecimal.valueOf(420);
    
    // 超過時間
    private BigDecimal overTime;
    
    // 累計超過時間
    @Min(value = 0, message = "累計労働時間は0以上でなければなりません")
    private BigDecimal cumOverTime;  // BigDecimalに変更
    
    // 備考（ユーザが入力）
    @Size(max = 100, message = "備考は100文字以内で入力してください")
    private String note;

    
    
    // 各種バリデーション
    @AssertTrue(message = "出勤時間は退勤時間より前である必要があります")
    public boolean isClockInBeforeClockOut() {
        if (clockInTime == null || clockOutTime == null) {
            return true;
        }
        return !clockInTime.isAfter(clockOutTime);
    }

    @AssertTrue(message = "出勤時間と退勤時間は8:00から22:45の間でなければなりません")
    public boolean isWithinWorkDayHours() {
        LocalTime startOfWorkDay = LocalTime.of(8, 0);
        LocalTime endOfWorkDay = LocalTime.of(22, 45);
        if (clockInTime == null || clockOutTime == null) {
            return true;
        }
        return !clockInTime.isBefore(startOfWorkDay) && !clockOutTime.isAfter(endOfWorkDay);
    }

    @AssertTrue(message = "コアタイムは10:45から15:00の間でなければなりません")
    public boolean isWithinCoreTime() {
        LocalTime coreTimeStart = LocalTime.of(10, 45);
        LocalTime coreTimeEnd = LocalTime.of(15, 0);
        if (clockInTime == null || clockOutTime == null) {
            return true;
        }
        return !clockInTime.isAfter(coreTimeStart) && !clockOutTime.isBefore(coreTimeEnd);
    }


    @AssertTrue(message = "実労働時間が4時間以上の場合は、1時間の休憩を取る必要があります")
    public boolean isBreakTimeValid() {
        if (clockInTime == null || clockOutTime == null) {
            return true;
        }
        BigDecimal workMinutes = BigDecimal.valueOf(java.time.Duration.between(clockInTime, clockOutTime).toMinutes());
        return workMinutes.compareTo(BigDecimal.valueOf(240)) < 0 || (breakTime != null && breakTime.compareTo(BigDecimal.valueOf(60)) >= 0);
    }

    @AssertTrue(message = "休憩時間が実労働時間を超えることはできません")
    public boolean isBreakTimeNotExceedingWorkTime() {
        if (clockInTime == null || clockOutTime == null || breakTime == null) {
            return true;
        }
        BigDecimal workMinutes = BigDecimal.valueOf(java.time.Duration.between(clockInTime, clockOutTime).toMinutes());
        return breakTime.compareTo(workMinutes) <= 0;
    }

    @AssertTrue(message = "休憩時間が1時間を超えています")
    public boolean isBreakTimeNotExceedingOneHour() {
        if (breakTime == null) {
            return true;
        }
        return breakTime.compareTo(BigDecimal.valueOf(60)) <= 0;
    }


    @AssertTrue(message = "実労働時間が4時間未満のため、数値を変更してください。")
    public boolean isBreakTimeAppropriateForShortWork() {
        if (clockInTime == null || clockOutTime == null || breakTime == null) {
            return true;
        }
        BigDecimal workMinutes = BigDecimal.valueOf(java.time.Duration.between(clockInTime, clockOutTime).toMinutes());
        return workMinutes.compareTo(BigDecimal.valueOf(240)) >= 0 || breakTime.compareTo(BigDecimal.valueOf(60)) < 0;
    }

    @AssertTrue(message = "実労働時間が8時間を超えています")
    public boolean isActualWorkTimeNotExceedingEightHours() {
        if (clockInTime == null || clockOutTime == null) {
            return true;
        }
        BigDecimal workMinutes = BigDecimal.valueOf(java.time.Duration.between(clockInTime, clockOutTime).toMinutes())
                .subtract(breakTime != null ? breakTime : BigDecimal.ZERO);
        return workMinutes.compareTo(BigDecimal.valueOf(480)) <= 0; // 8 hours * 60 minutes
    }
    
    
//    @AssertTrue(message = "日付が重複しています")
//    public boolean isDateNotDuplicated() {
//        if (userId == null || date == null) {
//            return true; // ユーザーIDまたは日付がnullの場合はバリデーションをスキップ
//        }
//        // データベースをチェックして日付の重複を確認
//        return !registRepository.isDateAlreadyRegistered(userId, date);
//    }   
    
    
    
    
    


        
        

        
        
        
//        // 勤怠区分に応じた処理
//        switch (workStatus) {
//            case 1: // 振出
//                // 振出の処理
//                break;
//            case 2: // 振休
//                // 振休の処理
//                break;
//            case 3: // 年休
//                // 年休の処理
//                break;
//            case 4: // 休日
//                // 休日の処理
//                break;
//            case 5: // 欠勤
//                // 欠勤の処理
//                break;
//            default:
//                // 通常勤務の処理
//                break;
//        }
    	

    
    // 各種メソッド
    
    // 日付と時刻を結合するメソッド
	public void combineDateTime() {
		if (date != null && clockInTime != null && clockOutTime != null) {
			LocalDate localDate = date.toLocalDate();
			LocalDateTime clockInDateTime = LocalDateTime.of(localDate, clockInTime);
			LocalDateTime clockOutDateTime = LocalDateTime.of(localDate, clockOutTime);
			this.clockIn = clockInDateTime;
			this.clockOut = clockOutDateTime;
			//            this.clockIn = Timestamp.valueOf(clockInDateTime);
			//            this.clockOut = Timestamp.valueOf(clockOutDateTime);
		}
	}
	
	// 労働時間を算出するメソッド
	public void culcWorkTime() {
		long minutesWorkTimeLong = Duration.between(this.clockIn, this.clockOut).toMinutes();
		BigDecimal minutesWorkTime = BigDecimal.valueOf(minutesWorkTimeLong);
		this.workTime = minutesWorkTime;
	}

	// 実労働時間を算出するメソッド
	public void culcActualWorkTime() {
		this.actualWorkTime = this.workTime.subtract(this.breakTime);
	}
	
	// 超過時間を算出するメソッド
	public void culcOverTime() {
		this.overTime = this.actualWorkTime.subtract(this.regularTime);
	}
	
}
    
    


