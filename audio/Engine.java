package caesura.audio;


import java.util.Random;

/**
 * Engine is the heart of granular synth. It schedules, creates,
 * runs and removes the grains.
 * @author oek
 */
public class Engine {

	public static final int MAX_GRAINS = 40;

	private Caesura plugin;
	private static int CHANNELS; // number of channels

	private Grain[] grains;
	public static Random random = new Random();

	private int list_1st_inactive = 0; // 1st inactive grain in list

	/*
	 * Grains are arranged 
	 * list that holds indices to grains
	 * n < list_1st_inactive ? active grain : inactive grain
	 */
	private int[] list;

	private float[][] material_buf; // buffer that holds source material

	private int time_next_grain = 0; // time to grain in samples
	private float interval = 22050;

	private float duration = 22050;
	private float pitch = 0;
	private float position = 0;
	private float crawl = 0;

	private float env_left = 0.5f;
	private float env_right = 0.5f;
	
	private float pan = 0.5f;

	private float irregularity = 0;
	private float displacement = 0;
	private float detune = 0;
	private float width = 0;
	private float scramble = 0;

	private double scale_basis;
	
	private Allocator grain_allocator;

	/**
	 * @param plugin refrence to the Caesura plugin
	 * @param channels number of channels
	 */
	public Engine(Caesura plugin, int channels) {

		this.plugin = plugin;
		CHANNELS = channels;
		// malloc for grains and list
		grains = new Grain[MAX_GRAINS];
		Envelope.initTable();

		list = new int[MAX_GRAINS];
		// call grain ctors and add 
		for (int i=0; i<grains.length; i++) {
			grains[i] = new Grain();
			list[i] = i;
		}

		grain_allocator = new Allocator(MAX_GRAINS);
		this.scale_basis = Math.pow(2.0, 1.0/12.0);
	}
	
	/**
	 * Remove all running grains
	 */
	public void clear() {
		list_1st_inactive = 0;
	}


	/**
	 * Add new Grain
	 * @param offset
	 */
	public void grainAdd(int offset)
	{
		int a = grain_allocator.add();
		if (a>=0)
		{
			Grain g = grains[a];
			this.list_1st_inactive++;

			////////////
			// CHECKS //
			////////////

			float displc = random.nextFloat()*2 - 1;
			float ratio = random.nextFloat();

			float pos = position + displacement * (displc*ratio);
			if (pos<0)
				pos = 0;
			float dur = duration + displacement * (displc * (1-ratio));
			if (dur<0)
				dur = 0;
			
			float rate = pitch + detune * (random.nextFloat()*2 - 1);
			
			rate = (float)Math.pow(scale_basis, (double)rate);
			if (rate<0) {
				rate = 1;
				dur = 0;
			}
			
			float pan = this.pan + (random.nextFloat() -0.5f) * width;
			if (pan>1) pan = 1;
			else if (pan<0) pan = 0;

			// prepare the new grains
			g.init(pos, dur, rate, offset, material_buf,
					env_left, env_right, pan, scramble);
			//taso.drawEnvelope(g);
		}
	}

	/**
	 * Remove grain
	 * @param r number of grain to be removed
	 */
	public void grainRemove(int r)
	{
		grain_allocator.remove(r);
	}

	/**
	 * @return number of grains in use
	 */
	public int grainsInUse() {
		return list_1st_inactive;
	}

	/**
	 * Per block operations of a Engine. Schedule new grains and run them.
	 * @param n number of samples to process
	 * @param out_buf the buffer to where the output signal is created
	 */
	public void tick(int n, float[][] out_buf)
	{
		//jvst.wrapper.VSTPluginAdapter.log("TICKTOCK\n\n\n");
		// TIMER FUNCTIONS
		int i;
		if (this.interval > 0) // if interval <=0, nothing happens
		{
			for (i=0; i < n; i++)
			{
				this.time_next_grain--;

				// increment position according to velocity
				this.position += this.crawl;
				if (this.position < 0)
					this.position = 0;

				if (this.time_next_grain <= 0)
				{
					this.grainAdd(i); // i is offset in time for grain
					this.time_next_grain = this.scheduleGrain();
				}
			}
		}
		if (this.crawl!=0) {
			plugin.updatePosition(position);
		}

		// ZERO THE BUFFER
		for (int ch=0; ch<CHANNELS; ch++) {
			for (i=0; i<n; i++) {
				out_buf[ch][i] = 0;
			}
		}

		// ACTUAL GRAIN FUNCTION CALLS
		i = 0;
		while (i < grain_allocator.getNumberOfActives())
		{
			
			//Grain g = this.grains[this.list[i]];
			int k = grain_allocator.getAllocation(i);
			Grain g = this.grains[k];
			boolean result = g.tick(n, out_buf);
			if (result) { // true if grain should be removed
				grain_allocator.remove(i);
			}
			else {
				// only increment if not removed,
				// as removing decreases the size with 1
				i++;
			}
		} // while grains to process

		// TODO?
		// COMPRESS
		// calc RMS
		/*
		float rms = 0;
		for (i=0; i<n; i++)
		{
			rms += out_buf[i]*out_buf[i];
		}
		rms = (float)Math.sqrt(rms);
		float target_level = 1;
		if (rms>threshold) {
			target_level = threshold + (rms - threshold)
					/ (float)Math.sqrt(grainsInUse());
			for (i=0; i<n; i++) {
				if (this.level<target_level)
					this.level+=level_increment;
				if (this.level>target_level)
					this.level+=level_increment;
				out_buf[i] *= rms/this.level;
			}
		}

		// Normalize to range [-1,1]
		for (i=0; i<n; i++) {
			float val = out_buf[i];
			if (val>1.0) val= 1.0f;
			if (val<-1.0) val= -1.0f;
			out_buf[i] = val;
		}
		 */
	}

	/**
	 * Schedule new grain to be added.
	 * @return the time in samples to new grain
	 */
	private int scheduleGrain() {
		float time = this.interval + this.irregularity
				* (random.nextFloat()*2 - 1);
		return (int) time;
	}

	/**
	 * Set new material buffer and remove all grains.
	 * @param buf new material
	 */
	public void setMaterialBuffer(float[][] buf)
	{
		this.material_buf = buf;
		this.clear();
	}

	/////////////////////////////////////////
	//SET BASIS 
	/////////////////////////////////////////
	
	
	/**
	 * @param interval in samples
	 */
	public void setInterval(float interval) {
		if (interval<0)
			interval = 1;
		this.interval = interval;
	}
	/**
	 * @param duration in samples
	 */
	public void setDuration(float duration) {
		this.duration = duration;
	}
	/**
	 * @param pitch in half-steps
	 */
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	/**
	 * @param position in samples
	 */
	public void setPosition(float position) {
		if (position>=0) {
			this.position = position;
		}
	}
	/**
	 * @param crawl as coefficient
	 */
	public void setCrawl(float crawl) {
		this.crawl = crawl * 0.01f;
	}
	/**
	 * @param env_left in range [-1, 1]
	 */
	public void setEnvLeft(float env_left) {
		//	jvst.wrapper.VSTPluginAdapter.log("ENVL: " + env_left);
		this.env_left = env_left;
	}
	/**
	 * @param env_right in range [-1, 1]
	 */
	public void setEnvRight(float env_right) {
		//	jvst.wrapper.VSTPluginAdapter.log("ENVR: " + env_right);
		this.env_right = env_right;
	}
	/**
	 * @param pan in range [0, 1]
	 */
	public void setPan(float pan) {
		this.pan = pan;
		//jvst.wrapper.VSTPluginAdapter.log("PAN: " + this.pan);
	}
	/**
	 * @param irregularity maximum offset in samples
	 */
	public void setIrregularity(float irregularity) {
		this.irregularity = irregularity;
	}
	/**
	 * @param displacement maximum offset in samples
	 */
	public void setDisplacement(float displacement) {
		this.displacement = displacement;
	}
	/**
	 * @param detune maximum offset in half-steps
	 */
	public void setDetune(float detune) {
		this.detune = detune;
	}
	/**
	 * @param width maximum offset in range [0, 1]
	 */
	public void setWidth(float width) {
		this.width = width;
	}
	/**
	 * @param scramble in range [0, 1]
	 */
	public void setScramble(float scramble) {
		this.scramble = scramble;
	}
	/*
	public static void main(String[] args) {
		Engine e = new Engine(null, 2);
		e.setEnvLeft(0.5f);
		e.setEnvRight(0.5f);
		e.grainAdd(0);
	}
	*/
	
}
