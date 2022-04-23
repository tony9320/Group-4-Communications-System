import java.io.*;
import java.net.*;
import java.util.Scanner;

class Client {
	public static void main(String[] args) {
		try (Socket socket = new Socket("localhost", 1234)) {
			// Used to send Messages to the server
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            // This is how you would get a Message back during the authentication process
            InputStream inputStream = socket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

			String login;
			Scanner messageScanner = new Scanner(System.in);

			login = messageScanner.nextLine();

			Message loginMessage = new Message("LOGIN", login);
			objectOutputStream.writeObject(loginMessage);

		
			boolean authenticated = false;
			try {
				loginMessage = (Message) objectInputStream.readObject();
				System.out.println(loginMessage.getStatus());
				System.out.println(loginMessage.getText());
				if (loginMessage.getStatus().equals("VERIFIED")) {
					ReceiveMessages inputThread = new ReceiveMessages(socket, objectInputStream);
					new Thread(inputThread).start();		
					authenticated = true;
				}
				else {
					System.out.println("NOT VERIFIED");
					login = messageScanner.nextLine();
					loginMessage = new Message("LOGIN", login);
					objectOutputStream.writeObject(loginMessage);
				}

				if (!authenticated) {
					loginMessage = (Message) objectInputStream.readObject();
					while (!loginMessage.getStatus().equals("VERIFIED")) {
						login = messageScanner.nextLine();
						loginMessage = new Message("LOGIN", login);
						objectOutputStream.writeObject(loginMessage);
						loginMessage = (Message) objectInputStream.readObject();
					}

					ReceiveMessages inputThread = new ReceiveMessages(socket, objectInputStream);
					new Thread(inputThread).start();
			}



			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			
			String messageType;
			String messageText;
			messageScanner = new Scanner(System.in);
			Message message;
			// Allows the user to send text Messages to the server until they decide to log out
			while (!socket.isClosed()) {
				try {
					message = new Message();
					messageType = messageScanner.nextLine();
					messageText = messageScanner.nextLine();

					message.setType(messageType);
					message.setText(messageText);
					objectOutputStream.writeObject(message);
					

					
                }
				catch (Exception e) {
					e.printStackTrace();
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
			System.out.println("Creating the receive thread...");
			
		} 

		public void run() {
		try {
			System.out.println("TRYING");
			Message returnedMessage;

			
			while (true) {
				returnedMessage = (Message) objectInputStream.readObject();
				System.out.println(returnedMessage.getText());
				
				
			
				
				
			}
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}

}
