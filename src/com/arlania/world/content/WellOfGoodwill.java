package com.arlania.world.content;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.content.Achievements.AchievementData;
import com.arlania.world.content.dialogue.Dialogue;
import com.arlania.world.content.dialogue.DialogueExpression;
import com.arlania.world.content.dialogue.DialogueManager;
import com.arlania.world.content.dialogue.DialogueType;
import com.arlania.world.entity.impl.player.Player;

public class WellOfGoodwill {

	private static long AMOUNT_NEEDED = 20; //100m
	private static final long LEAST_DONATE_AMOUNT_ACCEPTED = 1; //1m 
	private static final long BONUSES_DURATION = 120; //2 hours in minutes

	private static CopyOnWriteArrayList<Player> DONATORS = new CopyOnWriteArrayList<Player>();
	private static WellState STATE = WellState.EMPTY;
	private static long START_TIMER = 0;
	private static long MONEY_IN_WELL = 0;

	public static void init() {
		try {
			BufferedReader in = new BufferedReader(new FileReader("./data/saves/edgeville-well.txt"));
			if(in != null) {
				String line = in.readLine();
				if(line != null) {
					long startTimer = Long.parseLong(line);
					if(startTimer > 0) {
						STATE = WellState.FULL;
						START_TIMER = startTimer;
						MONEY_IN_WELL = AMOUNT_NEEDED;
					}
				}
			}
			in.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void save() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("./data/saves/edgeville-well.txt"));
			out.write(""+START_TIMER);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void lookDownWell(Player player) {
		if (checkFull(player)) {
			return;
		}
		DialogueManager.start(player, new Dialogue() {

			@Override
			public DialogueType type() {
				return DialogueType.NPC_STATEMENT;
			}

			@Override
			public DialogueExpression animation() {
				return DialogueExpression.NORMAL;
			}

			@Override
			public String[] dialogue() {
				return new String[]{ "It looks like the well could hold another "+Misc.insertCommasToNumber(""+getMissingAmount()+"")+" Pwnlite Taxbags."};
			}

			@Override
			public int npcId() {
				return 802;
			}

			@Override
			public Dialogue nextDialogue() {
				return DialogueManager.getDialogues().get(75);
			}

		});
	}

	public static boolean checkFull(Player player) {
		if (STATE == WellState.FULL) {
			DialogueManager.start(player, new Dialogue() {

				@Override
				public DialogueType type() {
					return DialogueType.NPC_STATEMENT;
				}

				@Override
				public DialogueExpression animation() {
					return DialogueExpression.NORMAL;
				}

				@Override
				public String[] dialogue() {
					return new String[]{"The well is already full of coins and Saradomin", "has granted players with bonus experience for their", "generosity! There are currently "+getMinutesRemaining()+" minutes", "of bonus experience left."};
				}

				@Override
				public int npcId() {
					return 802;
				}

				@Override
				public Dialogue nextDialogue() {
					return DialogueManager.getDialogues().get(75);
				}

			});
			return true;
		}
		return false;
	}

	public static void donate(Player player, int amount) {
		if (checkFull(player)) 
			return;
		
		if (amount < LEAST_DONATE_AMOUNT_ACCEPTED) {
			DialogueManager.sendStatement(player, "You must donate at least 1 1m ticket.");
			return;
		}
		if (amount > getMissingAmount()) {
			amount = (int) getMissingAmount();
		}
		
		if (player.getInventory().getAmount(10835) < amount) {
			DialogueManager.sendStatement(player, "You do not have that many Pwnlite Taxbags");
			return;
		}
		
		player.getInventory().delete(10835, amount);
		
		if (!DONATORS.contains(player)) {
			DONATORS.add(player);
		}
		
		MONEY_IN_WELL += amount;
		
		if(amount > 3) {
			World.sendMessage("@red@[Well of Goodwill]@bla@"+player.getUsername()+" has donated "+Misc.insertCommasToNumber(""+amount+"")+" Pwnlite Taxbags to the Well of Goodwill!");
		}
		
		DialogueManager.sendStatement(player, "Thank you for your donation.");
	
		
		if (getMissingAmount() <= 0) {
			STATE = WellState.FULL;
			START_TIMER = System.currentTimeMillis();
			World.sendMessage("@red@Well of Goodwill @bla@ is now granting everyone 2 hours of 30% bonus droprate.");
			World.getPlayers().forEach(p -> p.getPacketSender().sendString(39167, PlayerPanel.LINE_START + "@or1@Well of Goodwill: @yel@Active"));
		}
	}

	public static void updateState() {
		if (STATE == WellState.FULL) {
			if(getMinutesRemaining() <= 0) {
				World.sendMessage("<img=10> <col=6666FF>The Well of Goodwill is no longer granting bonus experience.");
				World.getPlayers().forEach(p -> p.getPacketSender().sendString(39167, "@or2@Well of Goodwill: @yel@N/A"));
				setDefaults();
			}
		}
	}

	public static void setDefaults() {
		DONATORS.clear();
		STATE = WellState.EMPTY;
		START_TIMER = 0;
		MONEY_IN_WELL = 0;
	}

	public static long getMissingAmount() {
		System.err.println(AMOUNT_NEEDED+" "+MONEY_IN_WELL); 
		return (AMOUNT_NEEDED - MONEY_IN_WELL);
	}

	public static long getMinutesRemaining() {
		return (BONUSES_DURATION - Misc.getMinutesPassed(System.currentTimeMillis() - START_TIMER));
	}

	public static boolean isActive() {
		updateState();
		return STATE == WellState.FULL;
	}

	public static boolean bonusLoyaltyPoints(Player player) {
		updateState();
		return STATE == WellState.FULL && DONATORS.contains(player);
	}

	public enum WellState {
		EMPTY,
		FULL;
	}
}
