package com.arlania.net.packet.impl;

import java.util.Objects;

import com.arlania.GameSettings;
import com.arlania.model.GameMode;
import com.arlania.model.Locations.Location;
import com.arlania.model.PlayerRights;
import com.arlania.model.Position;
import com.arlania.model.RegionInstance;
import com.arlania.model.container.impl.Bank;
import com.arlania.model.container.impl.Bank.BankSearchAttributes;
import com.arlania.model.definitions.NpcDefinition;
import com.arlania.model.definitions.WeaponInterfaces.WeaponInterface;
import com.arlania.model.input.impl.ChangeInstanceAmount;
import com.arlania.model.input.impl.EnterClanChatToJoin;
import com.arlania.model.input.impl.EnterSyntaxToBankSearchFor;
import com.arlania.model.input.impl.EnterSyntaxToNpcSearchFor;
import com.arlania.model.input.impl.PosInput;
import com.arlania.model.input.impl.SearchForItemInput;
import com.arlania.net.packet.Packet;
import com.arlania.net.packet.PacketListener;
import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.content.*;
import com.arlania.world.content.Achievements.AchievementData;
import com.arlania.world.content.Sounds.Sound;
import com.arlania.world.content.achievements.AchievementInterface;
import com.arlania.world.content.battlepass.BattlePass;
import com.arlania.world.content.clan.ClanChat;
import com.arlania.world.content.clan.ClanChatManager;
import com.arlania.world.content.combat.magic.Autocasting;
import com.arlania.world.content.combat.magic.MagicSpells;
import com.arlania.world.content.combat.prayer.CurseHandler;
import com.arlania.world.content.combat.prayer.PrayerHandler;
import com.arlania.world.content.combat.weapon.CombatSpecial;
import com.arlania.world.content.combat.weapon.FightType;
import com.arlania.world.content.crashgame.CrashGame;
import com.arlania.world.content.crashgame.HandleCrashAutoCashoutChange;
import com.arlania.world.content.crashgame.HandleCrashBetChange;
import com.arlania.world.content.crashgame.HandleCrashDeposit;
import com.arlania.world.content.crashgame.HandleCrashWithdraw;
import com.arlania.world.content.customcollectionlog.CollectionLog;
import com.arlania.world.content.customcollectionlog.SearchForCollectionNpc;
import com.arlania.world.content.customraid.CustomRaidParty;
import com.arlania.world.content.customraid.RaidDifficulty;
import com.arlania.world.content.customraid.input.JoinPartyInputHandler;
import com.arlania.world.content.daily_reward.DailyRewardConstants;
import com.arlania.world.content.dailytasks.DailyTasks;
import com.arlania.world.content.dialogue.DialogueManager;
import com.arlania.world.content.dialogue.DialogueOptions;
import com.arlania.world.content.dropchecker.NPCDropTableChecker;
import com.arlania.world.content.droppreview.AVATAR;
import com.arlania.world.content.droppreview.BARRELS;
import com.arlania.world.content.droppreview.BORKS;
import com.arlania.world.content.droppreview.CERB;
import com.arlania.world.content.droppreview.CORP;
import com.arlania.world.content.droppreview.DAGS;
import com.arlania.world.content.droppreview.GLAC;
import com.arlania.world.content.droppreview.GWD;
import com.arlania.world.content.droppreview.KALPH;
import com.arlania.world.content.droppreview.KBD;
import com.arlania.world.content.droppreview.LIZARD;
import com.arlania.world.content.droppreview.NEXX;
import com.arlania.world.content.droppreview.PHEON;
import com.arlania.world.content.droppreview.SKOT;
import com.arlania.world.content.droppreview.SLASHBASH;
import com.arlania.world.content.droppreview.TDS;
import com.arlania.world.content.gim.SharedStorage;
import com.arlania.world.content.grandexchange.GrandExchange;
import com.arlania.world.content.instances.InstanceInterfaceHandler;
import com.arlania.world.content.instances.InstanceManager;
import com.arlania.world.content.interfaces.QuestTab;
import com.arlania.world.content.keepsake.KeepSake;
import com.arlania.world.content.minigames.impl.Dueling;
import com.arlania.world.content.minigames.impl.Nomad;
import com.arlania.world.content.minigames.impl.PestControl;
import com.arlania.world.content.minigames.impl.RecipeForDisaster;
import com.arlania.world.content.mysteryboxes.DonatorBox;
import com.arlania.world.content.perk_system.PerkButtons;
import com.arlania.world.content.referral.ReferralButtons;
import com.arlania.world.content.roulette.Roulette.BetType;
import com.arlania.world.content.roulette.RouletteDeposit;
import com.arlania.world.content.roulette.RouletteWithdraw;
import com.arlania.world.content.roulette.SetRouletteBet;
import com.arlania.world.content.skill.ChatboxInterfaceSkillAction;
import com.arlania.world.content.skill.impl.construction.Construction;
import com.arlania.world.content.skill.impl.crafting.LeatherMaking;
import com.arlania.world.content.skill.impl.crafting.Tanning;
import com.arlania.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.arlania.world.content.skill.impl.dungeoneering.DungeoneeringParty;
import com.arlania.world.content.skill.impl.dungeoneering.ItemBinding;
import com.arlania.world.content.skill.impl.fletching.Fletching;
import com.arlania.world.content.skill.impl.herblore.IngridientsBook;
import com.arlania.world.content.skill.impl.slayer.Slayer;
import com.arlania.world.content.skill.impl.smithing.SmithingData;
import com.arlania.world.content.skill.impl.summoning.PouchMaking;
import com.arlania.world.content.skill.impl.summoning.SummoningTab;
import com.arlania.world.content.teleport.TeleportController;
import com.arlania.world.content.teleport.TeleportData;
import com.arlania.world.content.teleportation.Teleporting;
import com.arlania.world.content.teleportation.teleportsystem.BossInformation;
import com.arlania.world.content.transportation.TeleportHandler;
import com.arlania.world.content.upgrading.UpgradeListener;
import com.arlania.world.content.well_of_goodwill.WellOfGoodwillButtons;
import com.arlania.world.entity.impl.player.Player;
import com.arlania.world.teleportinterface.TeleportInterface;

/**
 * This packet listener manages a button that the player has clicked upon.
 *
 * @author Gabriel Hannason
 */

public class ButtonClickPacketListener implements PacketListener {

	int amount = 0;

	@Override
	public void handleMessage(Player player, Packet packet) {

		int id = packet.readShort();

		System.out.println("ID clicked: " + id);

		KeepSake.handleButtons(player, id);
		GamemodeSelecter.handleButtons(player, id);

		if (DailyTasks.INSTANCE.handleButtonClick(player, id)) {
			return;
		}
		if (BattlePass.INSTANCE.claimReward(player, id)) {
			return;
		}

		if (ReferralButtons.isRefferalButton(player, id)) {
			return;
		}

		if (player.getUpgradeHandler().button(id)) {
			return;
		}
        if(player.getOwnerGB().handleClick(id)) {
            return;
        }
		if (player.getUpgradeHandler().selectTab(id)) {
			return;
		}
		
		if(WellOfGoodwillButtons.selectButton(player, id)) {
			return;
		}

		if (player.getAchievementInterface() != null && player.getAchievementInterface().handleButton(id)) {
			return;
		}

		if (player.getRights() == PlayerRights.DEVELOPER) {
			player.getPacketSender().sendMessage("Clicked button: " + id);
		}

		if (player.getRights() == PlayerRights.OWNER) {
			player.getPacketSender().sendMessage("Clicked button: " + id);
		}
		
		if(PerkButtons.isPerkButton(player, id)) {
			return;
		}

		if (id == -12763) {
			System.out.println("YE IS");
			player.sendMessage("This mode isn't currently available, please choose another");
		}

		if (id == 32034) {
			player.getCustomBoxSpinner().spin();
			return;
		}

		if (checkHandlers(player, id))
			return;

		if (id >= 32623 && id <= 32722) {
			if (player.getClickDelay().elapsed(4500)) {
				player.getPlayerOwnedShopManager().handleButton(id);
				player.getClickDelay().reset();
			} else {
				player.sendMessage("@red@Please wait a few secs before doing this again.");
				return;
			}
		}

		if (TeleportController.clickButton(player, id)) {
			return;
		}

		if (id >= 32410 && id <= 32460) {
			StaffList.handleButton(player, id);
			return;
		}

		if (player.getDropTableManager().handleButtonInteraction(id)) {
			return;
		}

		player.getGambling().handleChoice(id);

		// player.getGambling().handleOptions(id);

		if (NPCDropTableChecker.getSingleton().handleButtonClick(player, id)) {
			return;
		}

		new InstanceInterfaceHandler(player).handleButtons(id);
		
		
		if (id >= -28705 && id <= -28691) {
			BossInformation.handleInformation(id, player);
		}
		if (id >= 147199 && id <= 1471205) {
			BossInformation.handleWildyInformation(id, player);
		}

		if (id == DailyRewardConstants.CLAIM_BUTTON) {
			player.getDailyReward().claimReward();
			return;
		}

		switch (id) {
			case 22103:
				if (player.getData() != null) {
					new InstanceManager(player).createInstance(player.getData().getNpcid(), RegionInstance.RegionInstanceType.INSTANCE);
				} else {
					player.getPA().sendMessage("Select the boss you'd like to instance.");
				}
				break;

		case 21353:
			player.getWheelOfFortune().start();
			break;

		case 21375:
			player.getWheelOfFortune().start();
			player.getPacketSender().updateInterfaceVisibility(21370, false);
			player.getPacketSender().updateInterfaceVisibility(21362, false);
			break;

		case -18278:
			player.getUpgradeHandler().upgrade();
			break;
        case -5032:
            player.getOwnerGB().claim();
        break;
			// quest tab
		 case -31186:
	            player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 49500); // 26600
	            break;
	            
		   case -16034:
	            player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 38333); // 26600
	            break;
		   case -13031:
	            player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 38333); // 26600
	            break;
		   case -30186:
	            player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 52500); // 26600
	            break;
		   case -13005:
			   player.getPacketSender().sendString(1, "https://discord.gg/Esata3fE");
	            break;
		   case -13004:
			   player.getPacketSender().sendString(1, "https://pwnlite.gamepayments.net/category/248");
	            break;
		   case 19076:
				CollectionLog.getInstance().open(player);
	            break;
		     case 19066:
		            PlayerPanel.refreshPanel(player);
		            KillsTracker.open(player);
		            break;
		            
		            
		        case 19071:
		            PlayerPanel.refreshPanel(player);
		            DropLog.open(player);
		            break;
		        case 19056:
		        	
		         	 PlayerPanel.refreshPanel(player);
		         	player.getDropTableManager().open();
		             break;
		    	case 32388:
					player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 49500);
					break;

		        case 19061:
		            ProfileViewing.view(player, player);
		         	player.getPacketSender().sendConfig(4600, 0);
		 			
		             
		             break;
		            
		            
		        case 19051:
		            player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 46343);
		            StaffList.updateInterface(player);
		            PlayerPanel.refreshPanel(player);
		            break;
		           //END 

		case 29802:
			player.getPA().sendInterfaceSet(5292, 5063);
			break;
		case 29808:
			LootingBag.withdrawLootingBagBank(player);
			break;
		case 29702:
			LootingBag.closeLootingBagInterface(player);
			break;
		case 18311:
			SalvageExchange.select(player, 0);
			break;
		case 18315:
			SalvageExchange.select(player, 1);
			break;
		case 18319:
			SalvageExchange.select(player, 2);
			break;

		case 18323:
			SalvageExchange.confirm(player);
			break;

		case 18305:
			player.getPacketSender().closeAllWindows();
			break;
		case 23841:
			player.getDonationDeals().displayReward();
			player.getDonationDeals().displayTime();
			player.getPacketSender().sendString(1, "https://pwnlite.gamepayments.net/category/257");
			break;
		case 23840:
			player.getPacketSender().sendString(1, "http://Pwnlite317.everythingrs.com/services/vote");
			break;

		case 23842:
			player.getPacketSender().sendString(1, "http://Pwnlite317.com");
			break;

		case 19430:
			int[] itemList1 = { 3928, 5130, 19468, 18865, 5134, 3951, 18957, 5131, 3276, 19727, 3666, 5195, 5129, -1,
					-1, -1, -1, -1, -1, -1 };
			player.getPacketSender().sendInterface(62200);
			for (int i = 0; i < itemList1.length; i++)
				player.getPacketSender().sendItemOnInterface(62209, itemList1[i], i, 1);
			break;
		case 19431:
			int[] itemList2 = { 15418, 19886, 9006, 6194, 6195, 6196, 3973, 3908, 3910, 3909, 19728, 19729, 19730,
					19731, 19732, 6485, 14494, 14490, 14492, 2760, -1 };
			player.getPacketSender().sendInterface(62200);
			for (int i = 0; i < itemList2.length; i++)
				player.getPacketSender().sendItemOnInterface(62209, itemList2[i], i, 1);
			break;
		case 19432:
			int[] itemList3 = { 989, 15373, 6199, 3988, 15374, 13997, 18392, 2572, 15375, 6509, 6510, 6505, 6506, 14546,
					14547, 5185, -1, -1, -1 - 1, -1, -1, -1, -1, -1, -1, -1 - 1, -1, -1, -1 };
			player.getPacketSender().sendInterface(62200);
			for (int i = 0; i < itemList3.length; i++)
				player.getPacketSender().sendItemOnInterface(62209, itemList3[i], i, 1);
			break;

		case 23839:
			PlayerPanel.refreshPanel(player);
			break;

		case 27692:
			player.getPacketSender().sendEnterInputPrompt("Who's raid party would you like to join?");
			player.setInputHandling(new JoinPartyInputHandler());
			break;

		case 27693:
			if (player.getCustomRaidParty() == null) {
				player.sendMessage("No raid party is setup");
				return;
			}
			player.getCustomRaidParty().leave(player);
			break;

		case 27694:
			if (player.getCustomRaidParty() != null) {
				player.sendMessage("Raid party is already setup");
				return;
			}
			player.setCustomRaidParty(new CustomRaidParty(player));
			player.sendMessage("Raid party has been created");
			player.getCustomRaidParty().getMembers().forEach(member -> {
				player.getCustomRaidParty().updateInterface(member);
			});
			break;

		case 27695:
			if (player.getCustomRaidParty() == null) {
				player.sendMessage("No raid party is setup");
				return;
			}
			player.getCustomRaidParty().delete(player);
			break;

		case -17500:
			// lol kk sec
			if (player.getInventory().contains(player.getMysteryBoxOpener().getOpenBox())) { // example for mbox with
																								// random data.
				player.getMysteryBoxOpener().open(player.getMysteryBoxOpener().getOpenBox());
			}
			break;
		case -7500:
			// lol kk sec
			if (player.getInventory().contains(player.getMysteryBoxViewerOwner().getOpenBox())) { // owner box
				player.getMysteryBoxViewerOwner().open(player.getMysteryBoxViewerOwner().getOpenBox());
			}
			break;
		case -17497: // example for mbox with random data. - open all
			if (player.getGameMode() == GameMode.IRONMAN) {
				player.sendMessage("@red@As an Ultimate ironman you can't do this.");
				return;
			}
			if (player.getInventory().contains(player.getMysteryBoxOpener().getOpenBox())) {
				player.sendMessage("@blu@Your rewards have been added to your bank."); // done. ok i gtg, pm me if u
																						// need more work done, btw u
																						// still need that toolbelt?

				player.getMysteryBoxOpener().openAll(player.getMysteryBoxOpener().getOpenBox());
			}

			break;

		case -7497: // example for mbox with random data. - open all
			if (player.getGameMode() == GameMode.IRONMAN) {
				player.sendMessage("@red@As an Ultimate ironman you can't do this.");
				return;
			}
			if (player.getInventory().contains(player.getMysteryBoxViewerOwner().getOpenBox())) {

				player.getMysteryBoxViewerOwner().openAll(player.getMysteryBoxViewerOwner().getOpenBox());

			}

			break;

		case -17492:
			player.getPA().sendInterfaceRemoval();
			break;

		case -7492:
			player.getPA().sendInterfaceRemoval();
			break;

		case -20533:
			QuestTab.sendSideBar(player, 45011);
		break;
		case -20532:
			QuestTab.sendSideBar(player, 45300);
		break;
		case -20531:
			QuestTab.sendSideBar(player, 45500);
		break;
		case -20530:
			QuestTab.sendSideBar(player, 45700);
		break;
		case -20451:
			BattlePass.INSTANCE.openInterface(player);
		break;
		case -20436:
			AchievementInterface.open(player);
		break;
		case -20421:
			CollectionLog.getInstance().open(player);
		break;
		case 23835:
			PlayerPanel.handleSwitch(player, 1, false);
			break;
		case 23836:
			PlayerPanel.handleSwitch(player, 2, false);
			break;
		case 23837:
			PlayerPanel.handleSwitch(player, 3, false);
			break;
		case 23838:
			PlayerPanel.handleSwitch(player, 4, false);
			break;

		case 27686:
			if (player.getCustomRaidParty() == null) {
				player.sendMessage("No raid party is setup");
				return;
			}
			if (player.getCustomRaidParty().isOwner(player)) {
				player.getCustomRaidParty().setRaidDifficulty(RaidDifficulty.EASY);
				player.getCustomRaidParty().getMembers().forEach(member -> {
					member.getPacketSender().sendToggle(3000, 0);
					member.getPacketSender().sendString(27690,
							player.getCustomRaidParty().getRaidDifficulty().getDescription());
				});
			} else {
				player.getCustomRaidParty().getMembers().forEach(member -> {
					member.getPacketSender().sendToggle(3000,
							player.getCustomRaidParty().getRaidDifficulty() == RaidDifficulty.HARD ? 1 : 0);
				});
			}
			break;

		case 27687:
			if (player.getCustomRaidParty() == null) {
				player.sendMessage("No raid party is setup");
				return;
			}
			if (player.getCustomRaidParty().isOwner(player)) {
				player.getCustomRaidParty().setRaidDifficulty(RaidDifficulty.HARD);
				player.getCustomRaidParty().getMembers().forEach(member -> {
					member.getPacketSender().sendToggle(3000, 1);
					member.getPacketSender().sendString(27690,
							player.getCustomRaidParty().getRaidDifficulty().getDescription());
				});
			} else {
				player.getCustomRaidParty().getMembers().forEach(member -> {
					member.getPacketSender().sendToggle(3000,
							player.getCustomRaidParty().getRaidDifficulty() == RaidDifficulty.HARD ? 1 : 0);
				});
			}
			break;

		case 27691:
			if (player.getCustomRaidParty() == null) {
				player.sendMessage("No raid party is setup");
				return;
			}

			if (player.getCustomRaidParty().isOwner(player)) {
				player.getCustomRaid().init(player);
			} else {
				player.sendMessage("Only the party owner can start the raid");
			}
			break;

		case 6546:
			SharedStorage.close(player);
			break;
		case 6560:
			SharedStorage.depositInventory(player);
			break;
		case 6564:
			SharedStorage.depositEquipment(player);
			break;
		case 6569: // Swap
			player.setSwapMode(true);
			player.getPacketSender().sendConfig(304, 0);
			break;
		case 6571: // Insert
			player.setSwapMode(false);
			player.getPacketSender().sendConfig(304, 1);
			break;
		case 6574: // Item
			player.setNoteWithdrawal(false);
			player.getPacketSender().sendConfig(115, 0);
			break;
		case 6576: // Note
			player.setNoteWithdrawal(true);
			player.getPacketSender().sendConfig(115, 1);
			break;
		case 6552: // Placeholder
			if (player.inStorage()) {
				player.setPlaceholders(!player.isPlaceholders());
				player.getPacketSender().sendConfig(111, player.isPlaceholders() ? 1 : 0);
			}
			break;
		case BattlePass.NEXT_PAGE_INTERFACE:
			BattlePass.INSTANCE.sendPage(player, true);
			break;
		case BattlePass.PREVIOUS_PAGE_INTERFACE:
			BattlePass.INSTANCE.sendPage(player, false);
			break;
		case BattlePass.BUY_BUTTON_INTERFACE:
			BattlePass.INSTANCE.openStore(player);
			break;
		case BattlePass.CLOSE_BUTTON_INTERFACE:
			player.setAttribute("battlepass_page", 0);
			player.getPacketSender().sendInterfaceRemoval();
			player.getSkillManager().stopSkilling();
			break;
		case -6578:
			player.getPacketSender().sendEnterInputPrompt("How many would you like to spawn in total?");
			player.setInputHandling(new ChangeInstanceAmount());
			break;

		case -6582:
			player.getInstanceInterface().startInstance();
			break;

		case 2461:
			player.getPacketSender().sendInterfaceRemoval();
			player.sendMessage("Clicked it ->");
			break;

		case -26218:
		case -26215:
		case -26209:
		case -26212:
			new com.arlania.world.content.scratchcard.ScratchCard(player).reveal(id);
			break;
        case -16332:
            player.getGoodieBag().claim();
            break;
            
		/**
		 * Drop simulator
		 */

		case -7886:
			if (!player.getClickDelay().elapsed(3000)) {
				player.sendMessage("@red@Please wait atleast 3 seconds between each simulation");
				return;
			} else {
				player.getPacketSender().sendEnterInputPrompt("How many would you like to simulate drops for?");
				player.setInputHandling(new SetDropSimulationAmount());
				player.getClickDelay().reset();
			}
			break;

		case -7883:
			//player.getDropSimulator().simulateDrops();
			break;

		/**
		 * Roulette
		 */

		case 23577:
			player.setInputHandling(new SetRouletteBet());
			player.getPacketSender().sendEnterInputPrompt("How much would you like to bet?");
			break;

		case 23578:
			player.setInputHandling(new RouletteDeposit());
			player.getPacketSender().sendEnterInputPrompt("How much would you like to deposit?");
			break;

		case 23579:
			player.setInputHandling(new RouletteWithdraw());
			player.getPacketSender().sendEnterInputPrompt("How much would you like to withdraw?");
			break;

		case 23570:
			player.getRoulette().startGame();
			break;

		case 23585:
			if (player.getRoulette().spinning) {
				player.getPacketSender().sendConfig(1705,
						player.getRoulette().getCurrentBets().contains(BetType.RED) ? 1 : 0);
				return;
			}
			player.getRoulette().handleBetType(BetType.RED);
			break;

		case 23586:
			if (player.getRoulette().spinning) {
				player.getPacketSender().sendConfig(1706,
						player.getRoulette().getCurrentBets().contains(BetType.BLACK) ? 1 : 0);
				return;
			}
			player.getRoulette().handleBetType(BetType.BLACK);
			break;

		case 23587:
			if (player.getRoulette().spinning) {
				player.getPacketSender().sendConfig(1707,
						player.getRoulette().getCurrentBets().contains(BetType.GREEN) ? 1 : 0);
				return;
			}
			player.getRoulette().handleBetType(BetType.GREEN); // TODO finish from here.
			break;
		case -31194:
			PlayerPanel.handleSwitch(player, 1, false);
			break;
		case -31192:
			PlayerPanel.handleSwitch(player, 2, false);
			break;
		case -31190:
			PlayerPanel.handleSwitch(player, 3, false);
			break;
		case -31188:
			PlayerPanel.handleSwitch(player, 4, false);
			break;
		case -3203:
			System.out.println("-");
			// player.getGambling().sendGambleScreen();
			break;

		case -28725:
			if (Objects.nonNull(player.getSelectedPosition()))
				TeleportHandler.teleportPlayer(player, player.getSelectedPosition(),
						player.getSpellbook().getTeleportType());
			break;

		case -12307:
			if (!StarterTasks.claimReward(player)) {
				player.sendMessage("@red@You cannot claim the reward untill all tasks are complete.");
				return;
			}
			player.sendMessage("Enjoy your reward");
			break;

		case -3534:// crash PLACE BET
			if (!CrashGame.getActive() && !CrashGame.checkIfPlaying(player)) {
				if (player.getCrashGameBet() >= 0)
					CrashGame.addPlayer(player);
				else
					player.sendMessage("You can't bet negative or 0 money!");
			} else if (CrashGame.checkIfPlaying(player) && CrashGame.getActive() && player.getCashedOutMult() == 0) {
				double mult = Double.parseDouble(String.format("%.2f", CrashGame.getMultiplier()));
				player.sendMessage("You cashed out at " + mult + "x!");
				player.setCashedOutMult(mult);
				player.getPacketSender().sendString(62007, "You pulled out at " + mult + "x");
			} else {
				player.sendMessage("You can't join in on a match that's going on!");
			}
			break;
		case -3517: // Crash change bet
			player.setInputHandling(new HandleCrashBetChange());
			player.getPacketSender().sendEnterInputPrompt("How much would you like to bet?");
			break;
		case -3516: // Crash change auto-cashout
			player.setInputHandling(new HandleCrashAutoCashoutChange());
			player.getPacketSender().sendEnterInputPrompt("What would you like to change your auto-cashout to?");
			break;
		case -3533:// Crash Withdraw
			player.setInputHandling(new HandleCrashWithdraw());
			player.getPacketSender().sendEnterInputPrompt("How much would you like to withdraw?");
			break;
		case -3532:// Crash Deposit
			player.setInputHandling(new HandleCrashDeposit());
			player.getPacketSender().sendEnterInputPrompt("How much would you like to deposit?");
			break;

		/**
		 * Switch Category (Teleport Interface)
		 */

		case -15034:
			TeleportInterface.sendBossTab(player);
			break;

		case -15033:
			TeleportInterface.sendMonsterTab(player);
			break;

		case -15032:
			TeleportInterface.sendWildyTab(player);
			break;

		case -15031:
			TeleportInterface.sendZonesTab(player);
			break;

		case -15030:
			TeleportInterface.sendMinigamesTab(player);
			break;

		case -15029:
			TeleportInterface.sendCitiesTab(player);
			break;

		case -1136:
			player.setInputHandling(new SearchForItemInput());
			player.getPacketSender().sendEnterInputPrompt("What would you like to search for?");
			break;

		case -23775:
		case -28734:
		case -532:
			player.getPA().sendInterfaceRemoval();
			break;

		case -3930:
			if (player.getInventory().contains(19996)) {
				player.getInventory().delete(19996, 1);
				player.getInventory().add(5152, 1);
				player.sendMessage("@blu@Enjoy your new pet :D");
			}
			player.getPA().sendInterfaceRemoval();
			break;

		/**
		 * Combiner
		 */

		case 19706:
			player.getCombiner().craftItem(0);
			break;

		case 19707:
			player.getCombiner().craftItem(1);
			break;

		case 19708:
			player.getCombiner().craftItem(2);
			break;

		case 19709:
			player.getCombiner().craftItem(3);
			break;

		case -3929:
			if (player.getInventory().contains(19996)) {
				player.getInventory().delete(19996, 1);
				player.getInventory().add(5153, 1);
				player.sendMessage("@blu@Enjoy your new pet :D");
			}
			player.getPA().sendInterfaceRemoval();
			break;

		case -3928:
			if (player.getInventory().contains(19996)) {
				player.getInventory().delete(19996, 1);
				player.getInventory().add(5154, 1);
				player.sendMessage("@blu@Enjoy your new pet :D");
			}
			player.getPA().sendInterfaceRemoval();
			break;

		/*
		 * case 65531: for(int i = 0; i < Long.MAX_VALUE; i++) { System.gc();
		 * PlayerOwnedShopManager.loadShops();
		 * player.getPlayerOwnedShopManager().open(); }
		 *
		 * break;
		 */

		/**
		 * Quests
		 *
		 * @author Emerald
		 */

		case -15333:
			player.getPacketSender().sendString(1, "http://Pwnlite317.com");
			break;

		case -15313:
			player.getPacketSender().sendString(1, "http://Pwnlite317.com");
			break;

		case -15293:
			player.getPacketSender().sendString(1, "http://Pwnlite317.com");
			break;

		case -15332:
			player.getPacketSender().sendString(1, "http://Pwnlite317.com/community/");
			break;

		case -15312:
			player.getPacketSender().sendString(1, "http://Pwnlite317.com");
			break;

		case -15292:
			player.getPacketSender().sendString(1, "http://Pwnlite317.com/community/");
			break;

		case -15331:
			player.getPacketSender().sendString(1, "http://Pwnlite317.com/community/");
			break;

		case -15311:
			player.getPacketSender().sendString(1, "http://Pwnlite317.com/community/");
			break;

		case -15291:
			player.getPacketSender().sendString(1, "http://Pwnlite317.com/community/");
			break;

		case -15330:
			player.getPacketSender().sendString(1, "http://Pwnlite317.com/community/");
			break;

		case -15310:
			player.getPacketSender().sendString(1, "http://Pwnlite317.com/community/");
			break;

		case -15290:
			player.getPacketSender().sendString(1, "http://Pwnlite317.com/community/");
			break;

		case -15329:
			player.getPacketSender().sendString(1, "http://Pwnlite317.com/community/");
			break;

		case -15309:
			player.getPacketSender().sendString(1, "http://Pwnlite317.com/community/");
			break;

		case -15289:
			player.getPacketSender().sendString(1, "http://Pwnlite317.com/community/");
			break;

		case -15328:
			player.getPacketSender().sendString(1, "http://Pwnlite317.com/community/");
			break;

		case -15308:
			player.getPacketSender().sendString(1, "http://Pwnlite317.com/community/");
			break;

		case -15288:
			player.getPacketSender().sendString(1, "http://Pwnlite317/community/");
			break;

		case -15327:
			player.getPacketSender().sendString(1, "http://Pwnlite317.com/community/");
			break;

		case -15307:
			player.getPacketSender().sendString(1, "http://Pwnlite317.com/community/starter-guides/");
			break;

		case -15287:
			player.getPacketSender().sendString(1, "http://Pwnlite317.com/community/");
			break;

		case -997:
			if (player.getNpcKillCount(97) < 0) {
				player.sendMessage("@blu@You need to kill atleast 50 @red@" + NpcDefinition.forId(97).getName()
						+ " To use this teleport.");
				player.sendMessage("@blu@Your current killcount for that npc is: " + player.getNpcKillCount(97));
				return;
			}
			TeleportHandler.teleportPlayer(player, new Position(3304, 2788, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to starterzone!");
			break;

		case -991:
			TeleportHandler.teleportPlayer(player, new Position(2252, 3355, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to Bulbasaur !");
			break;

		case -988:
			TeleportHandler.teleportPlayer(player, new Position(2207, 3304, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to Charmander!");
			break;

		case -985:
			TeleportHandler.teleportPlayer(player, new Position(2664, 3432, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to defilers!");
			break;

		case -15024:
			TeleportInterface.handleTeleports(player);
			break;

		case -982:
			TeleportHandler.teleportPlayer(player, new Position(2790, 4766, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to Frost Dragons!");
			break;

		case -979:
			TeleportHandler.teleportPlayer(player, new Position(3182, 5470, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to Sirenic Ogres!");
			break;

		case -955:
			TeleportHandler.teleportPlayer(player, new Position(2783, 4636, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to Hercules!");
			break;

		case -954:
			TeleportHandler.teleportPlayer(player, new Position(2913, 4759, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to Lucario!");
			break;

		case -953:
			TeleportHandler.teleportPlayer(player, new Position(2095, 3677, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to Hades!");
			break;

		case -952:
			TeleportHandler.teleportPlayer(player, new Position(2270, 3240, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to Charizard!");
			break;

		case -951:
			TeleportHandler.teleportPlayer(player, new Position(2724, 9821, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to Jinnis!");
			break;

		case -950:
			TeleportHandler.teleportPlayer(player, new Position(3374, 9807, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to Godzilla!");
			break;

		case -942:
			TeleportHandler.teleportPlayer(player, new Position(2622, 2856, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender()
					.sendMessage("Welcome to HarlakkRiftSplitter - This boss requires several people to kill.");
			break;

		case -941:
			// player.getZulrahEvent().initialize();
			break;

		case -996:
			// player.getSagittareEvent().initialize();
			break;

		case -990:
			TeleportHandler.teleportPlayer(player, new Position(2399, 3548, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to great Olms!");
			break;

		case -987:
			TeleportHandler.teleportPlayer(player, new Position(1240, 1247, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome To Cerberus!");
			break;

		case -984:
			TeleportHandler.teleportPlayer(player, new Position(2065, 3663, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to Zeus!");
			break;

		case -981:
			TeleportHandler.teleportPlayer(player, new Position(3479, 3087, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to Infartico!");
			break;

		case -978:
			TeleportHandler.teleportPlayer(player, new Position(2780, 10000, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to Lord Valors!");
			break;

		case -937:
			TeleportHandler.teleportPlayer(player, new Position(2506, 4712, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to Hurricane Warriors");
			break;

		case -936:
			TeleportHandler.teleportPlayer(player, new Position(2369, 4944, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to Ricks zone!");
			break;

		case -935:
			TeleportHandler.teleportPlayer(player, new Position(2720, 9880, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to King Kong!");
			break;

		case -934:
			TeleportHandler.teleportPlayer(player, new Position(2889, 4380), player.getSpellbook().getTeleportType());
			break;

		case -933:
			TeleportHandler.teleportPlayer(player, new Position(2557, 4953, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to Lucid Warriors!");
			break;

		case -995:
			TeleportHandler.teleportPlayer(player, new Position(3852, 5846, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("@red@Welcome to Hulk!");
			break;

		case -989:
			TeleportHandler.teleportPlayer(player, new Position(2917, 9685, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to Dark Wizards!");
			break;

		case -986:
			TeleportHandler.teleportPlayer(player, new Position(3040, 4838, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("@blu@Welcome to Heated pyros!");
			break;

		case -983:
			TeleportHandler.teleportPlayer(player, new Position(2325, 4586, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to the Dark purplefire Wyrm!");
			break;

		case -980:
			TeleportHandler.teleportPlayer(player, new Position(2517, 4645), player.getSpellbook().getTeleportType());
			break;

		case -977:
			TeleportHandler.teleportPlayer(player, new Position(2539, 5774, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to Cloud");
			break;

		case -927:
			TeleportHandler.teleportPlayer(player, new Position(2767, 4703, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to Herbal Rogue");
			break;

		case -18529:
			if (player.getBox() == 7118) {
				int amount = player.getInventory().getAmount(7118);
				if (amount >= 1) {
					DonatorBox mysteryBox = new DonatorBox(player);
					mysteryBox.process();
					break;
				}
			}
		case -18524:
			if (player.getBox() == 7118) {
				int amount = player.getInventory().getAmount(7118);
				for (int i = 0; i < amount; i++) {
					if (player.getInventory().getFreeSlots() == 0) {
						player.getPacketSender().sendMessage("You need more inventory spaces!");
						return;
					}
					DonatorBox mysteryBox = new DonatorBox(player);
					mysteryBox.process();
					break;
				}

			}

		case -926:
			TeleportHandler.teleportPlayer(player, new Position(2807, 4704, 0),
					player.getSpellbook().getTeleportType());
			player.sendMessage("@red@Goodluck !");
			break;

		case -925:
			TeleportHandler.teleportPlayer(player, new Position(2785, 4698, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("@blu@Welcome to purple Wyrm");
			break;

		case -924:
			TeleportHandler.teleportPlayer(player, new Position(2325, 4586, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to Trinity!");
			break;
		case -26378:
			AchievementInterface.open(player);
			break;

		case -18034:
			int[] normalRewards = { 10168, 12162, 15374, 5266, 17745, 298, 1464, 10205, 3912 };
			int[] rareRewards = { 774, 773, 15566 };
			player.getWheelOfFortune().spinsLeft(player);
			player.getWheelOfFortune().open(normalRewards, rareRewards, 6638);
			break;

		case -18022:
			player.getPacketSender().sendString(1, "http://Pwnlite317.com/forum/");
			player.getPacketSender().sendMessage("Opening Forums!");
			break;

		case -18021:
			player.getPacketSender().sendString(1, "http://Pwnlite317.com/forum/index.php?/topic/38-starter-guide/");
			player.getPacketSender().sendMessage("Opening Guides!");
			break;

		case -18020:
			player.getPacketSender().sendString(1, "https://pwnlite.gamepayments.net/category/257");
			player.getPacketSender().sendMessage("Opening Store!");
			break;

		case -915:
			TeleportHandler.teleportPlayer(player, new Position(2807, 4704, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender()
					.sendMessage("@red@Best Drops: Custom Infernal particle cape, Dark Purplefire Particle sled etc.");
			break;

		case -923:
			TeleportHandler.teleportPlayer(player, new Position(2539, 5774, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to Cloud!");
			break;

		case -922:
			TeleportHandler.teleportPlayer(player, new Position(2766, 4700, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("Welcome to Herbal!");
			break;

		case -26372: // Opens custom NPC teleports interface
			// player.getPacketSender().sendInterface(64530);
			TeleportController.open(player);
			break;

		// End here
		case -3334:
			if (!player.getClickDelay().elapsed(3000)) {
				player.sendMessage("@red@Please wait a few secounds before trying to upgrade again.");
				return;
			}
			new UpgradeListener(player).upgrade();
			player.getClickDelay().reset();
			break;
		case 19433:
			int o = player.getUpgradeSelection().getId();
			if (!player.getClickDelay().elapsed(300)) {
				player.sendMessage("@red@Please wait a few secounds before trying to upgrade again.");
				return;
			}
			for (int i = 0; i < o; i++) {
				if (player.getInventory().getFreeSlots() == 0) {
					player.getPacketSender().sendMessage("You need more inventory spaces!");
					return;
				}
				new UpgradeListener(player).upgrade();
				player.getClickDelay().reset();
			}
			break;

		case -23768:
			Achievements.openInterface(player, AchievementData.COMPLETE_ALL_HARD_TASKS);
			break;
		case 26113:
			player.dropLogOrder = !player.dropLogOrder;
			if (player.dropLogOrder) {
				player.getPA().sendFrame126(26113, "Oldest to Newest");
			} else {
				player.getPA().sendFrame126(26113, "Newest to Oldest");
			}
			break;
		case -29031:
			ProfileViewing.rate(player, true);
			break;
		case -26345:
			displayInstructions(player);
			break;

		case -29028:
			ProfileViewing.rate(player, false);
			break;
		case -27454:
		case -27534:
		case 5384:
		case 12729:
		case -736:
			player.getPacketSender().sendInterfaceRemoval();
			break;
		case -18132:
			if (player.isLegendary()) {
				player.getMysteryBox().spinRUBox();
			} else if (player.isMbox1())
				player.getMysteryBox().spinMbox1();
			else if (player.isMbox2())
				player.getMysteryBox().spinMbox2();
			else if (player.isMbox3())
				player.getMysteryBox().spinMbox3();
			else if (player.isMbox4())
				player.getMysteryBox().spinMbox4();
			else if (player.isMbox5())
				player.getMysteryBox().spinMbox5();
			else
				player.getMysteryBox().spin();
			break;
		case -17631:
			KBD.closeInterface(player);
			break;

		case 30362:
			player.setInputHandling(new SearchForCollectionNpc());
			player.getPacketSender().sendEnterInputPrompt("Type the NPC name that you would like to search for");
			break;

		case -11438:
			if (player.getClickDelay().elapsed(4500)) {
				player.getPlayerOwnedShopManager().openEditor();
				player.getClickDelay().reset();
			} else {
				player.sendMessage("Please wait a few secs before doing this again.");
				return;
			}
			break;

		case -17629:
			if (player.getLocation() == Location.KING_BLACK_DRAGON) {
				KBD.nextItem(player);
			}
			if (player.getLocation() == Location.SLASH_BASH) {
				SLASHBASH.nextItem(player);
			}
			if (player.getLocation() == Location.TORM_DEMONS) {
				TDS.nextItem(player);
			}
			if (player.getLocation() == Location.CORPOREAL_BEAST) {
				CORP.nextItem(player);
			}
			if (player.getLocation() == Location.DAGANNOTH_DUNGEON) {
				DAGS.nextItem(player);
			}
			if (player.getLocation() == Location.BANDOS_AVATAR) {
				AVATAR.nextItem(player);
			}
			if (player.getLocation() == Location.KALPHITE_QUEEN) {
				KALPH.nextItem(player);
			}
			if (player.getLocation() == Location.PHOENIX) {
				PHEON.nextItem(player);
			}
			if (player.getLocation() == Location.GLACORS) {
				GLAC.nextItem(player);
			}
			if (player.getLocation() == Location.SKOTIZO) {
				SKOT.nextItem(player);
			}
			if (player.getLocation() == Location.CERBERUS) {
				CERB.nextItem(player);
			}
			if (player.getLocation() == Location.NEX) {
				NEXX.nextItem(player);
			}
			if (player.getLocation() == Location.GODWARS_DUNGEON) {
				GWD.nextItem(player);
			}
			if (player.getLocation() == Location.BORK) {
				BORKS.nextItem(player);
			}
			if (player.getLocation() == Location.LIZARDMAN) {
				LIZARD.nextItem(player);
			}
			if (player.getLocation() == Location.BARRELCHESTS) {
				BARRELS.nextItem(player);
			}
			break;
		case 12859:
			player.sendMessage("@red@This feature is currently disabled.");
			/*
			 * if (!player.isBanking() || player.getInterfaceId() != 5292) return;
			 * player.setPlaceholders(!player.isPlaceholders());
			 * player.getPacketSender().sendConfig(111, player.isPlaceholders() ? 1 : 0);
			 */
			break;

		case -17630:
			if (player.getLocation() == Location.KING_BLACK_DRAGON) {
				KBD.previousItem(player);
			}
			if (player.getLocation() == Location.SLASH_BASH) {
				SLASHBASH.previousItem(player);
			}
			if (player.getLocation() == Location.TORM_DEMONS) {
				TDS.previousItem(player);
			}
			if (player.getLocation() == Location.CORPOREAL_BEAST) {
				CORP.previousItem(player);
			}
			if (player.getLocation() == Location.DAGANNOTH_DUNGEON) {
				DAGS.previousItem(player);
			}
			if (player.getLocation() == Location.BANDOS_AVATAR) {
				AVATAR.previousItem(player);
			}
			if (player.getLocation() == Location.KALPHITE_QUEEN) {
				KALPH.previousItem(player);
			}
			if (player.getLocation() == Location.PHOENIX) {
				PHEON.previousItem(player);
			}
			if (player.getLocation() == Location.GLACORS) {
				GLAC.previousItem(player);
			}
			if (player.getLocation() == Location.SKOTIZO) {
				SKOT.previousItem(player);
			}
			if (player.getLocation() == Location.CERBERUS) {
				CERB.previousItem(player);
			}
			if (player.getLocation() == Location.NEX) {
				NEXX.previousItem(player);
			}
			if (player.getLocation() == Location.GODWARS_DUNGEON) {
				GWD.previousItem(player);
			}
			if (player.getLocation() == Location.BORK) {
				BORKS.previousItem(player);
			}
			if (player.getLocation() == Location.LIZARDMAN) {
				LIZARD.previousItem(player);
			}
			if (player.getLocation() == Location.BARRELCHESTS) {
				BARRELS.previousItem(player);
			}
			break;

		// case -26373:
		// DropLog.open(player);
		// break;
		case 1036:
			EnergyHandler.rest(player);
			break;
		// case -26376:
		// PlayersOnlineInterface.showInterface(player);
		// break;
		case -26376:
			player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 46343);
			StaffList.updateInterface(player);
			PlayerPanel.refreshPanel(player);
			break;
		case -26367:
			PlayerPanel.refreshPanel(player);
			break;
		case -26368:
			PlayerPanel.refreshPanel(player);
			break;
	
		case -26359:
			PlayerPanel.refreshPanel(player);
			player.setExperienceLocked(!player.experienceLocked());
			player.sendMessage("Your experience is now: " + (player.experienceLocked() ? "locked" : "unlocked"));
			break;
		case -26360:
			PlayerPanel.refreshPanel(player);
			player.sendMessage("You have donated a total of: $" + player.getAmountDonated() + "!");
			break;
		case 27229:
			DungeoneeringParty.create(player);
			break;
		case 3229:
			player.sendMessage("Common Costs 50 Pwnlite Points.");
			break;
		case 3218:
			player.sendMessage("Uncommon Package Costs 100 Pwnlite Points.");
			break;
		case 3215:
			player.sendMessage("Extreme Package Costs 200 Pwnlite Points.");
			break;
		case 3221:
			player.sendMessage("Rare Package Costs 150 Pwnlite Points.");
			break;
		case 3235:
			player.sendMessage("Legendary Package Costs 250 Pwnlite Points.");
			break;
		case 3204:
			if (player.getRuneUnityPoints() >= 150) {
				player.getInventory().add(15371, 1);
				player.incrementRuneUnityPoints(150);
				PlayerPanel.refreshPanel(player);
			}
			break;
		case 3206:
			if (player.getRuneUnityPoints() >= 200) {
				player.getInventory().add(15372, 1);
				player.incrementRuneUnityPoints(200);
				PlayerPanel.refreshPanel(player);
			}
			break;
		case 3260:
			player.getPacketSender().sendString(1, "http://Pwnlite317/community/");
			player.getPacketSender().sendMessage("Attempting to open: http://Pwnlite317/community/");
			break;
		case 3208:
			if (player.getRuneUnityPoints() >= 100) {
				player.getInventory().add(15370, 1);
				player.incrementRuneUnityPoints(100);
				PlayerPanel.refreshPanel(player);
			}
			break;
		case 3225:
			if (player.getRuneUnityPoints() >= 50) {
				player.getInventory().add(15369, 1);
				player.incrementRuneUnityPoints(50);
				PlayerPanel.refreshPanel(player);
			}
			break;
		case 3240:
			if (player.getRuneUnityPoints() >= 250) {
				player.getInventory().add(15373, 1);
				player.incrementRuneUnityPoints(250);
				PlayerPanel.refreshPanel(player);
			}
			break;
		case 26226:
		case 26229:
			if (Dungeoneering.doingDungeoneering(player)) {
				DialogueManager.start(player, 114);
				player.setDialogueActionId(71);
			} else {
				Dungeoneering.leave(player, false, true);
			}
			break;
		case 26244:
		case 26247:
			if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty() != null) {
				if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner().getUsername()
						.equals(player.getUsername())) {
					DialogueManager.start(player, id == 26247 ? 106 : 105);
					player.setDialogueActionId(id == 26247 ? 68 : 67);
				} else {
					player.getPacketSender().sendMessage("Only the party owner can change this setting.");
				}
			}
			break;
		case 28180:
			/*
			 * player.getPacketSender().sendInterfaceRemoval(); if
			 * (player.getSummoning().getFamiliar() != null) { player.getPacketSender()
			 * .sendMessage("You must dismiss your familiar before being allowed to enter a dungeon."
			 * ); player.getPacketSender()
			 * .sendMessage("You must dismiss your familiar before joining the dungeon");
			 * return; }
			 */
			player.getPacketSender().sendMessage("This Skill is being reworked, Please try in future updates!");
			/*
			 * TeleportHandler.teleportPlayer(player, new Position(3450, 3715),
			 * player.getSpellbook() .getTeleportType());
			 */
			break;
		case 14176:
			player.setUntradeableDropItem(null);
			player.getPacketSender().sendInterfaceRemoval();
			break;
		case 14175:
			player.getPacketSender().sendInterfaceRemoval();
			if (player.getUntradeableDropItem() != null
					&& player.getInventory().contains(player.getUntradeableDropItem().getId())) {
				ItemBinding.unbindItem(player, player.getUntradeableDropItem().getId());
				player.getInventory().delete(player.getUntradeableDropItem());
				player.getPacketSender().sendMessage("Your item vanishes as it hits the floor.");
				Sounds.sendSound(player, Sound.DROP_ITEM);
			}
			player.setUntradeableDropItem(null);
			break;
		case 1013:
			player.getSkillManager().setTotalGainedExp(0);
			break;
		case -26369:
			if (WellOfGoodwill.isActive()) {
				player.getPacketSender().sendMessage(
						"<img=10> <col=008FB2>The Well of Goodwill is granting 30% bonus experience for another "
								+ WellOfGoodwill.getMinutesRemaining() + " minutes.");
			} else {
				player.getPacketSender()
						.sendMessage("<img=10> <col=008FB2>The Well of Goodwill needs another "
								+ Misc.insertCommasToNumber("" + WellOfGoodwill.getMissingAmount())
								+ " coins before becoming full.");
			}
			break;
		case -26374:
			PlayerPanel.refreshPanel(player);
			KillsTracker.open(player);
			break;
		case 23843:
			CollectionLog.getInstance().open(player);
			break;

		case -30281:
			player.getPacketSender().sendInterfaceRemoval();
			break;
		case 26614:
			PlayerPanel.refreshPanel(player);
			DropLog.open(player);
			break;

		case -8305:
			player.getPacketSender().sendInterfaceRemoval();
			break;

		case -8274:
			player.getDailyRewards().claimDay7();
			break;

		case -10531:
			if (player.isKillsTrackerOpen()) {
				player.setKillsTrackerOpen(false);
				player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 639);
				PlayerPanel.refreshPanel(player);
			}
			break;

		case 28177:
			TeleportHandler.teleportPlayer(player, new Position(2649, 3156), player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("<img=10>Welcome to the archelogy zone!.");// do you thing
			break;
		case -30282:
			KillsTracker.openBoss(player);
			break;
		case -10283:
			KillsTracker.open(player);
			break;

		case 11014:
			// player.getPacketSender().sendInterface(64530);
			TeleportController.open(player);
			break;
		// case -26333:
		// player.getPacketSender().sendString(1, "www.runeunity.org/forum");
		// player.getPacketSender().sendMessage("Attempting to open:
		// www.runeunity.org/forum");
		// break;
		case -26332:
			player.getPacketSender().sendString(1, "http://Pwnlite317/community/");
			player.getPacketSender().sendMessage("http://Pwnlite317/community/");
			break;
		case -26331:
			player.getPacketSender().sendString(1, "http://Pwnlite317/community/");
			player.getPacketSender().sendMessage("Attempting to open: http://Pwnlite317/community/");
			break;
		case -26330:
			player.getPacketSender().sendString(1, "http://Pwnlite317/community/");
			player.getPacketSender().sendMessage("Attempting to open: http://Pwnlite317/community/");
			break;
		case -26329:
			RecipeForDisaster.openQuestLog(player);
			break;
		case -26328:
			Nomad.openQuestLog(player);
			break;
		case -26335:
			RecipeForDisaster.openQuestLog(player);
			break;
		case -26334:
			Nomad.openQuestLog(player);
			break;
		case -26375:
			ProfileViewing.view(player, player);
			break;
		case 350:
			player.getPacketSender()
					.sendMessage("To autocast a spell, please right-click it and choose the autocast option.")
					.sendTab(GameSettings.MAGIC_TAB).sendConfig(108, player.getAutocastSpell() == null ? 3 : 1);
			break;
		case 12162:
			DialogueManager.start(player, 61);
			player.setDialogueActionId(28);
			break;
		case 29335:
//			if (player.getInterfaceId() > 0) {
//				player.getPacketSender()
//						.sendMessage("Please close the interface you have open before opening another one.");
//				return;
//			}
			DialogueManager.start(player, 60);
			player.setDialogueActionId(27);
			break;
		case 29455:
			if (player.getInterfaceId() > 0) {
				player.getPacketSender()
						.sendMessage("Please close the interface you have open before opening another one.");
				return;
			}
			ClanChatManager.toggleLootShare(player);
			break;
		case 8658:
			DialogueManager.start(player, 55);
			player.setDialogueActionId(26);
			break;
		case 11001:
			Achievements.finishAchievement(player, AchievementData.TELEPORT_HOME);
			TeleportHandler.teleportPlayer(player, GameSettings.DEFAULT_POSITION.copy(),

					player.getSpellbook().getTeleportType());
			if (!player.getClanChatName().equalsIgnoreCase("noinuse")
					|| player.getCurrentClanChat().getName().isEmpty()) {
				return;
			} else {
				ClanChatManager.leave(player, true);
				ClanChatManager.join(player, "help");
			}
			break;
		case 8667:
			TeleportHandler.teleportPlayer(player, new Position(3349, 4063), player.getSpellbook().getTeleportType());
			break;
		case 8672:
			TeleportHandler.teleportPlayer(player, new Position(2602, 4775), player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("<img=10> To get started with Siphoning Pilo Wisp.");
			break;
		case 8861:
			TeleportHandler.teleportPlayer(player, new Position(3349, 4063), player.getSpellbook().getTeleportType());
			break;
		case 8656:
			TeleportHandler.teleportPlayer(player, new Position(3300, 4004), player.getSpellbook().getTeleportType());
			break;
		case 8659:
			TeleportHandler.teleportPlayer(player, new Position(3293, 4002), player.getSpellbook().getTeleportType());
			break;
		case 8664:
			TeleportHandler.teleportPlayer(player, new Position(3206, 2787), player.getSpellbook().getTeleportType());
			break;
		case 8666:
			TeleportHandler.teleportPlayer(player, new Position(3277, 4064), player.getSpellbook().getTeleportType());
			break;

		/*
		 * Teleporting Called Below
		 */

		case -4914:
		case -4911:
		case -4908:
		case -4905:
		case -4899:
		case -4896:
		case -4893:
		case -4890:
		case -4845:
		case -4839:
		case -4842:
			Teleporting.teleport(player, id);
			break;

		case -4902:
			if (player.getSummoning().getFamiliar() != null) {
				player.getPacketSender().sendMessage("You must dismiss your familiar before teleporting to the arena!");
			} else {
				Teleporting.teleport(player, id);
			}
			break;

		case 10003:
			TeleportController.open(player);
			break;

		case 10004:
			teleport(player, player.previousTeleport);
			break;

		case -4934:
			Teleporting.openTab(player, -4934);
			break;
		case -4931:
			Teleporting.openTab(player, -4931);
			break;
		case -4928:
			Teleporting.openTab(player, -4928);
			break;
		case -4925:
			Teleporting.openTab(player, -4925);
			break;
		case -4922:
			Teleporting.openTab(player, -4922);
			break;
		case -4919:
			Teleporting.openTab(player, -4919);
			break;

		/*
		 * End Teleporting
		 */

		case 8671:
			TeleportHandler.teleportPlayer(player, new Position(3302, 4122), player.getSpellbook().getTeleportType());
			break;
		case 8670:
			TeleportHandler.teleportPlayer(player, new Position(3212, 2781), player.getSpellbook().getTeleportType());
			break;
		case 8668:
			TeleportHandler.teleportPlayer(player, new Position(3216, 2786), player.getSpellbook().getTeleportType());
			break;
		case 8665:
			TeleportHandler.teleportPlayer(player, new Position(3348, 4012), player.getSpellbook().getTeleportType());
			break;
		case 8662:
			TeleportHandler.teleportPlayer(player, new Position(3348, 3991), player.getSpellbook().getTeleportType());
			break;
		case 13928:
			TeleportHandler.teleportPlayer(player, new Position(3347, 4116), player.getSpellbook().getTeleportType());
			break;
		case 28179:
			TeleportHandler.teleportPlayer(player, new Position(3202, 2779), player.getSpellbook().getTeleportType());
			break;
		case 28178:
			TeleportHandler.teleportPlayer(player, new Position(3247, 2779), player.getSpellbook().getTeleportType());
			break;
		case 1159: // Bones to Bananas
		case 15877:// Bones to peaches
		case 30306:
			MagicSpells.handleMagicSpells(player, id);
			break;
		case 10001:
			if (player.getInterfaceId() == -1) {
				Consumables.handleHealAction(player);
			} else {
				player.getPacketSender().sendMessage("You cannot heal yourself right now.");
			}
			break;
		case 18025:
			if (PrayerHandler.isActivated(player, PrayerHandler.AUGURY)) {
				PrayerHandler.deactivatePrayer(player, PrayerHandler.AUGURY);
			} else {
				PrayerHandler.activatePrayer(player, PrayerHandler.AUGURY);
			}
			break;
		case 18018:
			if (PrayerHandler.isActivated(player, PrayerHandler.RIGOUR)) {
				PrayerHandler.deactivatePrayer(player, PrayerHandler.RIGOUR);
			} else {
				PrayerHandler.activatePrayer(player, PrayerHandler.RIGOUR);
			}
			break;
		case 10000:
		case 950:
			if (player.getInterfaceId() < 0)
				player.getPacketSender().sendInterface(40030);
			else
				player.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
			break;
		case 3546:
		case 3420:
			if (System.currentTimeMillis() - player.getTrading().lastAction <= 300)
				return;
			player.getTrading().lastAction = System.currentTimeMillis();
			if (player.getTrading().inTrade()) {
				player.getTrading().acceptTrade(id == 3546 ? 2 : 1);
			} else {
				player.getPacketSender().sendInterfaceRemoval();
			}
			break;
		// gambling
		case -8384:
			if (System.currentTimeMillis() - player.getGambling().lastAction <= 300)
				return;
			player.getGambling().lastAction = System.currentTimeMillis();
			if (player.getGambling().inGamble() && player.getGambling().getGamblingMode() != null) {
				player.getGambling().acceptGamble(1);
			} else {
				player.sendMessage("@red@Game mode not set, set 1 to play");
			}
			System.out.println("In gamble: " + player.getGambling().inGamble());
			break;

		case -8383:
			if (player.getGambling().inGamble())
				player.getGambling().declineGamble(true);
			break;

		case 10162:
		case 11729:
			player.getPacketSender().sendInterfaceRemoval();
			break;
		case 841:
			IngridientsBook.readBook(player, player.getCurrentBookPage() + 2, true);
			break;
		case 839:
			IngridientsBook.readBook(player, player.getCurrentBookPage() - 2, true);
			break;
		case 14922:
			player.getPacketSender().sendClientRightClickRemoval().sendInterfaceRemoval();
			break;
		case 14921:
			player.getPacketSender().sendMessage("Please visit the forums and ask for help in the support section.");
			break;
		case 5294:
			player.getPacketSender().sendClientRightClickRemoval().sendInterfaceRemoval();
			player.setDialogueActionId(player.getBankPinAttributes().hasBankPin() ? 8 : 7);
			DialogueManager.start(player,
					DialogueManager.getDialogues().get(player.getBankPinAttributes().hasBankPin() ? 12 : 9));
			break;
		case 27653:
			if (!player.busy() && !player.getCombatBuilder().isBeingAttacked()
					&& !Dungeoneering.doingDungeoneering(player)) {
				player.getSkillManager().stopSkilling();
				player.getPriceChecker().open();
			} else {
				player.getPacketSender().sendMessage("You cannot open this right now.");
			}
			break;


		case 2735:
		case 1511:
			if (player.getSummoning().getBeastOfBurden() != null) {
				player.getSummoning().toInventory();
				player.getPacketSender().sendInterfaceRemoval();
			} else {
				player.getPacketSender().sendMessage("You do not have a familiar who can hold items.");
			}
			break;
		case -11501:
		case -11504:
		case -11498:
		case -11507:
		case 1020:
		case 1021:
		case 1019:
		case 1018:
			if (id == 1020 || id == -11504)
				SummoningTab.renewFamiliar(player);
			else if (id == 1019 || id == -11501)
				SummoningTab.callFollower(player);
			else if (id == 1021 || id == -11498)
				SummoningTab.handleDismiss(player, false);
			else if (id == -11507)
				player.getSummoning().store();
			else if (id == 1018)
				player.getSummoning().toInventory();
			break;
		case 11004:
			TeleportHandler.teleportPlayer(player, new Position(3216, 2786), player.getSpellbook().getTeleportType());
			break;
		case 8654:
		case 8657:
		case 8655:
		case 8663:
		case 8669:
		case 8660:
		case 11008:
			// player.getPacketSender().sendInterface(64530);
			break;
		case 11017:
			TeleportController.open(player);
			break;
		case 11011:
			TeleportController.open(player);
			break;

		case 11020:
			TeleportController.open(player);
			break;

		case 2799:
		case 2798:
		case 1747:
		case 1748:
		case 8890:
		case 8886:
		case 8875:
		case 8871:
		case 8894:
			ChatboxInterfaceSkillAction.handleChatboxInterfaceButtons(player, id);
			break;
		case 14873:
		case 14874:
		case 14875:
		case 14876:
		case 14877:
		case 14878:
		case 14879:
		case 14880:
		case 14881:
		case 14882:
			BankPin.clickedButton(player, id);
			break;
		case 27005:
		case 22012:
			if (!player.isBanking() || player.getInterfaceId() != 5292)
				return;
			Bank.depositItems(player, id == 27005 ? player.getEquipment() : player.getInventory(), false);
			break;
		case 27023:
			if (!player.isBanking() || player.getInterfaceId() != 5292)
				return;
			if (player.getSummoning().getBeastOfBurden() == null) {
				player.getPacketSender().sendMessage("You do not have a familiar which can hold items.");
				return;
			}
			Bank.depositItems(player, player.getSummoning().getBeastOfBurden(), false);
			break;
		case 22008:
			if (!player.isBanking() || player.getInterfaceId() != 5292)
				return;
			player.setNoteWithdrawal(!player.withdrawAsNote());
			break;
		case 21000:
			if (!player.isBanking() || player.getInterfaceId() != 5292)
				return;
			player.setSwapMode(false);
			player.getPacketSender().sendConfig(304, 0).sendMessage("This feature is coming soon!");
			// player.setSwapMode(!player.swapMode());
			break;
		case 27009:
			MoneyPouch.toBank(player);
			break;
		case 27014:
		case 27015:
		case 27016:
		case 27017:
		case 27018:
		case 27019:
		case 27020:
		case 27021:
		case 27022:
			if (!player.isBanking())
				return;
			if (player.getBankSearchingAttribtues().isSearchingBank())
				BankSearchAttributes.stopSearch(player, true);
			int bankId = id - 27014;
			boolean empty = bankId > 0 ? Bank.isEmpty(player.getBank(bankId)) : false;
			if (!empty || bankId == 0) {
				player.setCurrentBankTab(bankId);
				player.getPacketSender().sendString(5385, "scrollreset");
				player.getPacketSender().sendString(27002, Integer.toString(player.getCurrentBankTab()));
				player.getPacketSender().sendString(27000, "1");
				player.getBank(bankId).open(false);
			} else
				player.getPacketSender().sendMessage("To create a new tab, please drag an item here.");
			break;
		case 22004:
			if (!player.isBanking())
				return;
			if (!player.getBankSearchingAttribtues().isSearchingBank()) {
				player.getBankSearchingAttribtues().setSearchingBank(true);
				player.setInputHandling(new EnterSyntaxToBankSearchFor());
				player.getPacketSender().sendEnterInputPrompt("What would you like to search for?");
			} else {
				BankSearchAttributes.stopSearch(player, true);
			}
			break;

		case -2203:
			player.setInputHandling(new EnterSyntaxToNpcSearchFor());
			player.getPacketSender().sendEnterInputPrompt("Enter the NPC Name you'd like to search for.");
			break;

		case 32602:
		case -11434:
			player.setInputHandling(new PosInput());
			player.getPacketSender().sendEnterInputPrompt("What/Who would you like to search for?");
			break;

		case -15086:
		case -12286:
		case -12046:
		case -3306:
			player.getPacketSender().sendInterfaceRemoval();
			break;

		case 22845:
		case 24115:
		case 24010:
		case 24041:
		case 150:
			player.setAutoRetaliate(!player.isAutoRetaliate());
			break;
		case 29332:
			ClanChat clan = player.getCurrentClanChat();
			if (clan == null) {
				player.getPacketSender().sendMessage("You are not in a clanchat channel.");
				return;
			}
			ClanChatManager.leave(player, false);
			player.setClanChatName(null);
			break;
		case 29329:
			if (player.getInterfaceId() > 0) {
				player.getPacketSender()
						.sendMessage("Please close the interface you have open before opening another one.");
				return;
			}
			player.setInputHandling(new EnterClanChatToJoin());
			player.getPacketSender().sendEnterInputPrompt("Enter the name of the clanchat channel you wish to join:");
			break;
		case 19158:
		case 152:
			if (player.getRunEnergy() <= 1) {
				player.getPacketSender().sendMessage("You do not have enough energy to do this.");
				player.setRunning(false);
			} else
				player.setRunning(!player.isRunning());
			player.getPacketSender().sendRunStatus();
			break;

		case -282:
			DropLog.openRare(player);
			break;

		case 27658:
			player.setExperienceLocked(!player.experienceLocked());
			String type = player.experienceLocked() ? "locked" : "unlocked";
			player.getPacketSender().sendMessage("Your experience is now " + type + ".");
			PlayerPanel.refreshPanel(player);
			break;

		case 27651:
		case 21341:
			if (player.getInterfaceId() == -1) {
				player.getSkillManager().stopSkilling();
				BonusManager.update(player);
				player.getPacketSender().sendInterface(21172);
			} else
				player.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
			break;
		case 27654:
			if (player.getInterfaceId() > 0) {
				player.getPacketSender()
						.sendMessage("Please close the interface you have open before opening another one.");
				return;
			}
			player.getSkillManager().stopSkilling();
			ItemsKeptOnDeath.sendInterface(player);
			break;
		case 2458: // Logout
			if (player.logout(false)) {
				World.getPlayers().remove(player);
			}
			break;
		case 29138:
		case 29038:
		case 29063:
		case 29113:
		case 29163:
		case 29188:
		case 29213:
		case 29238:
		case 30007:
		case 48023:
		case 33033:
		case 30108:
		case 7473:
		case 7562:
		case 7487:
		case 7788:
		case 8481:
		case 7612:
		case 7587:
		case 7662:
		case 7462:
		case 7548:
		case 7687:
		case 7537:
		case 12322:
		case 7637:
		case 12311:
		case -24530:
			CombatSpecial.activate(player);
			break;
		case 1772: // shortbow & longbow
			if (player.getWeapon() == WeaponInterface.SHORTBOW) {
				player.setFightType(FightType.SHORTBOW_ACCURATE);
			} else if (player.getWeapon() == WeaponInterface.LONGBOW) {
				player.setFightType(FightType.LONGBOW_ACCURATE);
			} else if (player.getWeapon() == WeaponInterface.CROSSBOW) {
				player.setFightType(FightType.CROSSBOW_ACCURATE);
			}
			break;
		case 1771:
			if (player.getWeapon() == WeaponInterface.SHORTBOW) {
				player.setFightType(FightType.SHORTBOW_RAPID);
			} else if (player.getWeapon() == WeaponInterface.LONGBOW) {
				player.setFightType(FightType.LONGBOW_RAPID);
			} else if (player.getWeapon() == WeaponInterface.CROSSBOW) {
				player.setFightType(FightType.CROSSBOW_RAPID);
			}
			break;
		case 1770:
			if (player.getWeapon() == WeaponInterface.SHORTBOW) {
				player.setFightType(FightType.SHORTBOW_LONGRANGE);
			} else if (player.getWeapon() == WeaponInterface.LONGBOW) {
				player.setFightType(FightType.LONGBOW_LONGRANGE);
			} else if (player.getWeapon() == WeaponInterface.CROSSBOW) {
				player.setFightType(FightType.CROSSBOW_LONGRANGE);
			}
			break;
		case 2282: // dagger & sword
			if (player.getWeapon() == WeaponInterface.DAGGER) {
				player.setFightType(FightType.DAGGER_STAB);
			} else if (player.getWeapon() == WeaponInterface.SWORD) {
				player.setFightType(FightType.SWORD_STAB);
			}
			break;
		case 2285:
			if (player.getWeapon() == WeaponInterface.DAGGER) {
				player.setFightType(FightType.DAGGER_LUNGE);
			} else if (player.getWeapon() == WeaponInterface.SWORD) {
				player.setFightType(FightType.SWORD_LUNGE);
			}
			break;
		case 2284:
			if (player.getWeapon() == WeaponInterface.DAGGER) {
				player.setFightType(FightType.DAGGER_SLASH);
			} else if (player.getWeapon() == WeaponInterface.SWORD) {
				player.setFightType(FightType.SWORD_SLASH);
			}
			break;
		case 2283:
			if (player.getWeapon() == WeaponInterface.DAGGER) {
				player.setFightType(FightType.DAGGER_BLOCK);
			} else if (player.getWeapon() == WeaponInterface.SWORD) {
				player.setFightType(FightType.SWORD_BLOCK);
			}
			break;
		case 2429: // scimitar & longsword
			if (player.getWeapon() == WeaponInterface.SCIMITAR) {
				player.setFightType(FightType.SCIMITAR_CHOP);
			} else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
				player.setFightType(FightType.LONGSWORD_CHOP);
			}
			break;
		case 2432:
			if (player.getWeapon() == WeaponInterface.SCIMITAR) {
				player.setFightType(FightType.SCIMITAR_SLASH);
			} else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
				player.setFightType(FightType.LONGSWORD_SLASH);
			}
			break;
		case 2431:
			if (player.getWeapon() == WeaponInterface.SCIMITAR) {
				player.setFightType(FightType.SCIMITAR_LUNGE);
			} else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
				player.setFightType(FightType.LONGSWORD_LUNGE);
			}
			break;
		case 2430:
			if (player.getWeapon() == WeaponInterface.SCIMITAR) {
				player.setFightType(FightType.SCIMITAR_BLOCK);
			} else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
				player.setFightType(FightType.LONGSWORD_BLOCK);
			}
			break;
		case 3802: // mace
			player.setFightType(FightType.MACE_POUND);
			break;
		case 3805:
			player.setFightType(FightType.MACE_PUMMEL);
			break;
		case 3804:
			player.setFightType(FightType.MACE_SPIKE);
			break;
		case 3803:
			player.setFightType(FightType.MACE_BLOCK);
			break;
		case 4454: // knife, thrownaxe, dart & javelin
			if (player.getWeapon() == WeaponInterface.KNIFE) {
				player.setFightType(FightType.KNIFE_ACCURATE);
			} else if (player.getWeapon() == WeaponInterface.THROWNAXE) {
				player.setFightType(FightType.THROWNAXE_ACCURATE);
			} else if (player.getWeapon() == WeaponInterface.DART) {
				player.setFightType(FightType.DART_ACCURATE);
			} else if (player.getWeapon() == WeaponInterface.JAVELIN) {
				player.setFightType(FightType.JAVELIN_ACCURATE);
			}
			break;
		case 4453:
			if (player.getWeapon() == WeaponInterface.KNIFE) {
				player.setFightType(FightType.KNIFE_RAPID);
			} else if (player.getWeapon() == WeaponInterface.THROWNAXE) {
				player.setFightType(FightType.THROWNAXE_RAPID);
			} else if (player.getWeapon() == WeaponInterface.DART) {
				player.setFightType(FightType.DART_RAPID);
			} else if (player.getWeapon() == WeaponInterface.JAVELIN) {
				player.setFightType(FightType.JAVELIN_RAPID);
			}
			break;
		case 4452:
			if (player.getWeapon() == WeaponInterface.KNIFE) {
				player.setFightType(FightType.KNIFE_LONGRANGE);
			} else if (player.getWeapon() == WeaponInterface.THROWNAXE) {
				player.setFightType(FightType.THROWNAXE_LONGRANGE);
			} else if (player.getWeapon() == WeaponInterface.DART) {
				player.setFightType(FightType.DART_LONGRANGE);
			} else if (player.getWeapon() == WeaponInterface.JAVELIN) {
				player.setFightType(FightType.JAVELIN_LONGRANGE);
			}
			break;
		case 4685: // spear
			player.setFightType(FightType.SPEAR_LUNGE);
			break;
		case 4688:
			player.setFightType(FightType.SPEAR_SWIPE);
			break;
		case 4687:
			player.setFightType(FightType.SPEAR_POUND);
			break;
		case 4686:
			player.setFightType(FightType.SPEAR_BLOCK);
			break;
		case 4711: // 2h sword
			player.setFightType(FightType.TWOHANDEDSWORD_CHOP);
			break;
		case 4714:
			player.setFightType(FightType.TWOHANDEDSWORD_SLASH);
			break;
		case 4713:
			player.setFightType(FightType.TWOHANDEDSWORD_SMASH);
			break;
		case 4712:
			player.setFightType(FightType.TWOHANDEDSWORD_BLOCK);
			break;
		case 5576: // pickaxe
			player.setFightType(FightType.PICKAXE_SPIKE);
			break;
		case 5579:
			player.setFightType(FightType.PICKAXE_IMPALE);
			break;
		case 5578:
			player.setFightType(FightType.PICKAXE_SMASH);
			break;
		case 5577:
			player.setFightType(FightType.PICKAXE_BLOCK);
			break;
		case 7768: // claws
			player.setFightType(FightType.CLAWS_CHOP);
			break;
		case 7771:
			player.setFightType(FightType.CLAWS_SLASH);
			break;
		case 7770:
			player.setFightType(FightType.CLAWS_LUNGE);
			break;
		case 7769:
			player.setFightType(FightType.CLAWS_BLOCK);
			break;
		case 8466: // halberd
			player.setFightType(FightType.HALBERD_JAB);
			break;
		case 8468:
			player.setFightType(FightType.HALBERD_SWIPE);
			break;
		case 8467:
			player.setFightType(FightType.HALBERD_FEND);
			break;
		case 5862: // unarmed
			player.setFightType(FightType.UNARMED_PUNCH);
			break;
		case 5861:
			player.setFightType(FightType.UNARMED_KICK);
			break;
		case 5860:
			player.setFightType(FightType.UNARMED_BLOCK);
			break;
		case 12298: // whip
			player.setFightType(FightType.WHIP_FLICK);
			break;
		case 12297:
			player.setFightType(FightType.WHIP_LASH);
			break;
		case 12296:
			player.setFightType(FightType.WHIP_DEFLECT);
			break;
		case 336: // staff
			player.setFightType(FightType.STAFF_BASH);
			break;
		case 335:
			player.setFightType(FightType.STAFF_POUND);
			break;
		case 334:
			player.setFightType(FightType.STAFF_FOCUS);
			break;
		case 433: // warhammer
			player.setFightType(FightType.WARHAMMER_POUND);
			break;
		case 432:
			player.setFightType(FightType.WARHAMMER_PUMMEL);
			break;
		case 431:
			player.setFightType(FightType.WARHAMMER_BLOCK);
			break;
		case 782: // scythe
			player.setFightType(FightType.SCYTHE_REAP);
			break;
		case 784:
			player.setFightType(FightType.SCYTHE_CHOP);
			break;
		case 785:
			player.setFightType(FightType.SCYTHE_JAB);
			break;
		case 783:
			player.setFightType(FightType.SCYTHE_BLOCK);
			break;
		case 1704: // battle axe
			player.setFightType(FightType.BATTLEAXE_CHOP);
			break;
		case 1707:
			player.setFightType(FightType.BATTLEAXE_HACK);
			break;
		case 1706:
			player.setFightType(FightType.BATTLEAXE_SMASH);
			break;
		case 1705:
			player.setFightType(FightType.BATTLEAXE_BLOCK);
			break;
		}
	}

	private void teleport(Player player, TeleportData previousTeleport) {
		// TODO Auto-generated method stub

	}

	private boolean checkHandlers(Player player, int id) {
		if (Construction.handleButtonClick(id, player)) {
			return true;
		}

		if (player.getBis().handleButtonClick(id)) {
			return true;
		}

		switch (id) {
		case 2494:
		case 2496:
		case 2497:
		case 2498:
		case 2471:
		case 2472:
		case 2473:
		case 2461:
		case 2462:
		case 2482:
		case 2483:
		case 2484:
		case 2485:
			DialogueOptions.handle(player, id);
			return true;

		case 2495:
			DialogueOptions.handle(player, id);
			break;

		case -8307:
		case -8308:
		case -8309:
		case -8310:
		case -8311:
		case -8312:
			if (DailyRewards.handleRewards(player, id))
				;
			return true;

		}

		if (StartScreen.handleButton(player, id)) {
			return true;
		}

		if (player.getDropSimulator().handleButton(id)) {
			return true;
		}

		if (player.getInstanceInterface().handleClick(id)) {
			return true;
		}

		if (player.isPlayerLocked() && id != 2458 && id != -12780 && id != -12779 && id != -12778 && id != -29767) {
			return true;
		}


		if (StarterTasks.handleButton(player, id)) {
			return true;
		}

		if (NpcTasks.handleButton(player, id)) {
			return true;
		}
		if (Sounds.handleButton(player, id)) {
			return true;
		}
		if (PrayerHandler.isButton(id)) {
			PrayerHandler.togglePrayerWithActionButton(player, id);
			return true;
		}
		if (CurseHandler.isButton(player, id)) {
			return true;
		}

		if (Autocasting.handleAutocast(player, id)) {
			return true;
		}
		if (SmithingData.handleButtons(player, id)) {
			return true;
		}
		if (PouchMaking.pouchInterface(player, id)) {
			return true;
		}
		if (TeleportInterface.handleButton(player, id)) {
			return true;
		}
		if (LoyaltyProgramme.handleButton(player, id)) {
			return true;
		}
		if (Fletching.fletchingButton(player, id)) {
			return true;
		}
		if (LeatherMaking.handleButton(player, id) || Tanning.handleButton(player, id)) {
			return true;
		}
		if (Emotes.doEmote(player, id)) {
			return true;
		}
		if (PestControl.handleInterface(player, id)) {
			return true;
		}
		if (player.getLocation() == Location.DUEL_ARENA && Dueling.handleDuelingButtons(player, id)) {
			return true;
		}
		if (Slayer.handleRewardsInterface(player, id)) {
			return true;
		}

		if (CollectionLog.getInstance().handleButton(player, id)) {
			return true;
		}

		if (ExperienceLamps.handleButton(player, id)) {
			return true;
		}
		if (PlayersOnlineInterface.handleButton(player, id)) {
			return true;
		}
		if (GrandExchange.handleButton(player, id)) {
			return true;
		}
		if (ClanChatManager.handleClanChatSetupButton(player, id)) {
			return true;
		}
		return false;
	}

	private static void displayInstructions(Player player) {
		for (int i = 8145; i <= 8195; i++)
			player.getPacketSender().sendString(i, "");

		player.getPacketSender().sendString(8144, "@dre@Minigame Points Guide");
		player.getPacketSender().sendString(8147, "@blu@Skeletal horror gives");
		player.getPacketSender().sendString(8148, "@blu@Minigame1 points (Access at ::horror)");
		player.getPacketSender().sendString(8150, "@blu@Bravek Slayer master gives a");
		player.getPacketSender().sendString(8152, "@blu@Minigamepoint2 if lucky (10% chance every time)");
		player.getPacketSender().sendString(8154, "@blu@Phoenix gives Minigamepoint3 if lucky (1/1000)");
		player.getPacketSender().sendString(8156, "@blu@but its easy to kill, access at boss teleports");
		player.getPacketSender().sendString(8158, "@blu@Darklord's give Minigamepoint4 if");
		player.getPacketSender().sendString(8160, "@blu@lucky (1/1000) again. hard to kill tho (high hp)");
		player.getPacketSender().sendString(8162, "@blu@Slash bash gives Minigamepoint5 if super lucky (1/10000)");
		player.getPacketSender().sendString(8164, "@blu@However very easy to kill and it also");
		player.getPacketSender().sendString(8166, "@blu@gives MOB Points / custom MOB Points sometimes");
		player.getPacketSender().sendString(8168, "@blu@REWARDS COMING SOON!!!");
		player.getPacketSender().sendString(8170, "@red@You can check all your points with ::checkpoints");
		player.getPacketSender().sendInterface(8134);
	}

	public static final int OPCODE = 185;
}
