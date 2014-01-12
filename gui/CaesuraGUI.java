package caesura.gui;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import jvst.wrapper.*;
import jvst.wrapper.gui.VSTPluginGUIRunner;

import caesura.common.*;
import caesura.audio.*;

public class CaesuraGUI extends VSTPluginGUIAdapter {

	private static final long serialVersionUID = -8641024370578430211L;

	//private JContentPanel content;
	private JImagePanel image_panel;
	private JFileChooserPanel filechooser_panel;
	private JWaveformPanel waveform_panel;
	//private JLabel param_change_value;
	private Controller controller;

	private JParamBox interval_box;
	private JParamBox duration_box;
	private JParamBox pitch_box;
	private JParamBox position_box;

	private JParamBox crawl_box;
	private JParamBox pan_box;
	private JParamBox irregularity_box;
	private JParamBox displacement_box;
	private JParamBox detune_box;
	private JParamBox width_box;
	private JParamBox scramble_box;

	private JParamBox attack_box;
	private JParamBox release_box;

	private JParamBar placement_bar;
	private JParamBar displacement_pre_bar;
	private JParamBar displacement_post_bar;
	private JParamBar interval_bar;
	private JParamBar irregularity_bar;
	private JParamBar pitch_bar;
	private JParamBar detune_pre_bar;
	private JParamBar detune_post_bar;
	private JParamBar pan_bar;
	private JParamBar width_pre_bar;
	private JParamBar width_post_bar;
	private JParamBar scramble_bar;

	private JPlayhead playhead;
	private JEnvelopePanel envelope_panel;
	private JParamBoxGlobal global_box;
	private JParamBarVertical global_bar;
	private JTextArea info_box;


	private VSTPluginAdapter plugin;
	//protected static boolean DEBUG = false;

	public CaesuraGUI(VSTPluginGUIRunner r, VSTPluginAdapter plug)
			throws Exception {
		super(r,plug);
		this.plugin = plug;

		this.setName("caesura");

		this.setResizable(false);
		this.setFocusable(true);

		this.setSize(new Dimension(Resources.CAESURA_W, Resources.CAESURA_H));

		this.init();
		this.setBackground(Color.WHITE);
		this.setVisible(true);
	}


	/**
	 * Initialise GUI: create elements and add them.
	 */
	public void init() { 

		// register to plugin
		((Caesura)plugin).setGUI(this);

		/*
		 * CREATE PARTS
		 */
		image_panel = new JImagePanel(Resources.CAESURA_W, Resources.CAESURA_H);

		filechooser_panel= new JFileChooserPanel(
				this, Resources.FILECHOOSER_W, Resources.FILECHOOSER_H);
		filechooser_panel.setLocation(
				Resources.FILECHOOSER_X, Resources.FILECHOOSER_Y);

		waveform_panel = new JWaveformPanel(
				this, Resources.WAVEFORM_W, Resources.WAVEFORM_H,
				Resources.WAVEFORM_CENTER_Y, Resources.WAVEFORM_GFX_H,
				Resources.WAVEFORM_COLOR, Resources.WAVEFORM_SHADE_COLOR);
		waveform_panel.setLocation(Resources.WAVEFORM_X, Resources.WAVEFORM_Y);
		/*
		param_change_value = new JLabel("MOI");
		param_change_value.setBounds(
				Resources.PARAM_CHANGE_TEXT_X, Resources.PARAM_CHANGE_TEXT_Y,
				Resources.PARAM_CHANGE_TEXT_W, Resources.PARAM_CHANGE_TEXT_H);
		 */

		/////////////////////////////////////////////
		// PARAMETER BOXES
		/////////////////////////////////////////////
		interval_box = new JParamBox(
				Resources.PARAMBOX_W, Resources.PARAMBOX_H,
				"interval", Caesura.P_INTERVAL);
		interval_box.setLocation(Resources.INTERVAL_BOX_X,
				Resources.INTERVAL_BOX_Y);
		/////////////////////////////////////////////
		duration_box = new JParamBox(
				Resources.PARAMBOX_W, Resources.PARAMBOX_H,
				"duration", Caesura.P_DURATION);
		duration_box.setLocation(Resources.DURATION_BOX_X,
				Resources.DURATION_BOX_Y);
		/////////////////////////////////////////////
		pitch_box = new JParamBox(
				Resources.PARAMBOX_W, Resources.PARAMBOX_H,
				"pitch", Caesura.P_PITCH);
		pitch_box.setLocation(Resources.PITCH_BOX_X,
				Resources.PITCH_BOX_Y);
		/////////////////////////////////////////////
		position_box = new JParamBox(
				Resources.PARAMBOX_W, Resources.PARAMBOX_H,
				"position", Caesura.P_POSITION);
		position_box.setLocation(Resources.POSITION_BOX_X,
				Resources.POSITION_BOX_Y);
		/////////////////////////////////////////////
		crawl_box = new JParamBox(
				Resources.PARAMBOX_W, Resources.PARAMBOX_H,
				"crawl", Caesura.P_CRAWL);
		crawl_box.setLocation(Resources.CRAWL_BOX_X,
				Resources.CRAWL_BOX_Y);
		/////////////////////////////////////////////
		pan_box = new JParamBox(
				Resources.PARAMBOX_W, Resources.PARAMBOX_H,
				"pan", Caesura.P_PAN);
		pan_box.setLocation(Resources.PAN_BOX_X,
				Resources.PAN_BOX_Y);
		/////////////////////////////////////////////
		irregularity_box = new JParamBox(
				Resources.PARAMBOX_W, Resources.PARAMBOX_H,
				"irregularity", Caesura.P_IRREGULARITY);
		irregularity_box.setLocation(Resources.IRREGULARITY_BOX_X,
				Resources.IRREGULARITY_BOX_Y);
		/////////////////////////////////////////////
		displacement_box = new JParamBox(
				Resources.PARAMBOX_W, Resources.PARAMBOX_H,
				"displacement", Caesura.P_DISPLACEMENT);
		displacement_box.setLocation(Resources.DISPLACEMENT_BOX_X,
				Resources.DISPLACEMENT_BOX_Y);
		/////////////////////////////////////////////
		detune_box = new JParamBox(
				Resources.PARAMBOX_W, Resources.PARAMBOX_H,
				"detune", Caesura.P_DETUNE);
		detune_box.setLocation(Resources.DETUNE_BOX_X,
				Resources.DETUNE_BOX_Y);
		/////////////////////////////////////////////
		width_box = new JParamBox(
				Resources.PARAMBOX_W, Resources.PARAMBOX_H,
				"width", Caesura.P_WIDTH);
		width_box.setLocation(Resources.WIDTH_BOX_X,
				Resources.WIDTH_BOX_Y);
		/////////////////////////////////////////////
		scramble_box = new JParamBox(
				Resources.PARAMBOX_W, Resources.PARAMBOX_H,
				"scramble", Caesura.P_SCRAMBLE);
		scramble_box.setLocation(Resources.SCRAMBLE_BOX_X,
				Resources.SCRAMBLE_BOX_Y);
		/////////////////////////////////////////////
		attack_box = new JParamBox(
				Resources.PARAMBOX_SMALL_W, Resources.PARAMBOX_SMALL_H,
				"attack", Caesura.P_SCRAMBLE);
		attack_box.setLocation(Resources.ATTACK_BOX_X,
				Resources.ATTACK_BOX_Y);
		release_box = new JParamBox(
				Resources.PARAMBOX_SMALL_W, Resources.PARAMBOX_SMALL_H,
				"release", Caesura.P_SCRAMBLE);
		release_box.setLocation(Resources.RELEASE_BOX_X,
				Resources.RELEASE_BOX_Y);


		/////////////////////////////////////////////
		// PARAMETER BARS
		/////////////////////////////////////////////
		placement_bar = new JParamBar(Resources.BARS_W, Resources.BARS_H,
				Resources.PLACEMENT_COLOR);
		placement_bar.setLocation(Resources.BARS_X, Resources.PLACEMENT_BAR_Y);

		displacement_pre_bar = new JParamBar(Resources.BARS_W, Resources.BARS_H,
				Resources.DISPLACEMENT_COLOR);
		displacement_pre_bar.setLocation(Resources.BARS_X,
				Resources.PLACEMENT_BAR_Y);
		displacement_post_bar = new JParamBar(Resources.BARS_W, Resources.BARS_H,
				Resources.DISPLACEMENT_COLOR);
		displacement_post_bar.setLocation(Resources.BARS_X,
				Resources.PLACEMENT_BAR_Y);
		/////////////////////////////////////////////
		interval_bar = new JParamBar(Resources.BARS_W, Resources.BARS_H,
				Resources.INTERVAL_COLOR);
		interval_bar.setLocation(Resources.BARS_X,
				Resources.INTERVAL_BAR_Y);
		irregularity_bar = new JParamBar(Resources.BARS_W, Resources.BARS_H,
				Resources.IRREGULARITY_COLOR);
		irregularity_bar.setLocation(Resources.BARS_X,
				Resources.INTERVAL_BAR_Y);
		/////////////////////////////////////////////
		pitch_bar = new JParamBar(Resources.BARS_W, Resources.BARS_H,
				Resources.PITCH_COLOR);
		pitch_bar.setLocation(Resources.BARS_X,
				Resources.PITCH_BAR_Y);
		detune_pre_bar = new JParamBar(Resources.BARS_W, Resources.BARS_H,
				Resources.DETUNE_COLOR);
		detune_pre_bar.setLocation(Resources.BARS_X,
				Resources.PITCH_BAR_Y);
		detune_post_bar = new JParamBar(Resources.BARS_W, Resources.BARS_H,
				Resources.DETUNE_COLOR);
		detune_post_bar.setLocation(Resources.BARS_X,
				Resources.PITCH_BAR_Y);
		/////////////////////////////////////////////
		pan_bar = new JParamBar(Resources.BARS_W, Resources.BARS_H,
				Resources.PAN_COLOR);
		pan_bar.setLocation(Resources.BARS_X,
				Resources.PAN_BAR_Y);
		width_pre_bar = new JParamBar(Resources.BARS_W, Resources.BARS_H,
				Resources.WIDTH_COLOR);
		width_pre_bar.setLocation(Resources.BARS_X,
				Resources.PAN_BAR_Y);
		width_post_bar = new JParamBar(Resources.BARS_W, Resources.BARS_H,
				Resources.WIDTH_COLOR);
		width_post_bar.setLocation(Resources.BARS_X,
				Resources.PAN_BAR_Y);
		/////////////////////////////////////////////
		scramble_bar = new JParamBar(Resources.BARS_W, Resources.BARS_H,
				Resources.SCRAMBLE_COLOR);
		scramble_bar.setLocation(Resources.BARS_X, Resources.SCRAMBLE_BAR_Y);


		/////////////////////////////////////////////
		// PLAYHEAD
		/////////////////////////////////////////////
		playhead = new JPlayhead(Resources.PLAYHEAD_W, Resources.PLAYHEAD_H,
				Resources.PLACEMENT_COLOR, Resources.DISPLACEMENT_COLOR,
				Resources.PLAYHEAD_MAIN_COLOR, Resources.PLAYHEAD_AUX_COLOR);
		playhead.setLocation(Resources.PLAYHEAD_X, Resources.PLAYHEAD_Y);

		/////////////////////////////////////////////
		// ENVELOPE
		/////////////////////////////////////////////
		envelope_panel = new JEnvelopePanel(
				Resources.ENVELOPE_BOX_W, Resources.ENVELOPE_BOX_H,
				Resources.ENVELOPE_PAD,
				Resources.ENVELOPE_COLOR, Resources.LINE_MARKING_COLOR);
		envelope_panel.setLocation(Resources.ENVELOPE_BOX_X,
				Resources.ENVELOPE_BOX_Y);

		/////////////////////////////////////////////
		// GLOBAL BOX and GLOBAL BAR
		/////////////////////////////////////////////
		global_box = new JParamBoxGlobal(Resources.GLOBAL_BOX_W,
				Resources.GLOBAL_BOX_H);
		global_box.setLocation(Resources.GLOBAL_BOX_X,
				Resources.GLOBAL_BOX_Y);
		global_bar = new JParamBarVertical(Resources.GLOBAL_BAR_W,
				Resources.GLOBAL_BAR_H, Resources.GLOBAL_BAR_COLOR );
		global_bar.setLocation(Resources.GLOBAL_BAR_X,
				Resources.GLOBAL_BAR_Y);

		/////////////////////////////////////////////
		// INFO BOX
		/////////////////////////////////////////////
		info_box = new JTextArea(Resources.TXT_DEFAULT_INFO);
		info_box.setBounds(Resources.INFO_BOX_X, Resources.INFO_BOX_Y,
				Resources.INFO_BOX_W, Resources.INFO_BOX_H);
		info_box.setForeground(Resources.FONT_COLOR);
		info_box.setFont(Resources.FONT_SMALL);
		info_box.setBackground(Resources.TRANSPARENT);
		info_box.setEditable(false);
		info_box.setFocusable(false);
		info_box.setHighlighter(null);
		info_box.setWrapStyleWord(true);

		/////////////////////////////////////////////
		// ADD TO FRAME, SET COORDINATES
		/////////////////////////////////////////////

		this.setContentPane(image_panel);
		image_panel.setLocation(0, 0);
		//image_panel.repaint();

		this.add(filechooser_panel);
		this.add(waveform_panel);
		//		this.add(param_change_value);

		// BOXES
		this.add(interval_box);
		this.add(duration_box);
		this.add(pitch_box);
		this.add(position_box);
		this.add(crawl_box);
		this.add(pan_box);
		this.add(irregularity_box);
		this.add(displacement_box);
		this.add(detune_box);
		this.add(width_box);
		this.add(scramble_box);
		this.add(attack_box);
		this.add(release_box);

		// BARS
		this.add(placement_bar);
		this.add(displacement_pre_bar);
		this.add(displacement_post_bar);
		this.add(interval_bar);
		this.add(irregularity_bar);
		this.add(pitch_bar);
		this.add(detune_pre_bar);
		this.add(detune_post_bar);
		this.add(pan_bar);
		this.add(width_pre_bar);
		this.add(width_post_bar);
		this.add(scramble_bar);

		this.add(global_box);
		this.add(global_bar);
		this.add(info_box);

		waveform_panel.add(playhead);
		this.add(envelope_panel);
		//envelope_panel.setVisible(true);
		this.add(info_box);


		// CONTROLLER has to be created last for other parts to init first
		controller = new Controller(this);

		Timer timer = new Timer(1/30, new TimerActionListener());
		timer.start();
		
		this.update();
		this.setVisible(true);
		this.repaint();
	}


	/**
	 * Update the parameter value display.
	 * @param index number of the parameter
	 */
	public void update(int index) {

		if (index < Resources.PARAMETER_NAMES.length) {
			String new_text = Resources.PARAMETER_NAMES[index];
			String new_value = plugin.getParameterDisplay(index)
					+ " " + plugin.getParameterLabel(index);
			float val =  plugin.getParameter(index);

			switch (index) {
			case (Caesura.P_INTERVAL):
				interval_box.updateParameter(new_value);
			updateIntervalBar();
			global_box.updateParameter(new_text, new_value,
					Resources.INTERVAL_COLOR);
			break;
			case (Caesura.P_DURATION):
				duration_box.updateParameter(new_value);
			this.updatePlacementBar();
			this.updateEnvelopePanel();
			break;
			case (Caesura.P_PITCH):
				pitch_box.updateParameter(new_value);
			updatePitchBar();
			break;
			case (Caesura.P_POSITION):
				position_box.updateParameter(new_value);
			updateIntervalBar();
			this.updatePlacementBar();
			break;
			case (Caesura.P_CRAWL):
				crawl_box.updateParameter(new_value);
			break;
			case (Caesura.P_PAN):
				pan_box.updateParameter(new_value);
			updatePanBar();
			break;
			case (Caesura.P_IRREGULARITY):
				irregularity_box.updateParameter(new_value);
			updateIntervalBar();
			break;
			case (Caesura.P_DISPLACEMENT):
				displacement_box.updateParameter(new_value);
			this.updatePlacementBar();
			break;
			case (Caesura.P_DETUNE):
				detune_box.updateParameter(new_value);
			updatePitchBar();
			break;
			case (Caesura.P_WIDTH):
				width_box.updateParameter(new_value);
			updatePanBar();

			break;
			case (Caesura.P_SCRAMBLE):
				scramble_box.updateParameter(new_value);
			scramble_bar.setRange(0, val);
			break;
			case (Caesura.P_ENV_L):
				attack_box.updateParameter(new_value);
			this.updateEnvelopePanel();
			break;
			case (Caesura.P_ENV_R):
				release_box.updateParameter(new_value);
			this.updateEnvelopePanel();
			break;
			default:
			}

			// change common bar
			// change common text

			//this.repaint();
		}
	}


	/**
	 * Change the parameter value and update the display.
	 * @param index number of the parameter
	 * @param val new value
	 */
	public void update(int index, float val) {
		this.plugin.setParameterAutomated(index, val);
		this.update(index);
		//		this.param_change_value.setText("" + val);
	}


	/**
	 * Update all parameter value displays.
	 */
	public void update() {
		for (int i=0; i<Caesura.NUM_PARAMS; i++) {
			update(i);
		}
	}

	/**
	 * Updates the global meters and passes the call on for updating specific
	 * parameters.
	 * @param index index of parameter
	 * @param val value from [0,1]
	 */
	public void updateFromControl(int index, float val) {
		this.update(index, val);
		String new_text = Resources.PARAMETER_NAMES[index];
		String new_value = plugin.getParameterDisplay(index)
				+ " " + plugin.getParameterLabel(index);
		global_box.updateParameter(new_text, new_value,
				Resources.PARAMETER_COLORS[index]);
		global_bar.setRange((1-val), 1);
	}

	/**
	 * Sets correct bar for DURATION, POSITION, and DISPLACEMENT
	 */
	private void updatePlacementBar() {

		float length_in_secs = ((Caesura)plugin).getMaterialLengthInSecs();
		float max_dur_in_secs = Caesura.PARAM_RANGE_HIGH[Caesura.P_DURATION]
				* 0.001f;
		float scale = Caesura.PARAM_RANGE_HIGH[Caesura.P_POSITION]
				/ length_in_secs;

		float main_left = plugin.getParameter(Caesura.P_POSITION) * scale;
		float main_right = main_left + plugin.getParameter(Caesura.P_DURATION)
				* max_dur_in_secs / length_in_secs;

		float displacement_in_secs = 0.001f
				* Caesura.PARAM_RANGE_HIGH[Caesura.P_DISPLACEMENT];
		float dis_offset = plugin.getParameter(Caesura.P_DISPLACEMENT)
				* displacement_in_secs / length_in_secs;

		float aux_pre = main_left - dis_offset;
		float aux_post = main_right + dis_offset;

		displacement_pre_bar.setRange(aux_pre, main_left);
		placement_bar.setRange(main_left, main_right);
		displacement_post_bar.setRange(main_right, aux_post);

		playhead.setRange(main_left, main_right, aux_pre, aux_post);
	//	playhead.repaint();
	}

	/**
	 * Set correct bar for INTERVAL and IRREGULARITY
	 */
	private void updateIntervalBar() {

		float length_in_secs = ((Caesura)plugin).getMaterialLengthInSecs();
		float max_intrvl_in_secs = Caesura.PARAM_RANGE_HIGH[Caesura.P_INTERVAL]
				* 0.001f;
		float scale = Caesura.PARAM_RANGE_HIGH[Caesura.P_POSITION]
				/ length_in_secs;

		float main_left = plugin.getParameter(Caesura.P_POSITION) * scale;
		float main_right = main_left + plugin.getParameter(Caesura.P_INTERVAL)
				* max_intrvl_in_secs / length_in_secs;

		float irregularity_in_secs = 0.001f *
				Caesura.PARAM_RANGE_HIGH[Caesura.P_IRREGULARITY];
		float dis_offset = plugin.getParameter(Caesura.P_IRREGULARITY)
				* irregularity_in_secs / length_in_secs;

		float aux_right = main_right + dis_offset;

		interval_bar.setRange(main_left, main_right);
		irregularity_bar.setRange(main_right, aux_right);
	}


	/**
	 * Set correct bar for PITCH and DETUNE
	 */
	private void updatePitchBar() {

		float center = plugin.getParameter(Caesura.P_PITCH);
		float offset = 0.5f * (float)Resources.BAR_DOT_WIDTH
				/ (float)Resources.BARS_W;
		float main_left = center - offset;
		float main_right = center + offset;

		float aux_offset = plugin.getParameter(Caesura.P_DETUNE);
		float aux_pre = main_left - aux_offset;
		float aux_post = main_right + aux_offset;

		detune_pre_bar.setRange(aux_pre, main_left);
		pitch_bar.setRange(main_left, main_right);
		detune_post_bar.setRange(main_right, aux_post);
	}

	/**
	 * Set correct bar for PAN and WIDTH
	 */
	private void updatePanBar() {

		float center = plugin.getParameter(Caesura.P_PAN);
		float offset = 0.5f * (float)Resources.BAR_DOT_WIDTH
				/ (float)Resources.BARS_W;
		float main_left = center - offset;
		float main_right = center + offset;

		float aux_offset = 0.5f * plugin.getParameter(Caesura.P_WIDTH);
		float aux_pre = main_left - aux_offset;
		float aux_post = main_right + aux_offset;

		// if goes over
		if (aux_pre<0) {
			aux_post += -aux_pre;
			aux_pre = 0;
		}
		else if (aux_post>1) {
			aux_pre -= (aux_post-1);
			aux_post = 1;
		}

		width_pre_bar.setRange(aux_pre, main_left);
		pan_bar.setRange(main_left, main_right);
		width_post_bar.setRange(main_right, aux_post);
	}

	/**
	 * Update envelope graphics
	 */
	private void updateEnvelopePanel() {
		float envl = Utils.scale(-1, 1, plugin.getParameter(Caesura.P_ENV_L));
		float envr = Utils.scale(-1, 1, plugin.getParameter(Caesura.P_ENV_R));
		float duration = Caesura.PARAM_RANGE_HIGH[Caesura.P_DURATION]
				* plugin.getParameter(Caesura.P_DURATION);
		envelope_panel.setEnvelopeValues(envl, envr, duration);
	//	envelope_panel.repaint();
	}

	/**
	 * Set text to the info box
	 * @param new_text
	 */
	public void changeInfoText(String new_text) {
		this.info_box.setText(new_text);
		//this.repaint();

	}

	/**
	 * Give the plugin info to the new path
	 * @param path
	 */
	public void setMaterialPath(String path) {
		((Caesura)this.plugin).setMaterialPath(path);
		waveform_panel.init(((Caesura)plugin).getMaterialBuffer());
	//	waveform_panel.repaint();
	}

	/**
	 * @return true if material has been loaded
	 */
	public boolean isMaterialLoaded() {
		return ((Caesura)plugin).isMaterialLoaded();
	}

	/**
	 * @return refrence to the plugin
	 */
	public Caesura getPlugin() {
		return (Caesura)this.plugin;
	}

	/**
	 * Run repaint.
	 * @author oek
	 */
	class TimerActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			CaesuraGUI.this.repaint();
		}
	}
}