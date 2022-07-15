package com.arlania.world.content.bosses;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.definitions.NpcDefinition;
import com.arlania.world.World;
import com.arlania.world.content.PlayerPanel;

public class HourlyBoss {

	private static final int[] HOURLY_BOSSES = new int[] { 5996, 12841, 9932, 6311, 8493, 6316, 9766 };

	public static int currentHourlyBoss = 0;
	private static int currentIndex = 0;

	private static int tick = 0;

	public static void setHourlyBoss() {

		TaskManager.submit(new Task(100) {
			@Override
			protected void execute() {
				tick++;

				if (tick >= 60) {
					setHourlyBoss();
					if (currentIndex >= HOURLY_BOSSES.length) {
						currentIndex = 0;
					}
					currentIndex++;
					currentHourlyBoss = HOURLY_BOSSES[currentIndex];
					World.getPlayers().forEach(p -> {
						PlayerPanel.refreshPanel(p);
					});
					tick = 0;
				}
			}
		});
	}

	protected static String getCurrent() {

		return NpcDefinition.forId(currentHourlyBoss).getName();
	}

}
