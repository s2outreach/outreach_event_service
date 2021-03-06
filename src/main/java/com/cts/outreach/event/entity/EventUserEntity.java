package com.cts.outreach.event.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="eventuser")
public class EventUserEntity {
	
	private String id;
	private String eventid;
	private String userid;
	private String eventname;
	private String username;
	private String email;
	
	@DynamoDBHashKey(attributeName="id")
	@DynamoDBAutoGeneratedKey
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@DynamoDBAttribute
	public String getEventid() {
		return eventid;
	}
	public void setEventid(String eventid) {
		this.eventid = eventid;
	}
	@DynamoDBAttribute
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	@DynamoDBRangeKey(attributeName="eventname")
	public String getEventname() {
		return eventname;
	}
	public void setEventname(String eventname) {
		this.eventname = eventname;
	}
	@DynamoDBAttribute
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
	public EventUserEntity(String id, String eventid, String userid, String eventname, String username, String email) {
		this.id = id;
		this.eventid = eventid;
		this.userid = userid;
		this.eventname = eventname;
		this.username = username;
		this.email = email;
	}
	public EventUserEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

}
