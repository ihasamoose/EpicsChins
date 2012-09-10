package EpicsChins.util.tasks;

import EpicsChins.util.Data;
import EpicsChins.util.Method;
import EpicsChins.util.Tiles;
import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.bot.Context;

/**
 * User: Epics
 * Date: 8/31/12
 * Time: 12:30 AM
 */
public class CheckSpots extends Strategy implements Runnable {
	@Override
	public void run() {

		if (Data.logCheckSpotsCode == 0) {
			Context.get().getActiveScript().log.info("Running CheckSpots code");
			Data.logCheckSpotsCode++;
		}

		if (Data.tempCheckRenewal) {
			Method.checkRenewal();
			Timer t = new Timer(5000);
			while (Data.tempCheckRenewal && t.isRunning()) {
				Time.sleep(50);
			}
		}
		Data.tempCheckRenewal = false;

		if (!Tiles.AREA_CHIN_1.contains(Players.getLocal().getLocation()) && !Tiles.AREA_CHIN_2.contains(Players.getLocal().getLocation()) && !Tiles.AREA_CHIN_3_4.contains(Players.getLocal().getLocation()) && !Data.walkToArea1 && !Data.walkToArea2 && !Data.walkToArea3) {
			Data.walkToArea1 = true;

		}

		if (Tiles.AREA_CHIN_1.contains(Players.getLocal().getLocation())) {
			Data.walkToArea1 = false;

		} else if (Tiles.AREA_CHIN_2.contains(Players.getLocal().getLocation())) {
			Data.walkToArea2 = false;

		} else if (Tiles.AREA_CHIN_3_4.contains(Players.getLocal().getLocation())) {
			Data.walkToArea3 = false;
		}

		if (Tiles.AREA_CHIN_1.contains(Players.getLocal().getLocation()) && Method.areaContainsTwoOrMore(Tiles.AREA_CHIN_1)) {
			Data.walkToArea2 = true;

		} else if (Tiles.AREA_CHIN_2.contains(Players.getLocal().getLocation()) && Method.areaContainsTwoOrMore(Tiles.AREA_CHIN_2)) {
			Data.walkToArea3 = true;

		} else if (Tiles.AREA_CHIN_3_4.contains(Players.getLocal().getLocation()) && Method.areaContainsTwoOrMore(Tiles.AREA_CHIN_3_4)) {
			Method.changeWorlds();
			Data.logCheckSpotsCode = 0;
		}

		if (Tiles.AREA_CHIN_1.contains(Players.getLocal().getLocation()) && !Method.areaContainsTwoOrMore(Tiles.AREA_CHIN_1)) {
			Context.get().getActiveScript().log.info("ASDF");

			if (Walking.findPath(Tiles.TILE_CHIN_1).traverse()) {
				Method.runState();

				if (Tiles.TILE_CHIN_1.validate() && Tiles.TILE_CHIN_1.isOnScreen()) {
					Tiles.TILE_CHIN_1.interact("Walk here");

				} else if (Tiles.TILE_CHIN_1.validate() && !Tiles.TILE_CHIN_1.isOnScreen()) {
					Camera.turnTo(Tiles.TILE_CHIN_1);

				} else if (!Tiles.TILE_CHIN_1.validate()) {
					Context.get().getActiveScript().log.severe("TILE DIDN'T VALIDATE");
				}
				Data.logCheckSpotsCode = 0;
			}
			if (Tiles.TILE_CHIN_1.equals(Players.getLocal().getLocation())) {
				Data.atDestination = true;
			}
		} else if (Tiles.AREA_CHIN_2.contains(Players.getLocal().getLocation()) && !Method.areaContainsTwoOrMore(Tiles.AREA_CHIN_2)) {
			Context.get().getActiveScript().log.info("DFAWR");

			if (Walking.findPath(Tiles.TILE_CHIN_2).traverse()) {
				Context.get().getActiveScript().log.info("TRQES");

				Method.runState();

				if (Tiles.TILE_CHIN_2.validate() && Tiles.TILE_CHIN_2.isOnScreen()) {
					Tiles.TILE_CHIN_2.interact("Walk here");

				} else if (Tiles.TILE_CHIN_2.validate() && !Tiles.TILE_CHIN_2.isOnScreen()) {
					Camera.turnTo(Tiles.TILE_CHIN_2);

				} else if (!Tiles.TILE_CHIN_2.validate()) {
					Context.get().getActiveScript().log.severe("TILE DIDN'T VALIDATE");
				}
				Data.logCheckSpotsCode = 0;
			}
			if (Tiles.TILE_CHIN_2.equals(Players.getLocal().getLocation())) {
				Data.atDestination = true;
			}

		} else if (Tiles.AREA_CHIN_3_4.contains(Players.getLocal().getLocation()) && !Method.areaContainsTwoOrMore(Tiles.AREA_CHIN_3_4)) {

			if (Walking.findPath(Tiles.TILE_CHIN_3).traverse()) {
				Method.runState();

				if (Tiles.TILE_CHIN_3.validate() && Tiles.TILE_CHIN_3.isOnScreen()) {
					Tiles.TILE_CHIN_3.interact("Walk here");

				} else if (Tiles.TILE_CHIN_3.validate() && !Tiles.TILE_CHIN_3.isOnScreen()) {
					Camera.turnTo(Tiles.TILE_CHIN_3);

				} else if (!Tiles.TILE_CHIN_3.validate()) {
					Context.get().getActiveScript().log.severe("TILE DIDN'T VALIDATE");
				}
			}
			if (Tiles.TILE_CHIN_3.equals(Players.getLocal().getLocation())) {
				Data.atDestination = true;
			}
			Data.logCheckSpotsCode = 0;
		}
	}

	@Override
	public boolean validate() {
		return  Game.isLoggedIn() && Tiles.AREA_APE_ATOLL_DUNGEON.contains(Players.getLocal().getLocation()) && !Data.runCheck && Data.START_SCRIPT;
	}
}
