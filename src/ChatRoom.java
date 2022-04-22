import java.io.*;
import java.util.ArrayList;


class ChatRoom {
    private String roomName;								//the unique name of the Chatroom
    private ArrayList<User> activeChatUsers;				//Array of Users actively logged into current chat
    private ArrayList<User> particpatingChatUsers;			//Array of all Users particpating in chat
    private Boolean chatLocked;								//Boolean if chat is locked
    private User host;										//host of the chat
    private File chatFile;									//File holding Chat Messages
    
    public ChatRoom(User user, Message message) {			//Type DISPLAYCHATROOM Message
    	this.activeChatUsers = new ArrayList<User>();		//create the array
    	this.activeChatUsers.add(user);						//user is added to List of activeUsers
    	this.particpatingChatUsers = new ArrayList<User>();	//create the array
    	this.particpatingChatUsers.add(user);				//user is added to List of participatingUsers
    	this.chatLocked = false;							//chat is created unlocked
    	this.host = user;									//set host
    	this.roomName = message.getText();					//set name of room
    	this.chatFile = new File(roomName);
    	try {
			this.chatFile.createNewFile();					//create the file for the chatroom!
		}//try 
    	catch (IOException e) {
			System.out.println("ERROR CREATING FILE");
		}//catch
    }//Chatroom()

    public void addActiveUser(User user) {
    	boolean found = false;
    	
    	for(int i = 0; i < this.activeChatUsers.size(); i++) {				//loop through all active users
    		if(this.activeChatUsers.get(i).equals(user) &&					//user is in current chatroom
    				!activeChatUsers.get(i).equals(null)) { 				//if present in list
    			found = true;												//FOUND!
    			reloadHistoryForUser(user);									//reload the messages
    			break;
    		}//if
    	}//for
    	
    	if(found == false) {												//if not found add to list
    		this.activeChatUsers.add(user);
    	}//if
    }//addActiveUser()
    
    public void removeActiveUser(User user) {
    	for(int i = 0; i < this.activeChatUsers.size(); i++) {				//loop through all active users
    		if(this.activeChatUsers.get(i).equals(user) &&					//user is in current chatroom
    				!activeChatUsers.get(i).equals(null)) { 				//if present in list
    			this.activeChatUsers.remove(i);								//remove from list
    			break;
    		}//if
    	}//for
    }//removeActiveUser()

    public void sendMessage(Message message) {														//message type CHATROOM
    	
    	for(int i = 0; i < this.activeChatUsers.size(); i++) { 										//loop through all participating users
    		if(activeChatUsers.get(i).getActiveChatRoom().equals(this.roomName) &&					//user is in current chatroom
    				!activeChatUsers.get(i).equals(null)) {											//user is not null
    			try {
    				ObjectOutputStream outStream = activeChatUsers.get(i).getObjectOutputStream();	//get Output Stream
					outStream.writeObject(message);													//send message through stream
				}//try 
    			catch (IOException e) {
					System.out.println("ERROR SENDING MESSAGE to User: " + activeChatUsers.get(i).getName());
				}//catch
    		}//if
    	}//for
    	
    	logMessage(message); 		//MESSAGE HAS BEEN SENT TO ACTIVE USERS
    }//sendMessage()

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
            String all="";
            String segment="";
            ObjectOutputStream userStream = user.getObjectOutputStream();
            
            try {
				br = new BufferedReader(new FileReader(this.chatFile));  			//tell what file to read from
				
	            try {
					while ((segment = br.readLine()) != null) { 					//read until end of file
						all += segment+"\n";
					}//while
					Message newMessage = new Message("CHATROOM","Undefined",all);	//new CHATROOM type message 
					userStream.writeObject(newMessage);								//send down users output Stream
				}//try
	            catch (IOException e) {
					System.out.println("ERROR: READING FROM FILE");
				}//catch
			}//try 
			catch (FileNotFoundException e1) {
				System.out.println("ERROR: RELOADING HISTORY");
			}//catch
	}//reloadHistoryForUser()

    public void setChatLock(Message message) {
    	if(message.getSender().equals(this.host)) {
    		this.chatLocked = true;
    	}
    	else {
    		// NOT HOST SENDING REQUEST
    		//CHAT REMAINS THE SAME
    	}
    }

    public void setChatUnlock(Message message) {
    	if(message.getSender().equals(this.host)) {
    		this.chatLocked = false;
    	}
    	else {
    		// NOT HOST SENDING REQUEST
    		//CHAT REMAINS THE SAME
    	}
    }
    
    public void addParticpatingUser(User user) {
    	boolean found = false;
    	
    	for(int i = 0; i < this.particpatingChatUsers.size(); i++) {			//loop through all users
    		if(this.particpatingChatUsers.get(i).equals(user)&&					//user is in current chatroom
    				!particpatingChatUsers.get(i).equals(null)) { 				//if present in list
    			found = true;													//FOUND A DUPLICATE!
    			break;
    		}//if
    	}//for
    	
    	if(found == false && this.chatLocked == false) {												//if not found add to list && if chat is not locked
    		this.particpatingChatUsers.add(user);
    	}//if
    }//addParticipatingUser()
    
    public void removeParticpatingUser(User user) {
    	for(int i = 0; i < this.particpatingChatUsers.size(); i++) {
    		if(this.particpatingChatUsers.get(i).equals(user) && this.chatLocked == false &&					//user is in current chatroom
    				!particpatingChatUsers.get(i).equals(null)) { 												//if present in list
    			this.particpatingChatUsers.remove(i);
    			break;
    		}//if
    	}//for
    }//removeParticipatingUser()
}//class