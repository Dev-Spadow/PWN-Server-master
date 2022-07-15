package com.arlania.model.input.impl;

import com.arlania.model.input.EnterAmount;
import com.arlania.world.entity.impl.player.Player;

public class EnterAmountToGamble extends EnterAmount {

	public EnterAmountToGamble(int item, int slot) {
		super(item, slot);
	}
	
	@Override
	public void handleAmount(Player player, int amount) {
		if(getItem() > 0 && getSlot() >= 0 && getSlot() < 28)
			player.getGambling().gambleItem(getItem(), amount, getSlot());
		else
			player.getPacketSender().sendInterfaceRemoval();
	}

}
