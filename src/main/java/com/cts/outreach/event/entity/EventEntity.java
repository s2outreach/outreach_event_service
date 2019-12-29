package com.cts.outreach.event.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="event")
public class EventEntity {
	
	private String eventid;
	private String eventname;
	private String eventdate;
	private String location;
	
	@DynamoDBHashKey(attributeName="eventid")
	@DynamoDBAutoGeneratedKey
	public String getEventid() {
		return eventid;
	}
	public void setEventid(String eventid) {
		this.eventid = eventid;
	}
	@DynamoDBAttribute
	public String getEventname() {
		return eventname;
	}
	@DynamoDBRangeKey(attributeName="eventdate")
	public String getEventdate() {
		return eventdate;
	}
	@DynamoDBAttribute
	public String getLocation() {
		return location;
	}
	public EventEntity(String eventid, String eventname, String eventdate, String location) {
		super();
		this.eventid = eventid;
		this.eventname = eventname;
		this.eventdate = eventdate;
		this.location = location;
	}	

}

