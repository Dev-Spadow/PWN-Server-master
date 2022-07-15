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

public class CorpBeastBoss extends NPC {

	public static int[] COMMONLOOT = {1573,6199,10835,15373,18782,989};
	public static int[] RARELOOT = { 1573,6199,18782,4770,4771,4772,3316,3931,3958,3959,3960};
	public static int[] SUPERRARELOOT = {930,931,926};
	public static int[] EXTREMELOOT = {5210,5211,15045,12001};
	/**
	 * 
	 */
	public static final int NPC_ID = 8133;

	/**
	 * add your maps to that folder open me your client.java in client
	 */
	public static final CorpBeastBossLocation[] LOCATIONS = { new CorpBeastBossLocation(2898, 4394, 0, "") };

	/**
	 * 
	 */
	private static CorpBeastBoss current;

	/**
	 * 
	 * @param position
	 */
	public CorpBeastBoss(Position position) {

		super(NPC_ID, position);
	}

	/**
	 * 
	 */
	
	private static int ticksRequired = 6000;
    private static int ticksColapsed;
	public static void initialize() {
        ticksColapsed = 0;

		TaskManager.submit(new Task( 400, false) { // 6000

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

		CorpBeastBossLocation location = Misc.randomElement(LOCATIONS);
		CorpBeastBoss instance = new CorpBeastBoss(location.copy());

		// System.out.println(instance.getPosition());

		World.register(instance);
		setCurrent(instance);
		// System.out.print("spawned.");
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

			if (++count >= 10) {
				break;
			}

		}

	}

	public static void giveLoot(Player player, NPC npc, Position pos) {
		int chance = Misc.getRandom(500);
		int common = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		@SuppressWarnings("unused")
		int common1 = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		int rare = RARELOOT[Misc.getRandom(RARELOOT.length - 1)];
		int superrare = SUPERRARELOOT[Misc.getRandom(SUPERRARELOOT.length - 1)];
		int superrare1 = EXTREMELOOT[Misc.getRandom(EXTREMELOOT.length - 1)];
		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(10835, 200), pos, player.getUsername(), false, 150, true, 200));
		if (chance >= 499) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(superrare1), pos, player.getUsername(), false, 150, true, 200));
					return;
		}
		if (chance >= 495) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(superrare), pos, player.getUsername(), false, 150, true, 200));
					return;
		}
		if (chance >= 235) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(rare), pos, player.getUsername(), false, 150, true, 200));
					return;
		}
		if (chance >= 200) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(common, 1), pos, player.getUsername(), false, 150, true, 200));
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
	public static CorpBeastBoss getCurrent() {
		return current;
	}

	/**
	 * 
	 * @param current
	 */
	public static void setCurrent(CorpBeastBoss current) {
		CorpBeastBoss.current = current;
	}

	/**
	 * 
	 * @author Levi <levi.patton69 @ skype>
	 *
	 */
	public static class CorpBeastBossLocation extends Position {

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
		public CorpBeastBossLocation(int x, int y, int z, String location) {
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