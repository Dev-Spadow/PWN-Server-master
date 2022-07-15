package com.arlania.model.input.impl;

import com.arlania.model.input.Input;
import com.arlania.world.content.bosses.BlackstoneWarmonger;
import com.arlania.world.entity.impl.player.Player;

public class DonateTaxBagsInput extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {

		player.getPacketSender().sendInterfaceRemoval();
		

		String regex = "[0-9]+";

		if (!syntax.matches(regex)) {
			player.sendMessage("@red@Enter an amount not letters :/");
			return;
		}

		int amount = Integer.parseInt(syntax);

		if (amount > BlackstoneWarmonger.amountNeeded) {
			player.sendMessage("@red@I can only take " + BlackstoneWarmonger.amountNeeded + " Vote scrolls.");
			return;
		} else if (player.getInventory().contains(19670, amount)) {
			player.getInventory().delete(19670, amount);
			player.sendMessage("You have donated: " + amount + " Vote scrolls");
			BlackstoneWarmonger.amountNeeded -= amount;
			hasReached();
			return;
		} else {
			player.sendMessage("@red@You don't have that many Vote scrolls :/");
		}
	}

	public boolean hasReached() {
		if(BlackstoneWarmonger.amountNeeded < 1) {
			BlackstoneWarmonger.spawn();
			return true;
		} else {
			return false;
		}
	}

}
