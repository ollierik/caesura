package caesura.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import caesura.audio.Caesura;

import caesura.common.*;
import caesura.audio.*;

/**
 * Box that visualises parameter label and value.
 * @author oek
 *
 */
public class JParamBox extends JPanel {

	private int w;
	private int h;
	private JLabel text;
	private JLabel value;
	//private int param_index;

	/**
	 * @param w width
	 * @param h height
	 * @param name name of the parameter
	 * @param param_index index of the parameter
	 */
	public JParamBox(int w, int h, String name, int param_index) {
		this.w = w;
		this.h = h;
		//this.param_index = param_index;
		this.text = new JLabel(name);
		this.value = new JLabel();
		
		// transparent by default
		this.setBackground(Resources.TRANSPARENT);

		this.text.setBounds(
				Resources.PARAMBOX_LABEL_X, Resources.PARAMBOX_TEXT_Y,
				Resources.PARAMBOX_LABEL_W, Resources.PARAMBOX_LABEL_H);
		this.text.setForeground(Color.WHITE);

		this.value.setBounds(
				Resources.PARAMBOX_LABEL_X, Resources.PARAMBOX_VALUE_Y,
				Resources.PARAMBOX_LABEL_W, Resources.PARAMBOX_LABEL_H);
		this.value.setForeground(Color.WHITE);

		this.setLayout(null);
		this.add(text);
		this.add(value);

		this.setSize(new Dimension(w,h));
		this.setFocusable(false);
	//	this.repaint();
	}

	/**
	 * Set text to the value field
	 * @param text parameter value to be displayed
	 */
	public void updateParameter(String text) {
		this.value.setText(text);
	}

}
