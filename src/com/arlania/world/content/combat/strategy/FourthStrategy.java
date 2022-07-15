package com.arlania.world.content.combat.strategy;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.*;
import com.arlania.world.content.combat.CombatContainer;
import com.arlania.world.content.combat.CombatType;
import com.arlania.world.entity.impl.Character;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class FourthStrategy implements CombatStrategy {
    @Override
    public boolean canAttack(Character entity, Character victim) {
        return true;
    }

    @Override
    public CombatContainer attack(Character entity, Character victim) {
        return null;
    }

    private final Random random = new Random();
    private List<Position> chosenTiles = new ArrayList<>();

    public static int iconIndex = 0;

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
        iconIndex = (iconIndex + 1) % 3;
        player.getPacketSender().sendNpcPrayerIcon(npc.getId(), iconIndex);
        npc.setChargingAttack(true);
        npc.getCombatBuilder().attack(player);
        npc.performAnimation(new Animation(npc.getDefinition().getAttackAnimation()));
        handleTileAttack(player, npc);
        player.getPacketSender().sendNpcPrayerIcon(npc.getId(), random.nextInt(3));
        return true;
    }

    private void handleTileAttack(Player player, NPC npc) {
        if (shouldTileAttack()) {
            setupTileAttack(player);
            npc.forceChat("Your time has come to an End. Prepare to DIE!!");
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
        new Projectile(npc, player, 1002, 45, 3, 85, 45, 0).sendProjectile();
        TaskManager.submit(new Task(3, false) {
            @Override
            protected void execute() {
                if (player.getPosition().equals(chosenTile)) {
                    player.performGraphic(new Graphic(1002, GraphicHeight.LOW));
                    player.getMovementQueue().freeze(3);
                    player.setPlayerLocked(true);
                    player.sendMessage("Been stunned");
                    TaskManager.submit(new Task(3, false) {
                        @Override
                        public void execute() {
                            player.setPlayerLocked(false);
                            stop();
                        }
                    });
                    player.dealDamage(new Hit(990, Hitmask.RED, CombatIcon.MELEE));
                }
                stop();
            }
        });
    }

    private boolean shouldTileAttack() {
        return chosenTiles.isEmpty() && random.nextInt(100) > 85; // 5% chance
    }

    private void setupTileAttack(Player player) {
        List<Player> players = pickNRandom(player.getLocalPlayers(), 4);
        if (!player.getLocalPlayers().contains(player)) {
            players.add(player);
        }
        chosenTiles = players.stream().map(Player::getPosition).collect(Collectors.toList());
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
        return CombatType.MELEE;
    }
}
