package wheelhouse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import wheelhouse.tone.ToneET;

public class ChordPattern extends ArrayList<Interval> {
	private static final long serialVersionUID = 767086031474344398L;
	
	public static final ChordPattern MAJOR = 
    		new ChordPattern(Arrays.asList(Interval.MAJ3RD_ASC, Interval.PER5TH_ASC), "");
	public static final ChordPattern MAJOR_SEVENTH = 
			new ChordPattern(Arrays.asList(Interval.MAJ3RD_ASC, Interval.PER5TH_ASC, Interval.MAJ7TH_ASC), "maj7");
    public static final ChordPattern DOMINANT_SEVENTH = 
    		new ChordPattern(Arrays.asList(Interval.MAJ3RD_ASC, Interval.PER5TH_ASC, Interval.MIN7TH_ASC), "7");
    
	public static final ChordPattern MINOR = 
			new ChordPattern(Arrays.asList(Interval.MIN3RD_ASC, Interval.PER5TH_ASC), "min");
	public static final ChordPattern MINOR_SEVENTH = 
			new ChordPattern(Arrays.asList(Interval.MIN3RD_ASC, Interval.PER5TH_ASC, Interval.MIN7TH_ASC), "min7");
    public static final ChordPattern MINOR_MAJOR_SEVENTH = 
    		new ChordPattern(Arrays.asList(Interval.MIN3RD_ASC, Interval.PER5TH_ASC, Interval.MAJ7TH_ASC), "minmaj7");
	
	public static final ChordPattern AUGMENTED = 
			new ChordPattern(Arrays.asList(Interval.MAJ3RD_ASC, Interval.AUG5TH_ASC), "aug");
    public static final ChordPattern AUGMENTED_MAJOR_SEVENTH = 
    		new ChordPattern(Arrays.asList(Interval.MAJ3RD_ASC, Interval.AUG5TH_ASC, Interval.MAJ7TH_ASC), "augmaj7");
    public static final ChordPattern AUGMENTED_MINOR_SEVENTH = 
    		new ChordPattern(Arrays.asList(Interval.MAJ3RD_ASC, Interval.AUG5TH_ASC, Interval.MIN7TH_ASC), "augmin7");
	
	public static final ChordPattern DIMINISHED = 
			new ChordPattern(Arrays.asList(Interval.MIN3RD_ASC, Interval.DIM5TH_ASC), "dim");
    public static final ChordPattern DIMINISHED_SEVENTH = 
    		new ChordPattern(Arrays.asList(Interval.MIN3RD_ASC, Interval.DIM5TH_ASC, Interval.DIM7TH_ASC), "dim7");
    public static final ChordPattern HALF_DIMINISHED_SEVENTH = 
    		new ChordPattern(Arrays.asList(Interval.MIN3RD_ASC, Interval.DIM5TH_ASC, Interval.MIN7TH_ASC), "min7b5");
    public static final ChordPattern DIMINISHED_MAJOR_SEVENTH = 
    		new ChordPattern(Arrays.asList(Interval.MIN3RD_ASC, Interval.DIM5TH_ASC, Interval.MAJ7TH_ASC), "dimmaj7");

    public static final ChordPattern DOMINANT_SEVENTH_FLAT_FIVE = 
    		new ChordPattern(Arrays.asList(Interval.MAJ3RD_ASC, Interval.DIM5TH_ASC, Interval.MIN7TH_ASC), "7b5");
	
    
    public static final ChordPattern FIVE = 
    		new ChordPattern(Arrays.asList(Interval.PER5TH_ASC), "5");
//    public static final ChordPattern FIVE_DIMINISHED_SEVENTH = 
//    		new ChordPattern(Arrays.asList(Interval.PER5TH_ASC, Interval.DIM7TH_ASC), "5dim7");
    public static final ChordPattern FIVE_MINOR_SEVENTH = 
    		new ChordPattern(Arrays.asList(Interval.PER5TH_ASC, Interval.MIN7TH_ASC), "5min7");
    public static final ChordPattern FIVE_MAJOR_SEVENTH = 
    		new ChordPattern(Arrays.asList(Interval.PER5TH_ASC, Interval.MAJ7TH_ASC), "5maj7");
    
    public static final ChordPattern DIMINISHED_FIVE = 
    		new ChordPattern(Arrays.asList(Interval.DIM5TH_ASC), "b5");
    public static final ChordPattern DIMINISHED_FIVE_DIMINISHED_SEVENTH = 
    		new ChordPattern(Arrays.asList(Interval.DIM5TH_ASC, Interval.DIM7TH_ASC), "b5dim7");
    public static final ChordPattern DIMINISHED_FIVE_MINOR_SEVENTH = 
    		new ChordPattern(Arrays.asList(Interval.DIM5TH_ASC, Interval.MIN7TH_ASC), "b5min7");
    public static final ChordPattern DIMINISHED_FIVE_MAJOR_SEVENTH = 
    		new ChordPattern(Arrays.asList(Interval.DIM5TH_ASC, Interval.MAJ7TH_ASC), "b5maj7");
    
    public static final ChordPattern AUGMENTED_FIVE = 
    		new ChordPattern(Arrays.asList(Interval.AUG5TH_ASC), "aug5");
//    public static final ChordPattern AUGMENTED_FIVE_DIMINISHED_SEVENTH = 
//    		new ChordPattern(Arrays.asList(Interval.AUG5TH_ASC, Interval.DIM7TH_ASC), "aug5dim7");
    public static final ChordPattern AUGMENTED_FIVE_MINOR_SEVENTH = 
    		new ChordPattern(Arrays.asList(Interval.AUG5TH_ASC, Interval.MIN7TH_ASC), "aug5min7");
    public static final ChordPattern AUGMENTED_FIVE_MAJOR_SEVENTH = 
    		new ChordPattern(Arrays.asList(Interval.AUG5TH_ASC, Interval.MAJ7TH_ASC), "aug5maj7");

	public static final ChordPattern SUSPENDED_SECOND = 
			new ChordPattern(Arrays.asList(Interval.MAJ2ND_ASC, Interval.PER5TH_ASC), "sus2");
	public static final ChordPattern SUSPENDED_SECOND_DOMINANT_SEVENTH = 
			new ChordPattern(Arrays.asList(Interval.MAJ2ND_ASC, Interval.PER5TH_ASC, Interval.MIN7TH_ASC), "7sus2");
	public static final ChordPattern SUSPENDED_SECOND_MAJOR_SEVENTH = 
			new ChordPattern(Arrays.asList(Interval.MAJ2ND_ASC, Interval.PER5TH_ASC, Interval.MAJ7TH_ASC), "maj7sus2");

	public static final ChordPattern SUSPENDED_FOURTH = 
			new ChordPattern(Arrays.asList(Interval.PER4TH_ASC, Interval.PER5TH_ASC), "sus4");
	public static final ChordPattern SUSPENDED_FOURTH_DOMINANT_SEVENTH = 
			new ChordPattern(Arrays.asList(Interval.PER4TH_ASC, Interval.PER5TH_ASC, Interval.MIN7TH_ASC), "7sus4");
	public static final ChordPattern SUSPENDED_FOURTH_MAJOR_SEVENTH = 
			new ChordPattern(Arrays.asList(Interval.PER4TH_ASC, Interval.PER5TH_ASC, Interval.MAJ7TH_ASC), "maj7sus4");
	private static final String UNKNOWN_SYMBOL = "¿";
    
    private String symbol;
    private Interval ninth = null;
    private Interval eleventh = null;
    private Interval thirteenth = null;

	public ChordPattern (List<Interval> intervals) {
    	super(intervals);
    }

	private ChordPattern (List<Interval> intervals, String symbol) {
    	super(intervals);
    	this.symbol = symbol;
    }
    
    public List<Interval> getIntervals(boolean includeRoot) {
    	if (includeRoot) {
    		List<Interval> allIntervals = new ArrayList<Interval>();
    		allIntervals.add(Interval.PER1ST_ASC);
    		allIntervals.addAll(this);
    		return allIntervals;
    	} else 
    		return this;
	}

	public String getSymbol() {
		if (symbol == null) {
	    	symbol = getSymbolFromIntervals(this);
		}
		return symbol;
	}
	
	public boolean hasFlatFive() {
		return (contains(Interval.DIM5TH_ASC));
	}
	
	public static ChordPattern getTypeFromSymbol (String symbol) {
		if (symbol.equals("maj"))
			return MAJOR;
		if (symbol.equals("m"))
			return MINOR;
		for (ChordPattern type : getAllTypes()) {
			if (type.getSymbol().equals(symbol))
				return type;
		}

		return null;
	}
	
	public static ChordPattern getPatternFromTones(List<ToneET> tones) {
		return new ChordPattern(removeDuplicates(Interval.normalizeIntervals(Wheel.toIntervals(findBestChordRoot(tones), tones))));
	}
	
	public static ToneET findBestChordRoot(List<ToneET> tones) {
		if (tones == null || tones.size() == 0)
			return null;
		
		Collections.sort(tones, new Comparator<ToneET>() {
			@Override
			public int compare(ToneET t1, ToneET t2) {
				return Integer.compare(t1.getAbsoluteNote(), t2.getAbsoluteNote());
			}
		});
		
		for (ToneET possibleRoot : tones) {
			for (ToneET possibleFifth : tones) {
				if (possibleRoot.getPitchClass().up(7) == possibleFifth.getPitchClass())
					return possibleRoot;
			}
		}
		
		return tones.get(0);
	}
	
	public static ChordPattern getPatternFromTones(ToneET root, List<ToneET> tones) {
		return new ChordPattern(removeDuplicates(Interval.normalizeIntervals(Wheel.toIntervals(root, tones))));
	}
	
	public static String getSymbolFromIntervals(List<Interval> intervals) {
		List<Interval> normIntervals = removeDuplicates(Interval.normalizeIntervals(intervals));
		ChordPattern basePattern = null;
		
		if (normIntervals.size() == 0)
			return "solo";

		outerloop:
		for (ChordPattern pattern : getAllTypes()) {
			for (Interval patternInterval : pattern) {
				boolean found = false;
				for (Interval argInterval : normIntervals) {
					if (patternInterval.getInterval() == argInterval.getInterval()) {
						found = true;
						break;
					}
				}
				if (!found)
					continue outerloop;
			}
			
			if (basePattern == null || pattern.size() > basePattern.size()) {
				basePattern = pattern;
				if (basePattern.size() == 4)
					break outerloop;
			}
		}
		
		if (basePattern != null) {
			
			String symbol = basePattern.getSymbol();
			
			int e9 = -1;
			int e11 = -1;
			int e13 = -1;

			// 9ths
			outerloop:
			for (int i = 1; i <= 3; i++) {
				for (Interval basePatternInterval : basePattern) {
					if (basePatternInterval.getInterval() == i)
						continue outerloop; // Note is part of base chord
				}
				for (Interval interval : normIntervals) {
					if (interval.getInterval() == i) {
						if (e9 == -1)
							e9 = interval.getInterval();
						else
							return UNKNOWN_SYMBOL; // Two of same extension
					}
				}
			}

			// 11ths
			outerloop:
			for (int i = 5; i <= 6; i++) {
				for (Interval basePatternInterval : basePattern) {
					if (basePatternInterval.getInterval() == i)
						continue outerloop; // Note is part of base chord
				}
				for (Interval interval : normIntervals) {
					if (interval.getInterval() == i) {
						if (e11 == -1)
							e11 = interval.getInterval();
						else
							return UNKNOWN_SYMBOL; // Two of same extension
					}
				}
			}

			// 13ths
			outerloop:
			for (int i = 8; i <= 9; i++) {
				for (Interval basePatternInterval : basePattern) {
					if (basePatternInterval.getInterval() == i)
						continue outerloop; // Note is part of base chord
				}
				for (Interval interval : normIntervals) {
					if (interval.getInterval() == i) {
						if (e13 == -1)
							e13 = interval.getInterval();
						else
							return UNKNOWN_SYMBOL; // Two of same extension
					}
				}
			}
			
			String extensionSymbol = "";
			
			if (e13 == 9)
				extensionSymbol = "13";
			else if (e11 == 5)
				extensionSymbol = "11";
			else if (e9 == 2)
				extensionSymbol = "9";
			
			if (e9 == 1) {
				extensionSymbol += "b9";
			} else if (e9 == 3) {
				extensionSymbol += "#9";
			}
			
			if (e11 == 6) {
				extensionSymbol += "#11";
			}
			
			if (e13 == 8) {
				extensionSymbol += "b13";
			}
			
			if (extensionSymbol.length() > 0 && symbol.endsWith("7"))
				symbol = symbol.substring(0, symbol.length() - 1);
			
			symbol += extensionSymbol;

			return symbol;
		} else {
			if (normIntervals.size() == 1) {
				int interval = normIntervals.get(0).getInterval();
				
				if (interval == 1)
					return "&b2";
				else if (interval == 2)
					return "&2";
				else if (interval == 3)
					return "&b3";
				else if (interval == 4)
					return "&3";
				else if (interval == 5)
					return "&4";
				else if (interval == 6)
					return "&b5";
				else if (interval == 7)
					return "&5";
				else if (interval == 8)
					return "&b6";
				else if (interval == 9)
					return "&6";
				else if (interval == 10)
					return "&b7";
				else if (interval == 11)
					return "&7";
			}
		}
		
		
		return UNKNOWN_SYMBOL;
	}
	
	public static ChordPattern getPatternFromIntervals(List<Interval> intervals) {
		intervals = cleanOctaves(intervals);
		for (int i = 0; i < intervals.size(); i++) {
			if (intervals.get(i).getInterval() == 0) {
				intervals.remove(i);
				break;
			}
		}
		
		ChordPattern result = null;

		outerloop:
		for (ChordPattern pattern : getAllTypes()) {
			for (Interval i : pattern) {
				boolean foundInterval = false;
				for (Interval argInterval : intervals) {
					if (argInterval.getInterval() == i.getInterval()) {
						foundInterval = true;
						break;
					}
				}
				if (!foundInterval) {
					continue outerloop;
				}
			}
			
			if (result == null || pattern.size() > result.size()) {
				result = pattern;
				if (result.size() == 4)
					break outerloop;
			}
		}
		// TODO extensions
		return result;
	}
  
    // Function to remove duplicates from an ArrayList 
    public static <T> ArrayList<T> removeDuplicates(List<T> list) { 
        ArrayList<T> newList = new ArrayList<T>();
        for (T element : list)
            if (!newList.contains(element))
                newList.add(element); 
        return newList; 
    }
	
	public static ChordPattern fromIntervals(Interval... intervals) {
		return (ChordPattern)Arrays.asList(intervals);
	}
	
	public static ChordPattern fromIntervals(List<Interval> intervals) {
		return new ChordPattern(intervals);
	}
	
	public Interval getNinth() {
		return ninth;
	}

	public void setNinth(Interval ninth) {
		this.ninth = ninth;
		symbol = null; // Refresh symbol
	}

	public Interval getEleventh() {
		return eleventh;
	}

	public void setEleventh(Interval eleventh) {
		this.eleventh = eleventh;
		symbol = null; // Refresh symbol
	}

	public Interval getThirteenth() {
		return thirteenth;
	}

	public void setThirteenth(Interval thirteenth) {
		this.thirteenth = thirteenth;
		symbol = null; // Refresh symbol
	}
	
	/**
	 * Normalizes all octaves and removes duplicates
	 * @param intervals list to be cleaned
	 * @return
	 */
	public static List<Interval> cleanOctaves(List<Interval> intervals) {
		List<Interval> newIntervals = new ArrayList<Interval>();
		for (Interval i : intervals) {
			Interval newInterval = i.toNormalizedInterval();
			if (!newIntervals.contains(newInterval))
				newIntervals.add(newInterval);
		}
		return newIntervals;
	}

	public static List<ChordPattern> getAllTypes() {
		List<ChordPattern> all = new ArrayList<ChordPattern>();

		all.add(MAJOR);
		all.add(MAJOR_SEVENTH);
		all.add(DOMINANT_SEVENTH);
		all.add(MINOR);
		all.add(MINOR_SEVENTH);
		all.add(MINOR_MAJOR_SEVENTH);
		all.add(AUGMENTED);
		all.add(AUGMENTED_MAJOR_SEVENTH);
		all.add(AUGMENTED_MINOR_SEVENTH);
		all.add(DIMINISHED);
		all.add(DIMINISHED_SEVENTH);
		all.add(HALF_DIMINISHED_SEVENTH);
		all.add(DIMINISHED_MAJOR_SEVENTH);
		all.add(DOMINANT_SEVENTH_FLAT_FIVE);
		all.add(FIVE);
//		all.add(FIVE_DIMINISHED_SEVENTH);
		all.add(FIVE_MINOR_SEVENTH);
		all.add(FIVE_MAJOR_SEVENTH);
		all.add(DIMINISHED_FIVE);
		all.add(DIMINISHED_FIVE_DIMINISHED_SEVENTH);
		all.add(DIMINISHED_FIVE_MAJOR_SEVENTH);
		all.add(DIMINISHED_FIVE_MINOR_SEVENTH);
		all.add(AUGMENTED_FIVE);
//		all.add(AUGMENTED_FIVE_DIMINISHED_SEVENTH);
		all.add(AUGMENTED_FIVE_MAJOR_SEVENTH);
		all.add(AUGMENTED_FIVE_MINOR_SEVENTH);
		all.add(SUSPENDED_SECOND);
		all.add(SUSPENDED_SECOND_DOMINANT_SEVENTH);
		all.add(SUSPENDED_SECOND_MAJOR_SEVENTH);
		all.add(SUSPENDED_FOURTH);
		all.add(SUSPENDED_FOURTH_DOMINANT_SEVENTH);
		all.add(SUSPENDED_FOURTH_MAJOR_SEVENTH);
		
		return all;
	}
}