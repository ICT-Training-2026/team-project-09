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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class RegistForm {

    @NotEmpty(message = "社員IDは必須です")
    private String userId;

    @NotNull(message = "日付は必須です")
    private LocalDate date;

    @NotNull(message = "勤怠区分は必須です")
//    private Integer workStatus;
    private BigDecimal workStatus;
    
    @NotNull(message = "出勤時間は必須です")
    private LocalTime clockInTime;

    @NotNull(message = "退勤時間は必須です")
    private LocalTime clockOutTime;
    
    @NotNull(message = "休憩時間は必須です")
    @Min(value = 0, message = "実労働時間は0以上でなければなりません")

//    private Integer breakTime;    
    private BigDecimal breakTime;  // BigDecimalに変更


//    private Timestamp clockIn;
//    private Timestamp clockOut;
    private LocalDateTime clockIn;
    private LocalDateTime clockOut;

    @Min(value = 0, message = "実労働時間は0以上でなければなりません")
//    private Integer actualWorkTime;

    private BigDecimal actualWorkTime;  // BigDecimalに変更

    @Min(value = 0, message = "累計労働時間は0以上でなければなりません")
//    private Integer cumOverTime;
    private BigDecimal cumOverTime;  // BigDecimalに変更
    


    @Size(max = 100, message = "備考は100文字以内で入力してください")
    private String note;

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
    	

    
    public void combineDateTime() {
        if (date != null && clockInTime != null && clockOutTime != null) {
            LocalDateTime clockInDateTime = LocalDateTime.of(date, clockInTime);
            LocalDateTime clockOutDateTime = LocalDateTime.of(date, clockOutTime);
            this.clockIn = clockInDateTime;
            this.clockOut = clockOutDateTime;
//            this.clockIn = Timestamp.valueOf(clockInDateTime);
//            this.clockOut = Timestamp.valueOf(clockOutDateTime);
    }
}
    
}
    
    


