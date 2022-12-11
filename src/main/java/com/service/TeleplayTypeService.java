package com.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.pojo.TeleplayType;

@Service
public interface TeleplayTypeService {
	
	public boolean queryAllForGOODS (HttpServletRequest request);			//用大写的GOODS，是为了在查找替换时不被更改	
	public boolean queryByTeleplayTypeId (HttpServletRequest request);
	
	//----------------------------------------------------------------
	
	public boolean queryAll (HttpServletRequest request);
	public boolean existByTeleplayTypeName (TeleplayType type, HttpServletRequest request);
	public boolean existByTeleplayTypeNameExceptTeleplayTypeId (TeleplayType type, HttpServletRequest request);
	
	public String  addTeleplayType (TeleplayType type, HttpServletRequest request);
	public boolean editTeleplayType (TeleplayType type, HttpServletRequest request);
	public boolean deleteByTeleplayTypeId (HttpServletRequest request);
	public boolean deleteByTeleplayTypeIdLot (String[] teleplayTypeIdArray, HttpServletRequest request);
}
