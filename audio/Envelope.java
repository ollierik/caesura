package caesura.audio;

import caesura.gui.*;
import caesura.common.*;


public class Envelope {

	/*
	 * Envelope read points
	 * lo = lowest value of envelope
	 * hi = highest value to envelope
	 */
	
	// left, lower side
	private float env_llo;
	private float env_lhi;

	// right, higher side
	private float env_rlo;
	private float env_rhi;

	// middle
	private float env_mlo;
	private float env_mhi;
	
	private float duration;

	public static final int ENVELOPE_SIZE = 4096;
	private static float envelope[] = null;

	/**
	 * Envelope initialises itself
	 */
	public Envelope() {
		if (envelope == null)
			initTable();
	}
	
	/**
	 * Initialise the Envelope table with half of an Hann-window.
	 */
	public static void initTable() {
		envelope = new float[ENVELOPE_SIZE+1];
		for (int i=0; i<=ENVELOPE_SIZE; i++) {

			double x = Math.PI * (double)i / (double)ENVELOPE_SIZE;
			envelope[i] = (float) ((1 - Math.cos(x)) * 0.5);
		}
	} // end of initEnvelope()

	/**
	 * Initialise envelope state
	 * @param env_left attack parameter from [-1, 1]
	 * @param env_right release parameter from [-1, 1]
	 */
	public void init(float env_left, float env_right, float duration) {

		this.duration = duration;
		float mid_lo;
		float mid_hi;

		/*
		 * CALCULATE INITIAL LIMITS FOR ENVELOPE SIDES
		 * if grain is longer than 300ms, the limit points are set to 150ms
		 * on both sides, otherwise they meet in the middle
		 */
		if (this.duration > 0.3 * 44100) {
			mid_lo = 0.15f * 44100f;
			mid_hi = this.duration - 0.15f * 44100f;
		} else {
			mid_lo = mid_hi = 0.5f * this.duration;
		}

		/*
		 * DECREASE THE SIZE OF THE LIMITS IF NEEDED
		 * higher the env_left, smaller the envelope side, scales as fraction
		 */
		if (env_left > 0.5) {
			//float f = 2.f * (1.f - env_left);
			float f = Utils.normalize(1.0f, 0.5f, env_left);
			mid_lo *= f;
		}
		/*
		 * higher the env_right, smaller the envelope side, scales as fraction
		 */
		if (env_right > 0.5) {
			//float f = 2.f * (1.f - env_right);
			float f = Utils.normalize(0.5f, 1.0f, env_right);
			mid_hi += (this.duration - mid_hi) * f;
		}


		/*
		 * INCREASE THE SIZE OF THE LIMITS IF NEEDED
		 * envelope sides taking the space in the middle
		 */
		float mid_temp = mid_lo; // save the original val of mid_lo for later
		if (env_left < 0) {
			float f = Utils.normalize(0, -0.5f, env_left);
			if (f>1) f = 1;
			/*
			 * increase the size of lower side of the envelope, that is,
			 * raise the limit value with a fraction f of the space between
			 * */
			mid_lo += (mid_hi - mid_lo) * f;
		}
		if (env_right < 0) {
			float f = Utils.normalize(0, -0.5f, env_right);
			if (f>1) f = 1;
			/*
			 * increase the size of higher side of the envelope, that is,
			 * lower the limit value with a fraction f of the space between
			 * */
			mid_hi -= (mid_hi - mid_temp) * f; // use the orig mid_lo
		}

		/*
		 * if envelope sides overlap, they get a common middle point inbetween
		 */
		if (mid_lo>mid_hi) {
			//System.out.println("OVERLAP!");
			mid_lo = mid_hi = (mid_lo + mid_hi) * 0.5f;
		}

		this.env_mlo = (int) (mid_lo);
		this.env_mhi = (int) (mid_hi);
		//System.out.println("mhi: " + this.env_mhi);

		/*
		 * set the control curves for envelopes
		 */
		if (env_left < -1) env_left = -1;
		// only the concave up part
		if (env_left < -0.5) {
			env_llo = 0;
			env_lhi = (int) ((1.5f + env_left) * ENVELOPE_SIZE);
		}
		// the whole envelope
		else if (env_left < 0) {
			env_llo = 0;
			env_lhi = ENVELOPE_SIZE;
		} // only the concave down part
		else if (env_left < 0.5) {
			env_llo = (int) (env_left * ENVELOPE_SIZE);
			env_lhi = ENVELOPE_SIZE;
		} else {
			env_llo = ENVELOPE_SIZE/2;
			env_lhi = ENVELOPE_SIZE;
		}

		/*
		 * Exactly the same for right hand side
		 */
		if (env_right < -1) env_right = -1;	
		if (env_right < -0.5) {
			env_rlo = 0;
			env_rhi = (int) ((1.5f + env_right) * ENVELOPE_SIZE);
		} else if (env_right < 0) {
			env_rlo = 0;
			env_rhi = ENVELOPE_SIZE;
		} else if (env_right < 0.5) {
			env_rlo = (int) (env_right * ENVELOPE_SIZE);
			env_rhi = ENVELOPE_SIZE;
		} else {
			env_rlo = ENVELOPE_SIZE/2;
			env_rhi = ENVELOPE_SIZE;
		}
	}

	/**
	 * Returns envelope coefficient for the specific value that corresponds to the
	 * playhead position of the grain, in the range [0, duration]
	 * @param point value in the range [0, duration]
	 * @return the envelope coefficient for point specified
	 */
	public float getCoef(float point) {
		float env;
		if (point < env_mlo) {
			float f = point / env_mlo;
			int index = (int) (env_llo + (env_lhi - env_llo)*f);
			env = envelope[index];
			env -= envelope[(int)env_llo];
			env /= (envelope[(int)env_lhi] - envelope[(int)env_llo]);

		} else if (point > env_mhi) {
			float f = (point - env_mhi) / (duration - env_mhi);
			f = 1 - f; // invert range
			int index = (int) (env_rlo + (env_rhi - env_rlo)*f);
			env = envelope[index];
			env -= envelope[(int)env_rlo];
			env /= (envelope[(int)env_rhi] - envelope[(int)env_rlo]);
		} else {
			env = 1;
		}
		return env;
	}
}
