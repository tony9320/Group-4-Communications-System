import java.io.*;
import java.util.ArrayList;


class ChatRoom {
    private String roomName;
    private ArrayList<User> chatUsers;
    private Boolean chatLocked;
    private User host;
    private String messageHistory; 			// This should prob be changed - you decide how to send 
                                   			// the old chat history so that the messaging is asynchronous
    public ChatRoom(User user, Message message) {	//Type DISPLAYCHATROOM Message
    	this.chatUsers.add(user);			//user is added to List of Users
    	this.chatLocked = false;			//chat is created unlocked
    	this.host = user;					//set host
    	this.roomName = message.getText();
    	//this.messageHistory = ?? //TODO

    }

    public void addUser(User user) {
    	boolean found = false;
    	
    	for(int i = 0; i < this.chatUsers.size(); i++) {			//loop through all users
    		if(this.chatUsers.get(i).equals(user)) { 				//if present in list
    			found = true;										//FOUND!
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

    public void logMessage(Message message) {
    	
    	//Create a file 

    }

    private void reloadHistoryForUser(Message message) {
        // This would be used if addUser attempts to add a User who was already in chatUsers
        // --- this would indicate the User just switched their active chat room back to this one
    }

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



}