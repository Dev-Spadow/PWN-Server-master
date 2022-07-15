package com.arlania.world.content.mysteryboxes;
import com.arlania.util.Misc;
import com.arlania.util.RandomUtility;
import com.arlania.world.entity.impl.player.Player;
public class RelaunchBox {
        /*
         * Rewards
         */
        public static final int [] shitRewards = {15373,989,19137,19138,19139};
        public static final int [] goodRewards = {5130,18865,19131,19132,19133 };
        public static final int [] bestRewards = {18940,18941,18942,19721,19722,19723,19734,19736,19468};


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
        public static void open(Player player) {
            if (player.getInventory().getFreeSlots() < 3) {
                player.getPacketSender().sendMessage("You need at least 3 inventory spaces");
                return;
            }
            giveReward(player);
                // Opens the box, gives the reward, deletes the box from the inventory, and sends a message to the player.
            player.getInventory().delete(15003, 1);
        }
	
        /*
         * Gives the reward base on misc Random chance
         */
        public static void giveReward(Player player) {
            if (RandomUtility.RANDOM.nextInt(4) == 2) player.getInventory().add(bestRewards[Misc.getRandom(bestRewards.length - 1)], 1);
            else if (RandomUtility.RANDOM.nextInt(3) == 2) player.getInventory().add(goodRewards[Misc.getRandom(goodRewards.length - 1)], 1);
            else player.getInventory().add(shitRewards[Misc.getRandom(shitRewards.length - 1)], 1);
        }

		public static void givebestReward(Player player) {
			if (RandomUtility.RANDOM.nextInt(4) == 2) {
                player.getInventory().add(bestRewards[Misc.getRandom(bestRewards.length - 1)], 1);
				
			} else {
				player.getInventory().add(shitRewards[Misc.getRandom(shitRewards.length - 1)], 1);
		    }
		}

		public void process() {
			// TODO Auto-generated method stub
			
		}
		public void reward() {
			// TODO Auto-generated method stub


		}
	}
