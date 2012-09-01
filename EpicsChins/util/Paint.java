package EpicsChins.util;

import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.util.Timer;

import java.awt.*;

/**
 * User: Epics
 * Date: 8/28/12
 * Time: 7:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class Paint {

	public static long startTime;
	public static int zombieKillCount;
	private final static Timer RUNTIME = new Timer(0);
	public static int rangedStartExp;
	public static int hpStartExp;
	public static int chinsThrown;
	private final static Color COLOR = new Color(255, 255, 255);
	private final static Font FONT = new Font("Verdana", 0, 10);
	private final static Image IMAGE_1 = GUI.getImage("http://2c1c.net/images/paint.png");

	public static void paintStuff(Graphics2D g) {

		int rangeGainedExp = Skills.getExperience(Skills.RANGE) - rangedStartExp;
		int hpGainedExp = Skills.getExperience(Skills.CONSTITUTION) - hpStartExp;
		int expHr = (int) ((rangeGainedExp) * 3600000D / (System.currentTimeMillis() - startTime));

		if (Data.SHOWPAINT) {
			g.drawImage(IMAGE_1, -4, 336, null);
			g.setFont(FONT);
			g.setColor(COLOR);
			g.drawString(RUNTIME.toElapsedString(), 215, 444);
			g.drawString(String.valueOf(rangeGainedExp), 198, 460);
			g.drawString(String.valueOf(hpGainedExp), 181, 480);
			g.drawString(String.valueOf(expHr), 183, 497);
			g.drawString(String.valueOf(chinsThrown), 351, 443);
			g.drawString(String.valueOf(zombieKillCount), 359, 461);

			Point centralPoint = Mouse.getLocation();
			Point ovalPoint = new Point((int) centralPoint.getX() - 5, (int) centralPoint.getY() - 5);
			final long mpt = System.currentTimeMillis() - Mouse.getPressTime();

			if (Mouse.getPressTime() == -1 || mpt >= 200) {
				Color malk = new Color(0, 136, 0, 50);
				g.setColor(malk);
				g.fillOval((int) ovalPoint.getX() - 2, (int) ovalPoint.getY() - 2, 15, 15);
				Color malk1 = new Color(0, 136, 0, 100);
				g.setColor(malk1);
				g.drawOval((int) ovalPoint.getX() - 2, (int) ovalPoint.getY() - 2, 15, 15);

				if (mpt < 200) {
					Color ralk = new Color(136, 0, 0, 50);
					g.setColor(ralk);
					g.fillOval((int) ovalPoint.getX() - 2, (int) ovalPoint.getY() - 2, 15, 15);
					Color ralk1 = new Color(136, 0, 0, 100);
					g.setColor(ralk1);
					g.drawOval((int) ovalPoint.getX() - 2, (int) ovalPoint.getY() - 2, 15, 15);
				}
			}
		}
	}
}