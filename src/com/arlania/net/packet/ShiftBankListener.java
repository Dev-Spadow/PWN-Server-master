package com.arlania.net.packet;

import com.arlania.model.Item;
import com.arlania.model.container.ItemContainer;
import com.arlania.model.container.impl.Bank;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.world.entity.impl.player.Player;

public class ShiftBankListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {

		int itemId = packet.readShort();
		int slot = packet.readByte();
		int amount = player.getInventory().getAmountForSlot(slot);

		if (slot < 0 || slot >= player.getInventory().capacity()) {
			return;
		}

		Item clicked_item = player.getInventory().get(slot);

		if (clicked_item == null || clicked_item.getId() != itemId) { // what the fuck u looping for
			return;
		}

		//System.out.println("clicked_item= " + clicked_item.getDefinition().getName() + " amount= " + amount);

		if(clicked_item.getDefinition().isNoted())
			clicked_item.setId(Item.getUnNoted(clicked_item.getId()));

		int tab = Bank.getTabForItem(player, clicked_item.getId());
		ItemContainer container = player.getBank(tab).add(clicked_item);

		if (container != null) {
			//System.out.println("clicked item amount: " + clicked_item.getAmount());
			player.getInventory().delete(clicked_item);
            player.getPacketSender().sendMessage(clicked_item.getDefinition().getName() + " added to your bank.");
		}

	}

}
