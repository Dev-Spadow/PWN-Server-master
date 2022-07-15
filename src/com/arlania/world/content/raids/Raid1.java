package com.arlania.world.content.raids;

import com.arlania.model.Position;
import com.arlania.world.entity.impl.npc.NPC;

public class Raid1 extends InstancedRaid {

	public Raid1(Position defaultSpawn, String raidName, RaidParty raidParty) {
		super(defaultSpawn, raidName, raidParty);
	}

	//author Milo

	@Override
	public void configureNpcs() {
		addNpc(new NPC(9176, new Position(2152, 4966))); //skeletal horror
		
		addNpc(new NPC(9912, new Position(2084, 4966))); // ugly ant man
		
		addNpc(new NPC(33, new Position(2024, 4965))); // link
	
	
	}
	
	@Override
	public void nextLevel() {
		stage++;
		switch(stage) {
		case 0: 
			setDefaultSpawn(2125, 4966);//first boss  room
			break;
		case 1: 
			setDefaultSpawn(2065, 4966);//second boss room
			break;
		case 2: 
			setDefaultSpawn(2002, 4966);//3rd boss room
			break;
		
		
		}
		teleportAll();
	}
}
