package wheelhouse;

import java.util.ArrayList;
import java.util.List;

import wheelhouse.Calc.Ratio;

public class Interval {

	private IntervalType type;
	private int interval;

	public static final Interval PER1ST_ASC = new Interval(IntervalType.PER1ST);
	public static final Interval AUG1ST_ASC = new Interval(IntervalType.AUG1ST);

	public static final Interval DIM2ND_ASC = new Interval(IntervalType.DIM2ND);
	public static final Interval MIN2ND_ASC = new Interval(IntervalType.MIN2ND);
	public static final Interval MAJ2ND_ASC = new Interval(IntervalType.MAJ2ND);
	public static final Interval AUG2ND_ASC = new Interval(IntervalType.AUG2ND);

	public static final Interval DIM3RD_ASC = new Interval(IntervalType.DIM3RD);
	public static final Interval MIN3RD_ASC = new Interval(IntervalType.MIN3RD);
	public static final Interval MAJ3RD_ASC = new Interval(IntervalType.MAJ3RD);
	public static final Interval AUG3RD_ASC = new Interval(IntervalType.AUG3RD);

	public static final Interval DIM4TH_ASC = new Interval(IntervalType.DIM4TH);
	public static final Interval PER4TH_ASC = new Interval(IntervalType.PER4TH);
	public static final Interval AUG4TH_ASC = new Interval(IntervalType.AUG4TH);

	public static final Interval DIM5TH_ASC = new Interval(IntervalType.DIM5TH);
	public static final Interval PER5TH_ASC = new Interval(IntervalType.PER5TH);
	public static final Interval AUG5TH_ASC = new Interval(IntervalType.AUG5TH);

	public static final Interval DIM6TH_ASC = new Interval(IntervalType.DIM6TH);
	public static final Interval MIN6TH_ASC = new Interval(IntervalType.MIN6TH);
	public static final Interval MAJ6TH_ASC = new Interval(IntervalType.MAJ6TH);
	public static final Interval AUG6TH_ASC = new Interval(IntervalType.AUG6TH);

	public static final Interval DIM7TH_ASC = new Interval(IntervalType.DIM7TH);
	public static final Interval MIN7TH_ASC = new Interval(IntervalType.MIN7TH);
	public static final Interval MAJ7TH_ASC = new Interval(IntervalType.MAJ7TH);
	public static final Interval AUG7TH_ASC = new Interval(IntervalType.AUG7TH);

	public static final Interval DIM8TH_ASC = new Interval(IntervalType.DIM8TH);
	public static final Interval PER8TH_ASC = new Interval(IntervalType.PER8TH);
	public static final Interval AUG8TH_ASC = new Interval(IntervalType.AUG8TH);

	public static final Interval DIM9TH_ASC = new Interval(IntervalType.DIM9TH);
	public static final Interval MIN9TH_ASC = new Interval(IntervalType.MIN9TH);
	public static final Interval MAJ9TH_ASC = new Interval(IntervalType.MAJ9TH);
	public static final Interval AUG9TH_ASC = new Interval(IntervalType.AUG9TH);

	public static final Interval DIM10TH_ASC = new Interval(IntervalType.DIM10TH);
	public static final Interval MIN10TH_ASC = new Interval(IntervalType.MIN10TH);
	public static final Interval MAJ10TH_ASC = new Interval(IntervalType.MAJ10TH);
	public static final Interval AUG10TH_ASC = new Interval(IntervalType.AUG10TH);

	public static final Interval DIM11TH_ASC = new Interval(IntervalType.DIM11TH);
	public static final Interval PER11TH_ASC = new Interval(IntervalType.PER11TH);
	public static final Interval AUG11TH_ASC = new Interval(IntervalType.AUG11TH);

	public static final Interval DIM12TH_ASC = new Interval(IntervalType.DIM12TH);
	public static final Interval PER12TH_ASC = new Interval(IntervalType.PER12TH);
	public static final Interval AUG12TH_ASC = new Interval(IntervalType.AUG12TH);

	public static final Interval DIM13TH_ASC = new Interval(IntervalType.DIM13TH);
	public static final Interval MIN13TH_ASC = new Interval(IntervalType.MIN13TH);
	public static final Interval MAJ13TH_ASC = new Interval(IntervalType.MAJ13TH);
	public static final Interval AUG13TH_ASC = new Interval(IntervalType.AUG13TH);
	
	public Interval (IntervalType type) {
		this.type = type;
		this.interval = type.interval;
	}
	
	public Interval (int interval) {
		this.type = getGenericTypeForInterval(interval);
		this.interval = interval;
	}
	
	/**
	 * @param other the interval to compare
	 * @return true if the intervals are equal in half steps
	 */
	public boolean equalsIgnoreContext(Interval other) {
		return (interval == other.getInterval());
	}
	
	public IntervalType getType() {
		return type;
	}

	/**
	 * @return a negative interval when descending
	 */
	public int getInterval() {
		return interval;
	}
	
	/**
	 * @return a positive interval even when descending
	 */
	public int getAbsoluteInterval() {
		return Math.abs(interval);
	}

	public boolean isAscending() {
		return interval >= 0;
	}
	
	public int getDissonance() {
		return getFrequencyRatio().getDenominator();
	}
	
	public Interval toNormalizedInterval() {
		return new Interval(Calc.mod(getInterval(), 12));
	}
	
	public static List<Interval> normalizeIntervals(List<Interval> intervals) {
		List<Interval> result = new ArrayList<Interval>();
		for (Interval interval : intervals)
			if (interval.toNormalizedInterval().getInterval() != 0)
				result.add(interval.toNormalizedInterval());
		return result;
	}

	public Ratio getFrequencyRatio() {
		int interval = type.interval;
		boolean flatFive = (type == IntervalType.DIM5TH || type == IntervalType.DIM13TH);
		int octaves = interval/12;
		int n = interval%12;
		Ratio ratio;
		switch (n) {
			case 0:
				ratio = new Ratio(1, 1);
				break;
			case 1:
				ratio = new Ratio(16, 15);
				break;
			case 2:
				ratio = new Ratio(9, 8);
				break;
			case 3:
				ratio = new Ratio(6, 5);
				break;
			case 4:
				ratio = new Ratio(5, 4);
				break;
			case 5:
				ratio = new Ratio(4, 3);
				break;
			case 6:
				if (flatFive)
					ratio = new Ratio(64, 45);
				else
					ratio = new Ratio(45, 32);
				break;
			case 7:
				ratio = new Ratio(3, 2);
				break;
			case 8:
				ratio = new Ratio(8, 5);
				break;
			case 9:
				ratio = new Ratio(5, 3);
				break;
			case 10:
				ratio = new Ratio(9, 5);
				break;
			case 11:
				ratio = new Ratio(15, 8);
				break;
			default:
				return null; //Error
		}
		ratio.setNumerator(ratio.getNumerator() + (ratio.getDenominator() * octaves));
		return ratio;
	}
	
	public static Interval fromScaleDegree(String deg) {
		switch (deg) {
		case "1" :
			return new Interval(0);
		case "b2" :
			return new Interval(1);
		case "2" :
			return new Interval(2);
		case "b3" :
			return new Interval(3);
		case "3" :
			return new Interval(4);
		case "4" :
			return new Interval(5);
		case "b5" :
			return new Interval(6);
		case "5" :
			return new Interval(7);
		case "b6" :
			return new Interval(8);
		case "6" :
			return new Interval(9);
		case "b7" :
			return new Interval(10);
		case "7" :
			return new Interval(11);
		case "8" :
			return new Interval(12);
		default :
			return new Interval(0);
		}	
	}
	
	private IntervalType getGenericTypeForInterval(int interval) {
		switch(interval) {
		case 0:
			return IntervalType.PER1ST;
		case 1:
			return IntervalType.MIN2ND;
		case 2:
			return IntervalType.MAJ2ND;
		case 3:
			return IntervalType.MIN3RD;
		case 4:
			return IntervalType.MAJ3RD;
		case 5:
			return IntervalType.PER4TH;
		case 6:
			return IntervalType.DIM5TH;
		case 7:
			return IntervalType.PER5TH;
		case 8:
			return IntervalType.MIN6TH;
		case 9:
			return IntervalType.MAJ6TH;
		case 10:
			return IntervalType.MIN7TH;
		case 11:
			return IntervalType.MAJ7TH;
		case 12:
			return IntervalType.PER8TH;
		case 13:
			return IntervalType.MIN9TH;
		case 14:
			return IntervalType.MAJ9TH;
		case 15:
			return IntervalType.MIN10TH;
		case 16:
			return IntervalType.MAJ10TH;
		case 17:
			return IntervalType.PER11TH;
		case 18:
			return IntervalType.DIM12TH;
		case 19:
			return IntervalType.PER12TH;
		case 20:
			return IntervalType.MIN13TH;
		case 21:
			return IntervalType.MAJ13TH;
		case 22:
			return IntervalType.AUG13TH;
		default:
			return IntervalType.DEFAULT;
		}
	}
	
	public enum IntervalType {
		PER1ST (0),
		AUG1ST (1),

		DIM2ND (0),
		MIN2ND (1),
		MAJ2ND (2),
		AUG2ND (3),

		DIM3RD (2),
		MIN3RD (3),
		MAJ3RD (4),
		AUG3RD (5),

		DIM4TH (4),
		PER4TH (5),
		AUG4TH (6),

		DIM5TH (6),
		PER5TH (7),
		AUG5TH (8),

		DIM6TH (7),
		MIN6TH (8),
		MAJ6TH (9),
		AUG6TH (10),

		DIM7TH (9),
		MIN7TH (10),
		MAJ7TH (11),
		AUG7TH (12),

		DIM8TH (11),
		PER8TH (12),
		AUG8TH (13),

		DIM9TH (12),
		MIN9TH (13),
		MAJ9TH (14),
		AUG9TH (15),

		DIM10TH (14),
		MIN10TH (15),
		MAJ10TH (16),
		AUG10TH (17),

		DIM11TH (16),
		PER11TH (17),
		AUG11TH (18),

		DIM12TH (18),
		PER12TH (19),
		AUG12TH (20),

		DIM13TH (19),
		MIN13TH (20),
		MAJ13TH (21),
		AUG13TH (22),
		
		DEFAULT (-1);
		
		public int interval;
		
		IntervalType(int interval) {
			this.interval = interval;
		}
	}
}
