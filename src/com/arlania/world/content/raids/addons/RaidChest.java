package com.arlania.world.content.raids.addons;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.arlania.model.GameObject;
import com.arlania.model.Item;
import com.arlania.model.Position;
import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.content.raids.InstancedRaid;
import com.arlania.world.content.raids.RaidParty;
import com.arlania.world.entity.impl.player.Player;

public class RaidChest extends GameObject {

	private ConcurrentHashMap<Player, Boolean> claims = new ConcurrentHashMap<Player, Boolean>();
	private Item[] rewards;
	private int range;
	private double percent;
	private InstancedRaid raid;
	
	public RaidChest(int id, Position position) {
		super(id, position);
	}

	public InstancedRaid getRaid() {
		return raid;
	}
	
	public void configure(RaidParty party, Item[] rewards) {
		raid = party.getCurrentRaid();
		claims.put(party.getLeader(), false);
		for (Player p : party.getMembers()) {
			claims.put(p, false);
		}
		this.rewards = rewards;
	}

	public void configureWithPercent(RaidParty party, Item[] rewards, int range, double percent) {
		raid = party.getCurrentRaid();
		claims.put(party.getLeader(), false);
		this.range = range;
		this.percent = percent;
		for (Player p : party.getMembers()) {
			claims.put(p, false);
		}
		this.rewards = rewards;
	}

	public void claimRewardWithPercent(Player player) {
		if (!claims.get(player)) {
			claims.put(player, true);
			player.getInventory().add(new Item(13591, Misc.inclusiveRandom(1, 2)));
			for (Item i : rewards) {
				double random = Misc.getRandom(range);
				if (random >= percent) {
					System.out.println("random(win): "+ random);
					player.getInventory().add(i.getId(), i.getAmount());
					player.sendMessage(
							"@or2@You got lucky and received x" + i.getAmount() + " " + i.getDefinition().getName() + "!");
				} else {
					System.out.println("random(lose): "+ random);
				}
			}
			if (checkForLastPerson()) {
				World.deregister(this);
				player.getRaidParty().succeededRaid();
			}
		} else {
			player.sendMessage("You have already claimed your reward from this chest.");
			player.sendMessage("You will automatically be teleported home when the last person clicks the chest.");
		}
	}

	public void claimReward(Player player) {
		if (!claims.get(player)) {
			player.sendMessage("Claiming reward.");
			claims.put(player, true);
			if (Misc.inclusiveRandom(0, 100) > 95) {
				Item reward = rewards[Misc.inclusiveRandom(rewards.length - 1)];
				player.getInventory().add(reward);
				player.getRaidParty().sendMessageToMembers("@blu@" + player.getUsername() + " has received x"
						+ reward.getAmount() + " " + reward.getDefinition().getName() + " from the Raid 2 chest!",
						true);
			} else {
				player.sendMessage("You got unlucky and only received some cash and tickets..");
			}
			if (checkForLastPerson()) {
				World.deregister(this);
				player.getRaidParty().succeededRaid();
			}
		} else {
			player.sendMessage("You have already claimed your reward from this chest.");
			player.sendMessage("You will automatically be teleported home when the last person clicks the chest.");
		}
	}

	private boolean checkForLastPerson() {
		for (Entry<Player, Boolean> entry : claims.entrySet()) {
			if (!entry.getValue())
				return false;
		}
		return true;
	}
}
