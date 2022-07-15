package com.arlania.engine.task.impl;

import com.arlania.engine.task.Task;
import com.arlania.util.QuickUtils;
import com.arlania.world.entity.impl.player.Player;

public class SmokeTheBongTask extends Task {

	final Player player;

	public SmokeTheBongTask(Player player) {
		super(100, player, true);
		this.player = player;
	}

	@Override
	protected void execute() {

		if (player.getSmokeTheBongTime() <= 100) {
			player.sendMessage("Your No longer feeling the buff from the weed. Your DR lowers.");
			player.setSmokeTheBongRateActive(false);
			player.setSmokeTheBongTime(0);
			stop();
			return;
		}
		if (player.getSmokeTheBongTime() % 1500 == 0) {
			player.sendMessage("@blu@Stoned time left:@red@ " + (int) QuickUtils.tickToMin(player.getSmokeTheBongTime())
					+ QuickUtils.getIceCreamPrefix(player));
		}
		player.decrementSmokeTheBongTime(100);
	}

}
