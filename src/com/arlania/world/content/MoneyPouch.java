package com.arlania.world.content;

import com.arlania.model.Locations.Location;
import com.arlania.model.container.impl.Bank;
import com.arlania.util.Misc;
import com.arlania.world.entity.impl.player.Player;

import java.text.NumberFormat;

/**
 * Handles the money pouch
 * @author Goml
 * Perfected by Gabbe
 */
public class MoneyPouch {


	public static String formatNumber(long number) {
		return NumberFormat.getInstance().format(number);
	}
	
	public static void refresh(Player player) {
		player.getPacketSender().sendString(8135, ""+player.getMoneyInPouch());
	}
	
	public static void removeTaxbags(Player player, long amount) {
		player.setMoneyInPouch(player.getMoneyInPouch() - amount);
		MoneyPouch.refresh(player);	
	}

	/**
	 * Deposits money into the money pouch
	 * @param amount How many Pwnlite Taxbags to deposit
	 * @return true Returns true if transaction was successful
	 * @return false Returns false if transaction was unsuccessful
	 */
	public static boolean depositMoney(Player plr, int amount, boolean fromTaxBag) {
		if(amount <= 0)
			return false;
		if(plr.getInterfaceId() > 0) {
			plr.getPacketSender().sendMessage("Please close the interface you have open before opening another one.");
			return false;
		}
		if(plr.getConstitution() <= 0) {
			plr.getPacketSender().sendMessage("You cannot do this while dying.");
			return false;
		}
		if(plr.getLocation() == Location.WILDERNESS) {
			plr.getPacketSender().sendMessage("You cannot do this here.");
			return false;
		}
		if (validateAmount(plr, amount, fromTaxBag)) {
			long addedMoney = (long)plr.getMoneyInPouch() + (long)amount;
			if (addedMoney > Long.MAX_VALUE) {
				long canStore = Long.MAX_VALUE - plr.getMoneyInPouch();
				if (!fromTaxBag)
				     plr.getInventory().delete(10835, (int)canStore);
				plr.setMoneyInPouch(plr.getMoneyInPouch() + canStore);
				plr.getPacketSender().sendString(8135, ""+plr.getMoneyInPouch());
				plr.getPacketSender().sendMessage("You've added "+canStore + " Pwnlite Taxbags to your money pouch.");
				return true;
			} else {
				if (!fromTaxBag)
				    plr.getInventory().delete(10835, amount);
				plr.setMoneyInPouch(plr.getMoneyInPouch() + amount);
				plr.getPacketSender().sendString(8135, ""+plr.getMoneyInPouch());
				plr.getPacketSender().sendMessage("You've added "+formatNumber(amount)+" Pwnlite Taxbags to your money pouch.");
				return true;
			}
		} else {
			plr.getPacketSender().sendMessage("You do not seem to have "+formatNumber(amount)+" Pwnlite Taxbags in your inventory.");
			return false;
		}
	}

	/**
	 * @param amount How many Pwnlite Taxbags to withdraw
	 * @return true Returns true if transaction was successful
	 * @return false Returns false if the transaction was unsuccessful
	 */
	/*public static boolean withdrawMoney(Player plr, long amount) {
		if(amount <= 0)
			return false;
		if(plr.getMoneyInPouch() <= 0) {
			plr.getPacketSender().sendMessage("Your money pouch is empty.");
			return false;
		}
		boolean allowWithdraw = plr.getTrading().inTrade() || plr.getDueling().inDuelScreen;
		if(!allowWithdraw) {
			if(plr.getInterfaceId() > 0) {
				plr.getPacketSender().sendMessage("Please close the interface you have open before opening another one.");
				return false;
			}
			plr.getPacketSender().sendInterfaceRemoval();
		}
		if(amount > plr.getMoneyInPouch())
			amount = plr.getMoneyInPouch();
		if ((plr.getInventory().getAmount(10835) + amount) < Integer.MAX_VALUE) {
			plr.setMoneyInPouch(plr.getMoneyInPouch() - amount);
			plr.getInventory().add(10835, (int) amount);
			plr.getPacketSender().sendString(8135, ""+plr.getMoneyInPouch());
			plr.getPacketSender().sendMessage("You withdraw "+formatNumber(amount)+" Pwnlite Taxbags from your pouch.");
			if(allowWithdraw)
				plr.getPacketSender().sendItemContainer(plr.getInventory(), 3322);
			return true;
		} else if((plr.getInventory().getAmount(10835) + amount) > Integer.MAX_VALUE) {
			int canWithdraw = (Integer.MAX_VALUE - plr.getInventory().getAmount(10835));
			if(canWithdraw == 0) {
				plr.getPacketSender().sendMessage("You cannot withdraw more money into your inventory.");
				return false;
			}
			plr.setMoneyInPouch(plr.getMoneyInPouch() - canWithdraw);
			plr.getInventory().add(10835, canWithdraw);
			plr.getPacketSender().sendString(8135, ""+plr.getMoneyInPouch());
			plr.getPacketSender().sendMessage("You could only withdraw "+canWithdraw+" Pwnlite Taxbags.");
			if(allowWithdraw)
				plr.getPacketSender().sendItemContainer(plr.getInventory(), 3322);
			return true;
		}
		return false; 
	}*/
	
	
	public static void withdrawMoney(Player player, long amount) {
		System.out.println("amount was(pouch): " + amount);
		if(amount <= 0)
			return;
		if(player.getMoneyInPouch() <= 0) {
			player.getPacketSender().sendMessage("Your money pouch is empty.");
			return;
		}
		
		if(amount > player.getMoneyInPouch())
			amount = player.getMoneyInPouch();
		
		if(amount < 1 && player.getInventory().getAmount(10835) < Integer.MAX_VALUE - amount) {
			if(player.getMoneyInPouch() >= amount) {
				player.setMoneyInPouch(player.getMoneyInPouch() - amount);
				player.getInventory().add(10835, (int)amount);
				player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
				player.sendMessage("You withdraw " + formatNumber(amount) + " Pwnlite Taxbags from your pouch.");
				return;
			}
		}
		
		if(amount >= 1 && player.getMoneyInPouch() >= amount) {
			
			long remainder = amount % 1;
			
			long totalInv = (remainder + player.getInventory().getAmount(10835));
			
			if(totalInv > Integer.MAX_VALUE) {
				player.sendMessage("@red@You cannot hold that much cash in ur inventory - add some of the cash to bank or pouch");
				return;
			}
			
			player.setMoneyInPouch(player.getMoneyInPouch() - amount);
			long convertedAmount = (amount - remainder) / 1;
			player.getInventory().add(10835, (int)convertedAmount);
			
			player.getInventory().add(10835, (int)remainder);
			
			player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
			
			player.sendMessage("You withdraw " + Misc.formatNumber(amount) + " From your pouch");
			
		}
		
	}


	public static void toBank(Player player) {
		if(!player.isBanking() || player.getInterfaceId() != 5292)
			return;
		
		if(player.getMoneyInPouch() == 0) {
			player.getPacketSender().sendMessage("You money pouch is empty.");
			return;
		}
		
		int amount = player.getMoneyInPouchAsInt();
		int bankAmt = player.getBank(Bank.getTabForItem(player, 10835)).getAmount(10835);
		int totalAmount = bankAmt+amount;
		player.setCurrentBankTab(Bank.getTabForItem(player, 10835));
		if(player.getBank(player.getCurrentBankTab()).getFreeSlots() <= 0 && !player.getBank(player.getCurrentBankTab()).contains(10835)) {
			player.getPacketSender().sendMessage("Your bank is currently full.");
			return;
		}
		if(totalAmount > Integer.MAX_VALUE || totalAmount < 0) {
			int canWithdraw = (Integer.MAX_VALUE - player.getBank(Bank.getTabForItem(player, 10835)).getAmount(10835));
			if(canWithdraw <= 0) {
				player.getPacketSender().sendMessage("You cannot withdraw more money into your bank.");
				return;
			}
			player.setMoneyInPouch(player.getMoneyInPouch() - canWithdraw);
			player.getBank(Bank.getTabForItem(player, 10835)).add(10835, canWithdraw);
			player.getPacketSender().sendString(8135, ""+player.getMoneyInPouch());
			player.getPacketSender().sendMessage("You could only withdraw "+canWithdraw+" Pwnlite Taxbags.");
		} else {
			player.getBank(player.getCurrentBankTab()).add(10835, amount);
			player.setMoneyInPouch(player.getMoneyInPouch() - amount);
			player.getPacketSender().sendString(8135, ""+player.getMoneyInPouch());
		}
	}

	/**
	 * Validates that the player has the Pwnlite Taxbags in their inventory
	 * @param amount The amount the player wishes to insert
	 * @return true Returns true if the player has the Pwnlite Taxbags in their inventory
	 * @return false Returns false if the player does not have the Pwnlite Taxbags in their inventory
	 */
	private static boolean validateAmount(Player plr, int amount, boolean fromPouch) {
		return plr.getInventory().getAmount(10835) >= amount || fromPouch;
	}

}