package com.arlania.world.content.mysteryboxes;

import com.arlania.util.Misc;
import com.arlania.util.RandomUtility;
import com.arlania.world.entity.impl.player.Player;

public class DonationBox {
	
	/*
	 * Rewards
	 */
	public static final int [] shitRewards = {10835};
	public static final int [] goodRewards = {19935};
	
	/*
	 * Handles the opening of the donation box
	 */
	public static void open (Player player) {
		if (player.getInventory().getFreeSlots() < 3) {
			player.getPacketSender().sendMessage("You need at least 3 inventory spaces");
			return;
		}
			//fk
		player.getInventory().delete(6183, 1);
		giveReward(player);
		player.getPacketSender().sendMessage("Congratulations on your reward!");
	}
	
	/*
	 * Gives the reward base on misc Random chance
	 */
	public static void giveReward(Player player) {
		/*
		 * 1/3 Chance for a good reward
		 */
		if (RandomUtility.RANDOM.nextInt(750) == 575) {
			player.getInventory().add(goodRewards[Misc.getRandom(goodRewards.length - 1)], 1);
		} else {
			player.getInventory().add(shitRewards[Misc.getRandom(shitRewards.length - 1)], 1);

		}
		/*
		 * Adds 5m + a random amount up to 100m every box
		 * Max cash reward = 105m
		 */
	}

}
