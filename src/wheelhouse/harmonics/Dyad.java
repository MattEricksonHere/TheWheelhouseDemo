package wheelhouse.harmonics;

import wheelhouse.Calc;
import wheelhouse.Calc.Vector1D;
import wheelhouse.tone.Tone;

public class Dyad {
	
	protected double freq1;
	protected double freq2;
	protected double avgFreq;
	
	protected double loudness1;
	protected double loudness2;
	protected double meanLoudness;
	
	protected double con; // pure tones are consonant only if they are unison
	protected double dis;
	protected double freqDifPerCB;
	
	public Dyad(Tone t1, Tone t2) {
		this.freq1 = t1.getFrequency();
		this.freq2 = t2.getFrequency();
		this.avgFreq = ((freq1 + freq2) / 2.0);
		
		this.loudness1 = t1.getLoudness();
		this.loudness2 = t2.getLoudness();
		this.meanLoudness = Calc.forceRange(((loudness1 + loudness2) / 2.0), 0, 1);
		
		this.freqDifPerCB = Calc.getFreqDifferencePerCB(freq1, freq2);
		this.dis = Calc.plompLeveltFunction(freqDifPerCB) * meanLoudness;

		if (freqDifPerCB < .25){
			this.con = (1 - dis) * meanLoudness;
//			System.out.println(this);
		}
		else
			this.con = 0;
	}
	
	@Override
	public String toString() {
		int RND = 2;
		return "[" + Calc.round(freq1, RND) + "," + Calc.round(freq2, RND) + "]"
					+ ", diff=" +  Calc.round(freqDifPerCB, RND) + ""
					+ " dis=" +  Calc.round(dis, RND) + ""
					+ ", con=" +  Calc.round(con, RND);
	}

	public double getFreq1() {
		return freq1;
	}

	public double getFreq2() {
		return freq2;
	}

	public double getCon() {
		return con;
	}

	public double getDis() {
		return dis;
	}

	public double getAvgFreq() {
		return avgFreq;
	}

	public double getMeanLoudness() {
		return meanLoudness;
	}

	public double getFreqDifPerCB() {
		return freqDifPerCB;
	}
	
	public Vector1D getDissonanceVector() {
		return new Vector1D(getAvgFreq(), getDis());
	}
}