package wheelhouse.harmonics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import wheelhouse.Calc;
import wheelhouse.Calc.Vector1D;
import wheelhouse.tone.Tone;

public class CompHarmonics extends Harmonics {
	
	public List<? extends Tone> tones1, tones2;
	private List<Dyad> unionDyads, intersectionDyads;
	private List<Vector1D> compDissonanceVectors;
	private Harmonics harmonics1, harmonics2;

	public CompHarmonics(List<? extends Tone> tones1, List<? extends Tone> tones2) {
		super(Stream.of(tones1, tones2).flatMap(x -> x.stream()).collect(Collectors.toList()));
		this.tones1 = tones1;
		this.tones2 = tones2;
	}
	
	public Harmonics getHarmonics1() {
		if (harmonics1 == null) {
			harmonics1 = new Harmonics(tones1);
		}
		return harmonics1;
	}

	public Harmonics getHarmonics2() {
		if (harmonics2 == null) {
			harmonics2 = new Harmonics(tones2);
		}
		return harmonics2;
	}

	public List<Dyad> getUnionDyads() {
		if (unionDyads == null) {
			unionDyads = new ArrayList<Dyad>();
			unionDyads.addAll(getDyads());
			unionDyads.addAll((new Harmonics(tones2)).getDyads());
			unionDyads.addAll(getIntersectionDyads());
			unionDyads.sort(Comparator.comparing(Dyad::getFreq1));
		}
		return unionDyads;
	}
	
	public List<Dyad> getIntersectionDyads() {
		if (intersectionDyads == null) {
			intersectionDyads = new ArrayList<Dyad>();
		
			List<Tone> partials1 = getHarmonics1().getAllPartials();
			List<Tone> partials2 = getHarmonics2().getAllPartials();
			
			outerloop:
			for (int i = 0; i < partials1.size(); i++) {
				for (int j = 0; j < partials2.size(); j++) {
					double f1 = partials1.get(i).getFrequency();
					double f2 = partials2.get(j).getFrequency();
					
					if (Calc.getFreqDifferencePerCB(f1, f2) > MAXCBDIFF)
						if (f2 < f1)
							continue;
						else
							continue outerloop;
					
					intersectionDyads.add(new Dyad(partials1.get(i), partials2.get(j)));
				}
			}
		}
		
		return intersectionDyads;
	}
	
	public double getTotalUnionDissonance() {
		double totalDiss = 0;
		for (Dyad dyad : getUnionDyads())
			totalDiss += dyad.getDis();
		return totalDiss;
	}
	
	public double getTotalIntersectionDissonance() {
		double totalDiss = 0;
		for (Dyad dyad : getIntersectionDyads())
			totalDiss += dyad.getDis();
		return totalDiss;
	}
	
	public List<Vector1D> getCompDissonanceVectors() {
		if (compDissonanceVectors == null) {
			compDissonanceVectors = new ArrayList<Vector1D>();
			for (Dyad dyad : getIntersectionDyads())
				compDissonanceVectors.add(dyad.getDissonanceVector());
		}
		return compDissonanceVectors;
	}
	
	// Uses bark scale.
	public List<DissonanceSection> getCompDissonanceSections() {
		List<DissonanceSection> sections = new ArrayList<DissonanceSection>();
		
		for(int i = 0; i < Calc.getBarks().size()-1; i++) {
			double diss = getCompDissonanceInRange(Calc.getBarks().get(i), Calc.getBarks().get(i+1));
			sections.add(new DissonanceSection(Calc.getBarks().get(i), Calc.getBarks().get(i+1), diss));
		}

//		double logLower = Calc.log2(lowerFreq);
//		double logUpper = Calc.log2(upperFreq);
//		double logRange = logUpper - logLower;
//		double logSectionRange = logRange/divisions;
//		
//		for (int i = 0; i < divisions; i++) {
//			double lower = Math.pow(2, logLower + (logSectionRange * i));
//			double upper = Math.pow(2, logLower + (logSectionRange * (i+1)));
//			double diss = getCompDissonanceInRange(lower, upper);
//			sections.add(new DissonanceSection(lower, upper, diss));
//		}
		return sections;
	}
	
	public List<DissonanceSection> getCompDissonanceSections(double lowerFreq, double upperFreq, int divisions) {
		List<DissonanceSection> sections = new ArrayList<DissonanceSection>();

		double logLower = Calc.log2(lowerFreq);
		double logUpper = Calc.log2(upperFreq);
		double logRange = logUpper - logLower;
		double logSectionRange = logRange/divisions;
		
		for (int i = 0; i < divisions; i++) {
			double lower = Math.pow(2, logLower + (logSectionRange * i));
			double upper = Math.pow(2, logLower + (logSectionRange * (i+1)));
			double diss = getCompDissonanceInRange(lower, upper);
			sections.add(new DissonanceSection(lower, upper, diss));
		}
		return sections;
	}
	
	protected double getCompDissonanceInRange(double lowerFreq, double upperFreq) {
		double total = 0;
		// dyads should already be sorted
		for (Dyad dyad : getIntersectionDyads()) {
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
