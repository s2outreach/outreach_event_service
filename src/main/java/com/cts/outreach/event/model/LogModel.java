package com.cts.outreach.event.model;

public class LogModel {
	
	private String logid;
	private String eventname;
	private String username;
	private String action;
	public String getLogid() {
		return logid;
	}
	public String getEventname() {
		return eventname;
	}
	public String getUsername() {
		return username;
	}
	public String getAction() {
		return action;
	}
	public LogModel(String logid, String eventname, String username, String action) {
		super();
		this.logid = logid;
		this.eventname = eventname;
		this.username = username;
		this.action = action;
	}

}
