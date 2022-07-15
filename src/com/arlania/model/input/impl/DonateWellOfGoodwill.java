package com.arlania.model.input.impl;

import com.arlania.model.input.EnterAmount;
import com.arlania.world.entity.impl.player.Player;

public class DonateWellOfGoodwill extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		player.getWellOfGoodwillHandler().donate(player, amount);
	}
}
