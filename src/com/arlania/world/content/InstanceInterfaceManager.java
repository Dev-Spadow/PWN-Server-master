package com.arlania.world.content;

import java.util.Arrays;

import java.util.List;

import com.arlania.GameSettings;
import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.Position;
import com.arlania.model.definitions.NPCDrops;
import com.arlania.model.definitions.NpcDefinition;
import com.arlania.world.World;
import com.arlania.world.content.combat.CombatFactory;
import com.arlania.world.content.skill.impl.summoning.BossPets;
import com.arlania.world.content.skill.impl.summoning.BossPets.BossPet;
import com.arlania.world.content.transportation.TeleportHandler;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

/**
 * @author Suic
 */

public class InstanceInterfaceManager {

    public static final int MAX_INSTANCE_AMOUNT = 150;

    private Player player;

    public InstanceInterfaceManager(Player player) {
        this.player = player;
    }

    private List<Integer> npcs = Arrays.asList(50, 7286, 2001, 2006, 2000);

    //

    public void open() {

        int startId = 58720;

        for (int i = 0; i < npcs.size(); i++) {
            player.getPacketSender().sendString(startId, NpcDefinition.forId(npcs.get(i)).getName());
            startId++;
        }

        player.getPacketSender().sendInterface(58705);
    }

    public boolean handleClick(int id) {

        if (!(id >= -6816 && id <= -6807)) {
            return false;
        }

        int index = 6816 + id;
        final int npcId = npcs.get(index);
        NpcDefinition def = NpcDefinition.forId(npcs.get(index));

        player.getPacketSender().sendString(58716, "Npc killcount: @gre@" + player.getNpcKillCount(npcId))
                .sendString(58717, "Npc hitpoints: @gre@" + def.getHitpoints())
                .sendString(58718, "Npc level: @gre@" + def.getCombatLevel()).sendString(58926, def.getName());
        player.getPacketSender().sendNpcOnInterface(58927, npcId, 0);

        sendDrops(npcId);

        this.npcId = npcId;

        return true;

    }

    private void sendDrops(int npcId) {
        player.getPacketSender().resetItemsOnInterface(58936, 100);
        try {
            NPCDrops drops = NPCDrops.forId(npcId);
            if (drops == null) {
                //system.out.println("Was null");
                return;
            }
            for (int i = 0; i < drops.getDropList().length; i++) {

                player.getPacketSender().sendItemOnInterface(58936, drops.getDropList()[i].getId(), i,
                        drops.getDropList()[i].getItem().getAmount());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int npcId;

    private NPC npcToSpawn;

    public NPC getNpcToSpawn() {
        return npcToSpawn;
    }

    private int spawnAmount = 0;

    public int getSpawnAmount() {
        return spawnAmount;
    }

    public void setSpawnAmount(int amount) {
        this.spawnAmount = amount;
    }

    public void startInstance() {

        if (spawnAmount < 1) {
            player.getPacketSender().sendMessage("@red@You need to atleast set the spawn amount to 1 to start!");
            //system.out.println("Was: " + spawnAmount);
            return;
        }

        if (npcToSpawn != null) {
            World.deregister(npcToSpawn);
        }

        int height = player.getIndex() * 4;

        player.getPacketSender().sendInterfaceReset().sendInterfaceRemoval();

        // We will do this to ensure that when a player teleports to an instance
        // there will be no NPCs from previous instances in it. Since this can happen if you gave
        //The same index as a previous player that logged out
        for (NPC npc : World.getNpcs()) {
            if (npc != null) {
                //Ignore any pets in the area cause we don't wanna affect them.
                BossPet pet = BossPet.forSpawnId(npc.getId());
                if (pet != null) {
                    continue;
                }
                Position playerPos = player.getPosition();
                Position npcPos = npc.getPosition();
                if (npcPos.getRegionId() == playerPos.getRegionId() && npcPos.getZ() == height) {
                    World.deregister(npc);
                }
            }
        }

        // Setting the npc to spawn in the proper location
        npcToSpawn = new NPC(this.npcId, new Position(2529, 3669, height));

        player.setInInstance(true);
        player.moveTo(new Position(2524, 3671, height));
        spawn();
    }

    public void spawn() {
        World.register(npcToSpawn);
        if (player.activeAgro && CombatFactory.checkHook(npcToSpawn, player)) {
            player.setTargeted(true);
            npcToSpawn.getCombatBuilder().attack(player);
            npcToSpawn.setFindNewTarget(false);
        }
    }

    public void handleKill() {
        // On Each Kill, We will de-register any npc located in this players instance
        // Regardless if the player is there or not
        for (NPC npc : World.getNpcs()) {
            if (npc != null) {
                //Ignore any pets in the area cause we don't wanna affect them.
                BossPet pet = BossPet.forSpawnId(npc.getId());
                if (pet != null) {
                    continue;
                }

                int height = player.getIndex() * 4;
                Position playerPos = player.getPosition();
                Position npcPos = npc.getPosition();
                if (npcPos.getRegionId() == playerPos.getRegionId() && npcPos.getZ() == height) {
                    World.deregister(npc);
                }
            }
        }

        spawnAmount--;
        System.out.println("Spawn amount: " + spawnAmount);
        if (spawnAmount > 0) {
            TaskManager.submit(new Task(5) {
                @Override
                protected void execute() {
                    spawn();
                    stop();
                }
            });
        } else {
            onFinish();
        }
    }

    public void onFinish() {
        // On Each Kill, We will de-register any npc located in this players instance
        // Regardless if the player is there or not
        for (NPC npc : World.getNpcs()) {
            if (npc != null) {
                //Ignore any pets in the area cause we don't wanna affect them.
                BossPet pet = BossPet.forSpawnId(npc.getId());
                if (pet != null) {
                    continue;
                }

                int height = player.getIndex() * 4;
                Position playerPos = player.getPosition();
                Position npcPos = npc.getPosition();
                if (npcPos.getRegionId() == playerPos.getRegionId() && npcPos.getZ() == height) {
                    World.deregister(npc);
                }
            }
        }
        npcToSpawn = null;
        player.setInInstance(false);
        spawnAmount = 0;
        player.getPacketSender().sendMessage("Your instance has finished, you will now be teleported out!");
        TeleportHandler.teleportPlayer(player, GameSettings.DEFAULT_POSITION.copy(),
                player.getSpellbook().getTeleportType());
    }

}
