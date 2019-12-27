package com.cts.outreach.event.model;

public class LogModel {
	
	private String eventname;
	private String username;
	private String action;
	public String getEventname() {
		return eventname;
	}
	public String getUsername() {
		return username;
	}
	public String getAction() {
		return action;
	}
	public LogModel(String eventname, String username, String action) {
		super();
		this.eventname = eventname;
		this.username = username;
		this.action = action;
	}

}
