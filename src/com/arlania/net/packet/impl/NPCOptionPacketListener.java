package com.arlania.net.packet.impl;

import com.arlania.engine.task.impl.WalkToTask;
import com.arlania.engine.task.impl.WalkToTask.FinalizedMovementTask;
import com.arlania.model.Animation;
import com.arlania.model.Direction;
import com.arlania.model.GameMode;
import com.arlania.model.Graphic;
import com.arlania.model.Item;
import com.arlania.model.PlayerRights;
import com.arlania.model.Position;
import com.arlania.model.Skill;
import com.arlania.model.container.impl.Equipment;
import com.arlania.model.container.impl.Shop.ShopManager;
import com.arlania.model.definitions.NpcDefinition;
import com.arlania.model.input.impl.EnterInstanceName;
import com.arlania.model.input.impl.GambleAmount;
import com.arlania.net.packet.Packet;
import com.arlania.net.packet.PacketListener;
import com.arlania.util.Misc;
import com.arlania.util.RandomUtility;
import com.arlania.world.World;
import com.arlania.world.content.Achievements;
import com.arlania.world.content.EnergyHandler;
import com.arlania.world.content.LoyaltyProgramme;
import com.arlania.world.content.MemberScrolls;
import com.arlania.world.content.NpcTasks;
import com.arlania.world.content.PlayerPanel;
import com.arlania.world.content.SalvageExchange;
import com.arlania.world.content.aoesystem.AoEInstance;
import com.arlania.world.content.battlepass.BattlePass;
import com.arlania.world.content.Achievements.AchievementData;
import com.arlania.world.content.combat.CombatFactory;
import com.arlania.world.content.combat.magic.CombatSpell;
import com.arlania.world.content.combat.magic.CombatSpells;
import com.arlania.world.content.combat.strategy.impl.SuicsBoss;
import com.arlania.world.content.combat.weapon.CombatSpecial;
import com.arlania.world.content.dailytasks.DailyTasks;
import com.arlania.world.content.dialogue.DialogueManager;
import com.arlania.world.content.dialogue.impl.AoEInstanceDialogue;
import com.arlania.world.content.dialogue.impl.Arianwyn;
import com.arlania.world.content.dialogue.impl.GhostGeorgie;
import com.arlania.world.content.dialogue.impl.slayernpc;
import com.arlania.world.content.dropchecker.NPCDropTableChecker;
import com.arlania.world.content.minigames.impl.WarriorsGuild;
import com.arlania.world.content.raids.RaidNpc;
import com.arlania.world.content.raids.RaidParty;
import com.arlania.world.content.skill.impl.crafting.Tanning;
import com.arlania.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.arlania.world.content.skill.impl.fishing.Fishing;
import com.arlania.world.content.skill.impl.hunter.PuroPuro;
import com.arlania.world.content.skill.impl.runecrafting.DesoSpan;
import com.arlania.world.content.skill.impl.slayer.BloodSlayerDialogues;
import com.arlania.world.content.skill.impl.slayer.SlayerDialogues;
import com.arlania.world.content.skill.impl.slayer.SlayerTasks;
import com.arlania.world.content.skill.impl.summoning.BossPets;
import com.arlania.world.content.skill.impl.summoning.Summoning;
import com.arlania.world.content.skill.impl.summoning.SummoningData;
import com.arlania.world.content.slayer.slayerparty;
import com.arlania.world.content.transportation.TeleportHandler;
import com.arlania.world.content.transportation.TeleportType;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;
import com.arlania.world.entity.impl.npc.NPCMovementCoordinator.Coordinator;

public class NPCOptionPacketListener implements PacketListener {

	public static boolean npcConfigEditing = false;
	
	private static void firstClick(Player player, Packet packet) {
		int index = packet.readLEShort();
		if (index < 0 || index > World.getNpcs().capacity())
			return;
		final NPC npc = World.getNpcs().get(index);
		if (npc == null)
			return;
		player.setEntityInteraction(npc);
		if (player.getRights() == PlayerRights.DEVELOPER
			|| player.getRights() == PlayerRights.OWNER
			|| player.getRights() == PlayerRights.CO_OWNER)
			player.getPacketSender().sendMessage("First click npc id: " + npc.getId());

		player.setWalkToTask(new WalkToTask(player, npc.getPosition(), npc.getSize(), new FinalizedMovementTask() {
			@Override
			public void execute() {
				if (SummoningData.beastOfBurden(npc.getId())) {
					Summoning summoning = player.getSummoning();
					if (summoning.getBeastOfBurden() != null && summoning.getFamiliar() != null
							&& summoning.getFamiliar().getSummonNpc() != null
							&& summoning.getFamiliar().getSummonNpc().getIndex() == npc.getIndex()) {
						summoning.store();
						player.getMovementQueue().reset();
					} else {
						player.getPacketSender().sendMessage("That familiar is not yours!");
					}
					return;
				}
				switch (npc.getId()) {
				
				case 232:
					ShopManager.getShops().get(122).open(player);
					break;
					
					case 904:
						ShopManager.getShops().get(132).open(player);
						break;
				case 957:
					ShopManager.getShops().get(118).open(player);
					break;
					
				case 1090:
					ShopManager.getShops().get(124).open(player);
					break;
					
					
				case 30:
					ShopManager.getShops().get(130).open(player);
					break;
					
					
				case 885:
                    if (player.getEquipment().getItems()[Equipment.AMULET_SLOT].getId() == 552) {
					DialogueManager.start(player, Arianwyn.getDialogue(player, 0));
                    }
                    else {
    					DialogueManager.start(player, GhostGeorgie.getDialogue(player, 0));
                    }
                    break;
				
				case 788:
					player.setDialogueActionId(831);
					DialogueManager.start(player, 186);
					break;
					
				case 905:
					player.setDialogueActionId(750);
					player.getPacketSender().sendInterface(53500);
				break;
			
				case 919:
					player.setDialogueActionId(189);
					DialogueManager.start(player, 102);
				break;
				
				case 920:
					player.setDialogueActionId(1999);
					DialogueManager.start(player, 1020);
				break;
				
				case 922:
					player.setDialogueActionId(187);
					DialogueManager.start(player, 1020);
				break;
				
				case 925: //elder boss dialogue
					player.setDialogueActionId(1923);
					DialogueManager.start(player, 1111);
				break;
				
				case 6072: // aoe instance
					if (player.getAoEInstance() != null) {
						player.sendMessage("@red@Please wait for your previous AoE Instance to end.");
						return;
					}
					if (AoEInstance.checkWeapon(player)) {
						player.setInputHandling(new EnterInstanceName());
						player.getPacketSender().sendEnterInputPrompt("Enter a NPC's name:");
					} else {
						DialogueManager.start(player, AoEInstanceDialogue.needAoEWeapon(player));
					}
					break;
				
				case 9285:
		            DailyTasks.INSTANCE.openShopInterface(player);
					break;
					
				case 11381:
					if (player.getNpcKills() < 25000) {
						player.sendMessage("@red@You need 25,000 NPC kill count to particpate in raids.");
						player.sendMessage("@red@You have " + player.getNpcKills() + " NPC kills.");
						return;
					}
			            player.getCustomRaid().open(player);
                   // player.sendMessage("@red@Raids are currently Disabled!");
					break;
				
				case 11306:
					if (player.lastVeigarRaid > System.currentTimeMillis()) {
					player.sendMessage("You can only Purchase 1 oz every 4hours, I need to Re-up Myself!");
					player.sendMessage("You still need to wait another " + player.getTimeRemaining(player.lastVeigarRaid));
					return;
					}
					player.getInventory().delete(10835, 5000);
					player.getInventory().add(9943, 1);
					player.sendMessage("You Purchase an oz of some fire weed from the dealer");
					player.lastVeigarRaid = System.currentTimeMillis() + 14400000;// 8hours
					break;
					
				case 1531:
//
					if (player.bloodFountain()) {
						DialogueManager.start(player, BloodSlayerDialogues.dialogue(player));
					} else {
						player.getPacketSender().sendMessage("@red@You must offer 20k blood bags to the offering table first");
					}
					break;
				
				case 3155:
					TeleportHandler.teleportPlayer(player, new Position(3344, 3409, 0), TeleportType.NORMAL);
					player.getPacketSender()
					.sendMessage("@red@Kill enough Boxs to advance!");
					break;
					
				case 4569:
					TeleportHandler.teleportPlayer(player, new Position(2140, 5537, 3), TeleportType.NORMAL);
					player.getPacketSender()
					.sendMessage("@red@Welcome to the DBZ Zone");
					break;

				case 933://training zone first npc
					 if (player.getInventory().getAmount(3706)  < 25) 
                     	
                     {
					
					player.getPacketSender()
				.sendMessage("@blu@You need at least 25 @red@Starter tokens"
				+ "@red@ to Move on.");
				return;
				}
				TeleportHandler.teleportPlayer(player, new Position(2782, 3173, 0), TeleportType.NORMAL);
				player.getPacketSender()
				.sendMessage("@blu@Welcome to Starter Zone Lvl #2");
				player.getInventory().delete(3706, 25);
				player.getInventory().add(3990, 1);
				break;
				
				
				case 940://training zone second npc
					 if (player.getInventory().getAmount(3706)  < 50) 
                    	
                    {
					
					player.getPacketSender()
				.sendMessage("@blu@You need at least 50 @red@Starter tokens"
				+ "@red@ to Move on.");
				return;
				}
				TeleportHandler.teleportPlayer(player, new Position(2783, 3161, 0), TeleportType.NORMAL);
				player.getPacketSender()
				.sendMessage("@blu@Welcome to Starter Zone Lvl #3");
				player.getInventory().delete(3706, 50);
				player.getInventory().add(5081, 1);
				break;
				
				
				
				
				
				
				
				case 945:
                          if (player.getInventory().getAmount(7775)  < 10) 
                        	
                          {
						
						player.getPacketSender()
					.sendMessage("@blu@You need at least 10 @red@Raid Tokens"
					+ "@red@ to Move on.");
					return;
					}
					TeleportHandler.teleportPlayer(player, new Position(2863, 3331, 4), TeleportType.NORMAL);
					player.getPacketSender()
					.sendMessage("@blu@Welcome to the Raids boss #2");
					player.getInventory().delete(7775, 10);
					break;
				
			case 942:
				  if (player.getInventory().getAmount(7774)  < 50) 
                  	
                  {
				
				player.getPacketSender()
			.sendMessage("@blu@You need at least 50 @red@Raid's Tokens"
			+ "@red@ to Move on.");
			return;
			}
                TeleportHandler.teleportPlayer(player, new Position(2855, 3371, 4), TeleportType.NORMAL);
				player.getPacketSender()
				.sendMessage("@blu@Welcome to the Raids boss #1");
				player.getInventory().delete(7774, 50);
				break;
				
				case 8945:
					player.getBank(player.getCurrentBankTab()).open(false);
					break;

				case 1394:
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
					break;
				case 3217:
					ShopManager.getShops().get(120).open(player);
					break;
					

					

				case 3218:
					player.sendMessage("@red@This shop is being redone therefore it is not currently available");
					// ShopManager.getShops().get(121).open(player);
					break;

				case 436:
					ShopManager.getShops().get(48).open(player);
					player.sendMessage("<img=0>You currently have @red@" + player.getPointsHandler().getDonationPoints()
							+ " Donation Points!");
					break;
				case 388:
					ShopManager.getShops().get(54).open(player);
					player.sendMessage("<img=0>You currently have @red@" + player.getPointsHandler().getDonationPoints()
							+ " Donation Points!");
					break;

				case 367:
					player.sendMessage("@red@Use your item on the NPC to gamble it.");
					player.sendMessage("@red@It then rolls a number 0-100");
					player.sendMessage("@red@If the number is greater than 60 you win");
					player.sendMessage("@red@Otherwise you lose :C");
					player.sendMessage("@blu@If you win u get your 2x your item that u gambled.");
					break;
					
					

				case 1923:
					ShopManager.getShops().get(116).open(player);
					player.sendMessage("<img=0>You should spend the tokens wisely :D");
					break;
					
				case 6603:
					if (player.getRights() == PlayerRights.ULTRA_DONATOR) if (player.getRights() == PlayerRights.SUPER_DONATOR) if (player.getRights() == PlayerRights.PLAYER) if (player.getRights() == PlayerRights.DONATOR){
						player.getPacketSender().sendMessage("This feature is currently only available for deluxe donators.");
						return;
					}
					
				case 4559:
					ShopManager.getShops().get(46).open(player);
					player.getPacketSender().sendMessage("<col=255>You currently have "
							+ player.getPointsHandler().getPrestigePoints() + " Prestige points!");
					break;
				case 3192:
					PlayerPanel.refreshPanel(player);
					player.getPacketSender().sendInterface(3200);
					break;
				case 9641:
					int chance = RandomUtility.exclusiveRandom(100);
					int rankChance = RandomUtility.exclusiveRandom(100);
					int[] noobRewards = { 13271, 13272, 13273, 13274, 13275,13594,13595,13596,13597,13664,5127,5128,4670, 4671,
							4672,4673 };
					int[] betterRewards = { 9484, 9485, 9486, 9487, 9488, 9489, 9490, 9491  };
					int[] bestRewards = { 20582, 20590, 9481, 9482, 9483, 10168, 10205 };
					if (!player.getInventory().contains(19100, 1)) {
						player.sendMessage("@red@You don't have the Final room key.");
						return;
					}
					player.getAchievementTracker().progress(com.arlania.world.content.achievements.AchievementData.COMPLETE_DBZ_MINIGAME_50_TIMES, 1);
					player.getAchievementTracker().progress(com.arlania.world.content.achievements.AchievementData.COMPLETE_THE_DBZ_MINIGAME, 1);
					player.getInventory().delete(19100, 1);
					if (chance >= 0 && chance < 60) {
						if (player.getRights() == PlayerRights.OBSIDIAN_DONATOR && rankChance >= 95
								|| player.getRights() == PlayerRights.LEGENDARY_DONATOR && rankChance >= 90
										|| player.getRights() == PlayerRights.SUPREME_DONATOR && rankChance >= 70
							|| player.getRights() == PlayerRights.EXECUTIVE_DONATOR && rankChance >= 75
							|| player.getRights() == PlayerRights.CELESTIAL_DONATOR && rankChance >= 80
							|| player.getRights() == PlayerRights.DIVINE_DONATOR && rankChance >= 65) {
							player.sendMessage(
									"@red@Because you were a Uber+ donator, you didn't get teled away.");
							player.getInventory().add(noobRewards[Misc.getRandom(noobRewards.length - 1)], 1);
							return;
						} else {
							TeleportHandler.teleportPlayer(player, new Position(2582, 4609, 0),
									player.getSpellbook().getTeleportType());
							player.getInventory().add(noobRewards[Misc.getRandom(noobRewards.length - 1)], 1);
						}
					} else if (chance >= 60 && chance <= 85) {
						if (player.getRights() == PlayerRights.OBSIDIAN_DONATOR && rankChance >= 90
								|| player.getRights() == PlayerRights.LEGENDARY_DONATOR && rankChance >= 75
							|| player.getRights() == PlayerRights.EXECUTIVE_DONATOR && rankChance >= 65
									|| player.getRights() == PlayerRights.SUPREME_DONATOR && rankChance >= 60
											|| player.getRights() == PlayerRights.DIVINE_DONATOR && rankChance >= 60
							|| player.getRights() == PlayerRights.CELESTIAL_DONATOR && rankChance >= 70){
							player.sendMessage(
									"@red@Because you were a Uber+ donator, you didn't get teled away.");
							player.getInventory().add(betterRewards[Misc.getRandom(betterRewards.length - 1)], 1);
							return;
						} else {
							TeleportHandler.teleportPlayer(player, new Position(2582, 4609, 0),
									player.getSpellbook().getTeleportType());
							player.getInventory().add(betterRewards[Misc.getRandom(betterRewards.length - 1)], 1);
						}
					} else {
						if (player.getRights() == PlayerRights.OBSIDIAN_DONATOR && rankChance >= 90
								|| player.getRights() == PlayerRights.LEGENDARY_DONATOR && rankChance >= 75
								
								|| player.getRights() == PlayerRights.EXECUTIVE_DONATOR && rankChance >= 70
								|| player.getRights() == PlayerRights.CELESTIAL_DONATOR && rankChance >= 65) {
							player.sendMessage(
									"@red@Because you were a Uber+ donator, you didn't get teled away.");
							player.getInventory().add(bestRewards[Misc.getRandom(bestRewards.length - 1)], 1);
							return;
						} else {
							TeleportHandler.teleportPlayer(player, new Position(2582, 4609, 0),
									player.getSpellbook().getTeleportType());
							player.getInventory().add(bestRewards[Misc.getRandom(bestRewards.length - 1)], 1);
						}
					}
					break;
				case 1552:
					try {
						ShopManager.getShops().get(57).open(player);
						player.getPacketSender().sendMessage("To get PVM Tickets keep killing NPCS");
						player.sendMessage("If you have any suggestions on what should be added");
						player.sendMessage("Make sure to make a suggestion and i will consider it.");
					} catch (Exception i) {
						player.sendMessage("Failed to open store.. shop doesn't exist in the list!");
					}
					break;
				case 712:
					try {
						ShopManager.getShops().get(59).open(player);
						player.sendMessage("<img=0>You currently have @red@"
								+ player.getPointsHandler().getSkillPoints() + " Skilling Points!");
						player.getPacketSender().sendMessage("Skilling points are obtained by leveling up");
						player.sendMessage("After u reach 99 in a skill, you can either prestige");
						player.sendMessage("Or keep getting xp, per every 15m xp you get 150 skilling points.");
					} catch (Exception i) {
						player.sendMessage("Failed to open store.. shop doesn't exist in the list!");
					}
					break;
				case 965:
					try {
						ShopManager.getShops().get(58).open(player);
						player.getPacketSender().sendMessage("This store is pretty cool");
						player.sendMessage("If you have any suggestions on what should be added");
						player.sendMessage("Make sure to make a suggestion and i will consider it.");
					} catch (Exception i) {
						player.sendMessage("Failed to open store.. shop doesn't exist in the list!");
					}
					break;
				case 560:
					ShopManager.getShops().get(205).open(player);
					break;
				case 541:
					ShopManager.getShops().get(5).open(player);
					break;
				
					
				case 2633:
					try {
						ShopManager.getShops().get(50).open(player);
						player.sendMessage("You currently have @red@" + player.getPointsHandler().getTriviaPoints()
								+ " Trivia Points!");
					} catch (Exception i) {
						player.sendMessage("Error loading shop.. invalid shop!");
					}
					break;
				case 741:
					ShopManager.getShops().get(49).open(player);
					player.sendMessage("<img=0>You currently have @red@" + player.getPointsHandler().getDonationPoints()
							+ " Donation Points!");
					break;
				case 2998:
					ShopManager.getShops().get(41).open(player);
					break;
				case 263:
					ShopManager.getShops().get(51).open(player);
					break;
				case 461:
					ShopManager.getShops().get(1).open(player);
					break;

				case 278:
					ShopManager.getShops().get(6).open(player);
					break;
				case 4902:
					ShopManager.getShops().get(55).open(player);
					break;

				case 543:
					DialogueManager.start(player, 159);
					player.setDialogueActionId(709);
					break;
				
				
				case 884:
					DialogueManager.start(player, 179);
					player.setDialogueActionId(743);
					break;
				
				
				case 4247:
					ShopManager.getShops().get(56).open(player);

					break;
				case 457:
					DialogueManager.start(player, 117);
					player.setDialogueActionId(74);
					break;

				case 8710:
				case 8707:
				case 8706:
				case 8705:
					EnergyHandler.rest(player);
					break;

				case 947:
					if (player.isIronMan()) {
						player.getPacketSender()
								.sendMessage("Ironman-players are not allowed to buy items from the general-store.");
						return;
					}
					player.getPlayerOwnedShopManager().options();
					break;

				case 11226:
					if (Dungeoneering.doingDungeoneering(player)) {
						ShopManager.getShops().get(45).open(player);
					}
					break;
				
				case 273: // MOB Point shop npc id can be anything as long as it opens shop 92
					ShopManager.getShops().get(92).open(player);
					player.sendMessage("<img=0>You currently have @red@" + player.getBossPoints() + " MOB Points!");
					break;
				
				
			/*	case 904://portal chambers
					 if (player.getInventory().getAmount(6640)  < 25) 
                     	
                     {	
						player.getPacketSender()
						.sendMessage("@blu@You need at least 25 @red@Cash Crystals"
						+ "@red@ to Enter");
						return;
						}
					   TeleportHandler.teleportPlayer(player, new Position(3103, 9756, 0), TeleportType.NORMAL);
						player.getPacketSender()
						.sendMessage("@blu@Welcome to Portal Chamber");
						player.getInventory().delete(6640, 25);
						break;
				*/
				case 903://portal chambers //IRONMAN CHAMBERS
					 if (player.getInventory().getAmount(6640)  < 15) 
                    	
                    {	
						player.getPacketSender()
						.sendMessage("@blu@You need at least 15 @red@Cash Crystals"
						+ "@red@ to Enter");
						return;
						}
					   TeleportHandler.teleportPlayer(player, new Position(2592, 3087, 0), TeleportType.NORMAL);
						player.getPacketSender()
						.sendMessage("@blu@Welcome to Portal Chamber");
						player.getInventory().delete(6640, 15);
						break;
				
				case 1532:
					ShopManager.getShops().get(122).open(player);
					player.sendMessage("<img=0>You currently have @red@" + player.getPointsHandler().getBloodSlayerPoints()
							+ " Blood Slayer points");
					break;
				case 241:
					ShopManager.getShops().get(77).open(player);
					break;
			/*	case 9713:
					DialogueManager.start(player, 107);
					player.setDialogueActionId(69);
					break;*/
				case 683:
					ShopManager.getShops().get(3).open(player);
					break;
				case 2622:
					ShopManager.getShops().get(43).open(player);
					break;
				case 3101:
					DialogueManager.start(player, 90);
					player.setDialogueActionId(57);
					break;
				case 7969:
					ShopManager.getShops().get(28).open(player);
					// DialogueManager.start(player, ExplorerJack.getDialogue(player));
					break;
				case 1597:
				case 8275:
				case 9085:
				case 7780:
					if (npc.getId() != player.getSlayer().getSlayerMaster().getNpcId()) {
						player.getPacketSender().sendMessage("This is not your current Slayer master.");
						return;
					}
					DialogueManager.start(player, SlayerDialogues.dialogue(player));
					break;
					

				case 3212:
					//if (npc.getId() != player.getSlayer().getSlayerMaster().getNpcId()) {
						//player.getPacketSender().sendMessage("@red@This is not your current Slayer master.");
						player.getPacketSender()
								.sendMessage("@red@Braveks tasks are currently disabled.");
						//return;
					//}
				//	DialogueManager.start(player, SlayerDialogues.dialogue(player));
					break;
				case 437:
					DialogueManager.start(player, 99);
					player.setDialogueActionId(58);
					break;
				case 5112:
					ShopManager.getShops().get(38).open(player);
					break;
				case 8591:
					// player.nomadQuest[0] = player.nomadQuest[1] = player.nomadQuest[2] = false;
					if (!player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(0)) {
						DialogueManager.start(player, 48);
						player.setDialogueActionId(23);
					} else if (player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(0)
							&& !player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(1)) {
						DialogueManager.start(player, 50);
						player.setDialogueActionId(24);
					} else if (player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(1))
						DialogueManager.start(player, 53);
					break;
				case 3385:
					if (player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0) && player
							.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() < 6) {
						DialogueManager.start(player, 39);
						return;
					}
					if (player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6) {
						DialogueManager.start(player, 46);
						return;
					}
					DialogueManager.start(player, 38);
					player.setDialogueActionId(20);
					break;
				case 6139:
					DialogueManager.start(player, 29);
					player.setDialogueActionId(17);
					break;
				case 3789:
					ShopManager.getShops().get(115).open(player);
					player.sendMessage("<img=0>You currently have @red@"
							+ player.getPointsHandler().getPestcontrolpoints() + " Pest Control Points!");
					break;
				case 2948:
					DialogueManager.start(player, WarriorsGuild.warriorsGuildDialogue(player));
					break;
				case 650:
					ShopManager.getShops().get(35).open(player);
					break;
				case 6055:
				case 6056:
				case 6057:
				case 6058:
				case 6059:
				case 6060:
				case 6061:
				case 6062:
				case 6063:
				case 6064:
				case 7903:
				case 7846:

					PuroPuro.catchImpling(player, npc);
					break;
				case 8022:
				case 8028:
					DesoSpan.siphon(player, npc);
					break;
				case 2579:
					player.setDialogueActionId(13);
					DialogueManager.start(player, 24);
					break;
				case 8948:
					player.setDialogueActionId(633);
					DialogueManager.start(player, 183);
					break;
				case 8725:
					player.setDialogueActionId(10);
					DialogueManager.start(player, 19);
					break;
				case 3213:
					player.setDialogueActionId(563);
					DialogueManager.start(player, 180);
					break;
				case 4475:
					int[] itemList = {989,15373,3928,15418,2572,18392,9006,6194,6195,6196,3973,3908,3910,3909,19886,5130,19468,18865,5134,3951,18957,5131,3276,15375,1959,6507,19727,19728,19729,19730,19731,19732,6485 };
					player.getPacketSender().sendInterface(62200);
					for (int i = 0; i < itemList.length; i++)
						player.getPacketSender().sendItemOnInterface(62209, itemList[i], i, 1);
					break;
				case 257:
					int[] itemList1 = {3928,5130,19468,18865,5134,3951,18957,5131,3276,19727,3666,5195,5129,-1,-1,-1,-1,-1,-1,-1  };
					player.getPacketSender().sendInterface(62200);
					for (int i = 0; i < itemList1.length; i++)
						player.getPacketSender().sendItemOnInterface(62209, itemList1[i], i, 1);
					break;
				case 382:
						NpcTasks.updateInterface(player);
						player.getPacketSender().sendInterfaceReset();
						player.getPacketSender().sendInterface(65400);
							break;
				case 435:
		            SalvageExchange.open(player);
		            break;
				case 380:
					int[] itemList3 = {989,15373,6199,3988,15374,13997,18392,2572,15375,6509,6510,6505,6506,14546,14547,5185,-1,-1,-1-1,-1,-1,-1,-1,-1,-1,-1-1,-1,-1,-1  };
					player.getPacketSender().sendInterface(62200);
					for (int i = 0; i < itemList3.length; i++)
						player.getPacketSender().sendItemOnInterface(62209, itemList3[i], i, 1);
					break;
				case 4249:
					player.setDialogueActionId(9);
					DialogueManager.start(player, 64);
					break;
				case 6807:
				case 6994:
				case 6995:
				case 6867:
				case 6868:
				case 6794:
				case 6795:
				case 6815:
				case 6816:
				case 6874:
				case 6873:
				case 3594:
				case 3590:
				case 3596:
					if (player.getSummoning().getFamiliar() == null
							|| player.getSummoning().getFamiliar().getSummonNpc() == null
							|| player.getSummoning().getFamiliar().getSummonNpc().getIndex() != npc.getIndex()) {
						player.getPacketSender().sendMessage("That is not your familiar.");
						return;
					}
					player.getSummoning().store();
					break;
					
				case 364:
					player.setDialogueActionId(8);
					DialogueManager.start(player, 13);
					break;
				case 6970:
					player.setDialogueActionId(3);
					DialogueManager.start(player, 3);
					break;
				case 4657:
					player.setDialogueActionId(5);
					DialogueManager.start(player, 5);
					break;
				case 318:
				case 316:
				case 313:
				case 312:
				case 310:
					player.setEntityInteraction(npc);
					Fishing.setupFishing(player, Fishing.forSpot(npc.getId(), false));
					break;
				case 3294:
					ShopManager.getShops().get(118).open(player);
					break;
				case 1164:
					ShopManager.getShops().get(137).open(player);
					break;
				case 805:
					ShopManager.getShops().get(34).open(player);
					break;
				case 462:
					ShopManager.getShops().get(33).open(player);
					break;
				case 1263:
					ShopManager.getShops().get(32).open(player);
					break;
				case 8444:

					ShopManager.getShops().get(31).open(player);
					break;
				case 8459:
					ShopManager.getShops().get(30).open(player);
					break;
				case 3299:
					ShopManager.getShops().get(21).open(player);
					break;
				case 548:
					ShopManager.getShops().get(20).open(player);
					break;
				case 1685:
					ShopManager.getShops().get(19).open(player);
					break;
				case 308:
					ShopManager.getShops().get(18).open(player);
					break;
		/*		case 3208:
					ShopManager.getShops().get(117).open(player);
					player.sendMessage("@blu@You currently have@red@ " + player.getPointsHandler().getSlayerPoints()
							+ "@blu@ Slayer Points");
					break;*/
				case 802:
					ShopManager.getShops().get(17).open(player);
					break;
				case 794:
					ShopManager.getShops().get(16).open(player);
					break;
				case 4946:
					ShopManager.getShops().get(15).open(player);
					break;
				case 948:
					ShopManager.getShops().get(13).open(player);
					break;
				case 4906:
					ShopManager.getShops().get(14).open(player);
					break;
				case 520:
				case 521:
					ShopManager.getShops().get(12).open(player);
					break;
				case 2292:
					ShopManager.getShops().get(11).open(player);
					break;
				case 2676:
					player.getPacketSender().sendInterface(3559);
					player.getAppearance().setCanChangeAppearance(true);
					break;
				case 494:
				case 1360:
					player.getBank(player.getCurrentBankTab()).open(false);
					break;
				}
				if (!(npc.getId() >= 8705 && npc.getId() <= 8710)) {
					npc.setPositionToFace(player.getPosition());
				}
				player.setPositionToFace(npc.getPosition());
			}
		}));

	}

	private static void attackNPC(Player player, Packet packet) {
		int index = packet.readShortA();
		if (index < 0 || index > World.getNpcs().capacity())
			return;
		final NPC npc = World.getNpcs().get(index);
		if (npc == null)
			return;

		if (!NpcDefinition.getDefinitions()[npc.getId()].isAttackable()) {
			return;
		}

		if (npc.getConstitution() <= 0) {
			//System.out.println("npc health is 0");
			player.getMovementQueue().reset();
			return;
		}
		
		player.getDpsOverlay().resetTimer();
		
		if (player.getCombatBuilder().getStrategy() == null) {
			//System.out.println("player strategy is null");
			player.getCombatBuilder().determineStrategy();
		}
		if (CombatFactory.checkAttackDistance(player, npc)) {
			//System.out.println("check attack distance failed");
			player.getMovementQueue().reset();
		}

		if (npc instanceof RaidNpc) {
			RaidNpc raidNpc = (RaidNpc) npc;
			RaidParty party = player.getRaidParty();
			if (party == null) {
				player.sendMessage("You cannot attack this.");
				return;
			}
			
			if (player.getCombatBuilder().getStrategy() == null) {
				player.getCombatBuilder().determineStrategy();
			}

			player.getCombatBuilder().attack(npc);
			return;
		}
		
		player.getCombatBuilder().attack(npc);
	}

	
    
    
	public void handleSecondClick(Player player, Packet packet) {
		int index = packet.readLEShortA();
		if (index < 0 || index > World.getNpcs().capacity())
			return;
		final NPC npc = World.getNpcs().get(index);
		if (npc == null)
			return;
		player.setEntityInteraction(npc);
		final int npcId = npc.getId();
		NpcDefinition defs = NpcDefinition.forId(npcId);
		if (player.getRights() == PlayerRights.DEVELOPER
				|| player.getRights() == PlayerRights.OWNER
				|| player.getRights() == PlayerRights.CO_OWNER)
			player.getPacketSender().sendMessage("Second click npc id: " + npcId);
		if (defs != null && defs.getCombatLevel() > 1 && !npcConfigEditing) {
			if (defs != null) {
//			player.getPacketSender().sendInterface(37600);
//			NPCDropTableChecker.getSingleton().getActionIdForName(player, npcId);
			player.getDropTableManager().showDropTable(npcId, true);
			player.getMovementQueue().reset();
			return;
			}}

		if (npcConfigEditing) {
			NPC.writeNpcData(npcId, npc.getDirection(), npc.getDefaultPosition(),
					npc.getMovementCoordinator().getCoordinator(), false);
			System.out.println("Added npc: " + npc.hashCode());
			World.deregister(npc);
			player.getMovementQueue().reset();
			return;
		}

		player.setWalkToTask(new WalkToTask(player, npc.getPosition(), npc.getSize(), new FinalizedMovementTask() {
			@Override
			public void execute() {

				switch (npc.getId()) {
				case 2998:
					// DialogueManager.start(player, 155);
					player.getPacketSender().sendEnterAmountPrompt("How many coins would you like to gamble?");
					player.setInputHandling(new GambleAmount());
					break;
				case 8948:
					player.getBank(player.getCurrentBankTab()).open(false);
					break;
				case 1164:
					ShopManager.getShops().get(137).open(player);
					break;
				case 885:
                    if (player.getEquipment().getItems()[Equipment.AMULET_SLOT].getId() == 552) {
    					ShopManager.getShops().get(133).open(player);
                    }
                    else {
    					DialogueManager.start(player, GhostGeorgie.getDialogue(player, 0));
                    }
					break;
					
					
				case 1394:
					int[] items = {2577,989,896,20016 ,20017,20018,10720,14006,20021,20022,18910,17911,19892,
							3956,3928,17908,17909,3932,4775,11732,5079,18933,4799,4800,4801,3973,15012,1499,3960,
							3958,3959,5187,5186,3316,3931,14559,6583,5131,4772,4771,4770,12708,13235,13239,18347,15020,15018,15019,15220,923,12605,3908,3909,3910,3907,19720,15649,15650,15651,15654,15655,5167,15652,4761,4762,4763,4764,4765,3905,5089,18894,15045,930,926,920,931,5211
							};
					player.getPacketSender().sendInterface(52300);
					for (int i = 0; i < items.length; i++)
						player.getPacketSender().sendItemOnInterface(52302, items[i], i, 1);
					break;

				case 1699:
					ShopManager.getShops().get(450).open(player);
					player.getPacketSender().sendMessage("<col=255>You currently have "
							+ Misc.format(player.getInventory().getAmount(10835)) + " Coins to spend!");
					break;

				case 3046:// deluxe zone banker
					player.getBank(player.getCurrentBankTab()).open(false);
					break;

				case 3192:
					PlayerPanel.refreshPanel(player);
					player.getPacketSender().sendInterface(3200);
					break;

				case 4559:
					ShopManager.getShops().get(46).open(player);
					player.getPacketSender().sendMessage("<col=255>You currently have "
							+ player.getPointsHandler().getPrestigePoints() + " Prestige points!");
					break;

				case 961:
					ShopManager.getShops().get(6).open(player);
					break;
					

				case 436:
					ShopManager.getShops().get(48).open(player);
					player.sendMessage("<img=0>You currently have @red@" + player.getPointsHandler().getDonationPoints()
							+ " Donation Points!");
					break;

				case 457:
					player.getPacketSender().sendMessage("The ghost teleports you away.");
					player.getPacketSender().sendInterfaceRemoval();
					player.moveTo(new Position(3651, 3486));
					break;

				case 947:
					if (player.getGameMode() == GameMode.IRONMAN) {
						player.getPacketSender()
								.sendMessage("Ironman-players are not allowed to buy items from the general-store.");
						return;
					}
					if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
						player.getPacketSender()
								.sendMessage("Hardcore-ironman-players are not allowed to buy items from the pos.");
						return;
					}
					player.getPlayerOwnedShopManager().open();
					break;

				case 2622:
					ShopManager.getShops().get(43).open(player);
					break;

				case 4902:
					ShopManager.getShops().get(55).open(player);
					break;

				case 462:
					npc.performAnimation(CombatSpells.CONFUSE.getSpell().castAnimation().get());
					npc.forceChat("Off you go!");
					TeleportHandler.teleportPlayer(player, new Position(2911, 4832),
							player.getSpellbook().getTeleportType());
					break;

				case 3101:
					DialogueManager.start(player, 95);
					player.setDialogueActionId(57);
					break;

				case 7969:
					ShopManager.getShops().get(28).open(player);
					break;

				case 525:
					ShopManager.getShops().get(75).open(player);
					player.getPacketSender()
							.sendMessage("If you have any suggestions what more cosmetics should be added");
					player.getPacketSender().sendMessage("Make sure to make a suggestion and i will consider it.");
					break;
				// case 1552:
				// ShopManager.getShops().get(57).open(player);
				// player.getPacketSender()
				// .sendMessage("If you have any suggestions on what should be added");
				// player.getPacketSender()
				// .sendMessage("Make sure to make a suggestion and i will consider it.");
				// break;
				case 364:
					ShopManager.getShops().get(27).open(player);
					player.sendMessage("<img=0>You currently have @red@" + player.getPointsHandler().getVotingPoints()
							+ " Voting Points!");
					break;

				case 4657:
					DialogueManager.start(player, MemberScrolls.getTotalFunds(player));
					break;

				case 1597:
				case 9085:
				case 7780:
					if (npc.getId() != player.getSlayer().getSlayerMaster().getNpcId()) {
						player.getPacketSender().sendMessage("This is not your current Slayer master.");
						return;
					}
					if (player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK)
						player.getSlayer().assignTask();
					else
						DialogueManager.start(player, SlayerDialogues.findAssignment(player));
					break;

				case 3212:
					if (npc.getId() != player.getSlayer().getSlayerMaster().getNpcId()) {
						player.getPacketSender().sendMessage("@red@This is not your current Slayer master.");
						player.getPacketSender()
								.sendMessage("@red@To change your slayer master to bravek type @blu@ ::changebravek");
						return;
					}
					if (player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK)
						player.getSlayer().assignTask();
					else
						DialogueManager.start(player, SlayerDialogues.findAssignment(player));
					break;

				case 8591:
					if (!player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(1)) {
						player.getPacketSender()
								.sendMessage("You must complete Nomad's quest before being able to use this shop.");
						return;
					}
					ShopManager.getShops().get(37).open(player);
					break;

				case 805:
					Tanning.selectionInterface(player);
					break;

				case 318:
				case 316:
				case 313:
				case 312:
					player.setEntityInteraction(npc);
					Fishing.setupFishing(player, Fishing.forSpot(npc.getId(), true));
					break;

				case 4946:
					ShopManager.getShops().get(15).open(player);
					break;

				case 683:
					ShopManager.getShops().get(3).open(player);
					break;

				case 705:
					ShopManager.getShops().get(4).open(player);
					break;

				case 2253:
					ShopManager.getShops().get(9).open(player);
					break;

				case 6970:
					player.setDialogueActionId(35);
					DialogueManager.start(player, 63);
					break;
				}
				npc.setPositionToFace(player.getPosition());
				player.setPositionToFace(npc.getPosition());
			}
		}));
	}

	public void handleThirdClick(Player player, Packet packet) {
		int index = packet.readShort();
		if (index < 0 || index > World.getNpcs().capacity())
			return;
		final NPC npc = World.getNpcs().get(index);
		if (npc == null)
			return;
		player.setEntityInteraction(npc).setPositionToFace(npc.getPosition().copy());
		npc.setPositionToFace(player.getPosition());
		if (player.getRights() == PlayerRights.DEVELOPER
				|| player.getRights() == PlayerRights.OWNER
				|| player.getRights() == PlayerRights.CO_OWNER)
			player.getPacketSender().sendMessage("Third click npc id: " + npc.getId());
		if (BossPets.pickup(player, npc)) {
			player.getMovementQueue().reset();
			return;
		}

		player.setWalkToTask(new WalkToTask(player, npc.getPosition(), npc.getSize(), new FinalizedMovementTask() {
			@Override
			public void execute() {
				switch (npc.getId()) {
				case 3101:
					ShopManager.getShops().get(42).open(player);
					break;
				case 9641:
					int[] itemList = { 13271, 13272, 13273, 13274, 13275,13594,13595,13596,13597,13664,5127,5128, 9484, 9485, 9486, 9487, 9488, 9489, 9490, 9491, 4670, 4671,
							4672,4673,20582, 20590, 9481, 9482, 9483, 10168, 10205 };
					player.getPacketSender().sendInterface(65000);
					for (int i = 0; i < itemList.length; i++)
						player.getPacketSender().sendItemOnInterface(65002, itemList[i], i, 1);
					break;
				case 1597:
				case 8275:
				case 9085:
				case 7780:
				case 3212:
					ShopManager.getShops().get(40).open(player);
					break;
				case 364:
					LoyaltyProgramme.open(player);
					break;
				case 4657:
					if (player.getRights() == PlayerRights.PLAYER) {
						player.getPacketSender().sendMessage("You need to be a member to teleport to this zone.")
								.sendMessage(
										"To become a member, visit http://Pwnlite317.com/ and purchase a scroll.");
						return;
					}
					TeleportHandler.teleportPlayer(player, new Position(3363, 9638),
							player.getSpellbook().getTeleportType());
					break;
				case 947:
					if (player.getGameMode() == GameMode.IRONMAN) {
						player.getPacketSender()
								.sendMessage("Ironman-players are not allowed to buy items from the general-store.");
						return;
					}
					if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
						player.getPacketSender()
								.sendMessage("Hardcore-ironman-players are not allowed to buy items from the pos.");
						return;
					}
					player.getPlayerOwnedShopManager().openEditor();
					break;
				case 946:
					ShopManager.getShops().get(0).open(player);
					break;
					
				case 1861:
					ShopManager.getShops().get(2).open(player);
					break;
				case 961:
					if (player.getRights() == PlayerRights.PLAYER) {
						player.getPacketSender().sendMessage("This feature is currently only available for members.");
						return;
					}

					boolean restore = player.getSpecialPercentage() < 100;
					if (restore) {
						player.setSpecialPercentage(100);
						CombatSpecial.updateBar(player);
						player.getPacketSender().sendMessage("Your special attack energy has been restored.");
					}
					for (Skill skill : Skill.values()) {
						if (player.getSkillManager().getCurrentLevel(skill) < player.getSkillManager()
								.getMaxLevel(skill)) {
							player.getSkillManager().setCurrentLevel(skill,
									player.getSkillManager().getMaxLevel(skill));
							restore = true;
						}
					}
					if (restore) {
						player.performGraphic(new Graphic(1302));
						player.getPacketSender().sendMessage("Your stats have been restored.");
					} else
						player.getPacketSender().sendMessage("Your stats do not need to be restored at the moment.");
					break;
				case 705:
					ShopManager.getShops().get(5).open(player);
					break;
				case 2253:
					ShopManager.getShops().get(10).open(player);
					break;
				}
				npc.setPositionToFace(player.getPosition());
				player.setPositionToFace(npc.getPosition());
			}
		}));
	}

	public void handleFourthClick(Player player, Packet packet) {
		int index = packet.readLEShort();
		if (index < 0 || index > World.getNpcs().capacity())
			return;
		final NPC npc = World.getNpcs().get(index);
		if (npc == null)
			return;
		player.setEntityInteraction(npc);
		if (player.getRights() == PlayerRights.DEVELOPER
				|| player.getRights() == PlayerRights.OWNER
				|| player.getRights() == PlayerRights.CO_OWNER)
			player.getPacketSender().sendMessage("Fourth click npc id: " + npc.getId());
		player.setWalkToTask(new WalkToTask(player, npc.getPosition(), npc.getSize(), new FinalizedMovementTask() {
			@Override
			public void execute() {
				switch (npc.getId()) {
				case 4657:
					if (player.getRights() == PlayerRights.PLAYER) {
						player.getPacketSender().sendMessage("You need to be a member to teleport to this zone.")
								.sendMessage(
										"To become a member, visit https://Pwnlite317.com and purchase a scroll.");
						return;
					}
					TeleportHandler.teleportPlayer(player, new Position(3363, 9638),
							player.getSpellbook().getTeleportType());
					break;
					

				case 947:
					if (player.getGameMode() == GameMode.IRONMAN) {
						player.getPacketSender()
								.sendMessage("Ironman-players are not allowed to buy items from the general-store.");
						return;
					}
					if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
						player.getPacketSender()
								.sendMessage("Hardcore-ironman-players are not allowed to buy items from the pos.");
						return;
					}
					player.getPlayerOwnedShopManager().claimEarnings();
					break;
				case 705:
					ShopManager.getShops().get(7).open(player);
					break;
				case 2253:
					ShopManager.getShops().get(8).open(player);
					break;
				case 1597:
				case 9085:
				case 8275:
				case 7780:
				case 3212:
					player.getPacketSender().sendString(36030,
							"Current Points:   " + player.getPointsHandler().getSlayerPoints());
					player.getPacketSender().sendInterface(36000);
					break;
				}
				npc.setPositionToFace(player.getPosition());
				player.setPositionToFace(npc.getPosition());
			}
		}));
	}

	@Override
	public void handleMessage(Player player, Packet packet) {

		if (player.isTeleporting() || player.isPlayerLocked() || player.getMovementQueue().isLockMovement())
			return;
		switch (packet.getOpcode()) {
		case ATTACK_NPC:
			attackNPC(player, packet);
			break;
		case FIRST_CLICK_OPCODE:
			firstClick(player, packet);
			break;
		case SECOND_CLICK_OPCODE:
			handleSecondClick(player, packet);
			break;
		case THIRD_CLICK_OPCODE:
			handleThirdClick(player, packet);
			break;
		case FOURTH_CLICK_OPCODE:
			handleFourthClick(player, packet);
			break;
		case MAGE_NPC:
			int npcIndex = packet.readLEShortA();
			int spellId = packet.readShortA();

			if (npcIndex < 0 || spellId < 0 || npcIndex > World.getNpcs().capacity()) {
				return;
			}

			NPC n = World.getNpcs().get(npcIndex);
			player.setEntityInteraction(n);

			CombatSpell spell = CombatSpells.getSpell(spellId);

			if (n == null || spell == null) {
				player.getMovementQueue().reset();
				return;
			}

			if (!NpcDefinition.getDefinitions()[n.getId()].isAttackable()) {
				player.getMovementQueue().reset();
				return;
			}

			if (n.getConstitution() <= 0) {
				player.getMovementQueue().reset();
				return;
			}

			player.setPositionToFace(n.getPosition());
			player.setCastSpell(spell);
			if (player.getCombatBuilder().getStrategy() == null) {
				player.getCombatBuilder().determineStrategy();
			}
			if (CombatFactory.checkAttackDistance(player, n)) {
				player.getMovementQueue().reset();
			}
			player.getCombatBuilder().resetCooldown();
			player.getCombatBuilder().attack(n);
			break;
		}
	}

	public static final int ATTACK_NPC = 72, FIRST_CLICK_OPCODE = 155, MAGE_NPC = 131, SECOND_CLICK_OPCODE = 17,
			THIRD_CLICK_OPCODE = 21, FOURTH_CLICK_OPCODE = 18;
}
