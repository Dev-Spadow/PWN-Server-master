package com.arlania.net.packet;

import com.arlania.world.entity.impl.player.Player;

public class AddGambleItemPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		
		int itemId = packet.readShort();
		
		//system.out.println("Item id ffs: " + itemId);

	}

}
