package caesura.audio;

import caesura.common.Utils;

/**
 * Caesura grain, gets its audio samples from a class implementing Segment-
 * interface and correlates that with output from it's instance of Envelope.
 * Is in charge of reporting to level up if it should be removed.
 * @author oek
 *
 */
public class Grain {

	private float phead;  // playhead, the current playing position
	private float position;
	private float duration;
	private float rate;
	private float pan;
	private int offset;
	private int channels;

	//private float[] material_buf = null;
	private Segment segment;
	private Envelope envelope;

	/**
	 * Grain has its own segment and envelope
	 */
	public Grain() {
		segment = new LongFFTSegment();
		envelope = new Envelope();
	}

	/**
	 * Adds a new running grain to the engine.
	 * @param position position in samples to start from
	 * @param duration duration of grain in samples
	 * @param rate pitch of the new grain as playbackspeed, 1 being 1:1
	 * @param offset offset for starting grain in block, 0 meaning it starts
	 * from the beginning of block
	 * @param material_buf buffer to read the material from
	 * @param env_left attack parameter value
	 * @param env_right release parameter value
	 * @param pan pan in range [0, 1], 0 being left
	 * @param scramble scramble parameter value
	 */
	public void init(float position, float duration, float rate, int offset,
			float[][] material_buf,
			float env_left, float env_right, float pan, float scramble)
	{
		if (duration<0) duration = 0;
		if (rate<=0) {
			rate = 1;
			duration = 0;
		}

		this.position = position;
		this.duration = duration;

		this.rate = rate;
		this.phead = 0;
		this.pan = pan;

		// offset for first block
		this.offset = offset;

		this.channels = material_buf.length;

		segment.init(material_buf, scramble);
		segment.prepare(this.position, (int)duration, rate);

		envelope.init(env_left, env_right, duration);
	} // end of init

	/**
	 * @return the duration of the grain in samples
	 */
	public float getDuration() {
		return duration;
	}

	/**
	 * Per block operations of a grain.
	 * @param n number of samples to process
	 * @param out_buf where to add the output to
	 * @return true if grain should be removed afterwards
	 */
	public boolean tick(int n, float[][] out_buf) {

		int i = this.offset;
		this.offset = 0;

		// loop for samples
		while (i<n) {
			if (phead < duration && phead < segment.preparedTo()) {

				// correlate with envelope


				if (channels == 1) {
					// TODO
					float env = envelope.getCoef(phead);
					float sample = segment.getSample(0, phead) * env;
					//float lpan = pan < 0.5 ? 1 : Utils.scale(1.f, 0.5f, pan);
					//float rpan = pan > 0.5 ? 1 : Utils.scale(1.f, 0.5f, pan);
					//float lpan = 2*(1-pan);
					//float rpan = pan;
					
					out_buf[0][i] += sample * (1-pan);
					out_buf[1][i] += sample * pan;
				}
				
				//float left = segment.getSample(0, phead);
				//float right = segment.getSample(1, phead);

				//out_buf[0][i] += left*lpan * (1-pan);
				//out_buf[1][i] += sample * pan;

				phead += rate;
				i++;
			} else {
				return true;
			}
		}
		return false;
	} // end of tick
}

