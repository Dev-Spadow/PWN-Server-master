package com.arlania.world.content.slayer;

import com.arlania.model.Position;
import com.arlania.world.entity.impl.npc.NPC;

public class slayer1 extends InstancedRaid {

	public slayer1(Position defaultSpawn, String raidName, slayerparty raidParty) {
		super(defaultSpawn, raidName, raidParty);
	}


	@Override
	public void configureNpcs() {
		addNpc(new NPC(3224, new Position(2878, 3372))); //YIPPIE
		addNpc(new NPC(33, new Position(2836, 3329))); // link
	}
	
	@Override
	public void nextLevel() {
		stage++;
		switch(stage) {
		case 0: //Yippie
			setDefaultSpawn(2972, 2542);//first skill room
			break;
		case 1: 
			setDefaultSpawn(2880, 3353);//second skill room
			break;
		case 2: //reward room
			setDefaultSpawn(2838, 3305);// key room
			break;
		}
		teleportAll();
	}
}
