import java.io.*;
import java.net.*;

class Client {
	public static void main(String[] args) {
		try (Socket socket = new Socket("localhost", 1234)) {
			// Used to send Messages to the server
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            		// This is how you would get a Message back during the authentication process
            		InputStream inputStream = socket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			
			// Starts the thread used to retrieve Messages after authentication
			// Notice how I am sending the original input stream to the thread
			ReceiveMessages inputThread = new ReceiveMessages(socket, objectInputStream);
			//new Thread(inputThread).start();			
			
			// Allows the user to send text Messages to the server until they decide to log out
			while (!socket.isClosed()) {
				try {
					
					// This is where Messages are SENT (and functions such as displayUsers() would be called)

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
