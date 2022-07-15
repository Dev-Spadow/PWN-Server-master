package com.arlania.world.content.bosses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.GroundItem;
import com.arlania.model.Item;
import com.arlania.model.Position;
import com.arlania.util.Misc;
import com.arlania.world.content.combat.CombatBuilder.CombatDamageCache;
import com.arlania.world.World;
import com.arlania.world.content.combat.CombatFactory;
import com.arlania.world.entity.impl.GroundItemManager;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

public class ElementalJad extends NPC {

	public static int[] COMMONLOOT = { 1464, 4670, 4671, 4672, 4673, 4761, 4762, 4763, 4764, 4765, 5089, 10168, 6507,
			4082 };
	public static int[] RARELOOT = { 19935, 19936, 9492, 19727, 926, 931, 930, 4781, 4782, 20240, 3820, 3821, 3822,
			20054, 10168 };
	public static int[] SUPERRARELOOT = { 5266, 13999, 16455, 19938, 4082, 15026, 16579, 8476, 8477, 8478, 8479, 8480,
			8481 };
	public static int[] LUCKYLOOT = { 11309, 455 };
	public static int[] EXTREMELOOT = { 15566, 773, 774 };
	/**
	 * 
	 */
	public static final int NPC_ID = 10010;

	/**
	 * add your maps to that folder open me your client.java in client
	 */
	public static final ElementalJadLocation[] LOCATIONS = { new ElementalJadLocation(2787, 4449, 0, "") };

	/**
	 * 
	 */
	private static ElementalJad current;

	/**
	 * 
	 * @param position
	 */
	public ElementalJad(Position position) {

		super(NPC_ID, position);
	}

	/**
	 * 
	 */

	private static int ticksRequired = 6000;
	private static int ticksColapsed;

	public static void initialize() {
		ticksColapsed = 0;

		TaskManager.submit(new Task(72000, false) { // 6000

			@Override
			public void execute() {
				spawn();
			}

		});

	}

	/**
	 * 
	 */
	public static void spawn() {

		if (getCurrent() != null) {
			System.out.println("Current isnt null.");
			return;
		}

		ElementalJadLocation location = Misc.randomElement(LOCATIONS);
		ElementalJad instance = new ElementalJad(location.copy());

		// System.out.println(instance.getPosition());

		World.register(instance);
		setCurrent(instance);
		// System.out.print("spawned.");
		World.sendMessage("Alert##Mass Event##" + "Massive Diablo  has just respawned at ::mass" + "## ");
		// World.sendMessage("<col=fcfcfc><shad=dd8a14><img=418>[Mass Boss]</shad>@bla@:
		// Massive Diablo<col=##585858>@bla@ has just Respawned" +
		// location.getLocation() + "");
	}

	/**
	 * 
	 * @param npc
	 */
	public static void handleDrop(NPC npc) {
		World.getPlayers().forEach(p -> p.getPacketSender().sendString(26707, "@or2@WildyWyrm: @gre@N/A"));

		System.out.println("1");
		setCurrent(null);
		System.out.println("2");

		if (npc.getCombatBuilder().getDamageMap().size() == 0) {
			return;
		}

		Map<Player, Integer> killers = new HashMap<>();

		for (Entry<Player, CombatDamageCache> entry : npc.getCombatBuilder().getDamageMap().entrySet()) {

			if (entry == null) {
				continue;
			}

			long timeout = entry.getValue().getStopwatch().elapsed();

			if (timeout > CombatFactory.DAMAGE_CACHE_TIMEOUT) {
				continue;
			}

			Player player = entry.getKey();

			if (player.getConstitution() <= 0 || !player.isRegistered()) {
				continue;
			}

			killers.put(player, entry.getValue().getDamage());

		}

		npc.getCombatBuilder().getDamageMap().clear();

		List<Entry<Player, Integer>> result = sortEntries(killers);
		int count = 0;

		for (Entry<Player, Integer> entry : result) {

			Player killer = entry.getKey();
			int damage = entry.getValue();

			handleDrop(npc, killer, damage);

			if (++count >= 50) {
				break;
			}

		}

	}

	public static void giveLoot(Player player, NPC npc, Position pos) {
		int chance = Misc.getRandom(1000);
		int common = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		@SuppressWarnings("unused")
		int common1 = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		int rare = RARELOOT[Misc.getRandom(RARELOOT.length - 1)];
		int superrare = SUPERRARELOOT[Misc.getRandom(SUPERRARELOOT.length - 1)];
		int extremeloot = EXTREMELOOT[Misc.getRandom(EXTREMELOOT.length - 1)];
		int luckyloot = LUCKYLOOT[Misc.getRandom(LUCKYLOOT.length - 1)];
		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(10835, 500), pos, player.getUsername(), false, 150, true, 200));
		player.getInventory().add(17750, Misc.inclusiveRandom(10, 150));
		player.getAchievementTracker().progress(com.arlania.world.content.achievements.AchievementData.KILL_MASSBOSS,
				1);
		if (chance >= 999) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(extremeloot), pos, player.getUsername(), false, 150, true, 200));
			String itemName = (new Item(extremeloot).getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage("<col=fcfcfc><shad=dd8a14><img=418>[Mass Boss]</shad>@bla@: " + player.getUsername()
					+ " received@yel@<shad=f4e019> " + itemMessage + "</shad>@bla@from the Mass Event");
			return;
		}

		if (chance >= 995) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(luckyloot), pos, player.getUsername(), false, 150, true, 200));
			String itemName = (new Item(luckyloot).getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage("<col=fcfcfc><shad=dd8a14><img=418>[Mass Boss]</shad>@bla@: " + player.getUsername()
					+ " received@yel@<shad=f4e019> " + itemMessage + "</shad>@bla@from the Mass Event");
			return;
		}

		if (chance >= 875) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(superrare), pos, player.getUsername(), false, 150, true, 200));
			String itemName = (new Item(superrare).getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage("<col=fcfcfc><shad=dd8a14><img=418>[Mass Boss]</shad>@bla@: " + player.getUsername()
					+ " received@mag@<shad=9f199d> " + itemMessage + "</shad>@bla@ from the Mass Event");
			return;
		}

		if (chance >= 600) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(rare), pos, player.getUsername(), false, 150, true, 200));
			String itemName = (new Item(rare).getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage("<col=fcfcfc><shad=dd8a14><img=418>[Mass Boss]</shad>@bla@: " + player.getUsername()
					+ " received@mag@<shad=9f199d> " + itemMessage + "</shad>@bla@ from the Mass Event");
			return;
		}
		if (chance >= 0) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(common, 1), pos, player.getUsername(), false, 150, true, 200));
			String itemName = (new Item(common).getDefinition().getName());
			World.sendMessage("<col=fcfcfc><shad=dd8a14><img=418>[Mass Boss]</shad>@bla@: " + player.getUsername()
					+ " received@mag@<shad=9f199d> " + itemName + "</shad>@bla@ from the Mass Event");
			return;
		}

	}

	/**
	 * 
	 * @param npc
	 * @param player
	 * @param damage
	 */
	private static void handleDrop(NPC npc, Player player, int damage) {
		Position pos = npc.getPosition();
		giveLoot(player, npc, pos);
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	static <K, V extends Comparable<? super V>> List<Entry<K, V>> sortEntries(Map<K, V> map) {

		List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(map.entrySet());

		Collections.sort(sortedEntries, new Comparator<Entry<K, V>>() {

			@Override
			public int compare(Entry<K, V> e1, Entry<K, V> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}

		});

		return sortedEntries;

	}

	/**
	 * 
	 * @return
	 */
	public static ElementalJad getCurrent() {
		return current;
	}

	/**
	 * 
	 * @param current
	 */
	public static void setCurrent(ElementalJad current) {
		ElementalJad.current = current;
	}

	/**
	 * 
	 * @author Levi <levi.patton69 @ skype>
	 *
	 */
	public static class ElementalJadLocation extends Position {

		/**
		 * 
		 */
		private String location;

		/**
		 * 
		 * @param x
		 * @param y
		 * @param z
		 * @param location
		 */
		public ElementalJadLocation(int x, int y, int z, String location) {
			super(x, y, z);
			setLocation(location);
		}

		/**
		 * 
		 * @return
		 */

		String getLocation() {
			return location;
		}

		/**
		 * 
		 * @param location
		 */
		public void setLocation(String location) {
			this.location = location;
		}

	}

	public static String getCurrentLocation() {
		return getCurrent() != null ? "ACTIVE" : getCurrentSpawnTime();
	}

	public static String getCurrentSpawnTime() {
		return (int) ((ticksRequired - ticksColapsed) * 0.6) + " @or1@secs";
	}

}