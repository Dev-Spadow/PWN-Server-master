package com.arlania.world.content.timedlocations;
//message on discord if need help - milo#0420

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.Position;

import com.arlania.world.entity.impl.player.Player;

public abstract class TimedLocation {

	private Position playerSpawnPoint = null;
	private int timeLimitInMinutes = -1;
	private long startTimer;
	private double secondsSpentInArea = -1;
	private Player player;
	private String instanceName = "Boomer";
	private boolean sessionStatus = false;


	public TimedLocation(Position playerSpawnPoint, int timeLimitInMinutes, Player player, String instanceName) {
		this.playerSpawnPoint = playerSpawnPoint;
		this.timeLimitInMinutes = timeLimitInMinutes;
		this.player = player;

	}



	public void setInstanceName(String name) {
		instanceName = name;
	}

	public String getInstanceName() {
		return instanceName;
	}



	public void setPlayer(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}


	public void setSession(boolean status) {
		sessionStatus = status;
	}

	public boolean getSessionStatus() {
		return sessionStatus;
	}

	public void runTimer() {
		player.moveTo(playerSpawnPoint);
		startTimer = System.nanoTime();
		TaskManager.submit(new Task(2, this, false) {
			@Override
			public void execute() {
				if (!getPlayer().getSession().getChannel().isConnected()) {
					endSession();
					stop();
				}
				final long elapsedTime = System.nanoTime() - startTimer;
				secondsSpentInArea = (double) elapsedTime / 1_000_000_000.0;
				if ((int) secondsSpentInArea >= timeLimitInMinutes * 30 && getSessionStatus()) {
					endSession();
					stop();
				}
			}
		});
	}

	public abstract void startSession();

	public abstract void endSession();

	public int getTimeLimit() {
		return timeLimitInMinutes;
	}

	public void sendPlayerToSpawnPoint() {
		if (playerSpawnPoint != null) {
			player.sendMessage("@red@Please relog to start another session");

			}

	}

}

