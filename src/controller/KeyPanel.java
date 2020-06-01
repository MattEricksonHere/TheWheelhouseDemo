package controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import messaging.Courier;
import messaging.Listener;
import messaging.QueueMessage;
import midiinput.KeyboardMidiController;
import vst.VSTPlayer;
import wheelhouse.Chord;
import wheelhouse.ChordPattern;
import wheelhouse.PC;
import wheelhouse.tone.ToneET;

public class KeyPanel extends SubPanel implements Listener {
	private static final long serialVersionUID = -1109240850064575874L;
	
	private static final int xMargins = 10;
	private static final int KEYHEIGHT = 80;
	
	private static Map<Integer, String> keyMap = new HashMap<Integer, String>();
	static {
		int note = 48;
		keyMap.put(KeyEvent.VK_Z, "Z");
		keyMap.put(KeyEvent.VK_S, "S");
		keyMap.put(KeyEvent.VK_X, "X");
		keyMap.put(KeyEvent.VK_D, "D");
		keyMap.put(KeyEvent.VK_C, "C");
		keyMap.put(KeyEvent.VK_V, "V");
		keyMap.put(KeyEvent.VK_G, "G");
		keyMap.put(KeyEvent.VK_B, "B");
		keyMap.put(KeyEvent.VK_H, "H");
		keyMap.put(KeyEvent.VK_N, "N");
		keyMap.put(KeyEvent.VK_J, "J");
		keyMap.put(KeyEvent.VK_M, "M");
		keyMap.put(KeyEvent.VK_COMMA, ",");
		keyMap.put(KeyEvent.VK_L, "L");
		keyMap.put(KeyEvent.VK_PERIOD, ".");
		keyMap.put(KeyEvent.VK_SEMICOLON, ";");
		keyMap.put(KeyEvent.VK_SLASH, "/");
		
		keyMap.put(KeyEvent.VK_Q, "Q");
		keyMap.put(KeyEvent.VK_2, "2");
		keyMap.put(KeyEvent.VK_W, "W");
		keyMap.put(KeyEvent.VK_3, "3");
		keyMap.put(KeyEvent.VK_E, "E");
		keyMap.put(KeyEvent.VK_R, "R");
		keyMap.put(KeyEvent.VK_5, "5");
		keyMap.put(KeyEvent.VK_T, "T");
		keyMap.put(KeyEvent.VK_6, "6");
		keyMap.put(KeyEvent.VK_Y, "Y");
		keyMap.put(KeyEvent.VK_7, "7");
		keyMap.put(KeyEvent.VK_U, "U");
		keyMap.put(KeyEvent.VK_I, "I");
		keyMap.put(KeyEvent.VK_9, "9");
		keyMap.put(KeyEvent.VK_O, "O");
		keyMap.put(KeyEvent.VK_0, "0");
		keyMap.put(KeyEvent.VK_P, "P");
		keyMap.put(KeyEvent.VK_OPEN_BRACKET, "[");
		keyMap.put(KeyEvent.VK_EQUALS, "=");
		keyMap.put(KeyEvent.VK_CLOSE_BRACKET, "]");
	}

	private JPanel analysisPanel;
	private JLabel chordSymbolLabel;

	private KeyboardCanvas canvas;
	private ToneET lowNote = new ToneET(PC.A, 0);
	private ToneET highNote = new ToneET(PC.C, 8);
	private int numWhiteKeys;
	
	private List<Key> keys;

	public KeyPanel() {
		super("Keys");
		Courier.registerListener(this);

		setPreferredSize(new Dimension(1000, 200));
		
		numWhiteKeys = 0;
		keys = new ArrayList<Key>();
		for (int i = lowNote.getAbsoluteNote(); i <= highNote.getAbsoluteNote(); i++) {
			ToneET note = new ToneET(i);
			if (note.getPitchClass() == PC.ASHARP
					|| note.getPitchClass() == PC.CSHARP
					|| note.getPitchClass() == PC.DSHARP
					|| note.getPitchClass() == PC.FSHARP
					|| note.getPitchClass() == PC.GSHARP) {
				keys.add(new Key(i, numWhiteKeys - 1, true));
			} else {
				keys.add(new Key(i, numWhiteKeys, false));
				numWhiteKeys++;
			}
		}

		new KeyboardMidiController(this, VSTPlayer.getInstance());
		
		initPanel();
	}
	
	private void initPanel() {
		
		setPreferredSize(new Dimension(1000, 300));
		setLayout(new BorderLayout());
		
		add(getAnalysisPanel(), BorderLayout.CENTER);
		
		canvas = new KeyboardCanvas();
		add(canvas, BorderLayout.SOUTH);
		
	}
	
	private JPanel getAnalysisPanel() {
		if (analysisPanel == null) {
			analysisPanel = new JPanel();
			
			analysisPanel.add(getChordSymbolLabel());
		}
		return analysisPanel;
	}
	
	private JLabel getChordSymbolLabel() {
		if (chordSymbolLabel == null) {
			chordSymbolLabel = new JLabel(" ");
			chordSymbolLabel.setFont(new Font("Dialog", Font.PLAIN, 30));
			chordSymbolLabel.setForeground(Color.WHITE);
		}
		return chordSymbolLabel;
	}
    
    private class KeyboardCanvas extends JPanel {
		private static final long serialVersionUID = -689114774394195507L;

		private static final int WIDTH = 300;
		private static final int HEIGHT = 100;
		

		public KeyboardCanvas() {
			setMinimumSize(new Dimension(WIDTH, HEIGHT));
			setPreferredSize(new Dimension(WIDTH, HEIGHT));
			setMaximumSize(new Dimension(WIDTH, HEIGHT));
			
		}

	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        
	        for (Key key: keys) {
	        	if (!key.isBlack) {
	        		if (key.on)
	        			g.setColor(Color.BLUE);
	        		else
	        			g.setColor(Color.WHITE);
	        		g.fillRoundRect(getXForKey(key),
	        				20,
	        				getWidth() / numWhiteKeys,
	        				KEYHEIGHT,
	        				5,
	        				5);
	        		g.setColor(Color.BLACK);
	        		g.drawRoundRect(getXForKey(key),
	        				20,
	        				getWidth() / numWhiteKeys,
	        				KEYHEIGHT,
	        				5,
	        				5);
	        	}
	        }
	        
	        for (Key key: keys) {
	        	if (key.isBlack) {
	        		if (key.on)
	        			g.setColor(Color.BLUE);
	        		else
	        			g.setColor(Color.GRAY);
	        		g.fillRoundRect(getXForKey(key),
	        				20,
	        				getWidth() / numWhiteKeys,
	        				KEYHEIGHT / 2,
	        				5,
	        				5);
	        		g.setColor(Color.BLACK);
	        		g.drawRoundRect(getXForKey(key),
	        				20,
	        				getWidth() / numWhiteKeys,
	        				KEYHEIGHT / 2,
	        				5,
	        				5);
	        	}
	        }

			g.setColor(Color.WHITE);
	    }

		private int getXForKey(Key key) {
			if (!key.isBlack) {
				return (((getWidth() - (2 * xMargins)) / numWhiteKeys) * key.whiteKeyIndex + xMargins);
			} else {
				return (((getWidth() - (2 * xMargins)) / numWhiteKeys) * key.whiteKeyIndex + (getWidth() / numWhiteKeys / 2) + xMargins);
			}
		}
    }
    
    private class Key {
    	public int absoluteNote;
    	public int whiteKeyIndex;
    	public boolean isBlack;
    	public boolean on;
    	
    	private Key(int absoluteNote, int whiteKeyIndex, boolean isBlack) {
			this.absoluteNote = absoluteNote;
			this.whiteKeyIndex = whiteKeyIndex;
			this.isBlack = isBlack;
		}
		
    	private void setOn(boolean isOn) {
			this.on = isOn;
		}
    }

	
	private void updateNotes() {
		List<ToneET> notes = VSTPlayer.getInstance().getCurrentTones();
		for (Key key : keys) {
			key.setOn(false);
			for (ToneET tone : notes) {
				if (tone.getAbsoluteNote() == key.absoluteNote) {
					key.setOn(true);
				}
			}
		}
		
		if (notes != null && notes.size() > 0)
			getChordSymbolLabel().setText((new Chord(notes)).getSymbol());
		else
			getChordSymbolLabel().setText(" ");
		
		repaint();
		revalidate();
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
