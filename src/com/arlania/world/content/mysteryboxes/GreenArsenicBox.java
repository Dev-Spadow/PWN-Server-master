package com.arlania.world.content.mysteryboxes;

import com.arlania.model.Item;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.util.Misc;
import com.arlania.util.RandomUtility;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class GreenArsenicBox {
	
	/*
	* Array of all the available rewards
	*/

	public static final int[] badRewards = {14030,14031,14032,919,16563};
    public static final int[] goodRewards = {14028,14027,14029};

    /*
     * Chances for the 3 array fields
     */
	public static void boxInfo(Player player) {

		int chance = RandomUtility.exclusiveRandom(12);
		if (chance >= 0 && chance <= 10) {
			player.getInventory().add(badRewards[Misc.getRandom(badRewards.length - 1)], 1);
            int itemId = badRewards[Misc.getRandom(badRewards.length - 1)];
			//World.sendMessage("<col=f2a111><shad=f4e019>[ARSENIC BOX] </shad>@bla@: "+player.getUsername().toString() + 
           // " has just received a @mag@<shad=9f199d>" + ItemDefinition.forId(itemId).getName() + 
				//	" </shad>@bla@from the @red@Green arsenic box");
		} else if(chance >=11) {
            int itemId = goodRewards[Misc.getRandom(goodRewards.length - 1)];
			player.getInventory().add(goodRewards[Misc.getRandom(goodRewards.length - 1)], 1);
			//World.sendMessage("<col=f2a111><shad=f4e019>[ARSENIC BOX] </shad>@bla@: "+player.getUsername().toString() + 
			//" has just received a @gre@<shad=136d08>" + ItemDefinition.forId(itemId).getName() + 
			//" </shad>@bla@from the @red@Green arsenic box");
		}
	}
	
	
	/*
	 * Handles the opening
	 */
	
	public static void openBox(Player player) {
		if (player.getInventory().getFreeSlots() >=1) { // checks if player has 3 or more slots available, if true, executes the method boxInfo
			player.getInventory().delete(14033, 1);
			boxInfo(player);
		} else {
			player.sendMessage("@red@You need atleast 1 free spaces in order to open this box"); // if not sends player a msg.
		}
	}

}