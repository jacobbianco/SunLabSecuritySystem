import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.*;

public class GUI {
	public static void main (String args[]) {
		
		//Create frame
		JFrame frame = new JFrame();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    frame.setLayout(new BorderLayout(25, 25));
	    frame.setVisible(true); 
	    frame.setTitle("Sun Lab Security System");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.WHITE);
		
		//Create heading
		JLabel heading = new JLabel ("Sun Lab Security System");
		heading.setFont(new Font("Serif", Font.BOLD, 50));
		
		//Create top panel
		JPanel topPanel = new JPanel ();
		topPanel.setBackground(Color.white);
		topPanel.add(heading, BorderLayout.CENTER);
		
		//Create top panel
		JPanel midPanel = new JPanel (new FlowLayout(FlowLayout.LEFT, 20, 25));
		midPanel.setBackground(Color.lightGray);
		
		//Create left panel and all of it's contents
		JPanel leftPanel = new JPanel (new BorderLayout());
		leftPanel.setBackground(Color.white);
		midPanel.add(leftPanel);
		
		JLabel leftSubheading = new JLabel ("This is the left panel");
		leftSubheading.setFont(new Font("Serif", Font.BOLD, 30));
		leftPanel.add(leftSubheading, BorderLayout.NORTH);
		
		//Create right panel and all of it's contents
		JPanel rightPanel = new JPanel (new BorderLayout());
		rightPanel.setBackground(Color.white);
		midPanel.add(rightPanel);
		
		JLabel rightSubheading = new JLabel ("This is the right panel");
		rightSubheading.setFont(new Font("Serif", Font.BOLD, 30));
		rightPanel.add(rightSubheading, BorderLayout.NORTH);
		
		//Add panels to the frame
		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(midPanel, BorderLayout.CENTER);
		
		
	}
}
