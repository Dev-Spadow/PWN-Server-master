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

public class GlacorBoss extends NPC {

	public static int[] COMMONLOOT = {10835,15374,3912,989,1543};
	public static int[] EXTREMELOOT = {14443,14482,14483};
	/**
	 * 
	 */
	public static final int NPC_ID = 1382;

	/**
	 * add your maps to that folder open me your client.java in client
	 */
	public static final GlacorBossLocation[] LOCATIONS = { new GlacorBossLocation(2772, 3745, 4, "") };

	/**
	 * 
	 */
	private static GlacorBoss current;

	/**
	 * 
	 * @param position
	 */
	public GlacorBoss(Position position) {

		super(NPC_ID, position);
	}

	/**
	 * 
	 */
	public static void initialize() {

		TaskManager.submit(new Task( 200000, false) { // 6000

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

		GlacorBossLocation location = Misc.randomElement(LOCATIONS);
		GlacorBoss instance = new GlacorBoss(location.copy());

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
		int chance = Misc.getRandom(250);
		int common = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		int extremeloot = EXTREMELOOT[Misc.getRandom(EXTREMELOOT.length - 1)];
		String itemName = (new Item(extremeloot).getDefinition().getName());
		String itemMessage = Misc.anOrA(itemName) + " " + itemName;
		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(10835, 200), pos, player.getUsername(), false, 150, true, 200));
		if (chance >= 150) {
			
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(common), pos, player.getUsername(), false, 150, true, 200));
					return;
		}

		if (chance >= 249) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(10835, 2500), pos, player.getUsername(), false, 150, true, 200));
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(extremeloot), pos, player.getUsername(), false, 150, true, 200));
			World.sendMessage(
					"<col=fcfcfc><shad=dd8a14><img=418>[Glacial Boss]</shad>@bla@: " + player.getUsername() + " received@mag@<shad=9f199d> " + itemMessage + "</shad>@bla@ from Glacor");
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
	public static GlacorBoss getCurrent() {
		return current;
	}

	/**
	 * 
	 * @param current
	 */
	public static void setCurrent(GlacorBoss current) {
		GlacorBoss.current = current;
	}

	/**
	 * 
	 * @author Levi <levi.patton69 @ skype>
	 *
	 */
	public static class GlacorBossLocation extends Position {

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
		public GlacorBossLocation(int x, int y, int z, String location) {
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

	public static Object getCurrentLocation() {
		// TODO Auto-generated method stub
		return null;
	}

}