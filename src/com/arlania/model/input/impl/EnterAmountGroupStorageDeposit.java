package com.arlania.model.input.impl;

import com.arlania.model.input.EnterAmount;
import com.arlania.world.content.gim.SharedStorage;
import com.arlania.world.entity.impl.player.Player;

public class EnterAmountGroupStorageDeposit extends EnterAmount {

	public EnterAmountGroupStorageDeposit(int item, int slot) {
		super(item, slot);
	}
	
	@Override
	public void handleAmount(Player player, int amount) {
		if(getItem() > 0 && getSlot() >= 0)
			SharedStorage.deposit(player, getSlot(), getItem(), amount, true, true);
		else
			player.getPacketSender().sendInterfaceRemoval();
	}
}
