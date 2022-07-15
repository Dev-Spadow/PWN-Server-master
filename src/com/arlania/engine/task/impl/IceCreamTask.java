package com.arlania.engine.task.impl;

import com.arlania.engine.task.Task;
import com.arlania.util.QuickUtils;
import com.arlania.world.entity.impl.player.Player;

public class IceCreamTask extends Task {

	final Player player;

	public IceCreamTask(Player player) {
		super(100, player, true);
		this.player = player;
	}

	@Override
	protected void execute() {

		if (player.getIceCreamTime() <= 100) {
			player.sendMessage("Your Ice Cream cone effect has ended.");
			player.setIceCreamRateActive(false);
			player.setIceCreamTime(0);
			stop();
			return;
		}
		if (player.getIceCreamTime() % 1500 == 0) {
			player.sendMessage("@blu@Ice Cream time left:@red@ " + (int) QuickUtils.tickToMin(player.getIceCreamTime())
					+ QuickUtils.getIceCreamPrefix(player));
		}
		player.decrementIceCreamTime(100);
	}

}
