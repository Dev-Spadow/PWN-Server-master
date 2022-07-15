package com.arlania.world.content.chests;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.Animation;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class SummerEventChest {

	// Item ids that will be dropped
	public static int pvmKey = 16641;

	// useless, just needed to write down object id
	public static int chest = 4123;

	// We roll for random on scale of 1 - 200
	
	// if random is 121 - 185, they get this table

	public static int rareLoots[] = { 6666,17743,17840,5185,5266,14249,8490,8491,8492,8493,8494,8495 };


	//if random is 186 to 200, they get this table
	public static int ultraLoots[] = { 20927,16543,3254,8498 };

	// not using this one
	//public static int amazingLoots[] = { 5022 };

	// if roll is 1 - 120, they get this table 
	public static int commonLoots[] = { 20920,20921,20922,20924,20925,20926,6507,298,4670,4671,4672,4673 };

	public static int getRandomItem(int[] array) {
		return array[Misc.getRandom(array.length - 1)];
	}

	public static void openChest(Player player) {
		if (!player.getClickDelay().elapsed(1000))
			return;
		if (player.getInventory().contains(5023, 3000)) {
			player.getInventory().delete(5023, 3000);

			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {

					player.getPacketSender().sendMessage("Opening Summer Event Pinata...");
					player.performAnimation(new Animation(827));
					giveReward(player);
					this.stop();

				}
			});
		} else {

			player.getPacketSender().sendMessage("@red@You require 3000 Summer tickets to open the Pinata!");
			return;
		}

	}

	// Main method for determining roll
	public static void giveReward(Player player) {
		int random = Misc.inclusiveRandom(1,10);
		if (random >= 0 && random <= 5) {
			int commonDrop = getRandomItem(commonLoots);
			player.getInventory().add(commonDrop, 1);
		} else if (random >= 6 && random <= 9) {
			int rareDrop = getRandomItem(rareLoots);
					World.sendMessage("@blu@<img=459>[Summer Pinata]:@bla@ " + player.getUsername() + " has received @blu@" + ItemDefinition.forId(rareDrop).getName() + " @bla@from the @blu@Summer Pinata<img=459>");
					player.getInventory().add(rareDrop, 1);
		} 
		else if (random >= 9) {
			int ultraDrops = getRandomItem(ultraLoots);
				player.getInventory().add(ultraDrops, 1);
				World.sendMessage("@blu@<img=459>[Summer Pinata]:@bla@ " + player.getUsername() + " has received @blu@" + ItemDefinition.forId(ultraDrops).getName() + " @bla@from the @blu@Summer Pinata<img=459>");

		}
	}
}