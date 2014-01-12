package caesura.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;

import caesura.common.*;
import caesura.audio.*;

/**
 * Visualises a main range and an auxiliary range on the waveform.
 * @author oek
 *
 */
public class JPlayhead extends JLabel {

	int main_left;
	int main_right;
	int aux_left;
	int aux_right;
	int w;
	int h;
	Color main_color;
	Color aux_color;
	Color main_shade_color;
	Color aux_shade_color;

	/**
	 * @param w width of the area that can be spanned
	 * @param h height of the area that can be spanned
	 * @param main_color color of the main lines
	 * @param aux_color color of the auxiliary lines
	 * @param main_shade_color color of the main shade
	 * @param aux_shade_color color of the side shades
	 */
	public JPlayhead(int w, int h, Color main_color, Color aux_color,
			Color main_shade_color, Color aux_shade_color) {
		this.h = h;
		this.w = w;

		this.setSize(new Dimension(w, h));
		this.main_color = main_color;
		this.aux_color = aux_color;
		this.main_shade_color = main_shade_color;
		this.aux_shade_color = aux_shade_color;	
	}


	/**
	 * Change the bars size and/or location
	 * @param main_left value in the range [0,1] that corresponds to the
	 * left-hand side of the bar 
	 * @param main_right left value in the range [0,1] that corresponds to the
	 * right-hand side of the bar
	 * @param aux_left value in the range [0,1] that corresponds to the
	 * left-hand side auxiliary shade
	 * @param aux_right value in the range [0,1] that corresponds to the
	 * right-hand side auxiliary shade
	 */
	public void setRange(float main_left, float main_right,
			float aux_left, float aux_right) {
		this.main_left = (int)(main_left*w);
		this.main_right = (int)(main_right*w);

		if (this.main_left<0)
			this.main_left = 0;
		if (this.main_right > w)
			this.main_right = w;
		
		this.aux_left =  (int)(aux_left*w);
		this.aux_right =  (int)(aux_right*w);
		if (this.aux_left<0)
			this.aux_left = 0;
		if (this.aux_right > w)
			this.aux_right = w;
	}

	/**
	 * Draw the playhead.
	 */
	protected void paintComponent(Graphics g) {
		
		// DRAW AUX LINES
		g.setColor(this.aux_color);
		g.drawLine(this.aux_left, 0, this.aux_left, h);
		g.drawLine(this.aux_right-1, 0, this.aux_right-1, h);
		
		// DRAW MAIN LINES
		g.setColor(this.main_color);
		g.drawLine(this.main_left, 0, this.main_left, h);
		g.drawLine(this.main_right-1, 0, this.main_right-1, h);
		
		// DRAW MAIN SHADE
		g.setColor(this.main_shade_color);
		g.fillRect(this.main_left, 0, this.main_right - this.main_left, h);
		
		// DRAW AUX SHADE
		g.setColor(this.aux_shade_color);
		g.fillRect(this.aux_left, 0, this.main_left - this.aux_left, h);
		g.fillRect(this.main_right, 0, this.aux_right - this.main_right, h);
	}

}
