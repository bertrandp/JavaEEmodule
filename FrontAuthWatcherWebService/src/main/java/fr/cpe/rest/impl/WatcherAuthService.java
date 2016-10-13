package fr.cpe.rest.impl;

import java.util.logging.Logger;

import javax.inject.Inject;

import fr.cpe.ejb.MessageReceiverSyncLocal;
import fr.cpe.ejb.MessageSenderLocal;
import fr.cpe.model.User;
import fr.cpe.rest.IWatcherAuthService;

public class WatcherAuthService implements IWatcherAuthService {
	
	private static Logger log = Logger.getLogger(WatcherAuthService.class.getName());
	
	
	@Inject 
	MessageSenderLocal sender;
	
	@Inject
	MessageReceiverSyncLocal receiver;

	@Override
	public User auth(User user) {

		log.info(user.toString());
		
		sender.sendMessage(user);
		
		User userResp = receiver.receiveMessage();
		
		log.info("Receive resp user: " + userResp.toString());
		
		return userResp;
	}

	@Override
	public String echo() {
		return "echo";		
	}

}
