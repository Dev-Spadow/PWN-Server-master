package com.arlania.world.content.crashgame;

import com.arlania.model.input.Input;
import com.arlania.world.content.MoneyPouch;
import com.arlania.world.entity.impl.player.Player;

public class HandleCrashDeposit extends Input
{
	@Override 
	public void handleSyntax(Player player, String amnt) {
		long amount = Long.parseLong(amnt);
		long playerPouch = player.getMoneyInPouch();
		int playerInv = player.getInventory().getAmount(995);
		
		if(amount <= playerPouch + playerInv)
		{
			player.sendMessage("You deposited "+amount+" coins into the Crash Game");
			player.addToCrashBalance(amount);
			if(amount <= playerInv)
			{
				player.getInventory().delete(995, (int)amount);
			}
			else
			{
				player.getInventory().delete(995,playerInv,true);
				amount -= playerInv;
				player.setMoneyInPouch(playerPouch - amount);
				MoneyPouch.refresh(player);
			}
			player.getPacketSender().sendString(62013, String.format("%.0fk", Math.floor(player.getCrashGameBalance()/1000)));
		}
		else
		{
			player.sendMessage("You can't deposit more money than you have!");
		}
	}
}

