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
import com.cts.outreach.event.repo.EventRepo;
import com.cts.outreach.event.repo.EventUserRepo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
public class EventController {
	
	@Autowired
	private EventRepo eventRepo;
	
	@Autowired
	private EventUserRepo eventUserRepo;
	
	private Logger LOGGER = LoggerFactory.getLogger(EventUserRepo.class);
	
	@PostMapping("/addEvent")
	public String addEvent(@RequestBody EventEntity newEvent) {
		LOGGER.info("New event added " + newEvent.getEventname());
		eventRepo.addevent(newEvent);
		return "success";
	}
	
	@GetMapping("/getAllEvents")
	public List<EventEntity> getAllRides() {
		LOGGER.info("All Events requested");
		return eventRepo.getAllevents();
	}
	
	@PostMapping("/addUserForEvent")
	public String addUserForEvent(@RequestBody EventUserEntity eventuser) {
		LOGGER.info("User " + eventuser.getUsername() + " added for the event " + eventuser.getEventname());
		eventUserRepo.addevent(eventuser);
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
	
	@GetMapping("/getAllEventUsers")
	public List<EventUserEntity> getAllEventUsers() {
		LOGGER.info("All users for all events requested");
		return eventUserRepo.getAllEventUsers();
	}

}
