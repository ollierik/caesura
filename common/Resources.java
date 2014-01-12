package caesura.common;

import java.awt.Color;
import java.awt.Font;

/**
 * File that holds constant values for the use of the GUI
 * @author oek
 */
public class Resources {

	public static final int CAESURA_W = 1100;
	public static final int CAESURA_H = 583;

	public static final int BORDER_PAD = 8;
	public static final int BIG_PAD = 50;

	public static final int WAVEFORM_X = 10;
	public static final int WAVEFORM_Y = 52;
	public static final int WAVEFORM_W = 1038;
	public static final int WAVEFORM_H = 303;

	// local
	public static final int WAVEFORM_CENTER_Y = 134;
	public static final int WAVEFORM_GFX_H = 234;

	public static final int FILECHOOSER_W = 330;
	public static final int FILECHOOSER_H = 34;
	public static final int FILECHOOSER_X = 720;
	public static final int FILECHOOSER_Y = 25;

	// locals inside filechooser_panel
	public static final int LOAD_BUTTON_X = 0;
	public static final int LOAD_BUTTON_Y = 0;
	public static final int LOAD_BUTTON_W = 34;
	public static final int LOAD_BUTTON_H = 17;
	public static final int FILENAME_TEXT_X = 45;
	public static final int FILENAME_TEXT_Y = 2;
	public static final int FILENAME_TEXT_W = 286;
	public static final int FILENAME_TEXT_H = 15;

	public static final int PARAMBOX_W = 168;
	public static final int PARAMBOX_H = 50;

	public static final int PARAMBOX_SMALL_W = 144;
	public static final int PARAMBOX_SMALL_H = PARAMBOX_H;

	// locals inside parambox
	public static final int PARAMBOX_LABEL_X = 54;
	public static final int PARAMBOX_TEXT_Y = 4;
	public static final int PARAMBOX_VALUE_Y = 22;
	public static final int PARAMBOX_LABEL_W = PARAMBOX_W - BIG_PAD;
	public static final int PARAMBOX_LABEL_H = 20;

	// grid for parameter boxes
	public static final int PARAMS_ROW1 = 367;
	public static final int PARAMS_ROW2 = PARAMS_ROW1 + BORDER_PAD + PARAMBOX_H;
	public static final int PARAMS_ROW3 = PARAMS_ROW2 + BORDER_PAD + PARAMBOX_H;
	public static final int PARAMS_COL1 = BORDER_PAD;
	public static final int PARAMS_COL2 = PARAMS_COL1 + BORDER_PAD + PARAMBOX_W;
	public static final int PARAMS_COL3 = PARAMS_COL2 + BORDER_PAD + PARAMBOX_W;
	public static final int PARAMS_COL4 = PARAMS_COL3 + BORDER_PAD + PARAMBOX_W;
	public static final int PARAMS_COL5 = PARAMS_COL4 + BIG_PAD + PARAMBOX_W;
	public static final int PARAMS_COL6
	= PARAMS_COL5 + BORDER_PAD + PARAMBOX_SMALL_W;


	////////////////////////////////////////
	// BOXES
	////////////////////////////////////////
	public static final int INTERVAL_BOX_X = PARAMS_COL2;
	public static final int INTERVAL_BOX_Y = PARAMS_ROW2;
	public static final int DURATION_BOX_X = PARAMS_COL3;
	public static final int DURATION_BOX_Y = PARAMS_ROW2;
	public static final int PITCH_BOX_X = PARAMS_COL1;
	public static final int PITCH_BOX_Y =  PARAMS_ROW2;
	public static final int POSITION_BOX_X = PARAMS_COL4;
	public static final int POSITION_BOX_Y =  PARAMS_ROW2;

	public static final int CRAWL_BOX_X = PARAMS_COL4;
	public static final int CRAWL_BOX_Y = PARAMS_ROW1;
	public static final int PAN_BOX_X = PARAMS_COL3;
	public static final int PAN_BOX_Y = PARAMS_ROW3;

	public static final int IRREGULARITY_BOX_X = PARAMS_COL2;
	public static final int IRREGULARITY_BOX_Y = PARAMS_ROW1;
	public static final int DISPLACEMENT_BOX_X = PARAMS_COL3;
	public static final int DISPLACEMENT_BOX_Y = PARAMS_ROW1;
	public static final int DETUNE_BOX_X = PARAMS_COL1;
	public static final int DETUNE_BOX_Y = PARAMS_ROW1;
	public static final int WIDTH_BOX_X = PARAMS_COL4;
	public static final int WIDTH_BOX_Y = PARAMS_ROW3;
	public static final int SCRAMBLE_BOX_X = PARAMS_COL1;
	public static final int SCRAMBLE_BOX_Y = PARAMS_ROW3;

	public static final int ATTACK_BOX_X = PARAMS_COL5;
	public static final int ATTACK_BOX_Y = PARAMS_ROW2;
	public static final int RELEASE_BOX_X = PARAMS_COL6;
	public static final int RELEASE_BOX_Y = PARAMS_ROW2;

	////////////////////////////////////////
	// ENVELOPE PANEL
	////////////////////////////////////////
	public static final int ENVELOPE_BOX_X = PARAMS_COL5;
	public static final int ENVELOPE_BOX_Y = PARAMS_ROW1;
	public static final int ENVELOPE_BOX_W = 2 * PARAMBOX_SMALL_W + BORDER_PAD;
	public static final int ENVELOPE_BOX_H = PARAMBOX_H;
	public static final int ENVELOPE_PAD = 1;


	////////////////////////////////////////
	// GLOBAL METERS
	////////////////////////////////////////
	public static final int GLOBAL_BOX_X = 754;
	public static final int GLOBAL_BOX_Y = 483;
	public static final int GLOBAL_BOX_W = 2 * PARAMBOX_SMALL_W + BORDER_PAD;
	public static final int GLOBAL_BOX_H = PARAMBOX_H;
	// locals
	public static final int GLOBAL_BOX_TEXT_X = 10;
	public static final int GLOBAL_BOX_TEXT_Y = 10;
	public static final int GLOBAL_BOX_VALUE_X = 115;
	public static final int GLOBAL_BOX_VALUE_Y = GLOBAL_BOX_TEXT_Y;

	public static final int GLOBAL_BOX_LABEL_W = GLOBAL_BOX_W/2 -BORDER_PAD;
	public static final int GLOBAL_BOX_LABEL_H = 30;

	public static final int GLOBAL_BOX_BAR_H = 7;
	public static final int GLOBAL_BOX_BAR_W = GLOBAL_BOX_W-2;
	public static final int GLOBAL_BOX_BAR_X = 1;
	public static final int GLOBAL_BOX_BAR_Y = GLOBAL_BOX_H - BORDER_PAD;

	public static final int GLOBAL_BAR_X = 1072;
	public static final int GLOBAL_BAR_Y = 51;
	public static final int GLOBAL_BAR_W = BORDER_PAD;
	public static final int GLOBAL_BAR_H = 481;

	////////////////////////////////////////
	// INFO
	////////////////////////////////////////
	// arvot empiirisesti todeutt toimiviksi
	public static final int INFO_BOX_PAD = 2;
	public static final int INFO_BOX_X = PARAMS_COL2 + INFO_BOX_PAD;
	public static final int INFO_BOX_Y = PARAMS_ROW3;
	public static final int INFO_BOX_W = PARAMBOX_W - INFO_BOX_PAD;
	public static final int INFO_BOX_H = PARAMBOX_H; 

	////////////////////////////////////////
	// BARS
	////////////////////////////////////////
	public static final int BARS_X = WAVEFORM_X;
	public static final int BARS_W = WAVEFORM_W;
	public static final int BARS_H = BORDER_PAD;

	public static final int BAR_DOT_WIDTH = BORDER_PAD;

	public static final int PLACEMENT_BAR_Y = 310;
	public static final int INTERVAL_BAR_Y = PLACEMENT_BAR_Y + BORDER_PAD;
	public static final int PITCH_BAR_Y = PLACEMENT_BAR_Y + 2 * BORDER_PAD;
	public static final int PAN_BAR_Y = PLACEMENT_BAR_Y + 3 * BORDER_PAD;
	public static final int SCRAMBLE_BAR_Y = PLACEMENT_BAR_Y + 4 * BORDER_PAD;

	// local inside waveform
	public static final int PLAYHEAD_X = 0;
	public static final int PLAYHEAD_Y = 0;
	public static final int PLAYHEAD_W = WAVEFORM_W;
	public static final int PLAYHEAD_H = 258;

	////////////////////////////////////////
	// COLORS
	////////////////////////////////////////
	public static final Color PLACEMENT_COLOR = new Color(237, 24, 72);
	public static final Color DISPLACEMENT_COLOR = new Color(158, 10, 52);
	public static final Color INTERVAL_COLOR = new Color(99, 225, 155);
	public static final Color IRREGULARITY_COLOR = new Color(0, 176,79);
	public static final Color PITCH_COLOR = new Color(96, 200, 255);
	public static final Color DETUNE_COLOR = new Color( 34, 141, 196 );
	public static final Color PAN_COLOR = new Color(255, 216, 60);
	public static final Color WIDTH_COLOR = new Color(197, 168, 52);
	public static final Color SCRAMBLE_COLOR = new Color(114, 139, 89);
	public static final Color CRAWL_COLOR = new Color(147, 115, 115);
	public static final Color ATTACK_COLOR = new Color(228, 126, 240);
	public static final Color RELEASE_COLOR = new Color(235, 38, 177);

	public static final Color[] PARAMETER_COLORS = {
		INTERVAL_COLOR,
		PLACEMENT_COLOR,
		PITCH_COLOR,
		PLACEMENT_COLOR,
		CRAWL_COLOR,
		ATTACK_COLOR,
		RELEASE_COLOR,
		PAN_COLOR,
		IRREGULARITY_COLOR,
		DISPLACEMENT_COLOR,
		DETUNE_COLOR,
		WIDTH_COLOR,
		SCRAMBLE_COLOR,
	};

	public static final Color PLAYHEAD_MAIN_COLOR = new Color(237, 24, 72, 60);
	public static final Color PLAYHEAD_AUX_COLOR = new Color(237, 24, 72, 30);

	public static final Color WAVEFORM_COLOR = new Color(213, 207, 200);
	public static final Color WAVEFORM_SHADE_COLOR = new Color(50, 50, 50);
	public static final Color ENVELOPE_COLOR = WAVEFORM_COLOR;
	public static final Color LINE_MARKING_COLOR = Color.GRAY;
	public static final Color GLOBAL_BAR_COLOR = WAVEFORM_COLOR;
	public static final Color FONT_COLOR = Color.WHITE;
	public static final Color TRANSPARENT = new Color(0, 0, 0, 0);

	////////////////////////////////////////
	// FONTS
	////////////////////////////////////////
	public static final Font FONT_SMALL
	= new Font("Sans", Font.PLAIN, 12);
	public static final Font FONT_LARGE
	= new Font("Sans", Font.PLAIN, 18);


	////////////////////////////////////////
	// CONTROL VALUES
	////////////////////////////////////////
	public static final float[] PARAMETER_SCALE_HEIGHT = {
		1400,	// interval
		1400,	// duration
		2000,	// pitch
		2000,	// position
		700,	// crawl
		700,	// attack
		700,	// release
		400,	// pan
		700,	// irregularity
		700,	// displacement
		700,	// detune
		400,	// width
		400		// scramble
	};

	////////////////////////////////////////
	// TEXTS
	////////////////////////////////////////

	public static final String TXT_NO_FILE_LOADED = "no file loaded";
	public static final String TXT_INVALID_FILE = "the file couldn't be loaded";

	public static final String[] PARAMETER_NAMES = {
		"interval",
		"duration",
		"pitch",
		"position",
		"crawl",
		"attack",
		"release",
		"pan",
		"irregularity",
		"displacement",
		"detune",
		"width",
		"scramble"
	};

	public static final String[] PARAMETER_INFO_TEXTS = {
		"how often things happen.",
		"the lenght of things\n" +
		"happening.",
		"the key where things\n" +
		"happen.",
		"the place where things\n" +
		"start to happen.",
		"to which direction the\n" +
		"position happens to move\n" +
		"on it's own.",
		"there happens to be a\n" +
		"chart for this one\n" +
		"right over there.",
		"it just so happens that\n" +
		"a chart was made for\n" +
		"this one.",
		"stereo balance of\n" +
		"things.",
		"things start to happen\n" +
		"when they're not supposed\n" +
		"to.",
		"a little uncertainty\n" +
		"in position and duration.",
		"nothing happens in the\n" +
		"same key.",
		"things happen around pan.",
		"weird things happen.",
	};

	public static final String TXT_DEFAULT_INFO
		= "hold key and move mouse\n" +
			"up or down to\n" +
			"control parameters.";
	////////////////////////////////////////
	// PAHTS
	////////////////////////////////////////
	public static final String PATH_TO_BG = "/caesura/gui/bg.png";
	public static final String PATH_TO_LOADBUTTON = "/caesura/gui/load.png";
	public static final String PATH_TO_LOADBUTTON_PRESSED
	= "/caesura/gui/loadp.png";
}
