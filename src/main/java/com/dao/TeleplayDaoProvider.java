package com.dao;

import org.apache.ibatis.jdbc.SQL;

import com.pojo.Teleplay;

//@Repository		注意：此处不能应用@Repository注解。因为这里由MyBatis应用了AOP动态代理技术
public class TeleplayDaoProvider {

	public String queryCount(String search) {

		String sql = new SQL() {
			{
				SELECT("count(*)");
				FROM("tb_teleplay");

				if (search != null && search.trim().equals("") == false) { // 如果有搜索内容
					WHERE("CONCAT_WS(',', teleplayName, teleplayTypeName) " + " like CONCAT('%', #{ search }, '%')");
				}
			}
		}.toString();
//		System.out.println("queryCount's sql: " + sql);
		return sql;
	}

	public String queryAll(String search, int countShowed, int pageSize) { // 带3个参数

		String sql = new SQL() {
			{
				SELECT("*");
				FROM("tb_teleplay");

				if (search != null && search.trim().equals("") == false) { // 如果有搜索内容
					WHERE("CONCAT_WS(',', teleplayName, teleplayTypeName) " + " like CONCAT('%', #{ search }, '%')");
				}

				ORDER_BY("teleplayName");
				LIMIT("#{ countShowed }, #{ pageSize }");
			}
		}.toString();

		return sql;
	}

	public String editTeleplay(Teleplay teleplay) {

		String episode, teleplayName, teleplayTypeId, teleplayTypeName, loc, year, image, plot;

		episode = teleplay.getEpisode();
		teleplayName = teleplay.getTeleplayName();
		teleplayTypeId = teleplay.getTeleplayTypeId();
		teleplayTypeName = teleplay.getTeleplayTypeName();
		loc = teleplay.getLoc();
		year = teleplay.getYear();
//		timeSale  	= teleplay.getTimeSale();
		image = teleplay.getImage();
		plot = teleplay.getPlot();

		String sql = new SQL() {
			{ // 匿名类方式构造
				UPDATE("tb_teleplay");

				if (episode != null) { // 如果输入的值与数据表中的值不同，才更新
					SET("episode = #{ episode }");
				}
				if (teleplayName != null) {
					SET("teleplayName = #{ teleplayName }");
				}
				if (teleplayTypeId != null) {
					SET("teleplayTypeId = #{ teleplayTypeId }");
				}
				if (teleplayTypeName != null) {
					SET("teleplayTypeName = #{ teleplayTypeName }");
				}
				if (loc != null) {
					SET("loc = #{ loc }");
				}
				if (year != null) {
					SET("year = #{ year }");
				}
				if (image != null) {
					SET("image = #{ image }");
				}
				if (plot != null) {
					SET("plot = #{ plot }");
				}

				WHERE("teleplayId = #{ teleplayId }");
			}
		}.toString();

		return sql;
	}
}
