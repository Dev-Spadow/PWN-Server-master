package com.arlania.world.content.mysteryboxes;

import com.arlania.util.Misc;
import com.arlania.util.RandomUtility;
import com.arlania.world.entity.impl.player.Player;

public class ExoticChest {
	
	/*
	 * Rewards
	 */
	public static final int [] shitRewards = {12428, 19067, 20250, 19055, 3072, 18901, 18916, 665, 11605, 3074, 16555, 18922, };
	public static final int [] goodRewards = {3666, 18979, 18980, 18981, 18982, 5130, 18998, 18918, 18917, 18916, };
	public static final int [] bestRewards = {18954, 18949, 18953, 18962, 18955, 19046, 18956, 19886, 19886, 19886, };
	
	
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
		player.getInventory().delete(13197, 1);
		example(player);
		player.getPacketSender().sendMessage("@red@Congratulations, You open the chest and get a reward!");
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
