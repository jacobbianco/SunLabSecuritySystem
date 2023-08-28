import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.*;

public class GUI {
	
	static int screenWidth;
	static int screenHeight;
	
	public static void main (String args[]) {
		
		screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
		screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
		
		//Create frame
		JFrame frame = new JFrame();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    frame.setLayout(new BorderLayout(25, 25));
	    frame.setTitle("Sun Lab Security System");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.WHITE);
		try { 
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		//Create heading
		JLabel heading = new JLabel ("Sun Lab Security System");
		heading.setFont(new Font("Serif", Font.BOLD, 50));
		
		//Create top panel
		JPanel topPanel = new JPanel ();
		topPanel.setBackground(Color.white);
		topPanel.add(heading, BorderLayout.CENTER);
		
		//Create mid panel
		JPanel midPanel = new JPanel (new FlowLayout(FlowLayout.LEFT, 20, 10));
		midPanel.setBackground(Color.white);
		
		//Create left panel and all of it's contents
		JPanel leftPanel = new JPanel (new BorderLayout(10,10));
		leftPanel.setBackground(Color.white);
		midPanel.add(leftPanel);
		
		JLabel leftSubheading = new JLabel ("                             Manage Users");
		leftSubheading.setFont(new Font("Serif", Font.BOLD, 30));
		leftPanel.add(leftSubheading, BorderLayout.NORTH);
		
		ArrayList<User> userArray = new ArrayList<User>();
		for(int i = 0; i< 10; i++) userArray.add(new User("Jacob Bianco", 1, false, "Student"));
		
		JScrollPane leftScrollPane = new JScrollPane(renderUsers(userArray));
		leftScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
		leftScrollPane.setBackground(Color.WHITE);
		leftScrollPane.setPreferredSize(new Dimension((int) (screenWidth/2.1), screenHeight));
		leftScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		leftPanel.add(leftScrollPane);
		leftPanel.updateUI();
		
		//Create right panel and all of it's contents
		JPanel rightPanel = new JPanel (new BorderLayout(10,10));
		rightPanel.setBackground(Color.white);
		midPanel.add(rightPanel);
		
		JLabel rightSubheading = new JLabel ("                                View History");
		rightSubheading.setFont(new Font("Serif", Font.BOLD, 30));
		rightPanel.add(rightSubheading, BorderLayout.NORTH);
		
		ArrayList<UserLog> userLogArray = new ArrayList<UserLog>();
		for(int i = 0; i< 10; i++) userLogArray.add(new UserLog("Jacob Bianco", "In", "2018-09-01 09:01:15", 1));
		
		JScrollPane rightScrollPane = new JScrollPane(renderUserHistory(userLogArray));
		rightScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
		rightScrollPane.setBackground(Color.WHITE);
		rightScrollPane.setPreferredSize(new Dimension((int) (screenWidth/2.1), screenHeight));
		rightScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		rightPanel.add(rightScrollPane);
		rightPanel.updateUI();
		
		//Add panels to the frame
		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(midPanel, BorderLayout.CENTER);
		
	    frame.setVisible(true); 
		
	}
	
	//Render the list of users
	public static JPanel renderUsers(ArrayList<User> userArray){
		
		JPanel userListPanel = new JPanel(new FlowLayout());	
		userListPanel.setBackground(Color.white);
		userListPanel.setPreferredSize(new Dimension((int) (screenWidth/2.1), screenHeight+150));		
		
		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		headerPanel.setBackground(Color.white);
		
		JLabel headerName = new JLabel("      Name");
		headerName.setFont(new Font("Serif", Font.PLAIN, 20));
		headerName.setPreferredSize(new Dimension(200, 30));
		headerPanel.add(headerName);
		
		JLabel headerType = new JLabel("     Type");
		headerType.setFont(new Font("Serif", Font.PLAIN, 20));
		headerType.setPreferredSize(new Dimension(150, 30));
		headerPanel.add((headerType));
		
		JLabel headerId = new JLabel("     ID");
		headerId.setFont(new Font("Serif", Font.PLAIN, 20));
		headerId.setPreferredSize(new Dimension(50, 30));
		headerPanel.add((headerId));
		
		JLabel headerButtonLabel = new JLabel("     Toggle Access");
		headerButtonLabel.setFont(new Font("Serif", Font.PLAIN, 20));
		headerButtonLabel.setPreferredSize(new Dimension(200, 30));
		headerPanel.add((headerButtonLabel));

		userListPanel.add(headerPanel);
		
		for(User user : userArray) {
			
			JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
			userPanel.setBackground(Color.white);
			userPanel.setBorder(BorderFactory.createLineBorder(Color.black));
			
			JLabel name = new JLabel(user.name);
			name.setFont(new Font("Serif", Font.PLAIN, 20));
			name.setPreferredSize(new Dimension(200, 30));
			userPanel.add(name);
			
			JLabel type = new JLabel(user.type);
			type.setFont(new Font("Serif", Font.PLAIN, 20));
			type.setPreferredSize(new Dimension(150, 30));
			userPanel.add((type));
			
			JLabel id = new JLabel(((Integer.toString(user.id))));
			id.setFont(new Font("Serif", Font.PLAIN, 20));
			id.setPreferredSize(new Dimension(50, 30));
			userPanel.add((id));
			
			JButton hasAccess = new JButton("Click to disable access");
			hasAccess.setBackground(Color.GREEN);
			
			JButton noAccess = new JButton("Click to enable access");
			noAccess.setBackground(Color.RED);
			
			if(user.access) userPanel.add(hasAccess); 
			else userPanel.add(noAccess);	
			
			userListPanel.add(userPanel);
		}
		
		userListPanel.updateUI();
		return userListPanel;
	}
	
	//Render the list of user logs
	public static JPanel renderUserHistory(ArrayList<UserLog> userLogArray){
		
		JPanel userListPanel = new JPanel(new FlowLayout());	
		userListPanel.setBackground(Color.white);
		userListPanel.setPreferredSize(new Dimension((int) (screenWidth/2.1), screenHeight+150));	
		
		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		headerPanel.setBackground(Color.white);
		
		JLabel headerName = new JLabel("Name");
		headerName.setFont(new Font("Serif", Font.PLAIN, 20));
		headerName.setPreferredSize(new Dimension(200, 30));
		headerPanel.add(headerName);
		
		JLabel headerType = new JLabel("In/Out");
		headerType.setFont(new Font("Serif", Font.PLAIN, 20));
		headerType.setPreferredSize(new Dimension(50, 30));
		headerPanel.add((headerType));
		
		JLabel headerId = new JLabel("ID");
		headerId.setFont(new Font("Serif", Font.PLAIN, 20));
		headerId.setPreferredSize(new Dimension(50, 30));
		headerPanel.add((headerId));
		
		JLabel headerButtonLabel = new JLabel("Timestamp");
		headerButtonLabel.setFont(new Font("Serif", Font.PLAIN, 20));
		headerButtonLabel.setPreferredSize(new Dimension(200, 30));
		headerPanel.add((headerButtonLabel));

		userListPanel.add(headerPanel);
		
		
		for(UserLog log : userLogArray) {
			
			JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
			userPanel.setBackground(Color.white);
			userPanel.setBorder(BorderFactory.createLineBorder(Color.black));
			
			JLabel name = new JLabel(log.name);
			name.setFont(new Font("Serif", Font.PLAIN, 20));
			name.setPreferredSize(new Dimension(200, 30));
			userPanel.add(name);
			
			JLabel in_out = new JLabel(log.in_out);
			in_out.setFont(new Font("Serif", Font.PLAIN, 20));
			in_out.setPreferredSize(new Dimension(50, 30));
			userPanel.add((in_out));
			
			JLabel id = new JLabel(((Integer.toString(log.id))));
			id.setFont(new Font("Serif", Font.PLAIN, 20));
			id.setPreferredSize(new Dimension(50, 30));
			userPanel.add((id));
			
			JLabel ts = new JLabel(log.timestamp);
			ts.setFont(new Font("Serif", Font.PLAIN, 20));
			ts.setPreferredSize(new Dimension(200, 30));
			userPanel.add((ts));
			
			userListPanel.add(userPanel);
		}
		
		userListPanel.updateUI();
		return userListPanel;
	}
	
}
