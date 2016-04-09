package com.voetsjoeba.buddhabrot;

/**
 * Complex number.
 * 
 * @author Jeroen De Ridder
 */
public class ComplexNumber {
	
	private double real;
	private double im;
	
	public static final ComplexNumber ZERO = new ComplexNumber(0,0);
	public static final ComplexNumber ONE = new ComplexNumber(1,0);
	
	public ComplexNumber(ComplexNumber c){
		this.real = c.getReal();
		this.im = c.getImaginary();
	}
	
	public ComplexNumber(double real, double im){
		this.real = real;
		this.im = im;
	}
	
	public ComplexNumber add(double real, double im){
		this.real += real;
		this.im += im;
		return this;
	}
	
	public ComplexNumber add(int real, int im){
		return add((double) real, (double) im);
	}
	
	public ComplexNumber add(ComplexNumber c){
		return add(c.getReal(), c.getImaginary());
	}
	
	public ComplexNumber multiply(ComplexNumber c){
		double newReal = real*c.getReal() - im*c.getImaginary();
		double newIm = real*c.getImaginary() + im*c.getReal();
		this.real = newReal;
		this.im = newIm;
		return this;
	}
	
	public ComplexNumber multiply(double by){
		this.real *= by;
		this.im *= by;
		return this;
	}

	public double getReal() {
		return real;
	}

	public double getImaginary() {
		return im;
	}

	public void setReal(double real) {
		this.real = real;
	}

	public void setImaginary(double im) {
		this.im = im;
	}
	
	public String toString(){
		return "("+real+","+im+")";
	}
	
}
