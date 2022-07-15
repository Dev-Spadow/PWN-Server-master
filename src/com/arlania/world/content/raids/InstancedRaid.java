package com.arlania.world.content.raids;

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
	private RaidParty raidParty;
	protected RegionInstance regionInstance;
	private Position defaultSpawn;
	private int height = 0;
	private HashMap<Integer, Integer> rewards = new HashMap<Integer, Integer>();

	public InstancedRaid(Position defaultSpawn, String raidName, RaidParty raidParty) {
		this.height = raidParty.getLeader().getIndex() * 4;
		this.defaultSpawn = new Position(0, 0, height);
		this.raidName = raidName;
		this.raidParty = raidParty;
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

	public RaidParty getRaidParty() {
		return raidParty;
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
		regionInstance = new RegionInstance(raidParty.getLeader(), RegionInstanceType.RAID);
		raidParty.getLeader().setRegionInstance(regionInstance);
		regionInstance.add(raidParty.getLeader());
		for (Player member : raidParty.getMembers()) {
			member.setRegionInstance(regionInstance);
			regionInstance.add(member);
		}
		if (this instanceof Raid3) {
			for (NPC n : raidNpcs) {
				n.getPosition().setZ(height);
			}
		} else {
			Iterator<NPC> spawnIterator = raidNpcs.iterator();
			while (spawnIterator.hasNext()) {
				NPC npc = spawnIterator.next();
				npc.getPosition().setZ(height);
				regionInstance.add(npc);
				World.register(npc);
			}
		}
		for (Player member : raidParty.getMembers()) {
			if (member != null) {
				raidParty.updateRaidingStatus(member, true);
			} else {
				raidParty.getMembers().remove(member);
			}
		}
		raidParty.updateRaidingStatus(raidParty.getLeader(), true);
	}

	public abstract void configureNpcs();

	public abstract void nextLevel();

	public void teleportAll() {
		for (Player member : raidParty.getMembers()) {
			if (member != null && !raidParty.isDefeated(member)) {
				Position alteredPosition = defaultSpawn;
				if (this instanceof Raid1) {
					alteredPosition.add(Misc.inclusiveRandom(-1, 1), 0);
				} else if (this instanceof Raid2) {
					alteredPosition.add(Misc.inclusiveRandom(-2, 2), Misc.inclusiveRandom(-2, 2));
				} else if (this instanceof Raid3) {
					alteredPosition.add(Misc.inclusiveRandom(-1, 1), Misc.inclusiveRandom(-1, 1));
				}

				member.moveTo(alteredPosition);
				if (stage > 1)
					
						member.sendMessage("->@red@Your raid party has been teleported to the next raid boss!");
				if (stage > 2)
					
						member.sendMessage("->@red@Your raid party has been teleported to the next raid boss!");
				
				if (stage > 0)
					member.sendMessage("->@blu@Your raid party has been teleported to the next raid boss!");
				else
					member.sendMessage("->@blu@Your raid party has begun the " + raidName + " raid!");
			}
		}

		if (raidParty.getLeader() != null) {
			if (!raidParty.isDefeated(raidParty.getLeader())) {
				Position alteredPosition = defaultSpawn;
				alteredPosition.add(Misc.inclusiveRandom(-1, 1), 0);
				raidParty.getLeader().moveTo(alteredPosition);
				if (stage > 1)
					raidParty.getLeader()
							.sendMessage("->@red@Your raid party has been teleported to the next raid boss!");
				if (stage > 2)
					raidParty.getLeader()
							.sendMessage("->@red@Your raid party has been teleported to the next raid boss!");
				if (stage > 0)
					raidParty.getLeader()
							.sendMessage("->@red@Your raid party has been teleported to the next raid boss!");
				else
					raidParty.getLeader().sendMessage("->@blu@Your raid party has begun the " + raidName + " raid!");
			}
		}
	}

	private Player findNewRegionOwner() {
		for (Player member : raidParty.getMembers()) {
			if (!raidParty.isDefeated(member))
				return member;
		}
		return null;
	}

	public void defeat(Player member) {
		if (raidParty.getLeader() == member) {
			// pass on region leadership
			Player newOwner = findNewRegionOwner();
			if (newOwner == null) {
				regionInstance.remove(member);
				raidParty.failedRaid();
				raidParty.updateRaidingStatus(member, false);
				return;
			} else {
				regionInstance.setOwner(findNewRegionOwner());
			}
		}
		regionInstance.remove(member);
		raidParty.addDefeatedMember(member);
		raidParty.updateRaidingStatus(member, false);
	}

	private void givePoints(int amount) {
		for(Player p : raidParty.getMembers()) {
			p.getPointsHandler().setRaidPoints(p.getPointsHandler().getRaidPoints() + amount, false);
			p.sendMessage("You received " + amount + " points, you now have " + p.getPointsHandler().getRaidPoints() + "!");
		}
		raidParty.getLeader().getPointsHandler().setRaidPoints(raidParty.getLeader().getPointsHandler().getRaidPoints() + amount, false);
		raidParty.getLeader().sendMessage("You received " + amount + " points, you now have " + raidParty.getLeader().getPointsHandler().getRaidPoints() + "!");
	}
	
	public void victory() {
		// for raids without chests
		rewards.put(14471, 1);
		raidParty.succeededRaid(rewards);
		givePoints(Misc.inclusiveRandom(100, 250));
	}

	public void respawn(Player player) {
		player.moveTo(defaultSpawn);
	}
}
