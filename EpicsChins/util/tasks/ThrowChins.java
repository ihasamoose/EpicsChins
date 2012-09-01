package EpicsChins.util.tasks;

import EpicsChins.util.Data;
import EpicsChins.util.Method;
import EpicsChins.util.Paint;
import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.util.Timer;
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

	@Override
	public void run() {
		if (Data.logAttackCode == 0) {
			Context.get().getActiveScript().log.info("Running attack code");
			Data.logAttackCode++;
		}
		Method.checkPrayer();
		Method.checkRenewal();
		Method.checkAntipoison();
		Method.checkRange();
		Method.setQuickOn();

		NPC monkeyZombie = NPCs.getNearest(Data.ID_NPC_MONKEY_ZOMBIE);

		if (monkeyZombie != null && monkeyZombie.isOnScreen()) {
			if (monkeyZombie.interact("Attack")) {
				Context.get().getActiveScript().log.info("a");
				final Timer t = new Timer(5000);
				while (t.isRunning() && monkeyZombie.interact("Attack")) {
					Context.get().getActiveScript().log.info("Killing monkeys and nothing is needed. Using antiban...");
					Method.antiban();
					Time.sleep(Random.nextInt(700, 800));
				}
				return;
			}

			final Item vial = Inventory.getItem(229);
			if (vial != null) {
				if (vial.getWidgetChild().interact("Drop")) {
					Time.sleep(1500);
				}
			}

			if (Players.getLocal().getAnimation() == Data.ID_CHIN_THROW) {
				Context.get().getActiveScript().log.info("Chins thrown: " + Paint.chinsThrown);
				Paint.chinsThrown++;
				Context.get().getActiveScript().log.info("Chin number:  " + Data.chinNumber);
				Data.chinNumber--;
			}

			if (monkeyZombie.validate() && monkeyZombie.get().getAnimation() == Data.ID_ANIMATION_DEATH_ZOMBIE) {
				Context.get().getActiveScript().log.info("Kill count: " + Paint.zombieKillCount);
				Paint.zombieKillCount++;
			}

			if (Players.getLocal().getInteracting().equals(monkeyZombie)) {
				final Timer t = new Timer(5000);
				while (t.isRunning() && monkeyZombie.interact("Attack")) {
					Time.sleep(50);
				}
			}
		} else {
			if (monkeyZombie == null) {
				Context.get().getActiveScript().log.severe("MONKEY NULL!!");
			} else if (!monkeyZombie.isOnScreen()) {
				Camera.turnTo(monkeyZombie);
			}
		}
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
		return Data.chinNumber >= 200 && prayPotCountData > 0 && Data.START_SCRIPT && Game.isLoggedIn() && !Data.runCheck && Data.atDestination;
	}
}
