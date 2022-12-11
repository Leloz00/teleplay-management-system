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

import com.pojo.Teleplay;
import com.service.TeleplayImageService;
import com.service.TeleplayService;
import com.service.TeleplayTypeService;
import com.validator.TeleplayValidator;

@Controller
public class TeleplayController {
	
	@Autowired
	private TeleplayService teleplayService;			//自动注入
	
	@Autowired
	private TeleplayTypeService  teleplayTypeService;	
	
	@Autowired
	TeleplayImageService imageService;	
	
	
	@RequestMapping("/teleplayList")				//美剧列表页
	public String teleplayList (HttpServletRequest request) {
				
		teleplayService.queryAll(request);
		teleplayTypeService.queryAllForGOODS(request);						//****获取类型列表，用大写的GOODS，是为了在查找替换时不被更改
		
		return "teleplay/teleplayList.jsp";
	}
	
	
	@RequestMapping("/teleplayListLayout")			//美剧平铺页
	public String teleplayListLayout (HttpServletRequest request) {
				
		teleplayService.queryAll(request);
		teleplayTypeService.queryAllForGOODS(request);						//****获取类型列表，用大写的GOODS，是为了在查找替换时不被更改
		
		return "teleplay/teleplayListLayout.jsp";
	}
	
	
	@RequestMapping("/teleplayShow")
	public String teleplayShow (HttpServletRequest request) {
				
		teleplayService.queryByTeleplayId(request);						//****根据ID获取记录

		//-------------------------- 生成美剧图片的标签和链接
		Teleplay teleplay = (Teleplay) request.getAttribute("teleplay");		//获取在Service中已经生成的goods		
		
		if (teleplay == null) {
			return "teleplay/teleplayShow.jsp";
		}
		
		String image = teleplay.getImage();
		image = imageService.getImageTag(request, image);		//****生成美剧图片标签和图片链接 
		
		teleplay.setImage(image);
		request.setAttribute("teleplay", teleplay);						//将image的新值更新到goods
		
		return "teleplay/teleplayShow.jsp";
	}
	
	@RequestMapping("/teleplayVideo")
	public String teleplayVideo (HttpServletRequest request) {
				
		teleplayService.queryByTeleplayId(request);						//****根据ID获取记录

		//-------------------------- 生成美剧图片的标签和链接
		Teleplay teleplay = (Teleplay) request.getAttribute("teleplay");		//获取在Service中已经生成的goods		
		
		if (teleplay == null) {
			return "teleplay/teleplayVideo.jsp";
		}
		
//		System.out.println(teleplay.getVideo());
		
		String image = teleplay.getImage();
		image = imageService.getImageTag(request, image);		//****生成美剧图片标签和图片链接 
		
		teleplay.setImage(image);
		request.setAttribute("teleplay", teleplay);						//将image的新值更新到goods
		
		return "teleplay/teleplayVideo.jsp";
	}
	
	
	@RequestMapping("/teleplayShowImageRenew")
	public String teleplayShowImageRenew (HttpServletRequest request) {
				
		teleplayService.queryByTeleplayId(request);
	
		return "teleplay/teleplayShowImageRenew.jsp";
	}
	
	
	@RequestMapping("/teleplayAdd")
	public String teleplayAdd (HttpServletRequest request) {

		teleplayTypeService.queryAllForGOODS(request);						//****获取类型列表

		String goodsUUID = java.util.UUID.randomUUID().toString();	//生成通用唯一标识，作为临时的ID，用于保存上传的图片
		Teleplay teleplay = new Teleplay();
		teleplay.setTeleplayId(goodsUUID);
		teleplay.setImage("");
		request.setAttribute("teleplay", teleplay);						//用于给网页的iframe的src和隐藏域控件赋值
		
		return "teleplay/teleplayAdd.jsp";
	}
	
	
	@InitBinder														//将首先执行的动作
	public void initBinder (DataBinder binder) {
		//System.out.println(111);
		binder.setValidator(new TeleplayValidator());					//进行数据校验
	}
	
	
	@RequestMapping(value="/teleplayAddDo", produces={"text/html;charset=UTF-8"})		//避免返回的中文内容为乱码
	@ResponseBody																	//返回数据给Ajax，而不是转发到网址
	public String teleplayAddDo (@Validated Teleplay teleplay, Errors errors, HttpServletRequest request) {
		//System.out.println(222);
		String msg = "";
		
		if (errors.hasFieldErrors()) {								//如果校验时生成了错误信息
			List<ObjectError> errorList = errors.getAllErrors();
			
			for (ObjectError error : errorList) {
				msg += error.getDefaultMessage();					//将错误消息置于msg
			}
			
			return msg;
		}
		
		String teleplayId = teleplayService.addTeleplay(teleplay, request);
		
		if (teleplayId.equals("0")) {									//新添失败
			msg = (String) request.getAttribute("msg");
			return msg;												//输出失败消息
		}
		
		return "@Redirect:teleplayShow?teleplayId=" + teleplayId;			//新添成功，将由JS实现网页跳转
	}
	

	@RequestMapping("/teleplayEdit")
	public String teleplayEdit(HttpServletRequest request) {
				
		teleplayService.queryByTeleplayId(request);						//****获取该记录
		teleplayTypeService.queryAllForGOODS(request);						//****获取类型列表
		
		return "teleplay/teleplayEdit.jsp";
	}	
	
	
	@RequestMapping(value="/teleplayEditDo", produces={"text/html;charset=UTF-8"})		//避免返回的中文内容为乱码
	@ResponseBody																	//返回数据给Ajax，而不是转发到网址
	public String teleplayEditDo (@Validated Teleplay teleplay, Errors errors, HttpServletRequest request) {
		
		String msg = "";
		
		if (errors.hasFieldErrors()) {								//如果校验时生成了错误信息
			List<ObjectError> errorList = errors.getAllErrors();
			
			for (ObjectError error : errorList) {
				msg += error.getDefaultMessage();
			}
			
			return msg;
		}
		
		boolean result = teleplayService.editTeleplay(teleplay, request);	//****更新记录
		
		if (result == false) {										//更新失败
			msg = (String) request.getAttribute("msg");
			return msg;
		}
		
		return "@Redirect:teleplayShow?teleplayId=" + teleplay.getTeleplayId();	//更新成功
	}
	
	
	@RequestMapping("/goodsDeleteDo")
	public String goodsDeleteDo (HttpServletRequest request) {
				
		teleplayService.deleteByTeleplayId(request);
		
		return "redirect:teleplayList";								//重定向到美剧列表页
	}
}
