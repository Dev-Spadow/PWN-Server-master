package com.arlania.world.content;

import com.arlania.GameSettings;
import com.arlania.engine.task.Task;
import com.arlania.world.entity.impl.player.Player;

public class DPSTask extends Task {
	
	private Player player;
	
	public DPSTask(Player player) {
		super(1, false);
		this.player = player;
	}
	
	protected void execute() {
		//System.out.println("Executing");
		if(player.getSendDpsOverlay())
			player.getPacketSender().sendString(23999, "DPS: @gre@" + player.getDpsOverlay().getDPS());
		
	}

}
