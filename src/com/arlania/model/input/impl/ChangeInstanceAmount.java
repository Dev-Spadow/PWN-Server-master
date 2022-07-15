package com.arlania.model.input.impl;

import com.arlania.model.input.Input;
import com.arlania.world.content.InstanceInterfaceManager;
import com.arlania.world.entity.impl.player.Player;

public class ChangeInstanceAmount extends Input {

	
	public void handleSyntax(Player player, String text) {
		
		boolean isNumber = text.matches("[0-9]+");
		
		if(!isNumber) {
			player.sendMessage("What you entered isn't a number.");
			return;
		}
		int spawnAmount = Integer.parseInt(text);
		if(spawnAmount > InstanceInterfaceManager.MAX_INSTANCE_AMOUNT) {
            player.sendMessage("@blu@The max amount is: @red@" + InstanceInterfaceManager.MAX_INSTANCE_AMOUNT);
		    return;
        }
		
		player.getInstanceInterface().setSpawnAmount(spawnAmount);
		
		player.sendMessage("@blu@Spawn amount has been set to: @red@" + player.getInstanceInterface().getSpawnAmount());
	}
	
}
