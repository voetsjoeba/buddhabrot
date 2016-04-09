package com.voetsjoeba.buddhabrot.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataIO {
	
	public static void writeData(int[][] data, File to) throws IOException{
		
		int height = data.length;
		if(height <= 0) throw new IllegalArgumentException("Data array was empty");
		int width = data[0].length;
		
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(to)));
		out.writeInt(width);
		out.writeInt(height);
		out.flush();
		
		for(int i=0; i<height; i++){
			for(int j=0; j<width; j++){
				out.writeInt(data[i][j]);
			}
		}
		
		out.flush();
		out.close();
		
	}
	
	public static int[][] readData(File from) throws IOException{
		
		DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(from)));
		
		int width = in.readInt();
		int height = in.readInt();
		int[][] data = new int[height][width];
		
		for(int i=0; i<height; i++){
			for(int j=0; j<width; j++){
				data[i][j] = in.readInt();
			}
		}
		
		return data;
		
	}
	
}
