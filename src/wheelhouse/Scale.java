package wheelhouse;
import java.util.List;
import java.util.Arrays;

public class Scale {
	
	public enum ScaleType{
		MAJOR (Arrays.asList(Interval.MAJ2ND_ASC, Interval.MAJ3RD_ASC, Interval.PER4TH_ASC, Interval.PER5TH_ASC, Interval.MAJ6TH_ASC, Interval.MAJ7TH_ASC)),
		MINOR (Arrays.asList(Interval.MAJ2ND_ASC, Interval.MIN3RD_ASC, Interval.PER4TH_ASC, Interval.PER5TH_ASC, Interval.MIN6TH_ASC, Interval.MIN7TH_ASC));
		
		private List<Interval> intervals;

		public List<Interval> getIntervals() {
			return intervals;
		}

		ScaleType (List<Interval> intervals) {
	    	this.intervals = intervals;
	    }
	}
}
