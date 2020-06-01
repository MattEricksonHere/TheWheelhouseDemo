package wheelhouse;

import java.util.ArrayList;
import java.util.List;

public class Spectrum {

	List<Double> amplitudes = new ArrayList<Double>();
	
	public Spectrum(List<Double> amplitudes) {
		this.amplitudes = amplitudes;
	}

	public List<Double> getAmplitudes() {
		return amplitudes;
	}

	public int getSize() {
		return amplitudes.size();
	}
	
	public double getAmplitudeForHarmonic(int harmonic) {
		if (harmonic > amplitudes.size())
			return 0;
		else
			return amplitudes.get(harmonic);
	}
	
	public static Spectrum SAW = getSaw(8);
	private static Spectrum getSaw(int harmonics) {
		List<Double> amplitudes = new ArrayList<Double>();
		for (int i = 0; i < harmonics; i++) {
			amplitudes.add(1.0 / (i + 2));
		}
		return new Spectrum(amplitudes);
	}
	
	public static Spectrum FLAT = getFlat(8);
	private static Spectrum getFlat(int harmonics) {
		List<Double> amplitudes = new ArrayList<Double>();
		for (int i = 0; i < harmonics; i++) {
			amplitudes.add(1.0);
		}
		return new Spectrum(amplitudes);
	}
}
