package com.arlania.world.content;

import java.time.LocalDateTime;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.GameMode;
import com.arlania.model.Item;
import com.arlania.model.PlayerRights;
import com.arlania.model.input.impl.JoinGroupInput;
import com.arlania.util.Misc;
import com.arlania.model.input.impl.CreateGroupInput;
import com.arlania.world.World;
import com.arlania.world.content.clan.ClanChatManager;
import com.arlania.world.content.gim.Group;
import com.arlania.world.content.gim.SharedStorage;
import com.arlania.world.entity.impl.player.Player;
import com.arlania.world.entity.impl.player.PlayerSaving;

public class GamemodeSelecter {

	public enum Gamemode {
		NORMAL(GameMode.NORMAL,
				"@or1@As a @re1@normal player @or1@you will be able to\\n @or1@play the game @re1@without any restrictions.",
				new Item[] { new Item(7806, 1), new Item(13867, 1), new Item(896, 1), new Item(15663, 1),
						new Item(15664, 1), new Item(15665, 1), (new Item(15661, 1)), (new Item(15662, 1)),
						(new Item(1639, 1)), (new Item(18928, 1)), (new Item(15003, 1)), (new Item(392, 200)),
						(new Item(2437, 25)), (new Item(2441, 25)), (new Item(3025, 25)), (new Item(2443, 25)),
						(new Item(995, 2147000)) }),
		IRON_MAN(GameMode.IRONMAN,
				"@or1@As a @re1@Ironman@or1@ You will \\n @or1@ be restricted from trading,\\n @or1@staking and looting items from killed players.\\n @or1@You will not get a npc drop if another player has\\n @or1@done more damage. You will have to rely\\n @or1@on your starter, skilling, pvming, and shops.\\n @or1@This game mode is for players that\\n @or1@love a challenge.",
				new Item[] { new Item(7806, 1), new Item(13867, 1), new Item(896, 1), new Item(15663, 1),
						new Item(15664, 1), new Item(15665, 1), (new Item(15661, 1)), (new Item(15662, 1)),
						(new Item(1639, 1)), (new Item(18928, 1)), (new Item(15003, 1)), (new Item(392, 200)),
						(new Item(2437, 25)), (new Item(2441, 25)), (new Item(4202, 1)), (new Item(5092, 1)),
						new Item(18904, 1), (new Item(3025, 25)), (new Item(2443, 25)), (new Item(995, 2147000)) }),
		ULTIMATE_IRON(GameMode.HARDCORE_IRONMAN,
				"@or1@As a @re1@Hardcore Ironman \\n  @or1@In addiction to the iron man rules you\\n @re1@cannot use banks. @or1@This gamemode is for the\\nplayers that @re1@love the impossible.",
				new Item[] { new Item(7806, 1), new Item(13867, 1), new Item(896, 1), new Item(15663, 1),
						new Item(15664, 1), new Item(15665, 1), (new Item(15661, 1)), (new Item(15662, 1)),
						(new Item(1639, 1)), (new Item(18928, 1)), (new Item(15003, 1)), (new Item(392, 200)),
						(new Item(2437, 25)), (new Item(2441, 25)), (new Item(4202, 1)), (new Item(5092, 1)),
						new Item(18904, 1), (new Item(3025, 25)), (new Item(2443, 25)), (new Item(995, 2147000)) }),
		GROUP_IRON(GameMode.GROUP_IRONMAN,
				"@re1@Group Ironman \\n  @or1@four of your friends can progress together\\n @or1@ as a team",
				new Item[] { new Item(7806, 1), new Item(13867, 1), new Item(896, 1), new Item(15663, 1),
						new Item(15664, 1), new Item(15665, 1), (new Item(15661, 1)), (new Item(15662, 1)),
						(new Item(1639, 1)), (new Item(18928, 1)), (new Item(15003, 1)), (new Item(392, 200)),
						(new Item(2437, 25)), (new Item(2441, 25)), (new Item(4202, 1)), (new Item(5092, 1)),
						new Item(18904, 1), (new Item(3025, 25)), (new Item(2443, 25)), (new Item(995, 2147000)) }),;

		GameMode gameMode;
		String description;
		Item[] items;

		Gamemode(GameMode gameMode, String description, Item[] items) {
			this.gameMode = gameMode;
			this.description = description;
			this.items = items;
		}
	}

	/**
	 * Handles opening the game mode selecter
	 * 
	 * @param player
	 */
	public static void open(Player player, boolean closeGroups) {
		selectGamemode(player, closeGroups ? player.selectedGamemode.ordinal() : Gamemode.NORMAL.ordinal(), false);
		player.getPacketSender().sendInterface(19301);
	}

	/**
	 * Handles switching between the different game modes
	 * 
	 * @param player
	 * @param ordinal
	 */
	public static void selectGamemode(Player player, int ordinal, boolean create) {
		Gamemode selected = Gamemode.values()[ordinal];
		//system.out.println("omegalul: " + selected.name());

		// Handles group iron man related stuff...
		if (selected.equals(Gamemode.GROUP_IRON) && (player.groupOwner == null || player.groupOwner == "") && !create) {
			sendOptionsTitle(player, "Select one of the following options", "Create new group", "Join existing group",
					"Show existing groups", "Cancel", true);
			player.setActionInterface("setupGamemode");
			return;
		} else if (!selected.equals(Gamemode.GROUP_IRON)
				&& (player.selectedGamemode.equals(Gamemode.GROUP_IRON) && player.groupOwner != "")) {
			sendOptionsTitle(player, "Are you sure you want to leave the group?", "Yes.", "No.", true);
			player.setTempSkillInt(ordinal);
			player.setActionInterface("leaveGroup");
			return;
		}

		player.selectedGamemode = selected;
		player.getPacketSender().sendItemContainer(player.selectedGamemode.items, 19311);
		player.getPacketSender().sendString(19309, player.selectedGamemode.description);
		player.getPacketSender().sendConfig(1676, player.selectedGamemode.ordinal());
	}

	/**
	 * Handles the button clicks made within the game selector interface
	 * 
	 * @param player
	 * @param buttonId
	 */
	public static void handleButtons(Player player, int buttonId) {

		switch (buttonId) {
		case 2461:
			if (player.getActionInterface().equals("acceptGroup"))
				acceptRequest(player);
			else if (player.getActionInterface().equals("groupStorage_open"))
				player.getBank(player.getCurrentBankTab()).open(player, true);
			break;

		case 2462:
			if (player.getActionInterface().equals("acceptGroup"))
				declineRequest(player);
			else if (player.getActionInterface().equals("groupStorage_open"))
				SharedStorage.open(player);
			break;
		}

		if (player.getInterfaceId() != 19301 && player.getInterfaceId() != 62252)
			return;

		// Handles the selecting of the gamemode
		if (buttonId >= 19312 && buttonId <= 19321) {
			player.setTempSkillInt((buttonId - 19312) / 3);
			selectGamemode(player, (buttonId - 19312) / 3, false);
			return;
		}

		switch (buttonId) {

		case 2461:
			if (player.getActionInterface().equals("setupGamemode")) {

			} else if (player.getActionInterface().equals("setupGamemode_1")) {
				player.setInputHandling(new CreateGroupInput());
				player.getPacketSender()
						.sendEnterInputPrompt("Please enter your unique code that allows others to join:");
				player.setTempSkillBoolean(true);
				player.setActionInterface("setupGamemode_2");
			} else if (player.getActionInterface().equals("leaveGroup"))
				leave(player);
			break;

		case 2462:
			if (player.getActionInterface().equals("setupGamemode")) {

			} else if (player.getActionInterface().equals("setupGamemode_1")) {
				player.setInputHandling(new CreateGroupInput());
				player.getPacketSender()
						.sendEnterInputPrompt("Please enter your unique code that allows others to join:");
				player.setTempSkillBoolean(false);
				player.setActionInterface("setupGamemode_2");
			} else if (player.getActionInterface().equals("leaveGroup")) {
				player.getPacketSender().closeChatInterface();
				player.setActionInterface("setupGamemode");
			}
			break;

		case 2482:
			if (player.getActionInterface().equals("setupGamemode")) {
				sendOptionsTitle(player, "Do you want this to be a public group?",
						"Yes, anyone with the code can join!", "No, nobody can join without my permission!", true);
				player.setActionInterface("setupGamemode_1");
			}
			break;

		case 2483:
			if (player.getActionInterface().equals("setupGamemode")) {
				player.setInputHandling(new JoinGroupInput());
				player.getPacketSender()
						.sendEnterInputPrompt("Please enter the unique code of the group you're trying to join:");
			}
			break;

		case 2484:
			if (player.getActionInterface().equals("setupGamemode")) {
				player.getPacketSender().sendGroups();
				player.getPacketSender().sendInterface(62252);
			}
			break;

		case -3282:
			GamemodeSelecter.open(player, true);
			break;

		case 2485:
			if (player.getActionInterface().equals("setupGamemode"))
				player.getPacketSender().closeChatInterface();
			break;

		case 19303: // Confirm
			if (player.didReceiveStarter())
				return;

			if (!PlayerPunishment.hasRecieved1stStarter(player.getHostAddress())) {
				System.out.println("a1");
				player.getPacketSender().sendInterfaceRemoval();
				player.setReceivedStarter(true);
				handleConfirm(player);
				addStarterToInv(player);
				ClanChatManager.join(player, "help");
				player.setPlayerLocked(false);
				if (player.referaledby.isEmpty() || player.referaledby == "") {
					player.getRefferalHandler().openRefferal(player);
				} else {
					player.getPacketSender().sendInterface(3559);
					player.getAppearance().setCanChangeAppearance(true);
				}
				player.setNewPlayer(false);
				PlayerPunishment.addIpToStarterList1(player.getHostAddress());
				PlayerPunishment.addIpToStarter1(player.getHostAddress());
				player.newPlayerBoostTime = LocalDateTime.now().plusHours(8);
				player.hasPlayerBoostTime = true;
				player.sendMessage("@whi@[STARTER BOOST]@bla@You gained 8 hours of @gre@100% drop rate bonus@bla@ for being a new player.");
				World.sendMessage("<img=493>@bla@[<shad=df7018>New Player</shad>@bla@]: <shad=df7018>"
						+ player.getUsername() + " </shad>has logged into <shad=df7018>Pwnlite for the first time!");
			} else if (PlayerPunishment.hasRecieved1stStarter(player.getHostAddress())
					&& !PlayerPunishment.hasRecieved2ndStarter(player.getHostAddress())) {
				System.out.println("a2");
				player.getPacketSender().sendInterfaceRemoval();
				player.setReceivedStarter(true);
				handleConfirm(player);
				addStarterToInv(player);
				ClanChatManager.join(player, "help");
				player.setPlayerLocked(false);
				if (player.referaledby.isEmpty() || player.referaledby == "") {
					player.getRefferalHandler().openRefferal(player);
				} else {
					player.getPacketSender().sendInterface(3559);
					player.getAppearance().setCanChangeAppearance(true);
				}
				player.setNewPlayer(false);
				PlayerPunishment.addIpToStarterList2(player.getHostAddress());
				PlayerPunishment.addIpToStarter2(player.getHostAddress());
				player.newPlayerBoostTime = LocalDateTime.now().plusHours(8);
				player.hasPlayerBoostTime = true;
				player.sendMessage("@whi@[STARTER BOOST]@bla@You gained 8 hours of @gre@100% drop rate bonus@bla@ for being a new player.");
				World.sendMessage("<img=493>@bla@[<shad=df7018>New Player</shad>@bla@]: <shad=df7018>"
						+ player.getUsername() + " </shad>has logged into <shad=df7018>Pwnlite for the first time!");
			} else if (PlayerPunishment.hasRecieved1stStarter(player.getHostAddress())
					&& PlayerPunishment.hasRecieved2ndStarter(player.getHostAddress())) {
				System.out.println("a3");
				handleConfirm(player);
				// ClanChatManager.join(player, "help");
				player.setPlayerLocked(false);
				if (player.referaledby.isEmpty() || player.referaledby == "") {
					player.getRefferalHandler().openRefferal(player);
				} else {
					player.getPacketSender().sendInterface(3559);
					player.getAppearance().setCanChangeAppearance(true);
				}
				player.setNewPlayer(false);
				player.getPacketSender().sendMessage("You've received to many starters.");
				World.sendMessage("<img=493>@bla@[<shad=df7018>New Player</shad>@bla@]: <shad=df7018>"
						+ player.getUsername() + " </shad>has logged into <shad=df7018>Pwnlite for the first time!");
			}
			player.getPacketSender().sendRights();
			PlayerSaving.save(player);
			break;
		}
	}

	/**
	 * Handles adding the starter items to the players inventory
	 * 
	 * @param player
	 */
	public static void addStarterToInv(Player player) {
		for (Item item : player.selectedGamemode.items)
			player.getInventory().add(item);
	}

	/**
	 * Handles confirming the players decision
	 * 
	 * @param player
	 */
	public static void handleConfirm(Player player) {
		System.out.println("Game mode: " + player.selectedGamemode);
		GameMode.set(player, player.selectedGamemode.gameMode, false);
	}

	private static void sendOptionsTitle(Player player, String title, String s, String s2, String s3, String s4,
			boolean expanded_swords) {
		player.getPacketSender().sendString(2481, title);
		player.getPacketSender().sendString(2482, s);
		player.getPacketSender().sendString(2483, s2);
		player.getPacketSender().sendString(2484, s3);
		player.getPacketSender().sendString(2485, s4);
		player.getPacketSender().sendFrame164(2480);
		if (expanded_swords) {
			player.getPacketSender().sendMouseTrigger(2489, false);
			player.getPacketSender().sendMouseTrigger(2488, true);
		} else {
			player.getPacketSender().sendMouseTrigger(2488, false);
			player.getPacketSender().sendMouseTrigger(2489, true);
		}
	}

	public static void sendOptionsTitle(Player player, String title, String s, String s2, boolean expanded_swords) {
		player.getPacketSender().sendString(2460, title);
		player.getPacketSender().sendString(2461, s);
		player.getPacketSender().sendString(2462, s2);
		player.getPacketSender().sendFrame164(2459);
		if (expanded_swords) {
			player.getPacketSender().sendMouseTrigger(2468, false);
			player.getPacketSender().sendMouseTrigger(2465, true);
		} else {
			player.getPacketSender().sendMouseTrigger(2465, false);
			player.getPacketSender().sendMouseTrigger(2468, true);
		}
	}

	private static void sendStatement(Player player, String s) {
		player.getPacketSender().sendString(357, s);
		player.getPacketSender().sendString(358, "Click here to continue");
		player.getPacketSender().sendFrame164(356);
	}

	private static void sendStatement(Player player, String s, String s2) {
		player.getPacketSender().sendString(360, s);
		player.getPacketSender().sendString(361, s2);
		player.getPacketSender().sendFrame164(359);
	}

	public static void create(Player player, String code, boolean public_group) {
		Group group = Group.forUnique(code);
		if (group != null) {
			player.setInputHandling(new CreateGroupInput());
			player.getPacketSender()
					.sendEnterInputPrompt("[Already in use] - Please enter a unique code that allows others to join:");
			return;
		}

		System.out.println("old Group.groups.size() :" + Group.groups.size());
		group = new Group(player.getUsername(), code, public_group);
		group.members.add(player.getUsername());
		group.save();
		System.out.println("new Group.groups.size() :" + Group.groups.size());
		open(player, false);
		selectGamemode(player, Gamemode.GROUP_IRON.ordinal(), true);
		player.groupOwner = group.owner;
		player.setActionInterface("setupGamemode");
		PlayerSaving.save(player);
	}

	public static void join(Player player, String code) {
		System.out.println("code:" + code);

		Group group = Group.forUnique(code);
		if (group == null) {
			player.getPacketSender().sendInterface(19301);
			sendStatement(player, "Incorect code - Please wait another 3 seconds...");
			TaskManager.submit(new Task(1, false) {
				int tick = 6;

				@Override
				protected void execute() {
					player.getPacketSender().sendString(357,
							"Incorect code - Please wait another " + tick / 2 + " seconds...");
					tick--;
					if (tick == -1) {
						sendOptionsTitle(player, "Select one of the following options", "Create new group",
								"Join existing group", "Show existing groups", "Cancel", true);
						player.setActionInterface("setupGamemode");
						stop();
					}
				}
			});
			return;
		}
		Player owner = World.getPlayerByName(group.owner);

		if (owner == null) {
			player.getPacketSender().sendInterface(19301);
			sendStatement(player, "Owner of the group is not online!");
			player.setActionInterface("setupGamemode");
			return;
		}

		if (owner.requestToJoin != null) {
			player.getPacketSender().sendInterface(19301);
			sendStatement(player, "Owner of the group already has an outgoing invite!");
			player.setActionInterface("setupGamemode");
			return;
		}

		owner.requestToJoin = player;
		sendOptionsTitle(owner, "Accept " + player.getUsername() + " into your group?", "Yes.", "No.", true);
		owner.setActionInterface("acceptGroup");
		player.getPacketSender().sendInterface(19301);
		sendStatement(player, "Requesting to join - Waiting for owner to accept!", "Please wait another 30 seconds...");

		TaskManager.submit(new Task(1, false) {
			int tick = 60;

			@Override
			protected void execute() {
				if (owner.requestToJoin == null) {
					stop();
					return;
				}
				player.getPacketSender().sendString(361, "Please wait another " + tick / 2 + " seconds...");
				tick--;
				if (tick == -1) {
					sendStatement(player, "Owner did not response on your request!");
					player.setActionInterface("setupGamemode");
					owner.requestToJoin = null;
					owner.getPacketSender().closeChatInterface();
					stop();
				}
			}
		});
	}

	private static void declineRequest(Player player) {
		Player requested = player.requestToJoin;
		// requested.getPacketSender().sendInterface(19301);
		// Tells the player trying to join he didnt get accepted
		sendStatement(requested, "Owner has declined your request!");
		requested.setActionInterface("setupGamemode");
		// Sets the join to null
		player.requestToJoin = null;
		// Closes the chat interface
		player.getPacketSender().closeChatInterface();
	}

	private static void acceptRequest(Player player) {
		// The group the player is joining
		Group group = Group.forOwner(player.getUsername());
		// The player joining
		Player requested = player.requestToJoin;
		// Sets the join to null
		player.requestToJoin = null;
		// Gives the player thats joining the confirm
		sendStatement(requested, "Owner has accepted your request!", "Your gamemode has been updated!");
		requested.setActionInterface("setupGamemode");
		// Sets the game mode
		selectGamemode(requested, Gamemode.GROUP_IRON.ordinal(), true);
		// Sets the group owner for the player joining
		requested.groupOwner = player.getUsername();
		// Closes the chat interface
		player.getPacketSender().closeChatInterface();
		// Tells the members someone joined there group
		for (String name : group.members) {
			Player member = World.getPlayerByName(name);
			if (member == null)
				continue;
			member.getPacketSender().sendMessage(
					"<col=FF0000>" + Misc.ucFirst(requested.getUsername()) + " has joined the group!</col>");
		}
		// Adds the member to the group
		group.members.add(requested.getUsername());
		// Saves the group
		group.save();

		PlayerSaving.save(player);
	}

	private static void leave(Player player) {
		// Grabs what group to leave
		Group group = Group.forOwner(player.groupOwner);
		// Removes the group owner
		player.groupOwner = "";
		// Sets the game mode he selected
		System.out.println("ordinal: " + player.getTempSkillInt());
		Gamemode mode = Gamemode.values()[player.getTempSkillInt()];
		// Removes the player
		group.members.remove(player.getUsername());
		//
		if (group.members.size() == 0) {
			System.out.println("Only member left so remove the clan");
			Group.groups.remove(group);
		}
		// Saves the group
		group.save();
		// Sends all players that a player left
		for (String name : group.members) {
			Player member = World.getPlayerByName(name);
			if (member == null)
				continue;
			member.getPacketSender()
					.sendMessage("<col=FF0000>" + Misc.ucFirst(player.getUsername()) + " has left the group!</col>");
		}
		// Selects the game mode
		selectGamemode(player, mode.ordinal(), false);
		// Sends the player an statement
		sendStatement(player, "Your gamemode has been updated.",
				"Please select a new gamemode or confirm on the bottom right!");
		// Sets it back to setup gamemode
		player.setActionInterface("setupGamemode");
		PlayerSaving.save(player);
	}

	public static void leaveGroup(Player player) {
		// Grabs what group to leave
		Group group = Group.forOwner(player.groupOwner);
		// Removes the group owner
		player.groupOwner = "";
		// Removes the player
		group.members.remove(player.getUsername());
		boolean skipGroup = false;
		if (group.members.size() == 0) {
			System.out.println("Only member left so remove the clan");
			Group.groups.remove(group);
			skipGroup = true;
		}
		// Saves the group
		group.save();

		if (skipGroup) {
			// Sends all players that a player left
			for (String name : group.members) {
				Player member = World.getPlayerByName(name);
				if (member == null)
					continue;
				member.getPacketSender().sendMessage(
						"<col=FF0000>" + Misc.ucFirst(player.getUsername()) + " has left the group!</col>");
			}
		}

		PlayerSaving.save(player);
	}
}
