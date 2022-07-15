package com.arlania.world.content.combat.strategy;

import com.arlania.model.Animation;
import com.arlania.model.Hit;
import com.arlania.model.Position;
import com.arlania.util.RandomUtility;
import com.arlania.world.World;
import com.arlania.world.content.combat.CombatContainer;
import com.arlania.world.content.combat.CombatType;
import com.arlania.world.entity.impl.Character;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.npc.NPCMovementCoordinator;
import com.arlania.world.entity.impl.player.Player;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

public class ThirdStrategy implements CombatStrategy {
    @Override
    public boolean canAttack(Character entity, Character victim) {
        return true;
    }

    @Override
    public CombatContainer attack(Character entity, Character victim) {
        return null;
    }

    public static List<NPC> minions = new ArrayList<>();
    private static boolean spawnedMinions = false;
    private static final int MINION_ID = 13;

    private final Random random = new Random();

    @Override
    public boolean customContainerAttack(Character entity, Character victim) {
        Player player = (Player) victim;
        NPC npc = (NPC) entity;
        npc.setChargingAttack(true);
        npc.getCombatBuilder()
            .setContainer(new CombatContainer(npc, player, 1, 2, CombatType.MELEE, true));
        npc.performAnimation(new Animation(npc.getDefinition().getAttackAnimation()));
        if (!spawnedMinions && npc.getConstitution() < npc.getDefaultConstitution() / 2) {
            System.out.println("Called");
            minions.add(new NPC(MINION_ID, new Position(entity.getPosition()
                .getX() - 1, entity.getPosition().getY())));
            minions.add(new NPC(MINION_ID, new Position(entity.getPosition()
                .getX() + 1, entity.getPosition().getY())));
            minions.add(new NPC(MINION_ID, new Position(entity.getPosition()
                .getX(), entity.getPosition().getY() - 1)));
            minions.forEach(World::register);
            spawnedMinions = true;
        }

        minions.forEach(minion -> {
            minion.setChargingAttack(true);
            minion.getCombatBuilder().attack(player);
            NPCMovementCoordinator.Coordinator coordinator = new NPCMovementCoordinator.Coordinator(true, 3);
            minion.getMovementCoordinator().setCoordinator(coordinator);
        });

        return true;
    }

    public static void handleMinionDeath(NPC npc) {
        if (npc.getId() == MINION_ID) {
            minions.remove(npc);
        }
    }

    public static void handleKill(Player player) {
        if (minions.isEmpty()) {
            return;
        }

        if (!player.getLocalPlayers().contains(player)) {
            player.dealDamage(new Hit(RandomUtility.random(100, 990)));
        }
        player.getLocalPlayers().forEach(local -> {
            local.dealDamage(new Hit(RandomUtility.random(100, 990)));
        });

        minions.forEach(World::deregister);
        spawnedMinions = false;
    }

    @Override
    public int attackDelay(Character entity) {
        return 2;
    }

    @Override
    public int attackDistance(Character entity) {
        return 15;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MELEE;
    }
    
}
