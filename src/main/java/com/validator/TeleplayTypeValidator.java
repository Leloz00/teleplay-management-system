package com.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.pojo.TeleplayType;

public class TeleplayTypeValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		
		return TeleplayType.class.equals(arg0);			//只针对TeleplayType类的对象进行数据校验
	}
	
	@Override
	public void validate(Object object, Errors errors) {
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "teleplayTypeName", 	null, "类型名称不能为空<br>");
		
		TeleplayType type = (TeleplayType) object;
		
		String teleplayTypeId 		= type.getTeleplayTypeId();					//新添记录时，ID为null，所以这里不能有trim()方法
		String teleplayTypeName 	= type.getTeleplayTypeName().trim();
		String note			= type.getNote().trim();
		
		type.setTeleplayTypeName(teleplayTypeName);
		type.setNote(note);
		
		if (teleplayTypeId != null) {									//编辑类型时才有参数teleplayTypeId
			teleplayTypeId = teleplayTypeId.trim();
			type.setTeleplayTypeId(teleplayTypeId);
			
			try { 									 
				Integer.parseInt(teleplayTypeId);					
			} catch (Exception e) {
				errors.rejectValue("teleplayTypeId", null, "参数teleplayTypeId错误<br>");
			}
		}
		
		if (teleplayTypeName.length() > 45) {
			errors.rejectValue("teleplayTypeName", null, "类型名称不能多于45个字符<br>");
		}
		
		if (note.length() > 2000) {
			errors.rejectValue("note", null, "备注不能多于20000个字符<br>");
		}
	}
}