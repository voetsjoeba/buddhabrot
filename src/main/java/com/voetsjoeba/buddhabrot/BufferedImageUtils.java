package com.voetsjoeba.buddhabrot;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class BufferedImageUtils {
	
	public static void saveAsPng(BufferedImage image, File to) throws Exception {
		ImageIO.write(image, "png", to);
	}
	
}
