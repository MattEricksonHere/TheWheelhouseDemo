package controller;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import vst.VSTPlayer;
import wheelhouse.Calc;
import wheelhouse.PC;
import wheelhouse.harmonics.Dyad;
import wheelhouse.harmonics.Harmonics;
import wheelhouse.tone.Tone;
import wheelhouse.tone.ToneET;
import messaging.Courier;
import messaging.Listener;
import messaging.QueueMessage;

public class HarmonicsPanel extends SubPanel implements Listener {
	private static final long serialVersionUID = 4274790804691407849L;

	private HarmonicsCanvas canvas;

	public HarmonicsPanel() {
		super("Harmonics");
		initPanel();
		Courier.registerListener(this);
	}
	
	private void initPanel() {
		setPreferredSize(new Dimension(1000, 200));
		setLayout(new BorderLayout());
		
		canvas = new HarmonicsCanvas();
		canvas.setBorder(new LineBorder(Color.WHITE));
		add(canvas, BorderLayout.CENTER);
		
	}

	public class HarmonicsCanvas extends JPanel {
		private static final long serialVersionUID = -1397984742508421004L;

		private static final int WIDTH = 300;
		private static final int HEIGHT = 300;
		
		private static final int MINFREQ = 20;
		private static final int MAXFREQ = 20000;
		
		private static final int COLUMNWIDTH = 40;
		private static final int COLUMNGAP = 20;

		private List<Double> tickFrequencies;

		private Harmonics harmonics;
		
		public HarmonicsCanvas() {
			setMinimumSize(new Dimension(WIDTH, HEIGHT));
			setPreferredSize(new Dimension(WIDTH, HEIGHT));
			setMaximumSize(new Dimension(WIDTH, HEIGHT));
			
			harmonics = new Harmonics(new ArrayList<Tone>());
			tickFrequencies = calculateTickFrequencies(new ToneET(PC.C));
		}
		
		public void updateCurrentTones(List<ToneET> tones) {
			harmonics = new Harmonics(tones);
		}

	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        
	        Graphics2D g2 = (Graphics2D) g;
	        paintGrid(g2);
	        
	        paintMatches(g2);
	        
	        g2.setColor(Color.WHITE);
	        int columnStartX = COLUMNGAP;

	        for (int i = 0; i < harmonics.getBaseTones().size(); i++) {
	        	Tone t = harmonics.getBaseTone(i);
	        	int y = getYForFrequency(t.getFrequency());

	        	g2.drawString(t.getSymbolWithOctave(), columnStartX, y - 2);
		        g2.setStroke(new BasicStroke(3));
	        	g2.drawLine(columnStartX, 
	        			y, 
	        			columnStartX + COLUMNWIDTH, 
	        			y);
		        g2.setStroke(new BasicStroke(1));
		        
		        for (Tone partial : harmonics.getPartials(i, false)) {
		        	int yP = getYForFrequency(partial.getFrequency());
		        	g2.drawLine(columnStartX + 4, 
		        			yP, 
		        			columnStartX + COLUMNWIDTH - 4, 
		        			yP);
		        }
		        
	        	columnStartX += COLUMNWIDTH + COLUMNGAP;
	        }
	    }
	    
	    private void paintMatches(Graphics2D g) {
	    	for (Dyad dyad : harmonics.getDyads()) {
	    		if (dyad.getDis() > 0 ) {
		    		g.setColor(getDissDyadColor(dyad));
		    		g.fillRect(0,
		    				getYForFrequency(dyad.getFreq2()),
		    				getWidth(),
		    				Math.abs(getYForFrequency(dyad.getFreq2()) - getYForFrequency(dyad.getFreq1())));
	    		}
	    	}

	    	for (Dyad dyad : harmonics.getDyads()) {
	    		if (dyad.getCon() > 0 ) {
	    		int avgY = getYForFrequency((dyad.getFreq1() + dyad.getFreq2()) / 2.0);
	    		g.setColor(getConsDyadColor(dyad));
	    		g.drawLine(0,
	    				avgY,
	    				getWidth(),
	    				avgY);
	    		glowLine(0, avgY, getWidth(), avgY, (int)(10.0 * dyad.getCon()), dyad.getCon(), g);
	    		}
	    	}
	    }
	    
	    private Color getDissDyadColor(Dyad dyad) {
	    	return new Color(1.0f, 0, 0, (float)dyad.getDis());
	    }
	    
	    private Color getConsDyadColor(Dyad dyad) {
	    	return new Color(38f/255f, 220f/255f, 255f/255f, (float)dyad.getCon());
	    }
	    
	    private List<Double> calculateTickFrequencies(Tone tickRef) {
	    	Double refFreq = tickRef.getFrequency();
	    	List<Double> tickFreqs = new ArrayList<Double>();
	    	tickFreqs.add(refFreq);
	    	for (int i = 1; refFreq * Math.pow(2, i) < MAXFREQ; i++)
		    	tickFreqs.add(refFreq * Math.pow(2, i));
	    	for (int i = 1; refFreq / Math.pow(2, i) > MINFREQ; i++)
		    	tickFreqs.add(refFreq / Math.pow(2, i));
	    	
	    	Collections.sort(tickFreqs);
	    	
	    	return tickFreqs;
	    }
	    
	    private void paintGrid(Graphics2D g) {
	    	for (Double freq : tickFrequencies) {
	    		int freqY = getYForFrequency(freq);
	    		g.drawLine(0, freqY, getWidth(), freqY);
	    		g.drawString(Tone.getSymbolWithOctaveForCustomFrequency(freq) + "=" + Calc.round(freq, 2), 4, freqY - 1);
	    	}
	    }
	    	
	    private int getYForFrequency(double frequency) {
	    	double max = Calc.log2(MAXFREQ);
	    	double min = Calc.log2(MINFREQ);
	    	double logFreq = Calc.log2(frequency);
	    	
	    	return (int)(getHeight() * ((max - logFreq) / (max - min)));
	    }
	}
	
	private void updateNotes() {
		List<ToneET> notes = VSTPlayer.getInstance().getCurrentTones();
		canvas.updateCurrentTones(notes);
//		if (notes != null && notes.size() > 1)
//			System.out.println(notes.get(0).getSymbol() + ChordPattern.getPatternFromTones(notes.get(0), notes).getSymbol());
		canvas.repaint();
		canvas.revalidate();
	}
	
	public static BufferedImage getSnapshot(List<ToneET> tones) {
		HarmonicsPanel p = new HarmonicsPanel();
		HarmonicsCanvas canvas = p.new HarmonicsCanvas();
		canvas.setSize(1000, 2000);
		canvas.setBackground(Color.BLACK);
		canvas.updateCurrentTones(tones);
		canvas.repaint();
		canvas.revalidate();
	    BufferedImage imagebuf = null;
	    try {
	        imagebuf = new Robot().createScreenCapture(canvas.getBounds());
	    } catch (AWTException e1) {
	        e1.printStackTrace();
	    }  
	    Graphics2D graphics2D = imagebuf.createGraphics();
	    canvas.paint(graphics2D);
	    return imagebuf;
	}

	@Override
	public List<Integer> acceptedMessageTypes() {
		List<Integer> types = new ArrayList<Integer>();
		types.add(QueueMessage.CURRENTLY_PLAYED_NOTES_CHANGED);
		return types;
	}

	@Override
	public boolean receiveMessage(QueueMessage msg) {
		if (msg.type == QueueMessage.CURRENTLY_PLAYED_NOTES_CHANGED) {
			updateNotes();
			return true;
		}
		return false;
	}
    
    private static void glowLine(int x1, int y1, int x2, int y2, int distance, double intensity, Graphics g) {
		Color currentColor = g.getColor();
    	for (int i = 1; i <= distance; i++) {
    		int opacity = (int)(255 * intensity * (((distance + 1) - i) / (double)distance));
    		g.setColor(new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), opacity));
    		if (x2 - x1 > y2 - y1) {
    			g.drawLine(x1, y1+i, x2, y2+i);
    			g.drawLine(x1, y1-i, x2, y2-i);
    		} else {
    			g.drawLine(x1+i, y1, x2+i, y2);
    			g.drawLine(x1-i, y1, x2-i, y2);
    		}
    	}
		g.setColor(currentColor);
    }
}
