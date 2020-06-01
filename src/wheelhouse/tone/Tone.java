package wheelhouse.tone;

import java.util.ArrayList;
import java.util.List;

import wheelhouse.Calc;
import wheelhouse.PC;
import wheelhouse.Spectrum;

public class Tone {
	protected int octave;
	private double loudness;
	
	private double frequency = -1;

	private static final int UNDEFINED_ABSOLUTE_NOTE = -1;
	protected static final ToneET refTone = new ToneET(PC.A, 4);
	protected static final double refFreq = 440;
	protected static final double twelfthRootOfTwo = Math.pow(2.0, (1.0 / 12.0));
	private static final double FREQ_MOE = 0.001;
	
	private static final double unequalToleranceForSymbols = .01;
	private static final double veryUnequalToleranceForSymbols = .1;
	
	// Used for frequency calculations
	public Tone (double frequency) {
		this.loudness = 1.0;
		this.octave = -1;
		this.frequency = frequency;
	}
	
	// Used for frequency calculations
	public Tone (double frequency, double loudness) {
		this.loudness = loudness;
		this.octave = -1;
		this.frequency = frequency;
	}
	
	public List<Tone> getOvertones(Spectrum spectrum) {
		List<Tone> harmonics = new ArrayList<Tone>();
		for (int i = 0; i < spectrum.getSize(); i++) {
			double freq = getFrequency() * (i + 2); // Skip fundamental
			double amp = spectrum.getAmplitudeForHarmonic(i);
			harmonics.add(new Tone(freq,amp));
		}
		return harmonics;
	}
	
	public List<Tone> getOvertones(Spectrum spectrum, double cutoffFrequency) {
		List<Tone> harmonics = new ArrayList<Tone>();
		for (int i = 0; i < spectrum.getSize(); i++) {
			double freq = getFrequency() * (i + 2); // Skip fundamental
			if (freq > cutoffFrequency)
				break;
			double amp = spectrum.getAmplitudeForHarmonic(i);
			harmonics.add(new Tone(freq,amp));
		}
		return harmonics;
	}
	
	public List<Tone> getUndertones(Spectrum spectrum) {
		List<Tone> subharmonics = new ArrayList<Tone>();
		for (int i = 0; i < spectrum.getSize(); i++) {
			double freq = getFrequency() / (i + 2); // Skip fundamental
			double amp = spectrum.getAmplitudeForHarmonic(i);
			subharmonics.add(new Tone(freq,amp));
		}
		return subharmonics;
	}
	
	public List<Tone> getUndertones(Spectrum spectrum, double cutoffFrequency) {
		List<Tone> subharmonics = new ArrayList<Tone>();
		for (int i = 0; i < spectrum.getSize(); i++) {
			double freq = getFrequency() / (i + 2); // Skip fundamental
			if (freq < cutoffFrequency)
				break;
			double amp = spectrum.getAmplitudeForHarmonic(i);
			subharmonics.add(new Tone(freq,amp));
		}
		return subharmonics;
	}
 	
	public double getFrequency() {
		return frequency;
	}
	
	/**
	 * Returns null if the frequency doesn't match a note
	 */
	public Tone getToneForCustomFrequency(double frequency, double marginOfError) {
		if (frequency <= 0)
			return null;
		double interval = Calc.log(twelfthRootOfTwo, frequency / refFreq);
		int wholeInterval = (int)Math.round(interval);
		if (Math.abs(interval - wholeInterval) <= marginOfError) {
			return refTone.getToneFromInterval(wholeInterval);
		} else {
			return null;
		}
	}

	public static String getSymbolForCustomFrequency(double frequency) {
		if (frequency <= 0)
			return "";
		double interval = Calc.log(twelfthRootOfTwo, frequency / refFreq);
		boolean increasing = interval > 0;
		int wholeInterval = (int)interval;
		String unequalSymbol;
		if (Calc.mod(Math.abs(interval),1.0) < unequalToleranceForSymbols) {
			unequalSymbol = "";
		} else if (increasing) {
			if (interval % 1.0 >= 0.5) {
				wholeInterval++;
				unequalSymbol = "<";
			} else {
				unequalSymbol = ">";
			}
		} else {
			if (interval % 1.0 <= -0.5) {
				wholeInterval--;
				unequalSymbol = ">";
			} else {
				unequalSymbol = "<";
			}
		}
		if (Calc.mod(Math.abs(interval),1.0) < veryUnequalToleranceForSymbols)
			unequalSymbol += unequalSymbol;
		Tone newTone = refTone.getToneFromInterval(wholeInterval);
		return unequalSymbol + newTone.getSymbol();
	}

	public static String getSymbolWithOctaveForCustomFrequency(double frequency) {
		if (frequency <= 0)
			return "";
		double interval = Calc.log(twelfthRootOfTwo, frequency / refFreq);
		boolean increasing = interval > 0;
		int wholeInterval = (int)interval;
		String unequalSymbol;
		if (Calc.mod(Math.abs(interval),1.0) < unequalToleranceForSymbols) {
			unequalSymbol = "";
		} else if (increasing) {
			if (interval % 1.0 >= 0.5) {
				wholeInterval++;
				unequalSymbol = "<";
			} else {
				unequalSymbol = ">";
			}
		} else {
			if (interval % 1.0 <= -0.5) {
				wholeInterval--;
				unequalSymbol = ">";
			} else {
				unequalSymbol = "<";
			}
		}
		if (Calc.mod(Math.abs(interval),1.0) < veryUnequalToleranceForSymbols)
			unequalSymbol += unequalSymbol;
		Tone newTone = refTone.getToneFromInterval(wholeInterval);
		return unequalSymbol + newTone.getSymbol() + newTone.getOctave();
	}
	
	public void setLoudness(double loudness) {
//		if (loudness > 1.0)
//			this.loudness = 1.0;
//		else if (loudness < 0.0)
//			this.loudness = 0.0;
//		else
			this.loudness = loudness;
	}
	
	public double getLoudness() {
		return loudness;
	}
	
	public String getSymbolWithOctave() {
		return getSymbol() + getOctave();
	}
	
	public String getSymbol() {
		String symbol = getSymbolForCustomFrequency(frequency);
		return symbol;
	}

	public int getOctave() {
		return octave;
	}
	
	public int getOctaveForAbsoluteNote(int absoluteNote) {
		int octave = absoluteNote / 12;
		return octave;
	}
}