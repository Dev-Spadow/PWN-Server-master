package com.arlania.net.packet.impl;

import com.arlania.model.Flag;
import com.arlania.model.ChatMessage.Message;
import com.arlania.net.packet.Packet;
import com.arlania.net.packet.PacketListener;
import com.arlania.world.content.PlayerPunishment;
import com.arlania.world.entity.impl.player.Player;

/**
 * This packet listener manages the spoken text by a player.
 * 
 * @author relex lawl
 */

public class ChatPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int effects = packet.readUnsignedByteS();
		int color = packet.readUnsignedByteS();
		int size = packet.getSize();
		byte[] text = packet.readReversedBytesA(size);
		
		if(PlayerPunishment.muted(player.getUsername()) || PlayerPunishment.IPMuted(player.getHostAddress())) {
			player.getPacketSender().sendMessage("You are muted and cannot chat.");
			return;
		}
		
		player.getChatMessages().set(new Message(color, effects, text));
		player.getUpdateFlag().flag(Flag.CHAT);
	}

}
