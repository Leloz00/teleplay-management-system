package com.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.pojo.Teleplay;

@Service
public interface TeleplayService {
	
	public boolean queryAll (HttpServletRequest request);
	public boolean queryByTeleplayId (HttpServletRequest request);
	public boolean existByTeleplayName (Teleplay teleplay, HttpServletRequest request);
	public boolean existByTeleplayNameExceptTeleplayId (Teleplay teleplay, HttpServletRequest request);
	
	public String  addTeleplay (Teleplay teleplay, HttpServletRequest request);
	public boolean editTeleplay (Teleplay teleplay, HttpServletRequest request);
	public boolean deleteByTeleplayId (HttpServletRequest request);
	public boolean deleteByTeleplayIdLot (String[] teleplayIdArray, HttpServletRequest request);
}
