package com.arlania.world.content.skill.impl.slayer;

import com.arlania.model.GameMode;
import com.arlania.model.Item;
import com.arlania.model.Locations;
import com.arlania.model.PlayerRights;
import com.arlania.model.Position;
import com.arlania.model.Skill;
import com.arlania.model.container.impl.Equipment;
import com.arlania.model.container.impl.Shop.ShopManager;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.model.definitions.NpcDefinition;
import com.arlania.util.Misc;
import com.arlania.util.RandomUtility;
import com.arlania.world.World;
import com.arlania.world.content.Achievements;
import com.arlania.world.content.PlayerPanel;
import com.arlania.world.content.Achievements.AchievementData;
import com.arlania.world.content.Emotes.*;
import com.arlania.world.content.dialogue.DialogueManager;
import com.arlania.world.content.event.SpecialEvents;
import com.arlania.world.content.transportation.TeleportHandler;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

public class BloodSlayer {

	private Player player;

	public BloodSlayer(Player p) {
		this.player = p;
	}

	private BloodSlayerTasks bloodslayerTask = BloodSlayerTasks.NO_TASK, lastTask = BloodSlayerTasks.NO_TASK;
	private BloodSlayerMaster bloodSlayerMaster = BloodSlayerMaster.IMP;
	private int amountToSlay, taskStreak;
	private String duoPartner, duoInvitation;

	public void assignBloodSlayerTask() {
		boolean hasTask = getBloodSlayerTask() != BloodSlayerTasks.NO_TASK && player.getBloodSlayer().getLastTask() != getBloodSlayerTask();
		boolean duoSlayer = duoPartner != null;
		if(hasTask) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		int[] taskData = BloodSlayerTasks.getNewBloodTaskData(bloodSlayerMaster, player);
		int bloodslayerTaskId = taskData[0], bloodslayerTaskAmount = taskData[1];
		BloodSlayerTasks taskToSet = BloodSlayerTasks.forId(bloodslayerTaskId);
		if(taskToSet == player.getBloodSlayer().getLastTask() || NpcDefinition.forId(taskToSet.getNpcId()).getSlayerLevel() > player.getSkillManager().getMaxLevel(Skill.SLAYER)) {
			assignBloodSlayerTask();
			return;
		}
		player.getPacketSender().sendInterfaceRemoval();
		this.amountToSlay = bloodslayerTaskAmount;
		this.bloodslayerTask = taskToSet;
		DialogueManager.start(player, BloodSlayerDialogues.receivedBloodTask(player, getBloodSlayerMaster(), getBloodSlayerTask()));
		PlayerPanel.refreshPanel(player);
		if(duoSlayer) {
			Player duo = World.getPlayerByName(duoPartner);
			duo.getBloodSlayer().setBloodSlayerTask(taskToSet);
			duo.getBloodSlayer().setAmountToSlay(bloodslayerTaskAmount);
			duo.getPacketSender().sendInterfaceRemoval();
		//	DialogueManager.start(duo, SlayerDialogues.receivedTask(duo, slayerMaster, taskToSet));
			PlayerPanel.refreshPanel(duo);
		}
	}

	public void resetBloodSlayerTask() {
		BloodSlayerTasks task = getBloodSlayerTask();
		if(task == BloodSlayerTasks.NO_TASK)
			return;
		this.bloodslayerTask = BloodSlayerTasks.NO_TASK;
		this.amountToSlay = 0;
		this.taskStreak = 0;
		player.getPointsHandler().setBloodSlayerPoints(player.getPointsHandler().getBloodSlayerPoints() - 25, false);
		PlayerPanel.refreshPanel(player);
		Player duo = duoPartner == null ? null : World.getPlayerByName(duoPartner);
		if(duo != null) {
			duo.getBloodSlayer().setBloodSlayerTask(BloodSlayerTasks.NO_TASK).setAmountToSlay(0).setTaskStreak(0);
			duo.getPacketSender().sendMessage("Your partner exchanged 25 Slayer points to reset your team's Slayer task.");
			PlayerPanel.refreshPanel(duo);
			player.getPacketSender().sendMessage("You've successfully reset your team's Slayer task.");
		} else {
			player.getPacketSender().sendMessage("Your Slayer task has been reset.");
		}
	}

	public void killedNpc(NPC npc) {
		if(bloodslayerTask != BloodSlayerTasks.NO_TASK) {
			if(bloodslayerTask.getNpcId() == npc.getId()) {
				handleBloodSlayerTaskDeath(true);
				//if(duoPartner != null) {
					//Player duo = World.getPlayerByName(duoPartner);
					//if(duo != null) {
						//if(checkDuoSlayer(player, false)) {
							//duo.getBloodSlayer().handleBloodSlayerTaskDeath(Locations.goodDistance(player.getPosition(), duo.getPosition(), 20));
						} else {
							//resetDuo(player, duo);
						}
					}
				}


	public void handleBloodSlayerTaskDeath(boolean giveXp) {
		int xp = bloodslayerTask.getXP() + Misc.getRandom(bloodslayerTask.getXP()/5);
		if(amountToSlay > 1) {
			amountToSlay--;
		} else {
			player.getPacketSender().sendMessage("").sendMessage("<img=458>You've completed your Slayer task! Return to a Slayer master for another one.");
			taskStreak++;
			if(bloodslayerTask.getTaskMaster() == SlayerMaster.IMP) {
				player.getAchievementTracker().progress(com.arlania.world.content.achievements.AchievementData.COMPLETE_AN_BLOODSLAYER_TASK, 1);

			}
			lastTask = bloodslayerTask;
			bloodslayerTask = BloodSlayerTasks.NO_TASK;
			amountToSlay = 0;
			givePoints(bloodSlayerMaster);
		}

		if(giveXp) {
			player.getSkillManager().addExperience(Skill.SLAYER, doubleSlayerXP ? xp * 2 : xp);
		}
		
		PlayerPanel.refreshPanel(player);
	}

	@SuppressWarnings("incomplete-switch")
	public void givePoints(BloodSlayerMaster bloodmaster) {
		int pointsReceived = pointsReceived = 10 + Misc.random(15);
		int petbonus = pointsReceived = 10 + Misc.random(15);
		switch(bloodmaster) {
		case IMP:
			pointsReceived = 10 + Misc.random(15);
			break;
		}

		
		
		if (Skillcape_Data.SLAYER.isWearingCape(player)) {
			pointsReceived += pointsReceived + 2;
		}
		int per5 = pointsReceived * 2;
		int per10 = pointsReceived * 3;
		
		int pet5 = pointsReceived * 4;
		int pet10 = pointsReceived * 6;
		
		//int per50 = pointsReceived * 5;
		double custom1 = pointsReceived * 0.75;
		double custom2 = pointsReceived * 1.25;
		double custom3 = pointsReceived * 2.0;

		
		if (player.getEquipment().getItems()[Equipment.AMULET_SLOT].getId() == 17291) {
			pointsReceived = pointsReceived * 2; //blood neck perk
		}

		int random = RandomUtility.exclusiveRandom(250);
		int random1 = RandomUtility.exclusiveRandom(1000);
		int random2 = RandomUtility.exclusiveRandom(750);
		int random3 = RandomUtility.exclusiveRandom(7);
		int random4 = RandomUtility.exclusiveRandom(50);
		
		if (random3 == 5) {
			player.getInventory().add(5205, 1);
			player.sendMessage("<img=458>Congratulations! x1 Bloodslayer key was given for your effort.");
		}
		
		if (random == 50) {
			player.getInventory().add(17750, 300);
			player.sendMessage("<img=458>Congratulations! x300 Bloodbags <img=457> has been added to your inventory");
			World.sendMessage("<col=fcfcfc><shad=dd8a14><img=418>[Blood Slayer]</shad>@bla@: " + player.getUsername().toString() + " @blu@Has received x300 Blood money <img=457>");
		}
		if (random1 == 50) {
			player.getInventory().add(14924, 1);
			player.sendMessage("<img=458>Congratulations! x1 Blood genie pet has been added to your inventory");
			World.sendMessage("<col=fcfcfc><shad=dd8a14><img=458>[Blood Slayer]</shad>@bla@: " + player.getUsername().toString() + " Has received a @red@Blood Genie pet!");
		}
		if (random == 70) {
			player.getInventory().add(17750, 500);
			player.sendMessage("<img=458>Congratulations! x500 Blood bags <img=457> has been added to your inventory");
			World.sendMessage("<col=fcfcfc><shad=dd8a14><img=458>[Blood Slayer]</shad>@bla@: " + player.getUsername().toString() + " @blu@Has received x500 Blood bags <img=457>");
		}
		
		if (random == 100) {
			player.getInventory().add(17750, 750);
			player.sendMessage("<img=458>Congratulations! 750 Blood bags <img=457> has been added to your inventory");
			World.sendMessage("<col=fcfcfc><shad=dd8a14><img=458>[Blood Slayer]</shad>@bla@: " + player.getUsername().toString() + " @blu@Has received x750 Blood bags <img=457>");
		}
		
		
		if (random2 == 749) { //IRONMAN LOOT BOX 3
		if (player.getGameMode() == GameMode.IRONMAN) {	
			if (player.getGameMode() == GameMode.GROUP_IRONMAN) {	
		if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {	
			player.getInventory().add(14924, 1);
			player.sendMessage("<img=458>Congratulations! x1 Blood genie pet has been added to your inventory");
			World.sendMessage("<col=fcfcfc><shad=dd8a14><img=458>[Blood Slayer]</shad>@bla@: " + player.getUsername().toString() + " Has received a @red@Blood Genie pet!");
		}
	}
}	
}		
		
		
		if (random4 == 40) { //IRONMAN LOOT BOX 3
		if (player.getGameMode() == GameMode.IRONMAN) {	
			if (player.getGameMode() == GameMode.GROUP_IRONMAN) {	
		if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {	
			player.getInventory().add(5205, 1);
			player.sendMessage("<img=458>Congratulations! x1 Bloodslayer key was given for your effort.");
		}
	}
}	
}		
		
		if (random == 20) { //IRONMAN LOOT BOX 3
		if (player.getGameMode() == GameMode.IRONMAN) {	
			if (player.getGameMode() == GameMode.GROUP_IRONMAN) {
		if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {	
			player.getInventory().add(17750, 1000);
			player.sendMessage("<img=458>Congratulations! x1000 Blood bags <img=457> has been added to your inventory");
			World.sendMessage("<col=fcfcfc><shad=dd8a14><img=458>[Blood Slayer]</shad>@bla@: " + player.getUsername().toString() + "  @blu@Has received x1000 Blood bags <img=457>");
		}
	}
}	
	}
		if (random == 30) {
			if (player.getGameMode() == GameMode.IRONMAN) {	
				if (player.getGameMode() == GameMode.GROUP_IRONMAN) {
				if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
			player.getInventory().add(17750, 1500);
			player.sendMessage("<img=458>Congratulations! x1500 Blood bags <img=457> has been added to your inventory");
			World.sendMessage("<col=fcfcfc><shad=dd8a14><img=458>[Blood Slayer]</shad>@bla@: " + player.getUsername().toString() + "  @blu@Has received x1500 Blood bags <img=457>");
		}
	}
}	
		}
		if (random == 40) {
			if (player.getGameMode() == GameMode.IRONMAN) {	
				if (player.getGameMode() == GameMode.GROUP_IRONMAN) {
				if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
				player.getInventory().add(17750, 2000);
			player.sendMessage("<img=458>Congratulations! x2000 Blood bags <img=457> has been added to your inventory");
			World.sendMessage("<col=fcfcfc><shad=dd8a14><img=458>[Blood Slayer]</shad>@bla@: " + player.getUsername().toString() + "  @blu@Has received x2000 Blood bags <img=457>");
		}
	}	
	}
			}
		
		if (SpecialEvents.getDay() == SpecialEvents.WEDNESDAY) {
			pointsReceived *= 2;
		}
		
		if (SpecialEvents.getDay() == SpecialEvents.SUNDAY) {
			pointsReceived *= 2;
		}
		
		if(player.getRights() == PlayerRights.LEGENDARY_DONATOR) {
			player.getPacketSender().sendMessage("<img=458><shad=ff0909>Your Rank Boosts your task reward 10%");
			pointsReceived *= 1.1;
		}
		if(player.getRights() == PlayerRights.CELESTIAL_DONATOR) {
			player.getPacketSender().sendMessage("<img=458><shad=ff0909>Your Rank Boosts your task reward 20%");
			pointsReceived *= 1.2;
		}
		if(player.getRights() == PlayerRights.EXECUTIVE_DONATOR) {
			player.getPacketSender().sendMessage("<img=458><shad=ff0909>Your Rank Boosts your task reward 30%");
			pointsReceived *= 1.3;
		}
		if(player.getRights() == PlayerRights.SUPPORT) {
			player.getPacketSender().sendMessage("<img=458><shad=ff0909>Your Rank Boosts your task reward 50%");
			pointsReceived *=  1.3;
			}
		if(player.getRights() == PlayerRights.SUPREME_DONATOR) {
			player.getPacketSender().sendMessage("<img=458><shad=ff0909>Your Rank Boosts your task reward 40%");
			pointsReceived *=  1.4;
		
		}
		if(player.getRights() == PlayerRights.MODERATOR) {
			player.getPacketSender().sendMessage("<img=458><shad=ff0909>Your Rank Boosts your task reward 50%");
			pointsReceived *=  1.4;
			}
		if(player.getRights() == PlayerRights.DIVINE_DONATOR) {
			player.getPacketSender().sendMessage("<img=458><shad=ff0909>Your Rank Boosts your task reward 50%");
			pointsReceived *=  1.5;
			}
		if(player.getRights() == PlayerRights.ADMINISTRATOR) {
			player.getPacketSender().sendMessage("<img=458><shad=ff0909>Your Rank Boosts your task reward 50%");
			pointsReceived *=  1.5;
			}
		//Blood slayer cape
			 if(player.getEquipment().contains(14036)) {
				pointsReceived *=  2;
				player.getInventory().add(17750, Misc.inclusiveRandom(10, 20));
				player.getPacketSender().sendMessage("<img=458><shad=ff0909>Your Cape Doubles your points for this task and gives extra Blood money <img=457>");
			}
		
			 if(player.getEquipment().contains(16539)) {
				pointsReceived *=  2;
				player.getInventory().add(17750, Misc.inclusiveRandom(10, 20));
				player.getPacketSender().sendMessage("<img=458><shad=ff0909>Your Cape Doubles your points for this task and gives extra Blood money <img=457>");
			}
			 
			 if(player.getEquipment().contains(7026)) {
				pointsReceived *=  2;
				player.getInventory().add(17750, Misc.inclusiveRandom(10, 20));
				player.getPacketSender().sendMessage("<img=458><shad=ff0909>Your Cape Doubles your points for this task and gives extra Blood money <img=457>");
			}
			 if(player.getEquipment().contains(8039)) {
				pointsReceived *=  2;
				player.getInventory().add(17750, Misc.inclusiveRandom(10, 20));
				player.getPacketSender().sendMessage("<img=458><shad=ff0909>Your Cape Doubles your points for this task and gives extra Blood money <img=457>");
			}
			 if(player.getEquipment().contains(13028)) {
				pointsReceived *=  2;
				player.getInventory().add(17750, Misc.inclusiveRandom(10, 20));
				player.getPacketSender().sendMessage("<img=458><shad=ff0909>Your Cape Doubles your points for this task and gives extra Blood money <img=457>");
			}
			 
					if ((player.getSummoning() != null && player.getSummoning().getFamiliar() !=
	                    null && player.getSummoning().getFamiliar().getSummonNpc() != null 
	                    && player.getSummoning().getFamiliar().getSummonNpc().getId() == 12826)) { 
						if(player.getBloodSlayer().getTaskStreak() == 5) {
							pointsReceived *=  2;
							player.getInventory().add(17750, Misc.inclusiveRandom(26, 100));
							player.getInventory().add(10835, Misc.inclusiveRandom(500, 2000));
						}
						if(player.getBloodSlayer().getTaskStreak() == 10) {
							pointsReceived *=  2;
							player.getInventory().add(10835, Misc.inclusiveRandom(1, 3000));
							player.getInventory().add(17750, Misc.inclusiveRandom(100, 200));
						}
						pointsReceived *=  2;
						player.getInventory().add(17750, Misc.inclusiveRandom(10, 25));
						player.getPacketSender().sendMessage("Your Blood genie pet doubles your rewards earned from this task");
					}
	
					
					
			 
					 // Main method for determining roll
                    int random5 = Misc.inclusiveRandom(1, 100);
                    if (random5 >= 1 && random5 <= 50) {
                            player.getInventory().add(17750, Misc.inclusiveRandom(10, 25));
                    } else if (random5 >= 51 && random5 <= 80) {
                        player.getInventory().add(17750, Misc.inclusiveRandom(25, 50));
                    } else if (random5 >= 81 && random5 <= 94) {
                        player.getInventory().add(17750, Misc.inclusiveRandom(60, 150));
                    } else if (random5 >= 95 && random5 <= 100) {
                        player.getInventory().add(17750, Misc.inclusiveRandom(200, 220));
                        }
			 
			 

		if(player.getBloodSlayer().getTaskStreak() == 5) {
			player.getPointsHandler().setBloodSlayerPoints(per5, true);
			player.getInventory().add(17750, Misc.inclusiveRandom(26, 100));
			player.getInventory().add(10835, Misc.inclusiveRandom(500, 2000));
			player.getPacketSender().sendMessage("<img=458>You received "+per5+" Blood Slayer points.");
		} else if(player.getBloodSlayer().getTaskStreak() == 10) {
			player.getPointsHandler().setBloodSlayerPoints(per10, true);
			player.getInventory().add(10835, Misc.inclusiveRandom(1, 3000));
			player.getInventory().add(17750, Misc.inclusiveRandom(100, 200));
			player.getPacketSender().sendMessage("<img=458>You received "+per10+" Blood Slayer points.");
			player.getBloodSlayer().setTaskStreak(0);
			
				
			} else if(player.getBloodSlayer().getTaskStreak() >= 0 && player.getBloodSlayer().getTaskStreak() < 5 || player.getBloodSlayer().getTaskStreak() >= 6 && player.getBloodSlayer().getTaskStreak() < 10) {
				player.getPointsHandler().setBloodSlayerPoints(pointsReceived, true);
				
			player.getPacketSender().sendMessage("<img=458>You received "+pointsReceived+" Blood Slayer points.");
		}
		player.getPointsHandler().refreshPanel();
	}

	public void handleSlayerRingTP(int itemId) {
		if(!player.getClickDelay().elapsed(4500))
			return;
		if(player.getMovementQueue().isLockMovement())
			return;
		BloodSlayerTasks task = getBloodSlayerTask();
		if(task == BloodSlayerTasks.NO_TASK)
			return;
		Position bloodslayerTaskPos = new Position(task.getTaskPosition().getX(), task.getTaskPosition().getY(), task.getTaskPosition().getZ());
		if(!TeleportHandler.checkReqs(player, bloodslayerTaskPos))
			return;
		TeleportHandler.teleportPlayer(player, bloodslayerTaskPos, player.getSpellbook().getTeleportType());
		Item slayerRing = new Item(itemId);
		player.getInventory().delete(slayerRing);
		if(slayerRing.getId() < 13288)
			player.getInventory().add(slayerRing.getId() + 1, 1);
		else
			player.getPacketSender().sendMessage("<img=458>Your Ring of Slaying crumbles to dust.");
	}

	public int getAmountToSlay() {
		return this.amountToSlay;
	}

	public BloodSlayer setAmountToSlay(int amountToSlay) {
		this.amountToSlay = amountToSlay;
		return this;
	}

	public int getTaskStreak() {
		return this.taskStreak;
	}

	public BloodSlayer setTaskStreak(int taskStreak) {
		this.taskStreak = taskStreak;
		return this;
	}

	public BloodSlayerTasks getLastTask() {
		return this.lastTask;
	}

	public void setLastTask(BloodSlayerTasks lastTask) {
		this.lastTask = lastTask;
	}

	public boolean doubleSlayerXP = false;

	public BloodSlayer setDuoPartner(String duoPartner) {
		this.duoPartner = duoPartner;
		return this;
	}

	public String getDuoPartner() {
		return duoPartner;
	}

	public BloodSlayerTasks getBloodSlayerTask() {
		return bloodslayerTask;
	}
	



	public BloodSlayer setBloodSlayerTask(BloodSlayerTasks bloodslayerTask) {
		this.bloodslayerTask = bloodslayerTask;
		return this;
	}

	public BloodSlayerMaster getBloodSlayerMaster() {
		return bloodSlayerMaster;
	}

	public void setBloodSlayerMaster(BloodSlayerMaster bloodmaster) {
		this.bloodSlayerMaster = bloodmaster;
	}

	public void setDuoInvitation(String player) {
		this.duoInvitation = player;
	}

	public static boolean handleRewardsInterface(Player player, int button) {
		if(player.getInterfaceId() == 36000) {
			switch(button) {
			case -29534:
				player.getPacketSender().sendInterfaceRemoval();
				break;
			case -29522:
				if(player.getPointsHandler().getBloodSlayerPoints() < 10) {
					player.getPacketSender().sendMessage("<img=458>You do not have 10 Slayer points.");
					return true;
				}
				player.getPointsHandler().refreshPanel();
				player.getPointsHandler().setSlayerPoints(-10, true);
				player.getSkillManager().addExperience(Skill.SLAYER, 10000);
				player.getPacketSender().sendMessage("<img=458>You've bought 10000 Slayer XP for 10 Slayer points.");
				break;
			case -29519:
				if(player.getPointsHandler().getBloodSlayerPoints() < 300) {
					player.getPacketSender().sendMessage("<img=458>You do not have 300 Slayer points.");
					return true;
				}
				if(player.getBloodSlayer().doubleSlayerXP) {
					player.getPacketSender().sendMessage("You already have this buff.");
					return true;
				}
				player.getPointsHandler().setSlayerPoints(-300, true);
				player.getBloodSlayer().doubleSlayerXP = true;
				player.getPointsHandler().refreshPanel();
				player.getPacketSender().sendMessage("<img=458>You will now permanently receive double Slayer experience.");
				break;
			case -29531:
				ShopManager.getShops().get(47).open(player);
				break;
			}
			player.getPacketSender().sendString(36030, "<img=458>Current Points:   "+player.getPointsHandler().getBloodSlayerPoints());
			return true;
		}
		return false;
	}
}
