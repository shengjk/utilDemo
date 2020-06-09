package xmht.java8;


import com.sun.istack.internal.NotNull;

import java.util.Optional;

public class OptionalTest {
	
	
	private String getUserName(User user) {
		Optional<User> userOptional = Optional.ofNullable(user);
		return userOptional.map(User::getUserName).orElse(null);
	}
	
	private String getUserName1(@NotNull User user) {
		if (user != null) {
			return user.getUserName();
		}
		return null;
	}
	
	
	class User {
		private final String userName;
		
		public User(String userName) {
			this.userName = userName;
		}
		
		public String getUserName() {
			return userName;
		}
	}
}
