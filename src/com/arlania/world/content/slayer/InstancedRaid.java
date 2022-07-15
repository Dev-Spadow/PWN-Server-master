package com.arlania.world.content.slayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.arlania.model.Item;
import com.arlania.model.Position;
import com.arlania.model.RegionInstance;
import com.arlania.model.RegionInstance.RegionInstanceType;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.content.raids.addons.RaidChest;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

public abstract class InstancedRaid {

	private String raidName;
	protected ArrayList<NPC> raidNpcs;
	protected int stage = -1;
	private slayerparty slayerparty;
	protected RegionInstance regionInstance;
	private Position defaultSpawn;
	private int height = 0;
	private HashMap<Integer, Integer> rewards = new HashMap<Integer, Integer>();

	public InstancedRaid(Position defaultSpawn, String raidName, slayerparty slayerparty) {
		this.height = slayerparty.getLeader().getIndex() * 4;
		this.defaultSpawn = new Position(0, 0, height);
		this.raidName = raidName;
		this.slayerparty = slayerparty;
	}

	public ArrayList<NPC> getNpcs() {
		return raidNpcs;
	}

	// destroys all npcs / instance
	public void destroyNpcs() {
		for (NPC n : raidNpcs) {
			if (World.getNpcs().contains(n)) {
				World.deregister(n);
			}
		}
	}

	public void setDefaultSpawn(int x, int y) {
		defaultSpawn.setX(x);
		defaultSpawn.setY(y);
	}

	public slayerparty getslayerparty() {
		return slayerparty;
	}

	public Position getDefaultPosition() {
		return defaultSpawn;
	}

	public RegionInstance getRegionInstance() {
		return regionInstance;
	}

	public ArrayList<NPC> getRaidNpcs() {
		return raidNpcs;
	}

	public int getStage() {
		return stage;
	}

	public String getName() {
		return raidName;
	}

	public void addNpc(NPC npc) {
		if (raidNpcs == null)
			raidNpcs = new ArrayList<NPC>();

		raidNpcs.add(npc);
	}

	public void spawnNpcs() {
		regionInstance = new RegionInstance(slayerparty.getLeader(), RegionInstanceType.RAID);
		slayerparty.getLeader().setRegionInstance(regionInstance);
		regionInstance.add(slayerparty.getLeader());
		for (Player member : slayerparty.getMembers()) {
			member.setRegionInstance(regionInstance);
			regionInstance.add(member);
		}
		
		
		for (Player member : slayerparty.getMembers()) {
			if (member != null) {
				slayerparty.updateslayerStatus(member, true);
			} else {
				slayerparty.getMembers().remove(member);
			}
		}
		slayerparty.updateslayerStatus(slayerparty.getLeader(), true);
	}

	public abstract void configureNpcs();

	public abstract void nextLevel();

	public void teleportAll() {
		for (Player member : slayerparty.getMembers()) {
			if (member != null && !slayerparty.isDefeated(member)) {
				Position alteredPosition = defaultSpawn;
				if (this instanceof slayer1) {
					alteredPosition.add(Misc.inclusiveRandom(-1, 1), 0);
				}

				member.moveTo(alteredPosition);
				if (stage > 0)
					member.sendMessage("->@blu@Your raid party has been teleported to the next raid boss!");
				else
					member.sendMessage("->@blu@Your raid party has begun the " + raidName + " raid!");
			}
		}

		if (slayerparty.getLeader() != null) {
			if (!slayerparty.isDefeated(slayerparty.getLeader())) {
				Position alteredPosition = defaultSpawn;
				alteredPosition.add(Misc.inclusiveRandom(-1, 1), 0);
				slayerparty.getLeader().moveTo(alteredPosition);
				if (stage > 0)
					slayerparty.getLeader()
							.sendMessage("->@blu@Your raid party has been teleported to the next raid boss!");
				else
					slayerparty.getLeader().sendMessage("->@blu@Your raid party has begun the " + raidName + " raid!");
			}
		}
	}

	private Player findNewRegionOwner() {
		for (Player member : slayerparty.getMembers()) {
			if (!slayerparty.isDefeated(member))
				return member;
		}
		return null;
	}




	

	public void respawn(Player player) {
		player.moveTo(defaultSpawn);
	}
}
