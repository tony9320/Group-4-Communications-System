import java.io.*;
import java.util.ArrayList;


class ChatRoom {
    private String roomName;
    private ArrayList<User> chatUsers;
    private Boolean chatLocked;
    private User host;
    private File chatFile;
    
    public ChatRoom(User user, Message message) {	//Type DISPLAYCHATROOM Message
    	this.chatUsers = new ArrayList<User>();		//create the array
    	this.chatUsers.add(user);					//user is added to List of Users
    	this.chatLocked = false;					//chat is created unlocked
    	this.host = user;							//set host
    	this.roomName = message.getText();			//set name of room
    	this.chatFile = new File(roomName);
    	try {
			this.chatFile.createNewFile();			//create the file for the chatroom!
		} catch (IOException e) {
			System.out.println("ERROR CREATING FILE");
		}
    }

    public void addUser(User user) {
    	boolean found = false;
    	
    	for(int i = 0; i < this.chatUsers.size(); i++) {			//loop through all users
    		if(this.chatUsers.get(i).equals(user)) { 				//if present in list
    			found = true;										//FOUND!
    			reloadHistoryForUser(user);
    			break;
    		}//if
    	}//for
    	
    	if(found == false) {												//if not found add to list
    		this.chatUsers.add(user);
    	}//if
    }//addUser()

    public void sendMessage(Message message) {	//message type Chatroom
    	
    	for(int i = 0; i < this.chatUsers.size(); i++) { 										// loop through all users
    		if(chatUsers.get(i).getActiveChatRoom().equals(this.roomName) &&					//user is in current chatroom
    				!chatUsers.get(i).equals(null)) {											//user is not null
    			try {
    				ObjectOutputStream outStream = chatUsers.get(i).getObjectOutputStream();	//get Output Stream
					outStream.writeObject(message);												//send message through stream
				}//try 
    			catch (IOException e) {
					System.out.println("ERROR SENDING MESSAGE to User: " + chatUsers.get(i).getName());
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
			try {
				br = new BufferedReader(new FileReader(this.chatFile));  //tell what file to read from
	            String st;
	            try {
					while ((st = br.readLine()) != null) { //read until end of file
					    System.out.println(st);            //print line by line 
					    //TODO PRINT ONTO GUI
					}//while
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

}//class