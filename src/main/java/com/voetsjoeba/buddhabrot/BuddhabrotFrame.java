package com.voetsjoeba.buddhabrot;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import com.voetsjoeba.buddhabrot.io.DataIO;

public class BuddhabrotFrame extends JFrame implements WindowListener, ComponentListener {
	
	private JButton toggleAxisButton;
	private JProgressBar progressBar;
	private JPanel controlsPanel;
	private BuddhabrotPanel buddhaPanel;
	private JButton takeScreenshotButton;
	private JButton writeDataButton;
	private JButton loadDataButton;
	
	private Container container;
	
	public BuddhabrotFrame(String title) throws HeadlessException {
		super(title);
		init();
	}
	
	private void init(){
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		addWindowListener(this);
		addComponentListener(this);
		
		container = getContentPane();
		container.setLayout(new BorderLayout());
		
		controlsPanel = new JPanel();
		progressBar = new JProgressBar(0, 100);
		buddhaPanel = new BuddhabrotPanel(1500, 1050, 350, 375, progressBar);
		
		
		/* ----------------------------------------- */
		
		controlsPanel.setLayout(new FlowLayout());
		
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		progressBar.setStringPainted(true);
		progressBar.setPreferredSize(new Dimension(progressBar.getPreferredSize().width*2, progressBar.getPreferredSize().height));
		
		toggleAxisButton = new JButton("Toggle Axis");
		toggleAxisButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				buddhaPanel.toggleAxis();
			}
		});
		
		takeScreenshotButton = new JButton("Take screenshot");
		takeScreenshotButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				buddhaPanel.takeScreenshot();
			}
		});
		
		writeDataButton = new JButton("Write data");
		writeDataButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				buddhaPanel.saveData();
			}
		});
		
		loadDataButton = new JButton("Load data");
		loadDataButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				loadData();
			}
		});
		
		controlsPanel.add(toggleAxisButton);
		controlsPanel.add(progressBar);
		controlsPanel.add(takeScreenshotButton);
		controlsPanel.add(writeDataButton);
		controlsPanel.add(loadDataButton);
		
		/* ------------------------------------------ */
		
		container.add(buddhaPanel, BorderLayout.CENTER);
		container.add(controlsPanel, BorderLayout.SOUTH);
		
	}
	
	public void loadData(){
		
		try {
			
			JFileChooser fileChooser = new JFileChooser(".");
			int chooseFileResult = fileChooser.showOpenDialog(this);
			if(chooseFileResult == JFileChooser.APPROVE_OPTION){
				
				File sourceFile = fileChooser.getSelectedFile();
				final int[][] data = DataIO.readData(sourceFile);
				if(data.length <= 0) return; // no data
				
				//buddhaPanel = new BuddhabrotPanel(data, data[0].length, data.length, progressBar);
				/*EventQueue.invokeAndWait(new Runnable(){
					public void run() {
						container.remove(buddhaPanel);
					}
				});*/
				
				/*container.remove(buddhaPanel);
				buddhaPanel = new BuddhabrotPanel(data, data[0].length, data.length, progressBar);
				
				container.add(buddhaPanel, BorderLayout.CENTER);
				buddhaPanel.repaint();*/
				new Thread(new Runnable(){
					public void run(){
						buddhaPanel.loadData(data);
					}
				}).start();
				
			}
			
		}
		catch(Exception ex){
			
		}
		
	}
	
	public void windowClosed(WindowEvent e) {
		System.exit(0);
	}
	
	public void windowActivated(WindowEvent e) {}
	public void windowClosing(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}

	public void componentResized(ComponentEvent e) {
		
	}
	
	public void componentHidden(ComponentEvent e) {}
	public void componentMoved(ComponentEvent e) {}
	public void componentShown(ComponentEvent e) {}
	
}
