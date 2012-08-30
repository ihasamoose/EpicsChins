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
		}
	}
}
