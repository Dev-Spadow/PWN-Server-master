package com.arlania.net.packet;

import com.arlania.util.Misc;
import com.arlania.world.entity.impl.player.Player;

public class InterfaceInputPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		
		String message = Misc.readString(packet.getBuffer());
		
		if(message == null) {
			return;
		} else {
			//System.out.println("Message sent from client to server: " + message);
		}
		

	}

}
