package com.arlania.net.packet.impl;

import com.arlania.model.CombatIcon;
import com.arlania.model.Graphic;
import com.arlania.model.GroundItem;
import com.arlania.model.Hit;
import com.arlania.model.Hitmask;
import com.arlania.model.Item;
import com.arlania.model.input.impl.EnterAmountToCheck;
import com.arlania.net.packet.Packet;
import com.arlania.net.packet.PacketListener;
import com.arlania.util.Misc;
import com.arlania.world.content.PlayerLogs;
import com.arlania.world.content.Sounds;
import com.arlania.world.content.PlayerPunishment.Jail;
import com.arlania.world.content.Sounds.Sound;
import com.arlania.world.content.skill.impl.dungeoneering.ItemBinding;
import com.arlania.world.entity.impl.GroundItemManager;
import com.arlania.world.entity.impl.player.Player;

//import net.dv8tion.jda.api.entities.TextChannel;

/**
 * This packet listener is called when a player drops an item they
 * have placed in their inventory.
 * 
 * @author relex lawl
 */

public class DropItemPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int id = packet.readUnsignedShortA();
		@SuppressWarnings("unused")
		int interfaceIndex = packet.readUnsignedShort();
		int itemSlot = packet.readUnsignedShortA();
		Item item = player.getInventory().getItems()[itemSlot];
				destroyItemInterface(player, item);
}

	public static void destroyItemInterface(Player player, Item item) {//Destroy item created by Remco
		player.setUntradeableDropItem(item);
		String[][] info = {//The info the dialogue gives
				{ "Are you sure you want to discard this item?", "14174" },
				{ "Yes.", "14175" }, { "No.", "14176" }, { "", "14177" },
				{"This item will vanish once it hits the floor.", "14182" }, {"You cannot get it back if discarded.", "14183" },
				{ item.getDefinition().getName(), "14184" } };
		player.getPacketSender().sendItemOnInterface(14171, item.getId(), 0, item.getAmount());
		for (int i = 0; i < info.length; i++)
			player.getPacketSender().sendString(Integer.parseInt(info[i][1]), info[i][0]);
		player.getPacketSender().sendChatboxInterface(14170);
	}
}
