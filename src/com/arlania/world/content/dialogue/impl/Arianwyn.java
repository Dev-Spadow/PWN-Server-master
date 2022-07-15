package com.arlania.world.content.dialogue.impl;

import com.arlania.model.input.impl.ItemSearch;
import com.arlania.util.Misc;
import com.arlania.world.content.dialogue.Dialogue;
import com.arlania.world.content.dialogue.DialogueExpression;
import com.arlania.world.content.dialogue.DialogueType;
import com.arlania.world.entity.impl.player.Player;

public class Arianwyn {

	public static Dialogue getDialogue(Player player, int stage) {
		Dialogue dialogue = null;
		switch (stage) {
		case 0:
			dialogue = new Dialogue() {

				@Override
				public DialogueType type() {
					return DialogueType.OPTION;
				}

				@Override
				public DialogueExpression animation() {
					return DialogueExpression.NORMAL;
				}

				@Override
				public int npcId() {
					return 2358;
				}

				@Override
				public String[] dialogue() {
					return new String[] { "Hello, Who are you?", "<img=552>Help the Ghost of Georgie", "<img=552>Free Georgie from the sewers", "Exit"};
				}
				
				@Override
				public void specialAction() {
					player.setDialogueActionId(2500);
				}
			};
			break;
		case 1: // selecting raid
			dialogue = new Dialogue() {

				@Override
				public DialogueType type() {
					return DialogueType.NPC_STATEMENT;
				}

				@Override
				public DialogueExpression animation() {
					return DialogueExpression.NORMAL;
				}

				@Override
				public int npcId() {
					return 104;
				}

				@Override
				public String[] dialogue() {
					return new String[] { "I lost my sailboat down the drain",
							"He said he would help me get it back if i played his game",
							"Help i just want to go home, Please find me!" };
				}
				@Override
				public void specialAction() {
					player.setDialogueActionId(2501);
				}
			};
			break;
		}
		return dialogue;
	}

	public static Dialogue foundDrop(String itemName, String npcName) {
		return new Dialogue() {

			@Override
			public DialogueType type() {
				return DialogueType.NPC_STATEMENT;
			}

			@Override
			public DialogueExpression animation() {
				return DialogueExpression.NORMAL;
			}

			@Override
			public int npcId() {
				return 7969;
			}

			@Override
			public String[] dialogue() {
				return new String[] { "Ah, yes! The " + Misc.formatText(itemName) + ".",
						"I believe the " + Misc.anOrA(npcName) + " " + npcName + "", "drops this item!",
						"Good luck in your adventure Pwnlite's finace!" };
			}
		};
	}
}
