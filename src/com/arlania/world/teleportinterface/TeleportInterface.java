package com.arlania.world.teleportinterface;

import com.arlania.model.Position;
import com.arlania.model.definitions.NPCDrops;
import com.arlania.world.content.transportation.TeleportHandler;
import com.arlania.world.entity.impl.player.Player;

/**
 * @author Emerald
 * @since 8th July 2019
 */

public class TeleportInterface {

	private final static int CATEGORY_NAME_ID = 50508;

	public enum Bosses {
		// this is 1st field etc
		//new zone for ki, coords = 2794,3172
		NOOBS(50601, "Beginner Zone", "Multi Npc", "Drops Beginner Items", "@red@Awesome starting", "", "", 420,
				new int[] { 2759, 3174, 0 }),
		
		DEFILERS(50602, "Defilers", "Defilers@gre@(T2)", "Drops the Golden Minigun", "@red@HP:@gre@ 4500(4.5k)", "", "", 3767,
				new int[] { 2664, 3432, 0 }),
		
		
		FROSTDRAGONS(50603, "Frost Dragons", "Frost Dragons@gre@(T2)", "Drops Frost set", "@red@HP:@gre@ 7500(7.5k)", "", "", 51,
		new int[] { 2790, 4766, 0 }, 3000),
		
		SIRENIC(50604, "Sirenic Ogres", "Sirenic Ogres@gre@(T2)", "Drops Sirenic set", "@red@HP:@gre@ 8500(8.5k)", "", "", 2783,
		new int[] { 3184, 5471, 0 }, 3000),
		
		HERCULES(50605, "Hercules", "Hercules@gre@(T2)", "Drops Hercules set ", "@red@HP:@gre@ 10000(10k)", "", "", 17,
				new int[] { 2783, 4636, 0 }),
		
		LUCARIO(50606, "Lucario", "Lucario@yel@(T3)", "Drops the blessed set", "@red@KC REQ: 25 Hercules", "@red@HP:@gre@ 11500(11.5k)", "", 3263,
				new int[] { 2913, 4759, 0 },2000),
		
		HADES(50607, "Hades", "Hades@yel@(T3)", "Drops Misc items", "@red@KC REQ: 25 Lucario", "@red@HP:@gre@ 12500(12.5k)", "", 15, 
				new int[] { 2095, 3677, 0 }, 3000),
		
		CHARIZARD(50608, "Charizard", "Charizard@yel@(T3)", "Gather Claw Tokens", "For the Shop", "@red@HP:@gre@ 14000(14k)", "@red@KC REQ: 25 Hades", 1982, 
				new int[] { 2270, 3240, 0 }),
		
		HELLFIREWIZARDS(50609, "Hellfire Wizards", "Wizards@yel@(T4)", "Crazy wizards", "For the Shop", "@red@HP:@gre@ 125000(125k)", "@red@Thank Ki", 9231, 
				new int[] { 2524, 4777, 4 });
		
		
		Bosses(int textId, String name, String description1, String description2, String description3,
				String description4, String description5, int npcId, int[] teleportCords) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;

		}

		Bosses(int textId, String name, String description1, String description2, String description3,
				String description4, String description5, int npcId, int[] teleportCords, int adjustedZoom) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;
			this.adjustedZoom = adjustedZoom;

		}

		private int textId;
		private String name;
		private String description1, description2, description3, description4, description5;
		private int npcId;
		private int[] teleportCords;
		private int adjustedZoom;
	}

	public enum Monsters {


		
		
		JINIS(50601, "Jinis", "Jinis@yel@(T4)", "Protects Itself", "from Range!", "@red@KC REQ: 50 Charizards", "@red@HP:@gre@ 22000(22k)", 9994, 
				new int[] { 2724, 9821, 0 }, 2000),
		
		
		GODZILLA(50602, "Godzilla", "Godzilla@yel@(T4)", "This NPC drops", "the Rex set", "@red@KC REQ: 50 Defenders", "@red@HP:@gre@ 30000(30k)", 9932, 
				new int[] { 3374, 9807, 0 }),
		
		DEMONOLM(50603, "Demonic Olm", "Demonic Olm@yel@(T4)", "This NPC drops", "Misc Items", "@red@KC REQ: 75 Godzilla", "@red@HP:@gre@ 40000(40k)", 224, 
				new int[] { 2399, 3548, 0 }),
		
		BLOATED_INFERNAL(50604, "Bloated infernals", "Bloated infernals@yel@(T4)", "Drops Misc Gear", "@red@KC REQ: 100 Demonic Olm", "@red@HP:@gre@ 45000(45k)", "", 1999, 
				new int[] { 1240, 1247, 0 }, 3000),
		
		ZEUS(50605, "Zeus", "Zeus@yel@(T5)", "Drops Zeus set", "@red@KC REQ: 100 Cerberus", "@red@HP:@gre@ 50000(50k)", "", 16, 
				new int[] { 2065, 3663, 0 }, 3000),
		
		INFERNAL_BEAST(50606, "Infartico", "Infartico@red@(T5)", "@red@KC REQ: 125 Zeus", "@red@HP:@gre@ 55000(55k)", "", "", 9993,
				new int[] { 3479, 3087, 0 }, 3000),
		

		VALOR(50607, "Lord Valor", "Lord Valor@red@(T5)", "Hybrid NPC", "@red@KC REQ: 50 Infartico", "@red@HP:@gre@ 70000(70k)", "", 9277, 
		new int[] { 2780, 10000, 0 }),

		SKYROCKET(50608, "Hurricane Warriors", "Hurricane Warriors@red@(T5)", "@red@KC REQ: 200 Lord valors", "@red@HP:@gre@ 85000(85k)", "", "", 9944,
				new int[] { 2509, 4693, 0 }),
		
		TRIDENT(50609, "Dzanth", "Dzanth@red@(T6)", "@red@KC REQ: 250 Hurricane Warriors", "@red@HP:@gre@ 125000(125k)", "", "", 9273,
				new int[] { 2369, 4944, 0 }),
		
		HARAMBE(50610, "King Kong", "King Kong@red@(T6)", "Multi MASS BOSS", "@red@KC REQ: 300 Dzanth", "@red@HP:@gre@ 150000(150k)", "", 9903,
				new int[] { 2720, 9880, 0 }),
		
		PLANEFREEZER(50611, "PlaneFreezer", "Freezer@red@(T6)", "Freezer Cold Brrr", "@red@KC REQ: 1000 KingKong", "@red@HP:@gre@ 450000(450k)", "", 9939,
				new int[] { 2867, 2829, 4 });
		
		Monsters(int textId, String name, String description1, String description2, String description3,
				String description4, String description5, int npcId, int[] teleportCords) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;

		}

		Monsters(int textId, String name, String description1, String description2, String description3,
				String description4, String description5, int npcId, int[] teleportCords, int adjustedZoom) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;
			this.adjustedZoom = adjustedZoom;

		}

		private int textId;
		private String name;
		private String description1, description2, description3, description4, description5;
		private int npcId;
		private int[] teleportCords;
		private int adjustedZoom;
	}

	public enum Wilderness {
		
	

		
		CORPOREAL_BEAST(50601, "Corp Beast", "Corp Beast@red@(T6)", "@red@KC REQ: 300 King Kong", "@red@HP:@gre@ 145000(145k)", "", "", 8133, 
				new int[] { 2886, 4376, 0 }, 4000),
		
		LUCID(50602, "Lucid Dragons", "Lucid Dragons@red@(T7)", "@red@KC REQ: 500 Corp Beasts", "@red@HP:@gre@ 225000(225k)", "", "", 9247,
				new int[] { 2557, 4953, 0 }),
		
		HULK(50603, "Hulk", "Hulk@red@(T7)", "@red@KC REQ: 500 Lucid Dragons", "@red@HP:@gre@ 300000(300k)", "", "", 8493,
				new int[] { 3852, 5846, 0 }),
		WIZARDS(50604, "Dark Wizards", "Dark Wizards@bla@(T7)", "@red@KC REQ: 500 Hulk", "@red@HP:@gre@ 330000(330k)", "", "", 9203,
				new int[] { 2920, 9687, 0 }),
		
		PYROS(50605, "Heated Pyros", "Heated Pyros@bla@(T7)", "@red@KC REQ:750 Dark wizards", "@red@HP:@gre@ 400000(400k)", "", "", 172,
				new int[] { 3040, 4838, 0 }),
		
		FRIEZA(50606, "Frieza", "frieza@bla@(T8)", "Its a badass", "@red@KC REQ: 750 Pyros", "@red@HP:@gre@ 500000(500k)", "", 180,
				new int[] { 2325, 4586, 0 }, 2000),
		
		
		PURPLEFIRE_WYRM(50607, "Dark Purple Wyrm", "Dark Purple Wyrm@bla@(T8)", "Its a massboss", "@red@KC REQ: 750 Frieza", "@red@HP:@gre@ 425000(425k)", "", 9935,
				new int[] { 2325, 4586, 0 }, 2000),
		
		TRINITY(50608, "Trinity", "Trinity@bla@(T8)", "ITS a Massboss", "@red@KC REQ: 1500 Purple Wyrms", "@red@HP:@gre@ 500000(500k)", "", 170,
				new int[] { 2525, 4649, 0 }, 3000),
		CLOUD(50609, "Cloud", "Cloud@bla@(T8)", "@red@KC REQ: 1800 Trinity", "@red@HP:@gre@ 550000(550k)", "", "", 169, 
				new int[] { 2539, 5774, 0 }),
		
		HERBAL(50610, "Herbal Rogue", "Herbal Rogue@bla@(T9)", "@red@KC REQ: 1500 Cloud", "@red@HP:@gre@ 750000(750k)", "", "", 219, new int[] { 2737, 5087, 0 },
				4000),
		
		EXODEN(50611, "Exoden", "Exoden@bla@(T9)", "@red@KC REQ: 1500 Herbal Rogue", "@red@HP:@gre@ 1000000(1m)", "", "", 12239, new int[] { 2540, 10162, 0 },
				4000);

		Wilderness(int textId, String name, String description1, String description2, String description3,
				String description4, String description5, int npcId, int[] teleportCords) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;

		}

		Wilderness(int textId, String name, String description1, String description2, String description3,
				String description4, String description5, int npcId, int[] teleportCords, int adjustedZoom) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;
			this.adjustedZoom = adjustedZoom;

		}

		private int textId;
		private String name;
		private String description1, description2, description3, description4, description5;
		private int npcId;
		private int[] teleportCords;
		private int adjustedZoom;

	}

	public enum Zones {
	
		

		
		NEX(50601, "Supreme", "Supreme@bla@(T9)", "This npc drops Supreme set", "@red@KC REQ: 2000 Exoden", "@red@HP:@gre@ 1400000(1.4M)", "", 3154,
				new int[] { 2599, 4699, 0 },2000),
		
		STORMBREAKER(50602, "Storm Breaker", "Storm Breaker@bla@(T10)", "This drops Stormbreaker", "@red@KC REQ: 2000 Nex", "@red@HP:@gre@ 1750000(1.75M)", "", 527,
				new int[] { 3226, 2844, 0 },2000),
		APOLLO(50603, "Apollo Ranger", "Apollo Ranger@bla@(T10)", "This drops Apollo Set", "@red@KC REQ: 2000 Stormbreakers", "@red@HP:@gre@ 2250000(2.25M)", "", 1684,
				new int[] { 3178, 4237, 2 },2000),
		TROLL(50604, "Noxious Troll", "Noxious Troll@bla@(T10)", "This drops Noxious Set", "@red@KC REQ: 2000 Apollo Rangers", "@red@HP:@gre@ 2750000(2.75M)", "", 5957,
				new int[] { 3232, 3043, 0 },3000),
		AZAZEL(50605, "Azazel Beast", "Azazel Beast@bla@(T10)", "This drops Azazel Set", "@red@KC REQ: 2000 Noxious Trolls", "@red@HP:@gre@ 3000000(3M)", "", 5958,
				new int[] { 2468, 3372, 0 },3000),
		RAVANA(50606, "Ravana", "Ravana@bla@(T10)", "This drops Detrimental Set", "@red@KC REQ: 2100 Azazel Beasts", "@red@HP:@gre@ 3500000(3.5M)", "", 5959,
				new int[] { 3595, 3492, 0 },3000),
		LUMINITIOS(50607, "Luminitous Warriors", "Warriors@bla@(T10)", "This drops Luminitous Set", "@red@KC REQ: 2200 Ravanas", "@red@HP:@gre@ 4000000(4M)", "", 185,
				new int[] { 2525, 4776, 0 },3000),
		HELLHOUND(50608, "Custom Hellhounds", "Hell Hounds@bla@(T10)", "This drops BFG set", "@red@KC REQ: 1000 Luminitous warriors", "@red@HP:@gre@ 5000000(5M)", "", 6311,
				new int[] { 3176, 3029, 0 },3000),
		RAZORSPAWN(50609, "Razorspawn", "Razorspawn@bla@(T10)", "This drops Vanquisher set", "@red@KC REQ: 2500 Hellhounds", "@red@HP:@gre@ 5000000(5M)", "", 2907,
				new int[] { 2861, 9775, 0 },3000),
		DREAMFLOW(50610, "Dreamflow Assassin", "Dreamflow@bla@(T10)", "This drops Dreamflow set", "@red@KC REQ: 2500 Razorspawn", "@red@HP:@gre@ 7500000(7.5M)", "", 20,
				new int[] { 2719, 4837, 0 },3000);
		Zones(int textId, String name, String description1, String description2, String description3,
				String description4, String description5, int npcId, int[] teleportCords) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;

		}

		Zones(int textId, String name, String description1, String description2, String description3,
				String description4, String description5, int npcId, int[] teleportCords, int adjustedZoom) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;
			this.adjustedZoom = adjustedZoom;

		}

		private int textId;
		private String name;
		private String description1, description2, description3, description4, description5;
		private int npcId;
		private int[] teleportCords;
		private int adjustedZoom;
	}

	public enum Minigames {
		
		
		TOKEN_ZONE(50601, "Token Zone", "Earn Tokens", "to be used at", "the Token shop","", "", 729, 
				new int[] { 2526, 2841, 0 }, 4000),
		
		DBZ_ZONE(50602, "DBZ Zone", "This place drops DBZ Tokens!", "Multi Area", "Used for Awesome Armours", "Tier 7 armour", "", 100,
				new int[] { 2141, 5535, 3 }),
		
		//UPGRADED_CRYSTAL (50603, "Upgrade Crystal", "Upgrade Crystal", "Bigger and Badder","Upgraded Key","", "", 499, new int[] { 2847, 5146, 0}),
		
		DROPRATE_ZONE(50603, "Low Level Dr Items", "Low Level Dr Items", "Good Start","Have Fun","", "", 842, new int[] { 2832, 9525, 0}),
		
		ONSLAUGHT(50604, "NOOBS WORLD BOSS", "Onslaught(T3)", "Drops a bunch of items", "@red@MASS STARTER BOSS", "@red@HP:@gre@ 30000(30k)", "", 422,
				new int[] { 2414, 2856, 0 }, 3000),
		
		MEDBOSS(50605, "MED BOSS", "THIS NPC Spawns", "25 Minutes", "Multiple People ", "May be Required!", "", 4972,
				new int[] { 2409, 4679, 0 }),
		
		
		EVENTBOSS(50606, "WORLD BOSS", "THIS NPC Spawns", "Every Hour", "Multiple People ", "May be Required!", "", 9911,
				new int[] { 2409, 4679, 0 }),
		
		VOTEBOSS(50607, "VOTE WORLD BOSS", "THIS NPC Spawns", "Every 50 votes", "Multiple People ", "May be Required!", "", 1067,
				new int[] { 1956, 4704, 0 });
		
		
		
		
		Minigames(int textId, String name, String description1, String description2, String description3,
				String description4, String description5, int npcId, int[] teleportCords) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;

		}

		Minigames(int textId, String name, String description1, String description2, String description3,
				String description4, String description5, int npcId, int[] teleportCords, int adjustedZoom) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;
			this.adjustedZoom = adjustedZoom;

		}

		private int textId;
		private String name;
		private String description1, description2, description3, description4, description5;
		private int npcId;
		private int[] teleportCords;
		private int adjustedZoom;
	}

	public enum Cities {
		FRANKENSTIEN(50601, "Frankenstiens Castle", "Frankenstiens Castle", "This Minigame drops", "Tier 1-7 Defenders", "", "", 4291, new int[] { 2845, 3540, 2 }),
		PESTCONTROL(50602, "Pest control", "Pest Control", "Earn points to use", "at the pest control shop", "", "", 3789, new int[] { 2657, 2648, 0 }),
		BARROWSMINIGAME(50603, "Barrows", "Barrows", "Dig your way for some", " fancy diamonds!", "", "", 2025, new int[] { 3564, 3289, 0 }),
		//DUNGEON(50604, "Dungeons Minigame", "Dungeons Minigame", "Do you have what it", " takes to survive?", "", "", 499, new int[] { 3309, 9808, 8 }),
		ZOMBIE (50604, "Corona Land", "Zombie Minigame", "Walking Dead"," Be ready","", "", 499, new int[] { 3503, 3563, 0});
		
		//HALLOWEEN(50602, "Trios Minigame", "Earn all 3 Keys", "to be used at", "the Trio Chest!","", "", 75, 
			//	new int[] { 3107, 3427, 0 }, 0);
		Cities(int textId, String name, String description1, String description2, String description3,
				String description4, String description5, int npcId, int[] teleportCords) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;

		}

		Cities(int textId, String name, String description1, String description2, String description3,
				String description4, String description5, int npcId, int[] teleportCords, int adjustedZoom) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;
			this.adjustedZoom = adjustedZoom;

		}

		private int textId;
		private String name;
		private String description1, description2, description3, description4, description5;
		private int npcId;
		private int[] teleportCords;
		private int adjustedZoom;
	}

	public static void resetOldData() {
		currentTab = 0;
		currentClickIndex = 0;
	}

	public static void handleTeleports(Player player) {
		switch (currentTab) {
		case 0:
			Bosses bossData = Bosses.values()[currentClickIndex];
			handleBossTeleport(player, bossData);
			break;
		case 1:
			Monsters monsterData = Monsters.values()[currentClickIndex];
			handleMonsterTeleport(player, monsterData);
			break;
		case 2:
			Wilderness wildyData = Wilderness.values()[currentClickIndex];
			handleWildyTeleport(player, wildyData);
			break;
		case 3:
			Zones ZonesData = Zones.values()[currentClickIndex];
			handleZonesTeleport(player, ZonesData);
			break;
		case 4:
			Minigames minigameData = Minigames.values()[currentClickIndex];
			handleMinigameTeleport(player, minigameData);
			break;
		case 5:
			Cities cityData = Cities.values()[currentClickIndex];
			handleCityTeleport(player, cityData);
			break;
		}
	}

	public static void handleBossTeleport(Player player, Bosses bossData) {

		TeleportHandler.teleportPlayer(player,
				new Position(bossData.teleportCords[0], bossData.teleportCords[1], bossData.teleportCords[2]),
				player.getSpellbook().getTeleportType());
	}

	public static void handleMonsterTeleport(Player player, Monsters monsterData) {

		TeleportHandler.teleportPlayer(player,
				new Position(monsterData.teleportCords[0], monsterData.teleportCords[1], monsterData.teleportCords[2]),
				player.getSpellbook().getTeleportType());
	}

	public static void handleWildyTeleport(Player player, Wilderness wildyData) {

		TeleportHandler.teleportPlayer(player,
				new Position(wildyData.teleportCords[0], wildyData.teleportCords[1], wildyData.teleportCords[2]),
				player.getSpellbook().getTeleportType());
	}

	public static void handleZonesTeleport(Player player, Zones skillData) {

		TeleportHandler.teleportPlayer(player,
				new Position(skillData.teleportCords[0], skillData.teleportCords[1], skillData.teleportCords[2]),
				player.getSpellbook().getTeleportType());
	}

	public static void handleMinigameTeleport(Player player, Minigames minigameData) {

		TeleportHandler.teleportPlayer(player, new Position(minigameData.teleportCords[0],
				minigameData.teleportCords[1], minigameData.teleportCords[2]), player.getSpellbook().getTeleportType());
	}

	public static void handleCityTeleport(Player player, Cities cityData) {

		TeleportHandler.teleportPlayer(player,
				new Position(cityData.teleportCords[0], cityData.teleportCords[1], cityData.teleportCords[2]),
				player.getSpellbook().getTeleportType());
	}

	private static void clearData(Player player) {
		for (int i = 50601; i < 50700; i++) {
			player.getPacketSender().sendString(i, "");
		}
	}

	private static int currentTab = 0; // 0 = Boss, 1 = Monsters, 2 = Wildy, 3 = Zones, 4 = Minigame, 5 = Cities

	public static boolean handleButton(Player player, int buttonID) {

		if (!(buttonID >= -14935 && buttonID <= -14836)) {
			return false;
		}
		int index = -1;

		if (buttonID >= -14935) {
			index = 14935 + buttonID;
		}
		if (currentTab == 0) {
			if (index >= 0 && index < Bosses.values().length) {
				System.out.println("Handled boss data [As index was 0]");
				Bosses bossData = Bosses.values()[index];
				currentClickIndex = index;
				sendBossData(player, bossData);
				sendDrops(player, bossData.npcId);
			}
		}
		if (currentTab == 1) {
			if (index >= 0 && index < Monsters.values().length) {
				System.out.println("Handled monster data [As index was 1]");
				Monsters monsterData = Monsters.values()[index];
				currentClickIndex = index;
				sendMonsterData(player, monsterData);
				sendDrops(player, monsterData.npcId);
			}
		}
		if (currentTab == 2) {
			if (index >= 0 && index < Wilderness.values().length) {
				System.out.println("Handled monster data [As index was 1]");
				Wilderness wildyData = Wilderness.values()[index];
				currentClickIndex = index;
				sendWildyData(player, wildyData);
				sendDrops(player, wildyData.npcId);
			}
		}
		if (currentTab == 3) {
			if (index >= 0 && index < Zones.values().length) {
				System.out.println("Handled monster data [As index was 1]");
				Zones ZonesData = Zones.values()[index];
				currentClickIndex = index;
				sendZonesData(player, ZonesData);
				sendDrops(player, ZonesData.npcId);
			}
		}
		if (currentTab == 4) {
			if (index >= 0 && index < Minigames.values().length) {
				System.out.println("Handled monster data [As index was 1]");
				Minigames minigamesData = Minigames.values()[index];
				currentClickIndex = index;
				sendMinigameData(player, minigamesData);
				sendDrops(player, minigamesData.npcId);
			}
		}
		if (currentTab == 5) {
			if (index >= 0 && index < Cities.values().length) {
				System.out.println("Handled monster data [As index was 1]");
				Cities cityData = Cities.values()[index];
				currentClickIndex = index;
				sendCityData(player, cityData);
				sendDrops(player, cityData.npcId);
			}
		}
		return true;

	}

	public static int currentClickIndex = 0;

	public static void sendBossData(Player player, Bosses data) {
		player.getPacketSender().sendString(51200, data.description1);
		player.getPacketSender().sendString(51201, data.description2);
		player.getPacketSender().sendString(51202, data.description3);
		player.getPacketSender().sendString(51203, data.description4);
		player.getPacketSender().sendString(51204, data.description5);
		player.getPacketSender().sendNpcOnInterface(50514, data.npcId, data.adjustedZoom != 0 ? data.adjustedZoom : 0);
	}

	public static void sendMonsterData(Player player, Monsters data) {
		player.getPacketSender().sendString(51200, data.description1);
		player.getPacketSender().sendString(51201, data.description2);
		player.getPacketSender().sendString(51202, data.description3);
		player.getPacketSender().sendString(51203, data.description4);
		player.getPacketSender().sendString(51204, data.description5);
		player.getPacketSender().sendNpcOnInterface(50514, data.npcId, data.adjustedZoom != 0 ? data.adjustedZoom : 0);
	}

	public static void sendWildyData(Player player, Wilderness data) {
		player.getPacketSender().sendString(51200, data.description1);
		player.getPacketSender().sendString(51201, data.description2);
		player.getPacketSender().sendString(51202, data.description3);
		player.getPacketSender().sendString(51203, data.description4);
		player.getPacketSender().sendString(51204, data.description5);
		player.getPacketSender().sendNpcOnInterface(50514, data.npcId, data.adjustedZoom != 0 ? data.adjustedZoom : 0);
	}

	public static void sendZonesData(Player player, Zones data) {
		player.getPacketSender().sendString(51200, data.description1);
		player.getPacketSender().sendString(51201, data.description2);
		player.getPacketSender().sendString(51202, data.description3);
		player.getPacketSender().sendString(51203, data.description4);
		player.getPacketSender().sendString(51204, data.description5);
		player.getPacketSender().sendNpcOnInterface(50514, data.npcId, data.adjustedZoom != 0 ? data.adjustedZoom : 0);
	}

	public static void sendMinigameData(Player player, Minigames data) {
		player.getPacketSender().sendString(51200, data.description1);
		player.getPacketSender().sendString(51201, data.description2);
		player.getPacketSender().sendString(51202, data.description3);
		player.getPacketSender().sendString(51203, data.description4);
		player.getPacketSender().sendString(51204, data.description5);
		player.getPacketSender().sendNpcOnInterface(50514, data.npcId, data.adjustedZoom != 0 ? data.adjustedZoom : 0);
	}

	public static void sendCityData(Player player, Cities data) {
		player.getPacketSender().sendString(51200, data.description1);
		player.getPacketSender().sendString(51201, data.description2);
		player.getPacketSender().sendString(51202, data.description3);
		player.getPacketSender().sendString(51203, data.description4);
		player.getPacketSender().sendString(51204, data.description5);
		player.getPacketSender().sendNpcOnInterface(50514, data.npcId, data.adjustedZoom != 0 ? data.adjustedZoom : 0);
	}

	public static void sendBossTab(Player player) {
		player.getPacketSender().sendInterface(50500);
		currentTab = 0;
		clearData(player);
		resetOldData();
		player.getPacketSender().sendString(CATEGORY_NAME_ID, "@gre@Starters");
		for (Bosses data : Bosses.values()) {
			player.getPacketSender().sendString(data.textId, data.name);
		}

	}

	public static void sendMonsterTab(Player player) {
		currentTab = 1;
		clearData(player);
		player.getPacketSender().sendString(CATEGORY_NAME_ID, "@yel@Monsters");
		for (Monsters data : Monsters.values()) {
			player.getPacketSender().sendString(data.textId, data.name);
		}
	}

	public static void sendWildyTab(Player player) {
		currentTab = 2;
		clearData(player);
		player.getPacketSender().sendString(CATEGORY_NAME_ID, "@red@Hardened ");
		for (Wilderness data : Wilderness.values()) {
			player.getPacketSender().sendString(data.textId, data.name);
		}
	}

	public static void sendZonesTab(Player player) {
		currentTab = 3;
		clearData(player);
		player.getPacketSender().sendString(CATEGORY_NAME_ID, "@bla@Expert");
		for (Zones data : Zones.values()) {
			player.getPacketSender().sendString(data.textId, data.name);
		}
	}

	public static void sendMinigamesTab(Player player) {
		currentTab = 4;
		clearData(player);
		player.getPacketSender().sendString(CATEGORY_NAME_ID, "@gre@Zones");
		for (Minigames data : Minigames.values()) {
			player.getPacketSender().sendString(data.textId, data.name);
		}
	}

	public static void sendCitiesTab(Player player) {
		currentTab = 5;
		clearData(player);
		player.getPacketSender().sendString(CATEGORY_NAME_ID, "@gre@Minigames");
		for (Cities data : Cities.values()) {
			player.getPacketSender().sendString(data.textId, data.name);
		}
	}

	public static void sendDrops(Player player, int npcId) {
		player.getPacketSender().resetItemsOnInterface(51251, 100);
		try {
			NPCDrops drops = NPCDrops.forId(npcId);
			if(drops == null) {
				System.out.println("Was null");
				return;
			}
			for (int i = 0; i < drops.getDropList().length; i++) {
				
				player.getPacketSender().sendItemOnInterface(51251, drops.getDropList()[i].getId(), i,
						drops.getDropList()[i].getItem().getAmount());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}