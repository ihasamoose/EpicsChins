package EpicsChins.util;

import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;

import java.util.Arrays;

public class Tiles {
	public final static Tile[] PATH_TO_PRAYER_FROM_GE =
			{new Tile(3185, 3492, 0), new Tile(3197, 3480, 0), new Tile(3196, 3462, 0), new Tile(3216, 3464, 0),
			 new Tile(3233, 3466, 0), new Tile(3245, 3480, 0)};
	public final static Tile[] PATH_TO_PRAYER_FROM_TELE =
			{new Tile(3216, 3464, 0), new Tile(3233, 3466, 0), new Tile(3245, 3480, 0)};
	public final static Tile[] PATH_TO_CHIN_TILE_1 =
			{new Tile(2770, 9120, 0), new Tile(2758, 9133, 0), new Tile(2740, 9137, 0), new Tile(2722, 9140, 0),
			 new Tile(2714, 9138, 0)};
	public final static Tile[] PATH_TO_CHIN_TILE_2 = {new Tile(2731, 9130, 0), new Tile(2744, 9125, 0)};
	public final static Tile[] PATH_TO_CHIN_TILE_3 =
			{new Tile(2743, 9120, 0), new Tile(2738, 9120, 0), new Tile(2737, 9121, 0), new Tile(2720, 9118, 0)};
	public final static Tile[] PATH_TO_BANK_TILE =
			{new Tile(3205, 3440, 0), new Tile(3191, 3451, 0), new Tile(3175, 3459, 0)};
	public final static Tile TILE_GRAND_BANK = new Tile(3181, 3502, 0);
	public final static Tile TILE_GRAND_TREE = new Tile(3185, 3508, 0);
	public final static Tile TILE_APE_LADDER_TOP = new Tile(2764, 2703, 0);
	public final static Tile TILE_CHIN_1 = new Tile(2715, 9127, 0);
	public final static Tile TILE_CHIN_2 = new Tile(2746, 9122, 0);
	public final static Tile TILE_CHIN_3 = new Tile(2709, 9116, 0);
	public final static Tile TILE_CHIN_4 = new Tile(2701, 9111, 0);
	public final static Tile TILE_TREE_DOOR = new Tile(2466, 3491, 0);
	public final static Tile TILE_TREE_DAERO = new Tile(2480, 3488, 1);
	public final static Tile TILE_PRAYER = new Tile(3253, 3485, 0);
	public final static Tile TILE_INSIDE_TREE_DOOR = new Tile(2466, 3493, 0);

	public final static java.util.List<Tile> CHIN_LIST =
			Arrays.asList(Tiles.TILE_CHIN_1, Tiles.TILE_CHIN_2, Tiles.TILE_CHIN_3, Tiles.TILE_CHIN_4);

	public final static Area AREA_CHIN_3_4 = new Area(new Tile(2709, 9116, 0), new Tile(2701, 9111, 0));
	public final static Area AREA_GE = new Area(new Tile(3135, 3464, 0), new Tile(3203, 3516, 0));
	public final static Area AREA_BLINDFOND_ZONE = new Area(new Tile(2660, 4501, 0), new Tile(2641, 4531, 0));
	public final static Area AREA_CRASH_ISLAND = new Area(new Tile(2880, 2735, 0), new Tile(2903, 2711, 0));
	public final static Area AREA_GRAND_TELE = new Area(new Tile(3208, 3430, 0), new Tile(3217, 2422, 0));
	public final static Area AREA_SPIRIT_MID = new Area(new Tile(2544, 3172, 0), new Tile(2541, 3167, 0));
	public final static Area AREA_GNOME_STRONGHOLD = new Area(new Tile(2470, 3440, 0), new Tile(2457, 3492, 0));
	public final static Area AREA_APE_ATOLL = new Area(new Tile(2809, 2690, 0), new Tile(2753, 2718, 0));
	public final static Area AREA_GNOME_LEVEL_ONE = new Area(new Tile(2490, 3478, 1), new Tile(2440, 3512, 1));
	public final static Area AREA_APE_ATOLL_DUNGEON = new Area(new Tile(2805, 9144, 0), new Tile(2704, 9043, 0));

}
