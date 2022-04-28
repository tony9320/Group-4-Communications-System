import static org.junit.jupiter.api.Assertions.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.junit.jupiter.api.Test;

public class ChatroomTester {

	@Test
	public void testAddUser() {
		try {
			FileOutputStream justNeedAnOutputStream = new FileOutputStream("b");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(justNeedAnOutputStream);
			User user1 = new User();
			User user2 = new User();
			Message myMessage = new Message();
			ChatRoom myChatroom = new ChatRoom(user1,myMessage,objectOutputStream);
			myChatroom.addUser(user2, objectOutputStream);
			assertTrue(myChatroom.getChatUsersSize() == 2);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetRoomName() {
		User user1 = new User();
		Message myMessage = new Message("hello","MyChatRoom");
		ChatRoom myChatroom = new ChatRoom(user1,myMessage,null);
		assertTrue(myChatroom.getRoomName().equals("MyChatRoom"));
	}
	
	@Test
	public void testisLocked() {
		User user1 = new User();
		Message myMessage = new Message("hello","MyChatRoom");
		ChatRoom myChatroom = new ChatRoom(user1,myMessage,null);
		assertTrue(myChatroom.isLocked() == false);
	}
	
	@Test
	public void testSetChatLock() {
		User user1 = new User();
		Message myMessage = new Message("hello","MyChatRoom");
		ChatRoom myChatroom = new ChatRoom(user1,myMessage,null);
		try {
			FileOutputStream justNeedAnOutputStream = new FileOutputStream("b");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(justNeedAnOutputStream);
			myChatroom.setChatLock(user1, myMessage, objectOutputStream);
			assertTrue(myChatroom.isLocked() == true);
		}
		catch (IOException e) {
			e.printStackTrace();
		}


	}
	
	@Test
	public void testSetChatUnlock() {
		User user1 = new User();
		Message myMessage = new Message("hello","MyChatRoom");
		ChatRoom myChatroom = new ChatRoom(user1,myMessage,null);
		try {
			FileOutputStream justNeedAnOutputStream = new FileOutputStream("b");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(justNeedAnOutputStream);
			myChatroom.setChatLock(user1, myMessage, objectOutputStream);
			myChatroom.setChatLock(user1, myMessage, objectOutputStream);
			myChatroom.setChatUnlock(user1, myMessage, objectOutputStream);
			assertTrue(myChatroom.isLocked() == false);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRemoveUser() {
		try {
			FileOutputStream justNeedAnOutputStream = new FileOutputStream("b");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(justNeedAnOutputStream);
			User user1 = new User();
			User user2 = new User();
			User user3 = new User();
			Message myMessage = new Message("hello","MyChatRoom");
			ChatRoom myChatroom = new ChatRoom(user1,myMessage,objectOutputStream);
			myChatroom.addUser(user2, objectOutputStream);
			myChatroom.addUser(user3, objectOutputStream);
			myChatroom.removeUser(user2);
			assertTrue(myChatroom.getChatUsersSize() == 2);
		}
		catch(IOException e) {
			
		}
		
	}
	
	@Test
	public void testGetActiveUserCount() {
		User user1 = new User();
		Message myMessage = new Message("hello","MyChatRoom");
		ChatRoom myChatroom = new ChatRoom(user1,myMessage,null);
		
		assertTrue(myChatroom.getActiveUserCount() == 1);
	}
	
	@Test
	public void testIncrementActiveUsers() {
		User user1 = new User();
		Message myMessage = new Message("hello","MyChatRoom");
		ChatRoom myChatroom = new ChatRoom(user1,myMessage,null);
		
		myChatroom.incrementActiveUsers();
		
		assertTrue(myChatroom.getActiveUserCount() == 2);
	}

	
	@Test
	public void testDecrementActiveUsers() {
		User user1 = new User();
		Message myMessage = new Message("hello","MyChatRoom");
		ChatRoom myChatroom = new ChatRoom(user1,myMessage,null);
		
		myChatroom.incrementActiveUsers();
		myChatroom.incrementActiveUsers();
		myChatroom.incrementActiveUsers();
		
		myChatroom.decrementActiveUsers();
		
		assertTrue(myChatroom.getActiveUserCount() == 3);
	}
}
