package com.arlania.util;

import com.arlania.world.entity.impl.player.Player;

public class QuickUtils {

	public static float tickToSec(int tick) {
		int ticksInMillis = tick * 600;
		float tickToSecs = ticksInMillis / 1000f;
		return tickToSecs;
	}
	
	public static float tickToMin(int tick) {
		int ticksInMillis = tick * 600;
		float tickToMin = ticksInMillis / 60000f;
		return tickToMin;
	}
	
	public static float tickToHour(int tick) {
		int ticksInMillis = tick * 600;
		float tickToHour = ticksInMillis / 3_600_000f;
		return tickToHour;
	}
	
	
	public static long msToSec(long ms) {
		
		return ms * 1000;
	}
	
	public static long msToHour(long ms) {
		
		return ms * 3_600_000;
	}
	
	public static long secToHour(long secs) {
		
		return secs * 3600;
	}
	
	public static long minutesToHour(long minutes) {
		
		return minutes * 60;
	}
	
	public static int hoursToDay(int hours) {
		return hours * 24;
	}
	
	public static int daysToWeek(int days) {
		if(days >= 7) {
			return 0;
		}
		return days * 7;
	}
	
	public static String getCleansingPrefix(Player player) {
		String prefix = player.getCleansingTime() > 100 ? "minutes" : "minute";
		
		return prefix;
	}
	
	public static String getPraisePrefix(Player player) {
		String prefix = player.getPraiseTime() > 100 ? "minutes" : "minute";
		
		return prefix;
	}
	
	
	public static String getIceCreamPrefix(Player player) {
		String prefix = player.getIceCreamTime() > 20 ? "minutes" : "minute";
		
		return prefix;
	}
	
	
	public static String getPumpkinPrefix(Player player) {
		String prefix = player.getEatPumpkinTime() > 20 ? "minutes" : "minute";
		
		return prefix;
	}
	
	public static String getCandyPrefix(Player player) {
		String prefix = player.getCandyTime() > 20 ? "minutes" : "minute";
		return prefix;
	}
	public static String getChocCreamPrefix(Player player) {
		String prefix = player.getIceCreamTime() > 30 ? "minutes" : "minute";
		
		return prefix;
	}
	
	public static String getSmokeTheBongPrefix(Player player) {
		String prefix = player.getSmokeTheBongTime() > 30 ? "minutes" : "minute";
		
		return prefix;
	}
	
}
