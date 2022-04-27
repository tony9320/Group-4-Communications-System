import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.junit.jupiter.api.Test;

class UserTester {

	@Test
	public void testConstructor() {
		User myUser = new User("John","admin");
		
		assertTrue(myUser.getName().equals("John") &&
				myUser.getPassword().equals("admin"));
	}
	
	@Test
	public void testChangePassword() {
		User myUser = new User("John","admin");
		String newPassword = "apple";
		
		myUser.changePassword(newPassword);
		
		assertTrue(myUser.getPassword().equals(newPassword));
	}
	
	@Test 
	public void testSetName() {
		User myUser = new User("John","admin");
		String newName = "Bob";
		
		myUser.setName(newName);
		
		assertTrue(myUser.getName().equals(newName));
	}
	
	
	@Test
	public void testSetActiveChatRoom() {
		User myUser = new User("John","admin");
		String chatRoom = "CHATROOM12";
		
		myUser.setActiveChatRoom(chatRoom);
		
		assertTrue(myUser.getActiveChatRoom().equals(chatRoom));
	}
	
	@Test
	public void testGetName() {
		User myUser = new User("John","admin");
		assertTrue(myUser.getName().equals("John"));
	}
	
	@Test
	public void testGetPassword() {
		User myUser = new User("John","admin");
		assertTrue(myUser.getPassword().equals("admin"));
	}

}
