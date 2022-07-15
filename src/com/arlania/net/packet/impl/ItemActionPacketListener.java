
package com.arlania.net.packet.impl;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.engine.task.impl.CleansingTask;
import com.arlania.engine.task.impl.HPRegainPotionTask;
import com.arlania.engine.task.impl.IceCreamTask;
import com.arlania.engine.task.impl.SmokeTheBongTask;
import com.arlania.engine.task.impl.CandyTask;
import com.arlania.engine.task.impl.ChocCreamTask;
import com.arlania.engine.task.impl.PraiseTask;
import com.arlania.engine.task.impl.PrayerRegainPotionTask;
import com.arlania.engine.task.impl.SpecialRegainPotionTask;
import com.arlania.model.Animation;
import com.arlania.model.Flag;
import com.arlania.model.GameMode;
import com.arlania.model.GameObject;
import com.arlania.model.Graphic;
import com.arlania.model.Item;
import com.arlania.model.Locations.Location;
import com.arlania.model.container.impl.Shop.ShopManager;
import com.arlania.model.Position;
import com.arlania.model.Skill;
import com.arlania.net.packet.Packet;
import com.arlania.net.packet.PacketListener;
import com.arlania.util.Misc;
import com.arlania.util.QuickUtils;
import com.arlania.util.RandomUtility;
import com.arlania.world.World;
import com.arlania.world.content.Achievements;
import com.arlania.world.content.Achievements.AchievementData;
import com.arlania.world.content.ClueScrolls;
import com.arlania.world.content.Consumables;
import com.arlania.world.content.Digging;
import com.arlania.world.content.battlepass.BattlePass;
import com.arlania.world.content.bosses.TrioBosses;
import com.arlania.world.content.boxspinner.MysteryBoxHandler;
import com.arlania.world.content.chests.BloodBellyChest;
import com.arlania.world.content.chests.CrypticChest;
import com.arlania.world.content.chests.CrystalChest;
import com.arlania.world.content.chests.DiamondChest;
import com.arlania.world.content.chests.FrozenChest;
import com.arlania.world.content.StarterTasks.StarterTaskData;
import com.arlania.world.content.Effigies;
import com.arlania.world.content.ExperienceLamps;
import com.arlania.world.content.Gambling;
//import com.arlania.world.content.ItemComparing;
import com.arlania.world.content.KeysEvent;
import com.arlania.world.content.LootingBag;
import com.arlania.world.content.MemberScrolls;
import com.arlania.world.content.MoneyPouch;
import com.arlania.world.content.StarterTasks;
import com.arlania.world.content.combat.range.DwarfMultiCannon;
import com.arlania.world.content.dialogue.DialogueManager;
import com.arlania.world.content.instances.InstanceInterfaceHandler;
import com.arlania.world.content.mysteryboxes.BlueArsenicBox;
import com.arlania.world.content.mysteryboxes.BondCasket;
import com.arlania.world.content.mysteryboxes.CharmBox;
import com.arlania.world.content.mysteryboxes.ChristmasEventPresent;
import com.arlania.world.content.mysteryboxes.ChristmasMysteryBox;
import com.arlania.world.content.mysteryboxes.ChristmasPresent;
import com.arlania.world.content.mysteryboxes.DonationBox;
import com.arlania.world.content.mysteryboxes.ExoticChest;
import com.arlania.world.content.mysteryboxes.FiftyFiftyBox;
import com.arlania.world.content.mysteryboxes.GreenArsenicBox;
import com.arlania.world.content.mysteryboxes.InfernalBox;
import com.arlania.world.content.mysteryboxes.LMSFoodBox;
import com.arlania.world.content.mysteryboxes.LMSRangeBox;
import com.arlania.world.content.mysteryboxes.LMSRuneBox;
import com.arlania.world.content.mysteryboxes.Lootbox;
import com.arlania.world.content.mysteryboxes.Lootbox2;
import com.arlania.world.content.mysteryboxes.Lootbox3;
import com.arlania.world.content.mysteryboxes.PetMysteryBox;
import com.arlania.world.content.mysteryboxes.RandomDrItemBox;
import com.arlania.world.content.mysteryboxes.RelaunchBox;
import com.arlania.world.content.mysteryboxes.TaxBagBox;
import com.arlania.world.content.mysteryboxes.UltraDonationBox;
import com.arlania.world.content.mysteryboxes.VoteMbox;
import com.arlania.world.content.raids.RaidParty;
import com.arlania.world.content.skill.impl.construction.Construction;
import com.arlania.world.content.skill.impl.dungeoneering.ItemBinding;
import com.arlania.world.content.skill.impl.herblore.Herblore;
import com.arlania.world.content.skill.impl.herblore.IngridientsBook;
import com.arlania.world.content.skill.impl.hunter.BoxTrap;
import com.arlania.world.content.skill.impl.hunter.Hunter;
import com.arlania.world.content.skill.impl.hunter.JarData;
import com.arlania.world.content.skill.impl.hunter.PuroPuro;
import com.arlania.world.content.skill.impl.hunter.SnareTrap;
import com.arlania.world.content.skill.impl.hunter.Trap.TrapState;
import com.arlania.world.content.skill.impl.prayer.Prayer;
import com.arlania.world.content.skill.impl.runecrafting.Runecrafting;
import com.arlania.world.content.skill.impl.runecrafting.RunecraftingPouches;
import com.arlania.world.content.skill.impl.runecrafting.RunecraftingPouches.RunecraftingPouch;
import com.arlania.world.content.skill.impl.slayer.BloodSlayerDialogues;
import com.arlania.world.content.skill.impl.slayer.BloodSlayerTasks;
import com.arlania.world.content.skill.impl.slayer.SlayerDialogues;
import com.arlania.world.content.skill.impl.slayer.SlayerTasks;
import com.arlania.world.content.skill.impl.summoning.CharmingImp;
import com.arlania.world.content.skill.impl.summoning.SummoningData;
import com.arlania.world.content.skill.impl.woodcutting.BirdNests;
import com.arlania.world.content.transportation.JewelryTeleporting;
import com.arlania.world.content.transportation.TeleportHandler;
import com.arlania.world.content.transportation.TeleportType;
import com.arlania.world.content.treasuretrails.ClueScroll;
import com.arlania.world.content.treasuretrails.CoordinateScrolls;
import com.arlania.world.content.treasuretrails.DiggingScrolls;
import com.arlania.world.content.treasuretrails.MapScrolls;
import com.arlania.world.content.treasuretrails.Puzzle;
import com.arlania.world.content.treasuretrails.SearchScrolls;
import com.arlania.world.content.upgrading.UpgradeListener;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

import java.time.LocalDateTime;

public class ItemActionPacketListener implements PacketListener {

	public static void cancelCurrentActions(Player player) {
		player.getPacketSender().sendInterfaceRemoval();
		player.setTeleporting(false);
		player.setWalkToTask(null);
		player.setInputHandling(null);
		player.getSkillManager().stopSkilling();
		player.setEntityInteraction(null);
		player.getMovementQueue().setFollowCharacter(null);
		player.getCombatBuilder().cooldown(false);
		player.setResting(false);
	}

	public static boolean checkReqs(Player player, Location targetLocation) {
		if (player.getConstitution() <= 0)
			return false;
		if (player.getTeleblockTimer() > 0) {
			player.getPacketSender().sendMessage("A magical spell is blocking you from teleporting.");
			return false;
		}
		if (player.getLocation() != null && !player.getLocation().canTeleport(player))
			return false;
		if (player.isPlayerLocked() || player.isCrossingObstacle()) {
			player.getPacketSender().sendMessage("You cannot teleport right now.");
			return false;
		}
		return true;
	}

	private static void firstAction(final Player player, Packet packet) {
		int interfaceId = packet.readUnsignedShort();
		int slot = packet.readShort();
		int itemId = packet.readShort();

		Location targetLocation = player.getLocation();

		if (interfaceId == 38274) {
			Construction.handleItemClick(itemId, player);
			return;
		}

		if (slot < 0 || slot > player.getInventory().capacity())
			return;

		if (player.getInventory().getItems()[slot].getId() != itemId)
			return;

        if (MysteryBoxHandler.getInstance().open(player, itemId, true)) {
            return;
        }

		player.setInteractingItem(player.getInventory().getItems()[slot]);

		if (itemId == RaidParty.MAGICAL_ORB && player.getClickDelay().elapsed(2000)) {
			int counter = 0;
			if (player.getRaidParty() == null)
				return;
			player.sendMessage("Checking for players that want to join your raid group..");
			for (Player p : World.getPlayers()) {
				if (p != null) {
					if (p.getRaidParty() == null) {
						if (p.getRaidPartyInvites().contains(player.getRaidParty())) {
							player.sendMessage("@blu@" + p.getUsername() + " is waiting to join your raid group.");
							counter++;
						}
					}
				}
			}
			player.sendMessage("Total: " + counter);
			return;
		}


		if (Prayer.isBone(itemId)) {
			Prayer.buryBone(player, itemId);
			return;
		}
		if (Consumables.isFood(player, itemId, slot))
			return;
		if (Consumables.isPotion(itemId)) {
			Consumables.handlePotion(player, itemId, slot);
			return;
		}
		if (BirdNests.isNest(itemId)) {
			BirdNests.searchNest(player, itemId);
			return;
		}
		if (Herblore.cleanHerb(player, itemId))
			return;
		if (MemberScrolls.handleScroll(player, itemId))
			return;
		if (ClueScroll.handleCasket(player, itemId) || SearchScrolls.loadClueInterface(player, itemId)
				|| MapScrolls.loadClueInterface(player, itemId) || Puzzle.loadClueInterface(player, itemId)
				|| CoordinateScrolls.loadClueInterface(player, itemId)
				|| DiggingScrolls.loadClueInterface(player, itemId))
			return;
		if (Effigies.isEffigy(itemId)) {
			Effigies.handleEffigy(player, itemId);
			return;
		}
		if (ExperienceLamps.handleLamp(player, itemId)) {
			return;
		}

		int drinkingAnimation = 829;
		switch (itemId) {
		
		case 6500:
			LootingBag.displayLootingBagInterface(player);
		break;
		case 19479:
			if(player.getInventory().contains(itemId)) {
				if(player.getPointsHandler().getSlayerPoints() > 20000) {
					player.getInventory().delete(itemId);
					player.getPointsHandler().decrementSlayerPoints(20000);
					player.getInventory().add(19477, 1);
				} else {
					player.sendMessage("You do not have the required items/points to make the lumberjack scroll :/");
					player.sendMessage("20k Slayer points are needed.");
				}
			}
			break;
			
		case 15373:
			int[] common3 = new int[]{2577, 902, 903, 904, 905,896}; //Uncommon, 0
			int[] uncommon3 = new int[]{20016,20017,20018,20021,20022,17911,19892,3956,3928,17908,17909,3932,4775};
			int[] rare3 = new int[]{19137, 19138, 19139, 18940,18942, 18941};
			player.getMysteryBoxOpener().setOpenBox(15373);
			player.getMysteryBoxOpener().display(15373, "Noobie Mbox", common3, uncommon3, rare3);
			break;
			
		case 6199:
			int[] common4 = new int[]{19721,19722,19723,19734,19736,19468,18363,15398,15418}; //Uncommon, 0
			int[] uncommon4 = new int[]{18380,18381,18382,9006,3941,3974,18392,4799,4800,4801};
			int[] rare4 = new int[]{15012,1499,3951,5079,3960,3958,3959,5187,5186,3316,3931,14559,6583};
			player.getMysteryBoxOpener().setOpenBox(6199);
			player.getMysteryBoxOpener().display(6199, "Gracious Mbox", common4, uncommon4, rare4);

			break;
			
			
		case 3988:
			int[] common5 = new int[]{4772,4771,4770,1543,18940,18941,18942,922,2572,19468,5186,5187,3316,3931,3958,3959,3960,18347}; //Uncommon, 0
			int[] uncommon5 = new int[]{19935,6193,6194,6195,6196,6197,6198,3994,3995,3996,3974,5131,18751,18748,18950 };
			int[] rare5 = new int[]{19936,923,3271,3272,3273,3283,3284,3285,3276,19720};
			player.getMysteryBoxOpener().setOpenBox(3988);
			player.getMysteryBoxOpener().display(3988, "Pleasant Mbox", common5, uncommon5, rare5);
			break;
			
		case 15374:
			int[] common6 = new int[]{19935,5082,5083,5084,15656,15045,926,931,5211,930,5210}; //Uncommon, 0
			int[] uncommon6 = new int[]{19886,19936,9492,9493,9494,9495,19159,19160,19161,19163,19164,19165,19166,19691,19692,19693,19694,19695,19696,19618};
			int[] rare6 = new int[]{19727,19728,19729,19730,19731,16455,19732,19620};
			player.getMysteryBoxOpener().setOpenBox(15374);
			player.getMysteryBoxOpener().display(15374, "Elemental Mbox", common6, uncommon6, rare6);
			break;
			
			
			
		case 13997:
			int[] common7 = new int[]{19935,5082,5083,5084,15656,15045,926,931,5211,930,5210,19886,9492,9493,9494,9495,19159,19160,19161,19163,19164,19165,19166,19691,19692,19693,19694,19695,19696,19618}; //Uncommon, 0
			int[] uncommon7 = new int[]{19936,19106,19727,19728,19729,19730,19731,19732,5171,19620,19821};
			int[] rare7 = new int[]{16455,19620,13202,13203,13204,5171,13206,13207,11143,11144,11145,11156,11147,4794,4795,4796,4797,19127,19128,19129,13991,13992,13993,13994,13995,14447,14448,10905,19155,9496,9497,9498,934,3952,3950,3949};
			player.getMysteryBoxOpener().setOpenBox(13997);
			player.getMysteryBoxOpener().display(13997, "Regal Mbox", common7, uncommon7, rare7);
			break;
			
			
			
		case 10168:
			int[] common = new int[]{19159,19160,19161,19163,19106,5210,15045,931,930,5211,926,3820,3821,3822,20054,3957,4785,5154,15032,19087,19089,19092,3985,5082,5083,5084,15656,5129,19470,19471,19472,19473,19474,19619,3983,4641,4642,4643,19618,19620,19691,19692,19693,19695,19696,9492,9493,9494,9495,19935}; //Uncommon, 0
			int[] uncommon = new int[]{19938,19821,13991,13202,13203,4652,5170,12426,3317,19958,13204,13205,13206,13207,13992,13993,13994,13995,14447,14448,4794,4795,4796,4797,19127,19128,19129,20427,20431,9496,9497,9498,9499,10905,19155,19935,19936};
			int[] rare = new int[]{773,774,15566,8654,11978,5020,5197,8699};
			player.getMysteryBoxViewerOwner().setOpenBox(10168);
			player.getMysteryBoxViewerOwner().display(10168, "Owner Mbox", common, uncommon, rare);
			break;
			
			
			
		case 15521:
			int[] common10 = new int[]{19936,19106,19727,19728,19729,19730,19731,19732,5171,19620,19821}; //Uncommon, 0
			int[] uncommon10 = new int[]{16455,19620,13202,13203,13204,5171,13206,13207,11143,11144,11145,11156,11147,4794,4795,4796,4797,19127,19128,19129,13991,13992,13993,13994,13995,14447,14448,10905,19155,9496,9497,9498,934,19154,19742,19743,19744,20427,20431,5226,5227,5228,5229,5230,5231};
			int[] rare10 = new int[]{10205,16443,16444,16445,16446,16449,16448,16450,16451,14437,14438,14439,14440,14441,14442,14443,14482,14483,18427,18419,18420,18421,18425,6770,6758};
			player.getMysteryBoxOpener().setOpenBox(15521);
			player.getMysteryBoxOpener().display(15521, "Marvelous Mbox", common10, uncommon10, rare10);
			break;
			
			
			
			
		case 16456:
			int[] common9 = new int[]{19159,19160,19161,19163,19106,5210,15045,931,927,930,5211,926,3820,3821,3822,20054,3957,4785,5154,15032,19087,19089,19092,3985,5082,5083,5084,15656,5129,19470,19471,19472,19473,19474,19619,3983,3984,4641,4642,4643,19618,19620,19691,19692,19693,19695,19696,9492,9493,9494,9495,19935}; //Uncommon, 0
			int[] uncommon9 = new int[]{5020,19938,8699,19821,13991,13202,13203,4652,5170,5197,12426,3317,19958,13204,13205,13206,13207,13992,13993,13994,13995,14447,14448,4794,4795,4796,4797,19127,19128,19129,20427,20431,9496,9497,9498,9499,10905,19155,19935,19936};
			int[] rare9 = new int[]{773,774,15566,8654,11978,9116,9117,5020};
			player.getMysteryBoxOpener().setOpenBox(16456);
			player.getMysteryBoxOpener().display(16456, "Grand Chest", common9, uncommon9, rare9);
			break;
			
		case 455:
			new com.arlania.world.content.scratchcard.ScratchCard(player).display();
			break;
			
		case 14415:
			if (!PrayerRegainPotionTask.effectStatus) {
				player.performAnimation(new Animation(drinkingAnimation));
				TaskManager.submit(new PrayerRegainPotionTask(player));
				player.getInventory().delete(14415, 1);
				player.sendMessage("This will increase your prayer 2 times (1 now, 1 afterwards)");
				PrayerRegainPotionTask.effectStatus = true;
			} else {
				player.sendMessage("@blu@You already have the effect.");
			}
			break;
			
		case 15648:
			player.getPointsHandler().incrementTriviaPoints( player, 10);
			player.getInventory().delete(15648, 1);
			player.sendMessage("10 Trivia points have been added to your account");
			break;

		case 4767:
			StarterTasks.finishTask(player, StarterTaskData.READ_THE_GUIDE_BOOK);
			player.sendMessage("@red@You've read the book!");
			player.getPacketSender().sendInterface(50200);
			break;

		case 3989:
			new RandomDrItemBox().open(player);
			break;
			
			
		case 16496:
			if (player.getInventory().getFreeSlots() < 8) {
				player.getPacketSender().sendMessage("<shad=67241f>You need at least 8 inventory spaces");
				return;
			}
			player.getInventory().delete(16496, 1);
			player.getInventory().add(16436, 1).refreshItems();
			player.getInventory().add(16435, 1).refreshItems();
			player.getInventory().add(16463, 1).refreshItems();
			player.getInventory().add(16489, 1).refreshItems();
			player.getInventory().add(16490, 1).refreshItems();
			player.getInventory().add(16491, 1).refreshItems();
			player.getInventory().add(16493, 1).refreshItems();
			player.getInventory().add(16494, 1).refreshItems();
			World.sendMessage( "<img=453><col=12abb3><shad=18549b>[NEWS]</shad>@bla@: " + player.getUsername() + " Claimed the <shad=67241f>Blood slayer set pack!</shad><shad=1f6767> Thank you for the support !");
			break;
			
		
		case 5185:
			if(player.getClickDelay().elapsed(60000)) {
			player.getSkillManager().setCurrentLevel(Skill.STRENGTH, 200);
			player.getSkillManager().setCurrentLevel(Skill.ATTACK, 200);
			player.getSkillManager().setCurrentLevel(Skill.DEFENCE, 200);
			player.getSkillManager().setCurrentLevel(Skill.RANGED, 250);
			player.getSkillManager().setCurrentLevel(Skill.MAGIC, 250);
			player.performAnimation(new Animation(2289));
			player.sendMessage("@blu@The Potion Makes you feel even stronger");
			player.getClickDelay().reset();
			} else {
				return;
			}
			break;
			
		case 6507:
			if(player.getClickDelay().elapsed(60000)) {
			player.getSkillManager().setCurrentLevel(Skill.STRENGTH, 200);
			player.getSkillManager().setCurrentLevel(Skill.ATTACK, 200);
			player.getSkillManager().setCurrentLevel(Skill.DEFENCE, 200);
			player.getSkillManager().setCurrentLevel(Skill.RANGED, 250);
			player.getSkillManager().setCurrentLevel(Skill.MAGIC, 250);
			player.performAnimation(new Animation(2289));
			player.getInventory().delete(6507, 1);
			player.sendMessage("@blu@The Potion Makes you feel even stronger");
			player.getClickDelay().reset();
			player.getAchievementTracker().progress(com.arlania.world.content.achievements.AchievementData.DRINK_A_DELUGE_POTION, 1);
			player.getAchievementTracker().progress(com.arlania.world.content.achievements.AchievementData.DRINK_100_DELUGE_POTIONS, 1);
			} else {
				return;
			}
			break;
			
		case 3961:
			if(player.getClickDelay().elapsed(60000)) {
			player.getSkillManager().setCurrentLevel(Skill.STRENGTH, 300);
			player.getSkillManager().setCurrentLevel(Skill.ATTACK, 300);
			player.getSkillManager().setCurrentLevel(Skill.DEFENCE, 300);
			player.getSkillManager().setCurrentLevel(Skill.RANGED, 300);
			player.getSkillManager().setCurrentLevel(Skill.MAGIC, 300);
			player.getClickDelay().reset();
			player.performAnimation(new Animation(2289));
			player.sendMessage("@blu@The Potion Makes you feel even stronger, and NPCs want to take it....");
			player.sentFadeAgroPot = false;
			player.agroPotionTime = LocalDateTime.now().plusMinutes(999);
			} else {
				return;
			}
			break;

		case 14269:
			if(player.getClickDelay().elapsed(60000)) {
					player.performAnimation(new Animation(1500));
					player.getCharacterAnimations().setStandingAnimation(1501);
					player.getCharacterAnimations().setWalkingAnimation(1851);
					player.getCharacterAnimations().setRunningAnimation(1851);
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					player.sendMessage("The potion makes you feel like a god");
				} else {
					player.getCharacterAnimations().setStandingAnimation(0x328);
					player.getCharacterAnimations().setWalkingAnimation(0x333);
					player.getCharacterAnimations().setRunningAnimation(0x328);
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					player.sendMessage("Flymode has been deactivated.");
				}

			break;
		
		
		
		case 5120:
			StarterTasks.updateInterface(player);
			int[] ids = { 12434, 12435, 12436, 5130, 4646, 4645, 4644, 3286, 19936, 6570, 6183 };
			for (int i = 0; i < ids.length; i++) {
				player.getPacketSender().sendItemOnInterface(53205, ids[i], i, 1);
			}
			player.getPacketSender().sendInterfaceReset();
			player.getPacketSender().sendInterface(53200);
			break;
			
		case 5168:
			TeleportHandler.teleportPlayer(player, new Position(2524, 4774, 4), player.getSpellbook().getTeleportType());
			break;


		case 5122:
			player.getPacketSender().sendInterface(64530);
			break;

		case 5148:
			if (!HPRegainPotionTask.effectStatus) {
				player.performAnimation(new Animation(drinkingAnimation));
				TaskManager.submit(new HPRegainPotionTask(player));
				player.getInventory().delete(5148, 1);
				player.sendMessage("This will heal you 2 times (1 now, 1 afterwards)");
				HPRegainPotionTask.effectStatus = true;
			} else {
				player.sendMessage("@blu@You already have the effect.");
			}
			break;
			
		case 3903:
			LMSRangeBox.openBox(player);
			break;

		case 3902:
			LMSRuneBox.openBox(player);
			break;

		case 3886:
			LMSFoodBox.openBox(player);
			break;

			
		case 14084:
			if (player.isCandyRateActive()) {
				player.sendMessage("@red@You already have DPS boost from the candy, save it for later");
				player.sendMessage("Your DPS effect will end in: @red@"
						+ (int) QuickUtils.tickToMin(player.getCandyTime()) + QuickUtils.getCandyPrefix(player));
			} else {
				player.getInventory().delete(itemId, 1);
                player.performAnimation(new Animation(829));
				player.forceChat("Nom Nom Nom");
				player.sendMessage("You eat the candy and instantly feel a rush of power through your body.");
				player.incrementCandyTime(3000);
				TaskManager.submit(new CandyTask(player));
				player.setCandyRateActive(true);
			}
			break;
			
			
			
		case 19890:
			if (player.isDoubleDropsActive()) {
				player.sendMessage("You already have double drops active");
				player.sendMessage(
						"Your double drops effect will end in: " + (int) QuickUtils.tickToMin(player.getCleansingTime())
								+ QuickUtils.getCleansingPrefix(player));
			} else {
				player.getInventory().delete(itemId, 1);
				player.sendMessage("You have been granted 1 hours of double drops.");
				player.incrementCleansingTime(6000);
				TaskManager.submit(new CleansingTask(player));
				player.setDoubleDropsActive(true);
			}
			break;
		

			
		case 14259:	
			if (player.isDoubleDropsActive()) {
				player.sendMessage("You already have double drops active");
				player.sendMessage(
						"Your double drops effect will end in: " + (int) QuickUtils.tickToMin(player.getCleansingTime())
								+ QuickUtils.getCleansingPrefix(player));
			} else {
				player.getInventory().delete(itemId, 1);
				player.getInventory().add(14249, 1);
				player.sendMessage("You have been granted @red@15Minutes @bla@of @red@double drops.");
				player.incrementCleansingTime(1500);
				player.performAnimation(new Animation(829));
				TaskManager.submit(new CleansingTask(player));
				player.setDoubleDropsActive(true);
			}
			break;
			
			
		case 14249:
			if (player.isDoubleDropsActive()) {
				player.sendMessage("You already have double drops active");
				player.sendMessage(
						"Your double drops effect will end in: " + (int) QuickUtils.tickToMin(player.getCleansingTime())
								+ QuickUtils.getCleansingPrefix(player));
			} else {
				player.getInventory().delete(itemId, 1);
				player.getInventory().add(20929, 1);
				player.sendMessage("You have been granted @red@15Minutes @bla@of @red@double drops.");
				player.incrementCleansingTime(1500);
				player.performAnimation(new Animation(829));
				TaskManager.submit(new CleansingTask(player));
				player.setDoubleDropsActive(true);
			}
			break;
			
			
		case 20929:
			if (player.isDoubleDropsActive()) {
				player.sendMessage("You already have double drops active");
				player.sendMessage(
						"Your double drops effect will end in: " + (int) QuickUtils.tickToMin(player.getCleansingTime())
								+ QuickUtils.getCleansingPrefix(player));
			} else {
				player.getInventory().delete(itemId, 1);
				player.getInventory().add(20930, 1);
				player.sendMessage("You have been granted @red@15Minutes @bla@of @red@double drops.");
				player.incrementCleansingTime(1500);
				player.performAnimation(new Animation(829));
				TaskManager.submit(new CleansingTask(player));
				player.setDoubleDropsActive(true);
			}
			break;
		case 20930:
			if (player.isDoubleDropsActive()) {
				player.sendMessage("You already have double drops active");
				player.sendMessage(
						"Your double drops effect will end in: " + (int) QuickUtils.tickToMin(player.getCleansingTime())
								+ QuickUtils.getCleansingPrefix(player));
			} else {
				player.getInventory().delete(itemId, 1);
				player.sendMessage("You have been granted @red@15Minutes @bla@of @red@double drops.");
				player.incrementCleansingTime(1500);
				player.performAnimation(new Animation(829));
				TaskManager.submit(new CleansingTask(player));
				player.setDoubleDropsActive(true);
			}
			break;
			
		case 19864:
			try {
				ShopManager.getShops().get(58).open(player);
				player.getPacketSender().sendMessage("This store is pretty cool");
				player.sendMessage("If you have any suggestions on what should be added");
				player.sendMessage("Make sure to make a suggestion and i will consider it.");
			} catch (Exception i) {
				player.sendMessage("Failed to open store.. shop doesn't exist in the list!");
			}
			break;
			
			
		case 14808:
			if (player.isDoubleRateActive()) {
				player.sendMessage("You already have double drop rate active");
				player.sendMessage("Your double drop rate effect will end in: "
						+ (int) QuickUtils.tickToMin(player.getPraiseTime()) + QuickUtils.getPraisePrefix(player));
			} else {
				player.getInventory().delete(itemId, 1);
				player.sendMessage("You have been granted 1 hours of double drop rates.");
				player.incrementPraiseTime(6000);
				TaskManager.submit(new PraiseTask(player));
				player.setDoubleRateActive(true);
			}
			break;
			
		case 20921:
			if (player.isIceCreamRateActive()) {
				player.sendMessage("@red@You already have DR boosted from Ice cream");
				player.sendMessage("Your drop rate effect will end in: @red@"
						+ (int) QuickUtils.tickToMin(player.getIceCreamTime()) + QuickUtils.getIceCreamPrefix(player));
			} else {
				player.getInventory().delete(itemId, 1);
                player.performAnimation(new Animation(829));
				player.forceChat("Nom Nom Nom");
				player.sendMessage("You eat the cone in one bite! It boosts your Droprate @red@20%");
				player.incrementIceCreamTime(2000);
				TaskManager.submit(new IceCreamTask(player));
				player.setIceCreamRateActive(true);
			}
			break;
			
		case 20920:
			if (player.isIceCreamRateActive()) {
				player.sendMessage("@red@You already have DR boosted from Ice cream");
				player.sendMessage("Your drop rate effect will end in: @red@"
						+ (int) QuickUtils.tickToMin(player.getIceCreamTime()) + QuickUtils.getIceCreamPrefix(player));
			} else {
				player.getInventory().delete(itemId, 1);
                player.performAnimation(new Animation(829));
				player.forceChat("Nom Nom Nom");
				player.getInventory().add(20921, 1);
				player.sendMessage("The cone is too much! You save some for later. Your Droprate boosted @red@20%");
				player.incrementIceCreamTime(2000);
				TaskManager.submit(new IceCreamTask(player));
				player.setIceCreamRateActive(true);
			}
			break;

		case 20922:
			if (player.isIceCreamRateActive()) {
				player.sendMessage("@red@You already have DR boosted from Ice cream");
				player.sendMessage("Your drop rate effect will end in: @red@"
						+ (int) QuickUtils.tickToMin(player.getIceCreamTime()) + QuickUtils.getIceCreamPrefix(player));
			} else {
				player.getInventory().delete(itemId, 1);
                player.performAnimation(new Animation(829));
				player.forceChat("Nom Nom Nom");
				player.getInventory().add(20920, 1);
				player.sendMessage("The cone is too much! You save some for later. Your Droprate boosted @red@20%");
				player.incrementIceCreamTime(2000);
				TaskManager.submit(new IceCreamTask(player));
				player.setIceCreamRateActive(true);
			}
			break;
			
		case 20926:
			if (player.isChocCreamRateActive()) {
				player.sendMessage("@red@You already have DR boosted from Chocolate Ice cream");
				player.sendMessage("Your drop rate effect will end in: @red@"
						+ (int) QuickUtils.tickToMin(player.getChocCreamTime()) + QuickUtils.getChocCreamPrefix(player));
			} else {
				player.getInventory().delete(itemId, 1);
                player.performAnimation(new Animation(829));
				player.forceChat("Nom Nom Nom");
				player.sendMessage("You eat the cone in one bite! It boosts your Droprate @red@30%");
				player.incrementChocCreamTime(2000);
				TaskManager.submit(new ChocCreamTask(player));
				player.setChocCreamRateActive(true);
			}
			break;
			
		case 20924:
			if (player.isChocCreamRateActive()) {
				player.sendMessage("@red@You already have DR boosted from Chocolate Ice cream");
				player.sendMessage("Your drop rate effect will end in: @red@"
						+ (int) QuickUtils.tickToMin(player.getChocCreamTime()) + QuickUtils.getChocCreamPrefix(player));
			} else {
				player.getInventory().delete(itemId, 1);
                player.performAnimation(new Animation(829));
				player.forceChat("Nom Nom Nom");
				player.getInventory().add(20926, 1);
				player.sendMessage("The cone is too much! You save some for later. Your Droprate boosted @red@30%");
				player.incrementChocCreamTime(3000);
				TaskManager.submit(new ChocCreamTask(player));
				player.setChocCreamRateActive(true);
			}
			break;
			
		case 20925:
			if (player.isChocCreamRateActive()) {
				player.sendMessage("@red@You already have DR boosted from Chocolate Ice cream");
				player.sendMessage("Your drop rate effect will end in: @red@"
						+ (int) QuickUtils.tickToMin(player.getChocCreamTime()) + QuickUtils.getChocCreamPrefix(player));
			} else {
				player.getInventory().delete(itemId, 1);
                player.performAnimation(new Animation(829));
				player.forceChat("Nom Nom Nom");
				player.getInventory().add(20924, 1);
				player.sendMessage("The cone is too much! You save some for later. Your Droprate boosted @red@30%");
				player.incrementChocCreamTime(3000);
				TaskManager.submit(new ChocCreamTask(player));
				player.setChocCreamRateActive(true);
			}
			break;
			
		//case 6639:
		//	player.getInventory().delete(itemId, 1);
			//player.getMinimeSystem().spawn();
			//break;
			
		case 13691:
            player.setBattlePass(true);
			player.getInventory().delete(13691, 1);
            BattlePass.INSTANCE.openInterface(player);
			break;
			

		case 3912:
			TaxBagBox.openBox(player);
			break;

		case 15369:
			player.setMbox1(true);
			player.setMbox2(false);
			player.setMbox3(false);
			player.setMbox4(false);
			player.setMbox5(false);
			player.setLegendary(false);
			player.getInventory().delete(15369, 1);
			player.getMysteryBox().processMbox1();
			player.getMysteryBox().reward();
			break;
		case 15370:
			player.setMbox1(false);
			player.setMbox2(true);
			player.setMbox3(false);
			player.setMbox4(false);
			player.setMbox5(false);
			player.setLegendary(false);
			player.getInventory().delete(15370, 1);
			player.getMysteryBox().processMbox2();
			player.getMysteryBox().reward();
			break;
		case 15371:
			player.setMbox1(false);
			player.setMbox2(false);
			player.setMbox3(true);
			player.setMbox4(false);
			player.setMbox5(false);
			player.setLegendary(false);
			player.getInventory().delete(15371, 1);
			player.getMysteryBox().processMbox3();
			player.getMysteryBox().reward();
			break;
		case 15372:
			player.setMbox1(false);
			player.setMbox2(false);
			player.setMbox3(false);
			player.setMbox4(true);
			player.setMbox5(false);
			player.setLegendary(false);
			player.getInventory().delete(15372, 1);
			player.getMysteryBox().processMbox4();
			player.getMysteryBox().reward();
			break;



		case 13663:
			if (player.getInterfaceId() > 0) {
				player.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
				return;
			}
			player.setUsableObject(new Object[2]).setUsableObject(0, "reset");
			player.getPacketSender().sendString(38006, "Choose stat to reset!")
					.sendMessage("@red@Please select a skill you wish to reset and then click on the 'Confim' button.")
					.sendString(38090, "Which skill would you like to reset?");
			player.getPacketSender().sendInterface(38000);
			break;
		case 19670:
			if (player.busy()) {
				player.getPacketSender().sendMessage("You can not do this right now.");
				return;
			}
			player.setDialogueActionId(70);
			DialogueManager.start(player, player.getGameMode() == GameMode.NORMAL ? 108 : 109);
			break;
		case 8007: // Varrock
			if (player.inFFALobby) {
				player.sendMessage("Use the portal to leave the ffa lobby!");
				return;
			}
			if (player.inFFA) {
				player.sendMessage("You can not teleport out of FFA!");
				return;
			}
			if (!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if (player.getLocation() == Location.CONSTRUCTION) {
				player.getPacketSender().sendMessage("Please use the portal to exit your house");
				return;
			}

			if (!checkReqs(player, targetLocation)) {
				return;
			}
			player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
			cancelCurrentActions(player);
			player.performAnimation(new Animation(4731));
			player.performGraphic(new Graphic(678));
			player.getInventory().delete(8007, 1);
			player.getClickDelay().reset();

			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {
					Position position = new Position(3210, 3424, 0);
					player.moveTo(position);
					player.getMovementQueue().setLockMovement(false).reset();
					this.stop();
				}
			});

			break;
		case 19475: // Custom zone
			if (player.inFFALobby) {
				player.sendMessage("Use the portal to leave the ffa lobby!");
				return;
			}
			if (player.inFFA) {
				player.sendMessage("You can not teleport out of FFA!");
				return;
			}
			if (!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if (player.getLocation() == Location.CONSTRUCTION) {
				player.getPacketSender().sendMessage("Please use the portal to exit your house");
				return;
			}

			if (!checkReqs(player, targetLocation)) {
				return;
			}
			player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
			cancelCurrentActions(player);
			player.performAnimation(new Animation(4731));
			player.performGraphic(new Graphic(678));
			player.getInventory().delete(8007, 1);
			player.getClickDelay().reset();

			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {
					Position position = new Position(2478, 10129, 0);
					player.moveTo(position);
					player.getMovementQueue().setLockMovement(false).reset();
					this.stop();
				}
			});

			break;
		case 18702:
			for (int i = 8145; i <= 8195; i++) {
				player.getPacketSender().sendString(i, "");
			}
			player.getPacketSender().sendString(8144, "@dre@Pwnlite Starter Guide");
			player.getPacketSender().sendString(8145, "@dre@Make sure to check out all the forum guides");
			player.getPacketSender().sendString(8149, "@dre@Start by going to ::defilers or ::unicorn");
			player.getPacketSender().sendString(8153, "@dre@Make sure to vote and sell the vote book");
			player.getPacketSender().sendString(8155, "@dre@Ask new players on what to do next");
			player.getPacketSender().sendInterface(8134);
			break;
		case 19476: // Custom zone 2 ( 30.09.2018 )
			if (player.inFFALobby) {
				player.sendMessage("Use the portal to leave the ffa lobby!");
				return;
			}
			if (player.inFFA) {
				player.sendMessage("You can not teleport out of FFA!");
				return;
			}
			if (!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if (player.getLocation() == Location.CONSTRUCTION) {
				player.getPacketSender().sendMessage("Please use the portal to exit your house");
				return;
			}

			if (!checkReqs(player, targetLocation)) {
				return;
			}
			player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
			cancelCurrentActions(player);
			player.performAnimation(new Animation(4731));
			player.performGraphic(new Graphic(678));
			player.getInventory().delete(8007, 1);
			player.getClickDelay().reset();

			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {
					Position position = new Position(2524, 4776, 0);
					player.moveTo(position);
					player.getMovementQueue().setLockMovement(false).reset();
					this.stop();
				}
			});

			break;
			
		case 5206: // DUNGEON FLOOR 2 TP
			TeleportHandler.teleportPlayer(player, new Position(2845, 3540, 0), TeleportType.NORMAL);
			break;
	
		case 17922: // DUNGEON FLOOR 2 TP
			TeleportHandler.teleportPlayer(player, new Position(2478, 10129, 0), TeleportType.NORMAL);
			player.getInventory().delete(17922, 1);
			break;
			
		case 5136: // DUNGEON FLOOR 2 TP
			TeleportHandler.teleportPlayer(player, new Position(2518, 4645, 8), TeleportType.NORMAL);
			break;
			
		case 8008: // Lumbridge
			if (player.inFFALobby) {
				player.sendMessage("Use the portal to leave the ffa lobby!");
				return;
			}
			if (player.inFFA) {
				player.sendMessage("You can not teleport out of FFA!");
				return;
			}
			if (!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if (player.getLocation() == Location.CONSTRUCTION) {
				player.getPacketSender().sendMessage("Please use the portal to exit your house");
				return;
			}

			if (!checkReqs(player, targetLocation)) {
				return;
			}
			player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
			cancelCurrentActions(player);
			player.performAnimation(new Animation(4731));
			player.performGraphic(new Graphic(678));
			player.getInventory().delete(8008, 1);
			player.getClickDelay().reset();

			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {
					Position position = new Position(3222, 3218, 0);
					player.moveTo(position);
					player.getMovementQueue().setLockMovement(false).reset();
					this.stop();
				}
			});

			break;
		case 8009: // Falador
			if (player.inFFALobby) {
				player.sendMessage("Use the portal to leave the ffa lobby!");
				return;
			}
			if (player.inFFA) {
				player.sendMessage("You can not teleport out of FFA!");
				return;
			}
			if (!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if (player.getLocation() == Location.CONSTRUCTION) {
				player.getPacketSender().sendMessage("Please use the portal to exit your house");
				return;
			}

			if (!checkReqs(player, targetLocation)) {
				return;
			}
			player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
			cancelCurrentActions(player);
			player.performAnimation(new Animation(4731));
			player.performGraphic(new Graphic(678));
			player.getInventory().delete(8009, 1);
			player.getClickDelay().reset();

			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {
					Position position = new Position(2964, 3378, 0);
					player.moveTo(position);
					player.getMovementQueue().setLockMovement(false).reset();
					this.stop();
				}
			});

			break;
		case 8010: // Camelot
			if (player.inFFALobby) {
				player.sendMessage("Use the portal to leave the ffa lobby!");
				return;
			}
			if (player.inFFA) {
				player.sendMessage("You can not teleport out of FFA!");
				return;
			}
			if (!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if (player.getLocation() == Location.CONSTRUCTION) {
				player.getPacketSender().sendMessage("Please use the portal to exit your house");
				return;
			}

			if (!checkReqs(player, targetLocation)) {
				return;
			}
			player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
			cancelCurrentActions(player);
			player.performAnimation(new Animation(4731));
			player.performGraphic(new Graphic(678));
			player.getInventory().delete(8010, 1);
			player.getClickDelay().reset();

			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {
					Position position = new Position(2757, 3477, 0);
					player.moveTo(position);
					player.getMovementQueue().setLockMovement(false).reset();
					this.stop();
				}
			});

			break;
		case 8011: // Ardy
			if (player.inFFALobby) {
				player.sendMessage("Use the portal to leave the ffa lobby!");
				return;
			}
			if (player.inFFA) {
				player.sendMessage("You can not teleport out of FFA!");
				return;
			}
			if (!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if (player.getLocation() == Location.CONSTRUCTION) {
				player.getPacketSender().sendMessage("Please use the portal to exit your house");
				return;
			}

			if (!checkReqs(player, targetLocation)) {
				return;
			}
			player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
			cancelCurrentActions(player);
			player.performAnimation(new Animation(4731));
			player.performGraphic(new Graphic(678));
			player.getInventory().delete(8011, 1);
			player.getClickDelay().reset();

			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {
					Position position = new Position(2662, 3305, 0);
					player.moveTo(position);
					player.getMovementQueue().setLockMovement(false).reset();
					this.stop();
				}
			});

			break;
		case 8012: // Watchtower Tele
			if (player.inFFALobby) {
				player.sendMessage("Use the portal to leave the ffa lobby!");
				return;
			}
			if (player.inFFA) {
				player.sendMessage("You can not teleport out of FFA!");
				return;
			}
			if (!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if (player.getLocation() == Location.CONSTRUCTION) {
				player.getPacketSender().sendMessage("Please use the portal to exit your house");
				return;
			}

			if (!checkReqs(player, targetLocation)) {
				return;
			}
			player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
			cancelCurrentActions(player);
			player.performAnimation(new Animation(4731));
			player.performGraphic(new Graphic(678));
			player.getInventory().delete(8012, 1);
			player.getClickDelay().reset();

			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {
					Position position = new Position(2728, 3349, 0);
					player.moveTo(position);
					player.getMovementQueue().setLockMovement(false).reset();
					this.stop();
				}
			});

			break;
		case 8013: // Home Tele
			TeleportHandler.teleportPlayer(player, new Position(3087, 3491), TeleportType.NORMAL);
			break;
		case 19121:
			if (player.getInventory().contains(19121, 1)) {
				player.getInventory().delete(19121, 1);
				player.getInventory().add(896, 1);
				player.getInventory().add(2577, 1);
				player.getInventory().add(10835, 1);
				player.getInventory().add(989, 1);
				player.getInventory().add(15243, 125);
				player.getInventory().add(5161, 1);
			} else {
				return;
			}
			break;

		case 19477: // Home Tele
			TeleportHandler.teleportPlayer(player, new Position(2392, 9907), TeleportType.NORMAL);
			player.sendMessage("@red@Welcome to the new zone, enjoy the drops :D");
			break;
		case 19478: // Home Tele
			TeleportHandler.teleportPlayer(player, new Position(2899, 9912), TeleportType.NORMAL);
			player.sendMessage("@red@Welcome to the Ant man zone!");
			break;
		case 13598: // Runecrafting Tele
			TeleportHandler.teleportPlayer(player, new Position(2595, 4772), TeleportType.NORMAL);
			break;
		case 13599: // Air Altar Tele
			TeleportHandler.teleportPlayer(player, new Position(2845, 4832), TeleportType.NORMAL);
			break;
		case 3062:
			int chance = RandomUtility.random(100);
			if (chance >= 50) {
				player.getInventory().delete(3062, 1);
				player.getPointsHandler().incrementboxPoints(1);
				player.sendMessage("You were lucky and got 1 Box Point, you now have @red@"
						+ player.getPointsHandler().getboxPoints() + " Box Points");
			} else {
				player.getInventory().delete(3062, 1);
				player.sendMessage("You were unlucky n got nothing");
			}
			break;
		case 6503:
			int tickets = RandomUtility.getRandom(5);
				player.getInventory().delete(6503, 1);
				
                player.getInventory().add(6532, tickets);
				player.sendMessage("You open the ticket box,and you get " + tickets + " from inside !");
			break;
			
		case 6504:
			int tickets2 = RandomUtility.getRandom(10);
				player.getInventory().delete(6504, 1);
				
                player.getInventory().add(6532, tickets2);
				player.sendMessage("You open the ticket box,and you get " + tickets2 + " from inside !");
			break;
			
		case 13600: // Mind Altar Tele
			TeleportHandler.teleportPlayer(player, new Position(2796, 4818), TeleportType.NORMAL);
			break;
		case 13601: // Water Altar Tele
			TeleportHandler.teleportPlayer(player, new Position(2713, 4836), TeleportType.NORMAL);
			break;
		case 13602: // Earth Altar Tele
			TeleportHandler.teleportPlayer(player, new Position(2660, 4839), TeleportType.NORMAL);
			break;
		case 13603: // Fire Altar Tele
			TeleportHandler.teleportPlayer(player, new Position(2584, 4836), TeleportType.NORMAL);
			break;
		case 13604: // Body Altar Tele
			TeleportHandler.teleportPlayer(player, new Position(2527, 4833), TeleportType.NORMAL);
			break;
		case 13605: // Cosmic Altar Tele
			TeleportHandler.teleportPlayer(player, new Position(2162, 4833), TeleportType.NORMAL);
			break;
		case 13606: // Chaos Altar Tele
			TeleportHandler.teleportPlayer(player, new Position(2269, 4843), TeleportType.NORMAL);
			break;
		case 13607: // Nature Altar Tele
			TeleportHandler.teleportPlayer(player, new Position(2398, 4841), TeleportType.NORMAL);
			break;
		case 13608: // Law Altar Tele
			TeleportHandler.teleportPlayer(player, new Position(2464, 4834), TeleportType.NORMAL);
			break;
		case 13609: // Death Altar Tele
			TeleportHandler.teleportPlayer(player, new Position(2207, 4836), TeleportType.NORMAL);
			break;
		case 18809: // Rimmington Tele
			TeleportHandler.teleportPlayer(player, new Position(2957, 3214), TeleportType.NORMAL);
			break;
		case 18811: // Pollnivneach Tele
			TeleportHandler.teleportPlayer(player, new Position(3359, 2910), TeleportType.NORMAL);
			break;
			
		case 18812: // Rellekka Tele
	            player.performAnimation(new Animation(2304));
				player.getInventory().delete(18812, 1);
				player.delayedMoveTo(new Position(3048, 3924), 2);
				player.sendMessage("You seem to have been poisoned and woke up in an unknown location.");
			break;
		case 18814: // Yanielle Tele
			TeleportHandler.teleportPlayer(player, new Position(2606, 3093), TeleportType.NORMAL);
			break;
		case 6542:
			ChristmasPresent.openBox(player);
			break;
		
		case 3567:
			Lootbox.openBox(player);
			break;
		case 2798:
			Lootbox2.openBox(player);
			break;
		
		case 2795:
			Lootbox3.openBox(player);
			break;
		
		case 3578:
			Lootbox2.openBox(player);
			break;
		case 10205:
			BondCasket.openBox(player);
			break;
			
		case 14033:
			GreenArsenicBox.openBox(player);
			break;
			
		case 16488:
			BlueArsenicBox.openBox(player);
			break;

			
		case 20450:
			if(player.getTransform() == 15) {
				player.setTimer(player.getTimer()+(100*30));
				player.getInventory().delete(20451, 1);

			} else {
				if(player.getTransform() < 1) {
					player.setNpcTransformationId(15);
					player.setTransform(15);
					player.setTimer(100 * 30);
					player.getInventory().delete(20450, 1);

					player.getUpdateFlag().flag(Flag.APPEARANCE);

					NPC npc = new NPC(15, new Position(0, 0));
					player.setConstitution(npc.getConstitution());
					player.sendMessage("<shad=0789de>You transformed into Zeus!");
				} else {
					player.sendMessage("Please wait till your timer is over!");
				}
			}
			break;
		case 20565:
			if(player.getTransform() == 9994) {
				player.setTimer(player.getTimer()+(100*30));
				player.getInventory().delete(20565, 1);

			} else {
				if(player.getTransform() < 1) {
					player.setNpcTransformationId(9994);
					player.setTransform(9994);
					player.setTimer(100 * 30);
					player.getInventory().delete(20565, 1);

					player.getUpdateFlag().flag(Flag.APPEARANCE);

					NPC npc = new NPC(9994, new Position(0, 0));
					player.setConstitution(npc.getConstitution());
					player.sendMessage("<shad=0789de>You transformed into a Defender.");
				} else {
					player.sendMessage("Please wait till your timer is over!");
				}
			}
			break;
		case 20566:
			if(player.getTransform() == 9932) {
				player.setTimer(player.getTimer()+(100*30));
				player.getInventory().delete(20566, 1);

			} else {
				if(player.getTransform() < 1) {
					player.setNpcTransformationId(9932);
					player.setTransform(9932);
					player.setTimer(100 * 30);
					player.getInventory().delete(20566, 1);

					player.getUpdateFlag().flag(Flag.APPEARANCE);

					NPC npc = new NPC(9932, new Position(0, 0));
					player.setConstitution(npc.getConstitution());
					player.sendMessage("<shad=0789de>You transformed into Godzilla.");
				} else {
					player.sendMessage("Please wait till your timer is over!");
				}
			}
			break;
		case 20567:
			if(player.getTransform() == 16) {
				player.setTimer(player.getTimer()+(100*30));
				player.getInventory().delete(20567, 1);

			} else {
				if(player.getTransform() < 1) {
					player.setNpcTransformationId(16);
					player.setTransform(16);
					player.setTimer(100 * 30);
					player.getInventory().delete(20567, 1);

					player.getUpdateFlag().flag(Flag.APPEARANCE);

					NPC npc = new NPC(16, new Position(0, 0));
					player.setConstitution(npc.getConstitution());
					player.sendMessage("<shad=cec9c9>You transformed into Hades!");
				} else {
					player.sendMessage("Please wait till your timer is over!");
				}
			}
			break;
		case 20568:
			if(player.getTransform() == 9903) {
				player.setTimer(player.getTimer()+(100*30));
				player.getInventory().delete(20568, 1);

			} else {
				if(player.getTransform() < 1) {
					player.setNpcTransformationId(9903);
					player.setTransform(9903);
					player.setTimer(100 * 30);
					player.getInventory().delete(20568, 1);

					player.getUpdateFlag().flag(Flag.APPEARANCE);

					NPC npc = new NPC(9903, new Position(0, 0));
					player.setConstitution(npc.getConstitution());
					player.sendMessage("<shad=cec9c9>You transformed into King Kong.");
				} else {
					player.sendMessage("Please wait till your timer is over!");
				}
			}
			break;
		case 20569:
			if(player.getTransform() == 8493) {
				player.setTimer(player.getTimer()+(100*30));
				player.getInventory().delete(20569, 1);

			} else {
				if(player.getTransform() < 1) {
					player.setNpcTransformationId(8493);
					player.setTransform(8493);
					player.setTimer(100 * 30);
					player.getInventory().delete(20569, 1);

					player.getUpdateFlag().flag(Flag.APPEARANCE);

					NPC npc = new NPC(8493, new Position(0, 0));
					player.setConstitution(npc.getConstitution());
					player.sendMessage("<shad=31e421>You transformed into Hulk.");
				} else {
					player.sendMessage("Please wait till your timer is over!");
				}
			}
			break;
		case 20570:
			if(player.getTransform() == 9935) {
				player.setTimer(player.getTimer()+(100*30));
				player.getInventory().delete(20570, 1);

			} else {
				if(player.getTransform() < 1) {
					player.setNpcTransformationId(9935);
					player.setTransform(9935);
					player.setTimer(100 * 30);
					player.getInventory().delete(20570, 1);

					player.getUpdateFlag().flag(Flag.APPEARANCE);

					NPC npc = new NPC(9935, new Position(0, 0));
					player.setConstitution(npc.getConstitution());
					player.sendMessage("<shad=c003b9>You transformed into Purple Wyrm.");
				} else {
					player.sendMessage("Please wait till your timer is over!");
				}
			}
			break;
		case 3576:
			Lootbox.openBox(player);
			break;
		
		case 10025:
			CharmBox.open(player);
			break;

		case 1959:
			TrioBosses.eatPumpkin(player);
			break;
		case 2677:
		case 2678:
		case 2679:
		case 2680:
		case 2681:
		case 2682:
		case 2683:
		case 2684:
		case 2685:
			ClueScrolls.giveHint(player, itemId);
			break;

		case 13195:
			CrypticChest.openBox(player);
			break;
		case 13196:
			BloodBellyChest.open(player);
			break;
		case 10478:
			DiamondChest.openBox(player);
			break;
		case 13197:
			ExoticChest.open(player);
			break;
		case 13198:
			FrozenChest.open(player);
			break;
		case 15420:
			ChristmasEventPresent.open(player);
			break;
			
		// Donation Box
		case 6183:
			DonationBox.open(player);
			break;
		case 18768:
			UltraDonationBox.open(player);
			break;
			
			
			
		case 19901:
			int[] bravekCasketRewards = { 3980, 3912, 15454, 15459, 15464, 15449, 15374, 18768, 3069, 3071, 3981, 5133,
					12434, 12435, 12436, 14012, 14013, 15373, 15372, 15371, 15370, 15369, 15449, 15454, 18782,
					18782, 18782, 18782, 18782, 18782, 18782, 18989, 14808, 19935, 19936, 1050, 1419,
					15426, 1053, 1055, 1057, 1046, 1048, 1044, 1040, 5130, 5130, 19040, 19040, 20000, 20000, 20001,
					20001, 20002, 20002, 6500, 6500, 6500, 6500, 6500, 13239, 13239, 13239, 13239, 13293 };
			if (player.getInventory().contains(19901)) {
				player.getInventory().delete(19901, 1);
				player.getInventory().add(bravekCasketRewards[Misc.getRandom(bravekCasketRewards.length - 1)], 1);
				player.sendMessage("@blu@You open the casket, and get a reward.");
			}
			break;
			
		case 3824:
			VoteMbox.openBox(player);
			break;
		case 6532:
			ShopManager.getShops().get(51).open(player);
			break;
		case 17745:
			ShopManager.getShops().get(131).open(player);
			break;
		case 7691:
			ShopManager.getShops().get(134).open(player);
			break;
		case 7692:
			ShopManager.getShops().get(135).open(player);
			break;
		case 7694:
			ShopManager.getShops().get(136).open(player);
			break;
		case 19935:
			player.getInventory().add(6532, 50);
			break;
		case 19936:
			player.getInventory().add(6532, 100);
			break;
		case 16455:
			player.getInventory().add(6532, 250);
			break;
		case 19938:
			player.getInventory().add(6532, 500);
			break;
		case 5020:
			player.getInventory().add(6532, 1000);
			break;
		case 1464:
			player.getInventory().add(6532, 10);
			break;
			

		case 19122:
			if (player.getInventory().getFreeSlots() < 5) {
				player.sendMessage("@red@You need atleast 5 free inventory slots to open this scroll");
				return;
			}
			player.getInventory().add(5130, 1);
			player.getInventory().add(14012, 1);
			player.getInventory().add(14013, 1);
			player.getInventory().add(15373, 1);
			player.getInventory().add(15243, 1000);
			player.getInventory().delete(19122, 1);
			player.sendMessage("@blu@Thanks for redeeming Merk Stream Scroll, Enjoy the items!");
			break;
			
		case 10835:
			MoneyPouch.depositMoney(player, player.getInventory().getAmount(10835), false);
			break;

		case 4805:
			ChristmasMysteryBox.openBox(player);
			break;
		case 14207:
			player.getInventory().delete(14207, 1);
			player.performAnimation(new Animation(829));
			player.performGraphic(new Graphic(1265));
			TaskManager.submit(new SpecialRegainPotionTask(player));
			player.sendMessage("@red@Enjoy the effect");
			break;
		case 4635:
			InfernalBox.openBox(player);
			break;
		case 6830:
			player.getInventory().delete(6830, 1);
			player.getInventory().add(19727, 1).refreshItems();
			player.getInventory().add(19728, 1).refreshItems();
			player.getInventory().add(19729, 1).refreshItems();
			player.getInventory().add(19730, 1).refreshItems();
			player.getInventory().add(19731, 1).refreshItems();
			player.getInventory().add(19732, 1).refreshItems();
			break;
			
		case 8657:
			if (player.getInventory().getFreeSlots() < 9) {
				player.getPacketSender().sendMessage("You need at least 12 inventory spaces");
				return;
			}
			player.getInventory().delete(8657, 1);
			player.getInventory().add(19938, 3).refreshItems();
			player.getInventory().add(3988, 2).refreshItems();
			player.getInventory().add(15374, 1).refreshItems();
			player.getInventory().add(5170, 1).refreshItems();
			player.getInventory().add(3966, 1).refreshItems();
			player.getInventory().add(3967, 1).refreshItems();
			player.getInventory().add(3968, 1).refreshItems();
			player.getInventory().add(3969, 1).refreshItems();
			player.getInventory().add(3970, 1).refreshItems();
			player.getPacketSender().sendMessage("You Claim the Forgotten set pack! Thank you for the support !");
			break;
			
		case 8658:
			if (player.getInventory().getFreeSlots() < 5) {
				player.getPacketSender().sendMessage("You need at least 5 inventory spaces");
				return;
			}
			player.getInventory().delete(8658, 1);
			player.getInventory().add(19938, 3).refreshItems();
			player.getInventory().add(3988, 2).refreshItems();
			player.getInventory().add(15374, 2).refreshItems();
			player.getInventory().add(773, 1).refreshItems();
			player.getInventory().add(8654, 1).refreshItems();
			player.getPacketSender().sendMessage("You Claim the Imbued package ! Thank you for the support !");
			break;
			
		case 8674:
			if (player.getInventory().getFreeSlots() < 4) {
				player.getPacketSender().sendMessage("You need at least 5 inventory spaces");
				return;
			}
			player.getInventory().delete(8674, 1);
			player.getInventory().add(3988, 3).refreshItems();
			player.getInventory().add(15374, 3).refreshItems();
			player.getInventory().add(13997, 3).refreshItems();
			player.getInventory().add(19935, 3).refreshItems();
			player.getPacketSender().sendMessage("You Claim the Mystery package ! Thank you for the support !");
			break;
			
			
		case 6990:
			if (player.getInventory().getFreeSlots() < 7) {
				player.getPacketSender().sendMessage("You need at least 7 inventory spaces");
				return;
			}
			player.getInventory().delete(6990, 1);
			player.getInventory().add(5219, 1).refreshItems();
			player.getInventory().add(5220, 1).refreshItems();
			player.getInventory().add(5221, 1).refreshItems();
			player.getInventory().add(5222, 1).refreshItems();
			player.getInventory().add(5223, 1).refreshItems();
			player.getInventory().add(5224, 1).refreshItems();
			player.getInventory().add(5225, 1).refreshItems();
			player.getPacketSender().sendMessage("You Claim the Byakuya set Package ! Thank you for the support !");
			break;
			
		case 8673:
			if (player.getInventory().getFreeSlots() < 5) {
				player.getPacketSender().sendMessage("You need at least 5 inventory spaces");
				return;
			}
			player.getInventory().delete(8673, 1);
			player.getInventory().add(8659, 1).refreshItems();
			player.getInventory().add(4741, 1).refreshItems();
			player.getInventory().add(773, 1).refreshItems();
			player.getInventory().add(15566, 1).refreshItems();
			player.getInventory().add(11978, 1).refreshItems();
			player.getPacketSender().sendMessage("You Claim the Pwnlite Investor pack! Thank you for the support !");
			break;
			
		case 14433:

		
		if (player.getInventory().contains(15693)) {
				player.getInventory().delete(15693, 1);
				player.getInventory().delete(14433, 1);
				player.getInventory().add(19871, 1);
				player.getPacketSender().sendMessage("@or2@<shad=1>You fill the vial with the green glaze.");
				} else {	
				player.getPacketSender().sendMessage("@or2@<shad=1>You need a bowl of green glaze to fill the vial.");
				}
		
			break;
			
		case 6309:
			if (player.getInventory().getFreeSlots() < 2) {
				player.getPacketSender().sendMessage("You need at least 2 inventory spaces");
				return;
			}
			player.getInventory().delete(6309, 1);
			player.getInventory().add(17929, 1).refreshItems();
			player.getInventory().add(17928, 1).refreshItems();
			player.getPacketSender().sendMessage("You Claim the Pwnlite crusher weapon pack! Thank you for the support !");
			break;
			
		case 18457:
			if (player.getInventory().getFreeSlots() < 2) {
				player.getPacketSender().sendMessage("You need at least 2 inventory spaces");
				return;
			}
			player.getInventory().delete(18457, 1);
			player.getInventory().add(17926, 1).refreshItems();
			player.getInventory().add(17927, 1).refreshItems();
			player.getPacketSender().sendMessage("You Claim the Bloodlightning weapon pack! Thank you for the support !");
			break;
			
			
		case 18458:
			if (player.getInventory().getFreeSlots() < 2) {
				player.getPacketSender().sendMessage("You need at least 2 inventory spaces");
				return;
			}
			player.getInventory().delete(18458, 1);
			player.getInventory().add(20427, 1).refreshItems();
			player.getInventory().add(20431, 1).refreshItems();
			player.getPacketSender().sendMessage("You Claim the BFG set weapon pack! Thank you for the support !");
			break;
			
		case 18459:
			if (player.getInventory().getFreeSlots() < 9) {
				player.getPacketSender().sendMessage("You need at least 9 inventory spaces");
				return;
			}
			player.getInventory().delete(18459, 1);
			player.getInventory().add(16435, 1).refreshItems();
			player.getInventory().add(16436, 1).refreshItems();
			player.getInventory().add(16463, 1).refreshItems();
			player.getInventory().add(16489, 1).refreshItems();
			player.getInventory().add(16490, 1).refreshItems();
			player.getInventory().add(16491, 1).refreshItems();
			player.getInventory().add(16493, 1).refreshItems();
			player.getInventory().add(16494, 1).refreshItems();
			player.getInventory().add(14036, 1).refreshItems();
			player.getPacketSender().sendMessage("You Claim the Blood set armor pack! Thank you for the support !");
			break;
			
		case 18460:
			if (player.getInventory().getFreeSlots() < 9) {
				player.getPacketSender().sendMessage("You need at least 9 inventory spaces");
				return;
			}
			player.getInventory().delete(18460, 1);
			player.getInventory().add(4060, 1).refreshItems();
			player.getInventory().add(4061, 1).refreshItems();
			player.getInventory().add(4062, 1).refreshItems();
			player.getInventory().add(8654, 1).refreshItems();
			player.getPacketSender().sendMessage("You Claim the Imbued set armor pack! Thank you for the support !");
			break;
			
		case 15003:
			RelaunchBox.open(player);
			break;
		case 15375:
			FiftyFiftyBox.open(player);
			break;
		case 14691:
			PetMysteryBox.open(player);
			break;
		// Clue Scroll
		case 2714:
			ClueScrolls.addClueRewards(player);
			break;
		/*case 6638:
			int[] normalRewards = { 10168,12162, 15374,5266,17745,298,1464,10205,3912};
			int[] rareRewards = { 774,773,15566 };
			player.getWheelOfFortune().open(normalRewards, rareRewards, 6638);
			break;*/


		case 6192:
			if (player.isSmokeTheBongRateActive()) {
				player.sendMessage("@red@You already have DR boosted from smoking the Reefer");
				player.sendMessage("Your drop rate effect will end in: @red@"
						+ (int) QuickUtils.tickToMin(player.getSmokeTheBongTime()) + QuickUtils.getSmokeTheBongPrefix(player));
			} else {
				player.getInventory().delete(itemId, 1);
                player.performAnimation(new Animation(837));
    			player.forceChat("now thats some good shit");
                player.performGraphic(new Graphic(188));
				player.sendMessage("You rip the bong in 1 hit, It boosts your Droprate @red@30% @bla@ for @red@ 1 Hour");
				player.getInventory().add(6191, 1);
				player.incrementSmokeTheBongTime(6000);
				TaskManager.submit(new SmokeTheBongTask(player));
				player.setSmokeTheBongRateActive(true);
			}
			break;
			
		
		
		case 407:
			player.getInventory().delete(407, 1);
			if (RandomUtility.getRandom(3) < 3) {
				player.getInventory().add(409, 1);
			} else if (RandomUtility.getRandom(4) < 4) {
				player.getInventory().add(411, 1);
			} else
				player.getInventory().add(413, 1);
			break;
		case 405:
			player.getInventory().delete(405, 1);
			if (RandomUtility.getRandom(1) < 1) {
				int coins = RandomUtility.getRandom(150);
				player.getInventory().add(10835, coins);
				player.getPacketSender().sendMessage("The casket contained " + coins + " Pwnlite Taxbags!!");
			} else
				player.getPacketSender().sendMessage("The casket was empty.");
			break;
			
		case 15084:
			if(player.getTotalPlayTime() >= 36000000 * 3) {
			Gambling.rollDice(player);
			} else {
				player.sendMessage("@red@You need to play for atleast 30 hours before u can gamble");
				return;
			}
			break;
		case 299:
			//if(player.getTotalPlayTime() >= 36000000 * 3) {
			Gambling.plantSeed(player);
			//} else {
			//	player.sendMessage("@red@You need to play for atleast 50 hours before u can gamble");
			//}
			break;
		case 4155:
			if (player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK) {
				player.getPacketSender().sendInterfaceRemoval();
				player.getPacketSender().sendMessage("Your Enchanted gem will only work if you have a Slayer task.");
				return;
			}
			DialogueManager.start(player, SlayerDialogues.dialogue(player));
			break;
		case 13022:
			if (player.bloodFountain()) {
				DialogueManager.start(player, BloodSlayerDialogues.dialogue(player));
			} else {
				player.getPacketSender().sendMessage("@red@You must offer 20k blood bags to the offering table first");
			}
			break;
		case 11858:
		case 11860:
		case 11862:
		case 11848:
		case 11856:
		case 11850:
		case 11854:
		case 11852:
		case 11846:
		case 11842:
		case 11844:
			if (!player.getClickDelay().elapsed(2000) || !player.getInventory().contains(itemId))
				return;
			if (player.busy()) {
				player.getPacketSender().sendMessage("You cannot open this right now.");
				return;
			}

			int[] items = itemId == 11858 ? new int[] { 10350, 10348, 10346, 10352 }
					: itemId == 11860 ? new int[] { 10334, 10330, 10332, 10336 }
							: itemId == 11862 ? new int[] { 16510, 16511, 16513,16514,16515,16516,16518 }
									: itemId == 11848 ? new int[] { 4716, 4720, 4722, 4718 }
											: itemId == 11856 ? new int[] { 4753, 4757, 4759, 4755 }
													: itemId == 11850 ? new int[] { 4724, 4728, 4730, 4726 }
															: itemId == 11854 ? new int[] { 4745, 4749, 4751, 4747 }
																	: itemId == 11852
																			? new int[] { 4732, 4734, 4736, 4738 }
																			: itemId == 11846
																					? new int[] { 4708, 4712, 4714,
																							4710 }
																					: itemId == 11842
																							? new int[] { 4087, 3140,
																									11335 }
																							: itemId == 11844
																									? new int[] { 4087,
																											14479,
																											11335 }
																									: new int[] {
																											itemId };

			if (player.getInventory().getFreeSlots() < items.length) {
				player.getPacketSender().sendMessage("You do not have enough space in your inventory.");
				return;
			}
			player.getInventory().delete(itemId, 1);
			for (int i : items) {
				player.getInventory().add(i, 1);
			}
			player.getPacketSender().sendMessage("You open the set and find items inside.");
			player.getClickDelay().reset();
			break;
		case 952:
			Digging.dig(player);
			break;
		case 10006:
			// Hunter.getInstance().laySnare(client);
			Hunter.layTrap(player, new SnareTrap(new GameObject(19175, new Position(player.getPosition().getX(),
					player.getPosition().getY(), player.getPosition().getZ())), TrapState.SET, 200, player));
			break;
		case 10008:
			Hunter.layTrap(player, new BoxTrap(new GameObject(19187, new Position(player.getPosition().getX(),
					player.getPosition().getY(), player.getPosition().getZ())), TrapState.SET, 200, player));
			break;
		case 5509:
		case 5510:
		case 5512:
			RunecraftingPouches.fill(player, RunecraftingPouch.forId(itemId));
			break;
		case 292:
			IngridientsBook.readBook(player, 0, false);
			break;
			
		/**
		 * case 6199: int rewards2[][] = { {20072, 7327, 7323, 989, 1959, 3749, 3753,
		 * 10828, 1215, 4587, 4091, 4093, 4095, 4097, 1079, 1127, 1093, 15332, 3024,
		 * 15272, 6685, 1434, 2, 536, 534, 1149, 1305, 1377, 1434, 1615, 3000, 3204,
		 * 2941, 2947, 2503, 15272, 2503, 10499, 6199, 6326, 861, 1163, 1201, 6111, 544,
		 * 6199, 542, 5574, 5575, 5576, 1215, 3105, 13734, 7400, 2572, 11118}, //Common,
		 * 0 {11730, 10025, 6739, 15501, 11133, 15126, 10828, 6199, 3751, 3753, 11884,
		 * 10589, 18782, 6739, 6739, 2577, 2581, 18782, 15332, 15332, 15332, 11732,
		 * 6199, 10564, 6809, 4587, 1249, 3204, 1305, 1377, 1434, 6528, 7158, 4153, 6,
		 * 8, 10, 12, 4675, 6914, 6889, 4716, 4718, 4720, 4722, 4745, 4747, 4749, 4751,
		 * 4708, 4710, 4712, 4714, 4732, 4734, 4736, 4738, 4724, 4726, 4728, 4730, 4151,
		 * 11732, 6585, 2577, 2581, 6737, 11235, 4675, 4087, 1187, 6914, 6889, 3140,
		 * 6916, 6918, 6920, 6922, 6924, 6731, 6735, 6733}, //Uncommon, 1 {18357, 18349,
		 * 3749, 3753, 10828, 1215, 4587, 4091, 4093, 4095, 4097, 1079, 1127,6739,
		 * 15259, 15332, 15126, 11856, 11854, 11852, 11846, 11850, 11732, 11848, 2577,
		 * 2581, 2572, 15501, 18782, 6920, 6922, 11335, 15241, 15243, 6585, 4151, 11696,
		 * 11724, 11726, 11728, 11694, 11718, 11720, 11722, 11700, 11716, 11698, 11730,
		 * 11283, 18349, 18351, 18353, 18355, 18357, 18359, 11484, 2527, 12601, 15486,
		 * 15018, 15019, 15020, 15220} //Rare, 2 }; double numGen = Math.random();
		 * Chances 50% chance of Common Items - cheap gear, high-end consumables 40%
		 * chance of Uncommon Items - various high-end coin-bought gear 10% chance of
		 * Rare Items - Highest-end coin-bought gear, some voting-point/pk-point
		 * equipment
		 * 
		 * int rewardGrade = numGen >= 0.5 ? 0 : numGen >= 0.20 ? 1 : 2; rewardPos =
		 * RandomUtility.getRandom(rewards2[rewardGrade].length-1);
		 * player.getInventory().delete(6199, 1);
		 * player.getInventory().add(rewards2[rewardGrade][rewardPos],
		 * 1).refreshItems(); break;
		 **/
		case 15501:
			player.setLegendary(false);
			player.setMbox1(false);
			player.setMbox2(false);
			player.setMbox3(false);
			player.setMbox4(false);
			player.setMbox5(true);
			player.getMysteryBox().openInterface();
			break;
		case 11882:
			player.getInventory().delete(11882, 1);
			player.getInventory().add(2595, 1).refreshItems();
			player.getInventory().add(2591, 1).refreshItems();
			player.getInventory().add(3473, 1).refreshItems();
			player.getInventory().add(2597, 1).refreshItems();
			break;
		case 18338:
			player.getOwnerGB().setBoxId(itemId);
			player.getOwnerGB().rewards = new int[] {15566, 5197, 6770, 10205, 5266, 10168, 11811, 5205, 19936, 3988, 13999, 16641,
		            13997, 15374, 3912, 19935, 15521, 5170, 6507, 13998};
			player.getOwnerGB().open();
			break;

		case 22003:
			player.getOwnerGB().setBoxId(itemId);
			player.getOwnerGB().rewards = new int[] {22000, 22001, 22002, 22004, 16455, 16455, 16455, 16455, 16455, 16455,
					16455, 16455, 16455, 16455, 16455, 16455, 16455, 16455, 16455, 16455};
			player.getOwnerGB().open();
		break;
		case 5606:
			if (player.currentInstanceAmount >= 1) {
				player.sendMessage("<shad=1>@red@You can't start a new instance until this one ends");
				return;
			}
			new InstanceInterfaceHandler(player).open();
		break;

		case 19626:
			player.getInventory().delete(19626, 1);
			int[] rewards1 = { 
					10835,10835,10835,10835,10835,10835,17750,17750,17750,10835,10835,989,1543,1464 };
			int[] rewardsAmount1 = {
					200,20,50,100,150,250,20,40,50,30,34,1,1,1};
			int rewardPos1 = Misc.getRandom(rewards1.length - 1);
			player.getInventory().add(rewards1[rewardPos1],
					(int) ((rewardsAmount1[rewardPos1] * 0.5) + (Misc.getRandom(rewardsAmount1[rewardPos1]))));
			break;
			
		case 11884:
			player.getInventory().delete(11884, 1);
			player.getInventory().add(2595, 1).refreshItems();
			player.getInventory().add(2591, 1).refreshItems();
			player.getInventory().add(2593, 1).refreshItems();
			player.getInventory().add(2597, 1).refreshItems();
			break;
		case 11906:
			player.getInventory().delete(11906, 1);
			player.getInventory().add(7394, 1).refreshItems();
			player.getInventory().add(7390, 1).refreshItems();
			player.getInventory().add(7386, 1).refreshItems();
			break;
		case 15262:
			if (!player.getClickDelay().elapsed(1000))
				return;
			player.getInventory().delete(15262, 1);
			player.getInventory().add(18016, 10000).refreshItems();
			player.getClickDelay().reset();
			break;
		case 6:
			DwarfMultiCannon.setupCannon(player);
			break;
		}
	}

	public static void secondAction(Player player, Packet packet) {
		@SuppressWarnings("unused")
		int interfaceId = packet.readLEShortA();
		int slot = packet.readLEShort();
		int itemId = packet.readShortA();
		if (slot < 0 || slot > player.getInventory().capacity())
			return;
		if (player.getInventory().getItems()[slot].getId() != itemId)
			return;
		if (SummoningData.isPouch(player, itemId, 2))
			return;
		switch (itemId) {

		

			
			
		case 18411:
			if (player.getInventory().contains(18411)) {
				player.getInventory().delete(18411, 1).add(12852, 1000);
				player.getPacketSender().sendMessage("You have Salvaged The @yel@Legendary Pokemon @bla@ for 1000 Tokens");
			}
			break;
			
			
		case 6500:
			if (player.getCombatBuilder().isAttacking() || player.getCombatBuilder().isBeingAttacked()) {
				player.getPacketSender().sendMessage("You cannot configure this right now.");
				return;
			}
			player.getPacketSender().sendInterfaceRemoval();
			DialogueManager.start(player, 101);
			player.setDialogueActionId(60);
			break;
		case 12926:
			player.getBlowpipeLoading().handleUnloadBlowpipe();
			break;
		case 11724:
			if (player.getInventory().contains(6530)) {
				player.getInventory().delete(6530, 250);
				player.getInventory().add(897, 1);
				World.sendMessage("<img=10> <col=008FB2>[Upgrade]" + player.getUsername()
						+ " has just upgraded his @red@Bandos Chestplate@la@ to tier 1!");
				player.getPacketSender().sendMessage("Your Bandos Items Has been Upgraded");
			} else {
				player.getPacketSender().sendMessage("@red@You Need 250 Upgrade Tokens To Do This");
			}
			break;
		case 11726:
			if (player.getInventory().contains(6530)) {
				player.getInventory().delete(6530, 250);
				player.getInventory().add(894, 1);
				World.sendMessage("<img=10> <col=008FB2>[Upgrade]" + player.getUsername()
						+ " has just upgraded his @red@Bandos Tassets@la@ to tier 1!");
				player.getPacketSender().sendMessage("Your Bandos Items Has been Upgraded");
			} else {
				player.getPacketSender().sendMessage("@red@You Need 250 Upgrade Tokens To Do This");
			}
			break;
		case 894:
			if (player.getInventory().contains(6530)) {
				player.getInventory().delete(6530, 500);
				player.getInventory().add(895, 1);
				World.sendMessage("<img=10> <col=008FB2>[Upgrade]" + player.getUsername()
						+ " has just upgraded his @red@Bandos Chestplate@la@ to tier 2!");
				player.getPacketSender().sendMessage("Your Bandos Items Has been Upgraded");
			} else {
				player.getPacketSender().sendMessage("@red@You Need 500 Upgrade Tokens To Do This");
			}
			break;
		case 895:
			if (player.getInventory().contains(6530)) {
				player.getInventory().delete(6530, 1000);
				player.getInventory().add(896, 1);
				World.sendMessage("<img=10> <col=008FB2>[Upgrade]" + player.getUsername()
						+ " has just upgraded his @red@Bandos Tassets@la@ to tier 3!");
				player.getPacketSender().sendMessage("Your Bandos Items Has been Upgraded");
			} else {
				player.getPacketSender().sendMessage("@red@You Need 1000 Upgrade Tokens To Do This");
			}
			break;
		case 897:
			if (player.getInventory().contains(6530)) {
				player.getInventory().delete(6530, 500);
				player.getInventory().add(898, 1);
				World.sendMessage("<img=10> <col=008FB2>[Upgrade]" + player.getUsername()
						+ " has just upgraded his @red@Bandos Chestplate@la@ to tier 2!");
				player.getPacketSender().sendMessage("Your Bandos Items Has been Upgraded");
			} else {
				player.getPacketSender().sendMessage("@red@You Need 500 Upgrade Tokens To Do This");
			}
			break;
		case 898:
			if (player.getInventory().contains(6530)) {
				player.getInventory().delete(6530, 1000);
				player.getInventory().add(899, 1);
				World.sendMessage("<img=10> <col=008FB2>[Upgrade]" + player.getUsername()
						+ " has just upgraded his @red@Bandos Chestplate@la@ to tier 3!");
				player.getPacketSender().sendMessage("Your Bandos Items Has been Upgraded");
			} else {
				player.getPacketSender().sendMessage("@red@You Need 1000 Upgrade Tokens To Do This");
			}
			break;
			
			

		case 1712:
		case 1710:
		case 1708:
		case 1706:
		case 11118:
		case 11120:
		case 11122:
		case 11124:
			JewelryTeleporting.rub(player, itemId);
			break;
		case 1704:
			player.getPacketSender().sendMessage("Your amulet has run out of charges.");
			break;
		case 11126:
			player.getPacketSender().sendMessage("Your bracelet has run out of charges.");
			break;
		case 13281:
		case 13282:
		case 13283:
		case 13284:
		case 13285:
		case 13286:
		case 13287:
		case 13288:
			player.getSlayer().handleSlayerRingTP(itemId);
			break;
		case 5509:
		case 5510:
		case 5512:
			RunecraftingPouches.check(player, RunecraftingPouch.forId(itemId));
			break;

		case 1438:
		case 1448:
		case 1440:
		case 1442:
		case 1444:
		case 1446:
		case 1454:
		case 1452:
		case 1462:
		case 1458:
		case 1456:
		case 1450:
			Runecrafting.handleTalisman(player, itemId);
			break;
		}
	}

	public void thirdClickAction(Player player, Packet packet) {
		int itemId = packet.readShortA();
		int slot = packet.readLEShortA();
		@SuppressWarnings("unused")
		int interfaceId = packet.readLEShortA();
		if (slot < 0 || slot > player.getInventory().capacity())
			return;
		if (player.getInventory().getItems()[slot].getId() != itemId)
			return;
		if (JarData.forJar(itemId) != null) {
			PuroPuro.lootJar(player, new Item(itemId, 1), JarData.forJar(itemId));
			return;
		}
		if (SummoningData.isPouch(player, itemId, 3)) {
			return;
		}
		if (ItemBinding.isBindable(itemId)) {
			ItemBinding.bindItem(player, itemId);
			return;
		}
		int amount;
		switch (itemId) {
		
			
		case 14019:
		case 14022:
			player.setCurrentCape(itemId);
			int[] colors = itemId == 14019 ? player.getMaxCapeColors() : player.getCompCapeColors();
			String[] join = new String[colors.length];
			for (int i = 0; i < colors.length; i++) {
				join[i] = Integer.toString(colors[i]);
			}
			player.getPacketSender().sendString(60000, "[CUSTOMIZATION]" + itemId + "," + String.join(",", join));
			player.getPacketSender().sendInterface(60000);
			break;

		/*
		 * case 1050: player.setCurrentHat(itemId); int[] santaColors =
		 * player.getSantaColors(); String[] santaJoin = new String[santaColors.length];
		 * for(int i = 0; i < santaColors.length; i++) { santaJoin[i] =
		 * Integer.toString(santaColors[i]); }
		 * 
		 * player.getPacketSender().sendInterface(58000);
		 * System.out.println("Current santa colors: " +
		 * Arrays.toString(player.getSantaColors())); break;
		 */

		case 12926:
			player.getBlowpipeLoading().handleCheckBlowpipe();
			break;
		case 15369:
			player.setMbox1(true);
			player.setMbox2(false);
			player.setMbox3(false);
			player.setMbox4(false);
			player.setMbox5(false);
			player.setLegendary(false);
			player.getMysteryBox().openInterface();
			break;
		case 15370:
			player.setMbox1(false);
			player.setMbox2(true);
			player.setMbox3(false);
			player.setMbox4(false);
			player.setMbox5(false);
			player.setLegendary(false);
			player.getMysteryBox().openInterface();
			break;
		case 15371:
			player.setMbox1(false);
			player.setMbox2(false);
			player.setMbox3(true);
			player.setMbox4(false);
			player.setMbox5(false);
			player.setLegendary(false);
			player.getMysteryBox().openInterface();
			break;
		case 15372:
			player.setMbox1(false);
			player.setMbox2(false);
			player.setMbox3(false);
			player.setMbox4(true);
			player.setMbox5(false);
			player.setLegendary(false);
			player.getMysteryBox().openInterface();
			break;
		case 15373:
			player.setLegendary(true);
			player.setMbox1(false);
			player.setMbox2(false);
			player.setMbox3(false);
			player.setMbox4(false);
			player.setMbox5(false);
			player.getMysteryBox().openInterface();
			break;
		case 19670:
			if (player.busy()) {
				player.getPacketSender().sendMessage("You can not do this right now.");
				return;
			}
			player.setDialogueActionId(71);
			DialogueManager.start(player, player.getGameMode() == GameMode.NORMAL ? 108 : 109);
			break;
		case 6500:
			CharmingImp.sendConfig(player);
			break;
		case 4155:
			player.getPacketSender().sendInterfaceRemoval();
			DialogueManager.start(player, 103);
			player.setDialogueActionId(65);
			break;
		case 13022:
			player.getPacketSender().sendInterfaceRemoval();
			DialogueManager.start(player, 103);
			player.setDialogueActionId(65);
			break;
		case 13281:
		case 13282:
		case 13283:
		case 13284:
		case 13285:
		case 13286:
		case 13287:
		case 13288:
			player.getPacketSender().sendInterfaceRemoval();
			player.getPacketSender().sendMessage(player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK
					? ("You do not have a Slayer task.")
					: ("Your current task is to kill another " + (player.getSlayer().getAmountToSlay()) + " "
							+ Misc.formatText(
									player.getSlayer().getSlayerTask().toString().toLowerCase().replaceAll("_", " "))
							+ "s."));
			break;
			
		//case 6639:
		//	player.getInventory().delete(itemId, 1);
			//player.getMinimeSystem().despawn();
			//break;
			
			//blood slavaging
		case 8476:
			if (player.getInventory().contains(8476)) {
				player.getInventory().delete(8476, 1).add(17750, 1500);
				player.getPacketSender().sendMessage("You have Salvaged The Item for@red@ 1500 Bloodbags");
			}
			break;
		case 8477:
			if (player.getInventory().contains(8477)) {
				player.getInventory().delete(8477, 1).add(17750, 1500);
				player.getPacketSender().sendMessage("You have Salvaged The Item for@red@ 1500 Bloodbags");
			}
			break;
		case 8478:
			if (player.getInventory().contains(8478)) {
				player.getInventory().delete(8478, 1).add(17750, 1500);
				player.getPacketSender().sendMessage("You have Salvaged The Item for@red@ 1500 Bloodbags");
			}
			break;
		case 8479:
			if (player.getInventory().contains(8479)) {
				player.getInventory().delete(8479, 1).add(17750, 1500);
				player.getPacketSender().sendMessage("You have Salvaged The Item for@red@ 1500 Bloodbags");
			}
			break;
		case 8480:
			if (player.getInventory().contains(8480)) {
				player.getInventory().delete(8480, 1).add(17750, 1500);
				player.getPacketSender().sendMessage("You have Salvaged The Item for@red@ 1500 Bloodbags");
			}
			break;
		case 8481:
			if (player.getInventory().contains(8481)) {
				player.getInventory().delete(8481, 1).add(17750, 1500);
				player.getPacketSender().sendMessage("You have Salvaged The Item for@red@ 1500 Bloodbags");
			}
			break;
			//end blood salvaging
			
			//Start salvaging
		case 19468:
			if (player.getInventory().contains(19468)) {
				player.getInventory().delete(19468, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for@red@ 10 Tokens");
			}
			break;
		case 18381:
			if (player.getInventory().contains(18381)) {
				player.getInventory().delete(18381, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for@red@ 10 Tokens");
			}
			break;
		case 18392:
			if (player.getInventory().contains(18392)) {
				player.getInventory().delete(18392, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for@red@ 10 Tokens");
			}
			break;
		case 18933:
			if (player.getInventory().contains(18933)) {
				player.getInventory().delete(18933, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for@red@ 10 Tokens");
			}
			break;
		case 19958:
			if (player.getInventory().contains(19958)) {
				player.getInventory().delete(19958, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for@red@ 50 Tokens");
			}
			break;
		case 15026:
			if (player.getInventory().contains(15026)) {
				player.getInventory().delete(15026, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for@red@ 20 Tokens");
			}
			break;
		case 4794:
			if (player.getInventory().contains(4794)) {
				player.getInventory().delete(4794, 1).add(12852, 75);
				player.getPacketSender().sendMessage("You have Salvaged The Item for@red@ 75 Tokens");
			}
			break;
		case 4795:
			if (player.getInventory().contains(4795)) {
				player.getInventory().delete(4795, 1).add(12852, 75);
				player.getPacketSender().sendMessage("You have Salvaged The Item for@red@ 75 Tokens");
			}
			break;
		case 4796:
			if (player.getInventory().contains(4796)) {
				player.getInventory().delete(4796, 1).add(12852, 75);
				player.getPacketSender().sendMessage("You have Salvaged The Item for@red@ 75 Tokens");
			}
			break;
		case 19165:
			if (player.getInventory().contains(19165)) {
				player.getInventory().delete(19165, 1).add(12852, 75);
				player.getPacketSender().sendMessage("You have Salvaged The Item for@red@ 75 Tokens");
			}
			break;
			
		case 4797:
			if (player.getInventory().contains(4797)) {
				player.getInventory().delete(4797, 1).add(12852, 75);
				player.getPacketSender().sendMessage("You have Salvaged The Item for@red@ 75 Tokens");
			}
			break;
		case 19127:
			if (player.getInventory().contains(19127)) {
				player.getInventory().delete(19127, 1).add(12852, 75);
				player.getPacketSender().sendMessage("You have Salvaged The Item for@red@ 75 Tokens");
			}
			break;
		case 19128:
			if (player.getInventory().contains(19128)) {
				player.getInventory().delete(19128, 1).add(12852, 75);
				player.getPacketSender().sendMessage("You have Salvaged The Item for@red@ 75 Tokens");
			}
			break;
		case 19129:
			if (player.getInventory().contains(19129)) {
				player.getInventory().delete(19129, 1).add(12852, 75);
				player.getPacketSender().sendMessage("You have Salvaged The Item for@red@ 75 Tokens");
			}
			break;
		case 2749:
			if (player.getInventory().contains(2749)) {
				player.getInventory().delete(2749, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 18363:
			if (player.getInventory().contains(18363)) {
				player.getInventory().delete(18363, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 18380:
			if (player.getInventory().contains(18380)) {
				player.getInventory().delete(18380, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 12434:
			if (player.getInventory().contains(12434)) {
				player.getInventory().delete(12434, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 12435:
			if (player.getInventory().contains(12435)) {
				player.getInventory().delete(12435, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 12436:
			if (player.getInventory().contains(12436)) {
				player.getInventory().delete(12436, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 17933:
			if (player.getInventory().contains(17933)) {
				player.getInventory().delete(17933, 1).add(12852, 85);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@85 Tokens");
			}
			break;
		case 17932:
			if (player.getInventory().contains(17932)) {
				player.getInventory().delete(17932, 1).add(12852, 85);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@85 Tokens");
			}
			break;
		case 18382:
			if (player.getInventory().contains(18382)) {
				player.getInventory().delete(18382, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 18383:
			if (player.getInventory().contains(18383)) {
				player.getInventory().delete(18383, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 18384:
			if (player.getInventory().contains(18384)) {
				player.getInventory().delete(18384, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 18402:
			if (player.getInventory().contains(18402)) {
				player.getInventory().delete(18402, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 15398:
			if (player.getInventory().contains(15398)) {
				player.getInventory().delete(15398, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 2750:
			if (player.getInventory().contains(2750)) {
				player.getInventory().delete(2750, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 6491:
			if (player.getInventory().contains(6491)) {
				player.getInventory().delete(6491, 1).add(12852, 125);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@125 Tokens");
			}
			break;
		case 16137:
			if (player.getInventory().contains(16137)) {
				player.getInventory().delete(16137, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 6492:
			if (player.getInventory().contains(6492)) {
				player.getInventory().delete(6492, 1).add(12852, 125);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@125 Tokens");
			}
			break;
		case 6493:
			if (player.getInventory().contains(6493)) {
				player.getInventory().delete(6493, 1).add(12852, 125);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@125 Tokens");
			}
			break;
		case 6494:
			if (player.getInventory().contains(6494)) {
				player.getInventory().delete(6494, 1).add(12852, 125);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@125 Tokens");
			}
			break;
		case 6495:
			if (player.getInventory().contains(6495)) {
				player.getInventory().delete(6495, 1).add(12852, 125);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@125 Tokens");
			}
			break;
		case 6934:
			if (player.getInventory().contains(6934)) {
				player.getInventory().delete(6934, 1).add(12852, 125);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@125 Tokens");
			}
			break;
		case 2751:
			if (player.getInventory().contains(2751)) {
				player.getInventory().delete(2751, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 2752:
			if (player.getInventory().contains(2752)) {
				player.getInventory().delete(2752, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
			
		case 2753:
			if (player.getInventory().contains(2753)) {
				player.getInventory().delete(2753, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
			
		case 2754:
			if (player.getInventory().contains(2754)) {
				player.getInventory().delete(2754, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 13261:
			if (player.getInventory().contains(13261)) {
				player.getInventory().delete(13261, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 19721:
			if (player.getInventory().contains(19721)) {
				player.getInventory().delete(19721, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 18892:
			if (player.getInventory().contains(18892)) {
				player.getInventory().delete(18892, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 19722:
			if (player.getInventory().contains(19722)) {
				player.getInventory().delete(19722, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 19723:
			if (player.getInventory().contains(19723)) {
				player.getInventory().delete(19723, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 19724:
			if (player.getInventory().contains(19724)) {
				player.getInventory().delete(19724, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 15418:
			if (player.getInventory().contains(15418)) {
				player.getInventory().delete(15418, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 19725:
			if (player.getInventory().contains(19725)) {
				player.getInventory().delete(19725, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 19734:
			if (player.getInventory().contains(19734)) {
				player.getInventory().delete(19734, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 19736:
			if (player.getInventory().contains(19736)) {
				player.getInventory().delete(19736, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 1499:
			if (player.getInventory().contains(1499)) {
				player.getInventory().delete(1499, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 3973:
			if (player.getInventory().contains(3973)) {
				player.getInventory().delete(3973, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 4799:
			if (player.getInventory().contains(4799)) {
				player.getInventory().delete(4799, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 4800:
			if (player.getInventory().contains(4800)) {
				player.getInventory().delete(4800, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 4801:
			if (player.getInventory().contains(4801)) {
				player.getInventory().delete(4801, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 5079:
			if (player.getInventory().contains(5079)) {
				player.getInventory().delete(5079, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 3951:
			if (player.getInventory().contains(3951)) {
				player.getInventory().delete(3951, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 3316:
			if (player.getInventory().contains(3316)) {
				player.getInventory().delete(3316, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 3931:
			if (player.getInventory().contains(3931)) {
				player.getInventory().delete(3931, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 3958:
			if (player.getInventory().contains(3958)) {
				player.getInventory().delete(3958, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 3959:
			if (player.getInventory().contains(3959)) {
				player.getInventory().delete(3959, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 3960:
			if (player.getInventory().contains(3960)) {
				player.getInventory().delete(3960, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 5186:
			if (player.getInventory().contains(5186)) {
				player.getInventory().delete(5186, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 5187:
			if (player.getInventory().contains(5187)) {
				player.getInventory().delete(5187, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 14559:
			if (player.getInventory().contains(14559)) {
				player.getInventory().delete(14559, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 6583:
			if (player.getInventory().contains(6583)) {
				player.getInventory().delete(6583, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 12426:
			if (player.getInventory().contains(12426)) {
				player.getInventory().delete(12426, 1).add(12852, 35);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@35 Tokens");
			}
			break;
		case 3277:
			if (player.getInventory().contains(3277)) {
				player.getInventory().delete(3277, 1).add(12852, 75);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@75 Tokens");
			}
			break;
		case 7617:
			if (player.getInventory().contains(7617)) {
				player.getInventory().delete(7617, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 936:
			if (player.getInventory().contains(936)) {
				player.getInventory().delete(936, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 18751:
			if (player.getInventory().contains(18751)) {
				player.getInventory().delete(18751, 1).add(12852, 15);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@15 Tokens");
			}
			break;
		case 14440:
			if (player.getInventory().contains(14440)) {
				player.getInventory().delete(14440, 1).add(12852, 100);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@100 Tokens");
			}
			break;
		case 14441:
			if (player.getInventory().contains(14441)) {
				player.getInventory().delete(14441, 1).add(12852, 100);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@100 Tokens");
			}
			break;
		case 14442:
			if (player.getInventory().contains(14442)) {
				player.getInventory().delete(14442, 1).add(12852, 100);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@100 Tokens");
			}
			break;
		case 14437:
			if (player.getInventory().contains(14437)) {
				player.getInventory().delete(14437, 1).add(12852, 100);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@100 Tokens");
			}
			break;
		case 14438:
			if (player.getInventory().contains(14438)) {
				player.getInventory().delete(14438, 1).add(12852, 100);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@100 Tokens");
			}
			break;
		case 14439:
			if (player.getInventory().contains(14439)) {
				player.getInventory().delete(14439, 1).add(12852, 100);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@100 Tokens");
			}
			break;
		case 4769:
			if (player.getInventory().contains(4769)) {
				player.getInventory().delete(4769, 1).add(12852, 15);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@15 Tokens");
			}
			break;
		case 934:
			if (player.getInventory().contains(934)) {
				player.getInventory().delete(934, 1).add(12852, 65);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@65 Tokens");
			}
			break;
		case 937:
			if (player.getInventory().contains(937)) {
				player.getInventory().delete(937, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 19140:
			if (player.getInventory().contains(19140)) {
				player.getInventory().delete(19140, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 938:
			if (player.getInventory().contains(938)) {
				player.getInventory().delete(938, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 939:
			if (player.getInventory().contains(939)) {
				player.getInventory().delete(939, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 14560:
			if (player.getInventory().contains(14560)) {
				player.getInventory().delete(14560, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 14561:
			if (player.getInventory().contains(14561)) {
				player.getInventory().delete(14561, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 14562:
			if (player.getInventory().contains(14562)) {
				player.getInventory().delete(14562, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 4770:
			if (player.getInventory().contains(4770)) {
				player.getInventory().delete(4770, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 9505:
			if (player.getInventory().contains(9505)) {
				player.getInventory().delete(9505, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 9506:
			if (player.getInventory().contains(9506)) {
				player.getInventory().delete(9506, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 9507:
			if (player.getInventory().contains(9507)) {
				player.getInventory().delete(9507, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 5131:
			if (player.getInventory().contains(5131)) {
				player.getInventory().delete(5131, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 18347:
			if (player.getInventory().contains(18347)) {
				player.getInventory().delete(18347, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 13239:
			if (player.getInventory().contains(13239)) {
				player.getInventory().delete(13239, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 13235:
			if (player.getInventory().contains(13235)) {
				player.getInventory().delete(13235, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 15019:
			if (player.getInventory().contains(15019)) {
				player.getInventory().delete(15019, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 15220:
			if (player.getInventory().contains(15220)) {
				player.getInventory().delete(15220, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 15018:
			if (player.getInventory().contains(15018)) {
				player.getInventory().delete(15018, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 15020:
			if (player.getInventory().contains(15020)) {
				player.getInventory().delete(15020, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 12708:
			if (player.getInventory().contains(12708)) {
				player.getInventory().delete(12708, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 4771:
			if (player.getInventory().contains(4771)) {
				player.getInventory().delete(4771, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 4772:
			if (player.getInventory().contains(4772)) {
				player.getInventory().delete(4772, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 6193:
			if (player.getInventory().contains(6193)) {
				player.getInventory().delete(6193, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 11148:
			if (player.getInventory().contains(11148)) {
				player.getInventory().delete(11148, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 11149:
			if (player.getInventory().contains(11149)) {
				player.getInventory().delete(11149, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 11150:
			if (player.getInventory().contains(11150)) {
				player.getInventory().delete(11150, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 11160:
			if (player.getInventory().contains(11160)) {
				player.getInventory().delete(11160, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 11161:
			if (player.getInventory().contains(11161)) {
				player.getInventory().delete(11161, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 6194:
			if (player.getInventory().contains(6194)) {
				player.getInventory().delete(6194, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 6195:
			if (player.getInventory().contains(6195)) {
				player.getInventory().delete(6195, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 6196:
			if (player.getInventory().contains(6196)) {
				player.getInventory().delete(6196, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 3271:
			if (player.getInventory().contains(3271)) {
				player.getInventory().delete(3271, 1).add(12852, 25);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@25 Tokens");
			}
			break;
		case 3272:
			if (player.getInventory().contains(3272)) {
				player.getInventory().delete(3272, 1).add(12852, 25);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@25 Tokens");
			}
			break;
		case 3273:
			if (player.getInventory().contains(3273)) {
				player.getInventory().delete(3273, 1).add(12852, 25);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@25 Tokens");
			}
			break;
		case 3283:
			if (player.getInventory().contains(3283)) {
				player.getInventory().delete(3283, 1).add(12852, 25);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@25 Tokens");
			}
			break;
		case 3284:
			if (player.getInventory().contains(3284)) {
				player.getInventory().delete(3284, 1).add(12852, 25);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@25 Tokens");
			}
			break;
		case 3285:
			if (player.getInventory().contains(3285)) {
				player.getInventory().delete(3285, 1).add(12852, 25);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@25 Tokens");
			}
			break;
		case 3276:
			if (player.getInventory().contains(3276)) {
				player.getInventory().delete(3276, 1).add(12852, 25);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@25 Tokens");
			}
			break;
		case 6197:
			if (player.getInventory().contains(6197)) {
				player.getInventory().delete(6197, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 6198:
			if (player.getInventory().contains(6198)) {
				player.getInventory().delete(6198, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 3994:
			if (player.getInventory().contains(6198)) {
				player.getInventory().delete(6198, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 3995:
			if (player.getInventory().contains(6198)) {
				player.getInventory().delete(6198, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 3996:
			if (player.getInventory().contains(6198)) {
				player.getInventory().delete(6198, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 3907:
			if (player.getInventory().contains(3907)) {
				player.getInventory().delete(3907, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 3908:
			if (player.getInventory().contains(3908)) {
				player.getInventory().delete(3908, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 3909:
			if (player.getInventory().contains(3909)) {
				player.getInventory().delete(3909, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 923:
			if (player.getInventory().contains(923)) {
				player.getInventory().delete(923, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 3910:
			if (player.getInventory().contains(3910)) {
				player.getInventory().delete(3910, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 12605:
			if (player.getInventory().contains(12605)) {
				player.getInventory().delete(12605, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 19720:
			if (player.getInventory().contains(19720)) {
				player.getInventory().delete(19720, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 19004:
			if (player.getInventory().contains(19004)) {
				player.getInventory().delete(19004, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 16549:
			if (player.getInventory().contains(16549)) {
				player.getInventory().delete(16549, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 16550:
			if (player.getInventory().contains(16550)) {
				player.getInventory().delete(16550, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 16551:
			if (player.getInventory().contains(16551)) {
				player.getInventory().delete(16551, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 16553:
			if (player.getInventory().contains(16553)) {
				player.getInventory().delete(16553, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 16556:
			if (player.getInventory().contains(16556)) {
				player.getInventory().delete(16556, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 16558:
			if (player.getInventory().contains(16558)) {
				player.getInventory().delete(16558, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 5198:
			if (player.getInventory().contains(5198)) {
				player.getInventory().delete(5198, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 5199:
			if (player.getInventory().contains(5199)) {
				player.getInventory().delete(5199, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 5200:
			if (player.getInventory().contains(5200)) {
				player.getInventory().delete(5200, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 3980:
			if (player.getInventory().contains(3980)) {
				player.getInventory().delete(3980, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 3999:
			if (player.getInventory().contains(3999)) {
				player.getInventory().delete(3999, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 4000:
			if (player.getInventory().contains(4000)) {
				player.getInventory().delete(4000, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 4001:
			if (player.getInventory().contains(4001)) {
				player.getInventory().delete(4001, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 18955:
			if (player.getInventory().contains(18955)) {
				player.getInventory().delete(18955, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 18956:
			if (player.getInventory().contains(18956)) {
				player.getInventory().delete(18956, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 18957:
			if (player.getInventory().contains(18957)) {
				player.getInventory().delete(18957, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 5167:
			if (player.getInventory().contains(5167)) {
				player.getInventory().delete(5167, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 15649:
			if (player.getInventory().contains(15649)) {
				player.getInventory().delete(15649, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 15650:
			if (player.getInventory().contains(15650)) {
				player.getInventory().delete(15650, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 15651:
			if (player.getInventory().contains(15651)) {
				player.getInventory().delete(15651, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 15652:
			if (player.getInventory().contains(15652)) {
				player.getInventory().delete(15652, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 15653:
			if (player.getInventory().contains(15653)) {
				player.getInventory().delete(15653, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 15654:
			if (player.getInventory().contains(15654)) {
				player.getInventory().delete(15654, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 15655:
			if (player.getInventory().contains(15655)) {
				player.getInventory().delete(15655, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 4761:
			if (player.getInventory().contains(4761)) {
				player.getInventory().delete(4761, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 18894:
			if (player.getInventory().contains(18894)) {
				player.getInventory().delete(18894, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 4762:
			if (player.getInventory().contains(4762)) {
				player.getInventory().delete(4762, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 4763:
			if (player.getInventory().contains(4763)) {
				player.getInventory().delete(4763, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 4764:
			if (player.getInventory().contains(4764)) {
				player.getInventory().delete(4764, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 4765:
			if (player.getInventory().contains(4765)) {
				player.getInventory().delete(4765, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 5089:
			if (player.getInventory().contains(5089)) {
				player.getInventory().delete(5089, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 3905:
			if (player.getInventory().contains(3905)) {
				player.getInventory().delete(3905, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 3820:
			if (player.getInventory().contains(3820)) {
				player.getInventory().delete(3820, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 19091:
			if (player.getInventory().contains(19091)) {
				player.getInventory().delete(19091, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 19090:
			if (player.getInventory().contains(19090)) {
				player.getInventory().delete(19090, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 19088:
			if (player.getInventory().contains(19088)) {
				player.getInventory().delete(19088, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 3821:
			if (player.getInventory().contains(3821)) {
				player.getInventory().delete(3821, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
			
		case 13271:
			if (player.getInventory().contains(13271)) {
				player.getInventory().delete(13271, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
			
		case 13272:
			if (player.getInventory().contains(13272)) {
				player.getInventory().delete(13272, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
			
		case 13273:
			if (player.getInventory().contains(13273)) {
				player.getInventory().delete(13273, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 13274:
			if (player.getInventory().contains(13274)) {
				player.getInventory().delete(13274, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 13275:
			if (player.getInventory().contains(13275)) {
				player.getInventory().delete(13275, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 13594:
			if (player.getInventory().contains(13594)) {
				player.getInventory().delete(13594, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 13595:
			if (player.getInventory().contains(13595)) {
				player.getInventory().delete(13595, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 13596:
			if (player.getInventory().contains(13596)) {
				player.getInventory().delete(13596, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 13597:
			if (player.getInventory().contains(13597)) {
				player.getInventory().delete(13597, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 13664:
			if (player.getInventory().contains(13664)) {
				player.getInventory().delete(13664, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 4643:
			if (player.getInventory().contains(4643)) {
				player.getInventory().delete(4643, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 3983:
			if (player.getInventory().contains(3983)) {
				player.getInventory().delete(3983, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 3984:
			if (player.getInventory().contains(3984)) {
				player.getInventory().delete(3984, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 9484:
			if (player.getInventory().contains(9484)) {
				player.getInventory().delete(9484, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 9485:
			if (player.getInventory().contains(9485)) {
				player.getInventory().delete(9485, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 9486:
			if (player.getInventory().contains(9486)) {
				player.getInventory().delete(9486, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 9487:
			if (player.getInventory().contains(9487)) {
				player.getInventory().delete(9487, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 9488:
			if (player.getInventory().contains(9488)) {
				player.getInventory().delete(9488, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 9489:
			if (player.getInventory().contains(9489)) {
				player.getInventory().delete(9489, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 9490:
			if (player.getInventory().contains(9490)) {
				player.getInventory().delete(9490, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 9491:
			if (player.getInventory().contains(9491)) {
				player.getInventory().delete(9491, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 9481:
			if (player.getInventory().contains(9481)) {
				player.getInventory().delete(9481, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 9482:
			if (player.getInventory().contains(9482)) {
				player.getInventory().delete(9482, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 9483:
			if (player.getInventory().contains(9483)) {
				player.getInventory().delete(9483, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 3822:
			if (player.getInventory().contains(3822)) {
				player.getInventory().delete(3822, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 20054:
			if (player.getInventory().contains(20054)) {
				player.getInventory().delete(20054, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 5173:
			if (player.getInventory().contains(5173)) {
				player.getInventory().delete(5173, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 4785:
			if (player.getInventory().contains(4785)) {
				player.getInventory().delete(4785, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 5195:
			if (player.getInventory().contains(5195)) {
				player.getInventory().delete(5195, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 15032:
			if (player.getInventory().contains(15032)) {
				player.getInventory().delete(15032, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 19087:
			if (player.getInventory().contains(19087)) {
				player.getInventory().delete(19087, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 19089:
			if (player.getInventory().contains(19089)) {
				player.getInventory().delete(19089, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 3282:
			if (player.getInventory().contains(3282)) {
				player.getInventory().delete(3282, 1).add(12852, 300);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@300 Tokens");
			}
			break;
		case 15660:
			if (player.getInventory().contains(15660)) {
				player.getInventory().delete(15660, 1).add(12852, 75);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@75 Tokens");
			}
			break;
		case 14546:
			if (player.getInventory().contains(14546)) {
				player.getInventory().delete(14546, 1).add(12852, 25);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@25 Tokens");
			}
			break;
		case 6509:
			if (player.getInventory().contains(6509)) {
				player.getInventory().delete(6509, 1).add(12852, 25);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@25 Tokens");
			}
			break;
		case 6505:
			if (player.getInventory().contains(6505)) {
				player.getInventory().delete(6505, 1).add(12852, 25);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@25 Tokens");
			}
			break;
		case 14547:
			if (player.getInventory().contains(14547)) {
				player.getInventory().delete(14547, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 6510:
			if (player.getInventory().contains(6510)) {
				player.getInventory().delete(6510, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 6506:
			if (player.getInventory().contains(6506)) {
				player.getInventory().delete(6506, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 6508:
			if (player.getInventory().contains(6508)) {
				player.getInventory().delete(6508, 1).add(12852, 75);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@75 Tokens");
			}
			break;
		case 6511:
			if (player.getInventory().contains(6511)) {
				player.getInventory().delete(6511, 1).add(12852, 75);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@75 Tokens");
			}
			break;
		case 14548:
			if (player.getInventory().contains(14548)) {
				player.getInventory().delete(14548, 1).add(12852, 75);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@75 Tokens");
			}
			break;
		case 4781:
			if (player.getInventory().contains(4781)) {
				player.getInventory().delete(4781, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
			
		case 4782:
			if (player.getInventory().contains(4782)) {
				player.getInventory().delete(4782, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 20240:
			if (player.getInventory().contains(20240)) {
				player.getInventory().delete(20240, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 3985:
			if (player.getInventory().contains(3985)) {
				player.getInventory().delete(3985, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 5082:
			if (player.getInventory().contains(5082)) {
				player.getInventory().delete(5082, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 5083:
			if (player.getInventory().contains(5083)) {
				player.getInventory().delete(5083, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 5084:
			if (player.getInventory().contains(5084)) {
				player.getInventory().delete(5084, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 15656:
			if (player.getInventory().contains(15656)) {
				player.getInventory().delete(15656, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 17151:
			if (player.getInventory().contains(17151)) {
				player.getInventory().delete(17151, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 3914:
			if (player.getInventory().contains(3914)) {
				player.getInventory().delete(3914, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 4641:
			if (player.getInventory().contains(4641)) {
				player.getInventory().delete(4641, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 4642:
			if (player.getInventory().contains(4642)) {
				player.getInventory().delete(4642, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 5129:
			if (player.getInventory().contains(5129)) {
				player.getInventory().delete(5129, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 19470:
			if (player.getInventory().contains(19470)) {
				player.getInventory().delete(19470, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 19471:
			if (player.getInventory().contains(19471)) {
				player.getInventory().delete(19471, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 19472:
			if (player.getInventory().contains(19472)) {
				player.getInventory().delete(19472, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 19473:
			if (player.getInventory().contains(19473)) {
				player.getInventory().delete(19473, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 19474:
			if (player.getInventory().contains(19474)) {
				player.getInventory().delete(19474, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 19619:
			if (player.getInventory().contains(19619)) {
				player.getInventory().delete(19619, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 19618:
			if (player.getInventory().contains(19618)) {
				player.getInventory().delete(19618, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 19620:
			if (player.getInventory().contains(19620)) {
				player.getInventory().delete(19620, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 19691:
			if (player.getInventory().contains(19691)) {
				player.getInventory().delete(19691, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 19821:
			if (player.getInventory().contains(19821)) {
				player.getInventory().delete(19821, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 3317:
			if (player.getInventory().contains(3317)) {
				player.getInventory().delete(3317, 1).add(12852, 40);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@40 Tokens");
			}
			break;
		case 19692:
			if (player.getInventory().contains(19692)) {
				player.getInventory().delete(19692, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 19693:
			if (player.getInventory().contains(19693)) {
				player.getInventory().delete(19693, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 19694:
			if (player.getInventory().contains(19694)) {
				player.getInventory().delete(19694, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 19695:
			if (player.getInventory().contains(19695)) {
				player.getInventory().delete(19695, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 19696:
			if (player.getInventory().contains(19696)) {
				player.getInventory().delete(19696, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 3648:
			if (player.getInventory().contains(3648)) {
				player.getInventory().delete(3648, 1).add(12852, 60);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@60 Tokens");
			}
			break;
		case 3649:
			if (player.getInventory().contains(3649)) {
				player.getInventory().delete(3649, 1).add(12852, 60);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@60 Tokens");
			}
			break;
		case 3650:
			if (player.getInventory().contains(3650)) {
				player.getInventory().delete(3650, 1).add(12852, 60);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@60 Tokens");
			}
			break;
		case 3651:
			if (player.getInventory().contains(3651)) {
				player.getInventory().delete(3651, 1).add(12852, 60);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@60 Tokens");
			}
			break;
		case 3652:
			if (player.getInventory().contains(3652)) {
				player.getInventory().delete(3652, 1).add(12852, 60);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@60 Tokens");
			}
			break;
		case 3659:
			if (player.getInventory().contains(3659)) {
				player.getInventory().delete(3659, 1).add(12852, 60);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@60 Tokens");
			}
			break;
		case 19159:
			if (player.getInventory().contains(19159)) {
				player.getInventory().delete(19159, 1).add(12852, 60);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@60 Tokens");
			}
			break;
		case 19160:
			if (player.getInventory().contains(19160)) {
				player.getInventory().delete(19160, 1).add(12852, 60);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@60 Tokens");
			}
			break;
		case 19161:
			if (player.getInventory().contains(19161)) {
				player.getInventory().delete(19161, 1).add(12852, 60);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@60 Tokens");
			}
			break;
		case 19162:
			if (player.getInventory().contains(19162)) {
				player.getInventory().delete(19162, 1).add(12852, 60);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@60 Tokens");
			}
			break;
		case 19163:
			if (player.getInventory().contains(19163)) {
				player.getInventory().delete(19163, 1).add(12852, 60);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@60 Tokens");
			}
			break;
		case 19164:
			if (player.getInventory().contains(19164)) {
				player.getInventory().delete(19164, 1).add(12852, 60);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@60 Tokens");
			}
			break;
		case 18931:
			if (player.getInventory().contains(18931)) {
				player.getInventory().delete(18931, 1).add(12852, 60);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@75 Tokens");
			}
			break;
		case 19166:
			if (player.getInventory().contains(19166)) {
				player.getInventory().delete(19166, 1).add(12852, 60);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@60 Tokens");
			}
			break;
		case 9492:
			if (player.getInventory().contains(9492)) {
				player.getInventory().delete(9492, 1).add(12852, 60);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@60 Tokens");
			}
			break;
		case 5210:
			if (player.getInventory().contains(5210)) {
				player.getInventory().delete(5210, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 15045:
			if (player.getInventory().contains(15045)) {
				player.getInventory().delete(15045, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 5211:
			if (player.getInventory().contains(5211)) {
				player.getInventory().delete(5211, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 926:
			if (player.getInventory().contains(926)) {
				player.getInventory().delete(926, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 14453:
			if (player.getInventory().contains(14453)) {
				player.getInventory().delete(14453, 1).add(12852, 15);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@15 Tokens");
			}
			break;
		case 14455:
			if (player.getInventory().contains(14455)) {
				player.getInventory().delete(14455, 1).add(12852, 15);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@15 Tokens");
			}
			break;
		case 4056:
			if (player.getInventory().contains(4056)) {
				player.getInventory().delete(4056, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 4057:
			if (player.getInventory().contains(4057)) {
				player.getInventory().delete(4057, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 4058:
			if (player.getInventory().contains(4058)) {
				player.getInventory().delete(4058, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 18950:
			if (player.getInventory().contains(18950)) {
				player.getInventory().delete(18950, 1).add(12852, 25);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@25 Tokens");
			}
			break;
		case 18748:
			if (player.getInventory().contains(18748)) {
				player.getInventory().delete(18748, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 2545:
			if (player.getInventory().contains(2545)) {
				player.getInventory().delete(2545, 1).add(12852, 15);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@15 Tokens");
			}
			break;
		case 2546:
			if (player.getInventory().contains(2546)) {
				player.getInventory().delete(2546, 1).add(12852, 15);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@15 Tokens");
			}
			break;
		case 2547:
			if (player.getInventory().contains(2547)) {
				player.getInventory().delete(2547, 1).add(12852, 15);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@15 Tokens");
			}
			break;
		case 2548:
			if (player.getInventory().contains(2548)) {
				player.getInventory().delete(2548, 1).add(12852, 15);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@15 Tokens");
			}
			break;
		case 5184:
			if (player.getInventory().contains(5184)) {
				player.getInventory().delete(5184, 1).add(12852, 10);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@10 Tokens");
			}
			break;
		case 4059:
			if (player.getInventory().contains(4059)) {
				player.getInventory().delete(4059, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 19886:
			if (player.getInventory().contains(19886)) {
				player.getInventory().delete(19886, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 19106:
			if (player.getInventory().contains(19106)) {
				player.getInventory().delete(19106, 1).add(12852, 30);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@30 Tokens");
			}
			break;
		case 4780:
			if (player.getInventory().contains(19106)) {
				player.getInventory().delete(19106, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 14457:
			if (player.getInventory().contains(14457)) {
				player.getInventory().delete(14457, 1).add(12852, 15);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@15 Tokens");
			}
			break;
		case 931:
			if (player.getInventory().contains(931)) {
				player.getInventory().delete(931, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 930:
			if (player.getInventory().contains(930)) {
				player.getInventory().delete(930, 1).add(12852, 50);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@50 Tokens");
			}
			break;
		case 19092:
			if (player.getInventory().contains(19092)) {
				player.getInventory().delete(19092, 1).add(12852, 20);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@20 Tokens");
			}
			break;
		case 15044:
			if (player.getInventory().contains(15044)) {
				player.getInventory().delete(15044, 1).add(12852, 5);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@5 Tokens");
			}
			break;
		case 9493:
			if (player.getInventory().contains(9493)) {
				player.getInventory().delete(9493, 1).add(12852, 60);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@60 Tokens");
			}
			break;
		case 9494:
			if (player.getInventory().contains(9494)) {
				player.getInventory().delete(9494, 1).add(12852, 60);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@60 Tokens");
			}
			break;
		case 9104:
			if (player.getInventory().contains(9104)) {
				player.getInventory().delete(9104, 1).add(12852, 60);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@60 Tokens");
			}
			break;
			
		case 9495:
			if (player.getInventory().contains(9495)) {
				player.getInventory().delete(9495, 1).add(12852, 60);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@60 Tokens");
			}
			break;
			
		case 13201:
			if (player.getInventory().contains(13201)) {
				player.getInventory().delete(13201, 1).add(12852, 90);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@90 Tokens");
			}
			break;
		case 14490:
			if (player.getInventory().contains(14490)) {
				player.getInventory().delete(14490, 1).add(12852, 60);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@60 Tokens");
			}
			break;
		case 14492:
			if (player.getInventory().contains(14492)) {
				player.getInventory().delete(14492, 1).add(12852, 60);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@60 Tokens");
			}
			break;
		case 14494:
			if (player.getInventory().contains(14494)) {
				player.getInventory().delete(14494, 1).add(12852, 60);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@60 Tokens");
			}
			break;

		case 2760:
			if (player.getInventory().contains(2760)) {
				player.getInventory().delete(2760, 1).add(12852, 60);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@60 Tokens");
			}
			break;
		case 6927:
			if (player.getInventory().contains(6927)) {
				player.getInventory().delete(6927, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 6928:
			if (player.getInventory().contains(6928)) {
				player.getInventory().delete(6928, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 6929:
			if (player.getInventory().contains(6929)) {
				player.getInventory().delete(6929, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 6930:
			if (player.getInventory().contains(6930)) {
				player.getInventory().delete(6930, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 6931:
			if (player.getInventory().contains(6931)) {
				player.getInventory().delete(6931, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 19727:
			if (player.getInventory().contains(19727)) {
				player.getInventory().delete(19727, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 19728:
			if (player.getInventory().contains(19728)) {
				player.getInventory().delete(19728, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 19729:
			if (player.getInventory().contains(19729)) {
				player.getInventory().delete(19729, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 19730:
			if (player.getInventory().contains(19730)) {
				player.getInventory().delete(19730, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 19731:
			if (player.getInventory().contains(19731)) {
				player.getInventory().delete(19731, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 19732:
			if (player.getInventory().contains(19732)) {
				player.getInventory().delete(19732, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 6485:
			if (player.getInventory().contains(6485)) {
				player.getInventory().delete(6485, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 13202:
			if (player.getInventory().contains(13202)) {
				player.getInventory().delete(13202, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 13203:
			if (player.getInventory().contains(13203)) {
				player.getInventory().delete(13203, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 13204:
			if (player.getInventory().contains(13204)) {
				player.getInventory().delete(13204, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 13205:
			if (player.getInventory().contains(13205)) {
				player.getInventory().delete(13205, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 13206:
			if (player.getInventory().contains(13206)) {
				player.getInventory().delete(13206, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 13207:
			if (player.getInventory().contains(13207)) {
				player.getInventory().delete(13207, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 11143:
			if (player.getInventory().contains(11143)) {
				player.getInventory().delete(11143, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 11144:
			if (player.getInventory().contains(11144)) {
				player.getInventory().delete(11144, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 11145:
			if (player.getInventory().contains(11145)) {
				player.getInventory().delete(11145, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 11146:
			if (player.getInventory().contains(11146)) {
				player.getInventory().delete(11146, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 11147:
			if (player.getInventory().contains(11147)) {
				player.getInventory().delete(11147, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 13991:
			if (player.getInventory().contains(13991)) {
				player.getInventory().delete(13991, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 13992:
			if (player.getInventory().contains(13992)) {
				player.getInventory().delete(13992, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 13993:
			if (player.getInventory().contains(13993)) {
				player.getInventory().delete(13993, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 13994:
			if (player.getInventory().contains(13994)) {
				player.getInventory().delete(13994, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 13995:
			if (player.getInventory().contains(13995)) {
				player.getInventory().delete(13995, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 14447:
			if (player.getInventory().contains(14447)) {
				player.getInventory().delete(14447, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 14448:
			if (player.getInventory().contains(14448)) {
				player.getInventory().delete(14448, 1).add(12852, 70);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@70 Tokens");
			}
			break;
		case 8664:
			if (player.getInventory().contains(8664)) {
				player.getInventory().delete(8664, 1).add(12852, 80);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@80 Tokens");
			}
			break;
		case 8665:
			if (player.getInventory().contains(8665)) {
				player.getInventory().delete(8665, 1).add(12852, 80);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@80 Tokens");
			}
			break;
		case 8666:
			if (player.getInventory().contains(8666)) {
				player.getInventory().delete(8666, 1).add(12852, 80);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@80 Tokens");
			}
			break;
		case 8667:
			if (player.getInventory().contains(8667)) {
				player.getInventory().delete(8667, 1).add(12852, 80);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@80 Tokens");
			}
			break;
		case 8668:
			if (player.getInventory().contains(8668)) {
				player.getInventory().delete(8668, 1).add(12852, 80);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@80 Tokens");
			}
			break;
		case 8669:
			if (player.getInventory().contains(8669)) {
				player.getInventory().delete(8669, 1).add(12852, 80);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@80 Tokens");
			}
			break;
		case 8670:
			if (player.getInventory().contains(8670)) {
				player.getInventory().delete(8670, 1).add(12852, 80);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@80 Tokens");
			}
			break;
		case 9496:
			if (player.getInventory().contains(9496)) {
				player.getInventory().delete(9496, 1).add(12852, 80);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@80 Tokens");
			}
			break;
		case 9497:
			if (player.getInventory().contains(9497)) {
				player.getInventory().delete(9497, 1).add(12852, 80);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@80 Tokens");
			}
			break;
		case 9498:
			if (player.getInventory().contains(9498)) {
				player.getInventory().delete(9498, 1).add(12852, 80);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@80 Tokens");
			}
			break;
		case 9499:
			if (player.getInventory().contains(9499)) {
				player.getInventory().delete(9499, 1).add(12852, 80);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@80 Tokens");
			}
			break;
		case 10905:
			if (player.getInventory().contains(10905)) {
				player.getInventory().delete(10905, 1).add(12852, 80);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@80 Tokens");
			}
			break;
		case 19155:
			if (player.getInventory().contains(19155)) {
				player.getInventory().delete(19155, 1).add(12852, 80);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@80 Tokens");
			}
			break;
		case 19154:
			if (player.getInventory().contains(19154)) {
				player.getInventory().delete(19154, 1).add(12852, 80);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@80 Tokens");
			}
			break;
		case 405:
			 amount = player.getInventory().getAmount(405);
			for(int i = 0; i < amount; i++) {
				if(player.getInventory().getFreeSlots() == 0) {
					player.getPacketSender().sendMessage("You need more inventory spaces!");
					return;
					}
			player.getInventory().delete(405, 1);
				int coins = RandomUtility.getRandom(100);
				player.getInventory().add(10835, coins);
			} 
			break;
			
		case 6183:
			 amount = player.getInventory().getAmount(6183);
			for(int i = 0; i < amount; i++) {
				if(player.getInventory().getFreeSlots() == 0) {
					player.getPacketSender().sendMessage("You need more inventory spaces!");
					return;
					}
				DonationBox.open(player);
			} 
			break;
		case 19741:
			if (player.getInventory().contains(19741)) {
				player.getInventory().delete(19741, 1).add(12852, 80);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@80 Tokens");
			}
			break;
		case 19742:
			if (player.getInventory().contains(19742)) {
				player.getInventory().delete(19742, 1).add(12852, 80);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@80 Tokens");
			}
			break;
		case 19743:
			if (player.getInventory().contains(19743)) {
				player.getInventory().delete(19743, 1).add(12852, 80);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@80 Tokens");
			}
			break;
		case 19744:
			if (player.getInventory().contains(19744)) {
				player.getInventory().delete(19744, 1).add(12852, 80);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@80 Tokens");
			}
			break;
		case 20427:
			if (player.getInventory().contains(20427)) {
				player.getInventory().delete(20427, 1).add(12852, 80);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@80 Tokens");
			}
			break;
		case 20431:
			if (player.getInventory().contains(20431)) {
				player.getInventory().delete(20431, 1).add(12852, 80);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@80 Tokens");
			}
			break;
		case 5226:
			if (player.getInventory().contains(5226)) {
				player.getInventory().delete(5226, 1).add(12852, 90);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@90 Tokens");
			}
			break;
		case 5227:
			if (player.getInventory().contains(5227)) {
				player.getInventory().delete(5227, 1).add(12852, 90);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@90 Tokens");
			}
			break;
		case 5228:
			if (player.getInventory().contains(5228)) {
				player.getInventory().delete(5228, 1).add(12852, 90);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@90 Tokens");
			}
			break;
		case 5229:
			if (player.getInventory().contains(5229)) {
				player.getInventory().delete(5229, 1).add(12852, 90);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@90 Tokens");
			}
			break;
		case 5230:
			if (player.getInventory().contains(5230)) {
				player.getInventory().delete(5230, 1).add(12852, 90);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@90 Tokens");
			}
			break;
		case 5231:
			if (player.getInventory().contains(5231)) {
				player.getInventory().delete(5231, 1).add(12852, 90);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@90 Tokens");
			}
			break;
		case 16443:
			if (player.getInventory().contains(16443)) {
				player.getInventory().delete(16443, 1).add(12852, 90);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@90 Tokens");
			}
			break;
		case 16444:
			if (player.getInventory().contains(16444)) {
				player.getInventory().delete(16444, 1).add(12852, 90);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@90 Tokens");
			}
			break;
		case 16445:
			if (player.getInventory().contains(16445)) {
				player.getInventory().delete(16445, 1).add(12852, 90);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@90 Tokens");
			}
			break;
		case 16446:
			if (player.getInventory().contains(16446)) {
				player.getInventory().delete(16446, 1).add(12852, 90);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@90 Tokens");
			}
			break;
		case 16448:
			if (player.getInventory().contains(16448)) {
				player.getInventory().delete(16448, 1).add(12852, 90);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@90 Tokens");
			}
			break;
		case 16449:
			if (player.getInventory().contains(16449)) {
				player.getInventory().delete(16449, 1).add(12852, 90);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@90 Tokens");
			}
			break;
		case 16450:
			if (player.getInventory().contains(16450)) {
				player.getInventory().delete(16450, 1).add(12852, 90);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@90 Tokens");
			}
			break;
		case 16451:
			if (player.getInventory().contains(16451)) {
				player.getInventory().delete(16451, 1).add(12852, 90);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@90 Tokens");
			}
			break;
		case 3085:
			if (player.getInventory().contains(3085)) {
				player.getInventory().delete(3085, 1).add(12852, 90);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@90 Tokens");
			}
			break;
		case 3086:
			if (player.getInventory().contains(3086)) {
				player.getInventory().delete(3086, 1).add(12852, 90);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@90 Tokens");
			}
			break;
		case 3087:
			if (player.getInventory().contains(3087)) {
				player.getInventory().delete(3087, 1).add(12852, 90);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@90 Tokens");
			}
			break;
		case 3088:
			if (player.getInventory().contains(3088)) {
				player.getInventory().delete(3088, 1).add(12852, 125);
				player.getPacketSender().sendMessage("You have Salvaged The Item for 125 Tokens");
			}
			break;
		case 3089:
			if (player.getInventory().contains(3089)) {
				player.getInventory().delete(3089, 1).add(12852, 90);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@90 Tokens");
			}
			break;
		case 3077:
			if (player.getInventory().contains(3077)) {
				player.getInventory().delete(3077, 1).add(12852, 125);
				player.getPacketSender().sendMessage("You have Salvaged The Item for 125 Tokens");
			}
			break;
		case 13265:
			if (player.getInventory().contains(13265)) {
				player.getInventory().delete(13265, 1).add(12852, 90);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@90 Tokens");
			}
			break;
		case 13266:
			if (player.getInventory().contains(13266)) {
				player.getInventory().delete(13266, 1).add(12852, 90);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@90 Tokens");
			}
			break;
		case 13267:
			if (player.getInventory().contains(13267)) {
				player.getInventory().delete(13267, 1).add(12852, 90);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@90 Tokens");
			}
			break;
		case 13268:
			if (player.getInventory().contains(13268)) {
				player.getInventory().delete(13268, 1).add(12852, 90);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@90 Tokens");
			}
			break;
		case 13269:
			if (player.getInventory().contains(13269)) {
				player.getInventory().delete(13269, 1).add(12852, 90);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@90 Tokens");
			}
			break;
		case 13270:
			if (player.getInventory().contains(13270)) {
				player.getInventory().delete(13270, 1).add(12852, 90);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@90 Tokens");
			}
			break;
		case 19158:
			if (player.getInventory().contains(19158)) {
				player.getInventory().delete(19158, 1).add(12852, 90);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@90 Tokens");
			}
			break;
		case 3971:
			if (player.getInventory().contains(3971)) {
				player.getInventory().delete(3971, 1).add(12852, 100);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@100 Tokens");
			}
			break;
		case 4280:
			if (player.getInventory().contains(4280)) {
				player.getInventory().delete(4280, 1).add(12852, 110);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@110 Tokens");
			}
			break;
		case 4281:
			if (player.getInventory().contains(4281)) {
				player.getInventory().delete(4281, 1).add(12852, 110);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@110 Tokens");
			}
			break;
		case 4282:
			if (player.getInventory().contains(4282)) {
				player.getInventory().delete(4282, 1).add(12852, 110);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@110 Tokens");
			}
			break;
		case 4283:
			if (player.getInventory().contains(4283)) {
				player.getInventory().delete(4283, 1).add(12852, 110);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@110 Tokens");
			}
			break;
		case 4284:
			if (player.getInventory().contains(4284)) {
				player.getInventory().delete(4284, 1).add(12852, 110);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@110 Tokens");
			}
			break;
		case 5115:
			if (player.getInventory().contains(5115)) {
				player.getInventory().delete(5115, 1).add(12852, 110);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@110 Tokens");
			}
			break;
		case 14694:
			if (player.getInventory().contains(14694)) {
				player.getInventory().delete(14694, 1).add(12852, 110);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@110 Tokens");
			}
			break;
		case 14695:
			if (player.getInventory().contains(14695)) {
				player.getInventory().delete(14695, 1).add(12852, 110);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@110 Tokens");
			}
			break;
		case 14872:
			if (player.getInventory().contains(14872)) {
				player.getInventory().delete(14872, 1).add(12852, 110);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@110 Tokens");
			}
			break;
		case 14873:
			if (player.getInventory().contains(14873)) {
				player.getInventory().delete(14873, 1).add(12852, 110);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@110 Tokens");
			}
			break;
		case 14874:
			if (player.getInventory().contains(14874)) {
				player.getInventory().delete(14874, 1).add(12852, 110);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@110 Tokens");
			}
			break;
		case 14932:
			if (player.getInventory().contains(14932)) {
				player.getInventory().delete(14932, 1).add(12852, 125);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@125 Tokens");
			}
			break;
		case 14933:
			if (player.getInventory().contains(14933)) {
				player.getInventory().delete(14933, 1).add(12852, 125);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@125 Tokens");
			}
			break;
		case 14934:
			if (player.getInventory().contains(14934)) {
				player.getInventory().delete(14934, 1).add(12852, 125);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@125 Tokens");
			}
			break;
		case 16524:
			if (player.getInventory().contains(16524)) {
				player.getInventory().delete(16524, 1).add(12852, 125);
				player.getPacketSender().sendMessage("You have Salvaged The Item for @red@125 Tokens");
			}
			break;
			//End Salvaging

		case 11425:
			if (player.getInventory().contains(6507)) {
			player.getInventory().delete(6507, 1);
			player.getSkillManager().setCurrentLevel(Skill.STRENGTH, 400);
			player.getSkillManager().setCurrentLevel(Skill.ATTACK, 400);
			player.getSkillManager().setCurrentLevel(Skill.DEFENCE, 400);
			player.getSkillManager().setCurrentLevel(Skill.RANGED, 400);
			player.getSkillManager().setCurrentLevel(Skill.MAGIC, 400);
			player.performAnimation(new Animation(6695));
			player.forceChat("I CAN FEEL THE POWER THROUGH ME!!");
			player.getClickDelay().reset();
			} else {
				player.getPacketSender().sendMessage("You need a Deluge Potion to charge the Offhand");
				return;
				
			}
			break;
			

		case 6570:
			if (player.getInventory().contains(6570) && player.getInventory().getAmount(6529) >= 50000) {
				player.getInventory().delete(6570, 1).delete(6529, 50000).add(19111, 1);
				player.getPacketSender().sendMessage("You have upgraded your Fire cape into a TokHaar-Kal cape!");
			} else {
				player.getPacketSender().sendMessage(
						"You need at least 50.000 Tokkul to upgrade your Fire Cape into a TokHaar-Kal cape.");
			}
			break;
			
		case 8655:
				player.getInventory().delete(8655, 1).add(8656, 1);
				player.getPacketSender().sendMessage("You have Successfully Switched your Imbued Weapon");
			break;
			
			
		case 8656:
				player.getInventory().delete(8656, 1).add(8654, 1);
				player.getPacketSender().sendMessage("You have Successfully Switched your Imbued Weapon");

			break;
		case 8654:
				player.getInventory().delete(8654, 1).add(8655, 1);
				player.getPacketSender().sendMessage("You have Successfully Switched your Imbued Weapon");

			break;
			
		case 3920:
			player.getInventory().delete(3920, 1).add(5263, 1);
			player.getPacketSender().sendMessage("You have Successfully Switched your Owner Weapon");
		break;
		
		
	case 5263:
			player.getInventory().delete(5263, 1).add(6499, 1);
			player.getPacketSender().sendMessage("You have Successfully Switched your Owner Weapon");

		break;
	case 6499:
			player.getInventory().delete(6499, 1).add(3920, 1);
			player.getPacketSender().sendMessage("You have Successfully Switched your Owner Weapon");

		break;
		
		
	case 8031:
		player.getInventory().delete(8031, 1).add(5265, 1);
		player.getPacketSender().sendMessage("You have Successfully Switched your 2x Damage Owner Weapon");
	break;
	
	
case 5265:
		player.getInventory().delete(5265, 1).add(5267, 1);
		player.getPacketSender().sendMessage("You have Successfully Switched your 2x Damage Owner Weapon");

	break;
case 5267:
		player.getInventory().delete(5267, 1).add(8031, 1);
		player.getPacketSender().sendMessage("You have Successfully Switched your 2x Damage Owner Weapon");
	break;
	
	
	
case 12185:
	player.getInventory().delete(12185, 1).add(13389, 1);
	player.getPacketSender().sendMessage("You have Successfully Switched your <shad=#4efbf5>Custom Weapon");
break;


case 13389:
	player.getInventory().delete(13389, 1).add(13388, 1);
	player.getPacketSender().sendMessage("You have Successfully Switched your <shad=#4efbf5>Custom Weapon");

break;
case 13388:
	player.getInventory().delete(13388, 1).add(12185, 1);
	player.getPacketSender().sendMessage("You have Successfully Switched your <shad=#4efbf5>Custom Weapon");
break;
	
	
	
case 8040:
	player.getInventory().delete(8040, 1).add(8041, 1);
	player.getPacketSender().sendMessage("You have Successfully Switched your <shad=#4efbf5>Custom Weapon");
break;


case 8041:
	player.getInventory().delete(8041, 1).add(8042, 1);
	player.getPacketSender().sendMessage("You have Successfully Switched your <shad=#4efbf5>Custom Weapon");

break;
case 8042:
	player.getInventory().delete(8042, 1).add(8040, 1);
	player.getPacketSender().sendMessage("You have Successfully Switched your <shad=#4efbf5>Custom Weapon");
break;
		
		case 18911:
			player.getInventory().delete(18911, 1).add(18912, 1);
			player.getPacketSender().sendMessage("You have Successfully Switched your Saturated Weapon");
		break;
		case 18912:
			player.getInventory().delete(18912, 1).add(14581, 1);
			player.getPacketSender().sendMessage("You have Successfully Switched your Saturated Weapon");
		break;
		case 14581:
			player.getInventory().delete(14581, 1).add(18911, 1);
			player.getPacketSender().sendMessage("You have Successfully Switched your Saturated Weapon");
		break;
		case 17842:
			if (player.getInventory().contains(4082) && player.getInventory().getAmount(4082) >= 10) {
				player.getInventory().delete(17842, 1).delete(4082, 10).add(17843, 1);
				player.getPacketSender().sendMessage("You have Combined the Shards with the gloves for more power!");
			} else {
				player.getPacketSender().sendMessage(
						"You need at least 10 Bloodshards to Buff the gloves");
			}
			break;
		case 17843:
			if (player.getInventory().contains(4082) && player.getInventory().getAmount(4082) >= 25) {
				player.getInventory().delete(17843, 1).delete(4082, 25).add(17844, 1);
				player.getPacketSender().sendMessage("You have Combined the Shards with the gloves for more power!");
			} else {
				player.getPacketSender().sendMessage(
						"You need at least 25 Bloodshards to Buff the gloves");
			}
			break;
			//IMBUED LEGS
			
		case 4067:
			if (player.getInventory().contains(4067) && player.getInventory().getAmount(4067) >= 1) {
				player.getInventory().delete(4067, 1).add(4060, 1);
				player.getPacketSender().sendMessage("You have Switched your Imbued Legs");

			}
			break;
			
		case 4063:
			if (player.getInventory().contains(4063) && player.getInventory().getAmount(4063) >= 1) {
				player.getInventory().delete(4063, 1).add(4067, 1);
				player.getPacketSender().sendMessage("You have Switched your Imbued Legs");

			}
			break;
			
		case 4060:
			if (player.getInventory().contains(4060) && player.getInventory().getAmount(4060) >= 1) {
				player.getInventory().delete(4060, 1).add(4063, 1);
				player.getPacketSender().sendMessage("You have Switched your Imbued Body");

			}
			break;
			
			//END IMBUED LEGS
			//START IMBUED BODY
		case 4061:
			if (player.getInventory().contains(4061) && player.getInventory().getAmount(4061) >= 1) {
				player.getInventory().delete(4061, 1).add(4064, 1);
				player.getPacketSender().sendMessage("You have Switched your Imbued Body");

			}
			break;
		case 4064:
			if (player.getInventory().contains(4064) && player.getInventory().getAmount(4064) >= 1) {
				player.getInventory().delete(4064, 1).add(4085, 1);
				player.getPacketSender().sendMessage("You have Switched your Imbued Body");

			}
			break;
		case 4085:
			if (player.getInventory().contains(4085) && player.getInventory().getAmount(4085) >= 1) {
				player.getInventory().delete(4085, 1).add(4061, 1);
				player.getPacketSender().sendMessage("You have Switched your Imbued Body");

			}
			break;
			//END IMBUED BODY
			//START IMBUED BOOTS
		case 4062:
			if (player.getInventory().contains(4062) && player.getInventory().getAmount(4062) >= 1) {
				player.getInventory().delete(4062, 1).add(4065, 1);
				player.getPacketSender().sendMessage("You have Switched your Imbued Boots");

			}
			break;
			
		case 4065:
			if (player.getInventory().contains(4065) && player.getInventory().getAmount(4065) >= 1) {
				player.getInventory().delete(4065, 1).add(4555, 1);
				player.getPacketSender().sendMessage("You have Switched your Imbued Boots");

			}
			break;
			
		case 4555:
			if (player.getInventory().contains(4555) && player.getInventory().getAmount(4555) >= 1) {
				player.getInventory().delete(4555, 1).add(4062, 1);
				player.getPacketSender().sendMessage("You have Switched your Imbued Boots");

			}
			break;
		case 6828:
			player.getInventory().delete(6828, 1);
			player.getInventory().add(8654, 1).refreshItems();
			player.getInventory().add(4060, 1).refreshItems();
			player.getInventory().add(4061, 1).refreshItems();
			player.getInventory().add(4062, 1).refreshItems();
			player.getInventory().add(19938, 5).refreshItems();
			break;
			
		case 12162:
			player.incrementNPCKills(100);
			player.getInventory().delete(12162, 1);
			player.sendMessage("100 Total NPC Killcount has been Added to your account");
			break;
			
		case 12164:
			player.incrementNPCKills(250);
			player.getInventory().delete(12164, 1);
			player.sendMessage("250 Total NPC Killcount has been Added to your account");
			break;
			
		case 1543:
			   KeysEvent.openChest(player);
			   break;
		case 989:
			CrystalChest.handleChest(player);
			   break;
			
			
			   
			   
			   //END IMBUED BOOTS
			
			
			
			
			
		case 15262:
			if (!player.getClickDelay().elapsed(1300))
				return;
			int amt = player.getInventory().getAmount(15262);
			if (amt > 0)
				player.getInventory().delete(15262, amt).add(18016, 10000 * amt);
			player.getClickDelay().reset();
			break;
		case 5509:
		case 5510:
		case 5512:
			RunecraftingPouches.empty(player, RunecraftingPouch.forId(itemId));
			break;
			
			
		case 11283: // DFS
			player.getPacketSender()
					.sendMessage("Your Dragonfire shield has " + player.getDfsCharges() + "/20 dragon-fire charges.");
			break;
		case 11613: // dkite
			player.getPacketSender()
					.sendMessage("Your Dragonfire shield has " + player.getDfsCharges() + "/20 dragon-fire charges.");
			break;
		}
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getConstitution() <= 0)
			return;
		switch (packet.getOpcode()) {
		case SECOND_ITEM_ACTION_OPCODE:
			secondAction(player, packet);
			break;
		case FIRST_ITEM_ACTION_OPCODE:
			firstAction(player, packet);
			break;
		case THIRD_ITEM_ACTION_OPCODE:
			thirdClickAction(player, packet);
			break;
		}
	}

	public static final int SECOND_ITEM_ACTION_OPCODE = 75;

	public static final int FIRST_ITEM_ACTION_OPCODE = 122;

	public static final int THIRD_ITEM_ACTION_OPCODE = 16;

}
