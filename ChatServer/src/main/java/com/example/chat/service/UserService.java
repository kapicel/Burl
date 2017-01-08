package com.example.chat.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.chat.model.User;
import com.example.chat.protocol.Message;
import com.example.chat.protocol.Protocol;

@Service
public class UserService implements IUserService {
	
	private static final Logger LOGGER=LoggerFactory.getLogger(UserService.class);
	private final Map<Long,User> users=new HashMap<>();
	private final Set<Long> HessianList=new HashSet<>();
	private final Set<Long> BurlapList=new HashSet<>();
	
	@Override
	public long login(String username) {
		User user = new User(username);
		users.put(user.getId(), user);
		LOGGER.debug("Zalogowano <{}>, <{}>", user.getId(), user.getUsername());
		return user.getId();
	}

	@Override
	public void logout(long userId) {
		users.remove(userId);
		User.removeID(userId);
		LOGGER.debug("Wylogowano <{}>", userId);
	}

	@Override
	public void sendMessage(long authorID, Message message) {
		LOGGER.debug("Nowa wiadomość od <{}>", authorID);
		if(HessianList.contains(authorID)){
			for(long id:HessianList){
				users.get(id).newMessage(message);
				users.get(id).readMessages().size();
			}
		}
		else if(BurlapList.contains(authorID)){
			for(long id:BurlapList){
				users.get(id).newMessage(message);
				users.get(id).readMessages().size();

			}
		}
		else{
			System.out.println("jestem w userservice nigdzie :(");
		}
	}

	@Override
	public void changeTechnology(long author, Protocol protocol) {
		switch(protocol){
		case HESSIAN:
				HessianList.add(author);
				BurlapList.remove(author);
			break;
		case BURLAP:
				BurlapList.add(author);
				HessianList.remove(author);
			break;
		}
		
	}
	
	@Override
	public List<Message> readMessagesForUser(long userId) {
		System.out.println("Nas jest tylu : "+users.get(userId).readMessages().size());
		return users.get(userId).readMessages();
	}

	


}
