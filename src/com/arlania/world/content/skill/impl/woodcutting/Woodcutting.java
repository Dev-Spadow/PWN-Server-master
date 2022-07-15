package com.arlania.world.content.skill.impl.woodcutting;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.Animation;
import com.arlania.model.GameObject;
import com.arlania.model.Skill;
import com.arlania.model.container.impl.Equipment;
import com.arlania.util.Misc;
import com.arlania.util.RandomUtility;
import com.arlania.world.World;
import com.arlania.world.content.Achievements;
import com.arlania.world.content.CustomObjects;
import com.arlania.world.content.EvilTrees;
import com.arlania.world.content.Sounds;
import com.arlania.world.content.StarterTasks;
import com.arlania.world.content.Achievements.AchievementData;
import com.arlania.world.content.Sounds.Sound;
import com.arlania.world.content.StarterTasks.StarterTaskData;
import com.arlania.world.content.dailytasks.DailyTasks;
import com.arlania.world.content.dailytasks.TaskType;
import com.arlania.world.content.skill.impl.firemaking.Logdata;
import com.arlania.world.content.skill.impl.firemaking.Logdata.logData;
import com.arlania.world.content.skill.impl.woodcutting.WoodcuttingData.Hatchet;
import com.arlania.world.content.skill.impl.woodcutting.WoodcuttingData.Trees;
import com.arlania.world.entity.impl.player.Player;

public class Woodcutting {

	public static void cutWood(final Player player, final GameObject object, boolean restarting) {
		if (!restarting)
			player.getSkillManager().stopSkilling();
		if (player.getInventory().getFreeSlots() == 0) {
			player.getPacketSender().sendMessage("You don't have enough free inventory space.");
			return;
		}
		player.setPositionToFace(object.getPosition());
		final int objId = object.getId();
		final Hatchet h = Hatchet.forId(WoodcuttingData.getHatchet(player));
		if (Misc.getRandom(25000) == 3) {
			player.getInventory().add(13322, 1);
			World.sendMessage("@blu@<img=10>[Skilling Pets] " + player.getUsername() + " has received the Beaver pet!");
			player.getPacketSender().sendMessage("@red@You have received a skilling pet!");
		}
		if (h != null) {
			if (player.getSkillManager().getCurrentLevel(Skill.WOODCUTTING) >= h.getRequiredLevel()) {
				final WoodcuttingData.Trees t = WoodcuttingData.Trees.forId(objId);
				if (t != null) {
					player.setEntityInteraction(object);
					//player.getInventory().add(10835, 2);
					if (player.getSkillManager().getCurrentLevel(Skill.WOODCUTTING) >= t.getReq()) {
						player.performAnimation(new Animation(h.getAnim()));
						int delay = Misc.getRandom(t.getTicks() - WoodcuttingData.getChopTimer(player, h)) + 1;
						player.setCurrentTask(new Task(1, player, false) {
							int cycle = 0, reqCycle = delay >= 2 ? delay : Misc.getRandom(1) + 1;

							@Override
							public void execute() {
								if (player.getInventory().getFreeSlots() == 0) {
									player.performAnimation(new Animation(65535));
									player.getPacketSender().sendMessage("You don't have enough free inventory space.");
									this.stop();
									return;
								}
								if (cycle != reqCycle) {
									cycle++;
									player.performAnimation(new Animation(h.getAnim()));
								} else if (cycle >= reqCycle) {
									int xp = t.getXp();
                                    player.getInventory().add(10835, 1);

                                    if (lumberJack(player))
										xp *= 1.5;

									player.getSkillManager().addExperience(Skill.WOODCUTTING, xp);
									if (player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 6739) {
										if (Misc.getRandom(3) == 1) {
											xp *= 1.5;
											player.getSkillManager().addExperience(Skill.WOODCUTTING, xp);

										}
									}
									cycle = 0;
									BirdNests.dropNest(player);
									this.stop();
									if (object.getId() == 11434) {
										if (EvilTrees.SPAWNED_TREE == null || EvilTrees.SPAWNED_TREE.getTreeObject()
												.getCutAmount() >= EvilTrees.MAX_CUT_AMOUNT) {
											player.getPacketSender().sendClientRightClickRemoval();
											player.getSkillManager().stopSkilling();
											return;
										} else {
											EvilTrees.SPAWNED_TREE.getTreeObject().incrementCutAmount();
										}
										// } else {
										// player.performAnimation(new Animation(65535));
									}
									if (!t.isMulti()) {
										// player.performAnimation(new Animation(65535));
										if (object.getId() == 11434) {
											return;
										}
										treeRespawn(player, object);
										player.getPacketSender().sendMessage("You've chopped the tree down.");
										StarterTasks.doProgress(player, StarterTaskData.CUT_1000_LOGS, 1);
									} else {
										cutWood(player, object, true);
										if (t == Trees.EVIL_TREE) {
											player.getPacketSender().sendMessage("You cut the Evil Tree...");
										} else {
											//StarterTasks.doProgress(player, StarterTaskData.CHOP_500_TREES, 100);
											player.getPacketSender().sendMessage("You get some logs..");
											StarterTasks.doProgress(player, StarterTaskData.CUT_1000_LOGS);
										}
									}
									Sounds.sendSound(player, Sound.WOODCUT);
									final boolean success = Misc.getRandom(5) <= 2;
									final boolean doubleLoot = Misc.getRandom(3) == 1;
									if (success) {
                                        DailyTasks.INSTANCE.updateTaskProgress(TaskType.WOODCUTTING, player, t.getReward(), doubleLoot ? 2 : 1);
                                    }
									if (!infernoAdze(player) && success) {
										player.getInventory().add(t.getReward(), 1);
										if (player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 6739) {
											if (doubleLoot) {
												player.getInventory().add(t.getReward(), 1);
											}
										}
									} else if (success) {
										logData fmLog = Logdata.getLogData(player, t.getReward());
										if (fmLog != null) {
											player.getSkillManager().addExperience(Skill.FIREMAKING, fmLog.getXp());
											player.getPacketSender().sendMessage(
													"Your Inferno Adze burns the log, granting you Firemaking experience.");
											if (fmLog == Logdata.logData.OAK) {
												
											} else if (fmLog == Logdata.logData.MAGIC) {
											}
										}
									}
									int chance = RandomUtility.inclusiveRandom(40000);
									int chance1 = RandomUtility.inclusiveRandom(30000);
									int chance2 = RandomUtility.inclusiveRandom(30000);
									if (t == Trees.MAGIC || t == Trees.CUSTOM_TREE) {
										if (chance >= 39999) {
									    World.sendMessage(
									            "<col=fcfcfc><shad=dd8a14><img=418>[Afk Tree]</shad>@bla@: " + player.getUsername()
									                    + " just received a Owner Mystery box from @red@woodcutting");
									    player.getInventory().add(10168, 1);
									}
										if (chance1 >= 29999) {
										    World.sendMessage(
										            "<col=fcfcfc><shad=dd8a14><img=418>[Afk Tree]</shad>@bla@: " + player.getUsername()
										                    + " just received a Owner Mystery box from @red@woodcutting");
										    player.getInventory().add(6503, 1);
										}
										if (chance2 >= 29999) {
										    World.sendMessage(
										            "<col=fcfcfc><shad=dd8a14><img=418>[Afk Tree]</shad>@bla@: " + player.getUsername()
										                    + " just received a Owner Mystery box from @red@woodcutting");
										    player.getInventory().add(6504, 1);
										}
									}
									if (t == Trees.OAK) {
										
									} else if (t == Trees.CUSTOM_TREE) {
									}
								}
							}
						});
						TaskManager.submit(player.getCurrentTask());
					} else {
						player.getPacketSender().sendMessage(
								"You need a Woodcutting level of at least " + t.getReq() + " to cut this tree.");
					}
				}
			} else {
				player.getPacketSender()
						.sendMessage("You do not have a hatchet which you have the required Woodcutting level to use.");
			}
		} else {
			player.getPacketSender().sendMessage("You do not have a hatchet that you can use.");
		}
	}

	public static boolean lumberJack(Player player) {
		return player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 10941
				&& player.getEquipment().get(Equipment.BODY_SLOT).getId() == 10939
				&& player.getEquipment().get(Equipment.LEG_SLOT).getId() == 10940
				&& player.getEquipment().get(Equipment.FEET_SLOT).getId() == 10933;
	}

	public static boolean infernoAdze(Player player) {
		return false; //player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 13661;
	}

	public static void treeRespawn(final Player player, final GameObject oldTree) {
		if (oldTree == null || oldTree.getPickAmount() >= 1)
			return;
		oldTree.setPickAmount(1);
		for (Player players : player.getLocalPlayers()) {
			if (players == null)
				continue;
			if (players.getInteractingObject() != null && players.getInteractingObject().getPosition()
					.equals(player.getInteractingObject().getPosition().copy())) {
				players.getSkillManager().stopSkilling();
				players.getPacketSender().sendClientRightClickRemoval();
			}
		}
		player.getPacketSender().sendClientRightClickRemoval();
		player.getSkillManager().stopSkilling();
		CustomObjects.globalObjectRespawnTask(new GameObject(1343, oldTree.getPosition().copy(), 10, 0), oldTree,
				20 + Misc.getRandom(10));
	}

}
