package com.arlania.model.input.impl;

import com.arlania.model.Item;
import com.arlania.model.input.EnterAmount;
import com.arlania.model.input.Input;
import com.arlania.util.Misc;
import com.arlania.world.content.keepsake.KeepSake;
import com.arlania.world.entity.impl.player.Player;

public class ConvertTaxBags extends Input {
	
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
		int totalTaxBags = player.getInventory().getAmount(10835);
		int totalBloodBagsAllowed = totalTaxBags / 250;
		if(amount > totalBloodBagsAllowed) amount = totalBloodBagsAllowed;
		int taxBagsRemoved = amount * 250;

		
		player.getInventory().delete(new Item(10835, taxBagsRemoved));
		player.getInventory().add(new Item(17750, amount));
		player.getPacketSender().sendMessage("You exchange " + Misc.format(taxBagsRemoved) + " Taxbags For " + Misc.format(amount) + " Blood bags");
	}
}