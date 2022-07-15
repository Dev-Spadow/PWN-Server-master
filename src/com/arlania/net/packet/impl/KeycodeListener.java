package com.arlania.net.packet.impl;

import com.arlania.GameSettings;
import com.arlania.model.Locations.Location;
import com.arlania.net.packet.Packet;
import com.arlania.net.packet.PacketListener;
import com.arlania.world.content.transportation.TeleportHandler;
import com.arlania.world.entity.impl.player.Player;

/**
 * nice
 */
public class KeycodeListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {

		int key = packet.readShort();
		System.out.println("Hello the key sent from client to server is: " + key);

		switch (key) {

		case 66:
			if (player.getRights().isHighDonator() || player.getRights().isStaff()) {
				if (player.getLocation() == Location.DUNGEONEERING || player.inLMS || player.inLMSLobby
						|| player.getLocation() == Location.FIGHT_PITS || player.getLocation() == Location.FIGHT_CAVES
						|| player.getLocation() == Location.DUEL_ARENA
						|| player.getLocation() == Location.RECIPE_FOR_DISASTER
						|| player.getLocation() == Location.WILDERNESS
						|| player.getLocation() == Location.FREE_FOR_ALL_ARENA
						|| player.getLocation() == Location.FREE_FOR_ALL_WAIT) {
					player.getPacketSender().sendMessage("You can not open your bank here!");
					return;
				}
				player.getBank(player.getCurrentBankTab()).open(true);
			}
			break;
		case 69:
			player.getPlayerOwnedShopManager().open(); // enjoy, pm me on discord if there are any bugs.
			break;
		case 72:
			TeleportHandler.teleportPlayer(player, GameSettings.DEFAULT_POSITION,
					player.getSpellbook().getTeleportType());
			break;

		}

		/*
		 * if(keyCode == 76) { if (!player.isBanking() || player.getInterfaceId() !=
		 * 5292) return; Bank.depositItems(player, player.getInventory(), false); }
		 */
	}

}
