package com.arlania.world.content;

import com.arlania.model.Skill;
import com.arlania.util.Misc;
import com.arlania.world.content.bosses.General;
import com.arlania.world.content.bosses.TheHarambe;
import com.arlania.world.content.event.SpecialEvents;
import com.arlania.world.content.minigames.impl.Nomad;
import com.arlania.world.content.minigames.impl.RecipeForDisaster;
import com.arlania.world.entity.impl.player.Player;
import com.arlania.model.definitions.DropUtils;
import com.arlania.model.definitions.NPCDrops;

public class PlayerPanel {
	
	
	//#1
		public static final String LINE_START = "            ";
		public static final String LINE5_START = " ";
		public static final String LINE1_START = "                ";
		public static final String LINE3_START = "              ";
		public static final String LINE2_START = " ";
		//#2
		public static final String LINE6_START = " ";
		public static final String MIDDLE_START = " ";
		public static final String MIDDLE2_START = " ";
		//#3
		public static final String LINE7_START = "          ";
		public static final String LINE9_START = " ";
		public static final String MIDDLE3_START = "           ";
		public static final String MIDDLE8_START = " ";
		public static final String MIDDLE7_START = "              ";
		public static final String MIDDLE6_START = " ";
		//#4
		public static final String MIDDLE9_START = " ";
		public static final String MIDDLE10_START = "            ";
		public static final String MIDDLE11_START = " ";
		public static final String MIDDLE12_START = "          ";
		//#5
		public static final String MIDDLE13_START = " ";
		public static final String MIDDLE14_START = " ";
		
	//#quest tab1
		public static final String Quest_START = "               ";
		public static final String Quest2_START = "                ";
		public static final String Quest3_START = "                 ";
		public static final String Quest4_START = "                     ";
		public static final String Quest5_START = "                   ";
		public static final String Quest6_START = "                  ";
		//#quest tab 1 under writting
		//#quest tab1
			public static final String QuestU_START = "                    ";
			public static final String QuestU2_START = "                      ";
			public static final String QuestU3_START = "-                    ";
			public static final String QuestU4_START = "                    ";
			public static final String QuestU5_START = "                       ";
			public static final String QuestU6_START = "                  ";

	private static int FIRST_STRING = 39159;
	private static int LAST_STRING = 39214;


	public static void handleSwitch(Player player, int index, boolean fromCurrent) {
		if (!fromCurrent) {
			resetStrings(player);
		}
		player.currentPlayerPanelIndex = index;
		switch (index) {
		case 1:
			refreshPanel(player); // first tab, cba rename just yet.
			break;

		case 2:
			sendSecondTab(player);
			break;
		case 3:
			sendThirdTab(player);
			break;
		case 4:
			sendForthTab(player);
			break;
		}
	}

	public static void refreshCurrentTab(Player player) {
		handleSwitch(player, player.currentPlayerPanelIndex, true);
	}

	public static void refreshPanel(Player player) {
		player.getPacketSender().sendFrame126("- Discord -", 52531);
		player.getPacketSender().sendFrame126("- Donate -", 52532);
		player.getPacketSender().sendFrame126("Comming soon", 52533);
		player.getPacketSender().sendFrame126("Comming soon", 52534);
		player.getPacketSender().sendFrame126("Comming soon", 52535);

		if (player.currentPlayerPanelIndex != 1) { // now it would update the other tab, if this is not the current tab
			refreshCurrentTab(player);
			return;
		}
		String[] Messages = new String[] { "  ", "<img=461> @or1@Player Data", "",
			 	 "@or2@Username: @or1@"+player.getUsername(),
			 	 "@or2@Rank: @or1@"+player.getRights().toString(),
				 "@or2@Time Played: @or1@" +Misc.getHoursPlayed((player.getTotalPlayTime() + player.getRecordedLogin().elapsed())),
			 	 "@or2@Donated: @or1@"+player.getAmountDonated(),
			 	 "@or2@Total NPC Kills: @or1@"+player.getNpcKills(),
			 	 "@or2@Total MOB Points: @or1@"+player.getBossPoints(),
			 	"@or2@Current Droprate: @or1@"+DropUtils.drBonus(player, true),
			 	 "@or2@Server Time: @or1@"+Misc.getCurrentServerTime(),
			 	 "@or2@Exp Lock: @or1@"+(player.experienceLocked() ? "@red@Locked" : "@gre@Unlocked"),
			 	 "","",
			 	 "@or1@NPC Kill Data:",
			 	 "@or2@Hercules Kills: @or1@"+player.getHerculesKC(),
			 	 "@or2@Crash Kills: @or1@"+player.getLucarioKC(),
			 	 "@or2@Charizard Kills: @or1@"+player.getCharizardKC(),
			 	 "@or2@Jini Kills: @or1@"+player.getDefendersKC(),
			 	 "@or2@Godzilla Kills: @or1@"+player.getGodzillaKC(),
			 	 "@or2@Bloated Kills: @or1@"+player.getCerbKC(),
			 	 "@or2@Hades Kills: @or1@"+player.getZeusKC(),
			 	 "@or2@Vader Kills: @or1@"+player.getValorKC(),
			 	 "@or2@Satan Kills: @or1@"+player.getHwKC(),
			 	 "@or2@Rick Kills: @or1@"+player.getDzanthKC(),
			 	 "@or2@Kong Kills: @or1@"+player.getKongKC(),
			 	 "@or2@Corp Kills: @or1@"+player.getCorpKC(),
			 	 "@or2@Lucid Kills: @or1@"+player.getLucidKC(),
			 	 "@or2@Hulk Kills: @or1@"+player.getHulkKC(),
			 	 "@or2@Dark Wizard Kills: @or1@"+player.getDarkblueKC(),
			 	 "@or2@Heated Pyro Kills: @or1@"+player.getPyroKC(),
			 	 "@or2@Purple Wyrm Kills: @or1@"+player.getWyrmKC(),
			 	 "@or2@Trinity Kills: @or1@"+player.getTrinityKC(),
			 	 "@or2@Cloud Kills: @or1@"+player.getCloudKC(),
			 	 "@or2@Herbal Rouge Kills: @or1@"+player.getHerbalKC(),
			 	 "@or2@Exoden Kills: @or1@"+player.getExodenKC(),
			 	 "@or2@Supreme Nex Kills: @or1@"+player.getSupremeKC(),
			 	 "@or2@Stormbreaker Kills: @or1@"+player.getBreakerKC(),
			 	 "@or2@Apollo Ranger Kills: @or1@"+player.getApolloKC(),
			 	 "@or2@Noxious troll Kills: @or1@"+player.getNoxKC(),
			 	 "@or2@Azazel beast Kills: @or1@"+player.getAzazelKC(),
			 	 "@or2@Ravana Kills: @or1@"+player.getRavanaKC(),
			 	 "@or2@Lumin warrior Kills: @or1@"+player.getLuminKC(),
			 	 "@or2@Hellhound Kills: @or1@"+player.getCustomhKC(),
			 	 "@or2@Razorspawn Kills: @or1@"+player.getRazorKC(),
			 	 "@or2@Dreamflow Kills: @or1@"+player.getDreamflowKC(),
			 	 "@or2@Khione Kills: @or1@"+player.getKhioneKC(),
			 	 "@or2@Sable beast Kills: @or1@"+player.getSableKC(),
			 	 "@or2@Demogorgon Kills: @or1@"+player.getDemoKC(),
			 	 "@or2@Yoshi Kills: @or1@"+player.getYoshiKC(),
			 	 "@or2@Avatar Kills: @or1@"+player.getAvatarKC(),
			 	 "@or2@Lili Kills: @or1@"+player.getLiliKC(),
			 	 "@or2@Obito Kills: @or1@"+player.getObitoKC(),
			 	 "@or2@Uru Kills: @or1@"+player.getUruKC(),
			 	 "@or2@Ahri Kills: @or1@"+player.getKumihoKC(),
			 	 "@or2@MysteryMan Kills: @or1@"+player.getMysteryKC(),
		};
		for (int i = 0; i < Messages.length; i++) {
			//System.out.println("i "+i+" FIRST_STRING "+FIRST_STRING+" LAST_STRING "+LAST_STRING);
			if (i + FIRST_STRING > LAST_STRING) {
				System.out.println("1PlayerPanel(" + player.getUsername() + "): " + i + " is larger than max string: "
				+ LAST_STRING + ". Breaking.");
				break;
			}

			player.getPacketSender().sendString(i + FIRST_STRING, Messages[i]);

		}

	}

	private static void sendSecondTab(Player player) {

		String[] Messages = new String[] { "  ", "<img=464>  @or2@World Events", "",
				"@or2@Evil Tree: @or1@"+(EvilTrees.getLocation() != null ? EvilTrees.getLocation().playerPanelFrame : "N/A"),
			 	 "",
			 "@or2@Well of Goodwill:@or1@" +(WellOfGoodwill.isActive() ? WellOfGoodwill.getMinutesRemaining() + " mins" : "N/A"),
		 	 "",
			 "@or2@Crashed Star:@or1@" +(ShootingStar.getLocation() != null ?ShootingStar.getLocation().playerPanelFrame : "N/A"),
		 	 "",
		 "@or2@Daily Bonus:@gre@" +SpecialEvents.getSpecialDay(),
		 
		};

		for (int i = 0; i < Messages.length; i++) {
			if (i + FIRST_STRING > LAST_STRING) {
				System.out.println("2PlayerPanel(" + player.getUsername() + "): " + i + " is larger than max string: "
						+ LAST_STRING + ". Breaking.");
				break;
			}

			player.getPacketSender().sendString(i + FIRST_STRING, Messages[i]);

		}

	}

	private static void sendThirdTab(Player player) {

		String[] Messages = new String[] { "  ", "@or2@Points @or1@& @or2@Statistics", "",
				
				"@or2@Loyalty Points: @or1@"+player.getPointsHandler().getLoyaltyPoints(),

			 "@or2@Custom Well Donations: @or1@"+player.getCustomDonations(),

	 "@or2@Prestige Points: @or1@"+player.getPointsHandler().getPrestigePoints(),

	 "@or2@Trivia Points: @or1@"+player.getPointsHandler().getTriviaPoints(),

 "@or2@Voting Points: @or1@"+player.getPointsHandler().getVotingPoints(),

 "@or2@Donation Points:  @or1@"+player.getPointsHandler().getDonationPoints(),

 "@or2@Raid Points: @or1@"+player.getPointsHandler().getRaidPoints(),
 
 "@or2@Skilling Points: @or1@"+player.getPointsHandler().getSkillPoints(),

 "@or2@Pest control points: @or1@"+player.getPointsHandler().getPestcontrolpoints(),

 "@or2@Dung. Tokens: @or1@"+player.getPointsHandler().getDungeoneeringTokens(),

"@or2@MOB Points: @or1@"+player.getBossPoints(),

"@or2@Custom MOB Points: @or1@"+player.getCustomPoints(),

	 "@or2@Slayer Points: @or1@"+player.getPointsHandler().getSlayerPoints(),
	 	 
	  "@or2@Bravek Tasks Completed: @or1@"+player.getBravekTasksCompleted(),

	 "@or2@Bloodslayer Points: @or1@"+player.getPointsHandler().getBloodSlayerPoints(),
	  
		};

		for (int i = 0; i < Messages.length; i++) {
			if (i + FIRST_STRING > LAST_STRING) {
				System.out.println("3PlayerPanel(" + player.getUsername() + "): " + i + " is larger than max string: "
						+ LAST_STRING + ". Breaking.");
				break;
			}

			player.getPacketSender().sendString(i + FIRST_STRING, Messages[i]);

		}

	}

	private static void sendForthTab(Player player) {

		String[] Messages = new String[] { "", "<img=481> @or2@Slayer Data <img=481>","",

				"@or2@Slayer Master: @or1@"+player.getSlayer().getSlayerMaster(),
		

			 "@or2@Duo Partner: @or1@"+ player.getSlayer().getDuoPartner(),
			
			"@or2@Slayer Task: @or1@"+player.getSlayer().getSlayerTask(),
				
		 "@or2@Task Amount: @or1@"	+player.getSlayer().getAmountToSlay(),
		 
		 "@or2@Task Streak: @or1@"+player.getSlayer().getTaskStreak(),
		 "",
			"@or2@Bloodslayer Task: @or1@"+player.getBloodSlayer().getBloodSlayerTask(),
			
		 "@or2@BloodTask Amount: @or1@"	+player.getBloodSlayer().getAmountToSlay(),
		 
		 "@or2@BloodTasks Complete: @or1@"+player.getBloodSlayer().getTaskStreak(),


		};

		for (int i = 0; i < Messages.length; i++) {
			if (i + FIRST_STRING > LAST_STRING) {
				System.out.println("4PlayerPanel(" + player.getUsername() + "): " + i + " is larger than max string: "
						+ LAST_STRING + ". Breaking.");
				break;
			}

			player.getPacketSender().sendString(i + FIRST_STRING, Messages[i]);

		}

	}

	private static void resetStrings(Player player) {
		for (int i = FIRST_STRING; i < LAST_STRING; i++) {
			player.getPacketSender().sendString(i, "");
		}
	}
	/***
	 * 
	 * 
	 * 
	 * if(player.currentPlayerPanelIndex != 1) { // now it would update the other
	 * tab, if this is not the current tab refreshCurrentTab(player); return; }
	 * 
	 * String[] Messages = new String[] { "@red@ - @whi@ World Overview",
	 * "@or2@Players Online: @or2@[ @yel@" + (int) (World.getPlayers().size()) +
	 * "@or2@ ]", (ShootingStar.CRASHED_STAR == null ? "@or2@Crashed
	 * Star: @red@Cleared" : "@or2@Crashed Star: @gre@" +
	 * ShootingStar.CRASHED_STAR.getStarLocation().playerPanelFrame),
	 * (EvilTree.SPAWNED_TREE == null ? "@or2@Evil Tree: @red@Cleared" : "@or2@Evil
	 * Tree: @gre@" + EvilTree.SPAWNED_TREE.getTreeLocation().playerPanelFrame),
	 * (Wildywyrm.wyrmAlive ? "@or2@WildyWyrm: @gre@" +
	 * Wildywyrm.getPlayerPanelHint() : "@or2@WildyWyrm: @red@Dead"), //
	 * (Abyssector.wyrmAlive ? "@or2@Abyssector:
	 * // @gre@"+Abyssector.getPlayerPanelHint() : "@or2@Abyssector: @red@Dead"),
	 * (WellOfGoodwill.isActive() ? "@or2@Well of Goodwill: @gre@On" : "@or2@Well of
	 * Goodwill: @red@Off"), (doMotivote.getVoteCount() <= 20 ? "@or2@Vote
	 * Boss: @yel@" + doMotivote.getVoteCount() + "/20" : "@or2@Vote
	 * Boss: @gre@::Vboss"), // "Vote Boss" + doMotivote.getVoteCount()+"@bla@
	 * votes.", "@or3@ - @whi@ Account Information", // "@yel@Difficulty:
	 * // @whi@"+Misc.capitalizeString(player.getDifficulty().toString().toLowerCase()),
	 * "@or2@Mode: @yel@" +
	 * Misc.capitalizeString(player.getGameMode().toString().toLowerCase().replace("_",
	 * " ")), "@or2@Claimed: @yel@$" + player.getAmountDonated(), "@or2@Time
	 * played: @yel@" + Misc.getTimePlayed((player.getTotalPlayTime() +
	 * player.getRecordedLogin().elapsed())), "@or2@Current Double XP skill:@yel@ "
	 * + StringUtils.capitalizeFirst(DoubleXPSkillEvent.currentSkill.toString()),
	 * "@or3@ - @whi@ Statistics", "@or2@NPC kill Count: @yel@ " +
	 * player.getPointsHandler().getNPCKILLCount(), "@red@MOB Points: @yel@ " +
	 * player.getPointsHandler().getBossPoints(), "@or2@Event Points: @yel@ " +
	 * player.getPointsHandler().getEventPoints(), "@or2@Prestige Points: @yel@" +
	 * player.getPointsHandler().getPrestigePoints(), "@or2@Total Prestige: @yel@" +
	 * player.getPointsHandler().getTotalPrestiges(), "@or2@Commendations: @yel@ " +
	 * player.getPointsHandler().getCommendations(), "@or2@Loyalty Points: @yel@" +
	 * (int) player.getPointsHandler().getLoyaltyPoints(), "@or2@Dung. Tokens: @yel@
	 * " + player.getPointsHandler().getDungeoneeringTokens(), "@or2@Voting
	 * Points: @yel@ " + player.getPointsHandler().getVotingPoints(), "@or2@Slayer
	 * Points: @yel@" + player.getPointsHandler().getSlayerPoints(), "@or2@Penguin
	 * Multiplier: +@yel@ " + player.getPointsHandler().getSHILLINGRate() +
	 * "%", "@or2@Barrows Points: @yel@" +
	 * player.getPointsHandler().getBarrowsPoints(), "@or2@Member Points: @yel@" +
	 * player.getPointsHandler().getMemberPoints(), "@or2@Pk Points: @yel@" +
	 * player.getPointsHandler().getPkPoints(), "@or2@Wilderness Killstreak: @yel@"
	 * + player.getPlayerKillingAttributes().getPlayerKillStreak(), "@or2@Wilderness
	 * Kills: @yel@" + player.getPlayerKillingAttributes().getPlayerKills(),
	 * "@or2@Wilderness Deaths: @yel@" +
	 * player.getPlayerKillingAttributes().getPlayerDeaths(), "@or2@Arena
	 * Victories: @yel@" + player.getDueling().arenaStats[0], "@or2@Arena
	 * Points: @yel@" + player.getDueling().arenaStats[1], "@or2@Imp Kill
	 * Count: @yel@ " + player.getPointsHandler().getSPAWNKILLCount(), "@or2@Lord
	 * Kill Count: @yel@ " + player.getPointsHandler().getLORDKILLCount(),
	 * "@or2@Demon Kill Count: @yel@ " +
	 * player.getPointsHandler().getDEMONKILLCount(), "@or2@Dragon Kill Count: @yel@
	 * " + player.getPointsHandler().getDRAGONKILLCount(), "@or2@Beast Kill
	 * Count: @yel@ " + player.getPointsHandler().getBEASTKILLCount(), "@or2@King
	 * Kill Count: @yel@ " + player.getPointsHandler().getKINGKILLCount(),
	 * "@or2@Avatar Kill Count: @yel@ " +
	 * player.getPointsHandler().getAVATARKILLCount(), "@or2@Angel Kill Count: @yel@
	 * " + player.getPointsHandler().getANGELKILLCount(), "@or2@Lucien Kill
	 * Count: @yel@ " + player.getPointsHandler().getLUCIENKILLCount(),
	 * "@or2@Hercules Kill Count: @yel@ " +
	 * player.getPointsHandler().getHERCULESKILLCount(), "@or2@Satan Kill
	 * Count: @yel@ " + player.getPointsHandler().getSATANKILLCount(), "@or2@Zeus
	 * Kill Count: @yel@ " + player.getPointsHandler().getZEUSKILLCount(), "",
	 * "@or3@ - @whi@ Slayer", // "@or2@Open Kills Tracker", // "@or2@Open Drop
	 * Log", "@or2@Master: @yel@" + Misc
	 * .formatText(player.getSlayer().getSlayerMaster().toString().toLowerCase().replaceAll("_",
	 * " ")), (player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK ?
	 * "@or2@Task: @yel@" + Misc.formatText(
	 * player.getSlayer().getSlayerTask().toString().toLowerCase().replaceAll("_", "
	 * ")) : "@or2@Task: @yel@" + Misc.formatText(
	 * player.getSlayer().getSlayerTask().toString().toLowerCase().replaceAll("_", "
	 * ")) + "s"), "@or2@Task Streak: @yel@" + player.getSlayer().getTaskStreak(),
	 * "@or2@Task Amount: @yel@" + player.getSlayer().getAmountToSlay(),
	 * (player.getSlayer().getDuoPartner() != null ? "@or2@Duo Partner: @yel@" +
	 * player.getSlayer().getDuoPartner() : "@or2@Duo Partner: @yel@N/A"),
	 * 
	 * /* "@yel@lre", "@red@red", "@dre@dre", "@yel@yel", "@whi@whi", "blu",
	 * "cya", "@mag@mag", "@bla@bla", "@gre@gre", "@gr1@gr1", "@gr2@gr2",
	 * "@gr3@gr3", "@str@str", "@or1@or1", "@or2@or2", "@or3@or3",
	 */

}
