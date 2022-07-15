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

public class Lootbox2 {
	
	

	 public static final int[] goodRewards = { 19935,12426,3988,14808,1666,19004,3069,3071 ,17918,5131,4771,4772,4770,19935,1666,19004,3069,3071 ,17918,5131,4771,4772,4770, 4799 ,4800,4801,5079,3973,3951,15012
				,5131,931,15373,5133,3912,926,3666,1543,2545,2546,5184,2547,4770,4771,4772,18347,3988,6193,2756,6194,4082,6195,6196,6197,3904,6198,12162};

	
    /*
     * Chances for the 3 array fields
     */
	public static void boxInfo(Player player) {
		int chance = RandomUtility.exclusiveRandom(100);
		if (chance >= 25 && chance <= 70) {
			player.getInventory().add(goodRewards[Misc.getRandom(goodRewards.length - 1)], 1);
			player.sendMessage("You got a rare reward");
		}
	}
	
	
	/*
	 * Handles the opening
	 */
	
	public static void openBox(Player player) {
		if (player.getInventory().getFreeSlots() >=3) { // checks if player has 3 or more slots available, if true, executes the method boxInfo
			player.getInventory().delete(3578, 1);
			boxInfo(player);
		} else {
			player.sendMessage("@red@You need atleast 3 free spaces in order to open this box"); // if not sends player a msg.
		}
	}

}
