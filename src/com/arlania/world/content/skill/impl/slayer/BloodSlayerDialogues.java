package com.arlania.world.content.skill.impl.slayer;

import com.arlania.util.Misc;
import com.arlania.world.content.dialogue.Dialogue;
import com.arlania.world.content.dialogue.DialogueExpression;
import com.arlania.world.content.dialogue.DialogueManager;
import com.arlania.world.content.dialogue.DialogueType;
import com.arlania.world.entity.impl.player.Player;

public class BloodSlayerDialogues {

	/**
	 * Dialogues that can't be handled by XML
	 */
	public static Dialogue dialogue(final Player player) {
		return new Dialogue() {

			@Override
			public DialogueType type() {
				return DialogueType.NPC_STATEMENT;
			}
			
			@Override
			public int npcId() {
				return player.getBloodSlayer().getBloodSlayerMaster().getNpcId();
			}
			
			@Override
			public DialogueExpression animation() {
				return DialogueExpression.TALK_SWING;
			}
			
			@Override
			public String[] dialogue() {
				return new String[] {
					"Hello.",
					"Do you need anything?"
				};
			}
			
			@Override
			public void specialAction() {
				
			}
			
			@Override
			public Dialogue nextDialogue() {
				return new Dialogue() {

					@Override
					public DialogueType type() {
						return DialogueType.OPTION;
					}

					@Override
					public int npcId() {
						return 1531;
					}
					
					@Override
					public DialogueExpression animation() {
						return null;
					}
					
					@Override
					public String[] dialogue() {
						return new String[] {
							"What's my current assignment?",
							"I'd like to reset my Slayer Task",
							"How many points do I currently receive per task?",
							player.getBloodSlayer().getDuoPartner() != null ? "I'd like to reset my duo team" : "Nothing, thanks"
								
						};
					}
					
					@Override
					public void specialAction() {
						if(player.getBloodSlayer().getBloodSlayerTask() == BloodSlayerTasks.NO_TASK) {
							player.getPacketSender().sendInterfaceRemoval();
							DialogueManager.start(player, secondDialogue(player));
							player.setDialogueActionId(333);
							return;
						}
						player.setDialogueActionId(32);
					}
				};
			}
		};
	}
	
	
	public static Dialogue secondDialogue(final Player player) {
		return new Dialogue() {

			@Override
			public DialogueType type() {
				return DialogueType.OPTION;
			}

			@Override
			public int npcId() {
				return 1531;
			}
			
			@Override
			public DialogueExpression animation() {
				return null;
			}
			
			@Override
			public String[] dialogue() {
				boolean inDuo = player.getBloodSlayer().getDuoPartner() != null;
				return new String[] {
					"I'd like a @red@Blood Slayer task",
					"I'd like to view your @red@Blood Slayer rewards",
					"I'd like to view your stock of @red@Blood Slayer items",
					inDuo ? "I'd like to reset my duo team" : "Nothing, thanks"
				};
			}
			
			@Override
			public void specialAction() {
				
			}
		};
	}
	
	public static Dialogue receivedBloodTask(final Player player, final BloodSlayerMaster bloodmaster, final BloodSlayerTasks task) {
		return new Dialogue() {	
			final int amountToKill = player.getBloodSlayer().getAmountToSlay();
			@Override
			public DialogueType type() {
				return DialogueType.NPC_STATEMENT;
			}

			@Override
			public int npcId() {
				return bloodmaster.getNpcId();
			}
			
			@Override
			public DialogueExpression animation() {
				return DialogueExpression.NORMAL;
			}
			
			@Override
			public String[] dialogue() {
				boolean duoSlayer = player.getBloodSlayer().getDuoPartner() != null;
				String you = duoSlayer ? "You and your partner" : "You";
				String line1 = "You have been assigned to kill "+amountToKill+" "+Misc.formatText(task.toString().toLowerCase().replaceAll("_", " "))+"s.";
				String line2 = "";
				if(duoSlayer) {
					line1 = ""+you+" have been assigned to kill";
					line2 = ""+amountToKill+" "+Misc.formatText(task.toString().toLowerCase().replaceAll("_", " "))+"s.";
				}
				if(player.getBloodSlayer().getLastTask() != BloodSlayerTasks.NO_TASK) {
					line1 = ""+you+" are doing great! Your new";
					line2 = "assignment is to kill "+amountToKill+" "+Misc.formatText(task.toString().toLowerCase().replaceAll("_", " "))+"s.";
				}
				return new String[] {
					""+line1+"",
					""+line2+""
				};
			}
			
			@Override
			public void specialAction() {
				
			}
			@Override
			public Dialogue nextDialogue() {
				return new Dialogue() {

					@Override
					public DialogueType type() {
						return DialogueType.OPTION;
					}

					@Override
					public int npcId() {
						return bloodmaster.getNpcId();
					}
					
					@Override
					public DialogueExpression animation() {
						return null;
					}
					
					@Override
					public String[] dialogue() {
						boolean inDuo = player.getBloodSlayer().getDuoPartner() != null;
						return new String[] {
							"What's my current assignment?",
							"I'd like to reset my Slayer Task",
							"How many points do I currently receive per task?"
							,inDuo ? "I'd like to reset my duo team" : "Nothing, thanks"
								
						};
					}
					
					@Override
					public void specialAction() {
						player.setDialogueActionId(35);
					}
				};
			}
		};
	}

	public static Dialogue findAssignment(final Player player) {
		final BloodSlayerMaster master = player.getBloodSlayer().getBloodSlayerMaster();
		final BloodSlayerTasks task = player.getBloodSlayer().getBloodSlayerTask();
			
		return new Dialogue() {
			@Override
			public DialogueType type() {
				return DialogueType.NPC_STATEMENT;
			}

			@Override
			public int npcId() {
				return master.getNpcId();
			}
			
			@Override
			public DialogueExpression animation() {
				return DialogueExpression.NORMAL;
			}
			
			@Override
			public String[] dialogue() {
				String l = "";
				if(task != null)
					l = task.getNpcLocation();
				return new String[] {
					"Your current task is to kill "+(player.getBloodSlayer().getAmountToSlay())+" "+Misc.formatText(task.toString().toLowerCase().replaceAll("_", " "))+"s.",
					""+l+""
				};
			}
			
			@Override
			public void specialAction() {
			
			}
		};
	}
	
	public static Dialogue resetTaskDialogue(final Player player) {
		final BloodSlayerMaster master = player.getBloodSlayer().getBloodSlayerMaster();
		return new Dialogue() {
			@Override
			public DialogueType type() {
				return DialogueType.NPC_STATEMENT;
			}

			@Override
			public int npcId() {
				return master.getNpcId();
			}
			
			@Override
			public DialogueExpression animation() {
				return DialogueExpression.NORMAL;
			}
			
			@Override
			public String[] dialogue() {
				return new String[] {
					"It currently costs 25 Blood Slayer points to reset a task.",
					"You will also lose your current Task Streak.",
					"Are you sure you wish to continue?"
				};
			}
			
			@Override
			public void specialAction() {
				
			}
			@Override
			public Dialogue nextDialogue() {
				return new Dialogue() {

					@Override
					public DialogueType type() {
						return DialogueType.OPTION;
					}
					
					@Override
					public DialogueExpression animation() {
						return null;
					}
					
					@Override
					public String[] dialogue() {
						return new String[] {
							"Yes, please",
							"Cancel"
								
						};
					}
					
					@Override
					public void specialAction() {
						player.setDialogueActionId(33);
					}
				};
			}
		};
	}
	
	public static Dialogue totalPointsReceived(final Player player) {
		return new Dialogue() {
			@Override
			public DialogueType type() {
				return DialogueType.NPC_STATEMENT;
			}

			@Override
			public int npcId() {
				return player.getBloodSlayer().getBloodSlayerMaster().getNpcId();
			}
			
			@Override
			public DialogueExpression animation() {
				return DialogueExpression.NORMAL;
			}
			
			@Override
			public String[] dialogue() {
				int pointsReceived = 4;
			/*	if(player.getBloodSlayerMaster() == Slayer) //medium task
					pointsReceived = 7;
				if(player.getBloodSlayerMaster().getTaskLevel() == 2) //hard/elite tasks
					pointsReceived = 10;*/
				int per5 = pointsReceived * 3;
				int per10 = pointsReceived * 5;
				return new String[] {
					"You currently receive "+pointsReceived+" points per task,",
					""+per5+" bonus points per 5 task-streak and",
					""+per10+" bonus points per 10 task-streak."
				};
			}
			
			@Override
			public void specialAction() {
				
			}
		};
	}
	
	public static Dialogue inviteDuo(final Player player, final Player inviteOwner) {
		return new Dialogue() {
			@Override
			public DialogueType type() {
				return DialogueType.STATEMENT;
			}
			
			@Override
			public DialogueExpression animation() {
				return null;
			}
			
			@Override
			public String[] dialogue() {
				return new String[] {
					""+inviteOwner.getUsername()+" has invited you to form a duo Slayer team.",
				};
			}
			
			@Override
			public Dialogue nextDialogue() {
				return new Dialogue() {

					@Override
					public DialogueType type() {
						return DialogueType.OPTION;
					}
					
					@Override
					public DialogueExpression animation() {
						return null;
					}
					
					@Override
					public String[] dialogue() {
						return new String[] {
							"Accept "+inviteOwner.getUsername()+"'s invitation",
							"Decline "+inviteOwner.getUsername()+"'s invitation"
								
						};
					}
					
					@Override
					public void specialAction() {
						player.setDialogueActionId(34);
						player.getBloodSlayer().setDuoInvitation(inviteOwner.getUsername());
					}
				};
			}
		};
	}
}
