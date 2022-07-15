package com.arlania.world.content.chests;

import com.arlania.model.Animation;
import com.arlania.model.GameObject;
import com.arlania.model.Item;
import com.arlania.model.PlayerRights;
import com.arlania.util.Misc;
import com.arlania.util.RandomUtility;
import com.arlania.world.content.Achievements;
import com.arlania.world.content.Achievements.AchievementData;
import com.arlania.world.entity.impl.player.Player;

public class CrystalChest {

	public static void handleChest(final Player p) {
		if(!p.getClickDelay().elapsed(3000)) 
			return;
		if(!p.getInventory().contains(989)) {
			p.getPacketSender().sendMessage("This chest can only be opened with a Starter Key.");
			return;
		}
		p.performAnimation(new Animation(827));
		if (p.getRights() == PlayerRights.DONATOR) {
			if (Misc.getRandom(50) == 5) {
				p.getPacketSender().sendMessage("Starter Key has been saved as a donator benefit");
			} else {
				p.getInventory().delete(989, 1);
			}
		}
		if (p.getRights() == PlayerRights.SUPER_DONATOR || p.getRights() == PlayerRights.SUPPORT) {
			if (Misc.getRandom(75) == 5) {
				p.getPacketSender().sendMessage("Starter Key has been saved as a donator benefit");
			} else {
				p.getInventory().delete(989, 1);
			}
		}
		if (p.getRights() == PlayerRights.ULTRA_DONATOR || p.getRights() == PlayerRights.MODERATOR) {
			if (Misc.getRandom(45) == 5) {
				p.getPacketSender().sendMessage("Starter Key has been saved as a donator benefit");
			} else {
				p.getInventory().delete(989, 1);
			}
		}
		if (p.getRights() == PlayerRights.MYSTIC_DONATOR  || p.getRights() == PlayerRights.OBSIDIAN_DONATOR  ||p.getRights() == PlayerRights.ADMINISTRATOR) {
			if (Misc.getRandom(40) == 5) {
				p.getPacketSender().sendMessage("Starter Key has been saved as a donator benefit");
			} else {
				p.getInventory().delete(989, 1);
			}
		}
		if (p.getRights() == PlayerRights.CELESTIAL_DONATOR  || p.getRights() == PlayerRights.EXECUTIVE_DONATOR|| p.getRights() == PlayerRights.DIVINE_DONATOR|| p.getRights() == PlayerRights.SUPREME_DONATOR|| p.getRights() == PlayerRights.LEGENDARY_DONATOR) {
			if (Misc.getRandom(30) == 5) {
				p.getPacketSender().sendMessage("Starter Key has been saved as a donator benefit");
			} else {
				p.getInventory().delete(989, 1);
			}
		}
		if (p.getRights() == PlayerRights.DEVELOPER) {
			if (Misc.getRandom(20) == 2) {
				p.getPacketSender().sendMessage("Starter Key has been saved as a donator benefit");
			} else {
				p.getInventory().delete(989, 1);
			}
		}
		if (p.getRights() == PlayerRights.PLAYER || p.getRights() == PlayerRights.YOUTUBER) {
			p.getInventory().delete(989, 1);
		}
		p.getPacketSender().sendMessage("You open the chest..");
		p.getAchievementTracker().progress(com.arlania.world.content.achievements.AchievementData.OPEN_THE_STARTER_CHEST, 1);
		
					Item[] loot = itemRewards[Misc.getRandom(itemRewards.length - 1)];
					for(Item item : loot) {
						p.getInventory().add(item);
					}
					p.getInventory().add(10835, 1 + RandomUtility.RANDOM.nextInt(10));
				
					//CustomObjects.objectRespawnTask(p, new GameObject(173 , chest.getPosition().copy(), 10, 0), chest, 10);
				
	}

	private static final Item[][] itemRewards =  {
			
			{new Item(13258,1)},
			{new Item(13259,1)},
			{new Item(13256,1)},
			{new Item(17849,1)},
			{new Item(19137,1)},
			{new Item(19138,1)},
			{new Item(19139,1)},
			{new Item(5130,1)},
			{new Item(19131,1)},
			{new Item(19132,1)},
			{new Item(19133,1)},
			{new Item(18865,1)},
			{new Item(15398,1)},
			
			{new Item(13261,1)},
			{new Item(2749,1)},
			{new Item(2750,1)},
			{new Item(2751,1)},
			{new Item(2752,1)},
			{new Item(2753,1)},
			{new Item(2754,1)},
			{new Item(19721,1)},
			{new Item(19722,1)},
			{new Item(19723,1)},
			{new Item(19734,1)},
			{new Item(19736,1)},
			
			
			{new Item(15332,1)},
			{new Item(15373,1)},
			{new Item(18392,1)},
			{new Item(19080,5)},
			{new Item(10835,10)},
			{new Item(10835,3)},
			{new Item(19864,200)},
			{new Item(19864,200)},
			{new Item(19864,200)},

		};
	
}
