package com.service;

import java.io.File;
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
public class TeleplayServiceImpl implements TeleplayService {

	@Autowired
	private TeleplayDao goodsDao;			//自动注入
	
	@Autowired
	private TeleplayTypeDao typeDao;
	
	
	@Override
	public boolean queryAll (HttpServletRequest request) {

		List<Teleplay> teleplayList = new ArrayList<Teleplay>(); 	//记录列表
		String search = ""; 								//搜索的内容
		int countShowed = 0;	 							//（要略过的）之前的记录数		
		int pageShow = 1; 									//当前页码					
		String page = ""; 									//页码链接组	
		String msg = "";
		
		try {
			if (request.getParameter("buttonDelete") != null) { 				//如果单击了删除按钮
				
				String[] teleplayIdArray = request.getParameterValues("teleplayId"); 	//获取ID列表	
				
				if (teleplayIdArray != null) {
					deleteByTeleplayIdLot(teleplayIdArray, request);					//****删除所选。调用在本类中定义的方法
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
			
			if (request.getParameter("buttonFilter") != null) { 		//如果单击了类型筛选按钮
				String teleplayTypeName = request.getParameter("teleplayTypeName"); 	//获取类型下拉列表的值		
				
				if (teleplayTypeName != null && teleplayTypeName.equals("") == false) {
					search = teleplayTypeName; 									//设为搜索内容
				}
			}
			
			int countRow = goodsDao.queryCount(search);	//****获得记录总数
					
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
			
			String mapName = request.getServletPath();				//当前的请求名称
			//System.out.println(mapName);
			
			if (pageShow <= 1) {
				page += "<span style='color:gray;'>首页&ensp;";
				page += "上一页&ensp;</span>";
			} else { 
				page += "<a href='" + mapName + "?pageUrl=1"
						+ "&searchUrl=" + searchUrl + "'>首页</a>&ensp;";
				page += "<a href='" + mapName + "?pageUrl=" + (pageShow - 1) 
						+ "&searchUrl=" + searchUrl + "'>上一页</a>&ensp;";
			}
	
			if (pageShow >= pageCount) {
				page += "<span style='color:gray;'>下一页&ensp;";
				page += "尾页</span>";
			} else { 
				page += "<a href='" + mapName + "?pageUrl=" + (pageShow + 1) 
						+ "&searchUrl=" + searchUrl + "'>下一页</a>&ensp;";
				page += "<a href='" + mapName + "?pageUrl=" + pageCount 
						+ "&searchUrl=" + searchUrl + "'>尾页</a>";
			}
			
			page += "&emsp;&emsp;";
			page += "页码：" + pageShow + "/" + pageCount + "&emsp;";
			page += "记录数：" + countRow + "&emsp;&emsp;";	
			
			page += "输入页码:";
			page += "	<input type='text' name='pageShow' value='" + pageShow 
					+ "' style='width:40px; text-align:center;'>";
			page += "	<input type='submit' name='buttonPage' value='提交'>&emsp;";

			if (pageShow > 0) {
				countShowed = (pageShow - 1) * pageSize;					//（要略过的）之前的记录数
			}
			
			teleplayList = goodsDao.queryAll(search, countShowed, pageSize);	//****获取当前页的记录列表
			
			if (teleplayList == null || teleplayList.size() == 0) {
				msg = "查无记录。";
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
			request.setAttribute("teleplayList", teleplayList);						//****传递对象
		}
		
		return false;
	}
	
	
	@Override
	public boolean queryByTeleplayId (HttpServletRequest request) {
		
		Teleplay teleplay = null;
		String msg = "";
		
		try {
			String teleplayId = request.getParameter("teleplayId"); 		//获取地址栏参数teleplayId的值 
			
			try { 									 
				Integer.parseInt(teleplayId);							//尝试转换为整数，即判断其是否为整数
			} catch (Exception e) {
				msg = "参数teleplayId错误！";
				return false;
			}	
			
			teleplay = goodsDao.queryByTeleplayId(teleplayId);				//****根据ID获取记录
			
			if (teleplay == null) {
				msg = "（查无此美剧记录）";
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
			
			request.setAttribute("teleplay", teleplay);								//****传递对象
		}
		
		return false;
	}


	@Override
	public boolean existByTeleplayName (Teleplay teleplay, HttpServletRequest request) {
		
		String msg = "";
		
		try {
			String teleplayName = teleplay.getTeleplayName();
			
			Teleplay goodsSelect = goodsDao.queryByTeleplayName(teleplayName);				//****根据teleplayName获取记录
			
			if (goodsSelect == null) {
				//msg = "在美剧列表中无相同的集数！";	
				return false;
			}
			
			//msg = "在美剧列表中已存在相同的集数！";			
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
	public boolean existByTeleplayNameExceptTeleplayId (Teleplay teleplay, HttpServletRequest request) {
		
		String msg = "";
		
		try {
			String teleplayName = teleplay.getTeleplayName();
			String teleplayId = teleplay.getTeleplayId();			
			
			Teleplay goodsSelect = goodsDao.queryByTeleplayNameExceptTeleplayId(teleplayName, teleplayId);	//****获取记录
			
			if (goodsSelect == null) {
				//msg = "在其他美剧中无相同的集数！";
				return false;
			}
			
			//msg = "在其他美剧中已存在相同的集数！";
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
	public String addTeleplay (Teleplay teleplay, HttpServletRequest request) {
		
		String teleplayId = "0";
		String msg = "";
		
		try {
			if (existByTeleplayName(teleplay, request)) {						//**** 本类中的方法	
				msg += "所输入的集数已经存在，请修改后再提交！";
				return teleplayId;
			}
			
			//-------------------------------------- 根据teleplayTypeId获取teleplayTypeName
			String teleplayTypeId, teleplayTypeName;
			
			teleplayTypeId = teleplay.getTeleplayTypeId();

			TeleplayType type = typeDao.queryByTeleplayTypeId(teleplayTypeId);					//****
			
			if (type == null) {			
				msg += "美剧类型对应的记录已不存在，请刷新页面后重试！";
				return teleplayId;
			}
			teleplayTypeName = type.getTeleplayTypeName();
			teleplay.setTeleplayTypeName(teleplayTypeName);

			
			int result = goodsDao.addTeleplay(teleplay);					//****新添记录
			
			if (result == 0) {				
				msg += "新添记录失败！";
				return teleplayId;
			}
			
			teleplayId = teleplay.getTeleplayId();								//****获取新添记录的ID

			if (teleplayId.equals("0")) {
				msg += "新添记录失败！请重试。";
				return teleplayId;
			}
			
			//------------------------ 将图片用新的teleplayId重命名，更新数据表中字段image的值
			String path = request.getServletContext().getRealPath("\\") + "\\"; 	//当前项目运行时所在的物理目录
			String uploadFolder = "fileUpload\\image\\";						//存放文件的子目录
			path += uploadFolder; 													//文件存放的目录

			String goodsUUID = request.getParameter("goodsUUID");					//从隐藏域控件中获取值
			
			if (goodsUUID == null || goodsUUID.equals("")) {
				msg += "获取通用唯一标识发生错误。";
				return teleplayId;
			}
			
			String imageUUID = (String) request.getSession().getAttribute("image" + goodsUUID);		//从session中获得图片名，含UUID	
			
			if (imageUUID != null && imageUUID.equals("") == false) {
				File fileOld = new File(path, imageUUID); 								//创建File对象
				
				if (fileOld.exists() == false || fileOld.isFile() == false) {			//如果不存在，或者不是文件
					msg += "上传的文件已不存在。";
					return teleplayId;
				}
				
				int index = imageUUID.lastIndexOf(".");					//路径中最后一个“.”的位置
				String nameExt = imageUUID.substring(index + 1);		//获取文件的扩展名
				
				String imageNew = teleplayId + "." + nameExt;				//用新记录的teleplayId生成的图片名			
				File fileNew = new File(path, imageNew); 				//创建File对象
				
				if (fileNew.exists()) {									//如果存在
					if (fileNew.delete() == false) {					//如果删除失败
						msg += "删除旧文件失败。";
						return teleplayId;
					}
				}
				
				if (fileOld.renameTo(fileNew) == false) {				//如果图片重命名失败
					msg += "文件重命名失败。";
					return teleplayId;
				}
							
				int count = goodsDao.editImage(imageNew, teleplayId);	//****更新记录。目的是根据teleplayId更新image字段
				
				if (count == 0) {
					msg += "数据表中的美剧图片名称更新失败！";
					return teleplayId;
				}
			}

			msg += "新添记录成功。";	
			request.getSession().setAttribute("msg", msg);				//通过session传递消息。消息将显示在详情页中		
			return teleplayId;												//返回新添记录的ID
		
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
		}
		
		return teleplayId;
	}


	@Override
	public boolean editTeleplay (Teleplay teleplay, HttpServletRequest request) {

		String msg = "";
		
		try {	
			String teleplayId, episode, teleplayName, teleplayTypeId, teleplayTypeName, loc, year, image, plot;
			
			teleplayId		= teleplay.getTeleplayId(); 							//从校验过的goods中获取值
			episode  	= teleplay.getEpisode();
			teleplayName  	= teleplay.getTeleplayName();
			teleplayTypeId  	= teleplay.getTeleplayTypeId();
			loc  		= teleplay.getLoc();
			year  		= teleplay.getYear();
//			timeSale  	= teleplay.getTimeSale();
			image  		= teleplay.getImage();
			plot		= teleplay.getPlot();

			Teleplay goodsOld = goodsDao.queryByTeleplayId(teleplayId);			//****	
			
			if (goodsOld == null) {	
				msg += "teleplayId号对应的记录已不存在，请刷新页面后重试！";
				return false;
			}

			if (existByTeleplayNameExceptTeleplayId(teleplay, request)) {			//**** 本类中的方法
				msg += "所输入的集数已经存在，请修改后再提交！";
				return false;
			}

			//-------------------------------------- 根据teleplayTypeId获取teleplayTypeName
			TeleplayType type = typeDao.queryByTeleplayTypeId(teleplayTypeId);					//****
			
			if (type == null) {			
				msg += "美剧类型对应的记录已不存在，请刷新页面后重试！";
				return false;
			}
			teleplayTypeName = type.getTeleplayTypeName();
			teleplay.setTeleplayTypeName(teleplayTypeName);
			
			
//			if (episode.equals(goodsOld.getEpisode())) {				//如果输入的值与数据表中的值相同
//				teleplay.setEpisode(null);									//将不予更新到数据表
//			}
			if (teleplayName.equals(goodsOld.getTeleplayName())) {
				teleplay.setTeleplayName(null);
			}
			if (teleplayTypeId.equals(goodsOld.getTeleplayTypeId())) {
				teleplay.setTeleplayTypeId(null);
			}
			if (teleplayTypeName.equals(goodsOld.getTeleplayName())) {
				teleplay.setTeleplayTypeName(null);
			}
			if (loc.equals(goodsOld.getLoc())) {
				teleplay.setLoc(null);
			}
			if (year.equals(goodsOld.getYear())) {
				teleplay.setYear(null);
			}
//			if (timeSale.equals(goodsOld.getTimeSale())) {
//				teleplay.setTimeSale(null);
//			}
			if (image == null || image.equals(goodsOld.getImage())) {	//修改美剧时，image=null。更新图片时，image有值
				teleplay.setImage(null);
			}
			if (plot.equals(goodsOld.getPlot())) {
				teleplay.setPlot(null);
			}			

			int count = goodsDao.editTeleplay(teleplay);					//****更新记录
			
			if (count == 0) {
				msg += "美剧信息修改失败！请重试。。";
				return false;
			}

			msg += "美剧信息修改成功！";
			request.getSession().setAttribute("msg", msg);				//通过session传递消息。消息将显示在详情页中
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
	public boolean deleteByTeleplayId (HttpServletRequest request) {
		
		String msg = "";
		
		try {	
			String teleplayId = request.getParameter("teleplayId"); 			//取得地址栏参数id
						
			try { 									 
				Integer.parseInt(teleplayId);				
			} catch (Exception e) {
				msg = "参数teleplayId错误！";
				return false;
			}
			
			Teleplay teleplay = goodsDao.queryByTeleplayId(teleplayId);			//****	
			
			if (teleplay == null) {
				msg += "teleplayId号对应的记录已不存在，请刷新页面后重试！";
				return false;
			}
			
			int result = goodsDao.deleteByTeleplayId(teleplayId);					//****删除这条记录
			
			if (result == 0) {
				msg = "删除记录失败！请重试。";
				return false;
			}

			msg += "删除记录成功。";		

			String path = request.getServletContext().getRealPath("\\") + "\\"; //当前项目运行时所在的物理目录
			String uploadFolder = "fileUpload\\image\\";					//存放文件的子目录
			path += uploadFolder; 	
			
			String image = teleplay.getImage();
			
			if (image == null || image.trim().equals("")) {						//如果image无效
				return true;
			}
			
			File file = new File(path, image);									//创建File对象
			
			if (file.exists()) {												//如果已存在同名文件，应先删除它
				if (file.delete() == false) {									//如果删除失败
					msg += "将文件" + image + "删除时失败。";
					return false;
				}
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
			
			request.getSession().setAttribute("msg", msg);				//通过session传递消息。消息将显示在列表页中
		}
		
		return false;
	}	
	
	
	public boolean deleteByTeleplayIdLot (String[] teleplayIdArray, HttpServletRequest request) {		//被本类中的queryAll()方法调用
		
		String msg = "";	
		String path = request.getServletContext().getRealPath("\\") + "\\"; 	//当前项目运行时所在的物理目录
		String uploadFolder = "fileUpload\\image\\";								//存放文件的子目录
		path += uploadFolder; 	
		
		try {
			Teleplay teleplay = null;
			String teleplayIdLot = "";
			
			for (int i = 0; i < teleplayIdArray.length; i++) {
				try { 									 
					Integer.parseInt(teleplayIdArray[i]);							//检测是否为整数
					
					teleplay = goodsDao.queryByTeleplayId(teleplayIdArray[i]);			//****	
					
					if (teleplay == null) {
						//msg += "teleplayId号对应的记录已不存在，请刷新页面后重试！";
						continue;
					}
					
					teleplayIdLot += "," + teleplayIdArray[i];
					
					String image = teleplay.getImage();
					
					if (image == null || image.trim().equals("")) {				//如果image无效
						continue;
					}
					
					File file = new File(path, image);							//创建File对象
					
					if (file.exists()) {										//如果已存在同名文件，应先删除它
						if (file.delete() == false) {							//如果删除失败
							continue;
						}
					}
					
				} catch (Exception e) {
					continue;													//略过此项
				}
			}
			
			if (teleplayIdLot.isEmpty()) {
				return false;
			}
			teleplayIdLot = teleplayIdLot.substring(1);								//去除最前面的逗号
			
			int count = goodsDao.deleteByTeleplayIdLot(teleplayIdLot);				//****删除这些记录

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
