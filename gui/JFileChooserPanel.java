package caesura.gui;

import java.awt.Color;
import caesura.common.*;
import caesura.audio.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

/**
 * Panel for CaesuraGUI that manages button for opening dialog and showing the
 * chosen file's name in text field.
 * @author oek
 *
 */
public class JFileChooserPanel extends JPanel {

	private CaesuraGUI gui;
	private JButton load_button;
	private ImageIcon load_gfx;
	private ImageIcon load_gfx_pressed;

	private JLabel filename_text;

	/**
	 * @param gui refrence to the CaesuraGUI instance
	 * @param w width
	 * @param h height
	 */
	public JFileChooserPanel(CaesuraGUI gui, int w, int h) {
		//jvst.wrapper.VSTPluginAdapter.log("FILECHOOSERPANEL\n\n");	
		this.gui = gui;

		// init window
		this.setSize(new Dimension(w, h));
		this.setBackground(new Color(1, 0, 0, 1));
		this.setFocusable(false);
		this.setLayout(null);

		// BUTTON

		load_button = new JButton();
		load_button.addActionListener(new Opener());

		URL load_path
		= this.getClass().getResource(Resources.PATH_TO_LOADBUTTON);
		URL load_pressed_path
		= this.getClass().getResource(Resources.PATH_TO_LOADBUTTON_PRESSED);
		
		load_gfx = new ImageIcon(load_path);
		load_gfx_pressed = new ImageIcon(load_pressed_path);

		load_button.setIcon(load_gfx);
		load_button.setPressedIcon(load_gfx_pressed);
		load_button.setBorder(null);
		load_button.setFocusable(false);

		this.add(load_button);
		load_button.setBounds(0, 0,
				Resources.LOAD_BUTTON_W, Resources.LOAD_BUTTON_H);

		// FILENAME FIELD

		filename_text = new JLabel(Resources.TXT_NO_FILE_LOADED);
		filename_text.setForeground(Color.WHITE);
		this.add(filename_text);

		filename_text.setBounds(
				Resources.FILENAME_TEXT_X, Resources.FILENAME_TEXT_Y,
				Resources.FILENAME_TEXT_W, Resources.FILENAME_TEXT_H
				);

	//	this.repaint();
	}

	/**
	 * Handles the file selection dialog action
	 */
	class Opener implements ActionListener {
		// if button is clicked, this gets called
		public void actionPerformed(ActionEvent e) {

			// create new file chooser
			JFileChooser c = new JFileChooser();
			// open dialog for file selection
			int result = c.showOpenDialog(JFileChooserPanel.this);
			// if file was of correct type
			if (result == JFileChooser.APPROVE_OPTION) {
				String path = c.getSelectedFile().getAbsolutePath();
				gui.setMaterialPath(path);
				jvst.wrapper.VSTPluginAdapter.log(path);
			}
			if (gui.isMaterialLoaded()) {
				filename_text.setText(c.getSelectedFile().getName());
			} else {
				filename_text.setText(Resources.TXT_INVALID_FILE);
			}
		}
	} // end of Class Opener
} // end of JFileChooserPanel
