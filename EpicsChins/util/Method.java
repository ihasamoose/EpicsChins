package EpicsChins.util;

import org.powerbot.game.api.methods.*;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
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

import java.util.logging.Logger;

/**
 * User: Epics
 * Date: 8/28/12
 * Time: 8:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class Method {
	public static boolean isPoisoned() {
		return Game.isLoggedIn() && Settings.get(102) != 0;
	}

	public static void changeWorlds() {
		Context.get().getActiveScript().log.info("Running changeWorlds code");
		Context.get().getActiveScript().log.info(String.valueOf(Game.logout(true)));
		if (Game.logout(true)) {
			Time.sleep(Random.nextInt(2000, 5000));
			if (Lobby.isOpen() && Game.getClientState() != Lobby.STATE_LOBBY_IDLE) {
				int randomWorld = Data.WORLDS_MEMBER[Random.nextInt(0, Data.WORLDS_MEMBER.length) - 1];
				Context.setLoginWorld(randomWorld);
				Time.sleep(Random.nextInt(200, 400));
				if (Game.isLoggedIn()) {
					Prayer.setQuick();
				}
			}
		}
	}

	public static void checkRun() {
		if (Walking.getEnergy() > 30) {
			Walking.setRun(true);
		}
	}

	private static void checkPrayer() {
		Context.get().getActiveScript().log.info("Running checkPrayer code");
		if (Prayer.getPoints() <= 250) {
			final Item PRAYER_POT_ITEM = Inventory.getItem(Data.POT_PRAYER);
			if (PRAYER_POT_ITEM != null && PRAYER_POT_ITEM.getWidgetChild().interact("Drink")) {
				final int id = PRAYER_POT_ITEM.getId();
				final int count = Inventory.getCount(id);
				final Timer t = new Timer(2500);
				while (t.isRunning() && Inventory.getCount(id) == count) {
					Time.sleep(50);
				}
			} else {
				Context.get().getActiveScript().log.info("Prayer is above 25%, not using potion!");
			}
		}
	}

	public static void checkRenewal() {
		Context.get().getActiveScript().log.info("Running checkRenewal code");
		if (Data.t == null || !Data.t.isRunning()) {
			doDrinkRenewal();
			Data.t = new Timer(300000);
		} else {
			Data.t.reset();
		}
	}

	private static void doDrinkRenewal() {
		Context.get().getActiveScript().log.info("Running doDrinkRenewal code");
		final Item PRAYER_RENEWAL_ITEM = Inventory.getItem(Data.FLASK_PRAYER_RENEWAL);
		if (PRAYER_RENEWAL_ITEM != null && PRAYER_RENEWAL_ITEM.getWidgetChild().interact("Drink")) {
			final int id = PRAYER_RENEWAL_ITEM.getId();
			final int count = Inventory.getCount(id);
			final Timer t = new Timer(2500);
			while (t.isRunning() && Inventory.getCount(id) == count) {
				Time.sleep(50);
			}
		}
	}

	public static void doDrinkAntipoison() {
		Context.get().getActiveScript().log.info("Running doDrinkAntipoison code");
		final Item ANTIPOISON_ALL_ITEM = Inventory.getItem(GUI.antipoisonUser);
		if (ANTIPOISON_ALL_ITEM != null && ANTIPOISON_ALL_ITEM.getWidgetChild().interact("Drink")) {
			final int id = ANTIPOISON_ALL_ITEM.getId();
			final int count = Inventory.getCount(id);
			final Timer t = new Timer(2500);
			while (t.isRunning() && Inventory.getCount(id) == count) {
				Time.sleep(50);
			}
		}
	}

	public static void doBreakTab() {
		Context.get().getActiveScript().log.info("Running doBreakTab code");
		final Item TAB_ITEM = Inventory.getItem(Data.TAB_VARROCK);
		if (TAB_ITEM != null && TAB_ITEM.getWidgetChild().interact("Break")) {
			final int id = TAB_ITEM.getId();
			final int count = Inventory.getCount(id);
			final Timer t = new Timer(2500);
			while (t.isRunning() && Inventory.getCount(id) == count) {
				Time.sleep(50);
			}
		}
	}

	private static void doDrinkRangePotion() {
		Context.get().getActiveScript().log.info("Running doDrinkRangePotion code");
		final Item RANGE_POT_ITEM = Inventory.getItem(Data.FLASK_RANGING);
		final int REAL_RANGE = Skills.getRealLevel(Skills.RANGE);
		final int POTTED_RANGE = Skills.getLevel(Skills.RANGE);
		final int RANGE_DIFFERENCE = POTTED_RANGE - REAL_RANGE;
		if (RANGE_POT_ITEM != null && RANGE_DIFFERENCE <= 3 && RANGE_POT_ITEM.getWidgetChild().interact("Drink")) {
			final int id = RANGE_POT_ITEM.getId();
			final int count = Inventory.getCount(id);
			final Timer t = new Timer(2500);
			while (t.isRunning() && Inventory.getCount(id) == count) {
				Time.sleep(50);
			}
		} else {
			Context.get().getActiveScript().log.info("We're out of ranging pots, resuming until prayer potions are gone!");
		}
	}

	public static void doAttackMonkey(final NPC npc) {
		Context.get().getActiveScript().log.info("Running doAttackMonkey code");
		checkPrayer();
		checkRenewal();
		doDrinkRangePotion();
		if (npc != null && npc.isOnScreen()) {
			if (npc.interact("Attack")) {
				Time.sleep(50);
				if (Players.getLocal().getInteracting().equals(npc)) {
					Time.sleep(Random.nextInt(700, 800));
				}
			}
		} else {
			Camera.turnTo(npc);
		}
	}

	private static void equipGreegree() {
		Item greegree = Inventory.getItem(Data.GREEGREE_IDS);
		if (greegree != null && greegree.getWidgetChild().click(true)) {
			final int ID = greegree.getId();
			final int COUNT = Inventory.getCount(ID);
			final Timer t = new Timer(2500);
			while (t.isRunning() && Inventory.getCount(ID) == COUNT) {
				Time.sleep(50);
			}
		} else {
			if (greegree == null) {
				Data.runCheck = true;
			}
		}
	}

	public static boolean areaContainsTwoOrMore(final Area area) {
		Logger.getLogger("EpicsChins").info("Running areaContainsTwoOrMore code");
		final Player[] PLAYERS_IN_AREA = Players.getLoaded(new Filter<Player>() {
			@Override
			public boolean accept(Player t) {
				return area.contains(t) && t.getName().equals(Players.getLocal().getName());
			}
		});
		return Game.getClientState() != Game.INDEX_MAP_LOADING && PLAYERS_IN_AREA.length >= 2;
	}

	public static void antiban() {
		final int RANDOM_PITCH = Random.nextInt(350, 10);
		final int RANDOM_ANGLE = Random.nextInt(89, 50);
		Logger.getLogger("EpicsChins").info("Running antiban code");
		int state = Random.nextInt(0, 3);
		switch (state) {
			case 1:
				Logger.getLogger("EpicsChins").info("Setting random camera angle & pitch");
				Camera.setAngle(RANDOM_ANGLE);
				Camera.setPitch(RANDOM_PITCH);
				break;
			case 2:
				Logger.getLogger("EpicsChins").info("Checking constitution leveling progress");
				WidgetChild c = Widgets.get(1213).getChild(12);
				if (c.validate()) {
					c.hover();
					Time.sleep(1000);
				}
				break;
			case 3:
				Logger.getLogger("EpicsChins").info("Checking ranging leveling progress");
				WidgetChild d = Widgets.get(1213).getChild(14);
				if (d.validate()) {
					d.hover();
					Time.sleep(1000);
					break;
				}
		}
	}

	private static void yesInterfaceClicker() {
		WidgetChild yesInterface = Widgets.get(1188, 3);

		if (yesInterface != null && yesInterface.click(true)) {
			final Timer YES_INTERFACE_TIMER = new Timer(2500);
			while (YES_INTERFACE_TIMER.isRunning()) {
				Time.sleep(50);
			}
			Time.sleep(2000);
		}
	}

	public static void preEat() {
		Item food = Inventory.getItem(GUI.foodUser);
		Item prayerPot = Inventory.getItem(Data.POT_PRAYER_DOSE_4);

		if (Players.getLocal().getHpPercent() < 30) {
			Walking.findPath(Tiles.TILE_GRAND_BANK).traverse();
			Method.checkRun();
			if (Tiles.TILE_GRAND_BANK.isOnScreen()) {
				Camera.turnTo(Tiles.TILE_GRAND_BANK);
			}
			Bank.open();
			if (Bank.isOpen()) {
				if (Inventory.isFull()) {
					Bank.deposit(Data.POT_PRAYER_DOSE_4, 3);
				}
				if (Bank.withdraw(food.getId(), 3)) {
					Time.sleep(200, 400);
				}
				Bank.close();
			}
			if (!Bank.isOpen() && Players.getLocal().getHpPercent() > 75) {
				food.getWidgetChild().interact("Eat");
				Time.sleep(Random.nextInt(300, 400));
			}
			if (Bank.open()) {
				Time.sleep(200, 400);
			}
			if (Bank.isOpen()) {
				Bank.deposit(prayerPot.getId(), 5);
				if (Inventory.getCount(prayerPot.getId()) == 0) {
					Bank.getNearest();
					Bank.open();
					if (Bank.isOpen()) {
						if (Bank.withdraw(food.getId(), 2)) {
							Time.sleep(200, 300);
						}
						Bank.withdraw(prayerPot.getId(), 3);
					}
				}
			}
		}
	}

	public static void checkNoobTele() {
		if (Tiles.AREA_GRAND_TELE.contains(Players.getLocal().getLocation())) {
			Context.get().getActiveScript().log.info("You have the n00b varrock teleport. Walking to bank");
			if (Tiles.TILE_GRAND_BANK.validate()) {
				if (Calculations.distanceTo(Tiles.TILE_GRAND_BANK) >= 2) {
					Walking.findPath(Tiles.TILE_GRAND_BANK).traverse();
					Players.getLocal().isMoving();
				}
			}
			if (!Tiles.TILE_GRAND_BANK.validate()) {
				Tile nextTile = Walking.newTilePath(Tiles.PATH_TO_BANK_TILE).randomize(2, 4).getNext();
				Walking.walk(nextTile);
				if (!Tiles.TILE_GRAND_BANK.validate() && Calculations.distanceTo(Tiles.TILE_GRAND_BANK) >= 2) {
					Walking.findPath(Tiles.TILE_GRAND_BANK).traverse();
					Players.getLocal().isMoving();
				}
			}
			Camera.turnTo(Tiles.TILE_GRAND_BANK);
			Context.get().getActiveScript().log.info("You aren't in the Grand Exchange or the n00b teleport zone! Shutting down...");
			Context.get().getActiveScript().stop();
		}
	}

	public static void chargePrayer() {
		final double prayerPoints = (Prayer.getPoints());
		final double prayerMinimum = ((Skills.getLevel(Skills.PRAYER) * 10)* 0.5);

		if (prayerPoints <= prayerMinimum) {
			if (Data.logWalkCode == 1) {
				Context.get().getActiveScript().log.info("Prayer is low, let's go charge up before we head out.");
				Data.logWalkCode++;
			}
			if (Tiles.AREA_GE.contains(Players.getLocal().getLocation())) {
				Tile nextTile = Walking.newTilePath(Tiles.PATH_TO_PRAYER_FROM_GE).randomize(2, 4).getNext();
				if (Walking.walk(nextTile)) {
					while (Players.getLocal().isMoving()) {
						Time.sleep(50);
					}
				}
			} else if (Tiles.AREA_GRAND_TELE.contains(Players.getLocal().getLocation())) {
				Tile nextTile = Walking.newTilePath(Tiles.PATH_TO_PRAYER_FROM_TELE).randomize(2, 4).getNext();
				Walking.walk(nextTile);
				while (Players.getLocal().isMoving()) {
					Time.sleep(50);
				}
			} else if (!Tiles.AREA_GRAND_TELE.contains(Players.getLocal().getLocation()) && !Tiles.AREA_GE.contains(Players.getLocal().getLocation())) {
				Tile nextTile = Walking.newTilePath(Tiles.PATH_TO_PRAYER_FROM_GE).randomize(2, 4).getNext();
				if (nextTile != null) {
					Walking.walk(nextTile);
					while (Players.getLocal().isMoving()) {
						Time.sleep(50);
					}
				} else {
					Context.get().getActiveScript().log.severe("OH MY GOD LAGG");
				}
			}
			if (Tiles.TILE_PRAYER.validate() && Calculations.distanceTo(Tiles.TILE_PRAYER) <= 5) {
				SceneObject varrockAltar = SceneEntities.getNearest(Data.ID_ALTAR_VARROCK);

				if (varrockAltar != null && varrockAltar.isOnScreen()) {
					varrockAltar.click(true);
					if (Players.getLocal().getAnimation() == Data.ID_ANIMATION_PRAY) {
						Time.sleep(100, 400);
					}
					if (Prayer.getPoints() == (Skills.getRealLevel(Skills.PRAYER))) {
						Context.get().getActiveScript().log.info("All charged up, let's get going.");
						if (!Tiles.TILE_GRAND_TREE.validate() && Calculations.distanceTo(Tiles.TILE_GRAND_TREE) >= 20) {
							Tile
									nextTile =
									Walking.newTilePath(Tiles.PATH_TO_PRAYER_FROM_GE).randomize(2,
											                                                           4).reverse().getNext();
							Walking.walk(nextTile);
							while (Players.getLocal().isMoving()) {
								Time.sleep(50);
							}
						} else {
							Walking.findPath(Tiles.TILE_GRAND_TREE).traverse();
							while (Players.getLocal().isMoving()) {
								Time.sleep(50);
							}
						}
					} else {
						Camera.turnTo(varrockAltar);
					}
				}
			}
		}
	}

	public static void isInGE() {
		double prayerPoints = (Prayer.getPoints());
		double prayerMinimum = ((Skills.getLevel(Skills.PRAYER) * 10)* 0.5);

		if (Tiles.TILE_GRAND_TREE.validate() && Tiles.AREA_GE.contains(Players.getLocal().getLocation()) && prayerPoints >= prayerMinimum) {
			Walking.findPath(Tiles.TILE_GRAND_TREE).traverse();
			while (Players.getLocal().isMoving()) {
				Time.sleep(50);
			}
		}
	}


	public static void isInGnome() {
		if (Tiles.TILE_TREE_DOOR.validate() && Tiles.AREA_GNOME_STRONGHOLD.contains(Players.getLocal().getLocation())) {
			Walking.findPath(Tiles.TILE_TREE_DOOR).traverse();
			while (Players.getLocal().isMoving()) {
				Time.sleep(50);
			}
		}
	}

	public static void openTreeDoor(final SceneObject se) {
		if (se != null && Calculations.distanceTo(Tiles.TILE_TREE_DOOR) <= 10 && se.isOnScreen() && !Tiles.TILE_INSIDE_TREE_DOOR.equals(Players.getLocal().getLocation()) && se.interact("Open")) {
			final Timer t = new Timer(2500);
			while (se.validate() && t.isRunning()) {
				Time.sleep(50);
			}
		}
	}

	public static void climbLadder(final SceneObject se) {
		if (Tiles.TILE_INSIDE_TREE_DOOR.equals(Players.getLocal().getLocation())) {
			if (se != null && Players.getLocal().getAnimation() == -1 && se.interact("Climb-up")) {
				final Timer t = new Timer(2500);
				while (se.validate() && t.isRunning()) {
					Time.sleep(50);
				}
			} else if (se != null && !se.isOnScreen()) {
				Camera.turnTo(se);
			}
		}
	}

	public static void doYesInteraction(final Area area, final NPC npc) {
		if (area.contains(Players.getLocal().getLocation())) {
			Walking.findPath(npc.getLocation()).traverse();
			while (Players.getLocal().isMoving()) {
				Time.sleep(50);
			}

			if (npc.isOnScreen() && npc.interact("Travel")) {
				final Timer t = new Timer(2500);
				while (npc.validate() && t.isRunning()) {
					Method.yesInterfaceClicker();
					Time.sleep(1000);
				}
			} else if (!npc.isOnScreen()) {
				Camera.turnTo(npc);
			}
		}
	}

	public static void clickSpiritTree(final Area area, final SceneObject se) {
		if (area.contains(Players.getLocal().getLocation())) {
			if (se != null && se.validate() && se.interact("Teleport")) {
				while (Players.getLocal().getAnimation() == Data.ID_ANIMATION_TREE || Players.getLocal().getAnimation() == Data.ID_ANIMATION_TREE_2) {
					Time.sleep(500);
				}
				final Timer spiritTreeTimer = new Timer(2500);
				while (se.validate() && spiritTreeTimer.isRunning()) {
					Time.sleep(50);
				}
			} else {
				if (se != null && !se.isOnScreen()) {
					Camera.turnTo(se);
					Time.sleep(500);
				}
			}
		}
	}

	public static void clickGnomeInterface() {
		WidgetChild spiritTreeInterface = Widgets.get(864, 6).getChild(0);
		if (spiritTreeInterface != null && spiritTreeInterface.click(true)) {
			final Timer tt = new Timer(2500);

			while (tt.isRunning()) {
				Time.sleep(50);
			}
			if (Players.getLocal().getAnimation() == Data.ID_ANIMATION_TREE || Players.getLocal().getAnimation() == Data.ID_ANIMATION_TREE_2) {
				Time.sleep(500);
			}
		}
	}

	public static void utilizeGreegree() {
		if (Tiles.AREA_APE_ATOLL.contains(Players.getLocal().getLocation()) && Data.usingGreegree) {
			Method.checkRun();
			Method.equipGreegree();
		}

		if (Tiles.AREA_APE_ATOLL.contains(Players.getLocal().getLocation()) && !Data.usingGreegree) {
			Method.checkRun();
		}
	}

	public static void interactAndWalkToLadder() {
		if (Tiles.AREA_APE_ATOLL.contains(Players.getLocal().getLocation())) {
			if (Calculations.distanceTo(Tiles.TILE_APE_LADDER_TOP) <= 10) {
				Data.runCheck = true;
			}

			Walking.findPath(Tiles.TILE_APE_LADDER_TOP).traverse();
			while (Players.getLocal().isMoving()) {
				Time.sleep(50);
			}
			SceneObject apeAtollLadder = SceneEntities.getNearest(Data.ID_LADDER_APE);

			if (apeAtollLadder != null && apeAtollLadder.isOnScreen() && Calculations.distanceTo(Tiles.TILE_APE_LADDER_TOP) <= 5 && apeAtollLadder.interact("Climb-down")) {
				final Timer t = new Timer(2500);
				while (apeAtollLadder.validate() && t.isRunning()) {
					Time.sleep(50);
				}
			} else if (apeAtollLadder == null) {
				Context.get().getActiveScript().log.info("How did you manage to break me? apeAtollLadder is null!");
			}
			if (apeAtollLadder != null && !apeAtollLadder.isOnScreen()) {
				Camera.turnTo(apeAtollLadder);
			}
		}
	}

	public static void checkSpotOne() {
		if (Calculations.distanceTo(Tiles.TILE_CHIN_1) >= 5) {
			Tile nextTile = Walking.newTilePath(Tiles.PATH_TO_CHIN_TILE_1).getNext();
			Walking.walk(nextTile);
			while (Players.getLocal().isMoving()) {
				Time.sleep(50);
			}
		} else {
			Walking.findPath(Tiles.TILE_CHIN_1).traverse();
			while (Players.getLocal().isMoving()) {
				Time.sleep(50);
			}
		}
	}

	public static void checkSpotTwo() {
		if (Calculations.distanceTo(Tiles.TILE_CHIN_2) >= 5) {
			Walking.newTilePath(Tiles.PATH_TO_CHIN_TILE_2).traverse();
			Walking.findPath(Tiles.TILE_CHIN_2).traverse();
			while (Players.getLocal().isMoving()) {
				if (Method.isPoisoned()) {
					Method.doDrinkAntipoison();
					return;
				}
				Time.sleep(50);
			}
		}
	}

	public static void checkSpotThree() {
		if (Calculations.distanceTo(Tiles.TILE_CHIN_3) >= 5) {
			Walking.newTilePath(Tiles.PATH_TO_CHIN_TILE_3).traverse();
			Walking.findPath(Tiles.TILE_CHIN_3).traverse();
			while (Players.getLocal().isMoving()) {
				if (Method.isPoisoned()) {
					Method.doDrinkAntipoison();
					return;
				}
				Time.sleep(50);
			}
		}
	}
	public static void chinSpotOne() {
		while (!Tiles.TILE_CHIN_1.equals(Players.getLocal().getLocation())) {
			Walking.findPath(Tiles.TILE_CHIN_1).traverse();
			while (Players.getLocal().isMoving()) {
				if (Method.isPoisoned()) {
					Method.doDrinkAntipoison();
					return;
				}
				Time.sleep(50);
			}
			Data.atDestination = true;
		}
	}

	public static void chinSpotThwo() {
		while (!Tiles.TILE_CHIN_2.equals(Players.getLocal().getLocation())) {
			Walking.findPath(Tiles.TILE_CHIN_2).traverse();
			while (Players.getLocal().isMoving()) {
				if (Method.isPoisoned()) {
					Method.doDrinkAntipoison();
					return;
				}
				Time.sleep(50);
			}
			Data.atDestination = true;
		}

	}
	public static void chinSpotThree() {
		if (!Tiles.TILE_CHIN_3.equals(Players.getLocal().getLocation())) {
			Walking.findPath(Tiles.TILE_CHIN_3).traverse();
			while (Players.getLocal().isMoving()) {
				if (Method.isPoisoned()) {
					Method.doDrinkAntipoison();
					return;
				}
				Time.sleep(50);
			}
			Data.atDestination = true;
		}
	}

	public static void setQuickOn() {
		if (Prayer.isQuickOn()) {
			Prayer.toggleQuick(true);
			Timer t = new Timer(2500);
			while (!Prayer.isQuickOn() && t.isRunning()) {
				Time.sleep(50);
			}
		}
	}
}
