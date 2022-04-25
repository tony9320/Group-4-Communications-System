import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

class Server {
	public static void main(String[] args) {
		ServerSocket server = null;
		try {
			// Load User file into ArrayList, save new Users to it ---> update the file
			server = new ServerSocket(1234);
			server.setReuseAddress(true);
	
			// Looping to accept an indefinite number of client connections
			while (true) {
				// Accept client connection
				Socket client = server.accept(); 
				System.out.println("IN SERVER: New client connected " + client.getInetAddress().getHostAddress());
	
				// Creating a new thread
				ClientHandler clientSock = new ClientHandler(client);
				
				// This thread will handle the client separately
				Thread newThread = new Thread(clientSock);
				newThread.start();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (server != null) {
				try {
					server.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static class ClientHandler implements Runnable {
		private final Socket clientSocket;
		private User localUser;
		private boolean authenticated = false;
		private File chatHistory = new File("logs");
		
		private static ArrayList<User> allUsers = new ArrayList<User>();
	        private static ArrayList<ChatRoom> allChatRooms = new ArrayList<ChatRoom>();
		private static HashMap<String, ObjectOutputStream> outputStreams = new HashMap<String, ObjectOutputStream>();
		private static HashMap<String, Socket> userSockets = new HashMap<String, Socket>();

		private static int userNum = 0;
		
		// Constructor
		public ClientHandler(Socket socket) {
			this.clientSocket = socket;
			System.out.println("New thread created");
			System.out.println("Number of connect clients: " + (userNum + 1));;

			if (userNum == 0) {
				loadUsers(allUsers);
			}

			userNum++;			
		}
	
		public void run() {
			try {
			    // Allows us to receive Messages from the client
				InputStream inputStream = clientSocket.getInputStream();
				ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
				
				
				// Allows us to send Messages back to the client
				OutputStream outputStream = clientSocket.getOutputStream();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

				Message loginMessage;
				loginMessage = (Message) objectInputStream.readObject();
				System.out.println(loginMessage.getText());


				String[] values = loginMessage.getText().split("/");
				System.out.println(values[0] + " " + values[1]);

				// Attempting to authenticate based on the first login attempt
				for (int i = 0; i < allUsers.size(); i++) {
					System.out.println(allUsers.size());
					if (allUsers.get(i).getName().equals(values[0])) {
						if (allUsers.get(i).getPassword().equals(values[1])) {
							localUser = allUsers.get(i);
							localUser.setObjectOutputStream(objectOutputStream);
							outputStreams.replace(localUser.getName(), objectOutputStream);
							System.out.println("User verified");
							authenticated = true;

							Message verifiedMessage = new Message();
							verifiedMessage.setStatus("VERIFIED");
							verifiedMessage.setText("YOU ARE VERIFIED");
							objectOutputStream.writeObject(verifiedMessage);
							break;
						}
						else {
							Message failureMessage = new Message();
							failureMessage.setStatus("FAILED");
							failureMessage.setText("WRONG PASSWORD");
							objectOutputStream.writeObject(failureMessage);
							System.out.println("WRONG PASSWORD :" + values[1]);
						}
					}
				}

				if (!authenticated) {
					Message failureMessage = new Message();
					failureMessage.setStatus("FAILED");
					failureMessage.setText("USERNAME NOT FOUND");
					objectOutputStream.writeObject(failureMessage);
				}
				
				// **** USER AUTHENTICATION, CREATE THE USER OBJECT AND SET ITS ATTRIBUTES ****
				while (!authenticated) {
					System.out.println("First auth failed");
					loginMessage = (Message) objectInputStream.readObject();
					values = loginMessage.getText().split("/");
					System.out.println(values[0] + " " + values[1]);

					for (int i = 0; i < allUsers.size(); i++) {
						System.out.println(allUsers.size());
						if (allUsers.get(i).getName().equals(values[0])) {
							if (allUsers.get(i).getPassword().equals(values[1])) {
								localUser = allUsers.get(i);
								outputStreams.replace(localUser.getName(), objectOutputStream);
								localUser.setObjectOutputStream(objectOutputStream);
								System.out.println("User verified");
								authenticated = true;
	
								Message verifiedMessage = new Message();
								verifiedMessage.setStatus("VERIFIED");
								verifiedMessage.setText("YOU ARE VERIFIED");
								objectOutputStream.writeObject(verifiedMessage);
								break;
							}
							else {
								Message verifiedMessage = new Message();
								verifiedMessage.setStatus("FAILED");
								verifiedMessage.setText("WRONG PASSWORD");
								objectOutputStream.writeObject(verifiedMessage);
								System.out.println("WRONG PASSWORD :" + values[1]);
							}
						}
					}

					if (!authenticated) {
						Message failureMessage = new Message();
						failureMessage.setStatus("FAILED");
						failureMessage.setText("USERNAME NOT FOUND");
						objectOutputStream.writeObject(failureMessage);
					}
			    }
				

				// Reading the message from the client thread
				try {
					userSockets.put(localUser.getName(), clientSocket);
                    			Message messageFromClient;
					// This is where the Server takes Messages from the Clients and decides what to do based on the Message's type
					while (true) {
						messageFromClient = (Message) objectInputStream.readObject();
						messageFromClient.setSender(localUser.getName());
						
						switch (messageFromClient.getType()) {
							case "CHATROOM": {
								boolean failed = false;

								if (localUser.getActiveChatRoom() == null) {
									messageFromClient.setStatus("FAILED");
									messageFromClient.setText("You are not active in a chat room!");
									objectOutputStream.writeObject(messageFromClient);
									break;
								}

								for (int i = 0; i < allChatRooms.size(); i++) {
									if (allChatRooms.get(i).getRoomName().equals(localUser.getActiveChatRoom())) {
										messageFromClient.setText(localUser.getName() + ": " + messageFromClient.getText());
										allChatRooms.get(i).sendMessage(messageFromClient);
										logMessage(messageFromClient, localUser.getActiveChatRoom());
										Message sendReceipt = new Message("VERIFIED");
										objectOutputStream.writeObject(sendReceipt);
										break;
									}
									else if ((i == allChatRooms.size() - 1)) {
										failed = true;
									}
								}

								if (failed) {
									Message sendReceipt = new Message("FAILED");
									sendReceipt.setText("Chat Room not found.");
									objectOutputStream.writeObject(sendReceipt);
								}						
								
								System.out.println("TYPE: CHATROOM");
								break;
							}
												
							case "DISPLAYCHATROOMS": {
								String[] roomNames = new String[allChatRooms.size()];
								for (int i = 0; i < allChatRooms.size(); i++) {
									roomNames[i] = allChatRooms.get(i).getRoomName();
								}
								messageFromClient.setRoomList(roomNames);
								messageFromClient.setStatus("VERIFIED");
								objectOutputStream.writeObject(messageFromClient);
								
								
								System.out.println("TYPE: DISPLAYCHATROOM");
								break;
							}

							case "JOINCHATROOM": {
								boolean failed = true;
								messageFromClient.setText(messageFromClient.getText().toUpperCase());
								for (int i = 0; i < allChatRooms.size(); i++) {
									if (allChatRooms.get(i).getRoomName().equals(messageFromClient.getText())) {
										if (allChatRooms.get(i).isLocked()) {
											break;
										}
										else {
											allChatRooms.get(i).addUser(localUser, objectOutputStream);
											localUser.setActiveChatRoom(messageFromClient.getText());
											messageFromClient.setStatus("VERIFIED");
											localUser.setActiveChatRoom(messageFromClient.getText());
											objectOutputStream.writeObject(messageFromClient);
											failed = false;
											break;
										}
									}
								}
								
								if (failed) {
									messageFromClient.setStatus("FAILED");
									messageFromClient.setText("That chat is locked!");
									objectOutputStream.writeObject(messageFromClient);
								}
								
								System.out.println("TYPE: JOINCHATROOM");
								break;
							}

							case "CREATECHATROOM": { 
								messageFromClient.setText(messageFromClient.getText().toUpperCase());
								boolean failed = false;

								for (int i = 0; i < allChatRooms.size(); i++) {
									if (allChatRooms.get(i).getRoomName().equals(messageFromClient.getText())) {
										messageFromClient.setStatus("FAILED"); 
										messageFromClient.setText("This room already exists!");
										objectOutputStream.writeObject(messageFromClient);   
										failed = true;
										break;               
									}
								}

								if (!failed) {
									localUser.setActiveChatRoom(messageFromClient.getText());
									allChatRooms.add(new ChatRoom(localUser, messageFromClient, objectOutputStream));
									Message sendReceipt = new Message("VERIFIED");
									objectOutputStream.writeObject(sendReceipt);
								}				
							
							
								System.out.println("TYPE: CREATECHATROOM");
								break;
							}

							case "CHANGEPASSWORD": { 
								localUser.changePassword(messageFromClient.getText());
								messageFromClient.setStatus("VERIFIED");
								objectOutputStream.writeObject(messageFromClient);
								updateUserFile();

								System.out.println("TYPE: CHANGEPASSWORD");
								break;
							}

							case "CREATEUSER": {
								boolean failed = false;
								User newUser;

								if (!(localUser instanceof Supervisor)) {
									messageFromClient.setStatus("FAILED");
									messageFromClient.setText("You are not a Supervisor.");
									objectOutputStream.writeObject(messageFromClient);
									break;
								}

								String[] userPass = messageFromClient.getText().split("/");
								
								for (int i = 0; i < allUsers.size(); i++) {
									if (allUsers.get(i).getName().equals(userPass[0])) {
										messageFromClient.setStatus("FAILED");
										messageFromClient.setText("This User already exists.");
										objectOutputStream.writeObject(messageFromClient);
										failed = true;
										break;
									}
								}
								
								// The User does not already exist and can be added
								if (!failed) {
									newUser = new User(userPass[0], userPass[1]);
									allUsers.add(newUser); 
									messageFromClient.setStatus("VERIFIED");
									System.out.println("New user created");
									objectOutputStream.writeObject(messageFromClient);
									saveUser(newUser);
								}
							
								System.out.println("TYPE: CREATEUSER");
								break;
							}

							case "CREATESUPERVISOR": { 
								boolean failed = false;
								Supervisor newSupervisor = null;

								if (!(localUser instanceof Supervisor)) {
									messageFromClient.setStatus("FAILED");
									messageFromClient.setText("You are not a Supervisor.");
									objectOutputStream.writeObject(messageFromClient);
									return;
								}
								
								String[] userPass = messageFromClient.getText().split("/");

								for (int i = 0; i < allUsers.size(); i++) {
									if (allUsers.get(i).getName().equals(userPass[0])) {
										messageFromClient.setStatus("FAILED");
										messageFromClient.setText("This User already exists.");
										objectOutputStream.writeObject(messageFromClient);
										failed = true;	
										return;
									}
								}
			
								if (!failed) {
									// The User does not already exist and can be added
									newSupervisor = new Supervisor(userPass[0], userPass[1]);
									allUsers.add(newSupervisor); // They do not yet have an output stream or active chat room
									messageFromClient.setStatus("VERIFIED");
									saveUser(newSupervisor);
									objectOutputStream.writeObject(messageFromClient);
								}
			
		
			
								System.out.println("TYPE: CREATESUPERVISOR");
								break;
							}
								
							case "DISPLAYUSERS": { 
								String listOfUsers = "";
								for (int i = 0; i < allUsers.size(); i++) {
									listOfUsers += allUsers.get(i).getName() + "\n";
								}
								
								messageFromClient.setText(listOfUsers);
								objectOutputStream.writeObject(messageFromClient);

								System.out.println("TYPE: DISPLAYUSERS");
								break;
							}

							case "LEAVECHATROOM": { 
								for (int i = 0; i < allChatRooms.size(); i++) {
									if (allChatRooms.get(i).getRoomName().equals(localUser.getActiveChatRoom())) {
											localUser.setActiveChatRoom(null);
									}
								}
								
								System.out.println("TYPE: LEAVECHATROOM");						
								break;
							}

							case "LOCKCHAT": {
								for (int i = 0; i < allChatRooms.size(); i++) {
									if (allChatRooms.get(i).getRoomName().equals(localUser.getActiveChatRoom())) {
										allChatRooms.get(i).setChatLock(localUser);  
										Message returnMessage = new Message("VERIFIED");
										objectOutputStream.writeObject(returnMessage);
										break;
									}
								}
								System.out.println("TYPE: LOCKCHAT");
								break;
							}

							case "UNLOCKCHAT": {  
								for (int i = 0; i < allChatRooms.size(); i++) {
									if (allChatRooms.get(i).getRoomName().equals(localUser.getActiveChatRoom())) {
										allChatRooms.get(i).setChatUnlock(localUser);  
										Message returnMessage = new Message("VERIFIED");
										objectOutputStream.writeObject(returnMessage);

										break;
									}
								}

								System.out.println("TYPE: UNLOCKCHAT");
								break;
							}

							case "RETRIEVELOGS ": {  
								if (localUser instanceof Supervisor) {
									messageFromClient.setStatus("VERIFIED");
									objectOutputStream.writeObject(messageFromClient);
									retrieveLogs(objectOutputStream);
								}
								else {
									messageFromClient.setStatus("FAILED");
									messageFromClient.setText("Only supervisors can retrieve logs!");
									objectOutputStream.writeObject(messageFromClient);
								}

								System.out.println("TYPE: RETRIEVELOGS");
								break;
							}

							case "DELETEUSER": {
								boolean success = false;
								if (!(localUser instanceof Supervisor)) {
									messageFromClient.setStatus("FAILED");
									messageFromClient.setText("You are not a Supervisor.");
									break;
								}
								
								for (int i = 0; i < allUsers.size(); i++) {
									if (allUsers.get(i).getName().equals(messageFromClient.getText())) {
										User toDelete = allUsers.get(i);
										outputStreams.remove(toDelete.getName());
										allUsers.remove(i); 

										for (int j = 0; j < allChatRooms.size(); j++) {
											allChatRooms.get(j).removeUser(toDelete);
										}
										
										if (userSockets.get(toDelete.getName()) != null) {
											userSockets.get(toDelete.getName()).close();
										}
										
										messageFromClient.setStatus("VERIFIED");
										messageFromClient.setText("The User has been deleted.");
										updateUserFile();
										success = true;

										break;
									}
								}

								if (!success) {
									messageFromClient.setStatus("FAILED");
									messageFromClient.setText("User not found");
								}
							
							
								System.out.println("TYPE: DELETEUSER");
								break;
							}

							default: { 
								System.out.println("TYPE: DEFAULT");
								break;
							}
			        
						}
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void saveUser(User newUser) {
			try {
				FileWriter writer = new FileWriter("..\\src\\users.txt", true);
				
				if (newUser instanceof Supervisor) {
					writer.write("\nS/");	
				}
				else {
					writer.write("\nU/");
				}
				writer.write(newUser.getName() + "/");
				writer.write(newUser.getPassword());
				
				writer.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}

			return;
		}

		public void loadUsers(ArrayList<User> allUsers) {
			try {
				BufferedReader reader;
				User newUser = null;

				reader = new BufferedReader(new FileReader("..\\src\\users.txt"));
				String line = reader.readLine();
				
				String[] values = line.split("/");
				if (values[0].equals("S")) {
					newUser = new Supervisor(values[1], values[2]);
					System.out.println(values[1] + values[2]);
				}
				else {
					newUser = new User(values[1], values[2]);
					System.out.println(values[1] + values[2]);
				}
				allUsers.add(newUser);

				line = reader.readLine();
			
				while (line != null) {	
					values = line.split("/");
					if (values[0].equals("S")) {
						newUser = new Supervisor(values[1], values[2]);
						System.out.println(values[1] + values[2]);
					}
					else {
						newUser = new User(values[1], values[2]);
						System.out.println(values[1] + values[2]);
					}
					allUsers.add(newUser);
					outputStreams.put(newUser.getName(), null);
	
					line = reader.readLine();
				}
					reader.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("Returning from loading");
			return;
		}

		public void updateUserFile() {
			try { 
			BufferedWriter writer = new BufferedWriter(new FileWriter("..\\src\\users.txt"));
			for (int i = 0; i < allUsers.size(); i++) {
				if (allUsers.get(i) instanceof Supervisor) {
					writer.write("S/");	
				}
				else {
					writer.write("U/");
				}
				writer.write(allUsers.get(i).getName() + "/");
				writer.write(allUsers.get(i).getPassword());
				if (i != allUsers.size() - 1) writer.write("\n");
			}
			
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

		public void logMessage(Message message, String chatRoom) {
			try { 
				BufferedWriter writer = new BufferedWriter(new FileWriter("logs", true));
				writer.write(message.getTimeStamp() + "\n" + "Room: " + chatRoom + "\n" + message.getText()+"\n" + "\n");
				writer.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void retrieveLogs(ObjectOutputStream objectOutputStream) {
			String currLine;
			String allLogs = "\n";
			Message message = new Message("VERIFIED");
			try { 
				BufferedReader reader = new BufferedReader(new FileReader(chatHistory));
				while ((currLine = reader.readLine()) != null) {
					allLogs += currLine + "\n";
				}
				reader.close();
				message.setText(allLogs);
				objectOutputStream.writeObject(message);
				return;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	}
}

