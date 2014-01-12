package caesura.audio;

import jvst.wrapper.*;
import jm.util.*;
import java.io.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import caesura.gui.*;
import caesura.common.*;

public class Caesura extends VSTPluginAdapter {

	/*
	 * Constants for parameters
	 */
	public static final int P_INTERVAL = 0;
	public static final int P_DURATION = 1;
	public static final int P_PITCH = 2;
	public static final int P_POSITION = 3;
	public static final int P_CRAWL = 4;
	public static final int P_ENV_L = 5;
	public static final int P_ENV_R = 6;
	public static final int P_PAN = 7;
	public static final int P_IRREGULARITY = 8;
	public static final int P_DISPLACEMENT = 9;
	public static final int P_DETUNE = 10;
	public static final int P_WIDTH = 11;
	public static final int P_SCRAMBLE = 12;

	public static int NUM_PARAMS = 13;

	/**
	 * Short parameter names
	 */
	public static String[] PARAM_NAMES = new String[] {
		"interva", "duratio", "pitch", "positio", "crawl",
		"attack", "release", "pan",
		"irregul", "displac", "detune", "width", "scrambl",
	};

	/**
	 * Parameter labels
	 */
	public static String[] PARAM_LABELS = new String[] {
		"ms", "ms", "steps", "s", "%",
		"", "", "",
		"ms", "ms", "step", "%", "%",
	};

	/**
	 * Low end of the parameter range
	 */
	public static float[] PARAM_RANGE_LOW = new float[] {
		0, 0, -12, 0, -100,
		-1, -1, -1,
		0, 0, 0, 0, 0,
	};

	/**
	 * High end of the parameter range
	 */
	public static float[] PARAM_RANGE_HIGH = new float[] {
		5900, 5900, 12, 30, 100,
		1, 1, 1,
		300, 300, 12, 100, 100
	};

	/**
	 * Default program
	 */
	private float[][] programs = new float[][] {
			{ 0.2f, 0.15f, 0.5f, 0.0f, 0.5f,
				0.5f, 0.5f, 0.5f,
				0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
				0.0f}
	};

	private int currentProgram = 0;

	/**
	 * Samplerate
	 */
	public static float SAMPLERATE = 44100;

	private CaesuraGUI gui;

	private AudioMaterial audioMaterial = new AudioMaterial(this);
	private Engine engine;

	/**
	 * @param wrapper required by jVSTwRapper
	 */
	public Caesura(long wrapper) {
		super(wrapper);
		
		jvst.wrapper.VSTPluginAdapter.log("#########################\n" +
				"MOI\n\n\n\n\n");
		
		currentProgram = 0;

		//communicate with the host
		this.setNumInputs(0);// mono input
		this.setNumOutputs(2);// mono output
		this.canProcessReplacing(true);//mandatory for vst 2.4!
		this.setUniqueID(9876543);//random unique number registered at steinberg (4 byte)
		this.isSynth(true);

		//this.canMono(false);

		// create only 2 channels
		engine = new Engine(this, 2);

		// Set all parameters
		 
		for (int i=0; i<NUM_PARAMS; i++) {
			update(i);
		}
		
		setMaterialPath("D:\\input.wav");
	}

	/**
	 * Let the craw update the position
	 * @param val new position
	 */
	public void updatePosition(float val) {
		val /= (SAMPLERATE * PARAM_RANGE_HIGH[P_POSITION]);
		setParameterAutomated(P_POSITION, val);
		((CaesuraGUI)gui).update(P_POSITION);
	}
	
	/**
	 * Update the parameter by calling the corresponding method from Engine.
	 * @param index of the parameter to update
	 */
	private void update(int index) {

		if (index<NUM_PARAMS) {

			float val = programs[currentProgram][index];
			float low = PARAM_RANGE_LOW[index];
			float high = PARAM_RANGE_HIGH[index];

			switch (index) {
			case (P_INTERVAL):
				engine.setInterval( Utils.scale(
						low, high * SAMPLERATE * 0.001f, val) );
			break;
			case (P_DURATION):
				engine.setDuration( Utils.scale(
						low, high * SAMPLERATE * 0.001f, val) );
			break;
			case (P_PITCH):
				engine.setPitch( Utils.scale( low, high, val) );
			break;
			case (P_POSITION):
				engine.setPosition( Utils.scale( low, high * SAMPLERATE, val) );
			break;
			case (P_CRAWL):
				engine.setCrawl( Utils.scale( low, high, val) );
			break;
			case (P_ENV_L):
				engine.setEnvLeft(  Utils.scale( low, high, val) );
			break;
			case (P_ENV_R):
				engine.setEnvRight( Utils.scale( low, high, val) );
			break;
			case (P_PAN):
				engine.setPan( val ); // don't scale
			break;
			case (P_IRREGULARITY):
				engine.setIrregularity( 
						Utils.scale( low, high * 0.001f * SAMPLERATE, val) );
			break;
			case (P_DISPLACEMENT):
				engine.setDisplacement(
						Utils.scale( low, high * 0.001f * SAMPLERATE, val) );
			break;
			case (P_DETUNE):
				engine.setDetune( Utils.scale( low, high, val) );
			break;
			case (P_WIDTH):
				engine.setWidth( Utils.scale( low, high*0.01f, val));
			break;
			case (P_SCRAMBLE):
				engine.setScramble( Utils.scale( low, high*0.01f, val));
			break;
			default:
			}
		}
	}

	/**
	 * Poll for properties. TODO better!
	 */
	public int canDo(String feature) {
		// the host asks us here what we are able to do
		int ret = CANDO_NO;
		if (feature.equals(CANDO_PLUG_1_IN_1_OUT)) ret = CANDO_YES;
		if (feature.equals(CANDO_PLUG_PLUG_AS_CHANNEL_INSERT)) ret = CANDO_YES;
		if (feature.equals(CANDO_PLUG_PLUG_AS_SEND)) ret = CANDO_NO;

		//log("canDo: " + feature + " = " + ret);
		return ret;
	}

	public String getProductString() {
		return "caesura";
	}

	public String getEffectName() {
		return "caesura";
	}

	public String getProgramNameIndexed(int category, int index) {
		return "program: cat: " + category + ", " + index;
	}

	public String getVendorString() { return "http://jvstwrapper.sourceforge.net/"; }

	public float[][] getMaterialBuffer() {
		return audioMaterial.getMaterial();
	}
	
	public int getNumParams() {
		return NUM_PARAMS;
	}

	public int getNumPrograms() {
		return programs.length;
	}

	public float getParameter(int index) {
		if (index < programs[currentProgram].length)
			return programs[currentProgram][index];
		return 0.0f;
	}

	public String getParameterDisplay(int index) {
		// check for pan
		if (index==P_PAN) {
			float f = this.getParameter(P_PAN);
			if (f>0.5f) {
				f = Utils.normalize(0.5f, 1f, f);
				f = ((int)(100*f))/100.0f;
				return "" + f;
			} else if (f==0.5f) {
				return "CENTER";
			} else {
				f = Utils.normalize(0.5f, 0f, f);
				f = ((int) (100*f))/100.0f;
				return "" + f;
			}
		}
		// otherwise
		if (index < programs[currentProgram].length) {
			float k = PARAM_RANGE_HIGH[index] - PARAM_RANGE_LOW[index];
			k = (programs[currentProgram][index] * k + PARAM_RANGE_LOW[index]);
			k = ((int) (100*k))/100.0f;
			return "" + k;
		}
		return "0.0";
	}

	public String getParameterLabel(int index) {
		// check for pan
		if (index==P_PAN) {
			float f = this.getParameter(P_PAN);
			if (f>0.5f) {
				return "% R";
			} else if (f==0.5f) {
				return "";
			} else {
				return "% L";
			}
		}
		// otherwise
		else if (index < PARAM_LABELS.length)
			return PARAM_LABELS[index];
		return "";
	}

	public String getParameterName(int index) {
		if (index < PARAM_NAMES.length)
			return PARAM_NAMES[index];
		return "param: " + index;
	}

	public float getParameterRangeLow(int index) {
		if (index<NUM_PARAMS) {
			return PARAM_RANGE_LOW[index];
		}
		return 0.0f;
	}

	public float getParameterRangeHigh(int index) {
		if (index<NUM_PARAMS) {
			return PARAM_RANGE_HIGH[index];
		}
		return 0.0f;
	}

	public int getProgram() {
		return currentProgram;
	}

	public String getProgramName() {
		return "program " + currentProgram;
	}

	public int getPlugCategory() {
		//log("getPlugCategory");
		return PLUG_CATEG_EFFECT;
	}

	public boolean setBypass(boolean value) {
		//do not support soft bypass!
		return false;
	}

	/**
	 * Attempt to load new material.
	 * @param path
	 */
	public void setMaterialPath(String path) {
		this.audioMaterial.readFile(path);
		float[][] buf = audioMaterial.getMaterial();
		engine.setMaterialBuffer(buf);
	}
	
	/**
	 * @return true if material is loaded
	 */
	public boolean isMaterialLoaded() {
		return (audioMaterial.getMaterial()!=null);
	}
	
	/**
	 * Set new parameter value
	 */
	public void setParameter(int index, float value) {
		//log("i: " + index + "v: " + value);
		programs[currentProgram][index] = value;
		update(index);
	}

	/**
	 * Set new program
	 */
	public void setProgram(int index) {
		currentProgram = index;
		//		update();
	}

	public void setProgramName(String name) {
		// Ignore so far...
	}

	/**
	 * Set new sample rate
	 */
	public void setSampleRate(float sr) { 
		SAMPLERATE = sr;
	}

	/**
	 * Set the refrence gui
	 * @param gui refrence to gui
	 */
	public void setGUI(CaesuraGUI gui) {
		this.gui = gui;
	}
	
	/**
	 * @return material length in seconds
	 */
	public float getMaterialLengthInSecs() {
		float[][] buf = audioMaterial.getMaterial();
		if (buf!=null) {
			float f = buf[0].length/this.getSampleRate();
			return f;
		}
		return PARAM_RANGE_HIGH[P_POSITION];
	}
	
	public boolean string2Parameter(int index, String value) {
		try {
			if (value != null) this.setParameter(index, Float.parseFloat(value));
			return true;
		}
		catch(Exception e) {   //ignore
			return false;
		}
	}

	/**
	 * This is called once every block. Call engines tick.
	 */
	public void processReplacing(float[][] inputs, float[][] outputs,
			int n) {

		if (audioMaterial.isSet())
		{
			engine.tick(n, outputs);
		} else {
			for (int ch=0; ch<outputs.length; ch++) {
				for (int k = 0; k < n; k++) {
					outputs[ch][k] = 0;
				} // streams
			} // channels
		} // no data
	} // end of processReplacing 
}
