package EpicsChins.util.tasks;

import EpicsChins.util.Data;
import EpicsChins.util.GUI;
import EpicsChins.util.Method;
import EpicsChins.util.Tiles;
import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.bot.Context;

/**
 * User: Epics
 * Date: 8/28/12
 * Time: 8:07 PM
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
			if (Method.prayerIsLow()) {
				Method.chargePrayer();
			}

			if (!Method.prayerIsLow()) {
				Method.isInGE();
			}

			Method.checkRun();
			Method.preEat();
			Method.checkNoobTele();


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

			if (daero == null && Tiles.AREA_GNOME_LEVEL_ONE.contains(Players.getLocal().getLocation())) {
				Walking.findPath(Tiles.TILE_DAERO).traverse();
				while (Players.getLocal().isMoving()) {
					Time.sleep(50);
				}
				return;
			}

			Method.doYesInteraction(Tiles.AREA_GNOME_LEVEL_ONE, daero);

			NPC waydar = NPCs.getNearest(Data.ID_NPC_WAYDAR);
			Method.doYesInteraction(Tiles.AREA_BLINDFOLD_ZONE, waydar);

			NPC lumdo = NPCs.getNearest(Data.ID_NPC_LUMBO);
			Method.doYesInteraction(Tiles.AREA_CRASH_ISLAND, lumdo);

			Data.checkChins = true;
			Method.utilizeGreegree();
			Method.interactAndWalkToLadder();
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
		return !Tiles.AREA_APE_ATOLL_DUNGEON.contains(Players.getLocal().getLocation()) && antipoisonData >= 1 && flaskRenewalCountData >= 3 && prayerPotCountData >= 18 && rangingFlaskData >= 3 && Data.chinNumber >= 1500 && Inventory.getCount(Data.TAB_VARROCK) >= 1 && Inventory.getCount(GUI.foodUser) >= 1 && Game.isLoggedIn() && !Data.runCheck && Data.START_SCRIPT;
	}
}
