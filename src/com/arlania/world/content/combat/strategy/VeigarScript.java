package com.arlania.world.content.combat.strategy;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.engine.task.impl.PlayerDeathTask;
import com.arlania.model.*;
import com.arlania.world.World;
import com.arlania.world.content.combat.CombatContainer;
import com.arlania.world.content.combat.CombatType;
import com.arlania.world.content.combat.prayer.CurseHandler;
import com.arlania.world.content.combat.prayer.PrayerHandler;
import com.arlania.world.entity.impl.Character;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.npc.NPCMovementCoordinator;
import com.arlania.world.entity.impl.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class VeigarScript implements CombatStrategy {
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
    private static final int MINION_ID = 11364;
    ;
    private final Random random = new Random();
    private List<Position> chosenTiles = new ArrayList<>();

    @Override
    public boolean customContainerAttack(Character entity, Character victim) {
        Player player = (Player) victim;
        NPC npc = (NPC) entity;
        if (player.isDying() || player.getConstitution() <= 0 || !player.getPosition()
            .isWithinDistance(npc.getPosition(), 15)) {
            npc.getCombatBuilder().reset(true);
            return false;
        }
        if (npc.getCombatBuilder().getStrategy() == null) {
            return false;
        }
        npc.setChargingAttack(true);
        npc.getCombatBuilder().attack(player);
        npc.performAnimation(new Animation(npc.getDefinition().getAttackAnimation()));
        handlePrayerRemoval(player, npc);
        handleTileAttack(player, npc);
        if (!spawnedMinions && npc.getConstitution() < 200000000) {
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

    private void handleTileAttack(Player player, NPC npc) {
        if (shouldTileAttack()) {
            setupTileAttack(player);
            TaskManager.submit(new Task(1) {
                @Override
                protected void execute() {
                    executeTileAttack(player, npc);
                    stop();
                }
            });
        }
    }

    private void executeTileAttack(Player player, NPC npc) {
        chosenTiles.forEach(chosenTile -> {
            if (!player.getLocalPlayers().contains(player)) {
                tileAttack(player, npc, chosenTile);
            }
            player.getLocalPlayers().forEach(local -> {
                tileAttack(local, npc, chosenTile);
            });
        });
        chosenTiles.clear();
    }

    private void tileAttack(Player player, NPC npc, Position chosenTile) {
        TaskManager.submit(new Task(3, false) {
            int n = random.nextInt(100);
            protected void execute() {
                if (n > 0 & n < 43) {
                    npc.forceChat("Got a present for ya!");
                    player.dealDamage(new Hit(133, Hitmask.RED, CombatIcon.MAGIC));
            		npc.performAnimation(new Animation(422));
            		new Projectile(npc, player, 1048, 44, 3, 43, 31, 0).sendProjectile();
              }
                
                 if (n > 44 & n < 75) {
                    npc.forceChat("I'll make weapons from your bones!");
            		new Projectile(npc, player, 1051, 44, 3, 43, 31, 0).sendProjectile();
            		npc.performAnimation(new Animation(422));
                    player.dealDamage(new Hit(178, Hitmask.RED, CombatIcon.MAGIC));
                    System.out.println("Tile was " + player.getPosition() + " | " + chosenTile);
                }
                
                 if (n > 75) {
                    npc.forceChat("I think I will knock you all down.");
            		npc.performAnimation(new Animation(422));
                if (player.getPosition().equals(chosenTile)) {
            		new Projectile(npc, player, 1049, 44, 3, 43, 31, 0).sendProjectile();
                    player.dealDamage(new Hit(353, Hitmask.RED, CombatIcon.MAGIC));
                    player.performGraphic(new Graphic(1050, GraphicHeight.HIGH));
                    System.out.println("Tile was " + player.getPosition() + " | " + chosenTile);
                }  
              }
                
                stop();
            }
                
        });
    }

    private boolean shouldTileAttack() {
        return chosenTiles.isEmpty() && random.nextInt(100) > 35; // 5% chance
    }

    private void setupTileAttack(Player player) {
        List<Player> players = pickNRandom(player.getLocalPlayers(), 4);
        if (!player.getLocalPlayers().contains(player)) {
            players.add(player);
        }
        chosenTiles = players.stream().map(Player::getPosition).collect(Collectors.toList());
    }

    private void handlePrayerRemoval(Player player, NPC npc) {
        int n = random.nextInt(100);
        if (n > 95) {
            PrayerHandler.deactivateAll(player);
            CurseHandler.deactivateAll(player);
            player.getLocalPlayers().forEach(local -> {
                PrayerHandler.deactivateAll(local);
                CurseHandler.deactivateAll(local);
            });
        }
    }

    private List<Player> pickNRandom(List<Player> lst, int n) {
        List<Player> copy = new ArrayList<>(lst);
        Collections.shuffle(copy);
        return n > copy.size() ? copy.subList(0, copy.size()) : copy.subList(0, n);
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
        return CombatType.MAGIC;
    }
}




