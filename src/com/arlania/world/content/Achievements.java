package com.arlania.world.content;

import com.arlania.model.Item;
import com.arlania.util.Misc;
import com.arlania.world.entity.impl.player.Player;

public class Achievements {

	public enum AchievementData {

		KILL_A_MONSTER_USING_MELEE(Difficulty.EASY, "Kill a Monster Using Melee", 45005, null, "", "", "", "", "", "",  new Item(10835, 100), new Item(989, 1), new Item(744, 10)),
		KILL_A_MONSTER_USING_RANGED(Difficulty.EASY, "Kill a Monster Using Ranged", 45006, null, "", "", "", "", "", "",  new Item(10835, 100), new Item(989, 1), new Item(744, 10)),
		KILL_A_MONSTER_USING_MAGIC(Difficulty.EASY, "Kill a Monster Using Magic", 45007, null, "", "", "", "", "", "",  new Item(10835, 100), new Item(989, 1), new Item(744, 10)),
		TELEPORT_HOME(Difficulty.EASY, "Teleport Home", 45008, null, "", "", "", "", "", "", new Item(10835, 100), new Item(989, 1), new Item(744, 10)),
		OPEN_THE_STARTER_CHEST(Difficulty.EASY, "Open the starter chest", 45009, null, "", "", "", "", "", "",  new Item(10835, 100), new Item(989, 1), new Item(744, 10)),
		OPEN_THE_MEDIUM_CHEST(Difficulty.EASY, "Open the medium chest", 45010, null, "", "", "", "", "", "",  new Item(10835, 100), new Item(989, 1), new Item(744, 10)),
		KILL_A_ZOMBIE_KID(Difficulty.EASY, "Kill a Zombie H", 45011, null, "", "", "", "", "", "",  new Item(10835, 100), new Item(989, 1), new Item(744, 10)),
		VISIT_KALVOTHS_LAIR(Difficulty.EASY, "Visit Kalvoths Lair", 45012, null, "", "", "", "", "", "",  new Item(10835, 1), new Item(989, 1), new Item(744, 10)),
		VISIT_POS(Difficulty.EASY, "Access ::pos", 45013, null, "", "", "", "", "", "",  new Item(10835, 100), new Item(989, 1), new Item(744, 10)),
		COMPLETE_A_SLAYER_TASK(Difficulty.EASY, "Complete A Slayer Task", 45014, null, "", "", "", "", "", "",  new Item(10835, 100), new Item(989, 1), new Item(744, 10)),
		REACH_MAX_EXP_IN_A_SKILL(Difficulty.EASY, "Reach Max Exp In A Skill", 45015, null, "", "", "", "", "", "", new Item(10835, 25), new Item(989, 1), new Item(15373, 1)),
		DEAL_EASY_DAMAGE_USING_MELEE(Difficulty.EASY, "Deal 50M Melee Damage", 45016, new int[]{0, 50000000}, "", "", "", "", "", "",  new Item(10835, 100), new Item(989, 1), new Item(744, 10)),
		DEAL_EASY_DAMAGE_USING_RANGED(Difficulty.EASY, "Deal 50M Ranged Damage", 45017, new int[]{1, 50000000}, "", "", "", "", "", "",  new Item(10835, 100), new Item(989, 1), new Item(744, 10)),
		DEAL_EASY_DAMAGE_USING_MAGIC(Difficulty.EASY, "Deal 50M Magic Damage", 45018, new int[]{2, 50000000}, "", "", "", "", "", "",  new Item(10835, 100), new Item(989, 1), new Item(744, 10)),
		PERFORM_A_SPECIAL_ATTACK(Difficulty.EASY, "Perform a Special Attack", 45019, null, "", "", "", "", "", "",  new Item(10835, 1), new Item(989, 100), new Item(744, 10)),
		CLAIM_A_BOND(Difficulty.EASY, "Claim a bond", 45020, null, "", "", "", "", "", "",  new Item(10835, 1), new Item(989, 100), new Item(744, 10)),
		DRINK_A_DELUGE_POTION(Difficulty.EASY, "Drink a Deluge Potion", 45021, null, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(744, 25)),
		KILL_ZEUS_50_TIMES(Difficulty.EASY, "Kill Zeus 50 Times", 45022, new int[]{3, 50}, "", "", "", "", "", "",  new Item(10835, 1), new Item(989, 100), new Item(744, 10)),
//KILL_NARUTO_5_TIMES
		KILL_NARUTO_100_TIMES(Difficulty.MEDIUM, "Kill Naruto 100 Times", 45026, new int[]{4, 100}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(744, 25)),
		KILL_ZEUS_250_TIMES(Difficulty.MEDIUM, "Kill Zeus 250 Times", 45027, new int[]{5, 250}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(744, 25)),
		OPEN_25_MYSTERY_BOXES(Difficulty.MEDIUM, "Open 25 Mystery boxes", 45028, new int[]{6, 25}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(744, 25)),
		CLAIM_25_BONDS(Difficulty.MEDIUM, "Claim 25 Bonds", 45029, new int[]{6, 25}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(744, 25)),
		COMPLETE_THE_DBZ_MINIGAME(Difficulty.MEDIUM, "Complete a DBZ Minigame", 45030, null, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(744, 25)),
		ENTER_THE_PORTAL_ZONE(Difficulty.MEDIUM, "Enter the portal Zone", 45031, null, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(744, 25)),
		EXCHANGE_SALVAGE_FOR_DR(Difficulty.MEDIUM, "Exchange Salvage for DR", 45032, null, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(744, 25)),
		COMPLETE_A_HARD_SLAYER_TASK(Difficulty.MEDIUM, "Complete A Hard Slayer Task", 45033, null, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(744, 25)),
		FORGE_A_ITEM(Difficulty.MEDIUM, "Forge an item", 45034, null, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(744, 25)),
		DEAL_MEDIUM_DAMAGE_USING_MELEE(Difficulty.MEDIUM, "Deal 100M Melee Damage", 45035, new int[]{18, 100000000}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(744, 25)),
		DEAL_MEDIUM_DAMAGE_USING_RANGED(Difficulty.MEDIUM, "Deal 100M Ranged Damage", 45036, new int[]{19, 100000000}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(744, 25)),
		DEAL_MEDIUM_DAMAGE_USING_MAGIC(Difficulty.MEDIUM, "Deal 100M Magic Damage", 45037, new int[]{20, 100000000}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(744, 25)),
		DEFEAT_THE_KING_BLACK_DRAGON(Difficulty.MEDIUM, "Defeat The King Black Dragon", 45038, null, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(744, 25)),
		DEFEAT_THE_CORPOREAL_BEAST(Difficulty.HARD, "Defeat The Corporeal Beast", 45039, null, "", "", "", "", "", "", new Item(10835, 50), new Item(6199, 1), new Item(13727, 25)),

		KILL_NARUTO_500_TIMES(Difficulty.HARD, "Kill Naruto 500 Times", 45043, new int[]{21, 500}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(744, 25)),
		OPEN_250_MYSTERY_BOXES(Difficulty.HARD, "Open 250 Mystery boxes", 45044, new int[]{22, 250}, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(13727, 25)),
		CLAIM_100_BONDS(Difficulty.HARD, "Claim 100 Bonds", 45045, new int[]{23, 100}, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(13727, 25)),
		COMPLETE_DBZ_MINIGAME_50_TIMES(Difficulty.HARD, "Complete 50 DBZ Minigames", 45046, new int[]{24, 50}, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(13727, 25)),
		OPEN_10_SEPHIROTH_CHESTS(Difficulty.HARD, "Open 10 Sephiroth Chests", 45047, new int[]{25, 10}, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(13727, 25)),
		OPEN_5_BLOODSLAYER_CHESTS(Difficulty.HARD, "Open 5 Bloodslayer Chests", 45048, new int[]{27, 5}, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(13727, 25)),
		ENTER_THE_PORTAL_ZONE_10_TIMES(Difficulty.HARD, "Enter Portals 10 times", 45049, new int[]{26, 10}, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(13727, 25)),
		DRINK_100_DELUGE_POTIONS(Difficulty.HARD, "Drink 100 Deluge Potions", 45050, new int[]{38, 100}, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(13727, 25)),
		COMPLETE_AN_BLOODSLAYER_TASK(Difficulty.HARD, "Complete A Bloodslayer Task", 45051, null, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(13727, 25)),
		FORGE_5_ITEMS(Difficulty.HARD, "Forge 5 Items", 45052, new int[]{37, 5}, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(13727, 250)),
		DEAL_HARD_DAMAGE_USING_MELEE(Difficulty.HARD, "Deal 200M Melee Damage", 45053, new int[]{39, 200000000}, "", "", "", "", "", "", new Item(10835, 25), new Item(15373, 1), new Item(13727, 25)),
		DEAL_HARD_DAMAGE_USING_RANGED(Difficulty.HARD, "Deal 200M Ranged Damage", 45054, new int[]{40, 200000000}, "", "", "", "", "", "", new Item(10835, 25), new Item(15373, 1), new Item(13727, 25)),
		DEAL_HARD_DAMAGE_USING_MAGIC(Difficulty.HARD, "Deal 200M Magic Damage", 45055, new int[]{41, 200000000}, "", "", "", "", "", "", new Item(10835, 25), new Item(15373, 1), new Item(13727, 25)),
		DEFEAT_NEX(Difficulty.HARD, "Defeat Nex", 45056, null, "", "", "", "", "", "", new Item(10835, 5000), new Item(19935, 1), new Item(13727, 100)),

		COMPLETE_ALL_HARD_TASKS(Difficulty.ELITE, "Complete All Hard Tasks", 45060, new int[]{42, 32}, "", "", "", "", "", "", new Item(10835, 2000), new Item(15373, 5), new Item(19936, 2)),
		CUT_AN_ONYX_STONE(Difficulty.ELITE, "Cut An Onyx Stone", 45061, null, "", "", "", "", "", "", new Item(10835, 2), new Item(6199, 1), new Item(989, 5)),
		DEFEAT_10000_MONSTERS(Difficulty.ELITE, "Defeat 10,000 Monsters", 45062, new int[]{43, 10000}, "", "", "", "", "", "", new Item(10835, 5000), new Item(15373, 4), new Item(16455, 1)),
		DEFEAT_500_BOSSES(Difficulty.ELITE, "Defeat 500 Boss Monsters", 45063, new int[]{44, 500}, "", "", "", "", "", "", new Item(10835, 1000), new Item(6199, 5), new Item(13727, 25)),
		OPEN_1000_MYSTERY_BOXES(Difficulty.ELITE, "Open 1000 Mystery Boxes", 45064, new int[]{46, 1000}, "", "", "", "", "", "", new Item(10835, 2000), new Item(15373, 5), new Item(19936, 2)),
		OPEN_100_SEPHIROTH_CHESTS(Difficulty.ELITE, "Open 100 Sephiroth Chests", 45065, new int[]{46, 100}, "", "", "", "", "", "", new Item(10835, 2000), new Item(15373, 5), new Item(19936, 2)),
		;
		
		AchievementData(Difficulty difficulty, String interfaceLine, int interfaceFrame, int[] progressData,
				String desc1, String desc2, String desc3, String desc4, String desc5, String desc6, Item Rewards,
				Item Rewards1, Item Rewards2) {
			this.difficulty = difficulty;
			this.interfaceLine = interfaceLine;
			this.interfaceFrame = interfaceFrame;
			this.progressData = progressData;
			this.desc1 = desc1;
			this.desc2 = desc2;
			this.desc3 = desc3;
			this.desc4 = desc4;
			this.desc5 = desc5;
			this.desc6 = desc6;
			this.Rewards = Rewards;
			this.Rewards1 = Rewards1;
			this.Rewards2 = Rewards2;
		}

		private Difficulty difficulty;
		private String interfaceLine;
		private int interfaceFrame;
		private int[] progressData;
		private String desc1;
		private String desc2;
		private String desc3;
		private String desc4;
		private String desc5;
		private String desc6;
		private Item Rewards;
		private Item Rewards1;
		private Item Rewards2;

		public Difficulty getDifficulty() {
			return difficulty;
		}
	}

	public enum Difficulty {
		BEGINNER, EASY, MEDIUM, HARD, ELITE;
	}

	public static boolean handleButton(Player player, int button) {
		if (!(button >= -23731 && button <= -20416)) {
			return false;
		}
		int index = -1;
		if (button >= -20530 && button <= -20503) {
			index = 20530 + button;
		} else if (button >= -20499 && button <= -20469) {
			index = 28 + 20499 + button;
		} else if (button >= -20466 && button <= -20437) {
			index = 56 + 20466 + button;
		} else if (button >= -20432 && button <= -20430) {
			index = 86 + 20432 + button;
		}  else if (button >= -20428 && button <= -20427) {
			index = 85 + 20432 + button;
		}  else if (button >= -20426 && button <= -20425) {
			index = 87 + 20430 + button;
		}  else if (button >= -20426 && button <= -20425) {
			index = 88 + 20428 + button;
		}  else if (button >= -20424 && button <= -20423) {
			index = 89 + 20426 + button;
		}  else if (button >= -20426 && button <= -20425) {
			index = 90 + 20424 + button;
		} else if (button >= -23731 && button <= -23672) {
			if (player.difficulty == Difficulty.EASY) {
				index = 23731 + button;
			} else if (player.difficulty == Difficulty.MEDIUM) {
				index = 28 + 23731 + button;
			} else if (player.difficulty == Difficulty.HARD) {
				index = 56 + 23731 + button;
			} else if (player.difficulty == Difficulty.ELITE) {
				index = 86 + 23731 + button;
			}
		}

		if (index >= 0 && index < AchievementData.values().length) {
			AchievementData achievement = AchievementData.values()[index];
			openInterface(player, achievement);
		}
		return true;
	}

	public static void updateInterface(Player player) {
		for (AchievementData achievement : AchievementData.values()) {
			boolean completed = player.getAchievementAttributes().getCompletion()[achievement.ordinal()];
			boolean progress = achievement.progressData != null
					&& player.getAchievementAttributes().getProgress()[achievement.progressData[0]] > 0;
			player.getPacketSender().sendString(-1,
					(completed ? "@gre@" : progress ? "@yel@" : "@red@") + achievement.interfaceLine);
		}
		player.getPacketSender().sendString(45001, "Achievements: " + player.getPointsHandler().getAchievementPoints()
				+ "/" + AchievementData.values().length);
	}

	public static void openInterface(Player player, AchievementData task) {
		int id = 41805;
		int id1 = 41805;
		if (task.difficulty == Difficulty.MEDIUM) {
			id -= 28;
		}
		if (task.difficulty == Difficulty.HARD) {
			id -= 56;
		}
		if (task.difficulty == Difficulty.ELITE) {
			id -= 86;
		}

		player.difficulty = task.difficulty;
		for (AchievementData achievement : AchievementData.values()) {
			player.getPacketSender().sendString(id1++, "");
			boolean completed = player.getAchievementAttributes().getCompletion()[achievement.ordinal()];
			boolean progress = achievement.progressData != null
					&& player.getAchievementAttributes().getProgress()[achievement.progressData[0]] > 0;
			if (achievement.difficulty == task.difficulty) {
				player.getPacketSender().sendString(id,
						(completed ? "@gre@" : progress ? "@yel@" : "@red@") + achievement.interfaceLine);
			}
			id++;
		}
		player.getPacketSender().sendString(41764, "Achievements");

		player.getPacketSender().sendString(41769, "Easy");
		player.getPacketSender().sendString(41770, "Medium");
		player.getPacketSender().sendString(41771, "Hard");
		player.getPacketSender().sendString(41772, "Elite");

		player.getPacketSender().sendString(41773, "" + task.difficulty);

		player.getPacketSender().sendString(41774, "" + task.interfaceLine);

		int points = 0;
		if (task.difficulty == Difficulty.EASY) {
			points = 3;
		}else if (task.difficulty == Difficulty.MEDIUM) {
			points = 10;
		}else if (task.difficulty == Difficulty.HARD) {
			points = 15;
		}else if (task.difficulty == Difficulty.ELITE) {
			points = 150;
		}
		player.getPacketSender().sendString(41881, points + " Pwnlite points");

		player.getPacketSender().sendString(41776, "" + task.desc1);
		player.getPacketSender().sendString(41777, "" + task.desc2);
		player.getPacketSender().sendString(41778, "" + task.desc3);
		player.getPacketSender().sendString(41779, "" + task.desc4);
		player.getPacketSender().sendString(41780, "" + task.desc5);
		player.getPacketSender().sendString(41781, "" + task.desc6);

		player.getPA().sendItemOnInterface(41901, task.Rewards.getId(), task.Rewards.getAmount());
		player.getPA().sendItemOnInterface(41902, task.Rewards1.getId(), task.Rewards1.getAmount());
		player.getPA().sendItemOnInterface(41903, task.Rewards2.getId(), task.Rewards2.getAmount());

		if (player.getAchievementAttributes().getCompletion()[task.ordinal()]) {
			player.getPacketSender().sendString(41775, "Progress: @gre@100% (1/1)");
		} else if (task.progressData == null) {
			player.getPacketSender().sendString(41775, "Progress: @gre@0% (0/0)");

		} else {
			int progressTask = player.getAchievementAttributes().getProgress()[task.progressData[0]];
			int progressTotal = task.progressData[1];
			long percent = (progressTask / progressTotal) * 100;
			if (progressTask == 0) {
				player.getPacketSender().sendString(41775,
						"Progress: @gre@0 (" + Misc.insertCommasToNumber("" + progressTask) + "/"
								+ Misc.insertCommasToNumber("" + progressTotal) + ")");
			} else if (progressTask != progressTotal) {
				player.getPacketSender().sendString(41775,
						"Progress: @gre@" + percent + " (" + Misc.insertCommasToNumber("" + progressTask) + "/"
								+ Misc.insertCommasToNumber("" + progressTotal) + ")");
			}
		}

		player.getPA().sendInterface(41750);
	}

	public static void setPoints(Player player) {
		int points = 0;
		for (AchievementData achievement : AchievementData.values()) {
			if (player.getAchievementAttributes().getCompletion()[achievement.ordinal()]) {
				points++;
			}
		}
		player.getPointsHandler().setAchievementPoints(points, false);
	}

	public static void doProgress(Player player, AchievementData achievement) {
		doProgress(player, achievement, 1);
	}

	public static void doProgress(Player player, AchievementData achievement, int amt) {
		if (player.getAchievementAttributes().getCompletion()[achievement.ordinal()])
			return;
		if (achievement.progressData != null) {
			int progressIndex = achievement.progressData[0];
			int amountNeeded = achievement.progressData[1];
			int previousDone = player.getAchievementAttributes().getProgress()[progressIndex];
			if ((previousDone + amt) < amountNeeded) {
				player.getAchievementAttributes().getProgress()[progressIndex] = previousDone + amt;
				if (previousDone == 0)
					player.getPacketSender().sendString(-1,
							"@yel@" + achievement.interfaceLine);
			} else {
				finishAchievement(player, achievement);
			}
		}
	}

	public static void finishAchievement(Player player, AchievementData achievement) {
		if (player.getAchievementAttributes().getCompletion()[achievement.ordinal()])
			return;
		player.getAchievementAttributes().getCompletion()[achievement.ordinal()] = true;
		player.getPacketSender().sendString(-1, ("@gre@") + achievement.interfaceLine)
				.sendMessage("<img=31> <shad=e77700>You have completed the achievement</shad>: "
						+ Misc.formatText(achievement.toString().toLowerCase() + "."))
				.sendString(45001, "Achievements: " + player.getPointsHandler().getAchievementPoints() + "/"
						+ AchievementData.values().length);
		player.getInventory().add(achievement.Rewards);
		player.getInventory().add(achievement.Rewards1);
		player.getInventory().add(achievement.Rewards2);
		//player.getBank(player.getCurrentBankTab()).add(achievement.Rewards);
		//player.getBank(player.getCurrentBankTab()).add(achievement.Rewards1);
		//player.getBank(player.getCurrentBankTab()).add(achievement.Rewards2);
		player.getPointsHandler().setAchievementPoints(1, true);
		
		int points = 0;
		if (achievement.difficulty == Difficulty.EASY) {
			points = 3;
		}else if (achievement.difficulty == Difficulty.MEDIUM) {
			points = 10;
		}else if (achievement.difficulty == Difficulty.HARD) {
			points = 15;
		}else if (achievement.difficulty == Difficulty.ELITE) {
			points = 150;
		}
		player.setRuneUnityPoints(player.getRuneUnityPoints() + points);
	}

	public static class AchievementAttributes {

		public AchievementAttributes() {
			
		}

		/** ACHIEVEMENTS **/
		private boolean[] completed = new boolean[AchievementData.values().length];
		private int[] progress = new int[55];

		public boolean[] getCompletion() {
			return completed;
		}

		public void setCompletion(int index, boolean value) {
			this.completed[index] = value;
		}

		public void setCompletion(boolean[] completed) {
			this.completed = completed;
		}

		public int[] getProgress() {
			return progress;
		}

		public void setProgress(int index, int value) {
			this.progress[index] = value;
		}

		public void setProgress(int[] progress) {
			this.progress = progress;
		}

		/** MISC **/
		private int coinsGambled;
		private double totalLoyaltyPointsEarned;
		private boolean[] godsKilled = new boolean[5];

		public int getCoinsGambled() {
			return coinsGambled;
		}

		public void setCoinsGambled(int coinsGambled) {
			this.coinsGambled = coinsGambled;
		}

		public double getTotalLoyaltyPointsEarned() {
			return totalLoyaltyPointsEarned;
		}

		public void incrementTotalLoyaltyPointsEarned(double totalLoyaltyPointsEarned) {
			this.totalLoyaltyPointsEarned += totalLoyaltyPointsEarned;
		}

		public boolean[] getGodsKilled() {
			return godsKilled;
		}

		public void setGodKilled(int index, boolean godKilled) {
			this.godsKilled[index] = godKilled;
		}

		public void setGodsKilled(boolean[] b) {
			this.godsKilled = b;
		}
	}
}
