package com.arlania.world.content;

import com.arlania.model.GameMode;
import com.arlania.model.Item;
import com.arlania.model.PlayerRights;
import com.arlania.world.World;
import com.arlania.world.content.clan.ClanChatManager;
import com.arlania.world.entity.impl.player.Player;

/**
 * Start screen functionality.
 * 
 * @author Joey wijers
 */
public class StartScreen {
	public enum GameModes {
		NORMAL("Normal", 52761,-12780, 1, 0, new Item[] { new Item(7806, 1), new Item(13867, 1), new Item(896, 1), new Item(15663, 1), new Item(15664, 1), new Item(15665, 1), (new Item (15661, 1)), (new Item (15662, 1)), (new Item (1639, 1)), (new Item (18928, 1)),(new Item (11142, 1)), (new Item (15373, 1)),(new Item (4767, 1)), (new Item (15243, 100)), (new Item (392, 200)),   (new Item (2437, 25)), (new Item (2441, 25)), (new Item (3025, 25)), (new Item (2443, 25))}, "As a normal player you will be able to", "play the game without any restrictions.", "", "", "", "", ""),
		IRONMAN("  Ironman", 52762, -12779, 1, 1, new Item[] { new Item(7806, 1), new Item(13867, 1), new Item(896, 1), new Item(18904, 1),  new Item(15663, 1), new Item(15664, 1), new Item(15665, 1), (new Item (15661, 1)), (new Item (15662, 1)), (new Item (1639, 1)), (new Item (18928, 1)),(new Item (11142, 1)), (new Item (15373, 1)),(new Item (4767, 1)), (new Item (15243, 100)), (new Item (392, 200)),   (new Item (2437, 25)), (new Item (2441, 25)),(new Item (4202, 1)),(new Item (5092, 1)),  (new Item (3025, 25)), (new Item (2443, 25))},"Play Pwnlite as an Iron man.", "You will be restricted from trading, staking and looting items from killed players.", "You will not get a npc drop if another player has done more damage.", "You will have to rely on your starter, skilling, pvming, and shops.", "This game mode is for players that love a challenge.", "", ""),
		ULTIMATE_IRON("  Ultimate Iron", 52763, -12778, 1, 2, new Item[] { new Item(7806, 1), new Item(13867, 1), new Item(896, 1), new Item(18904, 1), new Item(15663, 1), new Item(15664, 1), new Item(15665, 1), (new Item (15661, 1)), (new Item (15662, 1)), (new Item (1639, 1)), (new Item (18928, 1)),(new Item (11142, 1)), (new Item (15373, 1)),(new Item (4767, 1)), (new Item (15243, 100)), (new Item (392, 200)),   (new Item (2437, 25)),(new Item (4202, 1)),(new Item (5092, 1)),  (new Item (2441, 25)), (new Item (3025, 25)), (new Item (2443, 25))}, "Play Pwnlite as a Ultimate Ironman.", "In addiction to the iron man rules you cannot use banks.", "This gamemode is for the players that love the impossible.", "", "", "", "");
		private String name;
		private int stringId;
		private int checkClick;
		private int textClick;
		private int configId;
		private Item[] starterPackItems;
		private String line1;
		private String line2;
		private String line3;
		private String line4;
		private String line5;
		private String line6;
		private String line7;

		private GameModes(String name, int stringId, int checkClick, int textClick, int configId, Item[] starterPackItems, String line1, String line2, String line3, String line4, String line5, String line6, String line7) {
			this.name = name;
			this.stringId = stringId;
			this.checkClick = checkClick;
			this.textClick = textClick;
			this.configId = configId;
			this.starterPackItems = starterPackItems;
			this.line1 = line1;
			this.line2 = line2;
			this.line3 = line3;
			this.line4 = line4;
			this.line5 = line5;
			this.line6 = line6;
			this.line7 = line7;
		}
	}

	public static void open(Player player) {
		sendNames(player);
		ClanChatManager.join(player, "Help");
		player.getPacketSender().sendInterface(52750);
		player.selectedGameMode = GameModes.NORMAL;
		check(player, GameModes.NORMAL);
		sendStartPackItems(player, GameModes.NORMAL);
		sendDescription(player, GameModes.NORMAL);
	}

	public static void sendDescription(Player player, GameModes mode) {
		int s = 52764;
		player.getPacketSender().sendString(s, mode.line1);
		player.getPacketSender().sendString(s+1, mode.line2);
		player.getPacketSender().sendString(s+2, mode.line3);
		player.getPacketSender().sendString(s+3, mode.line4);
		player.getPacketSender().sendString(s+4, mode.line5);
		player.getPacketSender().sendString(s+5, mode.line6);
		player.getPacketSender().sendString(s+6, mode.line7);
	}

	public static void sendStartPackItems(Player player, GameModes mode) {
		final int START_ITEM_INTERFACE = 59025;
		for (int i = 0; i < 28; i++) {
			int id = -1;
			int amount = 0;
			try {
				id = mode.starterPackItems[i].getId();
				amount = mode.starterPackItems[i].getAmount();
			} catch (Exception e) {

			}
			player.getPacketSender().sendItemOnInterface(START_ITEM_INTERFACE + i, id, amount);
		}
	}

	public static boolean handleButton(Player player, int buttonId) {
		final int CONFIRM = -12767;
		if (buttonId == CONFIRM) {
			if (player.didReceiveStarter() == true) {
			
				return true;
			}//ConnectionHandler.getStarters(player.getHostAddress()) <= GameSettings.MAX_STARTERS_PER_IP
			if(!PlayerPunishment.hasRecieved1stStarter(player.getHostAddress())) {
				player.getPacketSender().sendInterfaceRemoval();
				player.setReceivedStarter(true);
				handleConfirm(player);
				addStarterToInv(player);
				ClanChatManager.join(player, "help");
				player.setPlayerLocked(false);
				player.getPacketSender().sendInterface(3559);
				player.getAppearance().setCanChangeAppearance(true);
				player.setNewPlayer(false);
				PlayerPunishment.addIpToStarterList1(player.getHostAddress());
				PlayerPunishment.addIpToStarter1(player.getHostAddress());		
				World.sendMessage("<img=459>@bla@[<shad=df7018>New Player</shad>@bla@]: <shad=df7018>"+player.getUsername()+" </shad>has logged into <shad=df7018>Pwnlite for the first time!");
				World.sendMessage(
						"<img=10> <col=008FB2><shad=200>Please give him/her a super warm welcome <3");
			}
			else if(PlayerPunishment.hasRecieved1stStarter(player.getHostAddress()) && !PlayerPunishment.hasRecieved2ndStarter(player.getHostAddress())) {
				player.getPacketSender().sendInterfaceRemoval();
				player.setReceivedStarter(true);
				handleConfirm(player);
				addStarterToInv(player);
				ClanChatManager.join(player, "help");
				player.setPlayerLocked(false);
				player.getPacketSender().sendInterface(3559);
				player.getAppearance().setCanChangeAppearance(true);
				player.setNewPlayer(false);
				PlayerPunishment.addIpToStarterList2(player.getHostAddress());
				PlayerPunishment.addIpToStarter2(player.getHostAddress());			
				World.sendMessage("<img=459>@bla@[<shad=df7018>New Player</shad>@bla@]: <shad=df7018>"+player.getUsername()+" </shad>has logged into <shad=df7018>Pwnlite for the first time!");
				World.sendMessage(
						"<img=10> <col=008FB2><shad=200>Please give him/her a super warm welcome <3");
			}
			else if(PlayerPunishment.hasRecieved1stStarter(player.getHostAddress()) && PlayerPunishment.hasRecieved2ndStarter(player.getHostAddress())) {
				//player.getPacketSender().sendInterfaceRemoval();
				ClanChatManager.join(player, "help");
				player.setPlayerLocked(false);
				player.getPacketSender().sendInterface(3559);
				player.getAppearance().setCanChangeAppearance(true);
				player.setNewPlayer(false);
				player.getPacketSender().sendMessage("You've recieved to many starters.");
				World.sendMessage("<img=459>@bla@[<shad=df7018>New Player</shad>@bla@]: <shad=df7018>"+player.getUsername()+" </shad>has logged into <shad=df7018>Pwnlite for the first time!");
				World.sendMessage(
						"<img=10> <col=008FB2><shad=200>Please give him/her a super warm welcome <3");
			}
			player.setRights(PlayerRights.PLAYER);
			player.getPacketSender().sendRights();
		}
		System.out.println("ID: " + buttonId);
		
		for (GameModes mode : GameModes.values()) {
			if (mode.checkClick == buttonId || mode.textClick == buttonId) {
				selectMode(player, mode);
				return true;
			}
		}
		return false;
		
	}
	public static void handleConfirm(Player player) {
		
		System.out.println("Game mode: " + player.selectedGameMode);
		
		if(!(player.selectedGameMode == GameModes.NORMAL) && !(player.selectedGameMode == GameModes.IRONMAN) && !(player.selectedGameMode == GameModes.ULTIMATE_IRON)) {
			
			player.sendMessage("@red@This mode is not available at the moment, choose another.");
		}
		
		if (player.selectedGameMode == GameModes.IRONMAN) {
			GameMode.set(player, GameMode.IRONMAN, false);
		} else if (player.selectedGameMode == GameModes.ULTIMATE_IRON) {
			GameMode.set(player, GameMode.HARDCORE_IRONMAN, false);
		} else {
			GameMode.set(player, GameMode.NORMAL, false);
		}
	}

	public static void addStarterToInv(Player player) {
		for (Item item : player.selectedGameMode.starterPackItems) {
			player.getInventory().add(item);
		}
	}

	public static void selectMode(Player player, GameModes mode) {
		player.selectedGameMode = mode;
		check(player, mode);
		sendStartPackItems(player, mode);
		sendDescription(player, mode);
	}

	public static void check(Player player, GameModes mode) {
		for (GameModes gameMode : GameModes.values()) {
			if (player.selectedGameMode == gameMode) {
				player.getPacketSender().sendConfig(gameMode.configId, 1);
				continue;
			}
			player.getPacketSender().sendConfig(gameMode.configId, 0);
		}
	}

	public static void sendNames(Player player) {
		for (GameModes mode : GameModes.values()) {
			player.getPacketSender().sendString(mode.stringId, mode.name);
		}
	}
}
