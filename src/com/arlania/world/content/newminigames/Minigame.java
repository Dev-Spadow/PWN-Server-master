package com.arlania.world.content.newminigames;
import java.util.ArrayList;
import com.arlania.model.GameObject;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

/**
 * A template for creating Minigame Children
 * 
 * @author Skwishy
 *
 */
public abstract class Minigame {

	/**
	 * A list of players in the minigame
	 */
	private ArrayList<Player> players;
	/**
	 * A list of npcs in the minigame
	 */
	private ArrayList<NPC> npcs;
	/**
	 * A list of objects in the minigame
	 */
	private ArrayList<GameObject> objects;
	/**
	 * The unique id of the minigame
	 */
	private int uniqueId;
	/**
	 * The index of the minigame
	 */
	private int idx;

	/**
	 * A default class constructor
	 */
	public Minigame() {
		this.players = new ArrayList<Player>();
		this.npcs = new ArrayList<NPC>();
		this.objects = new ArrayList<GameObject>();
		this.uniqueId = this.hashCode();
		this.idx = 1;
	}

	/**
	 * A default class constructor
	 */
	public Minigame(int idx) {
		this.players = new ArrayList<Player>();
		this.npcs = new ArrayList<NPC>();
		this.objects = new ArrayList<GameObject>();
		this.uniqueId = this.hashCode();
		if (idx == 0)
			idx = 1;
		this.idx = idx;
	}

	/**
	 * Returns the unique id of the Minigame
	 * 
	 * @return
	 */
	public int getUniqueId() {
		return this.uniqueId;
	}

	/**
	 * Returns a list of players in the minigame
	 * 
	 * @return
	 */
	public ArrayList<Player> getPlayers() {
		return this.players;
	}

	/**
	 * Returns a list of objects in the minigame
	 * 
	 * @return
	 */
	public ArrayList<GameObject> getObjects() {
		return this.objects;
	}

	/**
	 * Returns a list of npcs in the minigame
	 * 
	 * @return
	 */
	public ArrayList<NPC> getNpcs() {
		return this.npcs;
	}

	/**
	 * Returns the index of the minigame
	 * 
	 * @return
	 */
	public int getIndex() {
		return this.idx;
	}

	/**
	 * Invokes special actions on player entering minigame
	 * 
	 * @param player The player entering the minigame
	 */
	public abstract void enterMinigame(Player player);

	/**
	 * Invokes special actions on player leaving minigame
	 * 
	 * @param player The player leaving the minigame
	 */
	public abstract void leaveMinigame(Player player);

	/**
	 * The restriction of the minigame
	 * 
	 * @return Restriction
	 */


}
