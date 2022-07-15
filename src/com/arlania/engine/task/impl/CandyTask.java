package com.arlania.engine.task.impl;

import com.arlania.engine.task.Task;
import com.arlania.util.QuickUtils;
import com.arlania.world.entity.impl.player.Player;

public class CandyTask extends Task {

	final Player player;

	public CandyTask(Player player) {
		super(100, player, true);
		this.player = player;
	}

	@Override
	protected void execute() {

		if (player.getCandyTime() <= 100) {
			player.sendMessage("@red@Your No longer feeling the buff from the pumpkin. Your DPS lowers.");
			player.setCandyRateActive(false);
			player.setCandyTime(0);
			stop();
			return;
		}
		if (player.getEatPumpkinTime() % 1500 == 0) {
			player.sendMessage("@or2@Candy time left:@bla@ " + (int) QuickUtils.tickToMin(player.getCandyTime() )
					+  QuickUtils.getCandyPrefix (player));
		}
		player.decrementCandyTime(100);
	}

}
