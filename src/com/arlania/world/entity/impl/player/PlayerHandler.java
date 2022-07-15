package com.arlania.world.entity.impl.player;

import java.time.LocalDateTime;

import com.arlania.GameServer;
import com.arlania.GameSettings;
import com.arlania.engine.task.TaskManager;
import com.arlania.engine.task.impl.BonusExperienceTask;
import com.arlania.engine.task.impl.ChocCreamTask;
import com.arlania.engine.task.impl.SmokeTheBongTask;
import com.arlania.engine.task.impl.CleansingTask;
import com.arlania.engine.task.impl.CombatSkullEffect;
import com.arlania.engine.task.impl.FireImmunityTask;
import com.arlania.engine.task.impl.IceCreamTask;
import com.arlania.engine.task.impl.OverloadPotionTask;
import com.arlania.engine.task.impl.PlayerSkillsTask;
import com.arlania.world.DonationDeals;
import com.arlania.world.content.DPSTask;
import com.arlania.engine.task.impl.PlayerSpecialAmountTask;
import com.arlania.engine.task.impl.PraiseTask;
import com.arlania.engine.task.impl.PrayerRenewalPotionTask;
import com.arlania.engine.task.impl.StaffOfLightSpecialAttackTask;
import com.arlania.model.*;
import com.arlania.model.container.impl.Bank;
import com.arlania.model.container.impl.Equipment;
import com.arlania.model.definitions.WeaponAnimations;
import com.arlania.model.definitions.WeaponInterfaces;
import com.arlania.model.input.impl.EnterPinPacketListener;
import com.arlania.net.PlayerSession;
import com.arlania.net.SessionState;
import com.arlania.net.security.ConnectionHandler;
import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.content.Achievements;
import com.arlania.world.content.BonusManager;
import com.arlania.world.content.GamemodeSelecter;
import com.arlania.world.content.Lottery;
import com.arlania.world.content.PlayerLogs;
import com.arlania.world.content.PlayerPanel;
import com.arlania.world.content.PlayersOnlineInterface;
import com.arlania.world.content.StaffList;
import com.arlania.world.content.StartScreen;
import com.arlania.world.content.TimelyReward1;
import com.arlania.world.content.WellOfGoodwill;
import com.arlania.world.content.WellOfWealth;
import com.arlania.world.content.clan.ClanChatManager;
import com.arlania.world.content.combat.effect.CombatPoisonEffect;
import com.arlania.world.content.combat.effect.CombatTeleblockEffect;
import com.arlania.world.content.combat.magic.Autocasting;
import com.arlania.world.content.combat.prayer.CurseHandler;
import com.arlania.world.content.combat.prayer.PrayerHandler;
import com.arlania.world.content.combat.pvp.BountyHunter;
import com.arlania.world.content.combat.range.DwarfMultiCannon;
import com.arlania.world.content.combat.weapon.CombatSpecial;
import com.arlania.world.content.dropchecker.NPCDropTableChecker;
import com.arlania.world.content.grandexchange.GrandExchange;
import com.arlania.world.content.interfaces.QuestTab;
import com.arlania.world.content.minigames.impl.Barrows;
import com.arlania.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.arlania.world.content.skill.impl.hunter.Hunter;
import com.arlania.world.content.skill.impl.slayer.Slayer;
import com.arlania.world.content.skill.impl.slayer.BloodSlayer;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;
//import com.mysql.Kills;

import mysql.impl.*;

import static com.arlania.world.World.deals;

public class PlayerHandler {

	private static Object GodPotion;

	/**
	 * Gets the player according to said name.
	 * 
	 * @param name The name of the player to search for.
	 * @return The player who has the same name as said param.
	 */

	public static Player getPlayerForName(String name) {
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			if (player.getUsername().equalsIgnoreCase(name))
				return player;
		}
		return null;
	}

	public static void handleLogin(Player player) {
		System.out.println("[World] Registering player - [username, host] : [" + player.getUsername() + ", "
				+ player.getHostAddress() + "][" + player.getMacAddress() + "]");
		player.setPlaceholders(false);
		player.getPacketSender().sendConfig(111, player.isPlaceholders() ? 1 : 0);
		if (player.getHasPin() == true && !player.getSavedIp().equalsIgnoreCase(player.getHostAddress())) {
			player.setPlayerLocked(true);
		}
		player.getPlayerOwnedShopManager().hookShop();
		player.setActive(true);
		ConnectionHandler.add(player.getHostAddress());
		World.getPlayers().add(player);
		World.updatePlayersOnline();
		PlayersOnlineInterface.add(player);
		player.getSession().setState(SessionState.LOGGED_IN);

		player.getPacketSender().sendMapRegion().sendDetails();

		player.getRecordedLogin().reset();

		player.getPacketSender().sendTabs();
		player.getPacketSender().sendClientSettings();

		player.getMinimeSystem().onLogin();

		for (int i = 0; i < player.getBanks().length; i++) {
			if (player.getBank(i) == null) {
				player.setBank(i, new Bank(player));
			}
		}
		player.getInventory().refreshItems();
		player.getEquipment().refreshItems();

		WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
		WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
		CombatSpecial.updateBar(player);
		BonusManager.update(player);

		player.getSummoning().login();
		player.getFarming().load();
		// Slayer.checkDuoSlayer(player, true);
		for (Skill skill : Skill.values()) {
			player.getSkillManager().updateSkill(skill);
		}

		if (player.getTimer() > 0) {
			NPC npc = new NPC(player.getTransform(), new Position(0, 0));
			player.setNpcTransformationId(player.getTransform());
			player.setTransform(player.getTransform());
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getPacketSender().sendInterfaceRemoval();
		}

		player.getRelations().setPrivateMessageId(1).onLogin(player).updateLists(true);

		player.getPacketSender().sendConfig(172, player.isAutoRetaliate() ? 1 : 0)
				.sendTotalXp(player.getSkillManager().getTotalGainedExp())
				.sendConfig(player.getFightType().getParentId(), player.getFightType().getChildId()).sendRunStatus()
				.sendRunEnergy(player.getRunEnergy()).sendString(8135, "" + player.getMoneyInPouch())
				.sendInteractionOption("Follow", 3, false).sendInteractionOption("Trade With", 4, false)
				.sendInterfaceRemoval();

		// if(player.getPlayerOwnedShopManager().getMyShop() != null)
		if (!player.isIronMan())
			player.getPacketSender().sendInteractionOption("View shop", 5, false);

		Autocasting.onLogin(player);
		PrayerHandler.deactivateAll(player);
		CurseHandler.deactivateAll(player);
		BonusManager.sendCurseBonuses(player);
		//Achievements.updateInterface(player);
		Barrows.updateInterface(player);
		player.getUpgradeHandler().init();

		if (player.getDonationDeals().shouldReset()) {
			System.out.println("Resetting");
		} else {
			System.out.println("Not resetting");
		}

		TaskManager.submit(new PlayerSkillsTask(player));
		if (player.isPoisoned()) {
			TaskManager.submit(new CombatPoisonEffect(player));
		}
		if (player.getPrayerRenewalPotionTimer() > 0) {
			TaskManager.submit(new PrayerRenewalPotionTask(player));
		}
		if (player.getOverloadPotionTimer() > 0) {
			TaskManager.submit(new OverloadPotionTask(player));
		}
		if (player.getTeleblockTimer() > 0) {
			TaskManager.submit(new CombatTeleblockEffect(player));
		}
		if (player.getSkullTimer() > 0) {
			player.setSkullIcon(1);
			TaskManager.submit(new CombatSkullEffect(player));
		}

		if (player.lastLogin > System.currentTimeMillis() + 86400000) {
			player.getDailyRewards().resetData();
			System.out.println("Was so reset data :(");
		}

		player.getDailyRewards().handleDailyLogin();
		player.lastLogin = System.currentTimeMillis();

		if (!player.hasFirstTimeTimerSet) {
			player.lastDailyClaim = System.currentTimeMillis() + 86400000;
			player.hasFirstTimeTimerSet = true;
			System.out.println("Set for the first time.");
		}

		if (player.lastDailyClaim < 1) {
			player.lastDailyClaim = System.currentTimeMillis() + 86400000;
		}
		if (player.getController() != null) {
			player.getController().login(player);
		}

		/// player.getDailyRewards().setDataOnLogin();
//
//		player.getDailyRewards().processTime();
//		player.getDailyRewards().displayRewards();
		player.getDailyReward().openInterface();

		if (player.getSendDpsOverlay())
			player.getPacketSender().sendWalkableInterface(23998, true);
		TaskManager.submit(new DPSTask(player));

		if (player.lastLogin > System.currentTimeMillis() + 86400000) {
			player.getDailyRewards().resetData();
			System.out.println("Was so reset data :(");
		}
		player.getDailyRewards().handleDailyLogin();
		player.lastLogin = System.currentTimeMillis();

		if (!player.hasFirstTimeTimerSet) {
			player.lastDailyClaim = System.currentTimeMillis() + 86400000;
			player.hasFirstTimeTimerSet = true;
			System.out.println("Set for the first time.");
		}

		if (player.lastDailyClaim < 1) {
			player.lastDailyClaim = System.currentTimeMillis() + 86400000;
		}

		player.getPerkHandler().loadAllPerkHandler(player);

		// player.sendMessage("Called it");
		// TaskManager.submit(new TimelyReward1(player));
		if (player.getFireImmunity() > 0) {
			FireImmunityTask.makeImmune(player, player.getFireImmunity(), player.getFireDamageModifier());
		}
		if (player.getSpecialPercentage() < 100) {
			TaskManager.submit(new PlayerSpecialAmountTask(player));
		}
		if (player.hasStaffOfLightEffect()) {
			TaskManager.submit(new StaffOfLightSpecialAttackTask(player));
		}
		if (player.getMinutesBonusExp() >= 0) {
			TaskManager.submit(new BonusExperienceTask(player));
		}

		if (player.getCleansingTime() >= 100) {
			player.setDoubleDropsActive(true);
			TaskManager.submit(new CleansingTask(player));
		}

		if (player.getPraiseTime() >= 100) {
			player.setDoubleRateActive(true);
			TaskManager.submit(new PraiseTask(player));
		}

		if (player.getIceCreamTime() >= 100) {
			player.setIceCreamRateActive(true);
			TaskManager.submit(new IceCreamTask(player));
		}

		if (player.getChocCreamTime() >= 100) {
			player.setChocCreamRateActive(true);
			TaskManager.submit(new ChocCreamTask(player));
		}

		if (player.getSmokeTheBongTime() >= 100) {
			player.setSmokeTheBongRateActive(true);
			TaskManager.submit(new SmokeTheBongTask(player));
		}

		if (player.getPosition().getX() >= 2088 && player.getPosition().getX() <= 2107
				&& player.getPosition().getY() >= 4420 && player.getPosition().getY() <= 4438) {
			player.moveTo(new Position(3087, 3495));
			int[] keyIds = { 1544, 1545, 1546, 1547, 1548 };
			if (player.getInventory().containsAny(1544, 1545, 1546, 1547, 1548)) {
				for (int i = 0; i < keyIds.length; i++) {
					player.getInventory().delete(keyIds[i], 1);
				}
			}
		}

		if (player.getHasPin() == true && !player.getSavedIp().equalsIgnoreCase(player.getHostAddress())) {
			System.out.println("Current ip: " + player.getHostAddress());
			System.out.println("Saved ip: " + player.getSavedIp());
			player.setInputHandling(new EnterPinPacketListener());
			player.getPacketSender().sendEnterInputPrompt("Enter your pin to play#confirmstatus");
		} else {
			System.out.println("Player: " + player.getUsername() + " Didn't have pin set");
		}

		player.getUpdateFlag().flag(Flag.APPEARANCE);

		Lottery.onLogin(player);
		Locations.login(player);

		if (player.didReceiveStarter() == false) {
			// player.getInventory().add(10835, 1).add(15501, 1).add(1153, 1).add(1115,
			// 1).add(1067, 1).add(1323, 1).add(1191, 1).add(841, 1).add(882, 50).add(1167,
			// 1).add(1129, 1).add(1095, 1).add(1063, 1).add(579, 1).add(577, 1).add(1011,
			// 1).add(1379, 1).add(556, 50).add(558, 50).add(557, 50).add(555, 50).add(1351,
			// 1).add(1265, 1).add(1712, 1).add(11118, 1).add(1007, 1).add(1061,
			// 1).add(1419, 1);

			// player.setReceivedStarter(true);
		}
		// DialogueManager.start(player, 177);
		// DialogueManager.start(player, 177);
		player.getPacketSender().sendMessage("<img=454>Welcome back to <shad=df7018>Pwnlite");

		player.getPacketSender()
				.sendMessage("Latest server updates/announcements can be seen at <shad=df7018>::discord");

		if (player.experienceLocked()) {
			player.getPacketSender().sendMessage("@red@Warning: your experience is currently locked.");
		}
		ClanChatManager.handleLogin(player);

		if (World.deals.currentDeal != DonationDeals.Deals.NONE) {
			player.getPacketSender().sendMessage(deals.displayDeal());
		}


		ClanChatManager.join(player, "help");
		PlayerPanel.refreshPanel(player);
		QuestTab.showCharacterSummary(player);
		QuestTab.showQuestTab(player);
		QuestTab.showEventsTab(player);

		// New player
		if (player.newPlayer()) {
			// StartScreen.open(player);
			GamemodeSelecter.open(player, false);
			player.setPlayerLocked(true);
		}

		player.getPacketSender().updateSpecialAttackOrb().sendIronmanMode(player.getGameMode().ordinal());

		if (player.getRights() == PlayerRights.SUPREME_DONATOR) {
			World.sendMessage("<img=16><shad=a80000>Supreme Donator</shad> " + player.getUsername()
					+ "<shad=a80000> Has logged in<img=16>");
		}
		if (player.getRights() == PlayerRights.DIVINE_DONATOR) {
			World.sendMessage("<img=18><shad=da08c7>Divine Donator</shad> " + player.getUsername()
					+ "<shad=da08c7> Has logged in<img=18>");
		}

		if (player.getRights() == PlayerRights.EXECUTIVE_DONATOR) {
			World.sendMessage("<img=15><shad=d8d8d8>Exectuive Donator</shad> " + player.getUsername()
					+ "<shad=d8d8d8> Has logged in<img=15>");
		}

		if (player.getRights() == PlayerRights.CELESTIAL_DONATOR) {
			World.sendMessage("<img=14><shad=0badd4>Celestial Donator</shad> " + player.getUsername()
					+ "<shad=0badd4> Has logged in<img=14>");
		}

		if (player.getRights() == PlayerRights.SUPPORT) {
			World.sendMessage("<shad=312ccf><img=10>Support</shad> " + player.getUsername()
					+ "<shad=312ccf> Has logged in, Need help? PM away<img=10>");
		}
		if (player.getRights() == PlayerRights.MODERATOR) {
			World.sendMessage("<shad=b20303><img=1>Moderator</shad> " + player.getUsername()
					+ "<shad=b20303> Has logged in, Need help? PM away<img=1>");
		}
		if (player.getRights() == PlayerRights.ADMINISTRATOR) {
			World.sendMessage("<shad=F7BF16><img=2>Admin</shad> " + player.getUsername()
					+ "<shad=F7BF16> Has logged in, Need help? PM away<img=2>");
		}

		if (player.getRights() == PlayerRights.DEVELOPER) {
			World.sendMessage("<shad=d4470b><img=4>Developer</shad> " + player.getUsername()
					+ "<shad=d4470b> Has logged in<img=4>");
		}

		if (player.getRights() == PlayerRights.OWNER) {
			World.sendMessage(
					"<shad=db7200><img=3>Owner</shad> " + player.getUsername() + "<shad=db7200> Has logged in<img=3>");
		}

		if (player.getRights() == PlayerRights.CO_OWNER) {
			World.sendMessage("<shad=db7303><img=17>Co-Owner</shad> " + player.getUsername()
					+ "<shad=db7303> Has logged in<img=3>");
		}

		if (player.getRights() == PlayerRights.COMMUNITY_MANAGER) {
			World.sendMessage("<shad=89cff0><img=510>Community Manager </shad>" + player.getUsername()
					+ "<shad=89cff0> Has logged in<img=510> ");
		}

		if (player.getRights() == PlayerRights.EVENT_MANAGER) {
			World.sendMessage("<shad=020202><img=511>Event Manager </shad>" + player.getUsername()
					+ "<shad=020202> Has logged in<img=511> ");
		}

		GrandExchange.onLogin(player);
	//	new Thread(new Kills(player)).start();
		if (player.getPointsHandler().getAchievementPoints() == 0) {
			Achievements.setPoints(player);
		}

		if (player.getPlayerOwnedShopManager().getEarnings() > 0) {
			player.sendMessage("<col=FF0000>You have unclaimed earnings in your player owned shop!");
		}

	//	player.claimDonation(player, true);
		NPCDropTableChecker.getSingleton().refreshDropTableChilds(player);
		PlayerLogs.log(player.getUsername(),
				"Login from host " + player.getHostAddress() + ", serial number: " + player.getSerialNumber());
	}

	public static boolean handleLogout(Player player, boolean fromUpdate, boolean forced) {
		try {

			PlayerSession session = player.getSession();

			if (session.getChannel().isOpen()) {
				session.getChannel().close();
			}

			if (!player.isRegistered()) {
				return true;
			}

			boolean exception = GameServer.isUpdating()
					|| World.getLogoutQueue().contains(player) && player.getLogoutTimer().elapsed(600000);
			if (player.logout(forced) || exception) {
				System.out.println("[World] Deregistering player - [username, host] : [" + player.getUsername() + ", "
						+ player.getHostAddress() + "][" + player.getMacAddress() + "]");
				player.getSession().setState(SessionState.LOGGING_OUT);
				ConnectionHandler.remove(player.getHostAddress());
				player.incrementTotalPlayTime(player.getRecordedLogin().elapsed());
				player.getPacketSender().sendInterfaceRemoval();
				if (player.getCannon() != null) {
					DwarfMultiCannon.pickupCannon(player, player.getCannon(), true);
				}
				if (exception && player.getResetPosition() != null) {
					player.moveTo(player.getResetPosition());
					player.setResetPosition(null);
				}
				if (player.getRegionInstance() != null) {
					player.getRegionInstance().destruct();
				}

				if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR
						|| player.getRights() == PlayerRights.COMMUNITY_MANAGER
						|| player.getRights() == PlayerRights.EVENT_MANAGER
						|| player.getRights() == PlayerRights.SUPPORT || player.getRights() == PlayerRights.DEVELOPER
						|| player.getRights() == PlayerRights.OWNER || player.getRights() == PlayerRights.CO_OWNER
						|| player.getRights() == PlayerRights.CoolRank) {
					StaffList.logout(player);
				}
				if (player.getRaidParty() != null) {
					player.getRaidParty().leave(player);
				}
				player.endKeyRoom(GameServer.isUpdating() || fromUpdate);
				player.endCustomBossRoom();
				Hunter.handleLogout(player);
				Locations.logout(player);
				player.getSummoning().unsummon(false, false);
				player.getFarming().save();
				player.getPlayerOwnedShopManager().unhookShop();
				BountyHunter.handleLogout(player);
				ClanChatManager.save();
				// player.attackable = true;
				ClanChatManager.leave(player, false);
				player.getRelations().updateLists(false);
				PlayersOnlineInterface.remove(player);
				TaskManager.cancelTasks(player.getCombatBuilder());
				TaskManager.cancelTasks(player);
			//	new Thread(new Kills(player)).start(); // lets test this now
				player.save();
				World.getPlayers().remove(player);
				session.setState(SessionState.LOGGED_OUT);
				World.updatePlayersOnline();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public static boolean handleLogout(Player player, boolean forced) {
		try {
			if (player.isBot()) {
				System.out.println("Trying to get rid of bot: " + player.getUsername());
				return true;
			}
			PlayerSession session = player.getSession();

			if (session != null) {
				if (session.getChannel().isOpen()) {
					System.out.println(player.getUsername() + " closed channel 1");
					session.getChannel().close();
				}
			}

			if (!player.isRegistered()) {
				System.out.println(player.getUsername() + " returned true 1");
				return true;
			}

			boolean exception = GameServer.isUpdating()
					|| World.getLogoutQueue().contains(player) && player.getLogoutTimer().elapsed(600000);
			System.out.println(player.getUsername() + " exception: " + exception);
			if (player.logout(forced) || exception) {
				// new Thread(new HighscoresHandler(player)).start();
				System.out.println("[World] Deregistering player v2 - [username, host] : [" + player.getUsername() + ", "
						+ player.getHostAddress() + "]");
				ClanChatManager.leave(player, false);
				player.getSession().setState(SessionState.LOGGING_OUT);
				ConnectionHandler.remove(player.getHostAddress());
				player.incrementTotalPlayTime(player.getRecordedLogin().elapsed());
				player.getPacketSender().sendInterfaceRemoval();
				if (player.getCannon() != null) {
					DwarfMultiCannon.pickupCannon(player, player.getCannon(), true);
				}
				if (exception && player.getResetPosition() != null) {
					player.moveTo(player.getResetPosition());
					player.setResetPosition(null);
				}

				if (player.getRegionInstance() != null) {
					player.getRegionInstance().destruct();
				}

				if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR
						|| player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.SUPPORT
						|| player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.OWNER
						|| player.getRights() == PlayerRights.CO_OWNER) {
					StaffList.logout(player);
				}
				if (Dungeoneering.doingDungeoneering(player)) {
					player.getEquipment().resetItems();
					player.getInventory().resetItems();
					player.getMinigameAttributes().getDungeoneeringAttributes().getParty().remove(player, true, true);
					player.moveTo(GameSettings.DEFAULT_POSITION);
				}
				if (player.getPosition().getX() >= 2380 && player.getPosition().getX() <= 2430) {
					if (player.getPosition().getY() >= 3465 && player.getPosition().getY() <= 3514) {
						player.moveTo(GameSettings.DEFAULT_POSITION);
					}
				}
				if (player.getRaidParty() != null) {
					player.getRaidParty().leave(player);
				}
				if (player.getGodPotionStatus()) {
					player.setGodPotionStatus(false);
					player.setGodPotionStatus(0);
					System.out.println("Ended god potion for " + player.getUsername());
				}
				Hunter.handleLogout(player);
				Locations.logout(player);
				player.getSummoning().unsummon(false, false);
				player.getFarming().save();
				player.getPerkHandler().saveAllPerkHandler(player);
				player.getPlayerOwnedShopManager().unhookShop();
				BountyHunter.handleLogout(player);

				player.getRelations().updateLists(false);
				PlayersOnlineInterface.remove(player);

				if (player.getBank() != null) {
					Bank bankHolder = player.getBank();
					for (int i = 0; i < bankHolder.getBankTabs().length; i++) {
						Bank bank = new Bank(player);
						if (bank != null) {
							for (Item item : bankHolder.getBank(i).getItems()) {
								bank.add(item);
							}
							player.setBank(i, bank);
						}
					}
					player.setBankHolder(null);
				}
				TaskManager.cancelTasks(player.getCombatBuilder());
				TaskManager.cancelTasks(player);

				player.save();
				World.getPlayers().remove(player);
				session.setState(SessionState.LOGGED_OUT);
				World.updatePlayersOnline();
				System.out.println(player.getUsername() + " returned true 2");
				return true;
			}
			System.out.println(player.getUsername() + " returned false 1");
			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(player.getUsername() + " returned true 3");
		return true;
	}
}
