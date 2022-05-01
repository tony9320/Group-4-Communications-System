import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ClientGUI extends JFrame {

	private JTextField usernameTF = new JTextField(15);
	private JPasswordField passwordF = new JPasswordField(15);
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;
	private Socket socket;
	private String currentUser;
	private HomePanel homePanel;
	private String currentRoom;

	// Constructor.
	public ClientGUI() {

		connectToServer();

		JPanel p = createLoginPanel();
		getContentPane().add(p);
	}

	public void connectToServer() {

		try {
			socket = new Socket("localhost", 1234);
			// Used to send Messages to the server
			// Establish communication channel - to WRITE OUT information (bytes/String).
			OutputStream outputStream = socket.getOutputStream();
			// Create WRAPPER communication channel - to WRITE OUT Java Object.
			objectOutputStream = new ObjectOutputStream(outputStream);

			// This is how you would get a Message back during the authentication process
			// Establish communication channel - to READ IN information (bytes/String).
			InputStream inputStream = socket.getInputStream();
			// Create WRAPPER communication channel - to READ IN Java Object.
			objectInputStream = new ObjectInputStream(inputStream);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendMessage(String type, String text) {

		Message loginMessage = new Message(type, text);
		// Write - SEND the message Object to the server.
		try {
			objectOutputStream.writeObject(loginMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private JPanel createLoginPanel() {
		JPanel p = new JPanel(new GridLayout(3, 3));
		p.add(new JLabel("User name: "));
		p.add(usernameTF);
		p.add(new JLabel("Password: "));
		p.add(passwordF);

		JButton loginB = new JButton("Login");
		p.add(loginB);

		loginB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String username = usernameTF.getText();
				char[] passwordChars = passwordF.getPassword();
				String password = new String(passwordChars);

				login(username, password);

			}
		});

		return p;
	}

	protected void login(String username, String password) {
		String userinfo = username + "/" + password;

		// send Message with 'LOGIN' as the 'type'
		sendMessage("LOGIN", userinfo);

		boolean authenticated = false;
		try {
			// READ REPLY from the server.
			Message loginMessage = (Message) objectInputStream.readObject();
			// Print the REPLY status
			System.out.println(loginMessage.getStatus());
			// Print the REPLY text
			System.out.println(loginMessage.getText());

			// Check if the status is 'successful login'
			if (loginMessage.getStatus().equals("VERIFIED")) {
				// login success
				// Create parallel process (thread) to read/receive message from server.
//				ReceiveMessages inputThread = new ReceiveMessages(socket, objectInputStream);
//				new Thread(inputThread).start();		
				authenticated = true;
				currentUser = username;
				displayHomeScreen();
			} else {
				// Login failed
				JOptionPane.showMessageDialog(this, "Login failed.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void displayHomeScreen() {
		// remove old items
		getContentPane().removeAll();

		// add home panel
		homePanel = new HomePanel(this);
		getContentPane().add(homePanel);

		setTitle("Chat Room: " + currentUser);
		// refresh frame so that the new changes take effect.
		revalidate();

		// set display text
		homePanel.display("Login success!");
		homePanel.displayLine();

	}

	public static void main(String[] args) {
		ClientGUI gui = new ClientGUI();
		gui.setSize(800, 500); // setting x and y dim of frame
		gui.setTitle("Chat Room - Login");
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit button to close app
		gui.setVisible(true);
	}

	public void createChatRoom(String roomName) {

		sendMessage("CREATECHATROOM", roomName);

		try {
			Message replyMessage = (Message) objectInputStream.readObject();
			if ("VERIFIED".equals(replyMessage.getStatus())) {
				currentRoom = roomName;
				homePanel.display("CREATED Chat Room: " + roomName);
				homePanel.displayLine();

			} else {
				homePanel.display("FAILED to Create Chat Room: " + roomName);
				homePanel.display("Reason: " + replyMessage.getText());
				homePanel.displayLine();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void joinChatRoom(String roomName) {
		if (roomName.equals(currentRoom)) {
			homePanel.display("You're already in Chat Room: " + roomName);
			homePanel.displayLine();
			return;
		}
		sendMessage("JOINCHATROOM", roomName);

		try {
			Message replyMessage = (Message) objectInputStream.readObject();
			if ("VERIFIED".equals(replyMessage.getStatus())) {
				homePanel.display("JOINED Chat Room: " + roomName);
				homePanel.displayLine();

			} else {
				homePanel.display("FAILED to Join Chat Room: " + roomName);
				homePanel.display("Reason: " + replyMessage.getText());
				homePanel.displayLine();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
