package com.arlania.world.content.chests;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.Animation;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class OPDonationChest {

	// Item ids that will be dropped
	public static int pvmKey = 5266;

	// useless, just needed to write down object id
	public static int chest = 7350;

	// We roll for random on scale of 1 - 200
	
	// if random is 121 - 185, they get this table
	public static int rareLoots[] = { 19936,6770,6758,6759,5185,8699,16455,3317,2547,2546,2545,3647,5163,19821,19958,12426,934,13997,10205,4742,13999 };

	//if random is 186 to 200, they get this table
	public static int ultraLoots[] = { 3282,773,774,5020,5132,11978,15566,10905,4803,3277,19938,8654,5197,5170,19890,3961,16456,13691,9116,9117 };

	// not using this one
	//public static int amazingLoots[] = { 5022 };

	// if roll is 1 - 120, they get this table 
	public static int commonLoots[] = { 5184,6507,19935,17933,12162,18950,10168,14546,6505,6509,13998,10835,4671,4672,4670,4673,14249,298 };

	public static int getRandomItem(int[] array) {
		return array[Misc.getRandom(array.length - 1)];
	}

	public static void openChest(Player player) {
		if (!player.getClickDelay().elapsed(1000))
			return;
		if (player.getInventory().contains(5266)) {
			player.getInventory().delete(5266, 1);

			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {
					player.performAnimation(new Animation(827));
					player.getPacketSender().sendMessage("@red@You Open the Grand Donation chest...");
					giveReward(player);
					this.stop();

				}
			});
		} else {

			player.getPacketSender().sendMessage("@red@You need a donators key! Visit the ::store to purchase them");
			return;
		}

	}

	// Main method for determining roll
	public static void giveReward(Player player) {
		int random = Misc.inclusiveRandom(1, 100);
		if (random >= 1 && random <= 69) {
			int commonDrop = getRandomItem(commonLoots);
			if (commonDrop == 10835) {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(1000, 20000));
			} else if (commonDrop == 10835) {
				player.getInventory().add(commonDrop, 2);
			} else if (commonDrop == 10835) {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(100, 5000));
			} else {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(1, 1));
			}
		} else if (random >= 70 && random <= 96) {
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
		} else if (random >= 97) {
			int ultraDrops = getRandomItem(ultraLoots);
			if (ultraDrops == 10835) {
				player.getInventory().add(ultraDrops, Misc.inclusiveRandom(1500, 5000));
			} else if (ultraDrops == 10835) {
				player.getInventory().add(ultraDrops, 4);
			} else {
				player.getInventory().add(ultraDrops, 1);
				World.sendMessage("<shad=0789de>[Grand Chest]</shad>@bla@: " + player.getUsername() + " has received a <shad=0789de>" + ItemDefinition.forId(ultraDrops).getName() + " </shad>from the @red@Grand Chest!");
			}
		}
	}
}
