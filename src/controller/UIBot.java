package controller;

import java.awt.Color;
import java.awt.Font;

import javax.swing.UIManager;
import javax.swing.border.LineBorder;

/**
 * A static class that controls the UI constants of the application
 * @author Me
 */
class UIBot {
	
	private static final Font mainFont = new Font("SansSerif", Font.BOLD, 12);

	protected static final Color FGColor1 = new Color(250,255,234); // Pale Green	
	
	protected static final Color BGColor1 = new Color(0,0,0); // Black
	protected static final Color BGColor2 = new Color(30,18,9); // Dark Brown

	protected static final Color BorderColor = Color.DARK_GRAY;
	
	protected static final LineBorder ButtonBorder = new LineBorder(BorderColor, 2);
	
	protected static void initUIDefaults() {
		try {
			UIManager.setLookAndFeel(UIManager.getLookAndFeel());
		} catch (Exception e) {
			System.out.println("Error loading look and feel");
			e.printStackTrace();
			return;
		}
		
		//JPanel
		UIManager.put("Panel.background", BGColor1);
		
		//JLabel
		UIManager.put("Label.font", mainFont);
		UIManager.put("Label.foreground", FGColor1);
		
		//JSlider
		UIManager.put("Slider.background", BGColor1);
		
		//Button
		UIManager.put("Button.background", BGColor2);
		UIManager.put("Button.foreground", FGColor1);
	}
	
//	private static Font bigger(Font f, int amount) {
//		return new Font(f.getFamily(),f.getSize()+amount,f.getStyle());
//	}
//	
//	private static Color shader(Color c, int amount) {
//		float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
//		hsb[2] += amount * 0.1f;
//		return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
//	}
//	
//	private static Color saturator(Color c, int amount) {
//		float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
//		hsb[1] += amount * 0.1f;
//		return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
//	}
//	
//	private static Color huer(Color c, int amount) {
//		float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
//		hsb[0] += amount * 0.1f;
//		return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
//	}
	
//	private int[] hslToRgb(h, s, l){
//	    var r, g, b;
//
//	    if(s == 0){
//	        r = g = b = l; // achromatic
//	    }else{
//	        var hue2rgb = function hue2rgb(p, q, t){
//	            if(t < 0) t += 1;
//	            if(t > 1) t -= 1;
//	            if(t < 1/6) return p + (q - p) * 6 * t;
//	            if(t < 1/2) return q;
//	            if(t < 2/3) return p + (q - p) * (2/3 - t) * 6;
//	            return p;
//	        }
//
//	        var q = l < 0.5 ? l * (1 + s) : l + s - l * s;
//	        var p = 2 * l - q;
//	        r = hue2rgb(p, q, h + 1/3);
//	        g = hue2rgb(p, q, h);
//	        b = hue2rgb(p, q, h - 1/3);
//	    }
//
//	    return [Math.round(r * 255), Math.round(g * 255), Math.round(b * 255)];
//	}
//	
//	private int[] rgbToHsl(r, g, b){
//	    r /= 255, g /= 255, b /= 255;
//	    var max = Math.max(r, g, b), min = Math.min(r, g, b);
//	    var h, s, l = (max + min) / 2;
//
//	    if(max == min){
//	        h = s = 0; // achromatic
//	    }else{
//	        var d = max - min;
//	        s = l > 0.5 ? d / (2 - max - min) : d / (max + min);
//	        switch(max){
//	            case r: h = (g - b) / d + (g < b ? 6 : 0); break;
//	            case g: h = (b - r) / d + 2; break;
//	            case b: h = (r - g) / d + 4; break;
//	        }
//	        h /= 6;
//	    }
//
//	    return [h, s, l];
//	}
	
}
