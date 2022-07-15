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

public class TheZamorakLefosh extends NPC {
	public static Item[] COMMONLOOT = { new Item(3912, 1), new Item(6199, 1),
			new Item(10835, 1000),
			new Item(15373, 1),
			new Item(3988, 1),
			new Item(10835, 2500), new Item(1543, 1), new Item(3912, 1) };

	public static Item[] RARELOOT = { new Item(19886,1), new Item(19106,1), new Item(6199,1), new Item(3912,2), new Item(10835,5500), new Item(6507) };

	public static Item[] SUPERRARELOOT = { new Item(10205), new Item(18471, 1), new Item(18470, 1), new Item(18638, 1), new Item(18637, 1), new Item(18636, 1), new Item(5266),  new Item(19936),new Item(6640), new Item(19935) };
	
	/**
	 * 
	 */
	public static final int NPC_ID = 2509;

	/**
	 * add your maps to that folder open me your client.java in client
	 */
	public static final ZamorakLefoshLocation[] LOCATIONS = { new ZamorakLefoshLocation(3239, 9813, 0, " @red@( ::ironman )") };

	/**
	 * 
	 */
	private static TheZamorakLefosh current;

	/**
	 * 
	 * @param position
	 */
	public TheZamorakLefosh(Position position) {

		super(NPC_ID, position);
	}

	/**
	 * 
	 */
	public static void initialize() {

		TaskManager.submit(new Task(4500, false) { // 3000

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

		ZamorakLefoshLocation location = Misc.randomElement(LOCATIONS);
		TheZamorakLefosh instance = new TheZamorakLefosh(location.copy());

		// System.out.println(instance.getPosition());

		World.register(instance);
		setCurrent(instance);
		// System.out.print("spawned.");
		World.sendMessage("Alert##Ironman Boss##" + "Ironman has just respawned at ::ironman" + "## ");
		World.sendMessage("<col=fcfcfc><shad=dd8a14><img=418>[Ironman Boss]</shad>@bla@: Ironman<col=##585858>@bla@ has just Respawned" + location.getLocation() + "");
	}

	/**
	 * 
	 * @param npc
	 */
	public static void handleDrop(NPC npc) {

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
		Item common = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		@SuppressWarnings("unused")
		Item common1 = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		Item rare = RARELOOT[Misc.getRandom(RARELOOT.length - 1)];
		Item superrare = SUPERRARELOOT[Misc.getRandom(SUPERRARELOOT.length - 1)];

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(10835, 100), pos, player.getUsername(), false, 150, true, 200));
		player.getInventory().add(17750, Misc.inclusiveRandom(10, 200));
		if (chance >= 98) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(superrare, pos, player.getUsername(), false, 150, true, 200));
			String itemName = (superrare.getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage(
					"<col=fcfcfc><shad=dd8a14><img=418>[Ironman Boss]</shad>@bla@ " + player.getUsername() + " received@mag@<shad=7c0a9e> " + itemName + " </shad>@bla@from Ironman");
			return;
		}

		if (chance >= 94) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(rare, pos, player.getUsername(), false, 150, true, 200));
			String itemName = rare.getDefinition().getName();
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage(
					"<col=fcfcfc><shad=dd8a14><img=418>[Ironman Boss]</shad>@bla@ " + player.getUsername() + " received@mag@<shad=7c0a9e> " + itemName + " </shad>@bla@from Ironman");
			return;
		}
		if (chance >= 0) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(common, pos, player.getUsername(), false, 150, true, 200));
			String itemName = (common.getDefinition().getName());
			World.sendMessage(
					"<col=fcfcfc><shad=dd8a14><img=418>[Ironman Boss]</shad>@bla@ " + player.getUsername() + " received@mag@<shad=ebf217> " + itemName + " </shad>@bla@from Ironman");
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
	public static TheZamorakLefosh getCurrent() {
		return current;
	}

	/**
	 * 
	 * @param current
	 */
	public static void setCurrent(TheZamorakLefosh current) {
		TheZamorakLefosh.current = current;
	}

	/**
	 * 
	 * @author Levi <levi.patton69 @ skype>
	 *
	 */
	public static class ZamorakLefoshLocation extends Position {

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
		public ZamorakLefoshLocation(int x, int y, int z, String location) {
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
