package EpicsChins.util.tasks;

import EpicsChins.util.Data;
import EpicsChins.util.GUI;
import EpicsChins.util.Method;
import EpicsChins.util.Tiles;
import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Prayer;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import org.powerbot.game.bot.Context;

/**
 * User: Epics
 * Date: 8/28/12
 * Time: 8:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class RunToChins extends Strategy implements Runnable {

	@Override
	public void run() {
		// Interaction ID
		if (Data.logWalkCode == 0) {
			Context.get().getActiveScript().log.info("Running walk there code");
			Data.logWalkCode++;
		}

		SceneObject spiritTreeGe = SceneEntities.getNearest(Data.ID_SPIRITTREE_GE);
		if (!Tiles.CHIN_LIST.contains(Players.getLocal().getLocation())) {
			Item food = Inventory.getItem(GUI.foodUser);
			Item prayerPot = Inventory.getItem(Data.POT_PRAYER_DOSE_4);

			Method.checkRun();
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
			if (Tiles.AREA_GRAND_TELE.contains(Players.getLocal().getLocation())) {
				Context.get().getActiveScript().log.info("You have the n00b varrock teleport. Walking to bank");
				if (Tiles.TILE_GRAND_BANK != null) {
					if (Calculations.distanceTo(Tiles.TILE_GRAND_BANK) >= 2) {
						Walking.findPath(Tiles.TILE_GRAND_BANK).traverse();
						Players.getLocal().isMoving();
					}
				}
				if (Tiles.TILE_GRAND_BANK == null) {
					Tile nextTile = Walking.newTilePath(Tiles.PATH_TO_BANK_TILE).getNext();
					Walking.walk(nextTile);
					while (Tiles.TILE_GRAND_BANK != null && Calculations.distanceTo(Tiles.TILE_GRAND_BANK) >= 2) {
						Walking.findPath(Tiles.TILE_GRAND_BANK).traverse();
						Players.getLocal().isMoving();
					}
				}
				Camera.turnTo(Tiles.TILE_GRAND_BANK);
				Context.get().getActiveScript().log.info("You aren't in the Grand Exchange or the n00b teleport zone! Shutting down...");
				Context.get().getActiveScript().stop();
			}
			final double PRAYER_POINTS = (Skills.getRealLevel(Skills.PRAYER));
			final double PRAYER_DIFFERENCE = (PRAYER_POINTS * 0.45);
			final double TRUE_PRAYER_DIFFERENCE = PRAYER_POINTS - PRAYER_DIFFERENCE;

			if (PRAYER_POINTS <= TRUE_PRAYER_DIFFERENCE) {
				if (Data.logWalkCode == 1) {
					Context.get().getActiveScript().log.info("Prayer is low, let's go charge up before we head out.");
					Data.logWalkCode++;
				}
				if (Tiles.AREA_GE.contains(Players.getLocal().getLocation())) {
					Tile nextTile = Walking.newTilePath(Tiles.PATH_TO_PRAYER_FROM_GE).getNext();
					Walking.walk(nextTile);
					if (Tiles.TILE_PRAYER == null) {
						Tile nextTileTwo = Walking.newTilePath(Tiles.PATH_TO_PRAYER_FROM_GE).getNext();
						Walking.walk(nextTileTwo);
					}
					if (Tiles.TILE_PRAYER != null) {
						while (Calculations.distanceTo(Tiles.TILE_PRAYER) >= 5) {
							Walking.findPath(Tiles.TILE_PRAYER).traverse();
						}
					}
				} else if (Tiles.AREA_GRAND_TELE.contains(Players.getLocal().getLocation())) {
					if (Tiles.TILE_PRAYER == null) {
						Tile nextTile = Walking.newTilePath(Tiles.PATH_TO_PRAYER_FROM_TELE).getNext();
						Walking.walk(nextTile);
					}
					if (Tiles.TILE_PRAYER != null) {
						while (Calculations.distanceTo(Tiles.TILE_PRAYER) >= 5) {
							Walking.findPath(Tiles.TILE_PRAYER).traverse();
						}
					}
				} else if (!Tiles.AREA_GRAND_TELE.contains(Players.getLocal().getLocation()) && !Tiles.AREA_GE.contains(Players.getLocal().getLocation())) {
					if (Tiles.TILE_PRAYER == null) {
						Tile nextTile = Walking.newTilePath(Tiles.PATH_TO_PRAYER_FROM_GE).getNext();
						Walking.walk(nextTile);
					}
					if (Tiles.TILE_PRAYER != null) {
						while (Calculations.distanceTo(Tiles.TILE_PRAYER) >= 5) {
							Walking.findPath(Tiles.TILE_PRAYER).traverse();
						}
					}
				}
				if (Tiles.TILE_PRAYER != null && Calculations.distanceTo(Tiles.TILE_PRAYER) <= 5) {
					SceneObject varrockAltar = SceneEntities.getNearest(Data.ID_ALTAR_VARROCK);

					if (varrockAltar != null && varrockAltar.isOnScreen()) {
						varrockAltar.click(true);
						if (Players.getLocal().getAnimation() == Data.ID_ANIMATION_PRAY) {
							Time.sleep(100, 400);
						}
						if (Prayer.getPoints() == (Skills.getRealLevel(Skills.PRAYER) * 10)) {
							Context.get().getActiveScript().log.info("All charged up, let's get going.");
						}
					} else {
						Camera.turnTo(varrockAltar);
					}
				}
			}
			if (Tiles.TILE_GRAND_TREE.validate()) {
				Walking.findPath(Tiles.TILE_GRAND_TREE).traverse();
			} else if (Tiles.TILE_GRAND_TREE == null) {
				if (!Tiles.TILE_GRAND_TREE.validate()) {
					Tile nextTile = Walking.newTilePath(Tiles.PATH_TO_PRAYER_FROM_GE).getNext();
					Walking.walk(nextTile);
				}
				if (Tiles.TILE_GRAND_TREE.validate()) {
					Walking.findPath(Tiles.TILE_GRAND_TREE).traverse();
				}
			}
			if (spiritTreeGe != null && spiritTreeGe.isOnScreen() && spiritTreeGe.interact("Teleport")) {
				while (Players.getLocal().getAnimation() == Data.ID_ANIMATION_TREE || Players.getLocal().getAnimation() == Data.ID_ANIMATION_TREE_2) {
					Time.sleep(500);
				}
				final Timer SPIRIT_TREE_TIMER = new Timer(2500);
				while (spiritTreeGe.validate() && SPIRIT_TREE_TIMER.isRunning()) {
					Time.sleep(50);
				}
			}
		}
		SceneObject SpiritTreeMain = SceneEntities.getNearest(Data.ID_SPIRITTREE_MAIN);

		if (SpiritTreeMain != null && Tiles.AREA_SPIRIT_MID.contains(Players.getLocal().getLocation()) && SpiritTreeMain.isOnScreen() && SpiritTreeMain.interact("Teleport")) {
			final Timer t = new Timer(2500);

			while (SpiritTreeMain.validate() && t.isRunning()) {
				Time.sleep(50);
			}
			Time.sleep(50, 400);
			final WidgetChild spiritTreeInterface = Widgets.get(864).getChild(6).getChild(0);
			if (spiritTreeInterface.validate() && spiritTreeInterface.click(true)) {
				final Timer tt = new Timer(2500);

				while (spiritTreeInterface.validate() && tt.isRunning()) {
					Time.sleep(50);
				}
				while (Players.getLocal().getAnimation() == Data.ID_ANIMATION_TREE || Players.getLocal().getAnimation() == Data.ID_ANIMATION_TREE_2) {
					Time.sleep(500);
				}
			}
		} else if (SpiritTreeMain != null && !SpiritTreeMain.isOnScreen()) {
			Camera.turnTo(SpiritTreeMain);
			Time.sleep(Random.nextInt(500, 1000));
		}
		SceneObject treeDoor = SceneEntities.getNearest(Data.ID_TREEDOOR);

		if (Tiles.AREA_GNOME_STRONGHOLD.contains(Players.getLocal().getLocation())) {
			Walking.findPath(Tiles.TILE_TREE_DOOR).traverse();
		}
		if (Calculations.distanceTo(Tiles.TILE_TREE_DOOR) <= 10 && treeDoor != null && treeDoor.isOnScreen() && !Tiles.TILE_INSIDE_TREE_DOOR.equals(Players.getLocal().getLocation()) && treeDoor.interact("Open")) {
			final Timer t = new Timer(2500);
			while (treeDoor.validate() && t.isRunning()) {
				Time.sleep(50);
			}
		}
		if (Tiles.TILE_INSIDE_TREE_DOOR.equals(Players.getLocal().getLocation())) {
			SceneObject gnomeLadder = SceneEntities.getNearest(Data.ID_LADDER_GNOME);

			if (gnomeLadder != null && Players.getLocal().getAnimation() == -1 && gnomeLadder.interact("Climb-up")) {
				final Timer t = new Timer(2500);
				while (gnomeLadder.validate() && t.isRunning()) {
					Time.sleep(50);
				}
			} else if (!gnomeLadder.isOnScreen()) {
				Camera.turnTo(gnomeLadder);
			}
		}

		if (Tiles.AREA_GNOME_LEVEL_ONE.contains(Players.getLocal().getLocation())) {
			Walking.findPath(Tiles.TILE_TREE_DAERO).traverse();
			NPC daero = NPCs.getNearest(Data.ID_NPC_DAERO);

			if (daero != null && daero.isOnScreen() && daero.interact("Travel")) {
				final Timer t = new Timer(2500);
				while (daero.validate() && t.isRunning()) {
					Method.yesInterfaceClicker();
					Time.sleep(500);
				}
			} else if (!daero.isOnScreen()) {
				Camera.turnTo(daero);
			}
		}
		if (Tiles.AREA_BLINDFOND_ZONE.contains(Players.getLocal().getLocation())) {
			NPC waydar = NPCs.getNearest(Data.ID_NPC_WAYDAR);

			Walking.findPath(waydar).traverse();
			if (waydar != null && waydar.isOnScreen() && waydar.getAnimation() == -1 && waydar.interact("Travel")) {
				final Timer t = new Timer(2500);
				while (waydar.validate() && t.isRunning()) {
					Method.yesInterfaceClicker();
					Time.sleep(50);
				}
			} else if (!waydar.isOnScreen()) {
				Camera.turnTo(waydar);
			}
		}
		if (Tiles.AREA_CRASH_ISLAND.contains(Players.getLocal().getLocation())) {
			NPC lumdo = NPCs.getNearest(Data.ID_NPC_LUMBO);

			if (lumdo != null && lumdo.isOnScreen() && lumdo.getAnimation() == -1 && lumdo.interact("Travel")) {
				final Timer t = new Timer(2500);

				while (lumdo.validate() && t.isRunning()) {
					Method.yesInterfaceClicker();
					Time.sleep(50);
				}
			} else if (!lumdo.isOnScreen()) {
				Camera.turnTo(lumdo);
			}
		}
		Data.checkChins = true;
		if (Tiles.AREA_APE_ATOLL.contains(Players.getLocal().getLocation()) && Data.usingGreegree) {
			Method.equipGreegree();
			Data.runCheck = true;

			Walking.findPath(Tiles.TILE_APE_LADDER_TOP).traverse();
			SceneObject apeAtollLadder = SceneEntities.getNearest(Data.ID_LADDER_APE);

			if (apeAtollLadder != null && apeAtollLadder.isOnScreen() && Calculations.distanceTo(Tiles.TILE_APE_LADDER_TOP) <= 5 && apeAtollLadder.interact("Climb-down")) {
				final Timer t = new Timer(2500);
				while (apeAtollLadder.validate() && t.isRunning()) {
					Time.sleep(50);
				}
			} else if (!apeAtollLadder.isOnScreen()) {
				Camera.turnTo(apeAtollLadder);
			}
		} else if (Tiles.AREA_APE_ATOLL.contains(Players.getLocal().getLocation()) && !Data.usingGreegree) {
			Method.checkRun();
			return;
		}
		if (Tiles.AREA_APE_ATOLL_DUNGEON.contains(Players.getLocal().getLocation())) {
			Data.logWalkCode = 0;

			//Method.checkRenewal();
			if (!Prayer.isQuickOn()) {
				Prayer.toggleQuick(true);
				Timer t = new Timer(2500);
				while (!Prayer.isQuickOn() && t.isRunning()) {
					Time.sleep(50);
				}
			}
			if (Calculations.distanceTo(Tiles.TILE_CHIN_1) >= 5) {
				Tile nextTile = Walking.newTilePath(Tiles.PATH_TO_CHIN_TILE_1).getNext();
				Walking.walk(nextTile);
			}
			if (Method.tileContainsTwoOrMore(Tiles.TILE_CHIN_1)) {
				if (Calculations.distanceTo(Tiles.TILE_CHIN_2) >= 5) {
					Walking.newTilePath(Tiles.PATH_TO_CHIN_TILE_2).traverse();
					Players.getLocal().isMoving();
					Walking.findPath(Tiles.TILE_CHIN_2).traverse();
				}
				if (Method.tileContainsTwoOrMore(Tiles.TILE_CHIN_2)) {
					if (Calculations.distanceTo(Tiles.TILE_CHIN_3) >= 5) {
						Walking.newTilePath(Tiles.PATH_TO_CHIN_TILE_3).traverse();
						Players.getLocal().isMoving();
						Walking.findPath(Tiles.TILE_CHIN_3).traverse();
					}
					if (Method.areaContainsTwoOrMore()) {
						Method.changeWorlds();
					} else {
						if (!Tiles.TILE_CHIN_3.equals(Players.getLocal().getLocation())) {
							Walking.findPath(Tiles.TILE_CHIN_3).traverse();
							Players.getLocal().isMoving();
							Data.atDestination = true;
						}
					}
				} else {
					while (!Tiles.TILE_CHIN_2.equals(Players.getLocal().getLocation())) {
						Walking.findPath(Tiles.TILE_CHIN_2).traverse();
						Players.getLocal().isMoving();
						Data.atDestination = true;
					}
				}
			} else {
				while (!Tiles.TILE_CHIN_1.equals(Players.getLocal().getLocation())) {
					Walking.findPath(Tiles.TILE_CHIN_1).traverse();
					Players.getLocal().isMoving();
					Data.atDestination = true;
				}
			}
		}
	}

	@Override
	public boolean validate() {
		int antipoisonData = 0;
		int flaskRenewalCountData = 0;
		int prayerPotCountData = 0;
		int rangingFlaskData = 0;

		for (Item y : Inventory.getItems()) {
			for (int x : Data.ANTIPOISON_ALL) {
				if (y.getId() == x) {
					antipoisonData++;
				}
			}
			for (int x : Data.FLASK_PRAYER_RENEWAL) {
				if (y.getId() == x) {
					flaskRenewalCountData++;
				}
			}
			for (int x : Data.POT_PRAYER) {
				if (y.getId() == x) {
					prayerPotCountData++;
				}
			}
			for (int x : Data.FLASK_RANGING) {
				if (y.getId() == x) {
					rangingFlaskData++;
				}
			}
		}
		return antipoisonData >= 1 && flaskRenewalCountData >= 3 && prayerPotCountData >= 18 && rangingFlaskData >= 3 && Data.chinNumber >= 1500 && Inventory.getCount(Data.TAB_VARROCK) >= 1 && Inventory.getCount(GUI.foodUser) >= 1 &&
		       !Method.isPoisoned() && Game.isLoggedIn() && !Data.runCheck && Data.START_SCRIPT;
	}
}
