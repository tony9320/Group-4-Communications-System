import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SupervisorTester {
	
	@Test
	public void testIsSupervisor() {
		User myUser = new Supervisor("John","admin");
		assertTrue(myUser instanceof Supervisor);
	}

	// All tests below are repeats of Anthony's User class tester... I take no credit for the below tests.
	@Test
	public void testConstructor() {
		Supervisor myUser = new Supervisor("John","admin");
		
		assertTrue(myUser.getName().equals("John") &&
				myUser.getPassword().equals("admin"));
	}
	
	@Test
	public void testChangePassword() {
		Supervisor myUser = new Supervisor("John","admin");
		String newPassword = "apple";
		
		myUser.changePassword(newPassword);
		
		assertTrue(myUser.getPassword().equals(newPassword));
	}
	
	@Test 
	public void testSetName() {
		Supervisor myUser = new Supervisor("John","admin");
		String newName = "Bob";
		
		myUser.setName(newName);
		
		assertTrue(myUser.getName().equals(newName));
	}
	
	
	@Test
	public void testSetActiveChatRoom() {
		Supervisor myUser = new Supervisor("John","admin");
		String chatRoom = "CHATROOM12";
		
		myUser.setActiveChatRoom(chatRoom);
		
		assertTrue(myUser.getActiveChatRoom().equals(chatRoom));
	}
	
	@Test
	public void testGetName() {
		Supervisor myUser = new Supervisor("John","admin");
		assertTrue(myUser.getName().equals("John"));
	}
	
	@Test
	public void testGetPassword() {
		Supervisor myUser = new Supervisor("John","admin");
		assertTrue(myUser.getPassword().equals("admin"));
	}

}
