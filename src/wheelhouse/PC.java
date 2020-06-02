package wheelhouse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An enumeration of pitch classes
 * @author Me
 */
public enum PC {

	C (0, "C"),
	CSHARP (1, "C#"),
	D (2, "D"),
	DSHARP (3, "D#"),
	E (4, "E"),
	F (5, "F"),
	FSHARP (6, "F#"),
	G (7, "G"),
	GSHARP (8, "G#"),
	A (9, "A"),
	ASHARP (10, "A#"),
	B (11, "B"),
	UNDEFINED (-999, ""); // Used for funny business
	
//	DFLAT (2),
//	EFLAT (4),
//	GFLAT (7),
//	AFLAT (9),
//	BFLAT (11),
	
	public int n;
	public String symbol;
	
	PC (int index, String symbol) {
		this.n = index;
		this.symbol = symbol;
	}
	
	public static List<PC> all() {
		List<PC> all = Arrays.asList(values());
		all.remove(UNDEFINED);
		return all;
	}
	
	public static List<PC> all(PC root) {
		List<PC> all = new ArrayList<PC>();
		for (int i = root.n; i < root.n + 12; i++) {
			all.add(fromIndex(Calc.mod(i, 12)));
		}
		return all;
	}
	
	public static int delta(PC a, PC b) {
		int abs = Calc.mod(b.n - a.n, 12);
		if (abs > 6)
			abs -= 12;
		return abs;
	}
	
	public static int absDelta(PC a, PC b) {
		if (Calc.mod(a.n - b.n, 12) < Calc.mod(b.n - a.n, 12)) 
			return Calc.mod(a.n - b.n, 12);
		else
			return Calc.mod(b.n - a.n, 12);
	}
	
	public static int positiveDelta(PC from, PC to) {
		int abs = Calc.mod(to.n - from.n, 12);
		return abs;
	}
	
	public PC up(int by) {
		return fromIndex(Calc.mod(n + by, 12));
	}
	
	public PC down(int by) {
		return fromIndex(Calc.mod(n - by, 12));
	}
	
	public PC up(Interval by) {
		return fromIndex(Calc.mod(n + by.getInterval(), 12));
	}
	
	public PC down(Interval by) {
		return fromIndex(Calc.mod(n - by.getInterval(), 12));
	}
	
	public static PC fromIndex(int index) {
		if (index < 0 || index > 11)
			return null;
		switch (index) {
		case 0:
			return C;
		case 1:
			return CSHARP;
		case 2:
			return D;
		case 3:
			return DSHARP;
		case 4:
			return E;
		case 5:
			return F;
		case 6:
			return FSHARP;
		case 7:
			return G;
		case 8:
			return GSHARP;
		case 9:
			return A;
		case 10:
			return ASHARP;
		case 11:
			return B;
		default:
			return null;
		}
	}
	
	public static PC fromSymbol(String noteSymbol) {
		switch (noteSymbol) {
		case "A":
			return A;
		case "A#":
			return ASHARP;
		case "B":
			return B;
		case "C":
			return C;
		case "C#":
			return CSHARP;
		case "D":
			return D;
		case "D#":
			return DSHARP;
		case "E":
			return E;
		case "F":
			return F;
		case "F#":
			return FSHARP;
		case "G":
			return G;
		case "G#":
			return GSHARP;
		default:
			return null;
		}
	}
}
