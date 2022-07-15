package com.arlania.world.content.perk_system;

import com.arlania.util.Misc;
import com.arlania.world.content.dialogue.DialogueManager;
import com.arlania.world.entity.impl.player.Player;

public class PerkButtons {

	public static boolean isPerkButton(Player player, int buttonsId) {
		switch(buttonsId) {
		case -14028:
			player.getPerkHandler().setObtainablePerks(ObtainablePerks.BLOOD_HERO_I);
			player.getPacketSender().replacePerkButton(51508, 1244, Misc.capitalize(ObtainablePerks.BLOOD_HERO_I.name()));
			//DialogueManager.start(player, 127);
			//player.setDialogueActionId(58);
			PerkHandler.getDialogue(player);
			return true;
			
		}
		return false;
	}

}
