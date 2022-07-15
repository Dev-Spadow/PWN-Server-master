package com.arlania.world.content.customislands;

import com.arlania.model.Locations;
import com.arlania.model.Position;
import com.arlania.world.World;
import com.arlania.world.content.transportation.TeleportHandler;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

public class IslandHandler {

    public static void respawnNPCs(Player player) {

        if (!player.getLocation().equals(Locations.Location.HUMILIATION)) {
            player.getPA().sendMessage("You're not in the right location for the NPC's to respawn!");
            return;
        }

        int height = player.getIndex() * 4;

        NPC[] NPCS = {new NPC(10010, new Position(3218, 2918, height)),
                new NPC(12802, new Position(3218, 2925, height)),
                new NPC(12801, new Position(3216, 2936, height)),
                new NPC(12113, new Position(3222, 2913, height)),

                new NPC(12805, new Position(3230, 2913, height)),
                new NPC(12805, new Position(3230, 2920, height)),
                new NPC(12805, new Position(3230, 2927, height)),

                new NPC(3154, new Position(3235, 2914, height)),
                new NPC(3154, new Position(3235, 2923, height)),

                new NPC(1440, new Position(3240, 2913, height)),
                new NPC(1440, new Position(3240, 2917, height)),
                new NPC(1440, new Position(3240, 2921, height)),
                new NPC(1440, new Position(3240, 2925, height)),
                new NPC(1440, new Position(3240, 2929, height)),
        };

        for (NPC npc : NPCS) {
            if (!npc.isRegistered()) {
                World.register(npc);
                npc.setSpawnedFor(player);
            }
        }

    }

    public static void spawnHumiliation(Player player) {
        if (!player.getUsername().equalsIgnoreCase("humiliation")) {
            return;
        }
        int height = player.getIndex() * 4;

        Position island = new Position(3242, 2926, height);

        TeleportHandler.teleportPlayer(player, island, player.getSpellbook().getTeleportType());

        respawnNPCs(player);
    }


    public static void destroyHumiliation(Player player) {

        try {
            World.getNpcs().forEach(n -> {
                if (n.getSpawnedFor().getUsername().equalsIgnoreCase(player.getUsername())) {
                    World.deregister(n);
                }
            });
        } catch (Exception ignored) {

        }
    }


}
