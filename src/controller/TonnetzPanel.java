package controller;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import vst.VSTPlayer;
import wheelhouse.PC;
import wheelhouse.tone.ToneET;
import messaging.Courier;
import messaging.Listener;
import messaging.QueueMessage;

/**
 * A panel of the application which displays a tonnetz diagram
 * @author Me
 */
public class TonnetzPanel extends SubPanel implements Listener {
	private static final long serialVersionUID = -234281027530522640L;

	private TonnetzCanvas canvas;

	public TonnetzPanel() {
		super("Tonnetz");
		initPanel();
		Courier.registerListener(this);
	}
	
	/**
	 * Initializes the UI of the panel
	 */
	private void initPanel() {
		
		setPreferredSize(new Dimension(1000, 200));
		setLayout(new BorderLayout());
		
		canvas = new TonnetzCanvas();
		canvas.setBorder(new LineBorder(Color.WHITE));
		add(canvas, BorderLayout.CENTER);
		
	}
	
	/**
	 * Updates the notes currently being played
	 */
	private void updateNotes() {
		canvas.updateCurrentTones(VSTPlayer.getInstance().getCurrentTones());
		canvas.repaint();
		canvas.revalidate();
	}

	/**
	 * The subpanel on which the tonnetz will be painted
	 * @author Me
	 */
	public class TonnetzCanvas extends JPanel {
		private static final long serialVersionUID = -1397984742508421004L;

		private static final int WIDTH = 300;
		private static final int HEIGHT = 300;
		
		private final int UNIT = 100;
		private final double GRIDWIDTH = ((double)UNIT * Math.sqrt(3)) / 2;
		private final int nodeRadius = 18;
		private PC originPC = PC.F;
		
		private TonnetzNode[][] nodes;
		private boolean firstRowIndented = false;
		
		public TonnetzCanvas() {
			setMinimumSize(new Dimension(WIDTH, HEIGHT));
			setPreferredSize(new Dimension(WIDTH, HEIGHT));
			setMaximumSize(new Dimension(WIDTH, HEIGHT));
		}
		
		/**
		 * Generates a set of tonnetz nodes
		 * @param rows number of rows in the diagram
		 * @param columns number of columns in the diagram
		 * @return the 2 dimensional array of nodes
		 */
		public TonnetzNode[][] generateNodes(int rows, int columns) {
			
			TonnetzNode[][] nodes = new TonnetzNode[rows][columns];
			
			PC firstInRow = originPC;
			for (int row = 0; row < rows; row++) {

				// Determine first in the row
				firstInRow = (firstRowIndented ^ row % 2 != 0) ? firstInRow.up(4) : firstInRow.down(3);
				PC current = firstInRow;
					
				for (int column = 0; column < columns; column++) {
					nodes[row][column] = new TonnetzNode(current);
					current = current.up(7);
				}
			}
			
			return nodes;
		}
		
		/**
		 * Updates the list of currently played notes
		 * @param tones the currently played tones
		 */
		public void updateCurrentTones(List<ToneET> tones) {
			List<PC> pcs = ToneET.toPitchClasses(tones);
	        
	        for (int row = 0; row < nodes.length; row++) {
		        for (int column = 0; column < nodes[row].length; column++) {
		        	nodes[row][column].on = pcs.contains(nodes[row][column].pc);
		        }
			}
		}

	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        
	        Graphics2D g2 = (Graphics2D) g;
	        paintTonnetz(g2);
	        
	    }
	    
	    /** Paints the grid of tonnetz nodes
	     * @param g the Graphics2D object
	     */
	    private void paintTonnetz(Graphics2D g) {
	    	
	    	if (nodes == null) {
				nodes = generateNodes((int)(getHeight() / GRIDWIDTH) + 1, getWidth() / UNIT + 1);
	    	}

	        g.setColor(Color.WHITE);
	        g.setStroke(new BasicStroke(2));
			int y = 0;
			
			while (y < getHeight()) {
		    	g.drawLine(0, y, getWidth(), y);
		    	y += GRIDWIDTH;
			}

			y = 0;
			while (y < getHeight()) {
		    	g.drawLine(0, y, getWidth(), y + (int)(getWidth() * Math.tan(Math.PI/3.0)));
		    	y += 2*GRIDWIDTH;
			}
			
			int x = UNIT;
			while (x < getWidth()) {
		    	g.drawLine(x, 0, getWidth(), (int)((getWidth() - x) * Math.tan(Math.PI/3.0)));
		    	x += UNIT;
			}

			y = (int) (getHeight() - GRIDWIDTH);
			y = 0;
			while (y < getHeight()) {
		    	g.drawLine(0, y, getWidth(), y - (int)(getWidth() * Math.tan(Math.PI/3.0)));
		    	y += 2*GRIDWIDTH;
			}
			
			x = (int)((y - getHeight()) * Math.pow(Math.tan(Math.PI/3.0), -1));
			while (x < getWidth()) {
		    	g.drawLine(x, getHeight(), getWidth(), getHeight() - (int)((getWidth() - x) * Math.tan(Math.PI/3.0)));
		    	x += UNIT;
			}

	        g.setColor(Color.WHITE);
	        g.setStroke(new BasicStroke(1));
	        
	        for (int row = 0; row < nodes.length; row++) {
		        for (int column = 0; column < nodes[row].length; column++) {
		        	x = column * UNIT;
		        	if (firstRowIndented ^ row % 2 != 0)
		        		x += UNIT / 2;
		        	y = (int)((double)row * GRIDWIDTH);

			    	g.fillOval(x - nodeRadius, y - nodeRadius, nodeRadius*2, nodeRadius*2);
			    	if (nodes[row][column].on)
			    		g.setColor(Color.CYAN.darker());
			    	else
			    		g.setColor(Color.BLACK);
			    	g.fillOval(x - nodeRadius + 2, y - nodeRadius + 2, nodeRadius*2 - 4, nodeRadius*2 - 4);
			    	g.setColor(Color.WHITE);
			    	
			    	g.setFont(new Font("SansSerif", Font.BOLD, nodeRadius));
			    	g.drawString(
			    			nodes[row][column].pc.symbol, 
			    			x - (g.getFontMetrics().stringWidth(nodes[row][column].pc.symbol) / 2),
			    			y + (g.getFont().getSize() / 2) - 2);
		        }
	        }
	    }
	}
	
	/**
	 * One node of the tonnetz
	 * @author Me
	 */
	private class TonnetzNode {
		
		public PC pc;
		public boolean on = false;
		
		public TonnetzNode(PC pc) {
			this.pc = pc;
		}
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
}
