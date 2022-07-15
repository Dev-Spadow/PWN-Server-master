package com.arlania.model.input.impl;

import com.arlania.model.Item;
import com.arlania.model.input.Input;
import com.arlania.util.Misc;
import com.arlania.world.entity.impl.player.Player;

public class ConvertBloodBag extends Input {
	
	@Override
	public void handleSyntax(Player player, String syntax) {
		if(syntax.length() < 1) {
			player.getPacketSender().sendMessage("Invalid syntax entered.");
			return;
		}
		int amount = Integer.parseInt(syntax);
		if(amount < 1) {
			player.getPacketSender().sendMessage("Amount needs to be higher then 0.");
			return;
		}
		int totalBloodBags = player.getInventory().getAmount(17750);
		if(amount > totalBloodBags) amount = totalBloodBags;
		int taxBagsAdded = amount * 125;

		
		player.getInventory().delete(new Item(17750, amount));
		player.getInventory().add(new Item(10835, taxBagsAdded));
		player.getPacketSender().sendMessage("You exchange " + Misc.format(amount) + " Blood bags For " + Misc.format(taxBagsAdded) + " Taxbags");
	}
}