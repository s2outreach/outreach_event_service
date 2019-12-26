package com.cts.outreach.event.model;

import java.util.List;

public class UserModel {
	
	private String userid;
	private String username;
	private String email;
	private List<EventModel> eventdata;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<EventModel> getEventdata() {
		return eventdata;
	}
	public void setEventdata(List<EventModel> eventdata) {
		this.eventdata = eventdata;
	}

}
