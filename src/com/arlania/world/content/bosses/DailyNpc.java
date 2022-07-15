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

public class DailyNpc extends NPC {

	public static int[] COMMONLOOT = {  2572,19137,19138,19139,6041,5130,
			18865,19132,19131,19133,6199,18940,19935,18941,18942,2749,2750,2751,2752,2753,2754,13261,19721,19132,19131,19133,6199,18940,18941,18942,2749,2750,2751,2752,2753,2754,13261,19721,19722,19723,18892,15418,19468,
			10835,744,18957,12708,13235,13239,16137,4771,4770,4772,12601,15012,6197,2577,6733,4762,4761,4764,4059,5154,3957,5157,5160};
	public static int[] RARELOOT = {19935,19936,14259,5155,4082,926,5211,5210,15045,18950,5130,3928,18985,18928,12826,6192,19886,5184,14808,3647,19618};
	public static int[] SUPERRARELOOT = { 13265,14249,4082,13266,19158,19890,13270,13267,13268,19936,19958,6480,12827,5170,15660};

	/**
	 * 
	 */
	public static final int NPC_ID = 4595;

	/**
	 * add your maps to that folder open me your client.java in client
	 */
	public static final DailyNpcLocation[] LOCATIONS = { new DailyNpcLocation(3157, 9905, 0, " <col=56289b>") };

	/**
	 * 
	 */
	private static DailyNpc current;

	/**
	 * 
	 * @param position
	 */
	public DailyNpc(Position position) {

		super(NPC_ID, position);
	}

	/**
	 * 
	 */
	public static void initialize() {

		TaskManager.submit(new Task(12000, false) { // 6000

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

		DailyNpcLocation location = Misc.randomElement(LOCATIONS);
		DailyNpc instance = new DailyNpc(location.copy());

		// System.out.println(instance.getPosition());

		World.register(instance);
		setCurrent(instance);
		// System.out.print("spawned.");
		World.sendMessage("Alert##Halloween##" + "Pennywise has just respawned at ::event" + "## ");
		World.sendMessage("<col=fcfcfc><shad=dd8a14><img=418>[Halloween]</shad>@bla@: Pennywise <col=##585858>@bla@ has just Respawned @red@(::event)");
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
					"<img=382><col=a60a06><shad=200>" + player.getUsername() + " received <col=0f0e01><shad=100>" + itemMessage + "<col=a60a06> <shad=200>from Vip Boss!");
					return;
		}

		if (chance >= 70) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(rare), pos, player.getUsername(), false, 150, true, 200));
			String itemName = (new Item(rare).getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage(
					"<img=382><col=a60a06><shad=200>" + player.getUsername() + " received <col=0f0e01><shad=100>" + itemMessage + "<col=a60a06> <shad=200>from Vip Boss!");
					return;
		}
		if (chance >= 0) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(common, 1), pos, player.getUsername(), false, 150, true, 200));
			String itemName = (new Item(common).getDefinition().getName());
			World.sendMessage(
					"<img=382><col=a60a06><shad=200>" + player.getUsername() + " received <col=0f0e01><shad=100> " + itemName + "<col=a60a06> <shad=200>Vip Boss!");
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
	public static DailyNpc getCurrent() {
		return current;
	}

	/**
	 * 
	 * @param current
	 */
	public static void setCurrent(DailyNpc current) {
		DailyNpc.current = current;
	}

	/**
	 * 
	 * @author Levi <levi.patton69 @ skype>
	 *
	 */
	public static class DailyNpcLocation extends Position {

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
		public DailyNpcLocation(int x, int y, int z, String location) {
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

}
