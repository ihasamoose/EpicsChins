package EpicsChins.util;

import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Prayer;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.methods.widget.Lobby;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.interactive.Player;
import org.powerbot.game.api.wrappers.node.Item;
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

	public static void checkPrayer() {
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

	public static void doDrinkRenewal() {
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

	public static void doDrinkRangePotion() {
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

	public static void equipGreegree() {
		Item greegree = Inventory.getItem(Data.GREEGREE_IDS);
		if (greegree != null && greegree.getWidgetChild().click(true)) {
			final int ID = greegree.getId();
			final int COUNT = Inventory.getCount(ID);
			final Timer t = new Timer(2500);
			while (t.isRunning() && Inventory.getCount(ID) == COUNT) {
				Time.sleep(50);
			}
		}
	}

	public static boolean tileContainsTwoOrMore(final Tile tile) {
		Logger.getLogger("EpicsChins").info("Running tileContainsTwoOrMore code");
		final Player[] PLAYERS_ON_TILE = Players.getLoaded(new Filter<Player>() {
			@Override
			public boolean accept(Player t) {
				return t.getLocation().equals(tile);
			}
		});
		return Game.getClientState() != Game.INDEX_MAP_LOADING && PLAYERS_ON_TILE.length >= 2;
	}

	public static boolean areaContainsTwoOrMore() {
		Logger.getLogger("EpicsChins").info("Running areaContainsTwoOrMore code");
		final Player[] PLAYERS_IN_AREA = Players.getLoaded(new Filter<Player>() {
			@Override
			public boolean accept(Player t) {
				return Tiles.AREA_CHIN_3_4.contains(t);
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

	public static void yesInterfaceClicker() {
		final WidgetChild YES_INTERFACE = Widgets.get(1188, 3);

		if (YES_INTERFACE.validate() && YES_INTERFACE.click(true)) {
			final Timer YES_INTERFACE_TIMER = new Timer(2500);
			while (YES_INTERFACE.validate() && YES_INTERFACE_TIMER.isRunning()) {
				Time.sleep(50);
			}
			Time.sleep(2000);
		}
	}

	public static void getSpeedCheck() {
		Timer t = new Timer(2500);

		while (Players.getLocal().getSpeed() != 0 && t.isRunning()) {
			Time.sleep(50);
		}
	}
}
