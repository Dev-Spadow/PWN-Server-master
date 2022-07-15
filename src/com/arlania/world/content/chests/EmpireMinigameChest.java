package com.arlania.world.content.chests;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.Animation;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class EmpireMinigameChest {

	// Item ids that will be dropped
	public static int pvmKey = 16641;

	// useless, just needed to write down object id
	public static int chest = 4123;

	// We roll for random on scale of 1 - 200
	
	// if random is 121 - 185, they get this table

	public static int rareLoots[] = { 19935,19936,14249,19821,19958,13999,13999,4652,4652,12426,4670,4671,4672,4673,13999,6640,19890,5266,4780,4652,3918,3277,6507,455,4082 };


	//if random is 186 to 200, they get this table
	public static int ultraLoots[] = { 16455,3966,10168,3967,3968,3969,3970,4742 };

	// not using this one
	//public static int amazingLoots[] = { 5022 };

	// if roll is 1 - 120, they get this table 
	public static int commonLoots[] = { 10835 };

	public static int getRandomItem(int[] array) {
		return array[Misc.getRandom(array.length - 1)];
	}

	public static void openChest(Player player) {
		if (!player.getClickDelay().elapsed(1000))
			return;
		if (player.getInventory().contains(16641)) {
			player.getInventory().delete(16641, 1);

			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {

					player.getPacketSender().sendMessage("Opening Minigame Chest...");
					player.performAnimation(new Animation(827));
					giveReward(player);
					this.stop();

				}
			});
		} else {

			player.getPacketSender().sendMessage("@red@You require a Minigame Chest Key to open this chest!");
			return;
		}

	}

	// Main method for determining roll
	public static void giveReward(Player player) {
		int random = Misc.inclusiveRandom(1,50);
		if (random >= 1 && random <= 37) {
			int commonDrop = getRandomItem(commonLoots);
			if (commonDrop == 10835) {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(1000, 5000));
			} else if (commonDrop == 10835) {
				player.getInventory().add(commonDrop, 2);
			} else if (commonDrop == 10835) {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(1000, 5000));
			} else {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(1, 3));
			}
		} else if (random >= 37 && random <= 49) {
			int rareDrop = getRandomItem(rareLoots);
			if (rareDrop == 10835) {
				player.getInventory().add(rareDrop, Misc.inclusiveRandom(5000, 10000));
			} else if (rareDrop == 10835) {
				player.getInventory().add(rareDrop, 4);
			} else {
				if(ItemDefinition.forId(rareDrop).getName().toLowerCase().contains("gem"))
					player.getInventory().add(rareDrop, Misc.inclusiveRandom(3, 5));
				else
					World.sendMessage("@blu@<img=459>[Minigame Chest]:@bla@ " + player.getUsername() + " has received @blu@" + ItemDefinition.forId(rareDrop).getName() + " @bla@from the @blu@Minigame chest<img=459>");
					player.getInventory().add(rareDrop, 1);
			}
		} else if (random >= 49) {
			int ultraDrops = getRandomItem(ultraLoots);
			if (ultraDrops == 10835) {
				player.getInventory().add(ultraDrops, Misc.inclusiveRandom(10000, 20000));
			} else if (ultraDrops == 10835) {
				player.getInventory().add(ultraDrops, 4);
			} else {
				player.getInventory().add(ultraDrops, 1);
				World.sendMessage("@blu@<img=459>[Minigame Chest]:@bla@ " + player.getUsername() + " has received @blu@" + ItemDefinition.forId(ultraDrops).getName() + " @bla@from the @blu@Minigame chest<img=459>");
			}
		}
	}
}