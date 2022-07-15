package com.arlania.world.content.mysteryboxes;

import com.arlania.model.definitions.ItemDefinition;
import com.arlania.util.RandomUtility;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class ChristmasPresent {


	private static final String Name = null;

	public static void openBox (Player player) {
		player.getInventory().delete(6542, 1);
		
		if (RandomUtility.getRandom(10) == 5) {
			/*
			 * Landing on 5 and recieve reward
			 */
			player.getInventory().add(1050, 1);
			
			World.sendMessage("@red@[Christmas Present]: " + player.getUsername() + " has received a Santa Hat from Christmas Present!");
			
		} else {
			/*
			 * Not landing on 5
			 */
			player.getInventory().add(10835, 100 + RandomUtility.getRandom(500));
			player.getPacketSender().sendMessage("Sorry, you didn't get the santa hat. Try again");
		}
	}



}
