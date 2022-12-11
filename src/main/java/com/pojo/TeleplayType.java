package com.pojo;

public class TeleplayType {

	private String teleplayTypeId;				//类型ID
	private String teleplayTypeName;			//类型名称
	private String note;				//备注
	private String timeRenew;			//更新时间
	
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
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getTimeRenew() {
		return timeRenew;
	}
	public void setTimeRenew(String timeRenew) {
		this.timeRenew = timeRenew;
	}	
}
