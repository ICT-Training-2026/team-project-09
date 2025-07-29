package com.example.demo.validation;

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
            	System.out.println(registForm.getHurisyutsuCount());
            	System.out.println(registForm.getHurikyuCount());
            	errors.rejectValue("workStatus", "workStatus.cannotGetFurikyu", 
            			"振出を取得していない場合、振休を申請することはできません");
            }
    }
}
