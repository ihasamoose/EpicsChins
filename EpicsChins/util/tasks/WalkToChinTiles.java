package EpicsChins.util.tasks;

import EpicsChins.util.Data;
import EpicsChins.util.Method;
import EpicsChins.util.Tiles;
import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.util.Time;
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
			Tile nextTile = Walking.newTilePath(Tiles.PATH_TO_CHIN_TILE_1).getNext();

			if (!Tiles.TILE_CHIN_1.equals(Players.getLocal().getLocation()) && Calculations.distanceTo(Tiles.PATH_TO_CHIN_TILE_1[22]) > 5){// && Walking.walk(nextTile)) {
				if (Game.getClientState() == 12) { // game loading
					Time.sleep(200);
				}
				for (int x = Tiles.PATH_TO_CHIN_TILE_1.length - 1; x > 0; x--) {
					final Tile tile = Tiles.PATH_TO_CHIN_TILE_1[x];
					if (tile != null && Calculations.distanceTo(tile) <= 20.0) {
						if (Walking.walk(tile)) {
							Method.checkAntipoison();
							Method.watchHp();
							Method.runState();
							break;
						}
					}
				}
			}

			if (!Tiles.TILE_CHIN_1.equals(Players.getLocal().getLocation()) && Calculations.distanceTo(Tiles.PATH_TO_CHIN_TILE_1[22]) <= 5) {
				Tiles.PATH_TO_CHIN_TILE_1[22].interact("Walk here");

			}

			if (!Tiles.TILE_CHIN_1.equals(Players.getLocal().getLocation()) && Tiles.PATH_TO_CHIN_TILE_1[22].equals(Players.getLocal().getLocation())) {
				Walking.findPath(Tiles.TILE_CHIN_1).traverse();
			}

			if (Tiles.AREA_CHIN_1.contains(Players.getLocal().getLocation())) {//y u never reach me :.(
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
			Tile nextTile = Walking.newTilePath(Tiles.PATH_TO_CHIN_TILE_2).getNext();

			if (!Tiles.TILE_CHIN_2.equals(Players.getLocal().getLocation()) && Calculations.distanceTo(Tiles.PATH_TO_CHIN_TILE_2[2]) > 5){// && Walking.walk(nextTile)) {

				for (int x = Tiles.PATH_TO_CHIN_TILE_2.length - 1; x > 0; x--) {
					final Tile tile = Tiles.PATH_TO_CHIN_TILE_2[x];
					if (tile != null && Calculations.distanceTo(tile) <= 20.0) {
						if (Walking.walk(tile)) {
							Method.checkAntipoison();
							Method.watchHp();
							Method.runState();
							break;
						}
					}
				}
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
			Tile nextTile = Walking.newTilePath(Tiles.PATH_TO_CHIN_TILE_3).getNext();

			if (!Tiles.TILE_CHIN_3.equals(Players.getLocal().getLocation()) && Calculations.distanceTo(Tiles.PATH_TO_CHIN_TILE_3[6]) > 5){// && Walking.walk(nextTile)) {
				for (int x = Tiles.PATH_TO_CHIN_TILE_3.length - 1; x > 0; x--) {
					final Tile tile = Tiles.PATH_TO_CHIN_TILE_3[x];
					if (tile != null && Calculations.distanceTo(tile) <= 20.0) {
						if (Walking.walk(tile)) {
							Method.checkAntipoison();
							Method.watchHp();
							Method.runState();
							break;
						}
					}
				}

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
		return Tiles.AREA_APE_ATOLL_DUNGEON.contains(Players.getLocal().getLocation()) && Game.isLoggedIn() && !Data.runCheck && Data.START_SCRIPT;// && (Data.walkToArea1 || Data.walkToArea2 || Data.walkToArea3);
	}
}
