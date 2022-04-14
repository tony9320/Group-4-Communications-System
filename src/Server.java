import java.io.*;
import java.net.*;
import java.util.ArrayList;

class Server {
	public static void main(String[] args) {
		ServerSocket server = null;
		try {
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
				new Thread(clientSock).start();
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
		private static ArrayList<Socket> allUsers = new ArrayList<Socket>();
		private static ArrayList<ObjectOutputStream> allOutputStreams = new ArrayList<ObjectOutputStream>();

		private String username;
		private static int number = 0;
		
		// Constructor
		public ClientHandler(Socket socket) {
			this.clientSocket = socket;
			System.out.println("New thread created");
			number++;
			System.out.println("Number of connect clients: " + number);
			username = "User" + number;
			allUsers.add(socket);
			System.out.println(username + " " + socket);
		}
	
		public void run() {
			try {
				// Allows us to receive Messages from the client
				InputStream inputStream = clientSocket.getInputStream();
				ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
				
				
				// Allows us to send Messages back to the client
				OutputStream outputStream = clientSocket.getOutputStream();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
				allOutputStreams.add(objectOutputStream);
		
				// Reading the message from the client thread
				try {
                    Message messageFromClient;
					// This is where the Server takes Messages from the Clients and decides what to do based on the Message's type
					while (true) {
						messageFromClient = (Message) objectInputStream.readObject();
						
						if (messageFromClient.getType().equals("text")) {
							messageFromClient.setText(username + " " + messageFromClient.getText().toUpperCase());
						
							for (int i = 0; i < allOutputStreams.size(); i++) {
								allOutputStreams.get(i).writeObject(messageFromClient);	
							}
						}
						else if (messageFromClient.getType().equals("logout")) {
							messageFromClient.setStatus("success");
							objectOutputStream.writeObject(messageFromClient);
							System.out.println("Logout message received. Closing the socket");
							clientSocket.close();
					
							return;
						}
					}
				}
				catch (Exception e) {
					System.out.println("Error");
				}
			}
			catch (Exception e) {
				System.out.println("There was an exception");
			}
		}
	}
}
