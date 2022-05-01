import java.net.*;
import java.util.Scanner;
import java.io.*;

	class Client {
		private Socket socket;
		private OutputStream outputStream;
		private InputStream inputStream;
		private ObjectOutputStream objectOutputStream;
		private ObjectInputStream objectInputStream;
		

		private boolean authenticated;
		private String currentRoom;
		
		public Client()
		{	
			  try {
				  	socket = new Socket("localhost", 1234);
					// Used to send Messages to the server
					outputStream = socket.getOutputStream();
					objectOutputStream = new ObjectOutputStream(outputStream);
		
		            // This is how you would get a Message back during the authentication process
		            inputStream = socket.getInputStream();
					objectInputStream = new ObjectInputStream(inputStream);
					
					authenticated = false;
					currentRoom = null;
			  }
			  catch(Exception e)
			  {
				  e.printStackTrace();
			  }
		  
		}
		public Socket getSocket() {
			return socket;
		}

		public ObjectInputStream getObjectInputStream() {
			return objectInputStream;
		}

		
		public void login(String username, String password)
		{
			String login;
			
			login = username + "/" + password;
			
			try {
				Message loginMessage = new Message("LOGIN", login);
				objectOutputStream.writeObject(loginMessage);
				
				loginMessage = (Message) objectInputStream.readObject();
				System.out.println(loginMessage.getStatus());
				System.out.println(loginMessage.getText());
				if (loginMessage.getStatus().equals("VERIFIED")) {		
					authenticated = true;
				}
				else {
					System.out.println("NOT VERIFIED");
				}
	
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public Message createChatroom(String chatName)
		{
			Message chatMessage = new Message("CREATECHATROOM", chatName);
			
			chatMessage = sendMessage(chatMessage);
			
			return chatMessage;
			
		}
		
		public Message joinChatroom(String chatName)
		{
			Message chatMessage = new Message("JOINCHATROOM", chatName);
			
			chatMessage = sendMessage(chatMessage);
			
			return chatMessage;
			
		}
		
		public void leaveChatroom(String chatName)
		{
			Message chatMessage = new Message("LEAVECHATROOM", chatName);
			
			try {
				objectOutputStream.writeObject(chatMessage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			
		}
		
		public Message changePass(String password)
		{
			
			Message passMessage = new Message("CHANGEPASSWORD", password);
			
			passMessage = sendMessage(passMessage);
			
			return passMessage;
			
		}
		
		public Message displayUsers()
		{
			Message displayMessage = new Message();
			displayMessage.setType("DISPLAYUSERS");
			
			displayMessage = sendMessage(displayMessage);
			
			return displayMessage;
		}
		
		
		
		public void setChatLock()
		{
			
			Message lockMessage = new Message();
			lockMessage.setType("LOCKCHAT");
		
			try {
				objectOutputStream.writeObject(lockMessage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void setChatUnlock()
		{
			Message lockMessage = new Message();
			lockMessage.setType("UNLOCKCHAT");
			
			try {
				objectOutputStream.writeObject(lockMessage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public Message displayChatRooms()
		{
			Message chatMessage = new Message();
			chatMessage.setType("DISPLAYCHATROOMS");
			
			chatMessage = sendMessage(chatMessage);
		
			return chatMessage;
		}
		
		public void deliverMessage(String input)
		{
			Message userMessage = new Message();
			userMessage.setType("CHATROOM");
			userMessage.setText(input);
			try {
				objectOutputStream.writeObject(userMessage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		}
		
		public Message createUser(String login)
		{
			
			Message userMessage = new Message("CREATEUSER", login);
			
			userMessage = sendMessage(userMessage);
			
			return userMessage;
			
		}
		
		public Message createSuper(String login)
		{
			
			Message superMessage = new Message("CREATESUPERVISOR", login);
			
			superMessage = sendMessage(superMessage);
			
			return superMessage;
			
		}
		
		public Message deleteUser(String username)
		{
			Message deleteMessage = new Message("DELETEUSER", username);
		
			deleteMessage = sendMessage(deleteMessage);
			
			return deleteMessage;
		}
		
		public Message getChatLog()
		{
			Message logMessage = new Message();
			logMessage.setType("RETRIEVELOGS");
			
			logMessage = sendMessage(logMessage);
			
			return logMessage;
		}
		
		public void reloadChat()
		{
			Message chatMessage = new Message();
			chatMessage.setType("CHATLOG");
			try {
				objectOutputStream.writeObject(chatMessage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			
		}
		
		public void displayChatUsers()
		{
			Message chatMessage = new Message();
			chatMessage.setType("CHATUSERS");
			try {
				objectOutputStream.writeObject(chatMessage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void logOut()
		{
			
			Message logMessage = new Message();
			logMessage.setType("LOGOUT");
			
			try {
				objectOutputStream.writeObject(logMessage);
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
		
		private Message sendMessage(Message message)
		{
			try {
				System.out.println(message.getType());
				objectOutputStream.writeObject(message);
				message = (Message) objectInputStream.readObject();
			
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return message;
		}
		
		public boolean getAuthenticated()
		{
			return authenticated;
		}
		
		public void setCurrRoom(String room)
		{
			this.currentRoom = room;
		}
		
		public String getCurrRoom()
		{
			return this.currentRoom;
		}
		
}
