package com.arlania.world.content.timedlocations;

import com.arlania.GameSettings;
import com.arlania.model.Position;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class DreamZoneTimedLocation extends TimedLocation {

	public DreamZoneTimedLocation(Position playerSpawnPoint, int timeLimitInMinutes, Player player, String instanceName) {
		super(playerSpawnPoint, timeLimitInMinutes, player, instanceName);
	}

	@Override
	public void startSession() {

		super.setSession(true);
		super.getPlayer().inDreamZoneTimedLocation = true;
		getPlayer().sendMessage(
				"@blu@" + getInstanceName() + " will be sleeping for 30 minutes!");
		super.runTimer();
	}

	@Override
	public void endSession() {
		super.getPlayer().setPlayerLocked(true);
		if (getPlayer().getSession().getChannel().isConnected()) {
			getPlayer().moveTo(GameSettings.DEFAULT_POSITION);
			super.setSession(false);
			super.getPlayer().inDreamZoneTimedLocation = false;
			super.getPlayer().setPlayerLocked(false);
		}
	}

}
