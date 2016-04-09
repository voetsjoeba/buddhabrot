package com.voetsjoeba.buddhabrot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class MandelbrotPanel extends JPanel {
	
	private int width;
	private int height;
	
	private int unitReal; // amount of image pixels that correspond to unity length on the Re axis
	private int unitImaginary; // amount of image pixels that correspond to unity length on the Im axis
	private int originX; // origin of the Re/Im axes in terms of image x-coordinates
	private int originY; // origin of the Re/Im axes in terms of image y-coordinates
	
	private boolean drawAxis = false;
	
	private int[][] count;
	private BufferedImage image;
	
	public MandelbrotPanel(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		init();
	}
	
	private void init(){
		
		count = new int[height][width];
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		// Re axis runs vertically down the middle of the image; -2 at the image top, 1 at the image bottom
		// Im axis runs horizontally 2/3rds down from the top; -1 at the image left, 1 at the image right
		unitReal = height/3;
		unitImaginary = width/2;
		originY = 2*height/3;     // 2/3rds down, in the horizontal center
		originX = unitImaginary;  //
		
		Graphics imageGraphics = image.getGraphics();
		imageGraphics.setColor(Color.BLACK);
		imageGraphics.fillRect(0, 0, width, height);
		
		setPreferredSize(new Dimension(width, height));
		addMouseListener(new MandelbrotPanelMouseListener());
		
		toggleAxis();
		
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
		x = Math.max(0, Math.min(width-1, x));
		y = Math.max(0, Math.min(height-1, y));
		return new Point(x, y);
	}
	
	protected void iteratePixel(int x, int y){
		
		ComplexNumber c = pixelToComplexNumber(x, y);
		ComplexNumber z = new ComplexNumber(0, 0);
		
		long iterations = 0;
		long maxIterations = 1000;
		
		while(z.getReal()*z.getReal() + z.getImaginary()*z.getImaginary() < 4.0 && iterations < maxIterations){
			
			double newReal = (z.getReal()*z.getReal() - z.getImaginary()*z.getImaginary()) + c.getReal();
			double newImaginary = (2*z.getReal()*z.getImaginary()) + c.getImaginary();
			z.setReal(newReal);
			z.setImaginary(newImaginary);
			
			//Point pixel = complexNumberToPixel(z);
			//image.setRGB(pixel.x, pixel.y, 0xFFFFFFFF);
			//count[pixel.y][pixel.x]++;
			
			iterations++;
			
		}
		
		if(iterations == maxIterations){
			// in mandelbrot set
			image.setRGB(x, y, 0xFFFFFFFF);
		} else {
			// not in mandelbrot set
			image.setRGB(x, y, 0xFF203344);
		}
		
		repaint();
		
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
	
	private class MandelbrotPanelMouseListener extends MouseAdapter {
		
		public void mousePressed(MouseEvent e){
			
			Runnable r = new Runnable(){
				public void run(){
					
					for(int i=0; i<height; i++){
						for(int j=0; j<width; j++){
							iteratePixel(j, i);
						}
					}
					
				}
			};
			new Thread(r).start();
			//iteratePixel(e.getX(), e.getY());
		}
		
	}
	
}
