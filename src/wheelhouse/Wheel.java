package wheelhouse;

import java.util.ArrayList;
import java.util.List;

import wheelhouse.Calc.Ratio;
import wheelhouse.tone.ToneET;

public class Wheel {
	
	private Wheel(){} // Static calls only
	
	public static Interval getAbsoluteInterval(ToneET baseTone, ToneET productTone) {
		int interval = productTone.getAbsoluteNote() - baseTone.getAbsoluteNote();
		return new Interval(Calc.mod(interval, 12));
	}
	
	public static int[] getChordCenterHeights(List<ToneET> tones) {
		int[] heights = new int[12];
		
		for (int i = 0; i < 12; i++) {
			int height;
			ToneET center = new ToneET(i);
			List<Ratio> freqRatiosFromCenter = new ArrayList<Ratio>();
			boolean hasSix = false;
			for (ToneET tone : tones) {
				int interval = getAbsoluteInterval(center, tone).getInterval();
				Ratio ratio;
				if (interval == 6) {
					hasSix = true;
					ratio = Interval.AUG4TH_ASC.getFrequencyRatio();
				} else {
					ratio = getAbsoluteInterval(center, tone).getFrequencyRatio();
				}
				freqRatiosFromCenter.add(ratio);
			}
			height = Calc.lcd(freqRatiosFromCenter); //wrong - doesn't force the chord center
			
			if (hasSix) { //Checking both tritone ratios
				for (int j = 0; j < freqRatiosFromCenter.size(); j++) { // Find and replace it
					if (freqRatiosFromCenter.get(j).equals(Interval.AUG4TH_ASC.getFrequencyRatio())) {
						freqRatiosFromCenter.set(j, Interval.DIM5TH_ASC.getFrequencyRatio());
						break;
					}
				}
				int newHeight = Calc.lcd(freqRatiosFromCenter);
				if (newHeight < height) // Take the lower height
					height = newHeight;
			}
			heights[i] = height;
		}
		
		return heights;
	}
	
	public List<Interval> shiftIntervals(List<Interval> intervals, int shift) {
		List<Interval> newIntervals = new ArrayList<Interval>();
		for (int i = 0; i < intervals.size(); i++) {
			newIntervals.add(shiftInterval(intervals.get(i), shift));
		}
		return newIntervals;
	}
	
	public Interval shiftInterval(Interval interval, int shift) {
		return new Interval(interval.getInterval() + shift);
	}
	
	public static List<Interval> toIntervals(ToneET base, List<ToneET> tones) {
		List<Interval> intervals = new ArrayList<Interval>();
		for (ToneET tone : tones) {
			intervals.add(base.getIntervaltoTone(tone));
		}
		return intervals;
	}
	
	public static List<Interval> toNormalizedIntervals(ToneET base, List<ToneET> tones) {
		List<Interval> intervals = new ArrayList<Interval>();
		for (ToneET tone : tones) {
			intervals.add(base.getIntervaltoTone(tone).toNormalizedInterval());
		}
		return intervals;
	}
}