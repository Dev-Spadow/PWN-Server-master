package com.arlania.world;

import java.util.Iterator;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

import com.arlania.world.content.triviabot.TriviaBotHandler;

import com.arlania.GameSettings;
import com.arlania.engine.CharacterBackup;
import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.PlayerRights;
import com.arlania.util.Misc;
import com.arlania.world.content.CustomFreeForAll;
import com.arlania.world.content.EvilTrees;
import com.arlania.world.content.PlayerPanel;
import com.arlania.world.content.Reminders;
import com.arlania.world.content.ShootingStar;
import com.arlania.world.content.StaffList;
import com.arlania.world.content.minigames.impl.FightPit;
import com.arlania.world.content.minigames.impl.FreeForAll;
import com.arlania.world.content.minigames.impl.LastManStanding;
import com.arlania.world.content.minigames.impl.PestControl;
import com.arlania.world.entity.Entity;
import com.arlania.world.entity.EntityHandler;
import com.arlania.world.entity.impl.CharacterList;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;
import com.arlania.world.entity.impl.player.PlayerHandler;
import com.arlania.world.entity.updating.NpcUpdateSequence;
import com.arlania.world.entity.updating.PlayerUpdateSequence;
import com.arlania.world.entity.updating.UpdateSequence;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * @author Gabriel Hannason Thanks to lare96 for help with parallel updating
 *         system
 */
public class World {

	public static long LAST_SHOP_RELOAD;

	/** All of the registered players. */
	private static CharacterList<Player> players = new CharacterList<>(1000);

	/** All of the registered NPCs. */
	private static CharacterList<NPC> npcs = new CharacterList<>(5000);

    public static void setNpcs(CharacterList<NPC> npcs) {
        World.npcs = npcs;
    }

    /** Used to block the game thread until updating has completed. */
	private static Phaser synchronizer = new Phaser(1);

	/** A thread pool that will update players in parallel. */
	private static ExecutorService updateExecutor = Executors.newFixedThreadPool(
			Runtime.getRuntime().availableProcessors(),
			new ThreadFactoryBuilder().setNameFormat("UpdateThread").setPriority(Thread.MAX_PRIORITY).build());

	/** The queue of {@link Player}s waiting to be logged in. **/
	private static Queue<Player> logins = new ConcurrentLinkedQueue<>();

	/** The queue of {@link Player}s waiting to be logged out. **/
	private static Queue<Player> logouts = new ConcurrentLinkedQueue<>();

	/** The queue of {@link Player}s waiting to be given their vote reward. **/
	private static Queue<Player> voteRewards = new ConcurrentLinkedQueue<>();

	public static DonationDeals deals = new DonationDeals();

	public static boolean DOUBLE_DONATIONS;

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void register(Entity entity) {
		EntityHandler.register(entity);
	}

	public static void deregister(Entity entity) {
		EntityHandler.deregister(entity);
	}

	public static Player getPlayerByName(String username) {
		Optional<Player> op = players.search(p -> p != null && p.getUsername().equals(Misc.formatText(username)));
		return op.isPresent() ? op.get() : null;
	}

	public static Player getPlayerByIndex(int username) {
		Optional<Player> op = players.search(p -> p != null && p.getIndex() == username);
		return op.isPresent() ? op.get() : null;
	}

	public static Player getPlayerByLong(long encodedName) {
		Optional<Player> op = players.search(p -> p != null && p.getLongUsername().equals(encodedName));
		return op.isPresent() ? op.get() : null;
	}

	public static void sendMessage(String message) {
		players.forEach(p -> p.getPacketSender().sendMessage(message));
	}

	public static void sendChannelMessage(String channel, String message) {
		players.forEach(p -> p.getPacketSender().sendMessage("[<col=" + deteremineChannelColor(channel) + ">" + channel + "</col>]" + message));
	}

	public static String deteremineChannelColor(String channel) {
		switch (channel) {
			case "General":
				return "369408";
			case "Donation":
				return "8812E4";
			case "Voting":
				return "23ACBD";
			case "Drops":
				return "194CF4";
			default:
				return "255";
		}
	}

	//TODO: Implement a channel icon system

	public static void refreshPanel() {
        players.forEach(p -> PlayerPanel.refreshPanel(p));
    }

	public static void sendStaffMessage(String message) {
		players.stream()
				.filter(p -> p != null && (p.getRights() == PlayerRights.OWNER
						|| p.getRights() == PlayerRights.DEVELOPER|| p.getRights() == PlayerRights.CO_OWNER  || p.getRights() == PlayerRights.ADMINISTRATOR
								|| p.getRights() == PlayerRights.EVENT_MANAGER|| p.getRights() == PlayerRights.MODERATOR || p.getRights() == PlayerRights.COMMUNITY_MANAGER || p.getRights() == PlayerRights.SUPPORT))
				.forEach(p -> p.getPacketSender().sendMessage(message));
	}


	public static void register(Player c, NPC entity) {
		EntityHandler.register(entity);
		c.getRegionInstance().getNpcsList().add(entity);
	}

	public static void updatePlayersOnline() {
		// players.forEach(p-> p.getPacketSender().sendString(39173,
		// PlayerPanel.LINE_START + "@or1@Players Online: @yel@"+players.size()));
		players.forEach(
				p -> p.getPacketSender().sendString(45304, "@or2@Player Online: @gre@" + (int) (players.size()) + ""));
		players.forEach(
				p -> p.getPacketSender().sendString(26608, "@or2@Players Online: @gre@" + (int) (players.size()) + ""));
		players.forEach(
				p -> p.getPacketSender().sendString(57003, "Players:  @gre@" + (int) (World.getPlayers().size()) + ""));
		updateStaffList();
	}

	public static void updateStaffList() {
		TaskManager.submit(new Task(false) {
			@Override
			protected void execute() {
				players.forEach(p -> StaffList.updateInterface(p));
				stop();
			}
		});
	}

	public static void savePlayers() {
		players.forEach(p -> p.save());
	}

	public static CharacterList<Player> getPlayers() {
		return players;
	}

	public static CharacterList<NPC> getNpcs() {
		return npcs;
	}

	public static void sequence() {
		// Handle queued logins.
		for (int amount = 0; amount < GameSettings.LOGIN_THRESHOLD; amount++) {
			Player player = logins.poll();
			if (player == null)
				break;
			PlayerHandler.handleLogin(player);
		}
		// Handle queued logouts.
		int amount = 0;
		Iterator<Player> $it = logouts.iterator();
		while ($it.hasNext()) {
			Player player = $it.next();
			if (player == null || amount >= GameSettings.LOGOUT_THRESHOLD)
				break;
			if (PlayerHandler.handleLogout(player, false, false)) {
				$it.remove();
				amount++;
			}
		}
		CharacterBackup.sequence();
		FightPit.sequence();
		// Cows.sequence();
		Reminders.sequence();
		// Cows.spawnMainNPCs();
		PestControl.sequence();
		ShootingStar.sequence();
		EvilTrees.sequence();
		TriviaBotHandler.sequence();
		// ShopRestocking.sequence();
		FreeForAll.sequence();
		CustomFreeForAll.sequence();
		LastManStanding.sequence();
		// First we construct the update sequences.
		UpdateSequence<Player> playerUpdate = new PlayerUpdateSequence(synchronizer, updateExecutor);
		UpdateSequence<NPC> npcUpdate = new NpcUpdateSequence();
		// Then we execute pre-updating code.
		players.forEach(playerUpdate::executePreUpdate);
		npcs.forEach(npcUpdate::executePreUpdate);
		// Then we execute parallelized updating code.
		synchronizer.bulkRegister(players.size());
		players.forEach(playerUpdate::executeUpdate);
		synchronizer.arriveAndAwaitAdvance();
		// Then we execute post-updating code.
		players.forEach(playerUpdate::executePostUpdate);
		npcs.forEach(npcUpdate::executePostUpdate);
	}

	public static Queue<Player> getLoginQueue() {
		return logins;
	}

	public static Queue<Player> getLogoutQueue() {
		return logouts;
	}




	public static Queue<Player> getVoteRewardingQueue() {
		return voteRewards;
	}

	public static boolean npcIsRegistered(int id) {
		for (NPC n : getNpcs()) {
			if (n != null && n.getId() == id) {
				return true;
			}
		}
		return false;
	}

	public static void sendBroadcastMessage(String message) {
		for (Player p : players) {
			if (p == null)
				continue;
			p.getPacketSender().sendMessage("<col=ff0000>Server Message: <col=00ffff>" + message);
		}
	}
}

