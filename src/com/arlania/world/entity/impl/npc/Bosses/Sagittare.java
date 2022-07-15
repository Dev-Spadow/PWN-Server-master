package com.arlania.world.entity.impl.npc.Bosses;

import com.arlania.model.Position;
import com.arlania.model.RegionInstance;
import com.arlania.model.RegionInstance.RegionInstanceType;
import com.arlania.world.World;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.npc.Bosses.Boundary;
import com.arlania.world.entity.impl.player.Player;

public class Sagittare {

	/**
	 * The player associated with this event
	 */
	private final Player player;

	private int height;

	/**
	 * The boundary location
	 */
	public static final Boundary BOUNDARY = new Boundary(2197, 3302, 2217, 3301);

	/**
	 * The npc
	 */
	private NPC npc;
	

	/**
	 * Creates a new event for the player
	 * 
	 * @param player
	 *            the player
	 */
	public Sagittare(Player player) {
		this.player = player;
	}

	public void initialize() {
		if (player.getRegionInstance() != null) {
			player.getRegionInstance().destruct();
		}
		this.height =  player.getIndex() * 4;
		player.setRegionInstance(new RegionInstance(player, RegionInstanceType.SAGITTARE));
		player.getPacketSender().sendInterfaceReset();
		Position pos = new Position(2210, 3302, height);
		NPC sagittare = new NPC(6307, pos).setSpawnedFor(player);
		World.register(sagittare);
		this.npc = sagittare;
		player.getRegionInstance().getNpcsList().add(sagittare);
		teleport();
	}

	private void teleport() {
		Position sagittareMap = new Position(2212, 3300, this.getHeight());
		player.getPacketSender().sendMessage("@red@Welcome to Spidey!");
		player.moveTo(sagittareMap);
	}
	
	/**
	 * Stops the zulrah instance and concludes the events
	 */
	public void stop() {
		player.getRegionInstance().destruct();
	}

	/**
	 * The reference to zulrah, the npc
	 * 
	 * @return the reference to zulrah
	 */
	public NPC getNpc() {
		return npc;
	}

	public int getHeight() {
		return height;
	}
}
