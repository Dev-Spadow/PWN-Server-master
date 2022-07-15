package com.arlania.engine.task.impl;

import com.arlania.engine.task.Task;
import com.arlania.util.QuickUtils;
import com.arlania.world.entity.impl.player.Player;

public class EatPumpkinTask extends Task {

	final Player player;

	public EatPumpkinTask(Player player) {
		super(100, player, true);
		this.player = player;
	}

	@Override
	protected void execute() {

		if (player.getEatPumpkinTime() <= 100) {
			player.sendMessage("@red@Your No longer feeling the buff from the pumpkin. Your DR lowers.");
			player.setEatPumpkinRateActive(false);
			player.setEatPumpkinTime(0);
			stop();
			return;
		}
		if (player.getEatPumpkinTime() % 1500 == 0) {
			player.sendMessage("@or2@Pumpkin time left:@bla@ " + (int) QuickUtils.tickToMin(player.getEatPumpkinTime() )
					+  QuickUtils.getPumpkinPrefix (player));
		}
		player.decrementEatPumpkinTime(100);
	}

}
