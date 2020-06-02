package vst;

import java.io.File;
import java.io.FileNotFoundException;

import javax.sound.midi.ShortMessage;

import com.synthbot.audioio.vst.JVstAudioThread;
import com.synthbot.audioplugin.vst.JVstLoadException;
import com.synthbot.audioplugin.vst.vst2.JVstHost2;
import com.synthbot.audioplugin.vst.vst2.JVstHostListener;

/**
 * A class which loads a given VST instrument
 * @author Me
 */
public class VSTLoader implements JVstHostListener {

	private static final float SAMPLE_RATE = 44100f;
	private static final int BLOCK_SIZE = 4096;
	private static final String AUDIO_THREAD = "Audio Thread";
	  
	private static JVstHost2 vst;
	private JVstAudioThread audioThread;
	
	public VSTLoader (String vstpath) {
		// load the vst
	    try {
	      vst = JVstHost2.newInstance(new File(vstpath), SAMPLE_RATE, BLOCK_SIZE);
	    } catch (FileNotFoundException fnfe) {
	      fnfe.printStackTrace(System.err);
	      System.exit(1);
	    } catch (JVstLoadException jvle) {
	      jvle.printStackTrace(System.err);
	      System.exit(1);
	    }

	    vst.addJVstHostListener(this);
	    
	    // start the audio thread
	    audioThread = new JVstAudioThread(vst);
	    Thread thread = new Thread(audioThread);
	    thread.setName(AUDIO_THREAD); // for easy debugging
	    thread.setDaemon(true); // allows the JVM to exit normally
	    thread.start();
	}

	public JVstHost2 getHost() {
		return vst;
	}

	@Override
	public void onAudioMasterAutomate(JVstHost2 arg0, int arg1, float arg2) {
		
	}

	@Override
	public void onAudioMasterBeginEdit(JVstHost2 arg0, int arg1) {
		
	}

	@Override
	public void onAudioMasterEndEdit(JVstHost2 arg0, int arg1) {
		
	}

	@Override
	public void onAudioMasterIoChanged(JVstHost2 arg0, int arg1, int arg2,
			int arg3, int arg4) {
		
	}

	@Override
	public void onAudioMasterProcessMidiEvents(JVstHost2 arg0, ShortMessage arg1) {
		
	}
}
