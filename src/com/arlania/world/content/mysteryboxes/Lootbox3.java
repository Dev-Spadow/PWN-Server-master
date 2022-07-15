
package com.arlania.world.content.mysteryboxes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.util.Misc;
import com.arlania.util.RandomUtility;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

/**
 * @author Emerald
 */

public class Lootbox3 {
	
	

	public static final int[] bestRewards = {19936,13207,12926,3971,4082,14808,5184,16579,3317,9505,9506,9507,
			6507,3666,1038,1040,1042,1044,1046,1048,3666,14249,4082,6460,4769,19890,8664,19727,19821,13995,19140,19886,6320,4058,4057,4056,6640,19935,15032,13999,6507,19890 };

	
    /*
     * Chances for the 3 array fields
     */
	public static void boxInfo(Player player) {
		int chance = RandomUtility.exclusiveRandom(100);
		if(chance >=70) {
			player.getInventory().add(bestRewards[Misc.getRandom(bestRewards.length - 1)], 1);
			player.sendMessage("You got a rare reward");
			World.sendMessage("<img=10>@blu@[Loot box Tier 3]<img=10> @red@"+player.getUsername().toString() + " @blu@Has just received a Loot Box 3 reward.");
		
		}
	}
	
	
	/*
	 * Handles the opening
	 */
	
	public static void openBox(Player player) {
		if (player.getInventory().getFreeSlots() >=3) { // checks if player has 3 or more slots available, if true, executes the method boxInfo
			if (player.getInventory().contains(2795))
				player.getInventory().delete(2795,1);
			boxInfo(player);
		} else {
			player.sendMessage("@red@You need atleast 3 free spaces in order to open this box"); // if not sends player a msg.
		}
	}

}