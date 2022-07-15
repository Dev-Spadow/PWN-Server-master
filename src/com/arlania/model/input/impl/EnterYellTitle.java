package com.arlania.model.input.impl;

import com.arlania.model.input.Input;
import com.arlania.util.NameUtils;
import com.arlania.world.entity.impl.player.Player;

public class EnterYellTitle extends Input {

	private String[] bannedChars = { "owner","co owner", "admin", "administrator", "mod", "donator", "legendary", "dev",
			"developer", "mod", "staff", "support", };

	@Override
	public void handleSyntax(Player player, String syntax) {

		player.getPacketSender().sendInterfaceRemoval();

		if (syntax == null || syntax.length() < 2 || syntax.length() > 15 || !NameUtils.isValidName(syntax)) {
			player.getPacketSender().sendMessage("That yell title is invalid. Please try again...");
			return;
		}

		for (String s : bannedChars)
			if (s.toLowerCase().contains(syntax)) {
				player.sendMessage("You cannot use " + syntax + " in your custom yell title!");
				return;
			}

		player.getRights().customYellTitle = syntax;
		player.getPacketSender().sendMessage("Your new yell prefix is: [<col=ff0000>" + syntax + "</col>]");
		return;
	}
}