package com.service;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import com.pojo.Teleplay;

@Service
public interface TeleplayImageService {
	
	public String getImageTag (HttpServletRequest request, String image);
	public String getImageTagAndLink (HttpServletRequest request, String image);
	public String getImageTagAndLink (HttpServletRequest request);
	
	public boolean teleplayImageUploadDo (HttpServletRequest request);
	public Teleplay   imageDeleteDo (HttpServletRequest request);
}
