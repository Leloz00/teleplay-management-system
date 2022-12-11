package com.service;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.TeleplayDao;
import com.dao.TeleplayTypeDao;
import com.pojo.Teleplay;
import com.pojo.TeleplayType;

@Service
@Transactional							//开启数据库事务，如果进行数据的增、改、删操作时发生非检查异常，数据将回滚
public class TeleplayTypeServiceImpl implements TeleplayTypeService {

	@Autowired
	private TeleplayDao goodsDao;			//自动注入
	
	@Autowired
	private TeleplayTypeDao typeDao;
	
	
	@Override	
	public boolean queryAllForGOODS (HttpServletRequest request) {		//----给TeleplayTypeAdd.java和TeleplayTypeEdit.java用。用大写的GOODS，是为了在查找替换时不被更改
		
		List<TeleplayType> teleplayTypeList = new ArrayList<TeleplayType>(); 					//记录列表
		String msg = "";
		
		try {			
			teleplayTypeList = typeDao.queryAllForGOODS();						//****获取所有记录
			
			if (teleplayTypeList == null || teleplayTypeList.size() == 0) {
				msg += "查无类型记录。";
				return false;
			}

			return true;
		
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg = request.getAttribute("msg").toString() + msg;
			}
			request.setAttribute("msg", msg);
			
			request.setAttribute("teleplayTypeList", teleplayTypeList);					//****传递记录列表
		}
		
		return false;
	}	
	
	
	@Override	
	public boolean queryByTeleplayTypeId (HttpServletRequest request) {
		
		TeleplayType type = null;
		String msg = "";
		
		try {
			String teleplayTypeId = request.getParameter("teleplayTypeId"); 		//获取地址栏参数id的值 
			
			try { 									 
				Integer.parseInt(teleplayTypeId);							//尝试转换为整数，即判断其是否为整数
			} catch (Exception e) {
				msg = "参数teleplayTypeId错误！";
				return false;
			}	
			
			type = typeDao.queryByTeleplayTypeId(teleplayTypeId);					//****根据ID获取记录
			
			if (type == null) {
				msg = "（查无此类型记录）";
				return false;
			}
			
			return true;
		
		} catch (Exception e) {
			msg = "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg = request.getAttribute("msg").toString() + msg;
			}
			request.setAttribute("msg", msg);
			
			if (request.getSession().getAttribute("msg") != null) {				//如果session中有消息。新添或修改记录后保存的消息
				msg = request.getSession().getAttribute("msg").toString() + msg;//读取session中的消息
				request.getSession().removeAttribute("msg");					//从session中移除此键值
				request.setAttribute("msg", msg);								//将消息赋值给request
			}
			
			request.setAttribute("type", type);									//****传递对象
		}
		
		return false;
	}	
	
	//---------------------------------------------------------------------
	
	@Override
	public boolean queryAll (HttpServletRequest request) {

		List<TeleplayType> teleplayTypeList = new ArrayList<TeleplayType>(); 		//记录列表
		String search = ""; 								//搜索的内容
		int countShowed = 0;	 							//（要略过的）之前的记录数		
		int pageShow = 1; 									//当前页码					
		String page = ""; 									//页码链接组	
		String msg = "";
		
		try {
			if (request.getParameter("buttonDelete") != null) { 				//如果单击了删除按钮
				
				String[] teleplayTypeIdArray = request.getParameterValues("teleplayTypeId"); 	//获取ID数组		
				
				if (teleplayTypeIdArray != null && teleplayTypeIdArray.length > 0) {
					deleteByTeleplayTypeIdLot(teleplayTypeIdArray, request);					//****删除所选。在本类中定义的方法
				}
			}	

			String buttonSearch	= request.getParameter("buttonSearch");	//数据查询按钮		
			String buttonPage  	= request.getParameter("buttonPage"); 	//页码提交按钮
			String pageInput  	= "1";									//输入的页码
			
			if (buttonSearch != null) { 								//如果按下了数据查询按钮
				search = request.getParameter("search").trim(); 		//搜索内容
			} else if (buttonPage != null) { 							//如果按下了页码提交按钮
				search 	= request.getParameter("search").trim(); 		//搜索内容
				pageInput	= request.getParameter("pageShow"); 		//页码输入框中的值
			} else { 													//点击了页码链接，或者刚打开此页
				if (request.getParameter("searchUrl") != null) { 
					search = request.getParameter("searchUrl"); 		//不需要进行解码操作，系统会自动解码
				}
				
				if (request.getParameter("pageUrl") != null) {			//地址栏中的页码
					pageInput = request.getParameter("pageUrl");
				}
			}
			
			int countRow = typeDao.queryCount(search);		//****获得记录总数
					
			int pageSize  = 6;  							//每页6条记录
			int pageCount = 0; 								//预设总页数为0
		
			if (countRow % pageSize == 0) {					//如果余数为0，即能整除
				pageCount = countRow / pageSize; 			//总页数
			} else {
				pageCount = countRow / pageSize + 1;		//不能整除则加1页。如果除数为小数，将自动去除小数部分得到整数			
			}
			
			try { 									 
				pageShow = Integer.parseInt(pageInput);		//如果是数字，返回字符串对应的整数
			} catch (Exception e) {
				//showPage = 1; 							//如果抛出异常，则取预设值
			}				
		
			if (pageShow < 1) {								//如果当前页码小于1
				pageShow = 1;
			} else if (pageShow > pageCount && pageCount >= 1) { 	//如果当前页码大于总页数，且总页数>=1
				pageShow = pageCount;
			}
			
			String searchUrl = "";
			
			if (search.equals("") == false) {
				searchUrl = URLEncoder.encode(search, "UTF-8");		//进行URL集数，以便在地址栏传递		
			}
			
			if (pageShow <= 1) {
				page += "<span style='color:gray;'>首页&ensp;";
				page += "上一页&ensp;</span>";
			} else { 
				page += "<a href='?pageUrl=1&searchUrl=" + searchUrl + "'>首页</a>&ensp;";
				page += "<a href='?pageUrl=" + (pageShow - 1) + "&searchUrl=" + searchUrl 
						+ "'>上一页</a>&ensp;";
			}
	
			if (pageShow >= pageCount) {
				page += "<span style='color:gray;'>下一页&ensp;";
				page += "尾页</span>";
			} else { 
				page += "<a href='?pageUrl=" + (pageShow + 1) + "&searchUrl=" + searchUrl 
						+ "'>下一页</a>&ensp;";
				page += "<a href='?pageUrl=" + pageCount + "&searchUrl=" + searchUrl + "'>尾页</a>";
			}
			
			page += "&emsp;&emsp;";
			page += "页码：" + pageShow + "/" + pageCount + "&emsp;";
			page += "记录数：" + countRow + "&emsp;&emsp;";	
			
			page += "输入页码:";
			page += "	<input type='text' name='pageShow' value='" + pageShow 
					+ "' style='width:40px; text-align:center;'>";
			page += "	<input type='submit' name='buttonPage' value='提交'>&emsp;";

			if (pageShow > 0) {
				countShowed = (pageShow - 1) * pageSize;						//（要略过的）之前的记录数
			}
			
			teleplayTypeList = typeDao.queryAll(search, countShowed, pageSize);		//****获取当前页的记录列表
			
			if (teleplayTypeList == null || teleplayTypeList.size() == 0) {
				msg = "（查无记录）";
				return false;
			}

			return true;
		
		} catch (Exception e) {
			msg = "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg = request.getAttribute("msg").toString() + msg;
			}
			request.setAttribute("msg", msg);
			
			if (request.getSession().getAttribute("msg") != null) {				//如果session中有消息。在详情页删除记录后保存的消息
				msg = request.getSession().getAttribute("msg").toString() + msg;//读取session中的消息
				request.getSession().removeAttribute("msg");					//从session中移除此键值
				request.setAttribute("msg", msg);								//将消息赋值给request
			}

			request.setAttribute("search", search);
			request.setAttribute("countShowed", countShowed);
			request.setAttribute("page", page);
			request.setAttribute("teleplayTypeList", teleplayTypeList);							//****传递对象
		}
		
		return false;
	}
	
	
	@Override
	public boolean existByTeleplayTypeName (TeleplayType type, HttpServletRequest request) {
		
		String msg = "";
		
		try {
			String teleplayTypeName = type.getTeleplayTypeName();
			
			TeleplayType typeSelect = typeDao.queryByTeleplayTypeName(teleplayTypeName);				//****根据teleplayTypeName获取记录
			
			if (typeSelect == null) {
				return false;
			}
			
			//msg = "类型列表中已存在相同的类型名称！";			
			return true;
		
		} catch (Exception e) {
			msg = "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg = request.getAttribute("msg").toString() + msg;
			}
			request.setAttribute("msg", msg);
		}
		
		return false;
	}


	@Override
	public boolean existByTeleplayTypeNameExceptTeleplayTypeId (TeleplayType type, HttpServletRequest request) {
		
		String msg = "";
		
		try {
			String teleplayTypeName = type.getTeleplayTypeName();
			String teleplayTypeId = type.getTeleplayTypeId();			
			
			TeleplayType typeSelect = typeDao.queryByTeleplayTypeNameExceptTeleplayTypeId(teleplayTypeName, teleplayTypeId);	//****获取记录
			
			if (typeSelect == null) {
				return false;
			}
			
			//msg = "其他类型已存在相同的类型名称！";
			return true;
		
		} catch (Exception e) {
			msg = "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg = request.getAttribute("msg").toString() + msg;
			}
			request.setAttribute("msg", msg);
		}
		
		return false;
	}


	@Override
	public String addTeleplayType (TeleplayType type, HttpServletRequest request) {
		
		String teleplayTypeId = "0";
		String msg = "";
		
		try {
			if (existByTeleplayTypeName(type, request)) {						//**** 本类中的方法	
				msg += "所输入的类型名称已经存在，请修改后再提交！";
				return teleplayTypeId;
			}
			
			int result = typeDao.addTeleplayType(type);						//****新添记录
			
			if (result == 0) {				
				msg += "新添记录失败！";
				return teleplayTypeId;
			}
			
			teleplayTypeId = type.getTeleplayTypeId();									//****获取新添记录的ID

			if (teleplayTypeId.equals("0")) {
				msg += "新添记录失败！请重试。";
				return teleplayTypeId;
			}

			msg += "新添记录成功。";	
			request.getSession().setAttribute("msg", msg);				//通过session传递消息。消息将显示在详情页中		
			return teleplayTypeId;												//返回新添记录的ID
		
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
		}
		
		return teleplayTypeId;
	}

	
	@Override
	public boolean editTeleplayType (TeleplayType type, HttpServletRequest request) {

		String msg = "";
		
		try {	
			String teleplayTypeId, teleplayTypeName, note;
			
			teleplayTypeId		= type.getTeleplayTypeId(); 								//从校验过的type中获取值
			teleplayTypeName  	= type.getTeleplayTypeName();
			note		= type.getNote();

			TeleplayType typeOld = typeDao.queryByTeleplayTypeId(teleplayTypeId);					//****	
			
			if (typeOld == null) {	
				msg += "对应的记录已不存在，请刷新页面后重试！";
				return false;
			}

			if (existByTeleplayTypeNameExceptTeleplayTypeId(type, request)) {				//**** 本类中的方法
				msg += "所输入的类型名称已经存在，请修改后再提交！";
				return false;
			}			

			if (teleplayTypeName.equals(typeOld.getTeleplayTypeName())) {
				type.setTeleplayTypeName(null);
			}
			if (note.equals(typeOld.getNote())) {
				type.setNote(null);
			}			

			int count = typeDao.editTeleplayType(type);							//****更新记录
			
			if (count == 0) {
				msg += "类型信息修改失败！请重试。。";
				return false;
			}

			List<Teleplay> teleplayList = goodsDao.queryByTeleplayTypeId(teleplayTypeId);
			if (teleplayList != null && teleplayList.size() > 0) {				//****如果在美剧记录中还在使用该类型

				count = goodsDao.editTeleplayForTeleplayTypeNameByTeleplayTypeId(teleplayTypeName, teleplayTypeId);	//****更新美剧记录中的类型名称
				
				if (count == 0) {
					msg += "类型信息修改失败！请重试。。";
					return false;
				}
			}

			msg += "类型信息修改成功！";
			request.getSession().setAttribute("msg", msg);					//通过session传递消息。消息将显示在详情页中
			return true;	
		
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
		}	
		
		return false;
	}

	
	@Override
	public boolean deleteByTeleplayTypeId (HttpServletRequest request) {
		
		String msg = "";
		
		try {	
			String teleplayTypeId = request.getParameter("teleplayTypeId"); 			//取得地址栏参数id
						
			try { 									 
				Integer.parseInt(teleplayTypeId);				
			} catch (Exception e) {
				msg = "参数teleplayTypeId错误！";
				return false;
			}

			if (typeDao.queryByTeleplayTypeId(teleplayTypeId) == null) {				//****	
				msg = "要删除的记录已不存在，请刷新页面后重试！";
				return false;
			}

			List<Teleplay> teleplayList = goodsDao.queryByTeleplayTypeId(teleplayTypeId);
			if (teleplayList != null && teleplayList.size() > 0) {			//****在删除此类型之前，需先判断在美剧记录中是否还在使用它
				msg = "所选的类型在美剧列表中已被使用，不许删除此类型！";
				return false;
			}
			
			int result = typeDao.deleteByTeleplayTypeId(teleplayTypeId);				//****删除这条记录
			
			if (result == 0) {
				msg = "删除记录失败！请重试。";
				return false;
			}

			msg += "删除记录成功。";
			return true;
		
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg = request.getAttribute("msg").toString() + msg;
			}
			request.setAttribute("msg", msg);
			
			request.getSession().setAttribute("msg", msg);				//通过session传递消息。消息将显示在列表页中
		}
		
		return false;
	}
	
	
	public boolean deleteByTeleplayTypeIdLot (String[] teleplayTypeIdArray, HttpServletRequest request) {		//被本类中的queryAll()方法调用
		
		String msg = "";
		
		try {
			String teleplayTypeIdLot = "";
			
			for (int i = 0; i < teleplayTypeIdArray.length; i++) {
				try { 
					String teleplayTypeId = teleplayTypeIdArray[i];														 
					Integer.parseInt(teleplayTypeId);									//检测是否为整数		

					if (typeDao.queryByTeleplayTypeId(teleplayTypeId) == null) {				//****	
						msg = "要删除的某条记录已不存在，请刷新页面后重试！";
						return false;
					}
					
					List<Teleplay> teleplayList = goodsDao.queryByTeleplayTypeId(teleplayTypeId);
					if (teleplayList != null && teleplayList.size() > 0) {			//****在删除某类型之前，需先判断在美剧记录中是否还在使用它
						msg = "所选的某个类型在美剧列表中已被使用，不许删除此类型！";
						return false;
					}
					
					teleplayTypeIdLot += "," + teleplayTypeIdArray[i];
					
				} catch (Exception e) {
					continue;													//略过此项
				}
			}
			
			if (teleplayTypeIdLot.isEmpty()) {
				return false;
			}
			teleplayTypeIdLot = teleplayTypeIdLot.substring(1);								//去除最前面的逗号
			
			int count = typeDao.deleteByTeleplayTypeIdLot(teleplayTypeIdLot);				//****删除这些记录

			if (count == 0) {
				msg += "删除记录失败！请重试。";
				return false;
			}

			msg = "删除记录成功。";
			return true;
		
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg = request.getAttribute("msg").toString() + msg;
			}
			request.setAttribute("msg", msg);
		}
		
		return false;	
	}	
}
