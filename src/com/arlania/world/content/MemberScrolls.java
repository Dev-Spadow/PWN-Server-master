package com.arlania.world.content;

import com.arlania.model.PlayerRights;
import com.arlania.util.Misc;
import com.arlania.world.content.Achievements.AchievementData;
import com.arlania.world.content.dialogue.Dialogue;
import com.arlania.world.content.dialogue.DialogueExpression;
import com.arlania.world.content.dialogue.DialogueManager;
import com.arlania.world.content.dialogue.DialogueType;
import com.arlania.world.entity.impl.player.Player;

public class MemberScrolls {
	
	public static void checkForRankUpdate(Player player) {
		if(player.getRights().isStaff()) {
			return;
		}
		PlayerRights rights = null;
		if(player.getAmountDonated() >= 10)
			rights = PlayerRights.DONATOR;
		if(player.getAmountDonated() >= 25)
			rights = PlayerRights.SUPER_DONATOR;
		if(player.getAmountDonated() >= 50)
			rights = PlayerRights.ULTRA_DONATOR;
		if(player.getAmountDonated() >= 125)
			rights = PlayerRights.MYSTIC_DONATOR;
		if(player.getAmountDonated() >= 200)
			rights = PlayerRights.OBSIDIAN_DONATOR;
		if(player.getAmountDonated() >= 500)
			rights = PlayerRights.LEGENDARY_DONATOR;
		
		if(player.getAmountDonated() >= 1000)
			rights = PlayerRights.CELESTIAL_DONATOR;
		if(player.getAmountDonated() >= 2500)
			rights = PlayerRights.EXECUTIVE_DONATOR;
		
		if(player.getAmountDonated() >= 5000)
			rights = PlayerRights.SUPREME_DONATOR;
		
		if(player.getAmountDonated() >= 10000)
			rights = PlayerRights.DIVINE_DONATOR;
		
		if(rights != null && rights != player.getRights()) {
			player.getPacketSender().sendMessage("You've become a "+Misc.formatText(rights.toString().toLowerCase())+"! Congratulations!");
			player.setRights(rights);
			player.getPacketSender().sendRights();
		}
	}

	public static boolean handleScroll(Player player, int item) {
		switch(item) {
		case 1464:
		case 5020:
		case 19935:
		case 16453:
		case 16454:
		case 8660:
		case 8661:
		case 17743:
		case 17840:
		case 8662:
		case 8663:
		case 8671:
		case 8672:
		case 8659:
		case 19936:
		case 16455:
		case 19938:
			int funds = item == 19935 ? 5 : item == 19936 ? 10 : item == 17743 ? 5 : item == 17840 ? 10 : item == 8660 ? 1000 : item == 16454 ? 10 : item == 16455 ? 25 :  item == 16453 ? 5 :item == 8672 ? 25:  item == 8671 ? 50 :item == 8662 ? 200 : item == 8663 ? 125: item == 8661 ? 500 : item == 8659 ? 2500 : item == 1464 ? 1 : item == 5020 ? 100 :  item == 19938 ? 50 : -1;
			player.getInventory().delete(item, 1);
			player.getAchievementTracker().progress(com.arlania.world.content.achievements.AchievementData.CLAIM_A_BOND, 1);
			player.getAchievementTracker().progress(com.arlania.world.content.achievements.AchievementData.CLAIM_25_BONDS, 1);
			player.getAchievementTracker().progress(com.arlania.world.content.achievements.AchievementData.CLAIM_100_BONDS, 1);
			player.incrementAmountDonated(funds);
			player.getPointsHandler().incrementDonationPoints(funds);
			player.getPacketSender().sendMessage("Your account has gained funds worth $"+funds+". Your total is now at $"+player.getAmountDonated()+".");
			checkForRankUpdate(player);
			PlayerPanel.refreshPanel(player);
			break;
		}
		return false;
	}
	
	public static Dialogue getTotalFunds(final Player player) {
		return new Dialogue() {

			@Override
			public DialogueType type() {
				return DialogueType.NPC_STATEMENT;
			}

			@Override
			public DialogueExpression animation() {
				return DialogueExpression.NORMAL;
			}
			
			@Override
			public int npcId() {
				return 4657;
			}

			@Override
			public String[] dialogue() {
				return player.getAmountDonated() > 0 ? new String[]{"Your account has claimed scrolls worth $"+player.getAmountDonated()+" in total.", "Thank you for supporting us!"} : new String[]{"Your account has claimed scrolls worth $"+player.getAmountDonated()+" in total."};
			}
			
			@Override
			public Dialogue nextDialogue() {
				return DialogueManager.getDialogues().get(5);
			}
		};
	}
}
