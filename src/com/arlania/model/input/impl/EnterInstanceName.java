package com.arlania.model.input.impl;

import com.arlania.model.input.Input;
import com.arlania.world.content.aoesystem.AoEInstance;
import com.arlania.world.entity.impl.player.Player;

public class EnterInstanceName extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		AoEInstance.requestInstance(player, syntax);
	}
}
