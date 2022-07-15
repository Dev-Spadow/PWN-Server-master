package com.arlania.world.content.mysteryboxes;

import com.arlania.util.Misc;
import com.arlania.util.RandomUtility;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class InfernalBox {
	
	/*
	* Array of all the available rewards
	*/

	public static final int[] badRewards = { 3984, 3985, 3983, 3986, 19018, 1050, 7671, 19335, 15332, 2572};
    public static final int[] goodRewards = { 3984, 3985, 3983, 3986};
    public static final int[] bestRewards = { 19936, 4630, 3932, 3975, 14605, 3976, 3090, 6183, 4084, 18979};
	
    /*
     * Chances for the 3 array fields
     */
	public static void boxInfo(Player player) {
		int chance = RandomUtility.exclusiveRandom(100);
		if (chance >= 0 && chance <= 70) {
			player.getInventory().add(badRewards[Misc.getRandom(badRewards.length - 1)], 1);
			player.sendMessage("You got a common reward");
		} else if(chance >=71 && chance <=95) {
			player.getInventory().add(goodRewards[Misc.getRandom(goodRewards.length - 1)], 1);
			player.sendMessage("You got a uncommon reward");
		} else if(chance >=96) {
			player.getInventory().add(bestRewards[Misc.getRandom(bestRewards.length - 1)], 1);
			player.sendMessage("You got a rare reward");
			World.sendMessage("<img=10>@blu@[INFERNAL BOX]<img=10> @red@"+player.getUsername().toString() + " @blu@Has just received a rare reward.");
		}
	}
	
	
	/*
	 * Handles the opening
	 */
	
	public static void openBox(Player player) {
		if (player.getInventory().getFreeSlots() >=3) { // checks if player has 3 or more slots available, if true, executes the method boxInfo
			player.getInventory().delete(4635, 1);
			boxInfo(player);
		} else {
			player.sendMessage("@red@You need atleast 3 free spaces in order to open this box"); // if not sends player a msg.
		}
	}

}
