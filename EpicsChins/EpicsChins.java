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
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.util.*;
import org.powerbot.game.bot.event.listener.PaintListener;

import javax.swing.*;
import javax.swing.Timer;
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

	public void onRepaint(Graphics g) {
		Graphics2D g = (Graphics2D)g1;
		paintStuff(g);
	}

	public static long startTime;
	public static int zombieKillCount;
	private final static org.powerbot.game.api.util.Timer RUNTIME = new org.powerbot.game.api.util.Timer(0);
	public static int rangedStartExp;
	public static int hpStartExp;
	public static int chinsThrown;
	private final static Color COLOR = new Color(255, 255, 255);
	private final static Font FONT = new Font("Verdana", 0, 10);
	public final static Image IMAGE_1 = GUI.getImage("http://2c1c.net/images/paint.png");

	public static void paintStuff(Graphics g) {
		int mouseX = Mouse.getX();
		int mouseY = Mouse.getY();

		int rangeGainedExp = Skills.getExperience(Skills.RANGE) - rangedStartExp;
		int hpGainedExp = Skills.getExperience(Skills.CONSTITUTION) - hpStartExp;
		int expHr = (int) ((rangeGainedExp) * 3600000D / (System.currentTimeMillis() - startTime));

		if (Data.SHOWPAINT) {
			Graphics2D g1 = (Graphics2D) g;
			g1.drawImage(IMAGE_1, -4, 336, null);
			g1.setFont(FONT);
			g1.setColor(COLOR);
			g1.drawString(RUNTIME.toElapsedString(), 215, 444);
			g1.drawString(String.valueOf(rangeGainedExp), 198, 460);
			g1.drawString(String.valueOf(hpGainedExp), 181, 480);
			g1.drawLine(mouseX, mouseY - 10, mouseX, mouseY + 10);
			g1.drawLine(mouseX - 10, mouseY, mouseX + 10, mouseY);
			g1.drawString(String.valueOf(expHr), 183, 497);
			g1.drawString(String.valueOf(chinsThrown), 351, 443);
			g1.drawString(String.valueOf(zombieKillCount), 359, 461);

			Point centralPoint = Mouse.getLocation();
			Point ovalPoint = new Point((int) centralPoint.getX() - 5, (int) centralPoint.getY() - 5);
			Color malk = new Color(136, 0, 0, 48);
			g1.setColor(malk);
			final long mpt = System.currentTimeMillis() - Mouse.getPressTime();
			if (Mouse.getPressTime() == -1 || mpt >= 200) {
				g1.fillOval((int) ovalPoint.getX(), (int) ovalPoint.getY(), 10, 10);
			}
			if (mpt < 200) {
				g1.drawOval((int) ovalPoint.getX(), (int) ovalPoint.getY(), 10, 10);
			}
		}
	}
}