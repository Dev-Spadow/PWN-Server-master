package com.arlania.world.content;

import java.util.concurrent.CopyOnWriteArrayList;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.GameObject;
import com.arlania.model.GroundItem;
import com.arlania.model.Item;
import com.arlania.model.Position;
import com.arlania.world.World;
import com.arlania.world.clip.region.RegionClipping;
import com.arlania.world.content.raids.addons.RaidChest;
import com.arlania.world.entity.impl.GroundItemManager;
import com.arlania.world.entity.impl.player.Player;

/**
 * Handles customly spawned objects (mostly global but also privately for players)
 * @author Gabriel Hannason
 */
public class CustomObjects {
	
	private static final int DISTANCE_SPAWN = 70; //Spawn if within 70 squares of distance
	public static final CopyOnWriteArrayList<GameObject> CUSTOM_OBJECTS = new CopyOnWriteArrayList<GameObject>();
	public static final CopyOnWriteArrayList<RaidChest> RAID_CHESTS = new CopyOnWriteArrayList<RaidChest>();
	
	public static void init() {
		for(int i = 0; i < CLIENT_OBJECTS.length; i++) {
			int id = CLIENT_OBJECTS[i][0];
			int x = CLIENT_OBJECTS[i][1];
			int y = CLIENT_OBJECTS[i][2];
			int z = CLIENT_OBJECTS[i][3];
			int face = CLIENT_OBJECTS[i][4];
			GameObject object = new GameObject(id, new Position(x, y, z));
			object.setFace(face);
			RegionClipping.addObject(object);
		}
		for(int i = 0; i < CUSTOM_OBJECTS_SPAWNS.length; i++) {
			int id = CUSTOM_OBJECTS_SPAWNS[i][0];
			int x = CUSTOM_OBJECTS_SPAWNS[i][1];
			int y = CUSTOM_OBJECTS_SPAWNS[i][2];
			int z = CUSTOM_OBJECTS_SPAWNS[i][3];
			int face = CUSTOM_OBJECTS_SPAWNS[i][4];
			GameObject object = new GameObject(id, new Position(x, y, z));
			object.setFace(face);
			CUSTOM_OBJECTS.add(object);
			World.register(object);
		}
	}
	
	private static void handleList(GameObject object, String handleType) {
		switch(handleType.toUpperCase()) {
		case "DELETE":
			for(GameObject objects : CUSTOM_OBJECTS) {
				if(objects.getId() == object.getId() && object.getPosition().equals(objects.getPosition())) {
					CUSTOM_OBJECTS.remove(objects);
				}
			}
			break;
		case "ADD":
			if(!CUSTOM_OBJECTS.contains(object)) {
				CUSTOM_OBJECTS.add(object);
			}
			break;
		case "EMPTY":
			CUSTOM_OBJECTS.clear();
			break;
		}
	}

	private static void handleList(RaidChest object, String handleType) {
		switch (handleType.toUpperCase()) {
		case "DELETE":
			for (RaidChest objects : RAID_CHESTS) {
				if (objects.getId() == object.getId() && object.getPosition().equals(objects.getPosition())) {
					RAID_CHESTS.remove(objects);
				}
			}
			break;
		case "ADD":
			if (!RAID_CHESTS.contains(object)) {
				RAID_CHESTS.add(object);
			}
			break;
		case "EMPTY":
			RAID_CHESTS.clear();
			break;
		}
	}
	
	public static RaidChest getRaidChest(Position position) {
		for(RaidChest chest : RAID_CHESTS) {
			if(chest.getPosition().sameAs(position)) {
				if(chest instanceof RaidChest) {
					return (RaidChest) chest;
				}
			}
		}
		return null;
	}
	
	public static void deleteGlobalObjectWithinDistance(RaidChest object) {
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			if (object.getPosition().isWithinDistance(player.getPosition(), DISTANCE_SPAWN)) {
				deleteObject(player, object);
			}
		}
	}
	
	public static void spawnGlobalObjectWithinDistance(RaidChest object) {
		handleList(object, "add");
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			if (object.getPosition().isWithinDistance(player.getPosition(), DISTANCE_SPAWN)) {
				spawnObject(player, object);
			}
		}
	}
	
	public static void deleteObject(Player p, RaidChest object) {
		handleList(object, "delete");
		p.getPacketSender().sendObjectRemoval(object);
		if (RegionClipping.objectExists(object)) {
			RegionClipping.removeObject(object);
		}
	}
	
	public static void spawnObject(Player p, RaidChest object) {
		if (object.getId() != -1) {
			p.getPacketSender().sendObject(object);
			if (!RegionClipping.objectExists(object)) {
				RegionClipping.addObject(object);
			}
		} else {
			deleteObject(p, object);
		}
	}
	public static void spawnObject(Player p, GameObject object) {
		if(object.getId() != -1) {
			p.getPacketSender().sendObject(object);
			if(!RegionClipping.objectExists(object)) {
				RegionClipping.addObject(object);
			}
		} else {
			deleteObject(p, object);
		}
	}
	
	public static void deleteObject(Player p, GameObject object) {
		p.getPacketSender().sendObjectRemoval(object);
		if(RegionClipping.objectExists(object)) {
			RegionClipping.removeObject(object);
		}
	}
	
	public static void deleteGlobalObject(GameObject object) {
		handleList(object, "delete");
		World.deregister(object);
	}

	public static void spawnGlobalObject(GameObject object) {
		handleList(object, "add");
		World.register(object);
	}
	
	public static void spawnGlobalObjectWithinDistance(GameObject object) {
		for(Player player : World.getPlayers()) {
			if(player == null)
				continue;
			if(object.getPosition().isWithinDistance(player.getPosition(), DISTANCE_SPAWN)) {
				spawnObject(player, object);
			}
		}
	}
	
	public static void deleteGlobalObjectWithinDistance(GameObject object) {
		for(Player player : World.getPlayers()) {
			if(player == null)
				continue;
			if(object.getPosition().isWithinDistance(player.getPosition(), DISTANCE_SPAWN)) {
				deleteObject(player, object);
			}
		}
	}
	
		public static boolean objectExists(Position pos) {
			return getGameObject(pos) != null;
		}

		public static GameObject getGameObject(Position pos) {
			for(GameObject objects : CUSTOM_OBJECTS) {
				if(objects != null && objects.getPosition().equals(pos)) {
					return objects;
				}
			}
			return null;
		}

		public static void handleRegionChange(Player p) {
			for(GameObject object: CUSTOM_OBJECTS) {
				if(object == null)
					continue;
				if(object.getPosition().isWithinDistance(p.getPosition(), DISTANCE_SPAWN)) {
					spawnObject(p, object);
				}
			}
		}
	
		public static void objectRespawnTask(Player p, final GameObject tempObject, final GameObject mainObject, final int cycles) {
			deleteObject(p, mainObject);
			spawnObject(p, tempObject);
			TaskManager.submit(new Task(cycles) {
				@Override
				public void execute() {
					deleteObject(p, tempObject);
					spawnObject(p, mainObject);
					this.stop();
				}
			});
		}
		
		public static void globalObjectRespawnTask(final GameObject tempObject, final GameObject mainObject, final int cycles) {
			deleteGlobalObject(mainObject);
			spawnGlobalObject(tempObject);
			TaskManager.submit(new Task(cycles) {
				@Override
				public void execute() {
					deleteGlobalObject(tempObject);
					spawnGlobalObject(mainObject);
					this.stop();
				}
			});
		}

		public static void globalObjectRemovalTask(final GameObject object, final int cycles) {
			spawnGlobalObject(object);
			TaskManager.submit(new Task(cycles) {
				@Override
				public void execute() {
					deleteGlobalObject(object);
					this.stop();
				}
			});
		}

	public static void globalFiremakingTask(final GameObject fire, final Player player, final int cycles) {
		spawnGlobalObject(fire);
		TaskManager.submit(new Task(cycles) {
			@Override
			public void execute() {
				deleteGlobalObject(fire);
				if(player.getInteractingObject() != null && player.getInteractingObject().getId() == 2732) {
					player.setInteractingObject(null);
				}
				this.stop();
			}
			@Override
			public void stop() {
				setEventRunning(false);
				GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(592), fire.getPosition(), player.getUsername(), false, 150, true, 100));
			}
		});
	}
	
	/**
	 * Contains
	 * @param ObjectId - The object ID to spawn
	 * @param absX - The X position of the object to spawn
	 * @param absY - The Y position of the object to spawn
	 * @param Z - The Z position of the object to spawn
	 * @param face - The position the object will face
	 */
	
	//Only adds clips to these objects, they are spawned clientsided
	//NOTE: You must add to the client's customobjects array to make them spawn, this is just clipping!
	private static final int[][] CLIENT_OBJECTS = {

			{567, 2465, 4075, 0, 1}, //statue
			{562, 2481, 4049, 0, 1}, //statue
			{563, 2448, 4041, 0, 1}, //statue
			{564, 2480, 4023, 0, 1}, //statue
			{565, 2443, 4012, 0, 1}, //statue

			{2213, 3282, 4055, 0, 1}, //bank
			{2213, 3282, 4054, 0, 1}, //bank
			{2213, 3282, 4053, 0, 1}, //bank
			{2213, 3282, 4051, 0, 1}, //bank
			{2213, 3287, 4050, 0, 1}, //bank
			{2213, 3286, 4050, 0, 1}, //bank
			{2213, 3285, 4050, 0, 1}, //bank
			{2213, 3284, 4050, 0, 1}, //bank
			{2213, 3283, 4050, 0, 1}, //bank

			{2213, 3304, 4050, 0, 1}, //bank
			{2213, 3305, 4050, 0, 1}, //bank
			{2213, 3306, 4050, 0, 1}, //bank
			{2213, 3307, 4050, 0, 1}, //bank
			{2213, 3308, 4050, 0, 1}, //bank
			{2213, 3309, 4055, 0, 1}, //bank
			{2213, 3309, 4054, 0, 1}, //bank
			{2213, 3309, 4053, 0, 1}, //bank
			{2213, 3309, 4052, 0, 1}, //bank
			{2213, 3309, 4051, 0, 1}, //bank

			{2213, 3309, 4072, 0, 1}, //bank
			{2213, 3309, 4073, 0, 1}, //bank
			{2213, 3309, 4074, 0, 1}, //bank
			{2213, 3309, 4075, 0, 1}, //bank
			{2213, 3309, 4076, 0, 1}, //bank

			{2213, 3304, 4077, 0, 1}, //bank
			{2213, 3305, 4077, 0, 1}, //bank
			{2213, 3306, 4077, 0, 1}, //bank
			{2213, 3307, 4077, 0, 1}, //bank
			{2213, 3308, 4077, 0, 1}, //bank

			{2213, 3287, 4077, 0, 1}, //bank
			{2213, 3286, 4077, 0, 1}, //bank
			{2213, 3285, 4077, 0, 1}, //bank
			{2213, 3284, 4077, 0, 1}, //bank
			{2213, 3283, 4077, 0, 1}, //bank

			{2213, 3282, 4072, 0, 1}, //bank
			{2213, 3282, 4073, 0, 1}, //bank
			{2213, 3282, 4074, 0, 1}, //bank
			{2213, 3282, 4075, 0, 1}, //bank
			{2213, 3282, 4076, 0, 1}, //bank

			{28780, 3310, 4057, 0, 3},
			{4164, 3320, 4057, 0, 1},
			{2160, 3310, 4059, 0, 3},
			{172, 3311, 4072, 0, 2},
			{15541, 3310, 4068, 0, 3},
			{3009, 3299, 4056, 0, 3},
			{2021, 3320, 4067, 0, 1},
			{4483, 3320, 4069, 0, 1},
			{4163, 3301, 4049, 0, 0},
			{6195, 3301, 4039, 0, 0},
			{6195, 3117, 4063, 0, 1},
			{3005, 3288, 4067, 0, 0},
			{10091, 3368, 3999, 0, 0}, //Rocktail fishing 
			{10091, 3346, 3987, 0, 0}, //Rocktail fishing 
			{6420, 3313, 4072, 0, 2},
			{7350, 3315, 4072, 0, 2},
			{4123, 3317, 4072, 0, 2},
			{4170, 3319, 4072, 0, 2},
			{411, 3209, 3197, 0, 0}, //delete
			{409, 3209, 3211, 0, 2},
			{411, 3796, 3545, 0, 0}, //starterzone altar
			{409, 3793, 3545, 0, 0}, //starterzone altar
	
			//ironman zone
			{2213, 3251, 9637, 0, 1}, //bank
			{2213, 3251, 9636, 0, 1}, //bank
			{2213, 3251, 9635, 0, 1}, //bank
			{2213, 3251, 9634, 0, 1}, //bank
			{2213, 3251, 9633, 0, 1}, //bank
			
			{411, 3250, 9829, 0, 2}, //delete
			{409, 3250, 9841, 0, 2},
			
			{4123, 2600, 3132, 0, 3}, //minigame chest
			
			{2213, 3183, 3211, 0, 1}, //bank
			{2213, 3183, 3210, 0, 1}, //bank
			{2213, 3183, 3209, 0, 1}, //bank
			{2213, 3183, 3208, 0, 1}, //bank
			{2213, 3183, 3200, 0, 1}, //bank
			{2213, 3183, 3199, 0, 1}, //bank
			{2213, 3183, 3198, 0, 1}, //bank
			{2213, 3183, 3197, 0, 1}, //bank
			
			{3662, 2825, 3198, 0, 3}, //superior coffin
			{3449, 2860, 3198, 0, 1}, //superior coffin
			{2021, 3086, 3218, 0, 0}, //Forge table
			{884, 3099, 3214, 0, 0},
			
			{884, 3105, 3214, 0, 0},
			{2213, 3215, 3211, 0, 3}, //bank
			{2213, 3215, 3210, 0, 3}, //bank
			{2213, 3215, 3209, 0, 3}, //bank
			{2213, 3215, 3208, 0, 3}, //bank
			{2213, 3215, 3200, 0, 3}, //bank
			{2213, 3215, 3199, 0, 3}, //bank
			{2213, 3215, 3198, 0, 3}, //bank
			{2213, 3215, 3197, 0, 3}, //bank
			
			{3005, 3092, 3216, 0, 0}, //bank
			{3006, 3091, 3214, 0, 2}, //bank
			{3010, 3089, 3214, 0, 2}, //bank
			{3008, 3087, 3214, 0, 2}, //bank
			{4483, 3089, 3219, 0, 0}, //bank
			{411, 3188, 3211, 0, 2}, //delete
			{409, 3188, 3197, 0, 0},
			
			
			//arch
			{50306, 2742, 3437, 0, 1},
			{50306, 2747, 3438, 0, 1},
			{50306, 2740, 3438, 0, 1},
			//arch
			{50307, 2658, 2660, 0, 1},
			{50307, 2655, 2662, 0, 1},
			{50307, 2659, 2663, 0, 1},
			//arch
			{50305, 3034, 3305, 0, 1},
			{50305, 3030, 3307, 0, 1},
			{50305, 3038, 3307, 0, 1},
			{5542, 3207, 3151, 0, 0}, 
			{15541, 3095, 3245, 0, 0}, 
			//arch banks
			{2213, 2647, 3161, 0, 0}, //bank
			{2213, 2648, 3161, 0, 0}, //bank
			{2213, 2649, 3161, 0, 0}, //bank
			{2213, 2650, 3161, 0, 0}, //bank
			{2213, 2651, 3161, 0, 0}, //bank
			
			{48673, 2651, 3158, 0, 0}, //bank
			{48673, 2647, 3158, 0, 0}, //bank
			
			{48661, 2658, 3149, 0, 0},
			
			//bone pile
			{44379, 2646, 3152, 0, 0}, //bank
			{44379, 2647, 3149, 0, 0}, //bank
			
			{44379, 2650, 3151, 0, 0},
			
			{18399, 2652, 3159, 0, 0}, //bank
			{18399, 2646, 3159, 0, 0}, //bank
			
			/*** Bas Extreme Donator zone ***/
			
			{47180, 3413, 2919, 0, 0}, //Frost drags teleport
			
			{4875, 3451, 2881, 0, 0}, //Thief stalls
			{4874, 3450, 2881, 0, 0}, //Thief stalls
			{4876, 3449, 2881, 0, 0}, //Thief stalls
			{4877, 3448, 2881, 0, 0}, //Thief stalls
			{4878, 3447, 2881, 0, 0}, //Thief stalls
			{13493, 3446, 2881, 0, 0}, //Thief stalls
			{4875, 3200, 3431, 0, 0}, //theif stalls
			{4874, 3200, 3432, 0, 0}, //theif stalls
			{4876, 3200, 3433, 0, 0}, //theif stalls
			{4877, 3200, 3434, 0, 0}, //theif stalls
			{4878, 3200, 3435, 0, 0}, //theif stalls
			//theif stalls
			
			{2403, 2596, 5732, 0, 2},
			{2403, 2596, 5723, 0, 0},
			
			// all the new minigame objects
			{2469, 2975, 2552, 0, 0},
			{53, 2583, 4620, 0, 0},
			{52, 2582, 4620, 0, 0},
			
			/*** Home zone ***/
			{2213, 2194, 4976, 0, 0}, //bank
			{2213, 2195, 4976, 0, 0}, //bank
			{2213, 2196, 4976, 0, 0}, //bank
			{2213, 2197, 4976, 0, 0}, //bank
			{2213, 2198, 4976, 0, 0}, //bank
			{2213, 2199, 4976, 0, 0}, //bank
			{2213, 2200, 4976, 0, 0}, //bank
			{2213, 2201, 4976, 0, 0}, //bank
			{2213, 2202, 4976, 0, 0}, //bank
			{2213, 2203, 4976, 0, 0}, //bank
			
			
			{2213, 2209, 4976, 0, 0}, //bank
			{2213, 2210, 4976, 0, 0}, //bank
			{2213, 2211, 4976, 0, 0}, //bank
			{2213, 2212, 4976, 0, 0}, //bank
			{2213, 2213, 4976, 0, 0}, //bank
			{2213, 2214, 4976, 0, 0}, //bank
			{2213, 2215, 4976, 0, 0}, //bank
			{2213, 2216, 4976, 0, 0}, //bank
			{2213, 2217, 4976, 0, 0}, //bank
			{2213, 2218, 4976, 0, 0}, //bank
			
			
			
			
			//investor zone chests
			{4114, 2338, 3686, 0, 3},//vip chest
			{7350, 2340, 3686, 0, 1},//donator chest
			{ 2995, 2332, 3686, 0, 4},//uber chest
			{49347, 2334, 3686, 0, 0}, // Deluxe Chest
			
			{2213, 3218, 2756, 0, 0}, //delzone
			{2213, 3217, 2756, 0, 0}, //delzone
			{2213, 3216, 2756, 0, 0}, //delzone
			{2213, 3215, 2756, 0, 0}, //delzone
			{2213, 3214, 2756, 0, 0}, //delzone
			{2213, 3228, 2806, 0, 1}, //bank
			
			{49347, 3223, 2771, 0, 0}, // Deluxe Chest
			{4114, 3306, 5289, 0, 0}, // Vip Chest
			{2213, 3293, 5285, 0, 1}, // Vip bank
			{2213, 3293, 5284, 0, 1}, // Vip bank
			{2213, 3293, 5283, 0, 1}, // Vip bank
			{2160, 3116, 3219, 0, 0}, //lOL RAIDS
			{6970, 3206, 3144, 0, 2}, //lOL RAIDS
			{49347, 3219, 2771, 0, 0}, // Deluxe Chest
			{49347, 3223, 2789, 0, 2}, // Deluxe Chest
			{49347, 3219, 2789, 0, 2}, // Deluxe Chest
			// raids portal
			//{ 4389, 2329, 3674, 0, 1 }, // raids portal
			/*
			* Remove Uber Zone Objects
			*/
			{-1, 2425, 4714, 0, 0},
			{-1, 2420, 4716, 0, 0},
			{-1, 2426, 4726, 0, 0},
			{-1, 2420, 4709, 0, 0},
			{-1, 2419, 4698, 0, 0},
			{-1, 2420, 4700, 0, 0},
			{-1, 2399, 4721, 0, 0},
			{-1, 2398, 4721, 0, 0},
			{-1, 2399, 4720, 0, 0},
			{-1, 2395, 4722, 0, 0},
			{-1, 2398, 4717, 0, 0},
			{-1, 2396, 4717, 0, 0},
			{-1, 2396, 4718, 0, 0},
			{-1, 2396, 4719, 0, 0},
			{-1, 2395, 4718, 0, 0},
			{-1, 2394, 4711, 0, 0},
			{-1, 2396, 4711, 0, 0},
			{-1, 2397, 4711, 0, 0},
			{-1, 2397, 4713, 0, 0},
			{-1, 2399, 4713, 0, 0},
			{-1, 2402, 4726, 0, 0},
			{-1, 2407, 4728, 0, 0},
			{-1, 2405, 4724, 0, 0},
			{-1, 2409, 4705, 0, 0},
			{-1, 2410, 4704, 0, 0},
			{-1, 2407, 4702, 0, 0},
			{-1, 2407, 4701, 0, 0},
			{-1, 2408, 4701, 0, 0},
			{-1, 2412, 4701, 0, 0},
			{-1, 2413, 4701, 0, 0},
			{-1, 2414, 4703, 0, 0},
			{-1, 2416, 4714, 0, 0},
			{-1, 2412, 4732, 0, 0},
			{-1, 2413, 4729, 0, 0},
			{-1, 2414, 4733, 0, 0},
			{-1, 2415, 4730, 0, 0},
			{-1, 2416, 4730, 0, 0},
			{-1, 2416, 4731, 0, 0},
			{-1, 2419, 4731, 0, 0},
			{-1, 2420, 4731, 0, 0},
			{-1, 2420, 4732, 0, 0},
			{-1, 2415, 4725, 0, 0},
			{-1, 2417, 4729, 0, 0},
			{-1, 2418, 4727, 0, 0},
			{-1, 2418, 4723, 0, 0},
			{-1, 2419, 4722, 0, 0},
			{-1, 2420, 4726, 0, 0},
			{-1, 2415, 4725, 0, 0},
			{-1, 2417, 4729, 0, 0},
			{-1, 2418, 4727, 0, 0},
			{-1, 2418, 4723, 0, 0},
			{-1, 2419, 4722, 0, 0},
			{-1, 2420, 4726, 0, 0},
			{8972, 2180, 5080, 0, 0},
			{2469, 2199, 5099, 0, 0},
			{11434, 2186, 5089, 0, 0},
			{11434, 2185, 5083, 0, 0},
			{11434, 2188, 5077, 0, 0},
			{1306, 2191, 5081, 0, 0},
			{1306, 2190, 5086, 0, 0},
			{1306, 2192, 5089, 0, 0},
			{1306, 2188, 5092, 0, 0},
			{9032, 2193, 5084, 0, 0},
			{9032, 2195, 5086, 0, 0},
			{9032, 2195, 5088, 0, 0},
			{9032, 2193, 5092, 0, 0},
			{9032, 2192, 5094, 0, 0},
			{9032, 2188, 5094, 0, 0},
			{11231, 2771, 9340, 0, 3}, // Last Man Standing Object (Chest)
			{11231, 2788, 9320, 0, 3}, // Last Man Standing Object (Chest)
			{11231, 2809, 9338, 0, 3}, // Last Man Standing Object (Chest)
			{11231, 2809, 9307, 0, 3}, // Last Man Standing Object (Chest)
			{11231, 2785, 9307, 0, 3}, // Last Man Standing Object (Chest)
			{11231, 2769, 9297, 0, 3}, // Last Man Standing Object (Chest)
			
			//{2470, 2317, 3670, 0, 0},
			
			{2183, 2491, 10165, 0, 3},//Dungeon Chest
			
			{4306, 3431, 2872, 0, 2}, //Anvil
			{6189, 3433, 2871, 0, 3}, //Furnace
			
			{10091, 3452, 2871, 0, 0}, //Rocktail fishing 
			{10091, 3449, 2867, 0, 0}, //Rocktail fishing 
			
			{14859, 3439, 2872, 0, 0}, //Rune ore 
			{14859, 3442, 2871, 0, 0}, //Rune ore 
			{14859, 3444, 2870, 0, 0}, //Rune ore 
			{14859, 3445, 2868, 0, 0}, //Rune ore 
			{14859, 3443, 2869, 0, 0}, //Rune ore 
			{14859, 3441, 2869, 0, 0}, //Rune ore 
			{14859, 3439, 2870, 0, 0}, //Rune ore 
			{14859, 3437, 2872, 0, 0}, //Rune ore 
			//afk zone ore rocks
			{14859, 3231, 2806, 0, 0}, //Rune ore 
			{14859, 3232, 2805, 0, 0}, //Rune ore 
			{14859, 3232, 2804, 0, 0}, //Rune ore 
			{14859, 3232, 2803, 0, 0}, //Rune ore 
			{14859, 3231, 2802, 0, 0}, //Rune ore 

			//afk zone rocktail fishing
			{10091, 3227, 2793, 0, 0}, //Rocktail fishing 
			{10091, 3223, 2793, 0, 0}, //Rocktail fishing
			//afkzone
			{4306, 3226, 2805, 0, 1}, //Anvil
			{6189, 3222, 2805, 0, 1}, //Furnace
			
			{1306, 3422, 2870, 0, 0}, //Magic tree's 
			{1306, 3422, 2872, 0, 0}, //Magic tree's 
			{1306, 3422, 2874, 0, 0}, //Magic tree's 
			{1306, 3422, 2876, 0, 0}, //Magic tree's 
			{1306, 3422, 2878, 0, 0}, //Magic tree's 
			{1306, 3422, 2880, 0, 0}, //Magic tree's 
			{1306, 3422, 2882, 0, 0}, //Magic tree's 
			
			/**
			 * Minigame
			 */
			
			{23095, 2457, 10164, 0, 1}, //portal room 2
			{13619, 2460, 10136, 0, 1}, //portal room 1
			{13619, 2460, 10135, 0, 1}, //portal room 1
			
			
			{10804, 3213, 3223, 0, 0}, //portal for raids
			{2469, 3213, 3218, 0, 0}, //portal for raids
			//{42611, 2978, 2552, 0, 0}, //portal for raids

			{4407, 3418, 3308, 0, 1}, //Scorpia Lair
			{4407, 3425, 3308, 0, 1}, //Scorpia Lair
			{4407, 3432, 3308, 0, 1}, //Scorpia Lair
			
			{4389, 3435, 3306, 0, 1}, //Skotizo Lair
			{4389, 3435, 3299, 0, 1}, //Skotizo Lair
			{4389, 3435, 3293, 0, 1}, //Skotizo Lair
			
			{4406, 3415, 3293, 0, 1}, //Vet'ion Lair
			{4406, 3415, 3299, 0, 1}, //Vet'ion Lair
			{4406, 3415, 3306, 0, 1}, //Vet'ion Lair
			
			{4390, 3432, 3291, 0, 1}, //KBD Lair
			{4390, 3425, 3291, 0, 1}, //KBD Lair
			{4390, 3418, 3291, 0, 1}, //KBD Lair
			
			{8416, 2574, 3112, 0, 0}, //afk tree
			{38660, 2570, 3112, 0, 0}, //afk tree
			{38660, 2567, 3114, 0, 0}, //afk tree
			{38660, 2567, 3117, 0, 0}, //afk tree
			
			{38660, 2586, 3118, 0, 0}, //afk rock
			{38660, 2586, 3114, 0, 0}, //afk rock
			{38660, 2582, 3111, 0, 0}, //afk rock
			{38660, 2578, 3111, 0, 0}, //afk rock
			
			{1306, 2241, 3741, 0, 0}, //Magic tree's 
			{1306, 2241, 3738, 0, 0}, //Magic tree's 
			{1306, 2241, 3735, 0, 0}, //Magic tree's 
			
			{14859, 2242, 3751, 0, 0}, //Rune ore 
			{14859, 2242, 3750, 0, 0}, //Rune ore 
			{14859, 2242, 3749, 0, 0}, //Rune ore 
			{14859, 2242, 3748, 0, 0}, //Rune ore 
			{14859, 2242, 3747, 0, 0}, //Rune ore 
			{14859, 2242, 3746, 0, 0}, //Rune ore 
			{14859, 2242, 3745, 0, 0}, //Rune ore 
			{14859, 2242, 3752, 0, 0}, //Rune ore 
			{14859, 2242, 3753, 0, 0}, //Rune ore 
			
			{3192, 3087, 3504, 0, 2}, //Scoreboard
			
			{409, 3443, 2918, 0, 2}, //Altar 
			{ 6552, 2337, 3657, 0, 2},
			{8749, 3445, 2913, 0, 3}, //Altar 
			{411, 3441, 2910, 0, 0}, //Altar 
			{13179, 3439, 2912, 0, 2}, //Altar 
			
			{409, 2331, 3652, 0, 0}, //delAltar 
			{411, 2337, 3652, 0, 0}, //delAltar 
			//{10660, 3209, 2781, 0, 0}, // Custom Deluxe Tree
			{4090, 3212, 2771, 0, 1}, // special altar
			{10804, 2971, 2545, 0, 1}, //trapdoor 1
			

			
			
			//random zone to make one day
			//{26301, 3309, 9807, 0, 2},

			
			
			{2213, 3425, 2930, 0, 0}, //Banks
			{2213, 3426, 2930, 0, 0}, //Banks
			{2213, 3427, 2930, 0, 0}, //Banks
			{2213, 3428, 2930, 0, 0}, //Banks
			
			{2213, 3426, 2894, 0, 1}, //Banks
			{2213, 3426, 2893, 0, 1}, //Banks
			{2213, 3426, 2892, 0, 1}, //Banks
			{2213, 3426, 2891, 0, 1}, //Banks
			{2213, 3426, 2890, 0, 1}, //Banks
			{2213, 3426, 2889, 0, 1}, //Banks
		
			
			{10503, 3456, 2876, 0, 0}, //rocks to fix random wall
			{10503, 3456, 2877, 0, 0}, //rocks to fix random wall
			{10503, 3456, 2878, 0, 0}, //rocks to fix random wall
			{10503, 3456, 2879, 0, 0}, //rocks to fix random wall		
			{10503, 3446, 2889, 0, 0}, //rocks to fix random wall
			{10503, 3438, 2904, 0, 0}, //rocks to fix random wall
			{10503, 3415, 2917, 0, 0}, //rocks to fix random wall
			{10503, 3411, 2925, 0, 0}, //rocks to fix random wall
			
			/*** Bas Extreme Donator zone end ***/
			{2646, 3207, 3162, 0, 0}, // flax
			{2646, 3205, 3159, 0, 0}, // flax
			{2646, 3207, 3158, 0, 0}, // flax
			{2646, 3207, 3155, 0, 0}, // flax
			{2646, 3205, 3156, 0, 0}, // flax
			{2646, 3205, 3164, 0, 0}, // flax
			{2646, 3205, 3162, 0, 0}, // flax
			
			/*** Deluxe Zone ***/
			{49347, 1916, 4645, 0, 0}, // Deluxe Chest
			{49347, 1916, 4641, 0, 0}, // Deluxe Chest
			{10660, 1906, 4655, 0, 0}, // Custom Deluxe Tree
			{10660, 1906, 4649, 0, 0}, // Custom Deluxe Tree
			{4090, 3291, 5277, 0, 1},
			
		/***investor zone portals ***/

			
			
			{30141, 1656, 5685, 0, 0}, //minigame port
			{30141, 1658, 5683, 0, 0}, //minigame port
			{30141, 1656, 5681, 0, 0}, //minigame port
			{30141, 1654, 5683, 0, 0}, //minigame port
			
			
			{30141, 1674, 5688, 0, 0}, //minigame port
			{30141, 1676, 5686, 0, 0}, //minigame port
			{30141, 1678, 5688, 0, 0}, //minigame port
			{30141, 1676, 5690, 0, 0}, //minigame port
			
			
			{30141, 1667, 5706, 0, 0}, //minigame port
			{30141, 1669, 5704, 0, 0}, //minigame port
			{30141, 1667, 5702, 0, 0}, //minigame port
			{30141, 1665, 5704, 0, 0}, //minigame port
			
			{30141, 1651, 5701, 0, 0}, //minigame port
			{30141, 1653, 5703, 0, 0}, //minigame port
			{30141, 1651, 5705, 0, 0}, //minigame port
			{30141, 1649, 5703, 0, 0}, //minigame port
			
			{30146, 1656, 5683, 0, 0}, //minigame port
			/*** Bas Gambling area ***/
			{2213, 2729, 3467, 0, 3}, //bank
			{2213, 2729, 3468, 0, 3}, //bank
			{2213, 2729, 3469, 0, 3}, //bank
			{2213, 2729, 3470, 0, 3}, //bank
			{2213, 2729, 3471, 0, 3}, //bank
			
			/*** Bas Vip Zone ***/
			
			{10660, 1905, 4651, 0, 0}, // Custom Deluxe Tree
			{10660, 1905, 4657, 0, 0}, // Custom Deluxe Tree
			{4090, 1929, 4663, 0, 0}, // Special altar
			{409, 1931, 4663, 0, 0}, // Alter
			{411, 1934, 4663, 0, 0}, // Chaos altar
			{1306, 1898, 4652, 0, 0}, //Magic tree's 
			{1306, 1898, 4655, 0, 0}, //Magic tree's 
			{1306, 1898, 4658, 0, 0}, //Magic tree's 
			{1306, 1898, 4661, 0, 0}, //Magic tree's  
			{8702, 1897, 4639, 0, 0}, //Rocktail fishing 
			{8702, 1897, 4641, 0, 0}, //Rocktail fishing
			
			
			/*** vip dung banks ***/
			{2213, 2671, 10398, 0, 3}, //bank
			{2213, 2671, 10397, 0, 3}, //bank
			{2213, 2671, 10396, 0, 3}, //bank
			{2213, 2671, 10395, 0, 3}, //bank
			{2213, 2671, 10394, 0, 3}, //bank
			// vip dungeon magic trees
			{1306, 2664, 10393, 0, 0}, //Magic tree's 
			{1306, 2665, 10398, 0, 0}, //Magic tree's 
			{1306, 2654, 10371, 0, 0}, //Magic tree's 
			{1306, 2662, 10371, 0, 0}, //Magic tree's 
			{1306, 2666, 10411, 0, 0}, //Magic tree's 
			{1306, 2642, 10410, 0, 0}, //Magic tree's 
			//vip chest dung
			{4114, 2662, 10389, 0, 3}, // Vip Chest
			//altars
			{409, 2668, 10393, 0, 0}, // Alter
			{411, 2668, 10399, 0, 0}, // Chaos altar
			//vip evil tree
			//{11434, 2656, 10378, 0, 3},
			//{11434, 2661, 10376, 0, 3},
			
			//fountain vip zone
			{13479, 2657, 10411, 0, 0},
			//evil tree afk zone
			//{11434, 3210, 2784, 0, 0},
			//{11434, 3232, 2778, 0, 4},
			//{11434, 3232, 2785, 0, 4},
			
			{42220, 3111, 3249, 0, 3}, //afk portal
			{42220, 2662, 10407, 0, 1},
			//undead tree cutting
			{8416, 3227, 2796, 0, 0}, 
			{8416, 3227, 2794, 0, 0}, 
			//{8416, 3227, 2792, 0, 0},  
			
			//afk zone bank
			{2213, 2576, 3133, 0, 0}, //bank
			{2213, 2577, 3133, 0, 0}, //bank
			{2213, 2578, 3133, 0, 0}, //bank
			{2213, 2579, 3133, 0, 0}, //bank
			{2213, 2580, 3133, 0, 0}, //bank
			{2213, 2581, 3133, 0, 0}, //bank
			{2213, 2582, 3133, 0, 0}, //bank
			{2213, 2583, 3133, 0, 0}, //bank
			//crystal tree cutting
			{8416, 2662, 10400, 0, 0}, 
			{8416, 2662, 10402, 0, 0}, 
			{8416, 2662, 10404, 0, 0},  
			{8416, 2662, 10406, 0, 0},
			//{8416, 2662, 10408, 0, 0},
			
			//kid buu portal
			{42219, 2201, 4984, 0, 0},
			
			//slayer boss portal invesotzone
			
			//slayer boss portal
			
			//vip slayer boss portal
			
			// upgrade chest
			{12451, 2851, 5146, 0, 1},
			
			// upgrade c portal
			{2879, 2848, 5151, 0, 0},
			
			// Blood Fountain
			{4163, 3114, 3244, 0, 0},
			//Milos dream
			{4164, 3115, 3214, 0, 2},
			
			//Milos dream
			{4406, 3425, 3296, 0, 1},
			{6606, 3114, 9843, 0, 1},
			{13635, 2788, 9248, 0, 1},
			{13635, 2717, 9190, 0, 1},
			{13635, 2272, 4689, 0, 1},
			{13635, 2711, 9248, 0, 1},
			{3009, 3086, 3216, 0, 3},
			//new raids skilling zone 1.
			{9031, 2840, 3359, 0, 0}, 
			{9031, 2839, 3359, 0, 0}, 
			{9031, 2838, 3359, 0, 0},  
			{9031, 2837, 3359, 0, 0},
			{9031, 2836, 3359, 0, 0},
			
			{9031, 2835, 3359, 0, 0}, 
			{9031, 2834, 3359, 0, 0}, 
			{9031, 2833, 3359, 0, 0},  
			{9031, 2832, 3359, 0, 0},
			{9031, 2831, 3359, 0, 0},
			{9031, 2830, 3359, 0, 0}, 
		
			
			//raids tree
			
			{8417, 2827, 3382, 0, 3}, 
			//{8417, 2828, 3382, 0, 3}, 
			{8417, 2829, 3382, 0, 3},  
			//{8417, 2830, 3382, 0, 3},
			{8417, 2831, 3382, 0, 3},
			
			//{8417, 2832, 3382, 0, 3}, 
			{8417, 2833, 3382, 0, 3}, 
			//{8417, 2834, 3382, 0, 3},  
			{8417, 2835, 3382, 0, 3},
			//{8417, 2836, 3382, 0, 3},
			{8417, 2837, 3382, 0, 3},
			
			
			// tools crate
			{48629, 2846, 3358, 0, 0},
			
			
	
		 
			
		
			// tools crate
			{48629, 2875, 3326, 0, 0},
			
			
			
			
			
			
			
			
			
			
			
			
			
			{2213, 1906, 4639, 0, 1}, //bank
			{2213, 1906, 4640, 0, 1}, //bank
			{2213, 1906, 4641, 0, 1}, //bank
			{2213, 1906, 4642, 0, 1}, //bank
			{2213, 1905, 4643, 0, 0}, //bank
			{2213, 1904, 4643, 0, 0}, //bank
			{2213, 1903, 4642, 0, 1}, //bank
			{2213, 1903, 4641, 0, 1}, //bank
			{2213, 1903, 4640, 0, 1}, //bank
			{2213, 1903, 4639, 0, 1}, //bank
			{2213, 1904, 4638, 0, 0}, //bank
			{2213, 1905, 4638, 0, 0}, //bank
			{2213, 1945, 4642, 0, 1}, //bank
			{2213, 1945, 4641, 0, 1}, //bank
			{2213, 1945, 4640, 0, 1}, //bank
			{2213, 1945, 4639, 0, 1}, //bank
			{2213, 1944, 4638, 0, 0}, //bank
			{2213, 1943, 4638, 0, 0}, //bank
			{2213, 1942, 4639, 0, 1}, //bank
			{2213, 1942, 4640, 0, 1}, //bank
			{2213, 1942, 4641, 0, 1}, //bank
			{2213, 1942, 4642, 0, 1}, //bank
			{2213, 1944, 4643, 0, 0}, //bank
			{2213, 1943, 4643, 0, 0}, //bank

			/*** DUNGEON FLOOR 1***/
			
			{3193, 3308, 9813, 8, 0}, //bank
			
			
			
			//NEW HOME 12/25/20 START
			
			{172, 3086, 3241, 0, 1}, //Starter key chest
			{6420, 3086, 3243, 0, 1}, //Medium key chest
			{7350, 3090, 3245, 0, 2}, //OP Donators chest
			{4123, 3088, 3245, 0, 2}, //Minigame chest
			{12355, 1949, 4947, 0, 0},
			{42611, 2206, 4988, 0, 0}, //Raids portal
			{2469, 2208, 4988, 0, 0}, //Minigame portal
			{6195, 3116, 3240, 0, 2},	
			
			//NEW HOME 12/25/20 END
			
			//START NEW RAIDS MAP PORTALS
			{42019, 2168, 4969, 0, 0}, //ROOM 1 Barrier
			{42019, 2168, 4968, 0, 0}, //ROOM 1 Barrier
			{42019, 2168, 4967, 0, 0}, //ROOM 1 Barrier
			{42019, 2168, 4966, 0, 0}, //ROOM 1 Barrier
			{42019, 2168, 4965, 0, 0}, //ROOM 1 Barrier
			{42019, 2168, 4964, 0, 0}, //ROOM 1 Barrier
			{42019, 2168, 4963, 0, 0}, //ROOM 1 Barrier
			{42425, 2171, 4964, 0, 0}, //ROOM 2 Portal
			
			{38144, 2099, 4964, 0, 0}, //ROOM 2 Barrier
			{39515, 2103, 4965, 0, 0}, //ROOM 2 Portal
			
			{31435, 2040, 4970, 0, 0}, //ROOM 3 Barrier
			{31435, 2040, 4967, 0, 0}, //ROOM 3 Barrier
			{31435, 2040, 4964, 0, 0}, //ROOM 3 Barrier
			{31435, 2040, 4961, 0, 0}, //ROOM 3 Barrier
			{56146, 2046, 4965, 0, 0}, //ROOM 3 Portal
			
			
			//END NEW RAIDS MAP PORTALS
			
			//NEW HOME 12/25/20 START
			
			{172, 2600, 3128, 0, 3}, //
			{6420, 2600, 3130, 0, 3}, //
			{7350, 2600, 3126, 0, 3}, //
			{42611, 2594, 3130, 0, 0}, //
			{2469, 2208, 4988, 0, 0}, //
			{46935, 2574, 3104, 0, 3}, //
			{42219, 2604, 3077, 0, 0}, //
			{28780, 3119, 3216, 0, 0}, //
			{4389, 3107, 9766, 0, 0}, //
			{4390, 3098, 9766, 0, 0}, //
			{4406, 3098, 9746, 0, 0}, //
			{4407, 3107, 9746, 0, 0}, //
			
			
			
			{14382, 2592, 3084, 0, 0}, //
			{14382, 2585, 3083, 0, 0}, //
			{14382, 2584, 3091, 0, 0}, //
			{14382, 2590, 3092, 0, 0}, //
			
			{2213, 2617, 3091, 0, 2}, //
			{2213, 2615, 3091, 0, 2}, //
			{2213, 2614, 3089, 0, 3}, //
			{2213, 2614, 3087, 0, 3}, //
			//{10804, 2606, 3105, 0, 0}, //
			{409, 2611, 3103, 0, 2}, //
			{411, 2613, 3100, 0, 1}, //
			
			
			//POS OBJECT
			{14367, 2588, 3086, 0, 0}, //
			{14367, 2587, 3086, 0, 0}, //
			{14367, 2586, 3087, 0, 0}, //
			{14367, 2586, 3088, 0, 0}, //
			{14367, 2587, 3089, 0, 0}, //
			{14367, 2588, 3089, 0, 0}, //
			{14367, 2589, 3088, 0, 0}, //
			{14367, 2589, 3087, 0, 0}, //

			
			
			
			
			/*** Bas Varrock home ***/

			{172, 2331, 3685, 0, 2}, //Starter key chest
			
			{172, 2984, 2528, 0, 2}, //Starter key chest
			{6420, 2984, 2532, 0, 2}, //Starter key chest
			
			{13179, 3090, 3511, 0, 3}, //veng
			{ 6552, 2327, 3697, 0, 3},
			{409, 3085, 3508, 0, 1}, //pray altar
			{411, 3085, 3511, 0, 1}, //turmoil
			//{884, 2325, 3682, 0, 1}, //well
			{6420, 2341, 3685, 0, 2}, //medium chest
			
			{3192, 2343, 3661, 0, 2}, //scoreboard
			
			
			{-1, 3217, 3436, 0, 0}, //remove stall
			{-1, 3219, 3436, 0, 0}, //remove stall
			{-1, 3220, 3431, 0, 0}, //remove stall
			{-1, 3220, 3425, 0, 0}, //remove stall
			{-1, 3223, 3434, 0, 0}, //remove oak
			{-1, 3226, 3431, 0, 0}, //remove plant
			{-1, 2728, 3350, 0, 0}, //remove plant
			{-1, 2729, 3350, 0, 0}, //remove plant
			{7350, 2969, 2527, 0, 1}, //Donators chest
			{28780, 2201, 4955, 0, 0}, //apollo portal
			/*** Bas Varrock home end ***/
			
			
			/*** Bas UBER Donator zone ***/
			
			{8749, 2307, 9806, 0, 1}, //special attack altar 
			
			{8749, 2982, 2548, 0, 1}, //special attack altar //home
			
			{411, 2970, 2552, 0, 2}, //pray altar
			{409, 2967, 2551, 0, 1}, //pray switch altar

			{2213, 2317, 9798, 0, 0}, //bank
			{2213, 2316, 9798, 0, 0}, //bank
			{2213, 2315, 9798, 0, 0}, //bank
			{2213, 2314, 9798, 0, 0}, //bank
			{2213, 2313, 9798, 0, 0}, //bank
			{2213, 2312, 9798, 0, 0}, //bank
			{2213, 2311, 9798, 0, 0}, //bank
			{2213, 2310, 9798, 0, 0}, //bank
			{2213, 2309, 9798, 0, 0}, //bank


			//investorzone
			{2213, 2520, 3385, 0, 0}, //bank
			{2213, 2521, 3385, 0, 0}, //bank
			{2213, 2522, 3385, 0, 0}, //bank
			{2213, 2523, 3385, 0, 0}, //bank
			{2213, 2524, 3385, 0, 0}, //bank
		 
		
			{411, 2527, 3384, 0, 0}, //pray altar
			{409, 2514, 3382, 0, 0}, //pray switch altar
			
			{13479, 2526, 3362, 0, 0},//lzone foutain
			{13479, 2509, 3362, 0, 0},
			
			//investor zone chests
			{4114, 2503, 3367, 0, 3},//vip chest
			{7350, 2503, 3369, 0, 1},//donator chest
			{ 2995, 2503, 3371, 0, 4},//uber chest
			{49347, 2503, 3373, 0, 0}, // Deluxe Chest
			{4170, 3092, 3243, 0, 3}, // Bloodslayer Chest
			
			//lzone
			{2213, 2852, 3237, 0, 0}, //bank
			{2213, 2851, 3237, 0, 0}, //bank
			{2213, 2850, 3237, 0, 0}, //bank
			{2213, 2849, 3237, 0, 0}, //bank
			{2213, 2848, 3237, 0, 0}, //bank
			
			{2213, 2836, 3237, 0, 0}, //bank
			{2213, 2837, 3237, 0, 0}, //bank
			{2213, 2838, 3237, 0, 0}, //bank
			{2213, 2839, 3237, 0, 0}, //bank
			{2213, 2840, 3237, 0, 0}, //bank
			
			
			{411, 2847, 3239, 0, 1}, //pray altar
			{409, 2841, 3239, 0, 1}, //pray switch altar
			
			{13479, 2851, 3227, 0, 0},//lzone foutain
			
			
			{-1, 2325, 9798, 0, 0}, //remove box
			{-1, 2324, 9798, 0, 0}, //remove barrel
			{-1, 2324, 9799, 0, 0}, //remove boxes
			{-1, 2320, 9798, 0, 0}, //remove chair
			{-1, 2319, 9799, 0, 0}, //remove workspace
			{-1, 2319, 9798, 0, 0}, //remove workspace
			{-1, 2318, 9798, 0, 0}, //remove workspace
			{-1, 2309, 9799, 0, 0}, //remove workspace
			{-1, 2321, 9800, 0, 0}, //remove workspace
			{-1, 2309, 9806, 0, 0}, //remove workspace
			{-1, 2318, 9801, 0, 0}, //remove workspace
			{-1, 2327, 9800, 0, 0}, //remove workspace
			{-1, 2327, 9799, 0, 0}, //remove workspace
			{-1, 2327, 9798, 0, 0}, //remove workspace
			{-1, 2326, 9798, 0, 0}, //remove workspace

			{14859, 2819, 3242, 0, 0}, //rune ore's
			{14859, 2820, 3242, 0, 0}, //rune ore's
			{14859, 2821, 3242, 0, 0}, //rune ore's
			{14859, 2822, 3242, 0, 0}, //rune ore's

			{6189, 2826, 3241, 0, 0}, //Smith bars
			{4306, 2828, 3241, 0, 1}, //Anvil
			{4306, 2331, 9800, 0, 1}, //Anvil

			{13493, 2818, 3237, 0, 2}, //thief stall
			{13493, 2818, 3239, 0, 2}, //thief stall

			{8702, 2818,3226, 0, 0}, //fish spot

			{2732, 2819, 3232, 0, 0}, //fire

			{1306, 2820, 3220, 0, 0}, //magic trees
			{1306, 2822, 3320, 0, 0}, //magic trees
			{1306, 2324, 3320, 0, 0}, //magic trees
			
			{8416, 2828, 3227, 0, 0},  
			{8416, 2828, 3224, 0, 0},
			
			{2213, 2819, 3230, 0, 0}, //bank
			
			/*** Bas UBER Donator zone end ***/
			
			/*** Bas Donator zone ***/
			
			{11933, 3353, 9622, 0, 0},//Tin Ore
			{11933, 3351, 9620, 0, 0},//Tin Ore
			{11936, 3349, 9622, 0, 0},//Copper Ore
			{11936, 3347, 9620, 0, 0},//Copper Ore
			{11954, 3344, 9620, 0, 0},//Iron Ore
			{11954, 3345, 9622, 0, 0},//Iron Ore
			{11954, 3343, 9623, 0, 0},//Iron Ore
			{11963, 3345, 9625, 0, 0},//Coal Ore
			{11963, 3232, 2802, 0, 0},//Coal Ore
			{11963, 3344, 9627, 0, 0},//Coal Ore
			{11963, 3345, 9629, 0, 0},//Coal Ore
			{11963, 3346, 9631, 0, 0},//Coal Ore
			{11951, 3347, 9628, 0, 0},//Gold Ore
			{11951, 3347, 9628, 0, 0},//Gold Ore
			{11951, 3347, 9624, 0, 0},//Gold Ore
			{11947, 3351, 9623, 0, 0},//Mithril Ore
			{11947, 3350, 9626, 0, 0},//Mithril Ore
			{11947, 3349, 9628, 0, 0},//Mithril Ore
			
			{11941, 3373, 9622, 0, 0},//Adamant Ore
			{11941, 3376, 9621, 0, 0},//Adamant Ore
			{11941, 3379, 9622, 0, 0},//Adamant Ore
			{11941, 3383, 9623, 0, 0},//Adamant Ore
			{11941, 3382, 9626, 0, 0},//Adamant Ore
			{11941, 3381, 9629, 0, 0},//Adamant Ore
			{14859, 3378, 9627, 0, 0},//Rune Ore
			{14859, 3375, 9624, 0, 0},//Rune Ore
			
			{1307, 3382, 9651, 0, 0},//Tree's
			{1307, 3381, 9648, 0, 0},//Tree's
			{1307, 3383, 9655, 0, 0},//Tree's
			{1309, 3382, 9657, 0, 0},//Tree's
			{1309, 3378, 9658, 0, 0},//Tree's
			{1309, 3375, 9658, 0, 0},//Tree's
			{1309, 3371, 9657, 0, 0},//Tree's
			
			{1308, 3355, 9657, 0, 0},//Tree's
			{1308, 3351, 9659, 0, 0},//Tree's
			{1281, 3346, 9658, 0, 0},//Tree's
			{1281, 3344, 9656, 0, 0},//Tree's
			{1278, 3344, 9652, 0, 0},//Tree's
			{1278, 3345, 9648, 0, 0},//Tree's
			
			{28716, 3376, 9632, 0, 1},//Obelisk
			

			
			//custom boss portal chamber
			{4407, 2227, 4949, 0, 1},
			{4406, 2227, 4946, 0, 1},
			{4390, 2227, 4943, 0, 1},
			{4389, 2227, 4940, 0, 1},
			
			
			{4875, 3113, 3205, 0, 1},//Thief
			{4874, 3113, 3206, 0, 1},//Thief
			{4876, 3113, 3207, 0, 1},//Thief
			{4877, 3113, 3208, 0, 1},//Thief
			{4878, 3113, 3209, 0, 1},//Thief
			{13493, 3350, 9643, 0, 1},//Thief Donor
			
			//afkzone stalls
			{4875, 3204, 2789, 0, 1},//Thief
			{4874, 3204, 2788, 0, 1},//Thief
			{4876, 3204, 2787, 0, 1},//Thief
			{4877, 3204, 2786, 0, 1},//Thief
			{4878, 3204, 2785, 0, 1},//Thief
			{13493, 3204, 2784, 0, 3},//Thief Donor
			//afkzone 
			{28716, 3204, 2780, 0, 1},//Obelisk
			
			
			{4875, 3188, 3159, 0, 3},//Thief
			{4874, 3188, 3160, 0, 3},//Thief
			{4876, 3188, 3161, 0, 3},//Thief
			{4877, 3188, 3162, 0, 3},//Thief
			{4878, 3188, 3163, 0, 3},//Thief

			{8702, 3350, 9636, 0, 0},//Fish barrel
			{12269,  3213, 2790, 0, 1},//Cook
			{10091, 3230, 2781, 0, 0}, //Rocktail fishing 
			{10091, 3232, 2782, 0, 0}, //Rocktail fishing 
			//afk zone magic trees
			{1306, 3223, 2789, 0, 0}, 
			
			{411, 2267, 3246, 0, 0}, //delAltar
			{409, 2269, 3246, 0, 0}, //delAltar
			
			
			{6189, 3350, 9630, 0, 0},//Furnace
			{4306, 3350, 9632, 0, 1},//Anvil
			
			{14859, 3372, 9626, 0, 0},//Rune Ore
			{14859, 3371, 9626, 0, 0},//Rune Ore
			{14859, 3370, 9626, 0, 0},//Rune Ore
			{14859, 3369, 9626, 0, 0},//Rune Ore
			{14859, 3368, 9626, 0, 0},//Rune Ore
			{14859, 3367, 9626, 0, 0},//Rune Ore
			{14859, 3366, 9626, 0, 0},//Rune Ore
			{14859, 3365, 9626, 0, 0},//Rune Ore
			{14859, 3361, 9626, 0, 0},//Rune Ore
			{14859, 3360, 9626, 0, 0},//Rune Ore
			{14859, 3359, 9626, 0, 0},//Rune Ore
			{14859, 3358, 9626, 0, 0},//Rune Ore
			{14859, 3357, 9626, 0, 0},//Rune Ore
			{14859, 3356, 9626, 0, 0},//Rune Ore
			{14859, 3355, 9626, 0, 0},//Rune Ore
			{14859, 3354, 9626, 0, 0},//Rune Ore

			{210, 3361, 9642, 0, 0},//Ice Light
			{210, 3365, 9642, 0, 0},//Ice Light
			{210, 3361, 9638, 0, 0},//Ice Light
			{210, 3365, 9638, 0, 0},//Ice Light

			{586, 3363, 9640, 0, 2},//Statue

			{4483, 3363, 9652, 0, 0},//Bank North
			{4483, 3376, 9640, 0, 1},//Bank East
			{4483, 3363, 9627, 0, 4},//Bank South
			{4483, 3351, 9640, 0, 3},//Bank West

			{1306, 3355, 9652, 0, 0},//Magic trees
			{1306, 3357, 9652, 0, 0},//Magic trees
			{1306, 3359, 9652, 0, 0},//Magic trees

			{1306, 3370, 9652, 0, 0},//Magic trees
			{1306, 3368, 9652, 0, 0},//Magic trees
			{1306, 3366, 9652, 0, 0},//Magic trees
			
			/* REMOVE PORTALS */
			{-1, 3353, 9640, 0, 0}, //Delete Portals
			{-1, 3363, 9629, 0, 0}, //Delete Portals
			{-1, 3374, 9640, 0, 0}, //Delete Portals
			{-1, 3363, 9650, 0, 0}, //Delete Portals
			
			/* NORTH EAST REMOVE PILE'S */
			{-1, 3371, 9658, 0, 0}, //Delete Pile's corners
			{-1, 3375, 9657, 0, 0}, //Delete Pile's corners
			{-1, 3377, 9653, 0, 0}, //Delete Pile's corners
			{-1, 3379, 9655, 0, 0}, //Delete Pile's corners
			{-1, 3381, 9657, 0, 0}, //Delete Pile's corners
			{-1, 3381, 9653, 0, 0}, //Delete Pile's corners
			{-1, 3381, 9650, 0, 0}, //Delete Pile's corners
			
			/* NORTH WEST REMOVE PILE'S */
			{-1, 3346, 9651, 0, 0}, //Delete Pile's corners
			{-1, 3348, 9652, 0, 0}, //Delete Pile's corners
			{-1, 3345, 9654, 0, 0}, //Delete Pile's corners
			{-1, 3348, 9655, 0, 0}, //Delete Pile's corners
			{-1, 3352, 9654, 0, 0}, //Delete Pile's corners
			{-1, 3345, 9657, 0, 0}, //Delete Pile's corners
			{-1, 3350, 9657, 0, 0}, //Delete Pile's corners
			{-1, 3354, 9658, 0, 0}, //Delete Pile's corners
			{-1, 3356, 9657, 0, 0}, //Delete Pile's corners
			
			/* SOUTH EAST REMOVE PILE'S */
			{-1, 3381, 9727, 0, 0}, //Delete Pile's corners
			{-1, 3378, 9625, 0, 0}, //Delete Pile's corners
			{-1, 3376, 9624, 0, 0}, //Delete Pile's corners
			{-1, 3381, 9623, 0, 0}, //Delete Pile's corners
			{-1, 3379, 9621, 0, 0}, //Delete Pile's corners
			{-1, 3374, 9621, 0, 0}, //Delete Pile's corners
			{-1, 3370, 9621, 0, 0}, //Delete Pile's corners
			{-1, 3381, 9627, 0, 0}, //Delete Pile's corners
			
			/* SOUTH WEST REMOVE PILE'S */
			{-1, 3355, 9621, 0, 0}, //Delete Pile's corners
			{-1, 3352, 9622, 0, 0}, //Delete Pile's corners
			{-1, 3350, 9621, 0, 0}, //Delete Pile's corners
			{-1, 3348, 9625, 0, 0}, //Delete Pile's corners
			{-1, 3347, 9620, 0, 0}, //Delete Pile's corners
			{-1, 3345, 9623, 0, 0}, //Delete Pile's corners
			{-1, 3347, 9628, 0, 0}, //Delete Pile's corners
			{-1, 3345, 9628, 0, 0}, //Delete Pile's corners

			/*** Bas Donator zone end ***/
			
			//H'ween even chest
			//{2079, 3107, 3427, 0, 0},
			/**Grand EXCHANGE**/
			{8799, 3091, 3495, 0, 3},
			
		//lumby cows gate
		{2344, 3253, 3266, 0, 0},
		{3192, 3084, 3485, 0, 4},
		{-1, 3084, 3487, 0, 2},
		
		
		/* ZULRAH */
		
		{357, 3034, 3422, 0, 0},
		{ 25136, 2278, 3070, 0, 0 },
		{ 25136, 2278, 3065, 0, 0 },
		{ 25138, 2273, 3066, 0, 0 },
		{ 25136, 2272, 3065, 0, 0 },
		{ 25139, 2267, 3065, 0, 0 },
		{ 25136, 2260, 3081, 0, 0 },
		{401, 3503, 3567, 0, 0},
		{401, 3504, 3567, 0, 0},
		{1309, 2715, 3465, 0, 0},
		{1309, 2709, 3465, 0, 0},
		{1309, 2709, 3458, 0, 0},
		{1306, 2705, 3465, 0, 0},
		{1306, 2705, 3458, 0, 0},
		{-1, 2715, 3466, 0, 0},
		{-1, 2712, 3466, 0, 0},
		{-1, 2713, 3464, 0, 0},
		{-1, 2711, 3467, 0, 0},
		{-1, 2710, 3465, 0, 0},
		{-1, 2709, 3464, 0, 0},
		{-1, 2708, 3466, 0, 0},
		{-1, 2707, 3467, 0, 0},
		{-1, 2704, 3465, 0, 0},
		{-1, 2714, 3466, 0, 0},
		{-1, 2705, 3457, 0, 0},
		{-1, 2709, 3461, 0, 0},
		{-1, 2709, 3459, 0, 0},
		{-1, 2708, 3458, 0, 0},
		{-1, 2710, 3457, 0, 0},
		{-1, 2711, 3461, 0, 0},
		{-1, 2713, 3461, 0, 0},
		{-1, 2712, 3459, 0, 0},
		{-1, 2712, 3457, 0, 0},
		{-1, 2714, 3458, 0, 0},
		{-1, 2705, 3459, 0, 0},
		{-1, 2705, 3464, 0, 0},
		{2274, 2912, 5300, 2, 0},
		{2274, 2914, 5300, 1, 0},
		{2274, 2919, 5276, 1, 0},
		{2274, 2918, 5274, 0, 0},
		{2274, 3001, 3931, 0, 0},
		{-1, 2884, 9797, 0, 2},
		{4483, 2909, 4832, 0, 3},
		{4483, 2901, 5201, 0, 2},
		{4483, 2902, 5201, 0, 2},
		{9326, 3001, 3960, 0, 0},
		{1662, 3112, 9677, 0, 2},
		{1661, 3114, 9677, 0, 2},
		{1661, 3122, 9664, 0, 1},
		{1662, 3123, 9664, 0, 2},
		{1661, 3124, 9664, 0, 3},
		{4483, 2918, 2885, 0, 3},
		{12356, 3081, 3500, 0, 0},
		{2182, 3081, 3497, 0, 0},
		{1746, 3090, 3492, 0, 1},
		{2644, 2737, 3444, 0, 0},
		{-1, 2608, 4777, 0, 0},
		{-1, 2601, 4774, 0, 0},
		{-1, 2611, 4776, 0, 0},
		
		
		
		
		/**Oude ruse Member Zone*/
		
//		{2344, 3421, 2908, 0, 0}, //Rock blocking
//        {2345, 3438, 2909, 0, 0},
//        {2344, 3435, 2909, 0, 0},
//        {2344, 3432, 2909, 0, 0},
//        {2345, 3431, 2909, 0, 0},
//        {2344, 3428, 2921, 0, 1},
//        {2344, 3428, 2918, 0, 1},
//        {2344, 3428, 2915, 0, 1},
//        {2344, 3428, 2912, 0, 1},
//        {2345, 3428, 2911, 0, 1},
//        {2344, 3417, 2913, 0, 1},
//        {2344, 3417, 2916, 0, 1},
//        {2344, 3417, 2919, 0, 1},
//        {2344, 3417, 2922, 0, 1},
//        {2345, 3417, 2912, 0, 0},
//        {2346, 3418, 2925, 0, 0},
//        {10378, 3426, 2907, 0, 0},
//        {8749, 3426, 2923, 0, 2}, //Altar
//        {-1, 3420, 2909, 0, 10}, //Remove crate by mining
//        {-1, 3420, 2923, 0, 10}, //Remove Rockslide by Woodcutting
//        {14859, 3421, 2909, 0, 0}, //Mining
//        {14859, 3419, 2909, 0, 0},
//        {14859, 3418, 2910, 0, 0},
//        {14859, 3418, 2911, 0, 0},
//        {14859, 3422, 2909, 0, 0},
//        {1306, 3418, 2921, 0, 0}, //Woodcutting
//        {1306, 3421, 2924, 0, 0},
//        {1306, 3420, 2924, 0, 0},
//        {1306, 3419, 2923, 0, 0},
//        {1306, 3418, 2922, 0, 0},
//		{-1, 3430, 2912, 0, 2}, 
//		{13493, 3424, 2916, 0, 1},//Armour  stall
		
        /**Oude ruse Member Zone end*/
		
		
		{-1, 3098, 3496, 0, 1},
		{-1, 3095, 3498, 0, 1},
		{-1, 3088, 3509, 0, 1},
		{-1, 3095, 3499, 0, 1},
		{-1, 3085, 3506, 0, 1},
		{30205, 3085, 3509, 0, 3},
		{-1, 3206, 3263, 0, 0},
		{-1, 2794, 2773, 0, 0},
		{2, 2692, 3712, 0, 3},
		{2, 2688, 3712, 0, 1},
		{4483, 3081, 3496, 0, 1},
		{4483, 3081, 3495, 0, 1},
		{4875, 3094, 3500, 0, 0},
		{4874, 3095, 3500, 0, 0},
		{4876, 3096, 3500, 0, 0},
		{4877, 3097, 3500, 0, 0},
		{4878, 3098, 3500, 0, 0},
		{ 11758, 3019, 9740, 0, 0},
		{ 11758, 3020, 9739, 0, 1},
		{ 11758, 3019, 9738, 0, 2},
		{ 11758, 3018, 9739, 0, 3},
		{ 11933, 3028, 9739, 0, 1},
		{ 11933, 3032, 9737, 0, 2},
		{ 11933, 3032, 9735, 0, 0},
		{ 11933, 3035, 9742, 0, 3},
		{ 11933, 3034, 9739, 0, 0},
		{ 11936, 3028, 9737, 0, 1},
		{ 11936, 3029, 9734, 0, 2},
		{ 11936, 3031, 9739, 0, 0},
		{ 11936, 3032, 9741, 0, 3},
		{ 11936, 3035, 9734, 0, 0},
		{ 11954, 3037, 9739, 0, 1},
		{ 11954, 3037, 9735, 0, 2},
		{ 11954, 3037, 9733, 0, 0},
		{ 11954, 3039, 9741, 0, 3},
		{ 11954, 3039, 9738, 0, 0},
		{ 11963, 3039, 9733, 0, 1},
		{ 11964, 3040, 9732, 0, 2},
		{ 11965, 3042, 9734, 0, 0},
		{ 11965, 3044, 9737, 0, 3},
		{ 11963, 3042, 9739, 0, 0},
		{ 11963, 3045, 9740, 0, 1},
		{ 11965, 3043, 9742, 0, 2},
		{ 11964, 3045, 9744, 0, 0},
		{ 11965, 3048, 9747, 0, 3},
		{ 11951, 3048, 9743, 0, 0},
		{ 11951, 3049, 9740, 0, 1},
		{ 11951, 3047, 9737, 0, 2},
		{ 11951, 3050, 9738, 0, 0},
		{ 11951, 3052, 9739, 0, 3},
		{ 11951, 3051, 9735, 0, 0},
		{ 11947, 3049, 9735, 0, 1},
		{ 11947, 3049, 9734, 0, 2},
		{ 11947, 3047, 9733, 0, 0},
		{ 11947, 3046, 9733, 0, 3},
		{ 11947, 3046, 9735, 0, 0},
		{ 11941, 3053, 9737, 0, 1},
		{ 11939, 3054, 9739, 0, 2},
		{ 11941, 3053, 9742, 0, 0},
		{ 14859, 3038, 9748, 0, 3},
		{ 14859, 3044, 9753, 0, 0},
		{ 14859, 3048, 9754, 0, 1},
		{ 14859, 3054, 9746, 0, 2},
		{ 4306, 3026, 9741, 0, 0},
		{ 6189, 3022, 9742, 0, 1},
		{ 75 , 2914, 3452, 0, 2},
		{ 11954 , 2350, 3714, 0, 2},
		{ 11954 , 2350, 3716, 0, 2},
		{ 11951 , 2345, 3718, 0, 2},
		{ 11965 , 2349, 3718, 0, 2},
		{ 11965 , 2347, 3718, 0, 2},
		{ 11947 , 2348, 3714, 0, 2},
		{ 11947 , 2347, 3716, 0, 2},
		{ 11941 , 2345, 3714, 0, 2},
		{ 11941 , 2344, 3716, 0, 2},
		{ 14859 , 2341, 3714, 0, 2},
		{ 14859 , 2341, 3715, 0, 2},

		
		
		{ 4875 , 2339, 3726, 0, 2},
		{ 4874 , 2338, 3726, 0, 2},
		{ 4876 , 2337, 3726, 0, 2},
		{ 4877 , 2336, 3726, 0, 2},
		{ 4878 , 2335, 3726, 0, 2},
		
		{ 12269 , 2342, 3706, 0, 3},
		
		
		{ 1309, 2335, 3710, 0, 1},
		{ 1309, 2329, 3714, 0, 1},
		{ 1306, 2325, 3715, 0, 1},
		{ 1306 , 2333, 3706, 0, 1},
		
		
		{ 10091 , 2343, 3705, 0, 2},
		{ 6189 , 2330, 3701, 0, 1},
		{ 4306 , 2333, 3698, 0, 1},


		
		{ 11936, 2346, 3712, 0, 1},
		{ 11936, 2344, 3712, 0, 1},
		{ 11933, 2350, 3712, 0, 1},
		{ 11933, 2348, 3712, 0, 1},
		{ 11758, 3449, 3722, 0, 0},
		{ 11758, 3450, 3722, 0, 0},
		{ 50547, 3445, 3717, 0, 3},
		
		{2465, 3085, 3512, 0, 0},
		{ -1, 3090, 3496, 0, 0},
		{ -1, 3090, 3494, 0, 0},
		{ -1, 3092, 3496, 0, 0},
		
		{ -1, 3659, 3508, 0, 0},
		{ 4053, 3660, 3508, 0, 0},
		{ 4051, 3659, 3508, 0, 0},
		{ 1, 3649, 3506, 0, 0},
		{ 1, 3650, 3506, 0, 0},
		{ 1, 3651, 3506, 0, 0},
		{ 1, 3652, 3506, 0, 0},
		{ -1, 2860, 9734, 0, 1},
		{ -1, 2857, 9736, 0, 1},
		{ 664, 2859, 9742, 0, 1},
		{ 1167, 2860, 9742, 0, 1},
		{ 5277, 3691, 3465, 0, 2},
		{ 5277, 3690, 3465, 0, 2},
		{ 5277, 3688, 3465, 0, 2},
		{ 5277, 3687, 3465, 0, 2},
		{ 114, 3093, 3933, 0, 0},
		
	};
	
	
	/**
	 * Contains
	 * @param ObjectId - The object ID to spawn
	 * @param absX - The X position of the object to spawn
	 * @param absY - The Y position of the object to spawn
	 * @param Z - The Z position of the object to spawn
	 * @param face - The position the object will face
	 */
	
	//Objects that are handled by the server on regionchange
	private static final int[][] CUSTOM_OBJECTS_SPAWNS = {
			{2079, 2576, 9876, 0, 0},

			
			/**
			 * ZULRAH
			 */
			{ 25136, 2278, 3070, 0, 0 },
			{ 25136, 2278, 3065, 0, 0 },
			{ 25138, 2273, 3066, 0, 0 },
			{ 25136, 2272, 3065, 0, 0 },
			{ 25139, 2267, 3065, 0, 0 },
			{ 25136, 2260, 3081, 0, 0 },
			{357, 3034, 3422, 0, 0},
			{ -1, 2265, 3071, 0, 0 },
			{ -1, 2271, 3071, 0, 0 },
			
	
			
			{-1, 3084, 3487, 0, 2},
			
			{ 2274, 3652, 3488, 0, 0 },
			{ 2274, 3039, 9555, 0, 0 },
			{ 2274, 3039, 9554, 0, 0 },
			//{ 4389, 2329, 3674, 0, 1 },
			/*
			* Remove Uber Zone Objects
			*/
			{-1, 2425, 4714, 0, 0},
			{-1, 2420, 4716, 0, 0},
			{-1, 2426, 4726, 0, 0},
			{-1, 2420, 4709, 0, 0},
			{-1, 2419, 4698, 0, 0},
			{-1, 2420, 4700, 0, 0},
			{-1, 2399, 4721, 0, 0},
			{-1, 2398, 4721, 0, 0},
			{-1, 2399, 4720, 0, 0},
			{-1, 2395, 4722, 0, 0},
			{-1, 2398, 4717, 0, 0},
			{-1, 2396, 4717, 0, 0},
			{-1, 2396, 4718, 0, 0},
			{-1, 2396, 4719, 0, 0},
			{-1, 2395, 4718, 0, 0},
			{-1, 2394, 4711, 0, 0},
			{-1, 2396, 4711, 0, 0},
			{-1, 2397, 4711, 0, 0},
			{-1, 2397, 4713, 0, 0},
			{-1, 2399, 4713, 0, 0},
			{-1, 2402, 4726, 0, 0},
			{-1, 2407, 4728, 0, 0},
			{-1, 2405, 4724, 0, 0},
			{-1, 2409, 4705, 0, 0},
			{-1, 2410, 4704, 0, 0},
			{-1, 2407, 4702, 0, 0},
			{-1, 2407, 4701, 0, 0},
			{-1, 2408, 4701, 0, 0},
			{-1, 2412, 4701, 0, 0},
			{-1, 2413, 4701, 0, 0},
			{-1, 2414, 4703, 0, 0},
			{-1, 2416, 4714, 0, 0},
			{-1, 2412, 4732, 0, 0},
			{-1, 2413, 4729, 0, 0},
			{-1, 2414, 4733, 0, 0},
			{-1, 2415, 4730, 0, 0},
			{-1, 2416, 4730, 0, 0},
			{-1, 2416, 4731, 0, 0},
			{-1, 2419, 4731, 0, 0},
			{-1, 2420, 4731, 0, 0},
			{-1, 2420, 4732, 0, 0},
			{-1, 2415, 4725, 0, 0},
			{-1, 2417, 4729, 0, 0},
			{-1, 2418, 4727, 0, 0},
			{-1, 2418, 4723, 0, 0},
			{-1, 2419, 4722, 0, 0},
			{-1, 2420, 4726, 0, 0},
			{-1, 2415, 4725, 0, 0},
			{-1, 2417, 4729, 0, 0},
			{-1, 2418, 4727, 0, 0},
			{-1, 2418, 4723, 0, 0},
			{-1, 2419, 4722, 0, 0},
			{-1, 2420, 4726, 0, 0},
			
			
		//lumby cows gate
		{2344, 3253, 3266, 0, 1},
		
		//gamble zone
		{2213, 2842, 5143, 0, 0},
		{2213, 2843, 5143, 0, 0},
		{2213, 2844, 5143, 0, 0},
		{2213, 2845, 5143, 0, 0},
		{2213, 2846, 5143, 0, 0},
		{2213, 2847, 5143, 0, 0},
		{2213, 2848, 5143, 0, 0},
		{2213, 2849, 5143, 0, 0},
		{2213, 2850, 5143, 0, 0},
		{2213, 2851, 5143, 0, 0},
		
		
		{2274, 3652, 3488, 0, 0},
		/**Jail Start*/
		{ 12269, 3093, 3933, 0, 0},
		{ 1864, 3093, 3932, 0, 1},//Cell 1
		{ 1864, 3094, 3932, 0, 1},
		{ 1864, 3095, 3932, 0, 1},
		{ 1864, 3096, 3932, 0, 1},
		{ 1864, 3097, 3932, 0, 1},
		{ 1864, 3097, 3931, 0, 2},
		{ 1864, 3097, 3930, 0, 2},
		{ 1864, 3097, 3929, 0, 2},
		{ 1864, 3097, 3928, 0, 3},
		{ 1864, 3096, 3928, 0, 3},
		{ 1864, 3095, 3928, 0, 3},
		{ 1864, 3094, 3928, 0, 3},
		{ 1864, 3093, 3928, 0, 3},
		{ 1864, 3093, 3929, 0, 4},
		{ 1864, 3093, 3930, 0, 4},
		{ 1864, 3093, 3931, 0, 4},
		{ 1864, 3098, 3932, 0, 1},//Cell 2
		{ 1864, 3099, 3932, 0, 1},
		{ 1864, 3100, 3932, 0, 1},
		{ 1864, 3101, 3932, 0, 1},
		{ 1864, 3102, 3932, 0, 1},
		{ 1864, 3102, 3931, 0, 2},
		{ 1864, 3102, 3930, 0, 2},
		{ 1864, 3102, 3929, 0, 2},
		{ 1864, 3102, 3928, 0, 3},
		{ 1864, 3101, 3928, 0, 3},
		{ 1864, 3100, 3928, 0, 3},
		{ 1864, 3099, 3928, 0, 3},
		{ 1864, 3098, 3928, 0, 3},
		{ 1864, 3098, 3929, 0, 4},
		{ 1864, 3098, 3930, 0, 4},
		{ 1864, 3098, 3931, 0, 4},
		{ 1864, 3093, 3939, 0, 1},//Cell 3
		{ 1864, 3094, 3939, 0, 1},
		{ 1864, 3095, 3939, 0, 1},
		{ 1864, 3096, 3939, 0, 1},
		{ 1864, 3097, 3939, 0, 1},
		{ 1864, 3097, 3938, 0, 2},
		{ 1864, 3097, 3937, 0, 2},
		{ 1864, 3097, 3936, 0, 2},
		{ 1864, 3097, 3935, 0, 3},
		{ 1864, 3096, 3935, 0, 3},
		{ 1864, 3095, 3935, 0, 3},
		{ 1864, 3094, 3935, 0, 3},
		{ 1864, 3093, 3935, 0, 3},
		{ 1864, 3093, 3936, 0, 4},
		{ 1864, 3093, 3937, 0, 4},
		{ 1864, 3093, 3938, 0, 4},
		{ 1864, 3098, 3939, 0, 1},//Cell 4
		{ 1864, 3099, 3939, 0, 1},
		{ 1864, 3100, 3939, 0, 1},
		{ 1864, 3101, 3939, 0, 1},
		{ 1864, 3102, 3939, 0, 1},
		{ 1864, 3102, 3938, 0, 2},
		{ 1864, 3102, 3937, 0, 2},
		{ 1864, 3102, 3936, 0, 2},
		{ 1864, 3102, 3935, 0, 3},
		{ 1864, 3101, 3935, 0, 3},
		{ 1864, 3100, 3935, 0, 3},
		{ 1864, 3099, 3935, 0, 3},
		{ 1864, 3098, 3935, 0, 3},
		{ 1864, 3098, 3936, 0, 4},
		{ 1864, 3098, 3937, 0, 4},
		{ 1864, 3098, 3938, 0, 4},
		{ 1864, 3103, 3932, 0, 1},//Cell 5
		{ 1864, 3104, 3932, 0, 1},
		{ 1864, 3105, 3932, 0, 1},
		{ 1864, 3106, 3932, 0, 1},
		{ 1864, 3107, 3932, 0, 1},
		{ 1864, 3107, 3931, 0, 2},
		{ 1864, 3107, 3930, 0, 2},
		{ 1864, 3107, 3929, 0, 2},
		{ 1864, 3107, 3928, 0, 3},
		{ 1864, 3106, 3928, 0, 3},
		{ 1864, 3105, 3928, 0, 3},
		{ 1864, 3104, 3928, 0, 3},
		{ 1864, 3103, 3928, 0, 3},
		{ 1864, 3103, 3929, 0, 4},
		{ 1864, 3103, 3930, 0, 4},
		{ 1864, 3103, 3931, 0, 4},
		{ 1864, 3108, 3932, 0, 1},//Cell 6
		{ 1864, 3109, 3932, 0, 1},
		{ 1864, 3110, 3932, 0, 1},
		{ 1864, 3111, 3932, 0, 1},
		{ 1864, 3112, 3932, 0, 1},
		{ 1864, 3112, 3931, 0, 2},
		{ 1864, 3112, 3930, 0, 2},
		{ 1864, 3112, 3929, 0, 2},
		{ 1864, 3112, 3928, 0, 3},
		{ 1864, 3111, 3928, 0, 3},
		{ 1864, 3110, 3928, 0, 3},
		{ 1864, 3109, 3928, 0, 3},
		{ 1864, 3108, 3928, 0, 3},
		{ 1864, 3108, 3929, 0, 4},
		{ 1864, 3108, 3930, 0, 4},
		{ 1864, 3108, 3931, 0, 4},
		{ 1864, 3103, 3939, 0, 1},//Cell 7
		{ 1864, 3104, 3939, 0, 1},
		{ 1864, 3105, 3939, 0, 1},
		{ 1864, 3106, 3939, 0, 1},
		{ 1864, 3107, 3939, 0, 1},
		{ 1864, 3107, 3938, 0, 2},
		{ 1864, 3107, 3937, 0, 2},
		{ 1864, 3107, 3936, 0, 2},
		{ 1864, 3107, 3935, 0, 3},
		{ 1864, 3106, 3935, 0, 3},
		{ 1864, 3105, 3935, 0, 3},
		{ 1864, 3104, 3935, 0, 3},
		{ 1864, 3103, 3935, 0, 3},
		{ 1864, 3103, 3936, 0, 4},
		{ 1864, 3103, 3937, 0, 4},
		{ 1864, 3103, 3938, 0, 4},
		{ 1864, 3108, 3939, 0, 1},//Cell 8
		{ 1864, 3109, 3939, 0, 1},
		{ 1864, 3110, 3939, 0, 1},
		{ 1864, 3111, 3939, 0, 1},
		{ 1864, 3112, 3939, 0, 1},
		{ 1864, 3112, 3938, 0, 2},
		{ 1864, 3112, 3937, 0, 2},
		{ 1864, 3112, 3936, 0, 2},
		{ 1864, 3112, 3935, 0, 3},
		{ 1864, 3111, 3935, 0, 3},
		{ 1864, 3110, 3935, 0, 3},
		{ 1864, 3109, 3935, 0, 3},
		{ 1864, 3108, 3935, 0, 3},
		{ 1864, 3108, 3936, 0, 4},
		{ 1864, 3108, 3937, 0, 4},
		{ 1864, 3108, 3938, 0, 4},
		{ 1864, 3113, 3932, 0, 1},//Cell 9
		{ 1864, 3114, 3932, 0, 1},
		{ 1864, 3115, 3932, 0, 1},
		{ 1864, 3116, 3932, 0, 1},
		{ 1864, 3117, 3932, 0, 1},
		{ 1864, 3117, 3931, 0, 2},
		{ 1864, 3117, 3930, 0, 2},
		{ 1864, 3117, 3929, 0, 2},
		{ 1864, 3117, 3928, 0, 3},
		{ 1864, 3116, 3928, 0, 3},
		{ 1864, 3115, 3928, 0, 3},
		{ 1864, 3114, 3928, 0, 3},
		{ 1864, 3113, 3928, 0, 3},
		{ 1864, 3113, 3929, 0, 4},
		{ 1864, 3113, 3930, 0, 4},
		{ 1864, 3113, 3931, 0, 4},
		{ 1864, 3113, 3939, 0, 1},//Cell 10
		{ 1864, 3114, 3939, 0, 1},
		{ 1864, 3115, 3939, 0, 1},
		{ 1864, 3116, 3939, 0, 1},
		{ 1864, 3117, 3939, 0, 1},
		{ 1864, 3117, 3938, 0, 2},
		{ 1864, 3117, 3937, 0, 2},
		{ 1864, 3117, 3936, 0, 2},
		{ 1864, 3117, 3935, 0, 3},
		{ 1864, 3116, 3935, 0, 3},
		{ 1864, 3115, 3935, 0, 3},
		{ 1864, 3114, 3935, 0, 3},
		{ 1864, 3113, 3935, 0, 3},
		{ 1864, 3113, 3936, 0, 4},
		{ 1864, 3113, 3937, 0, 4},
		{ 1864, 3113, 3938, 0, 4},
		
		{ 2995, 2336, 3604, 0, 4},//VIP CHEST
		{ 2995, 2333, 3604, 0, 4},//VIP CHEST
		

		
		{ 27330, 2335, 3668, 0, 2},//Emerald STATUE
		
		
		{ 9075, 2333, 3649, 0, 2},//VIP BANKCHEST
		{ 11356, 2334, 3652, 0, 0},//VIP PORTAL
		
		{ 9075, 3089, 3240, 0, 2},//VIP BANKCHEST
		

		{ -1, 3092, 3488, 0, 0},

	};
}
