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

public class FrostBeast extends NPC {

	public static int[] COMMONLOOT = { 10835,1464};
	public static int[] RARELOOT = { 19935,19936,6640,19936,19129,19128,19127,4797,4796,4795,4794,8670,8669,8668,8667,8666,8665,8664,13999,15373,6507,15045,19821,12426,6931,6930,6640,6929,6928,6927,16455,16455,};
	public static int[] SUPERRARELOOT = {13997,15660,2547,2546,17927,17926,2545,9496,9497,9498,9499,10905,19155,14914,14912,14913,14915,19890,14916,14917,14918,14919,14920 };

	/**
	 * 
	 */
	public static final int NPC_ID = 12802;

	/**
	 * add your maps to that folder open me your client.java in client
	 */
	public static final FrostBeastLocation[] LOCATIONS = { new FrostBeastLocation(3167, 4956, 0, "@red@ (::eb)") };

	/**
	 * 
	 */
	private static FrostBeast current;

	/**
	 * 
	 * @param position
	 */
	public FrostBeast(Position position) {

		super(NPC_ID, position);
	}

	/**
	 * 
	 */
	private static int ticksRequired = 6000;
    private static int ticksColapsed;
	public static void initialize() {
        ticksColapsed = 0;

		TaskManager.submit(new Task( 10000, false) { // 6000

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
			return;
		}

		FrostBeastLocation location = Misc.randomElement(LOCATIONS);
		FrostBeast instance = new FrostBeast(location.copy());

		// System.out.println(instance.getPosition());

		World.register(instance);
		setCurrent(instance);
		// System.out.print("spawned.");
		World.sendMessage("Alert##Expert Boss##" + "Arctic Frost beast has just respawned at ::eb" + "## ");
		World.sendMessage("<col=fcfcfc><shad=dd8a14><img=418>[Expert Boss]</shad>@bla@: Arctic Frost beast has just Respawned" + location.getLocation() + "");
	}

	/**
	 * 
	 * @param npc
	 */
	public static void handleDrop(NPC npc) {
		World.getPlayers().forEach(p -> p.getPacketSender().sendString(26707, "@or2@WildyWyrm: @gre@N/A"));

		setCurrent(null);

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

			if (++count >= 20) {
				break;
			}

		}

	}

	public static void giveLoot(Player player, NPC npc, Position pos) {
		int chance = Misc.getRandom(100);
		int common = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		@SuppressWarnings("unused")
		int common1 = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		int rare = RARELOOT[Misc.getRandom(RARELOOT.length - 1)];
		int superrare = SUPERRARELOOT[Misc.getRandom(SUPERRARELOOT.length - 1)];

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(10835, 100), pos, player.getUsername(), false, 150, true, 200));

		if (chance >= 92) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(superrare), pos, player.getUsername(), false, 150, true, 200));
			String itemName = (new Item(superrare).getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage(
					"<col=fcfcfc><shad=dd8a14><img=418>[Expert Boss]</shad>@bla@: " + player.getUsername() + " received@yel@<shad=f4e019> " + itemMessage + "</shad>@bla@ from Arctic Frost beast");
					return;
		}

		if (chance >= 70) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(rare), pos, player.getUsername(), false, 150, true, 200));
			String itemName = (new Item(rare).getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage(
					"<col=fcfcfc><shad=dd8a14><img=418>[Expert Boss]</shad>@bla@: " + player.getUsername() + " received@mag@<shad=9f199d> " + itemMessage + "</shad>@bla@ from Arctic Frost beast");
					return;
		}
		if (chance >= 0) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(common, 1), pos, player.getUsername(), false, 150, true, 200));
			String itemName = (new Item(common).getDefinition().getName());
			World.sendMessage(
					"<col=fcfcfc><shad=dd8a14><img=418>[Expert Boss]</shad>@bla@: " + player.getUsername() + " received@mag@<shad=9f199d> " + itemName + "</shad>@bla@ from Arctic Frost beast");
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
	public static FrostBeast getCurrent() {
		return current;
	}

	/**
	 * 
	 * @param current
	 */
	public static void setCurrent(FrostBeast current) {
		FrostBeast.current = current;
	}

	/**
	 * 
	 * @author Levi <levi.patton69 @ skype>
	 *
	 */
	public static class FrostBeastLocation extends Position {

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
		public FrostBeastLocation(int x, int y, int z, String location) {
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
