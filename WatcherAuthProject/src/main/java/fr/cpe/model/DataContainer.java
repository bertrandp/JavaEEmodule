package fr.cpe.model;

import java.util.HashMap;
import java.util.Map;

public class DataContainer {
	
	private Map<String, User> userMap = new HashMap<>();
	
	public DataContainer(){
		userMap.put("tpadmin", new User("tpadmin","tp", null, null, Role.ADMIN));
		userMap.put("tpwatcher", new User("tpwatcher","tp", null, null, Role.WATCHER));
	}

	public Role checkUser(User user) {
		
		if(userMap.get(user.getLogin()) != null){
			return userMap.get(user.getLogin()).getRole();
		}
		return Role.NONE;
	}

}
