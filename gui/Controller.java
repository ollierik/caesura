package caesura.gui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import caesura.common.*;
import caesura.audio.*;

public class Controller implements
KeyListener, FocusListener {

	CaesuraGUI gui;

	private int control = -1;
	private int old_x;
	private int old_y;
	private MouseFollower mouse;
	private boolean info_text_is_set;

	/*
	// qwer asdf gh zcv space
	private int[] key_codes =
		{	81, 87, 69, 82,	//QWER
			65, 83, 68, 70,	//ASDF
			71, 72,			//GH
			90, 67, 86,		//ZCV
			32 };			// SPACE
			*/
	
	private int[] key_codes =
		{	KeyEvent.VK_S,	// interval
			KeyEvent.VK_D,	// duration
			KeyEvent.VK_A,	// pitch
			KeyEvent.VK_F,	// position
			KeyEvent.VK_R,	// crawl
			KeyEvent.VK_G,	// attack
			KeyEvent.VK_H,	// release
			KeyEvent.VK_C,	// pan
			KeyEvent.VK_W,	// irregularity
			KeyEvent.VK_E,	// displacement
			KeyEvent.VK_Q,	// detune
			KeyEvent.VK_V,	// width
			KeyEvent.VK_Z,	// scramble
			KeyEvent.VK_SPACE	// latch
		};
	
	// sdaf rghc wervz
	
	private boolean[] key_pressed = new boolean[14];

	public Controller(CaesuraGUI gui) {
		this.gui = gui;
		//jvst.wrapper.VSTPluginAdapter.log("CONTROLLER CTOR");
		this.gui.addKeyListener(this);
		this.gui.addFocusListener(this);
		this.info_text_is_set = false;
		this.mouse = new MouseFollower(this);
		this.mouse.setDaemon(true);
		this.mouse.start();

		//gui.addMouseMotionListener(this);
	}

	/**
	 * Set mouse position and update parameters if needed.s
	 * @param x
	 * @param y
	 */
	public void setMousePosition(int x, int y) {
		//jvst.wrapper.VSTPluginAdapter.log("MOUSEMOVE");
		if (this.keysPressed() == 0) {
			old_x = x;
			old_y = y;
			if (this.info_text_is_set) {
				gui.changeInfoText("");
				this.info_text_is_set = false;
			}
			
		} else {
			// calculate the change and add it to the current value
			float f = gui.getPlugin().getParameter(control);
			f -= (y-old_y) / Resources.PARAMETER_SCALE_HEIGHT[this.control];
			
			// limit values
			if (f<0) {
				f = 0;
			} else if (f>1) {
				f = 1;
			}
			
			// update value
			gui.updateFromControl(this.control, f);
			
			// update coordinates
			old_x = x;
			old_y = y;
			//jvst.wrapper.VSTPluginAdapter.log("MOUSE updated");
		}
	}

	/**
	 * If key is pressed, set the parameter to be used according the new key
	 * if applicable.
	 */
	public void keyPressed(KeyEvent e) {
		int c = e.getKeyCode();
		for (int i=0; i<this.key_codes.length; i++) {
			if (this.key_codes[i] == c) {
				this.key_pressed[i] = true;
				this.control = i;
				this.gui.changeInfoText(Resources.PARAMETER_INFO_TEXTS[i]);
				this.info_text_is_set = true;
			}
		}
	}
	
	/**
	 * If key is released, release control if applicable.
	 */
	public void keyReleased(KeyEvent e) {
		int c = e.getKeyCode();
		for (int i=0; i<key_codes.length; i++) {
			if (key_codes[i] == c) {
				key_pressed[i] = false;
			}
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void focusGained(FocusEvent arg0) {
	}

	/**
	 * If focus is lost, clear all key bindings.
	 */
	@Override
	public void focusLost(FocusEvent arg0) {
	//d	jvst.wrapper.VSTPluginAdapter.log("FOCUS LOST");
		for (int i=0; i<key_pressed.length; i++) {
			key_pressed[i] = false;
		}
		//gui.requestFocus();
	}

	/**
	 * Returns the number of keys being pressed.
	 * @return keys being pressed
	 */
	private int keysPressed() {
		int n = 0;
		for (int i=0; i<key_pressed.length; i++) {
			if (key_pressed[i]) {
				n++;
			}
		}
		return n;
	}
}
