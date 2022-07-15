package com.arlania.world.content.mysteryboxes;

import com.arlania.util.Misc;
import com.arlania.util.RandomUtility;
import com.arlania.world.entity.impl.player.Player;

public class SupremeMysteryBox {
	
	/*
	 * Rewards
	 */
	public static final int [] shitRewards = {19935,5082,5083,5084,15656,15045,926,931,5211,930,5210};
	public static final int [] goodRewards = {19886,19936,9492,9493,9494,9495,19159,19160,19161,19163,19164,19165,19166,19691,19692,19693,19694,19695,19696,19618 };
	public static final int [] bestRewards = {19727,19728,19729,19730,19731,16455,19732,19620};
	
	
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
		player.getInventory().delete(15374, 1);
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
