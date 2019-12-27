package com.cts.outreach.event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.cts.outreach.event.entity.EventEntity;
import com.cts.outreach.event.entity.EventUserEntity;
import com.cts.outreach.event.repo.EventUserRepo;
import com.fasterxml.jackson.databind.JsonMappingException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EventUserRepo.class)
@Import(DynamoDBConfigLocalTest.class)
public class EventUserRepoTest {
	
	@Autowired
	EventUserRepo eventUserRepo;
	
	private static DynamoDBMapper mapper;
	private static AmazonDynamoDB amazonDynamoDB;
	private static DynamoDB dynamoDB;
	
	private static String TABLENAME = "eventuser";
	private static String ATTRIBUTE1 = "id";
	private static String ATTRIBUTE2 = "eventname";
	private static String ATTRIBUTE3 = "eventid";
	private static String ATTRIBUTE4 = "userid";
	
	@Before
    public void setupClass() throws InterruptedException {
        amazonDynamoDB = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
                .build();
        mapper = new DynamoDBMapper(amazonDynamoDB);
        dynamoDB = new DynamoDB(amazonDynamoDB);
        
        Table table = dynamoDB.getTable(TABLENAME);
        System.out.println("Issuing DeleteTable request for " + TABLENAME);
//        table.delete();

        System.out.println("Waiting for " + TABLENAME + " to be deleted...this may take a while...");
//        table.waitForDelete();
        
        ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
        
        ArrayList<KeySchemaElement> tableKeySchema = new ArrayList<KeySchemaElement>();
        tableKeySchema.add(new KeySchemaElement().withAttributeName(ATTRIBUTE1).withKeyType(KeyType.HASH));
        tableKeySchema.add(new KeySchemaElement().withAttributeName(ATTRIBUTE2).withKeyType(KeyType.RANGE));
        
        attributeDefinitions.add(new AttributeDefinition(ATTRIBUTE1, ScalarAttributeType.S));
        attributeDefinitions.add(new AttributeDefinition(ATTRIBUTE2, ScalarAttributeType.S));
        attributeDefinitions.add(new AttributeDefinition(ATTRIBUTE3, ScalarAttributeType.S));
        attributeDefinitions.add(new AttributeDefinition(ATTRIBUTE4, ScalarAttributeType.S));
        
        ArrayList<GlobalSecondaryIndex> globalSecondaryIndex = new ArrayList<GlobalSecondaryIndex>();
        GlobalSecondaryIndex eventid = new GlobalSecondaryIndex()
        	    .withIndexName(ATTRIBUTE3)
        	    .withProvisionedThroughput(new ProvisionedThroughput()
        	        .withReadCapacityUnits((long) 1)
        	        .withWriteCapacityUnits((long) 1))
        	        .withProjection(new Projection().withProjectionType(ProjectionType.ALL));
        ArrayList<KeySchemaElement> eventidKeySchema = new ArrayList<KeySchemaElement>();
        eventidKeySchema.add(new KeySchemaElement()
        	    .withAttributeName(ATTRIBUTE3)
        	    .withKeyType(KeyType.HASH));
        eventid.setKeySchema(eventidKeySchema);
        
        GlobalSecondaryIndex userid = new GlobalSecondaryIndex()
        	    .withIndexName(ATTRIBUTE4)
        	    .withProvisionedThroughput(new ProvisionedThroughput()
        	        .withReadCapacityUnits((long) 1)
        	        .withWriteCapacityUnits((long) 1))
        	        .withProjection(new Projection().withProjectionType(ProjectionType.ALL));
        ArrayList<KeySchemaElement> driveridKeySchema = new ArrayList<KeySchemaElement>();
        driveridKeySchema.add(new KeySchemaElement()
        	    .withAttributeName(ATTRIBUTE4)
        	    .withKeyType(KeyType.HASH));
        userid.setKeySchema(driveridKeySchema);
        
        globalSecondaryIndex.add(eventid);
        globalSecondaryIndex.add(userid);
        
        CreateTableRequest createTableRequest = new CreateTableRequest()
        	    .withTableName(TABLENAME)
        	    .withProvisionedThroughput(new ProvisionedThroughput()
        	        .withReadCapacityUnits((long) 5)
        	        .withWriteCapacityUnits((long) 1))
        	    .withAttributeDefinitions(attributeDefinitions)
        	    .withKeySchema(tableKeySchema)
        	    .withGlobalSecondaryIndexes(globalSecondaryIndex);
        
//        table = dynamoDB.createTable(createTableRequest);
//        table.waitForActive();
        
        System.out.println(table.getDescription());
            
    }	
	@Test
	public void addeventTest() {
		EventUserEntity newEventUserEntity = 
				new EventUserEntity("1", "1", "1", "test event1", "test user1", "test mail1");
		eventUserRepo.addevent(newEventUserEntity);
	}
	
	@Test
	public void getEventsForUserTest() throws Exception {
		EventUserEntity newEventUserEntity = 
				new EventUserEntity("1", "1", "1", "test event1", "test user1", "test mail1");
		eventUserRepo.addevent(newEventUserEntity);
		List<EventUserEntity> allEvents = eventUserRepo.getEventsForUser("1");
	}
	
	@Test
	public void getAllEventUsersTest() throws Exception {
		EventUserEntity newEventUserEntity = 
				new EventUserEntity("1", "1", "1", "test event1", "test user1", "test mail1");
		eventUserRepo.addevent(newEventUserEntity);
		List<EventUserEntity> allEvents = eventUserRepo.getAllEventUsers();
	}
		
}

