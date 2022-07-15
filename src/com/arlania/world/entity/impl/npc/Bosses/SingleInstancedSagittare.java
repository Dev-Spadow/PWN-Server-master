package com.arlania.world.entity.impl.npc.Bosses;

import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;


public class SingleInstancedSagittare extends SingleInstancedArea {
	
	public SingleInstancedSagittare(Player player, Boundary boundary, int height) {
		super(player, boundary, height);
	}
	
	@Override
	public void onDispose() {
		Sagittare sagittare = player.getSagittareEvent();
		if (sagittare.getNpc() != null) {
			World.deregister(sagittare.getNpc());
		}
	}

}
