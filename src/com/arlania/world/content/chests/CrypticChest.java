package com.arlania.world.content.chests;

import com.arlania.util.Misc;
import com.arlania.util.RandomUtility;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class CrypticChest {
	
	/*
	 * Rewards
	 */
	public static void openBox (Player player) {
		player.getInventory().delete(13195, 1);
		
		if (RandomUtility.getRandom(10) == 5) {
			/*
			 * Landing on 5 and recieve reward
			 */
			player.getInventory().add(10835, 20000);
			player.getPacketSender().sendMessage("Congratulations you received 20k tax bagz");
			World.sendMessage("@blu@[Thousand Bagz]: " + player.getUsername() + " has received 20000 Tax Bagz From Thousands Chest");
		} else {
			/*
			 * Not landing on 5
			 */
			player.getInventory().add(10835, 100 + RandomUtility.getRandom(1000));
			player.getPacketSender().sendMessage("Sorry you only got 1000 Bagz");
		}
	}}
