package com.voetsjoeba.buddhabrot;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class Buddhabrot {
	
	public static void main(String[] args){
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e){
			// use default Swing L&F
		}
		
		JFrame frame = new BuddhabrotFrame("Buddhabrot");
		frame.pack();
		frame.setVisible(true);
		
	}
	
}
