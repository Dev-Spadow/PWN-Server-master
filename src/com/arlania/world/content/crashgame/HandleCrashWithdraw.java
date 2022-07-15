package com.arlania.world.content.crashgame;
import com.arlania.model.input.Input;
import com.arlania.world.content.MoneyPouch;
import com.arlania.world.entity.impl.player.Player;

public class HandleCrashWithdraw extends Input{
	
	@Override
	public void handleSyntax(Player player, String amnt) {
		long amount = Long.parseLong(amnt);
		long crashBalance = player.getCrashGameBalance();
		
		if(amount <= crashBalance)
		{
			player.setMoneyInPouch(player.getMoneyInPouch() + amount);
			MoneyPouch.refresh(player);
			player.sendMessage("You withdrew " + amount + " coins from Crash.");
			player.removeFromCrashBalance(amount);
			player.getPacketSender().sendString(62013, String.format("%.0fk", Math.floor(player.getCrashGameBalance()/1000)));
		}
		else
		{
			player.sendMessage("You can't withdraw more money than you have in Crash!");
		}
	}

}
