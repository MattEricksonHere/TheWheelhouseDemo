package midiinput;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

/**
 * A class which loads an external MIDI controller
 * @author Me
 */
public class MidiController {

	public static final String SL_MKII = "SL MkII";

	private MidiDevice device;
	private Transmitter transmitter;
	private Receiver receiver;
	private String name;
	
	public MidiController(String deviceName, Receiver receiver) throws MidiUnavailableException {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        
        for (int i = 0; i < infos.length; i++) {
        	if (infos[i].getName().equals(deviceName)) {
				device = MidiSystem.getMidiDevice(infos[i]);
				name = infos[i].getName();
	        	break;
        	}
        }
       
		transmitter = device.getTransmitter();
        transmitter.setReceiver(receiver);
        device.open();
	}
	
	public static List<String> getAvailableInputDevices() {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        List<String> deviceNames = new ArrayList<String>();
        for (int i = 0; i < infos.length; i++) {
        	deviceNames.add(infos[i].getName());
        }
        return deviceNames;
	}

	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
        transmitter.setReceiver(receiver);
	}

	public Receiver getReceiver() {
		return receiver;
	}

	public String getName() {
		return name;
	}

	public void close() {
		device.close();
	}
}
