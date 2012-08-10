import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.naming.Name;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.powerbot.concurrent.Task;
import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.ActiveScript;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Equipment;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Prayer;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.methods.widget.Lobby;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Locatable;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import org.powerbot.game.bot.Context;
import org.powerbot.game.bot.event.MessageEvent;
import org.powerbot.game.bot.event.listener.MessageListener;
import org.powerbot.game.bot.event.listener.PaintListener;

@Manifest(authors = { "Epics" }, name = "Epics Chinner", description = "Kills chins and banks when necessary.", version = 1.0)
public class EpicsChins extends ActiveScript implements PaintListener, MouseListener, MessageListener{
	
	//GUI
	private boolean guiwait = true;
	private GUI gui;
    
	//GUI variables
	public int Food = 0;							//user selected food
	public int[] Antipoison = { 0 };				//user selected Antipoison
	public boolean usingGreegree;
	public int stage = 0;
	
	//Paint variables
	private int onStart = 0;
	private long startTime;
	private int zombieKillCount;
	private final Timer runtime = new Timer(0);
	private int RANGEstartExp;
	private int HPstartExp;
	private int rangegainedExp;
	private int hpgainedExp;
	private int expHour;
	private int chinsThrown;
	 
	 
	private static int mouseX = 0;
	private static int mouseY = 0;
	 
	public boolean showpaint = true;
	
	//Members Worlds array
	public int[] membersWorlds = 	{5,6,9,12,15,18,21,22,23,24,25,26,27,28,31,32,36,39,40,42,44,45,46,48,49,51,
									52,53,54,56,58,59,60,62,63,64,65,66,67,68,69,70,71,72,73,74,76,77,78,79,82,
									83,84,85,86,87,88,89,91,92,96,97,99,100,103,104,105,114,115,116,117,119,123,
									124,137,138,139};
	
	//Path details
	public final Tile[] ladderToSpotOne =			{new Tile(2764,9103,0), new Tile(2810,9110,0), 
													new Tile(2810,9127,0),
													new Tile(2740,9137,0), new Tile(2714,9129,0)};
	
	public final Tile[] spotOneToSpotTwo = 			{new Tile(2714,9129,0), new Tile(2719,9129,0),
													new Tile(2740,9128,0),
													new Tile(2744,9125,0)};
	
	public final Tile[] spotTwoToSpotThree =		{new Tile(2743,9120,0), new Tile(2738,9120,0),
													new Tile(2736,9121,0)};
	
	public final Tile[] spotThreeToSpotFour = 		{new Tile(2705,9113,0), new Tile(2705,9111,0)};
	Area grandExchange	 	= new Area(new Tile(3192, 3512, 0), new Tile(3142, 3471, 0));
	Area daeroZone			= new Area(new Tile(2474, 3493, 1), new Tile(2490, 3483, 1));
	Area waydarZone			= new Area(new Tile(2642, 4525, 0), new Tile(2652, 4515, 0));
	Area lumdoZone			= new Area(new Tile(2896, 2730, 0), new Tile(2887, 2717, 0));
	Area treeDoorInsideTile	= new Area(new Tile(2896, 2730, 0), new Tile(2887, 2717, 0));
	Area spotThreeToSpotFourSq= new Area(new Tile(2709, 9116, 0), new Tile(2701, 9111, 0));
	Tile spiritMidTile 		= new Tile(2542, 3169, 0);
	Tile spiritEndTile 		= new Tile(2462, 3444, 0);
	Tile apeStart			= new Tile(2802, 2707, 0);
	Tile gnomeLadderBottom	= new Tile(2466, 3494, 0);
	Tile gnomeLadderMid		= new Tile(2466, 3994, 1);
	Tile apeLadderTop		= new Tile(2764, 2703, 0);
	Tile apeLadderBottom	= new Tile(2764, 9103, 0);
	Tile chinTile1 			= new Tile(2715, 9127, 0);
	Tile chinTile2 			= new Tile(2746, 9122, 0);
	Tile chinTile3			= new Tile(2709, 9116, 0);
	Tile chinTile4			= new Tile(2701, 9111, 0);
	final Tile[] randomChinArray = {chinTile1,chinTile2,chinTile3,chinTile4};
	Tile randomChinTile = randomChinArray[Random.nextInt(0, randomChinArray.length)];

	//Greegree IDs
	private final static int monkey_greegree = 4031; 
	private final static int[] greegree = 	{monkey_greegree,4024,
											4025,40256,4027,
											4028,4029, 4030};
	
	//Potion IDs
	private final static int[] ranging_flask = {23303,23305,23307,23309,23311,23313};
	private final static int[] prayer_pot = {2434,139,141,143};
	private final static int prayer_pot_four_dose = 2434;
	private final static int[] prayer_renewal_flask = {23609,23611,23613,23615,23617,23619};
	private final static int[] antipoison = {23315,23317,23319,23321,23323,23325, 	//antipoison FLASK
											23579,23581,23583,23585,23587,23589, 	//antipoison FLASK PLUS
											23591,23593,23595,23597,23599,23601, 	//antipoison FLASK PLUS PLUS
											23327,23329,23331,23333,23335,23337,	//antipoison FLASK SUPER 
											11433,11435,							//antipoison MIX
											2446,175,177,179,						//antipoison POT
											5943,5945,5947,5949,					//antipoison POT PLUS
											5952,5954,5956,5958,					//antipoison POT PLUS PLUS
											2448,181,183,185,						//antipoison POT SUPER
											20879									//antipoison elixir
											};
	
	public int[] antiPoisonSuperFlask		= {23327,23329,23331,23333,23335,23337};
	public int[] antiPoisonPlusPlusFlask 	= {23591,23593,23595,23597,23599,23601};
	public int[] antiPoisonPlusFlask 		= {23579,23581,23583,23585,23587,23589};
	public int[] antiPoisonFlask			= {23315,23317,23319,23321,23323,23325};
	public int[] antiPoisonMix				= {11433,11435};
	public int[] antiPoisonSuperPot			= {2448,181,183,185};
	public int[] antiPoisonPlusPlusPot		= {5952,5954,5956,5958};
	public int[] antiPoisonPlusPot			= {5943,5945,5947,5949};
	public int[] antiPoisonPot				= {2446,175,177,179};
	public int[] antiPoisonElixir 			= {20879};
	
	
	//Tab IDs 
	private final static int varrock_tab = 8007;
	private final static int lumbridge_tab = 8008;
	private final static int falador_tab = 8009;
	private final static int camelot_tab = 8010;
	private final static int ardougne_tab = 8011;
	private final static int watchtower_tab = 8012;
	private final static int house_tab = 8013;
	private final static int[] tab =			{varrock_tab,falador_tab,lumbridge_tab,
												camelot_tab,ardougne_tab,watchtower_tab,
												house_tab};
	//General IDs
	private final static int chin = 10034;
	private boolean renewalNeeded = false;
	private boolean doDrinkRenewal = false;//TODO
	long fiveMinuteTimer;
	Timer t = new Timer(fiveMinuteTimer);//TODO
	
	//Interaction IDs
	private final static int treeAnimation = 7082;				//Tree animation when being teleported
	private final static int prayAnimation = 645;
	private final static int[] tree_door_id = {69197,69198};	//Open Tree Door
	private final static int spirit_tree_ge_id = 1317; 			// Teleport Spirit tree
	private final static int spirit_tree_main_id = 68974;
	private final static int gnome_ladder_id = 69499;			//Climb-up Ladder
	private final static int ape_ladder_id = 4780;				//Climb-down Ladder
	private final static int varrock_altar_id = 24343;
	private final static int zombieDeathAnim = 1384;
	
	//NPC IDs
	private final static int daero_id = 824; 					//first NPC "Travel" and reply Yes
	private final static int waydar_id = 1407;					//second NPC "Travel" and reply Yes
	private final static int lumdo_id = 1408; 					//third NPC "Travel" and reply Yes
	private final static int monkey_zombie_id = 1465;
	
	@Override
    protected void setup() {
    	
    	int RANGEstartExp = Skills.getExperience(Skills.RANGE);
    	int HPstartExp    = Skills.getExperience(Skills.CONSTITUTION);
    	long startTime = System.currentTimeMillis();
    	
    	WidgetChild wid = Widgets.get(1,1);
    	int w = Widgets.get(1,1).getIndex();
    	if(w >= 20){
    		wid.interact("Buy all");
    	}
    	
    	try {
    		SwingUtilities.invokeLater(new Runnable() {
    		public void run() {
    		gui = new GUI();
    		gui.setVisible(true);
    		}
    		});
    		}
    		catch (Exception e) {
    		}
    	
    			if(Game.isLoggedIn() == false){
    				Time.sleep(Random.nextInt(2000, 4000));
    			}
        		
    			provide(new onStart());
    			provide(new runToChins());
    			provide(new throwChins());
    			provide(new Banking());
    			
        		final onStart onstart = new onStart();
        		final Strategy onstartAction = new Strategy(onstart, onstart);
        		provide(onstartAction);
        		
        }
        

		private class onStart extends Strategy implements Task {
                @Override
                public void run() {
                	onStart = 1;
                }

                @Override
                public boolean validate() {
                        return onStart == 0;
                }
                
        }
        private class runToChins extends Strategy implements Task {
        	@Override
        	public void run() {
        		for (final Item item : Inventory.getItems()) {
        	          for (int id : Antipoison) {
        	           if (item.getId() == id && item.getWidgetChild().interact(
        	                   "Drink")) {
        	                Time.sleep(Random.nextInt(50, 100));
        	           		}
        	          }
        		}
        	          
        		//Declaring NPCs
        		NPC daero = NPCs.getNearest(daero_id);
        		NPC waydar = NPCs.getNearest(waydar_id);
        		NPC lumdo = NPCs.getNearest(lumdo_id);
        		
        		//Declaring objects being interacted with
        		SceneObject tree_door = SceneEntities.getNearest(tree_door_id);
        		SceneObject gnome_ladder = SceneEntities.getNearest(gnome_ladder_id);
        		SceneObject spirit_tree_ge = SceneEntities.getNearest(spirit_tree_ge_id);
        		SceneObject spirit_tree_main = SceneEntities.getNearest(spirit_tree_main_id);
        		SceneObject ape_ladder = SceneEntities.getNearest(ape_ladder_id);
        		SceneObject varrock_altar = SceneEntities.getNearest(varrock_altar_id);
        		        		
        		//Declaring variables
        	          
    			Item food = Inventory.getItem(Food);
	        	Item antipoisonItem = Bank
	        		         .getItem(new Filter<Item>() {
	        		         @Override
	        		         public boolean accept(final Item l) {
	        		         for (int id : Antipoison) {
	        		         if (l.getId() == id)
	        		                 return true;
	        		         }
	        		         return false;
	        		         }
	        		         });
				
        		if(grandExchange.contains(Players.getLocal().getLocation())){
        			Prayer.toggleQuick(true);
        			if(Players.getLocal().getPrayerIcon() == 19){
        				Prayer.toggleQuick(true);
						Logger.getLogger("EpicsChins").info("You set it up correctly, good going!");
        			}else{
        				if(Players.getLocal().getPrayerIcon() == 9){
        					Prayer.toggleQuick(true);
    						Logger.getLogger("EpicsChins").info("You set it up correctly, good going!");
        				}
        			}
        			checkRun();
        			if(Equipment.getItems().equals(chin)){
        				int chinCount = Equipment.getCount(chin);
        					if(chinCount >= 500){
        						Logger.getLogger("EpicsChins").info("You don't have enough chins to run! Getting more chins...");
        		    	   		Walking.findPath((Locatable) Bank.getNearest());
        						Camera.turnTo((Locatable) Bank.getNearest());
        						Bank.open();
        						if(Bank.getItem(chin).getStackSize() >= 3000){
            						Bank.getWidget().getChild(chin).interact("All except one");
        						}else{
        						Bank.withdraw(chin, 2000);
        							}
        					}
        			}
        			if(isPoisoned() == true){
            	   		Walking.findPath((Locatable) Bank.getNearest());
        				Bank.open();
        				if(Bank.isOpen()){
        					if(Inventory.isFull()){
        						Bank.deposit(prayer_pot_four_dose, 1);
        					}
        						Bank.close();
        		        			for (final Item antiitem : Inventory.getItems()) {
        		        	          for (int id : Antipoison) {
        		        	           if (antiitem != null && antiitem.getId() == id && antiitem.getWidgetChild().interact("Drink")) {
        		        	                Time.sleep(Random.nextInt(50, 100));
        		        	           }else{
        		        	        	   Bank.withdraw(antipoisonItem.getId(), 1);
        		        	        	   }
        		        	          }
        						}
        					}else{
    							log.info("Can't get into bank and we're poisoned! Shutting down...");
    							Game.logout(false);
    							stop();
        				}
        			}
					if(Players.getLocal().getHpPercent() > 30){
		    	   		Walking.findPath((Locatable) Bank.getNearest());
						Bank.open();
						if(Bank.isOpen()){
							if(Inventory.isFull()){
								Bank.deposit(prayer_pot_four_dose, 3);
							}
							Bank.withdraw(Food, 3);
							Time.sleep(200, 400);
							Bank.close();
						}else{
							Logger.getLogger("EpicsChins").info("Can't get into bank and HP is low. Shutting down...");
							Game.logout(false);
							stop();
						}
						if(!Bank.isOpen()){
					    	food.getWidgetChild().interact("Eat");
						}
						if(Players.getLocal().getHpPercent() > 50){
							food.getWidgetChild().interact("Eat");
						}else{
							return;
						}
						if(Players.getLocal().getHpPercent() > 75){
							food.getWidgetChild().interact("Eat");
						}
		    	   		Walking.findPath((Locatable) Bank.getNearest());
						Bank.open();
						Time.sleep(200, 400);
						Bank.deposit(Food, 5);
							if(Inventory.getCount(Food) == 0){
								Bank.getNearest();
								Bank.open();
								if(Bank.isOpen()){
									Bank.withdraw(Food, 2);
									Time.sleep(200,300);
									Bank.withdraw(prayer_pot_four_dose, 3);
								}
							}
					}
					if(Prayer.getPoints() > 300){
						Logger.getLogger("EpicsChins").info("Prayer is lower than 80%, let's go charge up before we head out.");
						Walking.findPath(varrock_altar).traverse();
							if(varrock_altar.isOnScreen() && varrock_altar != null){
								Camera.turnTo(varrock_altar);
								Time.sleep(Random.nextInt(20, 50));
								varrock_altar.click(true);
									if(Players.getLocal().getAnimation() == prayAnimation){
										Time.sleep(100, 400);
									}
									if(Players.getLocal().getPrayerIcon() == 100){
										Logger.getLogger("EpicsChins").info("All charged up, let's get going.");
									}
										}else{
										Logger.getLogger("EpicsChins").info("Can't find the altar, we'll proceed without charging up I suppose...");
							}
					}
        			Walking.findPath(spirit_tree_ge).traverse();
        				if(spirit_tree_ge.isOnScreen() && spirit_tree_ge != null){
        					spirit_tree_ge.interact("Teleport");
                			if(Players.getLocal().getAnimation() == treeAnimation){
                				Time.sleep(50, 400);
                				}else{
                					Logger.getLogger("EpicsChins").info("Tree animation is not present. Something has gone turribly wrong!");
                			}
        				}
        		}else{
        			if(!grandExchange.contains(Players.getLocal().getLocation())){
        				Logger.getLogger("EpicsChins").info("You aren't in the Grand Exchange! Shutting down...");
        				Game.logout(false);
        				stop();
        			}
        		}
        		if(spiritMidTile.equals(Players.getLocal()) && spirit_tree_main.isOnScreen() && spirit_tree_main != null){
        			Camera.turnTo(spirit_tree_main);
        			spirit_tree_main.interact("Teleport");
        			if(Players.getLocal().getAnimation() == treeAnimation){
        				Time.sleep(50, 400);
        				}else{
        					Logger.getLogger("EpicsChins").info("Tree animation is not present. Something has gone turribly wrong!");
        			}
        			Time.sleep(50, 400);
        			WidgetChild spiritTreeInterface = Widgets.get(6, 0);
        				if(spiritTreeInterface.validate()){
        					spiritTreeInterface.click(true); 
        					Time.sleep(Random.nextInt(100, 200));
        			}
        		}
        		if(spiritEndTile.equals(Players.getLocal())){
        			Walking.findPath(tree_door).traverse();
        			if(tree_door.isOnScreen() && tree_door != null){
        				tree_door.interact("Open");
        				Time.sleep(100, 250);
        			}
        		}
        			if(treeDoorInsideTile.contains(Players.getLocal().getLocation())){
        			Walking.findPath(gnome_ladder).traverse();
        			if(gnome_ladder != null && Players.getLocal().getAnimation() == -1){
        				Camera.turnTo(gnome_ladder);
        				gnome_ladder.interact("Climb-up");
        				Time.sleep(Random.nextInt(100, 250));
        			}
        		}
        			if(gnomeLadderMid.equals(Players.getLocal())){
        				Walking.findPath(daero).traverse();
        					if(daero.isOnScreen() && daero.getAnimation() == -1){
        						Camera.turnTo(daero);
        						daero.interact("Travel");
        						WidgetChild yesInterface = Widgets.get(1188, 3);
        							if(yesInterface.validate()){
        								yesInterface.click(true); 
                						Time.sleep(Random.nextInt(100, 125));
        									}else{
        										Time.sleep(Random.nextInt(50, 75));
        								}
        							}
        						}
        			if(waydarZone.contains(Players.getLocal().getLocation())){
        				if(waydar.isOnScreen() && waydar.getAnimation() == -1){
        					Camera.turnTo(waydar);
        					waydar.interact("Travel");
        					WidgetChild yesInterface = Widgets.get(1188, 3);
        						if(yesInterface.validate()){
        							yesInterface.click(true); 
        							Time.sleep(100, 200);
        							}
        								}else{
                    						if(waydar.getAnimation() != -1 && waydar.isOnScreen()){
                    							Time.sleep(Random.nextInt(100, 125));
                    					}else{
                    						Walking.findPath(waydar).traverse();
                    						}
        				}
        			}
        			if(lumdoZone.contains(Players.getLocal().getLocation())){
        				if(lumdo.isOnScreen() && lumdo.getAnimation() == -1){
        					lumdo.interact("Travel");
        					WidgetChild yesInterface = Widgets.get(1188, 3);
        						if(yesInterface.validate()){
        							yesInterface.click(true); 
            						Time.sleep(Random.nextInt(100, 125));
								}else{
            						if(waydar.getAnimation() != -1 && waydar.isOnScreen()){
            							Time.sleep(Random.nextInt(100, 125));
            					}else{
            						Walking.findPath(waydar).traverse();
            						}
								}
        					}
        				}
        			if(apeStart.equals(Players.getLocal()) && usingGreegree == true){
	        			for (final Item item1 : Inventory.getItems()) {
		        	          for (int id : greegree) {
		        	           if (item1 != null && item1.getId() == id && item1.getWidgetChild()
		        	        		   .interact("Equip")) {
		        	        	   Time.sleep(Random.nextInt(50,75));
		        	           }
		        	          }
	        				}
        				Walking.findPath(ape_ladder).traverse();
						Time.sleep(Random.nextInt(50, 125));
        						if(ape_ladder.isOnScreen() && apeLadderTop.equals(Players.getLocal())
        								&& ape_ladder != null){
        							ape_ladder.interact("Climb-down");
        							Time.sleep(300,425);
        							
        			}
        		}else{
        			if(apeStart.equals(Players.getLocal()) && usingGreegree == false){
            			checkRun();
        			}
        		}
        			if(apeLadderBottom.equals(Players.getLocal())){
	        			for (final Item renewalFlask : Inventory.getItems()) {
		        	          for (int id : prayer_renewal_flask) {
		        	           if (renewalFlask != null && renewalFlask.getId() == id && renewalFlask.getWidgetChild()
		        	        		   .interact("Drink")) {
		        	                Time.sleep(Random.nextInt(50, 100));
		        	           	}
		        	          }
	        				}
        				Prayer.setQuick();
        				if(randomChinTile == chinTile1){
        					Walking.newTilePath(ladderToSpotOne).traverse();
        					Walking.findPath(chinTile1).traverse();
                				if(randomChinTile.equals(Players.getLocal())){
                					checkPrayer();
                					if(chinTile1.equals(Players.getLoaded().length >= 2)){
                						Logger.getLogger("EpicsChins").info("There's 2+ people" +
                								" here, trying next spot...");
                						Walking.newTilePath(spotOneToSpotTwo).traverse();
                						Walking.findPath(chinTile2).traverse();
                							if(chinTile2.equals(Players.getLoaded().length >=2)){
                        						Logger.getLogger("EpicsChins").info("There's 2+ people" +
                        								" here, trying next spot...");
                								Walking.newTilePath(spotTwoToSpotThree).traverse();
                								Walking.findPath(chinTile3).traverse();
                									if(spotThreeToSpotFourSq.equals(Players.getLoaded().
                											length>2)){
                                						Logger.getLogger("EpicsChins").info("There's 2+ " +
                                								"people here, hopping worlds...");
    													changeWorlds();
                									}
                								}
                							}
                						}
        							}
        				if(randomChinTile == chinTile2){
            				Prayer.setQuick();
        					Walking.newTilePath(ladderToSpotOne).traverse();
							Walking.newTilePath(spotOneToSpotTwo).traverse();
							Walking.findPath(chinTile2).traverse();
								if(randomChinTile.equals(Players.getLocal())){
									if(chinTile2.equals(Players.getLoaded().length >=2)){
	            						Logger.getLogger("EpicsChins").info("There's 2+ people" +
	            								" here, trying next spot...");
	            						Walking.newTilePath(spotTwoToSpotThree).traverse();
	            						Walking.findPath(chinTile3).traverse();
	            							if(spotThreeToSpotFourSq.equals(Players.getLoaded().length >=2)){
                        						Logger.getLogger("EpicsChins").info("There's 2+ people " +
                        								"here, trying next spot...");
                        						Walking.newTilePath(spotTwoToSpotThree).reverse().traverse();
                        						Walking.newTilePath(spotOneToSpotTwo).reverse().traverse();
                        						Walking.findPath(chinTile1).traverse();
                        							if(chinTile1.equals(Players.getLoaded().length >=2)){
                                						Logger.getLogger("EpicsChins").info("There's 2+ people" +
                                								" here, hopping worlds...");
    													changeWorlds();
                        							}
	            								}
											}
										}
        							}
        				if(randomChinTile == chinTile3){
            				Prayer.setQuick();
        					Walking.newTilePath(ladderToSpotOne).traverse();
							Walking.newTilePath(spotOneToSpotTwo).traverse();
							Walking.newTilePath(spotTwoToSpotThree).traverse();
							Walking.findPath(chinTile3).traverse();
								if(randomChinTile.equals(Players.getLocal())){
									if(spotThreeToSpotFourSq.equals(Players.getLoaded().length >=2)){
										Logger.getLogger("EpicsChins").info("There's 2+ people here, trying " +
												"next spot...");
										Walking.newTilePath(spotTwoToSpotThree).reverse().traverse();
										Walking.findPath(chinTile2).traverse();
											if(chinTile2.equals(Players.getLoaded().length >=2)){
												Logger.getLogger("EpicsChins").info("There's 2+ people here, " +
														"trying next spot...");
												Walking.newTilePath(spotOneToSpotTwo).reverse().traverse();
												Walking.findPath(chinTile1).traverse();
													if(chinTile1.equals(Players.getLoaded().length >=2)){
														Logger.getLogger("EpicsChins").info("There's 2+ people " +
																"here, hopping worlds...");
														changeWorlds();
                    							}
											}
										}
									}
        						}
        				if(randomChinTile == chinTile4){
            				Prayer.setQuick();
        					Walking.newTilePath(ladderToSpotOne).traverse();
							Walking.newTilePath(spotOneToSpotTwo).traverse();
							Walking.newTilePath(spotTwoToSpotThree).traverse();
							Walking.newTilePath(spotThreeToSpotFour).traverse();
								if(chinTile4.equals(Players.getLoaded().length >=2)){
									Logger.getLogger("EpicsChins").info("There's 2+ people here, trying next " +
											"spot...");
									Walking.newTilePath(spotThreeToSpotFour).reverse().traverse();
									Walking.newTilePath(spotTwoToSpotThree).reverse().traverse();
									Walking.findPath(chinTile2).traverse();
										if(spotThreeToSpotFourSq.equals(Players.getLoaded().length >=2)){
											Logger.getLogger("EpicsChins").info("There's 2+ people here, trying" +
													" next spot...");
											Walking.newTilePath(spotOneToSpotTwo).reverse().traverse();
											Walking.findPath(chinTile1).traverse();
												if(chinTile1.equals(Players.getLoaded().length >=2)){
													Logger.getLogger("EpicsChins").info("There's 2+ people " +
															"here, hopping worlds...");
													changeWorlds();
												}
											}
										}
        							}
        						}
        					}

			@Override
			public boolean validate() {
						return grandExchange.contains(Players.getLocal()) &&
						Inventory.getCount(Food) >= 1 && 
						Inventory.getCount(prayer_renewal_flask) == 3 &&
						Inventory.getCount(prayer_pot) == 18 &&
						Inventory.getCount(ranging_flask) == 3 &&
						Inventory.getCount(antipoison) == 1 &&
						Inventory.getCount(tab) > 0 &&
						isPoisoned() == false &&
						guiwait == false &&
						!randomChinArray.equals(Players.getLocal());
        }
    }
        
        private class throwChins extends Strategy implements Task {
    		NPC monkey_zombie = NPCs.getNearest(monkey_zombie_id);

			@Override
			public void run() {
				int realRange = Skills.getRealLevel(Skills.RANGE);
				int potRange = 	Skills.getLevel(Skills.RANGE);
				int rangeDifference = potRange - realRange;
				
				if(rangeDifference >= 3){
	        		for (final Item item : Inventory.getItems()) {
	        	          for (int id : ranging_flask) {
	        	           if (item.getId() == id && item.getWidgetChild().interact(
	        	                   "Drink")) {
	        	                Time.sleep(Random.nextInt(50, 100));
	        	           }else{
	   	        			Logger.getLogger("EpicsChins").info("We're out of ranging pots, resuming until prayer potions are gone!");
	        	}
	        		}
	        		}

				}
				if(renewalNeeded == true){
		            for(int i : prayer_renewal_flask) {
		                if(Inventory.getCount(i) > 0) {
		                    Item potion = Inventory.getItem(i);
		                    if(potion != null) {
		                        WidgetChild potionWidget = potion.getWidgetChild();
		                        if(potionWidget.validate()) {
		                            log.info("Drinking "+potion.getName());
		                            if(potionWidget.click(true)) {
		                                renewalNeeded = false;
		                                Time.sleep(Random.nextInt(1500, 2500));
		                                return;
		                            }
		                        }
		                    }
		                }
		            }
				}
				if(Prayer.isQuickOn() == false){
					Prayer.setQuick();
				}
				if(isPoisoned() == true){
        			for (final Item antiitem : Inventory.getItems()) {
	        	          for (int id : Antipoison) {
	        	           if (antiitem != null && antiitem.getId() == id && antiitem.getWidgetChild().interact("Drink")) {
	        	                Time.sleep(Random.nextInt(50, 100));
	        	           }else{
	        	        	   Logger.getLogger("EpicsChins").info("We're out of antipoison & we're poisoned! Teleporting to safety...");
	        	        	   		for (final Item tabItem : Inventory.getItems()) {
	        	        	   			for (int tabID : tab) {
	        	        	   				if (tabItem.getId() == tabID && tabItem.getWidgetChild().interact(
	        	        	   						"Break")) {
	        	        	   					Time.sleep(Random.nextInt(50, 100));
	        	        	   					Game.logout(false);
	        	        	   					stop();
	        	        	   					}
	        	        	   				}
	        	        	   			}
	        	           			}
	        	          		}
        					}
				}
				if(monkey_zombie.isOnScreen() && monkey_zombie.validate()){
					monkey_zombie.interact("Attack");			
						if(!Players.getLocal().isInCombat() && Players.getLocal().getInteracting() == null){
							Time.sleep(Random.nextInt(700, 800));
						}
						int chinThrowID = 2779;
						if(Players.getLocal().getAnimation() == chinThrowID){
							chinsThrown++;
							Time.sleep(Random.nextInt(20, 50));
						}
						if(Players.getLocal().getAnimation() == -1 && monkey_zombie.isOnScreen()
								&& monkey_zombie.validate()){
							Camera.turnTo(monkey_zombie);
							monkey_zombie.interact("Attack");
							Time.sleep(Random.nextInt(900, 1200));
				}else{
					Camera.turnTo(monkey_zombie);
				}
				if(monkey_zombie.getAnimation() == zombieDeathAnim){
					zombieKillCount++;
				}
				final int vialid = 229;
				Item vial = Inventory.getItem(vialid);
				if(Inventory.getItem() == vial){
					vial.getWidgetChild().interact("Drop");
					}
				}
			}

			@Override
			public boolean validate() {
				int chinCount = Equipment.getCount(chin);
				return chinTile1.equals(Players.getLocal()) ||
				chinTile2.equals(Players.getLocal())	||
				chinTile3.equals(Players.getLocal())	||
				chinTile4.equals(Players.getLocal())	&&
				chinCount >= 200						&&
				Inventory.getCount(prayer_pot) >= 1;
        	
        }
     }
        private class Banking extends Strategy implements Task {
        	
        	Item antipoisonItem = Bank
   		         .getItem(new Filter<Item>() {
   		         @Override
   		         public boolean accept(final Item l) {
   		         for (int id : Antipoison) {
   		         if (l.getId() == id)
   		                 return true;
   		         }
   		         return false;
   		         }
   		         });
        	
        	Item greegreeItem = Bank
   		         .getItem(new Filter<Item>() {
   		         @Override
   		         public boolean accept(final Item m) {
   		         for (int id : greegree) {
   		         if (m.getId() == id)
   		                 return true;
   		         }
   		         return false;
   		         }
   		         });

        	Item rangeFlaskItem = Bank
      		         .getItem(new Filter<Item>() {
      		         @Override
      		         public boolean accept(final Item n) {
      		         for (int id : ranging_flask) {
      		         if (n.getId() == id)
      		                 return true;
      		         }
      		         return false;
      		         }
      		         });
        	
        	Item tabItem = Bank
     		         .getItem(new Filter<Item>() {
     		         @Override
     		         public boolean accept(final Item o) {
     		         for (int id : tab) {
     		         if (o.getId() == id)
     		                 return true;
     		         }
     		         return false;
     		         }
     		         });
        	
        	Item prayer_renewal_flaskItem = Bank
    		         .getItem(new Filter<Item>() {
    		         @Override
    		         public boolean accept(final Item p) {
    		         for (int id : tab) {
    		         if (p.getId() == id)
    		                 return true;
    		         }
    		         return false;
    		         }
    		         });
        	
			@Override
			public void run() {
    	   		for (final Item tabItem : Inventory.getItems()) {
    	   			for (int tabID : tab) {
    	   				if (tabItem.getId() == tabID && tabItem.getWidgetChild().interact(
    	   						"Break")) {
    	   					Time.sleep(Random.nextInt(50, 100));
    	   				}
    	   			}
    	   		}
    			checkRun();
    	   		Walking.findPath((Locatable) Bank.getNearest());
				Camera.turnTo((Locatable) Bank.getNearest());
				if(Players.getLocal().getHpPercent() >= 70){
					Bank.open();
						if(Bank.isOpen()){
							Bank.depositInventory();
							Bank.withdraw(Food, 2);
							Bank.close();
						}
		    			Item food = Inventory.getItem(Food);
						food.getWidgetChild().interact("Eat");
						Time.sleep(Random.nextInt(900, 1200));
				}
    	   		Bank.open();
    	   		if(Bank.isOpen()){
	   				if(usingGreegree == true){
	    	   			Bank.depositInventory();
	    	   			Bank.withdraw(Food, 1);
	   					Bank.withdraw(greegreeItem.getId(), 1);
	   					Bank.withdraw(prayer_pot_four_dose, 18);
	   					Bank.withdraw(antipoisonItem.getId(), 1);
	   					Bank.withdraw(tabItem.getId(), 1);
	   					Bank.withdraw(prayer_renewal_flaskItem.getId(), 3);
    	   				Bank.withdraw(rangeFlaskItem.getId(), 3);
    	   				Bank.close();
	   				}
	   				if(usingGreegree == false){
	   					Bank.depositInventory();
	   					Bank.withdraw(Food, 1);
	   					Bank.withdraw(antipoisonItem.getId(), 1);
	   					Bank.withdraw(tabItem.getId(), 1);
	   					Bank.withdraw(prayer_renewal_flaskItem.getId(), 3);
    	   				Bank.withdraw(rangeFlaskItem.getId(), 3);
    	   				Bank.withdraw(prayer_pot_four_dose, 18);
    	   				Bank.close();
	   				}
    	   		}
    	   	}

			@Override
			public boolean validate() {
				return Inventory.getCount(prayer_pot) <= 1		||
					   Equipment.getCount(chin) <= 100			||
					   isPoisoned() == true 					&&
					   Antipoison == null						||
					   Players.getLocal().getHpPercent() <= 25;
						
			}
        }

        
        
        
        
        
        
			private boolean isPoisoned() {
				return Settings.get(102) != 0; 
			}
			private void changeWorlds() {
				Game.logout(true);
				Time.sleep(Random.nextInt(2000, 5000));
				if(Lobby.isOpen() && Lobby.STATE_LOBBY_IDLE != 0){
					int randomWorld = membersWorlds[Random.nextInt(0, membersWorlds.length)];
					Context.setLoginWorld(randomWorld);
					Time.sleep(Random.nextInt(200, 400));
					if(Game.isLoggedIn()){
        				Prayer.setQuick();
			}
        }
			}
			private void checkRun(){
    			if(Walking.getEnergy() > 30){
    				Walking.setRun(true);
			}
			}
			private void checkPrayer(){
				if(Prayer.getPoints() <= 250){
					for(final Item prayerPotItem : Inventory.getItems()){
						for(int prayerPotID : prayer_pot){
						if(prayerPotItem.getId() == prayerPotID && prayerPotItem.getWidgetChild()
								.interact("Drink")){
						Time.sleep(Random.nextInt(25, 50));
						}
				}
			}
				}else{
					Logger.getLogger("EpicsChins").info("Prayer is above 25%, not using potion!");
				}
			}
			@Override
			public void messageReceived(MessageEvent arg0) {
		        String message = arg0.getMessage().toLowerCase();
		        String messageSender = arg0.getSender();
		        if(messageSender == null){
			        if(message.contains("about to run out")) {
			            renewalNeeded = true;
			        }
		        }
		    }
			public void drinkRenewal(){//TODO
    			for (final Item renewalFlask : Inventory.getItems()) {
      	          for (int id : prayer_renewal_flask) {
      	           if (renewalFlask != null && renewalFlask.getId() == id && renewalFlask.getWidgetChild()
      	        		   .interact("Drink")) {
      	        	   if(renewalFlask != null && renewalFlask.getId() != id){
      	        		   Logger.getLogger("EpicsChins").info("Looks like we drank a dose, starting timer..");
      	        		   fiveMinuteTimer = 300000;
      	        		   		if(t.isRunning() && t.getRemaining() > 10){
      	        		   			doDrinkRenewal = false;
      	        		   		}else{
      	        		   			if(t.getRemaining() <= 100){
      	        		   				doDrinkRenewal = true;
      			        	}
      			        }
      	        	   }
      	           	}
      	          }
  				}
			}
			
		    
		    
		    
		    
		    
        @SuppressWarnings("serial")
		public class GUI extends javax.swing.JFrame {
			public GUI() {
                    String version = " v1.0";
                    
                    

                    //Title
                    setTitle("EC" + version);
                    setResizable(false);
                    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    Container contentPane = getContentPane();
                    contentPane.setLayout(null);
                    
        			//---- foodLabel ----
                    final JLabel foodLabel = new JLabel("What food should we use?");
        			foodLabel.setFont(foodLabel.getFont().deriveFont(foodLabel.getFont().getStyle() | Font.BOLD));
        			contentPane.add(foodLabel);
        			foodLabel.setBounds(20, 185, 155, foodLabel.getPreferredSize().height);
        			
        			//---- antiLabel ----
        			final JTextPane antiLabel = new JTextPane();
        			antiLabel.setText("What antipoison should we use?");
        			antiLabel.setBackground(new Color(240, 240, 240));
        			antiLabel.setFont(antiLabel.getFont().deriveFont(antiLabel.getFont().getStyle() | Font.BOLD));
        			antiLabel.setEditable(false);
        			contentPane.add(antiLabel);
        			antiLabel.setBounds(5, 235, 190, 25);
        			
        			//---- warningLabel ----
        			final JLabel warningLabel = new JLabel("WARNING");
        			warningLabel.setForeground(Color.red);
        			warningLabel.setFont(warningLabel.getFont().deriveFont(warningLabel.getFont().getStyle() | Font.BOLD));
        			contentPane.add(warningLabel);
        			warningLabel.setBounds(70, 285, 60, warningLabel.getPreferredSize().height);

        			//---- warningLabelB ----
        			final JLabel warningLabelB = new JLabel("Start in the Grand Exchange!");
        			contentPane.add(warningLabelB);
        			warningLabelB.setBounds(new Rectangle(new Point(40, 305), warningLabelB.getPreferredSize()));

        			//chinLabelLeft
            		final Image chinPictureLeft = getImage("http://2c1c.net/images/faceRight.png");
        			final JLabel chinLabelLeft = new JLabel(new ImageIcon(chinPictureLeft));
        			contentPane.add(chinLabelLeft);
        			chinLabelLeft.setBounds(10, 10, 24, 24);
   					 
        			//chinLabelRight
            		final Image chinPictureRight= getImage("http://2c1c.net/images/faceLeft.png");
            		if(chinPictureRight != null){
                        Logger.getLogger("EpicsChinsGUI").info("Image failed to load");
            		}
        			final JLabel chinLabelRight = new JLabel(new ImageIcon(chinPictureRight));
            		if(chinPictureLeft != null){
                        Logger.getLogger("EpicsChinsGUI").info("Image failed to load");
            		}
        			contentPane.add(chinLabelRight);
        			chinLabelRight.setBounds(160, 10, 24, 24);
   					 
   					//---- greeLabel ----
        			final JLabel greeLabel = new JLabel("Are we using a greegree?");
   					greeLabel.setFont(greeLabel.getFont().deriveFont(greeLabel.getFont().getStyle() | Font.BOLD));
   					contentPane.add(greeLabel);
   					greeLabel.setBounds(new Rectangle(new Point(25, 140), greeLabel.getPreferredSize()));
   					
        			//---- titleLabel ----
        			final JLabel titleLabel = new JLabel("Epics Chinner" + version);
        			titleLabel.setFont(titleLabel.getFont().deriveFont(titleLabel.getFont().getStyle() | Font.BOLD));
        			contentPane.add(titleLabel);
        			titleLabel.setBounds(45, 10, 110, 25);

        			//---- reqTextPane ----
                    final JTextPane reqTextPane = new JTextPane();
        			reqTextPane.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        			reqTextPane.setDisabledTextColor(new Color(240, 240, 240));
        			reqTextPane.setBackground(new Color(240, 240, 240));
        			reqTextPane.setEditable(false);
        			reqTextPane.setText("Requirements:");
        			reqTextPane.setFont(reqTextPane.getFont().deriveFont(reqTextPane.getFont().getStyle() | Font.BOLD));
        			contentPane.add(reqTextPane);
        			reqTextPane.setBounds(45, 40, 95, 20);

        			//---- reqTextPaneB ----
                    final JTextPane reqTextPaneB = new JTextPane();
        			reqTextPaneB.setBackground(new Color(240, 240, 240));
        			reqTextPaneB.setText("- Access to Ape Atoll\n- 43 Prayer\n- 55 Ranged\n- 3+ Prayer renewal flasks\n- 3+ Ranged flasks");
        			reqTextPaneB.setEditable(false);
        			contentPane.add(reqTextPaneB);
        			reqTextPaneB.setBounds(25, 55, 135, 75);
        			
   					//---- greeBoxYes ----
   					final JCheckBox greeBoxYes = new JCheckBox("Yes");
   					greeBoxYes.setSelected(true);
   					if(greeBoxYes.isSelected()){
   						usingGreegree = true;
   					}
   					contentPane.add(greeBoxYes);
   					greeBoxYes.setBounds(new Rectangle(new Point(45, 160), greeBoxYes.getPreferredSize()));
   					
   					//---- greeBoxNo ----
   					final JCheckBox greeBoxNo = new JCheckBox("No");
   					greeBoxNo.setSelected(false);
   					if(greeBoxNo.isSelected()){
   						usingGreegree = false;
   					}
   					contentPane.add(greeBoxNo);
   					greeBoxNo.setBounds(new Rectangle(new Point(100, 160), greeBoxNo.getPreferredSize()));
        			
   					//---- foodCombo ----
        			final JComboBox<String> foodCombo = new JComboBox<>();
        			foodCombo.setModel(new DefaultComboBoxModel<>(new String[] {
        				"Select your food...",
        				"Shark",
        				"Rocktail",
        				"Monkfish",
        				"Swordfish",
        				"Lobster",
        				"Tuna",
        				"Trout",
        				"Salmon"
        			}));
        			contentPane.add(foodCombo);
        			foodCombo.setBounds(25, 210, 150, foodCombo.getPreferredSize().height);

        			//---- poisonCombo ----
        			final JComboBox<String> poisonCombo = new JComboBox<String>();
        			poisonCombo.setModel(new DefaultComboBoxModel<>(new String[] {
        				"Select an antipoison...",
        				"Super antipoison flask",
        				"Antipoison++ flask",
        				"Antipoison+ flask",
        				"Antipoison flask",
        				"Super antipoison",
        				"Antipoison++",
        				"Antipoison+",
        				"Antipoison",
        				"Antipoison mix",
        				"Antipoison elixir"
        			}));
        			contentPane.add(poisonCombo);
        			poisonCombo.setBounds(25, 260, 150, poisonCombo.getPreferredSize().height);
            			
        			{ // compute preferred size
            				Dimension preferredSize = new Dimension();
            				for(int i = 0; i < contentPane.getComponentCount(); i++) {
            					Rectangle bounds = contentPane.getComponent(i).getBounds();
            					preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
            					preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            				}
            				Insets insets = contentPane.getInsets();
            				preferredSize.width += insets.right;
            				preferredSize.height += insets.bottom;
            				contentPane.setMinimumSize(preferredSize);
            				contentPane.setPreferredSize(preferredSize);
            			}
            			setSize(210, 395);
            			setLocationRelativeTo(null);
            			
            			//---- startButton ----
            			final JButton startButton = new JButton("Start");
            			contentPane.add(startButton);
            			startButton.setBounds(5, 330, 185, 25);
            			startButton.addActionListener(new ActionListener(){
    						public void actionPerformed(ActionEvent e) {
    							String chosenFood = foodCombo.getSelectedItem().toString();		
    								if(chosenFood.equals("Select your food...")){
    									Logger.getLogger("EpicsChins").info("No food selected, stopping script");
    									Game.logout(false);
    									stop();
    								}
    								if(chosenFood.equals("Shark")){
    									Food = 385;
    								}
    								if(chosenFood.equals("Rocktail")){
    									Food = 15272;
    								}
    								if(chosenFood.equals("Monkfish")){
    									Food = 7946;
    								}
    								if(chosenFood.equals("Swordfish")){
    									Food = 373;
    								}
    								if(chosenFood.equals("Lobster")){
    									Food = 379;
    								}
    								if(chosenFood.equals("Tuna")){
    									Food = 361;
    								}
    								if(chosenFood.equals("Trout")){
    									Food = 333;
    								}
    								if(chosenFood.equals("Salmon")){
    									Food = 329;
    								}
        						String chosenAntipoison = foodCombo.getSelectedItem().toString();		
									if(chosenAntipoison.equals("Select an antipoison...")){
										Logger.getLogger("EpicsChins").info("No antipoison selected, stopping script");
										Game.logout(false);
										stop();
									}
									if(chosenAntipoison.equals("Super antipoison flask")){
										Antipoison=  antiPoisonSuperFlask;
									}
									if(chosenAntipoison.equals("Antipoison++ flask")){
										Antipoison = antiPoisonPlusPlusFlask;
									}
									if(chosenAntipoison.equals("Antipoison+ flask")){
										Antipoison = antiPoisonPlusFlask;
									}
									if(chosenAntipoison.equals("Antipoison Flask")){
										Antipoison = antiPoisonFlask;
									}
									if(chosenAntipoison.equals("Super antipoison")){
										Antipoison = antiPoisonSuperPot;
									}
									if(chosenAntipoison.equals("Antipoison++")){
										Antipoison = antiPoisonPlusPlusPot;
									}
									if(chosenAntipoison.equals("Antipoison+")){
										Antipoison = antiPoisonPlusPot;
									}
									if(chosenAntipoison.equals("Antipoison")){
										Antipoison = antiPoisonPot;
									}
									if(chosenAntipoison.equals("Antipoison mix")){
										Antipoison = antiPoisonMix;	
									}
									if(chosenAntipoison.equals("Antipoison elixir")){
										Antipoison = antiPoisonElixir;
									}
    						}
            			});
            			this.dispose();
                        guiwait = false;

            }
    }
        private Image getImage(String url) {
            try {
                return ImageIO.read(new URL(url));
            } catch(IOException e) {
                return null;
            }
        }
     
        private final Color color1 = new Color(255, 255, 255);
     
        private final Font font1 = new Font("Verdana", 0, 10);
     
        private final Image img1 = getImage("http://2c1c.net/images/paint.png");
     
        public void onRepaint(Graphics g1) {
            mouseX = Mouse.getX();
                    mouseY = Mouse.getY();
           
            rangegainedExp = Skills.getExperience(Skills.RANGE) - RANGEstartExp;
                    hpgainedExp = Skills.getExperience(Skills.CONSTITUTION) - HPstartExp;
                    expHour = (int) ((rangegainedExp) * 3600000D / (System.currentTimeMillis() - startTime));
           
                    if (showpaint) {
            Graphics2D g = (Graphics2D)g1;
            g.drawImage(img1, -4, 336, null);
            g.setFont(font1);
            g.setColor(color1);
            g.drawString(runtime.toElapsedString(), 215, 444);
            g.drawString(String.valueOf(rangegainedExp),198,460);
            g.drawString(String.valueOf(hpgainedExp), 181, 480);
            g.drawLine(mouseX, mouseY - 10, mouseX, mouseY + 10);
                    g.drawLine(mouseX - 10, mouseY, mouseX + 10, mouseY);
            g.drawString(String.valueOf(expHour), 183, 497);
            g.drawString(String.valueOf(chinsThrown) , 351, 443);
            g.drawString(String.valueOf(zombieKillCount), 359, 461);
                    } else {
                            Graphics2D g = (Graphics2D) g1;
                            g.drawRect(502, 389, 15, 15);
                    }
                    }
       
       
       
        @Override
            public void mouseClicked(MouseEvent e) {
     
                    if (new Rectangle(502, 389, 14, 15).contains(e.getPoint())) {
                            if (showpaint) {
                                    showpaint = false;
                            } else {
                        		if(img1 != null){
                                    Logger.getLogger("EpicsChinsGUI").info("Image failed to load");
                        		}
                                    showpaint = true;
                            }
                    }
     
            }
     
            @Override
            public void mouseEntered(MouseEvent e) {
     
                   
            }
     
            @Override
            public void mouseExited(MouseEvent e) {
           
                   
            }
     
            @Override
            public void mousePressed(MouseEvent e) {
           
                   
            }
     
            @Override
            public void mouseReleased(MouseEvent e) {
     
                   
            }
}