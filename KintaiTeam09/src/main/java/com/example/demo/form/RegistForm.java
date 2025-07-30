package com.example.demo.form;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
//import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
    private BigDecimal workStatusTemp;
    
    // 編集前の勤怠区分コードを格納
    
    private LocalTime clockInTime;
    private LocalTime clockOutTime;

    private BigDecimal breakTime;

    //出退勤時刻（combineDateTimeメソッドで"日付+時刻"形式に変換）
    private LocalDateTime clockIn;
    private LocalDateTime clockOut;

//    追加7/25
    private BigDecimal annualLeaveDays; // 年休日数
    

//    private static final Logger logger = LoggerFactory.getLogger(RegistForm.class);
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
    // 編集前の超過時間を格納
    
    // 勤怠登録した月（フォームで選択した日付）の累計超過時間
    private BigDecimal cumOverTime;  // BigDecimalに変更
    // 今月（入力している日）の累計超過時間
    private BigDecimal cumOverTimeThisMonth;
    
    
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
    

    @AssertTrue(message = "勤務時間および休憩時間は設定しないでください")
    public boolean isNoWorkTimeRequired() {
        if (workStatus == null || (workStatus.intValue() != 3 && workStatus.intValue() != 4 && workStatus.intValue() != 5 && workStatus.intValue() != 6)) {
            return true;
        }
        return clockInTime == null && clockOutTime == null && breakTime == null ;
    } 
    
    
    //未来の日付指定、平日・土休日の指定
    @AssertTrue(message = "未来の日付は指定できません（出勤・振出の場合）")
    public boolean isDateNotInFutureForAttendance() {
        if (date == null) {
            return true; // 日付がnullの場合は他のバリデーションで処理されるため、ここではtrue
        }
        // workStatusが「振休」「年休」「休日」「欠勤」の場合はスキップ
        if (workStatus != null && (workStatus.intValue() == 3 || workStatus.intValue() == 4 || workStatus.intValue() == 5 || workStatus.intValue() == 6)) {
            return true;
        }

        // 現在の日付と比較
        LocalDate today = LocalDate.now();
        return date.toLocalDate().isBefore(today) || date.toLocalDate().isEqual(today);
    }
    

    @AssertTrue(message = "出勤・欠勤・年休は平日のみ指定可能です")
    public boolean isAttendanceOrAbsenceOnWeekday() {
        if (date == null || workStatus == null) {
            return true; // 日付または勤怠区分がnullの場合はスキップ
        }

        // 勤怠区分が「出勤 (1)」または「欠勤 (6)」の場合
        if (workStatus.intValue() == 1 || workStatus.intValue() == 4 || workStatus.intValue() == 6) {
            LocalDate localDate = date.toLocalDate();
            DayOfWeek dayOfWeek = localDate.getDayOfWeek();
            // 土日ではないことを確認
            return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
            // TODO: 祝日判定が必要な場合はここにロジックを追加
            // return !isHoliday(localDate); // 例: isHoliday() メソッドを別途実装
        }
        return true; // その他の勤怠区分は常にtrue
    }

    @AssertTrue(message = "休日は土日祝のみ指定可能です")
    public boolean isHolidayOnWeekendOrPublicHoliday() {
        if (date == null || workStatus == null) {
            return true; // 日付または勤怠区分がnullの場合はスキップ
        }

        // 勤怠区分が「休日 (5)」の場合
        if (workStatus.intValue() == 5) {
            LocalDate localDate = date.toLocalDate();
            DayOfWeek dayOfWeek = localDate.getDayOfWeek();
            // 土日であるかを確認
            boolean isWeekend = dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
            // TODO: 祝日判定が必要な場合はここにロジックを追加
            // boolean isPublicHoliday = isHoliday(localDate); // 例: isHoliday() メソッドを別途実装

            // return isWeekend || isPublicHoliday;
            return isWeekend; // 現時点では土日のみ
        }
        return true; // その他の勤怠区分は常にtrue
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
			BigDecimal overTimeDecimal = this.actualWorkTime.subtract(this.regularTime);
			if (overTimeDecimal.intValue() >= 0 ) {
				this.overTime = overTimeDecimal;
			} else {
				this.overTime = BigDecimal.valueOf(0);
			}
		} else {
			this.overTime = BigDecimal.valueOf(0);
		}
		
	}
	
}