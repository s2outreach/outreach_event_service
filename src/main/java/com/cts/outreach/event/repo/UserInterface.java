package com.cts.outreach.event.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.outreach.event.entity.UserEntity;

public interface UserInterface extends JpaRepository<UserEntity, Long>{
	
}
