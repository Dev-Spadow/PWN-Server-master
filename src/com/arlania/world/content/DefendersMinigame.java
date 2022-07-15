package com.arlania.world.content;

import com.arlania.model.Item;
import com.arlania.model.container.impl.Equipment;
import com.arlania.util.RandomUtility;
import com.arlania.world.entity.impl.player.Player;

public class DefendersMinigame {

	private Player player;

	public DefendersMinigame(Player player) {
		this.player = player;
	}

	private final int[] DEFENDER_IDS = new int[] { 20693, 20694, 20695, 20696, 20697, 20698, 20699 };

	public void handleKill() {

		if (!player.getInventory().contains(5206)) {
			player.sendMessage("Kill didn't count because u don't have atleast 1 minigame token in your inventory");
			return;
		}

		drainToken();
		handleDrop();

	}

	private void drainToken() {
		player.getInventory().delete(5206, 1);
	}

	private void handleDrop() {

		boolean gotDrop = RandomUtility.inclusiveRandom(0, 200) >= 100;

		if (gotDrop) {
			//system.out.println("Got drop!!!");
			getNextDefender();
		}

	}

	private void getNextDefender() {

		if (!player.getEquipment().containsAny(DEFENDER_IDS)) {
			player.getInventory().add(20693, 1);
			player.sendMessage("Congratulations, you got your first defender!");
			return;
		}

		for (int i = 0; i < DEFENDER_IDS.length; i++) {
			if (player.getEquipment().get(Equipment.SHIELD_SLOT).getId() == DEFENDER_IDS[i]) {
				if (player.getEquipment().get(Equipment.SHIELD_SLOT).getId() != 20699) {
					player.getEquipment().set(Equipment.SHIELD_SLOT, new Item(DEFENDER_IDS[i + 1], 1));
					player.getEquipment().refreshItems();
					player.sendMessage("Congrats, you've upgraded your defenders tier!");
					break;
				} else {
					player.sendMessage(
							":/ you got a drop, but you already have the best possible defender from the minigame");
				}
			}
		}

	}

}
