package caesura.gui;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import javax.swing.JPanel;

import caesura.common.*;
import caesura.audio.*;

/**
 * Panel that renders the waveform.
 * @author oek
 */
public class JWaveformPanel extends JPanel {

	private CaesuraGUI gui;

	private float[][] material_buf;
	private float[] waveform_pos;
	private float[] waveform_neg;

	private int w;
	private int h;
	private float scale = 0.6f; // how high the waveform is compared to room
	
	private Color waveform_color;
	private Color shade_color;
	
	private int gfx_y_center;
	private int gfx_h;
	BufferedImage image;

	/**
	 * @param gui refrence to CaesuraGUI
	 * @param w width of the container
	 * @param h height of the container
	 * @param gfx_y_center local y value of the center of the waveform
	 * @param gfx_h height of the waveform
	 * @param waveform_color color of the waveform
	 * @param shade_color color of the shade
	 */
	public JWaveformPanel(CaesuraGUI gui, int w, int h,
			int gfx_y_center, int gfx_h,
			Color waveform_color, Color shade_color) {

		this.gui = gui;
		this.w = w;
		this.h = h;
		
		this.gfx_y_center = gfx_y_center;
		this.gfx_h = gfx_h;
		
		this.waveform_color = waveform_color;
		this.shade_color = shade_color;

		this.waveform_neg = new float[this.w];
		this.waveform_pos = new float[this.w];

		this.setSize(w, h);
		this.setLayout(null);
		this.setFocusable(false);
	}

	/**
	 * Initialize the waveform from file
	 * @param buf
	 */
	public void init(float[][] buf) {

		this.material_buf = buf;

		int step = this.material_buf.length / this.w;
		int i = 0; // iterate over all points in material
		int k = 0; // count for steps
		int j = 0; // index for output

		float neg = 0;
		float pos = 0;

		float peak = 0;
		// go thru material
		while (i < this.material_buf[0].length) {

			float f = this.material_buf[0][i];

			// sort to right buffer, increase counter
			// clipping
			if (f<-1) {
				neg += 1;
			} else if (f>1) {
				pos += 1;
			}
			// not clipping
			else if (f<0) {
				neg += f*f;
			} else {
				pos += f*f;
			}

			// if window length met
			if (k>=step) {
				// calculate rms, move to buffer
				float cur_neg = (float)Math.sqrt(neg/step);
				float cur_pos = (float)(float)Math.sqrt(pos/step);
				waveform_neg[j] = cur_neg;
				waveform_pos[j] = cur_pos;

				// get the higher one
				float max = waveform_neg[j] > waveform_pos[j] ?
						waveform_neg[j] : waveform_pos[j];

						if (max>peak)
							peak = max;		

						// zero accumulators
						neg = 0;
						pos = 0;
						// increment buffer index
						j++;
						// reset counter
						k = 0;
			} else {
				k++;
			}
			i++;
		} // while

		// normalise
		jvst.wrapper.VSTPluginAdapter.log("MAX " + peak);
		for (i=0; i<waveform_neg.length; i++) {
			waveform_neg[i] *= this.scale * this.gfx_h/peak;
			waveform_pos[i] *= this.scale * this.gfx_h/peak;
		}

		renderWaveform();
	}

	/**
	 * Render the waveform image from buffers
	 */
	private void renderWaveform() {

		this.image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		// cast to Graphics2D
		Graphics2D g = (Graphics2D)image.getGraphics();

		// prepare image
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		// get the graphics for drawing
		Graphics2D imageg = image.createGraphics();


		int hmid = this.gfx_y_center;
		
		// DRAW SHADE
		// 1px right and 2px down
		imageg.setColor(this.shade_color);
		imageg.setStroke(new BasicStroke(1.4f));
		for (int i=0; i < this.w; i++) {
			imageg.drawLine(i+1, hmid+(int)waveform_neg[i]+2,
					i+1, hmid-(int)waveform_pos[i]);
		}
		
		// DRAW THE ACTUAL WAVEFORM
		imageg.setColor(this.waveform_color);
		imageg.setStroke(new BasicStroke(1.0f));
		for (int i=0; i < this.w; i++) {
			imageg.drawLine(i, hmid+(int)waveform_neg[i],
					i, hmid-(int)waveform_pos[i]);
		}

		// ANTIALIAISING BLUR
		// diagonal kernel
		Kernel kernel = new Kernel(3, 3, new float[] 
				{ 0.05f, 0.1f, 0.00f,
				0.1f, 0.5f, 0.1f,
				0.00f, 0.1f, 0.05f
				});
		BufferedImageOp op = new ConvolveOp(kernel);
		image = op.filter(image, null);
	}


	/**
	 * Draw the waveform.
	 */
	protected void paintComponent(Graphics g) {
		((Graphics2D)g).drawImage(image, 0, 0, null);
	}
}
