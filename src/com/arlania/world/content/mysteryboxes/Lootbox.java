
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

public class Lootbox {
	
	

	public static final int[] badRewards = {2572,19137,19138,19139,6041,5130,
			18865,19132,19131,3912,19133,3666,18940,5131,18941,18942,2749,2750,2751,2752,2753,2754,13261,19721,19132,19131,19133,6199,18940,18941,
			18942,2749,5082,6640,15653,15649,15650,15651,15654,14559,18957,15656,5083,5084,3985,2750,2751,5134,85,5131,5130,2752,2753,2754,13261,19721,19722,19723,18892,15418,19468};

	
    /*
     * Chances for the 3 array fields
     */
	public static void boxInfo(Player player) {
		int chance = RandomUtility.exclusiveRandom(100);
		if (chance >= 15 && chance <= 70) {
			player.getInventory().add(badRewards[Misc.getRandom(badRewards.length - 1)], 1);
			player.sendMessage("You got a rare reward");
		}
	}
	
	
	/*
	 * Handles the opening
	 */
	
	public static void openBox(Player player) {
		if (player.getInventory().getFreeSlots() >=3) { // checks if player has 3 or more slots available, if true, executes the method boxInfo
			if (player.getInventory().contains(3576))
				player.getInventory().delete(3576,1);
			boxInfo(player);
		} else {
			player.sendMessage("@red@You need atleast 3 free spaces in order to open this box"); // if not sends player a msg.
		}
	}

}
