package caesura.audio;


public interface Segment {

	/**
	 * Do what ever it is to be done to initialise segment. Also save
	 * refrence to material_buf!
	 * @param material_buf
	 * @param scramble float in range [0,1]
	 */
	public void init(float[][] material_buf, float scramble);
	
	/**
	 * Try to prepare n samples to buffer to be ready for reading. Should update
	 * the state which represents how many samples are ready in the buffer.
	 * @param position where to start preparing
	 * @param n how many points to prepare
	 * @param rate what is the step between points
	 */
	public void prepare(float position, int n, float rate);

	/**
	 * @return the position of the highest sample the buffer has been prepared
	 * to.
	 */
	public float preparedTo();
	
	/**
	 * Get sample from buffer at the phead position provided.
	 * @param position at which point the sample should be
	 * @return audio sample at that point
	 */
	public float getSample(int ch, float position);
}
