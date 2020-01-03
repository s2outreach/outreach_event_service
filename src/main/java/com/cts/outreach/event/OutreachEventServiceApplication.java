package com.cts.outreach.event;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.cts.outreach.event.entity.EventEntity;
import com.cts.outreach.event.entity.EventUserEntity;
import com.cts.outreach.event.repo.EventRepo;
import com.cts.outreach.event.repo.EventUserRepo;
import com.netflix.appinfo.AmazonInfo;

@SpringBootApplication
@EnableDiscoveryClient
public class OutreachEventServiceApplication {
	
	private Logger LOGGER = LoggerFactory.getLogger(OutreachEventServiceApplication.class);
	
	@Autowired
	private EventRepo eventRepo;
	
	@Autowired
	private EventUserRepo eventUserRepo;

	public static void main(String[] args) {
		SpringApplication.run(OutreachEventServiceApplication.class, args);
	}
	
	@Primary
	@Bean
	@Autowired
	@Profile("aws")
	public EurekaInstanceConfigBean eurekaInstanceConfig(InetUtils inetUtils) {
	    EurekaInstanceConfigBean config = new EurekaInstanceConfigBean(inetUtils);
	    AmazonInfo info = AmazonInfo.Builder.newBuilder().autoBuild("event");
	    config.setHostname(info.get(AmazonInfo.MetaDataKey.publicHostname));
	    config.setIpAddress(info.get(AmazonInfo.MetaDataKey.publicIpv4));
	    config.setNonSecurePort(2222);
	    config.setDataCenterInfo(info);
	    LOGGER.info(info.get(AmazonInfo.MetaDataKey.publicIpv4));
	    LOGGER.info(info.get(AmazonInfo.MetaDataKey.publicHostname));
	    return config;
	   }

	@Bean
	public CommandLineRunner loadData() {
		return (args) -> {
			File file = new File(
					getClass().getClassLoader().getResource("event.csv").getFile()
				);

			System.out.println("got file");
			InputStream in = getClass().getResourceAsStream("/event.csv");
//	        try (FileReader reader = new FileReader(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in)); 

	        	System.out.println("reading file");
	            String line;
	            int rowNum = 1;
	            int id = 0;
	            while ((line = reader.readLine()) != null) {
	            	String[] words = line.split(",");
	                EventEntity event = new EventEntity(words[0], words[1], words[2], words[3]);
	                eventRepo.addevent(event);

	        		int numVolunteers = 0;
	        		int i = 0;
	        		if ( rowNum <= 24) {
	        			numVolunteers = 7;
	        		} else if ( rowNum <= 60) {
	        			numVolunteers = 15;
	        		} else if ( rowNum <= 94) {
	        			numVolunteers = 25;
	        		}
	        		while(i < numVolunteers) {
	        			EventUserEntity newEventUserEntity = 
	        					new EventUserEntity(Integer.toString(id), words[0], Integer.toString(i), event.getEventname(), "volunteer" + Integer.toString(i), "volunteer" + Integer.toString(i) + "@testmail.com");
	        			eventUserRepo.addevent(newEventUserEntity);
	        			i = i + 1;

	        		}
	        		rowNum = rowNum + 1;
	            }
		};
	}

}
