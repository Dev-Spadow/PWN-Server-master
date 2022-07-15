package com.arlania.world.content.chests;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.Animation;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class BloodSlayerChest {

	// Item ids that will be dropped
	public static int pvmKey = 5205;

	// useless, just needed to write down object id
	public static int chest = 2995;

	// We roll for random on scale of 1 - 200
	
	// if random is 121 - 185, they get this table
	public static int rareLoots[] = { 10205,19935,16455,16488,15374,19106,2545,2546,2547,2548,5184,14546,6509,6505,6758,6759 };

	//if random is 186 to 200, they get this table
	public static int ultraLoots[] = { 14033,10168,13997,3949,3950,3952,5020};

	// not using this one
	//public static int amazingLoots[] = { 5022 };

	// if roll is 1 - 120, they get this table 
	public static int commonLoots[] = { 17750 };

	public static int getRandomItem(int[] array) {
		return array[Misc.getRandom(array.length - 1)];
	}

	public static void openChest(Player player) {
		if (!player.getClickDelay().elapsed(1000))
			return;
		if (player.getInventory().contains(5205)) {
			player.getInventory().delete(5205, 1);

			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {

					player.getPacketSender().sendMessage("Opening Bloodslayer Chest...");
					player.performAnimation(new Animation(827));
					giveReward(player);
					this.stop();

				}
			});
		} else {

			player.getPacketSender().sendMessage("@red@You require a Bloodslayer Key to open this chest!");
			return;
		}

	}

	// Main method for determining roll
	public static void giveReward(Player player) {
		int random = Misc.inclusiveRandom(1, 50);
		if (random >= 1 && random <= 42) {
			int commonDrop = getRandomItem(commonLoots);
			if (commonDrop == 17750) {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(300, 500));
			} else if (commonDrop == 17750) {
				player.getInventory().add(commonDrop, 2);
			} else if (commonDrop == 17750) {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(300, 500));
			} else {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(1, 3));
			}
		} else if (random >= 39 && random <= 48) {
			int rareDrop = getRandomItem(rareLoots);
			if (rareDrop == 17750) {
				player.getInventory().add(rareDrop, Misc.inclusiveRandom(1000, 2500));
			} else if (rareDrop == 17750) {
				player.getInventory().add(rareDrop, 4);
			} else {
				if(ItemDefinition.forId(rareDrop).getName().toLowerCase().contains("gem"))
					player.getInventory().add(rareDrop, Misc.inclusiveRandom(3, 5));
				else
					World.sendMessage("@red@<img=482>[Bloodslayer Chest]: " + player.getUsername() + " has received " + ItemDefinition.forId(rareDrop).getName() + " from the @red@Bloodslayer chest<img=482>");
					player.getInventory().add(rareDrop, 1);
			}
		} else if (random >= 49) {
			int ultraDrops = getRandomItem(ultraLoots);
			if (ultraDrops == 17750) {
				player.getInventory().add(ultraDrops, Misc.inclusiveRandom(2500, 3500));
			} else if (ultraDrops == 17750) {
				player.getInventory().add(ultraDrops, 4);
			} else {
				player.getInventory().add(ultraDrops, 1);
				World.sendMessage("@red@<img=482>[Bloodslayer Chest]: " + player.getUsername() + " has received " + ItemDefinition.forId(ultraDrops).getName() + " from the @red@Bloodslayer chest<img=482>");
			}
		}
	}
}
