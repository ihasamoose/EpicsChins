package EpicsChins.util.tasks;

import EpicsChins.util.Data;
import EpicsChins.util.GUI;
import EpicsChins.util.Method;
import EpicsChins.util.Tiles;
import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Prayer;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;
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

		if (!Tiles.CHIN_LIST.contains(Players.getLocal().getLocation())) {
			Method.checkRun();
			Method.preEat();
			Method.checkNoobTele();
			Method.chargePrayer();
			Method.isInGE();

			SceneObject spiritTreeGe = SceneEntities.getNearest(Data.ID_SPIRITTREE_GE);
			Method.clickSpiritTree(Tiles.AREA_SPIRIT_GE, spiritTreeGe);

			SceneObject spiritTreeMain = SceneEntities.getNearest(Data.ID_SPIRITTREE_MAIN);
			Method.clickSpiritTree(Tiles.AREA_SPIRIT_MID, spiritTreeMain);
			Method.clickGnomeInterface();
			Method.isInGnome();

			SceneObject treeDoor = SceneEntities.getNearest(Data.ID_TREEDOOR);
			Method.openTreeDoor(treeDoor);

			SceneObject gnomeLadder = SceneEntities.getNearest(Data.ID_LADDER_GNOME);
			Method.climbLadder(gnomeLadder);

			NPC daero = NPCs.getNearest(Data.ID_NPC_DAERO);
			Method.doYesInteraction(Tiles.AREA_GNOME_LEVEL_ONE, daero);

			NPC waydar = NPCs.getNearest(Data.ID_NPC_WAYDAR);
			Method.doYesInteraction(Tiles.AREA_BLINDFOLD_ZONE, waydar);

			NPC lumdo = NPCs.getNearest(Data.ID_NPC_LUMBO);
			Method.doYesInteraction(Tiles.AREA_CRASH_ISLAND, lumdo);
			Data.checkChins = true;
			if (Tiles.AREA_APE_ATOLL.contains(Players.getLocal().getLocation()) && Data.usingGreegree) {
				Method.equipGreegree();
				Data.runCheck = true;

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
				return;
			} else if (Tiles.AREA_APE_ATOLL.contains(Players.getLocal().getLocation()) && !Data.usingGreegree) {
				Method.checkRun();
				return;
			}
			if (Tiles.AREA_APE_ATOLL_DUNGEON.contains(Players.getLocal().getLocation())) {
				Data.logWalkCode = 0;

				Method.checkRenewal();
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
					while (Players.getLocal().isMoving()) {
						Time.sleep(50);
					}
					return;
				} else {
					Walking.findPath(Tiles.TILE_CHIN_1).traverse();
					while (Players.getLocal().isMoving()) {
						Time.sleep(50);
					}
				}
				if (Method.tileContainsTwoOrMore(Tiles.TILE_CHIN_1)) {
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
						return;
					}
					if (Method.tileContainsTwoOrMore(Tiles.TILE_CHIN_2)) {
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
							return;
						}
						if (Method.areaContainsTwoOrMore()) {
							Method.changeWorlds();
						} else {
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
					} else {
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
				} else {
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
