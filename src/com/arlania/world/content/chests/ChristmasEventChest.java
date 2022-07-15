package com.arlania.world.content.chests;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class ChristmasEventChest {

	// Item ids that will be dropped
	public static int pvmKey = 5232;

	// useless, just needed to write down object id
	public static int chest = 47857;

	// We roll for random on scale of 1 - 200
	
	// if random is 121 - 185, they get this table
	public static int rareLoots[] = { 5258,5259,5260,5262,5261,10501 };

	//if random is 186 to 200, they get this table
	public static int ultraLoots[] = { 5264,13198,10501 };

	// not using this one
	//public static int amazingLoots[] = { 5022 };

	// if roll is 1 - 120, they get this table 
	public static int commonLoots[] = { 15420,10501 };

	public static int getRandomItem(int[] array) {
		return array[Misc.getRandom(array.length - 1)];
	}

	public static void openChest(Player player) {
		if (!player.getClickDelay().elapsed(1000))
			return;
		if (player.getInventory().contains(5232)) {
			player.getInventory().delete(5232, 1);

			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {

					player.getPacketSender().sendMessage("@red@YOU HELP FREE SANTA...");
					giveReward(player);
					this.stop();

				}
			});
		} else {

			player.getPacketSender().sendMessage("@red@Help Save Christmas! Find Frozen Keys to Unlock the Cage!");
			return;
		}

	}

	// Main method for determining roll
	public static void giveReward(Player player) {
		int random = Misc.inclusiveRandom(1, 200);
		if (random >= 1 && random <= 160) {
			int commonDrop = getRandomItem(commonLoots);
			if (commonDrop == 10501) {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(10, 100));
			} else if (commonDrop == 10501) {
				player.getInventory().add(commonDrop, 2);
			} else if (commonDrop == 10501) {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(10, 100));
			} else {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(1, 1));
			}
		} else if (random >= 161 && random <= 190) {
			int rareDrop = getRandomItem(rareLoots);
			if (rareDrop == 10501) {
				player.getInventory().add(rareDrop, Misc.inclusiveRandom(100, 250));
			} else if (rareDrop == 10501) {
				player.getInventory().add(rareDrop, 4);
			} else {
				if(ItemDefinition.forId(rareDrop).getName().toLowerCase().contains("gem"))
					player.getInventory().add(rareDrop, Misc.inclusiveRandom(3, 5));
				else
					player.getInventory().add(rareDrop, 1);
			}
		} else if (random >= 191 && random <= 200) {
			int ultraDrops = getRandomItem(ultraLoots);
			if (ultraDrops == 10501) {
				player.getInventory().add(ultraDrops, Misc.inclusiveRandom(250, 500));
			} else if (ultraDrops == 10501) {
				player.getInventory().add(ultraDrops, 4);
			} else {
				player.getInventory().add(ultraDrops, 1);
				World.sendMessage("@red@[CHRISTMAS]: " + player.getUsername() + " has received " + ItemDefinition.forId(ultraDrops).getName() + " from Santa!");
			}
		}
	}
}
