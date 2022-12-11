package com.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
//import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Repository;

import com.pojo.Teleplay;

@Repository							//持久层
@Mapper								//注解为映射
public interface TeleplayDao {
	
	@SelectProvider(type = TeleplayDaoProvider.class, method = "queryCount")			//如果search为空字符串时，应用动态SQL将能提高效率
	public int queryCount (String search);
	
	@SelectProvider(type = TeleplayDaoProvider.class, method = "queryAll")			//如果search为空字符串时，应用动态SQL将能提高效率
	public List<Teleplay> queryAll (String search, int countShowed, int pageSize);	//带3个参数
	
	
	@Select("select * from tb_teleplay where teleplayId = #{ teleplayId }")	
	public Teleplay queryByTeleplayId (String teleplayId);									//根据teleplayId查询
	
	
	@Select("select * from tb_teleplay where teleplayName = #{ teleplayName }")	
	public Teleplay queryByTeleplayName (String teleplayName);	
	
	
	@Select("select * from tb_teleplay where teleplayName = #{ teleplayName } and teleplayId != #{ teleplayId }")	
	public Teleplay queryByTeleplayNameExceptTeleplayId (String teleplayName, String teleplayId);	
	
	
	@Insert("insert into tb_teleplay "
			+ " (episode, teleplayName, teleplayTypeId, teleplayTypeName, loc, year,  plot) "
			+ " values(#{ episode }, #{ teleplayName }, #{ teleplayTypeId }, #{ teleplayTypeName }, "
			+ " #{ loc }, #{ year },  #{ plot })")
	@Options(useGeneratedKeys = true, keyProperty = "teleplayId")				//将主键自动生成的值，赋值给对象的userId属性
	public int addTeleplay (Teleplay teleplay);
	
	
	@Update("update tb_teleplay set image = #{ image } where teleplayId = #{ teleplayId }")
	public int editImage (String image, String teleplayId);	

	
	@UpdateProvider(type = TeleplayDaoProvider.class, method = "editTeleplay")
	int editTeleplay (Teleplay teleplay);			//采用基于注解的动态SQL
	
	
	@Delete("delete from tb_teleplay where teleplayId = #{ teleplayId }")
	public int deleteByTeleplayId (String teleplayId);	
	
	
	@Delete("delete from tb_teleplay where teleplayId in (${ teleplayIdLot })")		//注意：占位符中的符号是“$”。此种占位符表示直接替换
	public int deleteByTeleplayIdLot (String teleplayIdLot);						//批量删除
	
	//------------------------------------------------------------------------
	
	@Select("select * from tb_teleplay where teleplayTypeId = #{ teleplayTypeId }")			//在删除某类型之前，需先判断在美剧记录中是否还在使用该类型
	public List<Teleplay> queryByTeleplayTypeId (String teleplayTypeId);						//由于某类型可能对应多个美剧，故返回类型用List<Teleplay>
	
	
	@Delete("update tb_teleplay set teleplayTypeName = #{ teleplayTypeName } where teleplayTypeId = #{ teleplayTypeId }")	//更新某类型名称时，美剧记录中的该类型名称也要被更新
	public int editTeleplayForTeleplayTypeNameByTeleplayTypeId (String teleplayTypeName, String teleplayTypeId);
}
