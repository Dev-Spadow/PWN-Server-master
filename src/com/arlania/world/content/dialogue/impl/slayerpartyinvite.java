package com.arlania.world.content.dialogue.impl;

import com.arlania.world.content.dialogue.Dialogue;
import com.arlania.world.content.dialogue.DialogueExpression;
import com.arlania.world.content.dialogue.DialogueType;
import com.arlania.world.entity.impl.player.Player;


public class slayerpartyinvite extends Dialogue {

	public slayerpartyinvite(Player inviter, Player p) {
		this.inviter = inviter;
		this.p = p;
	}
	
	private Player inviter, p;
	
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
			""+inviter.getUsername()+" has invited you to join their Slayer party."
		};
	}
	
	@Override
	public int npcId() {
		return -1;
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
				return new String[] {"Join "+inviter.getUsername()+"'s party", "Don't join "+inviter.getUsername()+"'s party."};
			}
			
			@Override
			public void specialAction() {
				p.addPendingslayerparty(inviter.getslayerparty());
				p.setDialogueActionId(2500);
			}
			
		};
	}
}