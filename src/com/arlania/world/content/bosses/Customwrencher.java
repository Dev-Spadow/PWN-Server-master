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
import com.arlania.world.content.combat.strategy.CombatStrategy;
import com.arlania.world.entity.impl.GroundItemManager;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

public class Customwrencher extends NPC {
	
	public static Item[] COMMONLOOT = { new Item(744, 25),new Item(10835, 25), new Item(19137, 1), new Item(14457, 1), new Item(14455, 1), new Item(14453, 1),new Item(6197, 1),
			new Item(13876),new Item(13873),new Item(14249),new Item(13870),new Item(1464,1),new Item(19864,100),new Item(3973),new Item(18865),new Item(1543, 2),new Item(19080, 50), new Item(989, 5),new Item(18748),new Item(5130),new Item(14559),new Item(19138, 1), new Item(19133), new Item(19132), new Item(19131), new Item(19139),  new Item(6041), new Item(15373), new Item(15044), new Item(15220) };

	public static Item[] RARELOOT = { 
			new Item(2749), new Item(13261), new Item(2754), new Item(18942), new Item(18941), new Item(18940), new Item(922),new Item(2753),  new Item(12502),
			new Item(5184),new Item(5173),new Item(5131),new Item(2752), new Item(2750), new Item(2751),
			new Item(13727,50),new Item(18929),new Item(19886),new Item(10835, 2000),new Item(19935, 1),new Item(926, 1) };

	public static Item[] SUPERRARELOOT = { new Item(19721, 1),new Item(18865, 1), new Item(19722, 1), new Item(19723, 1), new Item(19734, 1),
			new Item(5154),new Item(15653),new Item(19736), new Item(19468), new Item(15418),new Item(19936, 1),new Item(3911, 1) };
	
	/**
	 * 
	 */
	public static final int NPC_ID = 68;

	/**
	 * add your maps to that folder open me your client.java in client
	 */
	public static final CustomwrencherLocation[] LOCATIONS = { new CustomwrencherLocation(2400, 2843, 0, "@red@ ::sb") };

	/**
	 * 
	 */
	private static Customwrencher current;

	/**
	 * 
	 * @param position
	 */
	public Customwrencher(Position position) {

		super(NPC_ID, position);
	}

	/**
	 * 
	 */
	public static void initialize() {

		TaskManager.submit(new Task(1500, false) { // 6000

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

		CustomwrencherLocation location = Misc.randomElement(LOCATIONS);
		Customwrencher instance = new Customwrencher(location.copy());

		// System.out.println(instance.getPosition());

		World.register(instance);
		setCurrent(instance);
		// System.out.print("spawned.");
//f4e019
		World.sendMessage("<col=fcfcfc><shad=dd8a14><img=418>[Starter Boss]</shad>@bla@: Kalvoth<col=##585858>@bla@ has just Respawned" + location.getLocation() + "");
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
		int chance = Misc.getRandom(1000);
		Item common = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		Item common1 = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		Item rare = RARELOOT[Misc.getRandom(RARELOOT.length - 1)];
		Item superrare = SUPERRARELOOT[Misc.getRandom(SUPERRARELOOT.length - 1)];

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(10835, 10), pos, player.getUsername(), false, 150, true, 200));
		player.getInventory().add(17750, Misc.inclusiveRandom(10, 50));
		
		if (chance >= 980) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(superrare, pos, player.getUsername(), false, 150, true, 200));
			String itemName = (superrare.getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage(
					"<col=fcfcfc><shad=dd8a14><img=418>[Starter Boss]</shad>@bla@: " + player.getUsername() + " received@mag@<shad=9f199d> " + itemMessage + " </shad>@bla@from Kalvoth");
			return;
		}

		if (chance >= 830) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(rare, pos, player.getUsername(), false, 150, true, 200));
			String itemName = rare.getDefinition().getName();
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage(
					"<col=fcfcfc><shad=dd8a14><img=418>[Starter Boss]</shad>@bla@: " + player.getUsername() + " received @mag@<shad=9f199d>" + itemMessage + " </shad>@bla@from Kalvoth");
			return;
		}
		if (chance >= 0) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(common, pos, player.getUsername(), false, 150, true, 200));
			String itemName = (common.getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage(
					"<col=fcfcfc><shad=dd8a14><img=418>[Starter Boss]</shad>@bla@: " + player.getUsername() + " received @mag@<shad=9f199d>" + itemMessage + " </shad>@bla@from Kalvoth");
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
	public static Customwrencher getCurrent() {
		return current;
	}

	/**
	 * 
	 * @param current
	 */
	public static void setCurrent(Customwrencher current) {
		Customwrencher.current = current;
	}

	/**
	 * 
	 * @author Levi <levi.patton69 @ skype>
	 *
	 */
	public static class CustomwrencherLocation extends Position {

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
		public CustomwrencherLocation(int x, int y, int z, String location) {
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

