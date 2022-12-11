package com.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Repository;

import com.pojo.TeleplayType;

@Repository							//持久层
@Mapper								//注解为映射
public interface TeleplayTypeDao {
	
	@Select("select teleplayTypeId, teleplayTypeName from tb_teleplaytype order by teleplayTypeName")	
	public List<TeleplayType> queryAllForGOODS ();			
	
	
	@Select("select * from tb_teleplaytype where teleplayTypeId = #{ teleplayTypeId }")	
	public TeleplayType queryByTeleplayTypeId (String teleplayTypeId);
	
	
	//--------------------------------------------------------------------------
	
	@SelectProvider(type = TeleplayTypeDaoProvider.class, method = "queryCount")		//如果search为空字符串时，应用动态SQL将能提高效率
	public int queryCount (String search);
	

	@SelectProvider(type = TeleplayTypeDaoProvider.class, method = "queryAll")			//如果search为空字符串时，应用动态SQL将能提高效率
	public List<TeleplayType> queryAll (String search, int countShowed, int pageSize);	//带3个参数
	
	
	@Select("select * from tb_teleplaytype where teleplayTypeName = #{ teleplayTypeName }")	
	public TeleplayType queryByTeleplayTypeName (String teleplayTypeName);		
	
	
	@Select("select * from tb_teleplaytype where teleplayTypeName = #{ teleplayTypeName } and teleplayTypeId != #{ teleplayTypeId }")	
	public TeleplayType queryByTeleplayTypeNameExceptTeleplayTypeId (String teleplayTypeName, String teleplayTypeId);	
	
	
	@Insert("insert into tb_teleplaytype (teleplayTypeName, note) values(#{ teleplayTypeName }, #{ note })")
	@Options(useGeneratedKeys = true, keyProperty = "teleplayTypeId")			//将主键自动生成的值，赋值给对象的teleplayTypeId属性
	public int addTeleplayType (TeleplayType type);
	
	
	@UpdateProvider(type = TeleplayTypeDaoProvider.class, method = "editTeleplayType")
	int editTeleplayType (TeleplayType type);			//采用基于注解的动态SQL
	
	
	@Delete("delete from tb_teleplaytype where teleplayTypeId = #{ teleplayTypeId }")
	public int deleteByTeleplayTypeId (String teleplayTypeId);

	
	@Delete("delete from tb_teleplaytype where teleplayTypeId in (${ teleplayTypeIdLot })")		//注意：占位符中的符号是“$”。此种占位符表示直接替换
	public int deleteByTeleplayTypeIdLot (String teleplayTypeIdLot);
}
