package com.cts.outreach.event.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.outreach.event.entity.EventEntity;
import com.cts.outreach.event.entity.EventUserEntity;
import com.cts.outreach.event.model.EventModel;
import com.cts.outreach.event.model.LogModel;
import com.cts.outreach.event.model.RespModel;
import com.cts.outreach.event.model.UserCountRespModel;
import com.cts.outreach.event.model.UserModel;
import com.cts.outreach.event.repo.EventRepo;
import com.cts.outreach.event.repo.EventUserRepo;
import com.cts.outreach.event.service.KafkaProducer;
import com.cts.outreach.event.service.UserService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class EventController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EventRepo eventRepo;
	
	@Autowired
	private EventUserRepo eventUserRepo;
	
	private final KafkaProducer producer;
	
	@Autowired
	public EventController(KafkaProducer producer) {
		this.producer = producer;
	}
	
	private Logger LOGGER = LoggerFactory.getLogger(EventUserRepo.class);
	
	@GetMapping("/getUserCount")
	public UserCountRespModel getUserCount() {
		return (new UserCountRespModel(userService.getUserCount() - 1));
	}
	
	@PostMapping("/addEvent")
	public RespModel addEvent(@RequestBody EventEntity newEvent) throws Exception {
		LOGGER.info("New event added " + newEvent.getEventname());
		eventRepo.addevent(newEvent);
		
		LogModel log = new LogModel(newEvent.getEventname(), "", "Event added");
		ObjectMapper mapper = new ObjectMapper();
		String obj = mapper.writeValueAsString(log);
		this.producer.sendLog(obj);
		
		return (new RespModel("success"));
	}
	
	@GetMapping("/getAllEvents")
	public List<EventEntity> getAllEvents() {
		LOGGER.info("All Events requested");
		return eventRepo.getAllevents();
	}
	
	@PostMapping("/addUserForEvent")
	public RespModel addUserForEvent(@RequestBody EventUserEntity eventuser) throws Exception {
		LOGGER.info("User " + eventuser.getUsername() + " added for the event " + eventuser.getEventname());
		eventUserRepo.addevent(eventuser);
		
		LogModel log = new LogModel(eventuser.getEventname(), eventuser.getUsername(), "User registered");
		ObjectMapper mapper = new ObjectMapper();
		String obj = mapper.writeValueAsString(log);
		this.producer.sendLog(obj);
		
		return (new RespModel("success"));
	}
	
	@GetMapping("/getEventReport")
	public List<EventModel> getEventReport() throws JsonParseException, JsonMappingException, IOException {
		LOGGER.info("Event report requested");
		List<EventEntity> allEvents =  eventRepo.getAllevents();
		List<EventUserEntity> allUsers = eventUserRepo.getAllEventUsers();
		Map<String, EventModel> eventMap = new HashMap<>();
		allEvents.forEach((eachEvent) -> {
			EventModel eventDataRecord = new EventModel();
			eventDataRecord.setEventid(eachEvent.getEventid());
			eventDataRecord.setEventname(eachEvent.getEventname());
			eventDataRecord.setEventdate(eachEvent.getEventdate());
			eventDataRecord.setEventlocation(eachEvent.getLocation());
			eventMap.put(eachEvent.getEventid(), eventDataRecord);
		});
		
		allUsers.forEach((eachUser) -> {
			UserModel user = new UserModel();
			user.setUsername(eachUser.getUsername());
			user.setEmail(eachUser.getEmail());
			EventModel event = eventMap.get(eachUser.getEventid());
			List<UserModel> users = event.getUserdata();
			if (users == null ) {
				System.out.println("no user");
				users = new ArrayList<UserModel>();
			}
			users.add(user);
			event.setUserdata(users);
		});
		
		List<EventModel> resp = new ArrayList<EventModel>();
		eventMap.forEach((key, value ) -> {
			resp.add(value);
		});
		return resp;
		
	}
	
	@GetMapping("/getUserReport")
	public List<UserModel> getUserReport() throws JsonParseException, JsonMappingException, IOException {
		LOGGER.info("User report requested");
		List<EventUserEntity> allEvents =  eventUserRepo.getAllEventUsers();
		List<EventUserEntity> allUsers = eventUserRepo.getAllEventUsers();
		Map<String, UserModel> userMap = new HashMap<>();
		allUsers.forEach((eachUser) -> {
			UserModel userDataRecord = new UserModel();
			userDataRecord.setUserid(eachUser.getUserid());
			userDataRecord.setUsername(eachUser.getUsername());
			userDataRecord.setEmail(eachUser.getEmail());
			userMap.put(eachUser.getUserid(), userDataRecord);
		});
		
		allEvents.forEach((eachEvent) -> {
			EventModel event = new EventModel();
			event.setEventname(eachEvent.getEventname());
			UserModel user = userMap.get(eachEvent.getUserid());
			List<EventModel> events = user.getEventdata();
			if (events == null ) {
				events = new ArrayList<EventModel>();
			}
			events.add(event);
			user.setEventdata(events);
		});
		
		List<UserModel> resp = new ArrayList<UserModel>();
		userMap.forEach((key, value ) -> {
			resp.add(value);
		});
		return resp;
	}
	
	@GetMapping("/getEventsForUser")
	public List<EventUserEntity> getEventsForUser(@RequestParam String userid) throws Exception {
		LOGGER.info("All events requested for user " + userid);
		return eventUserRepo.getEventsForUser(userid);
	}

}
