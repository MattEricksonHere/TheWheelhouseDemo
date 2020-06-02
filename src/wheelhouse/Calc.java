package wheelhouse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import wheelhouse.tone.Tone;

/**
 * A static utility class for mathematical calculations
 * @author Me
 */
public class Calc {
	
	/**
	 * The total dissonance of a set of tones. The tones' intensities
	 *  are weighed in this formula.
	 *  https://arxiv.org/pdf/1306.1344.pdf pg3 (7)
	 * @param tonesWithHarmonics
	 * @return
	 */
	public static double getTotalDissonance(List<Tone> tonesWithHarmonics) {
		
		final double lStar = 1.0; // Arbitrary reference loudness
		final double alpha = 0.5; // Exponent variable?? Don't know what it does yet
		double multiplier = Math.pow(lStar, 2 * alpha);
		
		//Summation
		final boolean ROOT_ONLY = false;
		double sum = 0;
		for (int i = 0; i < tonesWithHarmonics.size() - 1; i++) {
			for (int j = i + 1; j < tonesWithHarmonics.size(); j++) {
				double loudness1 = tonesWithHarmonics.get(i).getLoudness();
				double loudness2 = tonesWithHarmonics.get(j).getLoudness();
				double loudnessFactor = Math.pow(loudness1 * loudness2, 2);

				double frequency1 = tonesWithHarmonics.get(i).getFrequency();
				double frequency2 = tonesWithHarmonics.get(j).getFrequency();
				double pureDissonance = getPureDyadDissonance(frequency1,frequency2);
				double dissonanceFactor = Math.pow(pureDissonance, (3.0 / alpha));
				
				sum += loudnessFactor * dissonanceFactor;
			}
			if (ROOT_ONLY)
				break;
		}
		
		double exponent = alpha / 3.0;
		return multiplier * Math.pow(sum, exponent);
	}
	
	
	public static double getTotalDissonanceSeparateHarmonics(List<? extends Tone> tones, Spectrum spectrum) {
		
		final boolean VERBOSE = false;
		
		List<List<Tone>> complexes = new ArrayList<List<Tone>>();
		for (Tone tone: tones) {
			complexes.add(tone.getOvertones(spectrum));
		}
		
		// Outer Summation
		final boolean ROOT_ONLY = false;
		double sum = 0;
		for (int i = 0; i < complexes.size() - 1; i++) {
			for (int j = i + 1; j < complexes.size(); j++) {
				List<Tone> harmonics1 = complexes.get(i);
				List<Tone> harmonics2 = complexes.get(j);
				for (int k = 0; k < harmonics1.size(); k++) {
					for (int l = 0; l < harmonics2.size(); l++) {

						double frequency1 = harmonics1.get(k).getFrequency();
						double frequency2 = harmonics2.get(l).getFrequency();
						double pureDissonance = getPureDyadDissonance(frequency1,frequency2);
						double dissonanceFactor = Math.pow(pureDissonance, 6);
						if (dissonanceFactor == 0) continue; // These frequencies are too far apart to create dissonance
						
						double loudness1 = harmonics1.get(k).getLoudness();
						double loudness2 = harmonics2.get(l).getLoudness();
						double loudnessFactor = Math.pow(loudness1 * loudness2, 2);
						
						double pairDissonance = loudnessFactor * dissonanceFactor;
						sum += pairDissonance;
						
						if (VERBOSE) {
							System.out.println(i + "." + k + "x" + j + "." + l + " | " 
									+ pairDissonance + "                   = " + dissonanceFactor + "*" + loudnessFactor);
						}
					}
				}
			}
			if (ROOT_ONLY)
				break;
		}
		return Math.pow(sum, (1.0/6.0));
	}
	
	/**
	 * The dissonance of a dyad of pure tones of frequencies
	 *  https://arxiv.org/pdf/1306.1344.pdf pg1 (1)
	 * @param freq1
	 * @param freq2
	 * @return Dissonance between pure tones of freq1 and freq2 normalized arbitrarily to 1.0
	 */
	public static double getPureDyadDissonance(double freq1, double freq2) {
		return plompLeveltFunction(getFreqDifferencePerCB(freq1, freq2));
	}

	public static double getFreqDifferencePerCB(double freq1, double freq2) {
		double freqDifference = Math.abs(freq2 - freq1);
		double avgFreq = (freq1 + freq2) / 2.0;
		double criticalBandwidth = getCriticalBandwitdh(avgFreq);
		return (freqDifference*1.0) / criticalBandwidth;
	}
	
	/**
	 * “Tonal dissonance” between two pure tones
	 * https://arxiv.org/pdf/1306.1344.pdf pg2 (3)
	 * @param freqDifferencePerCB is the frequency difference / the critical bandwidth
	 * @return Tonal dissonance normalized arbitrarily to 1.0
	 */
	public static double plompLeveltFunction(double freqDifferencePerCB) {
		// https://arxiv.org/pdf/1306.1344.pdf, (3)
		if (freqDifferencePerCB <= 0) return 0;
		if (freqDifferencePerCB >= 1.2) return 0;
		
		return 4.906 * freqDifferencePerCB * Math.pow((1.2-freqDifferencePerCB),4);
	}

	protected static double getInverseCriticalBandwitdh(double frequency) {
		return Math.pow(frequency / .0115587631, 1.32540605);
	}

	/**
	 * @param frequency of which to get the critical bandwidth
	 * @return the critical bandwidth in Hz
	 */
	protected static double getCriticalBandwitdh(double frequency) {
		// https://en.wikipedia.org/wiki/Bark_scale
		
		return .0115587631 * Math.pow(frequency, 1.32540605);
		
//		if (frequency < 20) {
//			return -1;
//		} else if (frequency < 100) {
//			return 80;
//		} else if (frequency < 200) {
//			return 100;
//		} else if (frequency < 300) {
//			return 100;
//		} else if (frequency < 400) {
//			return 100;
//		} else if (frequency < 510) {
//			return 110;
//		} else if (frequency < 630) {
//			return 120;
//		} else if (frequency < 770) {
//			return 140;
//		} else if (frequency < 920) {
//			return 150;
//		} else if (frequency < 1080) {
//			return 160;
//		} else if (frequency < 1270) {
//			return 190;
//		} else if (frequency < 1480) {
//			return 210;
//		} else if (frequency < 1720) {
//			return 240;
//		} else if (frequency < 2000) {
//			return 280;
//		} else if (frequency < 2320) {
//			return 320;
//		} else if (frequency < 2700) {
//			return 380;
//		} else if (frequency < 3150) {
//			return 450;
//		} else if (frequency < 3700) {
//			return 550;
//		} else if (frequency < 4400) {
//			return 700;
//		} else if (frequency < 5300) {
//			return 900;
//		} else if (frequency < 6400) {
//			return 1100;
//		} else if (frequency < 7700) {
//			return 1300;
//		} else if (frequency < 9500) {
//			return 1800;
//		} else if (frequency < 12000) {
//			return 2500;
//		} else if (frequency < 15500) {
//			return 3500;
//		} else  {
//			return -1;
//		}
	}
	
	public static List<Double> getBarks() {
		return Arrays.asList(
				20.0,100.0,200.0,300.0,400.0,510.0,630.0,770.0,920.0,
				1080.0,1270.0,1480.0,1720.0,2000.0,2320.0,2700.0,3150.0,
				3700.0,4400.0,5300.0,6400.0,7700.0,9500.0,12000.0,15500.0);
	}

		
//		if (frequency < 20) {
//			return -1;
//		} else if (frequency < 100) {
//			return 80;
//		} else if (frequency < 200) {
//			return 100;
//		} else if (frequency < 300) {
//			return 100;
//		} else if (frequency < 400) {
//			return 100;
//		} else if (frequency < 510) {
//			return 110;
//		} else if (frequency < 630) {
//			return 120;
//		} else if (frequency < 770) {
//			return 140;
//		} else if (frequency < 920) {
//			return 150;
//		} else if (frequency < 1080) {
//			return 160;
//		} else if (frequency < 1270) {
//			return 190;
//		} else if (frequency < 1480) {
//			return 210;
//		} else if (frequency < 1720) {
//			return 240;
//		} else if (frequency < 2000) {
//			return 280;
//		} else if (frequency < 2320) {
//			return 320;
//		} else if (frequency < 2700) {
//			return 380;
//		} else if (frequency < 3150) {
//			return 450;
//		} else if (frequency < 3700) {
//			return 550;
//		} else if (frequency < 4400) {
//			return 700;
//		} else if (frequency < 5300) {
//			return 900;
//		} else if (frequency < 6400) {
//			return 1100;
//		} else if (frequency < 7700) {
//			return 1300;
//		} else if (frequency < 9500) {
//			return 1800;
//		} else if (frequency < 12000) {
//			return 2500;
//		} else if (frequency < 15500) {
//			return 3500;
//		} else  {
//			return -1;
//		}
	
	public static double forceRange(double n, double min, double max) {
		if (n < min)
			return min;
		if (n > max)
			return max;
		return n;
	}
	
	public static int forceRangeI(int n, int min, int max) {
		if (n < min)
			return min;
		if (n > max)
			return max;
		return n;
	}
	
	protected static int gcd(int a, int b) {
	    while (b > 0) {
	        int temp = b;
	        b = a % b; // % is remainder
	        a = temp;
	    }
	    return a;
	}

	protected static int gcd(List<Integer> input) {
	    int result = input.get(0);
	    for (int i = 1; i < input.size(); i++)
	    	result = gcd(result, input.get(i));
	    return result;
	}
	
	protected static int lcm(int a, int b) {
	    return a * (b / gcd(a, b));
	}

	protected static int lcm(List<Integer> input) {
	    int result = input.get(0);
	    for (int i = 1; i < input.size(); i++)
	    	result = lcm(result, input.get(i));
	    return result;
	}
	
	protected static int lcd(List<Ratio> ratios) {
		List<Integer> denominators = new ArrayList<Integer>();
		for (Ratio r : ratios)
			denominators.add(r.getDenominator());
		return Calc.lcm(denominators);
	}

    public static List<Integer> factors(int number) {
    	int upperlimit = (int)(Math.sqrt(number));
        ArrayList<Integer> factors = new ArrayList<Integer>();
        for(int i=1;i <= upperlimit; i+= 1){
            if(number%i == 0){
                factors.add(i);
                if(i != number/i){
                    factors.add(number/i);
                }
            }
        }
        Collections.sort(factors);
        return factors;
    }

    public static List<Integer> primeFactors(int number) {
        List<Integer> factors = new ArrayList<Integer>();
        for (int i = 2; i <= number; i++) {
            while (number % i == 0) {
                factors.add(i);
                number /= i;
            }
        }
        return factors;
    }
	
	public static class Ratio {
		private int numerator, denominator;
		public Ratio(int numerator, int denominator) {
			this.numerator = numerator;
			this.denominator = denominator;
		}
		public int getNumerator() {
			return numerator;
		}

		public void setNumerator(int numerator) {
			this.numerator = numerator;
		}

		public int getDenominator() {
			return denominator;
		}

		public void setDenominator(int denominator) {
			this.denominator = denominator;
		}
		
		public boolean equals(Ratio r) {
			return (getNumerator()*r.getDenominator() == r.getNumerator()*getDenominator());
		}
		
		public static List<Integer> getMultiRatio(List<Ratio> ratios) {
			List<Integer> denominators = new ArrayList<Integer>();
			for (Ratio ratio : ratios) {
				ratio.simplify();
				denominators.add(ratio.getDenominator());
			}
			int lcm = Calc.lcm(denominators);
			List<Integer> normalizedNumerators = new ArrayList<Integer>();
			for (Ratio ratio : ratios) {
				ratio.simplify();
				normalizedNumerators.add(ratio.getNumerator()*(lcm/ratio.getDenominator()));
			}
			return normalizedNumerators;
		}
		
		public void simplify() {
			int gcd = Calc.gcd(numerator, denominator);
			numerator /= gcd;
			denominator /= gcd;
		}

		public static Ratio getResultingRatio(Ratio a, Ratio b) {
			// (a/b)/(c/d) = (ad)/(bc)
			int lcm = Calc.lcm(Arrays.asList(a.getNumerator(), a.getDenominator(), b.getNumerator(), b.getDenominator()));
			int num = (lcm * a.getNumerator()) * (lcm * b.getDenominator());
			int den = (lcm * a.getDenominator()) * (lcm * b.getNumerator());
			int gcd = Calc.gcd(num, den);
			if (gcd > 1) {
				num /= gcd;
				den /= gcd;
			}
			return new Ratio(num, den);
		}
	}
	
	public static class Vector1D {
		public double dir, mag;
		
		public Vector1D(double dir, double mag) {
			this.dir = dir;
			this.mag = mag;
		}
	}
	
	public static double log(double base, double arg) {
		return (Math.log(arg) / Math.log(base));
	}
	
	public static List<Double> log(int base, List<Integer> args) {
		List<Double> logs = new ArrayList<Double>();
		for (double arg : args)
			logs.add(Math.log(arg) / Math.log(base));
		return logs;
	}
	
	public static double log2(double arg) {
		return log(2, arg);
	}
	
	public static List<Double> log2(List<Integer> args) {
		return log(2, args);
	}
	
	public static double sum(List<Double> values) {
		double sum = 0;
		for (double value : values)
			sum += value;
		return sum;
	}
	
	public static int sign(boolean bool) {
		if (bool)
			return 1;
		return -1;
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	public static int round(double value) {
	    return (int)Math.round(value);
	}
	
	public static int mod(int x, int n) {
		int r = x % n;
		if (r < 0) {
		    r += n;
		}
		return r;
	}
	
	public static double mod(double x, double n) {
		double r = x % n;
		if (r < 0.0) {
		    r += n;
		}
		return r;
	}
	
	public static double avg(List<Double> nums) {
		double sum = 0;
		for (double n: nums)
			sum += n;
		return sum / nums.size();
	}
	
	public static double asinh(double x) {
		return Math.log(x + Math.sqrt(x*x + 1.0));
	}
	
	public static double random() {
		return Math.random();
	}
	
	public static double normRandom(double mean, double stdev) {
		return ((new Random()).nextGaussian() * stdev) + mean;
	}
}
