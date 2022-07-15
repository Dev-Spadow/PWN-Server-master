package com.arlania.world.content.skill.impl.slayer;

import com.arlania.model.Position;
import com.arlania.util.Misc;
import com.arlania.world.entity.impl.player.Player;

/**
 * @author Milo
 */

public enum SlayerTasks {

	NO_TASK(null, -1, null, -1, null),

	/**
	 * Easy tasks
	 */
	ZOMBIE_KID(SlayerMaster.VANNAKA, 11204, "Zombie Kids can be found in the Starter Teleports", 2100, new Position(3116, 9833, 0)),
	MR_INCREDIBLE(SlayerMaster.VANNAKA, 12833, "Mr Incredible can be found in the Starter Teleports ", 2120, new Position(2664, 3432, 0)),
	SIRENIC_OGRES(SlayerMaster.VANNAKA, 2783, "Sirenic Ogres beings can be found in the Starter Teleports!", 2000, new Position(1641, 5596,  0)),
    CRASH(SlayerMaster.VANNAKA, 12839, "Crash can be found in the Starter Teleports", 6500, new Position(2913, 4759)),
	CHARIZARD(SlayerMaster.VANNAKA, 1982, "Charizard can be found in the Starter Teleports", 6700, new Position(3282, 2970 )),
	INFERNAL(SlayerMaster.VANNAKA, 1999, "Bloated infernals can be found in the Starter Teleports", 5500, new Position(1240, 1260)),
	GODZILLA(SlayerMaster.VANNAKA, 9932, "Godzilla can be found in the Starter Teleports", 4900, new Position(2655, 4829)),
	JINIS(SlayerMaster.VANNAKA, 9994, "Jinis can be found in the Starter Teleports ", 3120, new Position(2724, 9821, 0)),

	/**
	 * Medium tasks
	 */
	
	
	CHARIZARD1(SlayerMaster.DURADEL, 1982, "Charizard can be found in the Starter Teleports", 6700, new Position(3282, 2970 )),
	INFERNAL1(SlayerMaster.DURADEL, 1999, "Bloated infernals can be found in the Starter Teleports", 5500, new Position(1240, 1260)),
	GODZILLA1(SlayerMaster.DURADEL, 9932, "Godzilla can be found in the Starter Teleports", 4900, new Position(2655, 4829)),
	JINIS1(SlayerMaster.DURADEL, 9994, "Jinis can be found in the Starter Teleports ", 3120, new Position(2724, 9821, 0)),
	RICK(SlayerMaster.DURADEL, 9273, "Rick can be found in the Medium Teleports", 9000, new Position(2369, 4944)),
	KING_KONG(SlayerMaster.DURADEL, 9903, "King Kong can be found in the Medium Teleports.", 10000, new Position(2720, 9880)),
	DARTH_VADER(SlayerMaster.DURADEL, 11, "Darth vader can be found in the Medium Teleports", 7000, new Position(2780, 10000)),
	DANTES_SATAN(SlayerMaster.DURADEL, 6303, "Dantes satan can be found in the Medium Teleports", 8000, new Position(2849, 3805)),
	HADES(SlayerMaster.DURADEL, 16, "Hades can be found in the Medium Teleports", 6500, new Position(2080, 3748)),
	LUCID_DRAGONS(SlayerMaster.DURADEL, 9247, "Lucid Dragons can be found in the Medium Teleports", 15590, new Position(2557, 4953)),
	HULK(SlayerMaster.DURADEL, 8493, "Hulk can be found in the Medium Teleports", 14400, new Position(3856, 5841)),
	DARKWIZARDS(SlayerMaster.DURADEL, 9203, "Find Dark Wizards in the Medium Teleports", 100000, new Position(2920, 9687)),
	HEATED_PYRO(SlayerMaster.DURADEL, 172, "Heated Pyro can be found in the Medium Teleports", 68000, new Position(2334, 4576, 2)),
	/**
	 * Hard tasks
	 */
	//GODZILLA1(SlayerMaster.KURADEL, 9932, "Godzilla can be found in the Medium Teleports", 6900, new Position(2452, 10147)),
	//CERBERUS1(SlayerMaster.KURADEL, 1999, "Cerberus can be found in the Medium Teleports", 7500, new Position(1240, 1247)),
	
	
	DANTES_SATAN1(SlayerMaster.KURADEL, 6303, "Dantes satan can be found in the Medium Teleports", 8000, new Position(2849, 3805)),
	HADES1(SlayerMaster.KURADEL, 16, "Hades can be found in the Medium Teleports", 6500, new Position(2080, 3748)),
	LUCID_DRAGONS1(SlayerMaster.KURADEL, 9247, "Lucid Dragons can be found in the Medium Teleports", 15590, new Position(2557, 4953)),
	HULK1(SlayerMaster.KURADEL, 8493, "Hulk can be found in the Medium Teleports", 14400, new Position(3856, 5841)),
	DARKWIZARDS1(SlayerMaster.KURADEL, 9203, "Find Dark Wizards in the Medium Teleports", 100000, new Position(2920, 9687)),
	HEATED_PYRO1(SlayerMaster.KURADEL, 172, "Heated Pyro can be found in the Medium Teleports", 68000, new Position(2334, 4576, 2)),
	STORM_BREAKER(SlayerMaster.KURADEL, 527, "Storm breakers can be found in the Hardened Teleports", 15590, new Position(2557, 4953)),
	APOLLO_RANGER(SlayerMaster.KURADEL, 1684, "Apollo rangers can be found in the Hardened Teleports", 14400, new Position(3856, 5841)),
	NOXIOUS_TROLLS(SlayerMaster.KURADEL, 5957, "Noxious trolls can be found in the Hardened Teleports", 100000, new Position(2920, 9687)),
	AZAZEL_BEASTS(SlayerMaster.KURADEL, 5958, "Azazel beasts can be found in the Hardened Teleports", 68000, new Position(2334, 4576, 2)),
	RAVANA(SlayerMaster.KURADEL, 5959, "Ravana can be found in the Hardened Teleports", 40000, new Position(2340, 3822)),
	PURPLE_WYRM(SlayerMaster.KURADEL, 9935, "Purple Wyrm can be found in the Hardened Teleports", 40000, new Position(2340, 3822)),
	HERBAL_ROUGE(SlayerMaster.KURADEL, 219, "Herabal rouges can be found in the Hardened Teleports", 26000, new Position(3038, 4384)),
	EXODEN(SlayerMaster.KURADEL, 12239, "Exoden can be found in the Hardened Teleports", 26000, new Position(3038, 4384)),
	TRINITY(SlayerMaster.KURADEL, 170, "Trinity can be found in the in the Hardened Teleports", 26000, new Position(3038, 4384)),
	/**
	 * Elite
	 */
	
	/**
	 * 
	 */
	
	STORM_BREAKER1(SlayerMaster.SUMONA, 527, "Storm breakers can be found in the Hardened Teleports", 20000, new Position(2557, 4953)),
	APOLLO_RANGER1(SlayerMaster.SUMONA, 1684, "Apollo rangers can be found in the Hardened Teleports", 20000, new Position(3856, 5841)),
	NOXIOUS_TROLLS1(SlayerMaster.SUMONA, 5957, "Noxious trolls can be found in the Hardened Teleports", 20000, new Position(2920, 9687)),
	AZAZEL_BEASTS1(SlayerMaster.SUMONA, 5958, "Azazel beasts can be found in the Hardened Teleports", 20000, new Position(2334, 4576, 2)),
	RAVANA1(SlayerMaster.SUMONA, 5959, "Ravana can be found in the Hardened Teleports", 20000, new Position(2340, 3822)),
	PURPLE_WYRM1(SlayerMaster.SUMONA, 9935, "Purple Wyrm can be found in the Hardened Teleports", 20000, new Position(2340, 3822)),
	HERBAL_ROUGE1(SlayerMaster.SUMONA, 219, "Herabal rouges can be found in the Hardened Teleports", 20000, new Position(3038, 4384)),
	EXODEN1(SlayerMaster.SUMONA, 12239, "Exoden can be found in the Hardened Teleports", 20000, new Position(3038, 4384)),
	TRINITY1(SlayerMaster.SUMONA, 170, "Trinity can be found in the in the Hardened Teleports", 20000, new Position(3038, 4384)),
	LUMINITOUS_WARRIORS(SlayerMaster.SUMONA, 185, "Lumin warriors can be found in the Expert Teleports", 20000, new Position(3038, 4384)),
	HELLHOUNDS(SlayerMaster.SUMONA, 6311, "Hellhounds can be found in the Expert Teleports", 20000, new Position(3038, 4384)),
	RAZORSPAWN(SlayerMaster.SUMONA, 2907, "Razorspawn can be found in the Expert Teleports", 20000, new Position(3038, 4384)),
    DREAMFLOW_ASSASSIN(SlayerMaster.SUMONA, 20, "Dreamflow assassins can be found in the Expert teleports", 20000, new Position(3178, 3030)),
    SABLE_BEAST(SlayerMaster.SUMONA, 1123, "Sable beasts can be found in the Expert teleports", 20000, new Position(3176, 4242, 0)),
    KHIONE(SlayerMaster.SUMONA, 259, "Khione can be found in the Expert teleports", 20000, new Position(3046, 9569, 0)),
   
    
    /**
	 * Extreme - Bravek
	 */
  
    
    
    OBITU(SlayerMaster.BRAVEK, 11383, "Obitu can be found in the Maniac teleports", 20000, new Position(3046, 9569, 0)),
    AVATAR(SlayerMaster.BRAVEK, 2264, "Avatar can be found in the Maniac teleports", 20000, new Position(3046, 9569, 0)),
    LILI(SlayerMaster.BRAVEK, 11360, "Lili can be found in the Maniac teleports", 20000, new Position(3046, 9569, 0)),
    YOSHI(SlayerMaster.BRAVEK, 8548, "Yoshi can be found in the Expert teleports", 20000, new Position(3046, 9569, 0)),
    DEMOGORGON(SlayerMaster.BRAVEK, 12835, "Demogorgons can be found in the expert teleports", 20000, new Position(3046, 9569, 0)),
    GLACIAL_QUEEN(SlayerMaster.BRAVEK, 1382, "Glacial can be found in the expert teleports", 20000, new Position(3046, 9569, 0)),
    APRICITY_QUEEN(SlayerMaster.BRAVEK, 12101, "Apricity can be found in the expert teleports", 20000, new Position(3046, 9569, 0)),
	LUMINITOUS_WARRIORS1(SlayerMaster.BRAVEK, 11383, "Obitu can be found in the Maniac teleports", 20000, new Position(3046, 9569, 0)),
	HELLHOUNDS1(SlayerMaster.BRAVEK, 2264, "Avatar can be found in the Maniac teleports", 20000, new Position(3046, 9569, 0)),
	RAZORSPAWN1(SlayerMaster.BRAVEK, 11360, "Lili can be found in the Maniac teleports", 20000, new Position(3046, 9569, 0)),
	DREAMFLOW_ASSASSIN1(SlayerMaster.BRAVEK, 8548, "Yoshi can be found in the Expert teleports", 20000, new Position(3046, 9569, 0)),
	SABLE_BEAST1(SlayerMaster.BRAVEK, 12835, "Demogorgons can be found in the expert teleports", 20000, new Position(3046, 9569, 0)),
	KHIONE1(SlayerMaster.BRAVEK, 1382, "Glacial can be found in the expert teleports", 20000, new Position(3046, 9569, 0));


    private SlayerTasks(SlayerMaster taskMaster, int npcId, String npcLocation, int XP, Position taskPosition) {
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

	public static SlayerTasks forId(int id) {
		for (SlayerTasks tasks : SlayerTasks.values()) {
			if (tasks.ordinal() == id) {
				return tasks;
			}
		}
		return null;
	}

	public static int[] getNewTaskData(SlayerMaster master, Player player) {
		int slayerTaskId = 1, slayerTaskAmount = 20;
		int easyTasks = 0, mediumTasks = 0, hardTasks = 0, eliteTasks = 0, extremeTasks = 0;

		/*
		 * Calculating amount of tasks
		 */
		for (SlayerTasks task : SlayerTasks.values()) {
			if (task.getTaskMaster() == SlayerMaster.VANNAKA)
				easyTasks++;
			else if (task.getTaskMaster() == SlayerMaster.DURADEL)
				mediumTasks++;
			else if (task.getTaskMaster() == SlayerMaster.KURADEL)
				hardTasks++;
			else if (task.getTaskMaster() == SlayerMaster.SUMONA)
				eliteTasks++;
			else if (task.getTaskMaster() == SlayerMaster.BRAVEK)
				extremeTasks++;
		}

		if (master == SlayerMaster.VANNAKA) {
			slayerTaskId = 1 + Misc.getRandom(easyTasks);
			if (slayerTaskId > easyTasks)
				slayerTaskId = easyTasks;
			slayerTaskAmount = 20 + Misc.getRandom(10);
			
		} else if (master == SlayerMaster.DURADEL) {
			slayerTaskId = easyTasks - 1 + Misc.getRandom(mediumTasks);
			slayerTaskAmount = 25 + Misc.getRandom(10);
		} else if (master == SlayerMaster.KURADEL) {
			slayerTaskId = 1 + easyTasks + mediumTasks + Misc.getRandom(hardTasks - 1);
			slayerTaskAmount = 30 + Misc.getRandom(10);
		} else if (master == SlayerMaster.SUMONA) {
			slayerTaskId = 1 + easyTasks + mediumTasks + hardTasks + Misc.getRandom(eliteTasks - 1);
			slayerTaskAmount = 35 + Misc.getRandom(15);
		} else if (master == SlayerMaster.BRAVEK) {
			slayerTaskId = 1 + easyTasks + mediumTasks + hardTasks + eliteTasks + Misc.getRandom(extremeTasks - 1);
			if (player.getBravekDifficulty() == null) {
				slayerTaskAmount = 35 + Misc.getRandom(20);
			} else {
				switch (player.getBravekDifficulty()) {
				case "easy":
					slayerTaskAmount = 20 + Misc.getRandom(5);
					break;
				case "medium":
					slayerTaskAmount = 35 + Misc.getRandom(10);
					break;
				case "hard":
					slayerTaskAmount = 50 + Misc.getRandom(30);
					break;
				}
			}
		}
		return new int[] { slayerTaskId, slayerTaskAmount };
	}
	
	@Override
	public String toString() {
		return Misc.ucFirst(name().toLowerCase().replaceAll("_", " "));
	}
}
