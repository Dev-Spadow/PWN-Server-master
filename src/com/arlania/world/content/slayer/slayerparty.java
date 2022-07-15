package com.arlania.world.content.slayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.arlania.GameSettings;
import com.arlania.model.Item;
import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class slayerparty {

	public static int ORB_OF_PROTECTION = 587;

	private Player slayerLeader;
	private ArrayList<Player> slayerMemebers;
	private InstancedRaid currentSlayer;
	private ArrayList<Player> defeatedMembers;
	private ConcurrentHashMap<Player, Boolean> slayerStatus;

	public slayerparty(Player slayerLeader) {
		if (!slayerLeader.getInventory().contains(ORB_OF_PROTECTION) && !slayerLeader.getBank().contains(ORB_OF_PROTECTION)) {
			if (slayerLeader.getInventory().getFreeSlots() > 0) {
				slayerLeader.getInventory().add(ORB_OF_PROTECTION, 1);
				slayerLeader.sendMessage("@blu@<shad=10>A magical orb was added to your inventory.");
				slayerLeader.sendMessage("@blu@<shad=10>->Use this magical orb on players to accept them into your slayer party.");
			} else {
				if (slayerLeader.getBank().getFreeSlots() > 0) {
					slayerLeader.getInventory().add(ORB_OF_PROTECTION, 1);
					slayerLeader.sendMessage("@blu@<shad=10>A magical orb was added to your bank.");
					slayerLeader
							.sendMessage("@blu@<shad=10>->Use the magical orb on players to accept them into your slayer party.");
				}
			}
		}
		this.slayerLeader = slayerLeader;
		slayerMemebers = new ArrayList<Player>();
		defeatedMembers = new ArrayList<Player>();
		slayerStatus = new ConcurrentHashMap<Player, Boolean>();
		slayerLeader.sendMessage("@blu@<shad=10>You have created a slayerdungeon party!");
		slayerLeader.sendMessage("->@blu@<shad=10>You can invite a maximum of 3 players to your slayer dungeon.");
	}

	public void updateslayerStatus(Player member, boolean value) {
		slayerStatus.put(member, value);
	}

	public boolean getIsRaiding(Player member) {
		if (slayerStatus.get(member) != null)
			return slayerStatus.get(member);
		else
			return false;
	}

	public void refresh() {
		defeatedMembers.clear();
		slayerStatus.clear();
		for (Player member : slayerMemebers) {
			slayerStatus.put(member, true);
		}
		slayerStatus.put(slayerLeader, true);
	}

	public void leave(Player member) {
		if (member == slayerLeader) {
			disband();
			return;
		}
		if (slayerMemebers.contains(member)) {
			slayerMemebers.remove(member);
			if (member.getRegionInstance() != null) {
				member.setRegionInstance(null);
				member.moveTo(GameSettings.DEFAULT_POSITION);
				if (slayerStatus.containsKey(member))
					slayerStatus.remove(member);
				sendMessageToMembers("@red@<shad=10>->" + member.getUsername() + " has left your slayer group.", false);
			}
		}
	}


	public boolean isDefeated(Player member) {
		return defeatedMembers.contains(member);
	}

	public void setSlayer(InstancedRaid raid) {
		currentSlayer = raid;
		sendMessageToMembers("<col=60148a><shad=200>Your current slayerdungeon has been set to: " + raid.getName(), false);
		slayerLeader.sendMessage("<col=60148a><shad=200>You have set your slayer dungeon to: " + raid.getName());
	}

	public InstancedRaid getcurrentSlayer() {
		return currentSlayer;
	}

	public void addMember(Player recruit) {
		for (Player member : slayerMemebers) {
			if (member == null || !member.getSession().getChannel().isConnected()) {
				slayerMemebers.remove(member);
			}
		}
		if (slayerMemebers.size() >= 3) {
			slayerLeader.sendMessage("@red@<shad=200>You can only add 3 additional players to your slayer party.");
			return;
		}

		if (slayerMemebers.contains(recruit)) {
			slayerLeader.sendMessage("@red@<shad=200>This player is already in your slayer party.");
			return;
		}

		if (recruit.getslayerparty() != null) {
			slayerLeader.sendMessage("@red@<shad=200>" + recruit.getUsername() + " must leave their current slayer party first.");
			return;
		}

		recruit.setslayerparty(this);
		slayerMemebers.add(recruit);
		slayerStatus.put(recruit, false);

		sendMessageToMembers("@blu@<shad=200>" + recruit.getUsername() + " was added to your slayer party!", false);

		slayerLeader.sendMessage("@blu@<shad=200>You added " + recruit.getUsername() + " to your slayer party!");
	}

	public void sendMessageToMembers(String message, boolean notifyLeader) {
		for (Player member : slayerMemebers) {
			member.sendMessage(message);
		}
		if (notifyLeader)
			slayerLeader.sendMessage(message);
	}

	public Player getLeader() {
		return slayerLeader;
	}

	public ArrayList<Player> getMembers() {
		return slayerMemebers;
	}

	// called if owner leaves
	public void disband() {
		ArrayList<Player> oldList = new ArrayList<Player>();
		for (Player p : slayerMemebers) {
			oldList.add(p);
		}
		for (Player member : oldList) {
			if (member != null) {
				if (slayerMemebers.contains(member)) {
					slayerMemebers.remove(member);
					if (member.getRegionInstance() != null) {
						member.setRegionInstance(null);
						member.moveTo(GameSettings.DEFAULT_POSITION);
					}
				}
			}
		}
		if (currentSlayer != null)
			currentSlayer.destroyNpcs();

		for (Player p : World.getPlayers()) {
			if (p != null) {
				if (p.getslayerpartyInvites().size() > 0) {
					p.removeslayerpartyInvite(this);
				}
			}
		}
	}

	public void removeMember(Player member) {
		if (member == null)
			return;

		member.setslayerparty(null);

		if (slayerMemebers.contains(member))
			slayerMemebers.remove(member);

		if (defeatedMembers.contains(member))
			defeatedMembers.remove(member);

		if (slayerStatus.containsKey(member))
			slayerStatus.remove(member);

		sendMessageToMembers("@red@<shad=200>" + member.getUsername() + " was removed from your slayer party.", false);

		slayerLeader.sendMessage("@blu@<shad=200>You removed " + member.getUsername() + " from your slayer party.");
	}
}
