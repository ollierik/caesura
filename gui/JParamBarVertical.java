package caesura.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;

import caesura.common.*;
import caesura.audio.*;

public class JParamBarVertical extends JLabel {

	int y_high;
	int h_bar;
	int w;
	int h;

	Color color;

	/**
	 * @param w width of the bar
	 * @param h height of the field the bar can span
	 * @param color color of the bar
	 */
	public JParamBarVertical(int w, int h, Color color) {
		this.h = h;
		this.w = w;

		this.setSize(new Dimension(w, h));
		this.color = color;
	}
	
	/**
	 * Set color of the bar.
	 * @param color new color for the bar
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	
	/**
	 * Change the bars size and/or location
	 * @param low value in the range [0,1] from low to high that corresponds to
	 * the lower end of the bar.
	 * @param high value in the range [0,1] from low to high that corresponds to
	 * the higher end of the bar.
	 */
	public void setRange(float high, float low) {
		y_high = (int)(high*h);
		h_bar = (int)(low*h) - y_high;
		
		if (y_high<0)
			y_high = 0;
		if (h_bar>h)
			h_bar = h;
	}

	/**
	 * Paint the bar.
	 */
	protected void paintComponent(Graphics g) {
		g.setColor(this.color);
		g.fillRect(0, y_high , this.w, this.h_bar);
		//g.fillRect(0, 0, w, h);
	}
}
