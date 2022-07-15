package com.arlania.world.content.chests;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.Position;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class DungeonMinigameChest {

	// Item ids that will be dropped
	public static int pvmKey = 14471;

	// useless, just needed to write down object id
	public static int chest = 2183;

	// We roll for random on scale of 1 - 200
	
	// if random is 121 - 185, they get this table
	public static int rareLoots[] = { 19475,4672,4670,4671,4673,6199,19886,3317,19935,5184,11133 };

	//if random is 186 to 200, they get this table
	public static int ultraLoots[] = { 3076,3075,3078,3242,3244,3250,17931 };

	// not using this one
	//public static int amazingLoots[] = { 5022 };

	// if roll is 1 - 120, they get this table 
	public static int commonLoots[] = { 19475,4672,4670,4671,4673,6199,19886,3317,19935,5184 };

	public static int getRandomItem(int[] array) {
		return array[Misc.getRandom(array.length - 1)];
	}

	public static void openChest(Player player) {
		if (!player.getClickDelay().elapsed(1000))
			return;
		if (player.getInventory().contains(14471)) {
			player.getInventory().delete(14471, 1);

			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {

					player.getPacketSender().sendMessage("Opening Dungeon Chest...");
					giveReward(player);
					this.stop();

				}
			});
		} else {

			player.getPacketSender().sendMessage("@red@You require a Dungeon Key(Room1) to open this chest!");
			return;
		}

	}

	// Main method for determining roll
	public static void giveReward(Player player) {
		int random = Misc.inclusiveRandom(1, 200);
		if (random >= 1 && random <= 120) {
			int commonDrop = getRandomItem(commonLoots);
			if (commonDrop == 10835) {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(10, 20));
			} else if (commonDrop == 10835) {
				player.getInventory().add(commonDrop, 2);
			} else if (commonDrop == 10835) {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(10, 50));
			} else {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(1, 1));
				player.moveTo(new Position(3199, 3212, 0));
			}
		} else if (random >= 121 && random <= 189) {
			int rareDrop = getRandomItem(rareLoots);
			if (rareDrop == 10835) {
				player.getInventory().add(rareDrop, Misc.inclusiveRandom(5000, 7500));
			} else if (rareDrop == 10835) {
				player.getInventory().add(rareDrop, 1);
			} else {
				if(ItemDefinition.forId(rareDrop).getName().toLowerCase().contains("gem"))
					player.getInventory().add(rareDrop, Misc.inclusiveRandom(3, 5));
				else
					player.getInventory().add(rareDrop, 1);
				player.moveTo(new Position(3199, 3212, 0));
			}
		} else if (random >= 190 && random <= 200) {
			int ultraDrops = getRandomItem(ultraLoots);
			if (ultraDrops == 10835) {
				player.getInventory().add(ultraDrops, Misc.inclusiveRandom(7500, 10000));
			} else if (ultraDrops == 10835) {
				player.getInventory().add(ultraDrops, 1);
			} else {
				player.getInventory().add(ultraDrops, 1);
				World.sendMessage("@blu@[DUNGEON CHEST]: " + player.getUsername() + " has received " + ItemDefinition.forId(ultraDrops).getName() + " from the Dungeon chest!");
				player.moveTo(new Position(3199, 3212, 0));
			}
		}
	}
}
