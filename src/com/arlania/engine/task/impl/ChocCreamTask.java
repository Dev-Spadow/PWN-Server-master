package com.arlania.engine.task.impl;

import com.arlania.engine.task.Task;
import com.arlania.util.QuickUtils;
import com.arlania.world.entity.impl.player.Player;

public class ChocCreamTask extends Task {

	final Player player;

	public ChocCreamTask(Player player) {
		super(100, player, true);
		this.player = player;
	}

	@Override
	protected void execute() {

		if (player.getChocCreamTime() <= 100) {
			player.sendMessage("Your IceCream cone effect has ended.");
			player.setChocCreamRateActive(false);
			player.setChocCreamTime(0);
			stop();
			return;
		}
		if (player.getChocCreamTime() % 1500 == 0) {
			player.sendMessage("@blu@Chocolate Ice cream time left:@red@ " + (int) QuickUtils.tickToMin(player.getChocCreamTime())
					+ QuickUtils.getIceCreamPrefix(player));
		}
		player.decrementChocCreamTime(100);
	}

}
