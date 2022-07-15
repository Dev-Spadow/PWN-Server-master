package com.arlania.model.input.impl;

import com.arlania.model.input.Input;
import com.arlania.util.Misc;
import com.arlania.world.content.keepsake.KeepSake;
import com.arlania.world.entity.impl.player.Player;

public class RenamePreset extends Input {
	
	@Override
	public void handleSyntax(Player player, String syntax) {
		if(syntax.length() <= 1) {
			player.getPacketSender().sendMessage("Invalid syntax entered.");
			return;
		}
		KeepSake.renamePreset(player, player.getTempItem(), false, Misc.ucFirst(syntax));
	}
	
}
