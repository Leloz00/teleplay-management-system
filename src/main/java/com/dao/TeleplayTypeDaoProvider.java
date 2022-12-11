package com.dao;

import org.apache.ibatis.jdbc.SQL;

import com.pojo.TeleplayType;

//@Repository		注意：此处不能应用@Repository注解。因为这里由MyBatis应用了AOP动态代理技术
public class TeleplayTypeDaoProvider {
	
	public String queryCount (String search) {
		
		String sql = new SQL() {{
			SELECT("count(*)");
			FROM("tb_teleplaytype");
			
			if (search != null && search.trim().equals("") == false) {			//如果有搜索内容
				WHERE("teleplayTypeName like CONCAT('%', #{ search }, '%')");
			}
		}}.toString();
			
		return sql;
	}
	
	public String queryAll (String search, int countShowed, int pageSize) { 	//带3个参数
		
		String sql = new SQL() {{
			SELECT("*");
			FROM("tb_teleplaytype");
			
			if (search != null && search.trim().equals("") == false) {			//如果有搜索内容
				WHERE("teleplayTypeName like CONCAT('%', #{ search }, '%')");
			}

			ORDER_BY("teleplayTypeName");
			LIMIT("#{ countShowed }, #{ pageSize }");
		}}.toString();
			
		return sql;
	}
	
	public String editTeleplayType (TeleplayType type) {
		
		String teleplayTypeName, note;
		
		teleplayTypeName  	= type.getTeleplayTypeName();
		note  		= type.getNote();
		
		String sql = new SQL() {{									//匿名类方式构造
				UPDATE("tb_teleplaytype");
				
				if (teleplayTypeName != null) {
					SET("teleplayTypeName = #{ teleplayTypeName }");
				}
				if (note != null) {
					SET("note = #{ note }");
				}
				
				WHERE("teleplayTypeId = #{ teleplayTypeId }");
		}}.toString();
			
		return sql;
	}
}
