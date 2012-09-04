package EpicsChins.util;

import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;

import java.util.Arrays;

public class Tiles {
	public final static
	Tile[]
			PATH_TO_PRAYER_FROM_GE =
			{new Tile(3183, 3493, 0), new Tile(3192, 3492, 0), new Tile(3196, 3482, 0), new Tile(3196, 3472, 0),
			 new Tile(3197, 3459, 0), new Tile(3206, 3456, 0), new Tile(3213, 3462, 0), new Tile(3222, 3465, 0),
			 new Tile(3233, 3467, 0), new Tile(3243, 3468, 0), new Tile(3245, 3478, 0), new Tile(3253, 3484, 0)};
	public final static
	Tile[]
			PATH_TO_PRAYER_FROM_TELE =
			{new Tile(3212, 3434, 0), new Tile(3212, 3445, 0), new Tile(3212, 3452, 0), new Tile(3213, 3462, 0),
			 new Tile(3222, 3465, 0), new Tile(3233, 3467, 0), new Tile(3243, 3468, 0), new Tile(3245, 3478, 0),
			 new Tile(3253, 3484, 0)};
	public final static
	Tile[]
			PATH_TO_CHIN_TILE_1 =
			{new Tile(2770, 9103, 0), new Tile(2778, 9101, 0), new Tile(2785, 9105, 0), new Tile(2776, 9108, 0),
			 new Tile(2766, 9110, 0), new Tile(2767, 9121, 0), new Tile(2778, 9119, 0), new Tile(2787, 9118, 0),
			 new Tile(2793, 9118, 0), new Tile(2800, 9110, 0), new Tile(2801, 9098, 0), new Tile(2808, 9105, 0),
			 new Tile(2810, 9118, 0), new Tile(2808, 9128, 0), new Tile(2795, 9130, 0), new Tile(2782, 9129, 0),
			 new Tile(2773, 9133, 0), new Tile(2760, 9134, 0), new Tile(2748, 9135, 0), new Tile(2736, 9138, 0),
			 new Tile(2722, 9140, 0), new Tile(2716, 9133, 0), new Tile(2715, 9132, 0)};
	public final static
	Tile[]
			PATH_TO_CHIN_TILE_2 =
			{new Tile(2726, 9130, 0), new Tile(2736, 9131, 0), new Tile(2744, 9124, 0)};
	public final static
	Tile[]
			PATH_TO_CHIN_TILE_3 =
			{new Tile(2741, 9122, 0), new Tile(2741, 9121, 0), new Tile(2733, 9121, 0), new Tile(2724, 9120, 0),
			 new Tile(2717, 9119, 0), new Tile(2710, 9119, 0), new Tile(2706, 9116, 0)};
	public final static
	Tile[]
			PATH_TO_BANK_TILE =
			{new Tile(3205, 3440, 0), new Tile(3191, 3451, 0), new Tile(3175, 3459, 0)};
	public final static
	Tile[]
			PATH_TO_BANK_FROM_TELE =
			{new Tile(3208,3431,0), new Tile(3201,3441,0), new Tile(3194,3450,0), new Tile(3181,3456,0), new Tile(3171,3460,0), new Tile(3167,3464,0)};
	public final static Tile TILE_GRAND_BANK = new Tile(3181, 3502, 0);
	public final static Tile TILE_GRAND_TREE = new Tile(3185, 3509, 0);
	public final static Tile TILE_APE_LADDER_TOP = new Tile(2765, 2703, 0);
	public final static Tile TILE_CHIN_1 = new Tile(2715, 9127, 0);
	public final static Tile TILE_CHIN_2 = new Tile(2746, 9122, 0);
	public final static Tile TILE_CHIN_3 = new Tile(2709, 9116, 0);
	public final static Tile TILE_TREE_DOOR = new Tile(2466, 3491, 0);
	public final static Tile TILE_PRAYER = new Tile(3253, 3485, 0);
	public final static Tile TILE_INSIDE_TREE_DOOR = new Tile(2466, 3493, 0);
	public final static Tile TILE_INSIDE_TREE_DOOR_2 = new Tile(2465, 3493, 0);
	public final static Tile TILE_DAERO = new Tile(2478, 3488, 1);

	//AREA_CHECK_TRAPS_1 traps list
	private final static Tile TRAP_TILE_1 = new Tile(2715, 9138, 0);
	private final static Tile TRAP_TILE_2 = new Tile(2712, 9136, 0);
	private final static Tile TRAP_TILE_3 = new Tile(2708, 9136, 0);
	private final static Tile TRAP_TILE_4 = new Tile(2709, 9134, 0);
	private final static Tile TRAP_TILE_5 = new Tile(2715, 9134, 0);
	private final static Tile TRAP_TILE_6 = new Tile(2717, 9133, 0);
	private final static Tile TRAP_TILE_7 = new Tile(2712, 9133, 0);
	private final static Tile TRAP_TILE_8 = new Tile(2708, 9132, 0);
	private final static Tile TRAP_TILE_9 = new Tile(2711, 9131, 0);
	private final static Tile TRAP_TILE_10 = new Tile(2716, 9131, 0);
	private final static Tile TRAP_TILE_11 = new Tile(2719, 9130, 0);
	private final static Tile TRAP_TILE_12 = new Tile(2713, 9130, 0);

	//AREA_CHECK_TRAPS_2 traps list
	private final static Tile TRAP_TILE_13 = new Tile(2739, 9129, 0);
	private final static Tile TRAP_TILE_14 = new Tile(2743, 9123, 0);
	private final static Tile TRAP_TILE_15 = new Tile(2741, 9125, 0);
	private final static Tile TRAP_TILE_16 = new Tile(2740, 9122, 0);
	private final static Tile TRAP_TILE_17 = new Tile(2737, 9123, 0);
	private final static Tile TRAP_TILE_18 = new Tile(2735, 9122, 0);
	private final static Tile TRAP_TILE_19 = new Tile(2736, 9120, 0);
	private final static Tile TRAP_TILE_20 = new Tile(2742, 9121, 0);
	private final static Tile TRAP_TILE_21 = new Tile(2739, 9119, 0);
	private final static Tile TRAP_TILE_22 = new Tile(2738, 9117, 0);

	//AREA_CHECK_TRAPS_3 traps list
	private final static Tile TRAP_TILE_23 = new Tile(2715, 9119, 0);
	private final static Tile TRAP_TILE_24 = new Tile(2710, 9114, 0);
	private final static Tile TRAP_TILE_25 = new Tile(2707, 9113, 0);
	private final static Tile TRAP_TILE_26 = new Tile(2704, 9113, 0);
	private final static Tile TRAP_TILE_27 = new Tile(2711, 9111, 0);
	private final static Tile TRAP_TILE_28 = new Tile(2709, 9110, 0);
	private final static Tile TRAP_TILE_29 = new Tile(2706, 9109, 0);
	private final static Tile TRAP_TILE_30 = new Tile(2713, 9109, 0);
	private final static Tile TRAP_TILE_31 = new Tile(2703, 9108, 0);
	private final static Tile TRAP_TILE_32 = new Tile(2703, 9106, 0);
	private final static Tile TRAP_TILE_33 = new Tile(2711, 9111, 0);
	private final static Tile TRAP_TILE_34 = new Tile(2706, 9106, 0);
	private final static Tile TRAP_TILE_35 = new Tile(2710, 9106, 0);
	private final static Tile TRAP_TILE_36 = new Tile(2713, 9107, 0);
	private final static Tile TRAP_TILE_37 = new Tile(2711, 9104, 0);
	private final static Tile TRAP_TILE_38 = new Tile(2708, 9103, 0);

	public final static java.util.List<Tile> CHIN_LIST = Arrays.asList(TILE_CHIN_1, TILE_CHIN_2, TILE_CHIN_3);
	public final static
	java.util.List<Tile>
			TRAP_TILE_LIST_AREA_1 =
			Arrays.asList(TRAP_TILE_1,
					             TRAP_TILE_2,
					             TRAP_TILE_3,
					             TRAP_TILE_4,
					             TRAP_TILE_5,
					             TRAP_TILE_5,
					             TRAP_TILE_6,
					             TRAP_TILE_7,
					             TRAP_TILE_8,
					             TRAP_TILE_9,
					             TRAP_TILE_10,
					             TRAP_TILE_11,
					             TRAP_TILE_12);
	public final static
	java.util.List<Tile>
			TRAP_TILE_LIST_AREA_2 =
			Arrays.asList(TRAP_TILE_13,
					             TRAP_TILE_14,
					             TRAP_TILE_15,
					             TRAP_TILE_16,
					             TRAP_TILE_17,
					             TRAP_TILE_18,
					             TRAP_TILE_19,
					             TRAP_TILE_20,
					             TRAP_TILE_21,
					             TRAP_TILE_22);
	public final static
	java.util.List<Tile>
			TRAP_TILE_LIST_AREA_3 =
			Arrays.asList(TRAP_TILE_1,
					             TRAP_TILE_23,
					             TRAP_TILE_24,
					             TRAP_TILE_25,
					             TRAP_TILE_26,
					             TRAP_TILE_27,
					             TRAP_TILE_28,
					             TRAP_TILE_29,
					             TRAP_TILE_30,
					             TRAP_TILE_31,
					             TRAP_TILE_32,
					             TRAP_TILE_33,
					             TRAP_TILE_34,
					             TRAP_TILE_35,
					             TRAP_TILE_36,
					             TRAP_TILE_37,
					             TRAP_TILE_38);

	public final static Area AREA_CHIN_1 = new Area(new Tile(2721, 9132, 0), new Tile(2700, 9126, 0));
	public final static Area AREA_CHIN_2 = new Area(new Tile(2741, 9129, 0), new Tile(2746, 9117, 0));
	public final static Area AREA_CHIN_3_4 = new Area(new Tile(2709, 9116, 0), new Tile(2701, 9111, 0));
	public final static Area AREA_GE = new Area(new Tile(3135, 3460, 0), new Tile(3203, 3516, 0));
	public final static Area AREA_BLINDFOLD_ZONE = new Area(new Tile(2660, 4501, 0), new Tile(2641, 4531, 0));
	public final static Area AREA_CRASH_ISLAND = new Area(new Tile(2880, 2735, 0), new Tile(2903, 2711, 0));
	public final static Area AREA_GRAND_TELE = new Area(new Tile(3208, 3430, 0), new Tile(3217, 2422, 0));
	public final static Area AREA_SPIRIT_MID = new Area(new Tile(2544, 3172, 0), new Tile(2541, 3167, 0));
	public final static Area AREA_GNOME_STRONGHOLD = new Area(new Tile(2470, 3440, 0), new Tile(2457, 3492, 0));
	public final static Area AREA_APE_ATOLL = new Area(new Tile(2809, 2690, 0), new Tile(2753, 2718, 0));
	public final static Area AREA_GNOME_LEVEL_ONE = new Area(new Tile(2490, 3478, 1), new Tile(2440, 3512, 1));
	public final static Area AREA_APE_ATOLL_DUNGEON = new Area(new Tile(2810, 9093, 0), new Tile(2685, 9160, 0));
	public final static Area AREA_SPIRIT_GE = new Area(new Tile(3195, 3506, 0), new Tile(3181, 3515, 0));

	public final static Area AREA_CHECK_TRAPS_1 = new Area(new Tile(2703, 9127, 0), new Tile(2720, 9140, 0));
	public final static Area AREA_CHECK_TRAPS_2 = new Area(new Tile(2734, 9132, 0), new Tile(2745, 9115, 0));
	public final static Area AREA_CHECK_TRAPS_3 = new Area(new Tile(2717, 9120, 0), new Tile(2706, 9098, 0));

}
