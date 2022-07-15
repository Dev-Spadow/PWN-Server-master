package com.arlania.world.content.chests;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class DeluxeDonatorChest {

	// Item ids that will be dropped
	public static int pvmKey = 17363;

	// useless, just needed to write down object id
	public static int chest = 49347;

	// We roll for random on scale of 1 - 200
	public static int commonLoots[] = { 10835,85,989,19670,19626 };
	// if random is 121 - 185, they get this table
	public static int rareLoots[] = {19140,5184,18950,18749,9943,17413,19936,2547,4762,4763,4764,4761,5089,15374,19618,19620,19691,19692,19693,19694,19695,19696};

	//if random is 186 to 200, they get this table
	public static int ultraLoots[] = { 17892,17894,17893,17890,16455,10835,6191,19890,5170,5211,13991,13992,13993,13994,13995,14447,14448,4794,4795,4796,4797,19127,19128,19129 };

	// not using this one
	//public static int amazingLoots[] = { 5022 };

	// if roll is 1 - 120, they get this table 
	public static int getRandomItem(int[] array) {
		return array[Misc.getRandom(array.length - 1)];
	}

	public static void openChest(Player player) {
		if (!player.getClickDelay().elapsed(1000))
			return;
		if (player.getInventory().contains(17363)) {
			player.getInventory().delete(17363, 1);

			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {

					player.getPacketSender().sendMessage("@red@You Open the Deluxe chest...");
					giveReward(player);
					this.stop();

				}
			});
		} else {

			player.getPacketSender().sendMessage("@red@You need a Deluxe key to open this");
			return;
		}

	}

	// Main method for determining roll
	public static void giveReward(Player player) {
		int random = Misc.inclusiveRandom(1, 200);
		if (random >= 1 && random <= 170) {
			int commonDrop = getRandomItem(commonLoots);
			if (commonDrop == 10835) {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(100, 200));
			} else if (commonDrop == 10835) {
				player.getInventory().add(commonDrop, 2);
			} else if (commonDrop == 10835) {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(100, 5000));
			} else {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(1, 1));
			}
		} else if (random >= 171 && random <= 194) {
			int rareDrop = getRandomItem(rareLoots);
			if (rareDrop == 10835) {
				player.getInventory().add(rareDrop, Misc.inclusiveRandom(1000, 25000));
			} else if (rareDrop == 10835) {
				player.getInventory().add(rareDrop, 4);
			} else {
				if(ItemDefinition.forId(rareDrop).getName().toLowerCase().contains("gem"))
					player.getInventory().add(rareDrop, Misc.inclusiveRandom(3, 5));
				else
					player.getInventory().add(rareDrop, 1);
			}
		} else if (random >= 195 && random <= 200) {
			int ultraDrops = getRandomItem(ultraLoots);
			if (ultraDrops == 10835) {
				player.getInventory().add(ultraDrops, Misc.inclusiveRandom(15000, 50000));
			} else if (ultraDrops == 10835) {
				player.getInventory().add(ultraDrops, 4);
			} else {
				player.getInventory().add(ultraDrops, 1);
				World.sendMessage("@bla@[DELUXE CHEST]:@red@ " + player.getUsername() + " @bla@has received " + ItemDefinition.forId(ultraDrops).getName() + " from CHEST!");
			}
		}
	}
}
