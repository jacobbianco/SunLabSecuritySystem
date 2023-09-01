import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firestore.v1.WriteResult;

public class StudentView extends JPanel {

	public StudentView() {}
	
	public StudentView(Firestore db) {
		
		// Set screen width and height
		int screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
		//Create log in panel
		JPanel swipeInPanel = new JPanel(new GridLayout(3, 1));

		JLabel lblPIN = new JLabel("Please swipe your ID card");
		lblPIN.setFont(new Font("Serif", Font.BOLD, 18));

		JTextField txtPIN = new JTextField(20);
		txtPIN.setPreferredSize(new Dimension(100, 50));
		txtPIN.setFont(new Font("Serif", Font.BOLD, 25));
		
		// Create the radio buttons
        JRadioButton inButton = new JRadioButton("In");
        inButton.setBackground(Color.WHITE);
        inButton.setPreferredSize(new Dimension(60, 50));
        inButton.setFont(new Font("Serif", Font.BOLD, 18));
        JRadioButton outButton = new JRadioButton("Out");
        outButton.setBackground(Color.WHITE);
        outButton.setPreferredSize(new Dimension(60, 50));
        outButton.setFont(new Font("Serif", Font.BOLD, 18));

        // Create a button group to ensure only one can be selected
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(inButton);
        buttonGroup.add(outButton);

		JButton swipe = new JButton("Swipe");
		swipe.setPreferredSize(new Dimension(100, 50));
		swipe.setFont(new Font("Serif", Font.BOLD, 18));
		swipe.addActionListener(e -> {
			if(txtPIN.getText().equals("") || txtPIN.getText().length() < 11) txtPIN.setText("Swipe failed, please try again");
			int query = Integer.parseInt(txtPIN.getText().stripIndent().substring(2, 11));
			boolean success;
			if(inButton.isSelected()) success = logTime(query, db, "In");
			else success = logTime(query, db, "Out");
			if(success) txtPIN.setText("Swipe was a success");
			else txtPIN.setText("Swipe failed, please try again");
		});

		JPanel centerPanel = new JPanel();
		centerPanel.add(lblPIN);
		centerPanel.add(txtPIN);
		centerPanel.add(inButton);
		centerPanel.add(outButton);
		centerPanel.add(swipe);
		centerPanel.setBackground(Color.WHITE);

		JPanel fillerPanel1 = new JPanel();
		fillerPanel1.setBackground(Color.WHITE);
		fillerPanel1.setPreferredSize(new Dimension(screenWidth, screenHeight/3));
		JPanel fillerPanel2 = new JPanel();
		fillerPanel2.setBackground(Color.WHITE);

		swipeInPanel.add(fillerPanel1);
		swipeInPanel.add(centerPanel);
		swipeInPanel.add(fillerPanel2);
		
		setBackground(Color.white);
		add(swipeInPanel);
	}
	//Make login parse input and then i am done

		//Searches for id in DB
		private static boolean logTime(int id, Firestore db, String in_out) {

			ApiFuture<QuerySnapshot> query = db.collection("Users").get();
			long lId = Integer.toUnsignedLong(id);
			boolean hasAccess = false;
			String name = "";
	        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
			
			try {
			    // Get the query results
			    QuerySnapshot querySnapshot = query.get();
			    for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) 
			        if (document.get("id").equals(lId) && document.get("access").equals(true)) {
			        	hasAccess = true;
			        	name = (String) document.get("name");
			        }
			}
			catch(Exception e) { e.printStackTrace();}
			
			if(hasAccess) {
			DocumentReference newDocRef = db.collection("Access").document();
			
			Map<String, Object> data = new HashMap<>();
	        data.put("id", lId);
	        data.put("in_out", in_out);
	        data.put("name", name);
	        data.put("timestamp", currentTimestamp);
	        
			// Add the data to the document
			ApiFuture<com.google.cloud.firestore.WriteResult> writeResult = newDocRef.set(data);

			try {
				// Wait for the write operation to complete
				com.google.cloud.firestore.WriteResult result = writeResult.get();
				System.out.println("Document added with ID: " + newDocRef.getId());
				System.out.println("Write time: " + result.getUpdateTime());
			} catch (InterruptedException | ExecutionException e) {
				System.err.println("Error: " + e.getMessage());
			}
			}
			
			return hasAccess;
		}
	
}
