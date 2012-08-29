package EpicsChins.util;

import org.powerbot.game.api.util.Timer;

/**
 * User: Epics
 * Date: 8/28/12
 * Time: 7:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class Data {

	//General
	public static int logWalkCode = 0;
	public static int logAttackCode = 0;
	public static int logBankingCode = 0;

	public static int chinNumber;
	public static Timer t = null;
	public final static int[] WORLDS_MEMBER = {5, 6, 9, 12, 15, 18, 21, 22, 23, 24, 25, 26, 27, 28, 31, 32, 36, 39, 40, 42, 44, 45, 46, 48, 49, 51, 52, 53, 54, 56, 58, 59, 60, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 76, 77, 78, 79, 82, 83, 84, 85, 86, 87, 88, 89, 91, 92, 96, 97, 99, 100, 103, 104, 105, 114, 115, 116, 117, 119, 123, 124, 137, 138, 139};

	//Item IDs
	public final static int FLASK_PRAYER_RENEWAL_FULL = 23609;
	public final static int FLASK_ANTIPOISON_SUPER_FULL = 23327;
	public final static int FLASK_ANTIPOISON_PLUSPLUS_FULL = 23591;
	public final static int FLASK_ANTIPOISON_PLUS_FULL = 23579;
	public final static int FLASK_ANTIPOISON_FULL = 23315;

	public final static int MIX_ANTIPOISON_FULL = 11433;

	public final static int POT_ANTIPOISON_SUPER_FULL = 2448;
	public final static int POT_ANTIPOISON_PLUSPLUS_FULL = 5952;
	public final static int POT_ANTIPOISON_PLUS_FULL = 5943;
	public final static int POT_ANTIPOISON_FULL = 2446;

	public final static int ELIXIR_ANTIPOISON = 20879;

	public final static int TAB_VARROCK = 8007;

	//Item arrays
	public final static int[] ANTIPOISON_ALL = {23327, 23329, 23331, 23333, 23335, 23337, 23591, 23593, 23595, 23597, 23599, 23601, 23579, 23581, 23583, 23585, 23587, 23589, 23315, 23317, 23319, 23321, 23323, 23325, 11433, 11435, 2448, 181, 183, 185, 5952, 5954, 5956, 5958, 5943, 5945, 5947, 5949, 2446, 175, 177, 179, 20879};

	public final static int[] FLASK_RANGING = {23303, 23305, 23307, 23309, 23311, 23313};
	public final static int FLASK_RANGING_FULL = 23303;
	public final static int[] POT_PRAYER = {2434, 139, 141, 143};
	public final static int POT_PRAYER_DOSE_4 = 2434;
	public final static int[] FLASK_PRAYER_RENEWAL = {23609, 23611, 23613, 23615, 23617, 23619};

	public final static int[] GREEGREE_IDS = {4031, 4024, 4025, 40256, 4027, 4028, 4029, 4030};

	// NPC IDs
	public final static int ID_NPC_DAERO = 824;
	public final static int ID_NPC_WAYDAR = 1407;
	public final static int ID_NPC_LUMBO = 1408;
	public final static int ID_NPC_MONKEY_ZOMBIE = 1465;

	// Animation IDs
	public final static int ID_ANIMATION_TREE = 7082;
	public final static int ID_ANIMATION_TREE_2 = 7084;
	public final static int ID_ANIMATION_PRAY = 645;
	public final static int ID_ANIMATION_DEATH_ZOMBIE = 1384;

	//SceneObject IDs
	public final static int ID_TREEDOOR = 69198;
	public final static int ID_SPIRITTREE_GE = 1317;
	public final static int ID_SPIRITTREE_MAIN = 68974;
	public final static int ID_LADDER_GNOME = 69499;
	public final static int ID_LADDER_APE = 2745; // or 4780
	public final static int ID_ALTAR_VARROCK = 24343;

	//Booleans
	public static boolean START_SCRIPT;
	public static boolean usingGreegree;
	public static boolean checkChins = true;
	public static boolean runCheck = true;
	public static boolean SHOWPAINT;
}
