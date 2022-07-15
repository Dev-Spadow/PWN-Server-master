package com.arlania.engine.task.impl;

import com.arlania.engine.task.Task;
import com.arlania.model.Locations;
import com.arlania.model.Position;
import com.arlania.util.RandomUtility;
import com.arlania.world.World;
import com.arlania.world.content.skill.impl.hunter.Hunter;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

public class NPCRespawnTask extends Task {

	private Player killer;

	public NPCRespawnTask(NPC npc, int respawn, Player killer) {
		super(respawn);
		this.npc = npc;
		this.killer = killer;
	}

	final NPC npc;

	@Override
	public void execute() {

		NPC npc_ = new NPC(npc.getId(), npc.getDefaultPosition());

		if (npc != null) {

			if (npc.isInstancedNPC()) {
				if (killer != null) {
					if (killer.getRegionInstance() == null) {
						stop();
						return;
					}
				}
			}
		}

		if (killer != null && killer.getLocation() != null && (killer.getLocation()  == Locations.Location.INSTANCE1) && killer.getCurrentInstanceAmount() == -1) {
			World.deregister(npc_);
			stop();
			return;
		}

		if (npc_.isKeyRoomNpc()) {
			
			if (killer == null) {
				stop();
				return;
			}

			if (killer != null) {
				if (killer.getKeyRoom() == null) {
					stop();
					return;
				}
				if (killer.getKeyRoom() != null) {

					if (killer.getKeyRoom().count >= 4) {
						stop();
						return;
					}
					killer.getKeyRoom().count++;
				}
			}
		}

		npc_.getMovementCoordinator().setCoordinator(npc.getMovementCoordinator().getCoordinator());

		if (npc_.getId() == 8022 || npc_.getId() == 8028) { // Desospan, respawn
															// at random
															// locations
			npc_.moveTo(new Position(2595 + RandomUtility.getRandom(12), 4772 + RandomUtility.getRandom(8)));
		} else if (npc_.getId() > 5070 && npc_.getId() < 5081) {
			Hunter.HUNTER_NPC_LIST.add(npc_);
		}
		if (killer != null) {
			if (killer.getRegionInstance() != null) {
				if ((killer.getLocation()  == Locations.Location.INSTANCE1 && npc_.getLocation() == killer.getLocation())) {
					killer.getRegionInstance().getNpcsList().add(npc_);
				}
			}
		}

		World.register(npc_);
		stop();
	}

}
