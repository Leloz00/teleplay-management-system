package com.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pojo.Teleplay;
import com.service.TeleplayImageService;

@Controller
public class TeleplayImageController {
	
	@Autowired
	private TeleplayImageService imageService;			//自动注入

	
	@RequestMapping("/teleplayImageUpload")
	public String teleplayImageUpload (HttpServletRequest request) {
		
		String msg = imageService.getImageTagAndLink(request);			//****生成美剧图片的标签、图片链接和管理链接。图片名称来自于地址栏中的参数值 
		request.setAttribute("msg", msg);
		
		return "teleplay/teleplayImageUpload.jsp";								//转发到美剧图片上传页
	}
	
	@RequestMapping("/teleplayImageUploadDo")
	public String teleplayImageUploadDo (HttpServletRequest request) {
				
		imageService.teleplayImageUploadDo(request);							//****上传图片，并更新数据表中的image字段，或在新添美剧页中更新session中的image的值 
		
		return "teleplay/teleplayImageUpload.jsp";								//转发到美剧图片上传页
	}
	
	@RequestMapping("/imageDeleteDo")
	public String imageDeleteDo (HttpServletRequest request) {
				
		Teleplay teleplay = imageService.imageDeleteDo(request);
		
		String teleplayId = teleplay.getTeleplayId();
		String image   = teleplay.getImage();	
		
		String url = "teleplayImageUpload?teleplayId=" + teleplayId + "&image=" + image;
		return "redirect:" + url;											//重定向到图片上传页
	}
}
