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

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class RegistForm {

    @NotEmpty(message = "社員IDは必須です")
    private String userId;

    @NotNull(message = "日付は必須です")
    private LocalDate date;

//    @NotNull(message = "勤怠区分は必須です")
    private Integer workStatus;

//    @NotNull(message = "出勤時間は必須です")
    private LocalTime clockInTime;

//    @NotNull(message = "退勤時間は必須です")
    private LocalTime clockOutTime;

    private Timestamp clockIn;
    private Timestamp clockOut;

//    @Min(value = 0, message = "実労働時間は0以上でなければなりません")
    private Integer actualWorkTime;

//    @Min(value = 0, message = "休憩時間は0以上でなければなりません")
    private Integer breakTime;

//    @Min(value = 0, message = "累計労働時間は0以上でなければなりません")
    private Integer cumOverTime;

//    @Size(max = 100, message = "備考は文字以内で入力してください")
    private String note;

    public void combineDateTime() {
        if (date != null && clockInTime != null && clockOutTime != null) {
            LocalDateTime clockInDateTime = LocalDateTime.of(date, clockInTime);
            LocalDateTime clockOutDateTime = LocalDateTime.of(date, clockOutTime);
            this.clockIn = Timestamp.valueOf(clockInDateTime);
            this.clockOut = Timestamp.valueOf(clockOutDateTime);

//            // 出勤時間が退勤時間より遅くならないバリデーション
//            if (clockInDateTime.isAfter(clockOutDateTime)) {
//                throw new IllegalArgumentException("出勤時間は退勤時間より前です");
////                System.out.println("出勤時間は退勤時間より前です");
//            }
//
//            // 所定時間のバリデーション
//            LocalTime startOfWorkDay = LocalTime.of(8, 0);
//            LocalTime endOfWorkDay = LocalTime.of(22, 45);
//            if (clockInTime.isBefore(startOfWorkDay) || clockOutTime.isAfter(endOfWorkDay)) {
//                throw new IllegalArgumentException("出勤時間と退勤時間は8:00から22:45の間でなければなりません");
//            }
//
//            // 実労働時間が4時間以上の場合は、1時間の休憩を取る
//            int workMinutes = (int) java.time.Duration.between(clockInTime, clockOutTime).toMinutes();
//            if (workMinutes >= 240 && (breakTime == null || breakTime < 60)) {
//                throw new IllegalArgumentException("実労働時間が4時間以上の場合は、1時間の休憩を取る必要があります");
//            }
        }
    }
}
