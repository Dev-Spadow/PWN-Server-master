package com.arlania.engine.task.impl;

import com.arlania.engine.task.Task;
import com.arlania.model.Skill;
import com.arlania.util.QuickUtils;
import com.arlania.world.entity.impl.player.Player;

public class HPRegainPotionTask extends Task {
	
	public static boolean effectStatus = false;
	
	final Player player;
	
	private int tick = 20;
	
	public HPRegainPotionTask(Player player) {
		super(1, player, true);
		this.player = player;
	}
	
	protected void execute() {
		tick -= 10;
		
		if(tick % 2 == 0) {
			player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 990);
			player.sendMessage("Your pray has been set to 99");
			player.sendMessage("Time left: @blu@" + QuickUtils.tickToSec(tick) + " Minutes" );
		}
		if(tick == 0) {
			player.sendMessage("Your prayer regain potion effect has ended");
			effectStatus = false;
			stop();
		}
	}

}
