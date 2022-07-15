package com.arlania.net.packet.impl;

import com.arlania.net.packet.Packet;
import com.arlania.net.packet.PacketListener;
import com.arlania.world.content.dialogue.DialogueManager;
import com.arlania.world.content.keepsake.KeepSake;
import com.arlania.world.entity.impl.player.Player;

/**
 * This packet listener handles player's mouse click on the
 * "Click here to continue" option, etc.
 * 
 * @author relex lawl
 */

public class DialoguePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		switch (packet.getOpcode()) {
		case DIALOGUE_OPCODE:
			if(!player.getActionInterface().equals("")) {
				switch(player.getActionInterface()) {
					case "keepsakeItem":
						KeepSake.sendOptionsTitle(player, "Do you really wanna keep sake this item?", "Yes", "No");
						player.setActionInterface("keepsakeItem_1");
						break;
					default:
						player.getPacketSender().closeChatInterface();
						break;
				}
				return;
			}
			DialogueManager.next(player);
			break;
		}
	}

	public static final int DIALOGUE_OPCODE = 40;
}
