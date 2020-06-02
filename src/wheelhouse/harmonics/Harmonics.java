package wheelhouse.harmonics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import wheelhouse.Calc;
import wheelhouse.Calc.Vector1D;
import wheelhouse.tone.Tone;
import wheelhouse.Spectrum;

/**
 * Represents a list of tones as well as their overtones and undertones
 * @author Me
 */
public class Harmonics {

	protected final double MAXCBDIFF = 1.2;
	
	private List<? extends Tone> baseTones;
	private List<List<Tone>> overtones;
	private List<List<Tone>> undertones;

	private List<Dyad> dyads;
	private List<Vector1D> dissonanceVectors;

	public Harmonics(List<? extends Tone> tones) {
		tones.sort(Comparator.comparing(Tone::getFrequency));

		baseTones = tones;
		overtones = new ArrayList<List<Tone>>();
		undertones = new ArrayList<List<Tone>>();
		final Spectrum spectrum = Spectrum.SAW;
		
		for (Tone tone : baseTones) {
			overtones.add(tone.getOvertones(spectrum));
			undertones.add(tone.getUndertones(spectrum));
		}
	}
	
	public Tone getBaseTone(int index) {
		return baseTones.get(index);
	}
	
	public List<? extends Tone> getBaseTones() {
		return baseTones;
	}

	public List<List<Tone>> getOvertones() {
		return overtones;
	}

	public List<List<Tone>> getUndertones() {
		return undertones;
	}

	public List<Dyad> getDyads() {
		if (dyads == null) {
			dyads = new ArrayList<Dyad>();
			List<Tone> sortedPartials = getAllPartials();
			
			outerloop:
			for (int i = 0; i < sortedPartials.size(); i++) {
				for (int j = i + 1; j < sortedPartials.size(); j++) {
					double f1 = sortedPartials.get(i).getFrequency();
					double f2 = sortedPartials.get(j).getFrequency();
					
					if (Calc.getFreqDifferencePerCB(f1, f2) > MAXCBDIFF)
						continue outerloop;
					
					dyads.add(new Dyad(sortedPartials.get(i), sortedPartials.get(j)));
				}
			}
		}
		return dyads;
	}

	public List<Tone> getPartials(int baseToneIndex, boolean includeBase) {
		List<Tone> partials = new ArrayList<Tone>();
		partials.addAll(undertones.get(baseToneIndex));
		if (includeBase)
			partials.add(baseTones.get(baseToneIndex));
		partials.addAll(overtones.get(baseToneIndex));
		return partials;
	}
	
	protected List<Tone> getAllPartials() {
		List<Tone> partials = new ArrayList<Tone>();
		partials.addAll(baseTones);
		for (List<Tone> overtoneSet : overtones) {
			partials.addAll(overtoneSet);
		}
		for (List<Tone> undertoneSet : undertones) {
			partials.addAll(undertoneSet);
		}
		partials.sort(Comparator.comparing(Tone::getFrequency));
		return partials;
	}
	
	public double getDissonanceAgainstTone(Tone tone) {
		double total = 0;
		for (Tone partial : getAllPartials()) {
			total += (new Dyad(tone, partial)).dis;
		}
		return total;
	}
	
	public List<Vector1D> getDissonanceVectors() {
		if (dissonanceVectors == null) {
			dissonanceVectors = new ArrayList<Vector1D>();
			for (Dyad dyad : getDyads())
				dissonanceVectors.add(dyad.getDissonanceVector());
		}
		return dissonanceVectors;
	}
	
	public static class DissonanceSection {
		public double lowerFreq, upperFreq, magnitude;

		public DissonanceSection(double lowerFreq, double upperFreq, double magnitude) {
			this.lowerFreq = lowerFreq;
			this.upperFreq = upperFreq;
			this.magnitude = magnitude;
		}
		
		@Override
		public String toString() {
			int RND = 3;
			return "{" + Calc.round(lowerFreq, RND) + "," + Calc.round(upperFreq, RND) + "}"
						+ ": " + Calc.round(magnitude, RND);
		}
	}
	
	public List<DissonanceSection> getCompDissonanceSections(double lowerFreq, double upperFreq, int divisions) {
		List<DissonanceSection> sections = new ArrayList<DissonanceSection>();

		double logLower = Calc.log2(lowerFreq);
		double logUpper = Calc.log2(upperFreq);
		double logRange = logUpper - logLower;
		double logSectionRange = logRange/divisions;
		
		for (int i = 0; i < divisions; i++) {
			double lower = lowerFreq + Math.pow(2, logLower + (logSectionRange * i));
			double upper = lowerFreq + Math.pow(2, logLower + (logSectionRange * (i+1)));
			double diss = getDissonanceInRange(lower, upper);
			sections.add(new DissonanceSection(lower, upper, diss));
		}
		return sections;
	}
	
	protected double getDissonanceInRange(double lowerFreq, double upperFreq) {
		double total = 0;
		// dyads should already be sorted
		for (Dyad dyad : getDyads()) {
			if (dyad.freq2 <= lowerFreq) // too low
				continue; // dyad not in range, move on
			else if (dyad.freq1 >= upperFreq) // too high
				break; // dyad not in range, all remaining dyads will also be out, end loop
			
			if (dyad.freq1 >= lowerFreq && dyad.freq2 <= upperFreq) { // dyad completely within range
				total += dyad.getDis();
			} else if (dyad.freq1 < lowerFreq && dyad.freq2 <= upperFreq) { // bottom overlaps
				double overlap = (dyad.freq2 - lowerFreq) / (dyad.freq2 - dyad.freq1);
				total += dyad.getDis() * overlap;
			} else if (dyad.freq1 >= lowerFreq && dyad.freq2 > upperFreq) { // top overlaps
				double overlap = (upperFreq - dyad.freq1) / (dyad.freq2 - dyad.freq1);
				total += dyad.getDis() * overlap;
			} else if (dyad.freq1 < lowerFreq && dyad.freq2 > upperFreq) { // bottom and top overlap
				double overlap = (upperFreq - lowerFreq) / (dyad.freq2 - dyad.freq1);
				total += dyad.getDis() * overlap;
			}
		}
		return total;
	}
}
