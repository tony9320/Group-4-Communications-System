import static org.junit.jupiter.api.Assertions.*;

import java.io.*;


import org.junit.Test;


//NOTE: Make sure to run Server.java in parallel to this test!
//Also restart the Server after each test!
public class ClientTester {
	
	private Client client;

	public ClientTester()
	{
		
		client = new Client();
	}
	
	@Test
	public void testGetSocket()
	{
		assertTrue(client.getSocket().getPort() == 1234);
	}
	
	
	@Test
	public void testLogin()
	{
		client.login("Matty", "password");
		assertTrue(client.getAuthenticated());
	}
	
	@Test
	public void testCreateChatroom()
	{
		client.login("Matty", "password");
		Message message = client.createChatroom("New Room");
		assertTrue(message.getStatus().equals("VERIFIED"));
	}
	
	@Test
	public void testJoinChatroom()
	{
		client.login("Matty", "password");
		client.createChatroom("Test Room");
		client.leaveChatroom("Test Room");
		Message message = client.joinChatroom("Test Room");
		assertTrue(message.getStatus().equals("VERIFIED"));
	}
	
	@Test
	public void testLeaveChatroom()
	{
		client.login("Matty", "password");
		client.createChatroom("Test Room");
		client.leaveChatroom("Test Room");
		ObjectInputStream objectInputStream = client.getObjectInputStream();
		Message message;
		try {
			client.setChatLock();
			message = (Message) objectInputStream.readObject();
			assertTrue(message.getStatus().equals("VERIFIED"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	public void testChangePassPart1()
	{
		client.login("Matty", "password");
		client.changePass("test");
		
	}
	@Test
	public void testChangePassPart2()
	{
		client.login("Matty", "test");
		assertTrue(client.getAuthenticated());
		client.changePass("password");
		
	}
	
	
	@Test
	public void testDisplayUsers()
	{
		client.login("Matty", "password");
		Message message = client.displayUsers();
		assertTrue(message.getStatus().equals("VERIFIED"));
	}
	
	@Test
	public void testSetChatLock()
	{
		client.login("Matty", "password");
		client.createChatroom("New Room 1");
		ObjectInputStream objectInputStream = client.getObjectInputStream();
		Message message;
		try {
			client.setChatLock();
			message = (Message) objectInputStream.readObject();
			assertTrue(message.getStatus().equals("VERIFIED"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testSetChatUnlock()
	{
		client.login("Matty", "password");
		client.createChatroom("New Room 2");
		ObjectInputStream objectInputStream = client.getObjectInputStream();
		Message message;
		try {
			client.setChatLock();
			message = (Message) objectInputStream.readObject();
			client.setChatUnlock();
			message = (Message) objectInputStream.readObject();
			assertTrue(message.getStatus().equals("VERIFIED"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	@Test
	public void testDisplayChatRooms()
	{
		client.login("Matty", "password");
		Message message = client.displayChatRooms();
		assertTrue(message.getStatus().equals("VERIFIED"));
	}
	
	@Test
	public void testDeliverMessage()
	{
		client.login("Matty", "password");
		client.createChatroom("New Room 4");
		ObjectInputStream objectInputStream = client.getObjectInputStream();
		Message message;
		try {
			client.deliverMessage("Hi");
			message = (Message) objectInputStream.readObject();
			assertTrue(message.getText().equals("Matty: Hi\n(Sent)\n"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCreateUserPart1()
	{
		client.login("Matty", "password");
		client.createUser("User/Pass");
	}
	
	public void testCreateUserPart2()
	{
		client.login("User", "Pass");
		assertTrue(client.getAuthenticated());
	}
	
	@Test
	public void testCreateSuperPart1()
	{
		client.login("Matty", "password");
		client.createSuper("Super/Pass");
	}
	
	@Test
	public void testCreateSuperPart2()
	{
		client.login("Super", "Pass");
		assertTrue(client.getAuthenticated());
	}
	
	@Test
	public void testDeleteUserPart1()
	{
		client.login("Matty", "password");
		client.deleteUser("User");
		client.deleteUser("Super");
	}
	
	public void testDeleteUserPart2()
	{
		client.login("Super", "Pass");
		assertTrue(!client.getAuthenticated());
		client.login("User", "Pass");
		assertTrue(!client.getAuthenticated());
	}
	
	
	
	@Test
	public void testDisplayChatUsers()
	{
		client.login("Matty", "password");
		client.createChatroom("New Room 5");
		ObjectInputStream objectInputStream = client.getObjectInputStream();
		Message message;
		try {
			client.displayChatUsers();
			message = (Message) objectInputStream.readObject();
			assertTrue(message.getText().equals("Current Users:\nMatty\n"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testLogOut()
	{
		client.login("Matty", "password");
		client.logOut();
		assertTrue(client.getSocket().isClosed());
	}
	
	@Test
	public void testGetAuthenticated()
	{
		client.login("Matty", "wrongPassword");
		assertFalse(client.getAuthenticated());
	}
	
	@Test
	public void testSetCurrRoom()
	{
		client.setCurrRoom("My Room");
		assertTrue(client.getCurrRoom().equals("My Room"));
	}
	
	@Test
	public void testGetCurrRoom()
	{
		assertNull(client.getCurrRoom());
	}
	

	
}
