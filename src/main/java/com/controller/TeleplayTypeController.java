package com.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pojo.TeleplayType;
import com.service.TeleplayTypeService;
import com.validator.TeleplayTypeValidator;

@Controller
public class TeleplayTypeController {
	
	@Autowired
	private TeleplayTypeService  teleplayTypeService;		
	
	
	@RequestMapping("/teleplayTypeList")
	public String teleplayTypeList (HttpServletRequest request) {
				
		teleplayTypeService.queryAll(request);
		
		return "type/teleplayTypeList.jsp";
	}
	
	
	@RequestMapping("/teleplayTypeShow")
	public String teleplayTypeShow (HttpServletRequest request) {
				
		teleplayTypeService.queryByTeleplayTypeId(request);
		
		TeleplayType type = (TeleplayType) request.getAttribute("type");
		
		if (type != null) {
			String note = type.getNote(); 
			note = note.replace("\n", "<br>");				//换行符替换
			type.setNote(note);
		
			request.setAttribute("type", type);				//****传递对象
		}
		
		return "type/teleplayTypeShow.jsp";
	}
	
	
	@RequestMapping("/teleplayTypeAdd")
	public String teleplayTypeAdd (HttpServletRequest request) {
		
		return "type/teleplayTypeAdd.jsp";
	}
	
	
	@InitBinder											//将首先执行的动作
	public void initBinder (DataBinder binder) {
		
		binder.setValidator(new TeleplayTypeValidator());		//进行数据校验
	}
	
	
	@RequestMapping(value="/teleplayTypeAddDo", produces={"text/html;charset=UTF-8"})		//避免返回的中文内容为乱码
	@ResponseBody																	//返回数据给Ajax，而不是转发到网址
	public String teleplayTypeAddDo (@Validated TeleplayType type, Errors errors, HttpServletRequest request) {
		
		String msg = "";
		
		if (errors.hasFieldErrors()) {								//如果校验时生成了错误信息
			List<ObjectError> errorList = errors.getAllErrors();
			
			for (ObjectError error : errorList) {
				msg += error.getDefaultMessage();					//该字段的错误信息
			}
			
			return msg;
		}
		
		String teleplayTypeId = teleplayTypeService.addTeleplayType(type, request);
		
		if (teleplayTypeId.equals("0")) {									//新添失败
			msg = (String) request.getAttribute("msg");
			return msg;
		}
		
		return "@Redirect:teleplayTypeShow?teleplayTypeId=" + teleplayTypeId;				//新添成功
	}
	

	@RequestMapping("/teleplayTypeEdit")
	public String teleplayTypeEdit (HttpServletRequest request) {
				
		teleplayTypeService.queryByTeleplayTypeId(request);
		
		return "type/teleplayTypeEdit.jsp";
	}	
	
	
	@RequestMapping(value="/teleplayTypeEditDo", produces={"text/html;charset=UTF-8"})		//避免返回的中文内容为乱码
	@ResponseBody																	//返回数据给Ajax，而不是转发到网址
	public String teleplayTypeEditDo (@Validated TeleplayType type, Errors errors, HttpServletRequest request) {
		
		String msg = "";
		
		if (errors.hasFieldErrors()) {								//如果校验时生成了错误信息
			List<ObjectError> errorList = errors.getAllErrors();
			
			for (ObjectError error : errorList) {
				msg += error.getDefaultMessage();					//该字段的错误信息
			}
			
			return msg;
		}
		
		boolean result = teleplayTypeService.editTeleplayType(type, request);
		
		if (result == false) {										//更新失败
			msg = (String) request.getAttribute("msg");
			return msg;
		}
		
		return "@Redirect:teleplayTypeShow?teleplayTypeId=" + type.getTeleplayTypeId();		//更新成功
	}
	
	
	@RequestMapping("/teleplayTypeDeleteDo")
	public String teleplayTypeDeleteDo (HttpServletRequest request) {
				
		teleplayTypeService.deleteByTeleplayTypeId(request);
		
		return "redirect:teleplayTypeList";									//重定向到类型列表页
	}
}
