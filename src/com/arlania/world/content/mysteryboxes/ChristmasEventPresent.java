package com.arlania.world.content.mysteryboxes;

import com.arlania.util.Misc;
import com.arlania.util.RandomUtility;
import com.arlania.world.entity.impl.player.Player;

public class ChristmasEventPresent {
	
	/*
	 * Rewards
	 */
	public static final int [] shitRewards = {5161,5160,5157,2572,15373,989,18782,11133,19131,19132,19133,922,18940,18941,18942};
	public static final int [] goodRewards = { 2749,2750,2751,2752,2753,2754,13261,5162,1499,3973,4799,4800,4801,15012,3951,18933,5186,5187,145591,3960,3931,3958,3959,3316};
	public static final int [] bestRewards = {4770,4771,4772,5131,6199,6193,6194,6195,6196,6197,6198,3994,3995,3996,923,13198,5258,5259,5260,5261,5262,5264 };
	
	
	public static void example(Player player) {
		int chance = RandomUtility.random(40);
		
		if (chance >= 0 && chance <= 25) {
			player.getInventory().add(shitRewards[Misc.getRandom(shitRewards.length - 1)], 1);
		} else if (chance >= 26 && chance <= 35) {
			player.getInventory().add(goodRewards[Misc.getRandom(goodRewards.length - 1)], 1); //
		} else if (chance >= 34 && chance <= 40) {
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
		player.getInventory().delete(11049, 1);
		player.getInventory().delete(4818, 1);
		player.getInventory().delete(11055, 1);
		player.getInventory().delete(11057, 1);
		player.getInventory().delete(11056, 1);
		example(player);
		player.getPacketSender().sendMessage("@red@Congratulations, You got a reward from the Artefact");
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
