package com.arlania.world.content.teleportation.teleportsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.arlania.model.Item;
import com.arlania.model.definitions.NPCDrops;
import com.arlania.model.definitions.NPCDrops.DropChance;
import com.arlania.model.definitions.NPCDrops.NpcDropItem;
import com.arlania.world.entity.impl.player.Player;

public class BossInformation {

	/**
	 * 
	 * @param buttonId
	 * @param player
	 */
	public static void handleInformation(int buttonId, Player player) {
		player.getPacketSender().resetItemsOnInterface(36921, 80);
		for (int i = 0; i < 5; i++) {
			player.getPA().sendFrame126("", 36926 + i);
		}

		for (int i = 0; i < 8; i++) {
			player.getPA().sendFrame126("", 36931 + i);
		}
		for (BossInformationEnum bie : BossInformationEnum.values()) {
			if (bie.getButtonId() == buttonId) {
				player.setSelectedPosition(bie.getPos());
				player.getPA().sendFrame126(bie.getBossName(), 36808);
				Map<DropChance, List<Item>> dropTables = new HashMap<>();
				NPCDrops drops = NPCDrops.forId(bie.npcId);
				NpcDropItem[] items = drops.getDropList();
				
				for(NpcDropItem item : items) {
					
					DropChance chance = item.getChance();
					List<Item> table = dropTables.get(chance);
					

					
					if(table == null) {
						dropTables.put(chance, table = new ArrayList<Item>());
					}
					
					boolean found = false;
					
					for(Item drop : table) {
						if(drop != null && drop.getId() == item.getId()) {
							found = true;
							break;
						}
					}
					
					if(!found) {
						
						int amount = arrayMax(item.getCount());
						
						if(amount >= 1 && amount <= Integer.MAX_VALUE) {
							table.add(new Item(item.getId(), amount));
						}
						
					}
					
				}
				for(Entry<DropChance, List<Item>> entry : dropTables.entrySet()) {
					List<Item> dropTable = entry.getValue();
					
					if (dropTable.size() == 0) {
						continue;
					}
					if (bie.npcId == 12800) {
						List<Item> borkDrops = new ArrayList<>(); 
						int[] LOOT = { 2572,11133,19025,15373,3824,3912,18782,3928,3956,4775,11732,17909,17911,2572,
								11133,19025,15373,3824,3912,18782,3928,3956,4775,11732,17909,17911,2572,11133,19025,
								15373,3824,3912,18782,3928,3956,4775,11732,17909,17911,19935,1666,19004,3069,
								3071 ,17918,5131,4771,4772,4770,19935,1666,19004,3069,3071 ,17918,5131,4771,
								4772,4770,15374,19936,5184,3317,9505,9506,9507,11978,19140,19886,6320,4058,4057,4056};
						for (int i = 0; i < LOOT.length - 1; i++) {
							borkDrops.add(new Item(LOOT[i]));
						}
						
						player.getPacketSender().sendItemsOnInterface(36921, 80, borkDrops, false);
					} else {
						player.getPacketSender().sendItemsOnInterface(36921, 80, dropTable, false);
					}					
				}
				
				for (int k = 0; k < bie.getInformation().length; k++) {
					player.getPA().sendFrame126(bie.getInformation()[k], 36926 + k);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param buttonId
	 * @param player
	 */
	public static void handleWildyInformation(int buttonId, Player player) {
		player.getPacketSender().resetItemsOnInterface(37921, 60);
		for (int i = 0; i < 5; i++) {
			player.getPA().sendFrame126("", 37926 + i);
		}

		for (int i = 0; i < 8; i++) {
			player.getPA().sendFrame126("", 37922 + i);
		}
		for (BossInformationEnum bie : BossInformationEnum.values()) {
			if (bie.getButtonId() == buttonId) {
				player.setSelectedPosition(bie.getPos());
				player.getPA().sendFrame126(bie.getBossName(), 37808);
				Map<DropChance, List<Item>> dropTables = new HashMap<>();
				NPCDrops drops = NPCDrops.forId(bie.npcId);
				NpcDropItem[] items = drops.getDropList();
				
				for(NpcDropItem item : items) {
					
					DropChance chance = item.getChance();
					List<Item> table = dropTables.get(chance);
					

					
					if(table == null) {
						dropTables.put(chance, table = new ArrayList<Item>());
					}
					
					boolean found = false;
					
					for(Item drop : table) {
						if(drop != null && drop.getId() == item.getId()) {
							found = true;
							break;
						}
					}
					
					if(!found) {
						
						int amount = arrayMax(item.getCount());
						
						if(amount >= 1 && amount <= Integer.MAX_VALUE) {
							table.add(new Item(item.getId(), amount));
						}
						
					}
					
				}
				
				for(Entry<DropChance, List<Item>> entry : dropTables.entrySet()) {
					List<Item> dropTable = entry.getValue();
					
					if (dropTable.size() == 0) {
						continue;
					}
					
					player.getPacketSender().sendItemsOnInterface(37921, 60, dropTable, true);
					
				}
				for (int k = 0; k < bie.getInformation().length; k++) {
					player.getPA().sendFrame126(bie.getInformation()[k], 37926 + k);
				}
			}
		}
	}
	
	public static int arrayMax(int[] arr) {
		int max = Integer.MIN_VALUE;

	    for(int cur: arr)
	        max = Math.max(max, cur);

	    return max;
	}
}
