package com.cts.outreach.event.model;

import java.util.List;

public class EventModel {
	
	private String eventid;
	private String eventname;
	private String eventdate;
	private String eventlocation;
	private List<UserModel> userdata;
	
	public String getEventid() {
		return eventid;
	}
	public void setEventid(String eventid) {
		this.eventid = eventid;
	}
	public String getEventname() {
		return eventname;
	}
	public void setEventname(String eventname) {
		this.eventname = eventname;
	}
	public String getEventdate() {
		return eventdate;
	}
	public void setEventdate(String eventdate) {
		this.eventdate = eventdate;
	}
	public String getEventlocation() {
		return eventlocation;
	}
	public void setEventlocation(String eventlocation) {
		this.eventlocation = eventlocation;
	}
	public List<UserModel> getUserdata() {
		return userdata;
	}
	public void setUserdata(List<UserModel> userdata) {
		this.userdata = userdata;
	}
	

}
