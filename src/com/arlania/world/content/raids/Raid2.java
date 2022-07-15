package com.arlania.world.content.raids;

import com.arlania.model.Position;
import com.arlania.model.definitions.NpcDefinition;
import com.arlania.world.entity.impl.npc.NPC;

public class Raid2 extends InstancedRaid {

	public Raid2(Position defaultSpawn, String raidName, RaidParty raidParty) {
		super(defaultSpawn, raidName, raidParty);
	}

	/*
	 * Change npc positions / default spawns where players suppose to spawn each stage
	 * Then you'll have to change chest location in instancedraid
	 * Nothing else has to be changed like the height etc. InstancedRaid class handles it
	 */

	@Override //2411, 5113
	public void configureNpcs() {
		RaidNpc diglet = new RaidNpc(9912, new Position(3109, 3099));
		diglet.setStageRequiredtoAttack(0);
		addNpc(diglet);
		
		RaidNpc charmeleon = new RaidNpc(6357, new Position(2596, 3415));
		charmeleon.setStageRequiredtoAttack(1);
		addNpc(charmeleon);
		
		RaidNpc lucario = new RaidNpc(5922, new Position(2981, 3223));
		lucario.setStageRequiredtoAttack(2);
		addNpc(lucario);
		
		RaidNpc mewtwo = new RaidNpc(812, new Position(1960, 4763));
		mewtwo.setStageRequiredtoAttack(3);
		addNpc(mewtwo);
		
		RaidNpc groudon = new RaidNpc(201, new Position(2344, 4759));
		groudon.setStageRequiredtoAttack(4);
		addNpc(groudon);
		
		RaidNpc squirtle2 = new RaidNpc(8000, new Position(2468, 4756));
		squirtle2.setStageRequiredtoAttack(5);
		addNpc(squirtle2);
		
		RaidNpc charmeleon2 = new RaidNpc(1120, new Position(2920, 9690));
		charmeleon2.setStageRequiredtoAttack(6);
		addNpc(charmeleon2);
		
		RaidNpc lucario2 = new RaidNpc(1123, new Position(2660, 4512));
		lucario2.setStageRequiredtoAttack(7);
		addNpc(lucario2);
		
		RaidNpc mewtwo2 = new RaidNpc(4972, new Position(2013, 4439));
		mewtwo2.setStageRequiredtoAttack(8);
		addNpc(mewtwo2);
		
		RaidNpc riachu = new RaidNpc(6250, new Position(2783, 3282));
		riachu.setStageRequiredtoAttack(9);
		addNpc(riachu);
		
		int hpScale = 2;
		for(NPC n : getNpcs()) {
			if(n.getId() == 1234)
				hpScale += 4;
			
			if(n.getPosition().getY() == 3930) {
				hpScale += 4;
			}
			n.setConstitution((1 * hpScale) + n.getConstitution());
			n.setPoisonDamage(10);
			n.setChargingAttack(true);
		}
	}
	
	@Override
	public void nextLevel() {
		if(stage == 9) {
			victory();
			return;
		}
		stage++;
		getNpcs().get(stage).setChargingAttack(false);
		getRaidParty().sendMessageToMembers("@or2@Attack the " + getNpcs().get(stage).getDefinition().getName() + "!", true);
		switch(stage) {
		case 0: //(2411, 5113)
			setDefaultSpawn(3104, 3097);
			break;
		case 1:
			setDefaultSpawn(2596, 3415);
			break;
		case 2: 
			setDefaultSpawn(2981, 3222);
			break;
		case 3:
			setDefaultSpawn(1958, 4755);
			break;
		case 4: 
			setDefaultSpawn(2336, 4759);
			break;
		case 5:
			setDefaultSpawn(2471, 4762);
			break;
		case 6:
			setDefaultSpawn(2912, 9686);
			break;
		case 7:
			setDefaultSpawn(2663, 4520);
			break;
		case 8:
			setDefaultSpawn(2013, 4439);
			break;
		case 9:
			setDefaultSpawn(2783, 3282);
			break;
		}
		teleportAll();
	}
}