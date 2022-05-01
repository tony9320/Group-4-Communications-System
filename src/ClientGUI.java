
import java.io.ObjectInputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;




public class ClientGUI extends JFrame {


	private String currentUser;
	private HomePanel homePanel;
	private Client client;
	private boolean supervisorStatus;
	// Constructor.
	public ClientGUI(Client myclient) {

		client = myclient;
		
		displayLogin("LOGIN");
		
		
	}

	public boolean getSupervisorStatus()
	{
		return supervisorStatus;
	}
	
	public void setSupervisorStatus(boolean status)
	{
		supervisorStatus = status;
	}
	public void displayLogin(String type)
	{
		// remove old items
		getContentPane().removeAll();
		
		HomePanel homePanel = new HomePanel(this);
		homePanel = homePanel.constructLoginWindow(type);
		getContentPane().add(homePanel);
		setTitle(type);
		// refresh frame so that the new changes take effect.
		revalidate();
	}

	
	//login function for authenticating user
	protected void login(String username, String password) {
		
		
		client.login(username, password);
		
		if (client.getAuthenticated()) {
			currentUser = username;
			if(!checkSupervisor())
			{
				setSupervisorStatus(false);
				displayHomeScreen("Login success!");
			}
			else
			{
				setSupervisorStatus(true);
				displaySuperHomeScreen("Super success!");
			}
		} else {
			// Login failed
			JOptionPane.showMessageDialog(this, "Login failed.");
		}
		
		
	}
	
	//Sends a RETRIEVEMESSAGE to determine if the user is a supervisor or not
	private boolean checkSupervisor()
	{
		Message replyMessage = client.getChatLog();
		if(replyMessage.getStatus().equals("VERIFIED"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//Creates and displays the main menu
	public void displayHomeScreen(String displayString) {
		// remove old items
		getContentPane().removeAll();

		// add home panel
		homePanel = new HomePanel(this);
		homePanel = homePanel.constructMainMenu();
		getContentPane().add(homePanel);

		setTitle("Chat Room: " + currentUser);
		// refresh frame so that the new changes take effect.
		revalidate();

		// set display text
		homePanel.display(displayString);
		homePanel.displayLine();

	}
	
	//Creates and displays the menu for supervisors
	public void displaySuperHomeScreen(String displayString)
	{
		// remove old items
		getContentPane().removeAll();

		// add home panel
		homePanel = new HomePanel(this);
		homePanel = homePanel.constructSuperWindow();
		getContentPane().add(homePanel);

		setTitle("Chat Room: " + currentUser);
		// refresh frame so that the new changes take effect.
		revalidate();

		// set display text
		homePanel.display(displayString);
		homePanel.displayLine();

		
	}
	
	public static void main(String[] args) {
		
		Client client = new Client();
		ClientGUI gui = new ClientGUI(client);
		
		//GUI settings
		gui.setSize(800, 500); // setting x and y dim of frame
		gui.setTitle("Chat Room - Login");
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit button to close app
		gui.setVisible(true);
	}

	//Functionality for Create Chat Room button in main menu
	public boolean createChatRoom(String roomName) 
	{
		if(roomName == null)
		{
			return false;
		}
		
		Message replyMessage = client.createChatroom(roomName);
		
		//Created chat room successfully
		if ("VERIFIED".equals(replyMessage.getStatus())) {
			client.setCurrRoom(roomName);
			return true;

		} else {	//Chat room already created
			homePanel.display("FAILED to Create Chat Room: " + roomName);
			homePanel.display("Reason: " + replyMessage.getText());
			homePanel.displayLine();
			return false;

		}
	

	}

	//Functionality for Join Chat Room button in main menu
	public Message joinChatRoom(String roomName) {
		
		
		
		Message replyMessage = client.joinChatroom(roomName);

		return replyMessage;

	}
	
	
	//Functionality for Lock button on chat window
	public void lockChatRoom()
	{
		client.setChatLock();

	}
	
	//Functionality for Unlock button on chat window
	public void unlockChatRoom()
	{
		
		client.setChatUnlock();

	}
	
	//Functionality for Change Password button in main menu
	public void changePassword(String password)
	{
		
		Message replyMessage = client.changePass(password);
		
		if ("VERIFIED".equals(replyMessage.getStatus())) {
			homePanel.display("Password changed");
			homePanel.displayLine();
		} else {
			homePanel.display("Unable to change password");
			homePanel.displayLine();

		}

	}
	
	//Functionality for Display Chat Room button in main menu
	public void displayChatRooms()
	{
		
		Message replyMessage = client.displayChatRooms();
		
		//Continue if verified and the RoomList is not empty 
		if("VERIFIED".equals(replyMessage.getStatus()) && replyMessage.getRoomList() != null)
		{	
			//Loop through the String array in replyMessage
			for(String s: replyMessage.getRoomList())
			{
				homePanel.display(s + "\n");
			}
			
			homePanel.displayLine();
		}else
		{
			homePanel.display("There are no Chat Rooms to show");
			homePanel.displayLine();
		}
				
	}
	
	//Functionality for Display Users button in main menu
	public void displayUsers()
	{
		Message replyMessage = client.displayUsers();
		System.out.println(replyMessage.getType());
		//Continue if verified and the RoomList is not empty 
		if("VERIFIED".equals(replyMessage.getStatus()) && replyMessage.getRoomList() != null)
		{	
			//Loop through the String array in replyMessage
			for(String s: replyMessage.getRoomList())
			{
				homePanel.display(s + "\n");
			}
			
			homePanel.displayLine();
		}else
		{
			homePanel.display("There are no users to show");
			homePanel.displayLine();
		}

	}

	public void createUser(String username, String password)
	{
		System.out.println(username + "/" + password);
		Message replyMessage = client.createUser(username + "/" + password);
		
		if(replyMessage.getStatus().equals("VERIFIED"))
		{
			displaySuperHomeScreen("User created!");
		}
		else
		{
			JOptionPane.showMessageDialog(this, replyMessage.getText());
		}
	}
	
	public void createSuper(String username, String password)
	{
		
		Message replyMessage = client.createSuper(username + "/" + password);
		
		if(replyMessage.getStatus().equals("VERIFIED"))
		{
			displaySuperHomeScreen("Supervisor created!");
		}
		else
		{
			JOptionPane.showMessageDialog(this, replyMessage.getText());
		}
	}
	
	public void deleteUser(String username)
	{
		Message replyMessage = client.deleteUser(username);
		if(replyMessage.getStatus().equals("VERIFIED"))
		{
			homePanel.display(replyMessage.getText());
		}
		else
		{
			homePanel.display("Unable to delete user.\nReason" + replyMessage.getText());
		}
		
	}
	
	public void getLogs()
	{
		Message replyMessage = client.getChatLog();
		if(replyMessage.getStatus().equals("VERIFIED"))
		{
			homePanel.display(replyMessage.getText());
		}
		else
		{
			homePanel.display("Unable to retrieve logs.\nReason" + replyMessage.getText());
		}
	}
	
	//Function that creates and displays all available chat rooms in a new window
	public void displayRooms()
	{
		Message replyMessage = client.displayChatRooms();
		// remove old items
		getContentPane().removeAll();
		
		//create a new HomePanel designed for displaying chat rooms
		homePanel = new HomePanel(this);
		homePanel = homePanel.constructRoomWindow(replyMessage);
		
		getContentPane().add(homePanel);
		setTitle("Chat Room: " + currentUser);
		// refresh frame so that the new changes take effect.
		revalidate();

	}
	
	//Function that creates and displays the chat window after selecting a room
	public void displayChat(String s, boolean firstTime)
	{
		
		// remove old items
		getContentPane().removeAll();
		
		//Create new HomePanel designed for displaying the chat
		homePanel = new HomePanel(this);
		homePanel = homePanel.constructChatWindow(s);
		
		getContentPane().add(homePanel);
		setTitle("Chat Room: " + currentUser);
		// refresh frame so that the new changes take effect.
		revalidate();

		// set display text
		homePanel.display(s);
		homePanel.displayLine();
		
		//Start a new thread for listening
		ReceiveMessages inputThread = new ReceiveMessages(client.getSocket(), client.getObjectInputStream(), homePanel, this);
		new Thread(inputThread).start();
		if(!firstTime)
		{
			reloadChat();
		}
	}
	
	//Functionality for the Send button in the chat window
	public void sendMessage(String s)
	{
		client.deliverMessage(s);
	}
	
	//Functionality for Exit button on chat window
	public void leaveChatRoom(String roomName)
	{
		if(roomName == null)
		{
			return;
		}
		
		client.leaveChatroom(roomName);

	}
	
	public void reloadChat()
	{
		client.reloadChat();
	}
	
	public void logOut()
	{
		client.logOut();
	}
	
	public String getCurrentUser()
	{
		return currentUser;
	}
	
	public void displayChatUsers()
	{
		client.displayChatUsers();
	}
	
	//Handles chat threads
	private static class ReceiveMessages implements Runnable {
		private final Socket clientSocket;
        private ObjectInputStream objectInStream;
		private HomePanel homePanel;
		private ClientGUI tgui;
		
		public ReceiveMessages(Socket socket, ObjectInputStream objectInputStream, HomePanel homeP, ClientGUI tClient) {
			this.clientSocket = socket;
            this.objectInStream = objectInputStream;
            this.homePanel = homeP;
            this.tgui = tClient;
			System.out.println("Creating the receive thread...");
			
		} 

		public void run() {
		try {
			System.out.println("TRYING");
			Message replyMessage;
			
			while (true) {
				
				replyMessage = (Message) objectInStream.readObject();
				
				if(replyMessage.getType() == null)
				{
					continue;
				}
				else if(replyMessage.getType().equals("CHATROOM"))
				{
					String displayText[] = replyMessage.getText().split("\n");
					if(!tgui.getCurrentUser().equals(replyMessage.getSender()))
					{
						homePanel.display(displayText[0]);
					}
					else
					{
						homePanel.display(replyMessage.getText());
					}
				}
				else if(replyMessage.getType().equals("LEAVECHATROOM"))
				{	//If the user clicks the Exit button, end the thread
					
					System.out.println("Ending thread");
					break;
				} 
				else if(replyMessage.getType().equals("LOCKCHAT") || replyMessage.getType().equals("UNLOCKCHAT"))//Handle lock/unlock messages
				{
					
					homePanel.display(replyMessage.getText());
					homePanel.displayLine();
				
				}
				else if(replyMessage.getType().equals("HISTORY"))
				{
					
					homePanel.display(parseHistory(replyMessage.getText(), tgui.getCurrentUser()));
					homePanel.displayLine();
				}
				else if(replyMessage.getType().equals("JOINED"))
				{
					homePanel.display(replyMessage.getText());
					homePanel.displayLine();
				}
				else if (replyMessage.getType().equals("CHATUSERS"))
				{
					homePanel.display(replyMessage.getText());
					homePanel.displayLine();
				}
				else if(replyMessage.getType().equals("LEFT"))
				{
					homePanel.display(replyMessage.getText());
					homePanel.displayLine();
				}
				
			}

			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	private String parseHistory(String text, String user)
	{
		
		String sArr[] = text.split("\n([A-z]*)\n");
		String returnString = "";
		
		for(String s : sArr)
		{
			
			String tempArr[] = s.split(":");
			if(tempArr[0].equals(user))
			{
				returnString += s;
				returnString += "\n";
			}
			else
			{
				returnString += s.split("\n")[0];
				returnString += "\n";
			}
			
			
		}
		
		return returnString;
	}
		
}
	
	
}
