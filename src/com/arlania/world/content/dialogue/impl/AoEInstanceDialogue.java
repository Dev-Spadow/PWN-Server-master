package com.arlania.world.content.dialogue.impl;

import com.arlania.util.Misc;
import com.arlania.world.content.aoesystem.AoEInstance;
import com.arlania.world.content.dialogue.Dialogue;
import com.arlania.world.content.dialogue.DialogueExpression;
import com.arlania.world.content.dialogue.DialogueType;
import com.arlania.world.entity.impl.player.Player;

public class AoEInstanceDialogue {

	public static Dialogue needAoEWeapon(Player player) {
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
				return 6072;
			}

			@Override
			public Dialogue nextDialogue() {
				return null;
			}

			@Override
			public String[] dialogue() {
				return new String[] { "You need an AoE weapon to start an instance!" };
			}

			@Override
			public void specialAction() {
				player.setDialogueActionId(0);
			}
		};
	}
	
	public static Dialogue confirmPayment(Player player, int npcId, int amount) {
		player.setPlayerLocked(true);
		player.setAoeNpc(npcId);
		AoEInstance instance = new AoEInstance(player);
		if(player.getInventory().getAmount(5606) > 0) {
			player.setAoEPayment(42);
			instance.setTime(30);
		} else {
			player.setAoEPayment(amount);
			instance.setTime(30);
		}
		return new Dialogue() {
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
				return 6072;
			}

			@Override
			public Dialogue nextDialogue() {
				return null;
			}

			@Override
			public String[] dialogue() {
				if(player.getAoEPayAmount() != 42)
					return new String[] { "Pay " + Misc.format(amount) + " for AoE Instance", "Exit" };
				else
					return new String[] { "Pay with voucher  for AoE Instance", "Exit" };
			}

			
			
			@Override
			public void specialAction() {
				player.setDialogueActionId(12000);
			}
		};
	}
}
