package com.arlania.engine.task.impl;

import com.arlania.engine.task.Task;
import com.arlania.model.Graphic;
import com.arlania.world.entity.impl.player.Player;

public class CivilRainTask extends Task {

	final Player player;
	
	
	public CivilRainTask(Player player) {
		super(2, player, true);
		this.player = player;
	}
	
	
	@Override
	protected void execute() {
		if(player.getEquipment().contains(6482)) {
			player.performGraphic(new Graphic(436));
		}
		
	}

}
