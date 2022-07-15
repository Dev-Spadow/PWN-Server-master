package com.arlania.world.content.upgrading;

import com.arlania.world.entity.impl.player.Player;

import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 */
public class UpgradeListener {
	private Player player;

	private static int upgrade_cost_id = 10835;

	public UpgradeListener(Player player) {
		this.player = player;
	}

	public void upgrade() {
		Arrays.stream(UpgradeData.values()).forEach(val -> {
			if (val.getRequired().getId() == player.getUpgradeSelection().getId()) {
				if (getRestrictions(val)) {
					player.getInventory().delete(val.getRequired());
					player.getInventory().delete(upgrade_cost_id, val.getBagsRequired());
																							
					TimerTask task = new TimerTask() {
						int tick = 0;

						@Override
						public void run() {
							if (tick == 0) {
							} else if (tick == 3) {
								boolean success = random.nextInt(100) <= val.getChance() ? true : false;
								if (success) {
									player.getInventory().add(val.getReward());
								}
								cancel();
							}
							tick++;
						}

					};

					Timer timer = new Timer();
					timer.schedule(task, 10, 10);
					;
				}
			} else {
			}
		});
	}

	private boolean getRestrictions(UpgradeData data) {
		if (!player.getInventory().contains(data.getRequired().getId())
				|| player.getInventory().getAmount(10835) < data.getBagsRequired()) {
			return false;
		}
		return true;
	}

	private Random random = new Random();
}
