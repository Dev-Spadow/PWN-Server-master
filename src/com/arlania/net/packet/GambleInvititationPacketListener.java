package com.arlania.net.packet;

import com.arlania.engine.task.impl.WalkToTask;
import com.arlania.engine.task.impl.WalkToTask.FinalizedMovementTask;
import com.arlania.model.Locations;
import com.arlania.model.Locations.Location;
import com.arlania.world.World;
import com.arlania.world.content.gamblinginterface.GamblingInterface;
import com.arlania.world.entity.impl.player.Player;

public class GambleInvititationPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getConstitution() <= 0)
			return;
		if (player.isTeleporting())
			return;
		player.getSkillManager().stopSkilling();
		if (player.getLocation() == Location.FIGHT_PITS) {
			player.getPacketSender().sendMessage("You cannot trade other players here.");
			return;
		}
		int index = packet.getOpcode() == GAMBLE_OPCODE ? (packet.readShort() & 0xFF) : packet.readLEShort();
		//system.out.println("Index from client: " + index);
		if (index < 0 || index > World.getPlayers().capacity())
			return;
		Player target = World.getPlayers().get(index);

		// System.out.println("Index: " + index);
		// System.out.println("Name: " + target.getUsername());
		if (target == null || !Locations.goodDistance(player.getPosition(), target.getPosition(), 13))
			return;
		player.setWalkToTask(
				new WalkToTask(player, target.getPosition(), target.getSize(), new FinalizedMovementTask() {
					@Override
					public void execute() {
					    if (target.getIndex() == player.getIndex()) {
					        return;
					    }
					    if(target.getGambling().inGamble()) {
					    	player.sendMessage("This player is busy at the moment.");
					    	return;
					    }
					    if(!player.getLocation().equals(Location.GAMBLING_ZONE) || !target.getLocation().equals(Location.GAMBLING_ZONE)) {
					    	return;
					    }
					  //  if (!player.getRights().isLegendaryDonator(player) || player.getTotalPlayTime() < 1000 * 60 * 60) {
					      //  player.sendMessage("@red@You need to be a legendary donator and have 1 hour(s) of playtime to gamble");
					      //  return;
					 //   }
					    player.getGambling().requestGamble(target);
					}
				}));
	}

	public static final int GAMBLE_OPCODE = 191;
	public static final int CHATBOX_GAMBLE_OPCODE = 193;
}
