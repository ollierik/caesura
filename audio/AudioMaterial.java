package caesura.audio;

import java.io.File;
import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import jm.util.Read;
import jm.audio.io.*;

import caesura.common.*;

/**
 * Part of Caesura-project.
 * Class to load and hold WAV-formatted audio material with
 * sampling rate of 44100. Currently the material will be truncated to the first
 * 30 seconds because of the GUI. Two channels will be mixed to one. More than
 * 2 channels will not load.
 * @author oek
 *
 */
public class AudioMaterial {

	private String path;

	private Caesura plugin;
	private float[][] data;
	// TODO changing samplerate
	private AudioFormat dataFormat; // not used for now, will implement later
	
	/**
	 * Create AudioMaterial
	 * @param plugin refrence to the plugin class
	 */
	public AudioMaterial(Caesura plugin) {
		this.plugin = plugin;
	}

	/**
	 * Read an audio file and save it to the buffer.
	 * @param path path to an audio file, currently only 44100sr WAV file
	 * is supported.
	 */
	public void readFile(String path) {
		this.path = path;
		dataFormat = readWavFile(path);
	}

	/**
	 * Read a WAV-file. File will be truncated to first 30 seconds.
	 * @param filename system path to WAV-file to be read.
	 * @return AudioFormat of the file
	 */
	private AudioFormat readWavFile(String filename) {

		// GET FORMAT
		AudioFormat af = null;
		try {
			AudioInputStream ais;
			ais = AudioSystem.getAudioInputStream(new File(filename));
			af = ais.getFormat();
		} catch (Exception e) {
			e.printStackTrace();
		}

		int channels = (int)af.getChannels();
		
		// Do the actual file reading with the help of jm.util.Read
		float[] raw = Read.audio(filename);
		
		// If 2-channeled, mix channels together
		if (channels==2) {
			// length is half from data's
			float[][] data = new float[2][raw.length /channels];
			//float[] sum = new float[data.length / channels];
			for (int i=0; i<data[0].length; i++) {
				data[0][i] = Utils.clip(raw[i*2], -1, 1);
				data[1][i] = Utils.clip(raw[i*2 + 1], -1, 1);
			}
		}
		// if >2 channels, discard the data as we won't be mixing that together
		else if (channels>2) {
			data = null;
			return null;
		}
		/*
		// truncate if longer than 30s long
		int max_index = (int)(plugin.getSampleRate()
				* Caesura.PARAM_RANGE_HIGH[Caesura.P_POSITION]);
		if (max_index > data.length) {
			max_index = data.length;
			data = Arrays.copyOf(data, max_index);
		} */

		return af;
	}
	
	/**
	 * @return refrence to the audio material
	 */
	public float[][] getMaterial() {
		return this.data;
	}
	
	/**
	 * @return true if valid material has been loaded
	 */
	public boolean isSet() {
		return (data!=null);
	}

}
