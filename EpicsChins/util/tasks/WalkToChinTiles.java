package EpicsChins.util.tasks;

import EpicsChins.util.Data;
import EpicsChins.util.Method;
import EpicsChins.util.Tiles;
import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.bot.Context;


/**
 * User: Epics
 * Date: 8/31/12
 * Time: 1:30 AM
 */
public class WalkToChinTiles extends Strategy implements Runnable {
	@Override
	public void run() {

		if (Data.logWalkingToChinCode == 0) {
			Context.get().getActiveScript().log.info("Running WalkToChinTiles code");
			Data.logWalkingToChinCode++;
		}

		if (Data.walkToArea1) {
			Method.chinRunState();

			if (!Tiles.TILE_CHIN_1.equals(Players.getLocal().getLocation()) && Calculations.distanceTo(Tiles.PATH_TO_CHIN_TILE_1[22]) > 5) {
				Method.walkPath(Tiles.PATH_TO_CHIN_TILE_1);
				Method.chinRunState();
			}

			if (!Tiles.TILE_CHIN_1.equals(Players.getLocal().getLocation()) && Calculations.distanceTo(Tiles.PATH_TO_CHIN_TILE_1[22]) <= 5) {
				Tiles.PATH_TO_CHIN_TILE_1[22].interact("Walk here");
			}

			if (!Tiles.TILE_CHIN_1.equals(Players.getLocal().getLocation()) && Tiles.PATH_TO_CHIN_TILE_1[22].equals(Players.getLocal().getLocation())) {
				Walking.findPath(Tiles.TILE_CHIN_1).traverse();
			}

			if (Tiles.AREA_CHIN_1.contains(Players.getLocal().getLocation())) {
				Context.get().getActiveScript().log.info("B");

				if (Tiles.AREA_CHECK_TRAPS_1.contains(Players.getLocal().getLocation())) {
					Context.get().getActiveScript().log.info("C");
					if (Tiles.TRAP_TILE_LIST_AREA_1.contains(Players.getLocal().getLocation())) {
						Context.get().getActiveScript().log.info("D");
						Tile
								nonTrapTile =
								new Tile(Players.getLocal().getLocation().getX() + 1,
										        Players.getLocal().getLocation().getY() + 1,
										        0);

						Walking.findPath(nonTrapTile).traverse();
					}
				}
				Data.walkToArea1 = false;
			}

		} else if (Data.walkToArea2) {
			Context.get().getActiveScript().log.info("GG");

			if (!Tiles.TILE_CHIN_2.equals(Players.getLocal().getLocation()) && Calculations.distanceTo(Tiles.PATH_TO_CHIN_TILE_2[2]) > 5) {// && Walking.walk(nextTile)) {
				Method.walkPath(Tiles.PATH_TO_CHIN_TILE_2);
				Method.chinRunState();
			}

			if (!Tiles.TILE_CHIN_2.equals(Players.getLocal().getLocation()) && Calculations.distanceTo(Tiles.PATH_TO_CHIN_TILE_2[2]) <= 5) {
				Tiles.PATH_TO_CHIN_TILE_2[2].interact("Walk here");
			}

			if (!Tiles.TILE_CHIN_2.equals(Players.getLocal().getLocation()) && Tiles.PATH_TO_CHIN_TILE_2[2].equals(Players.getLocal().getLocation())) {
				Walking.findPath(Tiles.TILE_CHIN_2).traverse();
			}

			if (Tiles.AREA_CHIN_2.contains(Players.getLocal().getLocation())) {

				if (Tiles.AREA_CHECK_TRAPS_2.contains(Players.getLocal().getLocation())) {
					if (Tiles.TRAP_TILE_LIST_AREA_2.contains(Players.getLocal().getLocation())) {
						Tile
								nonTrapTile =
								new Tile(Players.getLocal().getLocation().getX() + 1,
										        Players.getLocal().getLocation().getY() + 1,
										        0);

						Walking.findPath(nonTrapTile).traverse();
					}
				}
				Data.walkToArea2 = false;
				Data.logWalkingToChinCode = 0;
			}

		} else if (Data.walkToArea3) {

			if (!Tiles.TILE_CHIN_3.equals(Players.getLocal().getLocation()) && Calculations.distanceTo(Tiles.PATH_TO_CHIN_TILE_3[6]) > 5) {// && Walking.walk(nextTile)) {
				Method.walkPath(Tiles.PATH_TO_CHIN_TILE_3);
			}

			if (!Tiles.TILE_CHIN_3.equals(Players.getLocal().getLocation()) && Calculations.distanceTo(Tiles.PATH_TO_CHIN_TILE_3[6]) <= 5) {
				Tiles.PATH_TO_CHIN_TILE_3[6].interact("Walk here");
			}

			if (!Tiles.TILE_CHIN_3.equals(Players.getLocal().getLocation()) && Tiles.PATH_TO_CHIN_TILE_3[6].equals(Players.getLocal().getLocation())) {
				Walking.findPath(Tiles.TILE_CHIN_2).traverse();
			}

			if (Tiles.AREA_CHIN_3_4.contains(Players.getLocal().getLocation())) {

				if (Tiles.AREA_CHECK_TRAPS_3.contains(Players.getLocal().getLocation())) {
					if (Tiles.TRAP_TILE_LIST_AREA_3.contains(Players.getLocal().getLocation())) {
						Tile
								nonTrapTile =
								new Tile(Players.getLocal().getLocation().getX() + 1,
										        Players.getLocal().getLocation().getY() + 1,
										        0);

						Walking.findPath(nonTrapTile).traverse();
					}
				}
				Data.walkToArea3 = false;
				Data.logWalkingToChinCode = 0;
			}
		}
	}

	@Override
	public boolean validate() {
		return Game.isLoggedIn() && Tiles.AREA_APE_ATOLL_DUNGEON.contains(Players.getLocal().getLocation()) && !Data.runCheck && Data.START_SCRIPT;// && (Data.walkToArea1 || Data.walkToArea2 || Data.walkToArea3);
	}
}
