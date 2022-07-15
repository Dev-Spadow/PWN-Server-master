package com.arlania.world.content.combat.strategy.impl;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.Animation;
import com.arlania.model.CombatIcon;
import com.arlania.model.Graphic;
import com.arlania.model.Hit;
import com.arlania.model.Hitmask;
import com.arlania.util.Misc;
import com.arlania.world.content.combat.CombatContainer;
import com.arlania.world.content.combat.CombatType;
import com.arlania.world.content.combat.strategy.CombatStrategy;
import com.arlania.world.entity.impl.Character;
import com.arlania.world.entity.impl.npc.NPC;

public class SuperiorQueen implements CombatStrategy {

	@Override
	public boolean canAttack(Character entity, Character victim) {
		
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		
		return null;
	}
	
	int tick = 27;

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		
		NPC npc = (NPC) entity;
		
		TaskManager.submit(new Task(1, npc, false) {
			@Override
			protected void execute() {
				tick -= 1;
				if(tick == 26) {
			        npc.performAnimation(new Animation(npc.getDefinition().getAttackAnimation()));
					npc.forceChat("Who dares wake me from my passionate dreams");
				}
				if(tick == 20) {
			        npc.performAnimation(new Animation(npc.getDefinition().getAttackAnimation()));
					npc.forceChat("I WILL MAKE YOU PERISH");
				}
				if(tick == 15) {
			        npc.performAnimation(new Animation(npc.getDefinition().getAttackAnimation()));
					npc.forceChat("Only the Almighty have defeated me");
				}
				if(tick == 10) {
			        npc.performAnimation(new Animation(npc.getDefinition().getAttackAnimation()));
					npc.forceChat("You think you have what it takes");
				}
				if(tick == 5) {
			        npc.performAnimation(new Animation(npc.getDefinition().getAttackAnimation()));
					npc.forceChat("This is not the end, I will return one day even stronger");
				}

				if(tick == 0 || victim.getConstitution() > 1) {
			        npc.performAnimation(new Animation(npc.getDefinition().getAttackAnimation()));
				}
				npc.setChargingAttack(false).getCombatBuilder().setAttackTimer(npc.getDefinition().getAttackSpeed() - 1);
				if(tick == 0 || victim.getConstitution() < 1) {
					stop();
				}
				
			}
		});
		
		
		return true;
	}

	@Override
	public int attackDelay(Character entity) {
		
		return 6;
	}

	@Override
	public int attackDistance(Character entity) {
		
		return 4;
	}

	@Override
	public CombatType getCombatType() {
        return CombatType.MELEE;
	}

}
