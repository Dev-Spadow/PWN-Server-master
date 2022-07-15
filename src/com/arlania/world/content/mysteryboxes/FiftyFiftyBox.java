package com.arlania.world.content.mysteryboxes;

import com.arlania.util.Misc;
import com.arlania.util.RandomUtility;
import com.arlania.world.entity.impl.player.Player;

// created by Emerald

public class FiftyFiftyBox {
	
	public static final int [] Frimbrewards = {995,}; // Frimb shitty ass rewards
	public static final int [] Kahosrewards = {995,}; // kahos kinda shitty ass rewards
	public static final int [] Emeraldrewards = {10835,}; // Emerald's best rewards ^^
	
	
	public static void example (Player player) {
		int chance = RandomUtility.random(100);
		
		if (chance >= 0 && chance <= 45) {
			player.getInventory().add(Frimbrewards[Misc.getRandom(Frimbrewards.length - 1)], 1); // 45% chance of getting a Frimb reward.
		} else if (chance >=46 && chance <= 55) {
			player.getInventory().add(Kahosrewards[Misc.getRandom(Kahosrewards.length - 1)], 1); // 10% chance of getting a Kahos reward
		} else if (chance >=56 && chance <= 100) {
			player.getInventory().add(Emeraldrewards[Misc.getRandom(Kahosrewards.length - 1)], 1); // 45% Chance of getting a Emeralddaddy reward.
		}
		}
			
	public static void open (Player player) {
		if (player.getInventory().getFreeSlots() < 1) {
			player.getPacketSender().sendMessage("You need at least 1 inventory space");
			return;
		}
			// Opens the box, gives the reward, deletes the box from the inventory, and sends a message to the player.
		player.getInventory().delete(15375, 1);
		example(player);
		player.getPacketSender().sendMessage("Congratulations on your reward!");
	}
			
			
			


public static void giveReward(Player player) {
	/*
	 * 1/3 Chance for a good reward
	 */
	if (RandomUtility.RANDOM.nextInt(3) == 2) {
		
	} else {
		player.getInventory().add(Frimbrewards[Misc.getRandom(Frimbrewards.length - 1)], 1);

	}
}
	public static void givebestReward(Player player) {
		if (RandomUtility.RANDOM.nextInt(4) == 2) {
			
		} else {
			player.getInventory().add(Frimbrewards[Misc.getRandom(Frimbrewards.length - 1)], 1);
	}
	}
	
	// just do it like this its much easier sec ill add a new method for u
	/*
	 * S
	 * M
	 * D
	 */

	public void process() {
		// TODO Auto-generated method stub
		
	}

	public void reward() {
		// TODO Auto-generated method stub
		
	}
}
