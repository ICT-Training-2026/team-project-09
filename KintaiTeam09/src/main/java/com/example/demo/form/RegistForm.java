package com.example.demo.form;

import java.math.BigDecimal;
import java.sql.Date;
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
	
	
    // バリデーションメソッド（出勤・振出）
    @NotEmpty(message = "社員IDは必須です")
    private String userId;
    

    @NotNull(message = "日付は必須です")
    private Date date;

    @NotNull(message = "勤怠区分は必須です")
    private BigDecimal workStatus;
    
    private LocalTime clockInTime;

    private LocalTime clockOutTime;

    private BigDecimal breakTime;  // BigDecimalに変更

    private LocalDateTime clockIn;
    private LocalDateTime clockOut;
    
//    追加7/25
    private Integer annualLeaveDays; // 年休日数
    private boolean hasTakenFurikae; // 振出を取得したかどうか


    private BigDecimal actualWorkTime;  // BigDecimalに変更
    private BigDecimal cumOverTime;  // BigDecimalに変更
    
    
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
    

    

    // バリデーションメソッド（振休・休日・年休・欠勤）
    @AssertTrue(message = "振出を取得していない場合、振休を申請することはできません")
    public boolean isFurikaeValid() {
        if (workStatus == null || workStatus.intValue() != 3) { // 振休の場合のみチェック
            return true;
        }
        return hasTakenFurikae;
    }

    @AssertTrue(message = "有給休暇日数が残っていない場合、年休を申請することはできません")
    public boolean isAnnualLeaveValid() {
        if (workStatus == null || workStatus.intValue() != 4) { // 年休の場合のみチェック
            return true;
        }
        return annualLeaveDays > 0;
    }

    @AssertTrue(message = "勤務時間および休憩時間は設定しないでください")
    public boolean isNoWorkTimeRequired() {
        if (workStatus == null || (workStatus.intValue() != 3 && workStatus.intValue() != 4 && workStatus.intValue() != 5 && workStatus.intValue() != 6)) {
            return true;
        }
        return clockInTime == null && clockOutTime == null && breakTime == null ;
    } 
    

    
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
    
}
    
    


