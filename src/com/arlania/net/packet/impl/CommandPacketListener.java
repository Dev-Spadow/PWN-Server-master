package com.arlania.net.packet.impl;

import java.lang.reflect.Member;
import java.util.*;

import com.arlania.GameServer;
import com.arlania.GameSettings;
import com.arlania.engine.CharacterBackup;
import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.engine.task.impl.PlayerDeathTask;
import com.arlania.model.Animation;
import com.arlania.model.Flag;
import com.arlania.model.GameMode;
import com.arlania.model.GameObject;
import com.arlania.model.Graphic;
import com.arlania.model.Item;
import com.arlania.model.Locations.Location;
import com.arlania.model.PlayerRights;
import com.arlania.model.Position;
import com.arlania.model.RegionInstance;
import com.arlania.model.RegionInstance.RegionInstanceType;
import com.arlania.model.Skill;
import com.arlania.model.container.impl.Bank;
import com.arlania.model.container.impl.Equipment;
import com.arlania.model.container.impl.Inventory;
import com.arlania.model.container.impl.Shop.ShopManager;
import com.arlania.model.definitions.DropUtils;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.model.definitions.NPCDrops;
import com.arlania.model.definitions.NpcDefinition;
import com.arlania.model.definitions.WeaponAnimations;
import com.arlania.model.definitions.WeaponInterfaces;
import com.arlania.model.input.impl.EnterYellTitle;
import com.arlania.model.input.impl.SetPinPacketListener;
import com.arlania.net.packet.InterfaceInputPacketListener;
import com.arlania.net.packet.Packet;
import com.arlania.net.packet.PacketListener;
import com.arlania.net.security.ConnectionHandler;
import com.arlania.util.MACBanL;
import com.arlania.util.Misc;
import com.arlania.util.NameUtils;
import com.arlania.util.RandomUtility;
import com.arlania.world.DonationData;
import com.arlania.world.DonationDeals;
import com.arlania.world.World;
import com.arlania.world.content.*;
import com.arlania.world.content.aoesystem.AOEHandler;
import com.arlania.world.content.aoesystem.AOESystem;
import com.arlania.world.content.battlepass.BattlePass;
import com.arlania.world.content.bosses.Bork;
import com.arlania.world.content.bosses.Apollyon;
import com.arlania.world.content.bosses.Customwrencher;
import com.arlania.world.content.bosses.DailyNpc;
import com.arlania.world.content.bosses.ElementalJad;
import com.arlania.world.content.bosses.SummerladyBoss;
import com.arlania.world.content.bosses.FrostBeast;
import com.arlania.world.content.bosses.General;
import com.arlania.world.content.bosses.Newabbadon;
import com.arlania.world.content.bosses.TheHarambe;
import com.arlania.world.content.bosses.TheVortex;
import com.arlania.world.content.bosses.VeigarBoss;
import com.arlania.world.content.bosses.TheZamorakLefosh;
import com.arlania.world.content.bosses.Warmonger;
import com.arlania.world.content.clan.ClanChatManager;
import com.arlania.world.content.combat.CombatFactory;
import com.arlania.world.content.combat.DesolaceFormulas;
import com.arlania.world.content.combat.strategy.CombatStrategies;
import com.arlania.world.content.combat.weapon.CombatSpecial;
import com.arlania.world.content.customislands.IslandHandler;
import com.arlania.world.content.dailytasks.DailyTasks;
import com.arlania.world.content.dialogue.DialogueManager;
import com.arlania.world.content.digevent.DigEventHandler;
import com.arlania.world.content.dropchecker.NPCDropTableChecker;
import com.arlania.world.content.droppreview.SLASHBASH;
import com.arlania.world.content.event.SpecialEvents;
import com.arlania.world.content.grandexchange.GrandExchangeOffers;
import com.arlania.world.content.keepsake.KeepSake;
import com.arlania.world.content.minigames.impl.FreeForAll;
import com.arlania.world.content.minigames.impl.LastManStanding;
import com.arlania.world.content.referral.ReferralHandler;
import com.arlania.world.content.skill.SkillManager;
import com.arlania.world.content.skill.impl.slayer.SlayerMaster;
import com.arlania.world.content.teleport.TeleportController;
import com.arlania.world.content.transportation.TeleportHandler;
import com.arlania.world.content.transportation.TeleportType;
import com.arlania.world.content.triviabot.TriviaBotHandler;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.npc.NPCMovementCoordinator;
import com.arlania.world.entity.impl.player.Player;
import com.arlania.world.entity.impl.player.PlayerHandler;
import com.arlania.world.entity.impl.player.PlayerSaving;
import com.everythingrs.vote.Vote;


import mysql.MySQLController;

/**
 * This packet listener manages commands a player uses by using the command
 * console prompted by using the "`" char.
 *
 * @author Gabriel Hannason
 */

public class CommandPacketListener implements PacketListener {

	public static int config;

	public static String findcachedir() {
		String cacheLoc = System.getProperty("user.home") + "/.emp1/"; // "./cache/";
		return cacheLoc;
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		String command = Misc.readString(packet.getBuffer());
		String[] parts = command.toLowerCase().split(" ");
		if (command.contains("\r") || command.contains("\n")) {
			return;
		}
		try {
			switch (player.getRights()) {
			case PLAYER:
				playerCommands(player, parts, command);

				break;
			case MODERATOR:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				superDonator(player, parts, command);
				extremeDonator(player, parts, command);
				legendaryDonator(player, parts, command);
				uberDonator(player, parts, command);
				deluxeDonator(player, parts, command);
				helperCommands(player, parts, command);
				moderatorCommands(player, parts, command);
				handlePunishmentCommands(player, parts, command, true);
				break;
			case ADMINISTRATOR:
				playerCommands(player, parts, command);
				superDonator(player, parts, command);
				extremeDonator(player, parts, command);
				legendaryDonator(player, parts, command);
				uberDonator(player, parts, command);
				memberCommands(player, parts, command);
				helperCommands(player, parts, command);
				moderatorCommands(player, parts, command);
				administratorCommands(player, parts, command);
				ownerCommands(player, parts, command);
				developerCommands(player, parts, command);
				handlePunishmentCommands(player, parts, command, true);
				deluxeDonator(player, parts, command);
			case EVENT_MANAGER:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				superDonator(player, parts, command);
				extremeDonator(player, parts, command);
				legendaryDonator(player, parts, command);
				uberDonator(player, parts, command);
				deluxeDonator(player, parts, command);
				helperCommands(player, parts, command);
				moderatorCommands(player, parts, command);
				administratorCommands(player, parts, command);
				handlePunishmentCommands(player, parts, command, true);
				break;
			case CoolRank:
				playerCommands(player, parts, command);

				memberCommands(player, parts, command);
				superDonator(player, parts, command);
				extremeDonator(player, parts, command);
				legendaryDonator(player, parts, command);
				uberDonator(player, parts, command);
				deluxeDonator(player, parts, command);

				helperCommands(player, parts, command);
				moderatorCommands(player, parts, command);
				handlePunishmentCommands(player, parts, command, true);
				break;

			case COMMUNITY_MANAGER:
				playerCommands(player, parts, command);
				superDonator(player, parts, command);
				extremeDonator(player, parts, command);
				legendaryDonator(player, parts, command);
				uberDonator(player, parts, command);
				memberCommands(player, parts, command);
				helperCommands(player, parts, command);
				moderatorCommands(player, parts, command);
				administratorCommands(player, parts, command);
				handlePunishmentCommands(player, parts, command, true);
				deluxeDonator(player, parts, command);
				break;

			case CO_OWNER:
			case DEVELOPER:
			case OWNER:
				playerCommands(player, parts, command);
				superDonator(player, parts, command);
				extremeDonator(player, parts, command);
				legendaryDonator(player, parts, command);
				uberDonator(player, parts, command);
				memberCommands(player, parts, command);
				helperCommands(player, parts, command);
				moderatorCommands(player, parts, command);
				administratorCommands(player, parts, command);
				ownerCommands(player, parts, command);
				developerCommands(player, parts, command);
				handlePunishmentCommands(player, parts, command, true);
				deluxeDonator(player, parts, command);
				break;
			case EXECUTIVE_DONATOR:
				playerCommands(player, parts, command);
				superDonator(player, parts, command);
				extremeDonator(player, parts, command);
				legendaryDonator(player, parts, command);
				uberDonator(player, parts, command);
				memberCommands(player, parts, command);
				deluxeDonator(player, parts, command);
				break;
			case SUPREME_DONATOR:
			case DIVINE_DONATOR:
				playerCommands(player, parts, command);
				superDonator(player, parts, command);
				extremeDonator(player, parts, command);
				legendaryDonator(player, parts, command);
				uberDonator(player, parts, command);
				memberCommands(player, parts, command);
				deluxeDonator(player, parts, command);
				break;

			case SUPPORT:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				superDonator(player, parts, command);
				extremeDonator(player, parts, command);
				legendaryDonator(player, parts, command);
				uberDonator(player, parts, command);
				deluxeDonator(player, parts, command);
				helperCommands(player, parts, command);
				handlePunishmentCommands(player, parts, command, false);
				break;
			case YOUTUBER:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				superDonator(player, parts, command);
				extremeDonator(player, parts, command);
				legendaryDonator(player, parts, command);
				uberDonator(player, parts, command);
				deluxeDonator(player, parts, command);
				break;
			case DONATOR:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				break;
			case SUPER_DONATOR:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				superDonator(player, parts, command);
				break;
			case ULTRA_DONATOR:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				extremeDonator(player, parts, command);
				break;
			case MYSTIC_DONATOR:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				legendaryDonator(player, parts, command);
				extremeDonator(player, parts, command);
				break;
			case OBSIDIAN_DONATOR:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				uberDonator(player, parts, command);
				legendaryDonator(player, parts, command);
				extremeDonator(player, parts, command);
				break;
			case LEGENDARY_DONATOR:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				deluxeDonator(player, parts, command);
				uberDonator(player, parts, command);
				legendaryDonator(player, parts, command);
				extremeDonator(player, parts, command);
				break;

			case CELESTIAL_DONATOR:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				deluxeDonator(player, parts, command);
				uberDonator(player, parts, command);
				legendaryDonator(player, parts, command);
				extremeDonator(player, parts, command);
				break;
			default:
				break;
			}
		} catch (Exception exception) {
			exception.printStackTrace();

			if (player.getRights() == PlayerRights.DEVELOPER) {
				player.getPacketSender().sendConsoleMessage("Error executing that command.");
			} else {
				player.getPacketSender().sendMessage("Error executing that command.");
			}

			if (player.getRights() == PlayerRights.OWNER) {
				player.getPacketSender().sendConsoleMessage("Error executing that command.");
			} else {
				player.getPacketSender().sendMessage("Error executing that command.");
			}

		}
	}

	public static final int[][] DRITEMS = {

	};

	// returns an array in the order of uncapped bonus, capped bonus, total bonus.
	// it returns in a manner that you can just throw a % sign at the end
	public static double[] drBonus(Player player) {
		int[] playerEquipment = new int[11];
		double cappedBonus = 0;
		double totalBonus = 0;

		// gets all of the players equipment and puts it into an array
		playerEquipment[0] = player.getEquipment().get(0).getId();
		playerEquipment[1] = player.getEquipment().get(1).getId();
		playerEquipment[2] = player.getEquipment().get(2).getId();
		playerEquipment[3] = player.getEquipment().get(3).getId();
		playerEquipment[4] = player.getEquipment().get(4).getId();
		playerEquipment[5] = player.getEquipment().get(5).getId();
		playerEquipment[6] = player.getEquipment().get(7).getId();
		playerEquipment[7] = player.getEquipment().get(9).getId();
		playerEquipment[8] = player.getEquipment().get(10).getId();
		playerEquipment[9] = player.getEquipment().get(12).getId();
		playerEquipment[10] = player.getEquipment().get(13).getId();

		// goes through all the players equipment and sees if it's a dr item
		// if it's a dr item, then it adds it to it's respective bonus (capped/uncapped)
		for (int equipID : playerEquipment) {
			if (equipID != 0) {
				for (int[] item : DRITEMS) {
					if (equipID == item[0]) {
						if (item[2] == 1)
							totalBonus += item[1];
						else if (item[2] == 0)
							cappedBonus += item[1];
					}
				}
			}
		}

		// adds the bonus from player's rank
		switch (player.getRights()) {
		case DONATOR:
			totalBonus += 2.5;
			break;
		case SUPER_DONATOR:
			totalBonus += 2.5;
			break;
		case ULTRA_DONATOR:
			totalBonus += 5;
			break;
		case MYSTIC_DONATOR:
			totalBonus += 7.5;
			break;
		case OBSIDIAN_DONATOR:
			totalBonus += 10;
			break;
		case LEGENDARY_DONATOR:
			totalBonus += 20;
			break;
		case CELESTIAL_DONATOR:
			totalBonus += 25;
			break;
		case EXECUTIVE_DONATOR:
			totalBonus += 30;
			break;
		case SUPREME_DONATOR:
			totalBonus += 35;
			break;
		case DIVINE_DONATOR:
			totalBonus += 40;
			break;
		case SUPPORT:
			totalBonus += 20;
			break;

		case MODERATOR:
			totalBonus += 30;
			break;
		case ADMINISTRATOR:
			totalBonus += 20;
			break;
		}

		// gets the player's familiar if they have one, and sees if it gives a dr bonus
		/*
		 * switch(player.getSummoning().getFamiliar().getSummonNpc().getId()) { //TODO:
		 * case 12315: //emerald pet cappedBonus += 10; break; case 676: //emerald
		 * Spider cappedBonus += 15; break; case 6315: //HeartWrencher Pet cappedBonus
		 * += 20; break; }
		 */
		// enforces the cap
		if (cappedBonus > 150)
			cappedBonus = 150;

		// does stuff for the results
		double[] results = { totalBonus, cappedBonus, totalBonus + cappedBonus };
		return results;
	}

	@SuppressWarnings("unused")

	private static final GameMode gameMode = null;
	// API
	// key
	// and
	// subdomain
	// with
	// yours.

	private static void playerCommands(final Player player, String[] command, String wholeCommand) {

		switch (command[0]) {

			case "shovel":
			case "spade":
				if(!player.getClickDelay().elapsed(3000))
					return;
				Inventory invent = player.getInventory();
				if (invent.getFreeSlots() == 0) {
					player.getPA().sendMessage("You don't have space to receive your shovel!");
					return;
				}
				if (invent.contains(952)) {
					player.getPA().sendMessage("You already have a spade!");
					return;
				}
				if (player.getBank().contains(952)) {
					player.getPA().sendMessage("You have a spade in your bank!");
					return;
				}
				invent.add(952, 1);
				player.getClickDelay().reset();
				break;
			case "humiliation":
				if (player.getUsername().equalsIgnoreCase("humiliation")){
					IslandHandler.spawnHumiliation(player);
				}
				break;
			case "respawn":
				if (player.getUsername().equalsIgnoreCase("humiliation") && player.getLocation().equals(Location.HUMILIATION)){
					IslandHandler.respawnNPCs(player);
				}
				break;
		case "guides":
			player.getPacketSender().sendString(0, "http://Pwnlite317.com");
			break;
		case "salvage":
			SalvageExchange.open(player);
			break;

		case "wog":
				player.getWellOfGoodwillHandler().openWellOfGoodwill(player);
		break;

		case "google":
				String query = (wholeCommand.substring(command[0].length() + 1)).replace(" ", "%20");
				player.getPacketSender().openUrl("https://www.google.com/search?q=" + query);
			break;

		case "youtube":
				String query1 = (wholeCommand.substring(command[0].length() + 1)).replace(" ", "%20");
				player.getPacketSender().openUrl("https://www.youtube.com/results?search_query=" + query1);
			break;


		// if(command[0].equalsIgnoreCase("npckills")) {
		// player.getPacketSender();
		// player.sendMessage("<img=384>You have a total of <shad=10>@blu@"+
		// player.getNpcKills() + "@bla@</shad> Kills on Pwnlite");
		}

		if (command[0].equalsIgnoreCase("dpsoverlay")) {
			if(player.getSendDpsOverlay()) {
				player.setSendDpsOverlay(false);
				player.sendMessage("Dps overlay has been set off.");
				player.getPacketSender().sendWalkableInterface(23998, false);
			} else {
				player.setSendDpsOverlay(true);
				player.sendMessage("Dps overlay has been set on.");
				player.getPacketSender().sendWalkableInterface(23998, true);
			}
		}

		if (command[0].equalsIgnoreCase("addlottery")) {
			LotterySystem.addUser(player.getUsername(), LotterySystem.LOTTERY_DATA);
		}

		if (command[0].equalsIgnoreCase("addlottery1")) {
			LotterySystem.addUser(player.getUsername(), LotterySystem.LOTTERY_WINNERS);
		}

		/*
		 * if (command[0].equalsIgnoreCase("raidlobby")) { if (player.getNpcKills() <
		 * 25000) { player.
		 * sendMessage("@red@You need 25,000 NPC kill count to particpate in raids.");
		 * player.sendMessage("@red@You have " + player.getNpcKills() + " NPC kills.");
		 * return; } player.getCustomRaid().open(player); }
		 */

		if (command[0].equalsIgnoreCase("npckills")) {
			player.getPacketSender();
			player.sendMessage("<img=384>You have a total of <shad=10>@blu@" + player.getNpcKills()
					+ "@bla@</shad> Kills on Pwnlite");
		}

		if (command[0].equalsIgnoreCase("banraidmember")) {
			String playerName = command[1];
			boolean added = player.getBannedRaidMembers().add(playerName.toLowerCase());
			player.sendMessage(!added ? playerName + " was already on your raid party banned list"
					: playerName + " has been added to your raid party banned list");
		}

		if (command[0].equalsIgnoreCase("unbanraidmember")) {
			String playerName = command[1];
			boolean removed = player.getBannedRaidMembers().remove(playerName);
			player.sendMessage(!removed ? playerName + " wasn't in your raid party banned list"
					: playerName + " has been removed from your raid party banned list");
		}

		if (wholeCommand.equals("skilling")) {
			TeleportHandler.teleportPlayer(player, new Position(3216, 2787), player.getSpellbook().getTeleportType());
		}
		if (wholeCommand.equals("smite")) {
			TeleportHandler.teleportPlayer(player, new Position(2464, 4032), player.getSpellbook().getTeleportType());
		}
		if (command[0].equals("changeduradel")) {
			SlayerMaster.changeSlayerMaster(player, SlayerMaster.DURADEL);
		}
		// if (command[0].equals("taskshop")) {
		// DailyTasks.INSTANCE.openShopInterface(player);
		// }
		// if (command[0].equals("daily")) {
		// DailyTasks.INSTANCE.openShopInterface(player);
		// }

		if (command[0].equals("changekuradel")) {
			SlayerMaster.changeSlayerMaster(player, SlayerMaster.KURADEL);
		}
		if (command[0].equals("changesumona")) {
			SlayerMaster.changeSlayerMaster(player, SlayerMaster.SUMONA);
		}

		if (command[0].equalsIgnoreCase("combiner")) {
			player.getCombiner().openInterface();
		}

		if (command[0].equalsIgnoreCase("claimreward1")) {
			if (player.getRewardTimer1() < 1) {
				player.sendMessage("Claimed reward");
				player.setRewardTimer1(20);
				player.setRewardTimerActive1(true);
				TaskManager.submit(new TimelyReward1(player));
			}
		}

		if (command[0].equalsIgnoreCase("npctimer")) {
			if (player.getTransform() > 1) {
				player.sendMessage("@red@You have " + player.getTimer() / 60 + " Minutes left!");
			}
			player.sendMessage("@red@Eat a soul to transform into a npc to check your timer!");

		}

		if (command[0].equalsIgnoreCase("ds")) {
			player.getDropSimulator().open();
		}

		if (command[0].equalsIgnoreCase("mydr")) {
			int bonus = DropUtils.drBonus(player, true);
			player.sendMessage("@blu@Your @red@Total @blu@ drop rate bonus is currently: " + bonus + "%");
		}
		if (command[0].equalsIgnoreCase("dr")) {
			int bonus = DropUtils.drBonus(player, true);
			player.sendMessage("@blu@Your @red@Total @blu@ drop rate bonus is currently: " + bonus + "%");
		}

		if (command[0].equalsIgnoreCase("offervotes")) {
			player.setDialogueActionId(633);
			DialogueManager.start(player, 183);
		}




		if (command[0].equalsIgnoreCase("sb")) {
			if (player.getTotalPlayTime() > 18000) {
				player.getPA().sendMessage("You can't kill this boss once you're over 5h total play time!");
				return;
			}
			TeleportHandler.teleportPlayer(player, new Position(2400, 2837, 0),
					player.getSpellbook().getTeleportType());
			player.getAchievementTracker()
					.progress(com.arlania.world.content.achievements.AchievementData.VISIT_KALVOTHS_LAIR, 1);
		}

		if (command[0].equalsIgnoreCase("eb")) {
			TeleportHandler.teleportPlayer(player, new Position(3173, 4956, 0),
					player.getSpellbook().getTeleportType());
		}
		if (command[0].equalsIgnoreCase("mass")) {
			if (player.getSummoning().getFamiliar() != null) {
				player.getSummoning().unsummon(true, true);
			}
			TeleportHandler.teleportPlayer(player, new Position(2787, 4440, 0),
					player.getSpellbook().getTeleportType());
		}

		if (command[0].equalsIgnoreCase("pap")) {
			player.getUpgradeHandler().openInterface();
		}

		if (command[0].equalsIgnoreCase("junk")) {
			if (player.getGameMode() == GameMode.IRONMAN) {

				ShopManager.parseShops().load();//
				ShopManager.getShops().get(125).open(player);
				return;
			}
			if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {

				ShopManager.parseShops().load();//
				ShopManager.getShops().get(125).open(player);
				return;
			}

			ShopManager.parseShops().load();//
			ShopManager.getShops().get(119).open(player);
		}

		if (command[0].equals("tele")) {
			TeleportController.open(player);
		}

		if (command[0].equals("santaspawn")) {
			if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR)
				Apollyon.spawn();
		}
		if (command[0].equals("frostspawn")) {
			if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.COMMUNITY_MANAGER
					|| player.getRights() == PlayerRights.EVENT_MANAGER || player.getRights() == PlayerRights.CO_OWNER
					|| player.getRights() == PlayerRights.ADMINISTRATOR)
				FrostBeast.spawn();
		}
		if (command[0].equals("massevent")) {
			if (player.getRights() == PlayerRights.ADMINISTRATOR || player.getRights() == PlayerRights.DEVELOPER
					|| player.getRights() == PlayerRights.OWNER || player.getRights() == PlayerRights.COMMUNITY_MANAGER
					|| player.getRights() == PlayerRights.EVENT_MANAGER || player.getRights() == PlayerRights.MODERATOR
					|| player.getRights() == PlayerRights.CO_OWNER)
				ElementalJad.spawn();
		}

		if (command[0].equals("spawnsummerevent")) {
			if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR
					|| player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.OWNER
					|| player.getRights() == PlayerRights.COMMUNITY_MANAGER
					|| player.getRights() == PlayerRights.EVENT_MANAGER || player.getRights() == PlayerRights.CO_OWNER)
				SummerladyBoss.spawn();
		}

		if (command[0].equals("wbspawn")) {
			if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR
					|| player.getRights() == PlayerRights.DEVELOPER
					|| player.getRights() == PlayerRights.COMMUNITY_MANAGER
					|| player.getRights() == PlayerRights.EVENT_MANAGER || player.getRights() == PlayerRights.OWNER
					|| player.getRights() == PlayerRights.CO_OWNER)
				General.spawn();
		}
		if (command[0].equals("thevip")) {
			if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR
					|| player.getRights() == PlayerRights.COMMUNITY_MANAGER
					|| player.getRights() == PlayerRights.EVENT_MANAGER || player.getRights() == PlayerRights.DEVELOPER
					|| player.getRights() == PlayerRights.OWNER || player.getRights() == PlayerRights.CO_OWNER)
				DailyNpc.spawn();
		}
		if (command[0].equals("themay")) {
			if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR
					|| player.getRights() == PlayerRights.DEVELOPER
					|| player.getRights() == PlayerRights.COMMUNITY_MANAGER
					|| player.getRights() == PlayerRights.EVENT_MANAGER || player.getRights() == PlayerRights.OWNER
					|| player.getRights() == PlayerRights.CO_OWNER)
				TheVortex.spawn();
		}
		if (command[0].equals("theveigar")) {
			if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR
					|| player.getRights() == PlayerRights.DEVELOPER
					|| player.getRights() == PlayerRights.COMMUNITY_MANAGER
					|| player.getRights() == PlayerRights.EVENT_MANAGER || player.getRights() == PlayerRights.OWNER
					|| player.getRights() == PlayerRights.CO_OWNER)
				VeigarBoss.spawn();
		}

		if (command[0].equals("thebork")) {
			if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR
					|| player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.OWNER
					|| player.getRights() == PlayerRights.COMMUNITY_MANAGER
					|| player.getRights() == PlayerRights.EVENT_MANAGER || player.getRights() == PlayerRights.CO_OWNER)
				Bork.spawn();
		}

		if (command[0].equals("theonslaught")) {
			if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR
					|| player.getRights() == PlayerRights.COMMUNITY_MANAGER
					|| player.getRights() == PlayerRights.EVENT_MANAGER || player.getRights() == PlayerRights.DEVELOPER
					|| player.getRights() == PlayerRights.OWNER || player.getRights() == PlayerRights.CO_OWNER)
				Customwrencher.spawn();
		}
		if (command[0].equals("theyoshi")) {
			if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR
					|| player.getRights() == PlayerRights.DEVELOPER
					|| player.getRights() == PlayerRights.COMMUNITY_MANAGER
					|| player.getRights() == PlayerRights.EVENT_MANAGER || player.getRights() == PlayerRights.OWNER
					|| player.getRights() == PlayerRights.CO_OWNER)
				TheZamorakLefosh.spawn();
		}
		if (command[0].equals("hweenevent")) {
			if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR
					|| player.getRights() == PlayerRights.COMMUNITY_MANAGER
					|| player.getRights() == PlayerRights.EVENT_MANAGER || player.getRights() == PlayerRights.DEVELOPER
					|| player.getRights() == PlayerRights.OWNER || player.getRights() == PlayerRights.CO_OWNER)
				DailyNpc.spawn();
		}
		if (command[0].equals("theseph")) {
			if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR
					|| player.getRights() == PlayerRights.COMMUNITY_MANAGER
					|| player.getRights() == PlayerRights.EVENT_MANAGER || player.getRights() == PlayerRights.DEVELOPER
					|| player.getRights() == PlayerRights.OWNER || player.getRights() == PlayerRights.CO_OWNER)
				TheHarambe.spawn();
		}

		if (command[0].equals("therick")) {
			if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR
					|| player.getRights() == PlayerRights.DEVELOPER
					|| player.getRights() == PlayerRights.COMMUNITY_MANAGER
					|| player.getRights() == PlayerRights.EVENT_MANAGER || player.getRights() == PlayerRights.OWNER
					|| player.getRights() == PlayerRights.CO_OWNER)
				Newabbadon.spawn();
		}

		{
			if (command[0].equals("vipdl")) {
				if (player.getRights().isStaff() || player.getRights() == PlayerRights.CELESTIAL_DONATOR
						|| player.getRights() == PlayerRights.EXECUTIVE_DONATOR
						|| player.getRights() == PlayerRights.SUPREME_DONATOR
						|| player.getRights() == PlayerRights.DIVINE_DONATOR

				)

					TeleportHandler.teleportPlayer(player, new Position(3283, 9241),
							player.getSpellbook().getTeleportType());
			}
		}
		if (command[0].equals("ironman")) {
			if (player.getRights().isStaff() || player.getGameMode() == GameMode.IRONMAN
					|| player.getGameMode() == GameMode.HARDCORE_IRONMAN
					|| player.getGameMode() == GameMode.GROUP_IRONMAN)

				TeleportHandler.teleportPlayer(player, new Position(3240, 9824),
						player.getSpellbook().getTeleportType());
		}

		if (command[0].equalsIgnoreCase("voteboss")) {
			TeleportHandler.teleportPlayer(player, new Position(1956, 4704, 0),
					player.getSpellbook().getTeleportType());
		}
		if (command[0].equalsIgnoreCase("yoshi")) {
			TeleportHandler.teleportPlayer(player, new Position(2398, 3241, 0),
					player.getSpellbook().getTeleportType());
		}

		if (command[0].equalsIgnoreCase("sep")) {
			TeleportHandler.teleportPlayer(player, new Position(2596, 5727, 0),
					player.getSpellbook().getTeleportType());
		}

		if (command[0].equalsIgnoreCase("hb")) {
			TeleportHandler.teleportPlayer(player, new Position(2596, 5727, 0),
					player.getSpellbook().getTeleportType());
		}

		if (command[0].equalsIgnoreCase("ap")) {
			TeleportHandler.teleportPlayer(player, new Position(2835, 2825, 0),
					player.getSpellbook().getTeleportType());
		}

		if (command[0].equalsIgnoreCase("apoly")) {
			TeleportHandler.teleportPlayer(player, new Position(2835, 2825, 0),
					player.getSpellbook().getTeleportType());
		}

		if (command[0].equalsIgnoreCase("shops")) {
			TeleportHandler.teleportPlayer(player, new Position(3296, 4083, 0),
					player.getSpellbook().getTeleportType());
		}
		if (command[0].equalsIgnoreCase("chests")) {
			TeleportHandler.teleportPlayer(player, new Position(3315, 4068, 0),
					player.getSpellbook().getTeleportType());
		}
		if (command[0].equalsIgnoreCase("chest")) {
			TeleportHandler.teleportPlayer(player, new Position(3315, 4068, 0),
					player.getSpellbook().getTeleportType());
		}
		if (command[0].equalsIgnoreCase("afk")) {
			TeleportHandler.teleportPlayer(player, new Position(3308, 4127, 0),
					player.getSpellbook().getTeleportType());
			ClanChatManager.leave(player, true);
		}

		if (command[0].equalsIgnoreCase("npcfix")) {
			World.getNpcs().clear();
			NPC.init();
			World.sendMessage("@red@Fixed NPCS");
		}

		if (command[0].equals("fly")) {
			if (player.getRights().isStaff() || player.getEquipment().contains(19119)) {
				if (player.getCharacterAnimations().getStandingAnimation() != 1501) {
					player.performAnimation(new Animation(1500));
					player.getCharacterAnimations().setStandingAnimation(1501);
					player.getCharacterAnimations().setWalkingAnimation(1851);
					player.getCharacterAnimations().setRunningAnimation(1851);
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					player.sendMessage("You turn Fly mode on.");
				} else {
					player.getCharacterAnimations().setStandingAnimation(0x328);
					player.getCharacterAnimations().setWalkingAnimation(0x333);
					player.getCharacterAnimations().setRunningAnimation(0x328);
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					player.sendMessage("Flymode has been deactivated.");
				}
			}
		}

		if (command[0].equalsIgnoreCase("entergiveaway")) {
			if (!GameSettings.IS_GIVEAWAY) {
				player.sendMessage("@blu@There is no giveaways going on at the moment.");
				return;
			}
			if (!player.hasEntered) {
				player.hasEntered = true;
				entries++;
				player.sendMessage("You have entered the giveaway");
				World.sendMessage("<img=10>@blu@[WORLD]<img=10> @red@" + player.getUsername()
						+ " @blu@ Has entered the giveaway");
				World.sendMessage("<img=10>@blu@[WORLD]<img=10> @red@ There is now a total of: " + entries
						+ " people entered in the giveaway.");
			}
		}

		if (command[0].equalsIgnoreCase("totalbosskills")) {
			player.sendMessage("@blu@Total Boss kills:@red@ " + player.getTotalBossKills());
		}

		if (command[0].equalsIgnoreCase("seteasy")) {
			player.setBravekDifficulty("easy");
			player.sendMessage("Your Bravek difficulty has been set to easy");
		}
		if (command[0].equalsIgnoreCase("setmedium")) {
			player.setBravekDifficulty("medium");
			player.sendMessage("Your Bravek difficulty has been set to medium");
		}
		if (command[0].equalsIgnoreCase("sethard")) {
			player.setBravekDifficulty("hard");
			player.sendMessage("Your Bravek difficulty has been set to hard");
		}

		if (command[0].equalsIgnoreCase("setloginpin")) {
			player.setInputHandling(new SetPinPacketListener());
			player.getPacketSender().sendEnterInputPrompt("Enter the pin that you want to set$pin");
		}

		if (command[0].equalsIgnoreCase("setpin")) {
			player.setInputHandling(new SetPinPacketListener());
			player.getPacketSender().sendEnterInputPrompt("Enter the pin that you want to set$pin");
		}

		if (command[0].equalsIgnoreCase("donationdeals")) {
			player.getDonationDeals().displayReward();
			player.getDonationDeals().displayTime();
		}

		if (command[0].equalsIgnoreCase("handlerewards")) {
			player.getDonationDeals().handleRewards();
		}

		if (command[0].equalsIgnoreCase("donatetoday")) {
			int amount = Integer.parseInt(command[1]);

			player.incrementAmountDonatedToday(amount);
			player.sendMessage("@blu@Your total donated amount for today is: @red@" + player.getAmountDonatedToday());
			player.lastDonation = System.currentTimeMillis();
		}	
	/*	if (command[0].equalsIgnoreCase("referal")) {
			player.getRefferalHandler().openRefferal(player);
		}
*/
		if (command[0].equalsIgnoreCase("droptable")) {
			String npcName = command[1];
			NPCDropTableChecker.getSingleton().searchForNPC(player, npcName);
		}

		if (command[0].equalsIgnoreCase("lms")) {
			LastManStanding.enterLobby(player);
		}



		if (command[0].equalsIgnoreCase("wb")) {
			TeleportHandler.teleportPlayer(player, new Position(3038, 5348, 0),
					player.getSpellbook().getTeleportType());
		}
		if (command[0].equalsIgnoreCase("mb")) {
			TeleportHandler.teleportPlayer(player, new Position(3043, 3413, 0),
					player.getSpellbook().getTeleportType());
		}

		if (command[0].equalsIgnoreCase("keepsake")) {
			KeepSake.open(player, false);
		}

		if (command[0].equals("battlepass")) {
			if (player.hasBattlePass())
				;
			BattlePass.INSTANCE.openInterface(player);
		}

		if (command[0].equalsIgnoreCase("npctasks")) {
			NpcTasks.updateInterface(player);
			player.getPacketSender().sendInterfaceReset();
			player.getPacketSender().sendInterface(65400);
		}

		if (command[0].equalsIgnoreCase("may")) {
			TeleportHandler.teleportPlayer(player, new Position(2848, 3043, 0),
					player.getSpellbook().getTeleportType());
		}
		if (command[0].equalsIgnoreCase("worldboss")) {
			TeleportHandler.teleportPlayer(player, new Position(2410, 4679, 0),
					player.getSpellbook().getTeleportType());
		}

		if (command[0].equalsIgnoreCase("comparestats1")) {
			player.setDialogueActionId(750);
			player.getPacketSender().sendInterface(53500);
		}


		if (command[0].equalsIgnoreCase("bs")) {
			if (player.bloodFountain()) {
				TeleportHandler.teleportPlayer(player, new Position(2527, 4832, 0),
						player.getSpellbook().getTeleportType());
			}else {
				player.getPacketSender().sendMessage("@red@You must offer 20k blood bags to the offering table first");
			}
		}

		if (command[0].equalsIgnoreCase("drinfo")) {
			int bonus = DropUtils.drBonus(player, true);
			player.sendMessage("@blu@Your @red@Total @blu@ drop rate bonus is currently: " + bonus + "%");
		}

		if (command[0].equalsIgnoreCase("checkdr")) {
			Player target = World.getPlayerByName(command[1]);

			if (target == null) {
				player.sendMessage(command[1] + " player is currently offline");
				return;
			}

			player.sendMessage(target.getUsername() + " Dr bonus: @red@ " + DropUtils.drBonus(target, true) + "%");
		}

		if (command[0].equalsIgnoreCase("hasbonus")) {
			int itemID = Integer.parseInt(command[1]);
			ItemDefinition def = ItemDefinition.forId(itemID);
			for (int i = 0; i < def.getBonus().length; i++) {
				if (BonusManager.hasStats(itemID)) {
					System.out.println("This item does have stats");
				} else {
					System.out.println("All stats of this item are 0");
				}
			}
		}

		if (command[0].equalsIgnoreCase("clearblockedlist")) {
			player.getBlockedCollectorsList().clear();
			player.sendMessage("@red@Collector list is now unblocked");
		}

		if (command[0].equalsIgnoreCase("bossinfo")) {
			player.getPacketSender().sendInterface(36800);
		}

		if (command[0].equalsIgnoreCase("drop")) {
			player.getDropTableManager().open();
		}

		if (command[0].equalsIgnoreCase("drops")) {
			player.getDropTableManager().open();
		}

		if (command[0].equalsIgnoreCase("rankicons")) {
			player.getPacketSender().sendInterface(61500);
		}

		if (command[0].equalsIgnoreCase("war")) {
			if (player.getLastZulrah().elapsed(600000)) {
				TeleportHandler.teleportPlayer(player, new Position(2589, 4440, player.getIndex() * 4),
						player.getSpellbook().getTeleportType());
				Warmonger.start(player);
				player.setWarmonger(false);
				player.getLastZulrah().reset();
				player.setRegionInstance(new RegionInstance(player, RegionInstanceType.WARMONGER));
			} else {
				player.getPacketSender().sendMessage("You can only teleport here every 10 minutes!");
			}
		}

		if (command[0].startsWith("reward")) {
			if (command.length == 1) {
				player.getPacketSender()
						.sendMessage("Please use [::reward id], [::reward id amount], or [::reward id all].");
				return;
			}
			final String playerName = player.getUsername();
			final String id = command[1];
			final String amount = command.length == 3 ? command[2] : "1";

			Vote.service.execute(new Runnable() {
				@Override
				public void run() {
					try {
						Vote[] reward = Vote.reward(
								"A0fRA3GG2v0we8yCm0tnWcULR3LojfB7TkFyki38BrG6dAGzTAj1hthJ9DlksWJWGJs6Fjar", playerName,
								id, amount);
						if (reward[0].message != null) {
							player.getPacketSender().sendMessage(reward[0].message);
							return;
						}

						if (SpecialEvents.getDay() == SpecialEvents.MONDAY) {
							player.getInventory().add(reward[0].reward_id, reward[0].give_amount);
							player.getPacketSender()
									.sendMessage("@red@You get double vote scrolls because its Monday!!!!");
						}
						{
							if ((player.getSummoning() != null && player.getSummoning().getFamiliar() != null
									&& player.getSummoning().getFamiliar().getSummonNpc() != null
									&& player.getSummoning().getFamiliar().getSummonNpc().getId() == 1060)
									|| (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
											&& player.getSummoning().getFamiliar().getSummonNpc() != null
											&& player.getSummoning().getFamiliar().getSummonNpc().getId() == 809)) {
								player.getInventory().add(reward[0].reward_id, reward[0].give_amount);
								player.getPacketSender()
										.sendMessage("@red@ you get double votes because of your x2 vote pet!!!!");
							}
							player.getInventory().add(reward[0].reward_id, reward[0].give_amount);
							player.getAchievementTracker()
									.progress(com.arlania.world.content.achievements.AchievementData.DAILY_VOTER, 1);
							player.getPacketSender().sendMessage(
									"Thank you for voting! You now have " + reward[0].vote_points + " vote points.");
						}
					} catch (Exception e) {
						player.getPacketSender()
								.sendMessage("Api Services are currently offline. Please check back shortly");
						e.printStackTrace();
					}
				}
			});
		}

		if (command[0].equalsIgnoreCase("pos")) {
			if (player.isIronMan()) {
				player.getPacketSender()
						.sendMessage("Ironman-players are not allowed to buy items from the general-store.");
				return;
			}
			if (player.getLocation() == Location.DUNGEONEERING || player.getLocation() == Location.DUEL_ARENA) {
				player.getPacketSender().sendMessage("You can't open the player shops right now!");
			} else {
				player.getAchievementTracker()
						.progress(com.arlania.world.content.achievements.AchievementData.VISIT_POS, 1);

				player.getPlayerOwnedShopManager().options();
				return;
			}
		}

		if (command[0].equalsIgnoreCase("dismiss")) {
			if (player.getSummoning().getFamiliar() != null) {
				player.getSummoning().unsummon(true, true);
				player.getPacketSender().sendMessage("You've dismissed your familiar.");
			}
		}

		if (command[0].equalsIgnoreCase("pickup")) {
			if (player.getSummoning().getFamiliar() != null) {
				player.getSummoning().unsummon(true, true);
				player.getPacketSender().sendMessage("You've dismissed your familiar.");
			}
		}

		if (command[0].equalsIgnoreCase("commands")) {
			for (int i = 8145; i <= 8195; i++) {
				player.getPacketSender().sendString(i, "");
			}
			player.getPacketSender().sendString(8144, "@dre@       Helpful Commands on Pwnlite");
			player.getPacketSender().sendString(8146, "@dre@::help @blu@- contact staff for help");
			player.getPacketSender().sendString(8147, "@dre@::train @blu@- will take you to the starterzone");
			player.getPacketSender().sendString(8148, "@dre@::starterguide @blu@- will open a in depth starter guide");
			player.getPacketSender().sendString(8149, "@dre@::guides @blu@- will open all of the forum guides");
			player.getPacketSender().sendString(8150, "@dre@::dt @blu@- will open the drop tables interface");
			player.getPacketSender().sendString(8151, "@dre@::answer @blu@- to answer a trivia question");
			player.getPacketSender().sendString(8152, "@dre@::gamble @blu@- teleports you to the gamble area");
			player.getPacketSender().sendString(8153, "@dre@::vote @blu@- opens the voting page");
			player.getPacketSender().sendString(8154, "@dre@::donate @blu@- opens the donation page");
			player.getPacketSender().sendString(8155, "@dre@::forge @blu@- will open the forge interface");
			player.getPacketSender().sendString(8156, "@dre@::afk @blu@- will teleport you to the afk location");
			player.getPacketSender().sendString(8157, "@dre@::tele @blu@- will open the teleport interface");
			player.getPacketSender().sendString(8158, "@dre@::event @blu@- will teleport you to the seasonal event");
			player.getPacketSender().sendString(8159, "@dre@::chests @blu@- will teleport you to the chests area");
			player.getPacketSender().sendString(8160, "@dre@::sb @blu@- will teleport you to the starter boss");
			player.getPacketSender().sendString(8161, "@dre@::mb @blu@- will teleport you to the medium boss");
			player.getPacketSender().sendString(8162, "@dre@::hb @blu@- will teleport you to the hardened boss");
			player.getPacketSender().sendString(8163, "@dre@::eb @blu@- will teleport you to the expert boss");
			player.getPacketSender().sendString(8164, "@dre@::wb @blu@- will teleport you to the world boss");
			player.getPacketSender().sendString(8165,
					"@dre@::szone @blu@- will teleport you to the super donator zone");
			player.getPacketSender().sendString(8166,
					"@dre@::ob @blu@- will teleport you to the obsidian donators boss");
			player.getPacketSender().sendString(8167, "@dre@::mass @blu@- will teleport you to the mass event boss");
			player.getPacketSender().sendString(8168, "@dre@::giveaway @blu@- will teleport you to the giveaway zone");
			player.getPacketSender().sendString(8169, "@dre@::ds @blu@- will open the drop simulator interface");
			player.getPacketSender().sendString(8170, "@dre@::battlepass @blu@- will open the battlepass interface");
			player.getPacketSender().sendString(8171, "@dre@::salvage @blu@- will open the salvaging interface");
			player.getPacketSender().sendInterface(8134);
		}

		// if (command[0].equals("changebravek")) {
		// SlayerMaster.changeSlayerMaster(player, SlayerMaster.BRAVEK);
		// }

		if (command[0].equalsIgnoreCase("ob")) {
			if (player.getAmountDonated() >= 200) {
				TeleportHandler.teleportPlayer(player, new Position(3100, 5532, 0),
						player.getSpellbook().getTeleportType());
				player.getPacketSender().sendMessage("Welcome to Lugia, The Obsidian Boss!");
			} else if ((player.getSummoning() != null && player.getSummoning().getFamiliar() != null
					&& player.getSummoning().getFamiliar().getSummonNpc() != null
					&& player.getSummoning().getFamiliar().getSummonNpc().getId() == 12807)) {
				TeleportHandler.teleportPlayer(player, new Position(3100, 5532, 0),
						player.getSpellbook().getTeleportType());
				player.getPacketSender().sendMessage("<shad=155a91>Lugia Remembers the way to home");
			} else {
				player.sendMessage("@red@You need $200 Total donated to visit Lugia boss");
				return;
			}
		}

		if (command[0].equalsIgnoreCase("szone")) {
			if (player.getAmountDonated() >= 25) {
				TeleportHandler.teleportPlayer(player, new Position(2844, 3198, 0),
						player.getSpellbook().getTeleportType());
				player.getPacketSender().sendMessage("Welcome to The Super donator zone.");
			} else {
				player.sendMessage("@blu@You must be a Super donator to visit this zone.($25 claimed in game)");
				return;
			}
		}
		if (command[0].equalsIgnoreCase("uzone")) {
			if (player.getAmountDonated() >= 50) {
				TeleportHandler.teleportPlayer(player, new Position(3383, 4686, 0),
						player.getSpellbook().getTeleportType());
				player.getPacketSender().sendMessage("Welcome to The Ultra donator zone.");
			} else {
				player.sendMessage("@blu@You must be a Ultra donator to visit this zone. ($50 claimed in game)");
				return;
			}
		}
		if (command[0].equalsIgnoreCase("mzone")) {
			if (player.getAmountDonated() >= 125) {
				TeleportHandler.teleportPlayer(player, new Position(2837, 4152, 0),
						player.getSpellbook().getTeleportType());
				player.getPacketSender().sendMessage("Welcome to The Mystic donator zone.");
			} else {
				player.sendMessage("@blu@You must be a Mystic donator to visit this zone. ($125 claimed in game)");
				return;
			}
		}
		if (command[0].equalsIgnoreCase("giveaway")) {
			if (player.getAmountDonated() >= 0) {
				TeleportHandler.teleportPlayer(player, new Position(3355, 2830, 0),
						player.getSpellbook().getTeleportType());
				player.getPacketSender().sendMessage("Welcome to Giveaway Zone.");
			} else {
				player.sendMessage("@red@Good Luck");
				return;
			}
		}
		if (command[0].equals("endbuff")) {
			player.setNpcTransformationId(0);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if (command[0].equals("endsoul")) {
			player.setNpcTransformationId(0);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}

		if (command[0].equalsIgnoreCase("depositbags")) {
			int amount = Integer.parseInt(command[1]);
			if (player.getInventory().getAmount(10835) < amount) {
				player.sendMessage("You don't have that many Pwnlite Taxbags in your inventory.");
				return;
			}

			player.getInventory().delete(10835, amount);
			player.setMoneyInPouch(player.getMoneyInPouch() + (long) amount * 1);
			player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
			player.sendMessage(
					"You have deposited " + amount + " Pwnlite Taxbags for " + Misc.formatNumber((long) amount * 1L));
		}

		if (command[0].equalsIgnoreCase("withdrawbags")) {
			int amount = Integer.parseInt(command[1]);
			if (player.getMoneyInPouch() < (long) amount * 1L) {
				player.sendMessage("You don't enough in your pouch to withdraw that many Pwnlite Taxbags :/");
				player.sendMessage(
						"Maximum amount of Pwnlite Taxbags you can withdraw is: @red@" + player.getMoneyInPouch() / 1L);
				return;
			}
			player.setMoneyInPouch(player.getMoneyInPouch() - (long) amount * 1L);
			player.getInventory().add(10835, amount);
			player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
			player.sendMessage("You have withdrawed " + amount + " Pwnlite Taxbags");
		}

		if (command[0].equalsIgnoreCase("ffa")) {
			if (player.getSummoning().getFamiliar() != null) {
				player.sendMessage("@red@You cannot enter FFA with a pet on");
				return;
			}
			FreeForAll.enterLobby(player);
		}
		if (command[0].equals("players")) {
			PlayersOnlineInterface.showInterface(player);
		}

		/*
		 * if (command[0].equalsIgnoreCase("ge")) { GrandExchange.open(player); }
		 */

		if (wholeCommand.startsWith("droplog")) {
			final String[] s = wholeCommand.split(" ");
			if (s.length < 2) {
				PlayerDropLog.sendDropLog(player, player);
				return;
			}
			final String name = wholeCommand.substring(8);
			final Player other = World.getPlayerByName(name);
			if (other == null) {
				player.sendMessage("Player not found: " + name);
				return;
			}
			PlayerDropLog.sendDropLog(player, other);
		} else if (wholeCommand.startsWith("drojjjjjjp") && !wholeCommand.startsWith("droplog")) {
			final String[] s = wholeCommand.split(" ");
			if (s.length < 2) {
				player.sendMessage("Enter npc name!");
				return;
			}
			final String name = wholeCommand.substring(5).toLowerCase();

			if (NpcDefinition.forName(name) != null) {
				final int id = NpcDefinition.forName(name).getId();
				if (id == -1) {
					player.sendMessage("Npc not found: " + name);
					return;
				}
				MonsterDrops.sendNpcDrop(player, id, name);
			}
		}
		if (command[0].equalsIgnoreCase("answer")) {
			String triviaAnswer = wholeCommand.substring(7);
			if (TriviaBotHandler.acceptingQuestion()) {
				TriviaBotHandler.attemptAnswer(player, triviaAnswer);
			} else {
				player.sendMessage("[<col=255>Trivia</col>] TriviaBot currently has no question in which can be answered!");
			}
		}

		if (command[0].equalsIgnoreCase("event")) {

			TeleportHandler.teleportPlayer(player, new Position(3158, 9899, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender()
					.sendMessage("@red@Welcome to the Halloween event");
		}

		if (command[0].equalsIgnoreCase("qr")) {

			TeleportHandler.teleportPlayer(player, new Position(2527, 5407, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("@red@Welcome to the QR Zone - Scan the QR code on the minimap!");
		}

		//if (command[0].equalsIgnoreCase("hween")) {
		//	TeleportHandler.teleportPlayer(player, new Position(2597, 4816, 0),
			//		player.getSpellbook().getTeleportType());
		//	player.getPacketSender()
		//			.sendMessage("<img=552>@red@Welcome to the halloween event! Gather spooky bones to advance to room 2<img=552>");
		//}
		if (command[0].equalsIgnoreCase("squidgame") && player.getRights() == PlayerRights.OWNER) {
			TeleportHandler.teleportPlayer(player, new Position(3481, 4707, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender()
					.sendMessage("You teleport to the squidgames");
		}
		if (command[0].equalsIgnoreCase("squidgame") && player.getRights() == PlayerRights.DEVELOPER) {
			TeleportHandler.teleportPlayer(player, new Position(3481, 4707, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender()
					.sendMessage("You teleport to the squidgames");
		}
		if (command[0].equalsIgnoreCase("ytboss") && player.getRights() == PlayerRights.DEVELOPER) {
			TeleportHandler.teleportPlayer(player, new Position(3420, 4704, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender()
					.sendMessage("You teleport to the yt boss");
		}
		if (command[0].equalsIgnoreCase("ytboss") && player.getRights() == PlayerRights.OWNER) {
			TeleportHandler.teleportPlayer(player, new Position(3420, 4704, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender()
					.sendMessage("You teleport to the yt boss");
		}
		if (command[0].equalsIgnoreCase("addhweencuzlazy")) {
			player.incrementHweenKC(2500);
			player.getPacketSender()
					.sendMessage("@red@Because you are a Beast, 2500 Kills were added to your Halloween KillCount");
		}
		if (command[0].equalsIgnoreCase("notinuse")) {
			if (player.getSummoning().getFamiliar() != null) {
				player.sendMessage("@red@U have a pet on bye");
				return;
			}
			if (player.getUsername().equalsIgnoreCase("kech") || player.getUsername().equalsIgnoreCase("long john")
					|| player.getUsername().equalsIgnoreCase("glk") || player.getUsername().equalsIgnoreCase("ghrim")
					|| player.getUsername().equalsIgnoreCase("sir dope")
					|| player.getUsername().equalsIgnoreCase("andygal1960")
					|| player.getUsername().equalsIgnoreCase("prouddad")) {
				player.sendMessage("@red@You have requested a Gamble ban, Please contact Boomer to remove the ban");
				return;
			}

			TeleportHandler.teleportPlayer(player, new Position(3101, 2909, 0),
					player.getSpellbook().getTeleportType());
			ClanChatManager.leave(player, true);
			ClanChatManager.join(player, "notinuse2");
			player.getPacketSender().sendMessage("@red@Welcome to the Gambling zone");
		}

		if (command[0].equalsIgnoreCase("ninja")) {
			if (player.getUsername().equalsIgnoreCase("Boomer")
					|| player.getUsername().equalsIgnoreCase("121111")) {
				TeleportHandler.teleportPlayer(player, new Position(3037, 4000, 0),
						player.getSpellbook().getTeleportType());
				player.sendMessage("@red@You Teleport to a magical area - Purchased by Ninja himself.");
				return;
			}
		}
		if (command[0].equalsIgnoreCase("ninja2")) {
			if (player.getUsername().equalsIgnoreCase("Boomer")
					|| player.getUsername().equalsIgnoreCase("ninja")) {
				TeleportHandler.teleportPlayer(player, new Position(3037, 4000, 4),
						player.getSpellbook().getTeleportType());
				player.sendMessage("@red@You Teleport to a magical area - Purchased by Ninja himself.");
				return;
			}
		}
		if (command[0].equalsIgnoreCase("q1")) {
			if (player.getUsername().equalsIgnoreCase("Boomer")
					|| player.getUsername().equalsIgnoreCase("")) {
				TeleportHandler.teleportPlayer(player, new Position(3101, 4080, 0),
						player.getSpellbook().getTeleportType());
				player.sendMessage("@red@You Teleport to a magical area - Purchased by Q himself.");
				return;
			}
		}
		if (command[0].equalsIgnoreCase("q2")) {
			if (player.getUsername().equalsIgnoreCase("Boomer")
					|| player.getUsername().equalsIgnoreCase("1211")) {
				TeleportHandler.teleportPlayer(player, new Position(3101, 4080, 4),
						player.getSpellbook().getTeleportType());
				player.sendMessage("@red@You Teleport to a magical area - Purchased by Q himself.");
				return;
			}
		}
		if (command[0].equalsIgnoreCase("hui")) {
			if (player.getUsername().equalsIgnoreCase("Boomer")
					|| player.getUsername().equalsIgnoreCase("1211111")) {
				TeleportHandler.teleportPlayer(player, new Position(2910, 4129, 0),
						player.getSpellbook().getTeleportType());
				player.sendMessage("@red@You Teleport to a magical area - Purchased by Femboy himself.");
				return;
			}
		}
		if (command[0].equalsIgnoreCase("ween")) {
			if (player.getUsername().equalsIgnoreCase("Boomer")
					|| player.getUsername().equalsIgnoreCase("12111")) {
				TeleportHandler.teleportPlayer(player, new Position(2975, 4132, 0),
						player.getSpellbook().getTeleportType());
				player.sendMessage("@red@You Teleport to a magical area - Purchased by Big Ween himself.");
				return;
			}
		}


		if (command[0].equals("daily")) {
			DailyTasks.INSTANCE.openShopInterface(player);
		}

		if (wholeCommand.equalsIgnoreCase("forum") || wholeCommand.equalsIgnoreCase("site")) {
			player.getPacketSender().sendString(1, "http://Pwnlite317.com/community/");
			player.getPacketSender().sendMessage("Attempting to open: Pwnlite Forums");

		}
		if (wholeCommand.equalsIgnoreCase("rule") || wholeCommand.equalsIgnoreCase("rules")) {
			player.getPacketSender().sendString(1,
					"http://Pwnlite317.com/forum/index.php?/topic/33-please-read-rules/");
			player.getPacketSender().sendMessage("Attempting to open: Pwnlite Rules Forum Thread");

		}
		if (wholeCommand.equalsIgnoreCase("events")) {
			player.getPacketSender().sendString(1, "https://Pwnlite317.com/community/");
			player.getPacketSender().sendMessage("Attempting to open: Pwnlite Events Thread");
		}
		if (command[0].equals("zombie") || command[0].equals("zombies")) {
			TeleportHandler.teleportPlayer(player, new Position(3503, 3563), player.getSpellbook().getTeleportType());
		}

		if (wholeCommand.equalsIgnoreCase("benefit") || wholeCommand.equalsIgnoreCase("benefits")) {
			player.getPacketSender().sendString(1, "https://Pwnlite317.com/community/");
			player.getPacketSender().sendMessage("Attempting to open: Donation Information and benefits forum thread.");
		}

		if (wholeCommand.equalsIgnoreCase("starterguide") || wholeCommand.equalsIgnoreCase("iteminfo")) {
			player.getPacketSender().sendString(1, "http://Pwnlite317.com/forum/index.php?/topic/38-starter-guide/");
			player.getPacketSender().sendMessage("Attempting to open Ultimate Starter Guide Thread.");
		}

		if (command[0].equalsIgnoreCase("thread")) {
			String threadId = wholeCommand.substring(7);
			player.getPacketSender().sendMessage("Opening forums thread id: " + threadId);
			player.getPacketSender().sendString(1, "https://Pwnlite317.com/forum/index.php?/topic/" + threadId + "-");
		}

		if (command[0].equalsIgnoreCase("train")) {
			TeleportHandler.teleportPlayer(player, new Position(3795, 3550), player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to the starterzone!");
			player.getPacketSender().sendMessage("Type ::starterguide to open a beginners guide!");

		}
		if (command[0].equalsIgnoreCase("starter")) {
			TeleportHandler.teleportPlayer(player, new Position(3795, 3550), player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to the starterzone!");
			player.getPacketSender().sendMessage("Type ::starterguide to open a beginners guide!");
		}

		if (command[0].equalsIgnoreCase("dailyrewards")) {
			player.getDailyReward().openInterface();
		}

		if (command[0].equalsIgnoreCase("dbz")) {
			TeleportHandler.teleportPlayer(player, new Position(2976, 2781, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("@blu@Welcome to The DBZ Minigame, Collect keys to progress.");

		}

		if (command[0].equalsIgnoreCase("setemail")) {
			String email = wholeCommand.substring(9);
			player.setEmailAddress(email);
			player.getPacketSender().sendMessage("You set your account's email adress to: [" + email + "] ");
			PlayerPanel.refreshPanel(player);
		}

		if (command[0].equalsIgnoreCase("changepassword")) {
			String syntax = wholeCommand.substring(15);
			if (syntax == null || syntax.length() <= 2 || syntax.length() > 15 || !NameUtils.isValidName(syntax)) {
				player.getPacketSender().sendMessage("That password is invalid. Please try another password.");
				return;
			}
			if (syntax.contains("_")) {
				player.getPacketSender().sendMessage("Your password can not contain underscores.");
				return;
			}
			player.setPassword(syntax);
			player.getPacketSender().sendMessage("Your new password is: [" + syntax + "] Write it down!");

		}

		if (command[0].equalsIgnoreCase("dt")) {
			player.getDropTableManager().open();
		}

		if (command[0].equalsIgnoreCase("forge")) {
			player.getForgingManager().open();
		}


				//if (command[0].equalsIgnoreCase("ref")) {
			//player.getRefferalHandler().openRefferal(player);
		//}
		if (command[0].equalsIgnoreCase("home")) {
			TeleportHandler.teleportPlayer(player, new Position(3295, 4070, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("@blu@Welcome to the New Home area!");
		}
		if (command[0].equalsIgnoreCase("wardwisp")) {
			if (player.getSkillManager().getCurrentLevel(Skill.RUNECRAFTING) < 70) {
				player.getPacketSender().sendMessage("You need an Runecrafting lvl of 70  or higher to go here");
				return;
			}

			TeleportHandler.teleportPlayer(player, new Position(2596, 4772, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("@blu@Welcome to Ward Wisp!");

		}
		if (command[0].equalsIgnoreCase("claim")) {
            new java.lang.Thread() {
                public void run() {
                    try {
                        final com.teamgames.gamepayments.TransactionResponse gamepaymentsResponse = com.teamgames.gamepayments.Transaction.getResponse("SLjr0hl22cHxTJdoJiqARg5j04O2gcu3OeMShmAZR1kfTvGXpoE7LvVqXSVGNKg86JLn9hg0", player.getUsername());
                        com.teamgames.gamepayments.Transaction[] transactions = gamepaymentsResponse.getTransactions();
                        if (!gamepaymentsResponse.getMessage().equalsIgnoreCase("SUCCESS")) {
                            player.sendMessage(gamepaymentsResponse.getExtendedMessage());
                            return;
                        }
						float price = 0;
						float priceWithDeal = 0;
						int priceMathed = 0;
						HashMap<Integer, Integer> item = new HashMap<Integer, Integer>();
                        for (com.teamgames.gamepayments.Transaction transaction : transactions) {
							price += transaction.price;
							priceWithDeal += transaction.priceWithDiscount;
							item.put(Integer.valueOf(transaction.productId), transaction.quantity);
							switch (World.deals.getCurrentDeal()) {
								case BOGO:
									player.getInventory().add(Integer.valueOf(transaction.productId), transaction.quantity * 2);
								break;
								case BTGO:
									int extraAmount = (int) Math.floor(transaction.quantity / 2);
									player.getInventory().add(Integer.valueOf(transaction.productId), transaction.quantity + extraAmount);
								break;
								default:
									player.getInventory().add(Integer.valueOf(transaction.productId), transaction.quantity);
							}
                        }
						priceMathed = Math.round(price);
						DonationData.writeFile(player, item, priceMathed, priceWithDeal);
                        player.sendMessage("Thank you for donating!");
						if (priceMathed > 250) {
							World.sendChannelMessage("Donation", player.getUsername() + " has just made a MASSIVE! donation. Give them a thanks!");
						} else if (priceMathed >= 100) {
							World.sendChannelMessage("Donation", player.getUsername() + " has just made a huge donation. Give them a thanks!");
						} else if (priceMathed >= 50) {
							World.sendChannelMessage("Donation", player.getUsername() + " has just made a big donation. Give them a thanks!");
						} else {
							World.sendChannelMessage("Donation", player.getUsername() + " has just made a donation. Give them a thanks!");
						}
						if (World.deals.getCurrentDeal().equals(DonationDeals.Deals.$50_OR_MORE)) {
							int timesHitBonus = priceMathed / 50;
							for (int i = timesHitBonus; i > 0; i--) {
								boolean enough = player.getInventory().getFreeSlots() > timesHitBonus;
								if (enough) {
									player.getInventory().add(World.deals.getItemToGive().getId(), World.deals.getItemToGive().getAmount());
								} else {
									player.getBank().add(World.deals.getItemToGive().getId(), World.deals.getItemToGive().getAmount());
								}
							}
						}
						if (World.deals.getCurrentDeal().equals(DonationDeals.Deals.$75_OR_MORE)) {
							int timesHitBonus = priceMathed / 75;
							for (int i = timesHitBonus; i > 0; i--) {
								boolean enough = player.getInventory().getFreeSlots() > timesHitBonus;
								if (enough) {
									player.getInventory().add(World.deals.getItemToGive().getId(), World.deals.getItemToGive().getAmount());
								} else {
									player.getBank().add(World.deals.getItemToGive().getId(), World.deals.getItemToGive().getAmount());
								}
							}
						}
						player.incrementAmountDonated(priceMathed);
						MemberScrolls.checkForRankUpdate(player);
                    } catch (Exception e) {
                        player.sendMessage("Api Services are currently offline. Please check back shortly");
                        e.printStackTrace();
                    }
                }
            }.start();
        }
		if (command[0].equalsIgnoreCase("donate")) {
			player.getDonationDeals().displayReward();
			player.getDonationDeals().displayTime();
			player.getPacketSender().sendString(1, "https://pwnlite.gamepayments.net/category/248");
		}
		if (command[0].equalsIgnoreCase("store")) {
			player.getDonationDeals().displayReward();
			player.getDonationDeals().displayTime();
			player.getPacketSender().sendString(1, "https://pwnlite.gamepayments.net/category/257"); //https://app.gpay.io/store/Pwnlite/13541
		}
		if (command[0].equalsIgnoreCase("walkchaos")) {
			player.getPacketSender().sendString(1, "http://www.youtube.com/user/Walkchaos3");
		}
		if (command[0].equalsIgnoreCase("facebook")) {
			player.getPacketSender().sendString(1, "http://www.facebook.com/Pwnlite317");
		}

		if (command[0].equalsIgnoreCase("nahfam")) {
			player.getPacketSender().sendString(1, "https://www.twitch.tv/nahfam_official");
		}
		if (command[0].equalsIgnoreCase("perplexi")) {
			player.getPacketSender().sendString(1, "https://www.youtube.com/channel/UCtb_rEautI4iFM0ZVIB0lkw");
		}
		if (command[0].equalsIgnoreCase("bigzy")) {
			player.getPacketSender().sendString(1, "https://www.youtube.com/channel/UCkiMqw0NGlSYYja8l7u21dw");
		}
		if (command[0].equalsIgnoreCase("ceraxy")) {
			player.getPacketSender().sendString(1, "https://www.youtube.com/channel/UCe9NqnfPfqwWLv09Z3iXPgA");
		}

		if (command[0].equalsIgnoreCase("fpkmerk")) {
			player.getPacketSender().sendString(1, "http://www.youtube.com/channel/UCeVDQGzCI3dxp8NAT_YBIkQs3");
		}
		if (command[0].equalsIgnoreCase("frimb")) {
			player.getPacketSender().sendString(1, "http://www.youtube.com/c/Frimb/about");
		}

		if (command[0].equalsIgnoreCase("starterguide")) {
			player.getPacketSender().sendString(1, "http://Pwnlite317.com");
		}
		if (command[0].equalsIgnoreCase("guides")) {
			player.getPacketSender().sendString(1, "http://Pwnlite317.com");
		}
		if (command[0].equalsIgnoreCase("fe")) {
			player.getPacketSender().sendString(1, "http://www.youtube.com/user/RecklessCxn");
		}
		if (command[0].equalsIgnoreCase("vote")) {
			player.getPacketSender().sendString(1, "https://pwnlite.everythingrs.com/services/vote");
		}
		if (command[0].equalsIgnoreCase("discord")) {
			player.getPacketSender().sendString(1, "https://discord.gg/FfJG8HWDSH");
		}

		if (command[0].equalsIgnoreCase("maxhit")) {
			int attack = DesolaceFormulas.getMeleeAttack(player) / 10;
			int range = DesolaceFormulas.getRangedAttack(player) / 10;
			int magic = DesolaceFormulas.getMagicAttack(player) / 10;
			player.getPacketSender().sendMessage("@bla@Melee attack: @or2@" + attack + "@bla@, ranged attack: @or2@"
					+ range + "@bla@, magic attack: @or2@" + magic);
		}
		if (command[0].equals("save")) {
			player.save();
			player.getPacketSender().sendMessage("Your progress has been saved.");
		}

		if (command[0].equals("help")) {
			if (player.getLastYell().elapsed(30000)) {
				World.sendStaffMessage("<col=FF0066><img=10> [TICKET SYSTEM]<col=6600FF> " + player.getUsername()
						+ " has requested help. Please help them!");
				player.getLastYell().reset();
				player.getPacketSender()
						.sendMessage("<col=663300>Your help request has been received. Please be patient.");
			} else {
				player.getPacketSender().sendMessage("")
						.sendMessage("<col=663300>You need to wait 30 seconds before using this again.").sendMessage(
								"<col=663300>If it's an emergency, please private message a staff member directly instead.");
			}
		}

		if (command[0].equalsIgnoreCase("bis")) {
			player.getBis().open();
		}

		if (command[0].equals("empty")) {
			player.getPacketSender().sendInterfaceRemoval().sendMessage("You clear your inventory.");
			player.getSkillManager().stopSkilling();
			player.getInventory().resetItems().refreshItems();
		}

		if (command[0].equalsIgnoreCase("[cn]")) {
			if (player.getInterfaceId() == 40172) {
				ClanChatManager.setName(player, wholeCommand.substring(wholeCommand.indexOf(command[1])));
			}
		}
	}

	private static void superDonator(final Player player, String[] command, String wholeCommand) {

		if (command[0].equals("dzone")) {
			if (player.getRights().isStaff() || player.getRights() == PlayerRights.MYSTIC_DONATOR
					|| player.getRights() == PlayerRights.ULTRA_DONATOR
					|| player.getRights() == PlayerRights.SUPER_DONATOR || player.getRights() == PlayerRights.DONATOR)
				TeleportHandler.teleportPlayer(player, new Position(2142, 5376),
						player.getSpellbook().getTeleportType());
		}
	}

	private static void deluxeDonator(final Player player, String[] command, String wholeCommand) {

		if (!player.getRights().isDeluxeDonator(player))
			if (!player.getRights().isVipDonator(player))
				if (!player.getRights().isInvestorDonator(player))
					return;

		switch (command[0]) {

		case "setyelltitle": {
			player.setInputHandling(new EnterYellTitle());
			player.getPacketSender().sendEnterInputPrompt("Enter a new yell title:");
			return;
		}
		case "ref": {
			player.getRefferalHandler().openRefferal(player);
		}
				case "bank": {
			try {
				player.getBank(player.getCurrentBankTab()).open(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}

		case "hp": {
			if (player.lastHpRestore > System.currentTimeMillis()) {
				player.sendMessage("You can only use this command once every 15minutes!");
				player.sendMessage("You still need to wait another " + player.getTimeRemaining(player.lastHpRestore));
				return;
			}
			if (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) >= player.getSkillManager()
					.getMaxLevel(Skill.CONSTITUTION)) {
				player.sendMessage("You already have full hitpoints.");
				return;
			}
			player.sendMessage("You have restored your HP back to full.");
			player.lastHpRestore = System.currentTimeMillis() + 900000;// 15mins
			player.heal(player.getSkillManager().getMaxLevel(Skill.CONSTITUTION));
			return;
		}

		case "pray": {
			if (player.lastPrayerRestore > System.currentTimeMillis()) {
				player.sendMessage("You can only use this command once every 15minutes!");
				player.sendMessage(
						"You still need to wait another " + player.getTimeRemaining(player.lastPrayerRestore));
				return;
			}
			if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) >= player.getSkillManager()
					.getMaxLevel(Skill.PRAYER)) {
				player.sendMessage("You already have full prayer points.");
				return;
			}
			player.sendMessage("You have restored your Prayer Points back to full.");
			player.lastPrayerRestore = System.currentTimeMillis() + 900000;// 15mins
			player.getSkillManager().setCurrentLevel(Skill.PRAYER, player.getSkillManager().getMaxLevel(Skill.PRAYER));
			if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) > player.getSkillManager()
					.getMaxLevel(Skill.PRAYER))
				player.getSkillManager().setCurrentLevel(Skill.PRAYER,
						player.getSkillManager().getMaxLevel(Skill.PRAYER));
			return;
		}
		}
	}

	private static void uberDonator(final Player player, String[] command, String wholeCommand) {

		if (command[0].equals("bank")) {
			try {
				player.getBank(player.getCurrentBankTab()).open(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (command[0].equalsIgnoreCase("customtitle")) {
			String title = wholeCommand.substring(12);
			if (title.length() < 1 || title.length() > 21) {
				player.sendMessage("@red@Error - Try another title");
				return;
			}
			String color = command[1];
			switch (color) {
			case "orange":
				player.setTitle("@or2@" + title.replace(color, "").replace(" ", ""));
				break;
			case "yellow":
				player.setTitle("@yel@" + title.replace(color, "").replace(" ", ""));
				break;
			case "red":
				player.setTitle("@red@" + title.replace(color, "").replace(" ", ""));
				break;
			case "green":
				player.setTitle("@gre@" + title.replace(color, "").replace(" ", ""));
				break;
			case "blue":
				player.setTitle("@blu@" + title.replace(color, "").replace(" ", ""));
				break;
			}
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
	}

	private static void legendaryDonator(final Player player, String[] command, String wholeCommand) {
		/*
		 * if (command[0].equalsIgnoreCase("lzone")) { if (player.getAmountDonated() >=
		 * 500) { TeleportHandler.teleportPlayer(player, new Position(2652, 3262, 0),
		 * player.getSpellbook().getTeleportType());
		 * player.getPacketSender().sendMessage("Welcome to The Legendary donator zone."
		 * ); } else {
		 * player.sendMessage("@red@You must be a Legendary donator to visit this zone."
		 * ); return; } }
		 */
		if (command[0].equals("bank")) {
			try {
				player.getBank(player.getCurrentBankTab()).open(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void extremeDonator(final Player player, String[] command, String wholeCommand) {
		if (command[0].equalsIgnoreCase("title")) {
			String title = wholeCommand.substring(6);

			if (title == null || title.length() <= 1 || title.length() > 12 || !NameUtils.isValidName(title)) {
				player.getPacketSender().sendMessage("You can not set your title to that!");
				return;
			}
			if (command[0].equals("bank")) {
				try {
					player.getBank(player.getCurrentBankTab()).open(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// overriden permmited strings
			switch (player.getRights()) {
			case ADMINISTRATOR:
				for (String s : GameSettings.INVALID_NAMES) {
					if (Arrays.asList(admin).contains(s.toLowerCase())) {
						continue;
					}
					if (title.toLowerCase().contains(s.toLowerCase())) {
						player.getPacketSender().sendMessage("Your title contains an invalid tag.");
						return;
					}
				}
				break;
			case MODERATOR:
				for (String s : GameSettings.INVALID_NAMES) {
					if (Arrays.asList(mod).contains(s.toLowerCase())) {
						continue;
					}
					if (title.toLowerCase().contains(s.toLowerCase())) {
						player.getPacketSender().sendMessage("Your title contains an invalid tag.");
						return;
					}
				}
				break;

			// permitted to use whatever they'd like
			case OWNER:
			case DEVELOPER:
				break;
			default:
				for (String s : GameSettings.INVALID_NAMES) {
					if (title.toLowerCase().contains(s.toLowerCase())) {
						player.getPacketSender().sendMessage("Your title contains an invalid tag.");
						return;
					}
				}
				break;
			}
			player.setTitle("@red@" + title);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		/*
		 * if (command[0].equals("ezone")) { if (player.getRights().isStaff() ||
		 * player.getRights() == PlayerRights.OBSIDIAN_DONATOR || player.getRights() ==
		 * PlayerRights.MYSTIC_DONATOR || player.getRights() ==
		 * PlayerRights.ULTRA_DONATOR || player.getRights() ==
		 * PlayerRights.LEGENDARY_DONATOR
		 *
		 * )
		 *
		 * TeleportHandler.teleportPlayer(player, new Position(3429, 2919),
		 * player.getSpellbook().getTeleportType()); }
		 */
	}

	private static final String[] admin = { "admin", "administrator", "a d m i n" };
	private static final String[] mod = { "mod", "moderator", "m o d" };

	private static void memberCommands(final Player player, String[] command, String wholeCommand) {
		if (command[0].equals("dzone")) {
			TeleportHandler.teleportPlayer(player, new Position(3363, 9638), player.getSpellbook().getTeleportType());
		}

		if (wholeCommand.toLowerCase().startsWith("yell")) {
			if (!GameSettings.YELL_STATUS) {
				player.getPacketSender().sendMessage("Yell is currently unavailable");
				return;
			}
			if (PlayerPunishment.muted(player.getUsername()) || PlayerPunishment.IPMuted(player.getHostAddress())) {
				player.getPacketSender().sendMessage("You are muted and cannot yell.");
				return;
			}
			int delay = player.getRights().getYellDelay();
			if (!player.getLastYell().elapsed((delay * 1000))) {
				player.getPacketSender().sendMessage(
						"You must wait at least " + delay + " seconds between every yell-message you send.");
				return;
			}

			String yellMessage = wholeCommand.substring(4, wholeCommand.length());
			player.getLastYell().reset();

			if (player.getRights() == PlayerRights.EXECUTIVE_DONATOR) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img=412><shad=f4f4f4>[Executive Donator] "
						+ player.getUsername() + ":" + "<shad=0f0f0f>" + yellMessage + "");
				return;
			}

			if (player.getRights() == PlayerRights.SUPREME_DONATOR) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img=16><shad=b43c33>[Supreme Donator] "
						+ player.getUsername() + ":" + "<shad=822720>" + yellMessage + "");
				return;
			}

			if (player.getRights() == PlayerRights.DIVINE_DONATOR) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img=18><shad=890dbf>[Divine Donator] "
						+ player.getUsername() + ":" + "<shad=920c80>" + yellMessage + "");
				return;
			}

			if (player.getRights() == PlayerRights.CoolRank) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img=3><shad=890dbf>[Website Developer] "
						+ player.getUsername() + ":" + "<shad=020202>" + yellMessage + "");
				return;
			}

			if (player.getRights() == PlayerRights.CELESTIAL_DONATOR) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img=14><shad=ac23cf>[Celestial Donator] "
						+ player.getUsername() + ":" + "<shad=6c1683>" + yellMessage + "");
				return;
			}

			if (player.getRights() == PlayerRights.OWNER) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img=3><shad=fe0000>[Server Owner] "
						+ player.getUsername() + ":" + "<shad=020202>" + yellMessage + "");
				return;
			}
			if (player.getRights() == PlayerRights.CO_OWNER) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img=17><shad=fe0000>[Co Owner] "
						+ player.getUsername() + ":" + "<shad=020202>" + yellMessage + "");
				return;
			}
			if (player.getRights() == PlayerRights.DEVELOPER) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img=4><shad=890dbf>[Server Developer] "
						+ player.getUsername() + ":" + "<shad=020202>" + yellMessage + "");
				return;
			}
			if (player.getRights() == PlayerRights.SUPPORT) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img=10><shad=debb0e>[Server Support] "
						+ player.getUsername() + ":" + "</shad><shad=de3c0e>" + yellMessage + "");
				return;
			}
			if (player.getRights() == PlayerRights.MODERATOR) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img=1><shad=debb0e>[Server Mod] "
						+ player.getUsername() + ":" + "</shad><shad=de3c0e>" + yellMessage + "");
				return;
			}
			if (player.getRights() == PlayerRights.ADMINISTRATOR) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img=2><shad=debb0e>[Server Admin] "
						+ player.getUsername() + ":" + "</shad><shad=020202>" + yellMessage + "");
				return;
			}
			if (player.getRights() == PlayerRights.COMMUNITY_MANAGER) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img=510><shad=89cff0>[Community Manager] "
						+ player.getUsername() + ":" + "</shad><shad=020202>" + yellMessage + "");
				return;
			}
			if (player.getRights() == PlayerRights.EVENT_MANAGER) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img=511><shad=27a108>[Event Manager] "
						+ player.getUsername() + ":" + "</shad><shad=020202>" + yellMessage + "");
				return;
			}
			if (player.getRights() == PlayerRights.LEGENDARY_DONATOR) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img=13><shad=ebf217>[Legendary Donator] "
						+ player.getUsername() + ":" + "<shad=ebf217>" + yellMessage + "");
				return;
			}
			if (player.getRights() == PlayerRights.OBSIDIAN_DONATOR) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img=9><shad=343434>[Obsidian Donator] "
						+ player.getUsername() + ":" + "<shad=0f0f0f>" + yellMessage + "");
				return;
			}
			if (player.getRights() == PlayerRights.MYSTIC_DONATOR) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img=8><shad=ee22be>[Mystic Donator] "
						+ player.getUsername() + ":" + "<shad=c70c9a>" + yellMessage + "");
				return;
			}
			if (player.getRights() == PlayerRights.ULTRA_DONATOR) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img=7><shad=067fc6>[Ultra Donator] "
						+ player.getUsername() + ":" + "<shad=067fc6>" + yellMessage + "");
				return;
			}
			if (player.getRights() == PlayerRights.SUPER_DONATOR) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img=6><shad=31e421>[Super Donator] "
						+ player.getUsername() + ":" + "<shad=31e421>" + yellMessage + "");
				return;
			}
			if (player.getRights() == PlayerRights.DONATOR) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img=5><shad=fe0000>[Donator] "
						+ player.getUsername() + ":" + "<shad=fe0000>" + yellMessage + "");
				return;
			}
			if (player.getRights() == PlayerRights.YOUTUBER) {
				World.sendMessage("" + player.getRights().getYellPrefix() + "<img=11><shad=fe0000>[Youtuber] "
						+ player.getUsername() + ":" + "<shad=fe0000>" + yellMessage + "");
				return;
			}
			// TO-DO

		}

	}

	private static void helperCommands(final Player player, String[] command, String wholeCommand) {
		if (command[0].equalsIgnoreCase("kill")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(5));
			TaskManager.submit(new PlayerDeathTask(player2));
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " just ::killed " + player2.getUsername() + "!");
			player.getPacketSender().sendMessage("Killed player: " + player2.getUsername() + "");
			player2.getPacketSender().sendMessage("You have been Killed by " + player.getUsername() + ".");
		}
		if (command[0].equalsIgnoreCase("kick")) {
			String player2 = wholeCommand.substring(5);
			Player playerToKick = World.getPlayerByName(player2);

			if (playerToKick == null) {
				player.getPacketSender().sendConsoleMessage("Player " + player2 + " couldn't be found on Pwnlite.");
				return;
			}

			if (playerToKick.getRights().isAdmin() && !player.getRights().hasBanRights()) {
				player.sendMessage("You cannot kick administrators.");
				return;

			} else if (playerToKick.getLocation() != Location.WILDERNESS) {
				PlayerHandler.handleLogout(playerToKick, false, true);
				World.deregister(playerToKick);
				player.getPacketSender().sendConsoleMessage("Kicked " + playerToKick.getUsername() + ".");
				World.sendChannelMessage("Kick", player.getUsername() + " has just kicked " + playerToKick.getUsername() + " offline.");
				PlayerLogs.log(player.getUsername(),
						"" + player.getUsername() + " just kicked " + playerToKick.getUsername() + "!");
			}
		}

		if (command[0].equalsIgnoreCase("checkip")) {

			Player target = World.getPlayerByName(command[1]);
			player.sendMessage("@blu@" + target.getUsername() + "'s ip address: @red@" + target.getHostAddress());
			World.getPlayers().forEach(p -> {
				if (p.getHostAddress().equals(target.getHostAddress())
						&& !p.getUsername().equalsIgnoreCase(target.getUsername())) {
					player.sendMessage("@blu@User: " + p.getUsername() + " has the same ip as " + target.getUsername()
							+ "@red@ ( " + target.getHostAddress() + " )");
				}
			});

		}

		if (command[0].equalsIgnoreCase("checkmac")) {

			Player target = World.getPlayerByName(command[1]);
			player.sendMessage("@blu@" + target.getUsername() + "'s ip address: @red@" + target.getMacAddress());
			World.getPlayers().forEach(p -> {
				if (p.getMacAddress().equals(target.getMacAddress())
						&& !p.getUsername().equalsIgnoreCase(target.getUsername())) {
					player.sendMessage("@blu@User: " + p.getUsername() + " has the same Mac as " + target.getUsername()
							+ "@red@ ( " + target.getMacAddress() + " )");
				}
			});

		}

		if (command[0].equalsIgnoreCase("ffa-pure")) {
			FreeForAll.startEvent("pure");
		}

		if (command[0].equalsIgnoreCase("ffa-brid")) {
			FreeForAll.startEvent("brid");
		}
		if (command[0].equalsIgnoreCase("ffa-dharok")) {
			FreeForAll.startEvent("dharok");
		}

		if (command[0].equalsIgnoreCase("startcustomffa")) {
			CustomFreeForAll.startEvent("pure");
		}

		if (command[0].equalsIgnoreCase("entercustomffa")) {
			CustomFreeForAll.enterLobby(player);
		}

		if (command[0].equals("bank")) {
			try {
				player.getBank(player.getCurrentBankTab()).open(true);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		if (command[0].equals("remindvote")) {
			World.sendMessage(
					"<img=10>@blu@Remember to collect rewards by using the ::vote command every 12 hours!");
		}
		if (command[0].equals("remindstore")) {
			World.sendMessage("<img=10>@blu@Next over 20$ Donation will recieve a free Goodiebag 1-20 at Owner cape!");
		}
		if (command[0].equals("storedeal")) {
			World.sendMessage("<img=10>@blu@Next 25 Owner Box Donation gets 10 Boxes Extra");
		}
		if (command[0].equals("storedeal1")) {
			World.sendMessage("<img=10>@blu@Next $75+ Donation gets a free Fuzed Diamond!");
		}
		if (command[0].equals("remindvoting")) {
			World.sendMessage(
					"<img=10>@blu@Did you know that The voting is currently tripled? Vote for 9 points now");
		}

		if (command[0].equals("staffzone")) {
			if (command.length > 1 && command[1].equals("all")) {
				for (Player players : World.getPlayers()) {
					if (players != null) {
						if (players.getRights().isStaff()) {
							TeleportHandler.teleportPlayer(players, new Position(2013, 4501), TeleportType.NORMAL);
						}
					}
				}
			} else {
				TeleportHandler.teleportPlayer(player, new Position(2013, 4501), TeleportType.NORMAL);
			}
		}


		if (command[0].equalsIgnoreCase("saveall")) {
			World.savePlayers();
			player.getPacketSender().sendMessage("Saved players!");
		}

		if (command[0].equalsIgnoreCase("teleto")) {
			String playerToTele = wholeCommand.substring(7);
			Player player2 = World.getPlayerByName(playerToTele);
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
				return;
			} else {
				if (player2.getUsername().equalsIgnoreCase("emeraldgang3l")) {
					player.getPacketSender().sendMessage("@red@Fgt u must ask before teleing to me LUL gucci gang btw");
					player.getPacketSender().sendMessage("@red@Fgt Btw i spend em reast on new chain");
					player.getPacketSender().sendMessage("@red@Fgt Sup btw, idk what? u little nerd lul");
					player.getPacketSender().sendMessage("@or2@Fgt Tbh the bobby shmurda song hot nigga is quite good");
					player.getPacketSender().sendMessage("@or2@Fgt ye ik u agree u newb");
					player.getPacketSender().sendMessage("@or2@Fgt Tbh yee maybe u can tele to me eh?");
					player.getPacketSender()
							.sendMessage("@or2@Fgt nah, Btw , gucci gang gucci gang, Liiil Pumppeh kek");
					return;

				}
				boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy())
						&& player.getRegionInstance() == null && player2.getRegionInstance() == null;
				if (player.getRights() == PlayerRights.SUPPORT || player.getRights() == PlayerRights.MODERATOR) {
					if (player2.getMinigameAttributes().getDungeoneeringAttributes().getParty() != null) {
						player.sendMessage("you can't teleport to someone who is in dungeonnering");
						return;
					}
				}

				if (canTele) {
					TeleportHandler.teleportPlayer(player, player2.getPosition().copy(), TeleportType.NORMAL);
					player.getPacketSender().sendConsoleMessage("Teleporting to player: " + player2.getUsername() + "");
				} else {
					player.getPacketSender()
							.sendConsoleMessage("You can not teleport to this player at the moment. Minigame maybe?");
				}
			}
		}

		if (command[0].equalsIgnoreCase("movehome")) {
			String player2 = command[1];
			player2 = Misc.formatText(player2.replaceAll("_", " "));
			if (command.length >= 3 && command[2] != null) {
				player2 += " " + Misc.formatText(command[2].replaceAll("_", " "));
			}
			Player playerToMove = World.getPlayerByName(player2);
			if (playerToMove != null) {
				playerToMove.moveTo(GameSettings.DEFAULT_POSITION.copy());
				playerToMove.getPacketSender()
						.sendMessage("You've been teleported home by " + player.getUsername() + ".");
				player.getPacketSender()
						.sendConsoleMessage("Sucessfully moved " + playerToMove.getUsername() + " to home.");
			}
		}

	}

	private static boolean superiorCheck(Player player, Player target) {
		// TODO Auto-generated method stub
		return false;
	}

	private static void moderatorCommands(final Player player, String[] command, String wholeCommand) {

		if (command[0].equals("ban")) {
			String playerToBan = wholeCommand.substring(4);
			if (!PlayerSaving.playerExists(playerToBan)) {
				player.getPacketSender().sendConsoleMessage("Player " + playerToBan + " does not exist.");
				return;
			} else {
				if (PlayerPunishment.banned(playerToBan)) {
					player.getPacketSender()
							.sendConsoleMessage("Player " + playerToBan + " already has an active ban.");
					return;
				}
				PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " just banned " + playerToBan + "!");
				PlayerPunishment.ban(playerToBan);
				player.getPacketSender().sendConsoleMessage(
						"Player " + playerToBan + " was successfully banned. Command logs written.");
				Player toBan = World.getPlayerByName(playerToBan);
				World.deregister(player);
				if (toBan != null) {
					World.deregister(toBan);
				}
			}
			return;
		}

		if (command[0].equalsIgnoreCase("triplexp")) {
			GameSettings.TRIPLE_EXP = true;
			World.sendMessage("@red@TRIPLE XP HAS STARTED (TIME: 15mins)");
			Timer t = new Timer();
			t.schedule(new TimerTask() {

				@Override
				public void run() {
					GameSettings.TRIPLE_EXP = false;
					World.sendMessage("@red@TRIPLE XP HAS ENDED!");
				}
			}, 900000);
		}



		if (command[0].equals("tele")) {
			int x = Integer.valueOf(command[1]), y = Integer.valueOf(command[2]);
			int z = player.getPosition().getZ();
			if (command.length > 3) {
				z = Integer.valueOf(command[3]);
			}
			Position position = new Position(x, y, z);
			player.moveTo(position);
			player.getPacketSender().sendConsoleMessage("Teleporting to " + position.toString());
		}

		if (command[0].equals("sql")) {
			MySQLController.toggle();
			if (player.getRights() == PlayerRights.DEVELOPER) {
				player.getPacketSender().sendMessage("Sql toggled to status: " + GameSettings.MYSQL_ENABLED);
			} else {
				player.getPacketSender().sendMessage("Sql toggled to status: " + GameSettings.MYSQL_ENABLED + ".");
			}
		}

		if (command[0].equalsIgnoreCase("toggleinvis")) {
			player.setNpcTransformationId(player.getNpcTransformationId() > 0 ? -1 : 8254);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}

		if (command[0].equalsIgnoreCase("ipban")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(9));
			World.deregister(player2);
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Could not find that player online.");
				return;
			} else {
				if (PlayerPunishment.IPBanned(player2.getHostAddress())) {
					player.getPacketSender().sendConsoleMessage(
							"Player " + player2.getUsername() + "'s IP is already banned. Command logs written.");
					return;
				}
				final String bannedIP = player2.getHostAddress();
				PlayerPunishment.addBannedIP(bannedIP);
				player.getPacketSender().sendConsoleMessage(
						"Player " + player2.getUsername() + "'s IP was successfully banned. Command logs written.");
				for (Player playersToBan : World.getPlayers()) {
					if (playersToBan == null) {
						continue;
					}
					if (playersToBan.getHostAddress() == bannedIP) {
						PlayerLogs.log(player.getUsername(),
								"" + player.getUsername() + " just IPBanned " + playersToBan.getUsername() + "!");
						World.deregister(playersToBan);
						if (player2.getUsername() != playersToBan.getUsername()) {
							player.getPacketSender().sendConsoleMessage("Player " + playersToBan.getUsername()
									+ " was successfully IPBanned. Command logs written.");
						}
					}
				}
			}
		}
		if (command[0].equalsIgnoreCase("gpay")) {
			GpayDonationManager.gpay(player,player.getUsername());
		}
		
		if (command[0].equalsIgnoreCase("unipmute")) {
			player.getPacketSender().sendConsoleMessage("Unipmutes can only be handled manually.");
		}
		if (command[0].equalsIgnoreCase("teletome")) {
			String playerToTele = wholeCommand.substring(9);
			Player player2 = World.getPlayerByName(playerToTele);
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
				return;
			} else {
				boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy())
						&& player.getRegionInstance() == null && player2.getRegionInstance() == null;
				if (canTele) {
					TeleportHandler.teleportPlayer(player2, player.getPosition().copy(), TeleportType.NORMAL);
					player.getPacketSender()
							.sendConsoleMessage("Teleporting player to you: " + player2.getUsername() + "");
					player2.getPacketSender().sendMessage("You're being teleported to " + player.getUsername() + "...");
				} else {
					player.getPacketSender().sendConsoleMessage(
							"You can not teleport that player at the moment. Maybe you or they are in a minigame?");
				}
			}
		}
		if (command[0].equalsIgnoreCase("movetome")) {
			String playerToTele = wholeCommand.substring(9);
			Player player2 = World.getPlayerByName(playerToTele);
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player..");
				return;
			} else {
				boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy())
						&& player.getRegionInstance() == null && player2.getRegionInstance() == null;
				if (canTele) {
					player.getPacketSender().sendConsoleMessage("Moving player: " + player2.getUsername() + "");
					player2.getPacketSender().sendMessage("You've been moved to " + player.getUsername());
					player2.moveTo(player.getPosition().copy());
				} else {
					player.getPacketSender()
							.sendConsoleMessage("Failed to move player to your coords. Are you or them in a minigame?");
				}
			}
		}
	}

	private static void administratorCommands(final Player player, String[] command, String wholeCommand) {

		if (command[0].equalsIgnoreCase("ipmute")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(10));
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Could not find that player online.");
				return;
			} else {
				if (PlayerPunishment.IPMuted(player2.getHostAddress())) {
					player.getPacketSender().sendConsoleMessage(
							"Player " + player2.getUsername() + "'s IP is already IPMuted. Command logs written.");
					return;
				}
				final String mutedIP = player2.getHostAddress();
				PlayerPunishment.addMutedIP(mutedIP);
				player.getPacketSender().sendConsoleMessage(
						"Player " + player2.getUsername() + " was successfully IPMuted. Command logs written.");
				player2.getPacketSender().sendMessage("You have been IPMuted by " + player.getUsername() + ".");
				PlayerLogs.log(player.getUsername(),
						"" + player.getUsername() + " just IPMuted " + player2.getUsername() + "!");
			}
		}

		if (command[0].equalsIgnoreCase("cpuban")) {
			Player player2 = PlayerHandler.getPlayerForName(wholeCommand.substring(10));
			if (player2 != null && player2.getSerialNumber() != null) { //
				ConnectionHandler.banComputer(player2.getUsername(), player2.getSerialNumber());
				player.getPacketSender().sendConsoleMessage(player2.getUsername() + " has been CPU-banned.");
			} else
				player.getPacketSender().sendConsoleMessage("Could not CPU-ban that player.");
		}
		// works !

		if (command[0].contains("host")) {
			String plr = wholeCommand.substring(command[0].length() + 1);
			Player playr2 = World.getPlayerByName(plr);
			if (playr2 != null) {
				player.getPacketSender().sendConsoleMessage("" + playr2.getUsername() + " host IP: "
						+ playr2.getHostAddress() + ", serial number: " + playr2.getSerialNumber());
			} else {
				player.getPacketSender().sendConsoleMessage("Could not find player: " + plr);
			}
		}

		if (command[0].contains("macban")) {

			String name = Misc.formatPlayerName(wholeCommand.substring(7));

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Unable to find player: " + name);
				return;
			}

			if (MACBanL.ban(target, target != null)) {
				player.sendMessage("Succesfully mac banned " + target.getUsername());
			} else {
				player.sendMessage(
						"Could not mac " + target.getUsername() + " as no mac provided. Try ::ban or ::ipban instead.");
			}
		}

		if (command[0].equalsIgnoreCase("donamount")) {
			String name = wholeCommand.substring(10);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {

				player.getPacketSender().sendMessage("Player has donated: " + target.getAmountDonated() + " Dollars.");
			}
		}

		if (command[0].equals("reset")) {
			for (Skill skill : Skill.values()) {
				int level = skill.equals(Skill.CONSTITUTION) ? 100 : skill.equals(Skill.PRAYER) ? 10 : 1;
				player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
						SkillManager.getExperienceForLevel(skill == Skill.CONSTITUTION ? 10 : 1));
			}
			player.getPacketSender().sendConsoleMessage("Your skill levels have now been reset.");
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
	}

	private static int entries = 0;

	private static void ownerCommands(final Player player, String[] command, String wholeCommand) {

		if (wholeCommand.equalsIgnoreCase("testflub")) {
			System.out.println("Location: " + player.getLocation().name());
		}

		if (wholeCommand.equalsIgnoreCase("referrals")) {
			ReferralHandler.checkReferralList();
		}

		if (wholeCommand.equalsIgnoreCase("reloadreferrals")) {
			ReferralHandler.init();
		}

		if (wholeCommand.equalsIgnoreCase("checksites")) {
			DigEventHandler.checkSites(player);
		}

		if (command[0].equalsIgnoreCase("startdig")) {
			try {
				DigEventHandler.startGame(Integer.parseInt(command[1]));
			} catch (Exception e) {
				player.getPA().sendMessage("@red@Error! Usage: startdig [number of dig sites] - e.g. startdig 444");
			}
		}

		if (wholeCommand.equalsIgnoreCase("stopdig")) {
			DigEventHandler.stopGame();
		}

		if (wholeCommand.equalsIgnoreCase("checkdiggers")) {
			DigEventHandler.checkLeaderBoard();
		}

		if (wholeCommand.equals("afk")) {
			World.sendMessage("<img=10> <col=FF0000><shad=0>" + player.getUsername()
					+ ": I am now away, please don't message me; I won't reply.");
		}

		if (command[0].equalsIgnoreCase("removeAllEntries")) {
			if (player.getUsername().equalsIgnoreCase("emerald")) {
				entries = 0;
				World.getPlayers().forEach(players -> {
					players.hasEntered = false;
				});
			}
		}
		
		if(command[0].equals("backup")) {
			CharacterBackup.timer.reset(0);
			player.getPacketSender().sendConsoleMessage("Backup Method Called.");
		}

		if (command[0].equalsIgnoreCase("alltome")) {
			Position myPosition = player.getPosition();
			World.getPlayers().forEach(x -> {
				TeleportHandler.teleportPlayer(x, myPosition, x.getSpellbook().getTeleportType());
			});
		}

		if (command[0].equalsIgnoreCase("alert")) {
			String msg = "";
			for (int i = 1; i < command.length; i++) {
				msg += command[i] + " ";
			}
			World.sendMessage("Alert##Alert##" + msg + "## ");
		}
		
		if (command[0].equalsIgnoreCase("givedon")) {

			String name = wholeCommand.substring(8);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.DONATOR);
				target.getPacketSender().sendRights();
				target.incrementAmountDonated(10);
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "Donator Rank.");
			}
		}
		if (command[0].equals("master")) {
			for (Skill skill : Skill.values()) {
				int level = SkillManager.getMaxAchievingLevel(skill);
				player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
						SkillManager.getExperienceForLevel(level == 120 ? 120 : 99));
			}
			player.getPacketSender().sendConsoleMessage("You are now a master of all skills.");
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if (command[0].equals("rights")) {
			if (player.getUsername().equalsIgnoreCase("emerald")) {
				int rankId = Integer.parseInt(command[1]);
				if (player.getUsername().equalsIgnoreCase("server") && rankId != 10) {
					player.getPacketSender().sendMessage("You cannot do that.");
					return;
				}
				Player target = World
						.getPlayerByName(wholeCommand.substring(rankId >= 10 ? 10 : 9, wholeCommand.length()));
				if (target == null) {
					player.getPacketSender().sendConsoleMessage("Player must be online to give them rights!");
				} else {
					target.setRights(PlayerRights.forId(rankId));
					target.getPacketSender().sendMessage("Your player rights have been changed.");
					target.getPacketSender().sendRights();
				}
			}
		}
		if (command[0].equals("emptyitem")) {
			if (player.getInterfaceId() > 0
					|| player.getLocation() != null && player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			int item = Integer.parseInt(command[1]);
			int itemAmount = player.getInventory().getAmount(item);
			Item itemToDelete = new Item(item, itemAmount);
			player.getInventory().delete(itemToDelete).refreshItems();
		}
		if (command[0].equals("totalgold")) {
			Player p = World.getPlayerByName(wholeCommand.substring(5));
			if (p != null) {
				long gold = 0;
				for (Item item : p.getInventory().getItems()) {
					if (item != null && item.getId() > 0 && item.tradeable()) {
						gold += item.getDefinition().getValue();
					}
				}
				for (Item item : p.getEquipment().getItems()) {
					if (item != null && item.getId() > 0 && item.tradeable()) {
						gold += item.getDefinition().getValue();
					}
				}
				for (int i = 0; i < 9; i++) {
					for (Item item : p.getBank(i).getItems()) {
						if (item != null && item.getId() > 0 && item.tradeable()) {
							gold += item.getDefinition().getValue();
						}
					}
				}
				gold += p.getMoneyInPouch();
				player.getPacketSender().sendMessage(
						p.getUsername() + " has " + Misc.insertCommasToNumber(String.valueOf(gold)) + " coins.");
			} else {
				player.getPacketSender().sendMessage("Can not find player online.");
			}
		}

		if (command[0].equals("cashineco")) {
			int gold = 0, plrLoops = 0;
			for (Player p : World.getPlayers()) {
				if (p != null) {
					for (Item item : p.getInventory().getItems()) {
						if (item != null && item.getId() > 0 && item.tradeable()) {
							gold += item.getDefinition().getValue();
						}
					}
					for (Item item : p.getEquipment().getItems()) {
						if (item != null && item.getId() > 0 && item.tradeable()) {
							gold += item.getDefinition().getValue();
						}
					}
					for (int i = 0; i < 9; i++) {
						for (Item item : player.getBank(i).getItems()) {
							if (item != null && item.getId() > 0 && item.tradeable()) {
								gold += item.getDefinition().getValue();
							}
						}
					}
					gold += p.getMoneyInPouch();
					plrLoops++;
				}
			}
			player.getPacketSender().sendMessage(
					"Total bag in economy right now: " + gold + ", went through " + plrLoops + " players items.");
		}

		if (command[0].equalsIgnoreCase("emptypouch")) {
			String name = wholeCommand.substring(11);
			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is offline");
			} else {
				target.setMoneyInPouch(0);
			}

		}
		if (command[0].equals("setlev")) {
			String name = wholeCommand.substring(8);
			Player target = World.getPlayerByName(name);
			int skillId = Integer.parseInt(command[1]);
			int level = Integer.parseInt(command[2]);
			if (level > 15000) {
				player.getPacketSender().sendConsoleMessage("You can only have a maxmium level of 15000.");
				return;
			}
			Skill skill = Skill.forId(skillId);
			target.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
					SkillManager.getExperienceForLevel(level));
			player.getPacketSender().sendConsoleMessage("You have set his " + skill.getName() + " level to " + level);
		}
		if (command[0].equalsIgnoreCase("givedon1")) {
			String name = wholeCommand.substring(9);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.SUPER_DONATOR);
				target.getPacketSender().sendRights();
				target.incrementAmountDonated(25);
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "Donator Rank.");
			}
		}
		if (command[0].equalsIgnoreCase("givedon2")) {
			String name = wholeCommand.substring(9);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.ULTRA_DONATOR);
				target.getPacketSender().sendRights();
				target.incrementAmountDonated(50);
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "Donator Rank.");
			}
		}

		if (command[0].equalsIgnoreCase("givedon3")) {
			String name = wholeCommand.substring(9);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.MYSTIC_DONATOR);
				target.getPacketSender().sendRights();
				target.incrementAmountDonated(125);
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "Donator Rank.");
			}
		}
		if (command[0].equalsIgnoreCase("givedon4")) {
			String name = wholeCommand.substring(9);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.OBSIDIAN_DONATOR);
				target.getPacketSender().sendRights();
				target.incrementAmountDonated(200);
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "Donator Rank.");
			}
		}
		if (command[0].equalsIgnoreCase("givess")) {
			String name = wholeCommand.substring(7);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.SUPPORT);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "support.");
			}
		}
		if (command[0].equalsIgnoreCase("giveem")) {
			String name = wholeCommand.substring(7);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.EVENT_MANAGER);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "Event manager rank.");
			}
		}
		if (command[0].equalsIgnoreCase("givecm")) {
			String name = wholeCommand.substring(7);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.COMMUNITY_MANAGER);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "Community manager rank.");
			}
		}
		if (command[0].equalsIgnoreCase("givecm")) {
			String name = wholeCommand.substring(7);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.COMMUNITY_MANAGER);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "Community manager rank.");
			}
		}
		if (command[0].equalsIgnoreCase("tsql")) {
			MySQLController.toggle();
			player.getPacketSender().sendMessage("Sql toggled to status: " + GameSettings.MYSQL_ENABLED);

		}
		if (command[0].equalsIgnoreCase("givemod")) {
			String name = wholeCommand.substring(8);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.MODERATOR);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "mod.");
			}
		}
		if (command[0].equalsIgnoreCase("giveadmin")) {
			String name = wholeCommand.substring(10);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.ADMINISTRATOR);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "admin.");
			}
		}
		if (command[0].equalsIgnoreCase("giveyt")) {
			String name = wholeCommand.substring(7);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.YOUTUBER);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "yt.");
			}
		}
		if (command[0].equalsIgnoreCase("demote")) {
			String name = wholeCommand.substring(7);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.PLAYER);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "player.");
			}
		}

		if (command[0].equals("giveitem")) {
			int item = Integer.parseInt(command[1]);
			int amount = Integer.parseInt(command[2]);
			String rss = command[3];
			if (command.length > 4) {
				rss += " " + command[4];
			}
			if (command.length > 5) {
				rss += " " + command[5];
			}
			Player target = World.getPlayerByName(rss);
			if (target == null) {
				player.getPacketSender().sendConsoleMessage("Player must be online to give them stuff!");
			} else {
				player.getPacketSender().sendConsoleMessage("You give " + target + " " + amount + " " + item + ".");
				target.getInventory().add(item, amount);
			}
		}

		if (command[0].equalsIgnoreCase("giveall")) {
			int item = Integer.parseInt(command[1]);
			int amount = Integer.parseInt(command[2]);

			for (Player target : World.getPlayers()) {
				if (target == null) {
					continue;
				}
				target.getInventory().add(item, amount);
			}

			World.sendMessage(
					"@blu@" + player.getUsername() + " gave all: x " + amount + ItemDefinition.forId(item).getName());

		}

		if (command[0].equals("update")) {
			int time = Integer.parseInt(command[1]);
			World.sendMessage("@red@" + player.getUsername() + " has initiated a server update!"); // Debugging /
																									// Finding Attack
																									// Exploit
			if (time > 0) {
				GameServer.setUpdating(true);
				for (Player players : World.getPlayers()) {
					if (players == null) {
						continue;
					}
					players.getPacketSender().sendSystemUpdate(time);
				}
				TaskManager.submit(new Task(time) {
					@Override
					protected void execute() {
						for (Player player : World.getPlayers()) {
							if (player != null) {
								World.deregister(player);
							}
						}
						WellOfGoodwill.save();
						WellOfWealth.save();
						GrandExchangeOffers.save();
						ClanChatManager.save();
						ShopManager.saveTaxShop();
						ShopManager.saveTaxShop1();
						GameServer.getLogger().info("Update task finished!");
						stop();
					}
				});
			}
		}
		if (command[0].contains("host")) {
			String plr = wholeCommand.substring(command[0].length() + 1);
			Player playr2 = World.getPlayerByName(plr);
			if (playr2 != null) {
				player.getPacketSender().sendConsoleMessage("" + playr2.getUsername() + " host IP: "
						+ playr2.getHostAddress() + ", serial number: " + playr2.getSerialNumber());
			} else {
				player.getPacketSender().sendConsoleMessage("Could not find player: " + plr);
			}
		}
	}

	private static void developerCommands(Player player, String[] command, String wholeCommand) {
		if (command[0].equalsIgnoreCase("sitem") || command[0].equalsIgnoreCase("snpc") ||
			    command[0].equalsIgnoreCase("ositem") || command[0].equalsIgnoreCase("osnpc") ||
			    command[0].equalsIgnoreCase("osobject")) {

			    new Thread() {
			        public void run() {
			            synchronized(player) {
			                try {

			                    String query = command[1];
			                    com.everythingrs.commands.Search[] searchResults = com.everythingrs.commands.Search
			                        .searches("A0fRA3GG2v0we8yCm0tnWcULR3LojfB7TkFyki38BrG6dAGzTAj1hthJ9DlksWJWGJs6Fjar", command[0], query);
			                    if (searchResults.length > 0)
			                        if (searchResults[0].message != null) {
			                            player.getPacketSender().sendMessage(searchResults[0].message);
			                            return;
			                        }
			                    player.getPacketSender().sendMessage("-------------------");
			                    for (com.everythingrs.commands.Search search: searchResults) {
			                        player.getPacketSender().sendMessage(search.name + ":" + search.id);
			                    }
			                    player.getPacketSender()
			                        .sendMessage("Finished search with " + searchResults.length + " results");
			                    player.getPacketSender().sendMessage("-------------------");
			                } catch (Exception e) {
			                    player.getPacketSender()
			                        .sendMessage("Api Services are currently offline. Please check back shortly");
			                    e.printStackTrace();
			                }
			            }
			        }
			    }.start();
			}
		if (command[0].equalsIgnoreCase("thisis")) {
			player.getPacketSender().sendAnnouncement(command[1]);
		}
		if (command[0].equalsIgnoreCase("dismissmini")) {
			player.getMinimeSystem().despawn();
		}

		if (command[0].equalsIgnoreCase("ddeal")) {
			String deal = command[1];
			switch (deal) {
				case "none":
					World.deals.setCurrentDeal(DonationDeals.Deals.NONE);
					World.deals.setItemToGive(new Item(-1, -1));
					World.sendChannelMessage("Deals", "The current donation deal has ended");
					break;
				case "bogo":
					World.deals.setCurrentDeal(DonationDeals.Deals.BOGO);
					World.sendChannelMessage("Deals", "Buy one get one has been activated on everything in the store");
					break;
				case "btgo":
					World.deals.setCurrentDeal(DonationDeals.Deals.BTGO);
					World.sendChannelMessage("Deals", "Buy two get one has been activated on everything in the store");
					break;
				case "50":
					try {
						int itemId = Integer.parseInt(command[2]);
						int quantity = Integer.parseInt(command[3]);
						World.deals.setCurrentDeal(DonationDeals.Deals.$50_OR_MORE);
						World.sendChannelMessage("Deals", "Spend $50 or more and get a bonus of: " + quantity + (quantity == 1 ? " " : "x ") + ItemDefinition.forId(itemId).getName());
					} catch(Exception e) {
						player.sendMessage("Use as ::ddeal 50 itemID quantity");
					}
					break;
				case "75":
					try {
						int itemId = Integer.parseInt(command[2]);
						int quantity = Integer.parseInt(command[3]);
						World.deals.setCurrentDeal(DonationDeals.Deals.$75_OR_MORE);
						World.sendChannelMessage("Deals", "Spend $75 or more and get a bonus of: " + quantity + (quantity == 1 ? " " : "x ") + ItemDefinition.forId(itemId).getName());
					} catch(Exception e) {
						player.sendMessage("Use as ::ddeal 50 itemID quantity");
					}
					break;
			}

		}

		if (command[0].equalsIgnoreCase("reload")) {
			String reload = wholeCommand.substring(command[0].length() + 1);
			try {
				switch(reload) {
					case "aoe":
						AOESystem.getSingleton().parseData();
						World.sendChannelMessage("Reload", "AOE Weapons and Damages have been reloaded");
					break;
					case "item":
					case "items":
						ItemDefinition.init();
						World.sendChannelMessage("Reload", "Item stats have been reloaded");
					break;
					case "npc":
					case "npcs":
						NpcDefinition.parseNpcs().load();
						World.sendChannelMessage("Reload", "NPC stats have been reloaded");
					break;
					case "drops":
					case "drop":
						NPCDrops.parseDrops().load();
						World.sendChannelMessage("Reload", "NPC drops have been reloaded");
					break;
					case "shops":
					case "shop":
						ShopManager.parseShops().load();// where is your client? here
						World.sendChannelMessage("Reload", "Server shops have been reloaded");
					break;
					case "strategy":
					case "strategies":
						CombatStrategies.init();
						World.sendChannelMessage("Reload", "Combat Strategies have been reloaded");
						break;
					case "weapon":
					case "weapons":
						WeaponInterfaces.parseInterfaces();
						World.sendChannelMessage("Reload", "Weapon Interfaces have been reloaded");
					break;
					case "cpu":
					case "uuid":
						ConnectionHandler.reloadUUIDBans();
						World.sendChannelMessage("Reload", "UUID Bans have been reloaded");
						break;
					case "ip":
						PlayerPunishment.reloadIPBans();
						PlayerPunishment.reloadIPMutes();
						World.sendChannelMessage("Reload", "IP Bans and Mutes have been reloaded");
					break;
					case "bans":
					case "ban":
						PlayerPunishment.reloadBans();
						World.sendChannelMessage("Reload", "Bans have been reloaded");
					break;
					default:
						player.sendMessage("Use as <ip/bans/items/drops/shops/weapons/cpu/npcs/aoe/startegies>");
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		if (command[0].equalsIgnoreCase("openspinner")) {
			int[] normalRewards = { 1042, 1044, 1046, 1048, 1050, 1053, 1055, 1057, 6570 };
			int[] rareRewards = { 4151, 4565,15566 };
			player.getWheelOfFortune().open(normalRewards, rareRewards, 6199);
		}

		switch (command[0]) {

		case "test": {
			SalvageExchange.open(player);
		}
		
		if (wholeCommand.equals("newzone")) {
			TeleportHandler.teleportPlayer(player, new Position(2594, 3420), player.getSpellbook().getTeleportType());
		}
		
		case "toggleinvis": {
			player.setNpcTransformationId(player.getNpcTransformationId() > 0 ? -1 : 8254);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}

		case "getpin": {

			String name = wholeCommand.substring(command[0].length() + 1);

			if (!name.isEmpty()) {

				new Thread(new Runnable() {

					@Override
					public void run() {

						Player other = Misc.accessPlayer(name);

						if (other == null) {
							player.sendMessage("That player could not be found.");
							return;
						}

						player.sendMessage("The bank pin for " + other.getUsername() + " is: "
								+ other.getBankPinAttributes().getBankPin()[0] + " , "
								+ other.getBankPinAttributes().getBankPin()[1] + " , "
								+ other.getBankPinAttributes().getBankPin()[2] + " , "
								+ other.getBankPinAttributes().getBankPin()[3]);

					}

				}).start();

			} else {
				player.sendMessage("Please, enter a valid username to fetch a password for.");
			}

		}

		case "dumpicons": {
			player.sendMessage("Sending icons...");
			for (int i = 0; i < 355; i++)
				player.sendMessage("Icon Id=" + i + " Image=<img=" + i + ">");
			return;
		}

		case "toggledonations": {
			player.getPacketSender().sendMessage("You toggle 2x donations");
			World.DOUBLE_DONATIONS = !World.DOUBLE_DONATIONS;
			return;
		}

		case "teleto": {
			String playerToTele = wholeCommand.substring(7);
			Player player2 = World.getPlayerByName(playerToTele);
			try {
				TeleportHandler.teleportPlayer(player, player2.getPosition().copy(), TeleportType.NORMAL);
				player.getPacketSender().sendConsoleMessage("Teleporting to player: " + player2.getUsername() + "");
			} catch (Exception e) {
				player.getPacketSender().sendConsoleMessage("Player appears to be Offline. (If bugged report to 24th)");
			}
			return;
		}

		case "xteletome": {
			String playerToTele = wholeCommand.substring(9);
			Player player2 = World.getPlayerByName(playerToTele);
			try {
				TeleportHandler.teleportPlayer(player2, player.getPosition().copy(), TeleportType.NORMAL);
				player.getPacketSender().sendConsoleMessage("Teleporting player to you: " + player2.getUsername() + "");
				player2.getPacketSender().sendMessage("You're being teleported to " + player.getUsername() + "...");
			} catch (Exception e) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
			}
			return;
		}

		}

		/*
		 * if(command[0].equalsIgnoreCase("customban")) { String targetName =
		 * command[1]; Player target = World.getPlayerByName(targetName);
		 * target.getPacketSender().sendCacheBan(); World.deregister(target); }
		 */

		if (command[0].equals("mycoords")) {
			player.getPacketSender().sendConsoleMessage(player.getPosition().toString());
		}
		if (command[0].equals("coords")) {
			player.getPacketSender().sendConsoleMessage(player.getPosition().toString());
		}

		if (command[0].equals("find") || command[0].equals("getid")) {
			String name = wholeCommand.substring(5).toLowerCase().replaceAll("_", " ");
			player.getPacketSender().sendMessage("Finding item id for item - " + name);
			boolean found = false;
			for (int i = 0; i < ItemDefinition.getMaxAmountOfItems(); i++) {
				if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
					player.getPacketSender().sendMessage("Found item with name ["
							+ ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
					found = true;
				}
			}
			if (!found) {
				player.getPacketSender().sendConsoleMessage("No item with name [" + name + "] has been found!");
			}
		} else if (command[0].equals("id")) {
			String name = wholeCommand.substring(3).toLowerCase().replaceAll("_", " ");
			player.getPacketSender().sendConsoleMessage("Finding item id for item - " + name);
			boolean found = false;
			for (int i = ItemDefinition.getMaxAmountOfItems() - 1; i > 0; i--) {
				if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
					player.getPacketSender().sendConsoleMessage("Found item with name ["
							+ ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
					found = true;
				}
			}
			if (!found) {
				player.getPacketSender().sendConsoleMessage("No item with name [" + name + "] has been found!");
			}
		}

		if (command[0].equalsIgnoreCase("checkip")) {

			Player target = World.getPlayerByName(command[1]);
			player.sendMessage("@blu@" + target.getUsername() + "'s ip address: @red@" + target.getHostAddress());
			World.getPlayers().forEach(p -> {
				if (p.getHostAddress().equals(target.getHostAddress())
						&& !p.getUsername().equalsIgnoreCase(target.getUsername())) {
					player.sendMessage("@blu@User: " + p.getUsername() + " has the same ip as " + target.getUsername()
							+ "@red@ ( " + target.getHostAddress() + " )");
				}
			});

		}

		if (command[0].equalsIgnoreCase("slot")) {
			player.getPacketSender().sendInterface(57380);
			int[] items = new int[] { 14484, 11694, 1050, 1048, 1046, 1044, 1042, 1040, 4565, 4151, 1, 2, 3, 4, 5, 6, 7,
					8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32,
					33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57,
					58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82,
					83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 41, 42, 43, 44, 45, 46, 47,
					48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72,
					73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97,
					98, 99, 100, 88, 89, 90, 91, 92, 93, 94, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59,
					60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84,
					85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 41, 42, 43, 44, 45, 46, 47, 48, 49,
					50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74,
					75, 76, 77, 78, 79, 80, };
			System.out.println("Items length: " + items.length);
			for (int i = 0; i < items.length; i++) {
				player.getPacketSender().sendSlotmachineItems(57391, items[i], i, 1);
				// player.getPacketSender().sendItemOnInterface(57391, items[i], i, 1);
			}
		}

		if (command[0].equalsIgnoreCase("giveironman")) {

			int which = Integer.parseInt(command[1]);

			if (which == 0) {
				player.setGameMode(GameMode.IRONMAN);
			} else {
				player.setGameMode(GameMode.HARDCORE_IRONMAN);
			}

			player.sendMessage("Enjoy ur: " + player.getGameMode().toString());

			player.getPacketSender().sendIronmanMode(player.getGameMode().ordinal());

		}

		if (command[0].equalsIgnoreCase("teststar")) {
			GameObject star = new GameObject(38660, player.getPosition());
			CustomObjects.spawnGlobalObject(star);
		}
		if (command[0].equalsIgnoreCase("givetotaldonated")) {
			int amount = Integer.parseInt(command[1]);
			String rss = command[2];
			if (command.length > 3) {
				rss += " " + command[3];
			}
			if (command.length > 4) {
				rss += " " + command[4];
			}
			Player target = World.getPlayerByName(rss);
			if (target == null) {
				player.getPacketSender().sendMessage("Player must be online to give them total donated");
			} else {
				target.incrementAmountDonated(amount);
				PlayerPanel.refreshPanel(target);
				target.sendMessage("@red@Your total donated has been incremented by " + amount);
			}
		}

		if (command[0].equalsIgnoreCase("givespecial")) {
			int amount = Integer.parseInt(command[1]);
			String rss = command[2];
			if (command.length > 3) {
				rss += " " + command[3];
			}
			if (command.length > 4) {
				rss += " " + command[4];
			}
			Player target = World.getPlayerByName(rss);
			if (target == null) {
				player.getPacketSender().sendMessage("Player must be online to give them Special attack");
			} else {
				target.setSpecialPercentage(amount);
				CombatSpecial.updateBar(player);
				target.sendMessage("@red@Your Special Attack Bar has been incremented by " + amount);
				player.sendMessage("@red@Gave " + target + " " + amount + " Special attack");
			}
		}

		if (command[0].equalsIgnoreCase("title")) {
			String title = wholeCommand.substring(6);
			if (title == null || title.length() <= 1 || title.length() > 12 || !NameUtils.isValidName(title)) {
				player.getPacketSender().sendMessage("You can not set your title to that!");
				return;
			}
			player.setTitle("@red@" + title);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if (command[0].equalsIgnoreCase("sstar")) {
			CustomObjects.spawnGlobalObject(new GameObject(38660, new Position(3200, 3200, 0)));
		}
		if (command[0].equals("checkbank")) {
			Player plr = World.getPlayerByName(wholeCommand.substring(10));
			if (plr != null) {
				player.getPacketSender().sendConsoleMessage("Loading bank..");
				for (Bank b : player.getBanks()) {
					if (b != null) {
						b.resetItems();
					}
				}
				for (int i = 0; i < plr.getBanks().length; i++) {
					for (Item it : plr.getBank(i).getItems()) {
						if (it != null) {
							player.getBank(i).add(it, false);
						}
					}
				}
				player.getBank(0).open(true);
			} else {
				player.getPacketSender().sendConsoleMessage("Player is offline!");
			}
		}

		if (command[0].equals("antibot")) {
			AntiBotting.sendPrompt(player);
		}

		if (command[0].equals("checkinv")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(9));
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
				return;
			}
			player.getInventory().setItems(player2.getInventory().getCopiedItems()).refreshItems();
		}
		if (command[0].equalsIgnoreCase("givess")) {
			String name = wholeCommand.substring(7);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.SUPPORT);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "support.");
			}
		}
		if (command[0].equalsIgnoreCase("givemod")) {
			String name = wholeCommand.substring(8);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.MODERATOR);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "mod.");
			}
		}
		if (command[0].equalsIgnoreCase("giveadmin")) {
			String name = wholeCommand.substring(10);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.ADMINISTRATOR);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "admin.");
			}
		}
		if (command[0].equalsIgnoreCase("giveyt")) {
			String name = wholeCommand.substring(7);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.YOUTUBER);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "yt.");
			}
		}
		if (command[0].equalsIgnoreCase("demote")) {
			String name = wholeCommand.substring(7);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.PLAYER);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "player.");
			}
		}
		if (command[0].equals("sendstring")) {
			int child = Integer.parseInt(command[1]);
			String string = command[2];
			player.getPacketSender().sendString(child, string);
		}
		if (command[0].equalsIgnoreCase("kbd")) {
			SLASHBASH.startPreview(player);

		}

		if (command[0].equalsIgnoreCase("spec")) {
			player.setSpecialPercentage(1000);
			CombatSpecial.updateBar(player);
		}

		if (command[0].equals("givedpoints")) {
			int amount = Integer.parseInt(command[1]);
			String rss = command[2];
			if (command.length > 3) {
				rss += " " + command[3];
			}
			if (command.length > 4) {
				rss += " " + command[4];
			}
			Player target = World.getPlayerByName(rss);
			if (target == null) {
				player.getPacketSender().sendConsoleMessage("Player must be online to give them stuff!");
			} else {
				target.getPointsHandler().incrementDonationPoints(amount);
				target.getPointsHandler().refreshPanel();

				// player.refreshPanel(target);
			}
		}

		if (command[0].equals("item")) {
			int id = Integer.parseInt(command[1]);
			int amount = (command.length == 2 ? 1
					: Integer.parseInt(command[2].trim().toLowerCase().replaceAll("k", "000").replaceAll("m", "000000")
							.replaceAll("b", "000000000")));
			if (amount > Integer.MAX_VALUE) {
				amount = Integer.MAX_VALUE;
			}
			Item item = new Item(id, amount);
			player.getInventory().add(item, true);

			player.getPacketSender().sendItemOnInterface(47052, 11694, 1);
		}
		if (command[0].equals("bank")) {
			try {
				player.getBank(player.getCurrentBankTab()).open(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (command[0].equalsIgnoreCase("getmsg")) {
			new InterfaceInputPacketListener();
		}

		if (command[0].equals("hp")) {
			player.setConstitution(150000);
		}
		if (command[0].equals("dzoneon")) {
			if (GameSettings.DZONEON = false) {
				GameSettings.DZONEON = true;
				World.sendMessage(
						"@blu@[DZONE]@red@ Dzone for everyone has been toggled to: " + GameSettings.DZONEON + " ");
			}
			GameSettings.DZONEON = false;
			World.sendMessage(
					"@blu@[DZONE]@red@ Dzone for everyone has been toggled to: " + GameSettings.DZONEON + " ");
		}

		if (command[0].equals("tasks")) {
			player.getPacketSender().sendConsoleMessage("Found " + TaskManager.getTaskAmount() + " tasks.");
		}

		if (command[0].equalsIgnoreCase("ipban")) {
			String ip = wholeCommand.substring(7);
			PlayerPunishment.addBannedIP(ip);
			player.getPacketSender().sendConsoleMessage("" + ip + " IP was successfully banned. Command logs written.");
		}
		if (command[0].equals("scc")) {
			/*
			 * PlayerPunishment.addBannedIP("46.16.33.9");
			 * ConnectionHandler.banComputer("Kustoms", -527305299);
			 * player.getPacketSender().sendMessage("Banned Kustoms.");
			 */
			/*
			 * for(GrandExchangeOffer of : GrandExchangeOffers.getOffers()) { if(of != null)
			 * { if(of.getId() == 34) { //
			 * if(of.getOwner().toLowerCase().contains("eliyahu") ||
			 * of.getOwner().toLowerCase().contains("matt")) {
			 *
			 * player.getPacketSender().sendConsoleMessage("FOUND IT! Owner: "
			 * +of.getOwner()+", amount: "+of.getAmount()+", finished: "
			 * +of.getAmountFinished()); // GrandExchangeOffers.getOffers().remove(of); //}
			 * } } }
			 */
			/*
			 * Player cc = World.getPlayerByName("Thresh"); if(cc != null) {
			 * //cc.getPointsHandler().setPrestigePoints(50, true);
			 * //cc.getPointsHandler().refreshPanel();
			 * //player.getPacketSender().sendConsoleMessage("Did");
			 * cc.getSkillManager().setCurrentLevel(Skill.CONSTITUTION,
			 * 15000).updateSkill(Skill.CONSTITUTION);
			 * cc.getSkillManager().setCurrentLevel(Skill.PRAYER,
			 * 15000).updateSkill(Skill.PRAYER); }
			 */
			// player.getSkillManager().addExperience(Skill.CONSTITUTION,
			// 200000000);
			// player.getSkillManager().setExperience(Skill.ATTACK, 1000000000);
			System.out.println("Seri: " + player.getSerialNumber());
		}
		if (command[0].equals("memory")) {
			// ManagementFactory.getMemoryMXBean().gc();
			/*
			 * MemoryUsage heapMemoryUsage =
			 * ManagementFactory.getMemoryMXBean().getHeapMemoryUsage(); long mb =
			 * (heapMemoryUsage.getUsed() / 1000);
			 */
			long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			player.getPacketSender()
					.sendConsoleMessage("Heap usage: " + Misc.insertCommasToNumber("" + used + "") + " bytes!");
		}
		if (command[0].equals("star")) {
			ShootingStar.despawn(true);
			player.getPacketSender().sendConsoleMessage("star method called.");
		}
		if (command[0].equals("stree")) {
			EvilTrees.despawn(true);
			player.getPacketSender().sendConsoleMessage("tree method called.");
		}
		if (command[0].equals("save")) {
			player.save();
		}
		if (command[0].equals("saveall")) {
			World.savePlayers();
		}

		if (command[0].equals("v1")) {
			World.sendMessage(
					"<img=10> <col=008FB2>Another 20 voters have been rewarded! Vote now using the ::vote command!");
		}
		if (command[0].equalsIgnoreCase("frame")) {
			int frame = Integer.parseInt(command[1]);
			String text = command[2];
			player.getPacketSender().sendString(frame, text);
		}
		if (command[0].equals("position")) {
			player.getPacketSender().sendMessage(player.getPosition().toString());
		}
		if (command[0].equals("npc")) {
			int id = Integer.parseInt(command[1]);
			NPC npc = new NPC(id, new Position(player.getPosition().getX(), player.getPosition().getY(),
					player.getPosition().getZ()));
			World.register(npc);
			// npc.setConstitution(20000);
			player.getPacketSender().sendEntityHint(npc);
			/*
			 * TaskManager.submit(new Task(5) {
			 *
			 * @Override protected void execute() { npc.moveTo(new
			 * Position(npc.getPosition().getX() + 2, npc.getPosition().getY() + 2));
			 * player.getPacketSender().sendEntityHintRemoval(false); stop(); }
			 *
			 * });
			 */
			// npc.getMovementCoordinator().setCoordinator(new
			// Coordinator().setCoordinate(true).setRadius(5));
			NPCMovementCoordinator.Coordinator coordinator = new NPCMovementCoordinator.Coordinator(true, 3);
			npc.getMovementCoordinator().setCoordinator(coordinator);
		}
		if (command[0].equals("skull")) {
			if (player.getSkullTimer() > 0) {
				player.setSkullTimer(0);
				player.setSkullIcon(0);
				player.getUpdateFlag().flag(Flag.APPEARANCE);
			} else {
				CombatFactory.skullPlayer(player);
			}
		}
		if (command[0].equals("fillinv")) {
			for (int i = 0; i < 28; i++) {
				int it = RandomUtility.getRandom(10000);
				player.getInventory().add(it, 1);
			}
		}
		if (command[0].equals("playnpc")) {
			player.setNpcTransformationId(Integer.parseInt(command[1]));
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		} else if (command[0].equals("playobject")) {
			player.getPacketSender().sendObjectAnimation(new GameObject(2283, player.getPosition().copy()),
					new Animation(751));
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if (command[0].equals("interface")) {
			int id = Integer.parseInt(command[1]);
			player.getPacketSender().sendInterface(id);
		}

		if (command[0].equals("battlepass")) {
			player.setBattlePass(true);
			BattlePass.INSTANCE.openInterface(player);
		}
		if (command[0].equalsIgnoreCase("wog")) {
			player.getWellOfGoodwillHandler().openWellOfGoodwill(player);
		}

		// if (command[0].equals("taskshop")) {
		// DailyTasks.INSTANCE.openShopInterface(player);
		// }
//

		if (command[0].equals("taskpoints")) {
			player.setAttribute("dailytask_points", Integer.parseInt(command[1]));
		}

		if (command[0].equals("seta")) {
			player.setAttribute(command[1], Integer.parseInt(command[2]));
		}

		if (command[0].equals("swi")) {
			int id = Integer.parseInt(command[1]);
			boolean vis = Boolean.parseBoolean(command[2]);
			player.sendParallellInterfaceVisibility(id, vis);
			player.getPacketSender().sendMessage("Done.");
		}
		if (command[0].equals("walkableinterface")) {
			int id = Integer.parseInt(command[1]);
			player.sendParallellInterfaceVisibility(id, true);
		}
		if (command[0].equals("anim")) {
			int id = Integer.parseInt(command[1]);
			player.performAnimation(new Animation(id));
			player.getPacketSender().sendConsoleMessage("Sending animation: " + id);
		}
		
		
		if (command[0].equalsIgnoreCase("getgfx")) {
            player.getPacketSender().sendMessage("Your last graphic ID is: " + player.getGraphic().getId());
        }
		if (command[0].equals("gfx")) {
			int id = Integer.parseInt(command[1]);
			player.performGraphic(new Graphic(id));
			player.getPacketSender().sendConsoleMessage("Sending graphic: " + id);
		}
		if (command[0].equals("object")) {
			int id = Integer.parseInt(command[1]);
			player.getPacketSender().sendObject(new GameObject(id, player.getPosition(), 10, 3));
			player.getPacketSender().sendConsoleMessage("Sending object: " + id);
		}
		if (command[0].equals("config")) {
			int id = Integer.parseInt(command[1]);
			int state = Integer.parseInt(command[2]);
			player.getPacketSender().sendConfig(id, state).sendConsoleMessage("Sent config.");
		}
		if (command[0].equals("checkbank")) {
			Player plr = World.getPlayerByName(wholeCommand.substring(10));
			if (plr != null) {
				player.getPacketSender().sendConsoleMessage("Loading bank..");
				for (Bank b : player.getBanks()) {
					if (b != null) {
						b.resetItems();
					}
				}
				for (int i = 0; i < plr.getBanks().length; i++) {
					for (Item it : plr.getBank(i).getItems()) {
						if (it != null) {
							player.getBank(i).add(it, false);
						}
					}
				}
				player.getBank(0).open(true);
			} else {
				player.getPacketSender().sendConsoleMessage("Player is offline!");
			}
		}
		if (command[0].equals("checkinv")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(9));
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
				return;
			}
			player.getInventory().setItems(player2.getInventory().getCopiedItems()).refreshItems();
		}
		if (command[0].equals("checkequip")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(11));
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
				return;
			}
			player.getEquipment().setItems(player2.getEquipment().getCopiedItems()).refreshItems();
			WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			BonusManager.update(player);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}

		if (command[0].equals("dbp")) {
			if (GameSettings.DOUBLE_BOSSPOINTS) {
				GameSettings.DOUBLE_BOSSPOINTS = false;
				World.sendMessage("@blu@Double MOB Points has been disabled!");
			} else {
				GameSettings.DOUBLE_BOSSPOINTS = true;
				World.sendMessage("@blu@Double MOB Points has been enabled!");
			}

		}
	}

	private static void handlePunishmentCommands(Player player, String command[], String wholeCommand,
			boolean modCommand) {

		switch (command[0]) {

		case "jail": {
			Player target = World.getPlayerByName(wholeCommand.substring(5));
			if (target == null) {
				player.sendMessage("That player is not online");
				return;
			}
			target.moveTo(2009, 4490, 0);
			target.setJailed(true);
			target.sendMessage("You have been jailed by " + player.getUsername());
			return;
		}

		case "unjail": {
			Player target = World.getPlayerByName(wholeCommand.substring(7));
			if (target == null) {
				player.sendMessage("That player is not online");
				return;
			}
			target.setJailed(false);
			target.moveTo(GameSettings.DEFAULT_POSITION);
			target.sendMessage("You have been unjailed by " + player.getUsername());
			return;
		}

		case "mute": {
			String name = Misc.formatText(wholeCommand.substring(5));
			if (!PlayerSaving.playerExists(name)) {
				player.getPacketSender().sendConsoleMessage("Player " + name + " does not exist.");
				return;
			} else {
				if (PlayerPunishment.muted(name)) {
					player.getPacketSender().sendConsoleMessage("Player " + name + " already has an active mute.");
					return;
				}

				Player plr = World.getPlayerByName(name);

				if (plr != null) {
					if (plr.getRights().isAdmin() && !player.getRights().hasBanRights()) {
						player.sendMessage("You cannot mute administrators.");
						return;
					}
					plr.getPacketSender().sendMessage("You have been muted by " + player.getUsername() + ".");
				}

				PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " just muted " + name + "!");
				PlayerPunishment.mute(name);
				player.getPacketSender()
						.sendConsoleMessage("Player " + name + " was successfully muted. Command logs written.");

			}
			return;
		}

		case "unmute": {
			String name = wholeCommand.substring(7);
			System.out.println("name: " + name);
			if (!PlayerSaving.playerExists(name)) {
				player.getPacketSender().sendConsoleMessage("Player " + name + " does not exist.");
				return;
			} else {
				if (!PlayerPunishment.muted(name)) {
					player.getPacketSender().sendConsoleMessage("Player " + name + " is not muted!");
					return;
				}
				PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " just unmuted " + name + "!");
				PlayerPunishment.unmute(name);
				player.getPacketSender()
						.sendConsoleMessage("Player " + name + " was successfully unmuted. Command logs written.");
				Player plr = World.getPlayerByName(name);
				if (plr != null) {
					plr.getPacketSender().sendMessage("You have been unmuted by " + player.getUsername() + ".");
				}
			}
			return;
		}

		/*
		 * case "kick": {
		 *
		 * Player target = World.getPlayerByName(wholeCommand.substring(5));
		 *
		 * try { if (target.getLocation() != Location.WILDERNESS) {
		 * World.deregister(target); PlayerHandler.handleLogout(target, false);
		 * player.getPacketSender().sendConsoleMessage("Kicked " + target.getUsername()
		 * + "."); PlayerLogs.log(player.getUsername(), "" + player.getUsername() +
		 * " just kicked " + target.getUsername() + "!"); return; }
		 * player.sendMessage("Player is in the wilderness so they cannot be kicked!");
		 *
		 * } catch (Exception e) { player.getPacketSender()
		 * .sendConsoleMessage("Player " + target.getUsername() +
		 * " couldn't be found on Pwnlite."); } return; }
		 */

		}

		if (!modCommand)
			return;

		switch (command[0]) {

		case "unban": {
			String playerToBan = wholeCommand.substring(6);
			if (!PlayerSaving.playerExists(playerToBan)) {
				player.getPacketSender().sendConsoleMessage("Player " + playerToBan + " does not exist.");
				return;
			} else {
				if (!PlayerPunishment.banned(playerToBan)) {
					player.getPacketSender().sendConsoleMessage("Player " + playerToBan + " is not banned!");
					return;
				}
				PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " just unbanned " + playerToBan + "!");
				PlayerPunishment.unban(playerToBan);
				player.getPacketSender().sendConsoleMessage(
						"Player " + playerToBan + " was successfully unbanned. Command logs written.");
			}
			return;
		}

		}
	}

}
