package caesura.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

import caesura.audio.*;

public class JEnvelopePanel extends JPanel {

	private int w;
	private int w_draw;
	private int h_draw;
	private int h;
	private int pad;
	private float duration;
	private Color color;
	private Color marking_color;
	
	private int[] envelope_points;
	private Envelope envelope;

	/**
	 * Create JEnvelopePanel
	 * @param w width
	 * @param h	height
	 * @param pad padding on all sides
	 * @param color color of the graphic
	 * @param marking_color color of the marking lines
	 */
	public JEnvelopePanel(int w, int h, int pad, Color color,
			Color marking_color) {
		this.w = w;
		this.w_draw = w - 2*pad;
		this.h = h;
		this.h_draw = h - 2*pad;
		this.pad = pad;
		this.duration = 44100;
		
		envelope_points = new int[w_draw];
		
		this.color = color;
		this.marking_color = marking_color;
		this.envelope = new Envelope();
		this.setLayout(null);
		this.setSize(w, h);
		this.setEnvelopeValues(0, 0, 44100);
	}

	/**
	 * Set envelope values for the mock Envelope
	 * @param env_left attack parameter from [-1, 1]
	 * @param env_right release parameter from [-1, 1]
	 * @param duration duration of the prototype grain in samples
	 */
	public void setEnvelopeValues(float env_left, float env_right,
			float duration) {
		envelope.init(env_left, env_right, duration);
		this.duration = duration;
		//jvst.wrapper.VSTPluginAdapter.log("ENV: " + this.getWidth() + " " + 
		//		this.getHeight() + " " + this.getX() + " " +  this.getY());
		calculatePoints();
	}
	
	/**
	 * Calculate points of the envelope to envelope_points.
	 */
	public void calculatePoints() {
		//jvst.wrapper.VSTPluginAdapter.log("calculatePoints");
		for (int i=0; i < w_draw; i++) {
			float env = envelope.getCoef((float)i
					/ (this.w_draw) * this.duration);
			if (env<0) env = 0;
			envelope_points[i] = (int)((this.h_draw)*(1-env));
			//jvst.wrapper.VSTPluginAdapter.log("i: " + i + " env: " + env);
		}
	}

	/**
	 * Paint the waveform.
	 */
	protected void paintComponent(Graphics g) {
		//jvst.wrapper.VSTPluginAdapter.log("DRAW ENVELOPE");
		g.setColor(color);
		
		for (int i=0; i < w_draw; i++) {
			g.drawLine(i+this.pad, this.h_draw,
					i+this.pad, this.envelope_points[i]);
		}
		g.setColor(this.marking_color);
		g.drawLine(w/2, 0, w/2, h); 
	}
}
