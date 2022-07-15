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

public class Bork extends NPC {

	public static int[] COMMONLOOT = {  2572,19137,19138,19139,6041,5130,
			18865,19132,19131,19133,6199,18940,18941,18942,2749,2750,2751,2752,2753,2754,13261,19721,19722,19723,18892,15418,19468,2572,11133,19025,15373,3824,3912,18782,3928,3956,4775,11732,17909,17911};
	public static int[] RARELOOT = { 19935,1666,19004,3069,3071 ,17918,5131,4771,4772,4770,4799,4800,4801,5079,3973,3951,15012
			,5131,4770,4771,4772,18347,3988,6193,6194,6195,6196,6197,6198,19935,1666,19004,3069,3071 ,17918,5131,4771,4772,4770};
	public static int[] SUPERRARELOOT = { 15374,19936,5184,3317,9505,9506,9507,19140,19890,19886,6320,4058,4057,4056 };

	/**
	 * 
	 */
	public static final int NPC_ID = 12800;

	/**
	 * add your maps to that folder open me your client.java in client
	 */
	public static final BorkLocation[] LOCATIONS = { new BorkLocation(3100, 5534, 0, "@red@ (::ob)") };

	/**
	 * 
	 */
	private static Bork current;

	/**
	 * 
	 * @param position
	 */
	public Bork(Position position) {

		super(NPC_ID, position);
	}

	/**
	 * 
	 */
	public static void initialize() {

		TaskManager.submit(new Task( 6000, false) { // 6000

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

		BorkLocation location = Misc.randomElement(LOCATIONS);
		Bork instance = new Bork(location.copy());

		// System.out.println(instance.getPosition());

		World.register(instance);
		setCurrent(instance);
		// System.out.print("spawned.");
		World.sendMessage("Alert##Obsidian Boss##" + "Lugia has just respawned at ::ob" + "## ");
		World.sendMessage("<col=fcfcfc><shad=dd8a14><img=418>[Obsidian Boss]</shad>@bla@: Darkrealm Lugia<col=##585858>@bla@ has just Respawned @red@(::ob)");
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
		int chance = Misc.getRandom(100);
		int common = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		@SuppressWarnings("unused")
		int common1 = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		int rare = RARELOOT[Misc.getRandom(RARELOOT.length - 1)];
		int superrare = SUPERRARELOOT[Misc.getRandom(SUPERRARELOOT.length - 1)];

		
		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(10835, 100), pos, player.getUsername(), false, 150, true, 200));
			player.getInventory().add(17750, Misc.inclusiveRandom(10, 150));
			
		if (chance >= 90) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(superrare), pos, player.getUsername(), false, 150, true, 200));
			String itemName = (new Item(superrare).getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage(
					"<col=fcfcfc><shad=dd8a14><img=418>[Obsidian Boss]</shad>@bla@: " + player.getUsername() + " received@mag@<shad=9f199d> " + itemMessage + " </shad>@bla@from Lugia");
			return;
		}

		if (chance >= 65) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(rare), pos, player.getUsername(), false, 150, true, 200));
			String itemName = (new Item(rare).getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage(
					"<col=fcfcfc><shad=dd8a14><img=418>[Obsidian Boss]</shad>@bla@: " + player.getUsername() + " received@mag@<shad=9f199d> " + itemMessage + " </shad>@bla@from Lugia");
			return;
		}
		if (chance >= 1) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(common, 1), pos, player.getUsername(), false, 150, true, 200));
			String itemName = (new Item(common).getDefinition().getName());
			World.sendMessage(
					"<col=fcfcfc><shad=dd8a14><img=418>[Obsidian Boss]</shad>@bla@: " + player.getUsername() + " received@mag@<shad=9f199d> " + itemName + " </shad>@bla@from Lugia");
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
	public static Bork getCurrent() {
		return current;
	}

	/**
	 * 
	 * @param current
	 */
	public static void setCurrent(Bork current) {
		Bork.current = current;
	}

	/**
	 * 
	 * @author Levi <levi.patton69 @ skype>
	 *
	 */
	public static class BorkLocation extends Position {

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
		public BorkLocation(int x, int y, int z, String location) {
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