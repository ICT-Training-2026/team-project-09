package com.example.demo.validation;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.demo.form.RegistForm;

@Component
public class ValidatorImpl implements Validator {


	@Override
	public boolean supports(Class<?> clazz) {
		return RegistForm.class.isAssignableFrom(clazz);
	}

	@Override
    public void validate(Object target, Errors errors) {
		RegistForm registForm = (RegistForm) target;

		
            if (registForm.getWorkStatus().intValue() == 3 &&
            		registForm.getHurisyutsuCount() <= registForm.getHurikyuCount()) {
            	errors.rejectValue("workStatus", "workStatus.cannotGetFurikyu", 
            			"振出を取得していない場合、振休を申請することはできません");
            }
            
            if (registForm.getWorkStatus().intValue() == 4 &&
            		registForm.getAnnualLeaveDays().intValue() == 0) {
            	errors.rejectValue("workStatus", "workStatus.cannotGetNenkyu", 
            			"有給休暇日数が残っていない場合、年休を申請することはできません");
            }
            
            if (registForm.getDate() == null) {
            	errors.rejectValue("date", "date.dateNotNull","日付は必須です" );
            }
            
            if (registForm.getWorkStatus().intValue() == 1 || 
            		registForm.getWorkStatus().intValue() == 3 ||
            		registForm.getWorkStatus().intValue() == 4 ||
            		registForm.getWorkStatus().intValue() == 6) {
                LocalDate localDate = registForm.getDate().toLocalDate();
                DayOfWeek dayOfWeek = localDate.getDayOfWeek();
				boolean result = dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
				if (!result) {
					errors.rejectValue("date", "date.isNotAttendanceOrAbsenceOnWeekday", "出勤・欠勤・年休は平日のみ指定可能です");
				}
			}
		}
	}
