package com.arlania.world.content.well_of_goodwill;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class WellOfGoodwillHandler {

	public List<String> donators = new ArrayList<>();

	int currency = 10835;

	int requiredDoubleXP = 500_000;
	int requiredDoubleDropRate = 1_000_000;
	int requiredDoublePestControlPts = 1_000_000;

	int donatedAmountDoubleXp = 0;
	int donatedAmountDoubleDropRate = 0;
	int donatedAmountDoublePestControlPts = 0;

	static LocalDateTime doubleXpBoost = LocalDateTime.MIN;
	static LocalDateTime doubleDropRate = LocalDateTime.MIN;
	static LocalDateTime doublePcPoints = LocalDateTime.MIN;

	public void openWellOfGoodwill(Player player) {
		sendRewards(player);
		clearDonations(player);
		sendAmountDonated(player);
		sendDonators(player);
		player.getWellOfGoodwillHandler().setWellOfGoodwillType(null);
		player.getPacketSender().sendInterface(50750);
	}

	public void sendRewards(Player player) {
		player.getPA().sendItemOnInterface(50769, 10835, 500000);
		player.getPA().sendItemOnInterface(50778, 10835, 1000000);
		player.getPA().sendItemOnInterface(50784, 10835, 1000000);
	}

	public void sendAmountDonated(Player player) {
		player.getPacketSender().sendString(50772, isDoubleXpActive() ? "@whi@Boost active"
				: Misc.getTotalAmount1(donatedAmountDoubleXp) + "/" + Misc.getTotalAmount1(requiredDoubleXP));
		player.getPacketSender().sendString(50777,
				isDoubleDropRateActive() ? "@whi@Boost active"
						: Misc.getTotalAmount1(donatedAmountDoubleDropRate) + "/"
								+ Misc.getTotalAmount1(requiredDoubleDropRate));
		player.getPacketSender().sendString(50783,
				isDoublePestControlPtsActive() ? "@whi@Boost active"
						: Misc.getTotalAmount1(donatedAmountDoublePestControlPts) + "/"
								+ Misc.getTotalAmount1(requiredDoublePestControlPts));

		player.getPA().setSpriteLoadingPercentage(50771, isDoubleXpActive() ? 100 : calculatePercentage(donatedAmountDoubleXp, requiredDoubleXP));
		player.getPA().setSpriteLoadingPercentage(50776, isDoubleDropRateActive() ? 100 :
				calculatePercentage(donatedAmountDoubleDropRate, requiredDoubleDropRate));
		player.getPA().setSpriteLoadingPercentage(50782, isDoublePestControlPtsActive() ? 100 :
				calculatePercentage(donatedAmountDoublePestControlPts, requiredDoublePestControlPts));
	}

	public void sendDonators(Player player) {
		int start = 50788;
		if(donators.size() >= 22) {
			donators.remove(0);
		}
		for (String don : donators) {
			player.getPacketSender().sendString(start, "@gre@" + don);
			start++;
		}
	}

	public void clearDonations(Player player) {
		for (int i = 0; i < 22; i++) {
			player.getPacketSender().sendString(50788 + i, "");
		}
	}

	public WellOfGoodwillType wogType;

	public WellOfGoodwillType getWellOfGoodwillType() {
		return wogType;
	}

	public void setWellOfGoodwillType(WellOfGoodwillType wogType) {
		this.wogType = wogType;
	}

	public void donate(Player player, int amount) {
		if (player.getWellOfGoodwillHandler().getWellOfGoodwillType() == WellOfGoodwillType.DOUBLE_XP) {

			if (player.getInventory().contains(currency, amount)) {

				if (requiredDoubleXP <= donatedAmountDoubleXp) {
					player.sendMessage("This boost is already active..");
					return;
				}
				
				player.getInventory().delete(currency, amount);
				
				World.sendMessage(player.getUsername() + " has donated " + Misc.getTotalAmount1(amount) + " towards the well of goodwill!");
				
				donatedAmountDoubleXp += amount;
				
				if(donatedAmountDoubleXp > requiredDoubleXP)
					donatedAmountDoubleXp = requiredDoubleXP;
				
				donators.add(player.getUsername() + " " + Misc.getTotalAmount1(amount) + " Taxbag");
				sendDonators(player);
				if (requiredDoubleXP <= donatedAmountDoubleXp) {
					startEvent(WellOfGoodwillType.DOUBLE_XP);
				}
				sendAmountDonated(player);
			} else {
				player.sendMessage("You dont have the amount you want to donate..");
			}

		} else if (player.getWellOfGoodwillHandler().getWellOfGoodwillType() == WellOfGoodwillType.DOUBLE_DROP_RATE) {
			if (player.getInventory().contains(currency, amount)) {

				if (requiredDoubleDropRate <= donatedAmountDoubleDropRate) {
					player.sendMessage("This boost is already active..");
					return;
				}

				player.getInventory().delete(currency, amount);
				donatedAmountDoubleDropRate += amount;
				
				if(donatedAmountDoubleDropRate > requiredDoubleDropRate)
					donatedAmountDoubleDropRate = requiredDoubleDropRate;
				
				donators.add(player.getUsername() + " " + Misc.getTotalAmount1(amount) + " Taxbag");
				sendDonators(player);
				if (requiredDoubleDropRate <= donatedAmountDoubleDropRate) {
					startEvent(WellOfGoodwillType.DOUBLE_DROP_RATE);
				}
				sendAmountDonated(player);
			} else {
				player.sendMessage("You dont have the amount you want to donate..");
			}
		} else if (player.getWellOfGoodwillHandler()
				.getWellOfGoodwillType() == WellOfGoodwillType.DOUBLE_PEST_CONTROL) {

			if (player.getInventory().contains(currency, amount)) {

				if (requiredDoublePestControlPts <= donatedAmountDoublePestControlPts) {
					player.sendMessage("This boost is already active..");
					return;
				}

				player.getInventory().delete(currency, amount);
				donatedAmountDoublePestControlPts += amount;
				
				if(donatedAmountDoublePestControlPts > requiredDoublePestControlPts)
					donatedAmountDoublePestControlPts = requiredDoublePestControlPts;
				
				donators.add(player.getUsername() + " " + Misc.getTotalAmount1(amount) + " Taxbag");
				sendDonators(player);
				if (requiredDoublePestControlPts <= donatedAmountDoublePestControlPts) {
					startEvent(WellOfGoodwillType.DOUBLE_PEST_CONTROL);
				}
				sendAmountDonated(player);
			} else {
				player.sendMessage("You dont have the amount you want to donate..");
			}
		}
	}

	public static boolean isDoubleXpActive() {
		return doubleXpBoost != LocalDateTime.MIN;
	}

	public static boolean isDoubleDropRateActive() {
		return doubleDropRate != LocalDateTime.MIN;
	}

	public static boolean isDoublePestControlPtsActive() {
		return doublePcPoints != LocalDateTime.MIN;
	}

	public int calculatePercentage(int obtained, int total) {
		return obtained * 100 / total;
	}

	private void startEvent(WellOfGoodwillType type) {
		if (type == WellOfGoodwillType.DOUBLE_XP) {
			for(Player player : World.getPlayers()) {
				if(player == null)
					continue;
				player.sendMessage("@whi@[Well of Goodwill]@bla@ Double xp boost for 1 hour is now active!!");
			}
			doubleXpBoost = LocalDateTime.now().plusHours(1);
			donatedAmountDoubleXp = 0;
			
		} else if (type == WellOfGoodwillType.DOUBLE_DROP_RATE) {
			for(Player player : World.getPlayers()) {
				if(player == null)
					continue;
				player.sendMessage("@whi@[Well of Goodwill]@bla@ 50% drop rate for 1 hour is now active!!");
			}
			doubleDropRate = LocalDateTime.now().plusHours(1);
			donatedAmountDoubleDropRate = 0;
			
		} else if (type == WellOfGoodwillType.DOUBLE_PEST_CONTROL) {
			for(Player player : World.getPlayers()) {
				if(player == null)
					continue;
				player.sendMessage("@whi@[Well of Goodwill]@bla@ Double pc points for 1 hour is now active!!");
			}
			doublePcPoints = LocalDateTime.now().plusHours(1);
			donatedAmountDoublePestControlPts = 0;
		}
	}

	public static void update() {
		if(LocalDateTime.now().until(doubleXpBoost, ChronoUnit.SECONDS) <= 0) {
			doubleXpBoost = LocalDateTime.MIN;
		}
		if(LocalDateTime.now().until(doubleDropRate, ChronoUnit.SECONDS) <= 0) {
			doubleDropRate = LocalDateTime.MIN;
		}
		if(LocalDateTime.now().until(doublePcPoints, ChronoUnit.SECONDS) <= 0) {
			doublePcPoints = LocalDateTime.MIN;
		}
	}
}
