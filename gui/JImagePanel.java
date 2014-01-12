package caesura.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import caesura.common.*;
import caesura.audio.*;

/**
 * Panel that shows the background image.
 * @author oek
 */
public class JImagePanel extends JPanel {

	BufferedImage image;

	/**
	 * @param w width
	 * @param h height
	 */
	public JImagePanel(int w, int h) {
		this.setLayout(null);
		this.setFocusable(true);
		//this.setPreferredSize(new Dimension(w, h));

		try {
			URL path = this.getClass().getResource(Resources.PATH_TO_BG);
			image = ImageIO.read(path);
		} catch (IOException e) {
		}

		this.setVisible(true);
	}

	/**
	 * Paint the background image
	 */
	public void paintComponent(Graphics g) {
		//jvst.wrapper.VSTPluginAdapter.log("DRAW IMAGE!");
		g.drawImage(image, 0, 0, null);
	}
}
