import java.io.ObjectOutputStream;

public class User {
	protected String name;
	protected String password;
	protected ObjectOutputStream outputStream;
	protected String activeChatRoom;

	public User() {}

	public User(String name, String password) {
		this.name = name;
		this.password = password;
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
		return this.outputStream;
	}
	
	public String getActiveChatRoom() {
		return this.activeChatRoom;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getPassword() {
		return this.password;
	}

}
