package caesura.common;


/**
 * Static utilities to be used with the caesura project.
 * @author oek
 */
public class Utils {

	/**
	 * Returns value that is scaled so that in the range [zero, one] it
	 * will be normalized to [0, 1].
	 * @param zero value from the range corresponding to 0
	 * @param one value from the range corresponding to 1
	 * @param val the value to be normalized
	 * @return value normalized to range [0, 1]
	 */
	public static float normalize(float zero, float one, float val) {
		return (val - zero) / (one - zero);
	}

	/**
	 * Returns value that is scaled linearily from [0, 1] to range [zero, one].
	 * @param zero value for the range corresponding to 0
	 * @param one value for the range corresponding to 1
	 * @param val the value to be scaled
	 * @return value scaled to range [0, 1]
	 */
	public static float scale(float zero, float one, float val) {
		return (one - zero) * val + zero; 
	}

	public static float clip(float f, float lo, float hi) {
		if (f<lo)
			return lo;
		if (f>hi)
			return hi;
		return f;
	}


	/**
	 * Return the smallest 2^x that can fit n.
	 * @param n the number we compare pow2:s to
	 */
	public static int smallestPow2Exponent(int n) {
		int c = 0;
		int s = 1;
		while (s < n) {
			s*=2;
			c++;
		}
		return c;
	}


	/**
	 * Copy n samples from buf_fr to buf_to. If either of the buffers is too
	 * short, copy only as many samples as possible.
	 * @param buf_fr buffer to copy from
	 * @param offset_fr offset to start reading from
	 * @param n number of points
	 * @param buf_to buffer to copy to
	 * @return the number of points actually copied
	 */
	public static void bufferCopy(float[] buf_fr, int offset_fr, int n,
			float[] buf_to) {

		if (buf_fr==null)
			return;

		/*
		 * if we try to start from sub-zero spot, fill with zeroes;
		 */
		int k = 0;
		if (offset_fr<0) {
			while (k < -offset_fr && k<n) {
				buf_to[k] = 0;
				k++;
			}
		}

		/*
		 * check if the buffer we're copying from has enough length,
		 * resize n if needed.
		 */
		int a = offset_fr + n - buf_fr.length;
		if (a>0) n-=a;

		// if destination has enough space, resize n if needed
		int b = n - buf_to.length;
		if (b>0) n -= b; 

		while (k<n) {
			buf_to[k] = buf_fr[offset_fr + k];
			k++;
		}
	} // end of bufferCopy
	
	
	/**
	 * Copy n samples from buf_fr to buf_to. Multichannel version, where:
	 * array[channel][data].
	 * If either of the buffers is too short, copy only as many samples as
	 * possible.
	 * @param buf_fr buffer to copy from
	 * @param offset_fr offset to start reading from
	 * @param n number of points
	 * @param buf_to buffer to copy to
	 * @return the number of points actually copied
	 */
	public static void bufferCopy(float[][] buf_fr, int offset_fr, int n,
			float[][] buf_to) {
		// redirect calls to single channel version
		for (int ch=0; ch<buf_fr.length; ch++) {
			bufferCopy(buf_fr[ch], offset_fr, n, buf_to[ch]);
		}
	}


	public static void bufferClear(float[][] buf) {
		for (int ch=0; ch<buf.length; ch++) {
			for (int k=0; k<buf[ch].length; k++) {
				buf[ch][k] = 0;
			}
		}
	}

	/**
	 * Copy n samples from buf_fr to buf_to. If either of the buffers is too
	 * short, copy only as many samples as possible. Correlate with a envelope
	 * table as copying. Envelope table has to be n samples long, otherwise
	 * a crash may occur.
	 * @param buf_fr buffer to copy from
	 * @param offset_fr offset to start reading from
	 * @param n number of points
	 * @param buf_to buffer to copy to
	 * @param envelope to be correlated with.
	 * @return the number of points actually copied
	 */
	public static int bufferCopy(float[] buf_fr, int offset_fr, int n,
			float[] buf_to, float[] envelope) {

		//jvst.wrapper.VSTPluginAdapter.log("copy: "+ offset_fr + " - " + (offset_fr+n));

		/*
		 * if we try to start from sub-zero spot, fill with zeroes;
		 */
		int k = 0;
		if (offset_fr<0) {
			while (k < -offset_fr && k<n) {
				buf_to[k] = 0;
				k++;
			}
		}
		/*
		 * check if the buffer we're copying from has enough length,
		 * resize n if needed.
		 */
		int a = offset_fr + n - buf_fr.length;
		if (a>0) n-=a;

		// if destination has enough space, resize n if needed
		int b = n - buf_to.length;
		if (b>0) n -= b; 

		while (k<n) {
			buf_to[k] = buf_fr[offset_fr + k] * envelope[k];
			k++;
		}
		return k;
	} // end of bufferCopy

	/**
	 * Add (= sum with the existing) n samples from buf_fr to buf_to.
	 * If either of the buffers is too short, add only as many samples as
	 * possible.
	 * @param buf_fr buffer to copy from
	 * @param offset_fr offset to start reading from
	 * @param n number of points
	 * @param buf_to buffer to copy to
	 * @param offset_to offset in the buf_to for adding
	 */
	public static int bufferAdd(float[] buf_fr, int offset_fr, int n,
			float[] buf_to, int offset_to) {

		/*
		 * check if the buffer we're copying from has enough length,
		 * resize n if needed.
		 */
		int a = offset_fr + n - buf_fr.length;
		if (a>0) n-=a;

		// if destination has enough space
		int b = offset_to + n - buf_to.length;
		if (b>0) n -= b; 

		int k = 0;
		while (k<n) {
			buf_to[offset_to + k] += buf_fr[offset_fr + k];
			k++;
		}
		return k;
	} // end of bufferAdd

	/**
	 * Multiply a complex number with represented as two consecutive floats in an
	 * array, real part being the even number.
	 * @param buf buffer to operate in
	 * @param bin which bin we are multiplying
	 * @param re real part of the number we're multiplying with
	 * @param im imaginary part of the number we're multiplying with
	 */
	public static void complexMultiply(float[] buf, int bin,
			float re, float im) {
		float a = buf[2*bin];
		float b = buf[2*bin+1];
		buf[2*bin] = a*re - b*im;
		buf[2*bin+1] = a*im + b*re;
	}
}
