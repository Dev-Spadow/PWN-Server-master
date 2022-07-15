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
import com.arlania.world.content.ShootingStar;
import com.arlania.world.content.ShootingStar.LocationData;
import com.arlania.world.content.combat.CombatFactory;
import com.arlania.world.entity.impl.GroundItemManager;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

public class General extends NPC {

	public static int[] COMMONLOOT = { 2572,19137,19138,19139,6041,5130,6507,
			18865,19132,19131,19133,6199,18940,5131,18941,18942,2749,2750,2751,2752,2753,2754,13261,19721,19132,19131,19133,6199,18940,18941,18942,2749,2750,2751,2752,2753,2754,13261,19721,19722,19723,18892,15418,19468};
	public static int[] RARELOOT = { 19935,12426,3988,14808,1666,19004,3069,3071 ,17918,5131,4771,4772,4770,19935,1666,19004,3069,3071 ,17918,5131,4771,4772,4770, 4799 ,4800,4801,5079,3973,3951,15012
			,5131,4770,4771,4772,18347,3988,14079,6193,6640,2756,6194,4082,6195,6196,6197,3904,6198,12162};
	public static int[] SUPERRARELOOT = {19936,13207,14259,14249,6640,12926,3971,4082,11309,14808,5184,298,16579,3317,9505,9506,9507,11978,19140,19886,6320,4058,4057,4056,6507,19890 };

	/**
	 * 
	 */
	public static final int NPC_ID = 9911;

	/**
	 * add your maps to that folder open me your client.java in client
	 */
	public static final GeneralLocation[] LOCATIONS = { new GeneralLocation(3037, 5346, 0, "@red@ (::wb) ") };
	
	private static LocationData LAST_LOCATION = null;
	
	
	
	
	
	/**
	 * 
	 */
	private static General current;

    /**
	 * 
	 * @param position
	 */
	public General(Position position) {

		super(NPC_ID, position);
	}

	/**
	 * 
	 */
	private static int ticksRequired = 6000;
    private static int ticksColapsed;
	public static void initialize() {
        ticksColapsed = 0;
		TaskManager.submit(new Task(1, false) { // 6000
            
			@Override
			public void execute() {
                //System.out.println("ticksColapsed: " + ticksColapsed);
                //System.out.println("ticksRequired: " + ticksRequired);
                ticksColapsed++;
                World.refreshPanel();
                if(ticksColapsed == ticksRequired) spawn();
			}
		});

	}

	/**
	 * 
	 */
	public static void spawn() {
        ticksColapsed = 0;

		if (getCurrent() != null) {
			System.out.println("isntnull");
			return;
		}

		GeneralLocation location = Misc.randomElement(LOCATIONS);
		General instance = new General(location.copy());

		// System.out.println(instance.getPosition());
		System.out.println("work");
		World.register(instance);
		setCurrent(instance);
		// System.out.print("spawned.");
		World.sendMessage("Alert##World Boss##" + "World Boss has just respawned at ::wb" + "## ");
		World.sendMessage("<col=fcfcfc><shad=dd8a14><img=418>[World Boss]</shad>@bla@: Yk'Lagor<col=##585858>@bla@ has just Respawned" + location.getLocation() + "");
	
	
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
			System.out.println("player killer"+killer.getUsername());
			int damage = entry.getValue();

			handleDrop(npc, killer, damage);

			if (++count >= 20) {
				break;
			}

		}
	}

	public static void giveLoot(Player player, NPC npc, Position pos) {
		setCurrent(null);
		int chance = Misc.getRandom(100);
		int common = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		@SuppressWarnings("unused")
		int common1 = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		int rare = RARELOOT[Misc.getRandom(RARELOOT.length - 1)];
		int superrare = SUPERRARELOOT[Misc.getRandom(SUPERRARELOOT.length - 1)];

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(10835, 500), pos, player.getUsername(), false, 150, true, 200));
		player.getInventory().add(17750, Misc.inclusiveRandom(10, 200));
		if (chance >= 98) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(superrare), pos, player.getUsername(), false, 150, true, 200));
			String itemName = (new Item(superrare).getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage(
					"<col=fcfcfc><shad=dd8a14><img=418>[World Boss]</shad>@bla@ " + player.getUsername() + " received<col=FFFF64><shad=ebf217> " + itemMessage + "</shad>@bla@ from Yk'Lagor");
			return;
		}

		if (chance >= 86) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(rare), pos, player.getUsername(), false, 150, true, 200));
			String itemName = (new Item(rare).getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage(
					"<col=fcfcfc><shad=dd8a14><img=418>[World Boss]</shad>@bla@ " + player.getUsername() + " received@mag@<shad=9f199d> " + itemMessage + "</shad>@bla@ from Yk'Lagor");
			return;
		}
		if (chance >= 0) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(common, 1), pos, player.getUsername(), false, 150, true, 200));
			String itemName = (new Item(common).getDefinition().getName());
			World.sendMessage(
					"<col=fcfcfc><shad=dd8a14><img=418>[World Boss]</shad>@bla@ " + player.getUsername() + " received@mag@<shad=9f199d> " + itemName + "</shad>@bla@ from Yk'Lagor");
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
	public static General getCurrent() {
		return current;
	}

	/**
	 * 
	 * @param current
	 */
	public static void setCurrent(General current) {
		General.current = current;
	}

	/**
	 * 
	 * @author Levi <levi.patton69 @ skype>
	 *
	 */
	public static class GeneralLocation extends Position {

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
		public GeneralLocation(int x, int y, int z, String location) {
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

	public static String getCurrentSpawnTime() {
		return (int) ((ticksRequired - ticksColapsed) * 0.6) + " @or1@secs";
	}

	public static String getCurrentLocation() {
		return getCurrent() != null ? "ACTIVE" : getCurrentSpawnTime();
	}

}
