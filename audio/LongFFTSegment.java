package caesura.audio;

import edu.emory.mathcs.jtransforms.fft.FloatFFT_1D;

import caesura.gui.*;
import caesura.common.*;

/**
 * Takes FFT of the whole grain duration and scrambles phases if needed. To
 * function properly initFFT should be called before using.
 * @author oek
 */
public class LongFFTSegment implements Segment {

	private static final int FFT_MIN_POW2 = 10;
	private static final int FFT_MAX_POW2 = 18;  // ~5 sec
	private static final int BLOCK_SIZE = (int)Math.pow(2, FFT_MAX_POW2);

	private float[][] material_buf;
	private float[][] proc_buf;
	//	private CircularBuffer out_buf;

	private static float[] envelope_buf = null;
	private float prepared_to;

	private int fft_pow;

	private int channels = 0;

	private float scramble = 0;

	private static FloatFFT_1D[] FFT = null;

	public LongFFTSegment() {
		initFFT();
	}

	/**
	 * Initialise FFT-objects, this should be called before attempting to use
	 * LongFFTSegment.
	 */
	public static void initFFT() {
		if (FFT==null) {
			// jvst.wrapper.VSTPluginAdapter.log("init FFT");
			FFT = new FloatFFT_1D[FFT_MAX_POW2 - FFT_MIN_POW2 + 1];
			int k = 0;
			for (int i = FFT_MIN_POW2; i <= FFT_MAX_POW2; i++) {
				int n = (int)Math.pow(2, i);
				FFT[k] = new FloatFFT_1D(n);
				// jvst.wrapper.VSTPluginAdapter.log("n: " + n + " k: " + k);
				k++;
			}
		}
	}

	/**
	 * Initialise LongFFTSegment.
	 */
	public void init(float[][] material, float scramble) {
		//jvst.wrapper.VSTPluginAdapter.log("\n### INITIALIZE ###\n");
		this.material_buf = material;
		this.channels = material.length;
		this.scramble = scramble;
		this.prepared_to = 0;

		// if uninitialized or different number of channels, init
		if (proc_buf==null || (proc_buf.length !=  material_buf.length)) {
			proc_buf = new float[material_buf.length][BLOCK_SIZE];
		}
	}

	/**
	 * Prepare the whole grain into buffer.
	 */
	public void prepare(float position, int n, float rate) {

		int fft_length = (int)Math.pow(2, FFT_MIN_POW2);
		this.fft_pow = 0;

		if (n<BLOCK_SIZE) {
			int b = Utils.smallestPow2Exponent(n);
		} else {
			this.fft_pow = FFT_MAX_POW2 - FFT_MIN_POW2;
		}

		// how high we should prepare samples
		int target = (int)(position + n);

		// if we should prepare more samples
		if (target>prepared_to) {
			Utils.bufferClear(proc_buf);
			Utils.bufferCopy(material_buf, (int)position, n, proc_buf);
			// the fft operation
			operate(proc_buf, n);
			// set buffer
			this.prepared_to = n;
		}
	} // end of prepare

	/**
	 * Do the FFT operations.
	 * @param buf
	 * @param n	block sizw
	 */
	private void operate(float[][] buf, int n) {

		//jvst.wrapper.VSTPluginAdapter.log("### OPERATE");

		// if should scramble
		if (scramble>0) {

			//jvst.wrapper.VSTPluginAdapter.log("### SCRAMBLE");

			//for (int i=0; i<n; i++) {
			//	int j = i*(BLOCK_SIZE/n);
			//	buf[i] *= this.envelope_buf[j];
			//}

			// take FFT
			for (int ch=0; ch < this.channels; ch++) {
				FFT[fft_pow].realForward(buf[ch]);
				// eliminate DC and Nyquist bias
				buf[ch][0] = 0;
				buf[ch][1] = 0;
			}

			// scramble to 1/4 samplerate, that is, to around 10k

			int number_of_rands = (int)(BLOCK_SIZE/4 * scramble);
			jvst.wrapper.VSTPluginAdapter.log("scrmbl: " + scramble
					+ " n: " + number_of_rands);

			for (int i=0; i<number_of_rands; i++) {
				int j = Engine.random.nextInt(BLOCK_SIZE/4 - 1) + 1;
				double a = Math.PI*(Engine.random.nextFloat() -0.5) * scramble;
				float re = (float)Math.cos(a);
				float im = (float)Math.sin(a);
				for (int ch=0; ch<channels; ch++) {
					Utils.complexMultiply(buf[ch], j, re, im);
				}
			}
			// transmute back
			for (int ch=0; ch < this.channels; ch++) {
				FFT[fft_pow].realInverse(buf[ch], true);
			}

		}
	}

	public float preparedTo() {
		return this.prepared_to;
	}

	public float getSample(int ch, float phead) {
		int index = (int)phead;
		float decimal = phead - index;

		float y0 = proc_buf[ch][index];
		float y1 = proc_buf[ch][index+1];
		return (y1-y0)*decimal + y0;
	} // end of getSample

	/**
	 * Initialise the Envelope table for processing.
	 */
	public static void initEnvelopeTable() {
		envelope_buf = new float[BLOCK_SIZE];
		for (int i=0; i<BLOCK_SIZE; i++) {
			double x = Math.PI * 2 * (double)i / (double)BLOCK_SIZE;
			envelope_buf[i] = (float) ((1 - Math.cos(x)) * 0.5);
		}
	} // end of initEnvelope()
}
