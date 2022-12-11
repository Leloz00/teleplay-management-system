package com.validator;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.pojo.Teleplay;

public class TeleplayValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		
		return Teleplay.class.equals(arg0);			//只针对Teleplay类的对象进行数据校验
	}
	
	@Override
	public void validate(Object object, Errors errors) {
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "episode", 	null, "集数不能为空<br>");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "teleplayName", 	null, "名称不能为空<br>");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "teleplayTypeId", 	null, "请选择美剧类型<br>");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "loc", 		null, "美剧地区不能为空<br>");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "year", 		null, "美剧年份不能为空<br>");
		
		Teleplay teleplay = (Teleplay) object;
		
		String teleplayId 		= teleplay.getTeleplayId();				//新添记录时，ID为null，所以这里不能有trim()方法
		String episode		= teleplay.getEpisode().trim();
		String teleplayName 	= teleplay.getTeleplayName().trim();
		String teleplayTypeId 		= teleplay.getTeleplayTypeId().trim();
		String loc 		= teleplay.getLoc().trim();
		String year 		= teleplay.getYear().trim();
		String plot		= teleplay.getPlot().trim();
		
		teleplay.setEpisode(episode);
		teleplay.setTeleplayName(teleplayName);
		teleplay.setTeleplayTypeId(teleplayTypeId);
		teleplay.setLoc(loc);
		teleplay.setYear(year);
		teleplay.setPlot(plot);
		
		if (teleplayId != null) {									//编辑美剧时才有参数teleplayId
			teleplayId = teleplayId.trim();
			teleplay.setTeleplayId(teleplayId);
			
			try { 									 
				Integer.parseInt(teleplayId);					
			} catch (Exception e) {
				errors.rejectValue("teleplayId", null, "参数teleplayId错误<br>");
			}
		}
		
		if (teleplayTypeId.length() > 0) {
			try { 									 
				Integer.parseInt(teleplayTypeId);					
			} catch (Exception e) {
				errors.rejectValue("teleplayTypeId", null, "请选择美剧类型<br>");
			}
		}
		if (loc.length() > 45) {
			errors.rejectValue("loc", null, "地区不能多于45个字符<br>");
		}
		
		if (year.length() > 0) {
			try { 									 
				Integer.parseInt(year);					
			} catch (Exception e) {
				errors.rejectValue("year", null, "美剧年份输入有误<br>");
			}
		}
		
//		if (timeSale.length() > 0) {		
//			try {
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				sdf.parse(timeSale);
//			} catch (Exception e) {
//				errors.rejectValue("timeSale", null, "请输入正确的起售时间<br>");
//			}
//		}
			
		if (episode.length() > 45) {
			errors.rejectValue("episode", null, "集数不能多于45个字符<br>");
		}
		
		if (teleplayName.length() > 45) {
			errors.rejectValue("teleplayName", null, "名称不能多于45个字符<br>");
		}
		
		if (plot.length() > 10000) {
			errors.rejectValue("plot", null, "剧情简介不能多于10000个字符<br>");
		}
	}
}