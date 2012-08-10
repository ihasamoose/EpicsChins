import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.powerbot.concurrent.Task;
import org.powerbot.concurrent.strategy.Condition;
import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.ActiveScript;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.widget.Widget;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import org.powerbot.game.bot.event.MessageEvent;
import org.powerbot.game.bot.event.listener.MessageListener;
import org.powerbot.game.bot.event.listener.PaintListener;

/**
* AutoChinnerPro
* @author Harry Andreas
*/
@Manifest(
        name = "AutoChinnerPro ",
        version = 1.33,
        description = "Drinks potions and teleports when out of supplies",
        authors = { "RuneCore" }
        )
public class AutoChinnerPro extends ActiveScript implements MessageListener, PaintListener {
   
    /**
     * Features;
     *  ~ Supports;
     *      Prayer Renewals Potions/Flasks
     *      Prayer Potions/Flasks
     *      Super Prayer potions/Flasks
     *      Ranging Potions/Flasks
     *      Extreme Ranging Potions/Flasks
     *      Anti Poison Drinking when Poisoned
     *  ~ Teleports when out of prayer potions
     *  ~ Attacks Skeletons when not being attacked
     *  ~ Walks back to the original Tile
     *  ~ 3 Strong AntiBan Stratergies
     */
   
    /**
     * IDs of Prayer potions
     * From 1 to 4
     */
    private static final int[] PRAYER_POTION_IDS = new int[] {
        23253, 23251, 23249, 23247, 23245, 23243, 143, 141, 139, 2434,
        15331, 15330, 15329, 15328, 23530, 23529, 23528, 23527, 23526, 23525
    };
   
    /**
     * IDs of Prayer renewal potions
     * From 1 to 4
     */
    private static final int[] PRAYER_RENEWAL_POTION_IDS = new int[] {
        23619, 23617, 23615, 23613, 23611, 23609, 21636, 21634, 21632, 21630
    };
   
    /**
     * IDs of Ranging potions
     * From 1 to 4
     */
    private static final int[] RANGING_POTION_IDS = new int[] {
        23524, 23523, 23522, 23521, 23520, 23519, 15327, 15326, 15325, 15324,
        23313, 23311, 23309, 23307, 23305, 23303, 173, 171, 169, 2444
    };
   
    /**
     * IDS of Magic potions
     */
    private static final int[] MAGIC_POTION_IDS = new int[] {
        15323, 15322, 15321, 15320, 23518, 23517, 23516, 23515, 23514, 23513,
        23433, 23431, 23429, 23427, 23425, 23423, 3046, 3044, 3042, 3040
    };
   
    /**
     * Skeleton IDS to attack
     */
    private static final int[] SKELETON_IDS = new  int[] {
        1471
    };
   
    /**
     * ID of the teletab to use when out of supplies
     */
    private static final int[] TELETABS = new int[] {
        8007, 8008, 8009, 8010, 8011, 8013
    };
   
    /**
     * IDs of food
     */
    private static final int[] FOOD_IDS = new int[] {
        379, 361, 7942, 385, 391, 15272, 15266, 7218, 7220
    };
   
    /**
     * Is a renewal needed to be drunk?
     */
    private boolean renewalNeeded = Boolean.FALSE;
   
    /**
     * Information fields for the paint
     */
    private int[] startingXps = new int[3];
    private int[] startingLevels = new int[3];
    private long startTime;

    /**
     * Formatter for XP gained
     */
    private static final NumberFormat formatter = new DecimalFormat("###,###,###");
   
    /**
     * The Tile to return to
     */
    private Tile returnToTile;
   
    /**
     * Sets up the bot
     */
    @Override
    protected void setup() {
        returnToTile = Players.getLocal().getLocation();
        startTime = System.currentTimeMillis();
        startingXps[0] = Skills.getExperience(Skills.RANGE);
        startingXps[1] = Skills.getExperience(Skills.CONSTITUTION);
        startingXps[2] = Skills.getExperience(Skills.MAGIC);
        startingLevels[0] = Skills.getRealLevel(Skills.RANGE);
        startingLevels[1] = Skills.getRealLevel(Skills.CONSTITUTION);
        startingLevels[2] = Skills.getRealLevel(Skills.MAGIC);
        provide(new EatFood());
        provide(new DrinkPrayerPotion());
        provide(new DrinkRangingPotion());
        provide(new DrinkRenewalPotion());
        provide(new DrinkMagicPotion());
        provide(new DrinkAntiPoisonPotion());
        provide(new TeleportOut());
        provide(new AttackBack());
        provide(new WalkBack());
        provide(new AntiBan());
    }
   
    @Override
    public void onStop() {
        try {
            int currentRange = Skills.getRealLevel(Skills.RANGE);
            int currentHP = Skills.getRealLevel(Skills.CONSTITUTION);
            int currentMage = Skills.getRealLevel(Skills.MAGIC);
           
            int gainedRanged = currentRange - startingLevels[0];
            int gainedHP = currentHP - startingLevels[1];
            int gainedMage = currentMage - startingLevels[2];
           
            int gainedRangeXP = (Skills.getExperience(Skills.RANGE) - startingXps[0]);
            int gainedMageXP = (Skills.getExperience(Skills.MAGIC) - startingXps[2]);
            int gainedHPXP = (Skills.getExperience(Skills.CONSTITUTION) - startingXps[1]);
            URL tracker = new URL("http://scripts.runecore.org/scripts/submit.php?scriptid=1&rangexpgained="+gainedRangeXP+"&hpgained="+gainedHPXP+"&magegained="+gainedMageXP+"&rnglvlg="+gainedRanged+"&hplvlg="+gainedHP+"&magelvlg="+gainedMage);
            URLConnection connection = tracker.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            InputStream is = connection.getInputStream();
            byte[] input = new byte[is.available()];
            is.read(input);
            String s = new String(input);
            log.info("Server MSG; "+s);
            log.info("Thanks for submitting your progress to RuneCore Scripts!");
        } catch(Exception e) {
            e.printStackTrace();
            log.info("Failed to send tracker info :(");
        }
    }
   
    @Override
    public void messageReceived(MessageEvent arg0) {
        String message = arg0.getMessage().toLowerCase();
        if(message.contains("run out")) {
            renewalNeeded = Boolean.TRUE;
        }
    }

    @Override
    public void onRepaint(Graphics arg0) {
        int currentRange = Skills.getRealLevel(Skills.RANGE);
        int currentHP = Skills.getRealLevel(Skills.CONSTITUTION);
        int currentMage = Skills.getRealLevel(Skills.MAGIC);
       
        int gainedRanged = currentRange - startingLevels[0];
        int gainedHP = currentHP - startingLevels[1];
        int gainedMage = currentMage - startingLevels[2];
       
        int gainedRangeXP = (Skills.getExperience(Skills.RANGE) - startingXps[0]);
        int gainedMageXP = (Skills.getExperience(Skills.MAGIC) - startingXps[2]);
        int gainedHPXP = (Skills.getExperience(Skills.CONSTITUTION) - startingXps[1]);
       
        long elapsedTime = System.currentTimeMillis() - startTime;
         ((Graphics2D) arg0).setRenderingHints(new RenderingHints(
                 RenderingHints.KEY_ANTIALIASING,
                 RenderingHints.VALUE_ANTIALIAS_ON));
       
        arg0.setColor(Color.ORANGE);
        Point p = Mouse.getLocation();
        if(p != null) {
            arg0.drawLine((int)p.getX() - 5, (int)p.getY(), (int)p.getX() + 5, (int)p.getY());
            arg0.drawLine((int)p.getX() , (int)p.getY() - 5, (int)p.getX() , (int)p.getY() + 5);
        }
   
        arg0.drawString("AutoChinnerPro v1.32 by RuneCore", 270, 15);
        arg0.drawString("Range EXP Gained : "+formatter.format(gainedRangeXP)+" "+currentRange+"/"+startingLevels[0]+" ("+gainedRanged+")", 270, 30);
        arg0.drawString("Magic EXP Gained : "+formatter.format(gainedMageXP)+" "+currentMage+"/"+startingLevels[2]+" ("+gainedMage+")", 270, 45);
        arg0.drawString("HP EXP Gained : "+formatter.format(gainedHPXP)+" "+currentHP+"/"+startingLevels[1]+" ("+gainedHP+")", 270, 60);
        arg0.drawString("Running For: "+Time.format(elapsedTime), 270, 75);
    }
   
    /**
     * Moves the mouse to a random position after clicking
     */
    private void moveMouseRandomly() {
        int x = Random.nextInt(0, 700);
        int y = Random.nextInt(0, 500);
        Mouse.move(x, y);
    }
   

    public static int getItemToUse(int[] array) {
        for(int i : array) {
            if(Inventory.getCount(i) > 0) {
                return i;
            }
        }
        return -1;
    }
   
    /**
     * Waits for a condition
     * @param c The condition to wait for
     */
    private static void waitWhile(Condition c) {
        int timeout = 0;
        while(c.validate() && timeout < 10) {
            timeout++;
            Time.sleep(Random.nextInt(700, 1000));
        }
    }
   
    public Condition playerMoving() {
        return new Condition() {
            @Override
            public boolean validate() {
                return Players.getLocal().isMoving();
            }
        };
    }
   
    private final class AntiBan extends Strategy implements Task {
       
        @Override
        public void run() {
            int task = Random.nextInt(0, 5);
            log.info("Running AntiBan Task: "+task);
            switch(task) {
            case 0:
            case 2:
                Camera.setAngle(Random.nextInt(0, 360));
                break;
            case 1:
            case 3:
                Camera.setPitch(Random.nextInt(0, 100));
                break;
            default:
                int x = Random.nextInt(0, 700);
                int y = Random.nextInt(0, 500);
                Mouse.move(x, y);
                break;
            }
        }
       
        @Override
        public boolean validate() {
            return sucessful(Random.nextInt(6, 8));
        }
       
        public boolean sucessful(int times) {
            for(int i = 0; i < times; i++) {
                if(!Random.nextBoolean()) {
                    return false;
                }
            }
            return true;
        }
       
    }
   
    private final class WalkBack extends Strategy implements Task {

        @Override
        public void run() {
            log.info("Walking back to original tile");
            if(walkTileMM(returnToTile, 0)) {
                moveMouseRandomly();
                Time.sleep(Random.nextInt(700, 900));
                waitWhile(playerMoving());
            }
        }
       
        @Override
        public boolean validate() {
            return !Players.getLocal().getLocation().equals(returnToTile) && returnToTile.validate();
        }
       
        public boolean walkTileMM(Tile tile, int rnd) {
            float angle = angleTo(tile) - Camera.getAngleTo(0);
            float distance = distanceTo(tile);
            if (distance > 18)
                distance = 18;
            angle = (float) (angle * Math.PI / 180);
            int x = 627, y = 85;
            int dx = (int) (4 * (distance + Random.nextGaussian(0, rnd, 1)) * Math
                    .cos(angle));
            int dy = (int) (4 * (distance + Random.nextGaussian(0, rnd, 1)) * Math
                    .sin(angle));
            return Mouse.click(x + dx, y - dy, true);
        }

        public float distanceTo(Tile tile) {
            return (float) Calculations.distance(Players.getLocal().getLocation(),
                    tile);
        }

        public int angleTo(Tile tile) {
            final double ydif = tile.getY() - Players.getLocal().getLocation().getY();
            final double xdif = tile.getX() - Players.getLocal().getLocation().getX();
            return (int) (Math.atan2(ydif, xdif) * 180 / Math.PI);
        }
       
       
    };
   
    private final class EatFood extends Strategy implements Task {

        @Override
        public void run() {
            int foodToUse = getItemToUse(FOOD_IDS);
            if(foodToUse == -1) {
                log.info("Food not found in inventory!");
                return;
            }
            for(Item i : Inventory.getItems()) {
                if(i.getWidgetChild().validate()) {
                    if(i.getId() == foodToUse) {
                        if(i.getWidgetChild().interact("Eat")) {
                            Time.sleep(Random.nextInt(400, 550));
                            moveMouseRandomly();
                        }
                    }
                }
            }
        }
       
       
        @Override
        public boolean validate() {
            Widget w = Widgets.get(748);
            int hp = Integer.parseInt(w.getChild(8).getText());
            int fullHp = Skills.getRealLevel(Skills.CONSTITUTION) * 10;
            int half = fullHp / 2;
            return hp <= half;
        }
       
    }
   
    private final class AttackBack extends Strategy implements Task {

        @Override
        public void run() {
            NPC nearest = NPCs.getNearest(combatNPC);
            if(nearest != null) {
                if(nearest.interact("Attack")) {
                    moveMouseRandomly();
                    Time.sleep(Random.nextInt(1500, 2000));
                }
            }
        }
       
        private Filter<NPC> combatNPC = new Filter<NPC>() {
            @Override
            public boolean accept(NPC n) {
                for(int i : SKELETON_IDS) {
                    if(i == n.getId()) {
                        return n.isOnScreen() && n.validate();
                    }
                }
                return false;
            }
           
        };
       
        @Override
        public boolean validate() {
            return !Players.getLocal().isInCombat() && Players.getLocal().getInteracting() == null;
        }
       
    }

    private final class DrinkAntiPoisonPotion extends Strategy implements Task {
       
        @Override
        public void run() {
            for(Item i : Inventory.getItems()) {
                if(i.getWidgetChild().validate() && i.getId() != -1) {
                    if(i.getName().toLowerCase().contains("anti")) {
                        if(i.getWidgetChild().click(true)) {
                            moveMouseRandomly();
                            Time.sleep(Random.nextInt(1500, 2000));
                        }
                    }
                }
            }
        }
       
        @Override
        public boolean validate() {
            return isPoisoned();
        }
       
        public boolean isPoisoned() {
            Widget widget = Widgets.get(748);
            if(widget.validate()) {
                WidgetChild child = widget.getChild(4);
                if(child.validate()) {
                    return child.getTextureId() != 1208;
                }
            }
            return false;
        }
       
    }
   
   
    private final class DrinkPrayerPotion extends Strategy implements Task {
        @Override
        public void run() {
            for(int i : PRAYER_POTION_IDS) {
                if(Inventory.getCount(i) > 0) {
                    Item potion = Inventory.getItem(i);
                    if(potion != null) {
                        WidgetChild potionWidget = potion.getWidgetChild();
                        if(potionWidget.validate()) {
                            log.info("Drinking "+potion.getName());
                            if(potionWidget.click(true)) {
                                moveMouseRandomly();
                                Time.sleep(Random.nextInt(1500, 2500));
                                return;
                            }
                        }
                    }
                }
            }
        }
       
        public boolean validate() {
            return getPrayerPoints() <= Random.nextInt(200, 260);
        }
       
        private int getPrayerPoints() {
            Widget widget = Widgets.get(749);
            if(widget.validate()) {
                WidgetChild child = widget.getChild(6);
                return Integer.parseInt(child.getText());
            }
            return 0;
        }
       
    }
   
    private final class DrinkMagicPotion extends Strategy implements Task {
        @Override
        public void run() {
            for(int i : MAGIC_POTION_IDS) {
                if(Inventory.getCount(i) > 0) {
                    Item potion = Inventory.getItem(i);
                    if(potion != null) {
                        WidgetChild potionWidget = potion.getWidgetChild();
                        if(potionWidget.validate()) {
                            log.info("Drinking "+potion.getName());
                            if(potionWidget.click(true)) {
                                moveMouseRandomly();
                                Time.sleep(Random.nextInt(1500, 2500));
                                return;
                            }
                        }
                    }
                }
            }
        }
       
        public boolean validate() {
            return Skills.getLevel(Skills.MAGIC) - Skills.getRealLevel(Skills.MAGIC) <= Random.nextInt(1, 3);
        }
       
    }
   
    private final class DrinkRangingPotion extends Strategy implements Task {
        @Override
        public void run() {
            for(int i : RANGING_POTION_IDS) {
                if(Inventory.getCount(i) > 0) {
                    Item potion = Inventory.getItem(i);
                    if(potion != null) {
                        WidgetChild potionWidget = potion.getWidgetChild();
                        if(potionWidget.validate()) {
                            log.info("Drinking "+potion.getName());
                            if(potionWidget.click(true)) {
                                moveMouseRandomly();
                                Time.sleep(Random.nextInt(1500, 2500));
                                return;
                            }
                        }
                    }
                }
            }
        }
       
        public boolean validate() {
            return Skills.getLevel(Skills.RANGE) - Skills.getRealLevel(Skills.RANGE) <= Random.nextInt(3, 6);
        }
       
    }
   
    private final class DrinkRenewalPotion extends Strategy implements Task {
        @Override
        public void run() {
            for(int i : PRAYER_RENEWAL_POTION_IDS) {
                if(Inventory.getCount(i) > 0) {
                    Item potion = Inventory.getItem(i);
                    if(potion != null) {
                        WidgetChild potionWidget = potion.getWidgetChild();
                        if(potionWidget.validate()) {
                            log.info("Drinking "+potion.getName());
                            if(potionWidget.click(true)) {
                                moveMouseRandomly();
                                renewalNeeded = Boolean.FALSE;
                                Time.sleep(Random.nextInt(1500, 2500));
                                return;
                            }
                        }
                    }
                }
            }
        }
       
        public boolean validate() {
            return renewalNeeded;
        }
       
    }
   
    private final class TeleportOut extends Strategy implements Task {
       
        @Override
        public void run() {
            int tabToUse =  getItemToUse(TELETABS);
            if(tabToUse != -1) {
                for(Item i : Inventory.getItems()) {
                    if(i != null && i.getId() != -1) {
                        if(i.getWidgetChild().validate()) {
                            if(i.getId() == tabToUse) {
                                if(i.getWidgetChild().click(true)) {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
       
        public boolean validate() {
            Widget w = Widgets.get(748);
            int hp = Integer.parseInt(w.getChild(8).getText());
            int fullHp = Skills.getRealLevel(Skills.CONSTITUTION) * 10;
            int half = fullHp / 2;
            boolean halfOrBelow =  hp <= half;
            if(halfOrBelow) {
                int foodId = getItemToUse(FOOD_IDS);
                if(foodId == -1) {
                    log.info("Out of food, and low HP teleing out!");
                    return true;
                }
            }
            if(halfOrBelow) {
                if(Players.getLocal().getPrayerIcon() == -1) {
                    log.info("No prayer on! Teleporting out");
                    return true;
                }
            }
            int count = 0;
            for(int i : PRAYER_POTION_IDS) {
                count += Inventory.getCount(i);
            }
            return count == 0;
        }
       
    }

}