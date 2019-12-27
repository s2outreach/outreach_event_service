package com.cts.outreach.event.repo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.cts.outreach.event.entity.EventEntity;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class EventRepo {
	
	private Logger LOGGER = LoggerFactory.getLogger(EventRepo.class);
	
	@Autowired
	private DynamoDBMapper mapper;
	
	@Autowired
	private AmazonDynamoDB amazonDynamoDB;
	
	private static final String TABLENAME = "event";
	
	public void addevent(EventEntity event) {
		mapper.save(event);
	}
	
	public List<EventEntity> getAllevents() {
		ScanRequest scanRequest = new ScanRequest()
			    .withTableName(TABLENAME);
		ScanResult result = amazonDynamoDB.scan(scanRequest);
		List<EventEntity> allevents = new ArrayList<EventEntity>();
		for (Map<String, AttributeValue> item : result.getItems()){
			AttributeValue eventidAV = item.getOrDefault("eventid", new AttributeValue());
		    String eventid = eventidAV.getS();
		    AttributeValue eventnameAV = item.getOrDefault("eventname", new AttributeValue());
		    String eventname = eventnameAV.getS();
		    AttributeValue eventdateAV = item.getOrDefault("eventdate", new AttributeValue());
		    String eventdate = eventdateAV.getS();
		    AttributeValue locationAV = item.getOrDefault("location", new AttributeValue());
		    String location = locationAV.getS();
		    EventEntity record = new EventEntity(eventid, eventname, eventdate, location);
		    allevents.add(record);
		}
		return allevents;
	}
}

