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
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.cts.outreach.event.entity.EventUserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class EventUserRepo {
	
	private Logger LOGGER = LoggerFactory.getLogger(EventUserRepo.class);
	
	@Autowired
	private DynamoDBMapper mapper;
	
	@Autowired
	private AmazonDynamoDB amazonDynamoDB;
	
	private DynamoDB dynamoDB;
	
	private static final String TABLENAME = "eventuser";
	
	public void addevent(EventUserEntity eventuser) {
		mapper.save(eventuser);
	}
	
	public List<EventUserEntity> getEventsForUser(String userid) throws Exception {
		dynamoDB = new DynamoDB(amazonDynamoDB);
		Table table = dynamoDB.getTable(TABLENAME);
		Index index = table.getIndex("userid");
		
		QuerySpec spec = new QuerySpec()
				.withKeyConditionExpression("userid = :a")
				.withValueMap(new ValueMap()
						.withString(":a", userid));
		ItemCollection<QueryOutcome> items = index.query(spec);
		
		return formatItems(items);
	}
	
	public List<EventUserEntity> getAllEventUsers() {
		ScanRequest scanRequest = new ScanRequest()
			    .withTableName(TABLENAME);
		ScanResult result = amazonDynamoDB.scan(scanRequest);
		List<EventUserEntity> allevents = new ArrayList<EventUserEntity>();
		for (Map<String, AttributeValue> item : result.getItems()){
			AttributeValue idAV = item.getOrDefault("id", new AttributeValue());
		    String id = idAV.getS();
			AttributeValue eventidAV = item.getOrDefault("eventid", new AttributeValue());
		    String eventid = eventidAV.getS();
		    AttributeValue eventnameAV = item.getOrDefault("eventname", new AttributeValue());
		    String eventname = eventnameAV.getS();
		    AttributeValue useridAV = item.getOrDefault("userid", new AttributeValue());
		    String userid = useridAV.getS();
		    AttributeValue usernameAV = item.getOrDefault("username", new AttributeValue());
		    String username = usernameAV.getS();
		    AttributeValue emailAV = item.getOrDefault("email", new AttributeValue());
		    String email = emailAV.getS();
		    EventUserEntity record = new EventUserEntity(id, eventid, userid, eventname, username, email);
		    allevents.add(record);
		}
		return allevents;
	}
	
	public List<EventUserEntity> formatItems(ItemCollection<QueryOutcome> items)  throws Exception {
		ObjectMapper objmapper = new ObjectMapper();
		List<EventUserEntity> allevents = new ArrayList<EventUserEntity>();
		items.forEach(item -> {
			EventUserEntity eventRecord;
			try {
				eventRecord = objmapper.readValue(item.toJSON(), EventUserEntity.class);
				LOGGER.info(eventRecord.getEventid());
				allevents.add(eventRecord);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		return allevents;
	}

}
