package com.arlania.world.content.chests;

import com.arlania.util.Misc;
import com.arlania.util.RandomUtility;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class DiamondChest {
	
	/*
	 * Rewards
	 */
	public static void openBox (Player player) {
		int chance = RandomUtility.random(7);
		player.getInventory().delete(10478, 1);
		if (chance >= 0 && chance <= 1) {
			player.getInventory().add(4670, 1 + RandomUtility.getRandom(3));
		} else
			if (chance >= 2 && chance <= 3) {
				player.getInventory().add(4671, 1 + RandomUtility.getRandom(3));
			} else
				if (chance >= 4 && chance <= 5) {
					player.getInventory().add(4672, 1 + RandomUtility.getRandom(3));
				} else
					if (chance >= 6) {
						player.getInventory().add(4673, 1 + RandomUtility.getRandom(3));

				}
			}
		}
