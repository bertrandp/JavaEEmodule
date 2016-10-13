package fr.cpe.ejb;

import fr.cpe.model.User;

public interface MessageSenderLocal {

	void sendMessage(String message);

	void sendMessage(User user);

}
