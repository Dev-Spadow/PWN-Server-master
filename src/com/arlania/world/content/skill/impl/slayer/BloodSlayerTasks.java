package com.arlania.world.content.skill.impl.slayer;

import com.arlania.model.Position;
import com.arlania.util.Misc;
import com.arlania.world.entity.impl.player.Player;

/**
 * @author Milo
 */

public enum BloodSlayerTasks {

	NO_TASK(null, -1, null, -1, null),

	/**
	 * Easy tasks
	 */
	
	FROST_DEMON(SlayerMaster.IMP, 82, "Frost Demons can be found in the Blood Slayer cave", 2100, new Position(2759, 3174, 0)),
	RYUK(SlayerMaster.IMP, 354, "Ryuk can be found in the Blood Slayer cave", 2100, new Position(2759, 3174, 0)),
	KILLER_IMPS(SlayerMaster.IMP, 12825, "Killer imps can be found in the Blood Slayer cave", 2100, new Position(2759, 3174, 0)),
	BLOOD_NINJAS(SlayerMaster.IMP, 12806, "Blood Ninjas can be found in the Blood Slayer cave", 2100, new Position(2759, 3174, 0)),
	BLOOD_DEMON(SlayerMaster.IMP, 667, "Blood Demons can be found in the Blood Slayer cave", 2100, new Position(2759, 3174, 0)),
	SKAVID(SlayerMaster.IMP, 129, "Skavid can be found in the Blood Slayer cave", 2100, new Position(2759, 3174, 0)),
	OTAKU(SlayerMaster.IMP, 353, "Otaku can be found in the Blood Slayer cave", 2100, new Position(2759, 3174, 0)),
	REVENANT_HELLHOUND(SlayerMaster.IMP, 49, "Revenant Hellhounds can be found in the Blood Slayer cave", 2100, new Position(2759, 3174, 0)),
	BLOOD_WARRIORS(SlayerMaster.IMP, 34, "Blood warriors can be found in the Blood Slayer cave", 2100, new Position(2759, 3174, 0))
	
	;

    private BloodSlayerTasks(SlayerMaster taskMaster, int npcId, String npcLocation, int XP, Position taskPosition) {
		this.taskMaster = taskMaster;
		this.npcId = npcId;
		this.npcLocation = npcLocation;
		this.XP = XP;
		this.taskPosition = taskPosition;
	}

	private SlayerMaster taskMaster;
	private int npcId;
	private String npcLocation;
	private int XP;
	private Position taskPosition;

	public SlayerMaster getTaskMaster() {
		return this.taskMaster;
	}

	public int getNpcId() {
		return this.npcId;
	}

	public String getNpcLocation() {
		return this.npcLocation;
	}

	public int getXP() {
		return this.XP;
	}

	public Position getTaskPosition() {
		return this.taskPosition;
	}

	public static BloodSlayerTasks forId(int id) {
		for (BloodSlayerTasks tasks : BloodSlayerTasks.values()) {
			if (tasks.ordinal() == id) {
				return tasks;
			}
		}
		return null;
	}

	public static int[] getNewBloodTaskData(BloodSlayerMaster bloodSlayerMaster, Player player) {
		int slayerTaskId = 1, slayerTaskAmount = 30;
		int easyTasks = 0;

		/*
		 * Calculating amount of tasks
		 */
		for (BloodSlayerTasks task : BloodSlayerTasks.values()) {
			if (task.getTaskMaster() == SlayerMaster.IMP)
				easyTasks++;
		}

		if (bloodSlayerMaster == BloodSlayerMaster.IMP) {
			slayerTaskId = 1 + Misc.getRandom(easyTasks);
			if (slayerTaskId > easyTasks)
				slayerTaskId = easyTasks;
			slayerTaskAmount = 30 + Misc.getRandom(15);
		}


		return new int[] { slayerTaskId, slayerTaskAmount };
	}
	
	@Override
	public String toString() {
		return Misc.ucFirst(name().toLowerCase().replaceAll("_", " "));
	}
}
