package com.arlania.engine.task.impl;

import com.arlania.engine.task.Task;
import com.arlania.world.entity.impl.player.Player;

public class AutorelFixTask extends Task {

	
	int tick = 14;
	
	final Player player;
	public AutorelFixTask(Player player) {
		super(1, player, true);
		this.player = player;
	}
	
	
	@Override
	protected void execute() {
		tick -=1;
		
		if(tick == 13 && !player.isAutoRetaliate()) {
			stop();
		}

		
		player.getPacketSender().sendConfig(172, player.isAutoRetaliate() ? 1 : 0);
	}

}
