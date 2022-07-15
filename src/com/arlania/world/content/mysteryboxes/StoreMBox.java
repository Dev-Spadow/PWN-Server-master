package com.arlania.world.content.mysteryboxes;

import com.arlania.util.Misc;
import com.arlania.util.RandomUtility;
import com.arlania.world.entity.impl.player.Player;

public class StoreMBox {
	
	/*
	 * Rewards
	 */
	public static final int [] shitRewards = {4772,4771,4770,1543,1543,18940,18941,18942,922,2572,19468,5186,5187,3316,3931,3958,3959,3960,18347};
	public static final int [] goodRewards = {19935,6193,6194,6195,6196,6197,6198,3994,3995,3996,3974,5131,18751,18748,18950 };
	public static final int [] bestRewards = {19936,923,3908,3910,3909,3907,19720};
	
	
	public static void example(Player player) {
		int chance = RandomUtility.random(80);
		
		if (chance >= 0 && chance <= 55) {
			player.getInventory().add(shitRewards[Misc.getRandom(shitRewards.length - 1)], 1);
		} else if (chance >= 56 && chance <= 77) {
			player.getInventory().add(goodRewards[Misc.getRandom(goodRewards.length - 1)], 1); //
		} else if (chance >= 78 && chance <= 80) {
			player.getInventory().add(bestRewards[Misc.getRandom(bestRewards.length - 1)], 1);
		}
		
		
	}
	
	/*
	 * Handles opening obv
	 */
	public static void open (Player player) {
		if (player.getInventory().getFreeSlots() < 3) {
			player.getPacketSender().sendMessage("You need at least 3 inventory spaces");
			return;
		}
			// Opens the box, gives the reward, deletes the box from the inventory, and sends a message to the player.
		player.getInventory().delete(3988, 1);
		example(player);
		player.getPacketSender().sendMessage("Congratulations on your reward!");
	}
	
	/*
	 * Gives the reward base on misc Random chance
	 */
	public static void giveReward(Player player) {
		/*
		 * 1/3 Chance for a good reward
		 */
		if (RandomUtility.RANDOM.nextInt(3) == 2) {
			
		} else {
			player.getInventory().add(shitRewards[Misc.getRandom(shitRewards.length - 1)], 1);

		}
	}
		public static void givebestReward(Player player) {
			if (RandomUtility.RANDOM.nextInt(4) == 2) {
				
			} else {
				player.getInventory().add(shitRewards[Misc.getRandom(shitRewards.length - 1)], 1);
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
