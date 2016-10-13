package fr.cpe.ejb;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.jboss.logging.Logger;

import fr.cpe.model.DataContainer;
import fr.cpe.model.Role;
import fr.cpe.model.User;

import javax.ejb.MessageDriven;
import javax.inject.Inject;

import java.util.Date;

import javax.ejb.ActivationConfigProperty;

@MessageDriven(
		activationConfig = {
				@ActivationConfigProperty(
						propertyName = "destinationType",
						propertyValue = "javax.jms.Topic"),
				@ActivationConfigProperty(
						propertyName = "destination",
						propertyValue = "java:/jms/watcherAuthJMS")
		})
public class AuthWatcherMsgDrivenEJB implements MessageListener {
	
	Logger log = Logger.getLogger(AuthWatcherMsgDrivenEJB.class.getName());
	
	@Inject
	MessageSenderQueueLocal sender;

	private DataContainer dataContainer;
	
	public AuthWatcherMsgDrivenEJB() {
		dataContainer=new DataContainer();
	}
	
	@Override
	public void onMessage(Message message) {

		try {
			if (message instanceof TextMessage) {
				log.info("Topic: I received a TextMessage at " + new Date());
				TextMessage msg = (TextMessage) message;
				log.info("Message is : " + msg.getText());
			} else if (message instanceof ObjectMessage) {
				log.info("Topic: I received an ObjectMessage at " + new Date());
				ObjectMessage msg = (ObjectMessage) message;
				if (msg.getObject() instanceof User) {
					User user = (User) msg.getObject();
					log.info("User Details: ");
					log.info("login:" + user.getLogin());
					log.info("pwd:" + user.getPassword());
					Role currentTestRole = dataContainer.checkUser(user);
					if (Role.NONE == currentTestRole) {
						sender.sendMessage(user);
					} else {
						user.setValidAuth(true);
						user.setRole(currentTestRole);
						sender.sendMessage(user);
					}
				}
			} else {
				log.info("Not valid message for this Queue MDB");
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}
	
	

}
