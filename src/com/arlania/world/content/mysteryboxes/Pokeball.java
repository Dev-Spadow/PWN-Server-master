package com.arlania.world.content.mysteryboxes;

import com.arlania.model.Item;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.util.Misc;
import com.arlania.util.RandomUtility;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class Pokeball {

    
    // Array of all the available rewards
    

    public static final int[] badRewards = {5157,5160,5162};
    public static final int[] goodRewards = {1647};
    public static final int[] bestRewards = {16509,18411,4742,4743,4744,4786,4787};


    // * Chances for the 3 array fields

    public static void boxInfo(Player player) {
        int chance = RandomUtility.exclusiveRandom(100);
        if (chance >= 0 && chance <= 56) {
            int itemId = badRewards[Misc.getRandom(badRewards.length - 1)];
            player.getInventory().add(itemId, 1);
            World.sendMessage("<shad=bf0000>[Pokeball]</shad>@bla@:  "+player.getUsername().toString() + 
            " has just received a @mag@<shad=9f199d>" + ItemDefinition.forId(itemId).getName() + 
            " </shad>@bla@from the @red@Pokeball!");
        } else if(chance >=57 && chance <=98) {
            int itemId = goodRewards[Misc.getRandom(goodRewards.length - 1)];
            player.getInventory().add(itemId, 1);
            World.sendMessage("<shad=bf0000>[Pokeball]</shad>@bla@:  "+player.getUsername().toString() + 
            " has just received a @mag@<shad=9f199d>" + ItemDefinition.forId(itemId).getName() + 
            " </shad>@bla@from the @red@Pokeball!");
        } else if(chance >=99) {
            int itemId = bestRewards[Misc.getRandom(bestRewards.length - 1)];
            player.getInventory().add(itemId, 1);
            World.sendMessage("<shad=bf0000>[Pokeball]</shad>@bla@:  "+player.getUsername().toString() + 
            " has just received a <col=FFFF64><shad=ebf217>" + ItemDefinition.forId(itemId).getName() + 
            " </shad>@bla@from the @red@Pokeball!");
        }
    }
	
	/*
	 * Handles the opening
	 */
	
	public static void openBox(Player player) {
		if (player.getInventory().getFreeSlots() >=1) { // checks if player has 3 or more slots available, if true, executes the method boxInfo
			player.getInventory().delete(16566, 1);
			boxInfo(player);
		} else {
			player.sendMessage("@red@You need atleast 1 free space in order to open this Pokeball!"); // if not sends player a msg.
		}
	}

}
