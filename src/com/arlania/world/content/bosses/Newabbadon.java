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

public class Newabbadon extends NPC {
	public static Item[] COMMONLOOT = { new Item(10835, 250), new Item(10835, 50),new Item(744, 20),
			new Item(19721, 1), new Item(19722, 1), new Item(19723, 1), new Item(19724, 1), new Item(19736), new Item(19734), new Item(18380), new Item(18381), new Item(18382), new Item(9006), new Item(3941), new Item(18392) };

	public static Item[] RARELOOT = { new Item(4799), new Item(4800), new Item(4801), new Item(5079), new Item(3951), new Item(3960), new Item(3958), new Item(3959), new Item(5187), new Item(5186), new Item(3316), new Item(3931), new Item(14559),
			new Item(10835, 500),new Item(455, 3),new Item(1053),new Item(1055),new Item(1057),new Item(19886),new Item(19935),new Item(744, 30) };

	public static Item[] SUPERRARELOOT = { new Item(18950), new Item(4673), new Item(18748), new Item(18751),
			new Item(5131), new Item(4770), new Item(4771), new Item(4772), new Item(6199), new Item(3988), new Item(6193), new Item(6194), new Item(6195), new Item(6196), new Item(6197), new Item(6198) , new Item(3063),new Item(19936),
			new Item(10835, 5000),new Item(14808),new Item(455, 5),new Item(744, 60) };
	
	/**
	 * 
	 */
	public static final int NPC_ID = 4972;

	/**
	 * add your maps to that folder open me your client.java in client
	 */
	public static final AbbadonLocation[] LOCATIONS = { new AbbadonLocation(3043, 3416, 0, " @red@(::mb)") };

	/**
	 * 
	 */
	private static Newabbadon current;

	/**
	 * 
	 * @param position
	 */
	public Newabbadon(Position position) {

		super(NPC_ID, position);
	}

	/**
	 * 
	 */
	public static void initialize() {

		TaskManager.submit(new Task(3000, false) { // 6000

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

		AbbadonLocation location = Misc.randomElement(LOCATIONS);
		Newabbadon instance = new Newabbadon(location.copy());

		// System.out.println(instance.getPosition());

		World.register(instance);
		setCurrent(instance);
		// System.out.print("spawned.");
		World.sendMessage("Alert##Medium Boss##" + "Virulent Dragon has just respawned at ::mb" + "## ");
		World.sendMessage("<img=418><col=fcfcfc><shad=dd8a14>[Medium Boss]</shad>@bla@: Virulent Dragon<col=##585858>@bla@ has just Respawned" + location.getLocation() + "");
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

	@SuppressWarnings("unused")
	public static void giveLoot(Player player, NPC npc, Position pos) {
		int chance = Misc.getRandom(1000);
		Item common = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		Item common1 = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		Item rare = RARELOOT[Misc.getRandom(RARELOOT.length - 1)];
		Item superrare = SUPERRARELOOT[Misc.getRandom(SUPERRARELOOT.length - 1)];

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(10835, 10), pos, player.getUsername(), false, 150, true, 200));
		player.getInventory().add(17750, Misc.inclusiveRandom(10, 100));
		if (chance >= 980) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(superrare, pos, player.getUsername(), false, 150, true, 200));
			String itemName = (superrare.getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage(
					"<img=418><col=fcfcfc><shad=dd8a14>[Medium Boss]</shad>@bla@: " + player.getUsername() + " received@mag@<shad=9f199d> " + itemMessage + "</shad>@bla@ from Virulent Dragon");
			return;
		}

		if (chance >= 730) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(rare, pos, player.getUsername(), false, 150, true, 200));
			String itemName = rare.getDefinition().getName();
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage(
					"<img=418><col=fcfcfc><shad=dd8a14>[Medium Boss]</shad>@bla@: " + player.getUsername() + " received@mag@<shad=9f199d> " + itemMessage + "</shad>@bla@ from Virulent Dragon");
			return;
		}
		if (chance >= 0) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(common, pos, player.getUsername(), false, 150, true, 200));
			String itemName = (common.getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage(
					"<img=418><col=fcfcfc><shad=dd8a14>[Medium Boss]</shad>@bla@: " + player.getUsername() + " received@mag@<shad=9f199d> " + itemMessage + "</shad>@bla@ from Virulent Dragon");
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
	public static Newabbadon getCurrent() {
		return current;
	}

	/**
	 * 
	 * @param current
	 */
	public static void setCurrent(Newabbadon current) {
		Newabbadon.current = current;
	}

	/**
	 * 
	 * @author Levi <levi.patton69 @ skype>
	 *
	 */
	public static class AbbadonLocation extends Position {

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
		public AbbadonLocation(int x, int y, int z, String location) {
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
