package com.arlania.world.content.chests;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.Animation;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.content.Achievements;
import com.arlania.world.content.Achievements.AchievementData;
import com.arlania.world.entity.impl.player.Player;

public class SephirothChest {

	// Item ids that will be dropped
	public static int pvmKey = 13998;

	// useless, just needed to write down object id
	public static int chest = 2403;

	// We roll for random on scale of 1 - 200
	
	// if random is 121 - 185, they get this table
	public static int rareLoots[] = { 13266,13267,13268,13270,19158,19936,19935,3912,15374};

	//if random is 186 to 200, they get this table
	public static int ultraLoots[] = { 13265,16455,13999 };

	// not using this one
	//public static int amazingLoots[] = { 5022 };

	// if roll is 1 - 120, they get this table 
	public static int commonLoots[] = { 10835,3912 };

	public static int getRandomItem(int[] array) {
		return array[Misc.getRandom(array.length - 1)];
	}

	public static void openChest(Player player) {
		if (player.getInventory().contains(13998))	 {
			player.getInventory().delete(13998, 1);
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {

					player.getPacketSender().sendMessage("Opening Sephiorth Chest...");
					player.performAnimation(new Animation(827));
					giveReward(player);
					this.stop();

				}
			});
		} else {

			player.getPacketSender().sendMessage("@red@You require a Sephiroth Key to open this chest!");
			return;
		}
	}

	// Main method for determining roll
	public static void giveReward(Player player) {
		int random = Misc.inclusiveRandom(1, 200);
		if (random >= 1 && random <= 181) {
			int commonDrop = getRandomItem(commonLoots);
			if (commonDrop == 10835) {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(100, 200));
			} else if (commonDrop == 10835) {
				player.getInventory().add(commonDrop, 2);
			} else if (commonDrop == 10835) {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(100, 5000));
			} else {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(1, 3));
			}
		} else if (random >= 182 && random <= 195) {
			int rareDrop = getRandomItem(rareLoots);
			if (rareDrop == 10835) {
				player.getInventory().add(rareDrop, Misc.inclusiveRandom(1000, 2500));
			} else if (rareDrop == 10835) {
				player.getInventory().add(rareDrop, 4);
			} else {
				if(ItemDefinition.forId(rareDrop).getName().toLowerCase().contains("gem"))
					player.getInventory().add(rareDrop, Misc.inclusiveRandom(3, 5));
				else
					player.getInventory().add(rareDrop, 1);
			}
		} else if (random >= 198 && random <= 200) {
			int ultraDrops = getRandomItem(ultraLoots);
			if (ultraDrops == 10835) {
				player.getInventory().add(ultraDrops, Misc.inclusiveRandom(1500, 5000));
			} else if (ultraDrops == 10835) {
				player.getInventory().add(ultraDrops, 4);
			} else {
				player.getInventory().add(ultraDrops, 1);
				World.sendMessage("@blu@[SEPHIROTH CHEST]: " + player.getUsername() + " has received " + ItemDefinition.forId(ultraDrops).getName() + " from the Sephiroth chest!");
			}
		}
	}
}
