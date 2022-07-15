package com.arlania.world.content.mysteryboxes;

import com.arlania.model.Item;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.util.Misc;
import com.arlania.util.RandomUtility;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class BondCasket {
	
	/*
	* Array of all the available rewards
	*/

	public static final int[] badRewards = {1464,19935,19936};
    public static final int[] goodRewards = {16455,19938};
    public static final int[] bestRewards = {5020};

    /*
     * Chances for the 3 array fields
     */
	public static void boxInfo(Player player) {

		int chance = RandomUtility.exclusiveRandom(12);
		int reward = -1;
		if (chance >= 0 && chance <= 7) {
			reward = badRewards[RandomUtility.exclusiveRandom(0, badRewards.length)];
			player.getInventory().add(badRewards[Misc.getRandom(badRewards.length - 1)], 1);
           /* World.sendMessage("<shad=f4e019>[Bond Casket]</shad>@bla@: "+player.getUsername().toString() + 
            " has just received a @mag@<shad=9f199d>" + ItemDefinition.forId(reward).getName() + 
		" </shad>@bla@from the @red@Bond Casket");*/
		} else if(chance >=8 && chance <=10) {
			reward = goodRewards[RandomUtility.exclusiveRandom(0, goodRewards.length)];
				player.getInventory().add(goodRewards[Misc.getRandom(goodRewards.length - 1)], 1);
			/*World.sendMessage("<shad=f4e019>[Bond Casket]</shad>@bla@: "+player.getUsername().toString() + 
			" has just received a @mag@<shad=9f199d>" + ItemDefinition.forId(reward).getName() + 
			" </shad>@bla@from the @red@Bond Casket");*/
		} else if(chance >=11) {
			reward = bestRewards[RandomUtility.exclusiveRandom(0, bestRewards.length)];
			player.getInventory().add(bestRewards[Misc.getRandom(bestRewards.length - 1)], 1);
			/*World.sendMessage("<shad=f4e019>[Bond Casket]</shad>@bla@: "+player.getUsername().toString() + 
			" has just received a <col=FFFF64><shad=ebf217>" + ItemDefinition.forId(reward).getName() + 
			" </shad>@bla@from the @red@Bond Casket");*/
		}
	}
	
	
	/*
	 * Handles the opening
	 */
	
	public static void openBox(Player player) {
		if (player.getInventory().getFreeSlots() >=1) { // checks if player has 3 or more slots available, if true, executes the method boxInfo
			player.getInventory().delete(10205, 1);
			boxInfo(player);
		} else {
			player.sendMessage("@red@You need atleast 1 free spaces in order to open this box"); // if not sends player a msg.
		}
	}

}