package com.arlania.world.content.mysteryboxes;

import com.arlania.model.Item;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.util.Misc;
import com.arlania.util.RandomUtility;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class OPOwnersBox {
	
	/*
	* Array of all the available rewards
	*/

	public static final int[] badRewards = {13999,5266,19936,16455};
    public static final int[] goodRewards = {19938,4742,8671,4652,19938,13691,7036};
    public static final int[] bestRewards = {773,774,15566,8654,16524,8662,3307,3309,9116,9117};

    /*
     * Chances for the 3 array fields
     */
	public static void boxInfo(Player player) {

		int chance = RandomUtility.exclusiveRandom(12);
		if (chance >= 0 && chance <= 6) {
            int itemId = badRewards[Misc.getRandom(badRewards.length - 1)];
            player.getInventory().add(itemId, 1);
            World.sendMessage("<shad=bf0000>[Grand Box]</shad>@bla@: "+player.getUsername().toString() + 
            " has just received a @mag@<shad=9f199d>" + ItemDefinition.forId(itemId).getName() + 
            " </shad>@bla@from the @red@Grand Mystery Box!");
		} else if(chance >=7 && chance <=10) {
            int itemId = goodRewards[Misc.getRandom(goodRewards.length - 1)];
            player.getInventory().add(itemId, 1);
            World.sendMessage("<shad=bf0000>[Grand Box]</shad>@bla@: "+player.getUsername().toString() + 
            " has just received a @mag@<shad=9f199d>" + ItemDefinition.forId(itemId).getName() + 
            " </shad>@bla@from the @red@Grand Mystery Box!");
		} else if(chance >=11) {
            int itemId = bestRewards[Misc.getRandom(bestRewards.length - 1)];
            player.getInventory().add(itemId, 1);
            World.sendMessage("<shad=bf0000>[Grand Box]</shad>@bla@: "+player.getUsername().toString() + 
            " has just received a <col=FFFF64><shad=ebf217>" + ItemDefinition.forId(itemId).getName() + 
            " </shad>@bla@from the @red@Grand Mystery Box!");
		} 
		//else if(chance >=5) {
			//player.getInventory().contains(11078, 1); 
	        //    int itemId = bestRewards[Misc.getRandom(bestRewards.length - 1)];
	          //  player.getInventory().add(itemId, 1);
	           // World.sendMessage("<shad=bf0000>[Grand Box]</shad>@bla@: "+player.getUsername().toString() + 
	           // " has just received a <col=FFFF64><shad=ebf217>" + ItemDefinition.forId(itemId).getName() + 
	            //" </shad>@bla@from the @red@Grand Mystery Box!");
			//}
	}

	
	/*
	 * Handles the opening
	 */
	
	public static void openBox(Player player) {
		if (player.getInventory().getFreeSlots() >=1) { // checks if player has 3 or more slots available, if true, executes the method boxInfo
			player.getInventory().delete(16456, 1);
			//player.getInventory().delete(11078, 1);
			boxInfo(player);
		} else {
			player.sendMessage("@red@You need atleast 1 free spaces in order to open this box"); // if not sends player a msg.
		}
	}

}
