package com.cts.outreach.event.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.outreach.event.repo.UserInterface;

@Service
public class UserService {
	
	private Logger LOGGER = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserInterface userInterface;
	
	public Long getUserCount() {
		LOGGER.info("Number of users requested");
		return userInterface.count();
	}

}
