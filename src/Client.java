import java.io.*;
import java.net.*;

class Client {
	public static void main(String[] args) {
		try (Socket socket = new Socket("localhost", 1234)) {
			// Used to send Messages to the server
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

			// Send a login Message
			Message message = new Message("login", "Undefined", "login message");
			objectOutputStream.writeObject(message);

            // Checking if the User is authenticated
            InputStream inputStream = socket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			
			// Used to get the Message back from the server
			ReceiveMessages inputThread = new ReceiveMessages(socket, objectInputStream);
			new Thread(inputThread).start();			
			
			// Allows the user to send text Messages to the server until they decide to log out
			while (!socket.isClosed()) {
				try {

					// This is where Messages are SENT (and functions such as displayUsers() would be called)

				    message = new Message("CHATROOM", "Undefined", "login message");
					objectOutputStream.writeObject(message);
					 message = new Message("DISPLAYCHATROOMS", "Undefined", "login message");
					objectOutputStream.writeObject(message);
					 message = new Message("JOINCHATROOM", "Undefined", "login message");
					objectOutputStream.writeObject(message);
					 message = new Message("CHANGEPASSWORD", "Undefined", "login message");
					objectOutputStream.writeObject(message);
					 message = new Message("CREATEUSER", "Undefined", "login message");
					objectOutputStream.writeObject(message);
					 message = new Message("CREATESUPERVISOR", "Undefined", "login message");
					objectOutputStream.writeObject(message);
					 message = new Message("DISPLAYUSERS", "Undefined", "login message");
					objectOutputStream.writeObject(message);
					 message = new Message("DISPLAYUSERS", "Undefined", "login message");
					objectOutputStream.writeObject(message);
                }
				catch (Exception e) {
					System.out.println("An exception was thrown");
				}
			}
			
	
		}
		catch (IOException e) {
			e.printStackTrace();
		}	
    }
	
	private static class ReceiveMessages implements Runnable {
		private final Socket clientSocket;
        private ObjectInputStream objectInputStream;
		
		public ReceiveMessages(Socket socket, ObjectInputStream objectInputStream) {
			this.clientSocket = socket;
            this.objectInputStream = objectInputStream;
			
		} 

		public void run() {
		try {
			Message returnedMessage;
			returnedMessage = (Message) objectInputStream.readObject();
			
			while (true) {
				
				// This is where Messages are RECEIVED 
			
				
				
			}
			
			
		}
		catch (Exception e) {
			System.out.println("ReceiveMessages failed");
		}
	}
}

}