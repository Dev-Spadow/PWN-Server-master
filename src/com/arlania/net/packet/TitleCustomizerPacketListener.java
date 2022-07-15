package com.arlania.net.packet;

import org.jboss.netty.buffer.ChannelBuffer;

import com.arlania.util.Misc;
import com.arlania.world.entity.impl.player.Player;

public class TitleCustomizerPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		//System.out.println("ITS A PACKET!!!!!!!!!!");
		//int length = packet.readUnsignedByte();
		
		
		String message = Misc.readString(packet.getBuffer());
		
		
		/** Gets requested bytes from the buffer client > server **/
		
		
		//system.out.println("String is: " + message);

	}

}
