package com.arlania.net.packet.impl;

import com.arlania.GameSettings;
import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.engine.task.impl.WalkToTask;
import com.arlania.engine.task.impl.WalkToTask.FinalizedMovementTask;
import com.arlania.model.Animation;
import com.arlania.model.Direction;
import com.arlania.model.DwarfCannon;
import com.arlania.model.Flag;
import com.arlania.model.GameMode;
import com.arlania.model.GameObject;
import com.arlania.model.Graphic;
import com.arlania.model.MagicSpellbook;
import com.arlania.model.PlayerRights;
import com.arlania.model.Position;
import com.arlania.model.Prayerbook;
import com.arlania.model.Skill;
import com.arlania.model.Locations.Location;
import com.arlania.model.container.impl.Equipment;
import com.arlania.model.definitions.GameObjectDefinition;
import com.arlania.model.input.impl.DonateToWell;
import com.arlania.model.input.impl.EnterAmountOfLogsToAdd;
import com.arlania.net.packet.Packet;
import com.arlania.net.packet.PacketListener;
import com.arlania.util.Misc;
import com.arlania.util.RandomUtility;
import com.arlania.world.World;
import com.arlania.world.clip.region.RegionClipping;
import com.arlania.world.content.Achievements;
import com.arlania.world.content.Achievements.AchievementData;
import com.arlania.world.content.battlepass.BattlePass;
import com.arlania.world.content.bosses.TrioBosses;
import com.arlania.world.content.chests.BloodSlayerChest;
import com.arlania.world.content.chests.ChristmasEventChest;
import com.arlania.world.content.chests.CrystalChest;
import com.arlania.world.content.chests.DeluxeDonatorChest;
import com.arlania.world.content.chests.DungeonMinigameChest;
import com.arlania.world.content.chests.EmpireMinigameChest;
import com.arlania.world.content.chests.FantasyChest;
import com.arlania.world.content.chests.OPDonationChest;
import com.arlania.world.content.chests.RaidsEasyChest;
import com.arlania.world.content.chests.SephirothChest;
import com.arlania.world.content.bosses.SzoneBoss;
import com.arlania.world.content.bosses.SzoneBoss2;
import com.arlania.world.content.chests.SummerEventChest;
import com.arlania.world.content.chests.Upgradedchest;
import com.arlania.world.content.chests.VIPChest;
import com.arlania.world.content.CustomObjects;
import com.arlania.world.content.EvilTrees;
import com.arlania.world.content.KeysEvent;
import com.arlania.world.content.SalvageExchange;
import com.arlania.world.content.ShootingStar;
import com.arlania.world.content.WildernessObelisks;
import com.arlania.world.content.combat.magic.Autocasting;
import com.arlania.world.content.combat.prayer.CurseHandler;
import com.arlania.world.content.combat.prayer.PrayerHandler;
import com.arlania.world.content.combat.range.DwarfMultiCannon;
import com.arlania.world.content.combat.weapon.CombatSpecial;
import com.arlania.world.content.dialogue.DialogueManager;
import com.arlania.world.content.dialogue.impl.PennyWiseDialouge;
import com.arlania.world.content.grandexchange.GrandExchange;
import com.arlania.world.content.keepsake.KeepSake;
import com.arlania.world.content.minigames.impl.Barrows;
import com.arlania.world.content.minigames.impl.Dueling;
import com.arlania.world.content.minigames.impl.FightCave;
import com.arlania.world.content.minigames.impl.FightPit;
import com.arlania.world.content.minigames.impl.FreeForAll;
import com.arlania.world.content.minigames.impl.LastManStanding;
import com.arlania.world.content.minigames.impl.Nomad;
import com.arlania.world.content.minigames.impl.PestControl;
import com.arlania.world.content.minigames.impl.RecipeForDisaster;
import com.arlania.world.content.minigames.impl.WarriorsGuild;
import com.arlania.world.content.minigames.impl.Dueling.DuelRule;
import com.arlania.world.content.minigames.impl.gungame.GunGame;
import com.arlania.world.content.raids.Raid3;
import com.arlania.world.content.raids.RaidParty;
import com.arlania.world.content.raids.addons.RaidChest;
import com.arlania.world.content.skill.impl.agility.Agility;
import com.arlania.world.content.skill.impl.construction.Construction;
import com.arlania.world.content.skill.impl.crafting.Flax;
import com.arlania.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.arlania.world.content.skill.impl.fishing.Fishing;
import com.arlania.world.content.skill.impl.fishing.Fishing.Spot;
import com.arlania.world.content.skill.impl.hunter.Hunter;
import com.arlania.world.content.skill.impl.hunter.PuroPuro;
import com.arlania.world.content.skill.impl.mining.Mining;
import com.arlania.world.content.skill.impl.mining.MiningData;
import com.arlania.world.content.skill.impl.mining.Prospecting;
import com.arlania.world.content.skill.impl.runecrafting.Runecrafting;
import com.arlania.world.content.skill.impl.runecrafting.RunecraftingData;
import com.arlania.world.content.skill.impl.smithing.EquipmentMaking;
import com.arlania.world.content.skill.impl.smithing.Smelting;
import com.arlania.world.content.skill.impl.thieving.Stalls;
import com.arlania.world.content.skill.impl.woodcutting.Woodcutting;
import com.arlania.world.content.skill.impl.woodcutting.WoodcuttingData;
import com.arlania.world.content.skill.impl.woodcutting.WoodcuttingData.Hatchet;
import com.arlania.world.content.timedlocations.DreamZoneTimedLocation;
import com.arlania.world.content.transportation.TeleportHandler;
import com.arlania.world.content.transportation.TeleportType;
import com.arlania.world.entity.impl.npc.minigame.KeyRoom;
import com.arlania.world.entity.impl.player.Player;
import com.arlania.world.teleportinterface.TeleportInterface;
import com.arlania.world.teleportinterface.TeleportInterface.Bosses;

/**
 * This packet listener is called when a player clicked on a game object.
 * 
 * @author relex lawl
 */

public class ObjectActionPacketListener implements PacketListener {

	/**
	 * The PacketListener logger to debug information and print out errors.
	 */
	// private final static Logger logger =
	// Logger.getLogger(ObjectActionPacketListener.class);

	private static void firstClick(final Player player, Packet packet) {
		final int x = packet.readLEShortA();
		final int id = packet.readUnsignedShort();
		final int y = packet.readUnsignedShortA();
		final Position position = new Position(x, y, player.getPosition().getZ());
		final GameObject gameObject = new GameObject(id, position);
		if (player.getLocation() != Location.CONSTRUCTION) {
			if (id > 0 && id != 6 && !RegionClipping.objectExists(gameObject) && id != 9294) {
				// player.getPacketSender().sendMessage("An error occured. Error code:
				// "+id).sendMessage("Please report the error to a staff member.");
				return;
			}
		}
		int distanceX = (player.getPosition().getX() - position.getX());
		int distanceY = (player.getPosition().getY() - position.getY());
		if (distanceX < 0)
			distanceX = -(distanceX);
		if (distanceY < 0)
			distanceY = -(distanceY);
		int size = distanceX > distanceY ? GameObjectDefinition.forId(id).getSizeX()
				: GameObjectDefinition.forId(id).getSizeY();
		if (size <= 0)
			size = 1;
		gameObject.setSize(size);

		if (CustomObjects.getRaidChest(position) != null) {
			player.sendMessage("Opening raid chest...");
			RaidChest raidChest = (RaidChest) CustomObjects.getRaidChest(position);
			raidChest.setSize(size);
			player.setWalkToTask(new WalkToTask(player, position, raidChest.getSize(), new FinalizedMovementTask() {
				@Override
				public void execute() {
					/*
					 * Player controller
					 */
					if (raidChest.getRaid() instanceof Raid3) {
						raidChest.claimRewardWithPercent(player);
					} else {
						raidChest.claimReward(player);
					}
				}
			}));

			return;
		}

		if (player.getMovementQueue().isLockMovement())
			return;

		RaidParty raidParty = player.getRaidParty();

		if (player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.OWNER
				|| player.getRights() == PlayerRights.CO_OWNER)
			player.getPacketSender()
					.sendMessage("First click object id; [id, position] : [" + id + ", " + position.toString() + "]");
		player.setInteractingObject(gameObject)
				.setWalkToTask(new WalkToTask(player, position, gameObject.getSize(), new FinalizedMovementTask() {
					private int random;

					@Override
					public void execute() {
						player.setPositionToFace(gameObject.getPosition());
						if (player.getRegionInstance() != null) {
							Construction.handleFifthObjectClick(x, y, id, player);
						}
						if (WoodcuttingData.Trees.forId(id) != null) {
							Woodcutting.cutWood(player, gameObject, false);
							return;
						}
						if (MiningData.forRock(gameObject.getId()) != null) {
							Mining.startMining(player, gameObject);
							return;
						}
						if (player.getFarming().click(player, x, y, 1))
							return;
						if (Runecrafting.runecraftingAltar(player, gameObject.getId())) {
							RunecraftingData.RuneData rune = RunecraftingData.RuneData.forId(gameObject.getId());
							if (rune == null)
								return;
							Runecrafting.craftRunes(player, rune);
							return;
						}
						if (Agility.handleObject(player, gameObject)) {
							return;
						}
						if (Barrows.handleObject(player, gameObject)) {
							return;
						}
						if (player.getLocation() == Location.WILDERNESS
								&& WildernessObelisks.handleObelisk(gameObject.getId())) {
							return;
						}
						switch (id) {

						case 2466:
						case 2467:
						case 12120:
							KeyRoom.handleObjectClick(player, gameObject, 1);
							return;

						case 4163:
							if (player.getBloodFountain()) {
								player.getPacketSender().sendMessage("You already have this.");
								return;
							}

							if (!player.getInventory().contains(17750, 2000)) {
								player.getPacketSender().sendMessage("You need 2k Blood bags to offer to the table");
								return;
							}

							player.getInventory().delete(17750, 20000);
							player.performAnimation(new Animation(645));
							player.getPacketSender()
									.sendMessage("@red@You completed the offering, speak with the Blood slayermaster");
							player.setBloodFountain(true);

							break;

						case 2160:
							if (player.getRights() == PlayerRights.OWNER) {
								// if (player.lastVeigarRaid > System.currentTimeMillis()) {
								// player.sendMessage("You can only use this command once every 8 Hours");
								// player.sendMessage("You still need to wait another " +
								// player.getTimeRemaining(player.lastVeigarRaid));
								// return;
							}
							player.sendMessage("You teleport to the Veigar Boss");
							TeleportHandler.teleportPlayer(player, new Position(3439, 9580, 0),
									player.getSpellbook().getTeleportType());
							// player.lastVeigarRaid = System.currentTimeMillis() + 28800000;// 8hours
							break;
						case 881:
							DialogueManager.start(player, PennyWiseDialouge.getDialogue(player, 0));
							break;
						case 3005:
							SalvageExchange.open(player);
							break;

						case 3006:
							int[] itemList1 = { 3928, 5130, 19468, 18865, 5134, 3951, 18957, 5131, 3276, 19727, 5195,
									5129, -1, -1, -1, -1, -1, -1, -1 };
							player.getPacketSender().sendInterface(62200);
							for (int i = 0; i < itemList1.length; i++)
								player.getPacketSender().sendItemOnInterface(62209, itemList1[i], i, 1);
							break;
						case 3010:
							int[] itemList3 = { 989, 15373, 6199, 3988, 15374, 13997, 18392, 2572, 15375, 6509, 6510,
									6505, 6506, 14546, 14547, 5185, -1, -1, -1 - 1, -1, -1, -1, -1, -1, -1, -1 - 1, -1,
									-1, -1 };
							player.getPacketSender().sendInterface(62200);
							for (int i = 0; i < itemList3.length; i++)
								player.getPacketSender().sendItemOnInterface(62209, itemList3[i], i, 1);
							break;
						case 3008:
							int[] itemList2 = { 15418, 19886, 9006, 6194, 6195, 6196, 3973, 3908, 3910, 3909, 19728,
									19729, 19730, 19731, 19732, 6485, 14494, 14490, 14492, 2760, -1 };
							player.getPacketSender().sendInterface(62200);
							for (int i = 0; i < itemList2.length; i++)
								player.getPacketSender().sendItemOnInterface(62209, itemList2[i], i, 1);
							break;

						case 3009:
							if (player.hasBattlePass())
								;
							BattlePass.INSTANCE.openInterface(player);
							break;

						case 6970:
							// if (player.getSummerKC() < 10000) {
							// player.sendMessage("You need @red@10000 Summer guard KC @bla@to row this
							// boat");
							// return;
							// }
							player.sendMessage("You dont remember what happened, but you arrived!");
							player.moveTo(GameSettings.SUMMER_EVENT.copy());
							break;

						case 16792:
							player.performAnimation(new Animation(645));
							break;

						case 6195:
							//
							if (player.bloodFountain()) {
								TeleportHandler.teleportPlayer(player, new Position(2527, 4832),
										player.getSpellbook().getTeleportType());
							} else {
								player.getPacketSender()
										.sendMessage("@red@You must offer 20k blood bags to the offering table first");
							}
							break;

						case 10817:
							GunGame.start(player);
							break;
						case 38660:
							if (ShootingStar.CRASHED_STAR != null) {

							}
							break;
						case 2468:
							player.setDialogueActionId(776);
							DialogueManager.start(player, 180);
							break;

						case 4164:
							if (!player.getInventory().contains(8699)) {
								player.sendMessage("@red@You don't a have Boomers Dreampass");
								return;
							} else {
								if (player.getCurrentTimedLocation() != null) {
									player.getCurrentTimedLocation().sendPlayerToSpawnPoint();
									return;
								}
							}

							if (player.getCurrentTimedLocation() == null) {
								player.getInventory().delete(8699, 1);
								DreamZoneTimedLocation dreamZoneTimedLocation = new DreamZoneTimedLocation(
										new Position(2328, 5409, 0), 60, player, "Boomer");
								player.setCurrentTimedLocation(dreamZoneTimedLocation);
								player.getCurrentTimedLocation().startSession();
							}
							break;

						case 3662:
							if (!player.getInventory().contains(15251)) {
								player.sendMessage("@red@You don't a have a green stone");
								return;
							} else {
								player.getInventory().delete(15251, 1);
								SzoneBoss.spawn();
							}
							break;

						case 3449:
							if (!player.getInventory().contains(15251)) {
								player.sendMessage("@red@You don't a have a green stone");
								return;
							} else {
								player.getInventory().delete(15251, 1);
								SzoneBoss2.spawn();
							}
							break;

						case 42611: // raid portal
							if (raidParty == null) {
								player.sendMessage("@red@You must create or join a raid party before using this!");
								player.sendMessage("@blu@Speak with Arianwyn about raid parties.");
								return;
							}
							if (raidParty.getCurrentRaid() == null) {
								player.sendMessage("Your leader needs to select a raid.");
								return;
							}
							if (raidParty.getCurrentRaid().getStage() > -1) {
								raidParty.getCurrentRaid().respawn(player);
							}
							if (raidParty.getLeader() != player) {
								player.sendMessage("@red@Only the raid leader can start the raid.");
								return;
							}
							if (raidParty.getLeader() == null) {
								raidParty.disband();
								return;
							}
							if (raidParty.getCurrentRaid() == null) {
								player.sendMessage("@red@You need to select a raid with Arianwyn first.");
								return;
							}
							if (raidParty.getCurrentRaid().getStage() == -1) {
								raidParty.refresh();
								raidParty.getCurrentRaid().configureNpcs();
								raidParty.getCurrentRaid().spawnNpcs();
								raidParty.getCurrentRaid().nextLevel();
							}
							break;

						case 15541:
							player.getUpgradeHandler().openInterface();
							break;
						case 14367:
							if (player.isIronMan()) {
								player.getPacketSender().sendMessage(
										"Ironman-players are not allowed to buy items from the general-store.");
								player.getAchievementTracker()
										.progress(com.arlania.world.content.achievements.AchievementData.VISIT_POS, 1);

								return;
							}
							if (player.getLocation() == Location.DUNGEONEERING
									|| player.getLocation() == Location.DUEL_ARENA) {
								player.getPacketSender().sendMessage("You can't open the player shops right now!");
							} else {
								player.getPlayerOwnedShopManager().options();
								return;
							}

						case 2469:
							TeleportHandler.teleportPlayer(player, new Position(2582, 4609),
									player.getSpellbook().getTeleportType());
							player.sendMessage("@red@Get the key from the NPCS to advance to room 2");
							break;

						case 11231:
							if (player.getInventory().contains(19104)) {

								int[] lmsRewards = { 14415, 5148, 11694, 4151, 13896, 13887, 13893, 14009, 14010, 14008,
										15272, 15272, 15272, 15272, 13899, 3903, 10499, 11848, 11850, 11854, 14484,
										15300, 18349, 18353, 18351, 3886, 3902 };
								player.getInventory().delete(19104, 1);
								player.getInventory().add(lmsRewards[Misc.getRandom(lmsRewards.length - 1)], 1);
							} else {
								player.sendMessage("You need a LMS key to open this chest.");
								return;
							}
							break;

						case 2470:
							LastManStanding.enterLobby(player);
							break;

						case 52:
						case 53:
							if (player.getInventory().contains(19095)
									|| player.getRights() == PlayerRights.CELESTIAL_DONATOR
									|| player.getRights() == PlayerRights.LEGENDARY_DONATOR
									|| player.getRights().isStaff()) {
								player.getInventory().delete(19095, 1);
								TeleportHandler.teleportPlayer(player, new Position(2582, 4622, 0),
										player.getSpellbook().getTeleportType());
								player.sendMessage(
										"@red@You successfully open the gate with the key [Or without if your Deluxe Donator]");
							} else {
								player.sendMessage(
										"@red@You don't have the Room 1 key, which is needed to open this gate.");
								return;
							}
							break;
						case 11434:
							if (EvilTrees.SPAWNED_TREE != null) {

							}
							break;

						case 27330:
							player.performAnimation(new Animation(645));
							player.forceChat("Emerald is #1");
							if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) < player.getSkillManager()
									.getMaxLevel(Skill.PRAYER)) {
								player.getSkillManager().setCurrentLevel(Skill.PRAYER,
										player.getSkillManager().getMaxLevel(Skill.PRAYER), true);

								if (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) < player
										.getSkillManager().getMaxLevel(Skill.CONSTITUTION)) {
									player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION,
											player.getSkillManager().getMaxLevel(Skill.CONSTITUTION), true);
									player.sendMessage("@red@You feel Rejuvinated!");

								}
							} else
								;
							TeleportInterface.sendBossData(player, Bosses.NOOBS);
							TeleportInterface.sendBossTab(player);

							break;

						case 2079:
							TrioBosses.openChest(player);
							break;

						case 5542:
							SummerEventChest.openChest(player);
							break;
						case 2465:
							player.inFFALobby = false;
							player.moveTo(new Position(3208, 3426, 0));
							FreeForAll.removePlayer(player);
							return;

						case 5259:
							if (player.getPosition().getX() >= 3653) {
								player.moveTo(new Position(3652, player.getPosition().getY()));
							} else {
								player.setDialogueActionId(73);
								DialogueManager.start(player, 115);
							}
							break;

						case 13405:
							player.getPacketSender().sendMessage("Construction is disabled come back!");
							break;
						case 8799:
							GrandExchange.open(player);
							break;
						case 21505:
						case 21507:
							player.moveTo(new Position(2329, 3804));
							break;
						case 6420:
							KeysEvent.openChest(player);
							break;

						case 884:
						/*	DialogueManager.start(player, 179);
							player.setDialogueActionId(743);*/
							player.getWellOfGoodwillHandler().openWellOfGoodwill(player);
							break;

						case 38700:
							player.moveTo(new Position(3092, 3502));
							break;
						case 45803:

						case 1767:
							DialogueManager.start(player, 114);
							player.setDialogueActionId(72);
							break;
						case 920:
							DialogueManager.start(player, 1999);
							player.setDialogueActionId(1020);
							break;

						case 7352:
							if (Dungeoneering.doingDungeoneering(player) && player.getMinigameAttributes()
									.getDungeoneeringAttributes().getParty().getGatestonePosition() != null) {
								player.moveTo(player.getMinigameAttributes().getDungeoneeringAttributes().getParty()
										.getGatestonePosition());
								player.setEntityInteraction(null);
								player.getPacketSender().sendMessage("You are teleported to your party's gatestone.");
								player.performGraphic(new Graphic(1310));
							} else
								player.getPacketSender().sendMessage(
										"Your party must drop a Gatestone somewhere in the dungeon to use this portal.");
							break;
						case 7353:
							player.moveTo(new Position(2439, 4956, player.getPosition().getZ()));
							break;
						case 7321:
							player.moveTo(new Position(2452, 4944, player.getPosition().getZ()));
							break;
						case 7322:
							player.moveTo(new Position(2455, 4964, player.getPosition().getZ()));
							break;
						case 7315:
							player.moveTo(new Position(2447, 4956, player.getPosition().getZ()));
							break;
						case 8972:
							player.moveTo(new Position(2170, 5080, player.getPosition().getZ()));
							break;
						case 7316:
							player.moveTo(new Position(2471, 4956, player.getPosition().getZ()));
							break;

						case 7318:
							player.moveTo(new Position(2464, 4963, player.getPosition().getZ()));
							break;
						case 7319:
							player.moveTo(new Position(2467, 4940, player.getPosition().getZ()));
							break;
						case 7324:
							player.moveTo(new Position(2481, 4956, player.getPosition().getZ()));
							break;
						case 47180:

							player.getPacketSender().sendMessage("You activate the device..");
							player.moveTo(new Position(2586, 3912));
							break;
						case 10091:
						case 8702:

							Fishing.setupFishing(player, Spot.ROCKTAIL);
							break;
						case 9319:
							if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 61) {
								player.getPacketSender().sendMessage(
										"You need an Agility level of at least 61 or higher to climb this");
								return;
							}
							if (player.getPosition().getZ() == 0)
								player.moveTo(new Position(3422, 3549, 1));
							else if (player.getPosition().getZ() == 1) {
								if (gameObject.getPosition().getX() == 3447)
									player.moveTo(new Position(3447, 3575, 2));
								else
									player.moveTo(new Position(3447, 3575, 0));
							}
							break;

						case 9320:
							if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 61) {
								player.getPacketSender().sendMessage(
										"You need an Agility level of at least 61 or higher to climb this");
								return;
							}
							if (player.getPosition().getZ() == 1)
								player.moveTo(new Position(3422, 3549, 0));
							else if (player.getPosition().getZ() == 0)
								player.moveTo(new Position(3447, 3575, 1));
							else if (player.getPosition().getZ() == 2)
								player.moveTo(new Position(3447, 3575, 1));
							player.performAnimation(new Animation(828));
							break;
						case 2274:
							if (gameObject.getPosition().getX() == 2912 && gameObject.getPosition().getY() == 5300) {
								player.moveTo(new Position(2914, 5300, 1));
							} else if (gameObject.getPosition().getX() == 2914
									&& gameObject.getPosition().getY() == 5300) {
								player.moveTo(new Position(2912, 5300, 2));
							} else if (gameObject.getPosition().getX() == 2919
									&& gameObject.getPosition().getY() == 5276) {
								player.moveTo(new Position(2918, 5274));
							} else if (gameObject.getPosition().getX() == 2918
									&& gameObject.getPosition().getY() == 5274) {
								player.moveTo(new Position(2919, 5276, 1));
							} else if (gameObject.getPosition().getX() == 3001
									&& gameObject.getPosition().getY() == 3931
									|| gameObject.getPosition().getX() == 3652
											&& gameObject.getPosition().getY() == 3488) {
								player.moveTo(GameSettings.DEFAULT_POSITION.copy());
								player.getPacketSender().sendMessage("The portal teleports you to Edgeville.");
							}
							break;
						case 7836:
						case 7808:
							int amt = player.getInventory().getAmount(6055);
							if (amt > 0) {
								player.getInventory().delete(6055, amt);
								player.getPacketSender().sendMessage("You put the weed in the compost bin.");
								player.getSkillManager().addExperience(Skill.FARMING, 20 * amt);
							} else {
								player.getPacketSender().sendMessage("You do not have any weeds in your inventory.");
							}
							break;
						case 5960: // Levers
						case 5959:
							player.setDirection(Direction.WEST);
							TeleportHandler.teleportPlayer(player, new Position(3090, 3475), TeleportType.LEVER);
							break;

						case 48629:
							player.getInventory().add(1265, 1);
							player.getInventory().add(1351, 1);
							player.getInventory().add(309, 1);
							player.getInventory().add(314, 1000);
							break;

						case 5096:
							if (gameObject.getPosition().getX() == 2644 && gameObject.getPosition().getY() == 9593)
								player.moveTo(new Position(2649, 9591));
							break;

						case 5094:
							if (gameObject.getPosition().getX() == 2648 && gameObject.getPosition().getY() == 9592)
								player.moveTo(new Position(2643, 9594, 2));
							break;

						case 5098:
							if (gameObject.getPosition().getX() == 2635 && gameObject.getPosition().getY() == 9511)
								player.moveTo(new Position(2637, 9517));
							break;

						case 5097:
							if (gameObject.getPosition().getX() == 2635 && gameObject.getPosition().getY() == 9514)
								player.moveTo(new Position(2636, 9510, 2));
							break;
						case 26428:
						case 26426:
						case 26425:
						case 26427:
							String bossRoom = "Armadyl";
							boolean leaveRoom = player.getPosition().getY() > 5295;
							int index = 0;
							Position movePos = new Position(2839, !leaveRoom ? 5296 : 5295, 2);
							if (id == 26425) {
								bossRoom = "Bandos";
								leaveRoom = player.getPosition().getX() > 2863;
								index = 1;
								movePos = new Position(!leaveRoom ? 2864 : 2863, 5354, 2);
							} else if (id == 26427) {
								bossRoom = "Saradomin";
								leaveRoom = player.getPosition().getX() < 2908;
								index = 2;
								movePos = new Position(leaveRoom ? 2908 : 2907, 5265);
							} else if (id == 26428) {
								bossRoom = "Zamorak";
								leaveRoom = player.getPosition().getY() <= 5331;
								index = 3;
								movePos = new Position(2925, leaveRoom ? 5332 : 5331, 2);
							}
							if (!leaveRoom && (player.getRights() != PlayerRights.ADMINISTRATOR
									&& player.getRights() != PlayerRights.CO_OWNER
									&& player.getRights() != PlayerRights.OWNER
									&& player.getRights() != PlayerRights.OBSIDIAN_DONATOR
									&& player.getRights() != PlayerRights.MYSTIC_DONATOR
									&& player.getRights() != PlayerRights.ULTRA_DONATOR
									&& player.getRights() != PlayerRights.SUPER_DONATOR
									&& player.getRights() != PlayerRights.MODERATOR
									&& player.getRights() != PlayerRights.LEGENDARY_DONATOR
									&& player.getRights() != PlayerRights.SUPPORT
									&& player.getRights() != PlayerRights.DEVELOPER && player.getMinigameAttributes()
											.getGodwarsDungeonAttributes().getKillcount()[index] < 20)) {
								player.getPacketSender().sendMessage("You need " + Misc.anOrA(bossRoom) + " " + bossRoom
										+ " killcount of at least 20 to enter this room.");
								return;
							}
							player.moveTo(movePos);
							player.getMinigameAttributes().getGodwarsDungeonAttributes()
									.setHasEnteredRoom(leaveRoom ? false : true);
							player.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[index] = 0;
							player.getPacketSender().sendString(16216 + index, "0");
							break;
						case 26289:
						case 26286:
						case 26288:
						case 26287:
							if (System.currentTimeMillis() - player.getMinigameAttributes()
									.getGodwarsDungeonAttributes().getAltarDelay() < 600000) {
								player.getPacketSender().sendMessage("");
								player.getPacketSender()
										.sendMessage("You can only pray at a God's altar once every 10 minutes.");
								player.getPacketSender().sendMessage("You must wait another "
										+ (int) ((600 - (System.currentTimeMillis() - player.getMinigameAttributes()
												.getGodwarsDungeonAttributes().getAltarDelay()) * 0.001))
										+ " seconds before being able to do this again.");
								return;
							}
							int itemCount = id == 26289 ? Equipment.getItemCount(player, "Bandos", false)
									: id == 26286 ? Equipment.getItemCount(player, "Zamorak", false)
											: id == 26288 ? Equipment.getItemCount(player, "Armadyl", false)
													: id == 26287 ? Equipment.getItemCount(player, "Saradomin", false)
															: 0;
							int toRestore = player.getSkillManager().getMaxLevel(Skill.PRAYER) + (itemCount * 10);
							if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) >= toRestore) {
								player.getPacketSender()
										.sendMessage("You do not need to recharge your Prayer points at the moment.");
								return;
							}
							player.performAnimation(new Animation(645));
							player.getSkillManager().setCurrentLevel(Skill.PRAYER, toRestore);
							player.getMinigameAttributes().getGodwarsDungeonAttributes()
									.setAltarDelay(System.currentTimeMillis());
							break;
						case 23093:
							if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 70) {
								player.getPacketSender().sendMessage(
										"You need an Agility level of at least 70 to go through this portal.");
								return;
							}
							if (!player.getClickDelay().elapsed(2000))
								return;
							int plrHeight = player.getPosition().getZ();
							if (plrHeight == 2)
								player.moveTo(new Position(2914, 5300, 1));
							else if (plrHeight == 1) {
								int x = gameObject.getPosition().getX();
								int y = gameObject.getPosition().getY();
								if (x == 2914 && y == 5300)
									player.moveTo(new Position(2912, 5299, 2));
								else if (x == 2920 && y == 5276)
									player.moveTo(new Position(2920, 5274, 0));
							} else if (plrHeight == 0)
								player.moveTo(new Position(2920, 5276, 1));
							player.getClickDelay().reset();
							break;
						case 26439:
							if (player.getSkillManager().getMaxLevel(Skill.CONSTITUTION) <= 700) {
								player.getPacketSender()
										.sendMessage("You need a Constitution level of at least 70 to swim across.");
								return;
							}
							if (!player.getClickDelay().elapsed(1000))
								return;
							if (player.isCrossingObstacle())
								return;
							final String startMessage = "You jump into the icy cold water..";
							final String endMessage = "You climb out of the water safely.";
							final int jumpGFX = 68;
							final int jumpAnimation = 772;
							player.setSkillAnimation(773);
							player.setCrossingObstacle(true);
							player.getUpdateFlag().flag(Flag.APPEARANCE);
							player.performAnimation(new Animation(3067));
							final boolean goBack2 = player.getPosition().getY() >= 5344;
							player.getPacketSender().sendMessage(startMessage);
							player.moveTo(new Position(2885, !goBack2 ? 5335 : 5342, 2));
							player.setDirection(goBack2 ? Direction.SOUTH : Direction.NORTH);
							player.performGraphic(new Graphic(jumpGFX));
							player.performAnimation(new Animation(jumpAnimation));
							TaskManager.submit(new Task(1, player, false) {
								int ticks = 0;

								@Override
								public void execute() {
									ticks++;
									player.getMovementQueue().walkStep(0, goBack2 ? -1 : 1);
									if (ticks >= 10)
										stop();
								}

								@Override
								public void stop() {
									player.setSkillAnimation(-1);
									player.setCrossingObstacle(false);
									player.getUpdateFlag().flag(Flag.APPEARANCE);
									player.getPacketSender().sendMessage(endMessage);
									player.moveTo(
											new Position(2885, player.getPosition().getY() < 5340 ? 5333 : 5345, 2));
									setEventRunning(false);
								}
							});
							player.getClickDelay().reset((System.currentTimeMillis() + 9000));
							break;

						case 26384:
							if (player.isCrossingObstacle())
								return;
							if (!player.getInventory().contains(2347)) {
								player.getPacketSender()
										.sendMessage("You need to have a hammer to bang on the door with.");
								return;
							}
							player.setCrossingObstacle(true);
							final boolean goBack = player.getPosition().getX() <= 2850;
							player.performAnimation(new Animation(377));
							TaskManager.submit(new Task(2, player, false) {
								@Override
								public void execute() {
									player.moveTo(new Position(goBack ? 2851 : 2850, 5333, 2));
									player.setCrossingObstacle(false);
									stop();
								}
							});
							break;

						case 26303:
							if (!player.getClickDelay().elapsed(1200))
								return;
							if (player.getSkillManager().getCurrentLevel(Skill.RANGED) < 70)
								player.getPacketSender()
										.sendMessage("You need a Ranged level of at least 70 to swing across here.");
							else if (!player.getInventory().contains(9418)) {
								player.getPacketSender().sendMessage(
										"You need a Mithril grapple to swing across here. Explorer Jack might have one.");
								return;
							} else {
								player.performAnimation(new Animation(789));
								TaskManager.submit(new Task(2, player, false) {
									@Override
									public void execute() {
										player.getPacketSender().sendMessage(
												"You throw your Mithril grapple over the pillar and move across.");
										player.moveTo(new Position(2871,
												player.getPosition().getY() <= 5270 ? 5279 : 5269, 2));
										stop();
									}
								});
								player.getClickDelay().reset();
							}
							break;
						case 4493:
							if (player.getPosition().getX() >= 3432) {
								player.moveTo(new Position(3433, 3538, 1));
							}
							break;

						case 11356:
							if (player.getRights() == PlayerRights.OBSIDIAN_DONATOR || player.getRights().isStaff()) {
								player.getPacketSender().sendMessage("@red@Thank you for supporting Pwnlite!");
								player.moveTo(new Position(2335, 3612, 0));
							}

				else
								; {
							if (player.getRights() == PlayerRights.DONATOR
									|| player.getRights() == PlayerRights.SUPER_DONATOR)

								player.getPacketSender()
										.sendMessage("@red@You Need to be UBER Donator to access this Zone!");
							break;
						}

						case 16082:
							if (player.getAmountDonated() >= 1000) {
								TeleportHandler.teleportPlayer(player, new Position(2335, 3692, 0),
										player.getSpellbook().getTeleportType());
								player.getPacketSender().sendMessage("Welcome to the Deluxe Zone");
							} else {
								player.sendMessage("@red@nah fam, you need Deluxe Donator rank to enter");
								return;
							}
							break;

						// RAIDS PORTALS AND BARRIERS TELEPORTS

						case 42019: // Raids room 1 barriers to portal
							if (player.getInventory().getAmount(7774) < 1) {
								player.getPacketSender().sendMessage(
										"@blu@You need at least 1@red@Raids Token" + " orbs @blu@to Move on");
								return;
							}
							player.getPacketSender().sendMessage("@red@Goodluck on this one");
							TeleportHandler.teleportPlayer(player, new Position(2169, 4966, 8),
									player.getSpellbook().getTeleportType());
							player.performAnimation(new Animation(3094));
							break;

						case 42425: // Raids room 1 Portal to (2065, 4966,8) (ROOM 2 Coordinates)
							if (player.getInventory().getAmount(7775) < 1) {
								player.getPacketSender()
										.sendMessage("@red@<shad=2>You need a Raids token to  Breach the Portal");
								return;
							}
							TeleportHandler.teleportPlayer(player, new Position(2065, 4966, 8),
									player.getSpellbook().getTeleportType());
							player.performAnimation(new Animation(3094));
							player.getInventory().delete(7775, 10000);
							break;

						case 38144: // Raids room 2 barrier to portal
							if (player.getInventory().getAmount(7775) < 1) {
								player.getPacketSender()
										.sendMessage("@red@<shad=2>You need a Raids token to  Breach the Portal");
								return;
							}
							player.getPacketSender().sendMessage("@red@Goodluck on this one");
							TeleportHandler.teleportPlayer(player, new Position(2101, 4966, 8),
									player.getSpellbook().getTeleportType());
							player.performAnimation(new Animation(3094));
							break;

						case 39515: // Raids room 2Portal to (2002, 4966,8) (ROOM 3 Coordinates)
							if (player.getInventory().getAmount(7775) < 1) {
								player.getPacketSender()
										.sendMessage("@red@<shad=2>You need a Raids token to  Breach the Portal");
								return;
							}
							TeleportHandler.teleportPlayer(player, new Position(2002, 4966, 8),
									player.getSpellbook().getTeleportType());
							player.performAnimation(new Animation(3094));
							player.getInventory().delete(7775, 10000);
							break;

						case 31435: // Raids room 2 barrier to portal
							TeleportHandler.teleportPlayer(player, new Position(2041, 4966, 8),
									player.getSpellbook().getTeleportType());
							break;

						case 56146: // Raids room 2Portal to (1952, 4946,8) (Reward room Coordinates)
							TeleportHandler.teleportPlayer(player, new Position(1952, 4946, 8),
									player.getSpellbook().getTeleportType());
							break;

						// END RAIDS PORTALS AND BARRIERS TELEPORTS

						case 10804:// bonds dungeon

						{
							if (player.getInventory().getAmount(10835) < 100000)
								if (player.getPointsHandler().getBossPoints() < 50000) {
									player.getPacketSender()
											.sendMessage("@blu@You need at least 50k MOB Points  & 100K tax bagz"
													+ "@blu@to enter.");
									return;
								}
						}
							player.getPacketSender()

									.sendMessage("@red@Goodluck on this one");
							player.getPointsHandler().setBossPoints(-50000, true);
							player.getInventory().delete(10835, 100000);
							TeleportHandler.teleportPlayer(player, new Position(3309, 9808, 8),
									player.getSpellbook().getTeleportType());
							player.getPointsHandler().refreshPanel();

							break;

						case 13621:// zulrah dungeon vip zone
							if (player.getPointsHandler().getBossPoints() < 1000) {
								player.getPacketSender()
										.sendMessage("@blu@You need at least 1k MOB Points" + "@blu@to enter.");
								return;
							}

							player.getPacketSender().sendMessage("@red@Goodluck with zulrah");
							TeleportHandler.teleportPlayer(player, new Position(2517, 3349, 0),
									player.getSpellbook().getTeleportType());
							player.getPointsHandler().refreshPanel();
							player.getPointsHandler().setBossPoints(-1000, true);
							break;

						case 13628:// brothers of terror
							if (player.getPointsHandler().getBossPoints() < 250) {
								player.getPacketSender()
										.sendMessage("@blu@You need at least 250 MOB Points" + "@blu@to enter.");
								return;
							}

							player.getPacketSender().sendMessage("@red@Goodluck with the bros");
							TeleportHandler.teleportPlayer(player, new Position(2902, 4448, 0),
									player.getSpellbook().getTeleportType());
							player.getPointsHandler().refreshPanel();
							player.getPointsHandler().setBossPoints(-250, true);
							break;

						case 13620:// investor npc dungeon
							if (player.getPointsHandler().getBossPoints() < 1000) {
								player.getPacketSender().sendMessage("@red@Goodluck on this one");
								TeleportHandler.teleportPlayer(player, new Position(2596, 4435, 0),
										player.getSpellbook().getTeleportType());
								player.getPointsHandler().refreshPanel();
								player.getPointsHandler().setBossPoints(-1000, true);
								return;

							}
							player.getPacketSender()
									.sendMessage("@blu@You need at least 1k MOB Points" + "@blu@to enter.");

							break;

						case 13623:// instance spidy for investor
							player.getSagittareEvent().initialize();
							break;

						case 26301:
							player.performAnimation(new Animation(828));
							player.moveTo(new Position(2318, 3677, 0));
							player.sendMessage("@red@You manage to escape the dungeon");
							break;

						case 16050:
							if (player.getAmountDonated() >= 500) {
								TeleportHandler.teleportPlayer(player, new Position(2335, 3692, 0),
										player.getSpellbook().getTeleportType());
								player.getPacketSender().sendMessage("Welcome to the Deluxe Zone");
							} else {
								player.sendMessage("@red@nah fam, you need Deluxe Donator rank to enter");
								return;
							}
							break;

						case 4407:// venastis spider
							TeleportHandler.teleportPlayer(player, new Position(2717, 9248, 0),
									player.getSpellbook().getTeleportType());
							break;

						case 4406:// vet'ion boss
							TeleportHandler.teleportPlayer(player, new Position(2781, 9248, 0),
									player.getSpellbook().getTeleportType());
							// player.getPacketSender().sendInterface(64530);
							break;
						case 4390:// kbd boss
							TeleportHandler.teleportPlayer(player, new Position(2268, 4695, 0),
									player.getSpellbook().getTeleportType());
							break;
						case 4389:// skotizo lair
							TeleportHandler.teleportPlayer(player, new Position(2717, 9184, 0),
									player.getSpellbook().getTeleportType());
							break;

						case 12355:

							TeleportHandler.teleportPlayer(player, new Position(2594, 3126, 0),
									player.getSpellbook().getTeleportType());

							break;

						case 46935:
							if (player.getPointsHandler().getSlayerPoints() < 800) {
								player.getPacketSender().sendMessage(
										"@blu@You need at least 800 @red@Slayer Points" + "@blu@to Fight Mighty One.");
								return;
							}
							player.getPacketSender().sendMessage("@red@Goodluck on this one");
							TeleportHandler.teleportPlayer(player, new Position(2832, 2870, 0),
									player.getSpellbook().getTeleportType());
							player.getPointsHandler().refreshPanel();
							player.getPointsHandler().setSlayerPoints(-800, true);
							break;

						case 7527:
							if (player.getSkillManager().getCurrentLevel(Skill.ARCHEOLOGY) < 50) {
								if (player.getInventory().contains(15286, 200)) {

									player.getPacketSender()
											.sendMessage("@blu@You need @red@ 50 Archeology & 200 Stone clippings"
													+ "@blu@to Enter the gate");
									return;
								}
							}
							player.getPacketSender().sendMessage("@red@Goodluck training");
							TeleportHandler.teleportPlayer(player, new Position(3042, 3306, 0),
									player.getSpellbook().getTeleportType());
							player.performAnimation(new Animation(3094));
							player.getInventory().delete(15286, 200);
							break;

						case 50306:// arch stone 1
							if (!player.getClickDelay().elapsed(4000))

								return;
							if (player.getSkillManager().getCurrentLevel(Skill.ARCHEOLOGY) < 80) {

								if (Misc.getRandom(8000) == 3) {
									player.getInventory().add(1648, 1);
									World.sendMessage("@blu@<img=10>[Archeology] " + player.getUsername()
											+ " has received the Digger Pet!");
									player.getPacketSender().sendMessage("@red@You have received a skilling pet!");
								}

								player.getPacketSender().sendMessage(
										"@blu@You need @red@ 80 Archeology" + "@blu@to train on the rock pile");
								return;
							}
							player.performAnimation(new Animation(2094));
							player.getInventory().add(15286, 3);
							player.getInventory().add(19864, 3);
							player.getSkillManager().addExperience(Skill.ARCHEOLOGY, 7500);
							player.getPacketSender()
									.sendMessage("@red@You've just received " + 3 + " skilling points.");
							player.getPointsHandler().incrementSkillPoints(3);
							player.getClickDelay().reset();
							break;

						case 50307:// arch stone 1
							if (!player.getClickDelay().elapsed(5000))
								return;
							if (player.getSkillManager().getCurrentLevel(Skill.ARCHEOLOGY) < 1) {

								if (Misc.getRandom(8000) == 3) {
									player.getInventory().add(1648, 1);
									World.sendMessage("@blu@<img=10>[Archeology] " + player.getUsername()
											+ " has received the Digger Pet!");
									player.getPacketSender().sendMessage("@red@You have received a skilling pet!");
								}

								player.getPacketSender().sendMessage(
										"@blu@You need @red@ 1 Archeology" + "@blu@to train on the rock pile");
								return;
							}
							player.performAnimation(new Animation(2094));
							player.getInventory().add(15286, 1);
							player.getInventory().add(19864, 1);
							player.getSkillManager().addExperience(Skill.ARCHEOLOGY, 1500);
							player.getPacketSender()
									.sendMessage("@red@You've just received " + 2 + " skilling points.");
							player.getPointsHandler().incrementSkillPoints(2);
							player.getClickDelay().reset();
							break;

						case 50305:// arch stone 1
							if (!player.getClickDelay().elapsed(5000))
								return;
							if (player.getSkillManager().getCurrentLevel(Skill.ARCHEOLOGY) < 50) {

								if (Misc.getRandom(8000) == 3) {
									player.getInventory().add(1648, 1);
									World.sendMessage("@blu@<img=10>[Archeology] " + player.getUsername()
											+ " has received the Digger Pet!");
									player.getPacketSender().sendMessage("@red@You have received a skilling pet!");
								}

								player.getPacketSender().sendMessage(
										"@blu@You need @red@ 50 Archeology" + "@blu@to train on the rock pile");
								return;
							}
							player.performAnimation(new Animation(2094));
							player.getInventory().add(15286, 2);
							player.getInventory().add(19864, 2);
							player.getSkillManager().addExperience(Skill.ARCHEOLOGY, 1500);
							player.getPacketSender()
									.sendMessage("@red@You've just received " + 2 + " skilling points.");
							player.getPointsHandler().incrementSkillPoints(2);
							player.getClickDelay().reset();
							break;

						case 44379:// arch stone 1
							if (!player.getClickDelay().elapsed(4000))
								return;
							if (player.getSkillManager().getCurrentLevel(Skill.ARCHEOLOGY) < 95) {

								if (Misc.getRandom(8000) == 3) {
									player.getInventory().add(1648, 1);
									World.sendMessage("@blu@<img=10>[Archeology] " + player.getUsername()
											+ " has received the Digger Pet!");
									player.getPacketSender().sendMessage("@red@You have received a skilling pet!");
								}

								player.getPacketSender().sendMessage(
										"@blu@You need @red@ 95 Archeology" + "@blu@to search these bones");
								return;
							}
							player.performAnimation(new Animation(2094));
							player.getInventory().add(15286, 4);
							player.getInventory().add(4255, 1);
							player.getSkillManager().addExperience(Skill.ARCHEOLOGY, 7500);
							player.getPacketSender()
									.sendMessage("@red@You've just received " + 5 + " skilling points.");
							player.getPointsHandler().incrementSkillPoints(5);
							player.getClickDelay().reset();
							break;

						case 4518:
							if (!player.getClickDelay().elapsed(1000))
								return;
							if (player.gethweenKC() < 5) {
								player.getPacketSender().sendMessage("<img=552>@blu@You need @red@ 5 Spider Kills "
										+ "@blu@to search these bones<img=552>");
								return;
							}

							player.decreaseHweenKC(5);

							if (Misc.getRandom(5) == 4) {
								player.performAnimation(new Animation(2094));
								player.getInventory().add(12631, 1);
								player.getInventory().add(954, 1);
								player.sendMessage("@blu@<img=552>[RARE] "
										+ "You have discovered a sailboat and a rope from Georgies corpse");
								player.getSkillManager().addExperience(Skill.ARCHEOLOGY, 7500);
								player.getClickDelay().reset();
							} else {
								player.sendMessage("<img=552> 5 spider kills vanish when investigating Georgies corpse");
								player.performAnimation(new Animation(2094));
								player.getInventory().add(15410, 1);
								player.getSkillManager().addExperience(Skill.ARCHEOLOGY, 7500);
								player.getClickDelay().reset();
							}
							break;

						case 42219:
							if (player.getPointsHandler().getSkillPoints() < 999) {
								player.getPacketSender().sendMessage(
										"@blu@You need at least 1000 @red@Skilling Points" + "@blu@to Fight Kidd Bu.");
								return;
							}
							player.getPacketSender().sendMessage("@red@Goodluck on this one");
							TeleportHandler.teleportPlayer(player, new Position(2597, 3157, 8),
									player.getSpellbook().getTeleportType());
							player.getPointsHandler().refreshPanel();
							player.getPointsHandler().setSkillPoints(-1000, true);
							break;
						case 42220:

							if (player.getInventory().getAmount(744) < 5000) {
								player.getPacketSender().sendMessage("@blu@You need at least 5000 @red@Crystal Hearts"
										+ " orbs @blu@to Fight Spidy.");
								return;
							}
							player.getPacketSender().sendMessage("@red@Goodluck on this one");
							TeleportHandler.teleportPlayer(player, new Position(2867, 2830, 0),
									player.getSpellbook().getTeleportType());
							player.performAnimation(new Animation(3094));
							player.getInventory().delete(744, 5000);
							break;

						case 2021:
							player.getForgingManager().open();
							break;

						case 2879:

							if (player.getInventory().getAmount(989) < 150) {
								player.getPacketSender().sendMessage(
										"@blu@You need at least 150 @red@C-Keys" + "@blu@to Forge this Key.");
								return;
							}
							player.getPacketSender().sendMessage("@red@Congradulations u made Upgraded C Key");
							player.getInventory().delete(989, 150);
							player.getInventory().add(85, 1);
							break;

						case 13619:

							if (player.getInventory().getAmount(17923) < 1000) {
								player.getPacketSender().sendMessage(
										"@blu@You need at least 1000 @red@Hellfire" + " orbs @blu@to enter this area.");
								return;
							}
							player.getPacketSender().sendMessage("@red@Goodluck in Room 2!");
							player.getInventory().delete(17923, 1000);
							player.moveTo(new Position(2459, 10135));
							break;

						case 23095:

							if (player.getInventory().getAmount(17924) < 1000) {
								player.getPacketSender().sendMessage("@blu@You need at least 1000 @red@Nomads tickets"
										+ " @blu@to enter this area.");
								return;
							}
							player.getPacketSender().sendMessage("@red@Goodluck in the Final Room!");
							player.getInventory().delete(17924, 1000);
							player.moveTo(new Position(2458, 10164));
							break;

						case 4494:
							player.moveTo(new Position(3438, 3538, 0));
							break;
						case 4495:
							player.moveTo(new Position(3417, 3541, 2));
							break;
						case 4496:
							player.moveTo(new Position(3412, 3541, 1));
							break;
						case 2491:
							player.setDialogueActionId(48);
							DialogueManager.start(player, 87);
							break;
						case 25339:
						case 25340:
							player.moveTo(new Position(1778, 5346, player.getPosition().getZ() == 0 ? 1 : 0));
							break;
						case 10229:
						case 10230:
							boolean up = id == 10229;
							player.performAnimation(new Animation(up ? 828 : 827));
							player.getPacketSender().sendMessage("You climb " + (up ? "up" : "down") + " the ladder..");
							TaskManager.submit(new Task(1, player, false) {
								@Override
								protected void execute() {
									player.moveTo(up ? new Position(1912, 4367) : new Position(2900, 4449));
									stop();
								}
							});
							break;
						case 1568:
							player.moveTo(new Position(3097, 9868));
							break;
						case 5103: // Brimhaven vines
						case 5104:
						case 5105:
						case 5106:
						case 5107:
							if (!player.getClickDelay().elapsed(4000))
								return;
							if (player.getSkillManager().getCurrentLevel(Skill.WOODCUTTING) < 30) {
								player.getPacketSender()
										.sendMessage("You need a Woodcutting level of at least 30 to do this.");
								return;
							}
							if (WoodcuttingData.getHatchet(player) < 0) {
								player.getPacketSender().sendMessage(
										"You do not have a hatchet which you have the required Woodcutting level to use.");
								return;
							}
							final Hatchet axe = Hatchet.forId(WoodcuttingData.getHatchet(player));
							player.performAnimation(new Animation(axe.getAnim()));
							gameObject.setFace(-1);
							TaskManager.submit(new Task(3 + RandomUtility.getRandom(4), player, false) {
								@Override
								protected void execute() {
									if (player.getMovementQueue().isMoving()) {
										stop();
										return;
									}
									int x = 0;
									int y = 0;
									if (player.getPosition().getX() == 2689 && player.getPosition().getY() == 9564) {
										x = 2;
										y = 0;
									} else if (player.getPosition().getX() == 2691
											&& player.getPosition().getY() == 9564) {
										x = -2;
										y = 0;
									} else if (player.getPosition().getX() == 2683
											&& player.getPosition().getY() == 9568) {
										x = 0;
										y = 2;
									} else if (player.getPosition().getX() == 2683
											&& player.getPosition().getY() == 9570) {
										x = 0;
										y = -2;
									} else if (player.getPosition().getX() == 2674
											&& player.getPosition().getY() == 9479) {
										x = 2;
										y = 0;
									} else if (player.getPosition().getX() == 2676
											&& player.getPosition().getY() == 9479) {
										x = -2;
										y = 0;
									} else if (player.getPosition().getX() == 2693
											&& player.getPosition().getY() == 9482) {
										x = 2;
										y = 0;
									} else if (player.getPosition().getX() == 2672
											&& player.getPosition().getY() == 9499) {
										x = 2;
										y = 0;
									} else if (player.getPosition().getX() == 2674
											&& player.getPosition().getY() == 9499) {
										x = -2;
										y = 0;
									}
									CustomObjects.objectRespawnTask(player,
											new GameObject(-1, gameObject.getPosition().copy()), gameObject, 10);
									player.getPacketSender().sendMessage("You chop down the vines..");
									player.getSkillManager().addExperience(Skill.WOODCUTTING, 45);
									player.performAnimation(new Animation(65535));
									player.getMovementQueue().walkStep(x, y);
									stop();
								}
							});
							player.getClickDelay().reset();
							break;
						case 29942:
							if (player.getSkillManager().getCurrentLevel(Skill.SUMMONING) == player.getSkillManager()
									.getMaxLevel(Skill.SUMMONING)) {
								player.getPacketSender()
										.sendMessage("You do not need to recharge your Summoning points right now.");
								return;
							}
							player.performGraphic(new Graphic(1517));
							player.getSkillManager().setCurrentLevel(Skill.SUMMONING,
									player.getSkillManager().getMaxLevel(Skill.SUMMONING), true);
							player.getPacketSender().sendString(18045,
									" " + player.getSkillManager().getCurrentLevel(Skill.SUMMONING) + "/"
											+ player.getSkillManager().getMaxLevel(Skill.SUMMONING));
							player.getPacketSender().sendMessage("You recharge your Summoning points.");
							break;
						case 57225:
							if (!player.getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom()) {
								player.setDialogueActionId(44);
								DialogueManager.start(player, 79);
							} else {
								player.moveTo(new Position(2906, 5204));
								player.getMinigameAttributes().getGodwarsDungeonAttributes().setHasEnteredRoom(false);
							}
							break;
						// case 884:
						// player.setDialogueActionId(41);
						// DialogueManager.start(player, 75);
						// break;

						case 9294:
							if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 80) {
								player.getPacketSender()
										.sendMessage("You need an Agility level of at least 80 to use this shortcut.");
								return;
							}
							player.performAnimation(new Animation(769));
							TaskManager.submit(new Task(1, player, false) {
								@Override
								protected void execute() {
									player.moveTo(
											new Position(player.getPosition().getX() >= 2880 ? 2878 : 2880, 9813));
									stop();
								}
							});
							break;
						case 9293:
							boolean back = player.getPosition().getX() > 2888;
							player.moveTo(back ? new Position(2886, 9799) : new Position(2891, 9799));
							break;
						case 2320:
							back = player.getPosition().getY() == 9969 || player.getPosition().getY() == 9970;
							player.moveTo(back ? new Position(3120, 9963) : new Position(3120, 9969));
							break;
						case 1755:
							player.performAnimation(new Animation(828));
							player.getPacketSender().sendMessage("You climb the stairs..");
							TaskManager.submit(new Task(1, player, false) {
								@Override
								protected void execute() {
									if (gameObject.getPosition().getX() == 2547
											&& gameObject.getPosition().getY() == 9951) {
										player.moveTo(new Position(2548, 3551));
									} else if (gameObject.getPosition().getX() == 3005
											&& gameObject.getPosition().getY() == 10363) {
										player.moveTo(new Position(3005, 3962));
									} else if (gameObject.getPosition().getX() == 3084
											&& gameObject.getPosition().getY() == 9672) {
										player.moveTo(new Position(3117, 3244));
									} else if (gameObject.getPosition().getX() == 3116
											&& gameObject.getPosition().getY() == 9852) {
										player.moveTo(new Position(3295, 4070, 0));
									} else if (gameObject.getPosition().getX() == 3114
											&& gameObject.getPosition().getY() == 9843
											&& gameObject.getPosition().getZ() == 4) {
										player.moveTo(new Position(3295, 4070, 0));
									} else if (gameObject.getPosition().getX() == 3097
											&& gameObject.getPosition().getY() == 9867) {
										player.moveTo(new Position(3096, 3468));
									}
									stop();
								}
							});

							break;

						case 5110:
							player.moveTo(new Position(2647, 9557));
							player.getPacketSender().sendMessage("You pass the stones..");
							break;

						case 6606:
							player.moveTo(new Position(3115, 9843, 4));
							player.getPacketSender().sendMessage("@blu@Another room full of zombie hill giants.");
							break;
						case 5111:
							player.moveTo(new Position(2649, 9562));
							player.getPacketSender().sendMessage("You pass the stones..");
							break;
						case 6434:
							player.performAnimation(new Animation(827));
							player.getPacketSender().sendMessage("You enter the trapdoor..");
							TaskManager.submit(new Task(1, player, false) {
								@Override
								protected void execute() {
									player.moveTo(new Position(3085, 9672));
									stop();
								}
							});
							break;
						case 19187:
						case 19175:
							Hunter.dismantle(player, gameObject);
							break;
						case 25029:
							PuroPuro.goThroughWheat(player, gameObject);
							break;
						case 47976:
							Nomad.endFight(player, false);
							break;
						case 2182:
							if (!player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0)) {
								player.getPacketSender()
										.sendMessage("You have no business with this chest. Talk to the Gypsy first!");
								return;
							}
							RecipeForDisaster.openRFDShop(player);
							break;
						case 12356:
							if (!player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0)) {
								player.getPacketSender()
										.sendMessage("You have no business with this portal. Talk to the Gypsy first!");
								return;
							}
							if (player.getPosition().getZ() > 0) {
								RecipeForDisaster.leave(player);
							} else {
								player.getMinigameAttributes().getRecipeForDisasterAttributes().setPartFinished(1,
										true);
								RecipeForDisaster.enter(player);
							}
							break;
						case 9369:
							if (player.getPosition().getY() > 5175) {
								FightPit.addPlayer(player);
							} else {
								FightPit.removePlayer(player, "leave room");
							}
							break;
						case 9368:
							if (player.getPosition().getY() < 5169) {
								FightPit.removePlayer(player, "leave game");
							}
							break;
						case 357:

							break;
						case 1:

							break;
						case 9357:
							FightCave.leaveCave(player, false);
							break;
						case 9356:
							FightCave.enterCave(player);
							break;
						case 6704:
							player.moveTo(new Position(3577, 3282, 0));
							break;
						case 6706:
							player.moveTo(new Position(3554, 3283, 0));
							break;
						case 6705:
							player.moveTo(new Position(3566, 3275, 0));
							break;
						case 6702:
							player.moveTo(new Position(3564, 3289, 0));
							break;
						case 6703:
							player.moveTo(new Position(3574, 3298, 0));
							break;
						case 6707:
							player.moveTo(new Position(3556, 3298, 0));
							break;
						case 3203:
							if (player.getLocation() == Location.DUEL_ARENA && player.getDueling().duelingStatus == 5) {
								if (Dueling.checkRule(player, DuelRule.NO_FORFEIT)) {
									player.getPacketSender().sendMessage("Forfeiting has been disabled in this duel.");
									return;
								}
								player.getCombatBuilder().reset(true);
								if (player.getDueling().duelingWith > -1) {
									Player duelEnemy = World.getPlayers().get(player.getDueling().duelingWith);
									if (duelEnemy == null)
										return;
									duelEnemy.getCombatBuilder().reset(true);
									duelEnemy.getMovementQueue().reset();
									duelEnemy.getDueling().duelVictory();
								}
								player.moveTo(new Position(3368 + RandomUtility.getRandom(5),
										3267 + RandomUtility.getRandom(3), 0));
								player.getDueling().reset();
								player.getCombatBuilder().reset(true);
								player.restart();
							}
							break;
						case 14315:
							PestControl.boardBoat(player);
							break;

						case 14314:
							if (player.getLocation() == Location.PEST_CONTROL_BOAT) {
								player.getLocation().leave(player);
							}
							break;
						case 1738:
							if (player.getLocation() == Location.WARRIORS_GUILD) {
								player.moveTo(new Position(2840, 3539, 2));
							} else {
								player.getPacketSender().sendMessage("Nothing interesting happens.");
							}
							break;
						case 15638:
							player.moveTo(new Position(3199, 3212, 0));
							break;
						case 15644:
						case 15641:
							switch (player.getPosition().getZ()) {
							case 0:
								player.moveTo(new Position(2855, player.getPosition().getY() >= 3546 ? 3545 : 3546));
								break;
							case 2:
								if (player.getPosition().getX() == 2846) {
									if (player.getInventory().getAmount(8851) < 70) {
										player.getPacketSender()
												.sendMessage("You need at least 70 tokens to enter this area.");
										return;
									}
									DialogueManager.start(player, WarriorsGuild.warriorsGuildDialogue(player));
									player.moveTo(new Position(2847, player.getPosition().getY(), 2));
									WarriorsGuild.handleTokenRemoval(player);
								} else if (player.getPosition().getX() == 2847) {
									WarriorsGuild.resetCyclopsCombat(player);
									player.moveTo(new Position(2846, player.getPosition().getY(), 2));
									player.getMinigameAttributes().getWarriorsGuildAttributes()
											.setEnteredTokenRoom(false);
								}
								break;
							}
							break;
						case 28714:
							player.performAnimation(new Animation(828));
							player.delayedMoveTo(new Position(3089, 3492), 2);
							break;
						case 1746:
							player.performAnimation(new Animation(827));
							player.delayedMoveTo(new Position(2209, 5348), 2);
							break;
						case 19191:
						case 19189:
						case 19180:
						case 19184:
						case 19182:
						case 19178:
							Hunter.lootTrap(player, gameObject);
							break;
						case 13493:
							Stalls.stealFromStall(player, 99, 11230, 10835, "You steal a taxbag.");
							break;
						case 3192:
							player.setDialogueActionId(11);
							DialogueManager.start(player, 20);
							break;
						case 28716:
							// if(!player.busy()) {
							// player.getSkillManager().updateSkill(Skill.SUMMONING);
							// player.getPacketSender().sendInterface(63471);
							// } else
							// player.getPacketSender().sendMessage("Please finish what you're doing before
							// opening this.");
							// break;
							player.getPacketSender().sendMessage("Summoning is currently disabled check back later.");
							break;
						case 2:
							player.moveTo(new Position(player.getPosition().getX() > 2690 ? 2687 : 2694, 3714));
							player.getPacketSender().sendMessage("You walk through the entrance..");
							break;
						case 2026:
						case 2028:
						case 2029:
						case 2030:
						case 2031:
							player.setEntityInteraction(gameObject);
							Fishing.setupFishing(player, Fishing.forSpot(gameObject.getId(), false));
							return;
						case 12692:
						case 2783:
						case 4306:
							player.setInteractingObject(gameObject);
							EquipmentMaking.handleAnvil(player);
							// player.getPacketSender().sendMessage("Temporarily Disabled until fixed.");

							break;
						case 2732:
							EnterAmountOfLogsToAdd.openInterface(player);
							break;
						case 409:
						case 27661:
						case 2640:
						case 36972:
							player.performAnimation(new Animation(645));
							if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) < player.getSkillManager()
									.getMaxLevel(Skill.PRAYER)) {
								player.getSkillManager().setCurrentLevel(Skill.PRAYER,
										player.getSkillManager().getMaxLevel(Skill.PRAYER), true);
								player.getPacketSender().sendMessage("You recharge your Prayer points.");
							}
							break;
						case 8749:

							player.setSpecialPercentage(100);
							CombatSpecial.updateBar(player);
							player.getPacketSender().sendMessage("Your special attack energy has been restored.");
							player.performGraphic(new Graphic(1302));
							break;
						case 4859:
							player.performAnimation(new Animation(645));
							if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) < player.getSkillManager()
									.getMaxLevel(Skill.PRAYER)) {
								player.getSkillManager().setCurrentLevel(Skill.PRAYER,
										player.getSkillManager().getMaxLevel(Skill.PRAYER), true);
								player.getPacketSender().sendMessage("You recharge your Prayer points.");
							}
							break;
						case 411:
							if (player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 30) {
								player.getPacketSender()
										.sendMessage("You need a Defence level of at least 30 to use this altar.");
								return;
							}
							player.performAnimation(new Animation(645));
							if (player.getPrayerbook() == Prayerbook.NORMAL) {
								player.getPacketSender()
										.sendMessage("You sense a surge of power flow through your body!");
								player.setPrayerbook(Prayerbook.CURSES);
							} else {
								player.getPacketSender()
										.sendMessage("You sense a surge of purity flow through your body!");
								player.setPrayerbook(Prayerbook.NORMAL);
							}
							player.getPacketSender().sendTabInterface(GameSettings.PRAYER_TAB,
									player.getPrayerbook().getInterfaceId());
							PrayerHandler.deactivateAll(player);
							CurseHandler.deactivateAll(player);
							break;
						case 6552:
							player.performAnimation(new Animation(645));
							player.setSpellbook(player.getSpellbook() == MagicSpellbook.ANCIENT ? MagicSpellbook.NORMAL
									: MagicSpellbook.ANCIENT);
							player.getPacketSender()
									.sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId())
									.sendMessage("Your magic spellbook is changed..");
							Autocasting.resetAutocast(player, true);
							break;
						case 13179:
							if (player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 40) {
								player.getPacketSender()
										.sendMessage("You need a Defence level of at least 40 to use this altar.");
								return;
							}
							player.performAnimation(new Animation(645));
							player.setSpellbook(player.getSpellbook() == MagicSpellbook.LUNAR ? MagicSpellbook.NORMAL
									: MagicSpellbook.LUNAR);
							player.getPacketSender()
									.sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId())
									.sendMessage("Your magic spellbook is changed..");
							;
							Autocasting.resetAutocast(player, true);
							break;

						case 30141:
							if (player.getInventory().contains(19871)) {
								player.performAnimation(new Animation(11229));
								player.moveTo(new Position(1656, 5684, 0));
								player.getPacketSender().sendMessage("@gre@ <shad=1>You pass the barrier");
							} else {
								player.getPacketSender()
										.sendMessage("@or2@<shad=1>You need a Charged vial to pass the barrier");
							}

							break;

						case 30146:
							if (player.getInventory().contains(19871)) {
								player.getInventory().delete(19871, 1);
								TeleportHandler.teleportPlayer(player, new Position(2016, 4825, 0),
										player.getSpellbook().getTeleportType());
								player.getPacketSender().sendMessage(
										"@gre@ <shad=1>You offer the guards the charged vial to visit the Boss room");
							} else {
								player.getPacketSender()
										.sendMessage("@or2@<shad=1>You need a Charged vial to access this");
							}

							break;

						case 13479:
							player.performAnimation(new Animation(645));
							player.forceChat("Pwnlite #1");
							if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) < player.getSkillManager()
									.getMaxLevel(Skill.PRAYER)) {
								player.getSkillManager().setCurrentLevel(Skill.PRAYER,
										player.getSkillManager().getMaxLevel(Skill.PRAYER), true);

								if (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) < player
										.getSkillManager().getMaxLevel(Skill.CONSTITUTION)) {
									player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION,
											player.getSkillManager().getMaxLevel(Skill.CONSTITUTION), true);
									player.sendMessage("@red@You feel Rejuvinated!");

								}
							}
							break;

						case 4090:
							if (player.lastSpecialClaim > System.currentTimeMillis()) {
								player.sendMessage("You can only use this command once every 30 mins!");
								player.sendMessage("You still need to wait another <col=8C33FF>"
										+ player.getLongDurationTimer(player.lastSpecialClaim));
								return;
							}
							player.performAnimation(new Animation(645));
							player.setSpecialPercentage(100);
							CombatSpecial.updateBar(player);
							player.sendMessage("@red@Your special bar is now at 100%");
							player.lastSpecialClaim = System.currentTimeMillis() + 1800000;
							break;
						case 172:
							// if (!player.getClickDelay().elapsed(2000)) {
							// player.sendMessage("@red@Please wait a few seconds before trying to open the
							// chest again.");
							// return;
							// }
							CrystalChest.handleChest(player);
							// player.getClickDelay().reset();
							break;

						case 4123:
							EmpireMinigameChest.openChest(player);
							break;

						case 12451:
							Upgradedchest.handleChest(player, gameObject);
							break;
						case 2403:
							// if (!player.getClickDelay().elapsed(2000)) {
							// player.sendMessage("@red@Please wait a few seconds before trying to open the
							// chest again.");
							// return;
							// }
							SephirothChest.openChest(player);
							// player.getClickDelay().reset();
							break;

						case 2183:
							// if (!player.getClickDelay().elapsed(2000)) {
							// player.sendMessage("@red@Please wait a few seconds before trying to open the
							// chest again.");
							// return;
							// }
							DungeonMinigameChest.openChest(player);
							// player.getClickDelay().reset();
							break;

						case 54587:
							RaidsEasyChest.openChest(player);
							break;
						case 47857:
							if (!player.getClickDelay().elapsed(2000)) {
								player.sendMessage(
										"@red@Please wait a few seconds before trying to open the chest again.");
								return;
							}
							ChristmasEventChest.openChest(player);
							player.getClickDelay().reset();
							break;
						case 7350:
							// if (!player.getClickDelay().elapsed(2000)) {
							// player.sendMessage("@red@Please wait a few seconds before trying to open the
							// chest again.");
							// return;
							// }
							OPDonationChest.openChest(player);
							// player.getClickDelay().reset();
							break;
						case 28780:
							if (player.getInventory().getAmount(6640) < 20) {
								player.getPacketSender()
										.sendMessage("You need @red@20 cash crystals@bla@ to enter the portal zone");
								return;
							}

							player.getInventory().delete(6640, 20);
							player.getPacketSender().sendMessage("@red@You feel your body get sucked into the portal");
							TeleportHandler.teleportPlayer(player, new Position(3425, 3300, 0),
									player.getSpellbook().getTeleportType());
							player.getAchievementTracker().progress(
									com.arlania.world.content.achievements.AchievementData.ENTER_THE_PORTAL_ZONE, 1);
							player.getAchievementTracker().progress(
									com.arlania.world.content.achievements.AchievementData.ENTER_THE_PORTAL_ZONE_10_TIMES,
									1);
							player.getPacketSender().sendMessage("@blu@Welcome to The Portals Zone");
							break;

						case 13635:
							TeleportHandler.teleportPlayer(player, new Position(3425, 3300, 0),
									player.getSpellbook().getTeleportType());
							break;
						case 4483:
							KeepSake.open(player, false);
							break;
						case 2995:
							// if (!player.getClickDelay().elapsed(2000)) {
							// player.sendMessage("@red@Please wait a few seconds before trying to open the
							// chest again.");
							// return;
							// }
							FantasyChest.openChest(player);
							// player.getClickDelay().reset();
							break;
						case 4170:
							// if (!player.getClickDelay().elapsed(2000)) {
							// player.sendMessage("@red@Please wait a few seconds before trying to open the
							// chest again.");
							// return;
							// }
							BloodSlayerChest.openChest(player);
							// player.getClickDelay().reset();
							break;
						case 4114:
							// if (!player.getClickDelay().elapsed(2000)) {
							// player.sendMessage("@red@Please wait a few seconds before trying to open the
							// chest again.");
							// return;
							// }
							VIPChest.openChest(player);
							// player.getClickDelay().reset();
							break;

						case 49347:
							// if (!player.getClickDelay().elapsed(2000)) {
							// player.sendMessage("@red@Please wait a few seconds before trying to open the
							// chest again.");
							// return;
							// }
							DeluxeDonatorChest.openChest(player);
							// player.getClickDelay().reset();
							break;

						case 19042:
							// if (!player.getClickDelay().elapsed(2000)) {
							// player.sendMessage("@red@Please wait a few seconds before trying to open the
							// chest again.");
							// return;
							// }
							DungeonMinigameChest.openChest(player);
							// player.getClickDelay().reset();
							break;

						case 6910:
						case 3193:
						case 2213:
						case 14382:
						case 11758:
						case 42192:
						case 75:
						case 9075:
							player.getBank(player.getCurrentBankTab()).open(true);
							break;
						}
					}
				}));
	}

	private static void secondClick(final Player player, Packet packet) {
		final int id = packet.readLEShortA();
		final int y = packet.readLEShort();
		final int x = packet.readUnsignedShortA();
		final Position position = new Position(x, y, player.getPosition().getZ());
		final GameObject gameObject = new GameObject(id, position);
		if (id > 0 && id != 6 && !RegionClipping.objectExists(gameObject)) {
			// player.getPacketSender().sendMessage("An error occured. Error code:
			// "+id).sendMessage("Please report the error to a staff member.");
			return;
		}
		player.setPositionToFace(gameObject.getPosition());
		int distanceX = (player.getPosition().getX() - position.getX());
		int distanceY = (player.getPosition().getY() - position.getY());
		if (distanceX < 0)
			distanceX = -(distanceX);
		if (distanceY < 0)
			distanceY = -(distanceY);
		int size = distanceX > distanceY ? distanceX : distanceY;
		gameObject.setSize(size);
		player.setInteractingObject(gameObject)
				.setWalkToTask(new WalkToTask(player, position, gameObject.getSize(), new FinalizedMovementTask() {
					public void execute() {
						if (MiningData.forRock(gameObject.getId()) != null) {
							Prospecting.prospectOre(player, id);
							return;
						}
						if (player.getFarming().click(player, x, y, 1))
							return;
						switch (gameObject.getId()) {

						case 2466:
						case 2467:
							KeyRoom.handleObjectClick(player, gameObject, 2);
							return;
							
						case 4163:
							player.getPerkHandler().openPerkHandler(player);
							break;

						case 28780:
							/*
							 * if (player.getEquipment().contains(7028)) if
							 * (player.getInventory().getAmount(6640) < 10) { player.getPacketSender().
							 * sendMessage("You need @red@25 cash crystals@bla@ to enter the portal zone");
							 * return; } player.getInventory().delete(6640, 10); player.getPacketSender()
							 * .sendMessage("@red@You feel your body get sucked into the portal");
							 * TeleportHandler.teleportPlayer(player, new Position(3425, 3300, 0),
							 * player.getSpellbook().getTeleportType());
							 * player.getAchievementTracker().progress(com.arlania.world.content.
							 * achievements.AchievementData.ENTER_THE_PORTAL_ZONE, 1);
							 * player.getAchievementTracker().progress(com.arlania.world.content.
							 * achievements.AchievementData.ENTER_THE_PORTAL_ZONE_10_TIMES, 1);
							 */ player.getPacketSender().sendMessage("@red@This option is currently disabled.");
							break;

						// case 884:
						// player.setDialogueActionId(41);
						// player.setInputHandling(new DonateToWell());
						// player.getPacketSender().sendInterfaceRemoval().sendEnterAmountPrompt("How
						// much money would you like to contribute with?");
						// break;
						case 172:
							int[] starterrewards = { 13258, 13259, 13256, 17849, 19137, 19138, 19139, 5130, 19131,
									19132, 19133, 18865, 15398, 2755, 2749, 2750, 2751, 2752, 2753, 2754, 19721, 19722,
									19723, 19734, 19736, 15332, 15373, 18392, 19080, 10835, 19864, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
							player.getPacketSender().sendInterface(65000);
							for (int i = 0; i < starterrewards.length; i++)
								player.getPacketSender().sendItemOnInterface(65002, starterrewards[i], i, 1);
							break;

						case 3662:
							int[] superiorrewards = { 989, 1543, 1464, 6507, 19886, 298, 6199, 3912, 6640, 9943, 6191,
									6192, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
							player.getPacketSender().sendInterface(65000);
							for (int i = 0; i < superiorrewards.length; i++)
								player.getPacketSender().sendItemOnInterface(65002, superiorrewards[i], i, 1);
							break;

						case 3449:
							int[] superior1rewards = { 989, 1543, 1464, 6507, 19886, 298, 6199, 3912, 6640, 9943, 6191,
									6192, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
							player.getPacketSender().sendInterface(65000);
							for (int i = 0; i < superior1rewards.length; i++)
								player.getPacketSender().sendItemOnInterface(65002, superior1rewards[i], i, 1);
							break;

						case 5542:
							int[] summerrewards = { 20920, 20921, 20922, 20924, 20925, 20926, 6507, 298, 4670, 4671,
									4672, 4673, 6666, 17743, 17840, 5185, 5266, 14249, 20927, 16543, 13691, 3254, 8498,
									8490, 8491, 8492, 8493, 8494, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1 };
							player.getPacketSender().sendInterface(65000);
							for (int i = 0; i < summerrewards.length; i++)
								player.getPacketSender().sendItemOnInterface(65002, summerrewards[i], i, 1);
							break;

						case 4170:
							int[] bloodrewards = { 10205, 19935, 16455, 15374, 19106, 2546, 2545, 2547, 2548, 5184,
									14546, 6509, 6505, 14033, 10168, 13997, 3949, 3950, 3952, 5020, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1 };
							player.getPacketSender().sendInterface(65000);
							for (int i = 0; i < bloodrewards.length; i++)
								player.getPacketSender().sendItemOnInterface(65002, bloodrewards[i], i, 1);
							break;
						case 2213:
							if (player.isIronMan()) {
								player.getPacketSender().sendMessage(
										"Ironman-players are not allowed to buy items from the general-store.");
								player.getAchievementTracker()
										.progress(com.arlania.world.content.achievements.AchievementData.VISIT_POS, 1);
								return;
							}
							if (player.getLocation() == Location.DUNGEONEERING
									|| player.getLocation() == Location.DUEL_ARENA) {
								player.getPacketSender().sendMessage("You can't open the player shops right now!");
							} else {
								player.getPlayerOwnedShopManager().options();
								return;
							}
						case 4407: // scorpia boss
							TeleportHandler.teleportPlayer(player, new Position(2717, 9248, 0),
									player.getSpellbook().getTeleportType());
							break;
						case 4123:
							int[] itemList20 = { 19935, 19936, 16455, 14249, 19821, 19958, 13999, 12426, 4670, 4671,
									4672, 4673, 6640, 19890, 5266, 4780, 4652, 3918, 3277, 10168, 10169, 4082, 3966,
									6507, 3967, 3968, 3969, 3970, 4742, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1 };
							player.getPacketSender().sendInterface(65000);
							for (int i = 0; i < itemList20.length; i++)
								player.getPacketSender().sendItemOnInterface(65002, itemList20[i], i, 1);
							break;

						case 49347:
							int[] itemList12 = { 10835, 85, 989, 19670, 19626, 19140, 5184, 18950, 18749, 9943, 17413,
									19936, 2547, 4762, 4763, 4764, 4761, 5089, 15374, 19618, 19620, 19691, 19692, 19693,
									19694, 19695, 19696, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1 };
							player.getPacketSender().sendInterface(65000);
							for (int i = 0; i < itemList12.length; i++)
								player.getPacketSender().sendItemOnInterface(65002, itemList12[i], i, 1);
							break;

						case 7350:
							int[] rewards = { 3912, 6199, 3988, 5184, 6507, 19935, 19886, 17933, 12162, 18950, 10168,
									14546, 6505, 6509, 13998, 15374, 19936, 5185, 16455, 3317, 19106, 2547, 2546, 2545,
									3647, 5163, 19821, 19958, 12426, 934, 13997, 4670, 4671, 4672, 4673, 10205, 4742,
									13999, 3282, 773, 774, 5132, 11978, 15566, 10905, 4803, 3277, 19938, 8654, 5197,
									5170, 19890, 3961, 16456, 13691, 9116, 9117, 5020, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1 };
							player.getPacketSender().sendInterface(65000);
							for (int i = 0; i < rewards.length; i++)
								player.getPacketSender().sendItemOnInterface(65002, rewards[i], i, 1);
							break;
						case 6420:
							int[] itemList9 = { 18377, 15418, 19468, 2572, 16137, 11076, 18363, 18380, 18381, 18382,
									18383, 18384, 18385, 3077, 18380, 18381, 18382, 18383, 18384, 18385, 1499, 4799,
									4800, 4801, 3951, 15012, 3951, 3316, 3931, 3958, 3959, 3960, 5187, 14559, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
							player.getPacketSender().sendInterface(65000);
							for (int i = 0; i < itemList9.length; i++)
								player.getPacketSender().sendItemOnInterface(65002, itemList9[i], i, 1);
							break;
						case 2995:
							int[] itemList10 = { 4671, 4672, 4673, 4670, 10835, 923, 5167, 15649, 15650, 15653, 4765,
									5089, 930, 15045, 5210, 926, 931, 5211, 4781, 4782, 20240, 4785, 5195, 15032, 3321,
									16429, 4780, 932, 12426, 19935, 6450, 6451, 6452, 6480, 6481, 18950, 3988, 2547, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
									-1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
							player.getPacketSender().sendInterface(65000);
							for (int i = 0; i < itemList10.length; i++)
								player.getPacketSender().sendItemOnInterface(65002, itemList10[i], i, 1);
							break;
						case 21505:
						case 21507:
							player.moveTo(new Position(2328, 3804));
							break;
						case 2646:
						case 312:
							if (!player.getClickDelay().elapsed(1200))
								return;
							if (player.getInventory().isFull()) {
								player.getPacketSender().sendMessage("You don't have enough free inventory space.");
								return;
							}
							String type = gameObject.getId() == 312 ? "Potato" : "Flax";
							player.performAnimation(new Animation(827));
							player.getInventory().add(gameObject.getId() == 312 ? 1942 : 1779, 1);
							player.getPacketSender().sendMessage("You pick some " + type + "..");
							gameObject.setPickAmount(gameObject.getPickAmount() + 1);
							if (RandomUtility.getRandom(3) == 1 && gameObject.getPickAmount() >= 1
									|| gameObject.getPickAmount() >= 6) {
								player.getPacketSender().sendClientRightClickRemoval();
								gameObject.setPickAmount(0);
								CustomObjects.globalObjectRespawnTask(new GameObject(-1, gameObject.getPosition()),
										gameObject, 10);
							}
							player.getClickDelay().reset();
							break;
						case 2644:
							Flax.showSpinInterface(player);
							break;
						case 6:
							DwarfCannon cannon = player.getCannon();
							if (cannon == null || cannon.getOwnerIndex() != player.getIndex()) {
								player.getPacketSender().sendMessage("This is not your cannon!");
							} else {
								DwarfMultiCannon.pickupCannon(player, cannon, false);
							}
							break;
						case 4875:
							Stalls.stealFromStall(player, 1, 5100, 10835, "You steal a banana.");
							break;
						case 4874:
							Stalls.stealFromStall(player, 30, 6130, 10835, "You steal a golden ring.");
							break;
						case 4876:
							Stalls.stealFromStall(player, 60, 7370, 10835, "You steal a damaged hammer.");
							break;
						case 4877:
							Stalls.stealFromStall(player, 65, 7990, 10835, "You steal a staff.");
							break;
						case 4878:
							Stalls.stealFromStall(player, 80, 9230, 10835, "You steal a scimitar.");
							break;

						case 6189:
						case 26814:
						case 11666:
							Smelting.openInterface(player);
							break;
						case 2152:
							player.performAnimation(new Animation(8502));
							player.performGraphic(new Graphic(1308));
							player.getSkillManager().setCurrentLevel(Skill.SUMMONING,
									player.getSkillManager().getMaxLevel(Skill.SUMMONING));
							player.getPacketSender().sendMessage("You renew your Summoning points.");
							break;
						}
					}
				}));
	}

	private static void fifthClick(final Player player, Packet packet) {
		final int id = packet.readUnsignedShortA();
		final int y = packet.readUnsignedShortA();
		final int x = packet.readShort();
		final Position position = new Position(x, y, player.getPosition().getZ());
		final GameObject gameObject = new GameObject(id, position);
		if (!Construction.buildingHouse(player)) {
			if (id > 0 && !RegionClipping.objectExists(gameObject)) {
				// player.getPacketSender().sendMessage("An error occured. Error code:
				// "+id).sendMessage("Please report the error to a staff member.");
				return;
			}
		}
		player.setPositionToFace(gameObject.getPosition());
		int distanceX = (player.getPosition().getX() - position.getX());
		int distanceY = (player.getPosition().getY() - position.getY());
		if (distanceX < 0)
			distanceX = -(distanceX);
		if (distanceY < 0)
			distanceY = -(distanceY);
		int size = distanceX > distanceY ? distanceX : distanceY;
		gameObject.setSize(size);
		player.setInteractingObject(gameObject);
		player.setWalkToTask(new WalkToTask(player, position, gameObject.getSize(), new FinalizedMovementTask() {
			@Override
			public void execute() {
				switch (id) {
				}
				Construction.handleFifthObjectClick(x, y, id, player);
			}
		}));
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.isTeleporting() || player.isPlayerLocked() || player.getMovementQueue().isLockMovement())
			return;
		switch (packet.getOpcode()) {
		case FIRST_CLICK:
			firstClick(player, packet);
			break;
		case SECOND_CLICK:
			secondClick(player, packet);
			break;
		case THIRD_CLICK:
			// thirdClick(player, packet);
			break;
		case FOURTH_CLICK:
			// fourthClick(player, packet);
			break;
		case FIFTH_CLICK:
			fifthClick(player, packet);
			break;
		}
	}

	public static final int FIRST_CLICK = 132, SECOND_CLICK = 252, THIRD_CLICK = 70, FOURTH_CLICK = 234,
			FIFTH_CLICK = 228;
}
