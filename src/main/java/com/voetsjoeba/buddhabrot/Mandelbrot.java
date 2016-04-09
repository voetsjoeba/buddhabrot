package com.voetsjoeba.buddhabrot;

/**
 * Implements the Mandelbrot function
 * 
 * @author Jeroen De Ridder
 */
public class Mandelbrot {
	
	/**
	 * Calculates the mandelbrot function z -> z*z + c, where c is a complex parameter. The result is written back to z.
	 * 
	 * @param z
	 * @param c
	 */
	public static void mandelbrot(ComplexNumber z, ComplexNumber c){
		
		// --- buddhabrot ---
		z.multiply(z).add(c);
		
		
		// --- mountainbeast ---
		/*z.setReal((z.getReal()*z.getReal() - z.getImaginary()*z.getImaginary()) + c.getReal());
		z.setImaginary((2*z.getReal()*z.getImaginary()) + c.getImaginary());*/
		
		
		// --- diablo ---
		/*z.setReal((z.getReal()*z.getReal() - z.getImaginary()*z.getImaginary()) + c.getReal());
		z.setImaginary((3*z.getReal()*z.getImaginary()) + c.getImaginary());*/
		
		
		// --- mountain ---
		/*double real = (z.getReal()*z.getReal() - z.getImaginary()*z.getImaginary()) + c.getReal();
		double im = (2*z.getReal()*z.getImaginary()) + c.getImaginary();
		
		z.setReal(real);
		z.setImaginary(im);
		
		z.setReal(z.getReal()*c.getReal() - z.getImaginary()*c.getImaginary());
		z.setImaginary(z.getReal()*c.getImaginary() + z.getImaginary()*c.getReal());
		
		z.setReal(z.getReal()+c.getReal());
		z.setImaginary(z.getImaginary()+c.getImaginary());*/
		
		
		// --- double z ---
		/*z.multiply(z).add(c);
		
		z.setReal(z.getReal()*z.getReal() - z.getImaginary()*z.getImaginary());
		z.setImaginary(z.getReal()*z.getImaginary() + z.getImaginary()*z.getReal());
		
		z.setReal(z.getReal()+c.getReal());
		z.setImaginary(z.getImaginary()+c.getImaginary());*/
		
		
		// --- butterfly ---
		/*z.multiply(z).add(c);
		
		z.setReal(z.getReal()*z.getReal() - z.getImaginary()*z.getImaginary());
		z.setImaginary(z.getReal()*z.getImaginary() + z.getImaginary()*z.getReal());
		
		z.setReal(z.getReal()*z.getReal()+c.getReal());
		z.setImaginary(z.getImaginary()+c.getImaginary());*/
		
		
		// --- control ---
		/*z.multiply(z).add(c);
		
		z.setReal(z.getReal()*z.getReal() - z.getImaginary()*z.getImaginary());
		z.setImaginary(z.getReal()*z.getImaginary() + z.getImaginary()*z.getReal());
		
		z.setReal(z.getReal()*z.getReal()*z.getReal()+c.getReal());
		z.setImaginary(z.getImaginary()*z.getImaginary()*z.getImaginary()+c.getImaginary());*/
		
		
		// --- arch ---
		/*z.setReal((z.getReal()*z.getReal()*z.getReal() - z.getImaginary()*z.getImaginary()) + c.getReal());
		z.setImaginary((z.getReal()*z.getReal()*z.getImaginary()) + c.getImaginary());*/
		
		
		// --- flower ---
		//z.multiply(z).multiply(z).multiply(z).add(c);
		
		
		// --- mothership ---
		/*z.add(c);
		z.multiply(z);
		z.add(c);*/
		
		
		// --- sighting ---
		/*z.add(c);
		z.multiply(z).multiply(z);
		z.add(c);*/
		
	}
	
}
