package midiinput;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import vst.VSTPlayer;

/**
 * Class which allows computer keyboard to be used as a VST controller
 * @author Me
 */
public class KeyboardMidiController {
	
	private static final int VELO = 93;
	private InputMap inputMap;
	private static Map<Integer, Integer> keyMap = new HashMap<Integer, Integer>();
	static {
		int note = 60;
		keyMap.put(KeyEvent.VK_Z, note);
		keyMap.put(KeyEvent.VK_S, note + 1);
		keyMap.put(KeyEvent.VK_X, note + 2);
		keyMap.put(KeyEvent.VK_D, note + 3);
		keyMap.put(KeyEvent.VK_C, note + 4);
		keyMap.put(KeyEvent.VK_V, note + 5);
		keyMap.put(KeyEvent.VK_G, note + 6);
		keyMap.put(KeyEvent.VK_B, note + 7);
		keyMap.put(KeyEvent.VK_H, note + 8);
		keyMap.put(KeyEvent.VK_N, note + 9);
		keyMap.put(KeyEvent.VK_J, note + 10);
		keyMap.put(KeyEvent.VK_M, note + 11);
		keyMap.put(KeyEvent.VK_COMMA, note + 12);
		keyMap.put(KeyEvent.VK_L, note + 13);
		keyMap.put(KeyEvent.VK_PERIOD, note + 14);
		keyMap.put(KeyEvent.VK_SEMICOLON, note + 15);
		keyMap.put(KeyEvent.VK_SLASH, note + 16);
		
		keyMap.put(KeyEvent.VK_Q, note + 12);
		keyMap.put(KeyEvent.VK_2, note + 13);
		keyMap.put(KeyEvent.VK_W, note + 14);
		keyMap.put(KeyEvent.VK_3, note + 15);
		keyMap.put(KeyEvent.VK_E, note + 16);
		keyMap.put(KeyEvent.VK_R, note + 17);
		keyMap.put(KeyEvent.VK_5, note + 18);
		keyMap.put(KeyEvent.VK_T, note + 19);
		keyMap.put(KeyEvent.VK_6, note + 20);
		keyMap.put(KeyEvent.VK_Y, note + 21);
		keyMap.put(KeyEvent.VK_7, note + 22);
		keyMap.put(KeyEvent.VK_U, note + 23);
		keyMap.put(KeyEvent.VK_I, note + 24);
		keyMap.put(KeyEvent.VK_9, note + 25);
		keyMap.put(KeyEvent.VK_O, note + 26);
		keyMap.put(KeyEvent.VK_0, note + 27);
		keyMap.put(KeyEvent.VK_P, note + 28);
		keyMap.put(KeyEvent.VK_OPEN_BRACKET, note + 29);
		keyMap.put(KeyEvent.VK_EQUALS, note + 30);
		keyMap.put(KeyEvent.VK_CLOSE_BRACKET, note + 31);
	}
	
	private Receiver receiver;
	private List<Integer> currentlyPlayedNotes = new ArrayList<Integer>();
	
	public KeyboardMidiController(JComponent listener, Receiver receiver) {

		inputMap = listener.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		for (Entry<Integer, Integer> e : keyMap.entrySet()) {
			inputMap.put(KeyStroke.getKeyStroke(e.getKey(), 0, false), e.getKey() + "_ON");
			listener.getActionMap().put(e.getKey() + "_ON", new KeyAction(e.getValue(), true));

			inputMap.put(KeyStroke.getKeyStroke(e.getKey(), 0, true), e.getKey() + "_OFF");
			listener.getActionMap().put(e.getKey() + "_OFF", new KeyAction(e.getValue(), false));
		}
		this.receiver = receiver;
	}

	private class KeyAction extends AbstractAction {
		private static final long serialVersionUID = 2846192696291739453L;
		
		private int note;
		private boolean on;
		
		private KeyAction(int note, boolean on) {
			this.note = note;
			this.on = on;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				if (on && !currentlyPlayedNotes.contains(note)) {
					receiver.send(new ShortMessage(ShortMessage.NOTE_ON, VSTPlayer.CH_KEYBOARD, note, VELO), -1);
					currentlyPlayedNotes.add(note);
				} else if (!on && currentlyPlayedNotes.contains(note)) {
					receiver.send(new ShortMessage(ShortMessage.NOTE_OFF, VSTPlayer.CH_KEYBOARD, note, VELO), -1);
					currentlyPlayedNotes.remove(Integer.valueOf(note));
				}
			} catch (InvalidMidiDataException e1) {
				e1.printStackTrace();
			}
		}
	}
}
