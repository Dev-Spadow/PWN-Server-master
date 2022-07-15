package com.arlania.engine.task.impl;

import com.arlania.engine.task.Task;
import com.arlania.util.QuickUtils;
import com.arlania.world.entity.impl.player.Player;

public class CleansingTask extends Task {

	final Player player;

	public CleansingTask(Player player) {
		super(100, player, true);
		this.player = player;
	}

	@Override
	protected void execute() {

		if (player.getCleansingTime() <= 100) {
			player.sendMessage("Your double drop effect has ended.");
			player.setDoubleDropsActive(false);
			player.setCleansingTime(0);
			stop();
			return;
		}

		player.decrementCleansingTime(100);
		if (player.getCleansingTime() % 1500 == 0) {
			player.sendMessage("@blu@Double Drop Time left:@red@ " + (int) QuickUtils.tickToMin(player.getCleansingTime())
					+ QuickUtils.getCleansingPrefix(player));
		}
	}

}
