package project;

import java.io.ObjectOutputStream;
import java.io.Serializable;

public class User implements Serializable {
	protected String name;
	protected String password;
	protected ObjectOutputStream outputStream;
	protected String activeChatRoom;

	public User() {}

	public User(String name, String password) {
		this.name = name;
		this.password = password;
		outputStream = null;
		activeChatRoom = null;
	}	
	
	public void changePassword(String password) {
		this.password = password;
	}	
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setObjectOutputStream(ObjectOutputStream stream) {
		this.outputStream = stream;
	}
	
	public void setActiveChatRoom(String chat) {
		this.activeChatRoom = chat;
	}
	
	
	public ObjectOutputStream getObjectOutputStream() {
		return outputStream;
	}
	
	public String getActiveChatRoom() {
		return activeChatRoom;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPassword() {
		return password;
	}

}
