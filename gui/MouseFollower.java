package caesura.gui;

import java.awt.MouseInfo;

/**
 * Start daemon thread that gets the mouse pointer information. We want this
 * to be a lot faster than the GUI refresh.
 * @author oek
 */
public class MouseFollower extends Thread {

	private static final int REFRESH_RATE = 50; // 50 ms
	int x;
	int y;
	Controller controller;

	/**
	 * @param c refrence to the controller instance
	 */
	public MouseFollower(Controller c) {
		this.controller = c;
	}

	/**
	 * Sleep for REFRES_RATE milliseconds, then update the controllers mouse
	 * update.
	 */
	public void run()
	{
		while (true) {
			try {
				Thread.sleep(REFRESH_RATE);
			} catch (Exception e) {
			}
			this.x = MouseInfo.getPointerInfo().getLocation().x;
			this.y = MouseInfo.getPointerInfo().getLocation().y;
			controller.setMousePosition(this.x, this.y);
		}
	}
}

