package com.arlania.world.content.well_of_goodwill;

import com.arlania.model.input.impl.DonateWellOfGoodwill;
import com.arlania.world.entity.impl.player.Player;

public class WellOfGoodwillButtons {

	public static boolean selectButton(Player player, int buttonId) {
		switch (buttonId) {
		
		case -14784: // Close button
			player.getPacketSender().removeInterface();
			break;
			
		case -14778: // First Option
			player.getWellOfGoodwillHandler().setWellOfGoodwillType(WellOfGoodwillType.DOUBLE_XP);
			player.setInputHandling(new DonateWellOfGoodwill());
			player.getPacketSender().sendEnterAmountPrompt("How much would you like to donate?");
			break;
			
		case -14775: // Second Option
			player.getWellOfGoodwillHandler().setWellOfGoodwillType(WellOfGoodwillType.DOUBLE_DROP_RATE);
			player.setInputHandling(new DonateWellOfGoodwill());
			player.getPacketSender().sendEnterAmountPrompt("How much would you like to donate?");
			break;
			
		case -14772: // Third Option
			player.getWellOfGoodwillHandler().setWellOfGoodwillType(WellOfGoodwillType.DOUBLE_PEST_CONTROL);
			player.setInputHandling(new DonateWellOfGoodwill());
			player.getPacketSender().sendEnterAmountPrompt("How much would you like to donate?");
			break;
		}
		return false;
	}
}
