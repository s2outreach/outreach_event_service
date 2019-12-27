package com.cts.outreach.event;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.cts.outreach.event.controller.EventController;
import com.cts.outreach.event.entity.EventEntity;
import com.cts.outreach.event.entity.EventUserEntity;
import com.cts.outreach.event.repo.EventRepo;
import com.cts.outreach.event.repo.EventUserRepo;
import com.cts.outreach.event.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@ComponentScan(basePackages = {"com.cts.outreach.event"})
@AutoConfigureMockMvc
@SpringBootTest
class OutreachEventServiceApplicationTests {

	@Autowired
	MockMvc mockMvc;
	MvcResult mvcResult;
	
	@Autowired
	EventController eventController;
	
	@MockBean
	UserService userService;
	
	@MockBean
	EventRepo eventRepo;
	
	@MockBean
	EventUserRepo eventUserRepo;
	
	@Test
	public void getUserCountTest() throws Exception {
		Mockito.when(userService.getUserCount()).thenReturn(10L);
		this.mockMvc.perform(get("/getUserCount"))
		.andExpect(status().isOk());
	}
	
	@Test
	public void addEventTest() throws Exception {
//		Mockito.when(eventRepo.addevent(ArgumentMatchers.any())).doNothing();
		EventEntity newEvent = new EventEntity("1", "test event1", "test date1", "test location1");
		
		this.mockMvc.perform(post("/addEvent")
				.contentType(MediaType.APPLICATION_JSON)
	            .content(asJsonString(newEvent)))
		.andExpect(status().isOk());
	}
	
	@Test
	public void getAllEventsTest() throws Exception {
		List<EventEntity> allEvents = new ArrayList<EventEntity>();
		allEvents.add(new EventEntity("1", "test event1", "test date1", "test location1"));
		allEvents.add(new EventEntity("2", "test event2", "test date2", "test location2"));
		allEvents.add(new EventEntity("3", "test event3", "test date3", "test location3"));
		
		Mockito.when(eventRepo.getAllevents()).thenReturn(allEvents);
		this.mockMvc.perform(get("/getAllEvents"))
		.andExpect(status().isOk());
	}
	
	@Test
	public void addUserForEventTest() throws Exception {
//		Mockito.when(eventRepo.addevent(ArgumentMatchers.any())).doNothing();
		EventUserEntity newEventUserEntity = 
				new EventUserEntity("1", "1", "1", "test event1", "test user1", "test mail1");
		
		this.mockMvc.perform(post("/addUserForEvent")
				.contentType(MediaType.APPLICATION_JSON)
	            .content(asJsonString(newEventUserEntity)))
		.andExpect(status().isOk());
	}
	
	@Test
	public void getEventReportTest() throws Exception {
		List<EventEntity> allEvents = new ArrayList<EventEntity>();
		allEvents.add(new EventEntity("1", "test event1", "test date1", "test location1"));
		allEvents.add(new EventEntity("2", "test event2", "test date2", "test location2"));
		allEvents.add(new EventEntity("3", "test event3", "test date3", "test location3"));
		
		List<EventUserEntity> allEventUsers = new ArrayList<EventUserEntity>();
		allEventUsers.add( 
				new EventUserEntity("1", "1", "1", "test event1", "test user1", "test mail1"));
		allEventUsers.add( 
				new EventUserEntity("2", "1", "2", "test event1", "test user2", "test mail2"));
		allEventUsers.add( 
				new EventUserEntity("3", "2", "1", "test event2", "test user1", "test mail1"));
		
		Mockito.when(eventRepo.getAllevents()).thenReturn(allEvents);
		Mockito.when(eventUserRepo.getAllEventUsers()).thenReturn(allEventUsers);
		
		this.mockMvc.perform(get("/getEventReport"))
		.andExpect(status().isOk());
	}
	
	@Test
	public void getUserReportTest() throws Exception {
		List<EventEntity> allEvents = new ArrayList<EventEntity>();
		allEvents.add(new EventEntity("1", "test event1", "test date1", "test location1"));
		allEvents.add(new EventEntity("2", "test event2", "test date2", "test location2"));
		allEvents.add(new EventEntity("3", "test event3", "test date3", "test location3"));
		
		List<EventUserEntity> allEventUsers = new ArrayList<EventUserEntity>();
		allEventUsers.add( 
				new EventUserEntity("1", "1", "1", "test event1", "test user1", "test mail1"));
		allEventUsers.add( 
				new EventUserEntity("2", "1", "2", "test event1", "test user2", "test mail2"));
		allEventUsers.add( 
				new EventUserEntity("3", "2", "1", "test event2", "test user1", "test mail1"));
		
		Mockito.when(eventRepo.getAllevents()).thenReturn(allEvents);
		Mockito.when(eventUserRepo.getAllEventUsers()).thenReturn(allEventUsers);
		
		this.mockMvc.perform(get("/getUserReport"))
		.andExpect(status().isOk());
	}
	
	@Test
	public void getEventsForUserTest() throws Exception {
		
		List<EventUserEntity> allEventUsers = new ArrayList<EventUserEntity>();
		allEventUsers.add( 
				new EventUserEntity("1", "1", "1", "test event1", "test user1", "test mail1"));
		allEventUsers.add( 
				new EventUserEntity("2", "1", "2", "test event1", "test user2", "test mail2"));
		allEventUsers.add( 
				new EventUserEntity("3", "2", "1", "test event2", "test user1", "test mail1"));
		
		Mockito.when(eventUserRepo.getEventsForUser(ArgumentMatchers.anyString()))
		.thenReturn(allEventUsers);
		
		this.mockMvc.perform(get("/getEventsForUser").param("userid", "1"))
		.andExpect(status().isOk());
	}
	
	public static String asJsonString(final Object obj) throws Exception {
		return new ObjectMapper().writeValueAsString(obj);
    }
	
}
