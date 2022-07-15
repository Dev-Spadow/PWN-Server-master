package com.arlania.net.packet;

import com.arlania.model.Flag;
import com.arlania.world.entity.impl.player.Player;

public class TransformToNpcPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		
		int npcId = packet.readShort();
		
		//system.out.println("Received " + npcId + " from client, and now transforming");
		
		player.setNpcTransformationId(npcId);
		player.getUpdateFlag().flag(Flag.APPEARANCE);

	}

}
