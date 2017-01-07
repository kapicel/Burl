package com.example.chat.task;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.chat.service.IUserService;

@Component
public class TeskUserClean {

	private final IUserService userService;

	@Autowired
	public TeskUserClean(IUserService userService){
		this.userService = userService;
	}
	
	/*@Scheduled(fixedRate = 1000 * 5 *60)
	private void removeInactiveUsers(){
		Set<Long> inactiveUsers = userService.f
				
	}*/
	
}
