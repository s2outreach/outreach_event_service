package com.cts.outreach.event.controller;

import java.io.IOException;
import java.util.List;

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
import com.cts.outreach.event.model.LogModel;
import com.cts.outreach.event.model.UpdateReqModel;
import com.cts.outreach.event.repo.EventRepo;
import com.cts.outreach.event.repo.EventUserRepo;
import com.cts.outreach.event.service.KafkaProducer;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class EventController {
	
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
	
	@PostMapping("/addEvent")
	public String addEvent(@RequestBody EventEntity newEvent) throws Exception {
		LOGGER.info("New event added " + newEvent.getEventname());
		eventRepo.addevent(newEvent);
		
		LogModel log = new LogModel(newEvent.getEventname(), "", "Event added");
		ObjectMapper mapper = new ObjectMapper();
		String obj = mapper.writeValueAsString(log);
		this.producer.sendLog(obj);
		
		return "success";
	}
	
	@GetMapping("/getAllEvents")
	public List<EventEntity> getAllRides() {
		LOGGER.info("All Events requested");
		return eventRepo.getAllevents();
	}
	
	@PostMapping("/addUserForEvent")
	public String addUserForEvent(@RequestBody EventUserEntity eventuser) throws Exception {
		LOGGER.info("User " + eventuser.getUsername() + " added for the event " + eventuser.getEventname());
		eventUserRepo.addevent(eventuser);
		
		LogModel log = new LogModel(eventuser.getEventname(), eventuser.getUsername(), "User registered");
		ObjectMapper mapper = new ObjectMapper();
		String obj = mapper.writeValueAsString(log);
		this.producer.sendLog(obj);
		
		return "success";
	}
	
	@GetMapping("/getUsersForEvent")
	public List<EventUserEntity> getUsersForEvent(@RequestParam String eventid) throws JsonParseException, JsonMappingException, IOException {
		LOGGER.info("All users requested for event " + eventid);
		return eventUserRepo.getUsersForEvent(eventid);
	}
	
	@GetMapping("/getEventsForUser")
	public List<EventUserEntity> getEventsForUser(@RequestParam String userid) throws JsonParseException, JsonMappingException, IOException {
		LOGGER.info("All events requested for user " + userid);
		return eventUserRepo.getEventsForUser(userid);
	}
	
	@GetMapping("/getAllEventsUsers")
	public List<EventUserEntity> getAllEventsUsers() {
		LOGGER.info("All users for all events requested");
		return eventUserRepo.getAllEventUsers();
	}
	
	@PostMapping("/updateStatus")
	public String updateStatus(@RequestBody UpdateReqModel updateRequest) throws Exception {
		LOGGER.info("Update requested " + updateRequest.getId() + " " + updateRequest.getEventname() + " " + updateRequest.getUserstatus());
		eventUserRepo.updateStatus(updateRequest.getId(), updateRequest.getEventname(), updateRequest.getUserstatus());
		
		LogModel log = new LogModel(updateRequest.getEventname(), updateRequest.getUsername(), "User registered");
		ObjectMapper mapper = new ObjectMapper();
		String obj = mapper.writeValueAsString(log);
		this.producer.sendLog(obj);
		
		return "success";
	}

}
