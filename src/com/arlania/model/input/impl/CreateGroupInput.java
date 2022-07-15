package com.arlania.model.input.impl;

import com.arlania.model.input.Input;
import com.arlania.world.content.GamemodeSelecter;
import com.arlania.world.entity.impl.player.Player;

public class CreateGroupInput extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		player.getPacketSender().sendInterfaceRemoval();
		if (syntax.length() <= 1) {
			player.getPacketSender().sendMessage("@red@Invalid syntax entered.");
			return;
		}
		
		System.out.println("syntax: " + syntax);
		GamemodeSelecter.create(player, syntax, player.getTempSkillBoolean());
	}
}