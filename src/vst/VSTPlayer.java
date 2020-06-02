package vst;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import messaging.QueueMessage;
import wheelhouse.tone.ToneET;

import com.synthbot.audioplugin.vst.vst2.JVstHost2;

/**
 * A class which controls a VST instrument
 * @author Me
 *
 */
public class VSTPlayer implements Receiver {

	private static final String VSTFILE = "mda ePiano.dll";
	public static final int CH_MAESTRO = 0;
	public static final int CH_MIDI = 0;
	public static final int CH_KEYBOARD = 0;
	
	private JVstHost2 host;
	private List<ToneET> currentTones = new ArrayList<ToneET>();
	private static VSTPlayer instance = null;
	
	private VSTPlayer() {
		initVST();
	}
	
	public synchronized List<ToneET> getCurrentTones() {
		return currentTones;
	}
	
	public static VSTPlayer getInstance() {
		if (instance == null)
			instance = new VSTPlayer();
		return instance;
	}
	
	private void initVST() {
		VSTLoader vst = new VSTLoader(VSTFILE);
		
		host = vst.getHost();
	}
	
	private synchronized void updateCurrentTones(ShortMessage message) {
		if (message.getCommand() == ShortMessage.NOTE_ON) {
			currentTones.add(new ToneET(message.getData1()));
		} else if (message.getCommand() == ShortMessage.NOTE_OFF) {
			for (int i = 0; i < currentTones.size(); i++) {
				if (currentTones.get(i).getAbsoluteNote() == message.getData1())
					currentTones.remove(i);
			}
		}
		QueueMessage.currentlyPlayedNotesChanged();
	}
	
	private ShortMessage midiToShort(MidiMessage midi) {
		ShortMessage shortMsg = null;
		try {
			shortMsg = new ShortMessage(midi.getStatus(), midi.getMessage()[1], midi.getMessage()[2]);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
			return new ShortMessage();
		} catch (Exception e) {
			if (shortMsg != null)
				System.out.println(shortMsg.getMessage());
			e.printStackTrace();
			return new ShortMessage();
		}
		return shortMsg;
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		ShortMessage shortMsg = midiToShort(message);
		host.queueMidiMessage(shortMsg);
		updateCurrentTones(shortMsg);
	}

	@Override
	public void close() {
		
	}
}
