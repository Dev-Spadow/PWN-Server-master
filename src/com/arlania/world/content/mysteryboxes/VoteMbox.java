package com.arlania.world.content.mysteryboxes;

import com.arlania.util.Misc;
import com.arlania.util.RandomUtility;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class VoteMbox {
	
	/*
	* Array of all the available rewards
	*/

	public static final int[] badRewards = {10835,989,15373,18950,1543,2572,18392};
    public static final int[] goodRewards = {19935,18380,18381,18382,18383,18384,18385,3937,3938,3939,3944,3945,3946,3947,3948};
    public static final int[] bestRewards = {20054,5184,15332,6507,5163,19886};
	
    /*
     * Chances for the 3 array fields
     */
	public static void boxInfo(Player player) {
		int chance = RandomUtility.exclusiveRandom(100);
		if (chance >= 0 && chance <= 70) {
			player.getInventory().add(badRewards[Misc.getRandom(badRewards.length - 1)], 1);
			player.sendMessage("You were unlucky and got an tax bag :(");
		} else if(chance >=71 && chance <=95) {
			player.getInventory().add(goodRewards[Misc.getRandom(goodRewards.length - 1)], 1);
			player.sendMessage("You got a uncommon reward");
		} else if(chance >=96) {
			player.getInventory().add(bestRewards[Misc.getRandom(bestRewards.length - 1)], 1);
			player.sendMessage("You got a rare reward");
			World.sendMessage("<img=10>@blu@[VOTE BOX]<img=10> @red@"+player.getUsername().toString() + " @blu@Has just received a rare reward.");
		}
	}
	
	
	/*
	 * Handles the opening
	 */
	
	public static void openBox(Player player) {
		if (player.getInventory().getFreeSlots() >=3) { // checks if player has 3 or more slots available, if true, executes the method boxInfo
			player.getInventory().delete(3824, 1);
			boxInfo(player);
		} else {
			player.sendMessage("@red@You need atleast 3 free spaces in order to open this box"); // if not sends player a msg.
		}
	}

}
