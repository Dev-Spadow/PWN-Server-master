package com.arlania.world.content.bosses;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.Graphic;
import com.arlania.model.Locations.Location;
import com.arlania.model.Position;
import com.arlania.model.RegionInstance;
import com.arlania.model.RegionInstance.RegionInstanceType;
import com.arlania.world.World;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

public class Warmonger {

	@SuppressWarnings("unused")
	private static final Graphic gfx1 = new Graphic(1028);

	public static void start(Player p) {
		p.setRegionInstance(new RegionInstance(p, RegionInstanceType.WARMONGER));

		int H = p.getIndex() * 4;
		NPC war1 = new NPC(12841, new Position(2588, 4440, H));
		NPC war = new NPC(12840, new Position(2598, 4440, H));
		TaskManager.submit(new Task(1, p, false) {
			int tick = 0;

			@Override
			public void execute() {
				switch (tick) {
				case 7:
					World.register(p, war);
					p.getRegionInstance().getNpcsList().add(war);
					war.setPositionToFace(new Position(0, 4440));
					break;
				case 8:
					war.getMovementQueue().addStep(new Position(2588, 4440, H));
					break;
				case 20:
					World.register(p, war1);
					p.getRegionInstance().getNpcsList().add(war1);
					war1.setPositionToFace(new Position(1694, 4511));
					World.deregister(war);
					war1.setEntityInteraction(p);
					war1.getCombatBuilder().attack(p);
					break;
				}
				if (p.getLocation() != Location.WARMONGER && tick >= 5) {
					World.deregister(war);
					World.deregister(war1);
					stop();
				}
				tick++;
			}
		});
	}
}