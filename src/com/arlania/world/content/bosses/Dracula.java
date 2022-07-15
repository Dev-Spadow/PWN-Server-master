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

public class Dracula extends NPC {

	public static int[] COMMONLOOT = { 2572,19137,19138,19139,6041,5130,
			18865,19132,19131,19133,3912,1481,6640,6199};
	public static int[] RARELOOT = { 19935,1481,12426,3988,14808,4770,19935,1666,19004,3069,3071,
			5131,4770,4771,4772,18347,3988,6193,6640,2756,6194,4082,6195,6196,6197,3904,6198,12162,6507};
	public static int[] SUPERRARELOOT = {19936,1481,13207,14249,6640,12926,3444,3971,4082,14808,5184,16579,3317,9505,9506,9507,19140,19886,6320,4058,4057,4056,6507,19890 };

	/**
	 * 
	 */
	public static final int NPC_ID = 3340;

	/**
	 * add your maps to that folder open me your client.java in client
	 */
	public static final DraculaLocation[] LOCATIONS = { 
		
			new DraculaLocation(3240, 9812, 0, "<col=0999ad> <img=385> ::moley ") };
	
	
	
	
	
	
	private static LocationData LAST_LOCATION = null;
	
	
	
	
	
	/**
	 * 
	 */
	private static Dracula current;

	/**
	 * 
	 * @param position
	 */
	public Dracula(Position position) {

		super(NPC_ID, position);
	}

	/**
	 * 
	 */
	public static void initialize() {

		TaskManager.submit(new Task( 6000, false) { // 1hrs

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

		DraculaLocation location = Misc.randomElement(LOCATIONS);
		Dracula instance = new Dracula(location.copy());

		// System.out.println(instance.getPosition());

		World.register(instance);
		setCurrent(instance);
		// System.out.print("spawned.");

		World.sendMessage("<img=418><col=bababa>[<col=0999ad><shad=200>Iron Boss<col=bababa>]<col=0999ad>Moley has Respawned at <col=00a745>" + location.getLocation() + "");
	
	
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
				new GroundItem(new Item(10835, 500), pos, player.getUsername(), false, 150, true, 200));
		     
		        
		        
		if (chance >= 92) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(superrare), pos, player.getUsername(), false, 150, true, 200));
			player.getPointsHandler().setSlayerPoints(80, true);
		    player.getPointsHandler().setBossPoints(500, true);	
			String itemName = (new Item(superrare).getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage(
					"<img=382><col=FF0000>" + player.getUsername() + " received<col=eaeaea><img=386>[ " + itemMessage + "<col=eaeaea>]<img=386><col=FF0000>from Moley!");
			return;
		}

		if (chance >= 70) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(rare), pos, player.getUsername(), false, 150, true, 200));
			player.getPointsHandler().setSlayerPoints(80, true);
		    player.getPointsHandler().setBossPoints(500, true);
			String itemName = (new Item(rare).getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage(
					"<img=382><col=FF0000>" + player.getUsername() + " received<img=386><col=eaeaea>[ " + itemMessage + "<col=eaeaea>]<img=386><col=FF0000> from Moley!");
			return;
		}
		if (chance >= 0) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(common, 1), pos, player.getUsername(), false, 150, true, 200));
			player.getPointsHandler().setSlayerPoints(80, true);
		    player.getPointsHandler().setBossPoints(500, true);
			String itemName = (new Item(common).getDefinition().getName());
			World.sendMessage(
					"<img=382><col=FF0000>" + player.getUsername() + " received<col=eaeaea><img=386>[<col=07b481> " + itemName + "<col=eaeaea>]<img=386><col=FF0000> from Moley!");
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
	public static Dracula getCurrent() {
		return current;
	}

	/**
	 * 
	 * @param current
	 */
	public static void setCurrent(Dracula current) {
		Dracula.current = current;
	}

	/**
	 * 
	 * @author Levi <levi.patton69 @ skype>
	 *
	 */
	public static class DraculaLocation extends Position {

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
		public DraculaLocation(int x, int y, int z, String location) {
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
		// TODO Auto-generated method stub
		return null;
	}

	public static Object getCurrentLocation() {
		// TODO Auto-generated method stub
		return null;
	}

}