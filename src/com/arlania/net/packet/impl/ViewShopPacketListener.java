package com.arlania.net.packet.impl;

import com.arlania.engine.task.impl.WalkToTask;
import com.arlania.engine.task.impl.WalkToTask.FinalizedMovementTask;
import com.arlania.model.Locations;
import com.arlania.model.Locations.Location;
import com.arlania.net.packet.Packet;
import com.arlania.net.packet.PacketListener;
import com.arlania.world.World;
import com.arlania.world.content.pos.PlayerOwnedShop;
import com.arlania.world.entity.impl.player.Player;

public class ViewShopPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int index = (packet.readShort() & 0xFF);
		
		if (index < 0 || index > World.getPlayers().capacity())
			return;
		
		Player target = World.getPlayers().get(index);

		if (target == null)
			return;
		
		if(target.isMiniPlayer()) {
			return;
		}
		
		if(target.getPlayerOwnedShopManager().getMyShop() == null) {
			player.getPacketSender().sendMessage(target.getUsername() + " has no shop!");
			return;
		}
		
		player.setWalkToTask(
				new WalkToTask(player, target.getPosition(), target.getSize(), new FinalizedMovementTask() {
					@Override
					public void execute() {
						player.getPlayerOwnedShopManager().open();
						player.getPlayerOwnedShopManager().setCurrent(target.getPlayerOwnedShopManager().getMyShop());
						System.out.println("4352524352435423524352435");
						target.getPlayerOwnedShopManager().getMyShop().open(player);
					}
				}));
		
	}

	public static final int VIEW_SHOP_OPCODE = 190;
}
