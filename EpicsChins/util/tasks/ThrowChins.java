package EpicsChins.util.tasks;

import EpicsChins.util.Data;
import EpicsChins.util.Method;
import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.bot.Context;

/**
 * User: Epics
 * Date: 8/28/12
 * Time: 8:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class ThrowChins extends Strategy implements Runnable {
	private NPC monkeyZombie;

	@Override
	public void run() {
		if (Data.logAttackCode == 0) {
			Context.get().getActiveScript().log.info("Running attack code");
			Data.logAttackCode++;
		}

		Method.attackMonkey(monkeyZombie);
	}

	@Override
	public boolean validate() {
		int prayPotCountData = 0;

		for (Item y : Inventory.getItems()) {
			for (int x : Data.POT_PRAYER) {
				if (y.getId() == x) {
					prayPotCountData++;
				}
			}
		}
		/*Context.get().getActiveScript().log.info("NPC value: " + String.valueOf(monkeyZombie =NPCs.getNearest(Data.ID_NPC_MONKEY_ZOMBIE)));
		Context.get().getActiveScript().log.info("chinNum: " +String.valueOf(Data.chinNumber));
		Context.get().getActiveScript().log.info("atDest: " + String.valueOf(Data.atDestination));
		*/
		return (
				       monkeyZombie =
						       NPCs.getNearest(Data.ID_NPC_MONKEY_ZOMBIE)) != null && Data.chinNumber >= 200 && prayPotCountData > 0 && Data.START_SCRIPT && Game.isLoggedIn() && !Data.runCheck && Data.atDestination;
	}
}
