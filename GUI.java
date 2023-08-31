import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.*;

import com.google.firebase.cloud.FirestoreClient;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteBatch;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;


public class GUI {
	
	static int screenWidth;
	static int screenHeight;
	
	//Need these to be static so they can be accessed throughout the whole class
	static ArrayList<User> userArray;
	static ArrayList<UserLog> userLogArray;
	
	static ArrayList<User> filteredUserArray;
	static ArrayList<UserLog> filteredUserLogArray;
	
	static JScrollPane leftScrollPane;
	static JScrollPane rightScrollPane;
	
	static JPanel leftPanel;
	static JPanel rightPanel;
	
	static Firestore db;
	
	//Create log in panel and student view, then done 
	
	
	public static void main (String args[]) throws Exception {
		
		//Set screen width and height
		screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
		screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
		
		//Initialize db and retrieve data
		db = initializeDb();
		userArray = getUsers(db);
		userLogArray = getUserHistory(db);
		filteredUserArray = userArray;
		filteredUserLogArray = userLogArray;
	
		
		//Create frame
		JFrame frame = new JFrame();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    frame.setLayout(new BorderLayout(25, 25));
	    frame.setTitle("Sun Lab Security System");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.WHITE);
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
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
		leftPanel = new JPanel (new BorderLayout(10,10));
		leftPanel.setBackground(Color.white);
		midPanel.add(leftPanel);
		
		JLabel leftSubheading = new JLabel ("Manage Users");
		leftSubheading.setFont(new Font("Serif", Font.BOLD, 30));
		JPanel leftSubheadingContainer = new JPanel();
		leftSubheadingContainer.setBackground(Color.white);
		leftSubheadingContainer.add(leftSubheading);
		leftPanel.add(leftSubheadingContainer, BorderLayout.NORTH);
		
		leftScrollPane = new JScrollPane(renderUsers(filteredUserArray));
		leftScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
		leftScrollPane.setBackground(Color.WHITE);
		leftScrollPane.setPreferredSize(new Dimension((int) (screenWidth/2.1), screenHeight));
		leftScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		leftPanel.add(leftScrollPane);
		leftPanel.updateUI();
		
		//Create right panel and all of it's contents
		rightPanel = new JPanel (new BorderLayout(10,10));
		rightPanel.setBackground(Color.white);
		midPanel.add(rightPanel);
		
		JLabel rightSubheading = new JLabel ("View History");
		rightSubheading.setFont(new Font("Serif", Font.BOLD, 30));
		JPanel rightSubheadingContainer = new JPanel();
		rightSubheadingContainer.setBackground(Color.white);
		rightSubheadingContainer.add(rightSubheading);
		rightPanel.add(rightSubheadingContainer, BorderLayout.NORTH);
		
		JButton filterButton = new JButton("Filter");
		filterButton.setFont(new Font("Serif", Font.PLAIN, 15));
		filterButton.setPreferredSize(new Dimension(100,30));
		filterButton.addActionListener(e -> filterButtonPressed());
		rightSubheadingContainer.add(filterButton);
		
		JButton undoButton = new JButton("Undo Filter");
		undoButton.setFont(new Font("Serif", Font.PLAIN, 15));
		undoButton.setPreferredSize(new Dimension(100,30));
		undoButton.addActionListener(e -> undoButtonPressed());
		rightSubheadingContainer.add(undoButton);
		
		rightScrollPane = new JScrollPane(renderUserHistory(filteredUserLogArray));
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
	
	//Undo filter
	private static void undoButtonPressed() {
			filteredUserLogArray = userLogArray;
			//Add old info to scrollpane and update the panel
			rightPanel.remove(rightScrollPane);
			rightScrollPane = new JScrollPane(renderUserHistory(filteredUserLogArray));
			rightScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
			rightScrollPane.setBackground(Color.WHITE);
			rightScrollPane.setPreferredSize(new Dimension((int) (screenWidth/2.1), screenHeight));
			rightScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			rightPanel.add(rightScrollPane);
			rightPanel.updateUI();
	}

	//Create frame to let user filter
	private static void filterButtonPressed() {
		//Create frame
		JFrame filterFrame = new JFrame();
		filterFrame.setSize(new Dimension(500,300));
		filterFrame.setLocationRelativeTo(null);
		filterFrame.setBackground(Color.WHITE);
		
		//Create panel
		JPanel mainPanel = new JPanel(new GridLayout(5,1));
		mainPanel.setBackground(Color.WHITE);
		
		JLabel headerLabel = new JLabel("Note: You may either filter by ID, filter by date, or filter by date and time");
		headerLabel.setFont(new Font("Serif", Font.BOLD, 15));
		JPanel headerContainer = new JPanel();
		headerContainer.setBackground(Color.WHITE);
		headerContainer.add(headerLabel);
		mainPanel.add(headerContainer);
		
		JLabel idLabel = new JLabel("        Filter by ID( Ex: 1 ):");
		idLabel.setFont(new Font("Serif", Font.PLAIN, 20));
		JTextField id = new JTextField(4);
		id.setPreferredSize(new Dimension(30,30));
		id.setFont(new Font("Serif", Font.PLAIN, 20));
		JPanel idContainer = new JPanel();
		idContainer.setBackground(Color.WHITE);
		idContainer.add(idLabel);
		idContainer.add(id);
		mainPanel.add(idContainer);
		
		JLabel dateLabel = new JLabel("Filter by date( Ex: 2023-08-24 ):");
		dateLabel.setFont(new Font("Serif", Font.PLAIN, 20));
		JTextField date = new JTextField(8);
		date.setPreferredSize(new Dimension(50,30));
		date.setFont(new Font("Serif", Font.PLAIN, 20));
		JPanel dateContainer = new JPanel();
		dateContainer.setBackground(Color.WHITE);
		dateContainer.add(dateLabel);
		dateContainer.add(date);
		mainPanel.add(dateContainer);
		
		JLabel timeLabel = new JLabel("    Filter by time( Ex: 16:45:12 ):");
		timeLabel.setFont(new Font("Serif", Font.PLAIN, 20));
		JTextField time = new JTextField(8);
		time.setPreferredSize(new Dimension(50,30));
		time.setFont(new Font("Serif", Font.PLAIN, 20));
		JPanel timeContainer = new JPanel();
		timeContainer.setBackground(Color.WHITE);
		timeContainer.add(timeLabel);
		timeContainer.add(time);
		mainPanel.add(timeContainer);
		
		JButton submitButton = new JButton("Submit");
		submitButton.setFont(new Font("Serif", Font.PLAIN, 15));
		submitButton.setPreferredSize(new Dimension(100,30));
		submitButton.addActionListener(e -> filterResults(id.getText().trim(), date.getText().trim(), time.getText().trim(), filterFrame));
		JPanel buttonContainer = new JPanel();
		buttonContainer.setBackground(Color.WHITE);
		buttonContainer.add(submitButton);
		mainPanel.add(buttonContainer);
		
		filterFrame.add(mainPanel);
		filterFrame.setVisible(true);
	}

	//Filters userLogs
	@SuppressWarnings("deprecation")
	private static void filterResults(String id, String date, String time, JFrame filterFrame) {
		
		ArrayList<UserLog> newArray = new ArrayList<UserLog>();
		if(id.length() > 0 && date.length() == 0 & time.length() == 0) filteredUserLogArray.forEach(item -> {
			if((Integer.toString(item.id).equals(id))) newArray.add(item);
		});
		else if(date.length() > 0 && time.length() == 0 & id.length() == 0 ) filteredUserLogArray.forEach(item -> {
			if(item.timestamp.toString().substring(0, 10).equals(date)) 
				if(!newArray.contains(item)) newArray.add(item);
		});
		//Note: need to convert in funky way because google FB has 4 hour offset in time
		else if(time.length() > 0 && date.length() > 0 && id.length() == 0) filteredUserLogArray.forEach(item -> {
			String userTs = "" + date + " " + time; 
			java.sql.Timestamp ts = java.sql.Timestamp.valueOf(userTs);
			if(item.in_out.equals("In") && ts.getTime() > item.timestamp.toSqlTimestamp().getTime() + 14400000) {
				filteredUserLogArray.forEach(element -> {
					if(element.in_out.equals("Out") && (item.id == element.id) && ts.getTime() < element.timestamp.toSqlTimestamp().getTime() + 14400000) {
						System.out.println(id.toString());
						System.out.println(element.toString());
						System.out.println();
						if(!newArray.contains(item)) newArray.add(item);
						if(!newArray.contains(element)) newArray.add(element);
					}
					});
			}
		});
		else return;
		
		filteredUserLogArray = newArray;
		//Add new info to scrollpane and update the panel
		rightPanel.remove(rightScrollPane);
		rightScrollPane = new JScrollPane(renderUserHistory(filteredUserLogArray));
		rightScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
		rightScrollPane.setBackground(Color.WHITE);
		rightScrollPane.setPreferredSize(new Dimension((int) (screenWidth/2.1), screenHeight));
		rightScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		rightPanel.add(rightScrollPane);
		rightPanel.updateUI();
		filterFrame.dispose();
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
		
		JLabel headerType = new JLabel("        Type");
		headerType.setFont(new Font("Serif", Font.PLAIN, 20));
		headerType.setPreferredSize(new Dimension(150, 30));
		headerPanel.add((headerType));
		
		JLabel headerId = new JLabel("       ID");
		headerId.setFont(new Font("Serif", Font.PLAIN, 20));
		headerId.setPreferredSize(new Dimension(100, 30));
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
			type.setPreferredSize(new Dimension(100, 30));
			userPanel.add(type);
			
			JLabel id = new JLabel(((Integer.toString(user.id))));
			id.setFont(new Font("Serif", Font.PLAIN, 20));
			id.setPreferredSize(new Dimension(100, 30));
			userPanel.add(id);
			
			JButton noAccess = new JButton("Click to enable access");
			JButton hasAccess = new JButton("Click to disable access"); 
			
			hasAccess.setBackground(Color.RED);
			hasAccess.setFont(new Font("Serif", Font.PLAIN, 15));
			
			//Set listener for button
			hasAccess.addActionListener(e -> {
				try {
					//Toggle the access
					toggleUserAccess(user.id, false); 
					//Switch the button and update the UI
					userPanel.remove(hasAccess);
					userPanel.add(noAccess);
					userPanel.updateUI();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
			
			noAccess.setBackground(Color.GREEN);
			noAccess.setFont(new Font("Serif", Font.PLAIN, 15));
			
			//Set listener for button
			noAccess.addActionListener(e -> {
				try {
					//Toggle the access
					toggleUserAccess(user.id, true);
					//Switch the button and update the UI
					userPanel.remove(noAccess); 
					userPanel.add(hasAccess); 
					userPanel.updateUI();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
			
			if(user.access) userPanel.add(hasAccess); 
			else userPanel.add(noAccess);	
			
			userListPanel.add(userPanel);
		}
		
		return userListPanel;
	}
	
	//Toggles the user's access button
	private static void toggleUserAccess(int id, boolean access) throws Exception{
		String docId = null;

		ApiFuture<QuerySnapshot> query = db.collection("Users").get();
		long lId = Integer.toUnsignedLong(id);
		
		try {
		    // Get the query results
		    QuerySnapshot querySnapshot = query.get();
		    for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
		        if (document.get("id").equals(lId)) {
		        	//Find the document where the id is stored at
		            docId = document.getId();
		        }
		    }
		} catch (InterruptedException | ExecutionException e) {
		    e.printStackTrace();
		}
		
		DocumentReference documentRef = db.collection("Users").document(docId);
		// Create a WriteBatch to perform the update
		WriteBatch batch = db.batch();
		// Update the "access" field to a new value
		batch.update(documentRef, "access", access);
		
		try {
		    // Commit the batch to apply the changes
		    ApiFuture<List<WriteResult>> future = batch.commit();
		    future.get(); // Wait for the operation to complete
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}

	//Render the list of user logs
	public static JPanel renderUserHistory(ArrayList<UserLog> userLogArray){
		
		JPanel userListPanel = new JPanel(new FlowLayout());	
		userListPanel.setBackground(Color.white);
		userListPanel.setPreferredSize(new Dimension((int) (screenWidth/2.1), screenHeight+150));	
		
		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		headerPanel.setBackground(Color.white);
		
		JLabel headerName = new JLabel("    Name");
		headerName.setFont(new Font("Serif", Font.PLAIN, 20));
		headerName.setPreferredSize(new Dimension(150, 30));
		headerPanel.add(headerName);
		
		JLabel headerType = new JLabel("   In/Out");
		headerType.setFont(new Font("Serif", Font.PLAIN, 20));
		headerType.setPreferredSize(new Dimension(100, 30));
		headerPanel.add((headerType));
		
		JLabel headerId = new JLabel("   ID");
		headerId.setFont(new Font("Serif", Font.PLAIN, 20));
		headerId.setPreferredSize(new Dimension(100, 30));
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
			name.setPreferredSize(new Dimension(150, 30));
			userPanel.add(name);
			
			JLabel in_out = new JLabel(log.in_out);
			in_out.setFont(new Font("Serif", Font.PLAIN, 20));
			in_out.setPreferredSize(new Dimension(50, 30));
			userPanel.add((in_out));
			
			JLabel id = new JLabel(((Integer.toString(log.id))));
			id.setFont(new Font("Serif", Font.PLAIN, 20));
			id.setPreferredSize(new Dimension(100, 30));
			userPanel.add((id));
			
			JLabel ts = new JLabel((log.timestamp).toString().substring(0, 19));
			ts.setFont(new Font("Serif", Font.PLAIN, 20));
			ts.setPreferredSize(new Dimension(200, 30));
			userPanel.add((ts));
			
			userListPanel.add(userPanel);
		}

		return userListPanel;
	}

	//Get list of users from DB
	@SuppressWarnings("deprecation")
	public static ArrayList<User> getUsers(Firestore db) throws Exception{
		
		// Create arraylist to be populated
		ArrayList<User> userList = new ArrayList<User>();

		// asynchronously retrieve all documents
		ApiFuture<QuerySnapshot> future = db.collection("Users").get();
		future.get().getDocuments().forEach(item -> {
			userList.add(item.toObject(User.class));
		});

		return userList;
	}
	
	//Get list of users logs from DB
	public static ArrayList<UserLog> getUserHistory(Firestore db) throws Exception{
		
		//Create arraylist to be populated
		ArrayList<UserLog> userLogList = new ArrayList<UserLog>();

		// asynchronously retrieve all documents
		ApiFuture<QuerySnapshot> future = db.collection("Access").get();
		future.get().getDocuments().forEach(item -> {
			userLogList.add(item.toObject(UserLog.class));
		});

		return userLogList;
	}

		@SuppressWarnings("deprecation")
	private static Firestore initializeDb() throws Exception {
		// Use a service account to connect to DB
		FileInputStream serviceAccount = new FileInputStream("wproject1-c76c6-firebase-adminsdk-mtqki-9b61264bd2.json");
		GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
		FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials).build();
		FirebaseApp.initializeApp(options);
		Firestore db = FirestoreClient.getFirestore();
		return db;
	}

}
