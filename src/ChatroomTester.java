import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ChatroomTester {

	@Test
	public void testAddUser() {
		User user1 = new User();
		User user2 = new User();
		Message myMessage = new Message();
		ChatRoom myChatroom = new ChatRoom(user1,myMessage,null);
		myChatroom.addUser(user2, null);
		assertTrue(myChatroom.getChatUsersSize() == 2);
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
		
		myChatroom.setChatLock(user1);
		assertTrue(myChatroom.isLocked() == true);
	}
	
	@Test
	public void testSetChatUnlock() {
		User user1 = new User();
		Message myMessage = new Message("hello","MyChatRoom");
		ChatRoom myChatroom = new ChatRoom(user1,myMessage,null);
		
		myChatroom.setChatLock(user1);
		myChatroom.setChatUnlock(user1);
		assertTrue(myChatroom.isLocked() == false);
	}
	
	@Test
	public void testRemoveUser() {
		User user1 = new User();
		User user2 = new User();
		User user3 = new User();
		Message myMessage = new Message("hello","MyChatRoom");
		ChatRoom myChatroom = new ChatRoom(user1,myMessage,null);
		myChatroom.addUser(user2, null);
		myChatroom.addUser(user3, null);
		myChatroom.removeUser(user2);
		
		assertTrue(myChatroom.getChatUsersSize() == 2);
		
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
