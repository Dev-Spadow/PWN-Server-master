package com.arlania.world.content;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.Animation;
import com.arlania.model.Position;
import com.arlania.model.Skill;
import com.arlania.util.Misc;
import com.arlania.util.RandomUtility;
import com.arlania.world.World;
import com.arlania.world.content.StarterTasks.StarterTaskData;
import com.arlania.world.content.digevent.DigEventHandler;
import com.arlania.world.content.treasuretrails.CoordinateScrolls;
import com.arlania.world.content.treasuretrails.DiggingScrolls;
import com.arlania.world.content.treasuretrails.MapScrolls;
import com.arlania.world.entity.impl.player.Player;

public class Digging {
	
	
	
	
	
	
	public static void dig(final Player player) {
		
		if(!player.getClickDelay().elapsed(3000))
			return;
		
		if(Misc.getRandom(8000) == 3) {
			player.getInventory().add(1648, 1);
			World.sendMessage("@blu@<img=10>[Archeology] "+player.getUsername()+" has received the Digger Pet!");
			player.getPacketSender().sendMessage("@red@You have received a skilling pet!");
		}
		player.getMovementQueue().reset();
	    player.getSkillManager().addExperience(Skill.ARCHEOLOGY,  50);
		
	    if(Misc.getRandom(500) == 3) {
			player.getInventory().add(793, 1);
			World.sendMessage("@blu@<img=10>[Archeology] "+player.getUsername()+" has received the Daconia Rock!");
			player.getPacketSender().sendMessage("@red@You have received a special rock!");
		}
	    

		
		
		player.getPacketSender().sendMessage("You start digging..");
		
		
		player.performAnimation(new Animation(830));
		
		
		
		
		
		
		
		
		TaskManager.submit(new Task(2, player, false) {
			@Override
			public void execute() {

				if (DigEventHandler.gameActive) {
					DigEventHandler.handleDigAttempt(player);
				}


				/**
				 * Clue scrolls
				 */
				if (/*ClueScrolls.digSpot(player)*/DiggingScrolls.digClue(player) || MapScrolls.digClue(player) || CoordinateScrolls.digClue(player)) {
					stop();
					return;
				}
				Position targetPosition = null;
				
				/**
				 * Barrows
				 */
				if (inArea(player.getPosition(), 3553, 3301, 3561, 3294))
					targetPosition = new Position(3578, 9706, -1);
				/** clue scrolls** x,y**/
				else if (inClue(player.getPosition(), 3096, 3496))
					if (player.getInventory().contains(2677)) {
					player.getInventory().delete(2677, 1);
					player.getInventory().add(2714, 1);
					ClueScrolls.incrementCluesCompleted(1);
					player.getPacketSender().sendMessage("You found a casket, open it for your reward!");
					StarterTasks.finishTask(player, StarterTaskData.COMPLETE_A_CLUE_SCROLL);
					stop();
				} else {
					
				}
					else if (inClue(player.getPosition(), 3028, 9741))
						if (player.getInventory().contains(2678)) {
						player.getInventory().delete(2678, 1);
						player.getInventory().add(2714, 1);
						ClueScrolls.incrementCluesCompleted(1);
						player.getPacketSender().sendMessage("You found a casket, open it for your reward!");
						StarterTasks.finishTask(player, StarterTaskData.COMPLETE_A_CLUE_SCROLL);
						stop();
					} else {
						
					}
						else if (inClue(player.getPosition(), 3366, 3267))
							if (player.getInventory().contains(2679)) {
							player.getInventory().delete(2679, 1);
							player.getInventory().add(2714, 1);
							ClueScrolls.incrementCluesCompleted(1);
							player.getPacketSender().sendMessage("You found a casket, open it for your reward!");
							StarterTasks.finishTask(player, StarterTaskData.COMPLETE_A_CLUE_SCROLL);
							stop();
						}else {
							
						}
							else if (inClue(player.getPosition(), 3145, 9915))
								if (player.getInventory().contains(2680)) {
								player.getInventory().delete(2680, 1);
								player.getInventory().add(2714, 1);
								ClueScrolls.incrementCluesCompleted(1);
								player.getPacketSender().sendMessage("You found a casket, open it for your reward!");
								StarterTasks.finishTask(player, StarterTaskData.COMPLETE_A_CLUE_SCROLL);
								stop();
							} else {
								
							}
								else if (inClue(player.getPosition(), 2341, 3698))
									if (player.getInventory().contains(2681)) {
									player.getInventory().delete(2681, 1);
									player.getInventory().add(2714, 1);
									ClueScrolls.incrementCluesCompleted(1);
									player.getPacketSender().sendMessage("You found a casket, open it for your reward!");
									StarterTasks.finishTask(player, StarterTaskData.COMPLETE_A_CLUE_SCROLL);
									stop();
								} else {
									
								}
									else if (inClue(player.getPosition(), 3451, 3717))
										if (player.getInventory().contains(2682)) {
										player.getInventory().delete(2682, 1);
										player.getInventory().add(2714, 1);
										ClueScrolls.incrementCluesCompleted(1);
										player.getPacketSender().sendMessage("You found a casket, open it for your reward!");
										StarterTasks.finishTask(player, StarterTaskData.COMPLETE_A_CLUE_SCROLL);
										stop();
									} else {
										
									}
										else if (inClue(player.getPosition(), 2280, 4697))
											if (player.getInventory().contains(2683)) {
											player.getInventory().delete(2683, 1);
											player.getInventory().add(2714, 1);
											ClueScrolls.incrementCluesCompleted(1);
											player.getPacketSender().sendMessage("You found a casket, open it for your reward!");
											StarterTasks.finishTask(player, StarterTaskData.COMPLETE_A_CLUE_SCROLL);
											stop();
										} 	else {
											
										}
											else if (inClue(player.getPosition(), 2660, 2651))
												if (player.getInventory().contains(2684)) {
												player.getInventory().delete(2684, 1);
												player.getInventory().add(2714, 1);
												ClueScrolls.incrementCluesCompleted(1);
												player.getPacketSender().sendMessage("You found a casket, open it for your reward!");
												StarterTasks.finishTask(player, StarterTaskData.COMPLETE_A_CLUE_SCROLL);
												stop();
											} else {
												
											}
												
											
											
											
											
											
											
											else if (inArea(player.getPosition(), 2657, 2658, 2664, 2657))
													if (player.getInventory().contains(952)) {
						                                   player.getInventory().add(15286, 1);//arch frags
						                                   player.getInventory().add(19864, 1);
						                                   player.getSkillManager().addExperience(Skill.ARCHEOLOGY,  50);
						                                  
													stop();
												} else {
													
												}
												
												
												
												
												
				else if (inArea(player.getPosition(), 3042, 3026, 3305, 3306))
                        player.getInventory().add(15286, 2);//arch frags
                        player.getSkillManager().addExperience(Skill.ARCHEOLOGY,  50);
                       
               
				
				
				
				//else if (inArea(player.getPosition(), 3561, 3292, 3568, 3285))
					//targetPosition = new Position(3557, 9703, -1);
				//else if (inArea(player.getPosition(), 3570, 3302, 3579, 3293))
					//targetPosition = new Position(3556, 9718, -1);
				//else if (inArea(player.getPosition(), 3571, 3285, 3582, 3278))
					//targetPosition = new Position(3534, 9704, -1);
				//else if (inArea(player.getPosition(), 3562, 3279, 3569, 3273))
					//targetPosition = new Position(3546, 9684, -1);
				//else if (inArea(player.getPosition(), 2986, 3370, 3013, 3388))
					//targetPosition = new Position(3546, 9684, -1);
				//if(targetPosition != null)
					//player.moveTo(targetPosition);
				//else
					//player.getPacketSender().sendMessage("You find nothing of interest.");
			//	targetPosition = null;
				stop();
			}
		});
		player.getClickDelay().reset();
	}

	private static boolean inArea(Position pos, int x, int y, int x1, int y1) {
		return pos.getX() > x && pos.getX() < x1 && pos.getY() < y && pos.getY() > y1;
	}
	private static boolean inClue(Position pos, int x, int y) {
		return pos.getX() == x && pos.getY() == y ;
	}
}
