import java.util.ArrayList;

class ChatRoom {
    private String roomName;
    private ArrayList<User> chatUsers;
    private Boolean chatLocked;
    private User host; // Better name than what is on the UML imo, your choice!
    private String messageHistory; // This should prob be changed - you decide how to send 
                                   // the old chat history so that the messaging is asynchronous
    public ChatRoom(User user, Message message) {

    }

    public void addUser(User user) {

    }

    public void sendMessage(Message message) {
        logMessage(message); // Do it however you want, just an example
    }

    public String getRoomName() {
        return roomName;
    }

    public void logMessage(Message message) {

    }

    private void reloadHistoryForUser(Message message) {
        // This would be used if addUser attempts to add a User who was already in chatUsers
        // --- this would indicate the User just switched their active chat room back to this one
    }

    public void setChatLock(Message message) {

    }

    public void setChatUnlock(Message message) {
        
    }



}