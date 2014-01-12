package caesura.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;

import caesura.common.*;
import caesura.audio.*;

/**
 * Bar to visualise parameter
 * @author oek
 */
public class JParamBar extends JLabel {

	int x_left;
	int w_bar;
	int w;
	int h;

	Color color;

	/**
	 * @param w width of the total field the bar can span
	 * @param h height of the bar
	 * @param color color of the bar
	 */
	public JParamBar(int w, int h, Color color) {
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
	 * @param left value in the range [0,1] that corresponds to the left-hand
	 * side of the bar 
	 * @param right left value in the range [0,1] that corresponds to the right-hand
	 * side of the bar
	 */
	public void setRange(float left, float right) {
		x_left = (int)(left*w);
		w_bar = (int)(right*w) - x_left;
		
		if (x_left<0)
			x_left = 0;
		if (w_bar>w)
			w_bar = w;
	}

	/**
	 * Paint the bar.
	 */
	protected void paintComponent(Graphics g) {
		g.setColor(this.color);
		g.fillRect(this.x_left, 0 , this.w_bar, this.h);
	}
}
