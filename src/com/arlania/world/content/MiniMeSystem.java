package com.arlania.world.content;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.Flag;
import com.arlania.model.Item;
import com.arlania.model.Position;
import com.arlania.model.container.impl.Equipment;
import com.arlania.model.definitions.WeaponAnimations;
import com.arlania.model.definitions.WeaponInterfaces;
import com.arlania.net.PlayerSession;
import com.arlania.util.NameUtils;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

import java.util.Arrays;
import java.util.Objects;

public class MiniMeSystem {

    private final Player player;

    private Player miniMe;

    public MiniMeSystem(Player player) {
        this.player = player;
    }

    public void spawn() {
        if (miniMe == null) {
            createMinime();
        } else {
        	//system.out.println(miniMe + " already exists");
        }

        boolean added = World.getPlayers().add(miniMe);
        //system.out.println("Added mini me: " + added);
    }

    public Player getMiniMe() {
        return miniMe;
    }

    public void despawn() {
        if (miniMe == null) {
            return;
        }
        //System.out.println(Arrays.toString(player.getMinimeEquipment()));
        unequipAll();
        World.getPlayers().remove(miniMe);
        miniMe = null;
    }

    private void createMinime() {
        if (miniMe != null) {
            return;
        }
        PlayerSession minimeSession = new PlayerSession(null);
        miniMe = new Player(minimeSession);
        miniMe.setMiniPlayer(true);
        miniMe.setUsername("mini " + player.getUsername());
        miniMe.setLongUsername(NameUtils.stringToLong("mini " + player.getUsername()));
        miniMe.setHostAddress(player.getHostAddress());
        miniMe.setSerialNumber(player.getSerialNumber());
        miniMe.setPosition(new Position(player.getPosition().getX() - 1, player.getPosition()
                .getY()));
        miniMe.setAnimation(player.getAnimation());
        miniMe.setCharacterAnimations(player.getCharacterAnimations().clone());
        miniMe.getMovementQueue().setFollowCharacter(player);
        miniMe.getSkillManager().setSkills(player.getSkillManager().getSkills());
        miniMe.setOwner(player);
        miniMe.getUpdateFlag().flag(Flag.APPEARANCE);
        player.getUpdateFlag().flag(Flag.APPEARANCE);
        equipAll();
        TaskManager.submit(new Task(1, player.getUsername() + "minime", true) {
            @Override
            protected void execute() {
            	if(miniMe == null || player == null) {
            		stop();
            		return;
            	}
                if (player.getCombatBuilder() != null && player.getCombatBuilder()
                        .getVictim() != null) {
                    miniMe.getCombatBuilder().attack(player.getCombatBuilder().getVictim());
                } else {
                    miniMe.getMovementQueue().setFollowCharacter(player);
                }
            }
        });
    }

    public void unequipAll() {
        if (miniMe == null) {
            return;
        }
        for (int i = 0; i < player.getMinimeEquipment().length; i++) {
            if (player.getMinimeEquipment()[i] != null && player.getMinimeEquipment()[i].getId() >= 0) {
                player.getInventory().add(player.getMinimeEquipment()[i].copy());
            }
            player.setMinimeEquipment(new Item(-1), i);
        }
        miniMe.getEquipment().resetItems();
        miniMe.getUpdateFlag().flag(Flag.APPEARANCE);
    }

    private void equipAll() {
        for (Item item : player.getMinimeEquipment()) {
            if (item != null && item.getId() != -1) {
                equip(item, false);
            }
        }
    }

    public void equip(Item item, boolean check) {
        if (miniMe == null) {
            return;
        }
        Item copy = item.copy();
        player.getInventory().delete(item);
        int slot = copy.getDefinition().getEquipmentType().getSlot();
        if (check && player.getMinimeEquipment()[slot] != null && player.getMinimeEquipment()[slot].getId() >= 0) {
            player.getInventory().add(player.getMinimeEquipment()[slot]);
        }
        miniMe.getEquipment().setItem(slot, copy);
        player.getMinimeEquipment()[slot] = copy;
        miniMe.getUpdateFlag().flag(Flag.APPEARANCE);

        Item weapon = miniMe.getEquipment().get(Equipment.WEAPON_SLOT);
        System.out.println(weapon);
        if (weapon != null && weapon.getId() >= 0) {
            WeaponInterfaces.assign(miniMe, weapon);
            WeaponAnimations.assign(miniMe, weapon);
        }

        miniMe.getUpdateFlag().flag(Flag.APPEARANCE);
    }

    public void targetPlayer() {
        if (miniMe == null) {
            return;
        }
        resetAttack();
        Position newPos = new Position(player.getPosition().getX() - 1, player.getPosition()
                .getY(), player.getPosition().getZ());
        miniMe.moveTo(newPos);
        miniMe.getMovementQueue().setFollowCharacter(player);
    }

    public void onLogout() {
        if (miniMe != null) {
            boolean removed = World.getPlayers().remove(miniMe);
            System.out.println("Removed " + miniMe.getUsername() + " = " + removed);
            miniMe = null;
        }
    }

    public void onLogin() {
        boolean hasItems = Arrays.stream(player.getMinimeEquipment())
                .filter(Objects::nonNull)
                .anyMatch(item -> item.getId() >= 0);
        System.out.println("has items: " + hasItems);
        if (hasItems) {
            spawn();
        }
    }

    private void resetAttack() {
        if (miniMe == null) {
            return;
        }
        miniMe.getCombatBuilder().reset(true);
    }
}