package com.voetsjoeba.buddhabrot;

public class ColorConversion {
	
	/**
	 * 
	 * @param ir in [0,255]
	 * @param ig in [0,255]
	 * @param ib in [0,255]
	 * @param hsl in ([0,360[, [0,100], [0,100])
	 */
	public static void rgb2hsl(int ir, int ig, int ib, int[] hsl){
		
		double fr = (ir/255.0);
		double fg = (ig/255.0);
		double fb = (ib/255.0);
		
		int imax = Math.max(ir, Math.max(ig, ib));
		int imin = Math.min(ir, Math.min(ig, ib));
		double fmax = imax/255.0;
		double fmin = imin/255.0;
		
		double h = 0;
		if(imax != imin){
			
			if(imax == ir){
				h = 60 * (fg - fb)/(fmax - fmin) + 360;
				h = h % 360;
			} else if(imax == ig){
				h = 60 * (fb - fr)/(fmax - fmin) + 120;
			} else if(imax == ib){
				h = 60 * (fr - fg)/(fmax - fmin) + 240;
			}
			
		}
		
		double l = fmin + (fmax - fmin)/2.0;
		
		double s = 0;
		if(imax != imin){
			
			if(imax + imin <= 255){
				s = (fmax - fmin)/(fmax + fmin);
			} else {
				s = (fmax - fmin)/(2 - (fmax + fmin));
			}
			
		}
		
		hsl[0] = (int) h;
		hsl[1] = (int)(s*100);
		hsl[2] = (int)(l*100);
		
	}
	
	/**
	 * @param ih in [0,360[
	 * @param is in [0,100]
	 * @param il in [0,100]
	 * @param rgb in ([0,255], [0,255], [0,255])
	 */
	public static void hsl2rgb(int ih, int is, int il, int[] rgb){
		
		double fs = is/100.0;
		double fl = il/100.0;
		double fh = ih/360.0; // h normalized to [0,1[
		
		double q = 0;
		if(il < 50){
			q = fl * (1+fs);
		} else {
			q = fl + fs - (fl*fs);
		}
		
		double p = 2*fl - q;
		
		double tr = fh + 1/3.0;
		double tg = fh;
		double tb = fh - 1/3.0;
		
		tr = normalizeTc(tr);
		tg = normalizeTc(tg);
		tb = normalizeTc(tb);
		
		double fr = calcFc(p, q, tr);
		double fg = calcFc(p, q, tg);
		double fb = calcFc(p, q, tb);
		
		rgb[0] = (int) Math.round(fr*255);
		rgb[1] = (int) Math.round(fg*255);
		rgb[2] = (int) Math.round(fb*255);
		
	}
	
	private static double normalizeTc(double tc){
		double r = tc;
		if(r < 0) r += 1.0;
		if(r > 1) r -= 1.0;
		return r;
	}
	
	/**
	 * Calculate floating color component (Color_C @ http://en.wikipedia.org/wiki/HSL_color_space)
	 */
	private static double calcFc(double p, double q, double tc){
		
		if(tc < 1/6.0){
			return p + ((q - p) * 6.0 * tc);
		} else if(tc < 1/2.0){
			return q;
		} else if(tc < 2/3.0){
			return p + ((q - p) * 6.0 * (2/3.0 - tc));
		} else {
			return p;
		}
		
	}
}
