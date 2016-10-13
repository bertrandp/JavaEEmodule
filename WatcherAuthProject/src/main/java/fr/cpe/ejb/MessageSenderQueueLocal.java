package fr.cpe.ejb;

import fr.cpe.model.User;

public interface MessageSenderQueueLocal {

	void sendMessage(String message);

	void sendMessage(User user);

}
