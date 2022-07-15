package com.arlania.model.input.impl;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.input.Input;
import com.arlania.world.entity.impl.player.Player;

public class EnterPinPacketListener extends Input {

	@Override
	public void handleSyntax(Player player, String pin) {
		if (pin.equalsIgnoreCase(player.getSavedPin())) {
			player.setSavedIp(player.getHostAddress());
			player.sendMessage("Pin correctly entered");
			player.setPlayerLocked(false);
			player.sendMessage("Player status: unlocked");
		} else {
			TaskManager.submit(new Task(1, player, false) {
				@Override
				protected void execute() {
					player.setInputHandling(new EnterPinPacketListener());
					player.getPacketSender().sendEnterInputPrompt("Enter your pin to play#confirmstatus");
					stop();
				}
			});
		}
	}
}
