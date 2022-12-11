package com.pojo;

public class Teleplay {
	
	private String teleplayId;			//美剧ID
	private String episode;			//美剧编号
	private String teleplayName;		//名称
	private String teleplayTypeId;			//美剧类型ID
	private String teleplayTypeName;		//美剧类型名称
	private String loc;			//地区
	private String year;			//年份数量
	private String image;			//美剧图片
	private String plot;			//剧情简介
	private String video;
	
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	
	public String getTeleplayId() {
		return teleplayId;
	}
	public void setTeleplayId(String teleplayId) {
		this.teleplayId = teleplayId;
	}
	public String getEpisode() {
		return episode;
	}
	public void setEpisode(String episode) {
		this.episode = episode;
	}
	public String getTeleplayName() {
		return teleplayName;
	}
	public void setTeleplayName(String teleplayName) {
		this.teleplayName = teleplayName;
	}
	public String getTeleplayTypeId() {
		return teleplayTypeId;
	}
	public void setTeleplayTypeId(String teleplayTypeId) {
		this.teleplayTypeId = teleplayTypeId;
	}
	public String getTeleplayTypeName() {
		return teleplayTypeName;
	}
	public void setTeleplayTypeName(String teleplayTypeName) {
		this.teleplayTypeName = teleplayTypeName;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getPlot() {
		return plot;
	}
	public void setPlot(String plot) {
		this.plot = plot;
	}
}
