package com.example.demo.form;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
//import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Data;

@Data
public class RegistForm {
	
//    // バリデーションの順序を定義
////    @GroupSequence({Default.class, BasicChecks.class, AdvancedChecks.class})
//    public interface ValidationSequence {}
    
    @NotEmpty(message = "社員IDは必須です")
    private String userId;
    

    // 日付（ユーザが入力）
    @NotNull(message = "日付は必須です")
    private Date date;

    // 勤怠区分（ユーザが入力）
    @NotNull(message = "勤怠区分は必須です")
    private BigDecimal workStatus;
    
    private LocalTime clockInTime;
    private LocalTime clockOutTime;

    private BigDecimal breakTime;  // BigDecimalに変更

    private LocalDateTime clockIn;
    //退勤時刻（combineDateTimeメソッドで"日付+時刻"形式に変換）
    private LocalDateTime clockOut;
    
//    追加7/25
    private BigDecimal annualLeaveDays; // 年休日数
    

    private static final Logger logger = LoggerFactory.getLogger(RegistForm.class);
    private Integer hurisyutsuCount;
    private Integer hurikyuCount;
    	


    // 労働時間（culcWorktimeメソッドで自動算出）
    private BigDecimal workTime;
    
    // 実労働時間（culcActualWorktimeメソッドで自動算出）
    private BigDecimal actualWorkTime;  // BigDecimalに変更

    // 定時間（7時間=420分で固定）
    private final BigDecimal regularTime = BigDecimal.valueOf(420);
    
    // 超過時間
    private BigDecimal overTime;
    
    // 累計超過時間
    private BigDecimal cumOverTime;  // BigDecimalに変更
    
    // 備考（ユーザが入力）
    @Size(max = 100, message = "備考は100文字以内で入力してください")
    private String note;
    
    @AssertTrue(message = "出勤時間は必須です")
    public boolean isClockInTimeValid() {
        if ((workStatus.intValue() != 1 && workStatus.intValue() != 2 )) {
            return true; // バリデーションをスキップ
        }
        return clockInTime != null; // 出勤時間が設定されているかどうかをチェック
    }

    @AssertTrue(message = "退勤時間は必須です")
    public boolean isClockOutTimeValid() {
        if ((workStatus.intValue() != 1 && workStatus.intValue() != 2 )) {
            return true; // バリデーションをスキップ
        }
        return clockOutTime != null; // 出勤時間が設定されているかどうかをチェック
    }


    @AssertTrue(message = "休憩時間は必須です")
    public boolean isBreakTimeValid1() {
    	if ((workStatus.intValue() != 1 && workStatus.intValue() != 2 )) {
            return true; // バリデーションをスキップ
        }
        return breakTime != null; // 出勤時間が設定されているかどうかをチェック
    }

    

    
    
    // 各種バリデーション
    @AssertTrue(message = "出勤時間は退勤時間より前である必要があります")
    public boolean isClockInBeforeClockOut() {
        if (clockInTime == null || clockOutTime == null || (workStatus.intValue() != 1 && workStatus.intValue() != 2)) {
            return true;
            
            
        }
        return !clockInTime.isAfter(clockOutTime);
    }

    @AssertTrue(message = "出勤時間と退勤時間は8:00から22:45の間でなければなりません")
    public boolean isWithinWorkDayHours() {
        LocalTime startOfWorkDay = LocalTime.of(8, 0);
        LocalTime endOfWorkDay = LocalTime.of(22, 45);
        if (clockInTime == null || clockOutTime == null || (workStatus.intValue() != 1 && workStatus.intValue() != 2)) {
            return true;
        }
        return !clockInTime.isBefore(startOfWorkDay) && !clockOutTime.isAfter(endOfWorkDay);
    }

    @AssertTrue(message = "コアタイムは10:45から15:00の間でなければなりません")
    public boolean isWithinCoreTime() {
        LocalTime coreTimeStart = LocalTime.of(10, 45);
        LocalTime coreTimeEnd = LocalTime.of(15, 0);
        if (clockInTime == null || clockOutTime == null || (workStatus.intValue() != 1 && workStatus.intValue() != 2)) {
            return true;
        }
        return !clockInTime.isAfter(coreTimeStart) && !clockOutTime.isBefore(coreTimeEnd);
    }


    @AssertTrue(message = "実労働時間が4時間以上の場合は、1時間の休憩を取る必要があります")
    public boolean isBreakTimeValid() {
        if (clockInTime == null || clockOutTime == null || (workStatus.intValue() != 1 && workStatus.intValue() != 2)) {
            return true;
        }
        BigDecimal workMinutes = BigDecimal.valueOf(java.time.Duration.between(clockInTime, clockOutTime).toMinutes());
        return workMinutes.compareTo(BigDecimal.valueOf(240)) < 0 || (breakTime != null && breakTime.compareTo(BigDecimal.valueOf(60)) >= 0);
    }

    @AssertTrue(message = "休憩時間が実労働時間を超えることはできません")
    public boolean isBreakTimeNotExceedingWorkTime() {
        if (clockInTime == null || clockOutTime == null || breakTime == null ||  (workStatus.intValue() != 1 && workStatus.intValue() != 2)) {
            return true;
        }
        BigDecimal workMinutes = BigDecimal.valueOf(java.time.Duration.between(clockInTime, clockOutTime).toMinutes());
        return breakTime.compareTo(workMinutes) <= 0;
    }

    @AssertTrue(message = "休憩時間が1時間を超えています")
    public boolean isBreakTimeNotExceedingOneHour() {
        if (breakTime == null || (workStatus.intValue() != 1 && workStatus.intValue() != 2)) {
            return true;
        }
        return breakTime.compareTo(BigDecimal.valueOf(60)) <= 0;
    }


    @AssertTrue(message = "実労働時間が4時間未満のため、数値を変更してください。")
    public boolean isBreakTimeAppropriateForShortWork() {
        if (clockInTime == null || clockOutTime == null || breakTime == null || (workStatus.intValue() != 1 && workStatus.intValue() != 2)) {
            return true;
        }
        BigDecimal workMinutes = BigDecimal.valueOf(java.time.Duration.between(clockInTime, clockOutTime).toMinutes());
        return workMinutes.compareTo(BigDecimal.valueOf(240)) >= 0 || breakTime.compareTo(BigDecimal.valueOf(60)) < 0;
    }

    @AssertTrue(message = "実労働時間が8時間を超えています")
    public boolean isActualWorkTimeNotExceedingEightHours() {
        if (clockInTime == null || clockOutTime == null || (workStatus.intValue() != 1 && workStatus.intValue() != 2)) {
            return true;
        }
        BigDecimal workMinutes = BigDecimal.valueOf(java.time.Duration.between(clockInTime, clockOutTime).toMinutes())
                .subtract(breakTime != null ? breakTime : BigDecimal.ZERO);
        return workMinutes.compareTo(BigDecimal.valueOf(480)) <= 0; // 8 hours * 60 minutes
    }
    



//	private RegistRepository registRepositoryImpl;
//
//    public void RegistForm(RegistRepository registRepository) {
//        this.registRepositoryImpl = registRepository;
//    }
//    
//    // 振休を取得できるかどうかを判断するメソッド●
//    @AssertTrue(message = "振出を取得していない場合、振休を申請することはできません")
//    public boolean isHurikyuValid() {
//        int hurikaeCount = registRepositoryImpl.getHurisyutsuCount(userId);
//        int hurikyuCount = registRepositoryImpl.getHurikyuuCount(userId);
//        return hurikaeCount > hurikyuCount;
//    }
    
    @AssertTrue(message = "振出を取得していない場合、振休を申請することはできません")
    public boolean isHurikyuValid() {
        // 勤怠区分が振休 (workStatus == 3) の場合のみチェック
        if (workStatus == null || workStatus.intValue() != 3) {
            return true; // 振休でない場合はバリデーションをスキップ
        }
        // nullチェックを追加
        if (hurisyutsuCount == null || hurikyuCount == null) {
            logger.debug("振出カウント: {}", hurisyutsuCount);
            logger.debug("振休カウント: {}", hurikyuCount);
            System.out.println(hurisyutsuCount);
            System.out.println(hurikyuCount);
            return false; // データが設定されていない場合はエラー
        }
        logger.debug("振出カウント: {}", hurisyutsuCount);
        logger.debug("振休カウント: {}", hurikyuCount);

        return hurisyutsuCount > hurikyuCount;
    }
    
    
    
    
    @AssertTrue(message = "有給休暇日数が残っていない場合、年休を申請することはできません")
    public boolean isAnnualLeaveValid() {
        if (workStatus == null || workStatus.intValue() != 4) { // 年休の場合のみチェック
            return true;
        }
        if (this.annualLeaveDays == null) {
            return false; // バリデーション失敗
        }
        
        System.out.println(annualLeaveDays.compareTo(BigDecimal.ZERO));
        return annualLeaveDays.compareTo(BigDecimal.ZERO) == 1;
    }
    
    
    
    
    
    
    


    @AssertTrue(message = "勤務時間および休憩時間は設定しないでください")
    public boolean isNoWorkTimeRequired() {
        if (workStatus == null || (workStatus.intValue() != 3 && workStatus.intValue() != 4 && workStatus.intValue() != 5 && workStatus.intValue() != 6)) {
            return true;
        }
        return clockInTime == null && clockOutTime == null && breakTime == null ;
    } 
    

    
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
		if (this.workStatus.intValue() == 1 || this.workStatus.intValue() == 2) {
			long minutesWorkTimeLong = Duration.between(this.clockIn, this.clockOut).toMinutes();
			BigDecimal minutesWorkTime = BigDecimal.valueOf(minutesWorkTimeLong);
			this.workTime = minutesWorkTime;
		} else {
			this.workTime = BigDecimal.valueOf(0);
		}

	}

	// 実労働時間を算出するメソッド
	public void culcActualWorkTime() {
		if (this.workStatus.intValue() == 1 || this.workStatus.intValue() == 2) {
			this.actualWorkTime = this.workTime.subtract(this.breakTime);
		} else {
			this.actualWorkTime = BigDecimal.valueOf(0);
		}
	}

	// 超過時間を算出するメソッド
	public void culcOverTime() {
		if (this.workStatus.intValue() == 1 || this.workStatus.intValue() == 2) {
			this.overTime = this.actualWorkTime.subtract(this.regularTime);
		} else {
			this.overTime = BigDecimal.valueOf(0);
		}

	}
	
	
}