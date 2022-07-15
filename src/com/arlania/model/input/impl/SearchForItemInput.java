package com.arlania.model.input.impl;

import com.arlania.model.input.Input;
import com.arlania.world.content.dropchecker.NPCDropTableChecker;
import com.arlania.world.entity.impl.player.Player;

public class SearchForItemInput extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		NPCDropTableChecker.getSingleton().searchForItem(player, syntax);
	}
	
}
