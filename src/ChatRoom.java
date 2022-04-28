import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


class ChatRoom {
    private String roomName;
    private ArrayList<User> chatUsers;
	private HashMap<String, ObjectOutputStream> outputStreams;
    private boolean chatLocked;
	private int activeUserCount;
    private User host;
    private File chatFile;
    
    public ChatRoom(User user, Message message, ObjectOutputStream outputStream) {	//Type DISPLAYCHATROOM Message
    	this.chatUsers = new ArrayList<User>();		//create the array
    	this.chatUsers.add(user);					//user is added to List of Users
    	this.chatLocked = false;					//chat is created unlocked
    	this.host = user;							//set host
    	this.roomName = message.getText();			//set name of room
    	this.chatFile = new File(roomName);
		this.activeUserCount = 1;


		outputStreams = new HashMap<String, ObjectOutputStream>();
		outputStreams.put(host.getName(), outputStream);

    	try {
			this.chatFile.createNewFile();			//create the file for the chatroom!
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public void addUser(User user, ObjectOutputStream objectOutputStream) {
    	boolean found = false;
    	
    	for(int i = 0; i < this.chatUsers.size(); i++) {			//loop through all users
    		if(!chatUsers.get(i).equals(null) && (this.chatUsers.get(i).equals(user))) { 				//if present in list
    			found = true;										//FOUND!
				outputStreams.replace(user.getName(), objectOutputStream);
    			reloadHistoryForUser(user);
    			break;
    		}//if
    	}//for
    	
    	if(found == false) {												//if not found add to list
    		this.chatUsers.add(user);
			outputStreams.put(user.getName(), objectOutputStream);
			try {
				Message receipt = new Message("JOINCHATROOM", "VERIFIED", "get in there!");
				objectOutputStream.writeObject(receipt);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
    	}//if
    }//addUser()

    public void sendMessage(Message message) {	//message type Chatroom
    	
    	for(int i = 0; i < this.chatUsers.size(); i++) { 
			if (chatUsers.get(i) == null) {
				chatUsers.remove(i);
				continue;
			}										// loop through all users
    		if(chatUsers.get(i).getActiveChatRoom().equals(this.roomName) &&					//user is in current chatroom
    				!chatUsers.get(i).getActiveChatRoom().equals(null)) {											//user is not null
    			try {
    				ObjectOutputStream outStream = outputStreams.get(chatUsers.get(i).getName());	//get Output Stream
					outStream.writeObject(message);												//send message through stream
				}//try 
    			catch (IOException e) {
					System.out.println("ERROR SENDING MESSAGE to User: " + chatUsers.get(i).getName());
					e.printStackTrace();
				}//catch
    		}//if
    	}//for
    	
    	//MESSAGE HAS BEEN SENT TO ACTIVE USERS
        logMessage(message); 
    }

    public String getRoomName() {
        return this.roomName;
    }

    public void logMessage(Message message)  {	//MESSAGE TYPE CHATROOM
    	
    	try { 
            // Open chatroom file in append mode by creating an
            // object of BufferedWriter class
            BufferedWriter out = new BufferedWriter(
            new FileWriter(this.chatFile, true));
 
            // Writing on output stream
            out.write(message.getText()+"\n");
            // Closing the connection
            out.close();
        }//try
        catch (IOException e) { // Catch block to handle the exceptions
            System.out.println("ERROR WRITING TO FILE");
        }//catch
    }//logMessage()

    private void reloadHistoryForUser(User user) {    
            // Creating an object of BufferedReader class
            BufferedReader br;
			ObjectOutputStream os = outputStreams.get(user.getName());   
			try {
				br = new BufferedReader(new FileReader(this.chatFile));  //tell what file to read from
	            String st;
				String longString = "";
	            try {
					while ((st = br.readLine()) != null) { //read until end of file
					  longString = longString + st + "\n";
						        //print line by line 
					    //TODO PRINT ONTO GUI
					}//while

					Message message = new Message("HISTORY", "VERIFIED", null);
					message.setText(longString);
					os.writeObject(message);
				}//try
	            catch (IOException e) {
					System.out.println("ERROR: READING FROM FILE");
				}//catch
			}//try 
			catch (FileNotFoundException e1) {
				System.out.println("ERROR: RELOADING HISTORY");
			}//catch
	}//reloadHistoryForUser()

    public void setChatLock(User user, Message messageFromClient, ObjectOutputStream objectOutputStream) {
		Message returnMessage;
    	if(user == this.host) {
			if (!this.isLocked()) {
				returnMessage = new Message("LOCKCHAT", "VERIFIED", "The room has been locked!");
				this.chatLocked = true;
			}
			else 
				returnMessage = new Message("LOCKCHAT", "VERIFIED", "The room is already locked....");

			try {
				objectOutputStream.writeObject(returnMessage);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	else {
			returnMessage = new Message("LOCKCHAT", "FAILED", "Only the room host can lock the chat!");
			try {
				objectOutputStream.writeObject(returnMessage);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    public void setChatUnlock(User user, Message messageFromClient, ObjectOutputStream objectOutputStream) {
		Message returnMessage;
    	if(user == this.host) {
    		
			if (this.isLocked())
			{
				returnMessage = new Message("LOCKCHAT", "VERIFIED", "The room has been unlocked!");
				this.chatLocked = false;
			}
			else 
				returnMessage = new Message("LOCKCHAT", "VERIFIED", "The room is already unlocked....");
			try {
				objectOutputStream.writeObject(returnMessage);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	else {
			returnMessage = new Message("LOCKCHAT", "FAILED", "Only the room host can unlock the chat!");
			try {
				objectOutputStream.writeObject(returnMessage);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

	public boolean isLocked() {
		return chatLocked;
	}

	public void removeUser(User user) {
    	for (int i = 0; i < chatUsers.size(); i++) {
			if (chatUsers.get(i) == user) {
				outputStreams.remove(user.getName());
				chatUsers.remove(i);
				break;
			}
		}
    }

	public int getActiveUserCount() {
		return activeUserCount;
	}

	public void incrementActiveUsers() {
		activeUserCount++;
	}

	public void decrementActiveUsers() {
		activeUserCount--;
		if (activeUserCount == 0) {
			chatLocked = false;
		}
	}

	public int getChatUsersSize() {
		return this.chatUsers.size();
	}

	public String getOwner() {
		return host.getName();
	}
	
	public void getHistory(User user, ObjectOutputStream objectOutputStream) {
		boolean found = false;
		for(int i = 0; i < this.chatUsers.size(); i++) {			//loop through all users
    		if(!chatUsers.get(i).equals(null) && (this.chatUsers.get(i).equals(user))) { 				//if present in list
    			found = true;										//FOUND!
				outputStreams.replace(user.getName(), objectOutputStream);
    			reloadHistoryForUser(user);
    			break;
    		}//if
    	}//for
		
		if(!found)
		{
			try {
				objectOutputStream.writeObject(new Message("HISTORY", "FAILED", ""));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}//class