package com.arlania.model.input.impl;

import com.arlania.model.input.Input;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class EnterRaidParty extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		player.getPacketSender().sendInterfaceRemoval();
		if (syntax.length() <= 1) {
			player.getPacketSender().sendMessage("@red@Invalid syntax entered.");
			return;
		}
		Player raidLeader = World.getPlayerByName(syntax);
		if(raidLeader == player) {
			player.sendMessage("@red@You cannot join yourself.");
			return;
		}
		if (raidLeader == null) {
			player.sendMessage("@red@That player is not online.");
			return;
		}
		if (raidLeader.getRaidParty() == null) {
			player.sendMessage("@red@That player does not have a raid party to join.");
			return;
		}
		if (raidLeader.getRaidParty().getLeader() != raidLeader) {
			player.sendMessage("@red@That player is not the leader of the raid party.");
			return;
		}
		if(raidLeader.getRaidParty().getMembers().size() == 4) {
			player.sendMessage("@red@" + raidLeader.getUsername() + "'s party is currently full.");
			return;
		}
		if(!player.getRaidPartyInvites().contains(raidLeader.getRaidParty()) && raidLeader.getRaidParty().getIsRaiding(raidLeader)) {
			player.sendMessage("@red@That player is currently raiding. Please try again later.");
		}
		if (player.getRaidPartyInvites().contains(raidLeader.getRaidParty())) {
			player.sendMessage("@red@You already requested to join " + raidLeader.getUsername() + "'s raid party.");
			return;
		}
		if (raidLeader.getRelations().isFriendWith(player.getUsername())) {
			raidLeader.sendMessage("@blu@" + player.getUsername() + " has requested to join your raid party.");
			player.addPendingRaidParty(raidLeader.getRaidParty());
		} else {
			player.sendMessage("@red@" + raidLeader.getUsername() + " must be friends with you to request an invite.");
			return;
		}
	}
}
