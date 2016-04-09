package com.voetsjoeba.buddhabrot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.voetsjoeba.buddhabrot.io.DataIO;

public class BuddhabrotPanel extends JPanel {
	
	private int width;
	private int height;
	
	private int unitReal; // amount of image pixels that correspond to unity length on the Re axis
	private int unitImaginary; // amount of image pixels that correspond to unity length on the Im axis
	private int originX; // origin of the Re/Im axes in terms of image x-coordinates
	private int originY; // origin of the Re/Im axes in terms of image y-coordinates
	
	private boolean drawAxis = false;
	private static final Random random = new Random();
	
	private int[][] count;
	private BufferedImage image;
	private JProgressBar progressBar;
	
	private double accuracy = 0.0005;
	private long maxIterations = 100;
	private boolean drawing = false;
	private boolean boostLuminance = false;
	private boolean stopDrawingThread = false;
	
	private boolean takeSnapshots = false;
	private int snapshotFrequency = 10;
	
	// bit of reused memory
	private int[] rgb = new int[3];
	
	public BuddhabrotPanel(int width, int height, int unitReal, int unitImaginary, JProgressBar progressBar) {
		
		super();
		
		this.width = width;
		this.height = height;
		this.unitReal = unitReal;
		this.unitImaginary = unitImaginary;
		this.progressBar = progressBar;
		
		init();
		resetImage();
		
	}
	
	public void loadData(int[][] data){
		
		stopDrawing();
		
		int height = data.length;
		if(height <= 0) throw new IllegalArgumentException("Data was empty");
		int width = data[0].length;
		
		this.height = height;
		this.width = width;
		this.count = data;
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		colorImage();
		
	}
	
	private void init(){
		
		// Re axis runs vertically down the middle of the image; -2 at the image top, 1 at the image bottom
		// Im axis runs horizontally 2/3rds down from the top; -1.2 at the image left, 1.2 at the image right
		//unitReal = height/3;
		//unitImaginary = (int)(width/2.4);
		
		originY = 2*height/3;     // 2/3rds down, in the horizontal center
		//originY = height/2;
		originX = width/2;  //
		
		setPreferredSize(new Dimension(width, height));
		addMouseListener(new BuddhabrotPanelMouseListener());
		
		//toggleAxis();
		
	}
	
	/*private void drawLoadedImage(){
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		for(int i=0; i<height; i++){
			for(int j=0; j<width; j++){
				image.setRGB(j, i, getPixelColor(j, i, count[i][j]));
			}
		}
		
	}*/
	
	private void resetImage(){
		
		rgb = new int[3];
		count = new int[height][width];
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		Graphics imageGraphics = image.getGraphics();
		imageGraphics.setColor(Color.BLACK);
		imageGraphics.fillRect(0, 0, width, height);
		
	}
	
	protected void toggleAxis(){
		drawAxis = !drawAxis;
		repaint();
	}
	
	/**
	 * Converts a physical (x,y) pixel-position pair to a complex number
	 */
	protected ComplexNumber pixelToComplexNumber(int x, int y){
		double real = ((double) y - originY)/unitReal;
		double im = ((double) x - originX)/unitImaginary;
		return new ComplexNumber(real, im);
	}
	
	protected Point complexNumberToPixel(ComplexNumber c){
		int x = (int) Math.round(originX + c.getImaginary()*unitImaginary);
		int y = (int) Math.round(originY + c.getReal()*unitReal);
		/*x = Math.max(0, Math.min(width-1, x));
		y = Math.max(0, Math.min(height-1, y));*/
		if(x < 0 || x >= width || y < 0 || y >= height) return null;
		return new Point(x, y);
	}
	
	protected void determineMembership(ComplexNumber c){
		
		//ComplexNumber c = pixelToComplexNumber(x, y);
		ComplexNumber z = new ComplexNumber(0, 0);
		
		long iterations = 0;
		while(z.getReal()*z.getReal() + z.getImaginary()*z.getImaginary() < 4.0 && iterations < maxIterations){
			Mandelbrot.mandelbrot(z, c);
			iterations++;
		}
		
		if(iterations != maxIterations){
			
			// not in mandelbrot set, retrace orbit
			z = new ComplexNumber(0, 0);
			
			iterations = 0;
			while(z.getReal()*z.getReal() + z.getImaginary()*z.getImaginary() < 4.0 && iterations < maxIterations){
				
				Mandelbrot.mandelbrot(z, c);
				
				Point pixel = complexNumberToPixel(z);
				if(pixel != null){
					count[pixel.y][pixel.x]++;
					image.setRGB(pixel.x, pixel.y, getPixelColor(pixel.x, pixel.y, count[pixel.y][pixel.x]));
				}
				
				iterations++;
				
			}
			
			//repaint();
			
		}
		
		/*if(iterations == maxIterations){
			// in mandelbrot set
			//image.setRGB(x, y, 0xFFFFFFFF);
			set[y][x] = true;
		} else {
			// not in mandelbrot set
			//image.setRGB(x, y, 0xFF203344);
		}*/
		
		//image.setRGB(x, y, 0xFF222222);
		//repaint();
		
		//repaint();
		
	}
	
	protected int getPixelColor(int x, int y, int count){
		
		//if(count <= 0) return 0xFF000000;
		//count = (int)( Math.atan(count)/Math.PI*count );
		//count = count*count;
		
		double fraction = Math.min(count/400.0, 1); // deler = arbitrary waarde, bepaalt luminance (hoe lager, hoe shinier)
		if(boostLuminance) fraction = Math.sqrt(fraction);
		
		
		// wit
		//ColorConversion.hsl2rgb(0, 0, (int)(fraction*100), rgb);
		
		// geen idee meer wa dees is, ook iets blauw zo te zien
		// ColorConversion.hsl2rgb(240, 100, (int)(fraction*100), rgb);
		
		// geel-rood
		//ColorConversion.hsl2rgb((int)(fraction*100), 100, (int)(fraction*100), rgb);
		
		// zwart-blauw-paars
		// H: linear from 240 to 300
		// S: quadratic from 0 to 100 and back to 0
		// L: linear from 0 to 100
		//ColorConversion.hsl2rgb(240 + (int)(fraction*60), -(int)(400*fraction*(fraction-1)), (int)(fraction*100), rgb);
		
		// zwart-blauw-lichtblauw
		// H: linear from 240 to 210
		// S: quadratic from 0 to 100 and back to 0 (polynomial -400*x*(x-1) interpolates (0,0), (0.5,100) and (1,0))
		// L: linear from 0 to 100
		ColorConversion.hsl2rgb(240 - (int)(fraction*30), -(int)(400*fraction*(fraction-1)), (int)(fraction*100), rgb);
		
		// blauw
		//ColorConversion.hsl2rgb(250-(int)(fraction*80), 100 - (int)(fraction*91), (int)(fraction*100), rgb);
		
		//return 0xFF000000 + (count << 16) + (count << 8) + (count);
		return 0xFF000000 + (rgb[0] << 16) + (rgb[1] << 8) + (rgb[2]);
		
	}
	
	protected void colorImage(){
		
		// find maximum value of count
		/*int countMax = 0;
		for(int i=0; i<height; i++){
			for(int j=0; j<width; j++){
				countMax = Math.max(count[i][j], countMax);
			}
		}
		log.debug("count max: {}", countMax);*/
		
		for(int i=0; i<height; i++){
			for(int j=0; j<width; j++){
				image.setRGB(j, i, getPixelColor(j, i, count[i][j]));
			}
			repaint();
		}
		
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				progressBar.setValue(progressBar.getMinimum());
			}
		});
		
		/*BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		GaussianFilter filter = new GaussianFilter(1.3f);
		filter.filter(image, image);*/
		
		/*RaysFilter rf = new RaysFilter();
		rf.setAngle((float) Math.PI/4);
		rf.setCentreX(originX);
		rf.setCentreY(originY);
		rf.setStrength(2.0f);
		rf.filter(image, image);*/
		
		//repaint();
		
	}

	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
		
		if(drawAxis){
			g.setColor(Color.GRAY);
			g.drawLine(0, originY, width, originY); // imaginary axis
			g.drawLine(originX, 0, originX, height); // real axis
		}
		
	}
	
	public void stopDrawing(){
		
		stopDrawingThread = true;
		
		// yay spinlock
		while(drawing){
			Thread.yield();
		}
		
	}
	
	public void draw(){
		
		stopDrawing();
		resetImage();
		
		final int totalVerticalSteps = (int) Math.ceil((3.0/accuracy));
		progressBar.setMinimum(0);
		progressBar.setMaximum(totalVerticalSteps);
		progressBar.setValue(progressBar.getMinimum());
		
		Runnable r = new Runnable(){
			public void run() {
				
				drawing = true;
				stopDrawingThread = false;
				
				String imageSeriesName = String.format("%d", System.currentTimeMillis());
				File imageSeriesFolder = new File(imageSeriesName);
				if(takeSnapshots) imageSeriesFolder.mkdir();
				
				double startReal = -((double) originY/unitReal);
				double endReal = ((double) height-originY)/unitReal;
				double startIm = -((double) originX/unitImaginary);
				double endIm = -startIm;
				
				long imageIndex = 0;
				for(double r=startReal; r<endReal; r+=accuracy){
					
					for(double i=startIm; i<endIm; i+=accuracy){
						if(stopDrawingThread) break;
						determineMembership(new ComplexNumber(r, i));
					}
					
					repaint();
					
					EventQueue.invokeLater(new Runnable(){
						public void run(){
							progressBar.setValue(progressBar.getValue()+1);
						}
					});
					
					if(stopDrawingThread) break;
					
					if(takeSnapshots) {
						
						if(imageIndex % snapshotFrequency == 0){
							
							String fileName = String.format("%s_%d.png", imageSeriesName, imageIndex/snapshotFrequency);
							try {
								BufferedImageUtils.saveAsPng(image, new File(imageSeriesFolder, fileName));
							}
							catch(Exception ex){
								
							}
							
						}
						
						imageIndex++;
						
					}
					
				}
				
				drawing = false;
				
			}
		};
		new Thread(r).start();
		
	}
	
	public void takeScreenshot(){
		
		try {
			String filename = String.format("%d.png", System.currentTimeMillis());
			BufferedImageUtils.saveAsPng(image, new File(filename));
		}
		catch(Exception ex){
			ex.printStackTrace(System.err);
		}
		
	}
	
	public void saveData(){
		try {
			String filename = String.format("%d.dat", System.currentTimeMillis());
			DataIO.writeData(count, new File(filename));
		}
		catch(Exception ex){
			ex.printStackTrace(System.err);
		}
	}
	
	public int[][] getData(){
		return count;
	}
	
	private class BuddhabrotPanelMouseListener extends MouseAdapter {
		
		public void mousePressed(MouseEvent e){
			draw();
		}
		
	}
	
}
