import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class MessageTester {

	@Test
	public void testDefaultConstructor() {
		Message myMessage = new Message();
		
		assertTrue(myMessage.getType().equals("Undefined"));
	}

	@Test
	public void testSetType() {
		Message myMessage = new Message();
		
		String type = "CHATROOM";
		myMessage.setType(type);
		
		assertTrue(myMessage.getType().equals(type));
	}
	
	@Test
	public void testGetType() {
		Message myMessage = new Message("DEFAULT","This is the content");
		
		assertTrue(myMessage.getType().equals("DEFAULT"));
	}
	
	@Test
	public void testSetStatus() {
		Message myMessage = new Message();
		
		String status = "COMPLETE";
		myMessage.setStatus(status);
		
		assertTrue(myMessage.getStatus().equals(status));
	}
	
	@Test
	public void testGetStatus() {
		Message myMessage = new Message("COMPLETE");
		
		assertTrue(myMessage.getStatus().equals("COMPLETE"));
	}
	
	@Test
	public void testSetText() {
		Message myMessage = new Message("CHATROOM","content");
		
		String text = "updated contents";
		myMessage.setText(text);
		
		assertTrue(myMessage.getText().equals(text));
	}
	
	@Test
	public void testGetText() {
		Message myMessage = new Message("CHATROOM","content");
		
		assertTrue(myMessage.getText().equals("content"));
	}
	
	@Test
	public void testSetSender() {
		Message myMessage = new Message("CHATROOM","content","Bob",null);
		
		String sender = "Jane";
		myMessage.setSender(sender);
		
		assertTrue(myMessage.getSender().equals(sender));
	}
	
	@Test
	public void testGetSender() {
		Message myMessage = new Message("CHATROOM","content","Bob",null);
		
		assertTrue(myMessage.getSender().equals("Bob"));
	}
	
	
	@Test
	public void testSetRoomList() {
		String[] stringArray = new String[] {"a","b","c"};
		Message myMessage = new Message("CHATROOM","content","Bob",null);
		
		myMessage.setRoomList(stringArray);
		
		assertTrue(myMessage.getRoomList().equals(stringArray));
	}
	
	@Test
	public void testGetRoomList() {
		String[] stringArray = new String[] {"a","b","c"};
		Message myMessage = new Message("CHATROOM","content","Bob",stringArray);
		
		assertTrue(myMessage.getRoomList().equals(stringArray));
	}
}
