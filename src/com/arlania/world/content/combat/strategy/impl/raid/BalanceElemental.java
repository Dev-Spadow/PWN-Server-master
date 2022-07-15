package com.arlania.world.content.combat.strategy.impl.raid;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.Animation;
import com.arlania.model.Graphic;
import com.arlania.model.Locations;
import com.arlania.model.Position;
import com.arlania.model.Projectile;
import com.arlania.util.Misc;
import com.arlania.world.content.combat.CombatContainer;
import com.arlania.world.content.combat.CombatType;
import com.arlania.world.content.combat.strategy.CombatStrategy;
import com.arlania.world.entity.impl.Character;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

public class BalanceElemental implements CombatStrategy {

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
        NPC Balance = (NPC)entity;
        if(Balance.isChargingAttack() || victim.getConstitution() <= 0) {
            return true;
        }
        if(Misc.getRandom(15) <= 2){
            int hitAmount = 1;
            Balance.performGraphic(new Graphic(69));
            Balance.setConstitution(Balance.getConstitution() + hitAmount);
            Balance.forceChat("I am immortal!");
            ((Player)victim).getPacketSender().sendMessage("The Balance Elemental absorbs your attack, allowing him to heal himself.");
        }
        if(Locations.goodDistance(Balance.getPosition().copy(), victim.getPosition().copy(), 3) && Misc.getRandom(10) <= 3) {
            Balance.performAnimation(new Animation(Balance.getDefinition().getAttackAnimation()));
            Balance.getCombatBuilder().setContainer(new CombatContainer(Balance, victim, 1, 1, CombatType.MELEE, true));
            if(Misc.getRandom(5) <= 2) {
                Balance.forceChat("HAHAHAHA");
                Balance.performAnimation(new Animation(10692));
                Balance.performGraphic(new Graphic(1946));
                Balance.moveTo(new Position(victim.getPosition().getX(), victim.getPosition().getY(), 4));
                victim.performAnimation(new Animation(534));
                ((Player)victim).getPacketSender().sendMessage("You've been stunned");
            }
        } else {
            int ran = Misc.random(2);
            if(ran == 1) {
                Balance.setChargingAttack(true);
                Balance.forceChat("DIE");
                Balance.performAnimation(new Animation(10680));
                Balance.performGraphic(new Graphic(1939));
                Balance.getCombatBuilder().setContainer(new CombatContainer(Balance, victim, 1, 3, CombatType.MAGIC, true));
                TaskManager.submit(new Task(1, Balance, false) {
                    int tick = 0;

                    @Override
                    public void execute() {
                        if (tick == 0) {
                            new Projectile(Balance, victim, 1940, 44, 3, 41, 31, 0).sendProjectile();
                        } else if (tick == 1) {
                            Balance.setChargingAttack(false);
                            stop();
                        }
                        tick++;
                    }
                });
            } else if (ran == 2) {
                Balance.setChargingAttack(true);
                Balance.forceChat("GUESS WHO?");
                Balance.performAnimation(new Animation(10675));
                Balance.performGraphic(new Graphic(1936));
                Balance.getCombatBuilder().setContainer(new CombatContainer(Balance, victim, 1, 3, CombatType.RANGED, true));
                TaskManager.submit(new Task(1, Balance, false) {
                    int tick = 0;

                    @Override
                    public void execute() {
                        if (tick == 0) {
                            new Projectile(Balance, victim, 1937, 44, 3, 41, 31, 0).sendProjectile();
                        } else if (tick == 1) {
                            Balance.setChargingAttack(false);
                            stop();
                        }
                        tick++;
                    }
                });
            }
        }
        return true;
    }

    @Override
    public int attackDelay(Character entity) {
        return entity.getAttackSpeed();
    }

    @Override
    public int attackDistance(Character entity) {
        return 5;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MIXED;
    }
}