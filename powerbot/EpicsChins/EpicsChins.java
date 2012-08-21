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

import org.powerbot.concurrent.Task;
import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.ActiveScript;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Settings;
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
import org.powerbot.game.api.wrappers.Locatable;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.interactive.Player;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import org.powerbot.game.bot.Context;
import org.powerbot.game.bot.event.listener.PaintListener;

@Manifest(authors = { "Epics" }, name = "Epics Chinner", description = "Kills chins and banks when necessary.", version = 1.0)
public class EpicsChins extends ActiveScript implements PaintListener,
		MouseListener {

	// GUI
	private boolean guiwait = true;
	private GUI gui;

	// GUI variables
	private int Food = 0; // user selected food
	private int[] Antipoison = { 0 }; // user selected Antipoison
	private boolean usingGreegree;
	private int loop = 0;
	private boolean stratsProvided = true;

	// Paint variables
	private long startTime;
	private int zombieKillCount;
	private final Timer runtime = new Timer(0);
	private int RANGEstartExp;
	private int HPstartExp;
	private int rangegainedExp;
	private int hpgainedExp;
	private int expHour;
	private int chinsThrown;
	private final int chinThrowID = 2779;

	private int mouseX = 0;
	private int mouseY = 0;

	private boolean showpaint = false;

	// Members Worlds array
	private final int[] membersWorlds = { 5, 6, 9, 12, 15, 18, 21, 22, 23, 24,
			25, 26, 27, 28, 31, 32, 36, 39, 40, 42, 44, 45, 46, 48, 49, 51, 52,
			53, 54, 56, 58, 59, 60, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72,
			73, 74, 76, 77, 78, 79, 82, 83, 84, 85, 86, 87, 88, 89, 91, 92, 96,
			97, 99, 100, 103, 104, 105, 114, 115, 116, 117, 119, 123, 124, 137,
			138, 139 };

	// Path details
	public final Area grandExchange = new Area(new Tile(3192, 3512, 0),
			new Tile(3142, 3471, 0));
	private final Area waydarZone = new Area(new Tile(2642, 4525, 0), new Tile(
			2652, 4515, 0));
	private final Area lumdoZone = new Area(new Tile(2896, 2730, 0), new Tile(
			2887, 2717, 0));
	private final Area treeDoorInsideTile = new Area(new Tile(2896, 2730, 0),
			new Tile(2887, 2717, 0));
	private final Tile spiritMidTile = new Tile(2542, 3169, 0);
	private final Tile spiritEndTile = new Tile(2462, 3444, 0);
	private final Tile apeStart = new Tile(2802, 2707, 0);
	private final Tile gnomeLadderMid = new Tile(2466, 3994, 1);
	private final Tile apeLadderTop = new Tile(2764, 2703, 0);
	private final Tile apeLadderBottom = new Tile(2764, 9103, 0);
	private final Tile chinTile1 = new Tile(2715, 9127, 0);
	private final Tile chinTile2 = new Tile(2746, 9122, 0);
	private final Tile chinTile3 = new Tile(2709, 9116, 0);
	private final Tile chinTile4 = new Tile(2701, 9111, 0);
	private final Area chinArea3to4 = new Area(new Tile(2709, 9116, 0),
			new Tile(2701, 9111, 0));
	private final Tile[] chinArray = { chinTile1, chinTile2, chinTile3,
			chinTile4 };

	// Greegree IDs
	private final static int monkey_greegree = 4031;
	private final static int[] greegree = { monkey_greegree, 4024, 4025, 40256,
			4027, 4028, 4029, 4030 };

	// Potion IDs
	private final static int[] ranging_flask = { 23303, 23305, 23307, 23309,
			23311, 23313 };
	private final static int[] prayer_pot = { 2434, 139, 141, 143 };
	private final static int prayer_pot_four_dose = 2434;
	private final static int[] prayer_renewal_flask = { 23609, 23611, 23613,
			23615, 23617, 23619 };
	private final static int[] antipoison = { 23315, 23317, 23319, 23321,
			23323, 23325, // antipoison FLASK
			23579, 23581, 23583, 23585, 23587, 23589, // antipoison FLASK PLUS
			23591, 23593, 23595, 23597, 23599, 23601, // antipoison FLASK PLUS
														// PLUS
			23327, 23329, 23331, 23333, 23335, 23337, // antipoison FLASK SUPER
			11433, 11435, // antipoison MIX
			2446, 175, 177, 179, // antipoison POT
			5943, 5945, 5947, 5949, // antipoison POT PLUS
			5952, 5954, 5956, 5958, // antipoison POT PLUS PLUS
			2448, 181, 183, 185, // antipoison POT SUPER
			20879 // antipoison elixir
	};

	private int[] antiPoisonSuperFlask = { 23327, 23329, 23331, 23333, 23335,
			23337 };
	private int[] antiPoisonPlusPlusFlask = { 23591, 23593, 23595, 23597,
			23599, 23601 };
	private int[] antiPoisonPlusFlask = { 23579, 23581, 23583, 23585, 23587,
			23589 };
	private int[] antiPoisonFlask = { 23315, 23317, 23319, 23321, 23323, 23325 };
	private int[] antiPoisonMix = { 11433, 11435 };
	private int[] antiPoisonSuperPot = { 2448, 181, 183, 185 };
	private int[] antiPoisonPlusPlusPot = { 5952, 5954, 5956, 5958 };
	private int[] antiPoisonPlusPot = { 5943, 5945, 5947, 5949 };
	private int[] antiPoisonPot = { 2446, 175, 177, 179 };
	private int[] antiPoisonElixir = { 20879 };

	// Tab IDs
	private final static int varrock_tab = 8007;
	private final static int lumbridge_tab = 8008;
	private final static int falador_tab = 8009;
	private final static int camelot_tab = 8010;
	private final static int ardougne_tab = 8011;
	private final static int watchtower_tab = 8012;
	private final static int house_tab = 8013;
	private final static int[] tab = { varrock_tab, falador_tab, lumbridge_tab,
			camelot_tab, ardougne_tab, watchtower_tab, house_tab };

	// General IDs
	private final static int chin = 10034;
	Timer t = null;

	// Interaction IDs
	private final static int treeAnimation = 7082; // Tree animation when being
													// teleported
	private final static int prayAnimation = 645;
	private final static int[] treeDoorId = { 69197, 69198 }; // Open Tree
																// Door
	private final static int spiritTreeGeId = 1317; // Teleport Spirit tree
	private final static int spiritTreeMainId = 68974;
	private final static int gnomeLadderId = 69499; // Climb-up Ladder
	private final static int apeLadderId = 4780; // Climb-down Ladder
	private final static int varrockAltarId = 24343;
	private final static int zombieDeathAnim = 1384;

	// NPC IDs
	private final static int daero_id = 824; // first NPC "Travel" and reply Yes
	private final static int waydar_id = 1407; // second NPC "Travel" and reply
												// Yes
	private final static int lumdo_id = 1408; // third NPC "Travel" and reply
												// Yes
	private final static int monkey_zombie_id = 1465;

	@Override
	protected void setup() {

		provide(new Strats());

		RANGEstartExp = Skills.getExperience(Skills.RANGE);
		HPstartExp = Skills.getExperience(Skills.CONSTITUTION);
		startTime = System.currentTimeMillis();

	}

	private class Strats extends Strategy implements Task {
		@Override
		public void run() {
			if (Game.isLoggedIn()) {
				log.info("Starting script");
				provide(new runToChins());
				provide(new throwChins());
				provide(new Banking());
				stratsProvided = false;

				try {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							gui = new GUI();
							gui.setVisible(true);
						}
					});
				} catch (Exception e) {
				}
				showpaint = true;

			} else {
				loop++;
				if (loop == 1) {
					log.info("Waiting for RunescapeŽ to log in and load your character..");
				}
				Time.sleep(2000, 2500);
			}
		}

		@Override
		public boolean validate() {
			return stratsProvided;
		}
	}

	private class runToChins extends Strategy implements Task {
		@Override
		public void run() {

			// Declaring NPCs
			NPC daero = NPCs.getNearest(daero_id);
			NPC waydar = NPCs.getNearest(waydar_id);
			NPC lumdo = NPCs.getNearest(lumdo_id);

			// Declaring objects being interacted with
			SceneObject treeDoor = SceneEntities.getNearest(treeDoorId);
			SceneObject gnomeLadder = SceneEntities.getNearest(gnomeLadderId);
			SceneObject spiritTreeGe = SceneEntities.getNearest(spiritTreeGeId);
			SceneObject spiritTreeMain = SceneEntities
					.getNearest(spiritTreeMainId);
			SceneObject apeLadder = SceneEntities.getNearest(apeLadderId);
			SceneObject varrockAltar = SceneEntities.getNearest(varrockAltarId);

			// Declaring variables

			Item food = Inventory.getItem(Food);
			Item prayerPot = Inventory.getItem(prayer_pot_four_dose);

			if (grandExchange.contains(Players.getLocal().getLocation())) {
				checkRun();
				doPreEat(food, prayerPot);
				doChargePrayer(varrockAltar);
				Walking.findPath(spiritTreeGe).traverse();
				if (spiritTreeGe.isOnScreen() && spiritTreeGe != null) {
					spiritTreeGe.interact("Teleport");
					if (Players.getLocal().getAnimation() == treeAnimation) {
						Time.sleep(50, 400);
					}
				}
			} else if (!grandExchange
					.contains(Players.getLocal().getLocation())) {
				log.info("You aren't in the Grand Exchange! Shutting down...");
				Game.logout(false);
				stop();
			}
			if (spiritMidTile.equals(Players.getLocal())
					&& spiritTreeMain.isOnScreen() && spiritTreeMain != null) {
				Camera.turnTo(spiritTreeMain);
				spiritTreeMain.interact("Teleport");
				if (Players.getLocal().getAnimation() == treeAnimation) {
					Time.sleep(50, 400);
				} else {
					log.info("Tree animation is not present. Something has gone turribly wrong!");
				}
				Time.sleep(50, 400);
				WidgetChild spiritTreeInterface = Widgets.get(6, 0);
				if (spiritTreeInterface.validate()) {
					spiritTreeInterface.click(true);
					Time.sleep(Random.nextInt(100, 200));
				}
			}
			if (spiritEndTile.equals(Players.getLocal())) {
				Walking.findPath(treeDoor).traverse();
				if (treeDoor.isOnScreen() && treeDoor != null) {
					treeDoor.interact("Open");
					Time.sleep(100, 250);
				}
			}
			if (treeDoorInsideTile.contains(Players.getLocal().getLocation())) {
				Walking.findPath(gnomeLadder).traverse();
				if (gnomeLadder != null
						&& Players.getLocal().getAnimation() == -1) {
					Camera.turnTo(gnomeLadder);
					gnomeLadder.interact("Climb-up");
					Time.sleep(Random.nextInt(100, 250));
				}
			}
			if (gnomeLadderMid.equals(Players.getLocal())) {
				Walking.findPath(daero).traverse();
				if (daero.isOnScreen() && daero.getAnimation() == -1) {
					Camera.turnTo(daero);
					daero.interact("Travel");
					WidgetChild yesInterface = Widgets.get(1188, 3);
					if (yesInterface.validate()) {
						yesInterface.click(true);
						Time.sleep(Random.nextInt(100, 125));
					}
				}
			}
			if (waydarZone.contains(Players.getLocal().getLocation())) {
				if (waydar.isOnScreen() && waydar.getAnimation() == -1) {
					Camera.turnTo(waydar);
					waydar.interact("Travel");
					WidgetChild yesInterface = Widgets.get(1188, 3);
					if (yesInterface.validate()) {
						yesInterface.click(true);
						Time.sleep(100, 200);
					}
				} else if (waydar.getAnimation() != -1 && waydar.isOnScreen()) {
					Time.sleep(Random.nextInt(100, 125));
				}
			}
			if (lumdoZone.contains(Players.getLocal().getLocation())) {
				if (lumdo.isOnScreen() && lumdo.getAnimation() == -1) {
					lumdo.interact("Travel");
					WidgetChild yesInterface = Widgets.get(1188, 3);
					if (yesInterface.validate()) {
						yesInterface.click(true);
						Time.sleep(Random.nextInt(100, 125));
					} else if (waydar.getAnimation() != -1
							&& waydar.isOnScreen()) {
						Time.sleep(Random.nextInt(100, 125));
					}
				}
			}
			if (apeStart.equals(Players.getLocal().getLocation())
					&& usingGreegree) {
				for (final Item item1 : Inventory.getItems()) {
					for (int id : greegree) {
						if (item1 != null && item1.getId() == id
								&& item1.getWidgetChild().interact("Equip")) {
							Time.sleep(Random.nextInt(50, 75));
						}
					}
				}
				Walking.findPath(apeLadder).traverse();
				Time.sleep(Random.nextInt(50, 125));
				if (apeLadder.isOnScreen()
						&& apeLadderTop.equals(Players.getLocal())
						&& apeLadder != null) {
					apeLadder.interact("Climb-down");
					Time.sleep(300, 425);

				}
			} else if (apeStart.equals(Players.getLocal()) && !usingGreegree) {
				checkRun();
			}
			if (apeLadderBottom.equals(Players.getLocal())) {
				checkRenewal();
				Prayer.setQuick();
				Walking.walk(chinTile1);
				if (tileContainsTwoOrMore(chinTile1)) {
					Walking.walk(chinTile2);
					if (tileContainsTwoOrMore(chinTile2)) {
						Walking.walk(chinTile3);
						if (areaContainsTwoOrMore(chinArea3to4)) {
							changeWorlds();
						}
					}
				}
			}
		}

		@Override
		public boolean validate() {
			return grandExchange.contains(Players.getLocal().getLocation())
					&& !isPoisoned() && Inventory.getCount(Food) >= 1
					&& Inventory.getCount(prayer_renewal_flask) == 3
					&& Inventory.getCount(prayer_pot) == 18
					&& Inventory.getCount(ranging_flask) == 3
					&& Inventory.getCount(antipoison) == 1
					&& Inventory.getCount(tab) > 0 && !isPoisoned()
					&& Equipment.getCount(chin) >= 500 && !guiwait
					&& !chinArray.equals(Players.getLocal());
		}
	}

	private class throwChins extends Strategy implements Task {

		private NPC monkey_zombie;

		@Override
		public void run() {

			doAttackMonkey(monkey_zombie);
			Time.sleep(Random.nextInt(50, 75));
			if (!Prayer.isQuickOn()) {
				Prayer.setQuick();
			}
			if (isPoisoned()) {
				doDrinkAntipoison();
			} else {
				log.info("We're out of antipoison & we're poisoned! Teleporting to safety and shutting down...");
				doBreakTab();
				Game.logout(false);
				stop();
			}
			if (Players.getLocal().getAnimation() == chinThrowID) {
				chinsThrown++;
				Time.sleep(Random.nextInt(20, 50));
			}
			if (monkey_zombie.getAnimation() == zombieDeathAnim
					&& monkey_zombie.validate()) {
				zombieKillCount++;
			}
			final int vialid = 229;
			Item vial = Inventory.getItem(vialid);
			if (Inventory.getItem() == vial) {
				vial.getWidgetChild().interact("Drop");
			}
		}

		@Override
		public boolean validate() {
			int chinCount = Equipment.getCount(chin);
			return chinArray.equals(Players.getLocal().getLocation())
					&& chinCount >= 200
					&& Inventory.getCount(prayer_pot) >= 1
					&& (monkey_zombie = NPCs.getNearest(monkey_zombie_id)) != null
					&& !guiwait;

		}
	}

	private class Banking extends Strategy implements Task {

		Item antipoisonItem = Bank.getItem(new Filter<Item>() {
			@Override
			public boolean accept(final Item l) {
				for (int id : Antipoison) {
					if (l.getId() == id)
						return true;
				}
				return false;
			}
		});

		Item greegreeItem = Bank.getItem(new Filter<Item>() {
			@Override
			public boolean accept(final Item m) {
				for (int id : greegree) {
					if (m.getId() == id)
						return true;
				}
				return false;
			}
		});

		Item rangeFlaskItem = Bank.getItem(new Filter<Item>() {
			@Override
			public boolean accept(final Item n) {
				for (int id : ranging_flask) {
					if (n.getId() == id)
						return true;
				}
				return false;
			}
		});

		Item tabItem = Bank.getItem(new Filter<Item>() {
			@Override
			public boolean accept(final Item o) {
				for (int id : tab) {
					if (o.getId() == id)
						return true;
				}
				return false;
			}
		});

		Item prayer_renewal_flaskItem = Bank.getItem(new Filter<Item>() {
			@Override
			public boolean accept(final Item p) {
				for (int id : tab) {
					if (p.getId() == id)
						return true;
				}
				return false;
			}
		});

		@Override
		public void run() {
			for (final Item tabItem : Inventory.getItems()) {
				for (int tabID : tab) {
					if (tabItem.getId() == tabID
							&& tabItem.getWidgetChild().interact("Break")) {
						Time.sleep(Random.nextInt(50, 100));
					}
				}
			}
			checkRun();
			Walking.findPath((Locatable) Bank.getNearest());
			Camera.turnTo((Locatable) Bank.getNearest());
			if (Players.getLocal().getHpPercent() <= 70) {
				Bank.open();
				if (Bank.isOpen()) {
					Bank.depositInventory();
					Bank.withdraw(Food, 2);
					Bank.close();
				}
				Item food = Inventory.getItem(Food);
				food.getWidgetChild().interact("Eat");
				Time.sleep(Random.nextInt(900, 1200));
			}
			Bank.open();
			if (Bank.isOpen()) {
				if (usingGreegree) {
					Bank.withdraw(greegreeItem.getId(), 1);
					Bank.close();

					Bank.depositInventory();
					Bank.withdraw(Food, 1);
					Bank.withdraw(greegreeItem.getId(), 1);// how else could it
															// be..?
					Bank.withdraw(prayer_pot_four_dose, 18);
					Bank.withdraw(antipoisonItem.getId(), 1);
					Bank.withdraw(tabItem.getId(), 1);
					Bank.withdraw(prayer_renewal_flaskItem.getId(), 3);
					Bank.withdraw(rangeFlaskItem.getId(), 3);
					Bank.close();
				} else if (!usingGreegree) {
					Bank.depositInventory();
					Bank.withdraw(Food, 1);
					Bank.withdraw(prayer_pot_four_dose, 18);
					Bank.withdraw(antipoisonItem.getId(), 1);
					Bank.withdraw(tabItem.getId(), 1);// nvm LOL
					Bank.withdraw(prayer_renewal_flaskItem.getId(), 3);
					Bank.withdraw(rangeFlaskItem.getId(), 3);

				}
			}
		}

		@Override
		public boolean validate() {
			return Inventory.getCount(prayer_pot) <= 1
					|| Equipment.getCount(chin) <= 100 || isPoisoned()
					&& Antipoison == null && !guiwait
					|| Players.getLocal().getHpPercent() <= 25 && !guiwait;

		}
	}

	private boolean isPoisoned() {
		return Settings.get(102) != 0;
	}

	private void changeWorlds() {
		Game.logout(true);
		Time.sleep(Random.nextInt(2000, 5000));
		if (Lobby.isOpen() && Lobby.STATE_LOBBY_IDLE != 0) {
			int randomWorld = membersWorlds[Random.nextInt(0,
					membersWorlds.length)];
			Context.setLoginWorld(randomWorld);
			Time.sleep(Random.nextInt(200, 400));
			if (Game.isLoggedIn()) {
				Prayer.setQuick();
			}
		}
	}

	private void checkRun() {
		if (Walking.getEnergy() > 30) {
			Walking.setRun(true);
		}
	}

	private void checkPrayer() {
		if (Prayer.getPoints() <= 250) {
			final Item prayerPot = Inventory.getItem(prayer_pot);
			if (prayerPot != null
					&& prayerPot.getWidgetChild().interact("Drink")) {
				final int id = prayerPot.getId();
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
		if (t == null || t != null && !t.isRunning()) {
			doDrinkRenewal();
			t = new Timer(300000);
		} else {
			t.reset();
		}
	}

	private void doDrinkRenewal() {
		final Item prayerRenewal = Inventory.getItem(prayer_renewal_flask);
		if (prayer_renewal_flask != null
				&& prayerRenewal.getWidgetChild().interact("Drink")) {
			final int id = prayerRenewal.getId();
			final int count = Inventory.getCount(id);
			final Timer t = new Timer(2500);
			while (t.isRunning() && Inventory.getCount(id) == count) {
				Time.sleep(50);
			}
		}
	}

	private void doDrinkAntipoison() {
		final Item antipoison = Inventory.getItem(Antipoison);
		if (antipoison != null && antipoison.getWidgetChild().interact("Drink")) {
			final int id = antipoison.getId();
			final int count = Inventory.getCount(id);
			final Timer t = new Timer(2500);
			while (t.isRunning() && Inventory.getCount(id) == count) {
				Time.sleep(50);
			}
		}
	}

	private void doBreakTab() {
		final Item tabItem = Inventory.getItem(tab);
		if (tabItem != null && tabItem.getWidgetChild().interact("Break")) {
			final int id = tabItem.getId();
			final int count = Inventory.getCount(id);
			final Timer t = new Timer(2500);
			while (t.isRunning() && Inventory.getCount(id) == count) {
				Time.sleep(50);
			}
		}
	}

	private void doDrinkRangePotion() {
		final Item rangePotItem = Inventory.getItem(ranging_flask);
		int realRange = Skills.getRealLevel(Skills.RANGE);
		int potRange = Skills.getLevel(Skills.RANGE);
		int rangeDifference = potRange - realRange;
		if (rangeDifference >= 3 && rangePotItem != null
				&& rangePotItem.getWidgetChild().interact("Drink")) {
			final int id = rangePotItem.getId();
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
		checkPrayer();
		checkRenewal();
		doDrinkRangePotion();
		if (npc.isOnScreen() && npc.validate() && npc != null) {
			npc.interact("Attack");
			if (!Players.getLocal().isInCombat()
					&& Players.getLocal().getInteracting() == null) {
				Time.sleep(Random.nextInt(700, 800));
			}
			if (!npc.isOnScreen() && npc.validate()) {
				Camera.turnTo(npc);
				npc.interact("Attack");
			}
		}
	}

	private static void doChargePrayer(final SceneObject sceneobject) {
		if (Prayer.getPoints() > 300) {
			Logger.getLogger("EpicsChins")
					.info("Prayer is lower than 80%, let's go charge up before we head out.");
			Walking.findPath(sceneobject).traverse();
			if (sceneobject.isOnScreen() && sceneobject != null) {
				Camera.turnTo(sceneobject);
				Time.sleep(Random.nextInt(20, 50));
				sceneobject.click(true);
				if (Players.getLocal().getAnimation() == prayAnimation) {
					Time.sleep(100, 400);
				}
				if (Players.getLocal().getPrayerIcon() == 100) {
					Logger.getLogger("EpicsChins").info(
							"All charged up, let's get going.");
				}
			} else {
				Logger.getLogger("EpicsChins")
						.info("Can't find the altar, we'll proceed without charging up I suppose...");
			}
		}
	}

	private static void doPreEat(final Item item, Item item2) {
		if (Players.getLocal().getHpPercent() > 30) {
			Walking.findPath((Locatable) Bank.getNearest());
			Bank.open();
			if (Bank.isOpen()) {
				if (Inventory.isFull()) {
					Bank.deposit(prayer_pot_four_dose, 3);
				}
				Bank.withdraw(item.getId(), 3);
				Time.sleep(200, 400);
				Bank.close();
			}
			if (!Bank.isOpen() && Players.getLocal().getHpPercent() > 75) {
				item.getWidgetChild().interact("Eat");
				Time.sleep(Random.nextInt(300, 400));
			}
			Bank.open();
			Time.sleep(200, 400);
			if (Bank.isOpen()) {
				Bank.deposit(item2.getId(), 5);
				if (Inventory.getCount(item.getId()) == 0) {
					Bank.getNearest();
					Bank.open();
					if (Bank.isOpen()) {
						Bank.withdraw(item.getId(), 2);
						Time.sleep(200, 300);
						Bank.withdraw(item2.getId(), 3);
					}
				}
			}
		}
	}

	private boolean tileContainsTwoOrMore(final Tile tile) {// lol idk, getting
															// tired now :Psorry
		Player[] playersOnTile = Players.getLoaded(new Filter<Player>() {

			@Override
			public boolean accept(Player t) {
				return t.getLocation().equals(tile);
			}
		});
		if (playersOnTile.length >= 2) {
			return true;
		}
		return false;
	}

	private boolean areaContainsTwoOrMore(final Area area) {
		Player[] playersInArea = Players.getLoaded(new Filter<Player>() {

			@Override
			public boolean accept(Player t) {
				return t.getLocation().equals(area);
			}
		});
		if (playersInArea.length >= 2) {
			return true;
		}
		return false;
	}

	private class GUI extends JFrame {
		private static final long serialVersionUID = 3853009753324932631L;

		public GUI() {
			String version = " v1.0";

			// Title
			setTitle("EC" + version);
			setResizable(false);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			Container contentPane = getContentPane();
			contentPane.setLayout(null);

			// ---- foodLabel ----
			final JLabel foodLabel = new JLabel("What food should we use?");
			foodLabel.setBackground(new Color(212, 208, 200));
			foodLabel.setFont(foodLabel.getFont().deriveFont(
					foodLabel.getFont().getStyle() | Font.BOLD));
			contentPane.add(foodLabel);
			foodLabel.setBounds(20, 185, 155,
					foodLabel.getPreferredSize().height);

			// ---- antiLabel ----
			final JTextPane antiLabel = new JTextPane();
			antiLabel.setBackground(new Color(212, 208, 200));
			antiLabel.setText("What antipoison should we use?");
			antiLabel.setFont(antiLabel.getFont().deriveFont(
					antiLabel.getFont().getStyle() | Font.BOLD));
			antiLabel.setEditable(false);
			contentPane.add(antiLabel);
			antiLabel.setBounds(5, 235, 190, 25);

			// ---- warningLabel ----
			final JLabel warningLabel = new JLabel("WARNING");
			warningLabel.setForeground(Color.red);
			warningLabel.setFont(warningLabel.getFont().deriveFont(
					warningLabel.getFont().getStyle() | Font.BOLD));
			contentPane.add(warningLabel);
			warningLabel.setBounds(70, 285, 60,
					warningLabel.getPreferredSize().height);

			// ---- warningLabelB ----
			final JLabel warningLabelB = new JLabel(
					"Start in the Grand Exchange!");
			contentPane.add(warningLabelB);
			warningLabelB.setBounds(new Rectangle(new Point(40, 305),
					warningLabelB.getPreferredSize()));

			// chinLabelLeft
			final Image chinPictureLeft = getImage("http://2c1c.net/images/faceRight.png");
			final JLabel chinLabelLeft = new JLabel(new ImageIcon(
					chinPictureLeft));
			contentPane.add(chinLabelLeft);
			chinLabelLeft.setBounds(10, 10, 24, 24);

			// chinLabelRight
			final Image chinPictureRight = getImage("http://2c1c.net/images/faceLeft.png");
			if (chinPictureRight == null) {
				Logger.getLogger("EpicsChinsGUI").info("Image failed to load");
			}
			final JLabel chinLabelRight = new JLabel(new ImageIcon(
					chinPictureRight));
			if (chinPictureLeft == null) {
				Logger.getLogger("EpicsChinsGUI").info("Image failed to load");
			}
			contentPane.add(chinLabelRight);
			chinLabelRight.setBounds(160, 10, 24, 24);

			// ---- greeLabel ----
			final JLabel greeLabel = new JLabel("Are we using a greegree?");
			greeLabel.setFont(greeLabel.getFont().deriveFont(
					greeLabel.getFont().getStyle() | Font.BOLD));
			contentPane.add(greeLabel);
			greeLabel.setBounds(new Rectangle(new Point(25, 140), greeLabel
					.getPreferredSize()));

			// ---- titleLabel ----
			final JLabel titleLabel = new JLabel("Epics Chinner" + version);
			titleLabel.setFont(titleLabel.getFont().deriveFont(
					titleLabel.getFont().getStyle() | Font.BOLD));
			contentPane.add(titleLabel);
			titleLabel.setBounds(45, 10, 110, 25);

			// ---- reqTextPane ----
			final JTextPane reqTextPane = new JTextPane();
			reqTextPane.setBackground(new Color(212, 208, 200));
			reqTextPane.setCursor(Cursor
					.getPredefinedCursor(Cursor.TEXT_CURSOR));
			reqTextPane.setDisabledTextColor(new Color(240, 240, 240));
			reqTextPane.setEditable(false);
			reqTextPane.setText("Requirements:");
			reqTextPane.setFont(reqTextPane.getFont().deriveFont(
					reqTextPane.getFont().getStyle() | Font.BOLD));
			contentPane.add(reqTextPane);
			reqTextPane.setBounds(45, 40, 95, 20);

			// ---- reqTextPaneB ----
			final JTextPane reqTextPaneB = new JTextPane();
			reqTextPaneB.setBackground(new Color(212, 208, 200));
			reqTextPaneB
					.setText("- Access to Ape Atoll\n- 43 Prayer\n- 55 Ranged\n- 3+ Prayer renewal flasks\n- 3+ Ranged flasks");
			reqTextPaneB.setEditable(false);
			contentPane.add(reqTextPaneB);
			reqTextPaneB.setBounds(25, 55, 135, 75);

			// ---- greeBoxYes ----
			final JCheckBox greeBoxYes = new JCheckBox("Yes");
			greeBoxYes.setSelected(true);
			if (greeBoxYes.isSelected()) {
				usingGreegree = true;
			}
			contentPane.add(greeBoxYes);
			greeBoxYes.setBounds(new Rectangle(new Point(45, 160), greeBoxYes
					.getPreferredSize()));

			// ---- greeBoxNo ----
			final JCheckBox greeBoxNo = new JCheckBox("No");
			greeBoxNo.setSelected(false);
			if (greeBoxNo.isSelected()) {
				usingGreegree = false;
			}
			contentPane.add(greeBoxNo);
			greeBoxNo.setBounds(new Rectangle(new Point(100, 160), greeBoxNo
					.getPreferredSize()));

			// ---- foodCombo ----
			final JComboBox<String> foodCombo = new JComboBox<>();
			foodCombo.setModel(new DefaultComboBoxModel<>(new String[] {
					"Select your food...", "Shark", "Rocktail", "Monkfish",
					"Swordfish", "Lobster", "Tuna", "Trout", "Salmon" }));
			contentPane.add(foodCombo);
			foodCombo.setBounds(25, 210, 150,
					foodCombo.getPreferredSize().height);

			// ---- poisonCombo ----
			final JComboBox<String> poisonCombo = new JComboBox<String>();
			poisonCombo.setModel(new DefaultComboBoxModel<>(new String[] {
					"Select an antipoison...", "Super antipoison flask",
					"Antipoison++ flask", "Antipoison+ flask",
					"Antipoison flask", "Super antipoison", "Antipoison++",
					"Antipoison+", "Antipoison", "Antipoison mix",
					"Antipoison elixir" }));
			contentPane.add(poisonCombo);
			poisonCombo.setBounds(25, 260, 150,
					poisonCombo.getPreferredSize().height);

			{ // compute preferred size
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
			final JButton startButton = new JButton("Start");
			contentPane.add(startButton);
			startButton.setBounds(5, 330, 185, 25);
			startButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String chosenFood = foodCombo.getSelectedItem().toString();
					if (chosenFood.equals("Select your food...")) {
						Logger.getLogger("EpicsChins").info(
								"No food selected, stopping script");
						stop();
					}
					if (chosenFood.equals("Shark")) {
						Food = 385;
					}
					if (chosenFood.equals("Rocktail")) {
						Food = 15272;
					}
					if (chosenFood.equals("Monkfish")) {
						Food = 7946;
					}
					if (chosenFood.equals("Swordfish")) {
						Food = 373;
					}
					if (chosenFood.equals("Lobster")) {
						Food = 379;
					}
					if (chosenFood.equals("Tuna")) {
						Food = 361;
					}
					if (chosenFood.equals("Trout")) {
						Food = 333;
					}
					if (chosenFood.equals("Salmon")) {
						Food = 329;
					}
					String chosenAntipoison = foodCombo.getSelectedItem()
							.toString();
					if (chosenAntipoison.equals("Select an antipoison...")) {
						Logger.getLogger("EpicsChins").info(
								"No antipoison selected, stopping script");
						Game.logout(false);
						stop();
					}
					if (chosenAntipoison.equals("Super antipoison flask")) {
						Antipoison = antiPoisonSuperFlask;
					}
					if (chosenAntipoison.equals("Antipoison++ flask")) {
						Antipoison = antiPoisonPlusPlusFlask;
					}
					if (chosenAntipoison.equals("Antipoison+ flask")) {
						Antipoison = antiPoisonPlusFlask;
					}
					if (chosenAntipoison.equals("Antipoison Flask")) {
						Antipoison = antiPoisonFlask;
					}
					if (chosenAntipoison.equals("Super antipoison")) {
						Antipoison = antiPoisonSuperPot;
					}
					if (chosenAntipoison.equals("Antipoison++")) {
						Antipoison = antiPoisonPlusPlusPot;
					}
					if (chosenAntipoison.equals("Antipoison+")) {
						Antipoison = antiPoisonPlusPot;
					}
					if (chosenAntipoison.equals("Antipoison")) {
						Antipoison = antiPoisonPot;
					}
					if (chosenAntipoison.equals("Antipoison mix")) {
						Antipoison = antiPoisonMix;
					}
					if (chosenAntipoison.equals("Antipoison elixir")) {
						Antipoison = antiPoisonElixir;
					}
					gui.dispose();
					guiwait = false;
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

	private final Color color1 = new Color(255, 255, 255);

	private final Font font1 = new Font("Verdana", 0, 10);

	private final Image img1 = getImage("http://2c1c.net/images/paint.png");

	public void onRepaint(Graphics g1) {
		mouseX = Mouse.getX();
		mouseY = Mouse.getY();

		rangegainedExp = Skills.getExperience(Skills.RANGE) - RANGEstartExp;
		hpgainedExp = Skills.getExperience(Skills.CONSTITUTION) - HPstartExp;
		expHour = (int) ((rangegainedExp) * 3600000D / (System
				.currentTimeMillis() - startTime));

		if (showpaint) {
			Graphics2D g = (Graphics2D) g1;
			g.drawImage(img1, -4, 336, null);
			g.setFont(font1);
			g.setColor(color1);
			g.drawString(runtime.toElapsedString(), 215, 444);
			g.drawString(String.valueOf(rangegainedExp), 198, 460);
			g.drawString(String.valueOf(hpgainedExp), 181, 480);
			g.drawLine(mouseX, mouseY - 10, mouseX, mouseY + 10);
			g.drawLine(mouseX - 10, mouseY, mouseX + 10, mouseY);
			g.drawString(String.valueOf(expHour), 183, 497);
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
			if (showpaint) {
				showpaint = false;
			} else if (img1 == null) {
				Logger.getLogger("EpicsChinsGUI").info("Image failed to load");
			}
			showpaint = true;
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