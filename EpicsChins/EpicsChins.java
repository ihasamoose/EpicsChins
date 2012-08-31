package EpicsChins;

import EpicsChins.util.Data;
import EpicsChins.util.GUI;
import EpicsChins.util.Paint;
import EpicsChins.util.tasks.Banking;
import EpicsChins.util.tasks.Checks;
import EpicsChins.util.tasks.RunToChins;
import EpicsChins.util.tasks.ThrowChins;
import org.powerbot.game.api.ActiveScript;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.bot.event.listener.PaintListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Logger;

@Manifest(authors = {"Epics"}, name = "Epics Chinner", description = "Kills chins and banks when necessary.",
		         version = 0.1)
public class EpicsChins extends ActiveScript implements PaintListener {

	@Override
	protected void setup() {

		try {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					new GUI();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		Paint.rangedStartExp = Skills.getExperience(Skills.RANGE);
		Paint.hpStartExp = Skills.getExperience(Skills.CONSTITUTION);
		Paint.startTime = System.currentTimeMillis();

		provide(new Checks());
		provide(new RunToChins());
		provide(new ThrowChins());
		provide(new Banking());
	}

	public void onRepaint(Graphics g1) {
		Graphics2D g = (Graphics2D)g1;
		Paint.paintStuff(g);
	}
}