package com.arlania.world.content;

import java.time.LocalTime;
import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.world.entity.impl.player.Player;

/**
 * @author Emerald
 * sorta Bad code, but easy to use.
 */

public class DailyRewards {
	
	private Player player;
	
	public DailyRewards(Player player) {
		this.player = player;
	}
	
	final int[] day1 = new int[] {989, 10835};
	final int[] day2 = new int[] {1044, 10835};
	final int[] day3 = new int[] {14484, 10835};
	final int[] day4 = new int[] {1040, 1048};
	final int[] day5 = new int[] {4565, 1053};
	final int[] day6 = new int[] {989, 10835};
	final int[] day7 = new int[] {14484, 11694, 1050, 1042, 1044};
	
	
	public void resetData() {
		
		player.day2Claimed = false;
		player.day3Claimed = false;
		player.day4Claimed = false;
		player.day5Claimed = false;
		player.day6Claimed = false;
		player.day7Claimed = false;
		
	}
	
	public void handleVote() {
		
		if(System.currentTimeMillis() + 86400000 > player.lastVoteTime && !player.day1Claimed) {
			player.getPacketSender().sendConfig(1811, 1);
			player.hasVotedToday = true;
			boolean isGreater = System.currentTimeMillis() + 86400000 > player.lastVoteTime;
			//system.out.println("Called this: data - " + player.day1Claimed + " Boolean: " + isGreater);
		} else if(System.currentTimeMillis() + 86400000 > player.lastVoteTime && !player.day2Claimed) {
			player.getPacketSender().sendConfig(1813, 1);
			player.hasVotedToday = true;
		} else if(System.currentTimeMillis() + 86400000 > player.lastVoteTime && !player.day3Claimed) {
			player.getPacketSender().sendConfig(1815, 1);
			player.hasVotedToday = true;
		} else if(System.currentTimeMillis() + 86400000 > player.lastVoteTime && !player.day4Claimed) {
			player.getPacketSender().sendConfig(1817, 1);
			player.hasVotedToday = true;
		} else if(System.currentTimeMillis() + 86400000 > player.lastVoteTime && !player.day5Claimed) {
			player.getPacketSender().sendConfig(1819, 1);
			player.hasVotedToday = true;
		} else if(System.currentTimeMillis() + 86400000 > player.lastVoteTime && !player.day6Claimed) {
			player.getPacketSender().sendConfig(1821, 1);
			player.hasVotedToday = true;
		}
	}
	
	public void handleDailyLogin() {
		if(System.currentTimeMillis() + 86400000 > player.lastLogin) {
			if(!player.day1Claimed) {
				player.getPacketSender().sendConfig(1810, 1);
			} else if(!player.day2Claimed) {
				player.getPacketSender().sendConfig(1812, 1);
			} else if(!player.day3Claimed) {
				player.getPacketSender().sendConfig(1814, 1);
			} else if(!player.day4Claimed) {
				player.getPacketSender().sendConfig(1816, 1);
			} else if(!player.day5Claimed) {
				player.getPacketSender().sendConfig(1818, 1);
			} else if(!player.day6Claimed) {
				player.getPacketSender().sendConfig(1820, 1);
			}
		}
	}
	
	public void claimDay7() {
		
		if(player.day1Claimed && player.day2Claimed && player.day3Claimed && player.day4Claimed && player.day5Claimed && player.day6Claimed
				&& player.day7Claimed) {
			player.getInventory().add(10835, 1000);
			resetData();
		} else {
			player.sendMessage("@red@Claim day 1-6 first before u can claim day 7");
			return;
		}
	}
	
	public void setDataOnLogin() {
		if(player.day1Claimed) {
			player.getPacketSender().sendConfig(1810, 1);
			player.getPacketSender().sendConfig(1811, 1);
		}
		if(player.day2Claimed) {
			player.getPacketSender().sendConfig(1812, 1);
			player.getPacketSender().sendConfig(1813, 1);
		}
		if(player.day3Claimed) {
			player.getPacketSender().sendConfig(1814, 1);
			player.getPacketSender().sendConfig(1815, 1);
		}
		if(player.day4Claimed) {
			player.getPacketSender().sendConfig(1816, 1);
			player.getPacketSender().sendConfig(1817, 1);
		}
		if(player.day5Claimed) {
			player.getPacketSender().sendConfig(1818, 1);
			player.getPacketSender().sendConfig(1819, 1);
		}	
		if(player.day6Claimed) {
			player.getPacketSender().sendConfig(1820, 1);
			player.getPacketSender().sendConfig(1821, 1);
		}
		
	}
	
	
	public static boolean handleRewards(Player player, int buttonId) {
		
		long time =  player.lastDailyClaim - System.currentTimeMillis();
		
		//system.out.println("Handled.");
		
		switch(buttonId) {
		case -8312:
			// also votes -> true
			if(time < 1 && !player.day1Claimed) { // true
				player.day1Claimed = true;
				player.lastDailyClaim = System.currentTimeMillis() + 43200000;
				player.getInventory().add(989, 5).add(10835, 100);
				player.hasVotedToday = false;
			}
			
			break;
			
		case -8311:
			if(player.day1Claimed && !player.day2Claimed) { // can claim if day 1 true, day 2 not
				if(time < 1) {
					player.day2Claimed = true;
					player.lastDailyClaim = System.currentTimeMillis() + 43200000;
					player.getInventory().add(1044, 1).add(10835, 100);
					player.hasVotedToday = false;
				return true;
			} else {
				//system.out.println("Not yet");
				return false;
			}
			}
			break;
			
		case -8310:
			if (player.day1Claimed && player.day2Claimed && !player.day3Claimed) {
				if(time < 1) {
					player.day3Claimed = true;
					player.lastDailyClaim = System.currentTimeMillis() + 43200000;
					player.getInventory().add(14484, 1).add(10835, 200);
					player.hasVotedToday = true;
				return true;
			} else {
				//system.out.println("Not yet");
				return false;
			}
			
			}
			break;
			
		case -8309:
			if (player.day1Claimed && player.day2Claimed && player.day3Claimed && !player.day4Claimed) {
				if(time < 1) {
					player.day4Claimed = true;
					player.lastDailyClaim = System.currentTimeMillis() + 43200000;
					player.getInventory().add(1040, 1).add(1048, 5);
					player.hasVotedToday = true;
				return true;
			} else {
				//system.out.println("Not yet");
				return false;
			}
			}
			break;
			
		case -8308:
			if (player.day1Claimed && player.day2Claimed && player.day3Claimed && player.day4Claimed && !player.day5Claimed) {
				if(time < 1) {
					player.day5Claimed = true;
					player.lastDailyClaim = System.currentTimeMillis() + 43200000;
					player.getInventory().add(4565, 1).add(1053, 3);
					player.hasVotedToday = true;
				return true;
			} else {
				System.out.println("Not yet");
				return false;
			}
			}
			break;
			
		case -8307:
			if (player.day1Claimed && player.day2Claimed && player.day3Claimed && player.day4Claimed && player.day5Claimed && !player.day6Claimed) {
				if(time < 1) {
					player.day6Claimed = true;
					player.lastDailyClaim = System.currentTimeMillis() + 43200000;
					player.getInventory().add(989, 25).add(10835, 50);
					player.hasVotedToday = true;
				return true;
			} else {
				System.out.println("Not yet");
				return false;
			}
			}
			break;
		}
		return true;
	}
	

	
	
	public long displayTimeLeft(long timeInMs) {
		
		timeInMs = timeInMs - System.currentTimeMillis();
		
		long seconds = (int) Math.ceil((timeInMs / 1000));
		
		if(seconds < 0) {
			System.out.println("Stopping as its less than 0");
			return 0;
		}
		
		LocalTime timeOfDay = LocalTime.ofSecondOfDay(seconds);
		String time = timeOfDay.toString();
		
	    player.getPacketSender().sendString(57222, time);
	    //System.out.println(time);
	    
	    return seconds;
		
	}
	
	public void processTime() {
		
		TaskManager.submit(new Task(1, player, false) {
			@Override
			protected void execute() {
				//System.out.println("Called this method");
		    	displayTimeLeft(player.lastDailyClaim);
		    	//handleRewards();
			}
		});
	}
	

	
	
	
	
	
	
	public void displayRewards() {
		player.getPacketSender().sendInterface(57210);
		for(int i = 0; i < day7.length; i++) {
			player.getPacketSender().sendItemOnInterface(57223, day7[i], i, 1);
		}
		
		for(int t = 0; t < day1.length; t++) {
			player.getPacketSender().sendItemOnInterface(57232, day1[t], t, t == 0 ? 5 : 100);
		}
		
		for(int y = 0; y < day2.length; y++) {
			player.getPacketSender().sendItemOnInterface(57233, day2[y], y, y == 0 ? 1 : 100);
		}
		
		for(int u = 0; u < day3.length; u++) {
			player.getPacketSender().sendItemOnInterface(57234, day3[u], u, u == 0 ? 1 : 200);
		}
		
		for(int o = 0; o < day4.length; o++) {
			player.getPacketSender().sendItemOnInterface(57235, day4[o], o, o == 0 ? 1 : 5);
		}
		
		for(int p = 0; p < day5.length; p++) {
			player.getPacketSender().sendItemOnInterface(57236, day5[p], p, p == 0 ? 1 : 3);
		}
		
		for(int a = 0; a < day6.length; a++) {
			player.getPacketSender().sendItemOnInterface(57237, day6[a], a, a == 0 ? 25 : 50);
		}
	}
}
