package wheelhouse;
import java.util.ArrayList;
import java.util.List;

import wheelhouse.Calc.Ratio;
import wheelhouse.tone.Tone;
import wheelhouse.tone.ToneET;

/**
 * Wraps a list of tones to be interpreted as a chord
 * @author Me
 */
public class Chord {

	private List<ToneET> tones = new ArrayList<ToneET>();
	private ToneET root;
	private ChordPattern type;
	private ToneET bass;
	private boolean hasBass = false;

	private List<Integer> chordRatio;
	private List<List<Integer>> chordRatioPrimeFactors;
	private List<Integer> complexitySpace;
	private List<List<Integer>> complexitySpaceAllCenters;
	private Integer complexity;
	private Double otonality;
	private List<Double> toneConsonances;

	/**
	 * @param chordSymbol Creates a Chord from a chord symbol string.
	 * ie "Amin7"
	 */
	public Chord(String chordSymbol) {
		Chord c = fromSymbol(chordSymbol);

		this.root = c.getRoot();
		this.type = c.getType();
		this.bass = c.bass;
		if (bass.getPitchClass() != root.getPitchClass())
			hasBass = true;
		
		initTonesFromType();
	}
	
	public Chord(List<ToneET> tones) {
		this.root = ChordPattern.findBestChordRoot(tones);
		this.type = new ChordPattern(Wheel.toNormalizedIntervals(root, tones));
		
		//Find bass
		int bassNote = Integer.MAX_VALUE;
		for (ToneET tone : tones) {
			if (tone.getAbsoluteNote() < bassNote) {
				this.bass = tone;
				bassNote = tone.getAbsoluteNote();
			}
		}
		
		if (bass.getPitchClass() != root.getPitchClass())
			hasBass = true;
				
	}

	public Chord(ToneET root, ChordPattern type) {
		this.root = root;
		this.type = type;
		this.bass = root;
		
		initTonesFromType();
	}

	public Chord(ToneET root, ChordPattern type, ToneET bass) {
		this.root = root;
		this.type = type;
		this.bass = bass;
		if (bass.getPitchClass() != root.getPitchClass())
			hasBass = true;
		
		initTonesFromType();
	}
	
	private void initTonesFromType() {
		for (Interval i : type.getIntervals(true))
			tones.add(root.getToneFromInterval(i));
	}
	
	private List<Double> getToneConsonances() {
		if (toneConsonances == null) {
			toneConsonances = new ArrayList<Double>();
			for (int i = 0; i < 12; i++) {
				ToneET t = new ToneET(i);
			}
		}
		return toneConsonances;
	}
	
	public double getSpreadCoeff() {
		List<Integer> ch = getChordRatio();
		double n = ch.size();
		double lcy = Calc.log2(getComplexity());
		List<Double> lch = Calc.log2(ch);
		double sumLCH = Calc.sum(lch);
		double lgcd = Calc.log2(Calc.gcd(ch));
		double lm = (sumLCH / n) - lgcd;
		
		double sum = 0;
		for (Double k : lch)
			sum += Math.pow(((k - lm - lgcd) / lcy), 2);
		return Math.pow((4.0 / n) * sum, 0.5);
	}
	
	public static final int OTC_PRECISION = 3;
	public double getOtonality() {
		if (otonality == null) {
			if (getTones().size() < 3) {
				otonality = 0.0;
			} else {
				// Mathematical Harmony Analysis p17 https://arxiv.org/ftp/arxiv/papers/1603/1603.08904.pdf
				List<Integer> ch = getChordRatio();
				double n = ch.size();
				double lcy = Calc.log2(getComplexity());
				List<Double> lch = Calc.log2(ch);
				double sumLCH = Calc.sum(lch);
				double lgcd = Calc.log2(Calc.gcd(ch));
				double lm = (sumLCH / n) - lgcd;
				
				double coef = n / (n - 2.0);
				otonality = coef * ((lcy - (2.0 * lm)) / lcy);
			}
		}
		return otonality;
	}
	
	public int getComplexity() {
		if (complexity == null) {
			List<Integer> ratio = getChordRatio();
			complexity = Calc.lcm(ratio) / Calc.gcd(ratio);
		}
		return complexity;
	}
	
	public List<List<Integer>> getComplexitySpaceAllCenters() {
		if (complexitySpaceAllCenters == null) {
			
		}
		return complexitySpaceAllCenters;
	}
	
	public List<Integer> getComplexitySpace() {
		if (complexitySpace == null) {
			complexitySpace = Calc.factors(getComplexity());
		}
		return complexitySpace;
	}
	
	public List<Integer> getChordRatio() {
		if (chordRatio == null) {
			List<Ratio> allRatios = getFrequencyRatios(true);
			chordRatio = Ratio.getMultiRatio(allRatios);
		}
		return chordRatio;
	}
	
	public List<List<Integer>> getChordRatioPrimeFactors() {
		if (chordRatioPrimeFactors == null) {
			chordRatioPrimeFactors = new ArrayList<List<Integer>>();
			for (int c : getChordRatio()) {
				chordRatioPrimeFactors.add(Calc.primeFactors(c));
			}
		}
		return chordRatioPrimeFactors;
	} 
	
	public List<Ratio> getFrequencyRatios(boolean includeRoot) {
		// This only gets all ratios from the chord root
		List<Ratio> allRatios = new ArrayList<Ratio>();
		for (Interval interval: type.getIntervals(includeRoot)) {
			allRatios.add(interval.getFrequencyRatio());
		}
		return allRatios;
	}
	
	public List<Tone> separateIntoHarmonics(Spectrum spectrum) {
		List<Tone> tonesWithHarmonics = new ArrayList<Tone>();
		for(Tone tone : getTones()) {
			tonesWithHarmonics.addAll(tone.getOvertones(spectrum));
		}
		return tonesWithHarmonics;
	}
	
	public double getNormalizedDissonance(Spectrum spectrum) {
		List<Tone> tonesWithHarmonics = new ArrayList<Tone>();
		for(Tone tone : getTones()) {
			tonesWithHarmonics.addAll(tone.getOvertones(spectrum));
		}
		return Calc.getTotalDissonanceSeparateHarmonics(getTones(), spectrum);
	}
	
	public List<ToneET> getTones() {
		return tones;
	}
	
	public ToneET getRoot() {
		return root;
	}

	public ChordPattern getType() {
		return type;
	}

	public ToneET getBass() {
		return bass;
	}

	public boolean hasBass() {
		return hasBass;
	}
	
	public String getSymbol() {
		if (!hasBass)
			return root.getSymbol()+type.getSymbol();
		else
			return root.getSymbol()+type.getSymbol()+"/"+bass.getSymbol();
	}
	
	public static Chord fromSymbol(String chordSymbol) {
		String symbol = chordSymbol;
		ToneET root;
		if (symbol.length() == 1 || symbol.charAt(1) != '#') {
			root = new ToneET(PC.fromSymbol(symbol.substring(0, 1)));
			symbol = symbol.substring(1);
		} else {
			root = new ToneET(PC.fromSymbol(symbol.substring(0, 2)));
			symbol = symbol.substring(2);
		}
		
		ChordPattern chord;
		if (symbol.length() > 2 && symbol.charAt(symbol.length() - 2) == '/') {
			ToneET bass;
			chord = ChordPattern.getTypeFromSymbol(symbol.substring(0, symbol.length()-2));
			bass = new ToneET(PC.fromSymbol(symbol.substring(symbol.length()-1)));
			return new Chord(root, chord, bass);
		} else if (symbol.length() > 3 && symbol.charAt(symbol.length() - 3) == '/') {
			ToneET bass;
			chord = ChordPattern.getTypeFromSymbol(symbol.substring(0, symbol.length()-3));
			bass = new ToneET(PC.fromSymbol(symbol.substring(symbol.length()-2)));
			return new Chord(root, chord, bass);
		} else {
			chord = ChordPattern.getTypeFromSymbol(symbol);
			return new Chord(root, chord);
		}
	}
	
	public Chord transpose(int halfSteps) {
		return new Chord(root.getToneFromInterval(halfSteps), type, bass.getToneFromInterval(halfSteps));
	}
	
	public Chord getRelativeMinor() {
		return new Chord(root.getToneFromInterval(-3), ChordPattern.MINOR);
	}
}
