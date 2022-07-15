package com.arlania.world.content.chests;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.Animation;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class EnchantedSlayerChest {

	// Item ids that will be dropped
	public static int pvmKey = 5205;

	// useless, just needed to write down object id
	public static int chest = 2995;

	// We roll for random on scale of 1 - 200
	
	// if random is 121 - 185, they get this table
	public static int rareLoots[] = { 14033,10168,13997,3949,3950,3952,5020 };

	//if random is 186 to 200, they get this table
	// not using this one
	//public static int amazingLoots[] = { 5022 };

	// if roll is 1 - 120, they get this table 
	public static int commonLoots[] = { 10205,19935,16455,16488,15374,19106,2545,2546,2547,2548,5184,14546,6509,6505,6758,6759,19936,17750  };

	public static int getRandomItem(int[] array) {
		return array[Misc.getRandom(array.length - 1)];
	}

	public static void openChest(Player player) {
		if (!player.getClickDelay().elapsed(1000))
			return;
		if (player.getInventory().contains(11006)) {
			player.getInventory().delete(11006, 1);

			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {

					player.getPacketSender().sendMessage("Opening Enchanted Chest...");
					player.performAnimation(new Animation(827));
					giveReward(player);
					this.stop();

				}
			});
		} else {

			player.getPacketSender().sendMessage("@red@You require a Enchanted Key to open this chest!");
			return;
		}

	}

	// Main method for determining roll
	public static void giveReward(Player player) {
		int random = Misc.inclusiveRandom(1, 50);
		if (random >= 1 && random <= 42) {
			int commonDrop = getRandomItem(commonLoots);
				player.getInventory().add(commonDrop, 1);
			}
			else if (random >= 39 && random <= 50) {
			int rareDrop = getRandomItem(rareLoots);
					World.sendMessage("@red@<img=482>[Bloodslayer Chest]: " + player.getUsername() + " has received " + ItemDefinition.forId(rareDrop).getName() + " from the @red@Enchanted chest<img=482>");
					player.getInventory().add(rareDrop, 1);
		}
	}
}
