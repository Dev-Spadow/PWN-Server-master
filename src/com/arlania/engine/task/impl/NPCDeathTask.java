package com.arlania.engine.task.impl;
import com.arlania.world.content.instances.InstanceManager;
import com.arlania.world.content.interfaces.QuestTab;
import com.arlania.world.content.levelsystem.LevelSystem;
import com.arlania.world.entity.impl.npc.InstancedNpc;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.arlania.GameSettings;
import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.Animation;
import com.arlania.model.GameMode;
import com.arlania.model.Position;
import com.arlania.model.Locations.Location;
import com.arlania.model.definitions.NPCDrops;
import com.arlania.util.Misc;
import com.arlania.util.RandomUtility;
import com.arlania.world.World;
import com.arlania.world.content.NpcMultiDrops;
import com.arlania.world.content.StarterTasks.StarterTaskData;
import com.arlania.world.content.NpcTasks;
import com.arlania.world.content.NpcTasks.NpcTaskData;
import com.arlania.world.content.StarterTasks;
import com.arlania.world.content.battlepass.BattlePass;
import com.arlania.world.content.bosses.BellyBoss;
import com.arlania.world.content.bosses.Bork;
import com.arlania.world.content.bosses.SummerladyBoss;
import com.arlania.world.content.bosses.General;
import com.arlania.world.content.bosses.SlashBoss;
import com.arlania.world.content.combat.strategy.ThirdStrategy;
import com.arlania.world.content.dailytasks.DailyTasks;
import com.arlania.world.content.dailytasks.TaskType;
import com.arlania.world.content.event.SpecialEvents;
import com.arlania.world.content.raids.RaidNpc;
import com.arlania.world.content.raids.RaidParty;
import com.arlania.world.content.transportation.TeleportHandler;
import com.arlania.world.content.transportation.TeleportType;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

/**
 * Represents an npc's death task, which handles everything an npc does before
 * and after their death animation (including it), such as dropping their drop
 * table items.
 * 
 * @author relex lawl
 */

public class NPCDeathTask extends Task {
	/*
	 * The array which handles what bosses will give a player points after death
	 */
	private Set<Integer> BOSSES = new HashSet<>(
			Arrays.asList(9932,224,2745,12835,12805,2264,11360,82,354,12825,12806,667,129,353,49,34,11305,254,5931,11383,12805,9911,13447,4595,10010,259,1123,196,1683,282,8548,158,181,202,6357,20,6311,1999,11,6303,9273,9247,8493,9203,172,185,5959,170,169,2907,6311,9994,1982,16,9993,9903,8133,9935,219,3154,12239,527,1684,5957,5958,5959,185,6603,6314,6595,25,4972,2005,68)); // use this
																											// array of
																											// npcs
	// to change the npcs
	// you want to give boss
	// points
	
	private Set<Integer> CUSTOMBOSSES = new HashSet<>(Arrays.asList(436)); //same list


    private Set<Integer> OPBOSSES = new HashSet <>(Arrays.asList(9855));

    private Set<Integer> GROUP_BOSSES = new HashSet <>(Arrays.asList(2745));
	
	int[] justiciarIds = new int[] {9903,8133};

	/**
	 * The NPCDeathTask constructor.
	 * 
	 * @param npc
	 *            The npc being killed.
	 */
	public NPCDeathTask(NPC npc) {
		super(2);
		this.npc = npc;
		this.ticks = 2;
	}

	/**
	 * The npc setting off the death task.
	 */
	private final NPC npc;

	/**
	 * The amount of ticks on the task.
	 */
	private int ticks = 2;

	/**
	 * The player who killed the NPC
	 */
	private Player killer = null;

	@SuppressWarnings("incomplete-switch")
	@Override
	public void execute() {
		try {
			npc.setEntityInteraction(null);
			switch (ticks) {
			case 2:
				npc.getMovementQueue().setLockMovement(true).reset();
				// killer = npc.getCombatBuilder().getKiller(true);
				boolean reset = npc.getId() != 2745 && npc.getId() != 25 && npc.getId() != 4595  && npc.getId() != 6309 && npc.getId() != 2509 && npc.getId() != 254 && npc.getId() != 8949&& npc.getId() != 12802&& npc.getId() != 6593 && npc.getId() != 10010 && npc.getId() != 259 && npc.getId() != 9993 && npc.getId() != 6593 && npc.getId() != 12801 && npc.getId() != 4595 && npc.getId() != 12808 && npc.getId() != 9903 && npc.getId() != 2005 && npc.getId() != 4972 && npc.getId() != 6313 && npc.getId() != 10010 && npc.getId() != 9911 && npc.getId() != 15 && npc.getId() != 11813&& npc.getId() != 1382&& npc.getId() != 12101&& npc.getId() != 12382&& npc.getId() != 12805 && npc.getId() != 2509 && npc.getId() != 12823&& npc.getId() != 8133&& npc.getId() != 3154  && npc.getId() != 2005 && npc.getId() != 9913 && npc.getId() != 68 &&  npc.getId() != 7286 && !NpcMultiDrops.isMultiDropNpc(npc);

				killer = npc.getCombatBuilder().getKiller(false);

				//if(killer.getGameMode() != GameMode.GROUP_IRONMAN) npc.getCombatBuilder().getKiller(reset);

				if (!(npc.getId() >= 6142 && npc.getId() <= 6145) && !(npc.getId() > 5070 && npc.getId() < 5081))
					npc.performAnimation(new Animation(npc.getDefinition().getDeathAnimation()));

				/** CUSTOM NPC DEATHS **/

				break;
			case 0:
				
				if (killer != null) {
					
					if(killer.isMiniPlayer()) {
						killer = killer.getOwner();
					}

				

					Arrays.stream(BattlePass.INSTANCE.getNpcs()).filter(n -> n.getIndex() == npc.getId()).findFirst().ifPresent(n -> {
						BattlePass.INSTANCE.awardNpcKillExperience(killer, n);
					});
					DailyTasks.INSTANCE.updateTaskProgress(TaskType.PVM, killer, npc.getId(), 1);
					if (npc instanceof RaidNpc) {
						RaidParty party = killer.getRaidParty();
						RaidNpc raidNpc = (RaidNpc) npc;
						if (party != null) {
							if (party.getCurrentRaid().getStage() == raidNpc.getStageRequiredToAttack()) {
								party.getCurrentRaid().nextLevel();
							}
							stop();
							return;
						}
					}


					killer.getDpsOverlay().resetDamageDone(); // will work now
					
					if(npc.getId() == 10010) {
					//	killer.recievedDD = false;
					}
					if(npc.getId() == 4972) {
						killer.recievedDD = false;
					}
					if(npc.getId() == 68) {
						killer.recievedDD = false;
					}
					if(npc.getId() == 25) {
						killer.recievedDD = false;
					}
					if(npc.getId() == 2509) {
						killer.recievedDD = false;
					}
					if(npc.getId() == 12808) {
						killer.recievedDD = false;
					}
					if(npc.getId() == 12801) {
						killer.recievedDD = false;
					}
					if(npc.getId() == 12802) {
						killer.recievedDD = false;
					}
					if(npc.getId() == 2005) {
						killer.recievedDD = false;
					}
					if(npc.getId() == 11362) {
						killer.recievedDD = false;
					}
					if(npc.getId() == 2000) {
						killer.recievedDD = false;
					}
					if(npc.getId() == 2006) {
						killer.recievedDD = false;
					}
					if(npc.getId() == 11361) {
						killer.recievedDD = false;
					}
					if(npc.getId() == 8133) {
						killer.incrementCorpKC(1);
						killer.recievedDD = false;
					}
					if(npc.getId() == 15) {
						killer.recievedDD = false;
					}
					if(npc.getId() == 12823) {
						killer.recievedDD = false;
					}
					if(npc.getId() == 12101) {
						killer.recievedDD = false;
					}
					if(npc.getId() == 1382) {
						killer.recievedDD = false;
					}
					if(npc.getId() == 3154) {
						killer.incrementSupremeKC(1);
						killer.recievedDD = false;
					}
					if(npc.getId() == 169) {
						killer.recievedDD = false;
					}
					if(npc.getId() == 9286) {
						killer.recievedDD = false;
					}
					if(npc.getId() == 11813) {			
						killer.recievedDD = false;
					}
					if(npc.getId() == 7286) {
						killer.recievedDD = false;
					}
					if(npc.getId() == 50) {
						killer.recievedDD = false;
					}
					if(npc.getId() == 12836) {
						killer.recievedDD = false;
					}
					if(npc.getId() == 4595) {
						killer.recievedDD = false;
						SummerladyBoss.spawn();
					}
					if(npc.getId() == 4543) {
						killer.recievedDD = false;
					}
					if(npc.getId() == 254) {
						killer.recievedDD = false;
					}
					if(npc.getId() == 15) {
						killer.sendMessage("You make progress on the achievements : Killing Zeus X amount of times");
					killer.getAchievementTracker().progress(com.arlania.world.content.achievements.AchievementData.KILL_ZEUS_250_TIMES, 1);
					killer.getAchievementTracker().progress(com.arlania.world.content.achievements.AchievementData.KILL_ZEUS_50_TIMES, 1);
					}
					if(npc.getId() == 11813) {
						killer.sendMessage("You make progress on the achievements : Killing Naruto X amount of times");
					killer.getAchievementTracker().progress(com.arlania.world.content.achievements.AchievementData.KILL_NARUTO_500_TIMES, 1);
					killer.getAchievementTracker().progress(com.arlania.world.content.achievements.AchievementData.KILL_NARUTO_100_TIMES, 1);	
					}
					if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null
							&& killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
						killer.addNpcKillCount(npc.getId());
						killer.sendMessage("You now have: @blu@" + killer.getNpcKillCount(npc.getId()) + " Kills on this NPC");
					}

					if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null
							&& killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
						killer.addNpcKillCount(npc.getId());
						killer.sendMessage("You now have: @blu@" + killer.getNpcKillCount(npc.getId()) + " Kills on this NPC");
					}

					if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null
							&& killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
						killer.addNpcKillCount(npc.getId());
						killer.sendMessage("You now have: @blu@" + killer.getNpcKillCount(npc.getId()) + " Kills on this NPC");
					}

					if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null
							&& killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
						killer.addNpcKillCount(npc.getId());
						killer.sendMessage("You now have: @blu@" + killer.getNpcKillCount(npc.getId()) + " Kills on this NPC");
					}


					if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null
							&& killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
						killer.addNpcKillCount(npc.getId());
						killer.sendMessage("You now have: @blu@" + killer.getNpcKillCount(npc.getId()) + " Kills on this NPC");
					}

					if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null
							&& killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
						killer.addNpcKillCount(npc.getId());
						killer.sendMessage("You now have: @blu@" + killer.getNpcKillCount(npc.getId()) + " Kills on this NPC");
					}

					if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null
							&& killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
						killer.addNpcKillCount(npc.getId());
						killer.sendMessage("You now have: @blu@" + killer.getNpcKillCount(npc.getId()) + " Kills on this NPC" );
					}
					if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null
							&& killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
						killer.addNpcKillCount(npc.getId());
						killer.sendMessage("You now have: @blu@" + killer.getNpcKillCount(npc.getId()) + " Kills on this NPC" );
					}
					if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null
							&& killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
						killer.addNpcKillCount(npc.getId());
						killer.sendMessage("You now have: @blu@" + killer.getNpcKillCount(npc.getId()) + " Kills on this NPC" );
					}
					if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null
							&& killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
						killer.addNpcKillCount(npc.getId());
						killer.sendMessage("You now have: @blu@" + killer.getNpcKillCount(npc.getId()) + " Kills on this NPC" );
					}
					if (npc != null) {
						killer.handleKeyRates(killer, npc);
					}
					if (BOSSES.contains(npc.getId())) {
						
						killer.setBossPoints(killer.getBossPoints() + 1);
					//	killer.sendMessage("<img=0>You now have @red@" + killer.getBossPoints() + " MOB Points!");
					}
						if (GameSettings.DOUBLE_BOSSPOINTS) {
							
							killer.setBossPoints(killer.getBossPoints() + 1);
					//	killer.sendMessage("<img=0>You now have @red@" + killer.getBossPoints() + " MOB Points!");
					}
						if (SpecialEvents.getDay() == SpecialEvents.SATURDAY) {
							killer.setBossPoints(killer.getBossPoints() + 1);
						}

						
					if(npc.getId() == 100 && killer.getgokuKC() < 25) {
						killer.incrementGokuKC(1);
						killer.sendMessage("@blu@You now have: @red@" + killer.getgokuKC() + " @blu@Goku KC");
					}
					if(npc.getId() == 10100) {
						killer.incrementHweenKC(1);
					}
					if(killer.getgokuKC() == 25) {
						killer.setGokuKC(0);
						TeleportHandler.teleportPlayer(killer, new Position(2784, 2846, 0), TeleportType.NORMAL);
						killer.sendMessage("@red@You make it to Room 2, You must kill @blu@50 @red@Vegeta");
					}
					if(npc.getId() == 10702) {
						killer.getInventory().add(6532, 1);
			           }
					if ((npc.getId() == 10702 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 304)) {
						killer.getInventory().add(6532, 1);
			    	}	
					if(npc.getId() == 366) {
						killer.getInventory().add(6532, 1);
			           }
					if ((npc.getId() == 366 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 304)) {
						killer.getInventory().add(6532, 1);
			    	}	
					if(npc.getId() == 1440) {
						killer.getInventory().add(6532, 1);
			           }
					if ((npc.getId() == 1440 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 304)) {
						killer.getInventory().add(6532, 1);
			    	}	
			        if (NpcMultiDrops.handleDrop(killer, npc)) {
                        stop();
                        return;
                    }
					if(npc.getId() == 10100 && killer.getInventory().contains(8987, 1)) {
						killer.getInventory().add(5022, 1);
			           }
					else if ((npc.getId() == 10100 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 4545)) {
								killer.getInventory().add(5022, 2);
					    	}	
					if(npc.getId() == 10100 && killer.getInventory().contains(8987, 1)) {
			           if (Misc.inclusiveRandom(1, 250) == 40 || Misc.inclusiveRandom(1, 250) == -40) {
						killer.getInventory().add(5022, 50);
			    	}
			           }

						if(npc.getId() == 10100 && killer.getInventory().contains(8987, 1)) {
					           if (Misc.inclusiveRandom(1, 500) == 39 || Misc.inclusiveRandom(1, 500) == -39) {
								killer.getInventory().add(5022, 100);
					    	}
				           }

						
					if(npc.getId() == 10100 && killer.getInventory().contains(8987, 1)) {
			            if (Misc.inclusiveRandom(1, 1500) == 43 || Misc.inclusiveRandom(1, 1500) == -43) {
			            	killer.sendMessage("@or2@[HALLOWEEN DROP]@bla@: You managed to obtain a Georgie item while killing the spiders");
			            	killer.getInventory().add(14983, 1);
			            }
					}
			    		if(npc.getId() == 10100 && killer.getInventory().contains(8987, 1)) {
			            if (Misc.inclusiveRandom(1, 1500) == 45 || Misc.inclusiveRandom(1, 1500) == -45) {
			            	killer.sendMessage("@or2@[HALLOWEEN DROP]@bla@: You managed to obtain a Georgie item while killing the spiders");
			            	killer.getInventory().add(14984, 1);
			            }
			    	}	
			    		if(npc.getId() == 10100 && killer.getInventory().contains(8987, 1)) {
			            if (Misc.inclusiveRandom(1, 1500) == 46 || Misc.inclusiveRandom(1, 1500) == -46) {
			            	killer.sendMessage("@or2@[HALLOWEEN DROP]@bla@: You managed to obtain a Georgie item while killing the spiders");
			            	killer.getInventory().add(14985, 1);
			            }
			    	}
			    		if(npc.getId() == 10100 && killer.getInventory().contains(8987, 1)) {
			            if (Misc.inclusiveRandom(1, 1500) == 47 || Misc.inclusiveRandom(1, 1500) == -47) {
			            	killer.sendMessage("@or2@[HALLOWEEN DROP]@bla@: You managed to obtain a Georgie item while killing the spiders");
			            	killer.getInventory().add(14986, 1);
			            }
					}
			            

					if(npc.getId() == 101 && killer.getvegetaKC() < 50) {
						killer.incrementVegetaKC(1);
						killer.sendMessage("@blu@You now have: @red@" + killer.getvegetaKC() + " @blu@Vegeta KC");
					}
					if(killer.getvegetaKC() == 50) {
						killer.setVegetaKC(0);
						TeleportHandler.teleportPlayer(killer, new Position(3040, 2782, 0), TeleportType.NORMAL);
						killer.sendMessage("@red@You make it to the final room, You must kill @blu@75 @red@Kidbuu");
					}
					
					

					if(npc.getId() == 176 && killer.getcustomOlmKC() < 75) {
						killer.incrementCustomOlmKC(1);
						killer.sendMessage("@blu@You now have: @red@" + killer.getcustomOlmKC() + " @blu@Kidbuu KC");
					}
					if(killer.getcustomOlmKC() == 75) {
						killer.setCustomOlmKC(0);
						killer.getInventory().add(19100, 1);
						
						killer.sendMessage("@red@Enjoy the key");
					}
					
					
					
					if (CUSTOMBOSSES.contains(npc.getId())) {
						int chance = RandomUtility.random(100);
						if (GameSettings.DOUBLE_BOSSPOINTS && (chance >=100)) {
							killer.setCustomPoints(killer.getCustomPoints() + (killer.getRights().isLegendary() ? 8 : 4));
						}
						else
							if (chance >=100) {
								killer.setCustomPoints(killer.getCustomPoints() + (killer.getRights().isLegendary() ? 4 : 2));

								killer.sendMessage("<img=0>You now have @red@" + killer.getCustomPoints() + " Custom DBZ Points!");
							}
					}

					killer.addNpcKillCount(npc.getId());
					
					if (npc.getId() == 9247) {
						if(Misc.getRandom(100000) > 99990) {
							killer.getInventory().add(justiciarIds[Misc.getRandom(justiciarIds.length - 1)], 1);
							World.sendMessage("@blu@[LUCID DRAGONS] @red@ " + killer.getUsername() + " Just received a custom justiciar piece!");
						}


						if(npc.getId() == 9280) {
							killer.incrementMinionsKC(1);
							killer.sendMessage("@red@You now have: @blu@" + killer.getMinionsKC() + " Custom Rex Minions Kill-count");
						}
						else
							if(npc.getId() == 9280 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
								killer.incrementMinionsKC(2);

							}}


					if(npc.getId() == 17) {
						killer.incrementHerculesKC(1);

					}

					else if(npc.getId() == 17 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12299) {
						killer.incrementLucarioKC(1);
						killer.addNpcKillCount(17);	
					}

					else 
						if(npc.getId() == 17 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644)
						{
							killer.incrementHerculesKC(1);
							killer.addNpcKillCount(17);
						}
						else 
							if(npc.getId() == 17 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642)
							{
								killer.incrementHerculesKC(1);
								killer.addNpcKillCount(17);
							}
							else 
								if(npc.getId() == 17 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722)
								{
									killer.incrementHerculesKC(1);
									killer.addNpcKillCount(17);
								}
								else 
									if(npc.getId() == 17 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252)
									{
										killer.incrementHerculesKC(1);
										killer.addNpcKillCount(17);
									}
									else 
										if(npc.getId() == 17 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138)
										{
											killer.incrementHerculesKC(1);
											killer.addNpcKillCount(17);

										} else
											if(npc.getId() == 17 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
												killer.incrementHerculesKC(1);
												killer.addNpcKillCount(17);

											} else
												if(npc.getId() == 17 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementHerculesKC(1);
													killer.addNpcKillCount(17);

												}
												else 
													if(npc.getId() == 17 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementHerculesKC(1);
														killer.addNpcKillCount(17);

													}
													else 
														if(npc.getId() == 17 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementHerculesKC(1);
															killer.addNpcKillCount(17);

														}
														else 
															if(npc.getId() == 17 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementHerculesKC(1);
																killer.addNpcKillCount(17);

															}

					if(npc.getId() == 12839) {
						killer.incrementLucarioKC(1);
					} else 
						if(npc.getId() == 12839 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementLucarioKC(1);
							killer.addNpcKillCount(12839);
						}
						else if(npc.getId() == 12839 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12299) {
							killer.incrementLucarioKC(1);
							killer.addNpcKillCount(12839);	
						}
						else 
							if(npc.getId() == 12839 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementLucarioKC(1);
								killer.addNpcKillCount(12839);

							} else 
								if(npc.getId() == 12839 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementLucarioKC(1);
									killer.addNpcKillCount(12839);

								} else 
									if(npc.getId() == 12839 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementLucarioKC(1);
										killer.addNpcKillCount(12839);

									} else 
										if(npc.getId() == 12839 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementLucarioKC(1);
											killer.addNpcKillCount(12839);


										}
										else 
											if(npc.getId() == 12839 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementLucarioKC(1);
												killer.addNpcKillCount(12839);


											}
											else
												if(npc.getId() == 12839 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementLucarioKC(1);
													killer.addNpcKillCount(12839);
													killer.sendMessage("You now have: @blu@" + killer.getLucarioKC() + " @bla@Lucario Kills");

												}
												else
													if(npc.getId() == 12839 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementLucarioKC(1);
														killer.addNpcKillCount(12839);
														killer.sendMessage("You now have: @blu@" + killer.getLucarioKC() + " @bla@Lucario Kills");

													}
													else
														if(npc.getId() == 12839 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementLucarioKC(1);
															killer.addNpcKillCount(12839);
															killer.sendMessage("You now have: @blu@" + killer.getLucarioKC() + " @bla@Lucario Kills");

														}
														else
															if(npc.getId() == 12839 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementLucarioKC(1);
																killer.addNpcKillCount(12839);
																killer.sendMessage("You now have: @blu@" + killer.getLucarioKC() + " @bla@Lucario Kills");

															}
					if(npc.getId() == 1982) {
						killer.incrementCharizardKC(1);

					} else 
						if(npc.getId() == 1982 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644){
							killer.incrementCharizardKC(1);
							killer.addNpcKillCount(1982);

						} else 
							if(npc.getId() == 1982 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642){
								killer.incrementCharizardKC(1);
								killer.addNpcKillCount(1982);

							} else 
								if(npc.getId() == 1982 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722){
									killer.incrementCharizardKC(1);
									killer.addNpcKillCount(1982);

								} else 
									if(npc.getId() == 1982 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138){
										killer.incrementCharizardKC(1);
										killer.addNpcKillCount(1982);

									}
									else if(npc.getId() == 1982 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12299) {
										killer.incrementLucarioKC(1);
										killer.addNpcKillCount(1982);	
									}else 
										if(npc.getId() == 1982 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementCharizardKC(1);
											killer.addNpcKillCount(1982);


										}
										else 
											if(npc.getId() == 1982 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementCharizardKC(1);
												killer.addNpcKillCount(1982);


											}
											else 
												if(npc.getId() == 1982 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementCharizardKC(1);
													killer.addNpcKillCount(1982);


												}
												else 
													if(npc.getId() == 1982 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementCharizardKC(1);
														killer.addNpcKillCount(1982);


													}
													else 
														if(npc.getId() == 1982 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementCharizardKC(1);
															killer.addNpcKillCount(1982);


														}
														else 
															if(npc.getId() == 1982 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementCharizardKC(1);
																killer.addNpcKillCount(1982);


															}

					if(npc.getId() == 9994) {
						killer.incrementDefendersKC(1);


					} else 
						if(npc.getId() == 9994 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementDefendersKC(1);
							killer.addNpcKillCount(9994);

						} else 
							if(npc.getId() == 9994 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementDefendersKC(1);
								killer.addNpcKillCount(9994);

							} else 
								if(npc.getId() == 9994 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementDefendersKC(1);
									killer.addNpcKillCount(9994);

								} else 
									if(npc.getId() == 9994 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementDefendersKC(1);
										killer.addNpcKillCount(9994);

									} else 
										if(npc.getId() == 9994 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementDefendersKC(1);
											killer.addNpcKillCount(9994);


										}
										else 
											if(npc.getId() == 9994 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementDefendersKC(1);
												killer.addNpcKillCount(9994);


											}
											else 
												if(npc.getId() == 9994 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementDefendersKC(1);
													killer.addNpcKillCount(9994);


												}
												else 
													if(npc.getId() == 9994 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementDefendersKC(1);
														killer.addNpcKillCount(9994);


													}
													else 
														if(npc.getId() == 9994 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementDefendersKC(1);
															killer.addNpcKillCount(9994);


														}
														else 
															if(npc.getId() == 9994 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementDefendersKC(1);
																killer.addNpcKillCount(9994);


															}

					if(npc.getId() == 9932) {
						killer.incrementGodzillaKC(1);

					} else 
						if(npc.getId() == 9932 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementGodzillaKC(1);
							killer.addNpcKillCount(9932);

						} else 
							if(npc.getId() == 9932 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementGodzillaKC(1);
								killer.addNpcKillCount(9932);

							} else 
								if(npc.getId() == 9932 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementGodzillaKC(1);
									killer.addNpcKillCount(9932);

								} else 
									if(npc.getId() == 9932 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementGodzillaKC(1);
										killer.addNpcKillCount(9932);

									} else 
										if(npc.getId() == 9932 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementGodzillaKC(1);
											killer.addNpcKillCount(9932);


										}
										else 
											if(npc.getId() == 9932 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementGodzillaKC(1);
												killer.addNpcKillCount(9932);


											}
											else 
												if(npc.getId() == 9932 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementGodzillaKC(1);
													killer.addNpcKillCount(9932);


												}
												else 
													if(npc.getId() == 9932 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementGodzillaKC(1);
														killer.addNpcKillCount(9932);


													}
													else 
														if(npc.getId() == 9932 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementGodzillaKC(1);
															killer.addNpcKillCount(9932);


														}
														else 
															if(npc.getId() == 9932 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementGodzillaKC(1);
																killer.addNpcKillCount(9932);


															}

					if(npc.getId() == 224) {
						killer.incrementDemonolmKC(1);

					} else 
						if(npc.getId() == 224 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementDemonolmKC(1);
							killer.addNpcKillCount(224);

						} else 
							if(npc.getId() == 224 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementDemonolmKC(1);
								killer.addNpcKillCount(224);

							} else 
								if(npc.getId() == 224 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementDemonolmKC(1);
									killer.addNpcKillCount(224);

								} else 
									if(npc.getId() == 224 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementDemonolmKC(1);
										killer.addNpcKillCount(224);

									} else 
										if(npc.getId() == 224 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementDemonolmKC(1);
											killer.addNpcKillCount(224);


										}
										else 
											if(npc.getId() == 224 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementDemonolmKC(1);
												killer.addNpcKillCount(224);


											}
											else 
												if(npc.getId() == 224 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementDemonolmKC(1);
													killer.addNpcKillCount(224);


												}
												else 
													if(npc.getId() == 224 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementDemonolmKC(1);
														killer.addNpcKillCount(224);


													}
													else 
														if(npc.getId() == 224 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementDemonolmKC(1);
															killer.addNpcKillCount(224);


														}
														else 
															if(npc.getId() == 224 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementDemonolmKC(1);
																killer.addNpcKillCount(224);


															}
					if(npc.getId() == 1999) {
						killer.incrementCerbKC(1);

					} else 
						if(npc.getId() == 1999 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementCerbKC(1);
							killer.addNpcKillCount(1999);

						} else 
							if(npc.getId() == 1999 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementCerbKC(1);
								killer.addNpcKillCount(1999);

							} else 
								if(npc.getId() == 1999 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementCerbKC(1);
									killer.addNpcKillCount(1999);

								} else 
									if(npc.getId() == 1999 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementCerbKC(1);
										killer.addNpcKillCount(1999);

									} else 
										if(npc.getId() == 1999 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementCerbKC(1);
											killer.addNpcKillCount(1999);


										}
										else 
											if(npc.getId() == 1999 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementCerbKC(1);
												killer.addNpcKillCount(1999);


											}
											else 
												if(npc.getId() == 1999 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementCerbKC(1);
													killer.addNpcKillCount(1999);


												}
												else 
													if(npc.getId() == 1999 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementCerbKC(1);
														killer.addNpcKillCount(1999);


													}
													else 
														if(npc.getId() == 1999 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementCerbKC(1);
															killer.addNpcKillCount(1999);


														}
														else 
															if(npc.getId() == 1999 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementCerbKC(1);
																killer.addNpcKillCount(1999);


															}
					if(npc.getId() == 16) {
						killer.incrementZeusKC(1);


					} else 

						if(npc.getId() == 16 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementZeusKC(1);
							killer.addNpcKillCount(16);

						} else 
							if(npc.getId() == 16 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementZeusKC(1);
								killer.addNpcKillCount(16);

							} else 
								if(npc.getId() == 16 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementZeusKC(1);
									killer.addNpcKillCount(16);

								} else 
									if(npc.getId() == 16 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementZeusKC(1);
										killer.addNpcKillCount(16);


									} else 
										if(npc.getId() == 16 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementZeusKC(1);
											killer.addNpcKillCount(16);


										}
										else 
											if(npc.getId() == 16 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementZeusKC(1);
												killer.addNpcKillCount(16);


											}
											else 
												if(npc.getId() == 16 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementZeusKC(1);
													killer.addNpcKillCount(16);


												}
												else 
													if(npc.getId() == 16 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementZeusKC(1);
														killer.addNpcKillCount(16);


													}
													else 
														if(npc.getId() == 16 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementZeusKC(1);
															killer.addNpcKillCount(16);


														}
														else 
															if(npc.getId() == 16 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementZeusKC(1);
																killer.addNpcKillCount(16);


															}


					if(npc.getId() == 9993) {
						killer.incrementInfarticoKC(1);


					} else 
						if(npc.getId() == 9993 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementInfarticoKC(1);
							killer.addNpcKillCount(1);
							killer.addNpcKillCount(9993);

						} else 
							if(npc.getId() == 9993 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementInfarticoKC(1);
								killer.addNpcKillCount(1);
								killer.addNpcKillCount(9993);

							} else 
								if(npc.getId() == 9993 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementInfarticoKC(1);
									killer.addNpcKillCount(1);
									killer.addNpcKillCount(9993);

								} else 
									if(npc.getId() == 9993 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementInfarticoKC(1);
										killer.addNpcKillCount(1);
										killer.addNpcKillCount(9993);

									} else 
										if(npc.getId() == 9993 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementInfarticoKC(1);
											killer.addNpcKillCount(1);
											killer.addNpcKillCount(9993);


										}
										else 
											if(npc.getId() == 9993 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementInfarticoKC(1);
												killer.addNpcKillCount(1);
												killer.addNpcKillCount(9993);


											}
											else
												if(npc.getId() == 9993 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementInfarticoKC(1);
													killer.addNpcKillCount(1);
													killer.addNpcKillCount(9993);


												}
												else
													if(npc.getId() == 9993 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementInfarticoKC(1);
														killer.addNpcKillCount(1);
														killer.addNpcKillCount(9993);


													}
													else
														if(npc.getId() == 9993 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementInfarticoKC(1);
															killer.addNpcKillCount(1);
															killer.addNpcKillCount(9993);


														}
														else
															if(npc.getId() == 9993 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementInfarticoKC(1);
																killer.addNpcKillCount(1);
																killer.addNpcKillCount(9993);


															}



					if(npc.getId() == 11) {
						killer.incrementValorKC(1);


					} else 
						if(npc.getId() == 11 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementValorKC(1);
							killer.addNpcKillCount(11);

						} else 
							if(npc.getId() == 11 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementValorKC(1);
								killer.addNpcKillCount(11);

							} else 
								if(npc.getId() == 11 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementValorKC(1);
									killer.addNpcKillCount(11);

								} else 
									if(npc.getId() == 11 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementValorKC(1);
										killer.addNpcKillCount(11);

									} else 
										if(npc.getId() == 11 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementValorKC(1);
											killer.addNpcKillCount(11);


										}
										else 
											if(npc.getId() == 11 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementValorKC(1);
												killer.addNpcKillCount(11);


											}
											else 
												if(npc.getId() == 11 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementValorKC(1);
													killer.addNpcKillCount(11);


												}
												else 
													if(npc.getId() == 11 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementValorKC(1);
														killer.addNpcKillCount(11);


													}
													else 
														if(npc.getId() == 11 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementValorKC(1);
															killer.addNpcKillCount(11);


														}
														else 
															if(npc.getId() == 11 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementValorKC(1);
																killer.addNpcKillCount(11);


															}


					if(npc.getId() == 6303) {
						killer.incrementHwKC(1);

					} else 
						if(npc.getId() == 6303 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementHwKC(1);
							killer.addNpcKillCount(6303);

						} else 
							if(npc.getId() == 6303 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementHwKC(1);
								killer.addNpcKillCount(6303);

							} else 
								if(npc.getId() == 6303 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementHwKC(1);
									killer.addNpcKillCount(6303);

								} else 
									if(npc.getId() == 6303 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementHwKC(1);
										killer.addNpcKillCount(6303);


									} else 
										if(npc.getId() == 6303 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementHwKC(1);
											killer.addNpcKillCount(6303);


										}
										else 
											if(npc.getId() == 6303 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementHwKC(1);
												killer.addNpcKillCount(6303);


											}
											else 
												if(npc.getId() == 6303 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementHwKC(1);
													killer.addNpcKillCount(6303);


												}
												else 
													if(npc.getId() == 6303 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementHwKC(1);
														killer.addNpcKillCount(6303);


													}
													else 
														if(npc.getId() == 6303 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementHwKC(1);
															killer.addNpcKillCount(6303);


														}
														else 
															if(npc.getId() == 6303 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementHwKC(1);
																killer.addNpcKillCount(6303);


															}

					if(npc.getId() == 9273) {
						killer.incrementDzanthKC(1);

					} else 
						if(npc.getId() == 9273 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementDzanthKC(1);
							killer.addNpcKillCount(9273);

						} else 
							if(npc.getId() == 9273 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementDzanthKC(1);
								killer.addNpcKillCount(9273);

							} else 
								if(npc.getId() == 9273 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementDzanthKC(1);
									killer.addNpcKillCount(9273);

								} else 
									if(npc.getId() == 9273 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementDzanthKC(1);
										killer.addNpcKillCount(9273);

									} else 
										if(npc.getId() == 9273 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementDzanthKC(1);
											killer.addNpcKillCount(9273);


										}
										else 
											if(npc.getId() == 9273 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementDzanthKC(1);
												killer.addNpcKillCount(9273);


											}
											else 
												if(npc.getId() == 9273 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementDzanthKC(1);
													killer.addNpcKillCount(9273);


												}
												else 
													if(npc.getId() == 9273 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementDzanthKC(1);
														killer.addNpcKillCount(9273);


													}
													else 
														if(npc.getId() == 9273 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementDzanthKC(1);
															killer.addNpcKillCount(9273);


														}
														else 
															if(npc.getId() == 9273 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementDzanthKC(1);
																killer.addNpcKillCount(9273);


															}

					if(npc.getId() == 9903) {
						killer.incrementKongKC(1);

					} else 
						if(npc.getId() == 9903 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementKongKC(1);
							killer.addNpcKillCount(9903);

						} else 
							if(npc.getId() == 9903 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementKongKC(1);
								killer.addNpcKillCount(9903);

							} else 
								if(npc.getId() == 9903 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementKongKC(1);
									killer.addNpcKillCount(9903);

								} else 
									if(npc.getId() == 9903 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementKongKC(1);
										killer.addNpcKillCount(9903);

									} else 
										if(npc.getId() == 9903 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementKongKC(1);
											killer.addNpcKillCount(9903);


										}
										else 
											if(npc.getId() == 9903 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementKongKC(1);
												killer.addNpcKillCount(9903);


											}
											else 
												if(npc.getId() == 9903 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementKongKC(1);
													killer.addNpcKillCount(9903);


												}
												else 
													if(npc.getId() == 9903 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementKongKC(1);
														killer.addNpcKillCount(9903);


													}
													else 
														if(npc.getId() == 9903 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementKongKC(1);
															killer.addNpcKillCount(9903);


														}
														else 
															if(npc.getId() == 9903 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementKongKC(1);
																killer.addNpcKillCount(9903);


															}

					if(npc.getId() == 8133) {
						killer.incrementCorpKC(1);

					} else 
						if(npc.getId() == 8133 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementCorpKC(1);
							killer.addNpcKillCount(8133);

						} else 
							if(npc.getId() == 8133 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementCorpKC(1);
								killer.addNpcKillCount(8133);

							} else 
								if(npc.getId() == 8133 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementCorpKC(1);
									killer.addNpcKillCount(8133);


								} else 
									if(npc.getId() == 8133 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementCorpKC(1);
										killer.addNpcKillCount(8133);

									} else 
										if(npc.getId() == 8133 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementCorpKC(1);
											killer.addNpcKillCount(8133);


										}
										else 
											if(npc.getId() == 8133 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementCorpKC(1);
												killer.addNpcKillCount(8133);


											}
											else 
												if(npc.getId() == 8133 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementCorpKC(1);
													killer.addNpcKillCount(8133);


												}
												else 
													if(npc.getId() == 8133 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementCorpKC(1);
														killer.addNpcKillCount(8133);


													}
													else 
														if(npc.getId() == 8133 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementCorpKC(1);
															killer.addNpcKillCount(8133);


														}
														else 
															if(npc.getId() == 8133 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementCorpKC(1);
																killer.addNpcKillCount(8133);


															}


					if(npc.getId() == 9247) {
						killer.incrementLucidKC(1);

					} else 
						if(npc.getId() == 9247 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementLucidKC(1);
							killer.addNpcKillCount(9247);

						} else 
							if(npc.getId() == 9247 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementLucidKC(1);
								killer.addNpcKillCount(9247);

							} else 
								if(npc.getId() == 9247 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementLucidKC(1);
									killer.addNpcKillCount(9247);

								} else 
									if(npc.getId() == 9247 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementLucidKC(1);
										killer.addNpcKillCount(9247);

									} else 
										if(npc.getId() == 9247 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementLucidKC(1);
											killer.addNpcKillCount(9247);


										}
										else 
											if(npc.getId() == 9247 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementLucidKC(1);
												killer.addNpcKillCount(9247);


											}
											else 
												if(npc.getId() == 9247 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementLucidKC(1);
													killer.addNpcKillCount(9247);


												}
												else 
													if(npc.getId() == 9247 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementLucidKC(1);
														killer.addNpcKillCount(9247);


													}
													else 
														if(npc.getId() == 9247 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementLucidKC(1);
															killer.addNpcKillCount(9247);


														}
														else 
															if(npc.getId() == 9247 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementLucidKC(1);
																killer.addNpcKillCount(9247);


															}

					if(npc.getId() == 8493) {
						killer.incrementHulkKC(1);

					} else 
						if(npc.getId() == 8493 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementHulkKC(1);
							killer.addNpcKillCount(8493);

						} else 
							if(npc.getId() == 8493 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementHulkKC(1);
								killer.addNpcKillCount(8493);

							} else 
								if(npc.getId() == 8493 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementHulkKC(1);
									killer.addNpcKillCount(8493);

								} else 
									if(npc.getId() == 8493 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementHulkKC(1);
										killer.addNpcKillCount(8493);

									} else 
										if(npc.getId() == 8493 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementHulkKC(1);
											killer.addNpcKillCount(8493);


										}
										else 
											if(npc.getId() == 8493 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementHulkKC(1);
												killer.addNpcKillCount(8493);


											}
											else 
												if(npc.getId() == 8493 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementHulkKC(1);
													killer.addNpcKillCount(8493);


												}
												else 
													if(npc.getId() == 8493 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementHulkKC(1);
														killer.addNpcKillCount(8493);


													}
													else 
														if(npc.getId() == 8493 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementHulkKC(1);
															killer.addNpcKillCount(8493);


														}
														else 
															if(npc.getId() == 8493 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementHulkKC(1);
																killer.addNpcKillCount(8493);


															}

					if(npc.getId() == 9203) {
						killer.incrementDarkblueKC(1);

					} else 
						if(npc.getId() == 9203 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementDarkblueKC(1);
							killer.addNpcKillCount(9203);

						} else 
							if(npc.getId() == 9203 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementDarkblueKC(1);
								killer.addNpcKillCount(9203);

							} else 
								if(npc.getId() == 9203 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementDarkblueKC(1);
									killer.addNpcKillCount(9203);

								} else 
									if(npc.getId() == 9203 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementDarkblueKC(1);
										killer.addNpcKillCount(9203);

									} else 
										if(npc.getId() == 9203 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementDarkblueKC(1);
											killer.addNpcKillCount(9203);


										}
										else 
											if(npc.getId() == 9203 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementDarkblueKC(1);
												killer.addNpcKillCount(9203);


											}
											else 
												if(npc.getId() == 9203 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementDarkblueKC(1);
													killer.addNpcKillCount(9203);


												}
												else 
													if(npc.getId() == 9203 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementDarkblueKC(1);
														killer.addNpcKillCount(9203);


													}
													else 
														if(npc.getId() == 9203 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementDarkblueKC(1);
															killer.addNpcKillCount(9203);


														}
														else 
															if(npc.getId() == 9203 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementDarkblueKC(1);
																killer.addNpcKillCount(9203);


															}

					if(npc.getId() == 172) {
						killer.incrementPyroKC(1);

					} else 
						if(npc.getId() == 172 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementPyroKC(1);
							killer.addNpcKillCount(172);

						} else 
							if(npc.getId() == 172 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementPyroKC(1);
								killer.addNpcKillCount(172);

							} else 
								if(npc.getId() == 172 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementPyroKC(1);
									killer.addNpcKillCount(172);

								} else 
									if(npc.getId() == 172 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementPyroKC(1);
										killer.addNpcKillCount(172);

									} else 
										if(npc.getId() == 172 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementPyroKC(1);
											killer.addNpcKillCount(172);


										}
										else 
											if(npc.getId() == 172 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementPyroKC(1);
												killer.addNpcKillCount(172);


											}
											else 
												if(npc.getId() == 172 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementPyroKC(1);
													killer.addNpcKillCount(172);


												}
												else 
													if(npc.getId() == 172 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementPyroKC(1);
														killer.addNpcKillCount(172);


													}
													else 
														if(npc.getId() == 172 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementPyroKC(1);
															killer.addNpcKillCount(172);
														}
														else 
															if(npc.getId() == 172 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementPyroKC(1);
																killer.addNpcKillCount(172);
															}

					if(npc.getId() == 9935) {
						killer.incrementWyrmKC(1);


					} else 
						if(npc.getId() == 9935 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementWyrmKC(1);
							killer.addNpcKillCount(9935);

						} else 
							if(npc.getId() == 9935 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementWyrmKC(1);
								killer.addNpcKillCount(9935);

							} else 
								if(npc.getId() == 9935 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementWyrmKC(1);
									killer.addNpcKillCount(9935);

								} else 
									if(npc.getId() == 9935 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementWyrmKC(1);
										killer.addNpcKillCount(9935);

									} else 
										if(npc.getId() == 9935 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementWyrmKC(1);
											killer.addNpcKillCount(9935);


										}
										else 
											if(npc.getId() == 9935 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementWyrmKC(1);
												killer.addNpcKillCount(9935);


											}
											else 
												if(npc.getId() == 9935 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementWyrmKC(1);
													killer.addNpcKillCount(9935);


												}
												else 
													if(npc.getId() == 9935 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementWyrmKC(1);
														killer.addNpcKillCount(9935);


													}
													else 
														if(npc.getId() == 9935 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementWyrmKC(1);
															killer.addNpcKillCount(9935);


														}
														else 
															if(npc.getId() == 9935 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementWyrmKC(1);
																killer.addNpcKillCount(9935);


															}


					if(npc.getId() == 170) {
						killer.incrementTrinityKC(1);

					} else 	

						if(npc.getId() == 170 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementTrinityKC(1);
							killer.addNpcKillCount(170);

						} else 
							if(npc.getId() == 170 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementTrinityKC(1);
								killer.addNpcKillCount(170);

							} else 
								if(npc.getId() == 170 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementTrinityKC(1);
									killer.addNpcKillCount(170);

								} else 
									if(npc.getId() == 170 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementTrinityKC(1);
										killer.addNpcKillCount(170);

									} else 
										if(npc.getId() == 170 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementTrinityKC(1);
											killer.addNpcKillCount(170);


										}
										else 
											if(npc.getId() == 170 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementTrinityKC(1);
												killer.addNpcKillCount(170);


											}
											else 
												if(npc.getId() == 170 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementTrinityKC(1);
													killer.addNpcKillCount(170);


												}
												else 
													if(npc.getId() == 170 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementTrinityKC(1);
														killer.addNpcKillCount(170);


													}
													else 
														if(npc.getId() == 170 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementTrinityKC(1);
															killer.addNpcKillCount(170);


														}
														else 
															if(npc.getId() == 170 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementTrinityKC(1);
																killer.addNpcKillCount(170);


															}

					if(npc.getId() == 169) {
						killer.incrementCloudKC(1);

					} else 
						if(npc.getId() == 169 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementCloudKC(1);
							killer.addNpcKillCount(169);

						} else 
							if(npc.getId() == 169 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementCloudKC(1);
								killer.addNpcKillCount(169);

							} else 
								if(npc.getId() == 169 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementCloudKC(1);
									killer.addNpcKillCount(169);

								} else 
									if(npc.getId() == 169 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementCloudKC(1);
										killer.addNpcKillCount(169);


									}						 else 
										if(npc.getId() == 169 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementCloudKC(1);
											killer.addNpcKillCount(169);


										}
										else 
											if(npc.getId() == 169 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementCloudKC(1);
												killer.addNpcKillCount(169);


											}
											else 
												if(npc.getId() == 169 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementCloudKC(1);
													killer.addNpcKillCount(169);


												}
												else 
													if(npc.getId() == 169 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementCloudKC(1);
														killer.addNpcKillCount(169);


													}
													else 
														if(npc.getId() == 169 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementCloudKC(1);
															killer.addNpcKillCount(169);


														}
														else 
															if(npc.getId() == 169 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementCloudKC(1);
																killer.addNpcKillCount(169);


															}


					if(npc.getId() == 527) {
						killer.incrementBreakerKC(1);


					}
					else 
						if(npc.getId() == 527 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementBreakerKC(1);
							killer.addNpcKillCount(527);


						}					 else 
							if(npc.getId() == 527 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementBreakerKC(1);
								killer.addNpcKillCount(527);


							}					 else 
								if(npc.getId() == 527 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementBreakerKC(1);
									killer.addNpcKillCount(527);


								}					 else 
									if(npc.getId() == 527 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementBreakerKC(1);
										killer.addNpcKillCount(527);


									}					 else 
										if(npc.getId() == 527 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementBreakerKC(1);
											killer.addNpcKillCount(527);



										}	
										else 
											if(npc.getId() == 527 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementBreakerKC(1);
												killer.addNpcKillCount(527);



											}	
											else 
												if(npc.getId() == 527 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementBreakerKC(1);
													killer.addNpcKillCount(527);


												}	
												else 
													if(npc.getId() == 527 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementBreakerKC(1);
														killer.addNpcKillCount(527);


													}	
													else 
														if(npc.getId() == 527 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementBreakerKC(1);
															killer.addNpcKillCount(527);


														}
					
														else 
															if(npc.getId() == 527 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementBreakerKC(1);
																killer.addNpcKillCount(527);


															}


					if(npc.getId() == 1684) {
						killer.incrementApolloKC(1);


					}					 else 
						if(npc.getId() == 1684 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementApolloKC(1);
							killer.addNpcKillCount(1684);


						}					 else 
							if(npc.getId() == 1684 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementApolloKC(1);
								killer.addNpcKillCount(1684);


							}					 else 
								if(npc.getId() == 1684 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementApolloKC(1);
									killer.addNpcKillCount(1684);


								}					 else 
									if(npc.getId() == 1684 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementApolloKC(1);
										killer.addNpcKillCount(1684);


									}					 else 
										if(npc.getId() == 1684 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementApolloKC(1);
											killer.addNpcKillCount(1684);


										}	
										else 
											if(npc.getId() == 1684 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementApolloKC(1);
												killer.addNpcKillCount(1684);


											}	
											else 
												if(npc.getId() == 1684 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementApolloKC(1);
													killer.addNpcKillCount(1684);


												}	
												else 
													if(npc.getId() == 1684 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementApolloKC(1);
														killer.addNpcKillCount(1684);


													}	

													else 
														if(npc.getId() == 1684 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementApolloKC(1);
															killer.addNpcKillCount(1684);


														}	
														else 
															if(npc.getId() == 1684 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementApolloKC(1);
																killer.addNpcKillCount(1684);


															}	


					if(npc.getId() == 5957) {
						killer.incrementNoxKC(1);


					}					 else 
						if(npc.getId() == 5957 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementNoxKC(1);
							killer.addNpcKillCount(5957);


						}					 else 
							if(npc.getId() == 5957 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementNoxKC(1);
								killer.addNpcKillCount(5957);


							}					 else 
								if(npc.getId() == 5957 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementNoxKC(1);
									killer.addNpcKillCount(5957);


								}					 else 
									if(npc.getId() == 5957 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementNoxKC(1);
										killer.addNpcKillCount(5957);


									}					 else 
										if(npc.getId() == 5957 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementNoxKC(1);
											killer.addNpcKillCount(5957);


										}	
										else 
											if(npc.getId() == 5957 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementNoxKC(1);
												killer.addNpcKillCount(5957);


											}	
											else 
												if(npc.getId() == 5957 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementNoxKC(1);
													killer.addNpcKillCount(5957);


												}	
												else 
													if(npc.getId() == 5957 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementNoxKC(1);
														killer.addNpcKillCount(5957);


													}	
													else 
														if(npc.getId() == 5957 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementNoxKC(1);
															killer.addNpcKillCount(5957);


														}	
														else 
															if(npc.getId() == 5957 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementNoxKC(1);
																killer.addNpcKillCount(5957);


															}	



					if(npc.getId() == 5958) {
						killer.incrementAzazelKC(1);


					}					 else 
						if(npc.getId() == 5958 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementAzazelKC(1);
							killer.addNpcKillCount(5958);


						}					 else 
							if(npc.getId() == 5958 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementAzazelKC(1);
								killer.addNpcKillCount(5958);


							}					 else 
								if(npc.getId() == 5958 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementAzazelKC(1);
									killer.addNpcKillCount(5958);


								}					 else 
									if(npc.getId() == 5958 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementAzazelKC(1);
										killer.addNpcKillCount(5958);


									}					 else 
										if(npc.getId() == 5958 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementAzazelKC(1);
											killer.addNpcKillCount(5958);

										}	
										else 
											if(npc.getId() == 5958 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementAzazelKC(1);
												killer.addNpcKillCount(5958);

											}	
											else 
												if(npc.getId() == 5958 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementAzazelKC(1);
													killer.addNpcKillCount(5958);

												}	
												else 
													if(npc.getId() == 5958 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementAzazelKC(1);
														killer.addNpcKillCount(5958);

													}	
													else 
														if(npc.getId() == 5958 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementAzazelKC(1);
															killer.addNpcKillCount(5958);

														}	
														else 
															if(npc.getId() == 5958 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementAzazelKC(1);
																killer.addNpcKillCount(5958);

															}	

					if(npc.getId() == 2907) {
						killer.incrementRazorKC(1);


					}					 else 
						if(npc.getId() == 2907 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementRazorKC(1);
							killer.addNpcKillCount(2907);


						}					 else 
							if(npc.getId() == 2907 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementRazorKC(1);
								killer.addNpcKillCount(2907);


							}					 else 
								if(npc.getId() == 2907 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementRazorKC(1);
									killer.addNpcKillCount(2907);


								}					 else 
									if(npc.getId() == 2907 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementRazorKC(1);
										killer.addNpcKillCount(2907);


									}					 else 
										if(npc.getId() == 2907 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementRazorKC(1);
											killer.addNpcKillCount(2907);


										}	
										else 
											if(npc.getId() == 2907 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementRazorKC(1);
												killer.addNpcKillCount(2907);


											}	
											else 
												if(npc.getId() == 2907 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementRazorKC(1);
													killer.addNpcKillCount(2907);


												}	
												else 
													if(npc.getId() == 2907 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementRazorKC(1);
														killer.addNpcKillCount(2907);


													}	
													else 
														if(npc.getId() == 2907 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementRazorKC(1);
															killer.addNpcKillCount(2907);


														}	
														else 
															if(npc.getId() == 2907 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementRazorKC(1);
																killer.addNpcKillCount(2907);


															}	
						

					if(npc.getId() == 1123) {
						killer.incrementSableKC(1);


					}					 else 
						if(npc.getId() == 1123 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementSableKC(1);
							killer.addNpcKillCount(1123);


						}					 else 
							if(npc.getId() == 1123 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementSableKC(1);
								killer.addNpcKillCount(1123);


							}					 else 
								if(npc.getId() == 1123 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementSableKC(1);
									killer.addNpcKillCount(1123);


								}					 else 
									if(npc.getId() == 1123 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementSableKC(1);
										killer.addNpcKillCount(1123);


									}					 else 
										if(npc.getId() == 1123 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementSableKC(1);
											killer.addNpcKillCount(1123);


										}	
										else 
											if(npc.getId() == 1123 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementSableKC(1);
												killer.addNpcKillCount(1123);


											}	
											else 
												if(npc.getId() == 1123 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementSableKC(1);
													killer.addNpcKillCount(1123);


												}
												else 
													if(npc.getId() == 1123 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementSableKC(1);
														killer.addNpcKillCount(1123);


													}	
													else 
														if(npc.getId() == 1123 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementSableKC(1);
															killer.addNpcKillCount(1123);
														}	
														else 
															if(npc.getId() == 1123 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementSableKC(1);
																killer.addNpcKillCount(1123);
															}	
					

					if(npc.getId() == 12835) {
						killer.incrementDemoKC(1);


					}					 else 
						if(npc.getId() == 12835 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementDemoKC(1);
							killer.addNpcKillCount(12835);


						}					 else 
							if(npc.getId() == 12835 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementDemoKC(1);
								killer.addNpcKillCount(12835);


							}					 else 
								if(npc.getId() == 12835 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementDemoKC(1);
									killer.addNpcKillCount(12835);


								}					 else 
									if(npc.getId() == 12835 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementDemoKC(1);
										killer.addNpcKillCount(12835);


									}					 else 
										if(npc.getId() == 12835 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementDemoKC(1);
											killer.addNpcKillCount(12835);


										}	
										else 
											if(npc.getId() == 12835 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementDemoKC(1);
												killer.addNpcKillCount(12835);


											}	
											else 
												if(npc.getId() == 12835 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementDemoKC(1);
													killer.addNpcKillCount(12835);


												}	
												else 
													if(npc.getId() == 12835 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementDemoKC(1);
														killer.addNpcKillCount(12835);
													}	
													else 
														if(npc.getId() == 12835 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementDemoKC(1);
															killer.addNpcKillCount(12835);
														}	
														else 
															if(npc.getId() == 12835 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementDemoKC(1);
																killer.addNpcKillCount(12835);
															}	


					if(npc.getId() == 20) {
						killer.incrementDreamflowKC(1);


					}					 else 
						if(npc.getId() == 20 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementDreamflowKC(1);
							killer.addNpcKillCount(20);


						}					 else 
							if(npc.getId() == 20 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementDreamflowKC(1);
								killer.addNpcKillCount(20);


							}					 else 
								if(npc.getId() == 20 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementDreamflowKC(1);
									killer.addNpcKillCount(20);


								}					 else 
									if(npc.getId() == 20 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementDreamflowKC(1);
										killer.addNpcKillCount(20);


									}					 else 
										if(npc.getId() == 20 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementDreamflowKC(1);
											killer.addNpcKillCount(20);


										}	
										else 
											if(npc.getId() == 20 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementDreamflowKC(1);
												killer.addNpcKillCount(20);


											}	
											else 
												if(npc.getId() == 20 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementDreamflowKC(1);
													killer.addNpcKillCount(20);


												}	
												else 
													if(npc.getId() == 20 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementDreamflowKC(1);
														killer.addNpcKillCount(20);


													}	
													else 
														if(npc.getId() == 20 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementDreamflowKC(1);
															killer.addNpcKillCount(20);


														}	
														else 
															if(npc.getId() == 20 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementDreamflowKC(1);
																killer.addNpcKillCount(20);


															}	


					if(npc.getId() == 259) {
						killer.incrementKhioneKC(1);


					}					 else 
						if(npc.getId() == 259 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementKhioneKC(1);
							killer.addNpcKillCount(259);


						}					 else 
							if(npc.getId() == 259 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementKhioneKC(1);
								killer.addNpcKillCount(259);


							}					 else 
								if(npc.getId() == 259 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementKhioneKC(1);
									killer.addNpcKillCount(259);


								}					 else 
									if(npc.getId() == 259 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementKhioneKC(1);
										killer.addNpcKillCount(259);


									}					 else 
										if(npc.getId() == 259 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementKhioneKC(1);
											killer.addNpcKillCount(259);


										}	
										else 
											if(npc.getId() == 259 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementKhioneKC(1);
												killer.addNpcKillCount(259);


											}	
											else 
												if(npc.getId() == 259 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementKhioneKC(1);
													killer.addNpcKillCount(259);


												}
												else 
													if(npc.getId() == 259 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementKhioneKC(1);
														killer.addNpcKillCount(259);


													}	
													else 
														if(npc.getId() == 259 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementKhioneKC(1);
															killer.addNpcKillCount(259);


														}	
														else 
															if(npc.getId() == 259 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementKhioneKC(1);
																killer.addNpcKillCount(259);


															}	


					if(npc.getId() == 219) {
						killer.incrementHerbalKC(1);


					}					 else 
						if(npc.getId() == 219 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementHerbalKC(1);
							killer.addNpcKillCount(219);


						}					 else 
							if(npc.getId() == 219 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementHerbalKC(1);
								killer.addNpcKillCount(219);


							}					 else 
								if(npc.getId() == 219 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementHerbalKC(1);
									killer.addNpcKillCount(219);


								}					 else 
									if(npc.getId() == 219 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementHerbalKC(1);
										killer.addNpcKillCount(219);


									}					 else 
										if(npc.getId() == 219 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementHerbalKC(1);
											killer.addNpcKillCount(219);


										}	
										else 
											if(npc.getId() == 219 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementHerbalKC(1);
												killer.addNpcKillCount(219);


											}	
											else 
												if(npc.getId() == 219 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementHerbalKC(1);
													killer.addNpcKillCount(219);


												}	
												else 
													if(npc.getId() == 219 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementHerbalKC(1);
														killer.addNpcKillCount(219);


													}	
													else 
														if(npc.getId() == 219 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementHerbalKC(1);
															killer.addNpcKillCount(219);


														}	
														else 
															if(npc.getId() == 219 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementHerbalKC(1);
																killer.addNpcKillCount(219);


															}	
					
					
					


					if(npc.getId() == 2264) {
						killer.incrementAvatarKC(1);


					}					 else 
						if(npc.getId() == 2264 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementAvatarKC(1);
							killer.addNpcKillCount(2264);


						}					 else 
							if(npc.getId() == 2264 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementAvatarKC(1);
								killer.addNpcKillCount(2264);


							}					 else 
								if(npc.getId() == 2264 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementAvatarKC(1);
									killer.addNpcKillCount(2264);


								}					 else 
									if(npc.getId() == 2264 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementAvatarKC(1);
										killer.addNpcKillCount(2264);


									}					 else 
										if(npc.getId() == 2264 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementAvatarKC(1);
											killer.addNpcKillCount(2264);


										}	
										else 
											if(npc.getId() == 2264 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementAvatarKC(1);
												killer.addNpcKillCount(2264);


											}	
											else 
												if(npc.getId() == 2264 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementAvatarKC(1);
													killer.addNpcKillCount(2264);


												}
												else 
													if(npc.getId() == 2264 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementAvatarKC(1);
														killer.addNpcKillCount(2264);


													}	
													else 
														if(npc.getId() == 2264 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementAvatarKC(1);
															killer.addNpcKillCount(2264);


														}	
														else 
															if(npc.getId() == 2264 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementAvatarKC(1);
																killer.addNpcKillCount(2264);


															}	
					
					
					if(npc.getId() == 11360) {
						killer.incrementLiliKC(1);


					}					 else 
						if(npc.getId() == 11360 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementLiliKC(1);
							killer.addNpcKillCount(11360);


						}					 else 
							if(npc.getId() == 11360 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementLiliKC(1);
								killer.addNpcKillCount(11360);


							}					 else 
								if(npc.getId() == 11360 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementLiliKC(1);
									killer.addNpcKillCount(11360);


								}					 else 
									if(npc.getId() == 11360 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementLiliKC(1);
										killer.addNpcKillCount(11360);


									}					 else 
										if(npc.getId() == 11360 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementLiliKC(1);
											killer.addNpcKillCount(11360);


										}	
										else 
											if(npc.getId() == 11360 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementLiliKC(1);
												killer.addNpcKillCount(11360);


											}	
											else 
												if(npc.getId() == 11360 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementLiliKC(1);
													killer.addNpcKillCount(11360);


												}	
												else 
													if(npc.getId() == 11360 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementLiliKC(1);
														killer.addNpcKillCount(11360);


													}	
													else 
														if(npc.getId() == 11360 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementLiliKC(1);
															killer.addNpcKillCount(11360);


														}	
														else 
															if(npc.getId() == 11360 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementLiliKC(1);
																killer.addNpcKillCount(11360);


															}	
					
					
					
					
					if(npc.getId() == 11383) {
						killer.incrementObitoKC(1);


					}					 else 
						if(npc.getId() == 11383 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementObitoKC(1);
							killer.addNpcKillCount(11383);


						}					 else 
							if(npc.getId() == 11383 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementObitoKC(1);
								killer.addNpcKillCount(11383);


							}					 else 
								if(npc.getId() == 11383 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementObitoKC(1);
									killer.addNpcKillCount(11383);


								}					 else 
									if(npc.getId() == 11383 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementObitoKC(1);
										killer.addNpcKillCount(11383);


									}					 else 
										if(npc.getId() == 11383 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementObitoKC(1);
											killer.addNpcKillCount(11383);


										}	
										else 
											if(npc.getId() == 11383 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementObitoKC(1);
												killer.addNpcKillCount(11383);


											}	
											else 
												if(npc.getId() == 11383 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementObitoKC(1);
													killer.addNpcKillCount(11383);


												}
												else 
													if(npc.getId() == 11383 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementObitoKC(1);
														killer.addNpcKillCount(11383);


													}	
													else 
														if(npc.getId() == 11383 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementObitoKC(1);
															killer.addNpcKillCount(11383);


														}	
														else 
															if(npc.getId() == 11383 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementObitoKC(1);
																killer.addNpcKillCount(11383);


															}	
					
					
					
					if(npc.getId() == 11305) {
						killer.incrementUruKC(1);


					}					 else 
						if(npc.getId() == 11305 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementUruKC(1);
							killer.addNpcKillCount(11305);


						}					 else 
							if(npc.getId() == 11305 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementUruKC(1);
								killer.addNpcKillCount(11305);


							}					 else 
								if(npc.getId() == 11305 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementUruKC(1);
									killer.addNpcKillCount(11305);


								}					 else 
									if(npc.getId() == 11305 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementUruKC(1);
										killer.addNpcKillCount(11305);


									}					 else 
										if(npc.getId() == 11305 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementUruKC(1);
											killer.addNpcKillCount(11305);


										}	
										else 
											if(npc.getId() == 11305 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementUruKC(1);
												killer.addNpcKillCount(11305);


											}	
											else 
												if(npc.getId() == 11305 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementUruKC(1);
													killer.addNpcKillCount(11305);


												}
												else 
													if(npc.getId() == 11305 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementUruKC(1);
														killer.addNpcKillCount(11305);


													}	
													else 
														if(npc.getId() == 11305 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementUruKC(1);
															killer.addNpcKillCount(11305);


														}	
														else 
															if(npc.getId() == 11305 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementUruKC(1);
																killer.addNpcKillCount(11305);


															}	
					
					
					
					
					if(npc.getId() == 5931) {
						killer.incrementKumihoKC(1);


					}					 else 
						if(npc.getId() == 5931 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementKumihoKC(1);
							killer.addNpcKillCount(5931);


						}					 else 
							if(npc.getId() == 5931 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementKumihoKC(1);
								killer.addNpcKillCount(5931);


							}					 else 
								if(npc.getId() == 5931 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementKumihoKC(1);
									killer.addNpcKillCount(5931);


								}					 else 
									if(npc.getId() == 5931 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementKumihoKC(1);
										killer.addNpcKillCount(5931);


									}					 else 
										if(npc.getId() == 5931 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementKumihoKC(1);
											killer.addNpcKillCount(5931);


										}	
										else 
											if(npc.getId() == 5931 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementKumihoKC(1);
												killer.addNpcKillCount(5931);


											}	
											else 
												if(npc.getId() == 5931 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementKumihoKC(1);
													killer.addNpcKillCount(5931);


												}
												else 
													if(npc.getId() == 5931 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementKumihoKC(1);
														killer.addNpcKillCount(5931);


													}	
													else 
														if(npc.getId() == 5931 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementKumihoKC(1);
															killer.addNpcKillCount(5931);


														}	
														else 
															if(npc.getId() == 5931 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementKumihoKC(1);
																killer.addNpcKillCount(5931);


															}	
						
					
					
					if(npc.getId() == 254) {
						killer.incrementMysteryKC(1);


					}					 else 
						if(npc.getId() == 254 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementMysteryKC(1);
							killer.addNpcKillCount(254);


						}					 else 
							if(npc.getId() == 254 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementMysteryKC(1);
								killer.addNpcKillCount(254);


							}					 else 
								if(npc.getId() == 254 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementMysteryKC(1);
									killer.addNpcKillCount(254);


								}					 else 
									if(npc.getId() == 254 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementMysteryKC(1);
										killer.addNpcKillCount(254);


									}					 else 
										if(npc.getId() == 254 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementMysteryKC(1);
											killer.addNpcKillCount(254);


										}	
										else 
											if(npc.getId() == 254 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementMysteryKC(1);
												killer.addNpcKillCount(254);


											}	
											else 
												if(npc.getId() == 254 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementMysteryKC(1);
													killer.addNpcKillCount(254);


												}
												else 
													if(npc.getId() == 254 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementMysteryKC(1);
														killer.addNpcKillCount(254);


													}	
													else 
														if(npc.getId() == 254 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementMysteryKC(1);
															killer.addNpcKillCount(254);


														}	
					
														else 
															if(npc.getId() == 254 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementMysteryKC(1);
																killer.addNpcKillCount(254);


															}	

					if(npc.getId() == 12835) {
						killer.incrementYoshiKC(1);


					}					 else 
						if(npc.getId() == 12835 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementDemoKC(1);
							killer.addNpcKillCount(12835);


						}					 else 
							if(npc.getId() == 12835 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementDemoKC(1);
								killer.addNpcKillCount(12835);


							}					 else 
								if(npc.getId() == 12835 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementDemoKC(1);
									killer.addNpcKillCount(12835);


								}					 else 
									if(npc.getId() == 12835 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementDemoKC(1);
										killer.addNpcKillCount(12835);


									}					 else 
										if(npc.getId() == 12835 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementDemoKC(1);
											killer.addNpcKillCount(12835);


										}	
										else 
											if(npc.getId() == 12835 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementDemoKC(1);
												killer.addNpcKillCount(12835);


											}	
											else 
												if(npc.getId() == 12835 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementDemoKC(1);
													killer.addNpcKillCount(12835);


												}
												else 
													if(npc.getId() == 12835 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementDemoKC(1);
														killer.addNpcKillCount(12835);


													}	
													else 
														if(npc.getId() == 12835 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementDemoKC(1);
															killer.addNpcKillCount(12835);


														}	
														else 
															if(npc.getId() == 12835 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementDemoKC(1);
																killer.addNpcKillCount(12835);


															}	
					
					


					if(npc.getId() == 8548) {
						killer.incrementYoshiKC(1);


					}					 else 
						if(npc.getId() == 8548 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementYoshiKC(1);
							killer.addNpcKillCount(8548);


						}					 else 
							if(npc.getId() == 8548 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementYoshiKC(1);
								killer.addNpcKillCount(8548);


							}					 else 
								if(npc.getId() == 8548 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementYoshiKC(1);
									killer.addNpcKillCount(8548);


								}					 else 
									if(npc.getId() == 8548 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementYoshiKC(1);
										killer.addNpcKillCount(8548);


									}					 else 
										if(npc.getId() == 8548 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementYoshiKC(1);
											killer.addNpcKillCount(8548);


										}	
										else 
											if(npc.getId() == 8548 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementYoshiKC(1);
												killer.addNpcKillCount(8548);


											}	
											else 
												if(npc.getId() == 8548 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementYoshiKC(1);
													killer.addNpcKillCount(8548);


												}
												else 
													if(npc.getId() == 8548 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementYoshiKC(1);
														killer.addNpcKillCount(8548);


													}	
													else 
														if(npc.getId() == 8548 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementYoshiKC(1);
															killer.addNpcKillCount(8548);


														}	
														else 
															if(npc.getId() == 8548 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementYoshiKC(1);
																killer.addNpcKillCount(8548);


															}	

					if(npc.getId() == 185) {
						killer.incrementLuminKC(1);


					}					 else 
						if(npc.getId() == 185 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementLuminKC(1);
							killer.addNpcKillCount(185);


						}					 else 
							if(npc.getId() == 185 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementLuminKC(1);
								killer.addNpcKillCount(185);


							}					 else 
								if(npc.getId() == 185 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementLuminKC(1);
									killer.addNpcKillCount(185);


								}					 else 
									if(npc.getId() == 185 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementLuminKC(1);
										killer.addNpcKillCount(185);


									}					 else 
										if(npc.getId() == 185 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementLuminKC(1);
											killer.addNpcKillCount(185);


										}	
										else 
											if(npc.getId() == 185 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementLuminKC(1);
												killer.addNpcKillCount(185);


											}	
											else 
												if(npc.getId() == 185 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementLuminKC(1);
													killer.addNpcKillCount(185);


												}
												else 
													if(npc.getId() == 185 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementLuminKC(1);
														killer.addNpcKillCount(185);


													}	
													else 
														if(npc.getId() == 185 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementLuminKC(1);
															killer.addNpcKillCount(185);


														}	
														else 
															if(npc.getId() == 185 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementLuminKC(1);
																killer.addNpcKillCount(185);


															}	





					if(npc.getId() == 6311) {
						killer.incrementCustomhKC(1);


					}					 else 
						if(npc.getId() == 6311 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementCustomhKC(1);
							killer.addNpcKillCount(6311);


						}					 else 
							if(npc.getId() == 6311 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementCustomhKC(1);
								killer.addNpcKillCount(6311);


							}					 else 
								if(npc.getId() == 6311 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementCustomhKC(1);
									killer.addNpcKillCount(6311);


								}					 else 
									if(npc.getId() == 6311 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementCustomhKC(1);
										killer.addNpcKillCount(6311);


									}					 else 
										if(npc.getId() == 6311 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementCustomhKC(1);
											killer.addNpcKillCount(6311);


										}	
										else 
											if(npc.getId() == 6311 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementCustomhKC(1);
												killer.addNpcKillCount(6311);


											}	
											else 
												if(npc.getId() == 6311 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementCustomhKC(1);
													killer.addNpcKillCount(6311);


												}
												else 
													if(npc.getId() == 6311 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementCustomhKC(1);
														killer.addNpcKillCount(6311);


													}	
													else 
														if(npc.getId() == 6311 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementCustomhKC(1);
															killer.addNpcKillCount(6311);


														}	
														else 
															if(npc.getId() == 6311 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementCustomhKC(1);
																killer.addNpcKillCount(6311);


															}	

					if(npc.getId() == 12239) {
						killer.incrementExodenKC(1);


					}					 else 
						if(npc.getId() == 12239 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementExodenKC(1);
							killer.addNpcKillCount(12239);


						}					 else 
							if(npc.getId() == 12239 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementExodenKC(1);
								killer.addNpcKillCount(12239);


							}					 else 
								if(npc.getId() == 12239 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementExodenKC(1);
									killer.addNpcKillCount(12239);


								}					 else 
									if(npc.getId() == 12239 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementExodenKC(1);
										killer.addNpcKillCount(12239);


									}					 else 
										if(npc.getId() == 12239 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementExodenKC(1);
											killer.addNpcKillCount(12239);


										}	
										else 
											if(npc.getId() == 12239 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementExodenKC(1);
												killer.addNpcKillCount(12239);


											}
											else 
												if(npc.getId() == 12239 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementExodenKC(1);
													killer.addNpcKillCount(12239);


												}
												else 
													if(npc.getId() == 12239 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementExodenKC(1);
														killer.addNpcKillCount(12239);


													}	
													else 
														if(npc.getId() == 12239 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementExodenKC(1);
															killer.addNpcKillCount(12239);


														}	
														else 
															if(npc.getId() == 12239 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementExodenKC(1);
																killer.addNpcKillCount(12239);


															}	

					if(npc.getId() == 3154) {
						killer.incrementSupremeKC(1);
						killer.addNpcKillCount(3154);


					}	
					else  
						if(npc.getId() == 3154 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementSupremeKC(1);
							killer.addNpcKillCount(3154);

						}
						else 
							if(npc.getId() == 3154 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementSupremeKC(1);
								killer.addNpcKillCount(3154);

							}
							else 
								if(npc.getId() == 3154 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementSupremeKC(1);
									killer.addNpcKillCount(3154);

								}
								else 
									if(npc.getId() == 3154 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementSupremeKC(1);
										killer.addNpcKillCount(3154);

									}
									else 
										if(npc.getId() == 3154 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032){
											killer.incrementSupremeKC(1);
											killer.addNpcKillCount(3154);

										}
										else 
											if(npc.getId() == 3154 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252){
												killer.incrementSupremeKC(1);
												killer.addNpcKillCount(3154);

											}
											else 
												if(npc.getId() == 3154 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960){
													killer.incrementSupremeKC(1);
													killer.addNpcKillCount(3154);

												}
												else 
													if(npc.getId() == 3154 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213){
														killer.incrementSupremeKC(1);
														killer.addNpcKillCount(3154);

													}
													else 
														if(npc.getId() == 3154 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240){
															killer.incrementSupremeKC(1);
															killer.addNpcKillCount(3154);

														}
														else 
															if(npc.getId() == 3154 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53){
																killer.incrementSupremeKC(1);
																killer.addNpcKillCount(3154);

															}



					if(npc.getId() == 5959) {
						killer.incrementRavanaKC(1);
					}					 else 
						if(npc.getId() == 5959 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 644) {
							killer.incrementRavanaKC(1);
							killer.addNpcKillCount(5959);


						}					 else 
							if(npc.getId() == 5959 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 642) {
								killer.incrementRavanaKC(1);
								killer.addNpcKillCount(5959);


							}					 else 
								if(npc.getId() == 5959 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 722) {
									killer.incrementRavanaKC(1);
									killer.addNpcKillCount(5959);


								}					 else 
									if(npc.getId() == 5959 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3138) {
										killer.incrementRavanaKC(1);
										killer.addNpcKillCount(5959);


									}					 else 
										if(npc.getId() == 5959 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
											killer.incrementRavanaKC(1);
											killer.addNpcKillCount(5959);


										}	
										else 
											if(npc.getId() == 5959 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
												killer.incrementRavanaKC(1);
												killer.addNpcKillCount(5959);


											}	
											else 
												if(npc.getId() == 5959 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 5960) {
													killer.incrementRavanaKC(1);
													killer.addNpcKillCount(5959);


												}	
												else 
													if(npc.getId() == 5959 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12213) {
														killer.incrementRavanaKC(1);
														killer.addNpcKillCount(5959);


													}	
													else 
														if(npc.getId() == 5959 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 12240) {
															killer.incrementRavanaKC(1);
															killer.addNpcKillCount(5959);


														}	
														else 
															if(npc.getId() == 5959 && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 53) {
																killer.incrementRavanaKC(1);
																killer.addNpcKillCount(5959);


															}	












					if((npc.getDefaultConstitution() >= 50000) || (BOSSES.contains(npc.getId()))) {
						killer.incrementTotalBossKills(1);
					}

					if (npc.getId() == 11380) { // Joker
						NpcTasks.doProgress(killer, NpcTaskData.KILL_50_SHADOWLORD);
					}
					if (npc.getId() == 51) { // Frost dragons
						NpcTasks.doProgress(killer, NpcTaskData.KILL_100_LIGHTREAPERS);
					}
					if (npc.getId() == 2783) { // Sirenic ogres
						NpcTasks.doProgress(killer, NpcTaskData.KILL_150_SIRENIC_OGRES);
					}
					if (npc.getId() == 16) { // Hades
						NpcTasks.doProgress(killer, NpcTaskData.KILL_250_HADES);
					}
					if (npc.getId() == 9994) { // Jinnis
						NpcTasks.doProgress(killer, NpcTaskData.KILL_300_JINNIS);
					}
					if (npc.getId() == 224) { // Great Olm
						NpcTasks.doProgress(killer, NpcTaskData.KILL_250_GREAT_OLMS);
					}
					if (npc.getId() == 1999) { // Cerb
						NpcTasks.doProgress(killer, NpcTaskData.KILL_50_BLOATED_INFERNALS);
					}
					if (npc.getId() == 16) { // Abbadon
						NpcTasks.doProgress(killer, NpcTaskData.KILL_500_ZEUS);
					}
					if (npc.getId() == 9993) { // Custom infartico
						NpcTasks.doProgress(killer, NpcTaskData.KILL_50_INFARTICO);
					}
					if (npc.getId() == 11) { // Lord Valor
						NpcTasks.doProgress(killer, NpcTaskData.KILL_500_DARTH_VADER);
						NpcTasks.doProgress(killer, NpcTaskData.KILL_5000_DARTH_VADER);
					}
					if (npc.getId() == 6303) { // Hurricane warriors
						NpcTasks.doProgress(killer, NpcTaskData.KILL_500_DANTES_SATAN);
						NpcTasks.doProgress(killer, NpcTaskData.KILL_3000_DANTES_SATAN);
					}
					if (npc.getId() == 9273) { // Dzanth
						NpcTasks.doProgress(killer, NpcTaskData.KILL_500_RICK);
						NpcTasks.doProgress(killer, NpcTaskData.KILL_3000_RICK);
					}
					if (npc.getId() == 9903) { // King Kong
						NpcTasks.doProgress(killer, NpcTaskData.KILL_50_KINGKONG);
						NpcTasks.doProgress(killer, NpcTaskData.KILL_500_KINGKONG);
					}
					if (npc.getId() == 8133) { // CORP
						NpcTasks.doProgress(killer, NpcTaskData.KILL_50_CORP_BEAST);
						NpcTasks.doProgress(killer, NpcTaskData.KILL_500_CORP_BEAST);
						NpcTasks.doProgress(killer, NpcTaskData.KILL_2500_CORP_BEAST);
					}
					if (npc.getId() == 9247) { // Lucid Warriors
						NpcTasks.doProgress(killer, NpcTaskData.KILL_500_LUCID_DRAGONS);
						NpcTasks.doProgress(killer, NpcTaskData.KILL_2500_LUCID_DRAGONS);
						NpcTasks.doProgress(killer, NpcTaskData.KILL_10000_LUCID_DRAGONS);
					}
					if (npc.getId() == 8493) { // HULK
						NpcTasks.doProgress(killer, NpcTaskData.KILL_500_HULK);
						NpcTasks.doProgress(killer, NpcTaskData.KILL_10000_HULK);
					}
					if (npc.getId() == 9203) { // DARKBLUE WIZARD Dragon
						NpcTasks.doProgress(killer, NpcTaskData.KILL_500_DARK_WIZARDS);
						NpcTasks.doProgress(killer, NpcTaskData.KILL_2500_DARK_WIZARDS);
					}
					if (npc.getId() == 172) { // Ice Warrior
						NpcTasks.doProgress(killer, NpcTaskData.KILL_10000_HEATED_PYRO);
					}
					if (npc.getId() == 9935) { // WYRM
						NpcTasks.doProgress(killer, NpcTaskData.KILL_2500_WYRM);
						NpcTasks.doProgress(killer, NpcTaskData.KILL_5000_WYRM);
					}
					if (npc.getId() == 170) { // TRINITY
						NpcTasks.doProgress(killer, NpcTaskData.KILL_2500_TRINITY);
						NpcTasks.doProgress(killer, NpcTaskData.KILL_5000_TRINITY);
					}
					if (npc.getId() == 169) { // CLOUD
						NpcTasks.doProgress(killer, NpcTaskData.KILL_2500_CLOUD);
						NpcTasks.doProgress(killer, NpcTaskData.KILL_5000_CLOUD);
					}

					if (npc.getId() == 219) { // ROUGE
						NpcTasks.doProgress(killer, NpcTaskData.KILL_5000_HERBAL_ROGUE);
						NpcTasks.doProgress(killer, NpcTaskData.KILL_10000_HERBAL_ROGUE);
					}
					if (npc.getId() == 12239) { // Exoden
						NpcTasks.doProgress(killer, NpcTaskData.KILL_1000_EXODEN);
						NpcTasks.doProgress(killer, NpcTaskData.KILL_5000_EXODEN);
						NpcTasks.doProgress(killer, NpcTaskData.KILL_10000_EXODEN);
					}
					if (npc.getId() == 3154) { // Nex
						NpcTasks.doProgress(killer, NpcTaskData.KILL_1000_SUPREME_NEX);
						NpcTasks.doProgress(killer, NpcTaskData.KILL_5000_SUPREME_NEX);
						NpcTasks.doProgress(killer, NpcTaskData.KILL_10000_SUPREME_NEX);
					}
					if (npc.getId() == 527) { // stormbreaker
						NpcTasks.doProgress(killer, NpcTaskData.KILL_1000_STORMBREAKERS);
						NpcTasks.doProgress(killer, NpcTaskData.KILL_5000_STORMBREAKERS);
						NpcTasks.doProgress(killer, NpcTaskData.KILL_10000_STORMBREAKERS);
					}
					if (npc.getId() == 1684) { // apollo
						NpcTasks.doProgress(killer, NpcTaskData.KILL_1000_APOLLO_RANGERS);
						NpcTasks.doProgress(killer, NpcTaskData.KILL_5000_APOLLO_RANGERS);
						NpcTasks.doProgress(killer, NpcTaskData.KILL_10000_APOLLO_RANGERS);
					}
					if (npc.getId() == 5957) { // noxious troll
						NpcTasks.doProgress(killer, NpcTaskData.KILL_1000_NOXIOUS_TROLLS);
						NpcTasks.doProgress(killer, NpcTaskData.KILL_5000_NOXIOUS_TROLLS);
						NpcTasks.doProgress(killer, NpcTaskData.KILL_10000_NOXIOUS_TROLLS);
					}
					if (npc.getId() == 5958) { // azazel beasts
						NpcTasks.doProgress(killer, NpcTaskData.KILL_1000_AZAZEL_BEAST);
						NpcTasks.doProgress(killer, NpcTaskData.KILL_5000_AZAZEL_BEAST);
						NpcTasks.doProgress(killer, NpcTaskData.KILL_10000_AZAZEL_BEAST);
					}

				//	if (npc.getId() == 5959) { // ravanas
					//	NpcTasks.doProgress(killer, NpcTaskData.KILL_1000_RAVANA);
					//	NpcTasks.doProgress(killer, NpcTaskData.KILL_5000_RAVANA);
						//NpcTasks.doProgress(killer, NpcTaskData.KILL_10000_RAVANA);
					//}

					if (npc.getId() == 4545) {
						int chance = RandomUtility.random(500);
						if (chance >=0) {
			                killer.getInventory().add(5023, 1);
						}
						if (chance >=250) {
			                killer.getInventory().add(5023, 2);
						}
						if (chance >=495) {
			                killer.getInventory().add(5023, 100);
						}
						if (chance >=499) {
			                killer.getInventory().add(5023, 1000);
						}
						killer.incrementSummerKC(1);
						killer.sendMessage("You now have: @blu@" + killer.getSummerKC() + " Kills on this NPC" );
					}
					
					if (npc.getId() == 1049) {
						int chance = RandomUtility.random(500);
						if (chance >=0) {
			                killer.getInventory().add(5023, 2);
						}
						if (chance >=250) {
			                killer.getInventory().add(5023, 3);
						}
						if (chance >=495) {
			                killer.getInventory().add(5023, 200);
						}
						if (chance >=499) {
			                killer.getInventory().add(5023, 2000);
						}
						killer.incrementSummer1KC(1);
						killer.sendMessage("You now have: @blu@" + killer.getSummer1KC() + " Kills on this NPC" );
					}
					
					
					if (npc.getId() == 8281) {
						int chance = RandomUtility.random(500);
						if (chance >=0) {
			                killer.getInventory().add(5023, 3);
						}
						if (chance >=250) {
			                killer.getInventory().add(5023, 4);
						}
						if (chance >=495) {
			                killer.getInventory().add(5023, 300);
						}
						if (chance >=499) {
			                killer.getInventory().add(5023, 3000);
						}
						killer.incrementSummer2KC(1);
						killer.sendMessage("You now have: @blu@" + killer.getSummer2KC() + " Kills on this NPC" );
					}
					
					if (npc.getId() == 6599) {
						int chance = RandomUtility.random(500);
						if (chance >=0) {
			                killer.getInventory().add(5023, 4);
						}
						if (chance >=250) {
			                killer.getInventory().add(5023, 5);
						}
						if (chance >=495) {
			                killer.getInventory().add(5023, 400);
						}
						if (chance >=499) {
			                killer.getInventory().add(5023, 4000);
						}
						killer.incrementSummer2KC(1);
						killer.sendMessage("You now have: @blu@" + killer.getSummer2KC() + " Kills on this NPC" );
					}
					
					
					
					
					
					
					if (npc.getId() == 420) {
						int chance = RandomUtility.random(28);
						if (chance >=23) {
							killer.getInventory().add(6532, 1);
							killer.sendMessage("@red@LUCKY! @bla@You have received a @red@donator ticket");
						}
					}

					
					if (npc.getId() == 812) {
						int chance = RandomUtility.random(25);
						if (chance >=22) {
			                killer.getInventory().add(6532, 1);
							killer.sendMessage("@red@LUCKY! @bla@You have received a @red@donator ticket");
						}
					}
					
					if (npc.getId() == 3224) {
						int chance = RandomUtility.random(25);
						if (chance >=21) {
			                killer.getInventory().add(6532, 1);
							killer.sendMessage("@red@LUCKY! @bla@You have received a @red@donator ticket");
						}
					}
					
					if (npc.getId() == 12299) {
						int chance = RandomUtility.random(25);
						if (chance >=20) {
			                killer.getInventory().add(6532, 1);
							killer.sendMessage("@red@LUCKY! @bla@You have received a @red@donator ticket");
						}
					}
					
					if (npc.getId() == 2060) {
						int chance = RandomUtility.random(5);
						if (chance >=0) {
			                killer.getInventory().add(6532, 1);
							killer.sendMessage("@red@LUCKY! @bla@You have received a @red@donator ticket");
								SlashBoss.handleDrop(npc);
					}
					}
					
					if (npc.getId() == 1070) {
						int chance = RandomUtility.random(4);
						if (chance >=0) {
			                killer.getInventory().add(6532, 1);
							killer.sendMessage("@red@LUCKY! @bla@You have received a @red@donator ticket");
								BellyBoss.handleDrop(npc);
					}
					}

					//Start Raids1 NPCS

					//End Raids1 NPCS



					if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null
							&& killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
						killer.incrementNPCKills(1);
					}
					if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null
							&& killer.getSummoning().getFamiliar().getSummonNpc().getId() == 1252) {
						killer.incrementNPCKills(1);
					}

					if (npc.getId() == 6306) {
						int chance = RandomUtility.random(1000);
						if (chance >=1000) {
							killer.getPointsHandler().incrementMiniGamePoints4(1);
							killer.sendMessage("You have received a Minigamepoint4!! You now have: " + killer.getPointsHandler().getminiGamePoints4() + " Minigame4 Points");
						}
					}
					killer.getAchievementTracker().progress(com.arlania.world.content.achievements.AchievementData.KILL_1000_MONSTERS, 1);
					if (npc.getId() == 11204) {
						killer.getAchievementTracker().progress(com.arlania.world.content.achievements.AchievementData.KILL_A_ZOMBIE_KID, 1);
					} else if (npc.getId() == 3200) {
					} else if (npc.getId() == 8349) {
					} else if (npc.getId() == 3491) {
					} else if (npc.getId() == 12840 || npc.getId() == 12841) {
						StarterTasks.finishTask(killer, StarterTaskData.KILL_WARMONGER);
					} else if (npc.getId() == 8528) {
					} else if (npc.getId() == 2745) {
						StarterTasks.doProgress(killer, StarterTaskData.KILL_10_JADS);
					} else if (npc.getId() == 5996) {
						StarterTasks.doProgress(killer, StarterTaskData.KILL_10_GLODS);
					} else if (npc.getId() == 4540) {
					} else if (npc.getId() == 6260) {
						killer.getAchievementAttributes().setGodKilled(0, true);
					} else if (npc.getId() == 6222) {
						killer.getAchievementAttributes().setGodKilled(1, true);
					} else if (npc.getId() == 6247) {
						killer.getAchievementAttributes().setGodKilled(2, true);
					} else if (npc.getId() == 6203) {
						killer.getAchievementAttributes().setGodKilled(3, true);
					} else if (npc.getId() == 8133) {
						killer.getAchievementTracker().progress(com.arlania.world.content.achievements.AchievementData.DEFEAT_THE_CORPOREAL_BEAST, 1);
					} else if (npc.getId() == 3154) {
						killer.getAchievementTracker().progress(com.arlania.world.content.achievements.AchievementData.DEFEAT_NEX, 1);
						killer.getAchievementAttributes().setGodKilled(4, true);
					}
					killer.setNpcKills(killer.getNpcKills() + 1);
					/** ACHIEVEMENTS **/
					switch (killer.getLastCombatType()) {
					case MAGIC:
						killer.getAchievementTracker().progress(com.arlania.world.content.achievements.AchievementData.KILL_A_MONSTER_USING_MAGIC, 1);
						break;
					case MELEE:
						killer.getAchievementTracker().progress(com.arlania.world.content.achievements.AchievementData.KILL_A_MONSTER_USING_MELEE, 1);
						break;
					case RANGED:
						killer.getAchievementTracker().progress(com.arlania.world.content.achievements.AchievementData.KILL_A_MONSTER_USING_RANGED, 1);
						break;
					}

					/** LOCATION KILLS **/
					if (npc.getLocation().handleKilledNPC(killer, npc)) {
						stop();
						return;
					}

					

					//minigame @emerald

					//minigame @emerald
					//START FIRST ROOM NPCS
					if (npc.getId() == 196)
						if (RandomUtility.RANDOM.nextInt(10) == 1) {
							TeleportHandler.teleportPlayer(killer, new Position(2405, 3482, 4), TeleportType.NORMAL);
							killer.getPacketSender()
							.sendMessage("@whi@<shad=999>You have randomly been teleported to room 2");
						}



					//START SECOND ROOM NPCS
					if (npc.getId() == 1683) 
						if (RandomUtility.RANDOM.nextInt(15) == 2) {
							TeleportHandler.teleportPlayer(killer, new Position(2405, 3482, 8), TeleportType.NORMAL);
							killer.getPacketSender()
							.sendMessage("@whi@<shad=999>You have randomly been teleported to room 3");
						}

					//START THIRD ROOM NPCS
					if (npc.getId() == 282) 
						if (RandomUtility.RANDOM.nextInt(20) == 3) {
							TeleportHandler.teleportPlayer(killer, new Position(2405, 3482, 12), TeleportType.NORMAL);
							killer.getPacketSender()
							.sendMessage("@whi@<shad=999>You have randomly been teleported to room 4");
						}


					//START FOURTH ROOM NPCS

					if (npc.getId() == 158) 
						if (RandomUtility.RANDOM.nextInt(20) == 4) {
							TeleportHandler.teleportPlayer(killer, new Position(2405, 3482, 16), TeleportType.NORMAL);
							killer.getPacketSender()
							.sendMessage("@whi@<shad=999>You have randomly been teleported to room 5");
						}


					//START 5th ROOM NPCS

					if (npc.getId() == 181) 
						if (RandomUtility.RANDOM.nextInt(20) == 5) {
							TeleportHandler.teleportPlayer(killer, new Position(2405, 3482, 20), TeleportType.NORMAL);
							killer.getPacketSender()
							.sendMessage("@whi@<shad=999>You have randomly been teleported to room 6");
						}

					//START 6th ROOM NPCS


					if (npc.getId() == 202) 
						if (RandomUtility.RANDOM.nextInt(20) == 6) {
							TeleportHandler.teleportPlayer(killer, new Position(2405, 3482, 24), TeleportType.NORMAL);
							killer.getPacketSender()
							.sendMessage("@gre@<shad=999>You have randomly been teleported to the boss room !");
						}


					//LAST ROOM BOSSROOM

					if (npc.getId() == 6357) {		
						killer.getInventory().add(16641, 1);
						killer.moveTo(GameSettings.DEFAULT_POSITION);
						killer.getPacketSender()
						.sendMessage("You have completed the minigame! You earn a @red@");
					}

					//END LAST ROOM NPCS

                    if(killer.isInRaid()) {
                        killer.getCustomRaidParty().getOwner().getCustomRaid().handleKill();
                        ThirdStrategy.handleKill(killer);
                        ThirdStrategy.handleMinionDeath(npc);
                    }

					/** PARSE DROPS **/
                    if (npc.getId() == 12800) {
						Bork.handleDrop(npc);
						
					} else if(npc.getId() == 9911) {
						General.handleDrop(npc);
						System.out.println("deathtask");
						stop();
					} else if (npc.getId() == 4595) {
						SummerladyBoss.handleDrop(npc);
						System.out.println("deathtask");
								stop();
						return;
					} else {
						NPCDrops.dropItems(killer, npc);
					}

					/** SLAYER **/
					killer.getBloodSlayer().killedNpc(npc);
					
					/** SLAYER **/
					killer.getSlayer().killedNpc(npc);

					QuestTab.updateCharacterSummaryTab(killer, QuestTab.UpdateData.NPC_KILLS);
					LevelSystem.appendExperience(killer);
					new InstanceManager(killer).death(killer, npc, npc.getDefinition().getName());
				}

			
				stop();
				break;
		}
			ticks--;
		} catch (Exception e) {
			e.printStackTrace();
			stop();
		}
	}

	private void giveDemonKills(Player killer2, NPC npc2, Position position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		setEventRunning(false);
		npc.setDying(false);
		

		// respawn
		if (npc.shouldRespawnNpc() && npc.getDefinition().getRespawnTime() > 0
				&& npc.getLocation() != Location.GRAVEYARD &&
				npc.getLocation() != Location.DUNGEONEERING && !killer.isInRaid() 
				&& !npc.equals(killer.getInstanceInterface().getNpcToSpawn())
				&& !(npc instanceof InstancedNpc)) {
			TaskManager.submit(new NPCRespawnTask(npc, npc.getDefinition().getRespawnTime(), killer));

		} else {
			//System.out.println("Not setting respawn task for npc: " + npc.getId() + " OBJECT: " + npc);
		}// ok yeah its the one with bugs, but ill fix it dw
		
		//TODO: Figure out why this was here
		//System.out.println("-> " + npc.equals(killer.getInstanceInterface().getNpcToSpawn()));
		if (npc instanceof InstancedNpc) {
			((InstancedNpc) npc).runRespawn();
		} else if(killer.isInInstance()) {
	        killer.getInstanceInterface().handleKill();
		} else {
			World.deregister(npc);
		}
		


	}
}

