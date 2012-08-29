package EpicsChins.util.tasks;

import EpicsChins.util.Data;
import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.tab.Equipment;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.bot.Context;

/**
 * User: Epics
 * Date: 8/28/12
 * Time: 8:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class Checks extends Strategy implements Runnable {

	@Override
	public void run() {
		Item greegree = Inventory.getItem(Data.GREEGREE_IDS);

		Context.get().getActiveScript().log.info("Running checks!");
		Context.get().getActiveScript().log.severe("id: " + Equipment.getItem(14).getId());

		if (greegree == null) {
			greegree = Equipment.getItem(15);
			Context.get().getActiveScript().log.severe("NEW GREEGREE VALUE: " + greegree.getId());
		}
		if (Equipment.getItem(10034) != null && Data.checkChins) {
			Data.chinNumber = Equipment.getItem(10034).getStackSize();
			Context.get().getActiveScript().log.info("Number of chins equipped: " + String.valueOf(Data.chinNumber));
			if (Data.chinNumber != 0) {
				Data.checkChins = false;
			}
		} else {
			if (Equipment.getItem(3).getId() == greegree.getId() && Data.checkChins) {
				Context.get().getActiveScript().log.info("Greegree is equipped, counting chins in inventory...");
				Data.chinNumber = Inventory.getItem(10034).getStackSize();
				Context.get().getActiveScript().log.info("Number of chins in inventory: " + String.valueOf(Data.chinNumber));
				if (Data.chinNumber != 0) {
					Data.checkChins = false;
				}
			}
		}

		if (Tabs.ATTACK.open()) {
			if (Settings.get(172) != 0) {
				Context.get().getActiveScript().log.info("Auto retaliate not on, setting");
				Widgets.get(884).getChild(13).click(true);
				Timer t = new Timer(2500);
				while (Settings.get(172) != 0 && t.isRunning()) {
					Time.sleep(50);
				}
			}
		}
		Data.runCheck = false;
	}

	@Override
	public boolean validate() {
		return Data.runCheck && Data.START_SCRIPT && Game.isLoggedIn();
	}
}
