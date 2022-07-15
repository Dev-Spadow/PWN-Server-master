package com.arlania.world.content.combat.strategy;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.Animation;
import com.arlania.model.Graphic;
import com.arlania.model.GraphicHeight;
import com.arlania.util.RandomUtility;
import com.arlania.world.content.combat.CombatContainer;
import com.arlania.world.content.combat.CombatFactory;
import com.arlania.world.content.combat.CombatType;
import com.arlania.world.content.combat.effect.CombatPoisonEffect;
import com.arlania.world.entity.impl.Character;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

import java.util.Random;

public class SecondStrategy implements CombatStrategy {
    @Override
    public boolean canAttack(Character entity, Character victim) {
        return true;
    }

    @Override
    public CombatContainer attack(Character entity, Character victim) {
        return null;
    }

    private final Random random = new Random();

    @Override
    public boolean customContainerAttack(Character entity, Character victim) {
        int n = random.nextInt(100);
        Player player = (Player) victim;
        NPC npc = (NPC) entity;
        npc.setChargingAttack(true);
        npc.getCombatBuilder()
            .setContainer(new CombatContainer(npc, player, 1, 2, CombatType.MELEE, true));
        npc.performAnimation(new Animation(npc.getDefinition().getAttackAnimation()));
        if (n > 65) {
            player.performGraphic(new Graphic(601, GraphicHeight.LOW));
            player.getMovementQueue().freeze(12);
            player.setPlayerLocked(true);
            player.sendMessage("Been stunned");
            TaskManager.submit(new Task(12, false) {
                @Override
                public void execute() {
                    System.out.println("Was locked = " + player.isPlayerLocked());
                    player.setPlayerLocked(false);
                    stop();
                }
            });
        } else {
            player.performGraphic(new Graphic(364));
            System.out.println("Been frozen");
            player.getMovementQueue().freeze(5);
        }
        int rX = RandomUtility.inclusiveRandom(-3, 3);
        int rY = RandomUtility.inclusiveRandom(-3, 3);
        int attackerX = player.getPosition().getX();
        int attackerY = player.getPosition().getY();
        int attackerZ = player.getPosition().getZ();
        npc.moveTo(attackerX + rX, attackerY + rY, attackerZ);
        return true;
    }

    @Override
    public int attackDelay(Character entity) {
        return entity.getAttackSpeed();
    }

    @Override
    public int attackDistance(Character entity) {
        return 15;
    }

    @Override
    public CombatType getCombatType() {
        return null;
    }
}
