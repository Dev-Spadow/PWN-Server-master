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

public class Armadylabzyou extends NPC {
	public static Item[] COMMONLOOT = { new Item(10835, 500),new Item(989, 1),  new Item(10835, 200), new Item(13998, 1) };

	public static Item[] RARELOOT = { new Item(10835, 1500),new Item(14259, 1),  new Item(10835, 2000), new Item(989, 45), new Item(13998, 1), new Item(13999, 1)};

	public static Item[] SUPERRARELOOT = { new Item(10835, 5000), new Item(14249, 1), new Item(10835, 5000), new Item(13998, 2),new Item(13999, 2) };
	
	/**
	 * 
	 */
	public static final int NPC_ID = 25;

	/**
	 * add your maps to that folder open me your client.java in client
	 */
	public static final ArmadylabzyouLocation[] LOCATIONS = { new ArmadylabzyouLocation(2602, 5726, 0, " @red@(::hb)")  };

	/**
	 * 
	 */
	private static Armadylabzyou current;

	/**
	 * 
	 * @param position
	 */
	public Armadylabzyou(Position position) {

		super(NPC_ID, position);
	}

	/**
	 * 
	 */
	public static void initialize() {

		TaskManager.submit(new Task(4500, false) { // 6000

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

		ArmadylabzyouLocation location = Misc.randomElement(LOCATIONS);
		Armadylabzyou instance = new Armadylabzyou(location.copy());

		// System.out.println(instance.getPosition());

		World.register(instance);
		setCurrent(instance);
		// System.out.print("spawned.");
		//World.getPlayers().forEach(p -> p.getPacketSender().sendAnnouncement("[Hardened Boss]: Sephiroth has just Respawned"));
		World.sendMessage("Alert##Hardened Boss##" + "Sephiorth has just respawned at ::hb" + "## ");
		World.sendMessage("<col=fcfcfc><shad=dd8a14><img=418>[Hardened Boss]</shad>@bla@: Sephiroth@bla@ has just Respawned" + location.getLocation() + "");
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

	@SuppressWarnings("unused")
	public static void giveLoot(Player player, NPC npc, Position pos) {
		int chance = Misc.getRandom(100);
		Item common = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		Item common1 = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		Item rare = RARELOOT[Misc.getRandom(RARELOOT.length - 1)];
		Item superrare = SUPERRARELOOT[Misc.getRandom(SUPERRARELOOT.length - 1)];

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(10835, 750), pos, player.getUsername(), false, 150, true, 200));

		if (chance >= 99) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(superrare, pos, player.getUsername(), false, 150, true, 200));
			String itemName = (superrare.getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage(
					"<col=fcfcfc><shad=dd8a14><img=418>[Hardened Boss]</shad>@bla@: " + player.getUsername() + " received<col=FFFF64><shad=9f199d> " + itemMessage + " </shad>@bla@from Sephiroth");
			return;
		}

		if (chance >= 78) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(rare, pos, player.getUsername(), false, 150, true, 200));
			String itemName = rare.getDefinition().getName();
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage(
					"<col=fcfcfc><shad=dd8a14><img=418>[Hardened Boss]</shad>@bla@: " + player.getUsername() + " received@mag@<shad=9f199d> " + itemMessage + " </shad>@bla@from Sephiroth");
			return;
		}
		if (chance >= 0) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(common, pos, player.getUsername(), false, 150, true, 200));
			String itemName = (common.getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage(
					"<col=fcfcfc><shad=dd8a14><img=418>[Hardened Boss]</shad>@bla@: " + player.getUsername() + " received@mag@<shad=9f199d> " + itemMessage + " </shad>@bla@from Sephiroth");
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
	public static Armadylabzyou getCurrent() {
		return current;
	}

	/**
	 * 
	 * @param current
	 */
	public static void setCurrent(Armadylabzyou current) {
		Armadylabzyou.current = current;
	}

	/**
	 * 
	 * @author Levi <levi.patton69 @ skype>
	 *
	 */
	public static class ArmadylabzyouLocation extends Position {

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
		public ArmadylabzyouLocation(int x, int y, int z, String location) {
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