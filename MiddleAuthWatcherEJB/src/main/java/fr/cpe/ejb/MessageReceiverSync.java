package fr.cpe.ejb;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;

import fr.cpe.model.Role;
import fr.cpe.model.User;

@Stateless
public class MessageReceiverSync implements MessageReceiverSyncLocal {

	@Inject
	JMSContext context;
	
	@Resource(mappedName = "java:/jms/queue/watcherqueue")
	Queue queue;
	
	@Override
	public User receiveMessage() {

		Message message = context.createConsumer(queue).receive();
		
		try {
			if (message instanceof TextMessage) {
				System.out.println("Topic: I received a TextMessage at " + new Date());
				TextMessage msg = (TextMessage) message;
				System.out.println("Message is : " + msg.getText());
			} else if (message instanceof ObjectMessage) {
				System.out.println("Topic: I received an ObjectMessage at " + new Date());
				ObjectMessage msg = (ObjectMessage) message;
				if (msg.getObject() instanceof User) {
					User user = (User) msg.getObject();
					System.out.println("User Details: ");
					System.out.println("login:" + user.getLogin());
					System.out.println("pwd:" + user.getPassword());
					
					return user;
					/*Role currentTestRole = dataContainer.checkUser(user);
					if (Role.NONE == currentTestRole) {
						sender.sendMessage(user);
					} else {
						user.setRole(currentTestRole);
						sender.sendMessage(user);
					}*/
				}
			} else {
				System.out.println("Not valid message for this Queue MDB");
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
