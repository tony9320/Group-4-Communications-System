import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    protected String type;
    protected String text;
    protected String status;
    protected User sender;
    protected Date timeStamp; 
    protected String[] roomList;

    public Message(){    	
        this.type = "Undefined";
        this.status = "Undefined";
        this.text = "Undefined";
    	Date currentDate = new Date();
    	this.timeStamp = currentDate;
    }

    public Message(String type, String text) {
    	setType(type);
    	setText(text);
    	Date currentDate = new Date();
    	this.timeStamp = currentDate;
    }
    
    public Message(String status) {
    	setStatus(status);
    	Date currentDate = new Date();
    	this.timeStamp = currentDate;
    }
    
    // Constructor for sending messages to a chatroom
    public Message(String type, String text, User sender, String[] roomList) {
    	setType(type);
    	setText(text);
    	setSender(sender);
        setRoomList(roomList);
    	Date currentDate = new Date();
    	this.timeStamp = currentDate;
    }

    public Message(String type, String status, String text){
        setType(type);
        setStatus(status);
        setText(text);
    	Date currentDate = new Date();
    	this.timeStamp = currentDate;
    }

    private void setType(String type){
    	this.type = type;
    }

    public void setStatus(String status){
    	this.status = status;
    }

    public void setText(String text){
    	this.text = text;
    }
    
    public void setSender(User sender) {
    	this.sender = sender;
    }
    
    public void setRoomList(String[] roomList) {
    	this.roomList = roomList;
    }

    public String getType(){
    	return type;
    }

    public String getStatus(){
    	return status;
    }

    public String getText(){
    	return text;
    }
    
    public User getSender() {
    	return sender;
    }
    
    public Date getTimeStamp() {
    	return timeStamp;
    }
    
    public String[] getRoomList() {
    	return roomList;
    }

}
