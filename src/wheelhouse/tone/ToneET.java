package wheelhouse.tone;

import java.util.List;
import java.util.stream.Collectors;

import wheelhouse.Calc;
import wheelhouse.Interval;
import wheelhouse.PC;

public class ToneET extends Tone {

	protected static final int refAbsNote = 69; // A4
	
	private PC pc;
	
	public ToneET(PC pc, int octave, double loudness) {
		super(getFrequency(getAbsoluteNote(pc, octave)), loudness);
		this.pc = pc;
		this.octave = octave;
		setLoudness(loudness);
	}
	
	public ToneET(int absoluteNote, double loudness) {
		super(getFrequency(absoluteNote), loudness);
		this.pc = getClassForAbsoluteNote(absoluteNote);
		this.octave = 4;
		setLoudness(loudness);
	}
	
	public ToneET(PC pc, int octave) {
		super(getFrequency(getAbsoluteNote(pc, octave)), 1.0);
		this.pc = pc;
		this.octave = octave;
	}
	
	public ToneET(PC pc) {
		super(getFrequency(getAbsoluteNote(pc, 4)), 1.0);
		this.pc = pc;
		this.octave = 4;
	}
	
	public ToneET(int absoluteNote) {
		super(getFrequency(absoluteNote), 1.0);
		this.pc = getClassForAbsoluteNote(absoluteNote);
		this.octave = (absoluteNote - 12) / 12;
	}
	
	public PC getPitchClass() {
		return pc;
	}
	
	public static double getFrequency(int absoluteNote) {
		// This returns the twelve-tone equal temperament frequency of the tone
		//https://en.wikipedia.org/wiki/Equal_temperament#Calculating_absolute_frequencies
		int interval = absoluteNote - refAbsNote; // A4 = 440Hz
		return (refFreq * Math.pow(twelfthRootOfTwo, interval));
	}
	
	public static double getFrequency(PC pc, int octave) {
		return getFrequency(getAbsoluteNote(pc, octave));
	}
	
	public int getAbsoluteNote() {
		return ((octave + 1) * 12) + pc.n;
	}
	
	private static int getAbsoluteNote(PC pc, int octave) {
		return ((octave + 1) * 12) + pc.n;
	}
	
	public ToneET getToneFromInterval(Interval interval) {
		int newAbsoluteNote = getAbsoluteNote() + interval.getInterval();
		return new ToneET(newAbsoluteNote);
	}
	
	public ToneET getToneFromInterval(int interval) {
		int newAbsoluteNote = getAbsoluteNote() + interval;
		return new ToneET(newAbsoluteNote);
	}
	
	public Interval getIntervaltoTone(ToneET newTone) {
		return new Interval(newTone.getAbsoluteNote() - getAbsoluteNote());
	}
	
	public static List<PC> toPitchClasses(List<ToneET> tones) {
		return tones.stream().map(ToneET::getPitchClass).collect(Collectors.toList());
	}
	
	public static PC getClassForAbsoluteNote(int absoluteNote) {
		int note = Calc.mod(absoluteNote, 12);
		return PC.fromIndex(note);
	}

	public boolean equals(ToneET otherTone) {
		return (pc == otherTone.pc && octave == otherTone.getOctave());
	}
	
	public boolean equalsIgnoreOctave(ToneET otherTone) {
		return (pc == otherTone.pc);
	}
	
	public Tone up(int by) {
		return new ToneET(getAbsoluteNote() + by);
	}
	
	public Tone down(int by) {
		return new ToneET(getAbsoluteNote() - by);
	}
	
	public Tone up(Interval by) {
		return new ToneET(getAbsoluteNote() + by.getInterval());
	}
	
	public Tone down(Interval by) {
		return new ToneET(getAbsoluteNote() - by.getInterval());
	}
	
	@Override
	public String getSymbol() {
		String symbol;
		switch (pc) {
		case A:
			symbol =  "A";
			break;
		case ASHARP:
			symbol =  "A#";
			break;
		case B:
			symbol =  "B";
			break;
		case C:
			symbol =  "C";
			break;
		case CSHARP:
			symbol =  "C#";
			break;
		case D:
			symbol =  "D";
			break;
		case DSHARP:
			symbol =  "D#";
			break;
		case E:
			symbol =  "E";
			break;
		case F:
			symbol =  "F";
			break;
		case FSHARP:
			symbol =  "F#";
			break;
		case G:
			symbol =  "G";
			break;
		case GSHARP:
			symbol =  "G#";
			break;
		default:
			symbol =  "";
			break;
		}
		return symbol;
	}
}
