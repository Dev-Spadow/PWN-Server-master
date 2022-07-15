package com.arlania.world.content.chests;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.Position;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class RaidsEasyChest {

	// Item ids that will be dropped
	public static int pvmKey = 13591;

	// useless, just needed to write down object id
	public static int chest = 54587;

	// We roll for random on scale of 1 - 200
	
	// if random is 121 - 185, they get this table
	public static int rareLoots[] = { 10835,1464,19866,11978,3088,14249,19935,18753,6640,4082,4781,4782,19670,4082,14808,19936,4761,4056,4057,4058,4059,6507,4762,4763,4764,4765,5089,18894,931,930,5211,926,5210,4781,4782,20240,3957,4785,5195,15032,5082,5083,5084,15656,19470,19471,19472,19473,19474,19619,5163,12426,19140 };

	//if random is 186 to 200, they get this table
	public static int ultraLoots[] = { 10835,3971,4279,11978,5170,16455,14249,16455,3088,18753,17291,6640,18925,3937,2795,12926,3938,19958,5185,3939,3944,3945,19670,3946,3947,3948,19141,9492,19727,5129,16455,13258,13256,13259,19618,19691,19692,19693,19694,19695,19696,19159,19160,19161,19162,19163,19164,19165,19166,9493,9494,9495,14494,14492,14490,15374,19890 };

	// not using this one
	//public static int amazingLoots[] = { 5022 };

	// if roll is 1 - 120, they get this table 
	public static int commonLoots[] = {10835,18929,3988,3666,455,19935,14249,1038,19670,1040,4671,18401,1042,4672,13998,1044,1046,1048,3317,3912,3824,1499,3973,6640};

	public static int getRandomItem(int[] array) {
		return array[Misc.getRandom(array.length - 1)];
	}

	public static void openChest(Player player) {
		if (!player.getClickDelay().elapsed(1000))
			return;
		if (player.getInventory().contains(13591)) {
			player.getInventory().delete(13591, 1);

			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {

					player.getPacketSender().sendMessage("@red@<shad=355>Opening Raids Chest...");
					giveReward(player);
					this.stop();

				}
			});
		} else {

			player.getPacketSender().sendMessage("@red@<shad=355>You require a Raids Key to open this chest!");
			return;
		}

	}

	// Main method for determining roll
	public static void giveReward(Player player) {
		int random = Misc.inclusiveRandom(1, 200);
		if (random >= 1 && random <= 150) {
			int commonDrop = getRandomItem(commonLoots);
			if (commonDrop == 10835) {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(10, 20));
			} else if (commonDrop == 10835) {
				player.getInventory().add(commonDrop, 2);
			} else if (commonDrop == 10835) {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(10, 50));
			} else {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(1, 1));
			}
		} else if (random >= 151 && random <= 194) {
			int rareDrop = getRandomItem(rareLoots);
			if (rareDrop == 10835) {
				player.getInventory().add(rareDrop, Misc.inclusiveRandom(5000, 7500));
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
				player.getInventory().add(ultraDrops, Misc.inclusiveRandom(7500, 10000));
			} else if (ultraDrops == 10835) {
				player.getInventory().add(ultraDrops, 4);
			} else {
				player.getInventory().add(ultraDrops, 1);
				World.sendMessage("@blu@<shad=255>[@red@<shad=355>RAID CHEST@blu@<shad=255>]@blu@<shad=200>: " + player.getUsername() + " has received " + ItemDefinition.forId(ultraDrops).getName() + " from the Raids chest!");

			}
		}
	}
}
