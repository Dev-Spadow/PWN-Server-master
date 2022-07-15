package com.arlania.world.content.slayer;

import com.arlania.model.Position;
import com.arlania.world.entity.impl.npc.NPC;

public class slayerNPC2 extends NPC {

	private int stageRequiredToAttack = 0;
	
	public slayerNPC2(int id, Position position) {
		super(id, position);
	}

	public void setStageRequiredtoAttack(int stage) {
		this.stageRequiredToAttack = stage;
	}
	
	public int getStageRequiredToAttack() {
		return this.stageRequiredToAttack;
	}
}
