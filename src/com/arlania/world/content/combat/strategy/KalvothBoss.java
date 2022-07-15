package com.arlania.world.content.combat.strategy;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.Animation;
import com.arlania.model.Graphic;
import com.arlania.model.Locations;
import com.arlania.model.Projectile;
import com.arlania.util.Misc;
import com.arlania.world.content.combat.CombatContainer;
import com.arlania.world.content.combat.CombatType;
import com.arlania.world.entity.impl.Character;
import com.arlania.world.entity.impl.npc.NPC;

public class KalvothBoss implements CombatStrategy {

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	}
	
	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		NPC Kalvoth = (NPC)entity;
		if(victim.getConstitution() <= 0 || victim.getConstitution() <= 0) {
			return true;
		}
		if(Kalvoth.getConstitution() <= 50000 && !Kalvoth.hasHealed()) {
			Kalvoth.performAnimation(anim1);
			Kalvoth.performGraphic(gfx1);
			Kalvoth.setConstitution(Kalvoth.getConstitution() + Misc.getRandom(25000));
			Kalvoth.setHealed(true);
		}
		if(Kalvoth.isChargingAttack()) {
			return true;
		}
		int random = Misc.getRandom(10);
		if(random <= 8 && Locations.goodDistance(Kalvoth.getPosition().getX(), Kalvoth.getPosition().getY(), victim.getPosition().getX(), victim.getPosition().getY(), 3)) {
			Kalvoth.performAnimation(anim2);
			Kalvoth.getCombatBuilder().setContainer(new CombatContainer(Kalvoth, victim, 1, 2, CombatType.MELEE, true));
		} else if(random <= 4 || !Locations.goodDistance(Kalvoth.getPosition().getX(), Kalvoth.getPosition().getY(), victim.getPosition().getX(), victim.getPosition().getY(), 14)) {
			Kalvoth.getCombatBuilder().setContainer(new CombatContainer(Kalvoth, victim, 1, 6, CombatType.MAGIC, true));
			Kalvoth.performAnimation(anim3);
			Kalvoth.performGraphic(gfx3);
			Kalvoth.setChargingAttack(true);
			TaskManager.submit(new Task(2, Kalvoth, false) {
				int tick = 0;
				@Override
				public void execute() {
					switch(tick) {
					case 1:
						new Projectile(Kalvoth, victim, gfx5.getId(), 44, 3, 43, 31, 0).sendProjectile();
						Kalvoth.setChargingAttack(false);
						stop();
						break;
					}
					tick++;
				}
			});
		} else {
			Kalvoth.getCombatBuilder().setContainer(new CombatContainer(Kalvoth, victim, 1, 5, CombatType.RANGED, true));
			Kalvoth.performAnimation(anim4);
			Kalvoth.performGraphic(gfx2);
			Kalvoth.setChargingAttack(true);
			TaskManager.submit(new Task(2, Kalvoth, false) {
				@Override
				public void execute() {
					victim.performGraphic(gfx4);
					Kalvoth.setChargingAttack(false);
					stop();
				}
			});
		}
		return true;
	}
	
	
	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 10;
	}

	private static final Animation anim1 = new Animation(811);
	private static final Animation anim2 = new Animation(811);
	private static final Animation anim3 = new Animation(811);
	private static final Animation anim4 = new Animation(811);
	private static final Graphic gfx1 = new Graphic(1369);
	private static final Graphic gfx2 = new Graphic(1587);
	private static final Graphic gfx3 = new Graphic(1369);
	private static final Graphic gfx4 = new Graphic(1587);
	private static final Graphic gfx5 = new Graphic(1369);
	
	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}
