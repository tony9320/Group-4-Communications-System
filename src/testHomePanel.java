import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;

public class testHomePanel extends JPanel {

	private JTextArea displayTA;
	//private ClientGUI gui;
	private testClientGUI tgui;
	private JTextField usernameTF = new JTextField(15);
	private JPasswordField passwordF = new JPasswordField(15);
	
	
	
	
	// Constructor
	public testHomePanel(testClientGUI ttgui) {
		this.tgui = ttgui;
	}

	
	
	public testHomePanel constructMainMenu()
	{
		setLayout(new BorderLayout());
		
		
		JPanel west = createWestPanel();
		add(west, BorderLayout.WEST);

		JPanel center = createCenterPanel();
		add(center, BorderLayout.CENTER);
		
		JPanel east = createExitPanel();
		add(east, BorderLayout.EAST);
		
		return this;
	}
	
	public testHomePanel constructRoomWindow(Message message)
	{
		setLayout(new BorderLayout());
		
		JPanel rooms = createRoomPane(message);
		add(rooms, BorderLayout.WEST);
		return this;
	}
	
	public testHomePanel constructChatWindow(String chatroom)
	{

		setLayout(new BorderLayout());
		JPanel west = createWestChatPanel();
		add(west, BorderLayout.WEST);
		
		JPanel center = createCenterPanel();
		add(center, BorderLayout.CENTER);

		JPanel south = createSouthPanel();
		add(south, BorderLayout.SOUTH);
		
		JPanel east = createChatPanel(chatroom);
		add(east, BorderLayout.EAST);
		
		return this;
	}
	
	public testHomePanel constructLoginWindow(String type)
	{
		setLayout(new BorderLayout());
		JPanel loginP = createLoginPanel(type);
		add(loginP);
		return this;
	}
	
	public testHomePanel constructSuperWindow()
	{
		setLayout(new BorderLayout());
		
		JPanel west = createSuperPanel();
		add(west, BorderLayout.WEST);

		JPanel center = createCenterPanel();
		add(center, BorderLayout.CENTER);
		
		JPanel east = createExitPanel();
		add(east, BorderLayout.EAST);
		
		return this;
	}
	
	private JPanel createSouthPanel() {
		JPanel south = new JPanel();

		JTextField messageTF = new JTextField(25);
		JButton sendB = new JButton("Send");

		south.add(new JLabel("Enter Message: "));
		south.add(messageTF);
		south.add(sendB);

		sendB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO PENDING
				if(!messageTF.getText().isEmpty())
				{
					tgui.sendMessage(messageTF.getText());
				}
			}
		});

		return south;
	}

	private JPanel createCenterPanel() {
		
		JPanel center = new JPanel();

		displayTA = new JTextArea(20, 50);
		displayTA.setEditable(false);

		//Add a scroll to displayTA
		JScrollPane scroll = new JScrollPane(displayTA);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		//Add the scrollable TA to the panel
		center.add(scroll);
		
		return center;
	}

	private JPanel createWestPanel() {
		JPanel west = new JPanel(new GridLayout(10, 1));

		JButton createRoomB = new JButton("Create Chat Room");
		west.add(createRoomB);
		createRoomB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String roomName = JOptionPane.showInputDialog("Enter chat room name: ");
				if(tgui.createChatRoom(roomName))
				{
					tgui.displayChat("CREATED Chat Room: " + roomName, true);
				}
				
			}
		});

		JButton joinRoomB = new JButton("Join Chat Room");
		west.add(joinRoomB);
		joinRoomB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				tgui.displayRooms();
			}
		});

		JButton changePasswordB = new JButton("Change Password");
		west.add(changePasswordB);
		changePasswordB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String password = JOptionPane.showInputDialog("Enter new password: ");
				tgui.changePassword(password);
			}
		});
		JButton displayChatRoomsB = new JButton("Display Chatrooms");
		west.add(displayChatRoomsB);
		displayChatRoomsB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				tgui.displayChatRooms();
			}
		});
		JButton displayUsersB = new JButton("Display Users");
		west.add(displayUsersB);
		displayUsersB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				tgui.displayUsers();
			}
		});
		return west;
	}
	
	
	public void display(String text) {
		displayTA.append(text);
		displayTA.append("\n");
	}

	public void displayLine() {
		display("-----------------");

	}
	
	private JPanel createRoomPane(Message message)
	{
		ArrayList<String> temp = new ArrayList<String>();
		
		for(String s: message.getRoomList())
		{
			
			if (s.contains("\n"))	//if there's a chatroom with users in it, we need to extract the chatroom name
			{
				String get[] = s.split("\n");
				temp.add(get[0]);
			}else if(!s.contains("No active chat rooms!"))
			{
				temp.add(s);
			}
			
		}
		
		JPanel chatDisplay = new JPanel(new GridLayout(temp.size()+ 1, temp.size()+ 1));
		if(temp.size() > 0)
		{
			
			
			for(String t: temp)
			{
				{
					JButton button = new JButton(t);
					chatDisplay.add(button);
					button.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							Message getMessage = new Message();
							getMessage = tgui.joinChatRoom(t);
							if(getMessage.getStatus().equals("VERIFIED"))
							{
								if(getMessage.getType().equals("HISTORY") && getMessage.getText() != "") //reload chat history
								{
									tgui.displayChat("Welcome to Chat Room " + t + "!\n", false);
									
								}
								else	//first time in chat, display welcome message
								{
									tgui.displayChat("Welcome to Chat Room " + t + "!", true);
								}
							}
							else
							{
								JOptionPane.showMessageDialog(null, "Room is locked!");
							}
						}
					});
				}
				
			}
		}
		JButton button = new JButton("Exit");
		chatDisplay.add(button);
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!tgui.getSupervisorStatus())
				{
					tgui.displayHomeScreen("Main Menu");
				}
				else
				{
					tgui.displaySuperHomeScreen("Main menu");
				}
			}
		});
		return chatDisplay;

	}
	
	private JPanel createChatPanel(String chatroom)
	{
		JPanel chatPanel = new JPanel();
		
		JButton exitB = new JButton("Exit");
		chatPanel.add(exitB);

		exitB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tgui.leaveChatRoom(chatroom);
				if(!tgui.getSupervisorStatus())
				{
					tgui.displayHomeScreen("Main Menu");
				}
				else
				{
					tgui.displaySuperHomeScreen("Main menu");
				}

			}
		});

		return chatPanel;
	}
	
	private JPanel createWestChatPanel()
	{
		JPanel west = new JPanel(new GridLayout(5,1));
		
		JButton lockRoomB = new JButton("Lock Chat Room");
		west.add(lockRoomB);
		lockRoomB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				tgui.lockChatRoom();
			}
		});
		
		JButton unlockRoomB = new JButton("Unlock Chat Room");
		west.add(unlockRoomB);
		unlockRoomB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				tgui.unlockChatRoom();
			}
		});
		JButton historyB = new JButton("Reload Chat");
		west.add(historyB);
		historyB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				displayTA.setText("");
				tgui.reloadChat();
				
			}
		});
		JButton clearChatB = new JButton("Clear Chat");
		west.add(clearChatB);
		clearChatB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				displayTA.setText("");
			}
		});
		JButton displayUsersB = new JButton("Display Chat Users");
		west.add(displayUsersB);
		displayUsersB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				tgui.displayChatUsers();
			}
		});
		
		return west;
	}
	
	private JPanel createSuperPanel()
	{
		JPanel west = createWestPanel();
		
		JButton createUserB = new JButton("Create User");
		west.add(createUserB);
		createUserB.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				tgui.displayLogin("CREATE");
				
			}
		});

		JButton createSuperB = new JButton("Create Supervisor");
		west.add(createSuperB);
		createSuperB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				tgui.displayLogin("SUPER");
				
			}
		});
		JButton deleteB = new JButton("Delete User");
		west.add(deleteB);
		deleteB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = JOptionPane.showInputDialog("Enter user to delete");
				tgui.deleteUser(username);
			}
		});
		JButton logB = new JButton("Retrieve Logs");
		west.add(logB);
		logB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				tgui.getLogs();
			}
		});
		
		return west;
	}
	
	
		private JPanel createLoginPanel(String type) {
		
		JPanel p = new JPanel();
		SpringLayout layout = new SpringLayout();
		p.setLayout(layout);
		
		//Username and password labels
		JLabel user = new JLabel("User name: ");
		JLabel pass = new JLabel("Password: ");
		
		//Add user/password label and textfield components
		p.add(user);
		p.add(usernameTF);
		p.add(pass);
		p.add(passwordF);
		
		//user label position
		layout.putConstraint(SpringLayout.WEST,  user, 250, SpringLayout.WEST, p);
		layout.putConstraint(SpringLayout.NORTH,  user, 150, SpringLayout.NORTH, p);
		
		//user textfield position
		layout.putConstraint(SpringLayout.WEST,  usernameTF, 325, SpringLayout.WEST, p);
		layout.putConstraint(SpringLayout.NORTH,  usernameTF, 150, SpringLayout.NORTH, p);
		
		//password label position
		layout.putConstraint(SpringLayout.WEST,  pass, 250, SpringLayout.WEST, p);
		layout.putConstraint(SpringLayout.NORTH,  pass, 175, SpringLayout.NORTH, p);
		
		//password textfield position
		layout.putConstraint(SpringLayout.WEST,  passwordF, 325, SpringLayout.WEST, p);
		layout.putConstraint(SpringLayout.NORTH,  passwordF, 175, SpringLayout.NORTH, p);
		
		//Login button component
		JButton loginB = new JButton("Submit");
		p.add(loginB);
		
		//Login button position
		layout.putConstraint(SpringLayout.WEST,  loginB, 325, SpringLayout.WEST, p);
		layout.putConstraint(SpringLayout.NORTH,  loginB, 200, SpringLayout.NORTH, p);
		
		//Action Listener for login input
		loginB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String username = usernameTF.getText();
				char[] passwordChars = passwordF.getPassword();
				String password = new String(passwordChars);
	
				if(type.equals("LOGIN"))
				{
					tgui.login(username, password);
				}
				else if(type.equals("CREATE"))
				{
					tgui.createUser(username, password);
				}
				else if(type.equals("SUPER"))
				{
					tgui.createSuper(username, password);
				}

			}
		});
		
		JButton exitB = new JButton("   Quit   ");
	
		p.add(exitB);
		
		//Login button position
		layout.putConstraint(SpringLayout.WEST,  exitB, 405, SpringLayout.WEST, p);
		layout.putConstraint(SpringLayout.NORTH,  exitB, 200, SpringLayout.NORTH, p);
		exitB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
	
				if(type.equals("LOGIN"))
				{
					System.exit(ABORT);
				}
				else if(type.equals("CREATE"))
				{
					tgui.displaySuperHomeScreen("Main menu");
				}
				else if(type.equals("SUPER"))
				{
					tgui.displaySuperHomeScreen("Main menu");
				}

			}
		});
		
		
		return p;
	}
		
		public JPanel createExitPanel()
		{
			JPanel exitP = new JPanel();
			JButton exitB = new JButton("Exit");
			exitP.add(exitB);

			exitB.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					tgui.logOut();
					
					System.exit(ABORT);

				}
			});

			return exitP;
		}
	
}
