package com.arlania.world.content.newminigames;

import com.arlania.model.Position;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

/**
 * A template for creating Controller children
 * 
 * @author Skwishy
 *
 */
public abstract class Controller {

	/**
	 * The minigame that is associated with the controller
	 */
	private Minigame associatedMinigame;

	/**
	 * Class constructor with no associated minigame
	 */
	public Controller() {
	}

	/**
	 * Class constructor with associated minigame
	 */
	public Controller(Minigame associatedMinigame) {
		this.associatedMinigame = associatedMinigame;
	}

	/**
	 * Returns the minigame associated with the controller
	 * 
	 * @return The Associated minigame
	 * @return null If there is no associated minigame
	 */
	public Minigame getAssociatedMinigame() {
		return this.associatedMinigame;
	}

	/**
	 * 
	 * @return true If the controller has an associated minigame
	 * @return false If the controller is null
	 */
	public boolean isMinigameController() {
		return this.associatedMinigame != null;
	}

	/**
	 * Determines whether the player can fight another player
	 * 
	 * @return true or false Depends on implementation
	 */
	public abstract boolean canFightPlayer();

	/**
	 * Determines whether the player can fight another npc
	 * 
	 * @return true or false Depends on implementation
	 */
	public abstract boolean canFightNPC();

	/**
	 * Invokes special actions on player logging in with controller enabled
	 * 
	 * @param player The player that is logging in
	 */
	public abstract void login(Player player);

	/**
	 * Invokes special actions on player logging out with controller enabled
	 * 
	 * @param player The player that is logging out
	 * @return true If player can logout
	 * @return false If player cannot logout
	 */
	public abstract boolean logout(Player player);

	/**
	 * Invokes special actions on player dying with controller enabled
	 * 
	 * @param player The player that is dying
	 * @return true If player death should not be handled by normal death task
	 * @return false If the player death should be handled by normal death task
	 */
	public abstract boolean handleDeath(Player player);

	/**
	 * Return null if respawn is back home
	 * 
	 * @return The respawn location of the minigame.
	 */
	public abstract Position getRespawnLocation();

	/**
	 * Invokes special actions on player teleporting with controller enabled
	 * 
	 * @param player The player that is teleporting
	 * @return true If the player should not be teleported
	 * @return false If the player should be teleported
	 */
	public abstract boolean teleport(Player player);

	/**
	 * Sends a unique interface to player
	 * 
	 * @param player The player to send interface to
	 */
	public abstract void sendInterface(Player player);

	/**
	 * Invokes special actions on player killing & player dying with controller
	 * enabled
	 * 
	 * @param player The killer
	 * @param target The target
	 * @return true If special actions should be made
	 * @return false If normal handler should handle player death
	 */
	public abstract boolean handlePlayerDeath(Player player, Player target);

	/**
	 * Invokes special actions on player killing & npc dying with controller enabled
	 * 
	 * @param player The killer
	 * @param target The npc target
	 * @return true If special actions should be made
	 * @return false If normal handler should handle npc death
	 */
	public abstract boolean handleNPCDeath(Player player, NPC target);

	/**
	 * Invokes special actions on player clicking npc with controller enabled
	 * 
	 * @param player The player that is clicking
	 * @param n      The npc that is being clicked
	 * @param option The npc option (first, second...)
	 * @return true If special actions should be made
	 * @return false If normal handler should handle npc click
	 */
	public abstract boolean handleNPCClick(Player player, NPC n, int option);

	/**
	 * Invokes special actions on player clicking player with controller enabled
	 * 
	 * @param player The player that is clicking
	 * @param other  The other player being clicked
	 * @param option The player option (first, second...)
	 * @return true If special actions should be made
	 * @return false If normal handler should handle player click
	 */
	public abstract boolean handlePlayerClick(Player player, Player other, int option);

	/**
	 * Invokes special actions on player clicking object with controller enabled
	 * 
	 * @param player The player that is clicking
	 * @param o      The object that is being clicked
	 * @param option The object option (first, second...)
	 * @return true If special actions should be made
	 * @return false If normal handler should handle object click
	 */
	public abstract boolean handleObjectClick(Player player, Object o, int option);
	
	/**
	 * Invokes special actions on player clicking button with controller enabled
	 * 
	 * @param player The player that is clicking
	 * @param buttonId	The id of the button being pressed
	 * @return true If special actions should be made
	 * @return false If normal handler should handle object click
	 */
	public abstract boolean handleButtonClick(Player player, int buttonId);

}
