package com.arlania.engine.task.impl;

import com.arlania.engine.task.Task;
import com.arlania.model.Skill;
import com.arlania.util.QuickUtils;
import com.arlania.world.entity.impl.player.Player;

public class PrayerRegainPotionTask extends Task {
	
	final Player player;
	
	private int tick = 20;
	
	public static boolean effectStatus = false;
	
	public PrayerRegainPotionTask(Player player) {
		super(100, player, true);
		this.player = player;
	}

	@Override
	protected void execute() {
		tick -= 10;
		
		if(tick % 2 == 0) {
			player.getSkillManager().setCurrentLevel(Skill.PRAYER, 990);
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
