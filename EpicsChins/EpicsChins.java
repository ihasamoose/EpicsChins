import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.ActiveScript;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Equipment;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Prayer;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.methods.widget.Lobby;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.interactive.Player;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import org.powerbot.game.bot.Context;
import org.powerbot.game.bot.event.MessageEvent;
import org.powerbot.game.bot.event.listener.PaintListener;

@Manifest(authors = { "Epics" }, name = "Epics Chinner", description = "Kills chins and banks when necessary.", version = 0.1)
public class EpicsChins extends ActiveScript implements PaintListener,
		MouseListener {
	// GUI
	private GUI gui;
	// GUI variables
	private int foodUser = 0; // user selected food
	private int antipoisonUser = 0; // user selected Antipoison
	private boolean usingGreegree, START_SCRIPT, SHOWPAINT, runCheck = true;
	private boolean moveNeeded = false;
	String version = " v0.1";
	// Paint variables
	private long startTime;
	private int zombieKillCount;
	private final Timer RUNTIME = new Timer(0);
	private int rangedStartExp;
	private int hpStartExp;
	private int rangeGainedExp;
	private int hpGainedExp;
	private int expHr;
	private int chinsThrown;
	private final int CHIN_THROW_ID = 2779;
	private int mouseX = 0;
	private int mouseY = 0;
	// Antiban variables
	private final int RANDOM_PITCH = Random.nextInt(350, 10);
	private final int RANDOM_ANGLE = Random.nextInt(89, 50);
	private int state;
	// Logging ints
	private int logWalkCode = 0;
	private int logAttackCode = 0;
	private int logBankingCode = 0;
	// Members Worlds array
	private final int[] WORLDS_MEMBER = { 5, 6, 9, 12, 15, 18, 21, 22, 23, 24,
			25, 26, 27, 28, 31, 32, 36, 39, 40, 42, 44, 45, 46, 48, 49, 51, 52,
			53, 54, 56, 58, 59, 60, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72,
			73, 74, 76, 77, 78, 79, 82, 83, 84, 85, 86, 87, 88, 89, 91, 92, 96,
			97, 99, 100, 103, 104, 105, 114, 115, 116, 117, 119, 123, 124, 137,
			138, 139 };
	// Path details
	private final static Tile[] PATH_TO_PRAYER_FROM_GE = {
			new Tile(3185, 3492, 0), new Tile(3197, 3480, 0),
			new Tile(3196, 3462, 0), new Tile(3216, 3464, 0),
			new Tile(3233, 3466, 0), new Tile(3245, 3480, 0) };
	private final static Tile[] PATH_TO_PRAYER_FROM_TELE = {
			new Tile(3216, 3464, 0), new Tile(3233, 3466, 0),
			new Tile(3245, 3480, 0) };
	private final static Tile[] PATH_TO_CHIN_TILE_1 = {
			new Tile(2770, 9120, 0), new Tile(2758, 9133, 0),
			new Tile(2740, 9137, 0), new Tile(2722, 9140, 0),
			new Tile(2714, 9138, 0) };
	private final static Tile[] PATH_TO_CHIN_TILE_2 = {
			new Tile(2731, 9130, 0), new Tile(2744, 9125, 0) };
	private final static Tile[] PATH_TO_CHIN_TILE_3 = {
			new Tile(2743, 9120, 0), new Tile(2738, 9120, 0),
			new Tile(2737, 9121, 0), new Tile(2720, 9118, 0) };
	private final static Tile[] PATH_TO_BANK_TILE = { new Tile(3205, 3440, 0),
			new Tile(3191, 3451, 0), new Tile(3175, 3459, 0) };
	private final static Tile TILE_GRAND_BANK = new Tile(3181, 3502, 0);
	private final static Tile TILE_GRAND_TREE = new Tile(3185, 3508, 0);
	private final static Tile TILE_APE_LADDER_TOP = new Tile(2764, 2703, 0);
	private final static Tile TILE_CHIN_1 = new Tile(2715, 9127, 0);
	private final static Tile TILE_CHIN_2 = new Tile(2746, 9122, 0);
	private final static Tile TILE_CHIN_3 = new Tile(2709, 9116, 0);
	private final static Tile TILE_CHIN_4 = new Tile(2701, 9111, 0);
	private final static Tile TILE_TREE_DOOR = new Tile(2466, 3491, 0);
	private final static Tile TILE_TREE_DAERO = new Tile(2480, 3488, 1);
	private final static Tile TILE_PRAYER = new Tile(3253, 3485, 0);
	private final static Tile TILE_MOVE_1 = new Tile(2713, 9119, 0);
	private final static Area AREA_CHIN_3_4 = new Area(new Tile(2709, 9116, 0),
			new Tile(2701, 9111, 0));
	public final static Area AREA_GE = new Area(new Tile(3135, 3464, 0),
			new Tile(3203, 3516, 0));
	private final static Area AREA_BLINDFOND_ZONE = new Area(new Tile(2660,
			4501, 0), new Tile(2641, 4531, 0));
	private final static Area AREA_CRASH_ISLAND = new Area(new Tile(2880, 2735,
			0), new Tile(2903, 2711, 0));
	private final static Area AREA_INSIDE_TREE_DOOR = new Area(new Tile(2466,
			3493, 0), new Tile(2465, 3495, 0));
	private final static Area AREA_GRAND_TELE = new Area(
			new Tile(3208, 3430, 0), new Tile(3217, 2422, 0));
	private final static Area AREA_SPIRIT_MID = new Area(
			new Tile(2544, 3172, 0), new Tile(2541, 3167, 0));
	private final static Area AREA_GNOME_STRONGHOLD = new Area(new Tile(2470,
			3440, 0), new Tile(2457, 3492, 0));
	private final static Area AREA_APE_ATOLL = new Area(
			new Tile(2809, 2690, 0), new Tile(2753, 2718, 0));
	private final static Area AREA_GNOME_LEVEL_ONE = new Area(new Tile(2490,
			3478, 1), new Tile(2440, 3512, 1));
	public final static Area AREA_APE_ATOLL_DUNGEON = new Area(new Tile(2805,
			9144, 0), new Tile(2704, 9043, 0));
	private final static Tile[] CHIN_ARRAY = { TILE_CHIN_1, TILE_CHIN_2,
			TILE_CHIN_3, TILE_CHIN_4 };
	// GREEGREE_IDS IDs
	private final static int[] GREEGREE_IDS = { 4031, 4024, 4025, 40256, 4027,
			4028, 4029, 4030 };
	// Potion IDs
	private final static int[] ANTIPOISON_ALL = { 23327, 23329, 23331, 23333,
			23335, 23337, 23591, 23593, 23595, 23597, 23599, 23601, 23579,
			23581, 23583, 23585, 23587, 23589, 23315, 23317, 23319, 23321,
			23323, 23325, 11433, 11435, 2448, 181, 183, 185, 5952, 5954, 5956,
			5958, 5943, 5945, 5947, 5949, 2446, 175, 177, 179, 20879 };
	private final static int[] FLASK_RANGING = { 23303, 23305, 23307, 23309,
			23311, 23313 };
	private final static int FLASK_RANGING_FULL = 23303;
	private final static int[] POT_PRAYER = { 2434, 139, 141, 143 };
	private final static int POT_PRAYER_DOSE_4 = 2434;
	private final static int[] FLASK_PRAYER_RENEWAL = { 23609, 23611, 23613,
			23615, 23617, 23619 };
	private final static int FLASK_PRAYER_RENEWAL_FULL = 23609;
	private final static int FLASK_ANTIPOISON_SUPER_FULL = 23327;
	private final static int FLASK_ANTIPOISON_PLUSPLUS_FULL = 23591;
	private final static int FLASK_ANTIPOISON_PLUS_FULL = 23579;
	private final static int FLASK_ANTIPOISON_FULL = 23315;
	private final static int MIX_ANTIPOISON_FULL = 11433;
	private final static int POT_ANTIPOISON_SUPER_FULL = 2448;
	private final static int POT_ANTIPOISON_PLUSPLUS_FULL = 5952;
	private final static int POT_ANTIPOISON_PLUS_FULL = 5943;
	private final static int POT_ANTIPOISON_FULL = 2446;
	private final static int ELIXIR_ANTIPOISON = 20879;
	// Tab IDs
	private final static int TAB_VARROCK = 8007;
	private final static int TAB_LUMBRIDGE = 8008;
	private final static int TAB_FALADOR = 8009;
	private final static int TAB_CAMELOT = 8010;
	private final static int TAB_ARDOUGNE = 8011;
	private final static int TAB_WATCHTOWER = 8012;
	private final static int TAB_HOUSE = 8013;
	private final static int[] tab = { TAB_VARROCK, TAB_FALADOR, TAB_LUMBRIDGE,
			TAB_CAMELOT, TAB_ARDOUGNE, TAB_WATCHTOWER, TAB_HOUSE };
	// General IDs
	Timer t = null;
	private int chinNumber;
	// Interaction IDs
	private final static int ID_ANIMATION_TREE = 7082;
	private final static int ID_ANIMATION_PRAY = 645;
	private final static int ID_TREEDOOR = 69197;
	private final static int ID_SPIRITTREE_GE = 1317;
	private final static int ID_SPIRITTREE_MAIN = 68974;
	private final static int ID_LADDER_GNOME = 69499;
	private final static int ID_LADDER_APE = 2745; // or 4780
	private final static int ID_ALTAR_VARROCK = 24343;
	private final static int ID_ANIMATION_DEATH_ZOMBIE = 1384;
	// NPC IDs
	private final static int ID_NPC_DAERO = 824;
	private final static int ID_NPC_WAYDAR = 1407;
	private final static int ID_NPC_LUMBO = 1408;
	private final static int ID_NPC_MONKEY_ZOMBIE = 1465;

	@Override
	protected void setup() {

		try {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					gui = new GUI();
					gui.setVisible(true);
				}
			});
		} catch (Exception e) {
		}

		rangedStartExp = Skills.getExperience(Skills.RANGE);
		hpStartExp = Skills.getExperience(Skills.CONSTITUTION);
		startTime = System.currentTimeMillis();

		provide(new Checks());
		provide(new RunToChins());
		provide(new ThrowChins());
		provide(new Banking());
	}

	private class Checks extends Strategy implements Runnable {

		@Override
		public void run() {
			final Item GREEGREE_ITEM = Bank.getItem(new Filter<Item>() {
				@Override
				public boolean accept(final Item m) {
					for (int id : GREEGREE_IDS) {
						if (m.getId() == id)
							return true;
					}
					return false;
				}
			});
			log.info("Running checks to get current count of chins and make sure autoretaliate is on!");
			if (Equipment.getItem(10034) != null) {
				chinNumber = Equipment.getItem(10034).getStackSize();
				log.info("Number of chins equipped: "
						+ String.valueOf(chinNumber));
			} else if (Equipment.getItem(GREEGREE_ITEM.getId()) != null) {
				log.info("Using greegree, counting chins in inventory...");
				chinNumber = Inventory.getItem(10034).getStackSize();
				log.info("Number of chins in inventory: "
						+ String.valueOf(chinNumber));
			}

			if (Tabs.ATTACK.open()) {
				if (Settings.get(172) != 0) {
					log.info("Auto retaliate not on, setting");
					Widgets.get(884).getChild(13).click(true);
					Timer t = new Timer(2500);
					while (Settings.get(172) != 0 && t.isRunning()) {
						Time.sleep(50);
					}
				}
			}
			runCheck = false;
		}

		@Override
		public boolean validate() {
			return runCheck && START_SCRIPT && Game.isLoggedIn();
		}
	}

	private class RunToChins extends Strategy implements Runnable {
		@Override
		public void run() {
			// Interaction ID
			if (logWalkCode == 0) {
				log.info("Running walk there code");
				logWalkCode++;
			}
			SceneObject spiritTreeGe = SceneEntities
					.getNearest(ID_SPIRITTREE_GE);
			if (!CHIN_ARRAY.equals(Players.getLocal().getLocation())) {
				final Item FOOD = Inventory.getItem(foodUser);
				final Item PRAYER_POT = Inventory.getItem(POT_PRAYER_DOSE_4);
				checkRun();
				doPreEat(FOOD, PRAYER_POT);
				if (AREA_GRAND_TELE.contains(Players.getLocal().getLocation())) {// TODO
					log.info("You have the n00b varrock teleport. Walking to bank");
					if (TILE_GRAND_BANK != null) {
						while (Calculations.distanceTo(TILE_GRAND_BANK) >= 2) {
							Walking.findPath(TILE_GRAND_BANK).traverse();
						}
					}
					if (TILE_GRAND_BANK == null) {
						Walking.newTilePath(PATH_TO_BANK_TILE).traverse();
						while (TILE_GRAND_BANK != null
								&& Calculations.distanceTo(TILE_GRAND_BANK) >= 2) {
							Walking.findPath(TILE_GRAND_BANK).traverse();
						}
					}
					Camera.turnTo(TILE_GRAND_BANK);
					log.info("You aren't in the Grand Exchange or the n00b teleport zone! Shutting down...");
					stop();
				}
				double PRAYER_POINTS = (Skills.getRealLevel(Skills.PRAYER));
				double PRAYER_DIFFERENCE = (PRAYER_POINTS * 0.3);
				if (PRAYER_DIFFERENCE < PRAYER_POINTS) {
					if (logWalkCode == 1) {
						log.info("Prayer is low, let's go charge up before we head out.");
						logWalkCode++;
					}
					if (AREA_GE.contains(Players.getLocal().getLocation())) {
						Walking.newTilePath(PATH_TO_PRAYER_FROM_GE).traverse();
						if (TILE_PRAYER == null) {
							Walking.newTilePath(PATH_TO_PRAYER_FROM_GE)
									.traverse();
						}
						if (TILE_PRAYER != null) {
							while (Calculations.distanceTo(TILE_PRAYER) >= 5) {
								Walking.findPath(TILE_PRAYER).traverse();
							}
						}
					} else if (AREA_GRAND_TELE.contains(Players.getLocal()
							.getLocation())) {
						if (TILE_PRAYER == null) {
							Walking.newTilePath(PATH_TO_PRAYER_FROM_TELE)
									.traverse();
						}
						if (TILE_PRAYER != null) {
							while (Calculations.distanceTo(TILE_PRAYER) >= 5) {
								Walking.findPath(TILE_PRAYER).traverse();
							}
						}
					} else if (!AREA_GRAND_TELE.contains(Players.getLocal()
							.getLocation())
							&& !AREA_GE.contains(Players.getLocal()
									.getLocation())) {
						if (TILE_PRAYER == null) {
							Walking.newTilePath(PATH_TO_PRAYER_FROM_GE)
									.traverse();
						}
						if (TILE_PRAYER != null) {
							while (Calculations.distanceTo(TILE_PRAYER) >= 5) {
								Walking.findPath(TILE_PRAYER).traverse();
							}
						}
					}
					if (TILE_PRAYER != null
							&& Calculations.distanceTo(TILE_PRAYER) <= 5) {
						SceneObject varrockAltar = SceneEntities
								.getNearest(ID_ALTAR_VARROCK);
						if (varrockAltar != null && varrockAltar.isOnScreen()) {
							varrockAltar.click(true);
							if (Players.getLocal().getAnimation() == ID_ANIMATION_PRAY) {
								Time.sleep(100, 400);
							}
							if (Prayer.getPoints() == (Skills
									.getRealLevel(Skills.PRAYER) * 10)) {
								log.info("All charged up, let's get going.");
							}
						} else {
							Camera.turnTo(varrockAltar);
						}
					}
				}
				if (TILE_GRAND_TREE != null) {
					Walking.findPath(TILE_GRAND_TREE).traverse();
				} else if (TILE_GRAND_TREE == null) {
					while (TILE_GRAND_TREE == null) {
						Walking.newTilePath(PATH_TO_PRAYER_FROM_GE).reverse()
								.traverse();
					}
					if (TILE_GRAND_TREE != null) {
						Walking.findPath(TILE_GRAND_TREE).traverse();
					}
				}
				if (spiritTreeGe != null && spiritTreeGe.isOnScreen()
						&& spiritTreeGe.interact("Teleport")) {
					final Timer SPIRIT_TREE_TIMER = new Timer(2500);
					while (spiritTreeGe.validate()
							&& SPIRIT_TREE_TIMER.isRunning()) {
						Time.sleep(50);
					}
				}
			}
			SceneObject SpiritTreeMain = SceneEntities
					.getNearest(ID_SPIRITTREE_MAIN);
			if (SpiritTreeMain != null
					&& AREA_SPIRIT_MID.contains(Players.getLocal()
							.getLocation()) && SpiritTreeMain.isOnScreen()
					&& SpiritTreeMain.interact("Teleport")) {
				final Timer SPIRIT_TREE_MAIN_TIMER = new Timer(2500);
				while (SpiritTreeMain.validate()
						&& SPIRIT_TREE_MAIN_TIMER.isRunning()) {
					Time.sleep(50);
				}
				Time.sleep(50, 400);
				final WidgetChild SPIRIT_TREE_INTERFACE = Widgets.get(864)
						.getChild(6).getChild(0);
				if (SPIRIT_TREE_INTERFACE.validate()
						&& SPIRIT_TREE_INTERFACE.click(true)) {
					final Timer SPIRIT_TREE_INTERFACE_TIMER = new Timer(2500);
					while (SPIRIT_TREE_INTERFACE.validate()
							&& SPIRIT_TREE_INTERFACE_TIMER.isRunning()) {
						Time.sleep(50);
					}
					while (Players.getLocal().getAnimation() == ID_ANIMATION_TREE) {
						Time.sleep(50);
					}
				}
			} else if (SpiritTreeMain != null && !SpiritTreeMain.isOnScreen()) {
				Camera.turnTo(SpiritTreeMain);
				Time.sleep(Random.nextInt(500, 1000));
			}
			SceneObject treeDoor = SceneEntities.getNearest(ID_TREEDOOR);
			if (AREA_GNOME_STRONGHOLD
					.contains(Players.getLocal().getLocation())) {
				Walking.findPath(TILE_TREE_DOOR).traverse();
			}
			if (TILE_TREE_DOOR.equals(Players.getLocal().getLocation())
					&& treeDoor != null && treeDoor.isOnScreen()
					&& treeDoor.interact("Open")) {
				final Timer TREE_DOOR_TIMER = new Timer(2500);
				while (treeDoor.validate() && TREE_DOOR_TIMER.isRunning()) {
					Time.sleep(50);
				}
				if (AREA_INSIDE_TREE_DOOR.contains(Players.getLocal()
						.getLocation())) {
					SceneObject gnomeLadder = SceneEntities
							.getNearest(ID_LADDER_GNOME);
					if (gnomeLadder != null
							&& Players.getLocal().getAnimation() == -1
							&& gnomeLadder.interact("Climb-up")) {
						final Timer GNOME_LADDER_TIMER = new Timer(2500);
						while (gnomeLadder.validate()
								&& GNOME_LADDER_TIMER.isRunning()) {
							Time.sleep(50);
						}
					} else if (!gnomeLadder.isOnScreen()) {
						Camera.turnTo(gnomeLadder);
					}
				}
			}

			if (AREA_GNOME_LEVEL_ONE.contains(Players.getLocal().getLocation())) {
				Walking.findPath(TILE_TREE_DAERO).traverse();
				NPC daero = NPCs.getNearest(ID_NPC_DAERO);
				if (daero != null && daero.isOnScreen()
						&& daero.interact("Travel")) {
					final Timer DAERO_TIMER = new Timer(2500);
					while (daero.validate() && DAERO_TIMER.isRunning()) {
						yesInterfaceClicker();
						Time.sleep(50);
					}
				} else if (!daero.isOnScreen()) {
					Camera.turnTo(daero);
				}
			}
			if (AREA_BLINDFOND_ZONE.contains(Players.getLocal().getLocation())) {
				NPC waydar = NPCs.getNearest(ID_NPC_WAYDAR);
				Walking.findPath(waydar).traverse();
				if (waydar != null && waydar.isOnScreen()
						&& waydar.getAnimation() == -1
						&& waydar.interact("Travel")) {
					final Timer WAYDAR_TIMER = new Timer(2500);
					while (waydar.validate() && WAYDAR_TIMER.isRunning()) {
						yesInterfaceClicker();
						Time.sleep(50);
					}
				} else if (!waydar.isOnScreen()) {
					Camera.turnTo(waydar);
				}
			}
			if (AREA_CRASH_ISLAND.contains(Players.getLocal().getLocation())) {
				NPC lumdo = NPCs.getNearest(ID_NPC_LUMBO);
				if (lumdo != null && lumdo.isOnScreen()
						&& lumdo.getAnimation() == -1
						&& lumdo.interact("Travel")) {
					final Timer LUMDO_TIMER = new Timer(2500);
					while (lumdo.validate() && LUMDO_TIMER.isRunning()) {
						yesInterfaceClicker();
						Time.sleep(50);
					}
				} else if (!lumdo.isOnScreen()) {
					Camera.turnTo(lumdo);
				}
			}
			if (AREA_APE_ATOLL.contains(Players.getLocal().getLocation())
					&& usingGreegree) {
				equipGreegree();
				Walking.findPath(TILE_APE_LADDER_TOP).traverse();
				SceneObject apeAtollLadder = SceneEntities
						.getNearest(ID_LADDER_APE);
				if (apeAtollLadder != null
						&& apeAtollLadder.isOnScreen()
						&& TILE_APE_LADDER_TOP.equals(Players.getLocal()
								.getLocation())
						&& apeAtollLadder.interact("Climb-down")) {
					Timer APE_ATOLL_LADDER_TIMER = new Timer(2500);
					while (apeAtollLadder.validate()
							&& APE_ATOLL_LADDER_TIMER.isRunning()) {
						Time.sleep(50);
					}
				} else if (!apeAtollLadder.isOnScreen()) {
					Camera.turnTo(apeAtollLadder);
				}
			} else if (AREA_APE_ATOLL
					.contains(Players.getLocal().getLocation())
					&& !usingGreegree) {
				checkRun();
			}
			if (AREA_APE_ATOLL_DUNGEON.contains(Players.getLocal()
					.getLocation())) {
				checkRenewal();
				Prayer.setQuick();
				while (Calculations.distanceTo(TILE_CHIN_1) >= 5) {
					Walking.newTilePath(PATH_TO_CHIN_TILE_1).traverse();
					Time.sleep(50);
				}
				if (tileContainsTwoOrMore(TILE_CHIN_1)) {
					while (Calculations.distanceTo(TILE_CHIN_2) >= 5) {
						Walking.newTilePath(PATH_TO_CHIN_TILE_2).traverse();
						Timer t = new Timer(2500);
						while (Players.getLocal().getSpeed() != 0
								&& t.isRunning()) {
							Time.sleep(50);
						}
						Walking.findPath(TILE_CHIN_2).traverse();
					}
					if (tileContainsTwoOrMore(TILE_CHIN_2)) {
						while (Calculations.distanceTo(TILE_CHIN_3) >= 5) {
							Walking.newTilePath(PATH_TO_CHIN_TILE_3).traverse();
							Timer t = new Timer(2500);
							while (Players.getLocal().getSpeed() != 0
									&& t.isRunning()) {
								Time.sleep(50);
							}
							Walking.findPath(TILE_CHIN_3).traverse();
						}
						if (areaContainsTwoOrMore(AREA_CHIN_3_4)) {
							changeWorlds();
						} else {
							if (!TILE_CHIN_3.equals(Players.getLocal()
									.getLocation())) {
								Walking.findPath(TILE_CHIN_3).traverse();
							}
						}
					} else {
						while (!TILE_CHIN_2.equals(Players.getLocal()
								.getLocation())) {
							Walking.findPath(TILE_CHIN_2).traverse();
						}
					}
				} else {
					while (!TILE_CHIN_1
							.equals(Players.getLocal().getLocation())) {
						Walking.findPath(TILE_CHIN_1).traverse();
					}
				}
			}
			logWalkCode = 0;
		}

		@Override
		public boolean validate() {
			int prayerPotCountData = 0;
			for (Item y : Inventory.getItems()) {
				for (int x : POT_PRAYER) {
					if (y.getId() == x) {
						prayerPotCountData++;
					}
				}
			}
			int flaskRenewalCountData = 0;
			for (Item y : Inventory.getItems()) {
				for (int x : FLASK_PRAYER_RENEWAL) {
					if (y.getId() == x) {
						flaskRenewalCountData++;
					}
				}
			}
			int rangingFlaskData = 0;
			for (Item y : Inventory.getItems()) {
				for (int x : FLASK_RANGING) {
					if (y.getId() == x) {
						rangingFlaskData++;
					}
				}
			}
			int antiPoisonData = 0;
			for (Item y : Inventory.getItems()) {
				for (int x : ANTIPOISON_ALL) {
					if (y.getId() == x) {
						antiPoisonData++;
					}
				}
			}
			return !AREA_APE_ATOLL_DUNGEON.contains(Players.getLocal()
					.getLocation())
					&& !isPoisoned()
					&& Inventory.getCount(foodUser) >= 1
					&& flaskRenewalCountData == 3
					&& prayerPotCountData == 18
					&& rangingFlaskData == 3
					&& antiPoisonData == 1
					&& TAB_VARROCK > 0
					&& chinNumber >= 500
					&& !AREA_APE_ATOLL_DUNGEON.contains(Players.getLocal()
							.getLocation())
					&& START_SCRIPT
					&& Game.isLoggedIn();
		}
	}

	private class ThrowChins extends Strategy implements Runnable {
		private NPC monkeyZombie;

		@Override
		public void run() {
			if (logAttackCode == 0) {
				log.info("Running attack code");
				logAttackCode++;
			}
			if (Inventory.getCount(chinNumber) >= 1) {
				log.severe("Chins are in inventory, that must mean greegree is equipped. Equipping...");
				Item chinNumberItem = Inventory.getItem(10034);
				chinNumberItem.getWidgetChild().click(true);
				final int id = 10034;
				final int count = Inventory.getCount(id);
				final Timer t = new Timer(2500);
				while (t.isRunning() && Inventory.getCount(id) == count) {
					Time.sleep(50);
				}
			}
			final Item RANGE_POT_ITEM = Inventory.getItem(FLASK_RANGING);
			final int REAL_RANGE = Skills.getRealLevel(Skills.RANGE);
			final int POTTED_RANGE = Skills.getLevel(Skills.RANGE);
			final int RANGE_DIFFERENCE = POTTED_RANGE - REAL_RANGE;

			if (Players.getLocal().getInteracting() != null) {
				Timer throwtimer = new Timer(5000);
				while (Players.getLocal().getAnimation() == CHIN_THROW_ID
						&& throwtimer.isRunning() && isRunning()) {
					Time.sleep(Random.nextInt(20, 50));
					if (Players.getLocal().getAnimation() == CHIN_THROW_ID) {
						chinNumber--;
					}
				}
			}
			doAttackMonkey(monkeyZombie);
			if (RANGE_POT_ITEM != null
					&& Players.getLocal().getInteracting() != null
					&& Prayer.getPoints() >= 42 && !isPoisoned()
					&& Players.getLocal().getHpPercent() >= 90
					&& RANGE_DIFFERENCE >= 3) {
				log.info("Killing monkeys and nothing is needed. Using antiban...");
				antiban();
			}
			Time.sleep(Random.nextInt(50, 75));
			if (!Prayer.isQuickOn()) {
				Prayer.setQuick();
				if (Players.getLocal().getPrayerIcon() == Prayer.PRAYER_BOOK_NORMAL) {
					if (Players.getLocal().getPrayerIcon() == 19) {
					} else {
						Logger.getLogger("EpicsChins")
								.info("You didn't set up your quick prayer correctly. Shutting down...");
					}
				} else if (Players.getLocal().getPrayerIcon() == 9) {
				} else {
					Logger.getLogger("EpicsChins")
							.info("You didn't set up your quick prayer correctly. Shutting down...");

				}
			}
			if (isPoisoned()) {
				doDrinkAntipoison();
			} else {
				log.info("We're out of antipoison & we're poisoned! Teleporting to safety to bank...");
				doBreakTab();
			}
			if (Players.getLocal().getAnimation() == CHIN_THROW_ID) {
				chinsThrown++;
				Time.sleep(Random.nextInt(20, 50));
			}
			if (monkeyZombie != null
					&& monkeyZombie.getAnimation() == ID_ANIMATION_DEATH_ZOMBIE) {
				zombieKillCount++;
			}
			final int VIAL_ID = 229;
			final Item VIAL = Inventory.getItem(VIAL_ID);
			if (Inventory.getItem() == VIAL) {
				VIAL.getWidgetChild().interact("Drop");
			}
			logAttackCode = 0;
		}

		@Override
		public boolean validate() {
			int prayPotCountData = 0;
			for (Item y : Inventory.getItems()) {
				for (int x : POT_PRAYER) {
					if (y.getId() == x) {
						prayPotCountData++;
					}
				}
			}
			return (monkeyZombie = NPCs.getNearest(ID_NPC_MONKEY_ZOMBIE)) != null
					&& CHIN_ARRAY.equals(Players.getLocal().getLocation())
					&& chinNumber >= 200
					&& prayPotCountData >= 1
					&& START_SCRIPT && Game.isLoggedIn();
		}
	}

	private class Banking extends Strategy implements Runnable {
		@Override
		public void run() {
			if (logBankingCode == 0) {
				log.info("Running banking code");
				logBankingCode++;
			}
			final Item GREEGREE_ITEM = Bank.getItem(new Filter<Item>() {
				@Override
				public boolean accept(final Item m) {
					for (int id : GREEGREE_IDS) {
						if (m.getId() == id)
							return true;
					}
					return false;
				}
			});
			final Item PRAYER_RENEWAL_FLASK_ITEM = Bank
					.getItem(new Filter<Item>() {
						@Override
						public boolean accept(final Item p) {
							for (int id : FLASK_PRAYER_RENEWAL) {
								if (p.getId() == id)
									return true;
							}
							return false;
						}
					});

			if (AREA_GRAND_TELE.contains(Players.getLocal().getLocation())) {
				log.info("You have the n00b varrock teleport. Walking to bank");
			}
			if (TILE_GRAND_BANK != null
					&& Calculations.distanceTo(TILE_GRAND_BANK) >= 2) {
				Walking.findPath(TILE_GRAND_BANK).traverse();
			}
			Camera.turnTo(TILE_GRAND_BANK);
			if (!AREA_GE.contains(Players.getLocal().getLocation())
					&& !AREA_GRAND_TELE.contains(Players.getLocal()
							.getLocation()) && Inventory.getCount(tab) >= 1) {
				doBreakTab();
			}
			checkRun();
			if (!Bank.open()) {
				Bank.open();
			}
			Time.sleep(Random.nextInt(500, 700));
			if (chinNumber < 2000 && Bank.isOpen()) {
				if (chinNumber == 0) {
					log.info("NO chins detected. This means that you haven't set up your equipment right. Check it and try again!");
					stop();
					Game.logout(true);
				}
				if (chinNumber < 2000 && Bank.withdraw(10034, 2000)) {
					Time.sleep(80);
					Bank.close();
					if (!Bank.isOpen()) {
						log.info("Recalculating chin count");
						chinNumber = Equipment.getItem(10034).getStackSize();
					}
				} else if (Bank.getItem(10034).getStackSize() <= 1500
						&& chinNumber < 2000) {
					log.info("Not enough chins to continue! Shutting down...");
					Game.logout(true);
					stop();
				}
				if (Bank.close()) {
					if (Inventory.getCount(10034) >= 0) {
						Item chinItem = Inventory.getItem(10034);
						chinItem.getWidgetChild().click(true);
						if (Inventory.getCount(10034) < 1) {
							chinNumber = Equipment.getItem(10034)
									.getStackSize();
						}
					}
				}
			}
			if (usingGreegree) {
				log.info("Selected use a greegree, banking accordingly");
				if (Inventory.getCount() == 28
						&& Inventory.getCount(foodUser) < 1) {
					log.info("Supplies are done wrong, restarting...");
					Bank.depositInventory();
				}
				if (Inventory.getCount(foodUser) == 0
						&& Bank.getItemCount(foodUser) >= 1) {
					Bank.withdraw(foodUser, 1);
					Time.sleep(80);
					if (Inventory.getCount(foodUser) != 0) {
						log.severe("Food withdrawn");
					}
				} else if (Bank.getItem(foodUser).getStackSize() <= 1
						&& Inventory.getCount(foodUser) < 1) {
					log.severe("Food isn't present, shutting down...");
					Bank.close();
					Game.logout(true);
					stop();
				}
				if (GREEGREE_ITEM != null
						&& Inventory.getCount(GREEGREE_ITEM.getId()) == 0
						&& usingGreegree
						&& Bank.getItemCount(GREEGREE_ITEM.getId()) >= 1) {
					Bank.withdraw(GREEGREE_ITEM.getId(), 1);
					Time.sleep(80);
					if (Inventory.getCount(GREEGREE_ITEM.getId()) != 0) {
						log.severe("Greegree withdrawn");
					}
				} else if (Bank.getItemCount(GREEGREE_ITEM.getId()) == 0
						&& usingGreegree
						&& Inventory.getCount(GREEGREE_ITEM.getId()) == 0) {
					log.info("No greegree is present. Shutting down...");
					Bank.close();
					Game.logout(true);
					stop();
				}
				if (!usingGreegree) {
					log.info("Selected not to use a greegree, banking accordingly");
				}
				if (Inventory.getCount(POT_PRAYER_DOSE_4) == 0) {
					Bank.withdraw(POT_PRAYER_DOSE_4, 18);
					Time.sleep(80);
					if (Inventory.getCount(POT_PRAYER_DOSE_4) != 0) {
						log.severe("Prayer pots withdrawn");
					} else if (Bank.getItem(POT_PRAYER_DOSE_4).getStackSize() < 18
							&& Inventory.getCount(POT_PRAYER_DOSE_4) < 18) {
						log.info("Not enough prayer pots. Shutting down... ");
						Bank.close();
						Game.logout(true);
						stop();
					}
				}
				if (Inventory.getCount(antipoisonUser) == 0
						&& Bank.getItemCount(antipoisonUser) > 0) {
					Bank.withdraw(antipoisonUser, 1);
					Time.sleep(80);
					if (Inventory.getCount(antipoisonUser) != 0) {
						log.severe("Antipoison withdrawn");
					} else if (Bank.getItem(antipoisonUser).getStackSize() < 1
							&& Inventory.getCount(antipoisonUser) < 1) {
						log.info("Not enough antipoison. Shutting down...");
						Bank.close();
						Game.logout(true);
						stop();
					}
				}
				if (Inventory.getCount(TAB_VARROCK) == 0
						&& Bank.getItemCount(TAB_VARROCK) > 0) {
					Bank.withdraw(TAB_VARROCK, 1);
					Time.sleep(80);
					if (Inventory.getCount(TAB_VARROCK) != 0) {
						log.severe("Varrock tab withdrawn");
					} else if (Bank.getItem(TAB_VARROCK).getStackSize() == 0
							&& Inventory.getCount(TAB_VARROCK) < 1) {
						log.info("Not enough tabs. Shutting down...");
						Bank.close();
						Game.logout(true);
						stop();
					}
				}
				if (Inventory.getCount(FLASK_PRAYER_RENEWAL_FULL) == 0) {
					Bank.withdraw(FLASK_PRAYER_RENEWAL_FULL, 3);
					Time.sleep(80);
					if (Inventory.getCount(PRAYER_RENEWAL_FLASK_ITEM.getId()) != 0) {
						log.severe("Renewal flasks withdrawn");
					} else if (Bank.getItem(FLASK_PRAYER_RENEWAL_FULL)
							.getStackSize() < 3
							&& Inventory.getCount(FLASK_PRAYER_RENEWAL_FULL) < 3) {
						log.info("Not enough prayer renewal flasks. Shutting down...");
						Bank.close();
						Game.logout(true);
						stop();
					}
				}
				if (Inventory.getCount(FLASK_RANGING_FULL) == 0) {
					Bank.withdraw(FLASK_RANGING_FULL, 3);
					Time.sleep(80);
					if (Inventory.getCount(FLASK_RANGING_FULL) != 0) {
						log.severe("Range flasks withdrawn");
					} else if (Bank.getItem(FLASK_RANGING_FULL).getStackSize() < 3
							&& Inventory.getCount(FLASK_RANGING_FULL) < 3) {
						log.info("Not enough ranged flasks. Shutting down...");
						Bank.close();
						Game.logout(true);
						stop();
					}
					Bank.close();
				}
			}
			if (Players.getLocal().getHpPercent() <= 70) {
				log.info("HP is low when banking, eating");
				Bank.open();
				Time.sleep(Random.nextInt(500, 700));
				if (Bank.isOpen()) {
					Bank.depositInventory();
					Bank.withdraw(foodUser, 2);
					Bank.close();
				}
				final Item FOOD = Inventory.getItem(foodUser);
				if (FOOD.getWidgetChild().interact("Eat")) {
					Time.sleep(Random.nextInt(900, 1200));
				}
			}
			logBankingCode = 0;
		}

		@Override
		public boolean validate() {
			int antipoisonData = 0;
			for (Item y : Inventory.getItems()) {
				for (int x : ANTIPOISON_ALL) {
					if (y.getId() == x) {
						antipoisonData++;
					}
				}
			}
			return (Inventory.getCount(POT_PRAYER) <= 1 && START_SCRIPT
					&& Game.isLoggedIn() || chinNumber <= 100 && START_SCRIPT
					&& Game.isLoggedIn() || isPoisoned() && antipoisonData == 0
					&& START_SCRIPT && Game.isLoggedIn() || Players.getLocal()
					.getHpPercent() <= 25)
					|| Inventory.getCount(TAB_VARROCK) == 0
					&& START_SCRIPT
					&& Game.isLoggedIn();
		}
	}

	private boolean isPoisoned() {
		return Settings.get(102) != 0;
	}

	private void changeWorlds() {
		log.info("Running changeWorlds code");
		Game.logout(true);
		Time.sleep(Random.nextInt(2000, 5000));
		if (Lobby.isOpen() && Lobby.STATE_LOBBY_IDLE != 0) {
			int randomWorld = WORLDS_MEMBER[Random.nextInt(0,
					WORLDS_MEMBER.length) - 1];
			Context.setLoginWorld(randomWorld);
			Time.sleep(Random.nextInt(200, 400));
			if (Game.isLoggedIn()) {
				Prayer.setQuick();
			}
		} else {
			if (Game.isLoggedIn() && moveNeeded) {
				if (TILE_MOVE_1 != null) {
					Walking.findPath(TILE_MOVE_1).traverse();
					Timer t = new Timer(2500);
					while (Players.getLocal().getSpeed() != 0 && t.isRunning()) {
						Time.sleep(50);
					}
				}
			}
		}
	}

	private void checkRun() {
		if (Walking.getEnergy() > 30) {
			Walking.setRun(true);
		}
	}

	private void checkPrayer() {
		log.info("Running checkPrayer code");
		if (Prayer.getPoints() <= 250) {
			final Item PRAYER_POT_ITEM = Inventory.getItem(POT_PRAYER);
			if (PRAYER_POT_ITEM != null
					&& PRAYER_POT_ITEM.getWidgetChild().interact("Drink")) {
				final int id = PRAYER_POT_ITEM.getId();
				final int count = Inventory.getCount(id);
				final Timer t = new Timer(2500);
				while (t.isRunning() && Inventory.getCount(id) == count) {
					Time.sleep(50);
				}
			} else {
				log.info("Prayer is above 25%, not using potion!");
			}
		}
	}

	private void checkRenewal() {
		log.info("Running checkRenewal code");
		if (t == null || t != null && !t.isRunning()) {
			doDrinkRenewal();
			t = new Timer(300000);
		} else {
			t.reset();
		}
	}

	private void doDrinkRenewal() {
		log.info("Running doDrinkRenewal code");
		final Item PRAYER_RENEWAL_ITEM = Inventory
				.getItem(FLASK_PRAYER_RENEWAL);
		if (PRAYER_RENEWAL_ITEM != null
				&& PRAYER_RENEWAL_ITEM.getWidgetChild().interact("Drink")) {
			final int id = PRAYER_RENEWAL_ITEM.getId();
			final int count = Inventory.getCount(id);
			final Timer t = new Timer(2500);
			while (t.isRunning() && Inventory.getCount(id) == count) {
				Time.sleep(50);
			}
		}
	}

	private void doDrinkAntipoison() {
		log.info("Running doDrinkAntipoison code");
		final Item ANTIPOISON_ALL_ITEM = Inventory.getItem(antipoisonUser);
		if (ANTIPOISON_ALL_ITEM != null
				&& ANTIPOISON_ALL_ITEM.getWidgetChild().interact("Drink")) {
			final int id = ANTIPOISON_ALL_ITEM.getId();
			final int count = Inventory.getCount(id);
			final Timer t = new Timer(2500);
			while (t.isRunning() && Inventory.getCount(id) == count) {
				Time.sleep(50);
			}
		}
	}

	private void doBreakTab() {
		log.info("Running doBreakTab code");
		final Item TAB_ITEM = Inventory.getItem(tab);
		if (TAB_ITEM != null && TAB_ITEM.getWidgetChild().interact("Break")) {
			final int id = TAB_ITEM.getId();
			final int count = Inventory.getCount(id);
			final Timer t = new Timer(2500);
			while (t.isRunning() && Inventory.getCount(id) == count) {
				Time.sleep(50);
			}
		}
	}

	private void doDrinkRangePotion() {
		log.info("Running doDrinkRangePotion code");
		final Item RANGE_POT_ITEM = Inventory.getItem(FLASK_RANGING);
		final int REAL_RANGE = Skills.getRealLevel(Skills.RANGE);
		final int POTTED_RANGE = Skills.getLevel(Skills.RANGE);
		final int RANGE_DIFFERENCE = POTTED_RANGE - REAL_RANGE;
		if (RANGE_POT_ITEM != null && RANGE_DIFFERENCE <= 3
				&& RANGE_POT_ITEM.getWidgetChild().interact("Drink")) {
			final int id = RANGE_POT_ITEM.getId();
			final int count = Inventory.getCount(id);
			final Timer t = new Timer(2500);
			while (t.isRunning() && Inventory.getCount(id) == count) {
				Time.sleep(50);
			}
		} else {
			log.info("We're out of ranging pots, resuming until prayer potions are gone!");
		}
	}

	private void doAttackMonkey(final NPC npc) {
		log.info("Running doAttackMonkey code");
		checkPrayer();
		checkRenewal();
		doDrinkRangePotion();
		if (npc != null && npc.isOnScreen()) {
			if (npc.interact("Attack")) {
				Time.sleep(50);
				if (Players.getLocal().getInteracting() != null) {
					Time.sleep(Random.nextInt(700, 800));
				}
				if (!npc.isOnScreen()) {
					Camera.turnTo(npc);
					if (npc.interact("Attack")) {
						Time.sleep(50);
					}
				}
			}
		}
	}

	private void equipGreegree() {
		Item greegree = Inventory.getItem(GREEGREE_IDS);
		if (greegree != null && greegree.getWidgetChild().click(true)) {
			final int ID = greegree.getId();
			final int COUNT = Inventory.getCount(ID);
			final Timer t = new Timer(2500);
			while (t.isRunning() && Inventory.getCount(ID) == COUNT) {
				Time.sleep(50);
			}
		}
	}

	private static void doPreEat(final Item item, Item item2) {
		if (Players.getLocal().getHpPercent() < 30) {
			Walking.findPath(TILE_GRAND_BANK).traverse();
			while (Calculations.distanceTo(TILE_GRAND_BANK) >= 2) {
				Time.sleep(50);
			}
			Camera.turnTo(TILE_GRAND_BANK);
			Bank.open();
			if (Bank.isOpen()) {
				if (Inventory.isFull()) {
					Bank.deposit(POT_PRAYER_DOSE_4, 3);
				}
				if (Bank.withdraw(item.getId(), 3))
					Time.sleep(200, 400);
				Bank.close();
			}
			if (!Bank.isOpen() && Players.getLocal().getHpPercent() > 75) {
				item.getWidgetChild().interact("Eat");
				Time.sleep(Random.nextInt(300, 400));
			}
			if (Bank.open())
				Time.sleep(200, 400);
			if (Bank.isOpen()) {
				Bank.deposit(item2.getId(), 5);
				if (Inventory.getCount(item.getId()) == 0) {
					Bank.getNearest();
					Bank.open();
					if (Bank.isOpen()) {
						if (Bank.withdraw(item.getId(), 2))
							Time.sleep(200, 300);
						Bank.withdraw(item2.getId(), 3);
					}
				}
			}
		}
	}

	private boolean tileContainsTwoOrMore(final Tile tile) {
		Logger.getLogger("EpicsChins").info(
				"Running tileContainsTwoOrMore code");
		final Player[] PLAYERS_ON_TILE = Players
				.getLoaded(new Filter<Player>() {
					@Override
					public boolean accept(Player t) {
						return t.getLocation().equals(tile);
					}
				});
		if (Game.getClientState() != Game.INDEX_MAP_LOADING
				&& PLAYERS_ON_TILE.length >= 2) {
			return true;
		}
		return false;
	}

	private boolean areaContainsTwoOrMore(final Area area) {
		Logger.getLogger("EpicsChins").info(
				"Running areaContainsTwoOrMore code");
		final Player[] PLAYERS_IN_AREA = Players
				.getLoaded(new Filter<Player>() {
					@Override
					public boolean accept(Player t) {
						return t.getLocation().equals(area);
					}
				});
		if (Game.getClientState() != Game.INDEX_MAP_LOADING
				&& PLAYERS_IN_AREA.length >= 2) {
			return true;
		}
		return false;
	}

	private void antiban() {
		Logger.getLogger("EpicsChins").info("Running antiban code");
		state = Random.nextInt(0, 3);
		switch (state) {
		case 1:
			Logger.getLogger("EpicsChins").info(
					"Setting random camera angle & pitch");
			Camera.setAngle(RANDOM_ANGLE);
			Camera.setPitch(RANDOM_PITCH);
			break;
		case 2:
			Logger.getLogger("EpicsChins").info(
					"Checking constitution leveling progress");
			WidgetChild c = Widgets.get(1213).getChild(12);
			if (c.validate()) {
				c.hover();
				Time.sleep(1000);
			}
			break;
		case 3:
			Logger.getLogger("EpicsChins").info(
					"Checking ranging leveling progress");
			WidgetChild d = Widgets.get(1213).getChild(14);
			if (d.validate()) {
				d.hover();
				Time.sleep(1000);
				break;
			}
		}
	}

	private void yesInterfaceClicker() {
		final WidgetChild YES_INTERFACE = Widgets.get(1188, 3);
		if (YES_INTERFACE.isOnScreen() && YES_INTERFACE.click(true)) {
			final Timer YES_INTERFACE_TIMER = new Timer(2500);
			while (YES_INTERFACE != null && YES_INTERFACE_TIMER.isRunning()) {
				Time.sleep(50);
			}
		}
	}

	public void messageReceived(MessageEvent arg0) {
		String message = arg0.getMessage().toLowerCase();
		String messageSender = arg0.getSender();
		if (messageSender == null) {
			if (message
					.equals("you can't log out until 10 seconds after the end of combat.")) {
				Timer t = new Timer(10000);
				while (t.isRunning()) {
					Time.sleep(5);
				}
				moveNeeded = true;
			}
		}
	}

	private class GUI extends JFrame {
		private static final long serialVersionUID = 3853009753324932631L;

		public GUI() {
			// Title
			setTitle("EC" + version);
			setResizable(false);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			Container contentPane = getContentPane();
			contentPane.setLayout(null);
			// ---- foodLabel ----
			final JLabel FOOD_LABEL = new JLabel("What food should we use?");
			FOOD_LABEL.setBackground(new Color(212, 208, 200));
			FOOD_LABEL.setFont(FOOD_LABEL.getFont().deriveFont(
					FOOD_LABEL.getFont().getStyle() | Font.BOLD));
			contentPane.add(FOOD_LABEL);
			FOOD_LABEL.setBounds(20, 185, 155,
					FOOD_LABEL.getPreferredSize().height);
			// ---- antiLabel ----
			final JLabel ANTI_LABEL = new JLabel(
					"What antipoison should we use?");
			ANTI_LABEL.setBackground(new Color(212, 208, 200));
			ANTI_LABEL.setText("What antipoison should we use?");
			ANTI_LABEL.setFont(ANTI_LABEL.getFont().deriveFont(
					ANTI_LABEL.getFont().getStyle() | Font.BOLD));
			contentPane.add(ANTI_LABEL);
			ANTI_LABEL.setBounds(5, 235, 190, 25);
			// ---- warningLabel ----
			final JLabel WARNING_LABEL = new JLabel("WARNING");
			WARNING_LABEL.setForeground(Color.red);
			WARNING_LABEL.setFont(WARNING_LABEL.getFont().deriveFont(
					WARNING_LABEL.getFont().getStyle() | Font.BOLD));
			contentPane.add(WARNING_LABEL);
			WARNING_LABEL.setBounds(70, 285, 60,
					WARNING_LABEL.getPreferredSize().height);
			// ---- warningLabelB ----
			final JLabel WARNING_LABEL_B = new JLabel(
					"Start in the Grand Exchange!");
			contentPane.add(WARNING_LABEL_B);
			WARNING_LABEL_B.setBounds(new Rectangle(new Point(40, 305),
					WARNING_LABEL_B.getPreferredSize()));
			// chinLabelLeft
			final Image CHIN_PICTURE_LEFT = getImage("http://2c1c.net/images/faceRight.png");
			final JLabel CHIN_LABEL_LEFT = new JLabel(new ImageIcon(
					CHIN_PICTURE_LEFT));
			contentPane.add(CHIN_LABEL_LEFT);
			CHIN_LABEL_LEFT.setBounds(10, 10, 24, 24);
			// chinLabelRight
			final Image CHIN_PICTURE_RIGHT = getImage("http://2c1c.net/images/faceLeft.png");
			if (CHIN_PICTURE_RIGHT == null) {
				Logger.getLogger("EpicsChinsGUI").info("Image failed to load");
			}
			final JLabel CHIN_LABEL_RIGHT = new JLabel(new ImageIcon(
					CHIN_PICTURE_RIGHT));
			if (CHIN_PICTURE_LEFT == null) {
				Logger.getLogger("EpicsChinsGUI").info("Image failed to load");
			}
			contentPane.add(CHIN_LABEL_RIGHT);
			CHIN_LABEL_RIGHT.setBounds(160, 10, 24, 24);
			// ---- greeLabel ----
			final JLabel GREEGREE_LABEL = new JLabel("Are we using a greegree?");
			GREEGREE_LABEL.setFont(GREEGREE_LABEL.getFont().deriveFont(
					GREEGREE_LABEL.getFont().getStyle() | Font.BOLD));
			contentPane.add(GREEGREE_LABEL);
			GREEGREE_LABEL.setBounds(new Rectangle(new Point(25, 140),
					GREEGREE_LABEL.getPreferredSize()));
			// ---- titleLabel ----
			final JLabel TITLE_LABEL = new JLabel("Epics Chinner" + version);
			TITLE_LABEL.setFont(TITLE_LABEL.getFont().deriveFont(
					TITLE_LABEL.getFont().getStyle() | Font.BOLD));
			contentPane.add(TITLE_LABEL);
			TITLE_LABEL.setBounds(45, 10, 110, 25);
			// ---- reqTextPane ----
			final JTextPane REQ_TEXT_PANE = new JTextPane();
			REQ_TEXT_PANE.setBackground(new Color(212, 208, 200));
			REQ_TEXT_PANE.setCursor(Cursor
					.getPredefinedCursor(Cursor.TEXT_CURSOR));
			REQ_TEXT_PANE.setDisabledTextColor(new Color(240, 240, 240));
			REQ_TEXT_PANE.setEditable(false);
			REQ_TEXT_PANE.setText("Requirements:");
			REQ_TEXT_PANE.setFont(REQ_TEXT_PANE.getFont().deriveFont(
					REQ_TEXT_PANE.getFont().getStyle() | Font.BOLD));
			contentPane.add(REQ_TEXT_PANE);
			REQ_TEXT_PANE.setBounds(45, 40, 95, 20);
			// ---- reqTextPaneB ----
			final JTextPane REQ_TEXT_PANE_B = new JTextPane();
			REQ_TEXT_PANE_B.setBackground(new Color(212, 208, 200));
			REQ_TEXT_PANE_B
					.setText("- Access to Ape Atoll\n- 43 Prayer\n- 55 Ranged\n- 3+ Prayer renewal flasks\n- 3+ Ranged flasks");
			REQ_TEXT_PANE_B.setEditable(false);
			contentPane.add(REQ_TEXT_PANE_B);
			REQ_TEXT_PANE_B.setBounds(25, 55, 135, 75);
			// ---- greeBoxYes ----
			final JCheckBox GREEGREE_BOX_YES = new JCheckBox("Yes");
			GREEGREE_BOX_YES.setSelected(true);
			if (GREEGREE_BOX_YES.isSelected()) {
				usingGreegree = true;
			}
			contentPane.add(GREEGREE_BOX_YES);
			GREEGREE_BOX_YES.setBounds(new Rectangle(new Point(45, 160),
					GREEGREE_BOX_YES.getPreferredSize()));
			// ---- greeBoxNo ----
			final JCheckBox GREEGREE_BOX_NO = new JCheckBox("No");
			GREEGREE_BOX_NO.setSelected(false);
			if (GREEGREE_BOX_NO.isSelected()) {
				usingGreegree = false;
			}
			contentPane.add(GREEGREE_BOX_NO);
			GREEGREE_BOX_NO.setBounds(new Rectangle(new Point(100, 160),
					GREEGREE_BOX_NO.getPreferredSize()));
			// ---- foodCombo ----
			final JComboBox<String> FOOD_COMBO_BOX = new JComboBox<>();
			FOOD_COMBO_BOX.setModel(new DefaultComboBoxModel<>(new String[] {
					"Select your food...", "Shark", "Rocktail", "Monkfish",
					"Swordfish", "Lobster", "Tuna", "Trout", "Salmon" }));
			contentPane.add(FOOD_COMBO_BOX);
			FOOD_COMBO_BOX.setBounds(25, 210, 150,
					FOOD_COMBO_BOX.getPreferredSize().height);
			// ---- poisonCombo ----
			final JComboBox<String> POISON_COMBO_BOX = new JComboBox<String>();
			POISON_COMBO_BOX.setModel(new DefaultComboBoxModel<>(new String[] {
					"Select an antipoison...", "Super antipoison flask",
					"Antipoison++ flask", "Antipoison+ flask",
					"Antipoison flask", "Super antipoison", "Antipoison++",
					"Antipoison+", "Antipoison", "Antipoison mix",
					"Antipoison elixir" }));
			contentPane.add(POISON_COMBO_BOX);
			POISON_COMBO_BOX.setBounds(25, 260, 150,
					POISON_COMBO_BOX.getPreferredSize().height);
			{
				Dimension preferredSize = new Dimension();
				for (int i = 0; i < contentPane.getComponentCount(); i++) {
					Rectangle bounds = contentPane.getComponent(i).getBounds();
					preferredSize.width = Math.max(bounds.x + bounds.width,
							preferredSize.width);
					preferredSize.height = Math.max(bounds.y + bounds.height,
							preferredSize.height);
				}
				Insets insets = contentPane.getInsets();
				preferredSize.width += insets.right;
				preferredSize.height += insets.bottom;
				contentPane.setMinimumSize(preferredSize);
				contentPane.setPreferredSize(preferredSize);
			}
			setSize(210, 395);
			setLocationRelativeTo(null);
			// ---- startButton ----
			final JButton START_BUTTON = new JButton("Start");
			contentPane.add(START_BUTTON);
			START_BUTTON.setBounds(5, 330, 185, 25);
			START_BUTTON.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					START_SCRIPT = true;
					final String USER_FOOD = FOOD_COMBO_BOX.getSelectedItem()
							.toString();
					if (USER_FOOD.equals("Select your food...")) {
						Logger.getLogger("EpicsChins").info(
								"No food selected, stopping script");
						stop();
					}
					if (USER_FOOD.equals("Shark")) {
						foodUser = 385;
					}
					if (USER_FOOD.equals("Rocktail")) {
						foodUser = 15272;
					}
					if (USER_FOOD.equals("Monkfish")) {
						foodUser = 7946;
					}
					if (USER_FOOD.equals("Swordfish")) {
						foodUser = 373;
					}
					if (USER_FOOD.equals("Lobster")) {
						foodUser = 379;
					}
					if (USER_FOOD.equals("Tuna")) {
						foodUser = 361;
					}
					if (USER_FOOD.equals("Trout")) {
						foodUser = 333;
					}
					if (USER_FOOD.equals("Salmon")) {
						foodUser = 329;
					}
					final String USER_ANTIPOISON = POISON_COMBO_BOX
							.getSelectedItem().toString();
					if (USER_ANTIPOISON.equals("Select an antipoison...")) {
						Logger.getLogger("EpicsChins").info(
								"No antipoison selected, stopping script");
						Game.logout(false);
						stop();
					}
					if (USER_ANTIPOISON.equals("Super antipoison flask")) {
						antipoisonUser = FLASK_ANTIPOISON_SUPER_FULL;
					}
					if (USER_ANTIPOISON.equals("Antipoison++ flask")) {
						antipoisonUser = FLASK_ANTIPOISON_PLUSPLUS_FULL;
					}
					if (USER_ANTIPOISON.equals("Antipoison+ flask")) {
						antipoisonUser = FLASK_ANTIPOISON_PLUS_FULL;
					}
					if (USER_ANTIPOISON.equals("Antipoison Flask")) {
						antipoisonUser = FLASK_ANTIPOISON_FULL;
					}
					if (USER_ANTIPOISON.equals("Super Antipoison")) {
						antipoisonUser = POT_ANTIPOISON_SUPER_FULL;
					}
					if (USER_ANTIPOISON.equals("Antipoison++")) {
						antipoisonUser = POT_ANTIPOISON_PLUSPLUS_FULL;
					}
					if (USER_ANTIPOISON.equals("Antipoison+")) {
						antipoisonUser = POT_ANTIPOISON_PLUS_FULL;
					}
					if (USER_ANTIPOISON.equals("Antipoison")) {
						antipoisonUser = POT_ANTIPOISON_FULL;
					}
					if (USER_ANTIPOISON.equals("Antipoison mix")) {
						antipoisonUser = MIX_ANTIPOISON_FULL;
					}
					if (USER_ANTIPOISON.equals("Antipoison elixir")) {
						antipoisonUser = ELIXIR_ANTIPOISON;
					}
					gui.dispose();
					log.info("GUI disposed, providing methods");
				}
			});
		}
	}

	private Image getImage(String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
			return null;
		}
	}

	private final Color COLOR = new Color(255, 255, 255);
	private final Font FONT = new Font("Verdana", 0, 10);
	private final Image IMAGE_1 = getImage("http://2c1c.net/images/paint.png");

	public void onRepaint(Graphics g1) {
		mouseX = Mouse.getX();
		mouseY = Mouse.getY();
		rangeGainedExp = Skills.getExperience(Skills.RANGE) - rangedStartExp;
		hpGainedExp = Skills.getExperience(Skills.CONSTITUTION) - hpStartExp;
		expHr = (int) ((rangeGainedExp) * 3600000D / (System
				.currentTimeMillis() - startTime));
		if (SHOWPAINT) {
			Graphics2D g = (Graphics2D) g1;
			g.drawImage(IMAGE_1, -4, 336, null);
			g.setFont(FONT);
			g.setColor(COLOR);
			g.drawString(RUNTIME.toElapsedString(), 215, 444);
			g.drawString(String.valueOf(rangeGainedExp), 198, 460);
			g.drawString(String.valueOf(hpGainedExp), 181, 480);
			g.drawLine(mouseX, mouseY - 10, mouseX, mouseY + 10);
			g.drawLine(mouseX - 10, mouseY, mouseX + 10, mouseY);
			g.drawString(String.valueOf(expHr), 183, 497);
			g.drawString(String.valueOf(chinsThrown), 351, 443);
			g.drawString(String.valueOf(zombieKillCount), 359, 461);
		} else {
			Graphics2D g = (Graphics2D) g1;
			g.drawRect(502, 389, 15, 15);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (new Rectangle(502, 389, 14, 15).contains(e.getPoint())) {
			if (SHOWPAINT) {
				SHOWPAINT = false;
			} else if (IMAGE_1 == null) {
				Logger.getLogger("EpicsChinsGUI").info("Image failed to load");
			}
			SHOWPAINT = true;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}