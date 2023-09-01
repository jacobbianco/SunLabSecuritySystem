import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.FileInputStream;
import java.util.concurrent.ExecutionException;
import javax.swing.*;

import com.google.firebase.cloud.FirestoreClient;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;


public class GUI {
	
	static int screenWidth;
	static int screenHeight;
	
	static Firestore db;
	
	static String userType;
	
	//Create log in panel and student view, then done 
	
	
	public static void main (String args[]) throws Exception {
		//InitialiW DB
		db = initializeDb();
		
		userType = "";
		
		// Set screen width and height
		screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
		screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
		
		// Create frame
		JFrame frame = new JFrame();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setLayout(new BorderLayout(25, 25));
		frame.setTitle("Sun Lab Security System");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.WHITE);
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		//Create log in panel
		JPanel logInPanel = new JPanel(new GridLayout(3, 1));

		JLabel lblPIN = new JLabel("Please enter your 9 Digit ID");
		lblPIN.setFont(new Font("Serif", Font.BOLD, 18));

		JTextField txtPIN = new JTextField(10);
		txtPIN.setPreferredSize(new Dimension(50, 50));
		txtPIN.setFont(new Font("Serif", Font.BOLD, 25));

		JButton cmdSubmit = new JButton("Submit");
		cmdSubmit.setPreferredSize(new Dimension(100, 50));
		cmdSubmit.setFont(new Font("Serif", Font.BOLD, 18));
		cmdSubmit.addActionListener(e -> {
			if(txtPIN.getText().equals("") || txtPIN.getText().length() != 9 ) txtPIN.setText("Invalid ID");
			int query = Integer.parseInt(txtPIN.getText().stripIndent());
			userType = searchId(query);
			if(userType.equals("Professor") || userType.equals("Administrator")) {
			frame.remove(logInPanel);
			frame.add(new AdminView(db));
			frame.setVisible(true);
			}
			if(userType.equals("Student")) {
				frame.remove(logInPanel);
				frame.add(new StudentView(db));
				frame.setVisible(true);
			}
			else txtPIN.setText("Invalid ID");
		});
		
		JPanel centerPanel = new JPanel();
		centerPanel.add(lblPIN);
		centerPanel.add(txtPIN);
		centerPanel.add(cmdSubmit);
		centerPanel.setBackground(Color.WHITE);
		
		JPanel fillerPanel1 = new JPanel();
		fillerPanel1.setBackground(Color.WHITE);
		JPanel fillerPanel2 = new JPanel();
		fillerPanel2.setBackground(Color.WHITE);
		
		logInPanel.add(fillerPanel1);
		logInPanel.add(centerPanel);
		logInPanel.add(fillerPanel2);
		
		frame.add(logInPanel);
		frame.setVisible(true);
		
		
		
		
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
	
	
	//Searches for id in DB
	private static String searchId(int id) {

		ApiFuture<QuerySnapshot> query = db.collection("Users").get();
		long lId = Integer.toUnsignedLong(id);
		
		try {
		    // Get the query results
		    QuerySnapshot querySnapshot = query.get();
		    for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) 
		        if (document.get("id").equals(lId)) return (String) document.get("type");
		} catch (InterruptedException | ExecutionException e) {
		    e.printStackTrace();
		}
		return "";
	}
}