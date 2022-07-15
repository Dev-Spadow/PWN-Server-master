package com.arlania.world.content.chests;

import com.arlania.model.Animation;
import com.arlania.model.GameObject;
import com.arlania.model.Graphic;
import com.arlania.model.Item;
import com.arlania.model.PlayerRights;
import com.arlania.util.Misc;
import com.arlania.util.RandomUtility;
import com.arlania.world.entity.impl.player.Player;

public class Upgradedchest {

	public static void handleChest(final Player p, final GameObject chest) {
		if(!p.getClickDelay().elapsed(3000)) 
			return;
		if(!p.getInventory().contains(85)) {
			p.getPacketSender().sendMessage("This chest can only be opened with a Upgraded C Key.");
			return;
		}
		p.performAnimation(new Animation(7253));
		p.performGraphic(new Graphic(6));
		if (p.getRights() == PlayerRights.DONATOR) {
			if (Misc.getRandom(15) == 5) {
				p.getPacketSender().sendMessage("Upgraded C Key has been saved as a donator benefit");
			} else {
				p.getInventory().delete(85, 1);
			}
		}
		if (p.getRights() == PlayerRights.SUPER_DONATOR || p.getRights() == PlayerRights.SUPPORT) {
			if (Misc.getRandom(12) == 5) {
				p.getPacketSender().sendMessage("Upgraded C Key has been saved as a donator benefit");
			} else {
				p.getInventory().delete(85, 1);
			}
		}
		if (p.getRights() == PlayerRights.ULTRA_DONATOR || p.getRights() == PlayerRights.MODERATOR) {
			if (Misc.getRandom(9) == 5) {
				p.getPacketSender().sendMessage("Upgraded C Key has been saved as a donator benefit");
			} else {
				p.getInventory().delete(85, 1);
			}
		}
		if (p.getRights() == PlayerRights.MYSTIC_DONATOR  || p.getRights() == PlayerRights.ADMINISTRATOR) {
			if (Misc.getRandom(6) == 5) {
				p.getPacketSender().sendMessage("Upgraded C Key has been saved as a donator benefit");
			} else {
				p.getInventory().delete(85, 1);
			}
		}
		if (p.getRights() == PlayerRights.CELESTIAL_DONATOR  || p.getRights() == PlayerRights.OBSIDIAN_DONATOR|| p.getRights() == PlayerRights.LEGENDARY_DONATOR) {
			if (Misc.getRandom(6) == 5) {
				p.getPacketSender().sendMessage("Upgraded C Key has been saved as a donator benefit");
			} else {
				p.getInventory().delete(85, 1);
			}
		}
		if (p.getRights() == PlayerRights.DEVELOPER) {
			if (Misc.getRandom(3) == 2) {
				p.getPacketSender().sendMessage("Upgraded C Key has been saved as a donator benefit");
			} else {
				p.getInventory().delete(85, 1);
			}
		}
		if (p.getRights() == PlayerRights.PLAYER || p.getRights() == PlayerRights.YOUTUBER) {
			p.getInventory().delete(85, 1);
		}
		p.getPacketSender().sendMessage("You open the chest..");
	
					Item[] loot = itemRewards[Misc.getRandom(itemRewards.length - 1)];
					for(Item item : loot) {
						p.getInventory().add(item);
					}
					p.getInventory().add(10835, 1 + RandomUtility.RANDOM.nextInt(10));
				
					//CustomObjects.objectRespawnTask(p, new GameObject(173 , chest.getPosition().copy(), 10, 0), chest, 10);
				
	}

	private static final Item[][] itemRewards =  {
			
			{new Item(18967,1)},
			{new Item(18967,1)},
			{new Item(19024,1)},
			{new Item(19024,1)},
			{new Item(19025,1)},
			{new Item(19025,1)},
			{new Item(19026,1)},
			{new Item(19026,1)},
			{new Item(19027,1)},
			{new Item(19027,1)},
			{new Item(19043,1)},
			{new Item(19043,1)},
			{new Item(19044,1)},
			{new Item(19044,1)},
			{new Item(10835,1)},
			{new Item(10835,1)},
			{new Item(10835,1)},
			{new Item(10835,3)},
			{new Item(10835,3)},
			{new Item(19864,30)},
			{new Item(19864,30)},
			{new Item(19864,30)},
			{new Item(19864,200)},
			{new Item(19864,200)},
			{new Item(19864,200)},
			{new Item(15373,1)},
			{new Item(15373,1)},
			{new Item(19936,1)},
			{new Item(19106, 1)},
			{new Item(12001, 1)},
			{new Item(905, 1)},
			{new Item(3082, 1)},
			{new Item(3082, 1)},
			{new Item(2577, 1)},
			{new Item(3988, 1)},
			{new Item(2577, 1)},
			{new Item(3082, 1)},
			{new Item(3082, 1)},
			{new Item(989, 100)},
			{new Item(20016, 1)},
			{new Item(20017, 1)},
			{new Item(20018, 1)},
			{new Item(20021, 1)},
			{new Item(20022, 1)},
			{new Item(18910, 1)},
			{new Item(10720, 1)},
			{new Item(14006, 1)},
			{new Item(20016, 1)},
			{new Item(20017, 1)},
			{new Item(20018, 1)},
			{new Item(20021, 1)},
			{new Item(20022, 1)},
			{new Item(18910, 1)},
			{new Item(10720, 1)},
			{new Item(14006, 1)},
			{new Item(6041, 1)},
			{new Item(6041, 1)},
			{new Item(19935, 1)},
			{new Item(17911, 1)},
			{new Item(17908, 1)},
			{new Item(17909, 1)},
			{new Item(11732, 1)},
			{new Item(5161, 1)},
			{new Item(5157, 1)},
			{new Item(5160, 1)},
			{new Item(19004, 1)},
			{new Item(19138, 1)},
			{new Item(19139, 1)},
			{new Item(3662, 1)},
			{new Item(14453, 1)},
			{new Item(14455, 1)},
			{new Item(14457, 1)},
			{new Item(14453, 1)},
			{new Item(14455, 1)},
			{new Item(14457, 1)},
			{new Item(744, 100)},
			{new Item(18984, 1)},
			{new Item(18973, 1)},
			{new Item(18974, 1)},
			{new Item(18975, 1)},
			{new Item(18976, 1)},
		};
	
}

