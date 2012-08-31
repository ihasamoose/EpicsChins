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
		Context.get().getActiveScript().log.info("Running checks!");
		int greegreeTempId = Widgets.get(387, 15).getChildId();

		if (Equipment.getItem(10034) != null && Data.checkChins) {
			Context.get().getActiveScript().log.info("A");
			Data.chinNumber = Equipment.getItem(10034).getStackSize();
			Context.get().getActiveScript().log.info("Number of chins equipped: " + String.valueOf(Data.chinNumber));
			if (Data.chinNumber != 0) {
				Data.checkChins = false;
			}
		} else if(Equipment.getItem(10034) == null){
			if (Data.chinNumber < 2000) {
				Data.chinNumber = Inventory.getItem(10034).getStackSize();
				if(Data.chinNumber < 2000) {
					Data.chinNumber = 0;
					Data.runCheck = false;
				}
			}
			for (int i : Data.GREEGREE_IDS) {
				if (Equipment.getItem(greegreeTempId).getId() == i && Data.checkChins) {
					Context.get().getActiveScript().log.info("Greegree is equipped, counting chins in inventory...");
					Data.chinNumber = Inventory.getItem(10034).getStackSize();
					Context.get().getActiveScript().log.info("Number of chins in inventory: " + String.valueOf(Data.chinNumber));
					if (Data.chinNumber != 0) {
						Data.checkChins = false;
					}
					break;
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
