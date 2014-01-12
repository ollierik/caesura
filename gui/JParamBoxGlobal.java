package caesura.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import caesura.common.*;
import caesura.audio.*;

/**
 * Box that visualises the last changed parameter's label, value and color.
 * @author oek
 */
public class JParamBoxGlobal extends JPanel {

	private int w;
	private int h;
	private JLabel text;
	private JLabel value;
	private JParamBar bar;
	//private int param_index;

	/**
	 * @param w width
	 * @param h height
	 */
	public JParamBoxGlobal(int w, int h) {
		this.w = w;
		this.h = h;
		//this.param_index = param_index;
		Color transparent = new Color(0,0,0,0);
		this.setBackground(transparent);
		
		// TEXT BOX
		this.text = new JLabel("JEE");
		this.text.setBounds(
				Resources.GLOBAL_BOX_TEXT_X, Resources.GLOBAL_BOX_TEXT_Y,
				Resources.GLOBAL_BOX_LABEL_W, Resources.GLOBAL_BOX_LABEL_H);
		this.text.setForeground(Resources.FONT_COLOR);
		this.text.setFont(Resources.FONT_LARGE);
		
		// VALUE BOX
		this.value = new JLabel("HII");
		this.value.setBounds(
				Resources.GLOBAL_BOX_VALUE_X, Resources.GLOBAL_BOX_VALUE_Y,
				Resources.GLOBAL_BOX_LABEL_W, Resources.GLOBAL_BOX_LABEL_H);
		this.value.setForeground(Resources.FONT_COLOR);
		this.value.setFont(Resources.FONT_LARGE);
		
		// BAR
		this.bar = new JParamBar(Resources.GLOBAL_BOX_BAR_W,
				Resources.GLOBAL_BOX_BAR_H, Color.BLUE);
		
		this.bar.setLocation(Resources.GLOBAL_BOX_BAR_X,
				Resources.GLOBAL_BOX_BAR_Y);
		this.bar.setRange(0.f, 1.f);

		this.setLayout(null);
		this.add(this.text);
		this.add(this.value);
		this.add(this.bar);

		this.setSize(new Dimension(w,h));
		this.setFocusable(false);
	//	this.repaint();
	}

	/**
	 * Update the parameter showed.
	 * @param parameter name of the new parameter
	 * @param value value label of the new parameter
	 * @param color color of the parameter bar
	 */
	public void updateParameter(String parameter, String value, Color color) {
		this.text.setText(parameter);
		this.value.setText(value);
		this.bar.setColor(color);
	}
}
