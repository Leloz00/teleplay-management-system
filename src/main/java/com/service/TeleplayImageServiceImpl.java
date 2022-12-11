package com.service;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.TeleplayDao;
import com.pojo.Teleplay;

@Service
@Transactional					//开启数据库事务，如果进行数据的增、改、删操作时发生非检查异常，数据将回滚
public class TeleplayImageServiceImpl implements TeleplayImageService {

	@Autowired
	TeleplayDao goodsDao;

	String uploadFolder = "fileUpload\\image\\";								//存放文件的子目录
	
	
	/**------------------------------- 生成美剧图片标签和图片链接 */
	@Override
	public String getImageTag (HttpServletRequest request, String image) {
		
		String msg = "";
			
		try {	
			String path = request.getServletContext().getRealPath("\\") + "\\"; 	//当前项目运行时所在的物理目录
			path += uploadFolder; 			//System.out.println("【文件上传路径】：" + path);
			
			msg = "（无图片）";
			
			if (image == null || image.trim().equals("")) {
				return msg;
			}
			
			File file = new File(path, image); 										//创建File对象
			
			if (file.exists() == false || file.isFile() == false) {					//如果不存在，或者不是文件
				return msg;
			}
	
			uploadFolder = uploadFolder.replace("\\", "/");							// 将\替换为/
			String urlImage = uploadFolder + image + "?t=" + (new Date()).getTime();//加上时间参数以显示最新图片
			
			msg = "<a href='" + urlImage + "' target='_blank'><img src='" + urlImage + "' height='100'></a>";		
			return msg;
			
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		}
		
		return msg;
	}	
		
	/**------------------------------- 生成指定图片的标签、指定图片的链接和管理链接 */
	@Override
	public String getImageTagAndLink (HttpServletRequest request, String image) {
		
		String msg = "";		
		
		try {	
			String teleplayId = request.getParameter("teleplayId");						//从URL中获取值
		
			String action  = "teleplayImageUploadDo?teleplayId=" + teleplayId + "&image=" + image;
			request.setAttribute("action", action);									//给表单的action赋值
	
			String imageTag = "";			
			imageTag = getImageTag(request, image);									//****生成美剧图片标签和图片链接
			
			msg = imageTag;
			msg += "<div style='margin-top:60px;'>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;";
			
			if (image.equals("") == false) {
				String urlDelete = "imageDeleteDo?teleplayId=" + teleplayId + "&image=" + image + "&imageDelete=yes";
				msg += "<a href='" + urlDelete + "' onclick='return confirm(\"确认删除吗？\")'>删除此图片</a>&emsp;|&emsp;";	
			}
			
			if (teleplayId.length() > 30) {											//如果是新添美剧，则teleplayId为长度是36的字符串
				msg += "<a href='teleplayList' target='_top'>美剧列表</a>";				
			} else {																//如果是修改美剧，则teleplayId为一个整数
				msg += "<a href='teleplayShow?teleplayId=" + teleplayId + "' target='_top'>美剧详情</a>";
				msg += "&emsp;<a href='teleplayList' target='_top'>美剧列表</a>";	
			}
				
			msg += "</div>";
			return msg;
			
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		}
		
		return msg;
	}		

	/**------------------------------- 生成美剧图片的标签、图片链接和管理链接。图片名称来自于地址栏中的参数值 */
	@Override
	public String getImageTagAndLink (HttpServletRequest request) {	
		
		String msg = "";		
		
		try {	
			String image = request.getParameter("image");							//值来自于地址栏中的参数
			
			msg = getImageTagAndLink(request, image);								//****生成美剧图片标签和图片链接
			
			return msg;
			
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		}
		
		return msg;
	}
	
	/**------------------------------- 上传图片，并更新数据表中的image字段，或在新添美剧页中更新session中的image的值 */
	@Override	
	public boolean teleplayImageUploadDo (HttpServletRequest request) {
		
		String msg = "";	
		String teleplayId = "";
		String image = "";			
		String imageNew = "";
		
		try {	
			teleplayId = request.getParameter("teleplayId");							//从URL中获取值			
			image   = request.getParameter("image");	
			
			String imageTag = "";
			String imageTagAndLink = "";
			HttpSession session = request.getSession();							//获取session对象		
			
			if (teleplayId.length() > 30) {										//如果是新添美剧
				if (session.getAttribute("image" + teleplayId) == null) {			//如果此session还没创建
					session.setAttribute("image" + teleplayId, image);				//创建session并保存
				}
				
				image = (String) session.getAttribute("image" + teleplayId);		//从session中获取值
			}
			
			imageTag = getImageTag(request, image);								//****生成美剧图片标签和图片链接
			imageTagAndLink = getImageTagAndLink(request, image);				//****获取美剧图片的标签、图片链接和管理链接
			msg = imageTagAndLink;												//在没有上传任何图片时，能显示原来的图片

			String path = request.getServletContext().getRealPath("\\") + "\\"; //当前项目运行时所在的物理目录
			path += uploadFolder; 												//文件存放的目录
				
			File folder = new File(path); 										//创建File对象
			
			if (folder.exists() == false) {										//如果目录不存在
				if (folder.mkdirs() == false) { 								//创建多层目录，false表示创建失败
					msg = imageTag + "&emsp;&emsp;";
					msg += "图片没有更新。文件存放的目录创建失败！";
					return false;
				}
			}
					
			DiskFileItemFactory factory;									//磁盘文件工厂对象
			factory = new DiskFileItemFactory();
			
			ServletFileUpload upload;										//文件上传对象
			upload = new ServletFileUpload(factory);
			
			List<FileItem> listFileItem = null;								//文件元素列表
			
			try {
				listFileItem = upload.parseRequest(request);				//开始读取上传信息，得到列表	
			} catch (Exception e) {
				msg = imageTag + "&emsp;&emsp;";
				msg += "图片没有更新。没有有效输入！";
				return false;
			}
			
			for (FileItem fileItem: listFileItem) {							//对于每一个对象
				
				if (fileItem.isFormField()) {								//如果是表单字段
					continue;												//执行下一轮循环
				}
				//---------往下：得到的是文件对象了
				
				long size = fileItem.getSize();								//文件大小			
				size = (size + 1023) / 1024;								//转换为KB。如果1<=size<=1024，则结果为1
							
				if (size > 2 * 1024) {
					msg = imageTag + "&emsp;&emsp;";
					msg += "图片没有更新。上传的图片的大小不能超过2M。";
					break;
				}
			
				String fileName = fileItem.getName();						//文件名，而IE浏览器返回的是文件的整个路径
				
				if (fileName.equals("")) {									//未选择文件
					continue;
				}
					
				int index = fileName.lastIndexOf("\\");						//路径中最后一个“\”的位置
				if (index >= 0)	{											//如果有“\”（对于IE浏览器）
					fileName = fileName.substring(index + 1);				//获取最后的文件名（对于IE浏览器）
				}
								
				index = fileName.lastIndexOf(".");							//路径中最后一个“.”的位置

				if (index == -1)	{										//没有“.”
					msg = imageTag + "&emsp;&emsp;";
					msg += "图片没有更新。请上传图片文件。";
					break;
				}					
				
				if (index >= 0)	{													//如果有“.”
					String nameExt = fileName.substring(index + 1).toLowerCase();	//获取文件的扩展名，并转换为小写字母						
					String nameExtAll = ",jpg,jpeg,png,gif,bmp,";					//允许上传的图片类型
					
					if (nameExtAll.indexOf("," + nameExt + ",") == -1) {			//如果前者没包含后者						
						msg = imageTag + "&emsp;&emsp;";
						msg += "图片没有更新。请上传jpg,jpeg,png,gif,bmp类型图片。";
						break;
					}

					imageNew = teleplayId + "." + nameExt;							//新上传图片的名称
				}
				
				File fileNew = new File(path, imageNew);						//创建File对象
				
				if (fileNew.exists()) {											//如果已存在同名文件，应先删除它
					if (fileNew.delete() == false) {							//如果删除失败
						msg = imageTag + "&emsp;&emsp;";
						msg += "图片没有更新。与上传文件同名的文件已存在，但在将已有文件删除时失败。";
						continue;
					}
				}
	
				fileItem.write(fileNew);										//将缓冲的文件以fileNew的名称和位置保存到磁盘
				
				imageTag = getImageTag(request, imageNew);						//****生成美剧图片的标签和链接
				imageTagAndLink = getImageTagAndLink(request, imageNew);		//****获取美剧图片的标签、图片链接和管理链接
				msg = imageTagAndLink;											//在页面中显示新图片
					
				if (fileNew.exists()) {											//如果文件已上传成功
					if (imageNew.equals(image) == false) {						//如果新文件名与旧文件名不相同
					
						if (teleplayId.length() > 30) {							//如果是新添美剧
							session.setAttribute("image" + teleplayId, imageNew);	//新图片的名称保存到session	
							
						} else {												//如果是修改美剧
							
							Teleplay teleplay = new Teleplay();
							teleplay.setTeleplayId(teleplayId);
							teleplay.setImage(imageNew);
							
							int count = goodsDao.editImage(imageNew, teleplayId);	//****更新数据表中的image字段
							
							if (count == 0) {
								msg = imageTag + "&emsp;&emsp;";
								msg += "图片没有更新。数据表中的美剧图片修改失败！请重试。";
								
								if (fileNew.delete() == false) {				//如果删除失败
									msg += "将新上传的文件删除时失败。";
								}
								
								continue;
							}
						}
						
						if (image.equals("") == false) {						//如果旧文件名非空
							File fileOld = new File(path, image);				//创建File对象
						
							if (fileOld.exists()) {								//如果该文件存在，应删除它
								if (fileOld.delete() == false) {				//如果删除失败
									msg = imageTag + "&emsp;&emsp;";
									msg += "将旧文件删除时失败。";
								}
							}
						}
						
						image = imageNew;										//更新图片名称。先删旧图片然后在此处更新image值
					}
				}
			}

			return true;
			
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
			
			String action  = "teleplayImageUploadDo?teleplayId=" + teleplayId + "&image=" + image;
			request.setAttribute("action", action);								//给表单的action赋值
		}	
		
		return false;
	}
	
	/**------------------------------- 删除图片 */
	@Override
	public Teleplay imageDeleteDo (HttpServletRequest request) {
		
		String msg = "";
		Teleplay teleplay = null;
		
		try {	
			String teleplayId = request.getParameter("teleplayId");					//从URL中获取值			
			String image   = request.getParameter("image");	
			
			teleplay = new Teleplay();
			teleplay.setTeleplayId(teleplayId);
			teleplay.setImage(image);
			
			String imageTag = "";
			imageTag = getImageTag(request, image);								//****生成美剧图片标签和图片链接
			
			HttpSession session = request.getSession();							//获取session对象		
			
			if (teleplayId.length() > 30) {										//如果是新添美剧
				if (session.getAttribute("image" + teleplayId) == null) {			//如果此session还没创建
					session.setAttribute("image" + teleplayId, image);				//创建session并保存
				}
				
				image = (String) session.getAttribute("image" + teleplayId);		//从session中获取值
				teleplay.setImage(image);
			}
			
			if (image.equals("")) {
				return teleplay;
			}
	
			String path = request.getServletContext().getRealPath("\\") + "\\"; //当前项目运行时所在的物理目录
			path += uploadFolder; 
						
			File file = new File(path, image);
			
			if (file.exists()) {
				if (file.delete() == false) {
					msg = imageTag + "&emsp;&emsp;";
					msg += "删除图片失败！";
					return teleplay;
				}
			}
			
			image = "";															//重置image的值
			teleplay.setImage(image);
			
			if (teleplayId.length() > 30) {										//如果是新添美剧
				session.setAttribute("image" + teleplayId, image);					//设置值为空字符串
			} else {															//如果是修改美剧
				
				int count = goodsDao.editImage(image, teleplayId);				//****更新数据表中的image字段
				
				if (count == 0) {
					msg = imageTag + "&emsp;&emsp;";
					msg += "数据表中的美剧图片值清空失败！请重试。";
				}
			}
			
			return teleplay;
			
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
		}	
		
		return teleplay;
	}
}
